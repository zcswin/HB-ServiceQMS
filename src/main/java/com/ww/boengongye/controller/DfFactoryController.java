package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.entity.DfMachineController;
import com.ww.boengongye.entity.DfWorkshop;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
@Controller
@RequestMapping("/dfFactory")
@ResponseBody
@CrossOrigin
@Api(tags = "工厂")
public class DfFactoryController {
    @Autowired
    com.ww.boengongye.service.DfFactoryService DfFactoryService;

    private static final Logger logger = LoggerFactory.getLogger(DfFactoryController.class);

    @GetMapping("getAllFactoryList")
    @ApiOperation("获取所有厂别下拉列表")
    public Result getAllFactoryList(){
        List<DfFactory> list = DfFactoryService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取厂别下拉列表失败");
        }
        return new Result(200,"获取厂别下拉列表成功",list);
    }


    @GetMapping("/listAll")
    public Result listAll(String name) {
        LambdaQueryWrapper<DfFactory> qw = new LambdaQueryWrapper<>();
        if (null != name && !"".equals(name)) qw.like(DfFactory::getFactoryName, name)
                .or().like(DfFactory::getFactoryCode, name);
        return new Result(0, "查询成功",DfFactoryService.list(qw));
    }


    @PostMapping("/insert")
    public Result insert(@RequestBody(required = false) DfFactory factory) {
        DfFactoryService.save(factory);
        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        DfFactoryService.removeById(id);
        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody(required = false) DfFactory factory) {
        DfFactoryService.updateById(factory);
        return Result.UPDATE_SUCCESS;
    }

}
