package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 小组机台超时率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
@Controller
@RequestMapping("/dfGroupMacOvertime")
@Api(tags = "小组机台超时率")
@CrossOrigin
@ResponseBody
public class DfGroupMacOvertimeController {
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

    @ApiOperation("小组及时送检率")
    @GetMapping("/listAllByFactory")
    public Result listAllByFactory(String factory, String process, String project, String linebody, String dayOrNight,
                                   String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:01";
        List<DfGroupMacOvertime> dfSizeDetails = dfGroupMacOvertimeService.listOvertimeRate(factory, process, project, linebody, null,
                startDate, endDate, testType);

        Map<String, Integer> responResIndex = new HashMap<>();
        List<String> responlist = new ArrayList<>();
        int index = 0;
        for (DfGroupMacOvertime overtime : dfSizeDetails) {
            String respon = overtime.getRespon();
            if (!responResIndex.containsKey(respon)) {
                responResIndex.put(respon, index++);
                responlist.add(respon);
            }
        }
        Double[][] overtimeRate = new Double[index][12];
        for (DfGroupMacOvertime overtime : dfSizeDetails) {
            String respon = overtime.getRespon();
            Double rate = overtime.getOvertimeRate();
            /*if (overtime.getIntheTime().equals("7-9")) overtimeRate[responResIndex.get(respon)][0] = 1d;
            if (overtime.getIntheTime().equals("9-11")) overtimeRate[responResIndex.get(respon)][1] = 1d;
            if (overtime.getIntheTime().equals("11-13")) overtimeRate[responResIndex.get(respon)][2] = 1d;
            if (overtime.getIntheTime().equals("13-15")) overtimeRate[responResIndex.get(respon)][3] = 1d;
            if (overtime.getIntheTime().equals("15-17")) overtimeRate[responResIndex.get(respon)][4] = 1d;
            if (overtime.getIntheTime().equals("17-19")) overtimeRate[responResIndex.get(respon)][5] = 1d;
            if (overtime.getIntheTime().equals("19-21")) overtimeRate[responResIndex.get(respon)][6] = 1d;
            if (overtime.getIntheTime().equals("21-23")) overtimeRate[responResIndex.get(respon)][7] = 1d;
            if (overtime.getIntheTime().equals("23-1")) overtimeRate[responResIndex.get(respon)][8] = 1d;
            if (overtime.getIntheTime().equals("1-3")) overtimeRate[responResIndex.get(respon)][9] = 1d;
            if (overtime.getIntheTime().equals("3-5")) overtimeRate[responResIndex.get(respon)][10] = 1d;
            if (overtime.getIntheTime().equals("5-7")) overtimeRate[responResIndex.get(respon)][11] = 1d;*/
            if (overtime.getIntheTime().equals("7-9") && rate != 0) overtimeRate[responResIndex.get(respon)][0] = rate;
            if (overtime.getIntheTime().equals("9-11") && rate != 0) overtimeRate[responResIndex.get(respon)][1] = rate;
            if (overtime.getIntheTime().equals("11-13") && rate != 0) overtimeRate[responResIndex.get(respon)][2] = rate;
            if (overtime.getIntheTime().equals("13-15") && rate != 0) overtimeRate[responResIndex.get(respon)][3] = rate;
            if (overtime.getIntheTime().equals("15-17") && rate != 0) overtimeRate[responResIndex.get(respon)][4] = rate;
            if (overtime.getIntheTime().equals("17-19") && rate != 0) overtimeRate[responResIndex.get(respon)][5] = rate;
            if (overtime.getIntheTime().equals("19-21") && rate != 0) overtimeRate[responResIndex.get(respon)][6] = rate;
            if (overtime.getIntheTime().equals("21-23") && rate != 0) overtimeRate[responResIndex.get(respon)][7] = rate;
            if (overtime.getIntheTime().equals("23-1") && rate != 0) overtimeRate[responResIndex.get(respon)][8] = rate;
            if (overtime.getIntheTime().equals("1-3") && rate != 0) overtimeRate[responResIndex.get(respon)][9] = rate;
            if (overtime.getIntheTime().equals("3-5") && rate != 0) overtimeRate[responResIndex.get(respon)][10] = rate;
            if (overtime.getIntheTime().equals("5-7") && rate != 0) overtimeRate[responResIndex.get(respon)][11] = rate;
        }
        Map<String, Object> result = new HashMap<>();
        String[] times = new String[12];
        times[0] = "7:00-9:00";
        times[1] = "9:00-11:00";
        times[2] = "11:00-13:00";
        times[3] = "13:00-15:00";
        times[4] = "15:00-17:00";
        times[5] = "17:00-19:00";
        times[6] = "19:00-21:00";
        times[7] = "21:00-23:00";
        times[8] = "23:00-1:00";
        times[9] = "1:00-3:00";
        times[10] = "3:00-5:00";
        times[11] = "5:00-7:00";
        result.put("小组", responlist);
        result.put("data", overtimeRate);
        result.put("时间段", times);
        return new Result(200, "查询成功", result);

    }

