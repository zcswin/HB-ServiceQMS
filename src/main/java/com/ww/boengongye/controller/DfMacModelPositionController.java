package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.DfMacModelPosition;
import com.ww.boengongye.service.DfMacModelPositionService;
import com.ww.boengongye.utils.Result;
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
 * @since 2022-08-08
 */
@Controller
@RequestMapping("/dfMacModelPosition")
@ResponseBody
@CrossOrigin
public class DfMacModelPositionController {

    @Autowired
    DfMacModelPositionService DfMacModelPositionService;

    @RequestMapping(value = "/savePosition", method = RequestMethod.POST)
    public Result save(@RequestBody List<DfMacModelPosition> datas) {

        for(DfMacModelPosition d:datas){
            UpdateWrapper<DfMacModelPosition>uw=new UpdateWrapper<>();
            uw.set("position_x",d.getPositionX());
//            uw.set("position_y",d.getPositionY());
            uw.set("position_y",0.0);
            uw.set("position_z",d.getPositionZ());
            uw.eq("MachineCode",d.getMachineCode());
            DfMacModelPositionService.update(uw);
        }


        return new Result(0, "保存成功");

    }
}
