package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.DfAoiUnitDefect;
import com.ww.boengongye.service.DfAoiUnitDefectService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 单位缺陷机会数 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-26
 */
@Controller
@RequestMapping("/dfAoiUnitDefect")
@ResponseBody
@CrossOrigin
@Api(tags = "单位缺陷机会数")
public class DfAoiUnitDefectController {

    @Autowired
    private DfAoiUnitDefectService dfAoiUnitDefectService;


}
