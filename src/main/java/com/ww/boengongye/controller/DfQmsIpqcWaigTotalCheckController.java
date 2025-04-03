package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfApprovalTimeMapper;
import com.ww.boengongye.mapper.DfAuditDetailMapper;
import com.ww.boengongye.mapper.DfFlowDataMapper;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ww.boengongye.utils.ExcelExportUtil2.distinctByKey;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */

@Controller
@RequestMapping("/dfQmsIpqcWaigTotalCheck")
@Api(tags = "外观数据中央")
@ResponseBody
@CrossOrigin
public class DfQmsIpqcWaigTotalCheckController {

    private static final Logger logger = LoggerFactory.getLogger(DfQmsIpqcWaigTotalCheckController.class);
    @Autowired
    @Qualifier("jtJmsTemplate")
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Value("${config.factoryId}")
    private String fac;

    @Autowired
    @Qualifier(value = "topic2")
    private Destination topic;

    @Autowired
    DfProjectService dfProjectService;
    @Autowired
    DailySummaryService dailySummaryService;

    @Autowired
    DefectSummaryService defectSummaryService;
    @Autowired
    com.ww.boengongye.service.DfQmsIpqcWaigTotalCheckService DfQmsIpqcWaigTotalCheckService;
    @Autowired
    com.ww.boengongye.service.DfQmsIpqcWaigDetailCheckService DfQmsIpqcWaigDetailCheckService;

    @Autowired
    com.ww.boengongye.service.DfAuditDetailService DfAuditDetailService;
    @Autowired
    com.ww.boengongye.service.DfQmsTimeOverCheckLogService dfQmsTimeOverCheckLogService;
    @Autowired
    com.ww.boengongye.service.DfFlowDataService DfFlowDataService;

    @Autowired
    DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataOvertimeService DfFlowDataOvertimeService;

    @Autowired
    com.ww.boengongye.service.DfFlowOpinionService DfFlowOpinionService;
    @Autowired
    com.ww.boengongye.service.DfMacStatusAppearanceService DfMacStatusAppearanceService;
    @Autowired
    private com.ww.boengongye.service.DfProcessService DfProcessService;

    @Autowired
    com.ww.boengongye.service.DfApprovalTimeService dfApprovalTimeService;

    @Autowired
    com.ww.boengongye.service.UserService userService;

    @Autowired
    com.ww.boengongye.service.DfQmsIpqcFlawConfigService dfQmsIpqcFlawConfigService;
    @Autowired
    com.ww.boengongye.service.DfFlowDataUserService DfFlowDataUserService;

    @Autowired
    DfLiableManService dfLiableManService;

    @Autowired
    private DfLiableManMapper dfLiableManMapper;
    @Autowired
    private DfFlowDataMapper dfFlowDataMapper;
    @Autowired
    private DfApprovalTimeMapper dfApprovalTimeMapper;
    @Autowired
    private DfAuditDetailMapper dfAuditDetailMapper;

    @Autowired
    private DfQmsRfidClampSnService dfQmsRfidClampSnService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private Environment env;
    static Environment env2 = ApplicationContextProvider.getBean(Environment.class);
    BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<Runnable>(Integer.parseInt(env2.getProperty("sizeQueue")));

    ExecutorService appreancePool = new ThreadPoolExecutor(Integer.parseInt(env2.getProperty("sizePool")),
            Integer.parseInt(env2.getProperty("sizeMaxPool")), 0L, TimeUnit.MILLISECONDS, workingQueue);

    @GetMapping(value = "/listByMachineAndTime")
    public Object listByMachineAndTime(String machineCode, String time) throws ParseException {
        if (machineCode.indexOf("-") != -1) {
            String[] code = machineCode.split("-");
            QueryWrapper<DfProcess> qw = new QueryWrapper<>();
            qw.eq("process_name", code[0]);
            qw.last("limit 0,1");
            DfProcess process = DfProcessService.getOne(qw);
            machineCode = process.getFirstCode() + code[1];
        }
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq("f_mac", machineCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        qw.apply("UNIX_TIMESTAMP(f_time) >= UNIX_TIMESTAMP('" + TimeUtil.beforeOneHourToNowDate(sdf.parse(time)) + "')");
        qw.apply("UNIX_TIMESTAMP(f_time) <= UNIX_TIMESTAMP('" + TimeUtil.afterOneHourToNowDate(sdf.parse(time)) + "')");
        qw.orderByDesc("f_time");
        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.list(qw));
    }

    @RequestMapping(value = "/getManCount")
    public Object getManCount(String name) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<>();

        ew.eq("f_test_man", name);
        ew.ge("f_time", TimeUtil.getNowTimeNoHour() + " 00:00:00");
        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.count(ew));
    }

    //获取当天上传数量
    @RequestMapping(value = "/getDayCount")
    public Object getManCount(String factory, String project, String process) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<>();

        ew.eq("f_fac", factory);
        ew.eq("f_bigpro", project);
        ew.eq("f_seq", process);
        if (TimeUtil.getDayShift() == 1) {

        }
        ew.ge("f_time", TimeUtil.getNowTimeNoHour() + " 00:00:00");

        ew.select("IFNULL(sum(spot_check_count),0) as totalPcs");
        Map<String, Object> map = DfQmsIpqcWaigTotalCheckService.getMap(ew);
        BigDecimal sumCount = (BigDecimal) map.get("totalPcs");
        return new Result(0, "查询成功", sumCount);
    }

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.list());
    }

    @ApiOperation("Mapping图")
    @RequestMapping(value = "/listPcsWgBim")
    public Object listPcsWgBim(String project, String startDate, String endDate, String dayOrNight, String floor, String process, String fsort, String machineCode) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }

        WaigPcsDataCheck allData = new WaigPcsDataCheck();

        QueryWrapper<DfQmsIpqcWaigDetailCheck> ew = new QueryWrapper<>();

        ew
                .eq(StringUtils.isNotEmpty(project), "p.f_bigpro", project)
                .like("dp.floor", floor)
                .eq("d.f_result", "fail");
        if (null != fsort && !fsort.equals("") && !fsort.equals("undefined")) {
            ew.eq("d.f_sort", fsort);
        }

//        String sqlstr="select id as f_parent_id from df_qms_ipqc_waig_total_check where status='NG'  and f_time between '"+startTime+"' and '"+endTime+"' and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between " + startHour + " and " + endHour;
        ew.eq("p.status", "NG");
        ew.between("p.f_time", startTime, endTime);
        ew.between("HOUR(DATE_SUB(p.f_time, INTERVAL 7 HOUR))", startHour, endHour);
        if (null != process && !process.equals("")) {
//            sqlstr+=" and  f_seq='"+process+"' ";
            ew.eq("p.f_seq", process);
        }

        if (null != machineCode && !machineCode.equals("") && !machineCode.equals("null")) {
//            sqlstr+=" and  f_mac='"+machineCode+"' ";
            ew.eq("p.f_mac", machineCode);
        }


//        ew.inSql("d.f_parent_id",sqlstr);
        ew.ne("d.f_sort", "破片");  // 去掉破片
//        ew.last("limit 2000");

        List<DfQmsIpqcWaigDetailCheck> l = DfQmsIpqcWaigDetailCheckService.listByJoin(ew);


        allData.setDetail(l);
        return new Result(0, "查询成功", allData);
    }

    @RequestMapping(value = "/listPcs")
    public Object listPcs(String machineCode, int count, String type, String time, String position) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();

        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw2 = new QueryWrapper<>();
        qw2.select("IFNULL(sum(spot_check_count),0) as totalPcs");
//        qw2.eq("f_mac", machineCode);
//        qw2.last("limit " + count);
//        qw2.orderByDesc("f_time");

        qw.eq("f_mac", machineCode);
        qw.eq("status", "NG");

        if (null != time && !time.equals("")) {
            qw.ge("f_time", time);
//            qw2.like("f_time", time);

            qw2.inSql("id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_time >= '" + time + "' and f_mac='" + machineCode + "'  order by f_time desc limit " + count + ") tt");
            qw.inSql("id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_time >= '" + time + "' and f_mac='" + machineCode + "'  order by f_time desc limit " + count + ") tt");
        } else {
            qw2.inSql("id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_mac ='" + machineCode + "'  order by f_time desc limit " + count + ") tt");
            qw.inSql("id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_mac ='" + machineCode + "'  order by f_time desc limit " + count + ") tt");

        }


