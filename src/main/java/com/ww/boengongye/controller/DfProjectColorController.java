package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfProjectColor;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 项目颜色配置 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-01-05
 */
@Controller
@RequestMapping("/dfProjectColor")
@ResponseBody
@CrossOrigin
@Api(tags = "项目颜色")
public class DfProjectColorController {
    private static final Logger logger = LoggerFactory.getLogger(DfProjectColorController.class);

    @Autowired
    com.ww.boengongye.service.DfProjectColorService dfProjectColorService;

    @RequestMapping(value = "/listAll")
    public Object listAll(String project) {
        QueryWrapper<DfProjectColor>qw=new QueryWrapper<>();
        qw.eq("project_name",project);
        return new Result(0, "查询成功",dfProjectColorService.list(qw));
    }

}
