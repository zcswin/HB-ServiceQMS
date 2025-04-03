package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfGroupOkRateService;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小组两小时时间段良率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
@Controller
@RequestMapping("/dfGroupOkRate")
@CrossOrigin
@ResponseBody
@Api(tags = "小组两小时时间段良率")
public class DfGroupOkRateController {

    @Autowired
    private DfGroupOkRateService dfGroupOkRateService;

    @ApiOperation("根据时间生成数据")
    @GetMapping("/generateDataByDate")
    public Result generateDataByDate(String startDate, String endDate) throws ParseException {
        String[] times = {" 00:59:30"," 02:59:30"," 04:59:30"," 06:59:30"," 08:59:30"," 10:59:30",
                          " 12:59:30"," 14:59:30"," 16:59:30"," 18:59:30"," 20:59:30"," 22:59:30"};
        while(!startDate.equals(endDate)) {
            for (String time : times) {
                dfGroupOkRateService.generateDataByDateTime(startDate + time);
            }
            startDate = TimeUtil.getNextDay(startDate);
        }
        return new Result(200, "添加成功");
    }

    @ApiOperation("小组两小时良率对比")
    @GetMapping("/listGroupOkRate")
    public Result listGroupOkRate(String factory, String lineBody, String process,
                                                  @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupOkRate> qw = new QueryWrapper<>();
        qw.between("okr.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("okr.test_type", 1);
        List<Rate3> rates = dfGroupOkRateService.listGroupOkRate(qw);

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
            if (rate1.getStr2().equals("h7h9") && rate != 0) overtimeRate[responResIndex.get(respon)][0] = rate;
            if (rate1.getStr2().equals("h9h11") && rate != 0) overtimeRate[responResIndex.get(respon)][1] = rate;
            if (rate1.getStr2().equals("h11h13") && rate != 0) overtimeRate[responResIndex.get(respon)][2] = rate;
            if (rate1.getStr2().equals("h13h15") && rate != 0) overtimeRate[responResIndex.get(respon)][3] = rate;
            if (rate1.getStr2().equals("h15h17") && rate != 0) overtimeRate[responResIndex.get(respon)][4] = rate;
            if (rate1.getStr2().equals("h17h19") && rate != 0) overtimeRate[responResIndex.get(respon)][5] = rate;
            if (rate1.getStr2().equals("h19h21") && rate != 0) overtimeRate[responResIndex.get(respon)][6] = rate;
            if (rate1.getStr2().equals("h21h23") && rate != 0) overtimeRate[responResIndex.get(respon)][7] = rate;
            if (rate1.getStr2().equals("h23h1") && rate != 0) overtimeRate[responResIndex.get(respon)][8] = rate;
            if (rate1.getStr2().equals("h1h3") && rate != 0) overtimeRate[responResIndex.get(respon)][9] = rate;
            if (rate1.getStr2().equals("h3h5") && rate != 0) overtimeRate[responResIndex.get(respon)][10] = rate;
            if (rate1.getStr2().equals("h5h7") && rate != 0) overtimeRate[responResIndex.get(respon)][11] = rate;
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
        result.put("respon", responlist);
        result.put("rate", overtimeRate);

        return new Result(200, "查询成功", result);
    }

    @ApiOperation("小组两小时良率对比详情")
    @GetMapping("/listGroupOkRateDetail")
    public Result listGroupOkRateDetail(String factory, String lineBody, String process,
                                  @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupOkRate> qw = new QueryWrapper<>();
        qw.between("okr.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("okr.test_type", 1);
        List<Rate3> rates = dfGroupOkRateService.listGroupOkRate(qw);

        Map<String, Map<String, Object>> rateData = new HashMap<>();
        for (Rate3 rate : rates) {
            String respon = rate.getStr1();
            if (!rateData.containsKey(respon)) {
                Map<String, Object> map = new HashMap<>();
                map.put("respon", respon);
                map.put(rate.getStr2(), rate.getDou1());
                rateData.put(respon, map);
            } else {
                rateData.get(respon).put(rate.getStr2(), rate.getDou1());
            }
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : rateData.entrySet()) {
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);
    }

    @ApiOperation("良率最差机台小组排名")
    @GetMapping("/listGroupOkRateTop")
    public Result listGroupOkRateTop(String factory, String lineBody, String process,
                                        @RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfGroupOkRate> qw = new QueryWrapper<>();
        qw.between("okr.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("okr.test_type", 1);
        List<Rate3> rates = dfGroupOkRateService.listGroupOkRateTop(qw);

        Map<String, Object> result = new HashMap<>();
        List<String> responList = new ArrayList<>();
        List<Double> okRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            responList.add(rate.getStr1());
            okRateList.add(rate.getDou1());
        }
        result.put("respon", responList);
        result.put("okRate", okRateList);

        return new Result(200, "查询成功", result);
    }

}
