package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTePlateauGlossService;
import com.ww.boengongye.service.ExperimentService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-04-28
 */
@Controller
@RequestMapping("/experiment")
@CrossOrigin
@Api(tags = "实验数据导入")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;


    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        experimentService.exportModel(response, "null");
    }

    @ApiOperation("导入数据")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        System.out.println("来了喔！！！！！！！！");
        int count = experimentService.importExcel(file);
        System.out.println("来了吗？？？？？？！！！！！！！！");
        System.out.println(count);

        return null;
    }
}
