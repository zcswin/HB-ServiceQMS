package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTePlateauRoughnessService;
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
 * 平台粗糙度 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-10
 */
@Controller
@RequestMapping("/dfTePlateauRoughness")
@Api(tags = "测试-平台粗糙度")
@ResponseBody
@CrossOrigin
public class DfTePlateauRoughnessController {

    @Autowired
    private DfTePlateauRoughnessService dfTePlateauRoughnessService;

    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        dfTePlateauRoughnessService.exportModel(response, "测试-平台粗糙度模板");
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTePlateauRoughnessService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }
}
