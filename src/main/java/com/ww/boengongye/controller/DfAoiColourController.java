package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfAoiColour;
import com.ww.boengongye.service.DfAoiColourService;
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
 * AOI颜色表（临时） 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-24
 */
@Controller
@RequestMapping("/dfAoiColour")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI颜色")
public class DfAoiColourController {

    @Autowired
    DfAoiColourService dfAoiColourService;


    @GetMapping("getAllAoiColourList")
    @ApiOperation("颜色下拉列表")
    public Result getAllAoiColourList(){
        List<DfAoiColour> list = dfAoiColourService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取颜色下拉列表失败");
        }
        return new Result(200,"获取颜色下拉列表成功",list);
    }

}
