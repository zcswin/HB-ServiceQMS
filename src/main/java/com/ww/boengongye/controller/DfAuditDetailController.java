package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.boengongye.utils.ExcelExportUtil2.distinctByKey;

/**
 * <p>
 * 稽核NG详细 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-22
 */
@Controller
@RequestMapping("/dfAuditDetail")
@ResponseBody
@CrossOrigin
@Api(tags = "审批单")
public class DfAuditDetailController {
    @Autowired
    com.ww.boengongye.service.DfAuditDetailService DfAuditDetailService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataService DfFlowDataService;

    @Autowired
    com.ww.boengongye.service.DfSizeDetailService DfSizeDetailService;

    @Autowired
    com.ww.boengongye.service.DfFlowOpinionService dfFlowOpinionService;


    @Autowired
    com.ww.boengongye.service.DfFlowDataOvertimeService DfFlowDataOvertimeService;

    @Autowired
    DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    DfProcessService dfProcessService;

    @Autowired
    private DfControlStandardStatusService dfControlStandardStatusService;

    @Autowired
    com.ww.boengongye.service.DfMachineService dfMachineService;

    @Autowired
    private DfLiableManService dfLiableManService;

    @Autowired
    private DfApprovalTimeService dfApprovalTimeService;
    @Autowired
    private UserService userService;

    @Value("${config.factoryId}")
    private String fac;
    @Autowired
    private Environment env;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private DfScadaLockMacDataService dfScadaLockMacDataService;

    @Autowired
    @Qualifier("defaultJmsTemplate")
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    @Qualifier(value = "topic3")
    private Destination topic;


