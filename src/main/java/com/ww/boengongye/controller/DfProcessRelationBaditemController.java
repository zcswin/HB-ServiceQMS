package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfBadItem;
import com.ww.boengongye.entity.DfProcessRelationBaditem;
import com.ww.boengongye.service.DfProcessRelationBaditemService;
import com.ww.boengongye.utils.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序_不良项配置 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
@Controller
@RequestMapping("/dfProcessRelationBaditem")
@ResponseBody
@CrossOrigin
public class DfProcessRelationBaditemController {
    @Autowired
    private DfProcessRelationBaditemService dfProcessRelationBaditemService;

    @GetMapping("/listByProcess")
    public Result listByProcess(Integer processId) {
        Map<String, List<DfBadItem>> badItems = new HashMap<>();
        // 查询选中的不良项
        List<DfBadItem> selected = dfProcessRelationBaditemService.listSelectedBadItem(processId);
        // 查询没有选中的不良项
        List<DfBadItem> unselected = dfProcessRelationBaditemService.listUnselectedBadItem(processId);
        badItems.put("selected", selected);
        badItems.put("unselected", unselected);

        return new Result(200,"查询成功", badItems);
    }

    @Transactional
    @PostMapping("/save")
    public Result save(Integer processId, Integer[] badItemIds) {
        LambdaQueryWrapper<DfProcessRelationBaditem> qw = new LambdaQueryWrapper<>();
        qw.eq(DfProcessRelationBaditem::getProcessId, processId);
        dfProcessRelationBaditemService.remove(qw);

        List<DfProcessRelationBaditem> saveList = new ArrayList<>();
        for (Integer badItemId : badItemIds) {
            DfProcessRelationBaditem dfProcessRelationBaditem = new DfProcessRelationBaditem();
            dfProcessRelationBaditem.setProcessId(processId);
            dfProcessRelationBaditem.setBadItemId(badItemId);
            saveList.add(dfProcessRelationBaditem);
        }
        dfProcessRelationBaditemService.saveBatch(saveList);
        return new Result(200, "保存成功");
    }

}


