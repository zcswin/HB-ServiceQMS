package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAgvTestService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/testAGV")
@ResponseBody
@CrossOrigin
public class TestAGVController {
    @Autowired
    DfAgvTestService dfAgvTestService;

    @PostMapping (value = "/ics/out/device/list/deviceInfo")
    public Result deviceInfo(AGVTest data){
        System.out.println(data.toString());
        List<AgvResultDetail> datas=new ArrayList<>();

        List<DfAgvTest>ddd=dfAgvTestService.list();
        for(DfAgvTest dd:ddd){
            AgvResultDetail d=new AgvResultDetail();
            d.setDeviceName(dd.getDeviceCode());
            List<Double> xy=new ArrayList<>();
            xy.add(dd.getX());
            xy.add(dd.getY());
            d.setDevicePostionRec(xy);
            datas.add(d);
        }

        return new Result(0,"查询成功",datas);
    }


    @PostMapping (value = "/ics/out/controlDevice")
    public AgvResult controlDevice(AGVTest data){

        System.out.println("控制AGV");
        System.out.println(data.toString());
        return new AgvResult(1000,"查询成功");
    }
}
