package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
public class CheckOverTimeFlowData {


    @Autowired
    com.ww.boengongye.service.DfFlowDataService DfFlowDataService;

    @Autowired
    com.ww.boengongye.service.DfApprovalTimeService DfApprovalTimeService;

    @Autowired
    com.ww.boengongye.service.DfAuditDetailService DfAuditDetailService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataOvertimeService DfFlowDataOvertimeService;
    @Autowired
    com.ww.boengongye.service.DfLiableManService  dfLiableManService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataUserService dfFlowDataUserService;

    @Scheduled(initialDelay = 10000,fixedDelay = 60000)
    public void checkOverTime() throws ParseException {

//        System.out.println("检测超时审批单");
        QueryWrapper<DfFlowData>ew=new QueryWrapper<DfFlowData>();
        ew.and(wrapper -> wrapper.eq("status","待确认").or().eq("status", "待提交"));
//        ew.eq("status", "待提交");
        ew.isNull("start_timeout");
        ew.ne("data_type","设备状态");
        List<DfFlowData>datas=DfFlowDataService.list(ew);
        List<DfFlowData>saveDatas=new ArrayList<>();
        HashMap<String,Integer>approveTime=new HashMap<>();
        if(datas.size()>0){
            List<DfApprovalTime>times= DfApprovalTimeService.list();
            if(times.size()>0){
                Map<String,Integer> checkTime=new HashMap<>();
                for(DfApprovalTime t:times){
                    checkTime.put(t.getType()+"Level1ReadTime",t.getReadTimeLevel1());
                    checkTime.put(t.getType()+"Level2ReadTime",t.getReadTimeLevel2());
                    checkTime.put(t.getType()+"Level3ReadTime",t.getReadTimeLevel3());

                    checkTime.put(t.getType()+"Level1DisposeTime",t.getDisposeTimeLevel1()+t.getReadTimeLevel1());
                    checkTime.put(t.getType()+"Level2DisposeTime",t.getDisposeTimeLevel2()+t.getReadTimeLevel2());
                    checkTime.put(t.getType()+"Level3DisposeTime",t.getDisposeTimeLevel3()+t.getReadTimeLevel3());

                    approveTime.put(t.getType()+"-Level1",t.getDisposeTimeLevel1()+t.getReadTimeLevel1());
                    approveTime.put(t.getType()+"-Level2",t.getDisposeTimeLevel2()+t.getReadTimeLevel2());
                    approveTime.put(t.getType()+"-Level3",t.getDisposeTimeLevel3()+t.getReadTimeLevel3());

                }

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(DfFlowData d:datas){
//                    String getType="ReadTime";//取消阅读超时
                    String getType="DisposeTime";
                    if(d.getStatus().equals("待提交")){
                        getType="DisposeTime";
                    }
                    if(d.getFlowLevelName().equals("Level1")){

                        if(null!=d.getLevel1PushTime()){

                            long  diff=0;
//                            if(d.getStatus().equals("待确认")&&null==d.getLevel1ReadTime()) {
                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel1PushTime().getTime();
//                            }else if(d.getStatus().equals("待确认")){
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel1ReadTime().getTime();
//                            }else{
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel1ReadTime().getTime();
//                            }
//                            System.out.println(d.getId());

                            int standardTime=checkTime.containsKey(d.getFlowType()+d.getFlowLevelName()+getType)?checkTime.get(d.getFlowType()+d.getFlowLevelName()+getType):60;

                            if(diff/60/1000>=standardTime){

                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
                                //d.setFlowLevelName("Level2");
                                //d.setNowLevelUserName(ad.getResponsible2());
                                //d.setNowLevelUser(ad.getResponsibleId2());
//                                d.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                d.setOvertimeStatus("已超时");
                                //d.setNextLevelUser(ad.getResponsibleId3());

                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                }

                                saveDatas.add(d);
                                if(null!=ad&&null!=ad.getResponsibleId()){
                                    QueryWrapper fow=new QueryWrapper();
                                    fow.eq("flow_data_id",d.getDataId());
                                    fow.eq("user_account",ad.getResponsibleId());
                                    fow.last("limit 0,1");
                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
                                    if(null==already) {
                                        DfFlowDataOvertime ot = new DfFlowDataOvertime();
                                        ot.setFlowDataId(d.getDataId());
                                        ot.setUserAccount(ad.getResponsibleId());
                                        DfFlowDataOvertimeService.save(ot);
                                    }
                                }

                            }
                        }


                    }else  if(d.getFlowLevelName().equals("Level2")){
                        if(null!=d.getLevel2PushTime()){
                            long  diff=0;
//                            if(d.getStatus().equals("待确认")&&null==d.getLevel2ReadTime()) {
                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel2PushTime().getTime();
//                            }else if(d.getStatus().equals("待确认")){
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel2ReadTime().getTime();
//                            }else{
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel2ReadTime().getTime();
//                            }

//                            System.out.println(diff/60/1000);
                            int standardTime=checkTime.containsKey(d.getFlowType()+d.getFlowLevelName()+getType)?checkTime.get(d.getFlowType()+d.getFlowLevelName()+getType):60;

                            if(diff/60/1000>=standardTime){
                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
                                //d.setFlowLevelName("Level3");
                                //d.setNowLevelUserName(ad.getResponsible3());
                                //d.setNowLevelUser(ad.getResponsibleId3());
//                                d.setLevel3PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                d.setOvertimeStatus("已超时");
//                            d.setNextLevelUser(ad.getResponsibleId3());
                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                }
                                saveDatas.add(d);
                                if(null!=ad&&null!=ad.getResponsibleId2()) {
                                    QueryWrapper fow=new QueryWrapper();
                                    fow.eq("flow_data_id",d.getDataId());
                                    fow.eq("user_account",ad.getResponsibleId2());
                                    fow.last("limit 0,1");
                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
                                    if(null==already) {
                                        DfFlowDataOvertime ot = new DfFlowDataOvertime();
                                        ot.setFlowDataId(d.getDataId());
                                        ot.setUserAccount(ad.getResponsibleId2());
                                        DfFlowDataOvertimeService.save(ot);
                                    }
                                }
                            }
                        }
                    }else  if(d.getFlowLevelName().equals("Level3")){
                        if(null!=d.getLevel3PushTime()){
                            long  diff=0;
//                            if(d.getStatus().equals("待确认")&&null==d.getLevel3ReadTime()) {
                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel3PushTime().getTime();
//                            }else if(d.getStatus().equals("待确认")){
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel3ReadTime().getTime();
//                            }else{
//                                diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel3ReadTime().getTime();
//                            }
//                            System.out.println(diff/60/1000);
                            int standardTime=checkTime.containsKey(d.getFlowType()+d.getFlowLevelName()+getType)?checkTime.get(d.getFlowType()+d.getFlowLevelName()+getType):60;

                            if(diff/60/1000>=standardTime){

                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                }
                                d.setOvertimeStatus("已超时");
                                saveDatas.add(d);
                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
                                if(null!=ad&&null!=ad.getResponsibleId3()){
                                    QueryWrapper fow=new QueryWrapper();
                                    fow.eq("flow_data_id",d.getDataId());
                                    fow.eq("user_account",ad.getResponsibleId3());
                                    fow.last("limit 0,1");
                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
                                    if(null==already){
                                        DfFlowDataOvertime ot=new DfFlowDataOvertime();
                                        ot.setFlowDataId(d.getDataId());
                                        ot.setUserAccount(ad.getResponsibleId3());
                                        DfFlowDataOvertimeService.save(ot);
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
        if(saveDatas.size()>0){
            DfFlowDataService.saveOrUpdateBatch(saveDatas);
        }

        //更新处理完变成未超时的单据
        approveTime.forEach((key,value)->{
//            System.out.println(key);
//            System.out.println(value);
            UpdateWrapper<DfFlowData>uw=new UpdateWrapper<>();
            uw.set("overtime_status",null);
            uw.set("start_timeout",null);
            uw.inSql("data_id","select id from df_audit_detail where TIMESTAMPDIFF(MINUTE,report_time,end_time)< "+value);
            uw.isNotNull("start_timeout");
            uw.isNotNull("overtime_status");
            uw.eq("data_type",key.split("-")[0]);
            uw.eq("flow_level_name",key.split("-")[1]);
            DfFlowDataService.update(uw);
        });

        //超时等级升级
        QueryWrapper<DfFlowData> otlUpQw = new QueryWrapper<>();
        otlUpQw.eq("flow.data_type", "稽查")  // 稽查的
                .isNull("flow.submit_time");   // 未提交的
        List<DfFlowData> overTimeLevelUpList = DfFlowDataService.listOverTimeLevelUp(otlUpQw);
        if (overTimeLevelUpList.size() > 0) {
            for (DfFlowData dfFlowData : overTimeLevelUpList) {
                if (null != dfFlowData.getCurrentTimeoutLevel()){
                    //显示当前责任人
                    if(dfFlowData.getCurrentTimeoutLevel()>1&&(null!=dfFlowData.getProcess()&&!dfFlowData.getProcess().equals(""))){
                        QueryWrapper<DfLiableMan>lmQw=new QueryWrapper<>();
                        lmQw.eq("problem_level",dfFlowData.getCurrentTimeoutLevel());
                        lmQw.eq("type","check");
                        lmQw.like("process_name",dfFlowData.getProcess());

                        List<DfLiableMan> lmList=dfLiableManService.list(lmQw);
                        String showApproverName="";
                        if(lmList.size()>0){
                            for(DfLiableMan l:lmList){
                                showApproverName+=","+l.getLiableManName();

                                //绑定责任人
                                QueryWrapper<DfFlowDataUser> qw1 = new QueryWrapper<DfFlowDataUser>();
                                qw1.eq("flow_data_id", dfFlowData.getId());
                                qw1.eq("user_account", l.getLiableManCode());
                                DfFlowDataUser fb = dfFlowDataUserService.getOne(qw1);
                                if (null == datas) {
                                    DfFlowDataUser fdu=new DfFlowDataUser();
                                    fdu.setFlowDataId(dfFlowData.getId());
                                    fdu.setUserAccount(l.getLiableManCode());
                                    dfFlowDataUserService.save(fdu);
                                }

                            }
                        }

                        dfFlowData.setShowApprover(dfFlowData.getNowLevelUserName()+showApproverName);
                    }
                    DfFlowDataService.updateById(dfFlowData);
                }

            }
        }

    }


//    @Scheduled(initialDelay = 10000,fixedDelay = 60000)
//    public void testNg() throws ParseException {
//
//        System.out.println("检测超时审批单");
//        QueryWrapper<DfFlowData>ew=new QueryWrapper<DfFlowData>();
//        ew.and(wrapper -> wrapper.eq("status","待确认").or().eq("status", "待提交"));
//        //ew.eq("status","待确认").or().eq("status", "待提交");
//        ew.isNull("start_timeout");
//
//        List<DfFlowData>datas=DfFlowDataService.list(ew);
//        List<DfFlowData>saveDatas=new ArrayList<>();
//        if(datas.size()>0){
//            List<DfApprovalTime>times= DfApprovalTimeService.list();
//            if(times.size()>0){
//                Map<String,Integer> checkTime=new HashMap<>();
//                for(DfApprovalTime t:times){
//                    checkTime.put(t.getType()+"Level1",t.getLevel1());
//                    checkTime.put(t.getType()+"Level2",t.getLevel2());
//                    checkTime.put(t.getType()+"Level3",t.getLevel3());
//                }
//
//                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                for(DfFlowData d:datas){
//                    if(d.getFlowLevelName().equals("Level1")){
//                        if(null!=d.getLevel1PushTime()){
//                            long  diff =sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - d.getLevel1PushTime().getTime()  ;
//                            System.out.println(diff/60/1000);
//                            if(diff/60/1000>=checkTime.get(d.getFlowType()+d.getFlowLevelName())){
//
//                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
//                                //d.setFlowLevelName("Level2");
//                                //d.setNowLevelUserName(ad.getResponsible2());
//                                //d.setNowLevelUser(ad.getResponsibleId2());
//                                d.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                                d.setOvertimeStatus("已超时");
//                                //d.setNextLevelUser(ad.getResponsibleId3());
//
//                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
//                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                                }
//
//                                saveDatas.add(d);
//                                if(null!=ad&&null!=ad.getResponsibleId()){
//                                    QueryWrapper fow=new QueryWrapper();
//                                    fow.eq("flow_data_id",d.getDataId());
//                                    fow.eq("user_account",ad.getResponsibleId());
//                                    fow.last("limit 0,1");
//                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
//                                    if(null==already) {
//                                        DfFlowDataOvertime ot = new DfFlowDataOvertime();
//                                        ot.setFlowDataId(d.getDataId());
//                                        ot.setUserAccount(ad.getResponsibleId());
//                                        DfFlowDataOvertimeService.save(ot);
//                                    }
//                                }
//
//                            }
//                        }
//
//
//                    }else  if(d.getFlowLevelName().equals("Level2")){
//                        if(null!=d.getLevel2PushTime()){
//                            long  diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() -  d.getLevel2PushTime().getTime() ;
//                            System.out.println(diff/60/1000);
//                            if(diff/60/1000>=checkTime.get(d.getFlowType()+d.getFlowLevelName())){
//                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
//                                //d.setFlowLevelName("Level3");
//                                //d.setNowLevelUserName(ad.getResponsible3());
//                                //d.setNowLevelUser(ad.getResponsibleId3());
//                                d.setLevel3PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                                d.setOvertimeStatus("已超时");
////                            d.setNextLevelUser(ad.getResponsibleId3());
//                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
//                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                                }
//                                saveDatas.add(d);
//                                if(null!=ad&&null!=ad.getResponsibleId2()) {
//                                    QueryWrapper fow=new QueryWrapper();
//                                    fow.eq("flow_data_id",d.getDataId());
//                                    fow.eq("user_account",ad.getResponsibleId2());
//                                    fow.last("limit 0,1");
//                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
//                                    if(null==already) {
//                                        DfFlowDataOvertime ot = new DfFlowDataOvertime();
//                                        ot.setFlowDataId(d.getDataId());
//                                        ot.setUserAccount(ad.getResponsibleId2());
//                                        DfFlowDataOvertimeService.save(ot);
//                                    }
//                                }
//                            }
//                        }
//                    }else  if(d.getFlowLevelName().equals("Level3")){
//                        if(null!=d.getLevel3PushTime()){
//                            long  diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() -  d.getLevel3PushTime().getTime() ;
//                            System.out.println(diff/60/1000);
//                            if(diff/60/1000>=checkTime.get(d.getFlowType()+d.getFlowLevelName())){
//
//                                if(null==d.getStartTimeout()||d.getStartTimeout().equals("")){
//                                    d.setStartTimeout(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                                }
//                                d.setOvertimeStatus("已超时");
//                                saveDatas.add(d);
//                                DfAuditDetail ad=DfAuditDetailService.getById(d.getDataId());
//                                if(null!=ad&&null!=ad.getResponsibleId3()){
//                                    QueryWrapper fow=new QueryWrapper();
//                                    fow.eq("flow_data_id",d.getDataId());
//                                    fow.eq("user_account",ad.getResponsibleId3());
//                                    fow.last("limit 0,1");
//                                    DfFlowDataOvertime already=DfFlowDataOvertimeService.getOne(fow);
//                                    if(null==already){
//                                        DfFlowDataOvertime ot=new DfFlowDataOvertime();
//                                        ot.setFlowDataId(d.getDataId());
//                                        ot.setUserAccount(ad.getResponsibleId3());
//                                        DfFlowDataOvertimeService.save(ot);
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//        if(saveDatas.size()>0){
//            DfFlowDataService.saveOrUpdateBatch(saveDatas);
//        }
//
//    }
}
