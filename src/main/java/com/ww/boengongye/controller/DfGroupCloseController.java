package com.ww.boengongye.controller;


import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupClose;
import com.ww.boengongye.entity.DfGroupOkRate;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.service.DfGroupCloseService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/dfGroupClose")
@CrossOrigin
@Api(tags = "小组关闭率")
@ResponseBody
public class DfGroupCloseController {
    @Autowired
    private DfGroupCloseService dfGroupCloseService;

    @ApiOperation("根据时间生成数据")
    @GetMapping("/generateDataByDate")
    public Result generateDataByDate(String startDate, String endDate) throws ParseException {
        String[] times = {" 06:59:30", " 18:59:30"};
        while(!startDate.equals(endDate)) {
            for (String time : times) {
                dfGroupCloseService.generateDataByDateTime(startDate + time);
            }
            startDate = TimeUtil.getNextDay(startDate);
        }
        return new Result(200, "添加成功");
    }

    @ApiOperation("关闭率趋势")
    @GetMapping("listCloseRate")
    public Result listCloseRate(String factory, String lineBody, String process,
                                @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfGroupClose> qw = new QueryWrapper<>();
        qw.between("clo.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("clo.test_type", 1);
        List<Rate3> rates = dfGroupCloseService.listCloseRate(qw);

        Map<String, Object> result = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Integer> dayNumList = new ArrayList<>();
        List<Double> dayRateList = new ArrayList<>();
        List<Integer> nightNumList = new ArrayList<>();
        List<Double> nightRateList = new ArrayList<>();
        List<Double> closeRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            if ("白班".equals(rate.getStr2())) {
                dayNumList.add(rate.getInte1());
                dayRateList.add(rate.getDou1());
            } else {
                nightNumList.add(rate.getInte1());
                nightRateList.add(rate.getDou1());
            }
            if (dayNumList.size() != closeRateList.size() && nightNumList.size() != closeRateList.size()) {
                dateList.add(rate.getStr1());
                closeRateList.add(rate.getDou2());
            }
        }
        result.put("date", dateList);
        result.put("dayNum", dayNumList);
        result.put("dayRate", dayRateList);
        result.put("nightNum", nightNumList);
        result.put("nightRate", nightRateList);
        result.put("closeRate", closeRateList);

        return new Result(200, "查询成功", result);
    }

    @ApiOperation("小组最差关闭率排名")
    @GetMapping("listGroupCloseRateTop")
    public Result listGroupCloseRateTop(String factory, String lineBody, String process,
                                @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfGroupClose> qw = new QueryWrapper<>();
        qw.between("clo.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("clo.test_type", 1);
        List<Rate3> rates = dfGroupCloseService.listGroupCloseRateTop(qw);

        Map<String, Object> result = new HashMap<>();
        List<String> responList = new ArrayList<>();
        List<Double> closeRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            responList.add(rate.getStr1());
            closeRateList.add(rate.getDou1());
        }
        result.put("respon", responList);
        result.put("closeRate", closeRateList);

        return new Result(200, "查询成功", result);
    }


}
