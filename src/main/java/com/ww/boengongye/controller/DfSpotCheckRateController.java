package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfSpotCheckRate;
import com.ww.boengongye.service.DfSpotCheckRateService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 点检率表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-14
 */
@Controller
@RequestMapping("/dfSpotCheckRate")
@CrossOrigin
@ResponseBody
public class DfSpotCheckRateController {

    @Autowired
    private DfSpotCheckRateService dfSpotCheckRateService;

    @GetMapping("/listAll")
    public Result listAll(int page, int limit, Integer factoryId, Integer checkYear, Integer checkMonth) {
        Page<DfSpotCheckRate> pages = new Page<>(page, limit);
        QueryWrapper<DfSpotCheckRate> qw = new QueryWrapper<>();
        if (factoryId != null && !"".equals(factoryId)) qw.eq("spo.factory_id", factoryId);
        if (checkYear != null && !"".equals(checkYear)) qw.eq("spo.check_year", checkYear);
        if (checkMonth != null && !"".equals(checkMonth)) qw.eq("spo.check_month", checkMonth);
        qw.orderByDesc("create_time");
        IPage<DfSpotCheckRate> page1 = dfSpotCheckRateService.pageJoinFactory(pages, qw);
        return new Result(200, "查询成功", (int) page1.getTotal(), page1);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfSpotCheckRate dfSpotCheckRate) {
        dfSpotCheckRate.setPassNum(0);
        dfSpotCheckRate.setFailNum(0);
        dfSpotCheckRateService.save(dfSpotCheckRate);
        return Result.INSERT_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfSpotCheckRate dfSpotCheckRate) {
        dfSpotCheckRateService.updateById(dfSpotCheckRate);
        return Result.UPDATE_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        dfSpotCheckRateService.removeById(id);
        return Result.DELETE_SUCCESS;
    }
}
