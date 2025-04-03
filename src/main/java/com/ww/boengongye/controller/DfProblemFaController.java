package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfProblemFa;
import com.ww.boengongye.service.DfProblemFaService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-06-06
 */
@Controller
@RequestMapping("/dfProblemFa")
@CrossOrigin
@ResponseBody
@Api(tags = "问题点FA")
public class DfProblemFaController {
    @Autowired
    private DfProblemFaService dfProblemFaService;

    @PostMapping("/upload")
    public Result update(MultipartFile file) throws Exception {
        dfProblemFaService.importExcel(file);
        return new Result(200, "插入成功");
    }

    @ApiOperation("获取工序的FA")
    @GetMapping("/getFaByProcess")
    public Result getFaByItem(String projectType, String process) {
        QueryWrapper<DfProblemFa> qw = new QueryWrapper<>();
        qw.eq(null != projectType && !"".equals(projectType), "project_type", projectType)
                .like(null != process && !"".equals(process), "type", process);
        List<DfProblemFa> list = dfProblemFaService.list(qw);
        return new Result(200, "查询成功", list);
    }
}
