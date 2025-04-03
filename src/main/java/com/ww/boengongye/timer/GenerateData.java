package com.ww.boengongye.timer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfTeC6Cuc3Ums6Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class GenerateData {
    @Autowired
    private DfTeC6Cuc3Ums6Service dfTeC6Cuc3Ums6Service;

    private DecimalFormat df = new DecimalFormat("#.###");

    @Autowired
    private DfMacStatusDetailService dfMacStatusDetailService;

    @Autowired
    private DfMacStatusService dfMacStatusService;

    @Autowired
    private DfProcessService dfProcessService;


//    //@Async
//    //@Scheduled(cron ="0/10 * * * * ?")
//    public void autoGenInverDatas(){
//        System.out.println("执行定时任务：添加NG数据");
//        DfTeC6Cuc3Ums6 data = new DfTeC6Cuc3Ums6();
//        data.setTestTime(LocalDateTime.now());
//        data.setIndexing(0);
//        data.setResult("OK");
//        Double length = randDouble(28.64, 28.85);
//        data.setPlatformLength(length);
//        data.setPlatformWidth(randDouble(28.65, 28.85));
//        data.setPlatformCenterX(randDouble(17.58, 17.78));
//        data.setPlatformCenterY(randDouble(55.14, 55.34));
//        data.setPlatformLeftMargin(randDouble(2.53, 2.73));
//        data.setPlatformLeftMargin2(randDouble(2.53, 2.73));
//        data.setPlatformTopMargin(randDouble(2.53, 2.73));
//        data.setPlatformTopMargin2(randDouble(2.53, 2.73));
//        data.setLongsideChamfer(randDouble(0.105, 0.165));
//        data.setF03(randDouble(0.66, 0.7));
//        data.setP1(randDouble(0.66, 0.7));
//        data.setP2(randDouble(0.66, 0.7));
//        data.setP3(randDouble(0.66, 0.7));
//        data.setP4(randDouble(0.66, 0.7));
//        data.setP5(randDouble(0.66, 0.7));
//        data.setThicknessRange(randDouble(0, 0.02));
//        data.setMachineCode("Q" + (int)Math.floor(Math.random() * (241 - 131) + 131));
//        data.setTestResult("过程检OK");
//        data.setMachineStatus(null);
//
//        if (length < 28.65) {
//            data.setResult("NG");
//            data.setTestResult("过程检NG");
//        }
//        System.out.println(data);
//        dfTeC6Cuc3Ums6Service.save(data);
//
//    }
//
//    @Async
//    @Scheduled(cron ="0/10 * * * * ?")
//    @Transactional
//    public void autoGenMacStatusDetailData(){
//        System.out.println("执行定时任务：添加设备状态持续时间数据-------开始");
//        QueryWrapper<DfMacStatusDetail> qw = new QueryWrapper<>();
//        Integer[] ids = randUntilIntegers(1,220);
//        qw.in("mac.id", ids);
//        List<DfMacStatusDetail> dfMacStatusDetails = dfMacStatusDetailService.listInsertMac(qw);
//        List<DfMacStatusDetail> insertData = new ArrayList<>();
//        for (DfMacStatusDetail dfMacStatusDetail : dfMacStatusDetails) {
//            DfMacStatusDetail data = new DfMacStatusDetail();
//            data.setStatusidCur((int) (Math.random() * (2 + 1 - 1) + 1));
//            data.setStatusidPre(dfMacStatusDetail.getStatusidCur());
//            if (data.getStatusidCur() == data.getStatusidPre()) continue;
//            data.setMachineCode(dfMacStatusDetail.getMachineCode());
//            data.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
//            data.setPubTime(System.currentTimeMillis() / 1000);
//            if (dfMacStatusDetail.getCreateTime() != null) {
//                data.setStatusStep((int)(data.getPubTime() - dfMacStatusDetail.getCreateTime().getTime()/1000));
//            }
//
//            data.setTypeData(13);
//            data.setFileProgMain("o3333");
//            data.setNumProgMain("6666");
//
//            insertData.add(data);
//        }
//        if (insertData.size() > 0) {
//            insertData.get(0).setStatusidCur((int) (Math.random() * (9 + 1 - 1) + 1));
//            while (insertData.get(0).getStatusidCur() == insertData.get(0).getStatusidPre()) {
//                insertData.get(0).setStatusidCur((int) (Math.random() * (9 + 1 - 1) + 1));
//            }
//        }
//        dfMacStatusDetailService.saveBatch(insertData);
//
//        for (DfMacStatusDetail insertDatum : insertData) {
//            LambdaUpdateWrapper<DfMacStatus> qw2 = new LambdaUpdateWrapper<>();
//            qw2.eq(DfMacStatus::getMachineCode, insertDatum.getMachineCode())
//                    .set(DfMacStatus::getStatusidCur, insertDatum.getStatusidCur());
//
//            dfMacStatusService.update(qw2);
//        }
//        System.out.println("执行定时任务：添加设备状态持续时间数据-------结束");
//    }
//
//    @Async
//    @Scheduled(cron ="0/10 * * * * ?")
//    @Transactional
//    public void testProcessStatus() {
//        System.out.println("执行定时任务：更新工序状态-------开始");
//        List<DfProcess> dfProcesses = dfProcessService.listMacProcessStatus();
//        for (DfProcess dfProcess : dfProcesses) {
//            int s = dfProcess.getProcessStatus();
//            if (s == 2 || s == 4 || s == 5 || s == -1) {
//                dfProcess.setProcessStatus(2);
//            } else if (s == 6 || s == 7 || s == 8 || s == 9) {
//                dfProcess.setProcessStatus(3);
//            } else {
//                dfProcess.setProcessStatus(6);
//            }
//            System.out.println(dfProcess);
//        }
//        dfProcessService.updateBatchById(dfProcesses);
//        System.out.println("执行定时任务：更新工序状态-------结束");
//    }
//
//    private Integer[] randUntilIntegers(Integer min, Integer max) {
//        Integer[] result = new Integer[10];
//        for (int i = 0; i < result.length; i++) {
//            result[i] = (int) (Math.random() * (max + 1 - min) + min);
//        }
//        return result;
//    }
//
//    private Double randDouble(double min, double max) {
//        return Double.valueOf(df.format(Math.random() * (max - min) + min));
//    }

}
