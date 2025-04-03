package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfProblemCa;
import com.ww.boengongye.entity.DfProblemCa;
import com.ww.boengongye.service.DfProblemCaService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/dfProblemCa")
@CrossOrigin
@ResponseBody
@Api(tags = "问题点CA")
public class DfProblemCaController {

    @Autowired
    private DfProblemCaService dfProblemCaService;

    @ApiOperation("获取工序的CA")
    @GetMapping("/getCaByProcess")
    public Result getCaByItem(String projectType, String process) {
        QueryWrapper<DfProblemCa> qw = new QueryWrapper<>();
        qw.eq(null != projectType && !"".equals(projectType), "project_type", projectType)
                .like(null != process && !"".equals(process), "type", process);
        List<DfProblemCa> list = dfProblemCaService.list(qw);
        return new Result(200, "查询成功", list);
    }

}
