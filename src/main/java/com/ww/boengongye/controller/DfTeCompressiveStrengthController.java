package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTeCompressiveStrengthService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-16
 */
@Controller
@RequestMapping("/dfTeCompressiveStrength")
@CrossOrigin
@ResponseBody
@Api(tags = "测试-强度应力")
public class DfTeCompressiveStrengthController {

    @Autowired
    private DfTeCompressiveStrengthService dfTeCompressiveStrengthService;


    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        dfTeCompressiveStrengthService.exportModel(response, "测试-强度应力模板");
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeCompressiveStrengthService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }


}
