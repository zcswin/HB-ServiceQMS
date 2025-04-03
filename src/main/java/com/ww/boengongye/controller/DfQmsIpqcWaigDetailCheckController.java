package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
@Controller
@RequestMapping("/dfQmsIpqcWaigDetailCheck")
@ResponseBody
@CrossOrigin
@Api(tags = "外观受检信息")
public class DfQmsIpqcWaigDetailCheckController {
    private static final Logger logger = LoggerFactory.getLogger(DfQmsIpqcWaigDetailCheckController.class);

    @Autowired
    com.ww.boengongye.service.DfQmsIpqcWaigDetailCheckService DfQmsIpqcWaigDetailCheckService;
    @Autowired
    com.ww.boengongye.service.DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;
    @Autowired
    private DfYieldWarnMapper dfYieldWarnMapper;
    @Autowired
    private DfLiableManMapper dfLiableManMapper;
    @Autowired
    private DfAuditDetailMapper dfAuditDetailMapper;
    @Autowired
    private DfApprovalTimeMapper dfApprovalTimeMapper;
    @Autowired
    private DfFlowDataMapper dfFlowDataMapper;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfQmsIpqcWaigDetailCheckService.list());
    }


    @RequestMapping(value = "/listByParentId")
    public Object listByParentId(int id) {
        QueryWrapper<DfQmsIpqcWaigDetailCheck> qw=new QueryWrapper<>();
        qw.eq("d.f_parent_id",id);
        return new Result(0, "查询成功",DfQmsIpqcWaigDetailCheckService.listByJoin(qw));
    }


    @RequestMapping(value = "/listByMachineCode")
    public Result listByMachineCode( String machineCode) {
        QueryWrapper<DfQmsIpqcWaigDetailCheck> ew=new QueryWrapper<DfQmsIpqcWaigDetailCheck>();
        ew.inSql("f_parent_id", "select t.id from(select id from df_qms_ipqc_waig_total_check where status='NG' and f_mac='"+machineCode+"' order by f_time desc limit 0,1) as t");


        return new Result(0, "查询成功",DfQmsIpqcWaigDetailCheckService.list(ew));

    }

    @RequestMapping(value = "/listBySmAreaCount")
    public Result listBySmAreaCount( String project,String process,String fsort,String startDate,String endDate,String floor, String dayOrNight, String machineCode) throws ParseException {
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
        QueryWrapper<DfQmsIpqcWaigDetailCheck> ew=new QueryWrapper<DfQmsIpqcWaigDetailCheck>();
        QueryWrapper<DfQmsIpqcWaigDetailCheck>qw2=new QueryWrapper<>();
        ew.eq(StringUtils.isNotEmpty(project),"p.f_bigpro",project);
        qw2.eq(StringUtils.isNotEmpty(project),"tol.f_bigpro",project);

        if(null!=process&&!process.equals("")&&!process.equals("undefined")){
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
        return new Result(0, "查询成功",datas);

    }

    @ApiOperation("全外观NG-TOP5")
    @RequestMapping(value = "/fullApperanceNGTop5")
    public Result fullSizeNGTop5(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        }else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list= DfQmsIpqcWaigDetailCheckService.fullApperanceNGTop5(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> name = new ArrayList<>();
        ArrayList<Object> result = new ArrayList<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            name.add(dfSizeCheckItemInfos.getItemName());
            result.add(dfSizeCheckItemInfos.getNgRate());
        }
        map.put("process",name);
        map.put("result",result);
        return new Result(0, "查询成功",map);
    }



    @ApiOperation("获取直通率-5F")
    @RequestMapping(value = "/getFpy",method = RequestMethod.GET)
    public Result getFpy(@RequestParam String startTime,@RequestParam String endTime) throws ParseException {
        startTime = startTime + " 07:00:00";
        endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";

        Map<String,Object> map = new HashMap<>();

        //工序集合（x轴）
        List<Object> processList = new ArrayList<>();

        //直通率（y轴）
        List<Object> fqyPointList = new ArrayList<>();

        QueryWrapper<Rate3> fqyWrapper = new QueryWrapper<>();
        fqyWrapper
                .ge(StringUtils.isNotEmpty(startTime),"dqiwt.f_time",startTime)
                .le(StringUtils.isNotEmpty(endTime),"dqiwt.f_time",endTime);

        QueryWrapper<Rate3> itemWrapper = new QueryWrapper<>();
        itemWrapper
                .apply("item.check_time >='"+startTime+"'")
                .apply("item.check_time <='"+endTime+"'");
        List<Rate3> fqyList = DfQmsIpqcWaigDetailCheckService.getFpy(fqyWrapper,itemWrapper);

        Double fqyPoint = 100.0;
        for (Rate3 oneFqy : fqyList){
            String process = oneFqy.getStr1();
            Double passPoint = 100.0;
            if (oneFqy.getDou1()!=null){
                passPoint = oneFqy.getDou1();
            }
            fqyPoint = fqyPoint*passPoint/100;

            processList.add(process);
            fqyPointList.add(String.format("%.2f",fqyPoint));
        }

        map.put("processListX",processList);
        map.put("fqyPointListX",fqyPointList);
        return new Result(200,"获取直通率成功",map);
    }

    @ApiOperation("获取后段直通率")
    @RequestMapping(value = "/getAfterFpy",method = RequestMethod.GET)
    public Result getAfterFpy(@RequestParam String startTime,@RequestParam String endTime) throws ParseException {
        startTime = startTime + " 07:00:00";
        endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";

        Map<String,Object> map = new HashMap<>();

        //工序集合（x轴）
        List<Object> processList = new ArrayList<>();

        //直通率（y轴）
        List<Object> fqyPointList = new ArrayList<>();

        QueryWrapper<Rate3> fqyWrapper = new QueryWrapper<>();
        fqyWrapper
                .ge(StringUtils.isNotEmpty(startTime),"dqiwt.f_time",startTime)
                .le(StringUtils.isNotEmpty(endTime),"dqiwt.f_time",endTime);

        QueryWrapper<Rate3> itemWrapper = new QueryWrapper<>();
        itemWrapper
                .apply("item.check_time >='"+startTime+"'")
                .apply("item.check_time <='"+endTime+"'");
        List<Rate3> fqyList = DfQmsIpqcWaigDetailCheckService.getAfterFpy(fqyWrapper,itemWrapper);

        Double fqyPoint = 100.0;
        for (Rate3 oneFqy : fqyList){
            String process = oneFqy.getStr1();
            Double passPoint = 100.0;
            if (oneFqy.getDou1()!=null){
                passPoint = oneFqy.getDou1();
            }
            fqyPoint = fqyPoint*passPoint/100;

            processList.add(process);
            fqyPointList.add(String.format("%.2f",fqyPoint));
        }

        map.put("processListX",processList);
        map.put("fqyPointListX",fqyPointList);
        return new Result(200,"获取直通率成功",map);
    }

