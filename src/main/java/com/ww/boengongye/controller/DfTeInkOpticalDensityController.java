package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTeInkOpticalDensityService;
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
 * 油墨密度 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Controller
@RequestMapping("/dfTeInkOpticalDensity")
@CrossOrigin
@ResponseBody
@Api(tags = "测试-油墨密度")
public class DfTeInkOpticalDensityController {

    @Autowired
    private DfTeInkOpticalDensityService dfTeInkOpticalDensityService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeInkOpticalDensityService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

}
