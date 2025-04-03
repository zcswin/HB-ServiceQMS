package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacStatusOverTimeService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling // 1.开启定时任务
public class CheckSizeMacOverTime {
    @Value("${checkSizeStatus}")
    private int isOff;

    @Autowired
    com.ww.boengongye.service.DfSizeDetailService DfSizeDetailService;

    @Autowired
    com.ww.boengongye.service.DfMacModelPositionService DfMacModelPositionService;

    @Autowired
    com.ww.boengongye.service.DfMacStatusSizeService DfMacStatusSizeService;
    @Autowired
    com.ww.boengongye.service.DfMacStatusOverTimeService dfMacStatusOverTimeService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Environment env;

    @Scheduled(initialDelay = 10000,fixedDelay = 600000)
    public void testNg() throws ParseException {
    if(isOff==0){
        System.out.println("检测尺寸机台超时送检");


        List<DfMacModelPosition>macs=DfMacModelPositionService.list();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<DfMacStatusOverTime> oqw=new QueryWrapper<>();
        oqw.eq("type","尺寸");
        oqw.last("limit 1");
        DfMacStatusOverTime overTime= dfMacStatusOverTimeService.getOne(oqw);
        if(null!=overTime){
            for(DfMacModelPosition m:macs){
                QueryWrapper<DfSizeDetail>qw=new QueryWrapper();
                qw.eq("machine_code",m.getMachineCode());
                qw.orderByDesc("test_time");
                qw.last("limit 1");
                DfSizeDetail already=DfSizeDetailService.getOne(qw);
                int status=2;
                if(null!=already){
                    long  diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() -  already.getTestTime().getTime() ;

                    if(diff/60/1000>overTime.getStopTime()){
                        //方案一
//                        UpdateWrapper<DfMacStatusSize>uw=new UpdateWrapper<>();
//                        uw.set("StatusID_Cur",-1);
//                        uw.eq("MachineCode",m.getMachineCode());
//                        DfMacStatusSizeService.update(uw);
                        //方案二
//                        DfMacStatusSize sz=new DfMacStatusSize();
//                        sz.setMachineCode(m.getMachineCode());
//                        sz.setStatusidCur(-1);
//                        ObjectMapper mapper = new ObjectMapper();
//                        try {
//                            jmsMessagingTemplate.convertAndSend("size_status_data",mapper.writeValueAsString(sz) );
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }

                        status=-1;

                    }else if(diff/60/1000>overTime.getDebugTime()){
//                        UpdateWrapper<DfMacStatusSize>uw=new UpdateWrapper<>();
//                        uw.set("StatusID_Cur",4);
//                        uw.eq("MachineCode",m.getMachineCode());
//                        DfMacStatusSizeService.update(uw);


//                        DfMacStatusSize sz=new DfMacStatusSize();
//                        sz.setMachineCode(m.getMachineCode());
//                        sz.setStatusidCur(4);
//                        ObjectMapper mapper = new ObjectMapper();
//                        try {
//                            jmsMessagingTemplate.convertAndSend("size_status_data",mapper.writeValueAsString(sz) );
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }

                        status=4;
                    }else if(diff/60/1000>overTime.getOverTime()){
//                        UpdateWrapper<DfMacStatusSize>uw=new UpdateWrapper<>();
//                        uw.set("StatusID_Cur",10);
//                        uw.eq("MachineCode",m.getMachineCode());
//                        DfMacStatusSizeService.update(uw);

//                        DfMacStatusSize sz=new DfMacStatusSize();
//                        sz.setMachineCode(m.getMachineCode());
//                        sz.setStatusidCur(10);
//                        ObjectMapper mapper = new ObjectMapper();
//
//                        try {
//                            jmsMessagingTemplate.convertAndSend("size_status_data",mapper.writeValueAsString(sz) );
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }

                        status=10;
                    }
                    if(status!=2){
                            long diff2 = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - InitializeCheckRule.sizeStatus.get(m.getMachineCode()).getPubTime().getTime();
                            if (diff2 / 60 / 1000 > 59) {
                                DfMacStatusSize sz=new DfMacStatusSize();
                                sz.setMachineCode(m.getMachineCode());
                                sz.setStatusidCur(status);
                                ObjectMapper mapper = new ObjectMapper();

                                try {
                                    jmsMessagingTemplate.convertAndSend(env.getProperty("sizeStatusTopic"),mapper.writeValueAsString(sz) );
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                //方案三
                                DfMacStatusSize sz2  =InitializeCheckRule.sizeStatus.get(m.getMachineCode());
                                sz2.setStatusidCur(status);
                                sz2.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                InitializeCheckRule.sizeStatus.put(m.getMachineCode(),sz2);
                            }


                    }

                }
            }
        }



    }



    }
}
