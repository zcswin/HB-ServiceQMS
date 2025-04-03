package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfFaiPassRate;
import com.ww.boengongye.service.DfFaiPassRateService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 首检通过率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
@Controller
@RequestMapping("/dfFaiPassRate")
@CrossOrigin
@ResponseBody
@Api(tags = "首检通过率")
public class DfFaiPassRateController {

    @Autowired
    private DfFaiPassRateService dfFaiPassRateService;

    @GetMapping("/listRate")
    public Result listRate(String factory, String process, String project, String linebody,
                           String startDate, Integer testType) throws ParseException {
        String endDate = null;
        if (null != startDate) {
            endDate = TimeUtil.getNextDay(startDate) + " 07:00:00";
            startDate = startDate + " 07:00:01";
        }
        DfFaiPassRate day = dfFaiPassRateService.getRate(factory, process, project, linebody, "白班", startDate, endDate, testType);
        DfFaiPassRate night = dfFaiPassRateService.getRate(factory, process, project, linebody, "夜班", startDate, endDate, testType);

        if (null != day) {
            day.setOpenMacRate(day.getOpenMacNum().doubleValue() / day.getAllMacNum());
            day.setFaiPassRate(day.getFaiPassNum().doubleValue() / day.getFaiOpenNum());
            day.setFaiOpenRate(day.getFaiOpenNum().doubleValue() / day.getOpenMacNum());
        }

        if (null != night) {
            night.setOpenMacRate(night.getOpenMacNum().doubleValue() / night.getAllMacNum());
            night.setFaiPassRate(night.getFaiPassNum().doubleValue() / night.getFaiOpenNum());
            night.setFaiOpenRate(night.getFaiOpenNum().doubleValue() / night.getOpenMacNum());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("白班", day);
        result.put("夜班", night);
        return new Result(200, "查询成功", result);

    }

}
