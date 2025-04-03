package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfQmsSpotCheckMan;
import com.ww.boengongye.service.DfQmsSpotCheckManService;
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
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-06-12
 */
@Controller
@RequestMapping("/dfQmsSpotCheckMan")
@Api(tags = "外观确认人,抽检人")
@ResponseBody
@CrossOrigin
public class DfQmsSpotCheckManController {

    @Autowired
    DfQmsSpotCheckManService dfQmsSpotCheckManService;

    @GetMapping(value = "/listByType")
    public Object listByType(String type) {
        QueryWrapper<DfQmsSpotCheckMan>qw=new QueryWrapper<>();
        qw.eq("type",type);
        return new Result(0, "查询成功", dfQmsSpotCheckManService.list(qw));
    }

}
