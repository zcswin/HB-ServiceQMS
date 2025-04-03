package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfFurnaceOkRate;
import com.ww.boengongye.service.DfFurnaceOkRateService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 炉内尘点数量-良率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-06
 */
@Controller
@RequestMapping("/dfFurnaceOkRate")
@CrossOrigin
@ResponseBody
@Api(tags = "炉内尘点数量-良率")
public class DfFurnaceOkRateController {
    @Autowired
    private DfFurnaceOkRateService dfFurnaceOkRateService;

    @PostMapping("insert")
    public Result insert(DfFurnaceOkRate dfFurnaceOkRate) {
        dfFurnaceOkRateService.save(dfFurnaceOkRate);
        return new Result<>(200, "添加成功");
    }



}