    @ApiOperation("小组超时送检率")
    @GetMapping("/listAllByFactory2")
    public Result listAllByFactory2(String factory, String process, String project, String linebody, String dayOrNight,
                                   String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:01";
        List<DfGroupMacOvertime> dfSizeDetails = dfGroupMacOvertimeService.listOvertimeRate2(factory, process, project, linebody, null,
                startDate, endDate, testType);

        Map<String, Integer> responResIndex = new HashMap<>();
        List<String> responlist = new ArrayList<>();
        int index = 0;
        for (DfGroupMacOvertime overtime : dfSizeDetails) {
            String respon = overtime.getRespon();
            if (!responResIndex.containsKey(respon)) {
                responResIndex.put(respon, index++);
                responlist.add(respon);
            }
        }
        Double[][] overtimeRate = new Double[index][12];
        for (DfGroupMacOvertime overtime : dfSizeDetails) {
            String respon = overtime.getRespon();
            Double rate = overtime.getOvertimeRate();
            /*if (overtime.getIntheTime().equals("7-9")) overtimeRate[responResIndex.get(respon)][0] = 1d;
            if (overtime.getIntheTime().equals("9-11")) overtimeRate[responResIndex.get(respon)][1] = 1d;
            if (overtime.getIntheTime().equals("11-13")) overtimeRate[responResIndex.get(respon)][2] = 1d;
            if (overtime.getIntheTime().equals("13-15")) overtimeRate[responResIndex.get(respon)][3] = 1d;
            if (overtime.getIntheTime().equals("15-17")) overtimeRate[responResIndex.get(respon)][4] = 1d;
            if (overtime.getIntheTime().equals("17-19")) overtimeRate[responResIndex.get(respon)][5] = 1d;
            if (overtime.getIntheTime().equals("19-21")) overtimeRate[responResIndex.get(respon)][6] = 1d;
            if (overtime.getIntheTime().equals("21-23")) overtimeRate[responResIndex.get(respon)][7] = 1d;
            if (overtime.getIntheTime().equals("23-1")) overtimeRate[responResIndex.get(respon)][8] = 1d;
            if (overtime.getIntheTime().equals("1-3")) overtimeRate[responResIndex.get(respon)][9] = 1d;
            if (overtime.getIntheTime().equals("3-5")) overtimeRate[responResIndex.get(respon)][10] = 1d;
            if (overtime.getIntheTime().equals("5-7")) overtimeRate[responResIndex.get(respon)][11] = 1d;*/
            if (overtime.getIntheTime().equals("7-9")) overtimeRate[responResIndex.get(respon)][0] = rate;
            if (overtime.getIntheTime().equals("9-11")) overtimeRate[responResIndex.get(respon)][1] = rate;
            if (overtime.getIntheTime().equals("11-13")) overtimeRate[responResIndex.get(respon)][2] = rate;
            if (overtime.getIntheTime().equals("13-15")) overtimeRate[responResIndex.get(respon)][3] = rate;
            if (overtime.getIntheTime().equals("15-17")) overtimeRate[responResIndex.get(respon)][4] = rate;
            if (overtime.getIntheTime().equals("17-19")) overtimeRate[responResIndex.get(respon)][5] = rate;
            if (overtime.getIntheTime().equals("19-21")) overtimeRate[responResIndex.get(respon)][6] = rate;
            if (overtime.getIntheTime().equals("21-23")) overtimeRate[responResIndex.get(respon)][7] = rate;
            if (overtime.getIntheTime().equals("23-1")) overtimeRate[responResIndex.get(respon)][8] = rate;
            if (overtime.getIntheTime().equals("1-3")) overtimeRate[responResIndex.get(respon)][9] = rate;
            if (overtime.getIntheTime().equals("3-5")) overtimeRate[responResIndex.get(respon)][10] = rate;
            if (overtime.getIntheTime().equals("5-7")) overtimeRate[responResIndex.get(respon)][11] = rate;
        }
        Map<String, Object> result = new HashMap<>();
        String[] times = new String[12];
        times[0] = "7:00-9:00";
        times[1] = "9:00-11:00";
        times[2] = "11:00-13:00";
        times[3] = "13:00-15:00";
        times[4] = "15:00-17:00";
        times[5] = "17:00-19:00";
        times[6] = "19:00-21:00";
        times[7] = "21:00-23:00";
        times[8] = "23:00-1:00";
        times[9] = "1:00-3:00";
        times[10] = "3:00-5:00";
        times[11] = "5:00-7:00";
        result.put("小组", responlist);
        result.put("data", overtimeRate);
        result.put("时间段", times);
        return new Result(200, "查询成功", result);

    }

