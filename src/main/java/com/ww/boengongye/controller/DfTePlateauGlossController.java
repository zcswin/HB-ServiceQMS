package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTePlateauGlossService;
import com.ww.boengongye.utils.ExportExcelUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 平台光泽度 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-14
 */
@Controller
@RequestMapping("/dfTePlateauGloss")
@ResponseBody
@CrossOrigin
@Api(tags = "测试-平台光泽度")
public class DfTePlateauGlossController {

    @Autowired
    private DfTePlateauGlossService dfTePlateauGlossService;


    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        dfTePlateauGlossService.exportModel(response, "测试-平台光泽度模板");
    }

    @ApiOperation("导入数据")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTePlateauGlossService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }


}
