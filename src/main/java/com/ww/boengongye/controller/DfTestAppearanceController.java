package com.ww.boengongye.controller;


import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 外观类型 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Controller
@RequestMapping("/dfTestAppearance")
@ResponseBody
@CrossOrigin
public class DfTestAppearanceController {
    private static final Logger logger = LoggerFactory.getLogger(DfTestAppearanceController.class);

    @Autowired
    com.ww.boengongye.service.DfTestAppearanceService DfTestAppearanceService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfTestAppearanceService.list());
    }
}
