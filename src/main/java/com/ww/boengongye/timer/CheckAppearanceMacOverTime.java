package com.ww.boengongye.timer;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.utils.HttpUtil;
import com.ww.boengongye.utils.RFIDResult;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling // 1.开启定时任务
public class CheckAppearanceMacOverTime {


    @Autowired
    com.ww.boengongye.service.DfQmsIpqcWaigTotalService DfQmsIpqcWaigTotalService;

    @Autowired
    com.ww.boengongye.service.DfMacModelPositionService DfMacModelPositionService;

    @Autowired
    com.ww.boengongye.service.DfMacStatusAppearanceService DfMacStatusAppearanceService;
    @Autowired
    com.ww.boengongye.service.DfMacStatusOverTimeService dfMacStatusOverTimeService;

    @Autowired
    com.ww.boengongye.service.DfAuditDetailService dfAuditDetailService;

    @Autowired
    Environment env;

    @Scheduled(initialDelay = 10000,fixedDelay = 60000)
    public void testNg() throws ParseException {

        System.out.println("检测外观机台超时送检");
        System.out.println(env.getProperty("RFIDWaitMaterialURL"));
        Map<String, String> headers = new HashMap<>();

        //请求RFID
//        HttpParamTime param = new HttpParamTime(60);
//        RFIDResult dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDWaitMaterialURL"), null, headers, JSONObject.toJSONString(param),false), RFIDResult.class);
//
//        //请求RFID
//        HttpParamTime param2 = new HttpParamTime(120);
//        RFIDResult tj = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDWaitMaterialURL"), null, headers, JSONObject.toJSONString(param2),false), RFIDResult.class);
//
//        Map<String,String>machineMap=new HashMap<>();
        //待料
//        if(null!=dl&&null!=dl.getData()&&null!=dl.getData().getMachineCodeList()&&dl.getData().getMachineCodeList().size()>0){
//            for(String mac:dl.getData().getMachineCodeList()){
//                machineMap.put(mac,null);
//                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
//                uw.set("StatusID_Cur", 91);
//                uw.eq("MachineCode", mac);
//                DfMacStatusAppearanceService.update(uw);
//            }
//        }
//
//        //停机
//        if(null!=tj&&null!=tj.getData()&&null!=tj.getData().getMachineCodeList()&&tj.getData().getMachineCodeList().size()>0){
//            for(String mac:tj.getData().getMachineCodeList()){
//                machineMap.put(mac,null);
//                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
//                uw.set("StatusID_Cur", -1);
//                uw.eq("MachineCode", mac);
//                DfMacStatusAppearanceService.update(uw);
//            }
//        }
        List<DfMacModelPosition>macs=DfMacModelPositionService.listJoinAppearance();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<DfMacStatusOverTime> oqw=new QueryWrapper<>();
        oqw.eq("type","外观");
        oqw.last("limit 0,1");
        DfMacStatusOverTime overTime= dfMacStatusOverTimeService.getOne(oqw);

        if(null!=overTime) {
            for (DfMacModelPosition m : macs) {

                //暂时屏蔽CNC2超时06-04
                if(m.getMachineCode().indexOf("I")==-1){
                    //                if (!machineMap.containsKey(m.getMachineCode())){
                    QueryWrapper<DfQmsIpqcWaigTotal> qw = new QueryWrapper();
                    qw.eq("f_mac", m.getMachineCode());
                    qw.orderByDesc("f_time");
                    qw.last("limit 1");
                    DfQmsIpqcWaigTotal already = DfQmsIpqcWaigTotalService.getOne(qw);
                    if (null != already) {
                        if(m.getStatus()!=3&&m.getStatus()!=90){
                            long diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - already.getfTime().getTime();
//                        System.out.println(diff / 60 / 1000);
                            if (diff / 60 / 1000 > overTime.getStopTime()) {
                                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                uw.set("StatusID_Cur", -1);
                                uw.eq("MachineCode", m.getMachineCode());
                                DfMacStatusAppearanceService.update(uw);
                            }else  if (diff / 60 / 1000 > overTime.getDebugTime()) {
                                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                uw.set("StatusID_Cur", 4);
                                uw.eq("MachineCode", m.getMachineCode());
                                DfMacStatusAppearanceService.update(uw);
                            }else  if (diff / 60 / 1000 > overTime.getOverTime()) {
                                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                uw.set("StatusID_Cur", 10);
                                uw.eq("MachineCode", m.getMachineCode());
                                DfMacStatusAppearanceService.update(uw);
                            }
                        }else {
                            //判断是否超时未处理
                            QueryWrapper<DfAuditDetail>aqw=new QueryWrapper<>();
                            aqw.eq("ad.data_type","外观");
                            aqw.eq("w.f_mac",m.getMachineCode());
                            aqw.isNull("ad.end_time");
                            aqw.eq("fd.overtime_status","已超时");
                            List<DfAuditDetail> overtimeData=dfAuditDetailService.listByCheckOverTime(aqw);
                            if(overtimeData.size()>0){
                                UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                uw.set("StatusID_Cur", 90);
                                uw.eq("MachineCode", m.getMachineCode());
                                DfMacStatusAppearanceService.update(uw);
                            }else{
                                long diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - already.getfTime().getTime();
//                        System.out.println(diff / 60 / 1000);
                                if (diff / 60 / 1000 > overTime.getStopTime()) {
                                    UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                    uw.set("StatusID_Cur", -1);
                                    uw.eq("MachineCode", m.getMachineCode());
                                    DfMacStatusAppearanceService.update(uw);
                                }else  if (diff / 60 / 1000 > overTime.getDebugTime()) {
                                    UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                    uw.set("StatusID_Cur", 4);
                                    uw.eq("MachineCode", m.getMachineCode());
                                    DfMacStatusAppearanceService.update(uw);
                                }else  if (diff / 60 / 1000 > overTime.getOverTime()) {
                                    UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
                                    uw.set("StatusID_Cur", 10);
                                    uw.eq("MachineCode", m.getMachineCode());
                                    DfMacStatusAppearanceService.update(uw);
                                }
//                                else{
//                                    UpdateWrapper<DfMacStatusAppearance> uw = new UpdateWrapper<>();
//                                    uw.set("StatusID_Cur", 2);
//                                    uw.eq("MachineCode", m.getMachineCode());
//                                    DfMacStatusAppearanceService.update(uw);
//                                }
                            }

                        }

                    }
//                }
                }



            }
        }



    }
}
