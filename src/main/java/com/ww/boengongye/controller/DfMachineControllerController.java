package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfMachine;
import com.ww.boengongye.entity.DfMachineController;
import com.ww.boengongye.service.DfMachineControllerService;
import com.ww.boengongye.service.DfMachineService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 设备控制器 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-08
 */
@Controller
@RequestMapping("/dfMachineController")
@CrossOrigin
@ResponseBody
@Api(tags = "设备控制器")
public class DfMachineControllerController {

    @Autowired
    private DfMachineControllerService dfMachineControllerService;

    @GetMapping("/listAll")
    public Result listAll(String name) {
        LambdaQueryWrapper<DfMachineController> qw = new LambdaQueryWrapper<>();
        if (null != name && !"".equals(name)) qw.like(DfMachineController::getName, name)
                .or().like(DfMachineController::getCode, name);
        return new Result(200, "查询成功", dfMachineControllerService.list(qw));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfMachineController machineController) {
        dfMachineControllerService.save(machineController);
        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        dfMachineControllerService.removeById(id);
        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfMachineController machineController) {
        dfMachineControllerService.updateById(machineController);
        return Result.UPDATE_SUCCESS;
    }
}
