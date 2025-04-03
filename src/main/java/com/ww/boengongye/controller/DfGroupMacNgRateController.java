package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupMacNgRate;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.service.DfGroupMacNgRateService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 小组机台NG率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Controller
@RequestMapping("/dfGroupMacNgRate")
@CrossOrigin
@ResponseBody
@Api(tags = "小组机台NG率")
public class DfGroupMacNgRateController {

    @Autowired
    private DfGroupMacNgRateService dfGroupMacNgRateService;

    @ApiOperation("根据时间生成数据")
    @GetMapping("/generateDataByDate")
    public Result generateDataByDate(String startDate, String endDate) throws ParseException {
        String[] times = {" 06:59:30", " 18:59:30"};
        while(!startDate.equals(endDate)) {
            for (String time : times) {
                dfGroupMacNgRateService.generateDataByDateTime(startDate + time);
            }
            startDate = TimeUtil.getNextDay(startDate);
        }
        return new Result(200, "添加成功");
    }

    @ApiOperation("小组机台NG率汇总（小组分组）")
    @GetMapping("/listGroupMacNgRateTop")
    public Result listGroupMacNgRateTop(String factory, String lineBody, String process,
                                        @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfGroupMacNgRate> qw = new QueryWrapper<>();
        qw.between("ng.test_time", startTime, endTime)
                .eq(!"".equals(lineBody) && null != lineBody, "gro.linebody", lineBody)
                .eq(!"".equals(process) && null != process, "gro.process", process)
                .eq("ng.test_type", 1);
        List<Rate3> rates = dfGroupMacNgRateService.listGroupMacNgRateTop(qw);
        Map<String, Map<String, List<Object>>> responResData = new LinkedHashMap<>();
        for (Rate3 rate : rates) {
            String respon = rate.getStr1();
            String machine = rate.getStr2();
            Double ngRate = rate.getDou1();
            if (ngRate == 0) {
                continue;
            }
            if (!responResData.containsKey(respon)) {
                Map<String, List<Object>> dataResList = new HashMap<>();
                List<Object> machineList = new ArrayList<>();
                machineList.add(machine);
                List<Object> ngRateList = new ArrayList<>();
                ngRateList.add(ngRate);
                List<Object> responList = new ArrayList<>();
                responList.add(respon);
                dataResList.put("machine", machineList);
                dataResList.put("ngRate", ngRateList);
                dataResList.put("respon", responList);
                responResData.put(respon, dataResList);
            } else {
                responResData.get(respon).get("machine").add(machine);
                responResData.get(respon).get("ngRate").add(ngRate);
            }
        }
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : responResData.entrySet()) {
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);
    }
}