//    @ApiOperation("定时任务_外观单项不良预警报警")
//    @RequestMapping(value = "/scheduleWarnAndAlarm",method = RequestMethod.GET)
//    @Scheduled(cron = "0 0 * * * ?")
//    public Object checkAoiPassPointHour(){
//        //获取当前检测时间
//        String checkTime = TimeUtil.getNowTimeByNormal();
//        //当前时间戳
//        String checkTimeLong = TimeUtil.getNowTimeLong();
//
//        QueryWrapper<DfYieldWarn> dfYieldWarnWrapper = new QueryWrapper<>();
//        dfYieldWarnWrapper
//                .eq("`type`","外观")
//                .eq("name","单项不良预警报警")
//                .last("limit 1");
//        //外观单项不良预警和报警值
//        DfYieldWarn dfYieldWarn = dfYieldWarnMapper.selectOne(dfYieldWarnWrapper);
//
//        //获取近一小时所有检测项报警信息
//        List<Rate4> alarmList = DfQmsIpqcWaigDetailCheckService.getAlarmMessage(dfYieldWarn.getAlarmValue() / 100);
//        for (Rate4 rate4 : alarmList) {
//            QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
//            sqw.eq("type","check");
//            sqw.eq("problem_level","1");
//            if(TimeUtil.getBimonthly()==0){
//                sqw.like("bimonthly","双月");
//            }else{
//                sqw.like("bimonthly","单月");
//            }
//            sqw.like("process_name", rate4.getStr1());
//            List<DfLiableMan> lm =dfLiableManMapper.selectList(sqw);
//            if (lm==null||lm.size()==0){
//                continue;
//            }
//            StringBuilder manCode=new StringBuilder();
//            StringBuilder manName=new StringBuilder();
//            int manCount=0;
//            for(DfLiableMan l:lm){
//                if(manCount>0){
//                    manCode.append(",");
//                    manName.append(",");
//                }
//                manCode.append(l.getLiableManCode());
//                manName.append(l.getLiableManName());
//                manCount++;
//            }
//            DfAuditDetail aud=new DfAuditDetail();
//            aud.setProcess(rate4.getStr1());
//            aud.setProject(rate4.getStr2());
//            aud.setProjectName(rate4.getStr2());
//            aud.setDataType("外观");
//            aud.setDepartment(rate4.getStr1());
//            aud.setAffectNum(1.0);
//            aud.setControlStandard("单项良率小于等于"+dfYieldWarn.getAlarmValue()+"%,单项良率报警");
//            aud.setImpactType("单项良率");
//            aud.setIsFaca("0");
//
//            //问题名称和现场实际调换
//            aud.setQuestionName("单项良率报警");
//            aud.setReportMan("系统");
//            aud.setCreateName("系统");
//            aud.setAffectMac("0");
//            aud.setReportTime(Timestamp.valueOf(checkTime));
//            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
//            aud.setIpqcNumber(checkTimeLong);
//
//            aud.setScenePractical(rate4.getStr1() + rate4.getStr2()+rate4.getStr3()+rate4.getStr4() +"_良率报警，良率为"+rate4.getDou1()+"%");
//            aud.setQuestionType("良率报警");
//            aud.setDecisionLevel("Level1");
//            aud.setHandlingSug("隔离,全检");
//            aud.setResponsible(manName.toString());
//            aud.setResponsibleId(manCode.toString());
//            dfAuditDetailMapper.insert(aud);
//
//            DfFlowData fd = new DfFlowData();
//            fd.setFlowLevel(1);
//            fd.setDataType(aud.getDataType());
//            fd.setFlowType(aud.getDataType());
//            fd.setName(rate4.getStr1() + rate4.getStr2()+rate4.getStr3()+rate4.getStr4() +"_良率报警，良率为"+rate4.getDou1()+"%");
//            fd.setDataId(aud.getId());
//            fd.setStatus("待确定");
//
//            fd.setNowLevelUser(aud.getResponsibleId());
//            fd.setNowLevelUserName(aud.getResponsible());
//            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
//            fd.setFlowLevelName(aud.getDecisionLevel());
//
//            QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
//            atQw.eq("type","外观")
//                    .last("limit 1");
//            DfApprovalTime at= dfApprovalTimeMapper.selectOne(atQw);
//            if(null!=at){
//                if(fd.getFlowLevelName().equals("Level1")){
//                    fd.setReadTimeMax(at.getReadTimeLevel1());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
//                }else  if(fd.getFlowLevelName().equals("Level2")){
//                    fd.setReadTimeMax(at.getReadTimeLevel2());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
//                }else  if(fd.getFlowLevelName().equals("Level3")){
//                    fd.setReadTimeMax(at.getReadTimeLevel3());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
//                }
//            }
//            //设置显示人
//            fd.setShowApprover(fd.getNowLevelUserName());
//            dfFlowDataMapper.insert(fd);
//        }
//
//        //获取近一小时所有检测项预警信息
//        List<Rate4> warnList = DfQmsIpqcWaigDetailCheckService.getWarnMessage(dfYieldWarn.getAlarmValue() / 100,dfYieldWarn.getPrewarningValue() / 100);
//        for (Rate4 rate4 : warnList) {
//            QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
//            sqw.eq("type","check");
//            sqw.eq("problem_level","1");
//            if(TimeUtil.getBimonthly()==0){
//                sqw.like("bimonthly","双月");
//            }else{
//                sqw.like("bimonthly","单月");
//            }
//            sqw.like("process_name", rate4.getStr1());
//            List<DfLiableMan> lm =dfLiableManMapper.selectList(sqw);
//            if (lm==null||lm.size()==0){
//                continue;
//            }
//            StringBuilder manCode=new StringBuilder();
//            StringBuilder manName=new StringBuilder();
//            int manCount=0;
//            for(DfLiableMan l:lm){
//                if(manCount>0){
//                    manCode.append(",");
//                    manName.append(",");
//                }
//                manCode.append(l.getLiableManCode());
//                manName.append(l.getLiableManName());
//                manCount++;
//            }
//            DfAuditDetail aud=new DfAuditDetail();
//            aud.setReportMan("系统");
//            aud.setCreateName("系统");
//            aud.setAffectMac("0");
//            aud.setProcess(rate4.getStr1());
//            aud.setProject(rate4.getStr2());
//            aud.setProjectName(rate4.getStr2());
//            aud.setDataType("外观");
//            aud.setDepartment(rate4.getStr1());
//            aud.setAffectNum(1.0);
//            aud.setControlStandard("单项良率小于等于"+dfYieldWarn.getPrewarningValue()+"%,单项良率预警");
//            aud.setImpactType("单项良率");
//            aud.setIsFaca("0");
//
//            //问题名称和现场实际调换
//            aud.setQuestionName("单项良率预警");
//
//            aud.setReportTime(Timestamp.valueOf(checkTime));
//            aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
//            aud.setIpqcNumber(checkTimeLong);
//
//            aud.setScenePractical(rate4.getStr1() + rate4.getStr2()+rate4.getStr3()+rate4.getStr4() +"_良率预警，良率为"+rate4.getDou1()+"%");
//            aud.setQuestionType("良率预警");
//            aud.setDecisionLevel("Level1");
//            aud.setHandlingSug("隔离,全检");
//            aud.setResponsible(manName.toString());
//            aud.setResponsibleId(manCode.toString());
//            dfAuditDetailMapper.insert(aud);
//
//            DfFlowData fd = new DfFlowData();
//            fd.setFlowLevel(1);
//            fd.setDataType(aud.getDataType());
//            fd.setFlowType(aud.getDataType());
//            fd.setName(rate4.getStr1() + rate4.getStr2()+rate4.getStr3()+rate4.getStr4() +"_良率预警，良率为"+rate4.getDou1()+"%");
//            fd.setDataId(aud.getId());
//            fd.setStatus("待确定");
//
//            fd.setNowLevelUser(aud.getResponsibleId());
//            fd.setNowLevelUserName(aud.getResponsible());
//            fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
//            fd.setFlowLevelName(aud.getDecisionLevel());
//
//            QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
//            atQw.eq("type","外观")
//                    .last("limit 1");
//            DfApprovalTime at= dfApprovalTimeMapper.selectOne(atQw);
//            if(null!=at){
//                if(fd.getFlowLevelName().equals("Level1")){
//                    fd.setReadTimeMax(at.getReadTimeLevel1());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
//                }else  if(fd.getFlowLevelName().equals("Level2")){
//                    fd.setReadTimeMax(at.getReadTimeLevel2());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
//                }else  if(fd.getFlowLevelName().equals("Level3")){
//                    fd.setReadTimeMax(at.getReadTimeLevel3());
//                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
//                }
//            }
//            //设置显示人
//            fd.setShowApprover(fd.getNowLevelUserName());
//            dfFlowDataMapper.insert(fd);
//        }
//
//        return new Result(200, "添加成功");
//    }


}
