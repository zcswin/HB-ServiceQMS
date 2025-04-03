package com.ww.boengongye.timer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

@Component
@EnableScheduling // 1.开启定时任务
public class CheckSizeAllProcessYield {

    @Autowired
    DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    DfYieldWarnService dfYieldWarnService;

    @Autowired
    DfSizeDetailService  dfSizeDetailService;

    @Autowired
    DfLiableManService dfLiableManService;

    @Autowired
    DfAuditDetailService dfAuditDetailService;

    @Autowired
    DfApprovalTimeService dfApprovalTimeService;

    @Autowired
    DfFlowDataService dfFlowDataService;

    @Autowired
    Environment env;

//    @Scheduled(initialDelay = 10000,fixedDelay = 60000)
     @Scheduled(cron = "0 15 10 ? * MON")
    public void checkProcessYield() throws ParseException {
         if(env.getProperty("CcYieldWarn","N").equals("Y")){
             QueryWrapper<DfProcessProjectConfig> qw = new QueryWrapper<>();
             qw.like("type", "尺寸");
             qw.orderByAsc("sort");
             List<DfProcessProjectConfig> processList = dfProcessProjectConfigService.list(qw);
             QueryWrapper<DfYieldWarn> qw2 = new QueryWrapper<>();
             qw2.eq("type", "尺寸");
             qw2.eq("name", "直通预警");
             qw2.last("limit 1");
             DfYieldWarn yield = dfYieldWarnService.getOne(qw2);
             DecimalFormat decimalFormat = new DecimalFormat("#0.00");
             QueryWrapper<DfSizeDetail> qw3 = new QueryWrapper<>();
             qw3.between("create_time",  TimeUtil.getBeforeDay(7),TimeUtil.getYesterday() + " 23:59:59");
             Rate rate=dfSizeDetailService.getAllProcessYield(qw3);

             double processYield = rate.getRate() * 100.0;
             if (processYield>0 &&  processYield< Double.valueOf(yield.getAlarmValue()+"") ||  processYield < Double.valueOf(yield.getPrewarningValue()+"")) {
                 QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                 sqw.eq("type", "sizeProcessYield");
                 if (TimeUtil.getBimonthly() == 0) {
                     sqw.like("bimonthly", "双月");
                 } else {
                     sqw.like("bimonthly", "单月");
                 }
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
                     DfAuditDetail aud = new DfAuditDetail();
                     DfFlowData fd = new DfFlowData();
                     aud.setLine("Line-23");
//                        aud.setParentId(dd.getId());
                     aud.setDataType("尺寸");
                     if (processYield < yield.getAlarmValue()) {
                         aud.setQuestionType("良率报警");
                         aud.setControlStandard("良率报警:低于" + yield.getAlarmValue() + "%");
                         aud.setQuestionName("直通良率:"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "%");
                         aud.setScenePractical("直通良率" + ":"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "% "+ "," +TimeUtil.getBeforeDayMMdd(7)+"至"+ TimeUtil.getYesterdayNoYear() +",请及时处理");
                         fd.setName("工序良率报警_" + "直通良率"  + ":"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "%"+ ","  +TimeUtil.getBeforeDayMMdd(7)+"至"+ TimeUtil.getYesterdayNoYear());
                     } else {
                         aud.setQuestionType("良率预警");
                         aud.setControlStandard("良率预警:低于" + yield.getPrewarningValue() + "%");
                         aud.setQuestionName("直通良率" + ":"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%"+ "_" + TimeUtil.getYesterdayNoYear() );
                         aud.setScenePractical("直通良率"  + ":"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%"+ "," +TimeUtil.getBeforeDayMMdd(7)+"至"+ TimeUtil.getYesterdayNoYear()+",请及时处理");
                         fd.setName("工序良率预警_" + "直通良率"  + ":"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%"+ ","  +TimeUtil.getBeforeDayMMdd(7)+"至"+ TimeUtil.getYesterdayNoYear());
                     }

                     aud.setDepartment("全工序");
                     aud.setAffectMac("0");
//                        aud.setAffectNum(1.0);

                     aud.setImpactType("尺寸");
                     aud.setIsFaca("0");
                     //问题名称和现场实际调换

//                aud.setProcess("直通良率");
                     aud.setProjectName("C27");


                     aud.setReportMan("系统");
                     aud.setCreateName("系统");

                     aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                     aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                     aud.setIpqcNumber(TimeUtil.getNowTimeLong());




                     aud.setDecisionLevel("Level1");
                     aud.setHandlingSug("全检风险批");
                     aud.setResponsible(manName.toString());
                     aud.setResponsibleId(manCode.toString());
//                    aud.setCreateName(lm.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
                     dfAuditDetailService.save(aud);


                     fd.setFlowLevel(1);
                     fd.setDataType(aud.getDataType());
                     fd.setFlowType(aud.getDataType());

                     fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
                     fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
                     fd.setCreateName(aud.getCreateName());
                     fd.setCreateUserId(aud.getCreateUserId());

                     fd.setNowLevelUser(aud.getResponsibleId());
                     fd.setNowLevelUserName(aud.getResponsible());
                     fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

                     fd.setFlowLevelName(aud.getDecisionLevel());
                     QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                     atQw.eq("type", "尺寸");
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
                     dfFlowDataService.save(fd);
                 }



             }
         }


    }
}
