package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfMacResOrder;
import com.ww.boengongye.service.DfMacResOrderService;
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
 * 机台-工单关系 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-12
 */
@Controller
@RequestMapping("/dfMacResOrder")
@CrossOrigin
@ResponseBody
@Api(tags = "工单-机台联系")
public class DfMacResOrderController {

    @Autowired
    private DfMacResOrderService dfMacResOrderService;

    @GetMapping("/listAll")
    public Result listAll(String workOrderCode, String plainCode) {
        QueryWrapper<DfMacResOrder> qw = new QueryWrapper<>();
        if (null != plainCode && !"".equals(plainCode)) {
            qw.eq("pd.plain_code", plainCode);
        } else {
            qw.eq("res.work_order_code", workOrderCode);
        }
        return new Result(200, "查询成功", dfMacResOrderService.listAllJoinWorkOrder(qw));
    }

}
