package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Controller
@RequestMapping("/dfMacStatusDetail")
@CrossOrigin
@ResponseBody
@Api(tags = "设备状态持续时间明细")
public class DfMacStatusDetailController {

    @Autowired
    private DfMacStatusDetailService dfMacStatusDetailService;

    @GetMapping (value ="/listNg")
    public Result listNg(int count) {
        QueryWrapper<DfMacStatusDetail> qw=new QueryWrapper<>();
//        qw.eq("s.StatusID_Cur",3);
        qw.orderByDesc("s.create_time");
        qw.last("limit 0,"+count);
        return new Result(0,"查询成功", dfMacStatusDetailService.listJoinCode(qw));
    }

    @ApiOperation("加工信息")
    @GetMapping("/listNormalStatus")
    public Result listNormalStatus() {
        return new Result(200, "查询成功", dfMacStatusDetailService.listNormalStatus());
    }

    @ApiOperation("告警信息")
    @GetMapping("/listWarningStatus")
    public Result listWarningStatus() {
        return new Result(200, "查询成功", dfMacStatusDetailService.listWarningStatus());
    }
}
