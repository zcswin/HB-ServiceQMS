package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFurnaceDust;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.service.DfFurnaceDustService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 炉内尘点 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-05
 */
@Controller
@RequestMapping("/dfFurnaceDust")
@CrossOrigin
@Api(tags = "炉内尘点")
@ResponseBody
public class DfFurnaceDustController {

    @Autowired
    private DfFurnaceDustService dfFurnaceDustService;

    @PostMapping("uploadData")
    @ApiOperation("导入数据")
    public Result uploadData(MultipartFile file) throws Exception {
        int i = dfFurnaceDustService.importExcel(file);
        return new Result(200, "成功添加" + i + "条数据");
    }

    @GetMapping("/getProcessOKRate")
    @ApiOperation("获取工序的炉内尘点良率")
    public Result getProcessOKRate(@RequestParam String startDate,
                                   @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfFurnaceDust> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfFurnaceDustService.getProcessOKRate(qw);
        List<Object> result = new ArrayList<>();
        for (Rate3 rate : rates) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", rate.getStr1());
            map.put("um05Rate", rate.getDou1());
            map.put("um5Rate", rate.getDou2());
            map.put("totalRate", rate.getDou3());
            result.add(map);
        }
        return new Result<>(200, "查询成功", result);
    }

    @GetMapping("/getAllPositionDustNum")
    @ApiOperation("获取炉尘点数")
    public Result getAllPositionDustNum(@RequestParam String startDate,
                                   @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfFurnaceDust> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfFurnaceDustService.getAllPositionDustNum(qw);
        List<Object> result = new ArrayList<>();
        for (Rate3 rate : rates) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", rate.getStr1());
            map.put("um05Num", rate.getInte1());
            map.put("um5Num", rate.getInte2());
            result.add(map);
        }
        return new Result<>(200, "查询成功", result);
    }

    @GetMapping("getDustNumOrderByTime")
    @ApiOperation("获取该点位尘点数变化情况")
    public Result getDustNumOrderByTime(@RequestParam String startDate,
                                        @RequestParam String endDate,
                                        @RequestParam String position,
                                        @RequestParam String dateType) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfFurnaceDust> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime)
                .eq("position", position);
        String format;
        switch (dateType) {
            case "时": format = "%Y-%m-%d %h:%i"; break;
            case "日": format = "%Y-%m-%d %h"; break;
            case "周": format = "%Y-%m-%d"; break;
            case "月": format = "%Y 第%u周"; break;
            case "年": format = "%Y-%m"; break;
            default: return new Result<>(203, "上传类型错误");
        }
        List<Rate3> rates = dfFurnaceDustService.getDustNumOrderByTime(qw, format);
        Map<String, Object> result = new HashMap<>();
        List<String> time = new ArrayList<>();
        List<Integer> um05List = new ArrayList<>();
        List<Integer> um5List = new ArrayList<>();
        for (Rate3 rate : rates) {
            time.add(rate.getStr1());
            um05List.add(rate.getInte1());
            um5List.add(rate.getInte2());
        }
        result.put("time", time);
        result.put("um05", um05List);
        result.put("um5", um5List);
        return new Result<>(200, "查询成功",result);
    }

    @GetMapping("getPositionDetail")
    @ApiOperation("获取该点位尘详情")
    public Result getPositionDetail(@RequestParam String startDate,
                                        @RequestParam String endDate,
                                        @RequestParam String position) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfFurnaceDust> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime)
                .eq("position", position);
        List<Rate4> rates = dfFurnaceDustService.getPositionDetail(qw);
        List<Object> result = new ArrayList<>();
        for (Rate4 rate : rates) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", rate.getStr1() + "05um");
            map1.put("time", rate.getStr2());
            map1.put("value", rate.getInte1());
            map1.put("min", rate.getInte3());
            map1.put("max", rate.getInte4());
            map1.put("avg", rate.getDou1());
            result.add(map1);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("name", rate.getStr1() + "5um");
            map2.put("time", rate.getStr2());
            map2.put("value", rate.getInte2());
            map2.put("min", rate.getInte5());
            map2.put("max", rate.getInte6());
            map2.put("avg", rate.getDou2());
            result.add(map2);
        }
        return new Result<>(200, "查询成功",result);
    }

}
