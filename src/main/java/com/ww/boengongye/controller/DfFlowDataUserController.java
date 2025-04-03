package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfFlowDataUserService;
import com.ww.boengongye.service.DfFlowOpinionService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-28
 */
@Controller
@RequestMapping("/dfFlowDataUser")
@ResponseBody
@CrossOrigin
public class DfFlowDataUserController {

    @Autowired
    DfFlowDataUserService dfFlowDataUserService;
    @ApiOperation("保存审批单和用户关系")
    @PostMapping(value = "/save")
    public Result save(@RequestBody DfFlowDataUser datas) {
        QueryWrapper<DfFlowDataUser> qw1 = new QueryWrapper<DfFlowDataUser>();
        qw1.eq("flow_data_id", datas.getFlowDataId());
        qw1.eq("user_account", datas.getUserAccount());
        DfFlowDataUser fb = dfFlowDataUserService.getOne(qw1);
        if (null == datas) {
            dfFlowDataUserService.save(datas);
        }
        return new Result(200, "保存成功");
    }

    @GetMapping("/insertDataFromOpinion")
    public Result insertDataFromOpinion(String startTime, String endTime) {
        List<DfFlowDataUser> dfFlowDataUsers = dfFlowDataUserService.insertDataFromOpinion(startTime, endTime);
        dfFlowDataUserService.saveBatch(dfFlowDataUsers);
        return new Result(200, "成功插入" + dfFlowDataUsers.size() + "条数据");
    }

}
