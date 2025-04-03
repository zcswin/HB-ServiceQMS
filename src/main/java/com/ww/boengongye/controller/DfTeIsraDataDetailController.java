package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfTeIsraDataDetailService;
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
 * 17.4-ISRA Data 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Controller
@RequestMapping("/dfTeIsraDataDetail")
@CrossOrigin
@ResponseBody
@Api(tags = "测试-ISRA数据")
public class DfTeIsraDataDetailController {
    @Autowired
    private DfTeIsraDataDetailService dfTeIsraDataDetailService;


    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeIsraDataDetailService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

}
