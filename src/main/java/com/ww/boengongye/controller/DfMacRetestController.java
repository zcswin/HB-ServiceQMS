package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacRetestService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 机台复测 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Controller
@RequestMapping("/dfMacRetest")
@CrossOrigin
@ResponseBody
@Api(tags = "机台复测")
public class DfMacRetestController {

    @Autowired
    private DfMacRetestService dfMacRetestService;

    @GetMapping("/listBastPassRate")
    public Result listBastPassRate(String factory, String process, String project, String linebody,
                                   String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:00";
        List<DfMacRetest> dfMacRetestList = dfMacRetestService.listBastPassRate(factory, process, project, linebody,
                startDate, endDate, testType);
        List<String> macCode = new ArrayList<>();
        List<Double> macRate = new ArrayList<>();
        for (DfMacRetest macRetest : dfMacRetestList) {
            macCode.add(macRetest.getMachineCode());
            macRate.add(macRetest.getPassRate());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("机台", macCode);
        result.put("复测通过率", macRate);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listPoorestMacResponseTime")
    public Result listPoorestMacResponseTime(String factory, String process, String project, String linebody,
                                   String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:00";
        List<DfMacRetest> day = dfMacRetestService.listPoorestMacResponseTime(factory, process, project,
                linebody, "白班", startDate, endDate, testType);
        List<DfMacRetest> night = dfMacRetestService.listPoorestMacResponseTime(factory, process, project,
                linebody, "夜班", startDate, endDate, testType);
        List<String> dayMacCode = new ArrayList<>();
        List<String> nightMacCode = new ArrayList<>();
        List<Double> dayResponseTime = new ArrayList<>();
        List<Double> nightResponseTime = new ArrayList<>();
        for (DfMacRetest macRetest : day) {
            dayMacCode.add(macRetest.getMachineCode());
            dayResponseTime.add(macRetest.getResponseTime());
        }
        for (DfMacRetest macRetest : night) {
            nightMacCode.add(macRetest.getMachineCode());
            nightResponseTime.add(macRetest.getResponseTime());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("白班机台", dayMacCode);
        result.put("白班响应时间", dayResponseTime);
        result.put("夜班机台", nightMacCode);
        result.put("夜班响应时间", nightResponseTime);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listDayNightMacOkRate")
    @ApiOperation("机台白夜班复测通过率")
    public Result listDayNightMacOkRate(@RequestParam String startDate, @RequestParam String endDate, String factory,
                                        String lineBody, String project, String process, String machineCode) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.between("ret.create_time", startTime, endTime)
                .eq(!"".equals(factory) && null != factory, "ret.factory", factory)
                .eq(!"".equals(lineBody) && null != lineBody, "ret.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "ret.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process)
                .eq(!"".equals(machineCode) && null != machineCode, "ret.machine_code", machineCode)
                .eq("test_type", 1);
        List<Rate3> rates = dfMacRetestService.listDayNightMacOkRate(qw);
        Map<String, Object> result = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Double> dayOkRateList = new ArrayList<>();
        List<Double> nightOkRateList = new ArrayList<>();
        List<Double> OkRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            dayOkRateList.add(rate.getDou1());
            nightOkRateList.add(rate.getDou2());
            OkRateList.add(rate.getDou3());
        }
        result.put("date", dateList);
        result.put("dayOkRate", dayOkRateList);
        result.put("nightOkRate", nightOkRateList);
        result.put("OkRate", OkRateList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listDayNightMacOkRateDetail")
    @ApiOperation("机台白夜班复测通过率详情")
    public Result listDayNightMacOkRateDetail(@RequestParam String startDate, @RequestParam String endDate, String factory,
                                                String lineBody, String project, String process, String machineCode,
                                                String dayOrNight) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.between("ret.create_time", startTime, endTime)
                .between("HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(factory) && null != factory, "ret.factory", factory)
                .eq(!"".equals(lineBody) && null != lineBody, "ret.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "ret.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process)
                .eq(!"".equals(machineCode) && null != machineCode, "ret.machine_code", machineCode)
                .eq("test_type", 1);
        List<Rate3> rates = dfMacRetestService.listDayNightMacOkRateAndResTimeDetail(qw);
        Map<String, Object> result = new LinkedHashMap<>();
        List<Object> rateData = new ArrayList<>();
        Set<Object> dateList = new TreeSet<>();
        if (rates.size() > 0) {
            String lastMac = null;
            Map<String, Object> map = new LinkedHashMap<>();
            List<Object> rateList = new ArrayList<>();
            for (Rate3 rate : rates) {
                String thisMac = rate.getStr2();
                String date = rate.getStr1();
                dateList.add(date);
                if (null == lastMac) {
                    map.put("machineCode", thisMac);
                    rateList.add(rate.getDou1());
                    map.put("rateList", rateList);
                    lastMac = thisMac;
                } else if (!lastMac.equals(thisMac)) {
                    rateData.add(map);
                    map = new LinkedHashMap<>();
                    rateList = new ArrayList<>();
                    map.put("machineCode", thisMac);
                    rateList.add(rate.getDou1());
                    map.put("rateList", rateList);
                    lastMac = thisMac;
                } else {
                    rateList.add(rate.getDou1());
                }
            }
            rateData.add(map);
        }
        result.put("dateList", dateList);
        result.put("rateData", rateData);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listMacOkRateTop")
    @ApiOperation("机台复测最差排名")
    public Result listMacOkRateTop(@RequestParam String startDate, @RequestParam String endDate, String factory, String lineBody,
                                        String project, String process, String dayOrNight) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.between("ret.create_time", startTime, endTime)
                .between("HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(factory) && null != factory, "ret.factory", factory)
                .eq(!"".equals(lineBody) && null != lineBody, "ret.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "ret.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process)
                .eq("test_type", 1);
        List<Rate3> rates = dfMacRetestService.listMacOkRateTop(qw);
        Map<String, Object> result = new HashMap<>();
        List<String> macList = new ArrayList<>();
        List<Double> OkRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            macList.add(rate.getStr1());
            OkRateList.add(rate.getDou1());
        }
        result.put("machineCode", macList);
        result.put("OkRate", OkRateList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listMacResponseTimeDetail")
    @ApiOperation("机台反馈响应时间详情")
    public Result listMacResponseTimeDetail(@RequestParam String startDate, @RequestParam String endDate, String factory,
                                              String lineBody, String project, String process, String machineCode,
                                              String dayOrNight) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.between("ret.create_time", startTime, endTime)
                .between("HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(factory) && null != factory, "ret.factory", factory)
                .eq(!"".equals(lineBody) && null != lineBody, "ret.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "ret.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process)
                .eq(!"".equals(machineCode) && null != machineCode, "ret.machine_code", machineCode)
                .eq("test_type", 1);
        List<Rate3> rates = dfMacRetestService.listDayNightMacOkRateAndResTimeDetail(qw);
        Map<String, Object> result = new LinkedHashMap<>();
        List<Object> timeData = new ArrayList<>();
        Set<Object> dateList = new TreeSet<>();
        if (rates.size() > 0) {
            String lastMac = null;
            Map<String, Object> map = new LinkedHashMap<>();
            List<Object> timeList = new ArrayList<>();
            for (Rate3 rate : rates) {
                String thisMac = rate.getStr2();
                String date = rate.getStr1();
                dateList.add(date);
                if (null == lastMac) {
                    map.put("machineCode", thisMac);
                    timeList.add(rate.getDou2());
                    map.put("timeList", timeList);
                    lastMac = thisMac;
                } else if (!lastMac.equals(thisMac)) {
                    timeData.add(map);
                    map = new LinkedHashMap<>();
                    timeList = new ArrayList<>();
                    map.put("machineCode", thisMac);
                    timeList.add(rate.getDou2());
                    map.put("timeList", timeList);
                    lastMac = thisMac;
                } else {
                    timeList.add(rate.getDou2());
                }
            }
            timeData.add(map);
        }
        result.put("dateList", dateList);
        result.put("timeList", timeData);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listMacResponseTimeTop")
    @ApiOperation("机台响应时间最差排名")
    public Result listMacResponseTimeTop(@RequestParam String startDate, @RequestParam String endDate, String factory, String lineBody,
                                   String project, String process, String dayOrNight) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.between("ret.create_time", startTime, endTime)
                .between("HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(factory) && null != factory, "ret.factory", factory)
                .eq(!"".equals(lineBody) && null != lineBody, "ret.linebody", lineBody)
                .eq(!"".equals(project) && null != project, "ret.project", project)
                .eq(!"".equals(process) && null != process, "pro.process_name", process)
                .eq("test_type", 1);
        List<Rate3> rates = dfMacRetestService.listMacResponseTimeTop(qw);
        Map<String, Object> result = new HashMap<>();
        List<String> macList = new ArrayList<>();
        List<Double> responseTimeList = new ArrayList<>();
        for (Rate3 rate : rates) {
            macList.add(rate.getStr1());
            responseTimeList.add(rate.getDou1());
        }
        result.put("machineCode", macList);
        result.put("responseTime", responseTimeList);
        return new Result(200, "查询成功", result);
    }

}
