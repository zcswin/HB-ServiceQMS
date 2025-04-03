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
public class CheckAppearanceProcessYield {

    @Autowired
    DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    DfYieldWarnService dfYieldWarnService;

    @Autowired
    DfQmsIpqcWaigTotalService  dfQmsIpqcWaigTotalService;

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

    @Scheduled(initialDelay = 10000,fixedDelay = 3600000)
//    @Scheduled(cron = "0 5 7 ? * *")
    public void checkProcessYield() throws ParseException {

        if(env.getProperty("WgYieldWarn","N").equals("Y")){
            QueryWrapper<DfProcessProjectConfig> qw = new QueryWrapper<>();
            qw.like("type", "外观");
            qw.orderByAsc("sort");
            List<DfProcessProjectConfig> processList = dfProcessProjectConfigService.list(qw);

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            for (DfProcessProjectConfig p : processList) {
                QueryWrapper<DfYieldWarn> qw2 = new QueryWrapper<>();
                qw2.eq("type", "外观");
                qw2.eq("name", "工序预警");
                qw2.eq("process",p.getProcessName());
                qw2.last("limit 1");
                DfYieldWarn yield = dfYieldWarnService.getOne(qw2);

                QueryWrapper<DfQmsIpqcWaigTotal> qw3 = new QueryWrapper<>();
                qw3.eq("f_seq", p.getProcessName());
//            qw3.between("f_time", TimeUtil.getYesterday(), TimeUtil.getYesterday() + " 23:59:59");
                qw3.between("f_time", TimeUtil.getBeforeDay(7), TimeUtil.getNowTimeByNormal());
                DfQmsIpqcWaigTotal data = dfQmsIpqcWaigTotalService.getTotalAndNgCount(qw3);
                if (null != data && null != data.getAffectCount() && null != data.getSpotCheckCount()&&data.getSpotCheckCount()!=0) {


                    double processYield=(Double.valueOf((data.getSpotCheckCount()-data.getAffectCount())+"")/ Double.valueOf(data.getSpotCheckCount()+"")) * 100.0;
                    if ( processYield< Double.valueOf(yield.getAlarmValue()+"") ||  processYield < Double.valueOf(yield.getPrewarningValue()+"")) {
                        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
//                    sqw.eq("type", "appearanceProcessYield");
//                    if (TimeUtil.getBimonthly() == 0) {
//                        sqw.like("bimonthly", "双月");
//                    } else {
//                        sqw.like("bimonthly", "单月");
//                    }
                        sqw.eq("type","check");
                        sqw.eq("problem_level","2");
                        sqw.like("process_name",p.getProcessName());
                        if(TimeUtil.getBimonthly()==0){
                            sqw.like("bimonthly","双月");
                        }else{
                            sqw.like("bimonthly","单月");
                        }
                        sqw.like("process_name",p.getProcessName());
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
                            aud.setDataType("外观");
                            if (processYield < yield.getAlarmValue()) {
                                aud.setQuestionType("良率报警");
                                aud.setControlStandard("良率报警:低于" + yield.getAlarmValue() + "%");
                                aud.setQuestionName("良率:"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "%");
                                aud.setScenePractical(p.getProcessName() + "_" + TimeUtil.getYesterdayNoYear() + "_工序良率:"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "%,请及时处理");
                                fd.setName("工序良率报警_" + p.getProcessName() + "_" + TimeUtil.getYesterdayNoYear() + "_工序良率:"+decimalFormat.format(processYield)+"%低于" + yield.getAlarmValue() + "%");
                            } else {
                                aud.setQuestionType("良率预警");
                                aud.setControlStandard("良率预警:低于" + yield.getPrewarningValue() + "%");
                                aud.setQuestionName(p.getProcessName() + "_" + TimeUtil.getYesterdayNoYear() + "_工序良率:"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%");
                                aud.setScenePractical(p.getProcessName() + "_" + TimeUtil.getYesterdayNoYear() + "_工序良率:"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%");
                                fd.setName("工序良率预警_" + p.getProcessName() + "_" + TimeUtil.getYesterdayNoYear() + "_工序良率:"+decimalFormat.format(processYield)+"%低于" + yield.getPrewarningValue() + "%");
                            }

                            aud.setDepartment(p.getProcessName());
                            aud.setAffectMac("0");
//                        aud.setAffectNum(1.0);

                            aud.setImpactType("外观");
                            aud.setIsFaca("0");
                            //问题名称和现场实际调换

                            aud.setProcess(p.getProcessName());
                            aud.setProjectName("C27");


                            aud.setReportMan("系统");
                            aud.setCreateName("系统");

                            aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                            aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                            aud.setIpqcNumber(TimeUtil.getNowTimeLong());




                            aud.setDecisionLevel("Level1");
                            aud.setHandlingSug("隔离,全检");
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
                            atQw.eq("type", "外观");
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

    }
}
