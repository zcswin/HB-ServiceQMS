package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfKpiCount;
import com.ww.boengongye.service.DfKpiCountService;
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
 * KPI计算 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-01
 */
@Controller
@RequestMapping("/dfKpiCount")
@Api(tags = "KPI计算")
@CrossOrigin
@ResponseBody
public class DfKpiCountController {
    @Autowired
    private DfKpiCountService dfKpiCountService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfKpiCountService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

}
