package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfSizeContRelation;
import com.ww.boengongye.service.DfSizeContRelationService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
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
 * @since 2024-12-26
 */
@Controller
@RequestMapping("/dfSizeContRelation")
@ResponseBody
@CrossOrigin
@ApiOperation("TZ尺寸关联测试项")
public class DfSizeContRelationController {

    @Autowired
    DfSizeContRelationService dfSizeContRelationService;
    @ApiOperation("根据项目工序获取测试项")
    @GetMapping(value = "/listBySearch")
    public Object listBySearch(String project,String process) {
        QueryWrapper<DfSizeContRelation>qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(project),"project",project);
        qw.eq(StringUtils.isNotEmpty(process),"process",process);
        return new Result(0, "查询成功", dfSizeContRelationService.list(qw));
    }
}
