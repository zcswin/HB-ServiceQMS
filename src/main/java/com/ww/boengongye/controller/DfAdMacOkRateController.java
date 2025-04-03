package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.ww.boengongye.service.DfAdMacOkRateService;
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
 * 员工调机排名--机台良率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Controller
@RequestMapping("/dfAdMacOkRate")
@CrossOrigin
@ResponseBody
@Api(tags = "员工调机排名--机台良率")
public class DfAdMacOkRateController {

    @Autowired
    private DfAdMacOkRateService dfAdMacOkRateService;

    @GetMapping("/listOkRateTop10")
    public Result listOkRateTop10(String factory, String process, String dayOrNight, String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:01";
        QueryWrapper<DfAdMacOkRate> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "factory", factory)
                .eq(null != process && !"".equals(process), "process", process)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "day_or_night", dayOrNight)
                .eq(null != testType, "test_type", testType)
                .between(null != startDate && null != endDate,
                        "create_time", startDate, endDate);
        List<DfAdMacOkRate> dfAdMacOkRates = dfAdMacOkRateService.listOkRateTop10(qw);
        Map<String, Object> result = new HashMap<>();

        List<String> machine = new ArrayList<>();
        List<Double> okRate = new ArrayList<>();
        for (DfAdMacOkRate dfAdMacOkRate : dfAdMacOkRates) {
            machine.add(dfAdMacOkRate.getMachineCode());
            okRate.add(dfAdMacOkRate.getRateOk());
        }

        result.put("name", machine);
        result.put("value", okRate);
        return new Result(200, "查询成功", result);
    }
}
