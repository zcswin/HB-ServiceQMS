package com.ww.boengongye.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * mq接收砂轮报文
 */
@Component
public class GrindingWheelMq {
    @Autowired
    private DfYieldWarnService dfYieldWarnService;
    @Autowired
    private DfAuditDetailService dfAuditDetailService;
    @Autowired
    private DfFlowDataService dfFlowDataService;
    @Autowired
    private DfProcessService dfProcessService;
    @Autowired
    private DfLiableManService dfLiableManService;
    @Autowired
    private DfApprovalTimeService dfApprovalTimeService;
    @Autowired
    private DfKnifeUseInfoService dfKnifeUseInfoService;
    @Autowired
    private DfKnifeUseNumberService dfKnifeUseNumberService;
    @Autowired
    private Environment env;

    @JmsListener(destination = "${GrindingWheelTopic}", containerFactory = "jtJmsListenerContainerFactoryTopic")
    @Transactional(rollbackFor = Exception.class)
    public void consume(final String msg) {
        System.out.println("砂轮寿命预警 start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();
        //json字符串转砂轮对象
        GrindingWheel grindingWheel = JSON.parseObject(msg, com.ww.boengongye.entity.GrindingWheel.class);

        //从yield_warn获取预警和报警范围
        UpdateWrapper<DfYieldWarn> ew = new UpdateWrapper<>();
        ew.eq("type","砂轮" );
        DfYieldWarn warn = dfYieldWarnService.getOne(ew);
        //报文状态码
        String typeData = grindingWheel.getTypeData();

        switch (typeData){
            case "41":
                //通过机台编号的首字母查找对应的工序
                String firstCode1 = grindingWheel.getMachineCode().substring(0, 1);
                QueryWrapper<DfProcess> dfProcessQueryWrapper = new QueryWrapper<>();
                dfProcessQueryWrapper.eq("first_code",firstCode1);
                DfProcess dfProcess1 = dfProcessService.getOne(dfProcessQueryWrapper);
                //工序名称
                String process1 = dfProcess1.getProcessName();

                //根据机台编号首字母查对应工序,dtType是1的时候为换刀
                if ("1".equals(grindingWheel.getDtType())){//表示换刀
                    QueryWrapper<DfKnifeUseInfo> knifeWrapperOld = new QueryWrapper<>();
                    knifeWrapperOld
                            .eq("machine_code",grindingWheel.getMachineCode())
                            .eq("mac_tool_index", grindingWheel.getMacToolIndex())
                            .orderByDesc("create_time")
                            .last("limit 1");
                    //获取被换刀具信息
                    DfKnifeUseInfo knifeOld = dfKnifeUseInfoService.getOne(knifeWrapperOld);
                    if (knifeOld!=null&&knifeOld.getId()!=null){
                        //找到最新的切削次数
                        QueryWrapper<DfKnifeUseNumber> ew2 = new QueryWrapper<>();
                        ew2.eq("machine_code",grindingWheel.getMachineCode())
                                .eq("mac_tool_index", grindingWheel.getMacToolIndex())
                                .orderByDesc("create_time")
                                .last("limit 1");
                        DfKnifeUseNumber dfKnifeUseNumber = dfKnifeUseNumberService.getOne(ew2);

                        if (dfKnifeUseNumber != null&&dfKnifeUseNumber.getId()!=null){
                            knifeOld.setLifeAct(dfKnifeUseNumber.getLifeAct());
                            knifeOld.setDtType("2");
                            //修改被换刀具的信息
                            dfKnifeUseInfoService.updateById(knifeOld);

                            //关闭任务单
                            QueryWrapper<DfAuditDetail> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("tool_code",knifeOld.getToolCode());
                            List<DfAuditDetail> list = dfAuditDetailService.list(queryWrapper);
                            //根据编号可能找到多个任务,都设置endTime
                            for (DfAuditDetail dfAuditDetail : list) {
                                dfAuditDetail.setEndTime(Timestamp.valueOf(checkTime));
                                dfAuditDetail.setFa("砂轮寿命到期预警");
                                dfAuditDetail.setCa("更换砂轮");
                                dfAuditDetailService.updateById(dfAuditDetail);

                                //更新flow_data
                                UpdateWrapper<DfFlowData> flowDataUpdateWrapper = new UpdateWrapper<>();
                                DfFlowData dfFlowData = new DfFlowData();
                                dfFlowData.setStatus("已关闭");
                                flowDataUpdateWrapper.eq("data_id", dfAuditDetail.getId());
                                dfFlowDataService.update(dfFlowData,flowDataUpdateWrapper);
                            }
                        }
                    }
                }

                DfKnifeUseInfo dfKnifeUseInfo = new DfKnifeUseInfo();
                dfKnifeUseInfo.setToolCode(grindingWheel.getToolCode());
                dfKnifeUseInfo.setMachineCode(grindingWheel.getMachineCode());
                dfKnifeUseInfo.setMacToolIndex(grindingWheel.getMacToolIndex());
                dfKnifeUseInfo.setDtType(grindingWheel.getDtType());
                dfKnifeUseInfo.setDtOp(grindingWheel.getDtOp());
                dfKnifeUseInfo.setPubTime(grindingWheel.getPubTime());
                dfKnifeUseInfo.setProcess(process1);
                //保存刀具信息
                dfKnifeUseInfoService.save(dfKnifeUseInfo);
                break;
            case "43":
                //获取对应机台号对应的最新的(正在使用的)刀具
                QueryWrapper<DfKnifeUseInfo> ew2 = new QueryWrapper<>();
                ew2.eq("machine_code",grindingWheel.getMachineCode())
                        .eq("mac_tool_index", grindingWheel.getMacToolIndex())
                        .orderByDesc("create_time")
                        .last("limit 1");
                DfKnifeUseInfo knifeUseInfo = dfKnifeUseInfoService.getOne(ew2);
                if (knifeUseInfo==null||knifeUseInfo.getId()==null){
                    System.out.println("机台"+grindingWheel.getMachineCode()+"_刀号"+ grindingWheel.getMacToolIndex()+"不存在刀具");
                    return;
                }

                DfKnifeUseNumber dfKnifeUseNumber = new DfKnifeUseNumber();
                dfKnifeUseNumber.setMachineCode(grindingWheel.getMachineCode());
                dfKnifeUseNumber.setMacToolIndex(grindingWheel.getMacToolIndex());
                dfKnifeUseNumber.setLifeMax(grindingWheel.getLifeMax());
                dfKnifeUseNumber.setLifeAlarm(grindingWheel.getLifeAlarm());
                dfKnifeUseNumber.setLifeAct(grindingWheel.getLifeAct());
                dfKnifeUseNumber.setCheckTime(Timestamp.valueOf(checkTime));
                dfKnifeUseNumber.setPubTime(grindingWheel.getPubTime());
                dfKnifeUseNumber.setProcess(knifeUseInfo.getProcess());
                dfKnifeUseNumberService.save(dfKnifeUseNumber);


                QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
                sqw.like("process_name",knifeUseInfo.getProcess());
                sqw.eq("type","砂轮");
                sqw.eq("problem_level","1");
                if(TimeUtil.getBimonthly()==0){
                    sqw.like("bimonthly","双月");
                }else{
                    sqw.like("bimonthly","单月");
                }
                List<DfLiableMan> lm =dfLiableManService.list(sqw);
                if (lm==null||lm.size()==0){
                    return;
                }
                //责任人编号
                StringBuilder manCode=new StringBuilder();
                //责任人
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

                //砂轮结果是否推送RFID(Y/N)
                if (!"Y".equals(env.getProperty("isPushKnifeResult"))){
                    return;
                }

                //大于触发报警
                if (grindingWheel.getLifeAct() > warn.getAlarmValue()) {
                    DfAuditDetail aud = new DfAuditDetail();
                    aud.setDataType("砂轮寿命");
                    aud.setDepartment("砂轮寿命");
                    aud.setAffectMac("1");
                    aud.setAffectNum(1.0);
                    aud.setControlStandard("砂轮寿命报警:大于" + warn.getAlarmValue());
                    aud.setImpactType("砂轮寿命");
                    aud.setIsFaca("0");
                    aud.setQuestionName("砂轮寿命报警");
                    aud.setScenePractical("工序"+knifeUseInfo.getProcess()+"_机台号" +knifeUseInfo.getMachineCode() +"_刀号"+ knifeUseInfo.getMacToolIndex()+"_刀具切削次数"+grindingWheel.getLifeAct()+"_砂轮寿命报警");
                    aud.setReportMan("系统");
                    aud.setCreateName("系统");

                    aud.setReportTime(Timestamp.valueOf(checkTime));
                    aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
                    aud.setIpqcNumber(checkTimeLong);

                    aud.setProcess(knifeUseInfo.getProcess());
                    aud.setQuestionType("砂轮寿命");
                    aud.setDecisionLevel("Level1");
                    aud.setHandlingSug("全检风险批");
                    aud.setResponsible(manName.toString());
                    aud.setResponsibleId(manCode.toString());
                    aud.setMacCode(knifeUseInfo.getMachineCode());
                    aud.setnNumTool(knifeUseInfo.getMacToolIndex().toString());
                    aud.setToolCode(knifeUseInfo.getToolCode());
                    dfAuditDetailService.save(aud);

                    DfFlowData fd = new DfFlowData();
                    fd.setFlowLevel(1);
                    fd.setDataType(aud.getDataType());
                    fd.setFlowType(aud.getDataType());
                    fd.setName("IPQC_砂轮寿命_" + aud.getQuestionName() + "_NG_" + checkTime);
                    fd.setDataId(aud.getId());
                    fd.setStatus("待确认");
                    fd.setCreateName("系统");
                    fd.setCreateUserId("系统");
                    fd.setNowLevelUser(aud.getResponsibleId());
                    fd.setNowLevelUserName(aud.getResponsible());
                    fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
                    fd.setFlowLevelName(aud.getDecisionLevel());
                    QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
                    atQw.eq("type","砂轮")
                            .last("limit 1");
                    DfApprovalTime at= dfApprovalTimeService.getOne(atQw);
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
                    dfFlowDataService.save(fd);

                } else if (grindingWheel.getLifeAct() > warn.getPrewarningValue() && grindingWheel.getLifeAct() <= warn.getAlarmValue()) {
                    //两者之间触发预警
                    //触发预警
                    DfAuditDetail aud = new DfAuditDetail();
                    aud.setDataType("砂轮寿命");
                    aud.setDepartment("砂轮寿命");
                    aud.setAffectMac("1");
                    aud.setAffectNum(1.0);
                    aud.setControlStandard("砂轮寿命预警:大于" + warn.getPrewarningValue() + "小于等于" + warn.getAlarmValue());
                    aud.setImpactType("砂轮寿命");
                    aud.setIsFaca("0");
                    aud.setQuestionName("砂轮寿命预警");
                    aud.setScenePractical("工序"+knifeUseInfo.getProcess()+"_机台号" +knifeUseInfo.getMachineCode() +"_刀号"+ knifeUseInfo.getMacToolIndex()+"_刀具切削次数"+grindingWheel.getLifeAct()+"_砂轮寿命预警");
                    aud.setReportMan("系统");
                    aud.setCreateName("系统");

                    aud.setReportTime(Timestamp.valueOf(checkTime));
                    aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
                    aud.setIpqcNumber(checkTimeLong);

                    aud.setProcess(knifeUseInfo.getProcess());
                    aud.setQuestionType("砂轮寿命");
                    aud.setDecisionLevel("Level1");
                    aud.setHandlingSug("全检风险批");
                    aud.setResponsible(manName.toString());
                    aud.setResponsibleId(manCode.toString());
                    aud.setMacCode(knifeUseInfo.getMachineCode());
                    aud.setnNumTool(knifeUseInfo.getMacToolIndex().toString());
                    aud.setToolCode(knifeUseInfo.getToolCode());
                    dfAuditDetailService.save(aud);

                    DfFlowData fd = new DfFlowData();
                    fd.setFlowLevel(1);
                    fd.setDataType(aud.getDataType());
                    fd.setFlowType(aud.getDataType());
                    fd.setName("IPQC_砂轮寿命_" + aud.getQuestionName() + "_NG_" + checkTime);
                    fd.setDataId(aud.getId());
                    fd.setStatus("待确认");

                    fd.setCreateName("系统");
//                    fd.setCreateUserId("系统");
                    fd.setNowLevelUser(aud.getResponsibleId());
                    fd.setNowLevelUserName(aud.getResponsible());
                    fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
                    fd.setFlowLevelName(aud.getDecisionLevel());
                    QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
                    atQw.eq("type","砂轮")
                            .last("limit 1");
                    DfApprovalTime at= dfApprovalTimeService.getOne(atQw);
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
                    dfFlowDataService.save(fd);
                }
                break;
        }
    }
}
