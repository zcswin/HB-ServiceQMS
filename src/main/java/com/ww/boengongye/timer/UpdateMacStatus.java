package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
public class UpdateMacStatus {
    @Autowired
    DfMacStatusService dfMacStatusService;

    @Scheduled(initialDelay = 10000,fixedDelay = 60000)
    public void saveMacStatus() throws ParseException {
//        System.out.println("定时更新机台状态start:"+TimeUtil.getNowTimeByNormal());
        List<DfMacStatus>data=new ArrayList<>();
        for (Map.Entry<String, DfMacStatus> entry : InitializeCheckRule.macStatus.entrySet()) {
                data.add(entry.getValue());
        }
        dfMacStatusService.updateBatchById(data);
//        System.out.println("定时更新机台状态end:"+TimeUtil.getNowTimeByNormal());
    }


}