    private static final Logger logger = LoggerFactory.getLogger(DfAuditDetailController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfAuditDetailService.list());
    }

    @RequestMapping(value = "/getById")
    public Object getById(int id) {
        DfAuditDetail data = DfAuditDetailService.getById(id);
        if (data.getDataType().equals("设备状态")) {
            QueryWrapper<DfMachine> qw = new QueryWrapper<>();
            qw.eq("code", data.getMacCode());
            qw.last("limit 1");
            DfMachine mac = dfMachineService.getOne(qw);
            if (null != mac) {
                data.setProcessCode(mac.getProcessCode());
                data.setMacName(mac.getName());
                data.setPersonName(mac.getPersonName());
                data.setDevIp(mac.getDevIp());
                data.setPersonCode(mac.getPersonCode());

            }
        }
        return new Result(200, "查询成功", data);
    }

    @ApiOperation("问题点超时回复及关闭情况")
    @GetMapping(value = "/listByProcess")
    public Object listByProcess(String factory,String startTime, String endTime,String process,
                                String dataType,String line,String project,String classes){
        QueryWrapper<DfAuditDetail> qw=new QueryWrapper<>();
        if (null != factory && !factory.equals("")) {
            qw.eq("ad.factory", factory);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
        if (null != line && !line.equals("")) {
            qw.eq("ad.line", line);
        }
        if (null != project && !project.equals("") && !project.equals("undefined")) {
            qw.eq("ad.project_name", project);
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != classes && !classes.equals("")&&classes.equals("A班")){
            qw.apply("HOUR(ad.report_time) >= 7 AND HOUR(ad.report_time) < 19");
        }else if (null != classes && !classes.equals("")&&classes.equals("B班")){
            qw.apply("(HOUR(ad.report_time) >= 19 OR (HOUR(ad.report_time) < 7 AND DATE(ad.report_time) = DATE(NOW())))");
        }
        List<DfAuditDetail> list = DfAuditDetailService.listByProcess(qw);
        for (DfAuditDetail dfAuditDetail : list) {
            dfAuditDetail.setTimeOut((double) (dfAuditDetail.getQuestionNum()-dfAuditDetail.getRespondPromptly()));
        }
        return new Result(200, "查询成功", list);
    }
    @ApiOperation("问题点关闭率明细")
    @GetMapping(value = "/listByCloseDetail")
    public Object listByProcess(String process, String dataType,String classes,String startTime,String endTime){
        QueryWrapper<DfAuditDetail> qw=new QueryWrapper<>();

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != classes && !classes.equals("")&&classes.equals("A班")){
            qw.apply("HOUR(ad.report_time) >= 7 AND HOUR(ad.report_time) < 19");
        }else if (null != classes && !classes.equals("")&&classes.equals("B班")){
            qw.apply("(HOUR(ad.report_time) >= 19 OR (HOUR(ad.report_time) < 7 AND DATE(ad.report_time) = DATE(NOW())))");
        }
        List<DfAuditDetail> list = DfAuditDetailService.listByProcessHaveQuestionType(qw);
        for (DfAuditDetail dfAuditDetail : list) {
            if (dataType==null){
                dfAuditDetail.setDataType("全部");
            }else {
                dfAuditDetail.setDataType(dataType);
            }
            if(classes==null){
                dfAuditDetail.setClasses("全天");
            }else {
                dfAuditDetail.setClasses(classes);
            }
            dfAuditDetail.setTimeOut((double) (dfAuditDetail.getQuestionNum()-dfAuditDetail.getRespondPromptly()));
            dfAuditDetail.setUnclosed((double) (dfAuditDetail.getQuestionNum()-dfAuditDetail.getCloseNum()));
        }
        return new Result(200, "查询成功", list);
    }
    @GetMapping("/listByBigScreen")
    public Object listByBigScreen(String type, String startTime, String endTime, String factory,
                                  String line, String project, String workshop, String workstation, String process,
                                  String dataType, String questionName, String floor) {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != type && !type.equals("")) {
            qw.eq("ad.data_type", type);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
        if (null != factory && !factory.equals("") && !factory.equals("NaN")) {
            qw.eq("ad.factory", factory);
        }
        if (null != line && !line.equals("")) {
            qw.eq("ad.line", line);
        }
        if (null != project && !project.equals("") && !project.equals("undefined")) {
            qw.eq("ad.project_name", project);
        }
        if (null != workshop && !workshop.equals("")) {
            qw.eq("ad.workshop", workshop);
        }
        if (null != workstation && !workstation.equals("")) {
            qw.eq("ad.workstation", workstation);
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != questionName && !questionName.equals("")) {
            qw.and(wrapper -> wrapper
                    .like("question_name", questionName)
                    .or().like("scene_practical", questionName));
        }
        qw.eq("dp.floor", floor);
        qw.isNull("end_time");
        qw.ne("ad.data_type", "设备状态");
        qw.ne("ad.data_type", "动态IPQC");
        qw.ne("ad.data_type", "风险隔离全检");
        qw.orderByDesc("ad.report_time");

        qw.last("limit 1000");

        List<DfAuditDetail> list = DfAuditDetailService.listByBigScreen(qw);
        return new Result(200, "查询成功", list);
    }

    @GetMapping("/listByBigScreenClose")
    public Object listByBigScreenClose(String type, String startTime, String endTime, String factory,
                                       String line, String project, String workshop, String workstation, String process,
                                       String dataType, String questionName, String floor) {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != type && !type.equals("")) {
            qw.eq("ad.data_type", type);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
        if (null != factory && !factory.equals("") && !factory.equals("NaN")) {
            qw.eq("ad.factory", factory);
        }
        if (null != line && !line.equals("")) {
            qw.eq("ad.line", line);
        }
        if (null != project && !project.equals("") && !project.equals("undefined")) {
            qw.eq("ad.project_name", project);
        }
        if (null != workshop && !workshop.equals("")) {
            qw.eq("ad.workshop", workshop);
        }
        if (null != workstation && !workstation.equals("")) {
            qw.eq("ad.workstation", workstation);
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != questionName && !questionName.equals("")) {
            qw.and(wrapper -> wrapper
                    .like("question_name", questionName)
                    .or().like("scene_practical", questionName));
        }
        qw.eq("dp.floor", floor);
        qw.isNotNull("end_time");
        qw.ne("ad.data_type", "设备状态");
        qw.ne("ad.data_type", "动态IPQC");
        qw.ne("ad.data_type", "风险隔离全检");
        qw.orderByDesc("ad.report_time");

        List<DfAuditDetail> list = DfAuditDetailService.listByBigScreen(qw);

        return new Result(200, "查询成功", list);
    }

    @ApiOperation("项目分类点击事件")
    @GetMapping("/listByProjectClass")
    public Result listByProjectClass(String project, String projectClass, String process, String startDate, String endDate, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("ad.report_time", startDate, endDate + " 23:59:59");
        }
        qw
                .eq(null != process && !"".equals(process), "ad.process", process)
                .eq(StringUtils.isNotEmpty(project), "ad.project", project)
                .eq(null != projectClass, "ad.data_type", projectClass)
                .eq("dp.floor", floor)
                .orderByDesc("ad.report_time");
        List<DfAuditDetail> list = DfAuditDetailService.listByBigScreen(qw);
        return new Result(200, "查询成功", list);

    }

    @ApiOperation("问题类别点击事件")
    @GetMapping("/listByQuestionType")
    public Result listByQuestionType(String project, String questionType, String process, String startDate, String endDate, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("ad.report_time", startDate, endDate + " 23:59:59");
        }
        qw
                .eq(StringUtils.isNotEmpty(process), "ad.process", process)
                .eq(StringUtils.isNotEmpty(project), "ad.project_name", project)
                .eq(StringUtils.isNotEmpty(questionType), "ad.question_type", questionType)
                .eq("dp.floor", floor).orderByDesc("ad.report_time");
        List<DfAuditDetail> list = DfAuditDetailService.listByBigScreen(qw);
        return new Result(200, "查询成功", list);

    }

    //超时已处理
    @ApiOperation("超时已处理列表")
    @GetMapping(value = "/listByBigScreenOverTimeClose")
    public Object listByBigScreenOverTimeClose(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw
                .eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(project), "ad.project_name", project)
                .ne("ad.data_type", "设备状态")
                .ne("ad.data_type", "动态IPQC")
                .ne("ad.data_type", "风险隔离全检")
                .isNotNull("ad.end_time")
                .isNotNull("fd.start_timeout")
                .eq(StringUtils.isNotEmpty(dataType), "ad.data_type", dataType)
                .eq("dp.floor", floor)
                .orderByDesc("ad.create_time");

        return new Result(200, "查询成功", DfAuditDetailService.listByBigScreen(qw));
    }

    //超时未处理
    @ApiOperation("超时未处理列表")
    @GetMapping(value = "/listByBigScreenOverTimeNotClose")
    public Object listByBigScreenOverTimeNotClose(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }

        qw
                .eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(project), "ad.project_name", project)
                .ne("ad.data_type", "设备状态")
                .ne("ad.data_type", "动态IPQC")
                .ne("ad.data_type", "风险隔离全检")
                .isNull("ad.end_time")
                .isNotNull("fd.start_timeout")
                .eq(StringUtils.isNotEmpty(dataType), "ad.data_type", dataType)
                .eq("dp.floor", floor)
                .orderByDesc("ad.create_time");

        return new Result(200, "查询成功", DfAuditDetailService.listByBigScreen(qw));
    }

    //未超时处理
    @ApiOperation("未超时处理列表")
    @GetMapping(value = "/listByBigScreenNotOverTimeClose")
    public Object listByBigScreenNotOverTimeClose(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw
                .eq(StringUtils.isNotEmpty(process), "ad.process", process)
                .eq(StringUtils.isNotEmpty(project), "ad.project_name", project)
                .ne("ad.data_type", "设备状态")
                .ne("ad.data_type", "动态IPQC")
                .ne("ad.data_type", "风险隔离全检")
                .isNotNull("ad.end_time")
                .isNull("fd.start_timeout")
                .eq(StringUtils.isNotEmpty(dataType), "ad.data_type", dataType)
                .eq("dp.floor", floor)
                .orderByDesc("ad.create_time");

        return new Result(200, "查询成功", DfAuditDetailService.listByBigScreen(qw));
    }

    @RequestMapping(value = "/getByParentId")
    public Object getByParentId(int id, String type) {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        qw.eq("parent_id", id);
        qw.eq("data_type", type);
        qw.last("limit 0,1");
        return new Result(0, "查询成功", DfAuditDetailService.getOne(qw));
    }


    @RequestMapping(value = "/sendAgain", method = RequestMethod.POST)
    public Result sendAgain(@RequestBody DfAuditDetail datas) {
        System.out.println(datas);
        if (DfAuditDetailService.updateById(datas)) {
            QueryWrapper<DfFlowData> qw = new QueryWrapper<>();
            qw.eq("data_id", datas.getId());
            qw.eq("data_type", datas.getDataType());
            qw.last("limit 1");
            DfFlowData fd = DfFlowDataService.getOne(qw);
            fd.setStatus("待审批");
            if (datas.getDecisionLevel().equals("Level1")) {
                if (null != datas.getResponsibleId2()) {
                    fd.setNextLevelUser(datas.getResponsibleId2() + "");
                }
                fd.setNowLevelUser(datas.getResponsibleId());
                fd.setNowLevelUserName(datas.getResponsible());
                fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            } else if (datas.getDecisionLevel().equals("Level2")) {
                if (null != datas.getResponsibleId3()) {
                    fd.setNextLevelUser(datas.getResponsibleId3() + "");
                }
                fd.setNowLevelUser(datas.getResponsibleId2());
                fd.setNowLevelUserName(datas.getResponsible2());
                fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            } else {
                fd.setNowLevelUser(datas.getResponsibleId3());
                fd.setNowLevelUserName(datas.getResponsible3());
                fd.setLevel3PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            }
            fd.setFlowLevelName(datas.getDecisionLevel());


            DfFlowDataService.updateById(fd);

            DfFlowOpinion dfo = new DfFlowOpinion();
            dfo.setOpinion("发起人重新提交审批");
            dfo.setSender(datas.getCreateName());
            dfo.setSenderId(datas.getCreateUserId());
            dfo.setFlowDataId(fd.getId());
            dfFlowOpinionService.save(dfo);
            return new Result(200, "保存成功");
        } else {
            return new Result(500, "保存失败");
        }

    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfAuditDetail datas, String status) {

        if (null != datas.getId()) {
            if (null != status) {
                //处理时间防呆,end_time不能早于report_time
                if (StringUtils.isEmpty(datas.getReportTime().toString()) || datas.getReportTime().compareTo(Timestamp.valueOf(TimeUtil.getNowTimeByNormal())) >= 0) {
                    datas.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                }
                datas.setEndTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

            }

            DfAuditDetail already=DfAuditDetailService.getById(datas.getId());
            int ngPhase=0;
            //判定尺寸任务单为NG阶段为4的时候回复FACA自动解锁机台
            //判定尺寸任务单为NG阶段为3的时候回复FACA自动解锁机台 跳过临近一片后改成3
            if (null != already && already.getDataType().equals("尺寸") && env.getProperty("sizeFullCheckProcess").contains(already.getProcess()) && already.getNgPhase().equals("1")) {
                ValueOperations valueOperations = redisTemplate.opsForValue();
                //获取NG阶段
                SizeQueueData phaseData = (SizeQueueData) valueOperations.get("sizePhase:" + already.getnNumTool());
                if (null != phaseData && null != phaseData.getNgPhase()) {
                    ngPhase=Integer.parseInt(phaseData.getNgPhase());
                    if (Integer.parseInt(phaseData.getNgPhase()) < 3) {
                        return new Result(500, "任务单流程未到可手动处理节点");
                    }
                }
            }
            if (DfAuditDetailService.updateById(datas)) {

                if(ngPhase>=3){
                    ObjectMapper mapper=new ObjectMapper();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Type_Data", 32);
                    data.put("MachineCode", already.getMacCode());
                    data.put("nType_CMD_Src", 4);
                    data.put("nIndex_CH", 0);
//                    if (status.equals("锁机")) {
//                        data.put("MsgLevel", 444446);
//                    } else {
                    data.put("MsgLevel", 544446);
//                    }
                    data.put("MsgTitle", "");
                    data.put("MsgTxt", "");
                    data.put("nResult", 0);
                    data.put("CmdCRCKey", "");
                    data.put("pub_time", System.currentTimeMillis() / 1000);

                    String logData = JSON.toJSONString(data);
                    DfScadaLockMacData dfScadaLockMacData = new DfScadaLockMacData();
                    dfScadaLockMacData.setMachineCode(already.getMacCode());
                    dfScadaLockMacData.setMacStatus("解锁");
                    dfScadaLockMacData.setLogData(logData);
                    dfScadaLockMacDataService.save(dfScadaLockMacData);

                    try {
                        jmsMessagingTemplate.convertAndSend(topic, mapper.writeValueAsString(data));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                }

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfAuditDetailService.save(datas)) {
                DfFlowData fd = new DfFlowData();
                fd.setFlowLevel(1);
                fd.setDataType(datas.getDataType());
                fd.setFlowType(datas.getDataType());
                fd.setName("IPQC_点检_" + datas.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
                fd.setDataId(datas.getId());
//                    fd.setFlowLevelName(fb.getName());
                fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
                fd.setCreateName(datas.getCreateName());
                fd.setCreateUserId(datas.getCreateUserId());
                if (datas.getDecisionLevel().equals("Level1")) {
                    fd.setNextLevelUser(datas.getResponsibleId2() + "");
                    fd.setNowLevelUser(datas.getResponsibleId());
                    fd.setNowLevelUserName(datas.getResponsible());
                    fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                } else if (datas.getDecisionLevel().equals("Level2")) {
                    fd.setNextLevelUser(datas.getResponsibleId3() + "");
                    fd.setNowLevelUser(datas.getResponsibleId2());
                    fd.setNowLevelUserName(datas.getResponsible2());
                    fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                } else {
                    fd.setNowLevelUser(datas.getResponsibleId3());
                    fd.setNowLevelUserName(datas.getResponsible3());
                    fd.setLevel3PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                }
                fd.setFlowLevelName(datas.getDecisionLevel());

                //设置显示人
                fd.setShowApprover(fd.getNowLevelUserName());

                DfFlowDataService.save(fd);

                if (datas.getDataType().equals("尺寸")) {
                    DfSizeDetail dd = new DfSizeDetail();
                    dd.setHaveJob("YES");
                    dd.setId(datas.getParentId());
                    DfSizeDetailService.updateById(dd);
                }

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }



    // 根据id删除
    @GetMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfAuditDetailService.removeById(id)) {
            QueryWrapper<DfFlowData> qw3 = new QueryWrapper<>();
            qw3.eq("data_id", id);
            qw3.last("limit 1");
            DfFlowData fd = DfFlowDataService.getOne(qw3);
            if (null != fd) {
                QueryWrapper<DfFlowDataOvertime> qw4 = new QueryWrapper<>();
                qw4.eq("flow_data_id", fd.getId());
                DfFlowDataOvertimeService.remove(qw4);
                QueryWrapper<DfFlowOpinion> qw5 = new QueryWrapper<>();
                qw5.eq("flow_data_id", fd.getId());
                dfFlowOpinionService.remove(qw5);
            }
            DfFlowDataService.remove(qw3);
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String name, String factoryId, String lineBodyId) {
//        try {
        Page<DfAuditDetail> pages = new Page<DfAuditDetail>(page, limit);
        QueryWrapper<DfAuditDetail> ew = new QueryWrapper<DfAuditDetail>();
        if (null != name && !name.equals("")) {
            ew.like("p.name", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f.id", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("l.id", lineBodyId);
        }


        ew.orderByDesc("p.create_time");
        IPage<DfAuditDetail> list = DfAuditDetailService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = DfAuditDetailService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    @PostMapping("/upload2")
    public Result upload2(MultipartFile file) throws Exception {
        int count = DfAuditDetailService.importExcel2(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    @ApiOperation("问题关闭率统计")
    @GetMapping("/getEndNum")
    public Result getEndNum(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw.ne("data_type", "设备状态");
        qw.ne("data_type", "动态IPQC");
        qw.ne("data_type", "风险隔离全检");
        if (null != dataType && !dataType.equals("")) {
            qw.eq("data_type", dataType);
        }

        qw
                .eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(project), "project_name", project)

                .eq("dp.floor", floor);
        DfAuditDetail endNum = DfAuditDetailService.getEndNum(qw);
        List<KV> kvList = new ArrayList<>();

        if (endNum == null) {
            return new Result(200, "该条件下没有问题关闭率统计相关数据", kvList);
        }

        KV kv1 = new KV("未关闭", endNum.getId());
        KV kv2 = new KV("已关闭", endNum.getParentId());
        kvList.add(kv1);
        kvList.add(kv2);
        return new Result(200, "查询成功", kvList);
    }

    @ApiOperation("处理时效性统计")
    @GetMapping("/getTimeout")
    public Result getTimeout(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw.ne("aud.data_type", "设备状态");
        qw.ne("aud.data_type", "动态IPQC");
        qw.ne("aud.data_type", "风险隔离全检");
        if (null != dataType && !dataType.equals("")) {
            qw.eq("aud.data_type", dataType);
        }

        qw.eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(project), "project_name", project)
                .eq("dp.floor", floor);
//        qw.isNotNull("aud.end_time");
        DfAuditDetail timeout = DfAuditDetailService.getTimeout2(qw);
        List<KV> kvList = new ArrayList<>();

        if (timeout == null) {
            return new Result(200, "该条件下没有处理时效性统计相关数据", kvList);
        }

        KV kv1 = new KV("超时已处理", timeout.getId());
        KV kv2 = new KV("时效内处理", timeout.getProjectName());
//        KV kv3 = new KV("超时未处理", timeout.getDepartment());
//        kvList.add(kv3);
        kvList.add(kv1);
        kvList.add(kv2);

        return new Result(200, "查询成功", kvList);
    }

    @ApiOperation("问题分类统计")
    @GetMapping("/getProjectClassNum")
    public Result getProjectClassNum(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw.ne("data_type", "设备状态");
        qw.ne("data_type", "动态IPQC");
        qw.ne("data_type", "风险隔离全检");
        if (null != dataType && !dataType.equals("")) {
            qw.eq("data_type", dataType);
        }
        qw
                .eq(StringUtils.isNotEmpty(project), "project_name", project)
                .eq("dp.floor", floor);
        List<DfAuditDetail> numList = DfAuditDetailService.getProjectClassNum(qw);
        List<KV> kvList = new ArrayList<>();
        Map<String, Integer> result = new HashMap<>();

        if (numList == null) {
            return new Result(200, "该条件下没有问题分类统计相关数据", kvList);
        }

        for (DfAuditDetail dfAuditDetail : numList) {
            result.put(dfAuditDetail.getDataType(), dfAuditDetail.getId());
        }

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            KV kv = new KV(entry.getKey(), entry.getValue());
            kvList.add(kv);
        }
        return new Result(200, "查询成功", kvList);
    }

    @ApiOperation("问题类别统计")
    @GetMapping("/getQuestionNum")
    public Result getQuestionNum(String process, String project, String startDate, String endDate, String dataType, String floor) throws ParseException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)) {
            qw.between("report_time", startDate, endDate + " 23:59:59");
        }
        qw.ne("data_type", "设备状态");
        qw.ne("question_type", "良率预警");
        qw.ne("question_type", "良率报警");
        if (null != dataType && !dataType.equals("")) {
            qw.eq("data_type", dataType);
        }
        qw.eq(StringUtils.isNotEmpty(project), "project_name", project);
        qw.ne("question_type", "FAI");
        qw.ne("question_type", "CPK");
        qw.eq(null != process && !"".equals(process), "process", process);
        qw.inSql("question_type", "select dcsc.data_type from df_control_standard_config dcsc group by dcsc.data_type");
        qw.eq("dp.floor", floor);

        List<DfAuditDetail> numList = DfAuditDetailService.getQuestionNum(qw);
        Map<String, Integer> result = new HashMap<>();
        result.put("MIL", 0);
        result.put("DFM", 0);
        result.put("其他", 0);
        result.put("耗材", 0);
        result.put("QCP", 0);
        result.put("SOP", 0);
//        result.put("FAI", 0);
//        result.put("CPK", 0);
        for (DfAuditDetail dfAuditDetail : numList) {
            result.put(dfAuditDetail.getQuestionType(), dfAuditDetail.getId());
        }
        List<KV> kvList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            KV kv = new KV(entry.getKey(), entry.getValue());
            kvList.add(kv);
        }

        return new Result(200, "查询成功", kvList);
    }

    @GetMapping("/getSnCodeByIdAndType")
    @ApiOperation("根据ID获取条码")
    public Result getSnCodeByIdAndType(int id, String type) {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        qw.eq("det.id", id);
        if ("尺寸".equals(type)) {  // 尺寸
            DfSizeDetail size = DfAuditDetailService.getSizeSnCodeById(qw);
            if (null != size.getSn()) {
                String[] codes = size.getSn().split(",");
                return new Result(200, "查询成功", codes);
            }
        } else if ("外观".equals(type)) {  // 外观
            DfQmsIpqcWaigTotal appear = DfAuditDetailService.getAppearSnCodeById(qw);
            if (null != appear.getfBarcode()) {
                String[] codes = appear.getfBarcode().split(",");
                return new Result(200, "查询成功", codes);
            }
        } else {
            return new Result(200, "没有该类型");
        }
        return new Result(200, "查无条码");
    }


    class KV implements Serializable {
        String name;
        Object value;

        public KV(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }


    @RequestMapping(value = "/downloadExcel", method = {RequestMethod.GET})
    @ResponseBody
    public void downloadExcel(String type, String startTime, String endTime, String factory,
                              String line, String project, String workshop, String workstation, String process,
                              String dataType, String questionName, String exportType, String floor,
                              HttpServletResponse response, HttpServletRequest request
    ) throws IOException {
        System.out.println(exportType);
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != type && !type.equals("")) {
            qw.eq("ad.data_type", type);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
//        if (null != factory && !factory.equals("") && !factory.equals("NaN")) {
//            qw.eq("ad.factory", factory);
//        }
        if (null != line && !line.equals("")) {
            qw.eq("ad.line", line);
        }
        if (null != project && !project.equals("") && !project.equals("undefined")) {
            qw.eq("ad.project_name", project);
        }
        if (null != workshop && !workshop.equals("")) {
            qw.eq("ad.workshop", workshop);
        }
        if (null != workstation && !workstation.equals("")) {
            qw.eq("ad.workstation", workstation);
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != questionName && !questionName.equals("")) {
            qw.and(wrapper -> wrapper.like("question_name", questionName).or().like("scene_practical", questionName));
        }
//        if (exportType.equals("notClose")) {
//            qw.isNull("end_time");
//        } else {
//            qw.isNotNull("end_time");
//        }
        qw.eq("dp.floor", floor);
        qw.ne("ad.data_type", "设备状态");
        qw.orderByDesc("ad.report_time");

        qw.last("limit 3000");

        List<DfAuditDetail> datas = DfAuditDetailService.listByBigScreen(qw);
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
            if (r.getQuestionType().equals("CPK")) {
                r.setQuestionType("过程抽检");
            }

            try {
                maps.add(Excel.objectToMap(r));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


//        if (maps != null && maps.size() > 0) {
        String companyName = "稽查数据列表";
        String sheetTitle = companyName;
        String[] title = new String[]{"项目分类","项目","工序","班次", "线体",  "机台","问题类别",  "发起人", "发起时间", "事件等级", "责任人","点检要求", "问题明细", "影响机台", "处理意见", "当前责任人", "当前节点", "进行时长", "超时时长", "FA", "CA"};        //设置表格表头字段
        String[] properties = new String[]{"dataType", "projectName","process","shift","fLine",  "macCode","questionType", "createName", "reportTime",
                "decisionLevel",
                "decisionLevelStr",
                "questionName",
                "scenePractical", "affectMac", "handlingSug", "showApprover",
                "status",
                "useTime", "overTime", "fa", "ca"};  // 查询对应的字段
        ExcelExportUtil excelExport2 = new ExcelExportUtil();
        excelExport2.setStartTime(startTime);
        excelExport2.setEndTime(endTime);
        excelExport2.setData(maps);
        excelExport2.setHeardKey(properties);
        excelExport2.setFontSize(14);
        excelExport2.setSheetName(sheetTitle);
        excelExport2.setTitle(sheetTitle);
        excelExport2.setHeardList(title);
        excelExport2.exportExport(request, response);
//        }
    }


    @ApiOperation("稽查数据")
    @RequestMapping(value = "/waigExport")
    public void waigExport (
            String factoryId,String project, String startTime, String endTime,String floor,String period,String dataType
            ,HttpServletResponse response, HttpServletRequest request
    ) throws IOException, ParseException {


        if (!StringUtils.isNotEmpty(factoryId)){
            factoryId = fac;
        }

        QueryWrapper<DfControlStandardStatus> qw1 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw1.between("status.create_time", startTime+" 07:00:00", endTime +" 23:59:59");
        }else {
            startTime = LocalDate.now().minusDays(7).toString();
            endTime = LocalDate.now().toString();
            // 获取当前日期并计算最近7天的起止时间
            qw1.between("status.create_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() +" 23:59:59" ); // 查询最近7天的数据
            period = "week";
        }

        QueryWrapper<DfControlStandardStatus> qw11 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw11.between("status.create_time", startTime+" 07:00:00", endTime + " 23:59:59");
        }else {
            startTime = LocalDate.now().minusDays(7).toString();
            endTime = LocalDate.now().toString();
            // 获取当前日期并计算最近7天的起止时间
            qw11.between("status.create_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() +" 23:59:59"); // 查询最近7天的数据
            period = "week";
        }


        //第一个sheet**************************************************************************************************************
        qw1.eq(StringUtils.isNotEmpty(project),"status.project_id",project);
        // qw1.eq(StringUtils.isNotEmpty(factoryId),"status.factory_id",factoryId);
        //  qw1.eq("data_type","稽查");
        // qw1.eq("dp.floor","4F");

        List<DfControlStandardStatus> all = dfControlStandardStatusService.queryDailyData(qw1);

        ExcelNewExportUtil2 excelExport2 = new ExcelNewExportUtil2();
        excelExport2.setFac(factoryId);
        excelExport2.setStartDate(startTime);
        excelExport2.setEndDate(endTime);
        excelExport2.setPeriod(period);

        //第二个sheet****************************************************************************************************************
        QueryWrapper<DfControlStandardStatus> qw2 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
          //  qw2.between("status.create_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw2.between("status.create_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw2.eq(StringUtils.isNotEmpty(project),"status.project_id",project);
        // qw2.eq(StringUtils.isNotEmpty(factoryId),"status.factory_id",factoryId);
        qw2.eq(StringUtils.isNotEmpty(dataType),"config.data_type","稽查");
        qw2.eq("dp.floor","4F");

        List<DfControlStandardStatus> SheetList = dfControlStandardStatusService.queryEachProcessData(qw2,startTime+" 07:00:00",endTime + " 07:00:00");
        //第三个sheet****************************************************************************************************************
        QueryWrapper<DfAuditDetail> qw3 = new QueryWrapper<>();
        qw3.eq(StringUtils.isNotEmpty(project),"dad.project_name",project);
        // qw3.eq(StringUtils.isNotEmpty(factoryId),"dad.factory_id",factoryId);
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw3.between("report_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw3.between("dad.report_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw3.eq("dp.floor","4F");
        qw3.ne("dad.data_type", "设备状态");
        qw3.ne("dad.data_type", "动态IPQC");
        qw3.eq("dad.data_type","稽查");

        List<DfAuditDetail> numList = DfAuditDetailService.getQuestionNumJc(qw3);
        //第四个sheet********************************************************************************************************************
        QueryWrapper<DfAuditDetail> qw4 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw4.between("report_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw4.between("aud.report_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw4.ne("aud.data_type", "设备状态");
        qw4.ne("aud.data_type", "动态IPQC");
        qw4.eq("aud.data_type", "稽查");

        qw4.eq("dp.floor", floor);
        qw4.isNotNull("aud.end_time");
        List<DfAuditDetail> timeout = DfAuditDetailService.getTimeoutData(qw4);


        //第五个sheet********************************************************************************************************************
        QueryWrapper<DfAuditDetail> qw5 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw5.between("report_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw5.between("aud.report_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw5.ne("aud.data_type", "设备状态");
        qw5.ne("aud.data_type", "动态IPQC");
        qw5.eq("aud.data_type", "稽查");
        //qw5.isNotNull("aud.end_time");
        qw5.eq("dp.floor", floor);
        List<DfAuditDetail> list5 = DfAuditDetailService.getAuditSummaryData(qw5);

        //第六个sheet********************************************************************************************************************
        QueryWrapper<DfAuditDetail> qw6 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw4.between("report_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw6.between("aud.report_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw6.ne("aud.data_type", "设备状态");
        qw6.ne("aud.data_type", "动态IPQC");
        qw6.eq("aud.data_type", "稽查");

        qw6.eq("dp.floor", floor);
        qw6.isNotNull("aud.end_time");
        List<DfAuditDetail> Processtimeout = DfAuditDetailService.getProcessTimeoutData(qw4);



        //第七个sheet********************************************************************************************************************
        QueryWrapper<DfAuditDetail> qw7 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw7.between("report_time", startTime+" 07:00:00", endTime + " 07:00:00");
        }else {
            // 获取当前日期并计算最近7天的起止时间
            qw7.between("aud.report_time", LocalDate.now().minusDays(7).toString() + " 07:00:00",LocalDate.now().toString() + " 07:00:00" ); // 查询最近5天的数据
            period = "week";
        }
        qw7.ne("aud.data_type", "设备状态");
        qw7.ne("aud.data_type", "动态IPQC");
        qw7.eq("aud.data_type", "稽查");

        qw7.eq("dp.floor", floor);
//        qw4.isNotNull("aud.end_time");
        List<DfAuditDetail> list7 = DfAuditDetailService.getAuditLevelSummary(qw7);


        QueryWrapper<DfProcess> dw = new QueryWrapper<>(); // 创建缺陷配置查询包装器
        dw.orderByAsc("sort"); // 根据 priority 字段升序排列
        dw.eq("floor",floor);
       // List<DfProcessProjectConfig> dplistall = dfProcessProjectConfigService.listDistinct(dw);//获取工序数据


        List<DfProcess> dplistall = dfProcessService.listDfProcess(dw);//获取工序数据

        List<DfProcess> dplist = dplistall.stream().filter(distinctByKey(DfProcess::getProcessName))
                .collect(Collectors.toList());

        /**
         * 第八个sheet*****************************************************************************************
         */

        QueryWrapper<DfAuditDetail> wv = new QueryWrapper<>(); // 创建查询包装器
        wv.eq(StringUtils.isNotEmpty(project),"d.project_name",project);

        // 设置时间范围
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            wv.between("d.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            wv.ge("d.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            wv.le("d.report_time", endTime + " 23:59:59");
        }else {
            period = "week";
            wv.between("d.report_time", LocalDate.now().minusDays(7).toString() + " 23:59:59", LocalDate.now().toString() +  " 23:59:59" ); // 查询最近7天的数据
        }
        wv.eq("dp.floor", floor);

        wv.eq("d.data_type", "稽查");
        //wv.isNull("end_time");
        wv.ne("d.data_type", "设备状态");
        wv.ne("d.data_type", "动态IPQC");
        wv.ne("d.data_type", "风险隔离全检");
        wv.eq("d.data_type",  "稽查");
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


        String[] titleSheet8 = new String[]{"项目分类","项目", "工序","班次","线体", "机台","问题类别", "发起人", "发起时间",
                "事件等级", "问题点对应责任人","点检要求",
                "问题明细", "影响机台", "处理意见", "当前责任人", "当前节点", "进行时长", "超时时长", "FA", "CA"};        //设置表格表头字段
        String[] propertiesSheet8 = new String[]{"dataType","projectName","process","shift","fLine","fMac","questionType","createName", "reportTime",
                "decisionLevel",
                "decisionLevelStr",
                "questionName",
                "scenePractical", "affectMac", "handlingSug", "showApprover",
                "status",
                "useTime", "overTime", "fa", "ca"};  // 查询对应的字段

        ExcelNewExportUtil2 excelExportSheet8 = new ExcelNewExportUtil2(); // 创建Excel导出工具
        excelExportSheet8.setData(maps);
        excelExportSheet8.setHeardKey(propertiesSheet8);
        excelExportSheet8.setHeardList(titleSheet8);

        excelExport2.exportExportNew(request, response,all,SheetList,numList,timeout,list5,Processtimeout,list7,excelExportSheet8,datas,dplist);
    }




    @PostMapping("/generalApprovalForm")
    @ApiOperation("创建通用审批单")
    public Result generalApprovalForm(DfAuditDetail dfAuditDetail, @RequestParam String type, @RequestParam String process) {
        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
        sqw.eq("type", type);
        Integer flowLevel = null;
        switch (dfAuditDetail.getDecisionLevel()) {
            case "Level1":
                sqw.eq("problem_level", "1");
                flowLevel = 1;
                break;
            case "Level2":
                sqw.eq("problem_level", "2");
                flowLevel = 2;
                break;
            case "Level3":
                sqw.eq("problem_level", "3");
                flowLevel = 3;
                break;
        }

        if (TimeUtil.getBimonthly() == 0) {
            sqw.like("bimonthly", "双月");
        } else {
            sqw.like("bimonthly", "单月");
        }
        sqw.like("process_name", process);
        List<DfLiableMan> lm = dfLiableManService.list(sqw);
        if (lm.size() > 0) {
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
            dfAuditDetail.setResponsible(manName.toString());
            dfAuditDetail.setResponsibleId(manCode.toString());
            DfAuditDetailService.save(dfAuditDetail);

            DfFlowData fd = new DfFlowData();
            fd.setName(dfAuditDetail.getQuestionName());
            fd.setFlowLevel(flowLevel);
            fd.setDataType(dfAuditDetail.getDataType());
            fd.setFlowType(dfAuditDetail.getDataType());
            fd.setDataId(dfAuditDetail.getId());
            fd.setStatus("待确认");
            fd.setCreateName(dfAuditDetail.getCreateName());
            fd.setCreateUserId(dfAuditDetail.getCreateUserId());
            fd.setNowLevelUser(dfAuditDetail.getResponsibleId());
            fd.setNowLevelUserName(dfAuditDetail.getResponsible());
            fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            fd.setFlowLevelName(dfAuditDetail.getDecisionLevel());
            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
            atQw.eq("type", type);
            atQw.last("limit 1");
            DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
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
            DfFlowDataService.save(fd);
        }
        return new Result(200, "保存成功");
    }


    @GetMapping("/generalApprovalForm2")
    @ApiOperation("创建通用审批单2")
    public Result generalApprovalForm(@ApiParam("问题名称") @RequestParam String questionName,
                                      @ApiParam("问题类型") @RequestParam String questionType,
                                      @ApiParam("责任人工号列表") @RequestParam List<String> userCodeList,
                                      @ApiParam("责任部门") @RequestParam String department) {
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.in("name", userCodeList);
        List<User> userList = userService.list(ew);
        StringBuilder manName = new StringBuilder();
        StringBuilder manCode = new StringBuilder();
        int manCount = 0;
        for (User user : userList) {
            if (manCount > 0) {
                manCode.append(",");
                manName.append(",");
            }
            manName.append(user.getAlias());
            manCode.append(user.getName());
            manCount++;
        }
        DfAuditDetail dfAuditDetail = new DfAuditDetail();
        dfAuditDetail.setDepartment(department);
        dfAuditDetail.setResponsible(manName.toString());
        dfAuditDetail.setResponsibleId(manCode.toString());
        dfAuditDetail.setDataType("通用审批单");
        dfAuditDetail.setScenePractical(questionType);
        dfAuditDetail.setIsFaca("0");
        dfAuditDetail.setReportTime(Timestamp.valueOf(checkTime));
        dfAuditDetail.setOccurrenceTime(Timestamp.valueOf(checkTime));
        dfAuditDetail.setIpqcNumber(checkTimeLong);
        dfAuditDetail.setQuestionName(questionName);
        dfAuditDetail.setScenePractical(questionName);
        dfAuditDetail.setQuestionType(questionType);
        dfAuditDetail.setDecisionLevel("Level1");
        dfAuditDetail.setHandlingSug("隔离,全检");
        dfAuditDetail.setResponsible(manName.toString());
        dfAuditDetail.setResponsibleId(manCode.toString());
        dfAuditDetail.setAffectMac("0");
        DfAuditDetailService.save(dfAuditDetail);

        DfFlowData fd = new DfFlowData();
        fd.setName(dfAuditDetail.getQuestionName() + TimeUtil.getNowTimeByNormal());
        fd.setFlowLevel(1);
        fd.setDataType(dfAuditDetail.getDataType());
        fd.setFlowType(dfAuditDetail.getDataType());
        fd.setDataId(dfAuditDetail.getId());
        fd.setStatus("待确认");
        fd.setCreateName(dfAuditDetail.getCreateName());
        fd.setCreateUserId(dfAuditDetail.getCreateUserId());
        fd.setNowLevelUser(dfAuditDetail.getResponsibleId());
        fd.setNowLevelUserName(dfAuditDetail.getResponsible());
        fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        fd.setFlowLevelName(dfAuditDetail.getDecisionLevel());
        QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
        atQw.eq("type", questionType);
        atQw.last("limit 1");
        DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
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
        DfFlowDataService.save(fd);

        return new Result(200, "发送审批单成功");
    }


    /**
     * 关键字查询
     *
     * @param page
     * @param limit
     * @param questionType
     * @param process
     * @param startDate
     * @param endDate
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch", method = RequestMethod.GET)
    @ApiOperation("获取稽查任务单数据列表")
    public Result listBySearch(int page, int limit, String questionType, String process, String startDate, String endDate, String keywords) {
        String startTime = startDate + " 00:00:00";
        String endTime = endDate + " 23:59:59";

        Page<DfAuditDetail> pages = new Page<>(page, limit);
        QueryWrapper<DfAuditDetail> ew = new QueryWrapper<>();
        if (keywords != null && !"".equals(keywords)) {
            ew
                    .eq(StringUtils.isNotEmpty(questionType), "question_type", questionType)
                    .eq(StringUtils.isNotEmpty(process), "process", process)
                    .ge(StringUtils.isNotEmpty(startDate), "report_time", startTime)
                    .le(StringUtils.isNotEmpty(endDate), "report_time", endTime)
                    .and(wrapper -> wrapper
                            .like("name", keywords)
                            .or().like("data_type", keywords)
                            .or().like("flow_type", keywords)
                            .or().like("create_name", keywords)
                            .or().like("status", keywords)
                            .or().like("flow_level_name", keywords)
                            .or().like("now_level_user_name", keywords)
                            .or().like("overtime_status", keywords)
                    )
                    .orderByDesc("create_time");
        }
        IPage<DfAuditDetail> list = DfAuditDetailService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }


    /**
     * 修改信赖性异常信息
     *
     * @param datas
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("修改稽查任务单数据")
    public Result update(@RequestBody DfAuditDetail datas) {

        if (DfAuditDetailService.updateById(datas)) {
            return new Result(200, "修改成功");
        }
        return new Result(200, "修改失败");
    }

    /**
     * 删除信赖性异常信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ApiOperation("删除稽查任务单数据")
    @Transactional(rollbackFor = Exception.class)
    public Result deleteById(String id) {
        if (DfAuditDetailService.removeById(id)) {
            UpdateWrapper<DfFlowData> qw = new UpdateWrapper<>();
            qw.eq("data_id", id);
            if (DfFlowDataService.remove(qw)) {
                return new Result(200, "删除成功");
            }
        }
        return new Result(200, "删除失败");
    }

    @ApiOperation("稽查单导出")
    @GetMapping(value = "/AuditListExport")
    public void AuditListExport(String type, String startTime, String endTime, String factory,
                                String line, String project, String workshop, String workstation, String process,
                                String dataType, String questionName, String floor,
                                HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        if (null != type && !type.equals("")) {
            qw.eq("ad.data_type", type);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw.between("ad.report_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw.ge("ad.report_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw.le("ad.report_time", endTime + " 23:59:59");
        }
//        if (null != factory && !factory.equals("") && !factory.equals("NaN")) {
//            qw.eq("ad.factory", factory);
//        }
        if (null != line && !line.equals("")) {
            qw.eq("ad.line", line);
        }
        if (null != project && !project.equals("") && !project.equals("undefined")) {
            qw.eq("ad.project_name", project);
        }
        if (null != workshop && !workshop.equals("")) {
            qw.eq("ad.workshop", workshop);
        }
        if (null != workstation && !workstation.equals("")) {
            qw.eq("ad.workstation", workstation);
        }
        if (null != process && !process.equals("")) {
            qw.eq("ad.process", process);
        }
        if (null != dataType && !dataType.equals("")) {
            qw.eq("ad.data_type", dataType);
        }
        if (null != questionName && !questionName.equals("")) {
            qw.and(wrapper -> wrapper.like("question_name", questionName).or().like("scene_practical", questionName));
        }
        qw.eq("dp.floor", floor);
        qw.isNull("end_time");
        qw.ne("ad.data_type", "设备状态");
        qw.ne("ad.data_type", "动态IPQC");
        qw.ne("ad.data_type", "风险隔离全检");
        qw.orderByDesc("ad.report_time");
        qw.last("limit 3000");
        List<DfAuditDetail> list = DfAuditDetailService.listByBigScreen(qw);
        list.parallelStream().forEach(item -> {
            switch (item.getDecisionLevel()) {
                case "2":
                    item.setResponsible(item.getResponsible2());
                    break;
                case "3":
                    item.setResponsible(item.getResponsible3());
                case "4":
                    item.setResponsible(item.getResponsible4());
            }
        });

        List<Map> maps = new ArrayList<>();

        for (DfAuditDetail dfAuditDetail : list) {
            try {
                maps.add(Excel.objectToMap(dfAuditDetail));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        if (maps != null && maps.size() > 0) {
//            String companyName = "用户管理表";
//            String sheetTitle = companyName;
        String[] title = new String[]{"日期", "类别", "工序", "稽核事项", "严重等级",
                "中央IPQV稽核方式(抽检机台比例为10%)", "厂别", "部门主管", "问题明细", "确认人", "IPQC稽查员", "备注"};        //设置表格表头字段

        String[] properties = new String[]{"reportTime", "questionType", "process", "detail",
                "decisionLevel", "中央IPQV稽核方式(抽检机台比例为10%)", "factory", "部门主管", "scenePractical", "responsible", "reportMan", "remark"};  // 查询对应的字段
        ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
        excelExport2.setData(maps);
        excelExport2.setHeardKey(properties);
        excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
        excelExport2.setHeardList(title);
        excelExport2.exportExport(request, response);
//        }
    }


    @RequestMapping(value = "/closeByRFID", method = RequestMethod.GET)
    @ApiOperation("关闭RFID任务单")
    public Result closeOrUpdate(String process, String fa, String ca) {

        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        qw.eq("data_type", "RFID");
        qw.eq("process", process);
        qw.isNull("end_time");
        List<DfAuditDetail> haveUpdate = DfAuditDetailService.list(qw);
        if (haveUpdate.size() > 0) {
            UpdateWrapper<DfAuditDetail> uw = new UpdateWrapper<>();
            uw.eq("data_type", "RFID");
            uw.eq("process", process);
            uw.isNull("end_time");

            uw.set(null != fa && !fa.equals(""), "fa", fa);
            uw.set(null != fa && !fa.equals(""), "ca", ca);
            uw.set("end_time", TimeUtil.getNowTimeByNormal());
            DfAuditDetailService.update(uw);

            for (DfAuditDetail d : haveUpdate) {
                UpdateWrapper<DfFlowData> fduw = new UpdateWrapper<>();
                fduw.eq("data_id", d.getId());
                fduw.set("submit_time", TimeUtil.getNowTimeByNormal());
                fduw.set("status", "已关闭");
                fduw.set("submit_name", "系统");
                DfFlowDataService.update(fduw);
            }
        }

        return new Result(200, "删除成功");
    }


    @RequestMapping(value = "/updateByRFID", method = RequestMethod.GET)
    @ApiOperation("升级RFID任务单")
    public Result updateByRFID(String process, int level, String truth) {

        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
        qw.eq("data_type", "RFID");
        qw.eq("process", process);
        qw.isNull("end_time");
        List<DfAuditDetail> haveUpdate = DfAuditDetailService.list(qw);
        if (haveUpdate.size() > 0) {

            UpdateWrapper<DfAuditDetail> uw = new UpdateWrapper<>();
            uw.eq("data_type", "RFID");
            uw.eq("process", process);
            uw.isNull("end_time");


            uw.set(null != truth && !truth.equals(""), "scene_practical", truth);

            switch (level) {
                case 1:
                    uw.set("decision_level", "Level1");

                    break;
                case 2:
                    uw.set("decision_level", "Level2");

                    break;
                case 3:
                    uw.set("decision_level", "Level3");

                    break;
            }

            DfAuditDetailService.update(uw);

            for (DfAuditDetail d : haveUpdate) {
                UpdateWrapper<DfFlowData> fduw = new UpdateWrapper<>();
                fduw.set("flow_level", level);
                fduw.eq("data_id", d.getId());
                switch (level) {
                    case 1:
                        fduw.set("flow_level_name", "Level1");
                        fduw.set("now_level_user_name", d.getResponsible());
                        fduw.set("now_level_user", d.getResponsibleId());
                        fduw.set("show_approver", d.getResponsible());
                        break;
                    case 2:
                        fduw.set("flow_level_name", "Level2");
                        fduw.set("now_level_user_name", d.getResponsible2());
                        fduw.set("now_level_user", d.getResponsibleId2());
                        fduw.set("show_approver", d.getResponsible2());
                        break;
                    case 3:
                        fduw.set("flow_level_name", "Level3");
                        fduw.set("now_level_user_name", d.getResponsible3());
                        fduw.set("now_level_user", d.getResponsibleId3());
                        fduw.set("show_approver", d.getResponsible3());
                        break;
                }


                DfFlowDataService.update(fduw);
            }
        }


        return new Result(200, "升级成功");
    }


    @PostMapping("/createJobRFID")
    @ApiOperation("RFID创建任务单")
    public Result createJobRFID(@RequestBody DfAuditDetail dfAuditDetail) {

        int level=1;
        switch (dfAuditDetail.getDecisionLevel()){
            case "Level2":
                level=2;
                break;
            case "Level3":
                level=3;
                break;

        }
        dfAuditDetail.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        dfAuditDetail.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        DfAuditDetailService.save(dfAuditDetail);
        DfFlowData fd = new DfFlowData();
        fd.setName(dfAuditDetail.getQuestionName());
        fd.setFlowLevel(level);
        fd.setDataType(dfAuditDetail.getDataType());
        fd.setFlowType(dfAuditDetail.getDataType());
        fd.setDataId(dfAuditDetail.getId());
        fd.setStatus("待确认");
        fd.setCreateName(dfAuditDetail.getCreateName());
        fd.setCreateUserId(dfAuditDetail.getCreateUserId());
        fd.setNowLevelUser(dfAuditDetail.getResponsibleId());
        fd.setNowLevelUserName(dfAuditDetail.getResponsible());
        fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        fd.setFlowLevelName(dfAuditDetail.getDecisionLevel());
        fd.setReadTimeMax(120);
        fd.setDisposeTimeMax(240);

        //设置显示人
        fd.setShowApprover(fd.getNowLevelUserName());
        DfFlowDataService.save(fd);
        return new Result(200, "保存成功");
    }


//
//    @GetMapping("/examSampleAudit")
//    @ApiOperation("考核系统样品统计稽查")
//    public Result examSampleAudit(
//            @ApiParam("不良分类") @RequestParam String defectClass,
//            @ApiParam("不良区域") @RequestParam String defectArea,
//            @ApiParam("不良项") @RequestParam String defectName
//    ) {
//        //获取当前检测时间
//        String checkTime = TimeUtil.getNowTimeByNormal();
//        //当前时间戳
//        String checkTimeLong = TimeUtil.getNowTimeLong();
//        System.out.println("时间："+checkTime);
//        System.out.println("不良分类："+defectClass+",不良区域："+defectArea+",不良项："+defectName);
//
//        return new Result(200,"发送审批单成功");
//    }
//
}
