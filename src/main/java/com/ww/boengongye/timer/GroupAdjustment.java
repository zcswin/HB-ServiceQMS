package com.ww.boengongye.timer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class GroupAdjustment {

    @Autowired
    private DfAdMacOkRateService dfAdMacOkRateService;

    @Autowired
    private DfAdSizeNgRateService dfAdSizeNgRateService;

    @Autowired
    private DfGroupService dfGroupService;

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Autowired
    private DfGroupMacOvertimeService dfGroupMacOvertimeService;

    @Autowired
    private DfGroupAdjustmentService dfGroupAdjustmentService;

    @Autowired
    private DfMacRetestService dfMacRetestService;

    @Autowired
    private DfFaiPassRateService dfFaiPassRateService;

    @Autowired
    private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Autowired
    private DfTeRorService dfTeRorService;

    @Autowired
    private DfGroupOkRateService dfGroupOkRateService;

    @Autowired
    private DfGroupCloseService dfGroupCloseService;

    @Autowired
    private DfGroupMacNgRateService dfGroupMacNgRateService;
    //@Async
    ////@Scheduled(cron ="0 0/1 * * * ?")
    //@Scheduled(cron ="0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * ?")
    //@Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    ////@Transactional
    public void twoHours() throws ParseException {
        System.out.println("开始----定时任务：1,3,5,7,9,11,13,15,17,19,21,23点执行一次");

        System.out.println("===========更新小组超时送检（开始）===========");
        //Timestamp now = new Timestamp((Timestamp.valueOf(TimeUtil.getNowTimeByNormal()).getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0,10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19){
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }

        System.out.println("开始时间：" + startTime);
        System.out.println("结束时间：" + endTime);


        // 获取当前时间段的机台数据
        String littleStartTime;
        String littleEndTime = now.toString();
        String inTheTime; // 时间段

        // 需要更新的时间
        String updateStartTestTime;
        String updateEndTestTime;
        // 往前推六个小时
        String bigStartTime;
        switch (now.getHours()) {
            case 0:
                littleStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 21:00:00";
                updateEndTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 19:00:00";
                inTheTime = "23-1"; break;
            case 2:
                littleStartTime = today + " 01:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateEndTestTime = today + " 01:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 21:00:00";
                inTheTime = "1-3"; break;
            case 4:
                littleStartTime = today + " 03:00:00";
                updateStartTestTime = today + " 01:00:00";
                updateEndTestTime = today + " 03:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                inTheTime = "3-5"; break;
            case 6:
                littleStartTime = today + " 05:00:00";
                updateStartTestTime = today + " 03:00:00";
                updateEndTestTime = today + " 05:00:00";
                bigStartTime = today + " 01:00:00";
                inTheTime = "5-7"; break;
            case 8:
                littleStartTime = today + " 07:00:00";
                updateStartTestTime = today + " 05:00:00";
                updateEndTestTime = today + " 07:00:00";
                bigStartTime = today + " 03:00:00";
                inTheTime = "7-9"; break;
            case 10:
                littleStartTime = today + " 09:00:00";
                updateStartTestTime = today + " 07:00:00";
                updateEndTestTime = today + " 09:00:00";
                bigStartTime = today + " 05:00:00";
                inTheTime = "9-11"; break;
            case 12:
                littleStartTime = today + " 11:00:00";
                updateStartTestTime = today + " 09:00:00";
                updateEndTestTime = today + " 11:00:00";
                bigStartTime = today + " 07:00:00";
                inTheTime = "11-13"; break;
            case 14:
                littleStartTime = today + " 13:00:00";
                updateStartTestTime = today + " 11:00:00";
                updateEndTestTime = today + " 13:00:00";
                bigStartTime = today + " 09:00:00";
                inTheTime = "13-15"; break;
            case 16:
                littleStartTime = today + " 15:00:00";
                updateStartTestTime = today + " 13:00:00";
                updateEndTestTime = today + " 15:00:00";
                bigStartTime = today + " 11:00:00";
                inTheTime = "15-17"; break;
            case 18:
                littleStartTime = today + " 17:00:00";
                updateStartTestTime = today + " 15:00:00";
                updateEndTestTime = today + " 17:00:00";
                bigStartTime = today + " 13:00:00";
                inTheTime = "17-19"; break;
            case 20:
                littleStartTime = today + " 19:00:00";
                updateStartTestTime = today + " 17:00:00";
                updateEndTestTime = today + " 19:00:00";
                bigStartTime = today + " 15:00:00";
                inTheTime = "19-21"; break;
            case 22:
                littleStartTime = today + " 21:00:00";
                updateStartTestTime = today + " 19:00:00";
                updateEndTestTime = today + " 21:00:00";
                bigStartTime = today + " 17:00:00";
                inTheTime = "21-23"; break;
            default:
                littleStartTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateStartTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateEndTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                bigStartTime = null;
                inTheTime = "0-0";
        }
        QueryWrapper<DfSizeDetail> littleqw = new QueryWrapper<>();  //  -- 尺寸
        littleqw.between("create_time", littleStartTime, littleEndTime);
        List<DfSizeDetail> littleMachineCode = dfSizeDetailService.listMachineCode(littleqw);
        QueryWrapper<DfQmsIpqcWaigTotal> littleqwAppear = new QueryWrapper<>();  //  -- 外观
        littleqwAppear.between("f_time", littleStartTime, littleEndTime);
        List<DfQmsIpqcWaigTotal> littleMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(littleqwAppear);

        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取当班的机台数据
        QueryWrapper<DfSizeDetail> allqw = new QueryWrapper<>();  //  -- 尺寸
        allqw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allMachineCode = dfSizeDetailService.listMachineCode(allqw);
        QueryWrapper<DfQmsIpqcWaigTotal> allqwAppear = new QueryWrapper<>();  //  -- 外观
        allqwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(allqwAppear);

        // 获取小组当天开机数
        Map<Integer, Integer> groupIdResAllMacNum = new HashMap<>();  //  -- 尺寸
        for (DfSizeDetail allData : allMachineCode) {
            String machineCode = allData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResAllMacNumAppear = new HashMap<>();  //  -- 外观
        for (DfQmsIpqcWaigTotal allData : allMachineCodeAppear) {
            String machineCode = allData.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取小组当前时间段的送检数
        Map<Integer, Integer> groupIdResLittleMacNum = new HashMap<>();
        for (DfSizeDetail littleData : littleMachineCode) {
            String machineCode = littleData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResLittleMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResLittleMacNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal littleData : littleMachineCodeAppear) {
            String machineCode = littleData.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResLittleMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取小组超时数据
        List<DfGroupMacOvertime> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResAllMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupMacOvertime data = new DfGroupMacOvertime();
            Integer groupOpenMacNum = entry.getValue();  // 小组开机总数
            Integer groupCheckMacNum = groupIdResLittleMacNum.get(groupId) == null ? 0 : groupIdResLittleMacNum.get(groupId);  // 小组时间段送检数
            Integer groupOverTimeMacNum = groupOpenMacNum - groupCheckMacNum; // 小组机台超时送检数
            Double overTimeRate = groupOverTimeMacNum.doubleValue() / groupOpenMacNum;
            data.setGroupId(groupId);
            data.setAllMacNum(groupOpenMacNum);
            data.setOvertimeMacNum(groupOverTimeMacNum);
            data.setOvertimeRate(overTimeRate);
            data.setIntheTime(inTheTime);
            data.setTestTime(now);
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸

            list.add(data);
        }
        for (Map.Entry<Integer, Integer> entry : groupIdResAllMacNumAppear.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupMacOvertime data = new DfGroupMacOvertime();
            Integer groupOpenMacNum = entry.getValue();  // 小组开机总数
            Integer groupCheckMacNum = groupIdResLittleMacNumAppear.get(groupId) == null ? 0 : groupIdResLittleMacNumAppear.get(groupId);  // 小组时间段送检数
            Integer groupOverTimeMacNum = groupOpenMacNum - groupCheckMacNum; // 小组机台超时送检数
            Double overTimeRate = groupOverTimeMacNum.doubleValue() / groupOpenMacNum;
            data.setGroupId(groupId);
            data.setAllMacNum(groupOpenMacNum);
            data.setOvertimeMacNum(groupOverTimeMacNum);
            data.setOvertimeRate(overTimeRate);
            data.setIntheTime(inTheTime);
            data.setTestTime(now);
            data.setDayOrNight(dayOrNight);
            data.setTestType(2);  // 2-外观

            list.add(data);
        }
        dfGroupMacOvertimeService.saveBatch(list);

        // 更新前四个小时到两个小时的数据
        // 获取前6个小时的机台数据
        QueryWrapper<DfSizeDetail> bigqw = new QueryWrapper<>();  // -- 尺寸
        bigqw.between("create_time", bigStartTime, endTime);
        List<DfSizeDetail> bigMachineCode = dfSizeDetailService.listMachineCode(bigqw);
        Map<Integer, Integer> groupIdResBigMacNum = new HashMap<>();
        for (DfSizeDetail bigData : bigMachineCode) {
            String machineCode = bigData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResBigMacNum.merge(groupId, 1, Integer::sum);
        }
        QueryWrapper<DfQmsIpqcWaigTotal> bigqwAppear = new QueryWrapper<>();  // -- 外观
        bigqwAppear.between("f_time", bigStartTime, endTime);
        List<DfQmsIpqcWaigTotal> bigMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(bigqwAppear);
        Map<Integer, Integer> groupIdResBigMacNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal bigDataAppear : bigMachineCodeAppear) {
            String machineCode = bigDataAppear.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResBigMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取前四个小时到两个小时的数据
        QueryWrapper<DfGroupMacOvertime> updateOverTimeQw = new QueryWrapper<>();  // -- 尺寸
        updateOverTimeQw.between("test_time", updateStartTestTime, updateEndTestTime)
                .eq("test_type", 1);
        List<DfGroupMacOvertime> updateOverTime = dfGroupMacOvertimeService.list(updateOverTimeQw);
        for (DfGroupMacOvertime dfGroupMacOvertime : updateOverTime) {
            Integer groupId = dfGroupMacOvertime.getGroupId();
            if (groupId == null) continue;
            // 当班开机数
            Integer openNum = groupIdResAllMacNum.get(groupId) == null ? 0 : groupIdResAllMacNum.get(groupId);
            // 六小时送检机台数
            Integer bigNum = groupIdResBigMacNum.get(groupId) == null ? 0 : groupIdResBigMacNum.get(groupId);
            // 停机数
            Integer stopNum = Math.max(0, openNum - bigNum);

            dfGroupMacOvertime.setAllMacNum(Math.max(0, dfGroupMacOvertime.getAllMacNum() - stopNum));
            dfGroupMacOvertime.setOvertimeMacNum(Math.max(0, dfGroupMacOvertime.getOvertimeMacNum() - stopNum));
            dfGroupMacOvertime.setOvertimeRate(Math.max(0, dfGroupMacOvertime.getOvertimeMacNum().doubleValue() / dfGroupMacOvertime.getAllMacNum()));
        }
        if (!updateOverTime.isEmpty()) {
            dfGroupMacOvertimeService.updateBatchById(updateOverTime);
        }
        QueryWrapper<DfGroupMacOvertime> updateOverTimeQwAppear = new QueryWrapper<>();  // -- 外观
        updateOverTimeQwAppear.between("test_time", updateStartTestTime, updateEndTestTime)
                .eq("test_type", 2);
        List<DfGroupMacOvertime> updateOverTimeAppear = dfGroupMacOvertimeService.list(updateOverTimeQw);
        for (DfGroupMacOvertime dfGroupMacOvertime : updateOverTimeAppear) {
            Integer groupId = dfGroupMacOvertime.getGroupId();
            if (groupId == null) continue;
            // 当班开机数
            Integer openNum = groupIdResAllMacNumAppear.get(groupId) == null ? 0 : groupIdResAllMacNumAppear.get(groupId);
            // 六小时送检机台数
            Integer bigNum = groupIdResBigMacNumAppear.get(groupId) == null ? 0 : groupIdResBigMacNumAppear.get(groupId);
            // 停机数
            Integer stopNum = Math.max(0, openNum - bigNum);

            dfGroupMacOvertime.setAllMacNum(dfGroupMacOvertime.getAllMacNum() - stopNum);
            dfGroupMacOvertime.setOvertimeMacNum(dfGroupMacOvertime.getOvertimeMacNum() - stopNum);
            dfGroupMacOvertime.setOvertimeRate(dfGroupMacOvertime.getOvertimeMacNum().doubleValue() / dfGroupMacOvertime.getAllMacNum());
        }
        if (!updateOverTimeAppear.isEmpty()) {
            dfGroupMacOvertimeService.updateBatchById(updateOverTime);
        }

        System.out.println("===========更新小组超时送检（结束）===========");

        System.out.println("===========更新小组调机能力（开始）===========");
        // 更新小组调机能力
        // 小组机总台数
        Map<Integer, Integer> groupIdResMacNum = dfGroupService.getGroupMacNum(month, dayOrNight);
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();  // -- 尺寸
        qw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allSizeDetail = dfSizeDetailService.list(qw);
        QueryWrapper<DfQmsIpqcWaigTotal> qwAppear = new QueryWrapper<>();  // -- 外观
        qwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allAppear = dfQmsIpqcWaigTotalService.list(qwAppear);
        // 统计小组当班隔离、调机、正常数
        Map<Integer, Integer> quaranNum = new HashMap<>();  // -- 尺寸
        Map<Integer, Integer> adjustNum = new HashMap<>();
        Map<Integer, Integer> normalNum = new HashMap<>();
        for (DfSizeDetail sizeDetail : allSizeDetail) {
            Integer groupId = macResGroupId.get(sizeDetail.getMachineCode());
            if (groupId == null) continue;
            if (sizeDetail.getInfoResult() == null) continue;
            switch (sizeDetail.getInfoResult()) {
                case "NG": quaranNum.merge(groupId, 1, Integer::sum); break;
                case "TJ": adjustNum.merge(groupId, 1, Integer::sum); break;
                case "OK": normalNum.merge(groupId, 1, Integer::sum); break;
                default:
            }
        }
        Map<Integer, Integer> quaranNumAppear = new HashMap<>();  // -- 外观
        Map<Integer, Integer> normalNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal appear : allAppear) {
            Integer groupId = macResGroupId.get(appear.getfMac());
            if (groupId == null) continue;
            if (appear.getStatus() == null) continue;
            switch (appear.getStatus()) {
                case "NG": quaranNumAppear.merge(groupId, appear.getAffectCount(), Integer::sum); break;
                case "OK": normalNumAppear.merge(groupId, appear.getAffectCount(), Integer::sum); break;
                default:
            }
        }
        // 统计小组调机能力
        List<DfGroupAdjustment> adjustList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (groupId == null) continue;
            DfGroupAdjustment data = new DfGroupAdjustment();
            Integer allMacNum = entry.getValue();
            Integer todayOpenNum = groupIdResAllMacNum.get(groupId) == null ? 0 : groupIdResAllMacNum.get(groupId);
            Integer unused = allMacNum - todayOpenNum;
            Integer quaran = quaranNum.get(groupId) == null ? 0 : quaranNum.get(groupId);
            Integer adjust = adjustNum.get(groupId) == null ? 0 : adjustNum.get(groupId);
            Integer normal = normalNum.get(groupId) == null ? 0 : normalNum.get(groupId);
            Integer allTestNum = unused + quaran + adjust + normal;
            Double unusedRate = unused.doubleValue() / allTestNum;
            Double quaranRate = quaran.doubleValue() / allTestNum;
            Double adjustRate = adjust.doubleValue() / allTestNum;
            Double normalRate = normal.doubleValue() / allTestNum;
            data.setGroupId(groupId);
            data.setAllMacNum(allMacNum);
            data.setQuarantineNum(quaran);
            data.setAdjustmentNum(adjust);
            data.setNormalNum(normal);
            data.setUnusedNum(unused);
            data.setAllTestNum(allTestNum);
            data.setQuarantineRate(quaranRate);
            data.setAdjustmentRate(adjustRate);
            data.setNormalRate(normalRate);
            data.setUnusedRate(unusedRate);
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸
            adjustList.add(data);
        }
        for (Map.Entry<Integer, Integer> entry : groupIdResMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (groupId == null) continue;
            DfGroupAdjustment data = new DfGroupAdjustment();
            Integer allMacNum = entry.getValue();
            Integer todayOpenNum = groupIdResAllMacNumAppear.get(groupId) == null ? 0 : groupIdResAllMacNumAppear.get(groupId);
            Integer unused = allMacNum - todayOpenNum;
            Integer quaran = quaranNumAppear.get(groupId) == null ? 0 : quaranNumAppear.get(groupId);
            Integer adjust = 0;
            Integer normal = normalNumAppear.get(groupId) == null ? 0 : normalNumAppear.get(groupId);
            Integer allTestNum = unused + quaran + adjust + normal;
            Double unusedRate = unused.doubleValue() / allTestNum;
            Double quaranRate = quaran.doubleValue() / allTestNum;
            Double adjustRate = adjust.doubleValue() / allTestNum;
            Double normalRate = normal.doubleValue() / allTestNum;
            data.setGroupId(groupId);
            data.setAllMacNum(allMacNum);
            data.setQuarantineNum(quaran);
            data.setAdjustmentNum(adjust);
            data.setNormalNum(normal);
            data.setUnusedNum(unused);
            data.setAllTestNum(allTestNum);
            data.setQuarantineRate(quaranRate);
            data.setAdjustmentRate(adjustRate);
            data.setNormalRate(normalRate);
            data.setUnusedRate(unusedRate);
            data.setDayOrNight(dayOrNight);
            data.setTestType(2);  // 2-外观
            adjustList.add(data);
        }
        // 删除原有的数据
        QueryWrapper<DfGroupAdjustment> removeAdjustQW = new QueryWrapper<>();
        removeAdjustQW.between("create_time", startTime, endTime);
        dfGroupAdjustmentService.remove(removeAdjustQW);

        // 添加新的数据
        dfGroupAdjustmentService.saveBatch(adjustList);

        System.out.println("===========更新小组调机能力（结束）===========");

        System.out.println("===========更新机台复测数据（开始）===========");

        // 存储调机或者隔离的机台数据
        List<DfMacRetest> retestsList = new ArrayList<>();
        Set<String> adOrQuMacList = new HashSet<>();
        Map<String, Timestamp> macResAdOrQuTime = new HashMap<>();
        for (DfSizeDetail sizeDetail : allSizeDetail) {
            String machineCode = sizeDetail.getMachineCode();
            if (adOrQuMacList.contains(machineCode)) {  // 这是复测
                // 获取响应时间
                Timestamp lastNgTime = macResAdOrQuTime.get(machineCode);
                Timestamp thisTestTime = sizeDetail.getTestTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(machineCode);
                macRetest.setRetestResult(sizeDetail.getResult());
                macRetest.setCreateTime(sizeDetail.getTestTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(sizeDetail.getFactory());
                macRetest.setDayOrNight(dayOrNight);
                macRetest.setProcess(sizeDetail.getProcess());
                macRetest.setTestType(1);  // 1-尺寸
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    adOrQuMacList.remove(macRetest.getMachineCode());
                    macResAdOrQuTime.remove(macRetest.getMachineCode());
                } else {
                    macResAdOrQuTime.put(macRetest.getMachineCode(), sizeDetail.getTestTime());
                }
            } else if (null != sizeDetail.getInfoResult() && (sizeDetail.getInfoResult().equals("TJ") || sizeDetail.getInfoResult().equals("NG"))) {
                // 这不是复测，且调机或者隔离
                macResAdOrQuTime.put(machineCode, sizeDetail.getTestTime());
                adOrQuMacList.add(machineCode);
            }
        }
        Set<String> adOrQuMacListAppear = new HashSet<>();  // -- 外观
        Map<String, Timestamp> macResAdOrQuTimeAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal appear : allAppear) {
            String machineCode = appear.getfMac();
            if (adOrQuMacListAppear.contains(machineCode)) {  // 这是复测
                // 获取响应时间
                Timestamp lastNgTime = macResAdOrQuTimeAppear.get(machineCode);
                Timestamp thisTestTime = appear.getfTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(machineCode);
                macRetest.setRetestResult(appear.getStatus());
                macRetest.setCreateTime(appear.getfTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(appear.getfFac());
                macRetest.setDayOrNight(dayOrNight);
                macRetest.setProcess(appear.getfSeq());
                macRetest.setTestType(2);  // 2-外观
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    adOrQuMacListAppear.remove(macRetest.getMachineCode());
                    macResAdOrQuTimeAppear.remove(macRetest.getMachineCode());
                } else {
                    macResAdOrQuTimeAppear.put(macRetest.getMachineCode(), appear.getfTime());
                }
            } else if (null != appear.getStatus() && appear.getStatus().equals("NG")) {
                // 这不是复测，且调机或者隔离
                macResAdOrQuTimeAppear.put(machineCode, appear.getfTime());
                adOrQuMacListAppear.add(machineCode);
            }
        }

        // 删除原有的数据
        QueryWrapper<DfMacRetest> removeRetestQW = new QueryWrapper<>();
        removeRetestQW.between("create_time", startTime, endTime);
        dfMacRetestService.remove(removeRetestQW);

        // 添加新的数据
        dfMacRetestService.saveBatch(retestsList);

        System.out.println("===========更新机台复测数据（结束）===========");


        System.out.println("===========更新首检开机数（开始）===========");

        QueryWrapper<DfSizeDetail> faiPassRateQW = new QueryWrapper<>();  // -- 尺寸
        faiPassRateQW.between("create_time", startTime, endTime);
        List<DfSizeDetail> faiPassRateList = dfSizeDetailService.getFaiPassRate(faiPassRateQW);
        System.out.println("这就是startTime" + startTime);
        System.out.println("这就是endTime" + endTime);
        System.out.println("这就是faiPassRateList" + faiPassRateList);
        List<DfFaiPassRate> dfFaiPassRateList  = new ArrayList<>();
        for (DfSizeDetail data : faiPassRateList) {
            if (null != data) {
                DfFaiPassRate dfFaiPassRate = new DfFaiPassRate();
                dfFaiPassRate.setFactory(data.getFactory());
                dfFaiPassRate.setDayOrNight(dayOrNight);
                dfFaiPassRate.setFaiPassNum(data.getId());  // 首检通过数
                dfFaiPassRate.setFaiOpenNum(allMachineCode.size()); // 首检开机数
                dfFaiPassRate.setOpenMacNum(allMachineCode.size()); // 开机数
                dfFaiPassRate.setAllMacNum(allMachineCode.size()); // 所有机台
                dfFaiPassRate.setTestType(1);  // 1-尺寸
                dfFaiPassRateList.add(dfFaiPassRate);
            }
        }



        QueryWrapper<DfQmsIpqcWaigTotal> faiPassRateQWAppear = new QueryWrapper<>();  // -- 外观
        faiPassRateQWAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> faiPassRateAppear = dfQmsIpqcWaigTotalService.getFaiPassRate(faiPassRateQWAppear);
        List<DfFaiPassRate> dfFaiPassRateListAppear  = new ArrayList<>();
        for (DfQmsIpqcWaigTotal data : faiPassRateAppear) {
            if (null != data) {
                DfFaiPassRate dfFaiPassRateAppear = new DfFaiPassRate();
                dfFaiPassRateAppear.setFactory(data.getfFac());
                dfFaiPassRateAppear.setDayOrNight(dayOrNight);
                dfFaiPassRateAppear.setFaiPassNum(data.getId());  // 首检通过数
                dfFaiPassRateAppear.setFaiOpenNum(allMachineCodeAppear.size()); // 首检开机数
                dfFaiPassRateAppear.setOpenMacNum(allMachineCodeAppear.size()); // 开机数
                dfFaiPassRateAppear.setAllMacNum(allMachineCodeAppear.size()); // 所有机台
                dfFaiPassRateAppear.setTestType(2);  // 2-外观
                dfFaiPassRateListAppear.add(dfFaiPassRateAppear);
            }

        }


        // 删除原有的数据
        QueryWrapper<DfFaiPassRate> removeFaiPassQW = new QueryWrapper<>();
        removeFaiPassQW.between("create_time", startTime, endTime);
        dfFaiPassRateService.remove(removeFaiPassQW);
        // 添加新的数据
        if (dfFaiPassRateList.size() > 0) dfFaiPassRateService.saveBatch(dfFaiPassRateList);
        if (dfFaiPassRateListAppear.size() > 0) dfFaiPassRateService.saveBatch(dfFaiPassRateListAppear);

        System.out.println("===========更新首检开机数（结束）===========");


        System.out.println("结束----定时任务：1,3,5,7,9,11,13,15,17,19,21,23点执行一次");
    }

    // ========小组超时===========
    @Async
    //@Scheduled(cron ="0 0/1 * * * ?")
    //@Scheduled(cron ="0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * ?")
    @Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    ////@Transactional
    public void groupOverTime() throws ParseException {
        System.out.println("===========更新小组超时送检（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        //Timestamp now = new Timestamp((Timestamp.valueOf(TimeUtil.getNowTimeByNormal()).getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0, 10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19) {
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }

        System.out.println("开始时间：" + startTime);
        System.out.println("结束时间：" + endTime);


        // 获取当前时间段的机台数据
        String littleStartTime;
        String littleEndTime = now.toString();
        String inTheTime; // 时间段

        // 需要更新的时间
        String updateStartTestTime;
        String updateEndTestTime;
        // 往前推六个小时
        String bigStartTime;
        switch (now.getHours()) {
            case 23:
            case 0:
                littleStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 21:00:00";
                updateEndTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 19:00:00";
                inTheTime = "23-1";
                break;
            case 1:
            case 2:
                littleStartTime = today + " 01:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateEndTestTime = today + " 01:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 21:00:00";
                inTheTime = "1-3";
                break;
            case 3:
            case 4:
                littleStartTime = today + " 03:00:00";
                updateStartTestTime = today + " 01:00:00";
                updateEndTestTime = today + " 03:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                inTheTime = "3-5";
                break;
            case 5:
            case 6:
                littleStartTime = today + " 05:00:00";
                updateStartTestTime = today + " 03:00:00";
                updateEndTestTime = today + " 05:00:00";
                bigStartTime = today + " 01:00:00";
                inTheTime = "5-7";
                break;
            case 7:
            case 8:
                littleStartTime = today + " 07:00:00";
                updateStartTestTime = today + " 05:00:00";
                updateEndTestTime = today + " 07:00:00";
                bigStartTime = today + " 03:00:00";
                inTheTime = "7-9";
                break;
            case 9:
            case 10:
                littleStartTime = today + " 09:00:00";
                updateStartTestTime = today + " 07:00:00";
                updateEndTestTime = today + " 09:00:00";
                bigStartTime = today + " 05:00:00";
                inTheTime = "9-11";
                break;
            case 11:
            case 12:
                littleStartTime = today + " 11:00:00";
                updateStartTestTime = today + " 09:00:00";
                updateEndTestTime = today + " 11:00:00";
                bigStartTime = today + " 07:00:00";
                inTheTime = "11-13";
                break;
            case 13:
            case 14:
                littleStartTime = today + " 13:00:00";
                updateStartTestTime = today + " 11:00:00";
                updateEndTestTime = today + " 13:00:00";
                bigStartTime = today + " 09:00:00";
                inTheTime = "13-15";
                break;
            case 15:
            case 16:
                littleStartTime = today + " 15:00:00";
                updateStartTestTime = today + " 13:00:00";
                updateEndTestTime = today + " 15:00:00";
                bigStartTime = today + " 11:00:00";
                inTheTime = "15-17";
                break;
            case 17:
            case 18:
                littleStartTime = today + " 17:00:00";
                updateStartTestTime = today + " 15:00:00";
                updateEndTestTime = today + " 17:00:00";
                bigStartTime = today + " 13:00:00";
                inTheTime = "17-19";
                break;
            case 19:
            case 20:
                littleStartTime = today + " 19:00:00";
                updateStartTestTime = today + " 17:00:00";
                updateEndTestTime = today + " 19:00:00";
                bigStartTime = today + " 15:00:00";
                inTheTime = "19-21";
                break;
            case 21:
            case 22:
                littleStartTime = today + " 21:00:00";
                updateStartTestTime = today + " 19:00:00";
                updateEndTestTime = today + " 21:00:00";
                bigStartTime = today + " 17:00:00";
                inTheTime = "21-23";
                break;
            default:
                littleStartTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateStartTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateEndTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                bigStartTime = null;
                inTheTime = "0-0";
        }
        QueryWrapper<DfSizeDetail> littleqw = new QueryWrapper<>();  //  -- 尺寸
        littleqw.between("create_time", littleStartTime, littleEndTime);
        List<DfSizeDetail> littleMachineCode = dfSizeDetailService.listMachineCode(littleqw);
        QueryWrapper<DfQmsIpqcWaigTotal> littleqwAppear = new QueryWrapper<>();  //  -- 外观
        littleqwAppear.between("f_time", littleStartTime, littleEndTime);
        List<DfQmsIpqcWaigTotal> littleMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(littleqwAppear);

        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取当班的机台数据
        QueryWrapper<DfSizeDetail> allqw = new QueryWrapper<>();  //  -- 尺寸
        allqw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allMachineCode = dfSizeDetailService.listMachineCode(allqw);
        QueryWrapper<DfQmsIpqcWaigTotal> allqwAppear = new QueryWrapper<>();  //  -- 外观
        allqwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(allqwAppear);

        // 获取小组当天开机数
        Map<Integer, Integer> groupIdResAllMacNum = new HashMap<>();  //  -- 尺寸
        for (DfSizeDetail allData : allMachineCode) {
            String machineCode = allData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResAllMacNumAppear = new HashMap<>();  //  -- 外观
        for (DfQmsIpqcWaigTotal allData : allMachineCodeAppear) {
            String machineCode = allData.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取小组当前时间段的送检数
        Map<Integer, Integer> groupIdResLittleMacNum = new HashMap<>();
        for (DfSizeDetail littleData : littleMachineCode) {
            String machineCode = littleData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResLittleMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResLittleMacNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal littleData : littleMachineCodeAppear) {
            String machineCode = littleData.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResLittleMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取小组超时数据
        List<DfGroupMacOvertime> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResAllMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupMacOvertime data = new DfGroupMacOvertime();
            Integer groupOpenMacNum = entry.getValue();  // 小组开机总数
            Integer groupCheckMacNum = groupIdResLittleMacNum.get(groupId) == null ? 0 : groupIdResLittleMacNum.get(groupId);  // 小组时间段送检数
            Integer groupOverTimeMacNum = groupOpenMacNum - groupCheckMacNum; // 小组机台超时送检数
            Double overTimeRate = groupOverTimeMacNum.doubleValue() / groupOpenMacNum;
            data.setGroupId(groupId);
            data.setAllMacNum(groupOpenMacNum);
            data.setOvertimeMacNum(groupOverTimeMacNum);
            data.setOvertimeRate(overTimeRate);
            data.setIntheTime(inTheTime);
            data.setTestTime(now);
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸

            list.add(data);
        }
        for (Map.Entry<Integer, Integer> entry : groupIdResAllMacNumAppear.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupMacOvertime data = new DfGroupMacOvertime();
            Integer groupOpenMacNum = entry.getValue();  // 小组开机总数
            Integer groupCheckMacNum = groupIdResLittleMacNumAppear.get(groupId) == null ? 0 : groupIdResLittleMacNumAppear.get(groupId);  // 小组时间段送检数
            Integer groupOverTimeMacNum = groupOpenMacNum - groupCheckMacNum; // 小组机台超时送检数
            Double overTimeRate = groupOverTimeMacNum.doubleValue() / groupOpenMacNum;
            data.setGroupId(groupId);
            data.setAllMacNum(groupOpenMacNum);
            data.setOvertimeMacNum(groupOverTimeMacNum);
            data.setOvertimeRate(overTimeRate);
            data.setIntheTime(inTheTime);
            data.setTestTime(now);
            data.setDayOrNight(dayOrNight);
            data.setTestType(2);  // 2-外观

            list.add(data);
        }
        dfGroupMacOvertimeService.saveBatch(list);

        // 更新前四个小时到两个小时的数据
        // 获取前6个小时的机台数据
        QueryWrapper<DfSizeDetail> bigqw = new QueryWrapper<>();  // -- 尺寸
        bigqw.between("create_time", bigStartTime, endTime);
        List<DfSizeDetail> bigMachineCode = dfSizeDetailService.listMachineCode(bigqw);
        Map<Integer, Integer> groupIdResBigMacNum = new HashMap<>();
        for (DfSizeDetail bigData : bigMachineCode) {
            String machineCode = bigData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResBigMacNum.merge(groupId, 1, Integer::sum);
        }
        QueryWrapper<DfQmsIpqcWaigTotal> bigqwAppear = new QueryWrapper<>();  // -- 外观
        bigqwAppear.between("f_time", bigStartTime, endTime);
        List<DfQmsIpqcWaigTotal> bigMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(bigqwAppear);
        Map<Integer, Integer> groupIdResBigMacNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal bigDataAppear : bigMachineCodeAppear) {
            String machineCode = bigDataAppear.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResBigMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 获取前四个小时到两个小时的数据
        QueryWrapper<DfGroupMacOvertime> updateOverTimeQw = new QueryWrapper<>();  // -- 尺寸
        updateOverTimeQw.between("test_time", updateStartTestTime, updateEndTestTime)
                .eq("test_type", 1);
        List<DfGroupMacOvertime> updateOverTime = dfGroupMacOvertimeService.list(updateOverTimeQw);
        for (DfGroupMacOvertime dfGroupMacOvertime : updateOverTime) {
            Integer groupId = dfGroupMacOvertime.getGroupId();
            if (groupId == null) continue;
            // 当班开机数
            Integer openNum = groupIdResAllMacNum.get(groupId) == null ? 0 : groupIdResAllMacNum.get(groupId);
            // 六小时送检机台数
            Integer bigNum = groupIdResBigMacNum.get(groupId) == null ? 0 : groupIdResBigMacNum.get(groupId);
            // 停机数
            Integer stopNum = Math.max(0, openNum - bigNum);

            dfGroupMacOvertime.setAllMacNum(Math.max(0, dfGroupMacOvertime.getAllMacNum() - stopNum));
            dfGroupMacOvertime.setOvertimeMacNum(Math.max(0, dfGroupMacOvertime.getOvertimeMacNum() - stopNum));
            if (dfGroupMacOvertime.getAllMacNum() <= 0) {
                dfGroupMacOvertime.setOvertimeRate(0d);
            } else {
                dfGroupMacOvertime.setOvertimeRate(Math.max(0, dfGroupMacOvertime.getOvertimeMacNum().doubleValue() / dfGroupMacOvertime.getAllMacNum()));
            }
        }
        if (!updateOverTime.isEmpty()) {
            dfGroupMacOvertimeService.updateBatchById(updateOverTime);
        }
        QueryWrapper<DfGroupMacOvertime> updateOverTimeQwAppear = new QueryWrapper<>();  // -- 外观
        updateOverTimeQwAppear.between("test_time", updateStartTestTime, updateEndTestTime)
                .eq("test_type", 2);
        List<DfGroupMacOvertime> updateOverTimeAppear = dfGroupMacOvertimeService.list(updateOverTimeQwAppear);
        for (DfGroupMacOvertime dfGroupMacOvertime : updateOverTimeAppear) {
            Integer groupId = dfGroupMacOvertime.getGroupId();
            if (groupId == null) continue;
            // 当班开机数
            Integer openNum = groupIdResAllMacNumAppear.get(groupId) == null ? 0 : groupIdResAllMacNumAppear.get(groupId);
            // 六小时送检机台数
            Integer bigNum = groupIdResBigMacNumAppear.get(groupId) == null ? 0 : groupIdResBigMacNumAppear.get(groupId);
            // 停机数
            Integer stopNum = Math.max(0, openNum - bigNum);

            dfGroupMacOvertime.setAllMacNum(dfGroupMacOvertime.getAllMacNum() - stopNum);
            dfGroupMacOvertime.setOvertimeMacNum(dfGroupMacOvertime.getOvertimeMacNum() - stopNum);
            if (dfGroupMacOvertime.getAllMacNum() <= 0) {
                dfGroupMacOvertime.setOvertimeRate(0d);
            } else {
                dfGroupMacOvertime.setOvertimeRate(dfGroupMacOvertime.getOvertimeMacNum().doubleValue() / dfGroupMacOvertime.getAllMacNum());
            }
        }
        if (!updateOverTimeAppear.isEmpty()) {
            dfGroupMacOvertimeService.updateBatchById(updateOverTimeAppear);
        }

        System.out.println("===========更新小组超时送检（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    // ========小组调机===========
    @Async
    //@Scheduled(cron ="0 0/1 * * * ?")
    //@Scheduled(cron ="0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * ?")
    @Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    //@Transactional
    public void groupAdjustment() throws ParseException {
        System.out.println("===========更新小组调机能力（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0,10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19){
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }
        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取当班的机台数据
        QueryWrapper<DfSizeDetail> allqw = new QueryWrapper<>();  //  -- 尺寸
        allqw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allMachineCode = dfSizeDetailService.listMachineCode(allqw);
        QueryWrapper<DfQmsIpqcWaigTotal> allqwAppear = new QueryWrapper<>();  //  -- 外观
        allqwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(allqwAppear);

        // 获取小组当天开机数
        Map<Integer, Integer> groupIdResAllMacNum = new HashMap<>();  //  -- 尺寸
        for (DfSizeDetail allData : allMachineCode) {
            String machineCode = allData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResAllMacNumAppear = new HashMap<>();  //  -- 外观
        for (DfQmsIpqcWaigTotal allDataAppear : allMachineCodeAppear) {
            String machineCode = allDataAppear.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNumAppear.merge(groupId, 1, Integer::sum);
        }

        // 更新小组调机能力
        // 小组机总台数
        Map<Integer, Integer> groupIdResMacNum = dfGroupService.getGroupMacNum(month, dayOrNight);
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();  // -- 尺寸
        qw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allSizeDetail = dfSizeDetailService.list(qw);
        QueryWrapper<DfQmsIpqcWaigTotal> qwAppear = new QueryWrapper<>();  // -- 外观
        qwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allAppear = dfQmsIpqcWaigTotalService.list(qwAppear);
        // 统计小组当班隔离、调机、正常数
        Map<Integer, Integer> quaranNum = new HashMap<>();  // -- 尺寸
        Map<Integer, Integer> adjustNum = new HashMap<>();
        Map<Integer, Integer> normalNum = new HashMap<>();
        for (DfSizeDetail sizeDetail : allSizeDetail) {
            Integer groupId = macResGroupId.get(sizeDetail.getMachineCode());
            if (groupId == null) continue;
            if (sizeDetail.getInfoResult() == null) continue;
            switch (sizeDetail.getInfoResult()) {
                case "NG": quaranNum.merge(groupId, 1, Integer::sum); break;
                case "TJ": adjustNum.merge(groupId, 1, Integer::sum); break;
                case "OK": normalNum.merge(groupId, 1, Integer::sum); break;
                default:
            }
        }
        Map<Integer, Integer> quaranNumAppear = new HashMap<>();  // -- 外观
        Map<Integer, Integer> normalNumAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal appear : allAppear) {
            Integer groupId = macResGroupId.get(appear.getfMac());
            if (groupId == null) continue;
            if (appear.getStatus() == null) continue;
            switch (appear.getStatus()) {
                case "NG": quaranNumAppear.merge(groupId, appear.getAffectCount(), Integer::sum); break;
                case "OK": normalNumAppear.merge(groupId, appear.getAffectCount(), Integer::sum); break;
                default:
            }
        }
        // 统计小组调机能力
        List<DfGroupAdjustment> adjustList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (groupId == null) continue;
            DfGroupAdjustment data = new DfGroupAdjustment();
            Integer allMacNum = entry.getValue();
            Integer todayOpenNum = groupIdResAllMacNum.get(groupId) == null ? 0 : groupIdResAllMacNum.get(groupId);
            Integer unused = allMacNum - todayOpenNum;
            Integer quaran = quaranNum.get(groupId) == null ? 0 : quaranNum.get(groupId);
            Integer adjust = adjustNum.get(groupId) == null ? 0 : adjustNum.get(groupId);
            Integer normal = normalNum.get(groupId) == null ? 0 : normalNum.get(groupId);
            Integer allTestNum = unused + quaran + adjust + normal;
            Double unusedRate = unused.doubleValue() / allTestNum;
            Double quaranRate = quaran.doubleValue() / allTestNum;
            Double adjustRate = adjust.doubleValue() / allTestNum;
            Double normalRate = normal.doubleValue() / allTestNum;
            data.setGroupId(groupId);
            data.setAllMacNum(allMacNum);
            data.setQuarantineNum(quaran);
            data.setAdjustmentNum(adjust);
            data.setNormalNum(normal);
            data.setUnusedNum(unused);
            data.setAllTestNum(allTestNum);
            data.setQuarantineRate(quaranRate);
            data.setAdjustmentRate(adjustRate);
            data.setNormalRate(normalRate);
            data.setUnusedRate(unusedRate);
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸
            adjustList.add(data);
        }
        for (Map.Entry<Integer, Integer> entry : groupIdResMacNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (groupId == null) continue;
            DfGroupAdjustment data = new DfGroupAdjustment();
            Integer allMacNum = entry.getValue();
            Integer todayOpenNum = groupIdResAllMacNumAppear.get(groupId) == null ? 0 : groupIdResAllMacNumAppear.get(groupId);
            Integer unused = allMacNum - todayOpenNum;
            Integer quaran = quaranNumAppear.get(groupId) == null ? 0 : quaranNumAppear.get(groupId);
            Integer adjust = 0;
            Integer normal = normalNumAppear.get(groupId) == null ? 0 : normalNumAppear.get(groupId);
            Integer allTestNum = unused + quaran + adjust + normal;
            Double unusedRate = unused.doubleValue() / allTestNum;
            Double quaranRate = quaran.doubleValue() / allTestNum;
            Double adjustRate = adjust.doubleValue() / allTestNum;
            Double normalRate = normal.doubleValue() / allTestNum;
            data.setGroupId(groupId);
            data.setAllMacNum(allMacNum);
            data.setQuarantineNum(quaran);
            data.setAdjustmentNum(adjust);
            data.setNormalNum(normal);
            data.setUnusedNum(unused);
            data.setAllTestNum(allTestNum);
            data.setQuarantineRate(quaranRate);
            data.setAdjustmentRate(adjustRate);
            data.setNormalRate(normalRate);
            data.setUnusedRate(unusedRate);
            data.setDayOrNight(dayOrNight);
            data.setTestType(2);  // 2-外观
            adjustList.add(data);
        }
        // 删除原有的数据
        QueryWrapper<DfGroupAdjustment> removeAdjustQW = new QueryWrapper<>();
        removeAdjustQW.between("create_time", startTime, endTime);
        dfGroupAdjustmentService.remove(removeAdjustQW);

        // 添加新的数据
        dfGroupAdjustmentService.saveBatch(adjustList);

        System.out.println("===========更新小组调机能力（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    // ========机台复测===========
    @Async
    //@Scheduled(cron ="0 0/1 * * * ?")
    //@Scheduled(cron ="0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * ?")
    @Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    //@Transactional
    public void macRetest() throws ParseException {
        System.out.println("===========更新机台复测数据（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0,10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {

            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19){
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }

        // 小组机总台数
        Map<Integer, Integer> groupIdResMacNum = dfGroupService.getGroupMacNum(month, dayOrNight);
        QueryWrapper<DfSizeDetail> qw = new QueryWrapper<>();  // -- 尺寸
        qw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allSizeDetail = dfSizeDetailService.list(qw);
        QueryWrapper<DfQmsIpqcWaigTotal> qwAppear = new QueryWrapper<>();  // -- 外观
        qwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allAppear = dfQmsIpqcWaigTotalService.list(qwAppear);

        // 存储调机或者隔离的机台数据
        List<DfMacRetest> retestsList = new ArrayList<>();
        Set<String> adOrQuMacList = new HashSet<>();
        Map<String, Timestamp> macResAdOrQuTime = new HashMap<>();
        for (DfSizeDetail sizeDetail : allSizeDetail) {
            String machineCode = sizeDetail.getMachineCode();
            if (adOrQuMacList.contains(machineCode)) {  // 这是复测
                // 获取响应时间
                Timestamp lastNgTime = macResAdOrQuTime.get(machineCode);
                Timestamp thisTestTime = sizeDetail.getTestTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(machineCode);
                macRetest.setRetestResult(sizeDetail.getResult());
                macRetest.setCreateTime(sizeDetail.getTestTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(sizeDetail.getFactory());
                macRetest.setDayOrNight(dayOrNight);
                macRetest.setProcess(sizeDetail.getProcess());
                macRetest.setTestType(1);  // 1-尺寸
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    adOrQuMacList.remove(macRetest.getMachineCode());
                    macResAdOrQuTime.remove(macRetest.getMachineCode());
                } else {
                    macResAdOrQuTime.put(macRetest.getMachineCode(), sizeDetail.getTestTime());
                }
            } else if (null != sizeDetail.getInfoResult() && (sizeDetail.getInfoResult().equals("TJ") || sizeDetail.getInfoResult().equals("NG"))) {
                // 这不是复测，且调机或者隔离
                macResAdOrQuTime.put(machineCode, sizeDetail.getTestTime());
                adOrQuMacList.add(machineCode);
            }
        }
        Set<String> adOrQuMacListAppear = new HashSet<>();  // -- 外观
        Map<String, Timestamp> macResAdOrQuTimeAppear = new HashMap<>();
        for (DfQmsIpqcWaigTotal appear : allAppear) {
            String machineCode = appear.getfMac();
            if (adOrQuMacListAppear.contains(machineCode)) {  // 这是复测
                // 获取响应时间
                Timestamp lastNgTime = macResAdOrQuTimeAppear.get(machineCode);
                Timestamp thisTestTime = appear.getfTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(machineCode);
                macRetest.setRetestResult(appear.getStatus());
                macRetest.setCreateTime(appear.getfTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(appear.getfFac());
                macRetest.setDayOrNight(dayOrNight);
                macRetest.setProcess(appear.getfSeq());
                macRetest.setTestType(2);  // 2-外观
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    adOrQuMacListAppear.remove(macRetest.getMachineCode());
                    macResAdOrQuTimeAppear.remove(macRetest.getMachineCode());
                } else {
                    macResAdOrQuTimeAppear.put(macRetest.getMachineCode(), appear.getfTime());
                }
            } else if (null != appear.getStatus() && appear.getStatus().equals("NG")) {
                // 这不是复测，且调机或者隔离
                macResAdOrQuTimeAppear.put(machineCode, appear.getfTime());
                adOrQuMacListAppear.add(machineCode);
            }
        }

        // 删除原有的数据
        QueryWrapper<DfMacRetest> removeRetestQW = new QueryWrapper<>();
        removeRetestQW.between("create_time", startTime, endTime);
        dfMacRetestService.remove(removeRetestQW);

        // 添加新的数据
        dfMacRetestService.saveBatch(retestsList);

        System.out.println("===========更新机台复测数据（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    // ========小组检测良率===========
    @Async
    @Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    public void groupCheckOkRate() throws ParseException {
        String now = TimeUtil.getNowTimeByNormal();
        dfGroupOkRateService.generateDataByDateTime(now);
    }

    // ========小组关闭率===========
    @Async
    @Scheduled(cron ="30 59 6,18 * * ?")
    public void groupCloseRate() throws ParseException {
        String now = TimeUtil.getNowTimeByNormal();
        dfGroupCloseService.generateDataByDateTime(now);
    }

    // ========小组机台NG率===========
    @Async
    @Scheduled(cron ="30 59 6,18 * * ?")
    public void groupMacNgRate() throws ParseException {
        String now = TimeUtil.getNowTimeByNormal();
        dfGroupMacNgRateService.generateDataByDateTime(now);
    }

    // ========机台首检===========
    //@Async
    //@Scheduled(cron ="0 0/1 * * * ?")
    //@Scheduled(cron ="0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * ?")
    //@Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
    //@Transactional
    public void faiPass() throws ParseException {
        System.out.println("===========更新首检开机数（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0,10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19){
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }

        // 机台对应小组的id
        //Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取当班的机台数据
        QueryWrapper<DfSizeDetail> allqw = new QueryWrapper<>();  //  -- 尺寸
        allqw.between("create_time", startTime, endTime);
        List<DfSizeDetail> allMachineCode = dfSizeDetailService.listMachineCode(allqw);
        QueryWrapper<DfQmsIpqcWaigTotal> allqwAppear = new QueryWrapper<>();  //  -- 外观
        allqwAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> allMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(allqwAppear);

        // 获取小组当天开机数
        /*Map<Integer, Integer> groupIdResAllMacNum = new HashMap<>();  //  -- 尺寸
        for (DfSizeDetail allData : allMachineCode) {
            String machineCode = allData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNum.merge(groupId, 1, Integer::sum);
        }
        Map<Integer, Integer> groupIdResAllMacNumAppear = new HashMap<>();  //  -- 外观
        for (DfQmsIpqcWaigTotal allDataAppear : allMachineCodeAppear) {
            String machineCode = allDataAppear.getfMac();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllMacNumAppear.merge(groupId, 1, Integer::sum);
        }*/

        QueryWrapper<DfSizeDetail> faiPassRateQW = new QueryWrapper<>();  // -- 尺寸
        faiPassRateQW.between("create_time", startTime, endTime);
        List<DfSizeDetail> faiPassRateList = dfSizeDetailService.getFaiPassRate(faiPassRateQW);
        System.out.println("这就是startTime" + startTime);
        System.out.println("这就是endTime" + endTime);
        System.out.println("这就是faiPassRateList" + faiPassRateList);
        List<DfFaiPassRate> dfFaiPassRateList  = new ArrayList<>();
        for (DfSizeDetail data : faiPassRateList) {
            if (null != data) {
                DfFaiPassRate dfFaiPassRate = new DfFaiPassRate();
                dfFaiPassRate.setFactory(data.getFactory());
                dfFaiPassRate.setDayOrNight(dayOrNight);
                dfFaiPassRate.setFaiPassNum(data.getId());  // 首检通过数
                dfFaiPassRate.setFaiOpenNum(allMachineCode.size()); // 首检开机数
                dfFaiPassRate.setOpenMacNum(allMachineCode.size()); // 开机数
                dfFaiPassRate.setAllMacNum(allMachineCode.size()); // 所有机台
                dfFaiPassRate.setTestType(1);  // 1-尺寸
                dfFaiPassRateList.add(dfFaiPassRate);
            }
        }


        QueryWrapper<DfQmsIpqcWaigTotal> faiPassRateQWAppear = new QueryWrapper<>();  // -- 外观
        faiPassRateQWAppear.between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> faiPassRateAppear = dfQmsIpqcWaigTotalService.getFaiPassRate(faiPassRateQWAppear);
        List<DfFaiPassRate> dfFaiPassRateListAppear  = new ArrayList<>();
        for (DfQmsIpqcWaigTotal data : faiPassRateAppear) {
            if (null != data) {
                DfFaiPassRate dfFaiPassRateAppear = new DfFaiPassRate();
                dfFaiPassRateAppear.setFactory(data.getfFac());
                dfFaiPassRateAppear.setDayOrNight(dayOrNight);
                dfFaiPassRateAppear.setFaiPassNum(data.getId());  // 首检通过数
                dfFaiPassRateAppear.setFaiOpenNum(allMachineCodeAppear.size()); // 首检开机数
                dfFaiPassRateAppear.setOpenMacNum(allMachineCodeAppear.size()); // 开机数
                dfFaiPassRateAppear.setAllMacNum(allMachineCodeAppear.size()); // 所有机台
                dfFaiPassRateAppear.setTestType(2);  // 2-外观
                dfFaiPassRateListAppear.add(dfFaiPassRateAppear);
            }

        }


        // 删除原有的数据
        QueryWrapper<DfFaiPassRate> removeFaiPassQW = new QueryWrapper<>();
        removeFaiPassQW.between("create_time", startTime, endTime);
        dfFaiPassRateService.remove(removeFaiPassQW);
        // 添加新的数据
        if (dfFaiPassRateList.size() > 0) dfFaiPassRateService.saveBatch(dfFaiPassRateList);
        if (dfFaiPassRateListAppear.size() > 0) dfFaiPassRateService.saveBatch(dfFaiPassRateListAppear);

        System.out.println("===========更新首检开机数（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    // ========良率===========
    @Async
    @Scheduled(cron ="30 9,19,29,39,49,59 * * * ?")  // 50秒的时候运行，给予缓冲时间
    //@Scheduled(cron ="55 0/1 * * * ?")  // 55秒的时候运行，给予缓冲时间
    //@Transactional
    public void minute10() throws ParseException {
        System.out.println("开始----定时任务：10分钟执行一次");

        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0,10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19){
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }

        System.out.println("开始时间：" + startTime);
        System.out.println("结束时间：" + endTime);

        // 更新机台良率表
        addMachineOKRate(startTime, endTime, dayOrNight);

        // 更新尺寸不良率表
        addSizeNGRate(startTime, endTime, dayOrNight);


        System.out.println("结束----定时任务：10分钟执行一次");
    }

    private void addMachineOKRate(String startTime, String endTime, String dayOrNight) {
        System.out.println("===========更新机台良率表（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        QueryWrapper<DfAdMacOkRate> sizeDetailQW = new QueryWrapper<>();
        sizeDetailQW.between("create_time", startTime, endTime);

        // 删除原有的数据
        dfAdMacOkRateService.remove(sizeDetailQW);

        // 添加更新的尺寸数据
        List<DfAdMacOkRate> addOkRates = dfAdMacOkRateService.listOkRateBySizeDetail(sizeDetailQW);
        for (DfAdMacOkRate addOkRate : addOkRates) {
            addOkRate.setDayOrNight(dayOrNight);
            addOkRate.setTestType(1);  // 1-尺寸
        }
        // 添加更新的外观数据
        QueryWrapper<DfAdMacOkRate> appearQW = new QueryWrapper<>();
        appearQW.between("f_time", startTime, endTime);
        List<DfAdMacOkRate> addAppearList = dfAdMacOkRateService.listAppearOkRate(appearQW);
        for (DfAdMacOkRate addAppear : addAppearList) {
            addAppear.setDayOrNight(dayOrNight);
            addAppear.setTestType(2);  // 2-外观
        }

        dfAdMacOkRateService.saveBatch(addOkRates);
        dfAdMacOkRateService.saveBatch(addAppearList);

        System.out.println("===========更新机台良率表（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    private void addSizeNGRate(String startTime, String endTime, String dayOrNight) {
        System.out.println("===========更新尺寸、外观不良率表（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));


        // 删除原有的数据
        QueryWrapper<DfAdSizeNgRate> qw = new QueryWrapper<>();
        qw.between("create_time", startTime, endTime);
        dfAdSizeNgRateService.remove(qw);

        // 添加更新的尺寸数据
        QueryWrapper<DfAdSizeNgRate> itemInfosQW = new QueryWrapper<>();
        itemInfosQW.between("item.create_time", startTime, endTime);
        itemInfosQW.eq("item.key_point", 1);
        List<DfAdSizeNgRate> addSizeRates = dfAdSizeNgRateService.listNgRateByItemInfos(itemInfosQW);
        for (DfAdSizeNgRate addSizeRate : addSizeRates) {
            addSizeRate.setDayOrNight(dayOrNight);
            addSizeRate.setTestType(1);  // 1-尺寸
        }

        // 添加更新的外观数据
        List<DfAdSizeNgRate> addAppearRates = dfAdSizeNgRateService.listAppearNgInfos(startTime, endTime);
        for (DfAdSizeNgRate addAppearRate : addAppearRates) {
            addAppearRate.setDayOrNight(dayOrNight);
            addAppearRate.setTestType(2);  // 2-外观
        }
        dfAdSizeNgRateService.saveBatch(addSizeRates);
        dfAdSizeNgRateService.saveBatch(addAppearRates);

        System.out.println("===========更新尺寸、外观不良率表（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }


}
