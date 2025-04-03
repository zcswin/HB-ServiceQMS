package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfRfidRiskDetail;
import com.ww.boengongye.entity.DfScadaLockMacData;
import com.ww.boengongye.service.DfScadaLockMacDataService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * SCADA锁机/解锁 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2025-01-10
 */
@RestController
@RequestMapping("/dfScadaLockMacData")
@Api(tags = "锁机记录")
@CrossOrigin
public class DfScadaLockMacDataController {
    @Autowired
    DfScadaLockMacDataService dfScadaLockMacDataService;
    @GetMapping(value = "/getByMachineCode")
    @ApiOperation("获取机台锁机状态")
    public Object getByMachineCode(String machineCode) {

            QueryWrapper<DfScadaLockMacData> qw=new QueryWrapper<>();
            qw.eq("machine_code",machineCode);
            qw.orderByDesc("id");
            qw.last("limit 1");
            return new Result(200, "查询成功", dfScadaLockMacDataService.getOne(qw));


    }
}
