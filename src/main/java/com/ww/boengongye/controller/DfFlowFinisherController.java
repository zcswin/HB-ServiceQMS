package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfFlowFinisher;
import com.ww.boengongye.service.DfFlowFinisherService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-20
 */
@Controller
@RequestMapping("/dfFlowFinisher")
@CrossOrigin
@ResponseBody
@Api(tags = "可办结人员")
public class DfFlowFinisherController {
    @Autowired
    private DfFlowFinisherService dfFlowFinisherService;

    @GetMapping("/listAll")
    public Result listAll() {
        List<DfFlowFinisher> list = dfFlowFinisherService.list();
        if (null != list) {
            return new Result(200, "查询成功", list);
        }
        return new Result(203, "查无数据");
    }
}
