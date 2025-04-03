package com.ww.boengongye.controller;


import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-01-30
 */
@Controller
@RequestMapping("/dfMacPositionCode")
@ResponseBody
@CrossOrigin
public class DfMacPositionCodeController {

    @Autowired
    com.ww.boengongye.service.DfMacPositionCodeService DfMacPositionCodeService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfMacPositionCodeService.list());
    }
}
