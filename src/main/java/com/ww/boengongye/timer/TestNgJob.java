package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.Station;
import com.ww.boengongye.service.DfMacStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.service.user.UserManagerService;
import smartbi.user.IRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@EnableScheduling // 1.开启定时任务
public class TestNgJob {
    @Value("${testNgJob}")
    private int isOff;

    @Autowired
    DfMacStatusService DfMacStatusService;

    @Scheduled(initialDelay = 10000,fixedDelay = 10000)
    public void testNg(){
        if(isOff==0){
//            System.out.println("开始ng演示");
//            UpdateWrapper<DfMacStatus>uw=new UpdateWrapper<>();
//            uw.set("StatusID_Cur","2");
//            uw.eq("StatusID_Cur",3);
//            DfMacStatusService.update(uw);
//
//            Random r=new Random();
//            int num=r.nextInt(111);
//            System.out.println("随机数:"+num);
//            UpdateWrapper<DfMacStatus>uw2=new UpdateWrapper<>();
//            uw2.set("StatusID_Cur","3");
//            uw2.eq("MachineCode","N"+num);
//            DfMacStatusService.update(uw2);

        }


    }
}
