package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.DfSizeNgData;
import com.ww.boengongye.service.DfSizeNgDataService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-22
 */
@Controller
@RequestMapping("/dfSizeNgData")
@ResponseBody
@CrossOrigin
public class DfSizeNgDataController {

    @Autowired
    DfSizeNgDataService DfSizeNgDataService;
    @RequestMapping(value = "/listByMachineCode")
    public Result listByMachineCode(String machineCode) {
        QueryWrapper<DfSizeNgData> ew=new QueryWrapper<DfSizeNgData>();
        ew.inSql("check_id", "select t.id from(select id from df_size_detail where result='NG' and machine_code='"+machineCode+"' order by test_time desc limit 0,1) as t");


        //        ew.orderByDesc("check_time");
        return new Result(0, "查询成功",DfSizeNgDataService.list(ew));

    }

    @RequestMapping(value = "/listByCheckId")
    public Result listByCheckId( String checkId) {
        QueryWrapper<DfSizeNgData> ew=new QueryWrapper<DfSizeNgData>();
        ew.eq("check_id", checkId);


        //        ew.orderByDesc("check_time");
        return new Result(0, "查询成功",DfSizeNgDataService.list(ew));

    }
}
