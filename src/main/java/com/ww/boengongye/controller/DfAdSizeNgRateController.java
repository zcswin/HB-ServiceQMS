package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.ww.boengongye.service.DfAdSizeNgRateService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工调机排名--尺寸NG率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Controller
@RequestMapping("/dfAdSizeNgRate")
@Api(tags = "员工调机排名--尺寸NG率")
@CrossOrigin
@ResponseBody
public class DfAdSizeNgRateController {

    @Autowired
    private DfAdSizeNgRateService dfAdSizeNgRateService;

    @GetMapping("/listNgRateTop10")
    public Result listNgRateTop10(String factory, String process, String dayOrNight, String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:01";
        QueryWrapper<DfAdSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "factory", factory)
                .eq(null != process && !"".equals(process), "process", process)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "day_or_night", dayOrNight)
                .eq(null != testType, "test_type", testType)
                .between(null != startDate && null != endDate,
                        "create_time", startDate, endDate);
        List<DfAdSizeNgRate> dfAdSizeNgRates = dfAdSizeNgRateService.listNgTop10(qw);
        Map<String, Object> result = new HashMap<>();
        List<String> itemNames = new ArrayList<>();
        List<Double> ngRate = new ArrayList<>();
        for (DfAdSizeNgRate dfAdSizeNgRate : dfAdSizeNgRates) {
            itemNames.add(dfAdSizeNgRate.getItemName());
            ngRate.add(dfAdSizeNgRate.getRateNg());
        }

        result.put("name", itemNames);
        result.put("value", ngRate);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("listAppearNgInfos")
    public Result listAppearNgInfos(String startTime, String endTime) {
        List<DfAdSizeNgRate> list = dfAdSizeNgRateService.listAppearNgInfos(startTime, endTime);
        for (DfAdSizeNgRate dfAdSizeNgRate : list) {
            System.out.println(dfAdSizeNgRate);
        }
        return new Result(200, "查询成功");
    }

}
