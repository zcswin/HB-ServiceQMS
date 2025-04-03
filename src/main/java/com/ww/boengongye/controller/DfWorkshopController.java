package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfArea;
import com.ww.boengongye.entity.DfMachineController;
import com.ww.boengongye.entity.DfWorkshop;
import com.ww.boengongye.service.DfMachineControllerService;
import com.ww.boengongye.service.DfWorkshopService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 车间 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-08
 */
@Controller
@RequestMapping("/dfWorkshop")
@CrossOrigin
@ResponseBody
@Api(tags = "车间")
public class DfWorkshopController {

    @Autowired
    private DfWorkshopService dfWorkshopService;

    @GetMapping("/listAll")
    public Result listAll(String name) {
        LambdaQueryWrapper<DfWorkshop> qw = new LambdaQueryWrapper<>();
        if (null != name && !"".equals(name)) qw.like(DfWorkshop::getName, name)
                .or().like(DfWorkshop::getCode, name);
        return new Result(200, "查询成功", dfWorkshopService.list(qw));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfWorkshop workshop) {
        dfWorkshopService.save(workshop);
        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        dfWorkshopService.removeById(id);
        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfWorkshop workshop) {
        dfWorkshopService.updateById(workshop);
        return Result.UPDATE_SUCCESS;
    }
}
