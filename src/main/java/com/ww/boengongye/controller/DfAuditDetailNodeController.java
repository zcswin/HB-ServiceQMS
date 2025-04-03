package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAuditDetailNode;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @since 2025-02-18
 */
@Controller
@RequestMapping("/dfAuditDetailNode")
@ResponseBody
@CrossOrigin
@Api(tags = "任务单节点")
public class DfAuditDetailNodeController {
    @Autowired
    com.ww.boengongye.service.DfAuditDetailNodeService dfAuditDetailNodeService;

    private static final Logger logger = LoggerFactory.getLogger(DfAuditDetailNodeController.class);

    @GetMapping("listByAuditId")
    @ApiOperation("根据任务单ID获取流程节点")
    public Result listByAuditId(int id){
        QueryWrapper<DfAuditDetailNode>qw=new QueryWrapper<>();
        qw.eq("parent_id",id);
        qw.orderByDesc("create_time");
        return new Result(200,"查询成功",dfAuditDetailNodeService.list(qw));
    }

}
