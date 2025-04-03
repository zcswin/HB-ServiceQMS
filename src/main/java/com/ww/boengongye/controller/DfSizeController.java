package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfSizeService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 尺寸数据表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-09
 */
@Controller
@RequestMapping("/dfSize")
@ResponseBody
@CrossOrigin
public class DfSizeController {

    @Autowired
    DfSizeService DfSizeService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfSizeService.list());
    }
}
