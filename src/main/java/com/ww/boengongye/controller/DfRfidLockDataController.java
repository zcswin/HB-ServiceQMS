package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.RFID.UnLockClampCode;
import com.ww.boengongye.service.DfRfidLockDataService;
import com.ww.boengongye.service.DfScadaLockMacDataService;
import com.ww.boengongye.utils.HttpUtil;
import com.ww.boengongye.utils.RFIDResult3;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * SCADA锁机/解锁 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2025-01-12
 */
@Controller
@RequestMapping("/dfRfidLockData")
@ResponseBody
@CrossOrigin
@Api(tags = "解锁RFID风险品")
public class DfRfidLockDataController {
    @Autowired
    DfRfidLockDataService dfRfidLockDataService;

    @Autowired
    Environment env;
    @PostMapping (value = "/update")
    @ApiOperation("获取机台锁机状态")
    public Object getByMachineCode(@RequestBody UnLockClampCode data) {
        String logData = JSON.toJSONString(data);
        DfRfidLockData rd = new DfRfidLockData();

        rd.setStatus(data.getStatus());
        rd.setLogData(logData);
        dfRfidLockDataService.save(rd);

        Map<String, String> headers = new HashMap<>();
        //请求RFID
        HttpParamTime param = new HttpParamTime(60);
        RFIDResult3 dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDUnLockURL"), null, headers, logData,false), RFIDResult3.class);

        return new Result(200, "查询成功");


    }
}
