package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfArea;
import com.ww.boengongye.service.DfAreaService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 区域 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-08
 */
@Controller
@RequestMapping("/dfArea")
@CrossOrigin
@ResponseBody
@Api(tags = "区域")
public class DfAreaController {

    @Autowired
    private DfAreaService dfAreaService;

    @GetMapping("/listAll")
    public Result listAll() {
        return new Result(200, "查询成功", dfAreaService.list());
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfArea area) {
        dfAreaService.save(area);
        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        dfAreaService.removeById(id);
        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfArea area) {
        dfAreaService.updateById(area);
        return Result.UPDATE_SUCCESS;
    }
}