//        if (null != type && !type.equals("")) {
//            qw.like("f_sort", type);
//        }

        if (null != type && !type.equals("") && null != position && !position.equals("")) {
            qw.inSql("id", "select f_parent_id as id from df_qms_ipqc_waig_detail_check where f_sort like '%" + type + "%'   and f_sm_area='" + position + "'");
        } else if (null != type && !type.equals("")) {
            qw.inSql("id", "select f_parent_id as id from df_qms_ipqc_waig_detail_check where f_sort like '%" + type + "%'  ");
        } else if (null != position && !position.equals("")) {
            qw.inSql("id", "select f_parent_id as id from df_qms_ipqc_waig_detail_check where  f_sm_area='" + position + "'");
        }
        qw.orderByDesc("f_time");
        qw.last("limit " + count);
        WaigPcsDataCheck allData = new WaigPcsDataCheck();
        List<DfQmsIpqcWaigDetailCheck> dl = new ArrayList<>();
        List<DfQmsIpqcWaigTotalCheck> datas = DfQmsIpqcWaigTotalCheckService.list(qw);

        Map<String, Object> map = DfQmsIpqcWaigTotalCheckService.getMap(qw2);
        BigDecimal sumCount = (BigDecimal) map.get("totalPcs");
        int ngCount = 0;
        if (datas.size() > 0) {
            for (DfQmsIpqcWaigTotalCheck d : datas) {
                if (null != d.getAffectCount()) {
                    ngCount += d.getAffectCount();
                }

                QueryWrapper<DfQmsIpqcWaigDetailCheck> ew = new QueryWrapper<>();
                ew.eq("d.f_parent_id", d.getId());
                ew.eq("d.f_result", "fail");

                List<DfQmsIpqcWaigDetailCheck> l = DfQmsIpqcWaigDetailCheckService.listByJoin(ew);
                if (l.size() > 0) {
                    StringBuilder str = new StringBuilder();
                    StringBuilder pos = new StringBuilder();
                    int i = 0;
                    for (DfQmsIpqcWaigDetailCheck ld : l) {
                        dl.add(ld);
                        if (i > 0) {
                            str.append(",");
                            pos.append(",");
                        }
                        str.append(ld.getfSort());
                        pos.append(ld.getfSmArea());
                        i++;
                    }
                    d.setBadStatus(str.toString());
                    d.setBadPosition(pos.toString());
                }
            }
        }
        allData.setTotal(datas);
        allData.setDetail(dl);
        allData.setPercent((ngCount / sumCount.doubleValue()) * 100);
        return new Result(0, "查询成功", allData);
    }

    @GetMapping(value = "/listPcs2")
    public Object listPcs2(String project, String machineCode, int count, String type, String time, String position) {
        QueryWrapper<DfQmsIpqcWaigDetailCheck> qw = new QueryWrapper<>();
        if (null != time && !time.equals("")) {
            qw.inSql("d.f_parent_id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_time >= '" + time + "' and f_mac='" + machineCode + "'  order by f_time desc limit " + count + ") tt");
        } else {
            qw.inSql("d.f_parent_id", "select tt.id from (select   id from df_qms_ipqc_waig_total_check where  f_mac ='" + machineCode + "'  order by f_time desc limit " + count + ") tt");

        }
        if (null != type && !type.equals("")) {
            qw.eq("d.f_sort", type);

        }

        if (null != position && !position.equals("")) {
            qw.eq("d.f_sm_area", position);

        }
        qw
                .eq(StringUtils.isNotEmpty(project), "p.f_bigpro", project)
                .eq("d.is_uniwafe", 1)
                .orderByDesc("d.f_time")
                .last("limit " + count);
        List<DfQmsIpqcWaigDetailCheck> detailList = DfQmsIpqcWaigDetailCheckService.listByJoin(qw);
        WaigPcsDataCheck allData = new WaigPcsDataCheck();
        List<DfQmsIpqcWaigDetailCheck> dl = new ArrayList<>();
        double ngCount = 0.0;
        for (DfQmsIpqcWaigDetailCheck d : detailList) {
            if (d.getfResult().equals("fail")) {
                dl.add(d);
                ngCount++;
            }
        }

        allData.setDetail(dl);
        allData.setPercent((ngCount / detailList.size()) * 100);
        return new Result(0, "查询成功", allData);
    }


    @RequestMapping(value = "/getById")
    public Object getById(int id) {

        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.getById(id));
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfQmsIpqcWaigTotalCheck datas) throws ParseException {

        String resultMes = "保存成功";
        if (null != datas.getId()) {
            if (DfQmsIpqcWaigTotalCheckService.updateById(datas)) {
                QueryWrapper<DfQmsIpqcWaigDetailCheck> qw = new QueryWrapper<>();
                qw.eq("f_parent_id", datas.getId());
                DfQmsIpqcWaigDetailCheckService.remove(qw);

                List<DfQmsIpqcWaigDetailCheck> sl = new ArrayList<>();
                for (DfQmsIpqcWaigDetailCheck d : datas.getDetailList()) {
                    d.setFParentId(datas.getId());
                    sl.add(d);
                }
                if (sl.size() > 0) {
                    DfQmsIpqcWaigDetailCheckService.saveBatch(sl);
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {


            if (DfQmsIpqcWaigTotalCheckService.save(datas)) {

                if (null != datas.getfMac()) {
                    QueryWrapper<DfMacStatusAppearance> qw = new QueryWrapper<>();
                    qw.eq("MachineCode", datas.getfMac());
                    qw.last("limit 0,1");
                    DfMacStatusAppearance da = DfMacStatusAppearanceService.getOne(qw);
                    int status = 2;
                    if (datas.getStatus().equals("NG")) {
                        status = 3;
                    }


                    if (null != da && null != da.getStatusidCur()) {
                        int auditDetailCount = 0;
                        if (status == 2) {
                            QueryWrapper<DfAuditDetail> adQw = new QueryWrapper<>();
                            adQw.eq("t.status", "NG");
                            adQw.eq("t.f_mac", datas.getfMac());
                            adQw.isNull("d.end_time");
                            adQw.eq("d.data_type", "外观");
                            auditDetailCount = DfAuditDetailService.getNgCountByMacCode(adQw);
                        }
//
//
                        if ((status == 2 && auditDetailCount == 0) || status == 3) {
                            da.setStatusidCur(status);
                            da.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                            DfMacStatusAppearanceService.updateById(da);
                        }
//
                    }
                }
                List<DfQmsIpqcWaigDetailCheck> sl = new ArrayList<>();


                for (DfQmsIpqcWaigDetailCheck d : datas.getDetailList()) {
                    d.setFParentId(datas.getId());
                    sl.add(d);
                }


                if (sl.size() > 0) {
                    DfQmsIpqcWaigDetailCheckService.saveBatch(sl);
                }


                return new Result(200, resultMes, datas.getId());
            } else {


                return new Result(500, "保存失败");
            }
        }


    }

    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String name, String factoryId, String lineBodyId, String
            type, String machineCode, String process, String startTime, String endTime, String testMan) {
//        try {
        Page<DfQmsIpqcWaigTotalCheck> pages = new Page<DfQmsIpqcWaigTotalCheck>(page, limit);
        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<DfQmsIpqcWaigTotalCheck>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }
        if (null != type && !type.equals("")) {
            ew.eq("type", type);
        }
        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.eq("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }

        ew.orderByDesc("f_time");
        IPage<DfQmsIpqcWaigTotalCheck> list = DfQmsIpqcWaigTotalCheckService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @GetMapping(value = "/getTotalAndNgCount")
    public Result getTotalAndNgCount(String name, String factoryId, String lineBodyId, String type, String
            machineCode, String process, String startTime, String endTime, String testMan) {
//        try {

        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<DfQmsIpqcWaigTotalCheck>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }
        if (null != type && !type.equals("")) {
            ew.eq("type", type);
        }
        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.eq("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }
        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.getTotalAndNgCount(ew));

    }


    // 获取测试员名称
    @GetMapping(value = "/listTestMan")
    public Result listTestMan(String id) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<DfQmsIpqcWaigTotalCheck>();
        ew.isNotNull("f_test_man");
        ew.ne("f_test_man", "null");
        ew.select("distinct(f_test_man)");
        return new Result(0, "查询成功", DfQmsIpqcWaigTotalCheckService.list(ew));
    }

    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {

        if (DfQmsIpqcWaigTotalCheckService.removeById(id)) {
            QueryWrapper<DfQmsIpqcWaigDetailCheck> qw = new QueryWrapper<>();
            qw.eq("f_parent_id", id);
            DfQmsIpqcWaigDetailCheckService.remove(qw);
            QueryWrapper<DfAuditDetail> qw2 = new QueryWrapper<>();
            qw2.eq("parent_id", id);
            qw2.last("limit 1");
            DfAuditDetail aud = DfAuditDetailService.getOne(qw2);
            if (null != aud) {
                QueryWrapper<DfFlowData> qw3 = new QueryWrapper<>();
                qw3.eq("data_id", aud.getId());
                qw3.last("limit 1");
                DfFlowData fd = DfFlowDataService.getOne(qw3);
                if (null != fd) {
                    QueryWrapper<DfFlowDataOvertime> qw4 = new QueryWrapper<>();
                    qw4.eq("flow_data_id", fd.getId());
                    DfFlowDataOvertimeService.remove(qw4);
                    QueryWrapper<DfFlowOpinion> qw5 = new QueryWrapper<>();
                    qw5.eq("flow_data_id", fd.getId());
                    DfFlowOpinionService.remove(qw5);
                }
                DfFlowDataService.remove(qw3);
            }
            DfAuditDetailService.remove(qw2);
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @ApiOperation("底部良率")
    @GetMapping("/listNgItemRate")
    public Result listNgItemRate(String lineBody, String project, String floor, String process, String startDate, String endDate) throws
            ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listNgItemRateSortByNgTop3(lineBody, project, floor, process, startTime, endTime);
        Map<String, Map<String, List<Object>>> itemResData = new LinkedHashMap<>();
        List<Object> dateList = new ArrayList<>();
        for (Rate rate : rates) {
            if (null == rate) continue;
            String itemName = rate.getName();
            String date = rate.getDate();
            if (!dateList.contains(date)) {
                dateList.add(date);
            }
            /*if (!itemResData.containsKey(itemName)) {   // 全天
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                List<Object> ngNumList = new ArrayList<>();
                ngNumList.add(rate.getNgNum());
                List<Object> allNumList = new ArrayList<>();
                allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                itemValue.put("rateList", rateList);
                itemValue.put("ngNumList", ngNumList);
                itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                itemValue.get("rateList").add(rate.getRate());
                itemValue.get("ngNumList").add(rate.getNgNum());
                itemValue.get("allNumList").add(rate.getAllNum());
            }*/

            if (!itemResData.containsKey(itemName)) {   // 白夜班
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                //List<Object> ngNumList = new ArrayList<>();
                //ngNumList.add(rate.getNgNum());
                //List<Object> allNumList = new ArrayList<>();
                //allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.put("dayRateList", rateList);
                    itemValue.put("nightRateList", new ArrayList<>());
                } else {  // 晚班
                    itemValue.put("dayRateList", new ArrayList<>());
                    itemValue.put("nightRateList", rateList);
                }

                //itemValue.put("ngNumList", ngNumList);
                //itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.get("dayRateList").add(rate.getRate());
                } else {  // 晚班
                    itemValue.get("nightRateList").add(rate.getRate());
                }
                //itemValue.get("ngNumList").add(rate.getNgNum());
                //itemValue.get("allNumList").add(rate.getAllNum());
            }
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResData.entrySet()) {
            entry.getValue().put("date", dateList);
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);

    }

    @ApiOperation("外观良率趋势")
    @GetMapping("/listOkRateNear15Day")
    public Result listOkRateNear15Day(String lineBody, String project, String process, String startDate, String
            endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != process && !"".equals(process), "tol.f_seq", process)
                .eq(null != lineBody && !"".equals(lineBody), "tol.f_line", lineBody)
                .eq(null != project && !"".equals(project), "tol.f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startTime, endTime)
                .ne("WEEKDAY(DATE_SUB(TOL.F_TIME, INTERVAL 7 HOUR))", 6)   // 去掉周日的数据
                .isNotNull("spot_check_count");
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listOkRateNear15Day(qw);
        List<Object> dateList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        for (Rate rate : rates) {
//            if(rate.getAllNum()>0){
                rateList.add(rate.getRate());
//            }else{
//                rateList.add("");
//            }
            dateList.add(rate.getDate());

        }
        Map<String, List<Object>> result = new HashMap<>();
        result.put("date", dateList);
        result.put("rate", rateList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("缺陷占比TOP")
    @GetMapping("/listNgTop")
    public Result listNgTop(String lineBody, String project, String floor, String process, String startDate, String
            endDate, String dayOrNight) throws ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listNgTop103(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        List<Object> colorList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());
            rateList.add(rate.getRate());
            colorList.add(rate.getDate());
        }
        result.put("name", nameList);
        result.put("rate", rateList);
        result.put("color", colorList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("各工序直通趋势对比-4F")
    @GetMapping("/listAlwaysOkRate")
    public Result listAlwaysOkRate(String project, String startDate, String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(project), "tol.f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startTime, endTime);
        //.ne("WEEKDAY(tol.f_time)", 6);   // 去掉周日的数据
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listAlwaysOkRate(qw);
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());
            rateList.add(rate.getRate());
        }
        result.put("name", nameList);
        result.put("rate", rateList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("各工序外观不良项不良率")
    @GetMapping("/listAllProcessItemNgRate")
    public Result listAllProcessItemNgRate(String factory, String project, String lineBody, String
            startDate, String endDate) throws ParseException {
        if (null != endDate && !"".equals(endDate)) {
            endDate = TimeUtil.getNextDay(endDate);
        }
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "f_fac", factory)
                .eq(null != lineBody && !"".equals(lineBody), "f_line", lineBody)
                .eq(null != project && !"".equals(project), "f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startDate + " 00:00:00", endDate + " 00:00:00");
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listAllProcessItemNgRate(qw);

        Map<String, Integer> processResIndex = new HashMap<>();
        List<String> processList = new ArrayList<>();
        Map<String, Integer> ngItemResIndex = new HashMap<>();
        List<String> ngItemList = new ArrayList<>();
        int processIndex = 0, ngIndex = 0;
        for (Rate rate : rates) {
            String process = rate.getName();
            String ngItem = rate.getDate();
            if (!processResIndex.containsKey(process) && null != process) {
                processResIndex.put(process, processIndex++);
                processList.add(process);
            }
            if (!ngItemResIndex.containsKey(ngItem) && null != ngItem) {
                ngItemResIndex.put(ngItem, ngIndex++);
                ngItemList.add(ngItem);
            }
        }
        double[][] processResNgItemNgRate = new double[ngIndex][processIndex];
        for (Rate rate : rates) {
            if (null != rate.getRate()) {
                processResNgItemNgRate[ngItemResIndex.get(rate.getDate())][processResIndex.get(rate.getName())] = rate.getRate();

            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("process", processList);
        List<Map<String, Object>> itemDataList = new ArrayList<>();
        result.put("itemDataList", itemDataList);
        for (int i = 0; i < ngIndex; i++) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemName", ngItemList.get(i));
            itemData.put("itemNgRate", processResNgItemNgRate[i]);
            itemDataList.add(itemData);
        }

        List<Rate> alwaysOkRate = DfQmsIpqcWaigTotalCheckService.listAlwaysOkRate(qw); // 直通良率
        List<Double> processAlwaysOkRate = new ArrayList<>();
        for (Rate rate : alwaysOkRate) {
            processAlwaysOkRate.add(rate.getRate());
        }
        result.put("processAlwaysOkRate", processAlwaysOkRate);


        return new Result(200, "查询成功", result);
    }

    @ApiOperation("各工序外观不良项个数良率统计")
    @GetMapping("/listProcessAlwaysNumRate")
    public Result listProcessAlwaysNumRate(String factory, String project, String lineBody, String
            startDate, String endDate) throws ParseException {
        if (null != endDate && !"".equals(endDate)) {
            endDate = TimeUtil.getNextDay(endDate);
        }
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "f_fac", factory)
                .eq(null != lineBody && !"".equals(lineBody), "f_line", lineBody)
                .eq(null != project && !"".equals(project), "f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startDate + " 00:00:00", endDate + " 00:00:00");
        List<ProcessResRate> processResRates = DfQmsIpqcWaigTotalCheckService.listProcessAlwaysNumRate(qw);
        int size = processResRates.size();
        String[] process = new String[size];
        Integer[] allNum = new Integer[size];
        Integer[] okNum = new Integer[size];
        Integer[] ngNum = new Integer[size];
        Double[] okRate = new Double[size];
        Double[] alwaysOkRate = new Double[size];
        int i = 0;
        for (ProcessResRate processResRate : processResRates) {
            process[i] = processResRate.getProcess();
            allNum[i] = processResRate.getAllNum();
            okNum[i] = processResRate.getOkNum();
            ngNum[i] = processResRate.getNgNum();
            okRate[i] = processResRate.getOkRate();
            alwaysOkRate[i++] = processResRate.getAlwaysOkRate();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("process", process);
        result.put("allNum", allNum);
        result.put("okNum", okNum);
        result.put("ngNum", ngNum);
        result.put("okRate", okRate);
        result.put("alwaysOkRate", alwaysOkRate);

        return new Result(200, "查询成功", result);

    }

    @ApiOperation("各工序良率")
    @GetMapping("/listAllProcessOkRate")
    public Result listAllProcessOkRate(String factory, String project, String lineBody, String startDate, String
            endDate, String floor) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "tol.f_fac", factory)
                .eq(null != lineBody && !"".equals(lineBody), "tol.f_line", lineBody)
                .eq(null != project && !"".equals(project), "tol.f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startTime, endTime);
        //.ne("WEEKDAY(f_time)", 6);   // 去掉周日的数据
        if(StringUtils.isEmpty(project)){
            project="%%";
        }else {
            project="%"+project+"%";
        }
        List<Rate> processResRates = DfQmsIpqcWaigTotalCheckService.listAllProcessOkRate(qw, floor,project);
        String[] processArr = new String[processResRates.size()];
        Double[] rateArr = new Double[processResRates.size()];
        int i = 0;
        for (Rate processResRate : processResRates) {
            processArr[i] = processResRate.getName();
            rateArr[i++] = processResRate.getRate();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("name", processArr);
        result.put("rate", rateArr);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("获取外观实际良率")
    @GetMapping("/getAppearRealOkRate")
    public Result getAppearRealOkRate(String factory, String project, String lineBody, String process, String
            startDate, String endDate) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "f_fac", factory)
                .eq(null != lineBody && !"".equals(lineBody), "f_line", lineBody)
                .eq(null != project && !"".equals(project), "f_bigpro", project)
                .eq(null != process && !"".equals(process), "f_seq", process)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "f_time", startDate + " 00:00:00", endDate + " 23:59:59");
        Rate appearRealOkRate = DfQmsIpqcWaigTotalCheckService.getAppearRealOkRate(qw);
        return new Result(200, "查询成功", appearRealOkRate);

    }

    @ApiOperation("获取该不良项各工序的不良率--图")
    @GetMapping("/listProcessNgRateByNgItem")
    public Result listProcessNgRateByNgItem(String project, String lineBody, String floor, String ngItem, String
            startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listProcessNgRateByNgItem(lineBody, project, floor, ngItem, startTime, endTime, startHour, endHour);
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        List<Object> ngNumList = new ArrayList<>();
        List<Object> allNumList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());
            rateList.add(rate.getRate());
            ngNumList.add(rate.getNgNum());
            allNumList.add(rate.getAllNum());
        }
        result.put("name", nameList);
        result.put("rate", rateList);
        result.put("ngNum", ngNumList);
        result.put("allNum", allNumList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("获取该不良项各工序的不良率--数据")
    @GetMapping("/listProcessNgRateByNgItemData")
    public Result listProcessNgRateByNgItemData(String project, String lineBody, String floor, String ngItem, String
            startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listProcessNgRateByNgItem(lineBody, project, floor, ngItem, startTime, endTime, startHour, endHour);
        return new Result(200, "查询成功", rates);
    }

    @ApiOperation("获取该不良项各机台的不良率--图")
    @GetMapping("/listMacNgRateByNgItemAndProcess")
    public Result listMacNgRateByNgItemAndProcess(String process, String project, String floor, String lineBody, String
            ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listMacNgRateByNgItemAndProcess(process, lineBody, project, floor, ngItem, startTime, endTime, startHour, endHour);
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        List<Object> ngNumList = new ArrayList<>();
        List<Object> allNumList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());
            rateList.add(rate.getRate());
            ngNumList.add(rate.getNgNum());
            allNumList.add(rate.getAllNum());
        }
        result.put("name", nameList);
        result.put("rate", rateList);
        result.put("ngNum", ngNumList);
        result.put("allNum", allNumList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("获取该不良项各机台的不良率--数据")
    @GetMapping("/listMacNgRateByNgItemAndProcessData")
    public Result listMacNgRateByNgItemAndProcessData(String process, String project, String floor, String lineBody, String
            ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listMacNgRateByNgItemAndProcess(process, lineBody, project, floor, ngItem, startTime, endTime, startHour, endHour);
        for (Rate rate : rates) {
            System.out.println(rate);
        }
        return new Result(200, "查询成功", rates);
    }


    public List<Object> getDatesInRange(String startDate, String endDate) {
        List<Object> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        while (!start.isAfter(end)) {
            String date = start.format(formatter);
            dates.add(date);
            start = start.plusDays(1);
        }
        return dates;
    }


    @ApiOperation("获取工序良率")
    @GetMapping("/getProcessYield")
    public Result getProcessYield(String process, String startTime, String endTime) {
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq("f_seq", process);
        qw.between("f_time", startTime, endTime);
        double rate = 1.0;
        DfQmsIpqcWaigTotalCheck data = DfQmsIpqcWaigTotalCheckService.getProcessYield(qw);
        if (null != data && null != data.getSpotCheckCount() && null != data.getAffectCount()) {
            DecimalFormat ft = new DecimalFormat("#.000");
            rate = Double.valueOf(ft.format((1 - (Double.parseDouble(data.getAffectCount() + "") / Double.parseDouble(data.getSpotCheckCount() + "")))));

        }
        return new Result(200, "查询成功", rate);
    }

    @ApiOperation("获取多工序良率")
    @PostMapping("/listProcessYield")
    public Result listProcessYield(ProcessYield process) {
        List<DoubleData> datas = new ArrayList<>();
        if (process.getProcess().size() > 0) {
            for (String p : process.getProcess()) {
                System.out.println(p);
                QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
                qw
                        .eq(StringUtils.isNotEmpty(process.getProject()), "f_bigpro", process.getProject())
                        .eq("f_seq", p)
                        .between("f_time", process.getStartTime(), process.getEndTime());
                double rate = 100;
                DfQmsIpqcWaigTotalCheck data = DfQmsIpqcWaigTotalCheckService.getProcessYield(qw);
                DoubleData d = new DoubleData();
                d.setName(p);
                if (null != data && null != data.getSpotCheckCount() && null != data.getAffectCount()) {
                    DecimalFormat ft = new DecimalFormat("#.000");
                    rate = Double.valueOf(ft.format((1 - (Double.parseDouble(data.getAffectCount() + "") / Double.parseDouble(data.getSpotCheckCount() + ""))) * 100));

                }
                d.setData(rate);
                datas.add(d);

            }
        }
        return new Result(200, "查询成功", datas);
    }

    @ApiOperation("获取机台的不良率走势图")
    @GetMapping("/listMacNgRate")
    public Result listMacNgRate(String project, @RequestParam String startDate, @RequestParam String endDate, String
            dayOrNight, @RequestParam String machineCode) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(project), "f_bigpro", project)
                .between("f_time", startTime, endTime)
                .between("HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq("f_mac", machineCode);
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listDateNgRate(qw);
        List<Object> dateList = new ArrayList<>();
        List<Object> dayRateList = new ArrayList<>();
        List<Object> nightRateList = new ArrayList<>();
        for (Rate rate : rates) {
            String date = rate.getDate();
            if (!dateList.contains(date)) {
                dateList.add(date);
            }
            if ("A".equals(rate.getDayOrNight())) {
                dayRateList.add(rate.getRate());
            } else {
                nightRateList.add(rate.getRate());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("date", dateList);
        result.put("dayRate", dayRateList);
        result.put("nightRate", nightRateList);
        return new Result(200, "查询成功", result);
    }


    @RequestMapping(value = "/downloadExcel", method = {RequestMethod.GET})
    @ResponseBody
    public void downloadExcel(String type, String name, String factoryId, String lineBodyId, String
            machineCode, String process, String startTime, String endTime, String testMan, HttpServletResponse
                                      response, HttpServletRequest request
    ) throws IOException {

        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }

        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != type && !type.equals("")) {
            ew.like("type", type);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.like("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }

        ew.orderByDesc("f_time");
        List<DfQmsIpqcWaigTotalCheck> datas = DfQmsIpqcWaigTotalCheckService.list(ew);
        List<Map> maps = new ArrayList<>();
        if (datas != null && !datas.isEmpty()) {

            // 提取datas中的所有id
            List<Integer> ids = datas.stream().map(DfQmsIpqcWaigTotalCheck::getId).collect(Collectors.toList());

            List<DfQmsIpqcWaigDetailCheck> dpdats = DfQmsIpqcWaigDetailCheckService.list(
                    Wrappers.<DfQmsIpqcWaigDetailCheck>lambdaQuery().in(DfQmsIpqcWaigDetailCheck::getFParentId, ids)
            );

            // 将查询结果按id做一个映射，将多个name拼接成一个字符串
            Map<Integer, String> idToNameMap = dpdats.stream()
                    .filter(d -> d.getfSort() != null) // 过滤掉 fSort 为 null 的项
                    .collect(Collectors.groupingBy(
                            DfQmsIpqcWaigDetailCheck::getFParentId,
                            Collectors.mapping(DfQmsIpqcWaigDetailCheck::getfSort, Collectors.joining(","))
                    ))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // 过滤掉值为 null 或 空字符串的条目
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


            // 根据datas里的每个对象的id获取name，并设置到fSort字段
            datas.forEach(item -> {
                // 获取与当前item的id对应的name字段（多个用逗号拼接）
                String names = idToNameMap.get(item.getId());
                if (names != null) {
                    item.setfSort(names); // 将拼接的names设置到fSort字段
                }
            });

            // 将更新后的datas转换为Map（假设Excel.objectToMap是您的工具方法）
            for (DfQmsIpqcWaigTotalCheck r : datas) {
                try {
                    maps.add(Excel.objectToMap(r)); // 转换为Map
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

//            if (maps != null && maps.size() > 0) {
        String companyName = "外观数据列表";
        String sheetTitle = companyName;
        String[] title = new String[]{"外观类型", "工厂", "工序", "生产阶段", "项目", "线体", "颜色", "检验类型", "检验员", "测试类别", "机台号", "产品条码", "抽检总数", "NG数","NG名称", "创建时间"};        //设置表格表头字段
        String[] properties = new String[]{"fType", "fFac", "fSeq", "fStage",
                "fBigpro", "fLine", "fColor", "fTestType", "fTestMan", "fTestCategory", "fMac", "fBarcode", "spotCheckCount", "affectCount","fSort","fTime"};  // 查询对应的字段
        ExcelExportUtil excelExport2 = new ExcelExportUtil();
        excelExport2.setData(maps);
        excelExport2.setHeardKey(properties);
        excelExport2.setFontSize(14);
        excelExport2.setSheetName(sheetTitle);
        excelExport2.setTitle(sheetTitle);
        excelExport2.setHeardList(title);
        excelExport2.exportExport(request, response);
//            }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    @GetMapping("/weekOfPoorTOP3Warning")
    @ApiOperation("外观机台连续一周不良TOP3 预警(不包含周日)")
    public void weekOfPoorTOP3Warning() throws ParseException {
        System.out.println("外观机台连续一周不良TOP3 预警 start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<>();
        List<DfQmsIpqcWaigTotalCheck> list = DfQmsIpqcWaigTotalCheckService.weekOfPoorTOP3Warning(ew);
        for (DfQmsIpqcWaigTotalCheck dfQmsIpqcWaigTotalCheck : list) {
            QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
            sqw.eq("type", "外观机台连续一周不良TOP3");
            sqw.eq("problem_level", "1");
            if (TimeUtil.getBimonthly() == 0) {
                sqw.like("bimonthly", "双月");
            } else {
                sqw.like("bimonthly", "单月");
            }
//            sqw.like("process_name","AOI良率");
            List<DfLiableMan> lm = dfLiableManMapper.selectList(sqw);
            if (lm == null || lm.size() == 0) {
                return;
            }
            StringBuilder manCode = new StringBuilder();
            StringBuilder manName = new StringBuilder();
            int manCount = 0;
            for (DfLiableMan l : lm) {
                if (manCount > 0) {
                    manCode.append(",");
                    manName.append(",");
                }
                manCode.append(l.getLiableManCode());
                manName.append(l.getLiableManName());
                manCount++;
            }
            DfAuditDetail aud = new DfAuditDetail();
//            aud.setLine(machine.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
            aud.setDataType("外观");
            aud.setDepartment("外观");
            aud.setAffectMac(dfQmsIpqcWaigTotalCheck.getfMac());
            aud.setAffectNum(1.0);
            aud.setControlStandard("外观机台连续一周不良TOP3");
            aud.setImpactType("外观机台连续一周不良TOP3");
            aud.setIsFaca("0");

            //问题名称和现场实际调换
            aud.setQuestionName("良率报警");
//            aud.setProcess("AOI");
//            aud.setProjectName("");

            aud.setReportTime(Timestamp.valueOf(checkTime));
            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
            aud.setIpqcNumber(checkTimeLong);

            aud.setScenePractical("外观机台连续一周不良在TOP3 " + "机台号:" + dfQmsIpqcWaigTotalCheck.getfMac());
            aud.setQuestionType("良率报警");
            aud.setDecisionLevel("Level1");
            aud.setHandlingSug("隔离,全检");
            aud.setResponsible(manName.toString());
            aud.setResponsibleId(manCode.toString());
            dfAuditDetailMapper.insert(aud);

            DfFlowData fd = new DfFlowData();
            fd.setFlowLevel(1);
            fd.setDataType(aud.getDataType());
            fd.setFlowType(aud.getDataType());
            fd.setName("外观机台连续一周不良TOP3");
            fd.setDataId(aud.getId());
            fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

            fd.setNowLevelUser(aud.getResponsibleId());
            fd.setNowLevelUserName(aud.getResponsible());
            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
            fd.setFlowLevelName(aud.getDecisionLevel());

            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
            atQw.eq("type", "外观")
                    .last("limit 1");
            DfApprovalTime at = dfApprovalTimeMapper.selectOne(atQw);
            if (null != at) {
                if (fd.getFlowLevelName().equals("Level1")) {
                    fd.setReadTimeMax(at.getReadTimeLevel1());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                } else if (fd.getFlowLevelName().equals("Level2")) {
                    fd.setReadTimeMax(at.getReadTimeLevel2());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                } else if (fd.getFlowLevelName().equals("Level3")) {
                    fd.setReadTimeMax(at.getReadTimeLevel3());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                }
            }
            //设置显示人
            fd.setShowApprover(fd.getNowLevelUserName());
            dfFlowDataMapper.insert(fd);
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    @GetMapping("/twoWeekOfPoorTOP3Warning")
    @ApiOperation("外观缺陷连续两周周不良TOP3 预警(不包含周日)")
    public void twoWeekOfPoorTOP3Warning() throws ParseException {
        System.out.println("外观缺陷连续两周周不良TOP3 预警 start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        List<Rate3> list = DfQmsIpqcWaigTotalCheckService.twoWeekOfPoorTOP3Warning();
        if (list == null || list.size() == 0) {
            return;
        }

        for (Rate3 processDefect : list) {
            QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
            sqw.like("process_name", processDefect.getStr1());
            sqw.eq("type", "waig");
            sqw.eq("problem_level", "1");
            if (TimeUtil.getBimonthly() == 0) {
                sqw.like("bimonthly", "双月");
            } else {
                sqw.like("bimonthly", "单月");
            }
            List<DfLiableMan> lm = dfLiableManMapper.selectList(sqw);
            if (lm == null || lm.size() == 0) {
                return;
            }
            StringBuilder manCode = new StringBuilder();
            StringBuilder manName = new StringBuilder();
            int manCount = 0;
            for (DfLiableMan l : lm) {
                if (manCount > 0) {
                    manCode.append(",");
                    manName.append(",");
                }
                manCode.append(l.getLiableManCode());
                manName.append(l.getLiableManName());
                manCount++;
            }
            DfAuditDetail aud = new DfAuditDetail();
//            aud.setLine(machine.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
            aud.setDataType("外观");
            aud.setDepartment("外观");
//            aud.setAffectMac(dfQmsIpqcWaigTotalCheck.getfMac());
//            aud.setAffectNum(1.0);
            aud.setControlStandard("外观缺陷连续两周不良TOP3");
            aud.setImpactType("外观缺陷连续两周不良TOP3");
            aud.setIsFaca("0");

            //问题名称和现场实际调换
            aud.setQuestionName("不良报警");
//            aud.setProcess("AOI");
//            aud.setProjectName("");

            aud.setReportTime(Timestamp.valueOf(checkTime));
            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
            aud.setIpqcNumber(checkTimeLong);

            aud.setScenePractical("外观缺陷连续两周不良在TOP3-" + "工序为" + processDefect.getStr1() + ",缺陷名为" + processDefect.getStr2());
            aud.setQuestionType("不良报警");
            aud.setDecisionLevel("Level1");
            aud.setHandlingSug("隔离,全检");
            aud.setResponsible(manName.toString());
            aud.setResponsibleId(manCode.toString());
            dfAuditDetailMapper.insert(aud);

            DfFlowData fd = new DfFlowData();
            fd.setFlowLevel(1);
            fd.setDataType(aud.getDataType());
            fd.setFlowType(aud.getDataType());
            fd.setName("外观缺陷连续两周不良TOP3" + "_工序" + processDefect.getStr1() + "_缺陷" + processDefect.getStr2() + "_NG_" + checkTime);
            fd.setDataId(aud.getId());
            fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

            fd.setNowLevelUser(aud.getResponsibleId());
            fd.setNowLevelUserName(aud.getResponsible());
            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
            fd.setFlowLevelName(aud.getDecisionLevel());

            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
            atQw.eq("type", "外观")
                    .last("limit 1");
            DfApprovalTime at = dfApprovalTimeMapper.selectOne(atQw);
            if (null != at) {
                if (fd.getFlowLevelName().equals("Level1")) {
                    fd.setReadTimeMax(at.getReadTimeLevel1());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                } else if (fd.getFlowLevelName().equals("Level2")) {
                    fd.setReadTimeMax(at.getReadTimeLevel2());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                } else if (fd.getFlowLevelName().equals("Level3")) {
                    fd.setReadTimeMax(at.getReadTimeLevel3());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                }
            }
            //设置显示人
            fd.setShowApprover(fd.getNowLevelUserName());
            dfFlowDataMapper.insert(fd);
        }
    }



    @ApiOperation("外观汇总导出") // API操作描述
    @RequestMapping(value = "/waigExport") // GET请求映射
    public void waigExport(String factoryId,
            String process, String project, String startTime, String endTime,
            String floor, HttpServletResponse response, HttpServletRequest request,@RequestBody(required = false) List<NewImageRequest> base64List
    ) throws IOException, ParseException {



        if (!StringUtils.isNotEmpty(factoryId)) {
            factoryId = fac;
        }

        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>(); // 创建缺陷配置查询包装器
        QueryWrapper<DfProcessProjectConfig> dw = new QueryWrapper<>(); // 创建缺陷配置查询包装器
        dw.orderByAsc("sort"); // 根据 priority 字段升序排列

        QueryWrapper<DailySummary> ew = new QueryWrapper<>(); // 创建查询包装器


        String endDate = "";

        String startDate = "";
        ew.eq(StringUtils.isNotEmpty(project), "f_bigpro", project);


        ew.eq(StringUtils.isNotEmpty(process), "f_seq", process);

        QueryWrapper<DfProject> queryWrapper = new QueryWrapper<>();
        dw.eq("floor", "4F");
        queryWrapper.eq(StringUtils.isNotEmpty(project), "name", project);
        List<DfProject> activeProjects = dfProjectService.list(queryWrapper);

        // 获取缺陷配置
        List<DfQmsIpqcFlawConfig> dflist = dfQmsIpqcFlawConfigService.listDistinctArea(qw);
        dw.eq(StringUtils.isNotEmpty(project) && !"%%".equals(project), "project", project);
        dw.eq(StringUtils.isNotEmpty(process), "f_seq", process);
        dw.eq("floor", "4F");

        // 获取工序数据
        List<DfProcessProjectConfig> dplistall = dfProcessProjectConfigService.listDistinct(dw);
        ArrayList<String> dpNames = new ArrayList<>();
        dflist.removeIf(flawConfig -> StringUtils.isEmpty(flawConfig.getFlawName()));
        // 统计缺陷名称出现次数，重复的全部去掉
        Map<String, Long> flawNameCount = dflist.stream()
                .collect(Collectors.groupingBy(DfQmsIpqcFlawConfig::getFlawName, Collectors.counting()));
        dflist.removeIf(flawConfig -> flawNameCount.get(flawConfig.getFlawName()) > 1);
        // 按大区分组（用于后续动态生成缺陷列）
        Map<String, List<DfQmsIpqcFlawConfig>> collect1 = dflist.stream()
                .collect(Collectors.groupingBy(DfQmsIpqcFlawConfig::getBigArea));

        // 去重工序
        List<DfProcessProjectConfig> dplist = dplistall.stream()
                .filter(distinctByKey(DfProcessProjectConfig::getProcessName))
                .collect(Collectors.toList());
        dplist.parallelStream()
                .filter(Objects::nonNull) // 过滤掉 null
                .forEach(m -> {
                    if (m != null && m.getProcessName() != null) {
                        dpNames.add(m.getProcessName());
                    }
                });

        // 固定表头分为前后两段
        List<String> preDefectHeader = new ArrayList<>(Arrays.asList("生产阶段", "型号", "颜色", "厂别", "抽检类型", "日期", "班次", "工序", "测试类别", "线体", "机台号", "抽检数", "良品数", "抽检良率", "不良数", "不良率"));
        List<String> postDefectHeader = new ArrayList<>(Arrays.asList("校验类型", "检验员"));
        List<String> fixedHeader = new ArrayList<>();
        fixedHeader.addAll(preDefectHeader);
        // 收集动态缺陷名称（确保唯一性）
        List<String> defectList = new ArrayList<>();
        collect1.forEach((bigArea, flawConfigs) -> {
            for (DfQmsIpqcFlawConfig config : flawConfigs) {
                String flawName = config.getFlawName();
                if (StringUtils.isNotEmpty(flawName) && !defectList.contains(flawName)) {
                    defectList.add(flawName);
                    fixedHeader.add(flawName);
                }
            }
        });
        fixedHeader.addAll(postDefectHeader);

        // 创建工作簿和工作表
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        SXSSFSheet sheet = wb.createSheet("外观明细数据");
        sheet.setDefaultColumnWidth(8);

        // 创建单元格样式
        XSSFCellStyle titleStyle = createTitleCellStyle(wb);
        XSSFCellStyle percentageStyle = createPercentageStyle(wb, titleStyle);
        XSSFCellStyle titleStyle2 = createTitleStyle2(wb, titleStyle);
        XSSFCellStyle lastRowStyle = createLastRowStyle(wb, titleStyle);


        // 创建两行表头（主表头行和子表头行）
        SXSSFRow headerRow0 = sheet.createRow(0); // 主表头行（第0行）
        SXSSFRow headerRow1 = sheet.createRow(1); // 子表头行（第1行）
        int cellIndex = 0; // 当前列索引

        // 用于存储每列的最大宽度
        Map<Integer, Integer> columnWidths = new HashMap<>();
        // 处理固定前表头（合并两行）
        for (String header : preDefectHeader) {
            SXSSFCell cell = headerRow0.createCell(cellIndex);
            cell.setCellValue(header);
            cell.setCellStyle(titleStyle);
            // 合并主表头行和子表头行的单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 1, cellIndex, cellIndex));
            // 更新列宽
            columnWidths.put(cellIndex, Math.max(columnWidths.getOrDefault(cellIndex, 0), header.length() ));
            cellIndex++;
        }

        // 处理动态缺陷部分（每个缺陷占两列）
        for (String defect : defectList) {
            // 主表头行：合并两列显示缺陷名称
            SXSSFCell defectCell = headerRow0.createCell(cellIndex);
            defectCell.setCellValue(defect);
            defectCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, cellIndex, cellIndex + 1));

            // 子表头行：分别为“数量”和“占比”
            SXSSFCell countCell = headerRow1.createCell(cellIndex);
            countCell.setCellValue("数量");
            countCell.setCellStyle(titleStyle);

            SXSSFCell ratioCell = headerRow1.createCell(cellIndex + 1);
            ratioCell.setCellValue("占比");
            ratioCell.setCellStyle(titleStyle);
            // 更新列宽
            columnWidths.put(cellIndex, Math.max(columnWidths.getOrDefault(cellIndex, 0), "数量".length() - 3));
            columnWidths.put(cellIndex + 1, Math.max(columnWidths.getOrDefault(cellIndex + 1, 0), "占比".length() - 3));

            cellIndex += 2; // 每个缺陷占两列，索引递增2
        }

        // 处理固定后表头（合并两行）
        for (String header : postDefectHeader) {
            SXSSFCell cell = headerRow0.createCell(cellIndex);
            cell.setCellValue(header);
            cell.setCellStyle(titleStyle);
            // 合并主表头行和子表头行的单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 1, cellIndex, cellIndex));

            // 更新列宽
            columnWidths.put(cellIndex, Math.max(columnWidths.getOrDefault(cellIndex, 0), header.length()));
            cellIndex++;
        }


        // 设置冻结窗格：要求从“不良率”开始冻结
        // 计算冻结列索引：preDefectHeader.size() + 动态缺陷列数
        // 每个缺陷2列，假设冻结从“不良率”这一列开始，即在后半部分的第一个列为“不良数”，第二列为“不良率”
        // 因此冻结的起始列为：preDefectHeader.size() + defectList.size()*2 + 1
        int freezeCol = preDefectHeader.size(); // 即13
        sheet.createFreezePane(freezeCol, 2);

        // 计算日期列表（周期为“天”）
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate startDateObj = LocalDate.parse(startTime);
        LocalDate endDateObj = LocalDate.parse(endTime);
        AtomicInteger rowIndex = new AtomicInteger(2);
        String period = "day";

        // ------------------------- 分周期逻辑 -------------------------
        if ("day".equals(period)) {
            // 设置时间范围
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                ew.between("date", startTime, endTime);
            } else {
                endDate = LocalDate.now().toString();
                startDate = LocalDate.now().minusDays(7).toString();
                ew.between("f_time", startDate, endDate);
            }
            // 分页导出各部分数据（f1: 白班/晚班/合计，f2: 工序合计，f3: 总体总计）
            // 使用 CompletableFuture 异步执行查询操作
                batchExport(ew.clone().eq("data_Type", "f1").eq("statistic_type", "day").orderByAsc("date"),
                        "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
                batchExport(ew.clone().eq("data_Type", "f2").eq("statistic_type", "day").orderByAsc("date"),
                        "f2", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
                batchExport(ew.clone().eq("data_Type", "f3").eq("statistic_type", "day").orderByAsc("date"),
                        "f3", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
        } else if ("week".equals(period)) {
            LocalDate startDateWeek;
            LocalDate endDateWeek;
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                startDateWeek = LocalDate.parse(startTime);
                endDateWeek = LocalDate.parse(endTime);
                if (startDateWeek.isAfter(endDateWeek)) {
                    // 交换起止日期
                    LocalDate temp = startDateWeek;
                    startDateWeek = endDateWeek;
                    endDateWeek = temp;
                }
            } else {
                // 默认最近1周
                endDateWeek = LocalDate.now();
                startDateWeek = endDateWeek.minusWeeks(1);
            }
            // 按周分割时间范围
            List<WeekRange> weeks = splitIntoWeeks(startDateWeek, endDateWeek);
            for (WeekRange week : weeks) {
                List<String> targetWeeks = getWeekNumbersBetween(week.getStartDate(), week.getEndDate());
                // 3.1 分批查询当周每日数据（f1，按天统计）
                QueryWrapper<DailySummary> dayQuery = new QueryWrapper<>();
                dayQuery.between("date", week.getStartDate(), week.getEndDate())
                        .eq("data_type", "f1")
                        .eq("statistic_type", "day")
                        .orderByAsc("date");
                batchExport(dayQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
                // 3.2 分批查询当周汇总数据（f1，按周统计）
                QueryWrapper<DailySummary> weekSummaryQuery = new QueryWrapper<>();
                weekSummaryQuery.in("type_num", targetWeeks)
                        .eq("data_type", "f1")
                        .eq("statistic_type", "week");
                batchExport(weekSummaryQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            }
            // 全周期的周汇总数据
            List<String> targetWeeks = getWeekNumbersBetween(startDateWeek, endDateWeek);
            QueryWrapper<DailySummary> processTotalQuery = new QueryWrapper<>();
            processTotalQuery.in("type_num", targetWeeks)
                    .eq("data_type", "f2")
                    .eq("statistic_type", "week");
            batchExport(processTotalQuery, "f2", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            QueryWrapper<DailySummary> overallTotalQuery = new QueryWrapper<>();
            overallTotalQuery.in("type_num", targetWeeks)
                    .eq("data_type", "f3")
                    .eq("statistic_type", "week");
            batchExport(overallTotalQuery, "f3", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
        } else if ("month".equals(period)) {
            LocalDate startDateMonth;
            LocalDate endDateMonth;
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                startDateMonth = LocalDate.parse(startTime);
                endDateMonth = LocalDate.parse(endTime);
                if (startDateMonth.isAfter(endDateMonth)) {
                    LocalDate temp = startDateMonth;
                    startDateMonth = endDateMonth;
                    endDateMonth = temp;
                }
            } else {
                // 默认最近1个月
                endDateMonth = LocalDate.now();
                startDateMonth = endDateMonth.minusMonths(1);
            }
            // 按月分割时间范围
            List<MonthRange> months = splitIntoMonths(startDateMonth, endDateMonth);
            for (MonthRange month : months) {
                // 3.1 当月每周数据（f1，按周统计）
                QueryWrapper<DailySummary> weekQuery = new QueryWrapper<>();
                weekQuery.in("type_num", month.getWeekNumbers())
                        .eq("data_type", "f1")
                        .eq("statistic_type", "week")
                        .orderByAsc("date");
                batchExport(weekQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
                // 3.2 当月汇总数据（f1，按月统计）
                QueryWrapper<DailySummary> monthSummaryQuery = new QueryWrapper<>();
                monthSummaryQuery.between("date", month.getStartDate(), month.getEndDate())
                        .eq("data_type", "f1")
                        .eq("statistic_type", "month");
                batchExport(monthSummaryQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            }
            QueryWrapper<DailySummary> processTotalQuery = new QueryWrapper<>();
            processTotalQuery.between("date", startDate, endDate)
                    .eq("data_type", "f2")
                    .eq("statistic_type", "month");
            batchExport(processTotalQuery, "f2", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            QueryWrapper<DailySummary> overallTotalQuery = new QueryWrapper<>();
            overallTotalQuery.between("date", startDate, endDate)
                    .eq("data_type", "f3")
                    .eq("statistic_type", "month");
            batchExport(overallTotalQuery, "f3", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
        } else if ("year".equals(period)) {
            LocalDate startDateYear;
            LocalDate endDateYear;
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                startDateYear = LocalDate.parse(startTime);
                endDateYear = LocalDate.parse(endTime);
                if (startDateYear.isAfter(endDateYear)) {
                    LocalDate temp = startDateYear;
                    startDateYear = endDateYear;
                    endDateYear = temp;
                }
            } else {
                // 默认最近1年
                endDateYear = LocalDate.now();
                startDateYear = endDateYear.minusYears(1);
            }
            // 按年分割时间范围
            List<YearRange> years = splitIntoYears(startDateYear, endDateYear);
            for (YearRange year : years) {
                // 3.1 当年每月数据（f1，按月统计）
                QueryWrapper<DailySummary> monthQuery = new QueryWrapper<>();
                monthQuery.between("date", year.getStartDate(), year.getEndDate())
                        .eq("data_type", "f1")
                        .eq("statistic_type", "month")
                        .orderByAsc("date");
                batchExport(monthQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
                // 3.2 当年汇总数据（f1，按年统计）
                QueryWrapper<DailySummary> yearSummaryQuery = new QueryWrapper<>();
                yearSummaryQuery.between("date", year.getStartDate(), year.getEndDate())
                        .eq("data_type", "f1")
                        .eq("statistic_type", "year");
                batchExport(yearSummaryQuery, "f1", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            }
            QueryWrapper<DailySummary> processTotalQuery = new QueryWrapper<>();
            processTotalQuery.between("date", startDate, endDate)
                    .eq("data_type", "f2")
                    .eq("statistic_type", "year");
            batchExport(processTotalQuery, "f2", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
            QueryWrapper<DailySummary> overallTotalQuery = new QueryWrapper<>();
            overallTotalQuery.between("date", startDate, endDate)
                    .eq("data_type", "f3")
                    .eq("statistic_type", "year");
            batchExport(overallTotalQuery, "f3", sheet, rowIndex, defectList, titleStyle, percentageStyle, titleStyle2, lastRowStyle);
        }


        QueryWrapper<DfAuditDetail> wv = new QueryWrapper<>(); // 创建查询包装器

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            wv.between("d.report_time", startTime + " 07:00:00", TimeUtil.getNextDay(endTime) + " 07:00:00");
        }else {
            // 获取当前日期并计算最近10天的起止时间
            endDate = LocalDate.now().toString() + " 07:00:00"; // 当前日期
            startDate = LocalDate.now().minusDays(7).toString() + " 07:00:00"; // 7天前的日期
            wv.between("d.report_time", startDate, endDate); // 查询最近10天的数据
        }
        wv.eq(StringUtils.isNotEmpty(project)&&!"%%".equals(project), "d.project_name", project)
                .eq(StringUtils.isNotEmpty(floor), "dp.floor", floor);

        wv.like(StringUtils.isNotEmpty(project), "dp.project",   project );



        wv.eq(StringUtils.isNotEmpty(process), "d.process", process);

        wv.eq("d.data_type", "外观");

        // 设置时间范围
//        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
//            wv.between("t.f_time", startTime + " 07:00:00", TimeUtil.getNextDay(endTime) + " 07:00:00");
//        }
        wv.orderByDesc("d.report_time");

        List<DfAuditDetail> datas = DfAuditDetailService.exportExcleAuditNgDetail(wv);

        List<Map> maps = new ArrayList<>();
        for (DfAuditDetail r : datas) {
            String decisionLevelStr = "";
            String levelString = "";
            if (r.getDecisionLevel().equals("Level1") || r.getDecisionLevel() == "Level-1") {
                levelString = " Level-1";
                if (r.getResponsible() != null) {
                    decisionLevelStr = r.getResponsible();
                }
            } else if (r.getDecisionLevel().equals("Level2") || r.getDecisionLevel().equals("Level-2")) {
                // console.log(r.responsible2);
                levelString = "Level-2";
                if (r.getResponsible2() != null) {
                    decisionLevelStr = r.getResponsible2();
                }
            } else if (r.getDecisionLevel().equals("Level3") || r.getDecisionLevel().equals("Level-3")) {
                levelString = "Level-3";
                if (r.getResponsible3() != null) {
                    decisionLevelStr = r.getResponsible3();
                }
            }

            if (decisionLevelStr.equals("")) {
                if (r.getDecisionLevel().equals("Level2") || r.getDecisionLevel().equals("Level-2")) {
                    if (r.getResponsible() != null) {
                        decisionLevelStr = r.getResponsible();
                    }
                } else if (r.getDecisionLevel().equals("Level3") || r.getDecisionLevel().equals("Level-3")) {
                    if (r.getResponsible2() != null) {
                        decisionLevelStr = r.getResponsible2();
                    }
                }
            }
            r.setDecisionLevelStr(decisionLevelStr);
            r.setDecisionLevel(levelString);
            if (null!=r.getStatus()&&r.getStatus().equals("待发起人确认")) {
                r.setShowApprover(r.getCreateName());
            }

            String statusStr = r.getStatus();
            if (null != r.getStatus() && !r.getStatus().equals("null")) {
                if (r.getStatus().equals("待确认")) {
                    statusStr = "工厂接收";
                }
                if (r.getStatus().equals("待提交") || r.getStatus().equals("FACA退回待提交")) {
                    statusStr = "工厂行动";
                }
                // if (r.getStatus().equals(" '待办结人确认'||r.getStatus().equals(" '待发起人确认') {
                if (r.getStatus().equals("待发起人确认")) {
                    statusStr = "IPQC确认";
                }
                if (r.getStatus().equals("待办结人确认")) {
                    statusStr = "已关闭";
                }
            } else {
                statusStr = "";
            }
            r.setStatus(statusStr);
            DecimalFormat decimalFormat = new DecimalFormat("#0");
            if (null != r.getEndTime()) {
                r.setUseTime(decimalFormat.format(((r.getEndTime().getTime() - r.getReportTime().getTime()) / 1000 / 60)) + "分");
                if (((r.getEndTime().getTime() - r.getReportTime().getTime()) / 1000 / 60) > 60) {
                    r.setOverTime(decimalFormat.format((((r.getEndTime().getTime() - r.getReportTime().getTime()) / 1000 / 60) - 60)) + "分");
                } else {
                    r.setOverTime("未超时");
                }

            } else {
                r.setUseTime(decimalFormat.format(((new Date().getTime() - r.getReportTime().getTime()) / 1000 / 60)) + "分");

                if (((new Date().getTime() - r.getReportTime().getTime()) / 1000 / 60) > 60) {
                    r.setOverTime(decimalFormat.format((((new Date().getTime() - r.getReportTime().getTime()) / 1000 / 60) - 60)) + "分");
                } else {
                    r.setOverTime("未超时");
                }

            }
            if ("CPK".equals(r.getQuestionType())) {
                r.setQuestionType("过程抽检");
            }

            try {
                maps.add(Excel.objectToMap(r));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        String[] titleSheet5 = new String[]{"项目分类","项目", "工序","班次","线体", "机台","问题类别","问题名称", "发起人", "发起时间",
        "事件等级", "问题点对应责任人",
                "问题明细", "影响机台", "处理意见", "当前责任人", "当前节点", "进行时长", "超时时长", "FA", "CA"};        //设置表格表头字段
        String[] propertiesSheet5 = new String[]{"dataType","projectName","process","shift","fLine","fMac","questionType", "questionName", "createName", "reportTime",
                "decisionLevel",
                "decisionLevelStr",
                "scenePractical", "affectMac", "handlingSug", "showApprover",
                "status",
                "useTime", "overTime", "fa", "ca"};  // 查询对应的字段




        // 导出Excel
        ExcelCheckExportUtil2 excelExport2 = new ExcelCheckExportUtil2(); // 创建Excel导出工具
        excelExport2.setFontSize(14); // 设置字体大小
        excelExport2.setStartDate(startDate);
        excelExport2.setEndDate(endDate);
        excelExport2.setFactoryId(factoryId);
        excelExport2.setPeriod(period);

        ExcelCheckExportUtil2 excelExportSheet5 = new ExcelCheckExportUtil2(); // 创建Excel导出工具
        excelExportSheet5.setData(maps);
        excelExportSheet5.setHeardKey(propertiesSheet5);
        excelExportSheet5.setHeardList(titleSheet5);

        excelExport2.exportExportNew(wb,request, response, collect1, dplist, activeProjects,base64List,excelExportSheet5,datas); // 导出文件
    }

    public XSSFCellStyle createTitleCellStyle(SXSSFWorkbook wb) {
        XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
        // XSSFFont titleFont = (XSSFFont) wb.createFont();
        //titleFont.setFontHeightInPoints((short) 14);  // 设置字体大小
        //titleFont.setBold(true);  // 设置字体加粗
        // titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
/*        titleStyle.setBorderTop(BorderStyle.THIN);  // 上边框
        titleStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
        titleStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
        titleStyle.setBorderRight(BorderStyle.THIN);  // 右边框*/
        return titleStyle;
    }


    // 分页导出数据的公共方法
    private void batchExport(QueryWrapper<DailySummary> baseQuery, String label, SXSSFSheet sheet,
                             AtomicInteger rowIndex, List<String> defectList, XSSFCellStyle titleStyle,
                             XSSFCellStyle percentageStyle, XSSFCellStyle titleStyle2, XSSFCellStyle lastRowStyle) {
        int pageSize = 1000;
        Long lastId = 0L;
        while (true) {
            QueryWrapper<DailySummary> pageQuery = baseQuery.clone()
                    .gt("id", lastId)
                    .orderByAsc("id")
                    .last("LIMIT " + pageSize);
            List<DailySummary> chunk = dailySummaryService.list(pageQuery);
            if (chunk.isEmpty()) {
                break;
            }
            // 加载缺陷数据（统一处理）
            selectDefect(chunk);
            for (DailySummary ds : chunk) {
                rowIndex.set(createDataRowFromObject(sheet, rowIndex.get(), defectList, label, ds,
                        titleStyle, percentageStyle, titleStyle2, lastRowStyle));
            }
            lastId = Long.valueOf(chunk.get(chunk.size() - 1).getId());
        }
    }


    private XSSFCellStyle createPercentageStyle(SXSSFWorkbook wb, XSSFCellStyle titleStyle) {
        XSSFCellStyle percentageStyle = (XSSFCellStyle) wb.createCellStyle();
        percentageStyle.setAlignment(HorizontalAlignment.CENTER);
        percentageStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        percentageStyle.cloneStyleFrom(titleStyle);
        XSSFColor percentageColor = new XSSFColor(new java.awt.Color(218, 238, 243), new DefaultIndexedColorMap());
        percentageStyle.setFillForegroundColor(percentageColor);
        percentageStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        percentageStyle.setBorderBottom(BorderStyle.THIN);
        percentageStyle.setBorderTop(BorderStyle.THIN);
        percentageStyle.setBorderLeft(BorderStyle.THIN);
        percentageStyle.setBorderRight(BorderStyle.THIN);
        return percentageStyle;
    }

    private XSSFCellStyle createTitleStyle2(SXSSFWorkbook wb, XSSFCellStyle titleStyle) {
        XSSFCellStyle titleStyle2 = (XSSFCellStyle) wb.createCellStyle();
        titleStyle2.cloneStyleFrom(titleStyle);
        titleStyle2.setAlignment(HorizontalAlignment.CENTER);
        titleStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFColor colorPartTwo = new XSSFColor(new java.awt.Color(0xFC, 0xD5, 0xB4), new DefaultIndexedColorMap());
        titleStyle2.setFillForegroundColor(colorPartTwo);
        titleStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle2.setBorderBottom(BorderStyle.THIN);
        titleStyle2.setBorderTop(BorderStyle.THIN);
        titleStyle2.setBorderLeft(BorderStyle.THIN);
        titleStyle2.setBorderRight(BorderStyle.THIN);
        return titleStyle2;
    }

    private XSSFCellStyle createLastRowStyle(SXSSFWorkbook wb, XSSFCellStyle titleStyle) {
        XSSFCellStyle lastRowStyle = (XSSFCellStyle) wb.createCellStyle();
        lastRowStyle.cloneStyleFrom(titleStyle);
        lastRowStyle.setAlignment(HorizontalAlignment.CENTER);
        lastRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFColor colorLastRow = new XSSFColor(new java.awt.Color(0xDC, 0xE6, 0xF1), new DefaultIndexedColorMap());
        lastRowStyle.setFillForegroundColor(colorLastRow);
        lastRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        lastRowStyle.setBorderBottom(BorderStyle.THIN);
        lastRowStyle.setBorderTop(BorderStyle.THIN);
        lastRowStyle.setBorderLeft(BorderStyle.THIN);
        lastRowStyle.setBorderRight(BorderStyle.THIN);
        return lastRowStyle;
    }


    public List<String> getWeekNumbersBetween(LocalDate startDate, LocalDate endDate) {
        List<String> weekNumbers = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            // 生成周标识符（如 2024-WK2）
            int year = current.get(WeekFields.ISO.weekBasedYear());
            int week = current.get(WeekFields.ISO.weekOfWeekBasedYear());
            String weekNumber = String.format("%04d-WK%02d", year, week);

            weekNumbers.add(weekNumber);
            current = current.plusWeeks(1); // 移动到下一周
        }
        return weekNumbers;
    }

    public List<MonthRange> splitIntoMonths(LocalDate startDate, LocalDate endDate) {
        List<MonthRange> months = new ArrayList<>();
        LocalDate current = startDate.withDayOfMonth(1);

        while (!current.isAfter(endDate)) {
            LocalDate monthStart = current;
            LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
            if (monthEnd.isAfter(endDate)) {
                monthEnd = endDate;
            }

            // 获取该月包含的所有周编号
            List<String> weekNumbers = getWeekNumbersBetween(monthStart, monthEnd);

            MonthRange monthRange = new MonthRange(monthStart, monthEnd);
            monthRange.setWeekNumbers(weekNumbers);
            months.add(monthRange);

            current = monthEnd.plusDays(1).withDayOfMonth(1);
        }

        return months;
    }

    // 按年分割时间范围
    public List<YearRange> splitIntoYears(LocalDate startDate, LocalDate endDate) {
        List<YearRange> years = new ArrayList<>();
        LocalDate current = startDate.withDayOfYear(1);
        while (!current.isAfter(endDate)) {
            LocalDate yearStart = current;
            LocalDate yearEnd = current.withDayOfYear(current.lengthOfYear());
            if (yearEnd.isAfter(endDate)) {
                yearEnd = endDate;
            }
            years.add(new YearRange(yearStart, yearEnd));
            current = yearEnd.plusDays(1).withDayOfYear(1);
        }
        return years;
    }


    // 按周分割时间范围的工具方法
    private List<WeekRange> splitIntoWeeks(LocalDate startDate, LocalDate endDate) {
        List<WeekRange> weeks = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDate weekStart = current.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(endDate)) {
                weekEnd = endDate;
            }
            weeks.add(new WeekRange(weekStart, weekEnd));
            current = weekEnd.plusDays(1);
        }
        return weeks;
    }


    private void selectDefect(List<DailySummary> list) {
        // 2. 提取主表ID集合
        List<Integer> processSummaryIds = list.stream()
                .filter(ds -> ds.getSpotCheckCount() != null && ds.getSpotCheckCount() > 0 && ds.getNgNum() != null && ds.getNgNum() > 0)
                .map(DailySummary::getId)
                .collect(Collectors.toList());

        // 3. 批量查询子表缺陷数据
        List<DefectSummary> delist = defectSummaryService.batchQuery(processSummaryIds);

        // 4. 将子表数据按 process_summary_id 分组
        Map<Integer, List<DefectSummary>> defectGroupByProcess = delist.stream()
                .collect(Collectors.groupingBy(DefectSummary::getProcessSummaryId));

        // 5. 将子表数据设置到主表对象的 defect 列表中
        list.forEach(da -> {
            // 获取当前主表记录关联的所有缺陷
            List<DefectSummary> defects = defectGroupByProcess.getOrDefault(da.getId(), Collections.emptyList());

            // 将缺陷列表直接设置到主表记录中
            da.setDefect(defects);  // 将缺陷列表设置到 defect 属性中
        });
    }


    private int createDataRowFromObject(
            SXSSFSheet sheet,
            int rowIndex,
            List<String> defectList,
            String dataType,
            DailySummary data,
            XSSFCellStyle titleStyle,
            XSSFCellStyle percentageStyle,
            XSSFCellStyle defaultStyle,
            XSSFCellStyle lastRowStyle
    ) {
        SXSSFRow row = sheet.createRow(rowIndex++);
        int cellIdx = 0;

        // 固定列（根据 dataType 设置样式）
        createCell(row, cellIdx++, data.getFStage(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFBigpro(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFColor(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFFac(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFType(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(
                row,
                cellIdx++,
                data.getDate() != null ? data.getDate() : data.getTypeNum(),
                getStyle(dataType, titleStyle, defaultStyle, lastRowStyle)
        );
        createCell(row, cellIdx++, data.getShift(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFSeq(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFTestType(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFLine(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getFMac(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));

        // 数值列
        createCell(row, cellIdx++, data.getSpotCheckCount(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, data.getOkNum(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        createCell(row, cellIdx++, String.format("%.2f%%", data.getOkRate()), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));

        //   createCell(row, cellIdx++, String.format("%.2f%%", data.getOkRate()), titleStyle);
        createCell(row, cellIdx++, data.getNgNum(), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));
        //   createCell(row, cellIdx++, String.format("%.2f%%", data.getNgRate()), titleStyle);
        createCell(row, cellIdx++, String.format("%.2f%%", data.getNgRate()), getStyle(dataType, titleStyle, defaultStyle, lastRowStyle));


        // 动态缺陷列处理
        for (String defectName : defectList) {
            // 获取 defect 对象
            DefectSummary defect = null;
            if (data.getDefect() != null) {
                // 遍历 defect 列表，找到与 defectName 匹配的 defect
                for (DefectSummary ds : data.getDefect()) {
                    if (ds.getDefectName().trim().equals(defectName.trim())) {
                        defect = ds;
                        break;  // 找到后退出循环
                    }
                }
            }

            // 处理缺陷数量
            int count = (defect != null) ? defect.getDefectCount() : 0;
            createCell(row, cellIdx++, count, titleStyle);

            // 处理缺陷率（带百分比格式化）
            double rate = (defect != null) ? defect.getDefectRate() : 0.0;
            createCell(row, cellIdx++, String.format("%.2f%%", rate), percentageStyle);
        }


        // 尾部列
        createCell(row, cellIdx++, data.getFTestType(), titleStyle);
        createCell(row, cellIdx++, data.getFTestMan(), titleStyle);

        return rowIndex;
    }

    // 辅助方法：创建单元格并设置值和样式
    private void createCell(SXSSFRow row, int index, Object value, XSSFCellStyle style) {
        Cell cell = row.createCell(index);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private XSSFCellStyle getStyle(String dataType, XSSFCellStyle titleStyle, XSSFCellStyle defaultStyle, XSSFCellStyle lastRowStyle) {
        switch (dataType) {
            case "f1":
                return titleStyle;
            case "f2":
                return defaultStyle;
            case "f3":
                return lastRowStyle;
            default:
                return null; // 或者抛出异常，或者返回一个默认的样式
        }
    }





    public List<List<DfQmsIpqcWaigTotalCheck>> getOptimizedDataWithCustomExecutor(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew, int totalCount, String floor,String project) {
        // 创建一个固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1); // 线程池大小为1

        // 创建 CompletableFuture 使用指定的线程池
        CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> futureList = CompletableFuture.supplyAsync(() -> {
            try {
                String projectWithPercent = "%" + project + "%";
                return getPagedData(ew, totalCount, floor,projectWithPercent);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executorService);

        // 等待任务完成并获取结果
        List<DfQmsIpqcWaigTotalCheck> list = futureList.join();

        // 合并结果（这里只返回单一的查询结果列表）
        List<List<DfQmsIpqcWaigTotalCheck>> result = new ArrayList<>();
        result.add(list);

        // 关闭线程池
        executorService.shutdown();

        return result;
    }

    public List<DfQmsIpqcWaigTotalCheck> getPagedData(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew,int totalCount,String floor,String project) throws InterruptedException, ExecutionException {
        List<DfQmsIpqcWaigTotalCheck> allData = new ArrayList<>();
        int pageSize = 5000;  // 每页查询5000条记录
        ExecutorService executor = Executors.newFixedThreadPool(4);  // 使用4个线程池进行分页查询并发处理

//        // 获取总记录数
//        int totalCount = DfQmsIpqcWaigTotalService.Count(ew);
//        ew.eq(StringUtils.isNotEmpty(floor), "dp.floor", floor); // 根据楼层过滤
        int totalPages = (totalCount + pageSize - 1) / pageSize;  // 计算总页数

        // 使用CompletableFuture提交所有任务
        List<CompletableFuture<List<DfQmsIpqcWaigTotalCheck>>> futures = new ArrayList<>();
        for (int page = 1; page <= totalPages; page++) {
            final int pageOffset = (page - 1) * pageSize;
            CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return DfQmsIpqcWaigTotalCheckService.listWaigExcelDataPage(ew, pageOffset, pageSize,floor,project);
                } catch (Exception e) {
                    // 捕获每个查询任务中的异常，并打印日志
                    System.err.println("Error fetching data for page " + pageOffset + ": " + e.getMessage());
                    return new ArrayList<>();  // 返回一个空的列表以确保继续执行
                }
            }, executor);
            futures.add(future);
        }

        // 等待所有任务完成并收集结果
        for (CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> future : futures) {
            try {
                List<DfQmsIpqcWaigTotalCheck> pageData = future.get();  // 获取结果
                if (pageData != null && !pageData.isEmpty()) {
                    allData.addAll(pageData);
                }
            } catch (ExecutionException e) {
                // 捕获线程执行中的异常
                System.err.println("ExecutionException occurred while processing data: " + e.getMessage());
            } catch (InterruptedException e) {
                // 捕获线程被中断的异常
                Thread.currentThread().interrupt();  // 恢复中断状态
                System.err.println("Thread was interrupted: " + e.getMessage());
            }
        }

        executor.shutdown();  // 关闭线程池
        return allData;
    }

    @ApiOperation("大屏主面板统计导出")
    @GetMapping("/LargeScreenDownload")
    public void listNgItemRateDownload(String factory, String lineBody, String project, String floor, String dayOrNight, String process, String startDate, String endDate, HttpServletResponse response, HttpServletRequest request) throws ParseException, IOException {

        if (null == floor || "".equals(floor)){} floor = "%%";
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";

        String startTime = startDate + " 07:00:00";
        String endTime = "";
        if (endDate != null && !"".equals(endDate)) {
            endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        }
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }


        //第一个sheet数据
        List<Rate> rates1 = DfQmsIpqcWaigTotalCheckService.listNgTop103(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
        //第二个sheet数据  *********************************************************************************************
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listNgItemRateSortByNgTop3(lineBody, project, floor, process, startTime, endTime);
        Map<String, Map<String, List<Object>>> itemResData = new LinkedHashMap<>();
        List<Object> dateList = new ArrayList<>();
        for (Rate rate : rates) {
            if (null == rate) continue;
            String itemName = rate.getName();
            String date = rate.getDate();
            if (!dateList.contains(date)) {
                dateList.add(date);
            }

            if (!itemResData.containsKey(itemName)) {   // 白夜班
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                Double ngRate = rate.getRate();// 获取 rate 值
                double formattedRate = 0.0;
                if (ngRate == null) {
                    ngRate = 0.0;
                }else {
                    // 乘以100并保留两位小数
                    DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                    formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型

                }
                rateList.add(formattedRate);
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                itemValue.put("name", name);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.put("dayRateList", rateList);
                    itemValue.put("nightRateList", new ArrayList<>());
                } else {  // 晚班
                    itemValue.put("dayRateList", new ArrayList<>());
                    itemValue.put("nightRateList", rateList);
                }
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.get("dayRateList").add(rate.getRate());
                } else {  // 晚班
                    itemValue.get("nightRateList").add(rate.getRate());
                }
            }
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResData.entrySet()) {
            entry.getValue().put("date", dateList);
            result.add(entry.getValue());
        }

        //第三个sheet数据 *********************************************************************************************
        lineBody=lineBody.equals("%%")?null:lineBody;
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw = new QueryWrapper<>();
        qw.eq(null != process && !"".equals(process)&&!"%%".equals(process), "tol.f_seq", process)
                .eq(null != lineBody && !"".equals(lineBody), "tol.f_line", lineBody)
                .eq(null != project && !"".equals(project), "tol.f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startTime, endTime)
                .ne("WEEKDAY(DATE_SUB(TOL.F_TIME, INTERVAL 7 HOUR))", 6)   // 去掉周日的数据
                .isNotNull("spot_check_count");
        List<Rate> processResRates = DfQmsIpqcWaigTotalCheckService.listAllProcessOkRate(qw, floor,project.equals("%%")?project:"%"+project+"%");
        String[] processArr = new String[processResRates.size()];
        Double[] rateArr = new Double[processResRates.size()];
        int i = 0;
        for (Rate processResRate : processResRates) {
            Double ngRate = processResRate.getRate(); // 获取 rate 值
            double formattedRate = 0.0;
            if (ngRate == null) {
                ngRate = 0.0;
            }else {
                // 乘以100并保留两位小数
                DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型

            }
            processArr[i] = processResRate.getName();
            rateArr[i++] = formattedRate;
        }
        Map<String, Object> resultSheet3 = new HashMap<>();
        resultSheet3.put("name", processArr);
        resultSheet3.put("rate", rateArr);

        //第四个sheet数据**********************************************************************************************
        QueryWrapper<DfQmsIpqcWaigTotalCheck> qw4 = new QueryWrapper<>();
        qw4
                .eq(StringUtils.isNotEmpty(project), "tol.f_bigpro", project)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "tol.f_time", startTime, endTime);
        List<Rate> ratesSheet4 = DfQmsIpqcWaigTotalCheckService.listAlwaysOkRate(qw4);
        Map<String, List<Object>> resultSheet4 = new HashMap<>();
        List<Object> nameListSheet4 = new ArrayList<>();
        List<Object> rateListSheet4 = new ArrayList<>();
        for (Rate rate : ratesSheet4) {
            Double ngRate = rate.getRate(); // 获取 rate 值
            double formattedRate = 0.0;
            if (ngRate == null) {
                ngRate = 0.0;
            }else {
                // 乘以100并保留两位小数
                DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型

            }
            nameListSheet4.add(rate.getName());
            rateListSheet4.add(formattedRate);
        }
        resultSheet4.put("name", nameListSheet4);
        resultSheet4.put("rate", rateListSheet4);
        //第五个sheet数据展示**************************************************************************************************
        List<Rate> ratesSheet5 = DfQmsIpqcWaigTotalCheckService.listOkRateNear15Day(qw);
        List<Object> dateListSheet5 = new ArrayList<>();
        List<Object> rateListSheet5 = new ArrayList<>();
        List<Object> nameSheet5 = new ArrayList<>();

        for (Rate rate : ratesSheet5) {
            Double ngRate = rate.getRate(); // 获取 rate 值
            double formattedRate = 0.0;
            if (ngRate == null) {
                ngRate = 0.0;
            }else {
                // 乘以100并保留两位小数
                DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型
            }
            dateListSheet5.add(rate.getDate());
            rateListSheet5.add(formattedRate);
        }
        if ("%%".equals(process))
            process = null;
        nameSheet5.add(process);
        Map<String, List<Object>> resultSheet5 = new HashMap<>();
        resultSheet5.put("date", dateListSheet5);
        resultSheet5.put("rate", rateListSheet5);
        resultSheet5.put("fseq",nameSheet5);


        // 导出Excel
        BigScreenExcelExporter excelExport2 = new BigScreenExcelExporter(); // 创建Excel导出工具
        excelExport2.exportExport(request, response, rates, result, rates1, processResRates, resultSheet3, resultSheet4, resultSheet5);

        //return new Result(200, "查询成功", result);

    }


    @ApiOperation("弹框统计表导出")
    @GetMapping("/NgRateByDownload")
    public void NgRateByDownload(String machineCode,String fsort,String factory, String lineBody, String project, String floor, String dayOrNight, String process, String startDate, String endDate, HttpServletResponse response, HttpServletRequest request) throws ParseException, IOException {
        if (null == lineBody || "".equals(lineBody)) lineBody = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == process || "".equals(process)) process = "%%";
        if (null == process || "".equals(process)) process = "%%";
        if (null == floor || "".equals(floor)) floor = "%%";

        String startTime = startDate + " 07:00:00";

        String endTime = "";
        if (endDate!=null && !"".equals(endDate)) {
            endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";;
        }

        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        //sheet1*************************************************************************************************************************************
        List<Rate> rates = DfQmsIpqcWaigTotalCheckService.listProcessNgRateByNgItem(lineBody, project, floor, fsort, startTime, endTime, startHour, endHour);
        //sheet2***********************************************************************************************************
        //  List<Rate> ratesSheet2 = DfQmsIpqcWaigTotalService.listProcessNgRateByNgItem(lineBody, project, floor, ngItem, startTime, endTime, startHour, endHour);
        Map<String, List<Object>> resultSheet2 = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        List<Object> ngNumList = new ArrayList<>();
        List<Object> allNumList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());

            Double ngRate = rate.getRate(); // 获取 rate 值
            double formattedRate = 0.0;
            if (ngRate == null) {
                ngRate = 0.0;
            }else {
                // 乘以100并保留两位小数
                DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型

            }
            rateList.add(formattedRate);
            ngNumList.add(rate.getNgNum());
            allNumList.add(rate.getAllNum());
        }
        resultSheet2.put("name", nameList);
        resultSheet2.put("rate", rateList);
        resultSheet2.put("ngNum", ngNumList);
        resultSheet2.put("allNum", allNumList);


        //sheet3******************************************************************************************
        List<Rate> ratesSheet3 = DfQmsIpqcWaigTotalCheckService.listMacNgRateByNgItemAndProcess(process, lineBody, project, floor, fsort, startTime, endTime, startHour, endHour);
        Map<String, List<Object>> resultSheet3 = new HashMap<>();
        List<Object> nameListSheet3 = new ArrayList<>();
        List<Object> rateListSheet3 = new ArrayList<>();
        List<Object> ngNumListSheet3 = new ArrayList<>();
        List<Object> allNumListSheet3 = new ArrayList<>();
        for (Rate rate : ratesSheet3) {
            nameListSheet3.add(rate.getName());
            Double ngRate = rate.getRate(); // 获取 rate 值
            double formattedRate = 0.0;
            if (ngRate == null) {
                ngRate = 0.0;
            }else {
                // 乘以100并保留两位小数
                DecimalFormat df = new DecimalFormat("0.00"); // 创建格式化对象，保留两位小数
                formattedRate = Double.valueOf(df.format(ngRate * 100)); // 将ngRate乘以100并转换为Double类型

            }
            rateListSheet3.add(formattedRate);
            ngNumListSheet3.add(rate.getNgNum());
            allNumListSheet3.add(rate.getAllNum());
        }
        resultSheet3.put("name", nameListSheet3);
        resultSheet3.put("rate", rateListSheet3);
        resultSheet3.put("ngNum", ngNumListSheet3);
        resultSheet3.put("allNum", allNumListSheet3);

        //sheet4***********************************************************************************************************
        List<Rate> ratesSheet4 = DfQmsIpqcWaigTotalCheckService.listMacNgRateByNgItemAndProcess(process, lineBody, project, floor, fsort, startTime, endTime, startHour, endHour);

        //sheet5***************************************************************************************************************
        QueryWrapper<DfQmsIpqcWaigDetailCheck> ew= new QueryWrapper<>();
        QueryWrapper<DfQmsIpqcWaigDetailCheck>qw2=new QueryWrapper<>();
        if ("%%".equals(project)) {
            project = "";
        }
        ew.eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(project),"p.f_bigpro",project);
        qw2.eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(project),"tol.f_bigpro",project);


        if(null!=process&&!process.equals("")&&!process.equals("undefined")&&!"%%".equals(process)){
            if ("%%".equals(floor)){
                floor = "";
            }
            ew.like("dp.floor",floor)
                    .eq("p.f_seq",process);
            qw2.like("dp.floor",floor)
                    .eq("f_seq",process);
        }
        if(null!=fsort&&!fsort.equals("")&&!fsort.equals("undefined")){
            ew.eq("d.f_sort",fsort);
        }
        if(null!=startTime&&!startTime.equals("")&&!startTime.equals("undefined")&&null!=endTime&&!endTime.equals("")&&!endTime.equals("undefined")){
            ew.between("p.f_time",startTime,endTime);
            ew.between("HOUR(DATE_SUB(p.f_time, INTERVAL 7 HOUR))", startHour, endHour);
            qw2.between("f_time",startTime,endTime);
            qw2.between("HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR))",startHour,endHour);
        }
        if(null!=machineCode&&!machineCode.equals("")&&!machineCode.equals("undefined")&&!machineCode.equals("null")){
            ew.eq("p.f_mac",machineCode);
            qw2.eq("f_mac",machineCode);
        }
        ew.isNotNull("d.f_sm_area");
        ew.orderByAsc("d.f_sm_area");
//        ew.last("limit "+count);

        DfQmsIpqcWaigDetailCheck counts=DfQmsIpqcWaigDetailCheckService.getSumAffectCount(qw2);
        //用parentID存放总数
        List<DfQmsIpqcWaigDetailCheck>datas=DfQmsIpqcWaigDetailCheckService.listBySmAreaCount(ew);
        if(null!=datas&&datas.size()>0){
            for(DfQmsIpqcWaigDetailCheck d:datas){
                d.setFParentId(counts.getId());
            }
        }


        NgRateByCheckExcelExport excelExport2 = new NgRateByCheckExcelExport(); // 创建Excel导出工具
        // 导出Excel
        excelExport2.NgRateByExport(request, response, rates,resultSheet2,resultSheet3,ratesSheet4,datas,fsort);

    }
    // 新增：通用处理方法，用于将 map 中的数据添加到对应的列表中
    private void processShiftMap(Map<String, List<DfQmsIpqcWaigTotalCheck>> shiftMap, List<DfQmsIpqcWaigTotalCheck> shiftList, List<DfQmsIpqcWaigTotalCheck> nightShiftList, List<DfQmsIpqcWaigTotalCheck> totalShiftList) {
        for (Map.Entry<String, List<DfQmsIpqcWaigTotalCheck>> entry : shiftMap.entrySet()) {
            DfQmsIpqcWaigTotalCheck dfQmsIpqcWaigTotalCheck = new DfQmsIpqcWaigTotalCheck();
            String[] keyParts = entry.getKey().split(",");

            // 设置必要的字段
            dfQmsIpqcWaigTotalCheck.setDate(keyParts[0]);
            dfQmsIpqcWaigTotalCheck.setfFac(keyParts[2]);
            dfQmsIpqcWaigTotalCheck.setfTime(Timestamp.valueOf(keyParts[13]));
            // dfQmsIpqcWaigTotalCheck.setfTime(Timestamp.valueOf(keyParts[4]));
            //判断班次
            String classes = keyParts[4];
            dfQmsIpqcWaigTotalCheck.setfBigpro(keyParts[5]);
            dfQmsIpqcWaigTotalCheck.setShift(classes);
            dfQmsIpqcWaigTotalCheck.setfSeq(keyParts[7]);
            dfQmsIpqcWaigTotalCheck.setfMac(keyParts[11]);
            dfQmsIpqcWaigTotalCheck.setSort(Integer.parseInt(keyParts[15]));
            //dfQmsIpqcWaigTotalCheck.setfSort(keyParts[13]);

            // 计算抽检数、良品数、不良数
            if (!CollectionUtils.isEmpty(entry.getValue())) {
                DfQmsIpqcWaigTotalCheck total = entry.getValue().get(0); // 取第一个
                dfQmsIpqcWaigTotalCheck.setSpotCheckCount(total.getSpotCheckCount());
                String okNum = total.getOkNum(); // 获取良品数
                if (okNum != null) { // 检查良品数是否有效
                    dfQmsIpqcWaigTotalCheck.setOkNum(okNum); // 设置良品数
                } else {
                    dfQmsIpqcWaigTotalCheck.setOkNum("0"); // 如果无效，则设置为默认值 0
                }

                // 获取不良品数量并检查是否非负
                String ngNum = total.getNgNum(); // 获取不良数
                if (ngNum != null) { // 检查不良数是否有效
                    dfQmsIpqcWaigTotalCheck.setNgNum(ngNum); // 设置不良数
                } else {
                    dfQmsIpqcWaigTotalCheck.setNgNum("0"); // 如果无效，则设置为默认值 0
                }
                dfQmsIpqcWaigTotalCheck.setfSort(total.getfSort());
                dfQmsIpqcWaigTotalCheck.setOkRate(total.getOkRate());
                // 检查是否为零以避免除零异常
                if (total.getSpotCheckCount() > 0) {
                    // 直接进行不良率计算，避免多余的类型转换
                    double ngRate = Double.parseDouble(total.getNgNum()) / total.getSpotCheckCount(); // 计算不良率
                    dfQmsIpqcWaigTotalCheck.setNgRate(ngRate); // 设置不良率
                } else {
                    dfQmsIpqcWaigTotalCheck.setNgRate(0.0); // 抽检数为零时不良率为零
                }
            }

            if ("白班".equals(classes)) {
                shiftList.add(dfQmsIpqcWaigTotalCheck);
            } else {
                nightShiftList.add(dfQmsIpqcWaigTotalCheck);
            }
            totalShiftList.add(dfQmsIpqcWaigTotalCheck);
        }
    }


    private void processShiftMap5(Map<String, List<DfQmsIpqcWaigTotalCheck>> shiftMap, List<DfQmsIpqcWaigTotalCheck> shiftList) {
        for (Map.Entry<String, List<DfQmsIpqcWaigTotalCheck>> entry : shiftMap.entrySet()) {
            DfQmsIpqcWaigTotalCheck dfQmsIpqcWaigTotalCheck = new DfQmsIpqcWaigTotalCheck();
            String[] keyParts = entry.getKey().split(",");

            // 设置必要的字段
            dfQmsIpqcWaigTotalCheck.setDate(keyParts[0]);
            dfQmsIpqcWaigTotalCheck.setShift(keyParts[4]);
            dfQmsIpqcWaigTotalCheck.setfBigpro(keyParts[5]);
            dfQmsIpqcWaigTotalCheck.setfSeq(keyParts[7]);

            // 计算抽检数、良品数、不良数
            if (!CollectionUtils.isEmpty(entry.getValue())) {
                DfQmsIpqcWaigTotalCheck total = entry.getValue().get(0); // 取第一个
                // 判断每个属性是否为空，如果为空则设置为0
                dfQmsIpqcWaigTotalCheck.setCpkTotal(total.getCpkTotal() != null ? total.getCpkTotal() : 0);
                dfQmsIpqcWaigTotalCheck.setCpkNg(total.getCpkNg() != null ? total.getCpkNg() : 0);
                dfQmsIpqcWaigTotalCheck.setFaiStartupRate(total.getFaiStartupRate() != null ? total.getFaiStartupRate() : 0);
                dfQmsIpqcWaigTotalCheck.setCpkPassRate(total.getCpkPassRate() != null ? total.getCpkPassRate() : 0);
                dfQmsIpqcWaigTotalCheck.setFaiCount(total.getFaiCount() != null ? total.getFaiCount() : 0);
                dfQmsIpqcWaigTotalCheck.setMachineNum(total.getMachineNum() != null ? total.getMachineNum() : 0);
            }

            shiftList.add(dfQmsIpqcWaigTotalCheck);
        }
    }

}
