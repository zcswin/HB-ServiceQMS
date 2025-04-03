package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
@Controller
@RequestMapping("/dfGroup")
@ResponseBody
@Api(tags = "小组")
@CrossOrigin
public class DfGroupController {
    @Autowired
    private DfGroupService dfGroupService;

    @GetMapping("/test")
    public Result getMacResGroupId(String month, String dayOrNight) {
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);
        return new Result(200, "查询成功", macResGroupId);
    }
}