    /*@ApiOperation("根据日期生成数据")
    @GetMapping("/generateDataByDate")
    public Result generateDataByDate(String date) throws ParseException {
        groupOverTime(date + " 08:59:30");
        groupOverTime(date + " 10:59:30");
        groupOverTime(date + " 12:59:30");
        groupOverTime(date + " 14:59:30");
        groupOverTime(date + " 16:59:30");
        groupOverTime(date + " 18:59:30");
        groupOverTime(date + " 20:59:30");
        groupOverTime(date + " 22:59:30");
        groupOverTime(TimeUtil.getNextDay(date) + " 00:59:30");
        groupOverTime(TimeUtil.getNextDay(date) + " 02:59:30");
        groupOverTime(TimeUtil.getNextDay(date) + " 04:59:30");
        groupOverTime(TimeUtil.getNextDay(date) + " 06:59:30");
        return new Result(200, "添加成功");
    }*/

    @ApiOperation("根据时间生成数据")
    @GetMapping("/generateDataByDateTime")
    public Result groupOverTime(String dateTime) throws ParseException {
        System.out.println("===========更新小组超时送检（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        //Timestamp now = new Timestamp((Timestamp.valueOf(TimeUtil.getNowTimeByNormal()).getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
        Timestamp now = Timestamp.valueOf(dateTime);
        //Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
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
        return new Result(200, "添加成功");
    }


    @GetMapping("/listOverTimeRateGroupByLineBody")
    @ApiOperation("超时送检机台趋势（线体分组）")
    public Result listOverTimeRateGroupByLineBody(String factory, String lineBody, String process,
                                                  @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupMacOvertime> qw = new QueryWrapper<>();
        qw.between("ove.create_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("ove.test_type", 1);;
        List<Rate3> rates = dfGroupMacOvertimeService.listOverTimeRateGroupByLineBody(qw);

        Map<String, Integer> responResIndex = new HashMap<>();
        List<String> responlist = new ArrayList<>();
        int index = 0;
        for (Rate3 rate : rates) {
            String respon = rate.getStr1();
            if (!responResIndex.containsKey(respon)) {
                responResIndex.put(respon, index++);
                responlist.add(respon);
            }
        }
        Double[][] overtimeRate = new Double[index][12];
        for (Rate3 rate1 : rates) {
            String respon = rate1.getStr1();
            Double rate = rate1.getDou1();
            /*if (overtime.getIntheTime().equals("7-9")) overtimeRate[responResIndex.get(respon)][0] = 1d;
            if (overtime.getIntheTime().equals("9-11")) overtimeRate[responResIndex.get(respon)][1] = 1d;
            if (overtime.getIntheTime().equals("11-13")) overtimeRate[responResIndex.get(respon)][2] = 1d;
            if (overtime.getIntheTime().equals("13-15")) overtimeRate[responResIndex.get(respon)][3] = 1d;
            if (overtime.getIntheTime().equals("15-17")) overtimeRate[responResIndex.get(respon)][4] = 1d;
            if (overtime.getIntheTime().equals("17-19")) overtimeRate[responResIndex.get(respon)][5] = 1d;
            if (overtime.getIntheTime().equals("19-21")) overtimeRate[responResIndex.get(respon)][6] = 1d;
            if (overtime.getIntheTime().equals("21-23")) overtimeRate[responResIndex.get(respon)][7] = 1d;
            if (overtime.getIntheTime().equals("23-1")) overtimeRate[responResIndex.get(respon)][8] = 1d;
            if (overtime.getIntheTime().equals("1-3")) overtimeRate[responResIndex.get(respon)][9] = 1d;
            if (overtime.getIntheTime().equals("3-5")) overtimeRate[responResIndex.get(respon)][10] = 1d;
            if (overtime.getIntheTime().equals("5-7")) overtimeRate[responResIndex.get(respon)][11] = 1d;*/
            if ("h7h9".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][0] = rate;
            if ("h9h11".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][1] = rate;
            if ("h11h13".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][2] = rate;
            if ("h13h15".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][3] = rate;
            if ("h15h17".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][4] = rate;
            if ("h17h19".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][5] = rate;
            if ("h19h21".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][6] = rate;
            if ("h21h23".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][7] = rate;
            if ("h23h1".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][8] = rate;
            if ("h1h3".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][9] = rate;
            if ("h3h5".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][10] = rate;
            if ("h5h7".equals(rate1.getStr2()) && rate != null) overtimeRate[responResIndex.get(respon)][11] = rate;
        }
        Map<String, Object> result = new HashMap<>();
        String[] times = new String[12];
        times[0] = "7:00-9:00";
        times[1] = "9:00-11:00";
        times[2] = "11:00-13:00";
        times[3] = "13:00-15:00";
        times[4] = "15:00-17:00";
        times[5] = "17:00-19:00";
        times[6] = "19:00-21:00";
        times[7] = "21:00-23:00";
        times[8] = "23:00-1:00";
        times[9] = "1:00-3:00";
        times[10] = "3:00-5:00";
        times[11] = "5:00-7:00";
        result.put("times", times);
        result.put("lineBody", responlist);
        result.put("rate", overtimeRate);

        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listOverTimeRateGroupByLineBodyDetail")
    @ApiOperation("超时送检机台趋势（线体分组）详情")
    public Result listOverTimeRateGroupByLineBodyDetail(String factory, String lineBody, String process,
                                                  @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupMacOvertime> qw = new QueryWrapper<>();
        qw.between("ove.create_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("ove.test_type", 1);;
        List<Rate3> rates = dfGroupMacOvertimeService.listOverTimeRateGroupByLineBody(qw);

        Map<String, Map<String, Object>> map = new LinkedHashMap<>();

        for (Rate3 rate : rates) {
            Integer openNum = rate.getInte2();
            Integer allOverTimeNum = rate.getInte1();
            Double overRate = rate.getDou1();
            String line = rate.getStr1();
            String facLine = factory + line;
            String inTheTime = rate.getStr2();
            if (!map.containsKey(facLine + "openNum")) {
                Map<String, Object> openNumMap = new LinkedHashMap<>();
                Map<String, Object> allOverTimeNumMap = new LinkedHashMap<>();
                Map<String, Object> overRateMap = new LinkedHashMap<>();
                openNumMap.put(inTheTime, openNum);
                openNumMap.put("factory", factory);
                openNumMap.put("lineBody", line);
                openNumMap.put("project", "开机数量");
                allOverTimeNumMap.put(inTheTime, allOverTimeNum);
                allOverTimeNumMap.put("factory", factory);
                allOverTimeNumMap.put("lineBody", line);
                allOverTimeNumMap.put("project", "超时机台数");
                overRateMap.put(inTheTime, overRate);
                overRateMap.put("factory", factory);
                overRateMap.put("lineBody", line);
                overRateMap.put("project", "机台占比");
                map.put(facLine + "openNum", openNumMap);
                map.put(facLine + "allOverTimeNum", allOverTimeNumMap);
                map.put(facLine + "overRate", overRateMap);
            } else {
                map.get(facLine + "openNum").put(inTheTime, openNum);
                map.get(facLine + "allOverTimeNum").put(inTheTime, allOverTimeNum);
                map.get(facLine + "overRate").put(inTheTime, overRate);
            }
        }
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listOverTimeRateGroupByResponDetail")
    @ApiOperation("超时送检机台趋势（小组分组）详情")
    public Result listOverTimeRateGroupByResponDetail(String factory, String lineBody, String process,
                                                        @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupMacOvertime> qw = new QueryWrapper<>();
        qw.between("ove.create_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("ove.test_type", 1);;
        List<Rate3> rates = dfGroupMacOvertimeService.listOverTimeRateGroupByRespon(qw);

        Map<String, Map<String, Object>> map = new LinkedHashMap<>();

        for (Rate3 rate : rates) {
            Integer openNum = rate.getInte2();
            Integer allOverTimeNum = rate.getInte1();
            Double overRate = rate.getDou1();
            String respon = rate.getStr1();
            String facLine = factory + respon;
            String inTheTime = rate.getStr2();
            if (!map.containsKey(facLine + "openNum")) {
                Map<String, Object> openNumMap = new LinkedHashMap<>();
                Map<String, Object> allOverTimeNumMap = new LinkedHashMap<>();
                Map<String, Object> overRateMap = new LinkedHashMap<>();
                openNumMap.put(inTheTime, openNum);
                openNumMap.put("factory", factory);
                openNumMap.put("lineBody", respon);
                openNumMap.put("project", "开机数量");
                allOverTimeNumMap.put(inTheTime, allOverTimeNum);
                allOverTimeNumMap.put("factory", factory);
                allOverTimeNumMap.put("lineBody", respon);
                allOverTimeNumMap.put("project", "超时机台数");
                overRateMap.put(inTheTime, overRate);
                overRateMap.put("factory", factory);
                overRateMap.put("lineBody", respon);
                overRateMap.put("project", "机台占比");
                map.put(facLine + "openNum", openNumMap);
                map.put(facLine + "allOverTimeNum", allOverTimeNumMap);
                map.put(facLine + "overRate", overRateMap);
            } else {
                map.get(facLine + "openNum").put(inTheTime, openNum);
                map.get(facLine + "allOverTimeNum").put(inTheTime, allOverTimeNum);
                map.get(facLine + "overRate").put(inTheTime, overRate);
            }
        }
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listOverTimeRateGroupByResponTop")
    @ApiOperation("超时送检机台排名（小组分组）")
    public Result listOverTimeRateGroupByResponTop(String factory, String lineBody, String process,
                                                      @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupMacOvertime> qw = new QueryWrapper<>();
        qw.between("ove.create_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("ove.test_type", 1);
        List<Rate3> rates = dfGroupMacOvertimeService.listOverTimeRateGroupByResponTop(qw);

        Map<String, Object> result = new HashMap<>();
        List<String> responList = new ArrayList<>();
        List<Double> overTimeRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            responList.add(rate.getStr1());
            overTimeRateList.add(rate.getDou1());
        }
        result.put("respon", responList);
        result.put("overTimeRate", overTimeRateList);

        return new Result(200, "查询成功", result);
    }
}
