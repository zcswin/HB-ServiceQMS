package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfWorkOrderService;
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
 * 工单 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Controller
@RequestMapping("/dfWorkOrder")
@ResponseBody
@Api(tags = "工单")
@CrossOrigin
public class DfWorkOrderController {
    @Autowired
    private DfWorkOrderService dfWorkOrderService;

    @GetMapping("/listAll")
    public Result listAll() {
        return new Result(200, "查询成功", dfWorkOrderService.list());
    }
}
