package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFlowBlock;
import com.ww.boengongye.entity.DfFlowNextLevel;
import com.ww.boengongye.entity.DfFlowOpinion;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 流程的下一级配置 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Controller
@RequestMapping("/dfFlowNextLevel")
@ResponseBody
@CrossOrigin
public class DfFlowNextLevelController {
    @Autowired
    com.ww.boengongye.service.DfFlowNextLevelService DfFlowNextLevelService;
    @Autowired
    com.ww.boengongye.service.DfFlowBlockService DfFlowBlockService;
    private static final Logger logger = LoggerFactory.getLogger(DfFlowNextLevelController.class);

    @RequestMapping(value = "/listNextLevel")
    public Object listNextLevel(int sort,String flowType) {
        System.out.println(sort);
        System.out.println(flowType);
        QueryWrapper<DfFlowBlock> qw1 = new QueryWrapper<>();
        qw1.eq("sort",sort);
        qw1.eq("flow_type",flowType);
        qw1.last("limit 0,1");

        DfFlowBlock b=DfFlowBlockService.getOne(qw1);
        System.out.println(b.toString());
        QueryWrapper<DfFlowNextLevel> qw = new QueryWrapper<>();
        qw.eq("l.parent_id", b.getId());
        qw.eq("l.flow_type", flowType);
        return new Result(200, "查询成功", DfFlowNextLevelService.listNextLevel(qw));
    }

}
