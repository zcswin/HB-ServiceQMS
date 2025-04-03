package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTeThermalCyclingService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;

/**
 * <p>
 * 测试_冷热循环+百格 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-27
 */
@Controller
@RequestMapping("/dfTeThermalCycling")
@ResponseBody
@CrossOrigin
@Api(tags = "测试-冷热循环+百格")
public class DfTeThermalCyclingController {
    @Autowired
    private DfTeThermalCyclingService dfTeThermalCyclingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeThermalCyclingService.importExcel(file);

        String url = new ClassPathResource("/static/bat/timmerTask.bat").getURL().getPath();
        String decode = URLDecoder.decode(url, "UTF-8");
        Runtime.getRuntime().exec("cmd /c " + decode.substring(1));
        System.out.println("JMP更新数据完成");

        return new Result(200, "成功添加" + count + "条数据");
    }

}
