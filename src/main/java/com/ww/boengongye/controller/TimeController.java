package com.ww.boengongye.controller;

import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Time")
@ResponseBody
@CrossOrigin
@Api(tags = "时间")
public class TimeController {

    @GetMapping("/getNowTime")
    public Result getNowTime() {
    String now=  TimeUtil.getNowTimeByNormal();

        return new Result(200,"查询成功",now);
    }
}
