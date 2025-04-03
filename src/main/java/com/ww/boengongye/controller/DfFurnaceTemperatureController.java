package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.DfFurnaceTemperature;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.service.DfFurnaceTemperatureService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 炉温表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-10-16
 */
@Controller
@RequestMapping("/dfFurnaceTemperature")
@ResponseBody
@CrossOrigin
@Api(tags = "炉温表")
public class DfFurnaceTemperatureController {

    @Autowired
    private DfFurnaceTemperatureService dfFurnaceTemperatureService;


    @PostMapping("/importData")
    @ApiOperation("导入数据")
    @Transactional(rollbackFor = Exception.class)
    public Result importData(MultipartFile file) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] strings = excel.readExcelBlock(1, -1, 1, 11);

        List<DfFurnaceTemperature> list = new ArrayList<>();
        for (int i = 2; i < strings.length; i++) {
            //检测时间
            Date parse = sd.parse(strings[i][1]);
            Timestamp checkTime = new Timestamp(parse.getTime());
            //线体
            String lineBody = "J6-6研发线-3#线";

            for (int j = 2; j <= 10; j++) {
                //隧道炉名称
                String name = strings[1][j];
                //检测值
                Double checkValue = Double.valueOf(strings[i][j]);
                if (checkValue==null||checkValue==0){
                    continue;
                }

                DfFurnaceTemperature dfFurnaceTemperature = new DfFurnaceTemperature();
                dfFurnaceTemperature.setLineBody(lineBody);
                dfFurnaceTemperature.setName(name);
                dfFurnaceTemperature.setCheckValue(checkValue);
                dfFurnaceTemperature.setCheckTime(checkTime);
                list.add(dfFurnaceTemperature);
            }
        }
        dfFurnaceTemperatureService.saveBatch(list);
        return new Result(200,"成功导入"+list.size()+"条数据");
    }


    @GetMapping("getRealTimeList")
    @ApiOperation("炉温实时曲线看板")
    public Result getRealTimeList(
            String lineBody,String name,
            @RequestParam String startDate, @RequestParam String endDate
    ){
        Map<String,Object> map = new HashMap<>();

        //时间（X轴）
        LinkedHashSet<Object> timeList = new LinkedHashSet<>();

        //检测值（Y轴）
        List<Object> checkValueList = new ArrayList<>();


        QueryWrapper<DfFurnaceTemperature> timeWrapper = new QueryWrapper<>();
        timeWrapper.apply("dft.check_time between '"+startDate+"' and '"+endDate+"'");

        QueryWrapper<DfFurnaceTemperature> furnaceNameWrapper = new QueryWrapper<>();
        furnaceNameWrapper
                .eq(StringUtils.isNotEmpty(lineBody),"dft.line_body",lineBody)
                .eq(StringUtils.isNotEmpty(name),"dft.name",name)
                .between("dft.check_time",startDate,endDate);

        //所有隧道炉名称
        List<String> furnaceNameList = dfFurnaceTemperatureService.getFurnaceNameList(furnaceNameWrapper);
        if (furnaceNameList==null||furnaceNameList.size()==0){
            return new Result(200,"该条件下没有炉温实时曲线看板相关数据");
        }

        for (String furnaceName:furnaceNameList){
            List<Object> oneFurnaceCheckValueList = new ArrayList<>();

            QueryWrapper<DfFurnaceTemperature> oneFurnaceWrapper = new QueryWrapper<>();
            oneFurnaceWrapper
                    .eq(StringUtils.isNotEmpty(lineBody),"dft.line_body",lineBody)
                    .eq("dft.name",furnaceName)
                    .between("dft.check_time",startDate,endDate);
            //单个隧道炉所有时间段的检查值
            List<Rate3> oneFurnaceList = dfFurnaceTemperatureService.getOneFurnaceCheckValueList(oneFurnaceWrapper,timeWrapper);
            for (Rate3 rate3:oneFurnaceList){
                timeList.add(rate3.getStr1());
                oneFurnaceCheckValueList.add(rate3.getDou1());
            }

            checkValueList.add(oneFurnaceCheckValueList);
        }

        map.put("timeListX",timeList);
        map.put("checkValueListY",checkValueList);
        map.put("furnaceNameList",furnaceNameList);
        return new Result(200,"获取炉温实时曲线看板数据成功",map);
    }

}
