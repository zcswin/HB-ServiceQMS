package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfTeRor;
import com.ww.boengongye.service.DfTeRorService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * ror测试 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-14
 */
@Controller
@RequestMapping("/dfTeRor")
@CrossOrigin
@ResponseBody
@Api(tags = "测试-ROR强度应力")
public class DfTeRorController {
    @Autowired
    private DfTeRorService dfTeRorService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeRorService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

}
