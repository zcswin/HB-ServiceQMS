package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfPlainData;
import com.ww.boengongye.service.DfPlainDataService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 明码信息表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Controller
@RequestMapping("/dfPlainData")
@ResponseBody
@Api(tags = "单片")
@CrossOrigin
public class DfPlainDataController {

    @Autowired
    private DfPlainDataService dfPlainDataService;

    @GetMapping("/listAll")
    public Result listAll(Integer workOrderId) {
        LambdaQueryWrapper<DfPlainData> qw = new LambdaQueryWrapper<>();
        qw.eq(null != workOrderId, DfPlainData::getWorkOrderId, workOrderId);
        return new Result(200, "查询成功", dfPlainDataService.list(qw));
    }

}
