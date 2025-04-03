package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfAoiProcess;
import com.ww.boengongye.service.DfAoiProcessService;
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
 * AOI工序（临时） 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-24
 */
@Controller
@RequestMapping("/dfAoiProcess")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI工序")
public class DfAoiProcessController {

    @Autowired
    DfAoiProcessService dfAoiProcessService;

    @GetMapping("getAllAoiProcessList")
    @ApiOperation("工序下拉列表")
    public Result getAllAoiProcessList(){
        List<DfAoiProcess> list = dfAoiProcessService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取工序下拉列表失败");
        }
        return new Result(200,"获取工序下拉列表成功",list);
    }
}
