package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.service.DfTeC6Cuc3Ums6Service;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * C6_CNC3_UMS6检测数据 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-06
 */
@Controller
@RequestMapping("/dfTeC6Cuc3Ums6")
@ResponseBody
@CrossOrigin
@Api(tags = "C6_CNC3_UMS6测量数据")
public class DfTeC6Cuc3Ums6Controller {
    @Autowired
    private DfTeC6Cuc3Ums6Service dfTeC6Cuc3Ums6Service;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        int count = dfTeC6Cuc3Ums6Service.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    /*@GetMapping("/numOfToday")
    public Result numOfToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwng = new LambdaQueryWrapper<>();
        qwng.like(DfTeC6Cuc3Ums6::getTestTime, today);
        qwng.eq(DfTeC6Cuc3Ums6::getResult, "NG");
        int numOfNG = dfTeC6Cuc3Ums6Service.count(qwng);
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwok = new LambdaQueryWrapper<>();
        qwok.like(DfTeC6Cuc3Ums6::getTestTime, today);
        qwok.eq(DfTeC6Cuc3Ums6::getResult, "OK");
        int numOfOK = dfTeC6Cuc3Ums6Service.count(qwok);

        Map<String, Integer> result = new HashMap<>();
        result.put("numOfNG", numOfNG);
        result.put("numOfOK", numOfOK);
        return new Result(200, "查询成功", result);
    }*/

    /*@GetMapping("/numOfThisMonth")
    public Result numOfThisMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String thisMonth = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwng = new LambdaQueryWrapper<>();
        qwng.like(DfTeC6Cuc3Ums6::getTestTime, thisMonth);
        qwng.eq(DfTeC6Cuc3Ums6::getResult, "NG");
        int numOfNG = dfTeC6Cuc3Ums6Service.count(qwng);
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwok = new LambdaQueryWrapper<>();
        qwok.like(DfTeC6Cuc3Ums6::getTestTime, thisMonth);
        qwok.eq(DfTeC6Cuc3Ums6::getResult, "OK");
        int numOfOK = dfTeC6Cuc3Ums6Service.count(qwok);

        Map<String, Integer> result = new HashMap<>();
        result.put("numOfNG", numOfNG);
        result.put("numOfOK", numOfOK);
        return new Result(200, "查询成功", result);
    }*/

    @GetMapping("/dayTop10NGOfMachine")
    public Result dayTop10NGOfMachine() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qw = new LambdaQueryWrapper<>();
        qw.like(DfTeC6Cuc3Ums6::getTestTime, today)
                .eq(DfTeC6Cuc3Ums6::getResult, "NG");

        List<DfTeC6Cuc3Ums6> list = dfTeC6Cuc3Ums6Service.listTop10NG(qw);
        Map<String, Integer> result = new HashMap<>();
        for (DfTeC6Cuc3Ums6 dfTeC6Cuc3Ums6 : list) {
            result.put(dfTeC6Cuc3Ums6.getMachineCode(), dfTeC6Cuc3Ums6.getIndexing());
        }
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/thisMonthTop10NGOfMachine")
    public Result thisMonthTop10NGOfMachine() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String thisMonth = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qw = new LambdaQueryWrapper<>();
        qw.like(DfTeC6Cuc3Ums6::getTestTime, thisMonth)
                .eq(DfTeC6Cuc3Ums6::getResult, "NG");

        List<DfTeC6Cuc3Ums6> list = dfTeC6Cuc3Ums6Service.listTop10NG(qw);
        Map<String, Integer> result = new HashMap<>();
        for (DfTeC6Cuc3Ums6 dfTeC6Cuc3Ums6 : list) {
            result.put(dfTeC6Cuc3Ums6.getMachineCode(), dfTeC6Cuc3Ums6.getIndexing());
        }
        return new Result(200, "查询成功", result);
    }



    @GetMapping("/numOfToday")
    public Result numOfToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwng = new LambdaQueryWrapper<>();
        qwng.like(DfTeC6Cuc3Ums6::getTestTime, today);
        qwng.eq(DfTeC6Cuc3Ums6::getResult, "NG");
        int numOfNG = dfTeC6Cuc3Ums6Service.count(qwng);
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwok = new LambdaQueryWrapper<>();
        qwok.like(DfTeC6Cuc3Ums6::getTestTime, today);
        qwok.eq(DfTeC6Cuc3Ums6::getResult, "OK");
        int numOfOK = dfTeC6Cuc3Ums6Service.count(qwok);

        Map<String, Integer> result = new HashMap<>();
        result.put("numOfNG", numOfNG);
        result.put("numOfOK", numOfOK);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/numOfThisMonth")
    public Result numOfThisMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String thisMonth = format.format(new Date());
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwng = new LambdaQueryWrapper<>();
        qwng.like(DfTeC6Cuc3Ums6::getTestTime, thisMonth);
        qwng.eq(DfTeC6Cuc3Ums6::getResult, "NG");
        int numOfNG = dfTeC6Cuc3Ums6Service.count(qwng);
        LambdaQueryWrapper<DfTeC6Cuc3Ums6> qwok = new LambdaQueryWrapper<>();
        qwok.like(DfTeC6Cuc3Ums6::getTestTime, thisMonth);
        qwok.eq(DfTeC6Cuc3Ums6::getResult, "OK");
        int numOfOK = dfTeC6Cuc3Ums6Service.count(qwok);

        Map<String, Integer> result = new HashMap<>();
        result.put("numOfNG", numOfNG);
        result.put("numOfOK", numOfOK);
        return new Result(200, "查询成功", result);
    }



    @RequestMapping(value = "/getNgCount")
    public Result getNgCount() {
//        try {
        QueryWrapper<DfTeC6Cuc3Ums6> ew=new QueryWrapper<DfTeC6Cuc3Ums6>();
        ew.eq("result", "NG");

        return new Result(0, "查询成功",dfTeC6Cuc3Ums6Service.count(ew));

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String machineCode,String result,String testTime) {
//        try {
        Page<DfTeC6Cuc3Ums6> pages = new Page<DfTeC6Cuc3Ums6>(page, limit);
        QueryWrapper<DfTeC6Cuc3Ums6> ew=new QueryWrapper<DfTeC6Cuc3Ums6>();
        if(null!=machineCode&&!machineCode.equals("")) {
            ew.like("machine_code", machineCode);
        }
        if(null!=result&&!result.equals("")) {
            ew.eq("result", result);
        }
        if(null!=testTime&&!testTime.equals("")) {
            ew.like("test_time", testTime);
        }
        ew.orderByAsc("result");
        ew.orderByDesc("create_time");
        IPage<DfTeC6Cuc3Ums6> list=dfTeC6Cuc3Ums6Service.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

}
