package com.ww.boengongye.controller;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfApprovalTimeMapper;
import com.ww.boengongye.mapper.DfAuditDetailMapper;
import com.ww.boengongye.mapper.DfFlowDataMapper;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.*;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.activemq.plugin.DiscardingDLQBrokerPlugin.log;

/**
 * <p>
 * 尺寸数据 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
@Controller
@RequestMapping("/dfSizeDetail")
@Api(tags = "尺寸数据")
@CrossOrigin
@ResponseBody
public class DfSizeDetailController {

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Autowired
    private DfSizeContStandService dfSizeContStandService;

    @Autowired
    private com.ww.boengongye.service.DfSizeFailService DfSizeFailService;
    @Autowired
    private com.ww.boengongye.service.DfSizeCheckItemInfosService DfSizeCheckItemInfosService;
    @Autowired
    private com.ww.boengongye.service.DfProcessService DfProcessService;
    @Autowired
    private com.ww.boengongye.service.DfSizeNgDataService DfSizeNgDataService;

    @Autowired
    private com.ww.boengongye.service.DfMacStatusSizeService DfMacStatusSizeService;
    @Autowired
    private DfLiableManMapper dfLiableManMapper;
    @Autowired
    private DfFlowDataMapper dfFlowDataMapper;
    @Autowired
    private DfApprovalTimeMapper dfApprovalTimeMapper;
    @Autowired
    private DfAuditDetailMapper dfAuditDetailMapper;

    @Autowired
    private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Autowired
    private DfSizeOkRateService dfSizeOkRateService;

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    Environment env;
//    @Autowired
//    @Qualifier("defaultJmsTemplate")
//    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("defaultJmsTemplate")
    private JmsMessagingTemplate jmsQueueTemplate;

    // 获取vb数据接口
    @Value("${FindVbCodeAPI}")
    private String FindVbCodeAPI;



    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfSizeDetailService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }
    @ApiOperation("更新机台状态")
    @GetMapping("/updateStatus")
    public Result updateStatus()  {
        InitializeCheckRule.sizeStatus=new HashMap<>();
        List<DfMacStatusSize>sizeSta=DfMacStatusSizeService.list();
        if(sizeSta.size()>0){
            for(DfMacStatusSize s:sizeSta){
                InitializeCheckRule.sizeStatus.put(s.getMachineCode(),s);
            }
        }
        return new Result(200, "操作成功");
    }

    @ApiOperation("机台不良率")
    @GetMapping("/listMachineNgRateByFactory")
    public Result listAllByFactory(String factory, String process, String project, String linebody, String dayOrNight,
                                   String startDate, String endDate) {
        List<DfSizeDetail> dfSizeDetails = dfSizeDetailService.listAllByFactory(factory, process, project, linebody, dayOrNight, startDate, endDate + " 23:59:59");
        return new Result(200, "查询成功", dfSizeDetails);

    }

    @ApiOperation("总体良率")
    @GetMapping("/getOkRate")
    public Result getOkRate(String factory, String process, String project, String linebody, String dayOrNight,
                            String startDate, String endDate) {
        DfSizeDetail dfSizeDetails = dfSizeDetailService.getOkRate(factory, process, project, linebody, dayOrNight, startDate, endDate + " 23:59:59");
        return new Result(200, "查询成功", dfSizeDetails.getProject());

    }

    @GetMapping(value = "/getById")
    public Object getById(int id) {

        return new Result(0, "查询成功", dfSizeDetailService.getById(id));
    }
    @GetMapping(value = "/listNg")
    public Object listNg() {
            QueryWrapper<DfSizeDetail>qw=new QueryWrapper<>();
            qw.eq("result","NG");
            qw.orderByDesc("test_time");
            qw.last("limit 0,7");
        return new Result(0, "查询成功", dfSizeDetailService.list(qw));
    }

    @GetMapping(value = "/listByMachineAndTime")
    public Object listByMachineAndTime(String machineCode,String time) throws ParseException {
        if(machineCode.indexOf("-")!=-1){
            String[] code=machineCode.split("-");
            QueryWrapper<DfProcess>qw=new QueryWrapper<>();
            qw.eq("process_name",code[0]);
            qw.last("limit 0,1");
        DfProcess process= DfProcessService.getOne(qw);
        machineCode=process.getFirstCode()+code[1];
        }
        QueryWrapper<DfSizeDetail>qw=new QueryWrapper<>();
        qw.eq("machine_code",machineCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        qw.apply("UNIX_TIMESTAMP(test_time) >= UNIX_TIMESTAMP('" +TimeUtil.beforeOneHourToNowDate(sdf.parse(time))  + "')");
        qw.apply("UNIX_TIMESTAMP(test_time) <= UNIX_TIMESTAMP('" +TimeUtil.afterOneHourToNowDate(sdf.parse(time))  + "')");
        qw.orderByDesc("test_time");
        return new Result(0, "查询成功", dfSizeDetailService.list(qw));
    }


    @GetMapping(value = "/listPcs")
    public Object listPcs(String machineCode,int count) {
        QueryWrapper<DfSizeDetail>qw=new QueryWrapper<>();
//        qw.eq("machine_code",machineCode);
        qw.eq("result","NG");
        qw.inSql("id","select tt.id from (select id from df_size_detail where machine_code='"+machineCode+"' order by test_time desc limit "+count+")tt");
        qw.orderByDesc("test_time");
//        qw.last("limit 0,"+count);


        QueryWrapper<DfSizeDetail>qw2=new QueryWrapper<>();
        qw2.eq("machine_code",machineCode);
        qw2.orderByDesc("test_time");
//        qw2.last("limit 0,"+count);
        qw2.inSql("id","select tt.id from (select id from df_size_detail where machine_code='"+machineCode+"' order by test_time desc limit "+count+")tt");
        int allCount=dfSizeDetailService.count(qw2);

        List<DfSizeDetail>datas=dfSizeDetailService.list(qw);
        if(datas.size()>0){
            for(DfSizeDetail d:datas){
//                QueryWrapper<DfSizeFail>ew=new QueryWrapper<>();
//                ew.eq("parent_id",d.getId());
//                List<DfSizeFail> l=DfSizeFailService.list(ew);
//                if(l.size()>0){
//                    String  str="";
//                    int i=0;
//                    for(DfSizeFail ld:l){
//                        if(i>0){
//                            str+=",";
//                        }
//                        str+=ld.getBadCondition();
//                        i++;
//                    }
//                    d.setBadStatus(str);
//                }
                QueryWrapper<DfSizeNgData>ew=new QueryWrapper<>();
                ew.eq("check_id",d.getCheckId());
//                ew.eq("check_result","NG");
                List<DfSizeNgData> l=DfSizeNgDataService.list(ew);
                if(l.size()>0){
                    String  str="";
                    int i=0;
                    for(DfSizeNgData ld:l){
                        if(i>0){
                            str+=",";
                        }
                        if(null!=ld.getBadCondition()){
                            str+=ld.getBadCondition();
                            i++;
                        }

                    }
                    d.setBadStatus(str);
                }
            }
        }
        return new Result(0, "查询成功",allCount ,datas);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfSizeDetail datas) {

        if (null != datas.getId()) {
            if (dfSizeDetailService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (dfSizeDetailService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @GetMapping(value = "/delete")
    public Result delete(String id) {
        if (dfSizeDetailService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String factoryCode,String process,String barCode,String startTime,String endTime) {
        Page<DfSizeDetail> pages = new Page<DfSizeDetail>(page, limit);
        QueryWrapper<DfSizeDetail> ew=new QueryWrapper<DfSizeDetail>();

        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("factory", factoryCode);
        }
        if(null!=process&&!process.equals("")) {
            ew.eq("process", process);
        }
        if(null!=barCode&&!barCode.equals("")) {
            ew.eq("machine_code", barCode);
        }

        if(null!=startTime&&!startTime.equals("")&&null!=endTime&&!endTime.equals("")) {
            ew.between("test_time", startTime,endTime);
        }else if(null!=startTime&&!startTime.equals("")){
            ew.ge("test_time",startTime);
        }else  if(null!=endTime&&!endTime.equals("")){
            ew.le("test_time",endTime);
        }

        ew.isNotNull("result");
        ew.ne("result","");
//        ew.orderByAsc("r.result");
        ew.orderByDesc("test_time");
        IPage<DfSizeDetail> list=dfSizeDetailService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

    @GetMapping(value = "/listDateOkRate")
    public Result listDateOkRate(String project, String color, String process, String startDate, String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfSizeDetail> qw=new QueryWrapper<DfSizeDetail>();
        qw
                .eq(null!=process && !"".equals(process), "process", process)
                .eq(null!=project && !"".equals(project), "project", project)
                .eq(null!=color && !"".equals(color), "color", color)
                .between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                        "create_time", startTime, endTime)
                .ne("WEEKDAY(create_time)", 6);  // 去掉周日的数据
        List<Rate> rates = dfSizeDetailService.listDateOkRate2(qw);
        Map<String, Double> dateResRate = new HashMap<>();
        /*for (Rate rate : rates) {
            dateResRate.put(rate.getDate(), rate.getRate());
            System.out.println(rate);
        }*/

        List<Object> dateList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        for (Rate rate : rates) {
            dateList.add(rate.getDate());
            rateList.add(rate.getRate());
        }
        /*for (Object date : dateList) {
            rateList.add(dateResRate.getOrDefault(date.toString(), null));
        }*/
        Map<String, List<Object>> result = new HashMap<>();
        result.put("date", dateList);
        result.put("okRateList", rateList);

        return new Result(0, "查询成功", result);
    }

    @ApiOperation("获取机台的不良率走势图")
    @GetMapping("/listMacNgRate")
    public Result listMacNgRate(String project,String color,@RequestParam String startDate, @RequestParam String endDate, String dayOrNight, @RequestParam String machineCode) throws ParseException {
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
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(project),"project",project)
                .eq(StringUtils.isNotEmpty(color),"item_name",color)
                .between("test_time", startTime, endTime)
                .between("HOUR(DATE_SUB(test_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq("machine_code", machineCode);
        List<Rate> rates = dfSizeDetailService.listDateNgRate(qw);
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

    @ApiOperation("获取机台不良项的不良率")
    @GetMapping("/getMacNgItemNgRate")
    public Result getMacNgItemNgRate(String project,String color,String startDate, String endDate, String machineCode, String dayOrNight) throws ParseException {
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
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(project),"det.project",project)
                .eq(StringUtils.isNotEmpty(color),"det.item_name",color)
                .between("det.test_time", startTime, endTime)
                .between("HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq("machine_code", machineCode)
                .eq("key_point", 1);
        List<Rate> rates = dfSizeDetailService.listItemNgRate(qw);
        List<Object> itemList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        for (Rate rate : rates) {
            itemList.add(rate.getName());
            rateList.add(rate.getRate());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("itemList",itemList);
        result.put("rateList",rateList);

        return new Result<>(200, "查询成功", result);
    }

    @ApiOperation("获取尺寸实际良率")
    @GetMapping(value = "/getSizeRealOkRate")
    public Result getSizeRealRate(String process, String factory, String startDate, String endDate) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:01";
        QueryWrapper<DfSizeDetail> qw=new QueryWrapper<DfSizeDetail>();
        qw.eq(null!=process && !"".equals(process), "process", process)
                .eq(null!=factory && !"".equals(factory), "factory", factory)
                .between(null != startDate && null != endDate, "test_time", startDate, endDate);
        Rate rates = dfSizeDetailService.getSizeRealRate(qw);
        return new Result(0, "查询成功", rates);
    }

    @ApiOperation("各工序直通趋势对比")
    @GetMapping("/listAlwaysOkRate")
    public Result listAlwaysOkRate(String project, String color, String startDate, String endDate) throws ParseException {
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw.between(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate),
                "create_time", startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
                .eq(!"".equals(color) && null != color, "color", color);
        if ("".equals(project) || null == project) {
            project = "%%";
        } else {
            project = "%" + project + "%";
        }
        //List<Rate> rates = dfSizeDetailService.listAlwaysOkRate(qw);
        //List<Rate> rates = dfSizeDetailService.listAlwaysOkRate2(qw);
        List<Rate> rates = dfSizeDetailService.listAlwaysOkRate2(qw, project);
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> rateList = new ArrayList<>();
        for (Rate rate : rates) {
            nameList.add(rate.getName());
            rateList.add(rate.getRate());
        }
        System.out.println();
        result.put("name", nameList);
        result.put("rate", rateList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("获取该不良项各工序的不良率--图")
    @GetMapping("/listProcessNgRateByNgItem")
    public Result listProcessNgRateByNgItem(String project, String color, String ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
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
        //List<Rate> rates = dfSizeDetailService.listProcessNgRateByNgItem(ngItem, startTime, endTime, startHour, endHour);

        // 使用新表获取数据
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw.between("create_time", startTime, endTime)
                .eq(!"".equals(dayOrNight) && null != dayOrNight, "day_or_night", dayOrNight)
                .eq("item_name", ngItem)
                .eq(StringUtils.isNotEmpty(project), "project", project)
                .eq(StringUtils.isNotEmpty(color), "color", color);
        List<Rate> rates = dfSizeDetailService.listProcessNgRateByNgItem2(qw);

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
    public Result listProcessNgRateByNgItemData(String project, String color, String ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
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
        //List<Rate> rates = dfSizeDetailService.listProcessNgRateByNgItem(ngItem, startTime, endTime, startHour, endHour);

        // 使用新表获取数据
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw.between("create_time", startTime, endTime)
                .eq(!"".equals(dayOrNight) && null != dayOrNight, "day_or_night", dayOrNight)
                .eq("item_name", ngItem)
                .eq(StringUtils.isNotEmpty(project), "project", project)
                .eq(StringUtils.isNotEmpty(color), "color", color);
        List<Rate> rates = dfSizeDetailService.listProcessNgRateByNgItem2(qw);
        return new Result(200, "查询成功", rates);
    }

    @ApiOperation("获取该不良项各机台的不良率--图")
    @GetMapping("/listMacNgRateByNgItemAndProcess")
    public Result listMacNgRateByNgItemAndProcess(String process,String project,String color, String ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == process || "".equals(process)) process = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == color || "".equals(color)) color = "%%";
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
        List<Rate> rates = dfSizeDetailService.listMacNgRateByNgItemAndProcess(process,project,color, ngItem, startTime, endTime, startHour, endHour);
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
    public Result listMacNgRateByNgItemAndProcessData(String process,String project,String color, String ngItem, String startDate, String endDate, String dayOrNight) throws ParseException {
        if (null == process || "".equals(process)) process = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == color || "".equals(color)) color = "%%";
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
        List<Rate> rates = dfSizeDetailService.listMacNgRateByNgItemAndProcess(process,project,color, ngItem, startTime, endTime, startHour, endHour);
        return new Result(200, "查询成功", rates);
    }

    @GetMapping("/listAllProcessOkRate")
    @ApiOperation("各工序良率")
    public Result listAllProcessOkRate(String project, String color, String startDate, String endDate) throws ParseException {
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw
                .between("det.create_time", startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
                .eq(!"".equals(project) && null != project, "det.project", project)
                .eq(!"".equals(color) && null != color, "det.color", color)
                .isNotNull("pro.sort");

        List<Rate> rates = dfSizeDetailService.listAllProcessOkRate2(qw,StringUtils.isNotEmpty(project)?"%"+project+"%":"%%");
        Map<String, Object> result = new HashMap<>();
        List<String> processList = new ArrayList<>();
        List<Double> rateList = new ArrayList<>();
        List<Object> passTargetList = new ArrayList<>();

        for (Rate rate : rates) {
            processList.add(rate.getName());
            rateList.add(rate.getRate());
            passTargetList.add(rate.getPassTarget());
        }
        result.put("process", processList);
        result.put("rate", rateList);
        result.put("passTarget",passTargetList);
        return new Result<>(200, "查询成功", result);
    }

    @GetMapping(value = "/listNgData")
    public Object getById() {
        QueryWrapper<DfSizeDetail>qw=new QueryWrapper<>();
        qw.eq("result","NG");
        qw.orderByDesc("test_time");

        qw.last("limit 300");
        List<DfSizeDetail>datas=dfSizeDetailService.list(qw);
        if(datas.size()>0){
            for(DfSizeDetail d:datas){
                QueryWrapper<DfSizeFail>qw2=new QueryWrapper<>();
                qw2.eq("parent_id",d.getId());
                List<DfSizeFail> l=DfSizeFailService.list(qw2);
                if(l.size()>0) {
                    String str = "";
                    int i = 0;
                    for (DfSizeFail ld : l) {
                        if (i > 0) {
                            str += ",";
                        }
                        str += ld.getBadCondition();
                        i++;
                    }
                    d.setBadStatus(str);
                }
            }
        }
        return new Result(0, "查询成功", datas);
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

    @GetMapping(value = "/sendMQ")
    public Result sendMQ() {
        Destination testQueueDestination = new ActiveMQQueue("Report_ProductGague_J10-1_J10-1-4");
        jmsQueueTemplate.convertAndSend(testQueueDestination, "{\n" +
                "    \"CheckDevCode\": \"002\",\n" +
                "    \"CheckItemInfos\": [\n" +
                "        {\n" +
                "            \"CheckResult\": \"NG\",\n" +
                "            \"CheckTime\": \"2023-03-22 20:41:05\",\n" +
                "            \"CheckValue\": 80,\n" +
                "            \"ComDirection\": \"\",\n" +
                "            \"CompRatio\": 1.000,\n" +
                "            \"CompValue\": 0.000,\n" +
                "            \"ControlLowerLimit\": 0.000,\n" +
                "            \"ControlUpperLimit\": 0.000,\n" +
                "            \"DCode\": \"M617\",\n" +
                "            \"FloatValue\": 0.000,\n" +
                "            \"FocusType\": 0,\n" +
                "            \"ItemName\": \"平台长\",\n" +
                "            \"LSL\": 29.740,\n" +
                "            \"Remark\": \"Remark\",\n" +
                "            \"RepairType\": 0,\n" +
                "            \"SN\": \"C42G\",\n" +
                "            \"StandardValue\": 29.820,\n" +
                "            \"ToolCode\": \"T1\",\n" +
                "            \"ToolFlag\": \"\",\n" +
                "            \"TrendValue\": 0.000,\n" +
                "            \"USL\": 29.900\n" +
                "        }\n" +
                "    ],\n" +
                "    \"CheckResult\": \"NG\",\n" +
                "    \"CheckTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"CheckType\": 1,\n" +
                "    \"CurrentTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"FactoryCode\": \"J6-3\",\n" +
                "    \"GroupCode\": \"G001\",\n" +
                "    \"ID\": \"1c39014ae19d48f1828e6abb6b49b0b3\",\n" +
                "    \"ItemName\": \"C42G-YN-CNC2-1\",\n" +
                "    \"MacType\": \"PVG400\",\n" +
                "    \"MachineCode\": \"U001\",\n" +
                "    \"ProcessNO\": \"CNC3\",\n" +
                "    \"Remark\": \"C42G\",\n" +
                "    \"SN\": \"C42G\",\n" +
                "    \"ShiftName\": \"\",\n" +
                "    \"Tester\": \"A\",\n" +
                "    \"UnitCode\": \"C001\",\n" +
                "    \"WorkShopCode\": \"J6-3-9\"\n" +
                "}");

        jmsQueueTemplate.convertAndSend(testQueueDestination, "{\n" +
                "    \"CheckDevCode\": \"002\",\n" +
                "    \"CheckItemInfos\": [\n" +
                "        {\n" +
                "            \"CheckResult\": \"NG\",\n" +
                "            \"CheckTime\": \"2023-03-22 20:41:05\",\n" +
                "            \"CheckValue\": 80,\n" +
                "            \"ComDirection\": \"\",\n" +
                "            \"CompRatio\": 1.000,\n" +
                "            \"CompValue\": 0.000,\n" +
                "            \"ControlLowerLimit\": 0.000,\n" +
                "            \"ControlUpperLimit\": 0.000,\n" +
                "            \"DCode\": \"M617\",\n" +
                "            \"FloatValue\": 0.000,\n" +
                "            \"FocusType\": 0,\n" +
                "            \"ItemName\": \"平台长\",\n" +
                "            \"LSL\": 29.740,\n" +
                "            \"Remark\": \"Remark\",\n" +
                "            \"RepairType\": 0,\n" +
                "            \"SN\": \"C42G\",\n" +
                "            \"StandardValue\": 29.820,\n" +
                "            \"ToolCode\": \"T1\",\n" +
                "            \"ToolFlag\": \"\",\n" +
                "            \"TrendValue\": 0.000,\n" +
                "            \"USL\": 29.900\n" +
                "        }\n" +
                "    ],\n" +
                "    \"CheckResult\": \"NG\",\n" +
                "    \"CheckTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"CheckType\": 3,\n" +
                "    \"CurrentTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"FactoryCode\": \"J6-3\",\n" +
                "    \"GroupCode\": \"G001\",\n" +
                "    \"ID\": \"1c39014ae19d48f1828e6abb6b49b0b3\",\n" +
                "    \"ItemName\": \"C42G-YN-CNC2-1\",\n" +
                "    \"MacType\": \"PVG400\",\n" +
                "    \"MachineCode\": \"U001\",\n" +
                "    \"ProcessNO\": \"3D抛光\",\n" +
                "    \"Remark\": \"C42G\",\n" +
                "    \"SN\": \"C42G\",\n" +
                "    \"ShiftName\": \"\",\n" +
                "    \"Tester\": \"A\",\n" +
                "    \"UnitCode\": \"C001\",\n" +
                "    \"WorkShopCode\": \"J6-3-9\"\n" +
                "}");
        return new Result(200, "请求成功");


    }

    @GetMapping("/insertDataByNumberAndProcess")
    public Result insertDataByNumberAndProcess(String process, Integer number, Integer maxMachineNum) {
        QueryWrapper<DfProcess> processQW = new QueryWrapper<>();
        processQW.eq("process_name", process).last("limit 1");
        DfProcess one = DfProcessService.getOne(processQW);
        int count = 0;
        String firstCode;
        if (null == one || null == one.getFirstCode()) {
            return new Result<>(202, "没有此工序");
        } else {
            firstCode = one.getFirstCode();
        }

        QueryWrapper<DfSizeContStand> qw = new QueryWrapper<>();
        qw.eq("process", process)
                .eq("key_point", 1);
        List<DfSizeContStand> processStandard = dfSizeContStandService.list(qw);
        Map<String, Double> processResMin = new HashMap<>();
        Map<String, Double> processResMax = new HashMap<>();
        Map<String, Double> processResStandard = new HashMap<>();
        Map<String, Double> processResIslandMin = new HashMap<>();
        Map<String, Double> processResIslandMax = new HashMap<>();
        for (DfSizeContStand dfSizeContStand : processStandard) {
            String testItem = dfSizeContStand.getTestItem();
            if (null == dfSizeContStand.getLowerLimit()) {
                processResMin.put(testItem, dfSizeContStand.getIsolaLowerLimit());
            } else {
                processResMin.put(testItem, dfSizeContStand.getLowerLimit());
            }
            if (null == dfSizeContStand.getUpperLimit()) {
                processResMax.put(testItem, dfSizeContStand.getIsolaUpperLimit());
            } else {
                processResMax.put(testItem, dfSizeContStand.getUpperLimit());
            }
            processResStandard.put(testItem, dfSizeContStand.getStandard());
            processResIslandMin.put(testItem, dfSizeContStand.getIsolaLowerLimit());
            processResIslandMax.put(testItem, dfSizeContStand.getIsolaUpperLimit());
        }

        for (int i = 0; i < number; i++) {
            Timestamp createTime = new Timestamp((long)(Math.random() * (Timestamp.valueOf("2023-07-23 07:00:00").getTime() - Timestamp.valueOf("2023-07-18 07:00:00").getTime()) + Timestamp.valueOf("2023-07-18 07:00:00").getTime()));
            String machineCode = firstCode + String.format("%03d", (int) range(1, maxMachineNum));

            DfSizeDetail detail = new DfSizeDetail();
            detail.setProcess(process);
            detail.setMachineCode(machineCode);
            detail.setResult("OK");
            detail.setTestTime(createTime);
            detail.setCreateTime(createTime);
            detail.setFactory("J10-1");
            detail.setInfoResult("OK");
            detail.setCurTime("1");  // 标记
            this.save(detail);
            count++;

            List<DfSizeCheckItemInfos> infoList = new ArrayList<>();
            for (Map.Entry<String, Double> entry : processResMin.entrySet()) {
                String itemName = entry.getKey();
                Double min = entry.getValue();
                Double max = processResMax.get(itemName);
                DfSizeCheckItemInfos info = new DfSizeCheckItemInfos();
                info.setItemName(itemName);
                info.setCheckResult("OK");
                info.setCheckTime(createTime.toString());
                info.setCheckValue(range(min, max));
                info.setStandardValue(processResStandard.get(itemName));
                info.setLsl(processResIslandMin.get(itemName));
                info.setUsl(processResIslandMax.get(itemName));
                info.setCheckId(detail.getId() + "");
                info.setCreateTime(createTime);
                info.setKeyPoint("1");
                info.setComDirection("1"); // 标记
                infoList.add(info);
                count++;
            }
            DfSizeCheckItemInfosService.saveBatch(infoList);
        }

        return new Result<>(200, "成功插入" + count + "条数据");
    }

    private double range(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    @GetMapping("/getrfid")
    public Result getrfid() {
        Rfid r1 = new Rfid("2023-07-21 10:48:28", "HF3GTK0005C000039W+G", "QXCJ");
        Rfid r2 = new Rfid("2023-07-21 10:48:28", "HF3GTK0005C000039W+G", "QXCJ");
        Rfid r3 = new Rfid("2023-07-21 10:48:28", "HF3GTK0005C000039W+G", "QXCJ");
        List<Rfid> result = new ArrayList<>();
        result.add(r1);
        result.add(r2);
        result.add(r3);
        return new Result(1000, "查询成功", result);
    }

    @GetMapping("/listOpenRate")
    @ApiOperation("获取开机数开机率等")
    public Result listOpenRate(String lineBody, String project, String process, @RequestParam String startDate,
                               @RequestParam String endDate, String dayOrNight) throws ParseException {

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
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();
        qw.between("det.test_time", startTime, endTime)
                .between("HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(lineBody) && null != lineBody, "det.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "det.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process);
        if ("".equals(process) || null == process) process = "%%";
        List<Rate3> rates = dfSizeDetailService.listOpenRate(qw, process);
        Map<String, Object> result = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Integer> openNumList = new ArrayList<>();
        List<Double> openRateList = new ArrayList<>();
        List<Double> firstOpenRateList = new ArrayList<>();
        List<Double> firstOpenOkRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            openNumList.add(rate.getInte1());
            openRateList.add(rate.getDou1());
            firstOpenRateList.add(rate.getDou2());
            firstOpenOkRateList.add(rate.getDou3());
        }
        result.put("date", dateList);
        result.put("openNum", openNumList);
        result.put("openRate", openRateList);
        result.put("firstOpenRate", firstOpenRateList);
        result.put("firstOpenOkRate", firstOpenOkRateList);

        return new Result(200, "查询成功", result);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    @GetMapping("/weekOfPoorTOP3Warning")
    @ApiOperation("尺寸机台连续一周不良TOP3 预警(不包含周日)")
    public void weekOfPoorTOP3Warning() throws ParseException {
        System.out.println("尺寸机台连续一周不良TOP3 预警 start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        QueryWrapper<DfSizeDetail> ew = new QueryWrapper<>();
        List<DfSizeDetail> list = dfSizeDetailService.weekOfPoorTOP3Warning(ew);
        for (DfSizeDetail dfSizeDetail : list) {
            QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
            sqw.eq("type","尺寸机台连续一周不良TOP3");
            sqw.eq("problem_level","1");
            if(TimeUtil.getBimonthly()==0){
                sqw.like("bimonthly","双月");
            }else{
                sqw.like("bimonthly","单月");
            }
//            sqw.like("process_name","AOI良率");
            List<DfLiableMan> lm =dfLiableManMapper.selectList(sqw);
            if (lm==null||lm.size()==0){
                return;
            }
            StringBuilder manCode=new StringBuilder();
            StringBuilder manName=new StringBuilder();
            int manCount=0;
            for(DfLiableMan l:lm){
                if(manCount>0){
                    manCode.append(",");
                    manName.append(",");
                }
                manCode.append(l.getLiableManCode());
                manName.append(l.getLiableManName());
                manCount++;
            }
            DfAuditDetail aud=new DfAuditDetail();
//            aud.setLine(machine.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
            aud.setDataType("尺寸");
            aud.setDepartment("尺寸");
            aud.setAffectMac(dfSizeDetail.getMachineCode());
            aud.setAffectNum(1.0);
            aud.setControlStandard("尺寸机台连续一周不良TOP3");
            aud.setImpactType("尺寸机台连续一周不良TOP3");
            aud.setIsFaca("0");

            //问题名称和现场实际调换
            aud.setQuestionName("良率报警");
//            aud.setProcess("AOI");
//            aud.setProjectName("");

            aud.setReportTime(Timestamp.valueOf(checkTime));
            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
            aud.setIpqcNumber(checkTimeLong);

            aud.setScenePractical("尺寸机台连续一周不良在TOP3 " + "机台号:" + dfSizeDetail.getMachineCode());
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
            fd.setName("尺寸机台连续一周不良TOP3");
            fd.setDataId(aud.getId());
            fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

            fd.setNowLevelUser(aud.getResponsibleId());
            fd.setNowLevelUserName(aud.getResponsible());
            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
            fd.setFlowLevelName(aud.getDecisionLevel());

            QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
            atQw.eq("type","尺寸")
                    .last("limit 1");
            DfApprovalTime at= dfApprovalTimeMapper.selectOne(atQw);
            if(null!=at){
                if(fd.getFlowLevelName().equals("Level1")){
                    fd.setReadTimeMax(at.getReadTimeLevel1());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                }else  if(fd.getFlowLevelName().equals("Level2")){
                    fd.setReadTimeMax(at.getReadTimeLevel2());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                }else  if(fd.getFlowLevelName().equals("Level3")){
                    fd.setReadTimeMax(at.getReadTimeLevel3());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                }
            }
            //设置显示人
            fd.setShowApprover(fd.getNowLevelUserName());
            dfFlowDataMapper.insert(fd);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    @GetMapping("/twoWeekOfPoorTOP3Warning")
    @ApiOperation("尺寸缺陷连续两周不良TOP3 预警(不包含周日)")
    public void twoWeekOfPoorTOP3Warning() throws ParseException {
        System.out.println("尺寸缺陷连续两周不良TOP3 预警 start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        List<Rate3> list = dfSizeDetailService.twoWeekOfPoorTOP3Warning();
        if (list==null||list.size()==0){
            return;
        }

        for (Rate3 processDefect : list) {

            QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
            sqw.like("process_name",processDefect.getStr1());
            sqw.eq("type","size");
            sqw.eq("problem_level","1");
            if(TimeUtil.getBimonthly()==0){
                sqw.like("bimonthly","双月");
            }else{
                sqw.like("bimonthly","单月");
            }
//            sqw.like("process_name","AOI良率");
            List<DfLiableMan> lm =dfLiableManMapper.selectList(sqw);
            if (lm==null||lm.size()==0){
                return;
            }
            StringBuilder manCode=new StringBuilder();
            StringBuilder manName=new StringBuilder();
            int manCount=0;
            for(DfLiableMan l:lm){
                if(manCount>0){
                    manCode.append(",");
                    manName.append(",");
                }
                manCode.append(l.getLiableManCode());
                manName.append(l.getLiableManName());
                manCount++;
            }
            DfAuditDetail aud=new DfAuditDetail();
//            aud.setLine(machine.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
            aud.setDataType("尺寸");
            aud.setDepartment("尺寸");
//            aud.setAffectMac(dfSizeDetail.getMachineCode());
//            aud.setAffectNum(1.0);
            aud.setControlStandard("尺寸缺陷连续两周在不良TOP3中");
            aud.setImpactType("尺寸");
            aud.setIsFaca("0");

            //问题名称和现场实际调换
            aud.setQuestionName("不良报警");
            aud.setProcess(processDefect.getStr1());
//            aud.setProjectName("");

            aud.setReportTime(Timestamp.valueOf(checkTime));
            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
            aud.setIpqcNumber(checkTimeLong);

            aud.setScenePractical("工序" + processDefect.getStr1()+"_缺陷名为"+processDefect.getStr2()+"_尺寸缺陷连续两周不良在TOP3");
            aud.setQuestionType("不良报警");
            aud.setDecisionLevel("Level1");
            aud.setHandlingSug("全检风险批");
            aud.setResponsible(manName.toString());
            aud.setResponsibleId(manCode.toString());
            dfAuditDetailMapper.insert(aud);

            DfFlowData fd = new DfFlowData();
            fd.setFlowLevel(1);
            fd.setDataType(aud.getDataType());
            fd.setFlowType(aud.getDataType());
            fd.setName("工序" + processDefect.getStr1()+"_缺陷"+processDefect.getStr2()+"_尺寸缺陷连续两周不良TOP3_"+checkTime);
            fd.setDataId(aud.getId());
            fd.setStatus("待确定");

            fd.setNowLevelUser(aud.getResponsibleId());
            fd.setNowLevelUserName(aud.getResponsible());
            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
            fd.setFlowLevelName(aud.getDecisionLevel());

            QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
            atQw.eq("type","尺寸")
                    .last("limit 1");
            DfApprovalTime at= dfApprovalTimeMapper.selectOne(atQw);
            if(null!=at){
                if(fd.getFlowLevelName().equals("Level1")){
                    fd.setReadTimeMax(at.getReadTimeLevel1());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                }else  if(fd.getFlowLevelName().equals("Level2")){
                    fd.setReadTimeMax(at.getReadTimeLevel2());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                }else  if(fd.getFlowLevelName().equals("Level3")){
                    fd.setReadTimeMax(at.getReadTimeLevel3());
                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                }
            }
            //设置显示人
            fd.setShowApprover(fd.getNowLevelUserName());
            dfFlowDataMapper.insert(fd);
        }
    }

    @GetMapping("/updateWorkTime")
    public Result updateWorkTime() throws ParseException {

        System.out.println("更新加工时间");
        System.out.println(FindVbCodeAPI);
        HashMap<String, String> body = new HashMap<>();
        body.put("he", "hehe");
        body.put("hehe", "haha");
        Map<String, Object> result = getResponse(body);
        System.out.println(result);

        return new Result<>(200, "查询成功", FindVbCodeAPI);
    }

    public Map<String, Object> getResponse(HashMap<String, String> map){
        try {
            String urlString = FindVbCodeAPI; // 替换为实际URL
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法，默认是GET
            connection.setRequestMethod("POST");

            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json");

            // 允许向服务器发送数据
            connection.setDoOutput(true);

            // 创建要发送的 JSON 数据
            JSONObject jsonInput = new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                jsonInput.put(entry.getKey(), entry.getValue());
            }

            // 获取连接的输出流，并将 JSON 数据写入其中
            OutputStream os = connection.getOutputStream();
            os.write(jsonInput.toString().getBytes());
            os.flush();
            os.close();

            // 开始连接
            connection.connect();

            // 获取状态码
            int status = connection.getResponseCode();

            // 如果成功处理响应
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                // 读取服务器响应内容
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();

                // 假设响应内容存储在 content 变量中
                String jsonResponse = content.toString();
                System.out.println(jsonResponse);

                // 将 JSON 响应内容解析为 JSONObject
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // 将 JSONObject 转换为 Map
                Map<String, Object> resultMap = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    resultMap.put(key, jsonObject.get(key));
                }
                return resultMap;
            } else {
                System.out.println("GET request not worked");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Data
    @AllArgsConstructor
    class Rfid {
        private String productiveTime;
        private String barCode;
        private String machineCode;
    }


    @ApiOperation("导出尺寸、外观、机台良率、刀具寿命等信息汇总")
    @GetMapping("/exportSummary")
    public Result exportSummary(
            String process
            , String project
            , String color
            , @RequestParam String startDate
            , @RequestParam String endDate
            ,HttpServletResponse response, HttpServletRequest request
    ) throws ParseException, IOException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //尺寸数据
        List<Map> sizeList = new ArrayList<>();

        QueryWrapper<DfSizeContStand> sizeStandNameQw = new QueryWrapper<>();
        sizeStandNameQw
                .select("test_item")
                .eq(StringUtils.isNotEmpty(process),"process",process)
                .eq(StringUtils.isNotEmpty(project),"project",project)
                .eq(StringUtils.isNotEmpty(color),"color",color)
                .orderByAsc("sort");
        //尺寸名
        List<DfSizeContStand> sizeContStandList = dfSizeContStandService.list(sizeStandNameQw);

        //列名
        List<String> title = new ArrayList<>(Arrays.asList("明码"));
        sizeContStandList.forEach(item -> title.add(item.getTestItem()));

        List<String> titleFinal = new ArrayList<>(Arrays.asList(
                "Dim.(OK/NG)","IPQCDim.Yield","IPQCDimTime","Cos.(OK/NG)","Cos.NGDefect","IPQCCos.Yield","IPQCCosTime","Build","Factory#","Line#"
                ,"Process(CNC1/CNC2/CNC3)","加工Time","MachineID","FixtureIDRFID","机台寿命","是否同一玻璃复测","刀1寿命","刀2寿命","刀3寿命","刀4寿命"
                ,"有无换刀(0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)","有无调机(0=无调机；1=测量异常调机后首件)","有无换班(0=无换班；1=换班后首件)","有无换夹治具(0=无换夹治具；1=换夹治具后首件)"
        ));
        title.addAll(titleFinal);
        Map<String,String> emptyMap = new HashMap<>();
        for(String name:title){
            emptyMap.put(name,"");
        }

        QueryWrapper<Rate3> sizeMachineQw  = new QueryWrapper<>();
        sizeMachineQw
                .eq(StringUtils.isNotEmpty(process),"dsd.process",process)
                .eq(StringUtils.isNotEmpty(project),"dsd.project",project)
                .eq(StringUtils.isNotEmpty(color),"dsd.item_name",color)
                .and(wrapper ->wrapper
                        .between("dsd.work_time",startTime,endTime)
                        .or().between("dsd.test_time",startTime,endTime)
                )
                .ne("dsd.sn","");
        //尺寸机台OK数
        List<Rate3> sizeMachineOkNumList = dfSizeDetailService.getMachineOkNumList(sizeMachineQw);
        //尺寸机台良率
        Map<String,String> sizeMachineOkPointMap = new HashMap<>();
        for (Rate3 item:sizeMachineOkNumList){
            String machineCode = item.getStr1();
            Integer okNum = item.getInte1();
            Integer total = item.getInte2();
            String okPoint = String.format("%.2f",Double.valueOf(okNum)/total*100)+"%";
            sizeMachineOkPointMap.put(machineCode,okPoint);
        }

        QueryWrapper<Map<String,Object>> sizeDetailInfoQw = new QueryWrapper<>();
        sizeDetailInfoQw
                .eq(StringUtils.isNotEmpty(process),"dsd.process",process)
                .eq(StringUtils.isNotEmpty(project),"dsd.project",project)
                .eq(StringUtils.isNotEmpty(color),"dsd.item_name",color)
                .and(wrapper ->wrapper
                        .between("dsd.work_time",startTime,endTime)
                        .or().between("dsd.test_time",startTime,endTime)
                )
                .ne("dsd.sn","");
        //尺寸数据
        List<Map<String,Object>> sizeDetailInfoList = dfSizeDetailService.getSizeDetailInfoList(sizeDetailInfoQw);

        if(sizeDetailInfoList == null || sizeDetailInfoList.size()==0){
            String[] titleString = title.toArray(new String[0]);
            String[] enTitleString = title.toArray(new String[0]);
            ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
            excelExport2.setData(sizeList);
            excelExport2.setHeardKey(enTitleString);
            excelExport2.setFontSize(14);
            excelExport2.setHeardList(titleString);
            excelExport2.exportExport(request, response);
            return new Result(200, "当前查询条件没有相关数据");
        }

        //sn玻璃码
        List<String> snList = sizeDetailInfoList.stream().map(map -> String.valueOf(map.get("sn"))).distinct().collect(Collectors.toList());

        QueryWrapper<Map<String,Object>> qmsWaigInfoQw = new QueryWrapper<>();
        qmsWaigInfoQw.in("dqiwd.f_product_id",snList);
        //外观数据
        List<Map<String,Object>> qmsWaigInfoList = dfSizeDetailService.getQmsWaigInfoList(qmsWaigInfoQw);

        Map<String,List<Map<String,Object>>> qmsWaigColloct = qmsWaigInfoList.stream().collect(Collectors.groupingBy(item ->{
            return String.valueOf(item.get("f_product_id"))+","+String.valueOf(item.get("f_stage"))+","+String.valueOf(item.get("status"))
                    +","+String.valueOf(item.get("f_time"))+","+String.valueOf(item.get("f_line"));
        }));

        //外观数据
        Map<String,List<String>> qmsWaigInfoMap = new HashMap<>();
        for (Map.Entry<String,List<Map<String,Object>>> entry:qmsWaigColloct.entrySet()){
            String sn = entry.getKey().split(",")[0];
            String stage = entry.getKey().split(",")[1];
            String status = entry.getKey().split(",")[2];
            String checkTime = entry.getKey().split(",")[3];
            String line = entry.getKey().split(",")[4];
            checkTime = LocalDateTime.parse(checkTime).format(formatter);

            String defect = "/";

            if("NG".equals(status)) {
                Set<String> defectSet = new HashSet<>();
                for(Map<String,Object> map:entry.getValue()){
                    if (!"ok".equals(String.valueOf(map.get("f_result")))){
                        defectSet.add(String.valueOf(map.get("f_sort")));
                    }
                }
                defect = String.join(",",defectSet);
            }
            log.info("DfSizeDetailController exportSummary entry:"+entry);
            List<String> qmsWaigInfo = new ArrayList<>(Arrays.asList(stage,status,defect,checkTime,line));
            log.info("DfSizeDetailController exportSummary entry:"+entry+",qmsWaigInfo:"+qmsWaigInfo);
            qmsWaigInfoMap.put(sn,qmsWaigInfo);
        }

        QueryWrapper<Rate3> qmsWaigMachineQw = new QueryWrapper<>();
        qmsWaigMachineQw.in("dqiwd.f_product_id",snList);
        //外观机台OK数
        List<Rate3> qmsWaigMachineOkNumList = dfSizeDetailService.getQmsWaigMachineOkNumList(qmsWaigMachineQw);
        //外观机台良率
        Map<String,String> qmsWaigMachineOkPointMap = new HashMap<>();
        for (Rate3 item:qmsWaigMachineOkNumList){
            String machineCode = item.getStr1();
            Integer okNum = item.getInte1();
            Integer total = item.getInte2();
            String okPoint = String.format("%.2f",Double.valueOf(okNum)/total*100)+"%";
            qmsWaigMachineOkPointMap.put(machineCode,okPoint);
        }


        Map<String,List<Map<String,Object>>> sizeCollect = sizeDetailInfoList.stream().collect(Collectors.groupingBy(item ->{
            return String.valueOf(item.get("work_time"))+","+String.valueOf(item.get("machine_code"))+","+String.valueOf(item.get("result"))+","
                    +String.valueOf(item.get("factory"))+","+String.valueOf(item.get("process"))+","+String.valueOf(item.get("project"))+","
                    +String.valueOf(item.get("test_time"))+","+String.valueOf(item.get("item_name"))+","+String.valueOf(item.get("sn"))+","
                    +String.valueOf(item.get("fixture_id"))+","+String.valueOf(item.get("knife_first_life"))+","+String.valueOf(item.get("knife_second_life"))+","
                    +String.valueOf(item.get("knife_third_life"))+","+String.valueOf(item.get("knife_fourth_life"))+","+String.valueOf(item.get("change_knife_status"))+","
                    +String.valueOf(item.get("debug_status"))+","+String.valueOf(item.get("change_class_status"))+","+String.valueOf(item.get("change_clamp_status"))
                    +","+String.valueOf(item.get("machine_life"));
        }));


        for (Map.Entry<String,List<Map<String,Object>>> entry:sizeCollect.entrySet()){
            Map<String,String> sizeMap = new HashMap<>(emptyMap);
            String workTime = entry.getKey().split(",")[0];
            String machineCode = entry.getKey().split(",")[1];
            String result = entry.getKey().split(",")[2];
            String factoryName = entry.getKey().split(",")[3];
            String processName = entry.getKey().split(",")[4];
            String projectName = entry.getKey().split(",")[5];
            String testTime = entry.getKey().split(",")[6];
            testTime = LocalDateTime.parse(testTime).format(formatter);
            String itemName = entry.getKey().split(",")[7];
            String sn = entry.getKey().split(",")[8];
            String fixtureId = entry.getKey().split(",")[9];
            String knifeFirstLife = entry.getKey().split(",")[10];
            String knifeSecondLife = entry.getKey().split(",")[11];
            String knifeThirdLife = entry.getKey().split(",")[12];
            String knifeFourthLife = entry.getKey().split(",")[13];
            String changeKnifeStatus = entry.getKey().split(",")[14];
            String debugStatus = entry.getKey().split(",")[15];
            String changeClassStatus = entry.getKey().split(",")[16];
            String changeClampStatus = entry.getKey().split(",")[17];
            String machineLife = entry.getKey().split(",")[18];

            String sizeOkPoint = sizeMachineOkPointMap.get(machineCode);
            log.info("DfSizeDetailController exportSummary qmsWaigInfoMap:"+qmsWaigInfoMap+",sn:"+sn);

            String stage = "";
            String waigStatus = "";
            String waigDefect = "";
            String waigTestTime = "";
            String line = "";

            if(qmsWaigInfoMap.get(sn) != null){
                stage = qmsWaigInfoMap.get(sn).get(0);
                waigStatus = qmsWaigInfoMap.get(sn).get(1);
                waigDefect = qmsWaigInfoMap.get(sn).get(2);
                waigTestTime = qmsWaigInfoMap.get(sn).get(3);
                line = qmsWaigInfoMap.get(sn).get(4);
            }

            String waigOkPoint = qmsWaigMachineOkPointMap.get(machineCode);

            sizeMap.put("明码",!"null".equals(sn)?sn:"");
            sizeMap.put("Dim.(OK/NG)",!"null".equals(result)?result:"");
            sizeMap.put("IPQCDim.Yield",!"null".equals(sizeOkPoint)?sizeOkPoint:"");
            sizeMap.put("IPQCDimTime",!"null".equals(testTime)?testTime:"");
            sizeMap.put("Cos.(OK/NG)",!"null".equals(waigStatus)?waigStatus:"");
            sizeMap.put("Cos.NGDefect",!"null".equals(waigDefect)?waigDefect:"");
            sizeMap.put("IPQCCos.Yield",!"null".equals(waigOkPoint)?waigOkPoint:"");
            sizeMap.put("IPQCCosTime",!"null".equals(waigTestTime)?waigTestTime:"");
            sizeMap.put("Build",!"null".equals(stage)?stage:"");
            sizeMap.put("Factory#",!"null".equals(factoryName)?factoryName:"");
            sizeMap.put("Line#",!"null".equals(line)?line:"");
            sizeMap.put("Process(CNC1/CNC2/CNC3)",!"null".equals(processName)?processName:"");
            sizeMap.put("加工Time",!"null".equals(workTime)?workTime:"");
            sizeMap.put("MachineID",!"null".equals(machineCode)?machineCode:"");
            sizeMap.put("FixtureIDRFID",!"null".equals(fixtureId)?fixtureId:"");
            sizeMap.put("机台寿命",!"null".equals(machineLife)?machineLife:"");
            sizeMap.put("是否同一玻璃复测","0");
            sizeMap.put("刀1寿命",!"null".equals(knifeFirstLife)?knifeFirstLife:"");
            sizeMap.put("刀2寿命",!"null".equals(knifeSecondLife)?knifeSecondLife:"");
            sizeMap.put("刀3寿命",!"null".equals(knifeThirdLife)?knifeThirdLife:"");
            sizeMap.put("刀4寿命",!"null".equals(knifeFourthLife)?knifeFourthLife:"");
            sizeMap.put("有无换刀(0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)",!"null".equals(changeKnifeStatus)?changeKnifeStatus:"");
            sizeMap.put("有无调机(0=无调机；1=测量异常调机后首件)",!"null".equals(debugStatus)?debugStatus:"");
            sizeMap.put("有无换班(0=无换班；1=换班后首件)",!"null".equals(changeClassStatus)?changeClassStatus:"");
            sizeMap.put("有无换夹治具(0=无换夹治具；1=换夹治具后首件)",!"null".equals(changeClampStatus)?changeClampStatus:"");

            for (Map<String,Object> map:entry.getValue()){
                String defectName = String.valueOf(map.get("defect_name"));
//                String checkResult = String.valueOf(map.get("check_result"));
                String checkValue = String.valueOf(map.get("check_value"));

                sizeMap.put(defectName,!"null".equals(checkValue)?checkValue:"");
            }

            sizeList.add(sizeMap);
        }

        String[] titleString = title.toArray(new String[0]);
        String[] enTitleString = title.toArray(new String[0]);
        ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
        excelExport2.setData(sizeList);
        excelExport2.setHeardKey(enTitleString);
        excelExport2.setFontSize(14);
        excelExport2.setHeardList(titleString);
        excelExport2.exportExport(request, response);
        return new Result(200, "操作成功");
    }

    @ApiOperation("导出外观动态IPQC数据汇总")
    @GetMapping("/exportWGDTIPQC")
    public Result exportWGDTIPQC(
            String process
            , String project
            , String color
            , String type
            , @RequestParam String startDate
            , @RequestParam String endDate
            ,HttpServletResponse response, HttpServletRequest request
    ) throws ParseException, IOException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //数据
        List<Map> sizeList = new ArrayList<>();

        //列名
        List<String> title = new ArrayList<>(Arrays.asList("明码"));

        List<String> titleFinal = new ArrayList<>(Arrays.asList(
               "Cos.(OK/NG)","Cos.NGDefect","IPQCCos.Yield","IPQCCosTime","Build","Factory#","Line#"
                ,"Process(CNC2)","加工Time","MachineID" ,"检查类型","是否同一玻璃复测","是否触发了动态规则","哪条动态规则(1h、2h、4h)"

        ));
        title.addAll(titleFinal);
        Map<String,String> emptyMap = new HashMap<>();
        for(String name:title){
            emptyMap.put(name,"");
        }



        QueryWrapper<Map<String,Object>> sizeDetailInfoQw = new QueryWrapper<>();
        sizeDetailInfoQw
                .eq(StringUtils.isNotEmpty(process),"tol.f_seq",process)
                .eq(StringUtils.isNotEmpty(project),"tol.f_bigpro",project)
                .eq(StringUtils.isNotEmpty(color),"tol.f_color",color)
                .and(wrapper ->wrapper
                        .between("de.f_time",startTime,endTime)

                )
                .isNotNull("de.f_product_id");


        //外观数据
        List<Map<String,Object>> sizeDetailInfoList = dfQmsIpqcWaigTotalService.getWGDetailInfoList(sizeDetailInfoQw);

        if(sizeDetailInfoList == null || sizeDetailInfoList.size()==0){
            String[] titleString = title.toArray(new String[0]);
            String[] enTitleString = title.toArray(new String[0]);
            ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
            excelExport2.setData(sizeList);
            excelExport2.setHeardKey(enTitleString);
            excelExport2.setFontSize(14);
            excelExport2.setHeardList(titleString);
            excelExport2.exportExport(request, response);
            return new Result(200, "当前查询条件没有相关数据");
        }






        Map<String,List<Map<String,Object>>> qmsWaigColloct = sizeDetailInfoList.stream().collect(Collectors.groupingBy(item ->{
            return String.valueOf(item.get("f_product_id"))+","+String.valueOf(item.get("f_stage"))
                    +","+String.valueOf(item.get("f_time"))+","+String.valueOf(item.get("f_line"))
                    +","+String.valueOf(item.get("f_sort"))
                    +","+String.valueOf(item.get("product_time"))
                    +","+String.valueOf(item.get("handling_sug"))
                    +","+String.valueOf(item.get("f_seq"))
                    +","+String.valueOf(item.get("f_fac"))
                    +","+String.valueOf(item.get("f_mac"))
                    +","+String.valueOf(item.get("f_test_category"))

                    ;
        }));
        //sn玻璃码
        List<String> snList = sizeDetailInfoList.stream().map(map -> String.valueOf(map.get("f_product_id"))).distinct().collect(Collectors.toList());

        QueryWrapper<Rate3> qmsWaigMachineQw = new QueryWrapper<>();
        qmsWaigMachineQw.in("dqiwd.f_product_id",snList);
        //外观机台OK数
        List<Rate3> qmsWaigMachineOkNumList = dfSizeDetailService.getQmsWaigMachineOkNumList(qmsWaigMachineQw);
        //外观机台良率
        Map<String,String> qmsWaigMachineOkPointMap = new HashMap<>();
        for (Rate3 item:qmsWaigMachineOkNumList){
            String machineCode = item.getStr1();
            Integer okNum = item.getInte1();
            Integer total = item.getInte2();
            String okPoint = String.format("%.2f",Double.valueOf(okNum)/total*100)+"%";
            qmsWaigMachineOkPointMap.put(machineCode,okPoint);
        }

        //外观数据
        Map<String,List<String>> qmsWaigInfoMap = new HashMap<>();
        for (Map.Entry<String,List<Map<String,Object>>> entry:qmsWaigColloct.entrySet()){
            Map<String,String> sizeMap = new HashMap<>(emptyMap);

            String sn = entry.getKey().split(",")[0];
            String stage = entry.getKey().split(",")[1];
            String checkTime = entry.getKey().split(",")[2];
            String line = entry.getKey().split(",")[3];
            checkTime = LocalDateTime.parse(checkTime).format(formatter);
            String sort = entry.getKey().split(",")[4];
            String productTime = entry.getKey().split(",")[5];
            String handlingsug = entry.getKey().split(",")[6];
            String fSeq = entry.getKey().split(",")[7];
            String fFac = entry.getKey().split(",")[8];
            String fMac = entry.getKey().split(",")[9];
            String fTestCategory = entry.getKey().split(",")[10];
            String waigOkPoint = qmsWaigMachineOkPointMap.get(fMac);


            sizeMap.put("明码",!"null".equals(sn)?sn:"");
            sizeMap.put("Cos.(OK/NG)",!"null".equals(sort)?"NG":"OK");
            sizeMap.put("Cos.NGDefect",!"null".equals(sort)?sort:"");
            sizeMap.put("IPQCCos.Yield",!"null".equals(waigOkPoint)?waigOkPoint:"");
            sizeMap.put("IPQCCosTime",!"null".equals(checkTime)?checkTime:"");
            sizeMap.put("Build",!"null".equals(stage)?stage:"");
            sizeMap.put("Factory#",!"null".equals(fFac)?fFac:"");
            sizeMap.put("Line#",!"null".equals(line)?line:"");
            sizeMap.put("Process(CNC2)",!"null".equals(fSeq)?fSeq:"");
            sizeMap.put("加工Time",!"null".equals(productTime)?productTime:"");
            sizeMap.put("MachineID",!"null".equals(fMac)?fMac:"");
            sizeMap.put("检查类型",!"null".equals(fTestCategory)?fTestCategory:"");
            sizeMap.put("是否同一玻璃复测","0");
            sizeMap.put("是否触发了动态规则",!"null".equals(handlingsug)?"是":"否");
            sizeMap.put("哪条动态规则(1h、2h、4h)",!"null".equals(handlingsug)?handlingsug:"0");

            sizeList.add(sizeMap);

        }

        String[] titleString = title.toArray(new String[0]);
        String[] enTitleString = title.toArray(new String[0]);
        ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
        excelExport2.setData(sizeList);
        excelExport2.setHeardKey(enTitleString);
        excelExport2.setFontSize(14);
        excelExport2.setHeardList(titleString);
        excelExport2.exportExport(request, response);
        return new Result(200, "操作成功");
    }




    @PostMapping(value = "/uploadExcel")
    @ApiOperation("嘉泰数据补录")
    @ResponseBody
    public Object uploadExcel(@RequestParam(value = "color", required = false) String  color, @RequestParam(value = "project", required = false) String  project,@RequestParam(value = "process", required = false) String  process, @RequestParam(value = "file", required = false) MultipartFile file,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
//		String id2 = request.getParameter("id");// 获取data中数据
//		System.out.println(id);
            if (file != null) {
                // 获取文件名
                String fileName = file.getOriginalFilename();
                map.put("code", 0);
                System.out.println(fileName);
            } else {
                map.put("code", 1);
            }

            if (file.isEmpty()) {
                return new Result(0, "上传失败，请选择文件");
            }

//	        System.out.println(id);
//	        ImgManager img = new ImgManager();
//	        img.setArticleId(id);
            System.out.println("开始上传");
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);

            if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
                return new Result(1, "请上传xlsx或xls格式的文件");
            }
            InputStream ins = null;
            try {
                ins = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File fl = new File(env.getProperty("uploadPath") + "excel/");
            if (!fl.exists()) {
                fl.mkdirs();
            }
            File f = new File(env.getProperty("uploadPath") + "excel/" + file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                System.out.println(i);
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = new File(env.getProperty("uploadPath") + "excel/" + fileName);
                i++;
            }

            CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "excel",
                    env.getProperty("uploadPath") + "excel/");

            try {
                ImportExcelResult ter = DfSizeCheckItemInfosService.importOrder( file,project,process,color);
                return new Result(0, "上传成功", ter);
            } catch (Exception e) {

                e.printStackTrace();
            }

            return new Result(500, "上传失败");

        } catch (Exception e) {
//            logger.error("导入excel接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    @PostMapping("/importData")
    @ApiOperation("导入数据")
    @Transactional(rollbackFor = Exception.class)
    public Result importData(
            MultipartFile file,
            String factory,
            String project,
            String color,
            String process,
            Integer timeCol,
            Integer defectRow,
            Integer machineCol,
            Integer startRow,
            Integer startCol,
            Integer endCol
    ) throws Exception {
        timeCol--;
        defectRow--;
        machineCol--;
        startRow--;

        QueryWrapper<DfProcess> processQw = new QueryWrapper<>();
        processQw
                .eq("factory_code",factory)
                .like("project", project)
                .eq("process_name", process);

        DfProcess processData = DfProcessService.getOne(processQw);
        if (processData == null || processData.getFirstCode() == null){
            return new Result(500,"未找到对应工序");
        }

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);

        String[][] strings = excel.readExcelBlockSheet(1, -1, 1, endCol,0);

        Integer saveNum = 0;
        for (int i = startRow - 1; i < strings.length; i++){
            if (StringUtils.isEmpty(strings[i][timeCol]) || StringUtils.isEmpty(strings[i][machineCol])){
                continue;
            }

            //机台
            String machineCode = strings[i][machineCol];
            if (machineCode.contains("#")){
                machineCode = String.format("%s%03d",processData.getFirstCode(),Integer.parseInt(machineCode.split("#")[0]));
            }else if (machineCode.contains("-")){
                machineCode = String.format("%s%03d",processData.getFirstCode(),Integer.parseInt(machineCode.split("-")[0]));
            }

            if (!machineCode.contains(processData.getFirstCode())){
                machineCode = String.format("%s%03d",processData.getFirstCode(),Integer.parseInt(machineCode));
            }

            //测试时间
            Date checkTimeDate = sd.parse(strings[i][timeCol]);
            Timestamp checkTime = new Timestamp(checkTimeDate.getTime());
            //结果
            String result = "OK";

            List<DfSizeCheckItemInfos> checkItemInfosList = new ArrayList<>();
            for (int j = startCol - 1; j < endCol; j++){
                if (StringUtils.isEmpty(strings[1][j])){
                    continue;
                }

                //不良项
                String ngItem = strings[defectRow][j];
                if (!InitializeCheckRule.sizeContStand.containsKey(project + color + process + ngItem)){
                    continue;
                }

                //标准
                DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + ngItem);
                //测试值
                Double checkValue = Double.valueOf(strings[i][j]);
                //标准值
                Double standard = standardData.getStandard();
                //隔离上限
                Double usl = standardData.getIsolaUpperLimit();
                //隔离下限
                Double lsl = standardData.getIsolaLowerLimit();
                //内控上限
                Double controlUpperLimit = standardData.getUpperLimit();
                //内控下限
                Double controlLowerLimit = standardData.getLowerLimit();
                //是否重点
                String keyPoint = standardData.getKeyPoint();
                //测试结果
                String checkResult = "OK";
                //不良情况
                String badCondition = "";

//                if (checkValue > usl){
//                    checkResult = "NG";
//                    badCondition = ngItem + "偏大";
//                    result = "NG";
//                }else if (checkValue < lsl){
//                    checkResult = "NG";
//                    badCondition = ngItem + "偏小";
//                    result = "NG";
//                }

                if (checkValue > usl || checkValue < lsl){
                    continue;
                }

                DfSizeCheckItemInfos dfSizeCheckItemInfos = new DfSizeCheckItemInfos();
                dfSizeCheckItemInfos.setCheckResult(checkResult);
                dfSizeCheckItemInfos.setCheckTime(checkTime.toString());
                dfSizeCheckItemInfos.setCheckValue(checkValue);
                dfSizeCheckItemInfos.setControlLowerLimit(controlLowerLimit);
                dfSizeCheckItemInfos.setControlUpperLimit(controlUpperLimit);
                dfSizeCheckItemInfos.setItemName(ngItem);
                dfSizeCheckItemInfos.setLsl(lsl);
                dfSizeCheckItemInfos.setStandardValue(standard);
                dfSizeCheckItemInfos.setUsl(usl);
                dfSizeCheckItemInfos.setBadCondition(badCondition);
                dfSizeCheckItemInfos.setKeyPoint(keyPoint);

                checkItemInfosList.add(dfSizeCheckItemInfos);
            }

            if (checkItemInfosList.size() == 0){
                continue;
            }
            DfSizeDetail dfSizeDetail = new DfSizeDetail();
            dfSizeDetail.setMachineCode(machineCode);
            dfSizeDetail.setRemarks(color);
            dfSizeDetail.setResult(result);
            dfSizeDetail.setTestTime(checkTime);
            dfSizeDetail.setFactory(factory);
            dfSizeDetail.setProject(project);
            dfSizeDetail.setProcess(process);
            dfSizeDetail.setItemName(color);
            dfSizeDetail.setCheckType("1");
            dfSizeDetail.setInfoResult("OK");

            dfSizeDetailService.save(dfSizeDetail);
            Integer sizeDetailId = dfSizeDetail.getId();

            checkItemInfosList.forEach(item -> {
                item.setCheckId(sizeDetailId.toString());
            });
            DfSizeCheckItemInfosService.saveBatch(checkItemInfosList);

            saveNum++;
        }

        return new Result(200, MessageFormat.format("成功添加尺寸数据{0}条", saveNum));
    }


    @ApiOperation("PDA返回风险品状态")
    @PostMapping("/uploadIntercept")
    public Result uploadIntercept(DfSizeDetail data) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object v = valueOperations.get("FP"+data.getMachineCode());
        if(null!=v){
            SizeQueueData fdDatas = new Gson().fromJson(v.toString(), SizeQueueData.class);
            if(null!=fdDatas){
                    redisTemplate.delete("FP"+data.getMachineCode());
                    if(data.getResult().equals("NG")){
                        try {
                            ServerInitializeUtil.dfSizeDetailService.saveMqData(v.toString(),"foolProofig");
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }



            }
        }

        return new Result(200, "操作成功");
    }

    @ApiOperation("更新尺寸良率中间表")
    @PostMapping("/sizeOkRateUpdateByDate")
    public Result sizeOkRateUpdate(String Date) throws ParseException {

        String startTime = Date + " 07:00:01";
        String endTime = TimeUtil.getNextDay(Date) + " 07:00:00";
        System.out.println("======================尺寸看板良率数据汇总开始 " + TimeUtil.getNowTimeByNormal() + "========================");
        QueryWrapper<DfSizeOkRate> qw = new QueryWrapper<>();
        qw.between("test_time", startTime, endTime);

        List<DfSizeOkRate> dfSizeOkRates = dfSizeOkRateService.listSizeOkRate(qw);

        QueryWrapper<DfSizeOkRate> deleteQw = new QueryWrapper<>();
        deleteQw.between("create_time", startTime, endTime);
        dfSizeOkRateService.remove(deleteQw);  // 删除该天的数据
        if (dfSizeOkRates.size() > 0) {
            for (DfSizeOkRate dfSizeOkRate : dfSizeOkRates) {
                dfSizeOkRate.setCreateTime(Timestamp.valueOf(startTime));
            }
            dfSizeOkRateService.saveBatch(dfSizeOkRates);   // 添加该天的数据
        }

        System.out.println("======================尺寸看板良率数据汇总结束 " + TimeUtil.getNowTimeByNormal() + "========================");
        return Result.UPDATE_SUCCESS;
    }
}

