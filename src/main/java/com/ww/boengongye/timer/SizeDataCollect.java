package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfSizeContStandService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.service.DfSizeItemNgRateService;
import com.ww.boengongye.service.DfSizeOkRateService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class SizeDataCollect {
    @Autowired
    private DfSizeItemNgRateService dfSizeItemNgRateService;

    @Autowired
    private DfSizeOkRateService dfSizeOkRateService;

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Autowired
    private DfSizeContStandService dfSizeContStandService;

    @Autowired
    private com.ww.boengongye.service.DfSizeFailService DfSizeFailService;
    @Autowired
    private com.ww.boengongye.service.DfSizeCheckItemInfosService DfSizeCheckItemInfosService;
    @Autowired
    private com.ww.boengongye.service.DfProcessService DfProcessService;
    @Autowired
    private com.ww.boengongye.service.DfSizeNgDataService DfSizeNgDataService;

    @Autowired
    private com.ww.boengongye.service.DfMacStatusSizeService DfMacStatusSizeService;

    // 尺寸ng TOP数据更新
    @Async
    @Scheduled(cron ="55 9/10 * * * ?")
    public void sizeNgTopUpdate() throws ParseException {

        String startTime;
        String endTime;
        String dayOrNight;
        int hour = new Date().getHours();
        if (hour < 7) {
            startTime = TimeUtil.getYesterday() + " 19:00:01";
            endTime = TimeUtil.getNowTimeNoHour() + " 07:00:00";
            dayOrNight = "B";
        } else if (hour < 19){
            startTime = TimeUtil.getNowTimeNoHour() + " 07:00:01";
            endTime = TimeUtil.getNowTimeNoHour() + " 19:00:00";
            dayOrNight = "A";
        } else {
            startTime = TimeUtil.getNowTimeNoHour() + " 19:00:01";
            endTime = TimeUtil.getNextDay(TimeUtil.getNowTimeNoHour()) + " 07:00:00";
            dayOrNight = "B";
        }
        System.out.println("======================尺寸看板NGTOP数据汇总开始 " + TimeUtil.getNowTimeByNormal() + "========================");
        List<DfSizeItemNgRate> dfSizeItemNgRates = dfSizeItemNgRateService.listSizeItemNgRate(startTime, endTime);

        QueryWrapper<DfSizeItemNgRate> deleteQw = new QueryWrapper<>();
        deleteQw.between("create_time", startTime, endTime);
            dfSizeItemNgRateService.remove(deleteQw);  // 删除今天的数据
        if (dfSizeItemNgRates.size() > 0) {
            for (DfSizeItemNgRate dfSizeItemNgRate : dfSizeItemNgRates) {
                dfSizeItemNgRate.setCreateTime(Timestamp.valueOf(startTime));
                dfSizeItemNgRate.setDayOrNight(dayOrNight);
                //System.out.println(dfSizeItemNgRate);
            }
            dfSizeItemNgRateService.saveBatch(dfSizeItemNgRates);   // 添加今天的数据
        }

        System.out.println("======================尺寸看板NGTOP数据汇总结束 " + TimeUtil.getNowTimeByNormal() + "========================");
    }

    // 尺寸每日良率数据更新
    @Async
//    @Scheduled(cron ="55 * * * * ?")
    @Scheduled(cron ="55 9/10 * * * ?")
    public void sizeOkRateUpdate() throws ParseException {

        String startTime;
        String endTime;
        int hour = new Date().getHours();
        if (hour < 7) {
            startTime = TimeUtil.getYesterday() + " 07:00:01";
            endTime = TimeUtil.getNowTimeNoHour() + " 07:00:00";
        } else {
            startTime = TimeUtil.getNowTimeNoHour() + " 07:00:01";
            endTime = TimeUtil.getNextDay(TimeUtil.getNowTimeNoHour()) + " 07:00:00";
        }
        System.out.println("======================尺寸看板良率数据汇总开始 " + TimeUtil.getNowTimeByNormal() + "========================");
        QueryWrapper<DfSizeOkRate> qw = new QueryWrapper<>();
        qw.between("test_time", startTime, endTime);

        List<DfSizeOkRate> dfSizeOkRates = dfSizeOkRateService.listSizeOkRate(qw);

        QueryWrapper<DfSizeOkRate> deleteQw = new QueryWrapper<>();
        deleteQw.between("create_time", startTime, endTime);
        dfSizeOkRateService.remove(deleteQw);  // 删除今天的数据
        if (dfSizeOkRates.size() > 0) {
            for (DfSizeOkRate dfSizeOkRate : dfSizeOkRates) {
                dfSizeOkRate.setCreateTime(Timestamp.valueOf(startTime));
                //System.out.println(dfSizeOkRate);
            }
            dfSizeOkRateService.saveBatch(dfSizeOkRates);   // 添加今天的数据
        }

        System.out.println("======================尺寸看板良率数据汇总结束 " + TimeUtil.getNowTimeByNormal() + "========================");
    }

}
