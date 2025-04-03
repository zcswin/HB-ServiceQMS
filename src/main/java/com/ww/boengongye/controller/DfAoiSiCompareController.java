package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.service.DfAoiSiCompareService;
import com.ww.boengongye.service.DfAoiSiDefectService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI SI工厂对比 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
@Controller
@RequestMapping("/dfAoiSiCompare")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI SI工厂对比")
public class DfAoiSiCompareController {

    @Autowired
    private DfAoiSiCompareService dfAoiSiCompareService;

    @Autowired
    private DfAoiSiDefectService dfAoiSiDefectService;

    /**
     * 关键字查询
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("AOI SI工厂对比数据")
    public Result listBySearch(int page, int limit, String keywords){
        Page<DfAoiSiCompare> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiSiCompare> ew = new QueryWrapper<>();
        if (keywords != null && !"".equals(keywords)) {
            ew.and(wrapper -> wrapper
                    .like("series", keywords)
                    .or().like("factory", keywords)
                    .or().like("si_customer", keywords)
                    .or().like("sell_place", keywords)
                    .or().like("`type`", keywords)
                    .or().like("batch", keywords)
                    .or().like("colour", keywords)
            );
        }
        IPage<DfAoiSiCompare> list = dfAoiSiCompareService.page(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }

    @RequestMapping(value = "getBatchDefectDetail",method = RequestMethod.GET)
    @ApiOperation("通过批次号获取该批次的所有缺陷集合")
    public Result getBatchDefectDetail(String batch){
        QueryWrapper<DfAoiSiDefect> siDefectWrapper = new QueryWrapper<>();
        siDefectWrapper.eq("batch",batch);
        List<DfAoiSiDefect> siDefectList = dfAoiSiDefectService.list(siDefectWrapper);
        if (siDefectList==null||siDefectList.size()==0){
            return new Result(500,"该批次没有缺陷数据");
        }
        return new Result(200,"获取该批次的缺陷集合成功",siDefectList);
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改AOI SI工厂对比数据")
    public Result saveOrUpdate(@RequestBody DfAoiSiCompare datas){
        if (datas.getId()!=null){
            if (dfAoiSiCompareService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfAoiSiCompareService.save(datas)){
                return new Result(200,"添加成功");
            }
            return new Result(500,"添加失败");
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ApiOperation("删除AOI SI工厂对比数据")
    public Result delete(String id){
        if (dfAoiSiCompareService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("导入")
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "file", required = false)MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取SI工厂对比信息失败,导入文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
            return new Result(500, "请上传xlsx或xls格式的文件");
        }
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        //导入的SI工厂比较数据相关集合
        List<DfAoiSiCompare> list = new ArrayList<>();

        for (Map<String, String> map : maps) {

            DfAoiSiCompare dfAoiSiCompare = new DfAoiSiCompare();
            dfAoiSiCompare.setSeries(map.get("系列"));
            dfAoiSiCompare.setCheckTime(map.get("日期"));
            dfAoiSiCompare.setCheckYear(map.get("年"));
            dfAoiSiCompare.setCheckMonth(map.get("月"));
            dfAoiSiCompare.setCheckWeek(map.get("周"));
            dfAoiSiCompare.setFactory(map.get("工厂"));
            dfAoiSiCompare.setSiCustomer(map.get("SI客户"));
            dfAoiSiCompare.setSellPlace(map.get("工厂区"));
            dfAoiSiCompare.setType(map.get("型号"));
            dfAoiSiCompare.setBatch(map.get("批次号"));
            dfAoiSiCompare.setBinConfig(map.get("Bin/config"));
            dfAoiSiCompare.setColour(map.get("颜色"));
            dfAoiSiCompare.setBin(map.get("Bin"));
            dfAoiSiCompare.setVender(map.get("Vender"));
            dfAoiSiCompare.setSiNumber(map.get("SI次数"));
            dfAoiSiCompare.setStage(map.get("阶段"));
            dfAoiSiCompare.setBenchmarkLine(map.get("标杆线"));
            dfAoiSiCompare.setAql(map.get("AQL"));
            dfAoiSiCompare.setInputNumber(Integer.valueOf(map.get("投入数")));
            dfAoiSiCompare.setCheckNumber(Integer.valueOf(map.get("抽检数")));
            dfAoiSiCompare.setOkNumber(Integer.valueOf(map.get("OK数")));
            dfAoiSiCompare.setNgNumber(Integer.valueOf(map.get("NG数")));
            dfAoiSiCompare.setPassPoint(map.get("良率"));
            dfAoiSiCompare.setTarget(map.get("Target"));
            dfAoiSiCompare.setDetermine(map.get("判定"));

            boolean startIteration = false;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (startIteration) {
                    DfAoiSiDefect dfAoiSiDefect = new DfAoiSiDefect();
                    dfAoiSiDefect.setBatch(map.get("批次号"));
                    dfAoiSiDefect.setDefectName(entry.getKey());
                    dfAoiSiDefect.setDefectNumber(Integer.valueOf(entry.getValue()));
                    dfAoiSiDefectService.save(dfAoiSiDefect);
                }

                if (entry.getKey().equals("判定")) {
                    startIteration = true;
                }
            }

            if (!dfAoiSiCompareService.save(dfAoiSiCompare)){
                return new Result(500,"批次号为"+dfAoiSiCompare.getBatch()+"的SI工厂对比数据导入失败");
            }
        }
        return new Result(200, "SI工厂比较数据导入成功");
    }


    @RequestMapping(value = "getSiPassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI工厂对比良率")
    public Result getSiPassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //良率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> factoryNameWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            factoryNameWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            factoryNameWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            factoryNameWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            factoryNameWrapper.eq("dasc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiSiCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("month(dasc.check_time)>="+startTime)
                        .apply("month(dasc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> siFactoryNameMonthList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameMonthList==null||siFactoryNameMonthList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比良率相关数据");
                }

                for (String siFactoryName:siFactoryNameMonthList){
                    //当前工厂一段时间（月）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("month(dasc.check_time)>="+startTime)
                            .apply("month(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（月）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointMonth(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（月）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(dasc.check_time)>="+startTime)
                        .apply("week(dasc.check_time)<="+endTime);
                //一段时间（周）内的工厂名
                List<String> siFactoryNameWeekList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameWeekList==null||siFactoryNameWeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比良率相关数据");
                }

                for (String siFactoryName:siFactoryNameWeekList){
                    //当前工厂一段时间（周）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("week(dasc.check_time)>="+startTime)
                            .apply("week(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（周）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（周）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiSiCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("day(dasc.check_time)>="+startTime)
                        .apply("day(dasc.check_time)<="+endTime);
                //一段时间（天）内的工厂名
                List<String> siFactoryNameDayList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameDayList==null||siFactoryNameDayList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比良率相关数据");
                }

                for (String siFactoryName:siFactoryNameDayList){
                    //当前工厂一段时间（天）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("day(dasc.check_time)>="+startTime)
                            .apply("day(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（天）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointDay(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（天）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(dasc.check_time)>=week(now())-3 ")
                        .apply("week(dasc.check_time)<=week(now())");
                //近4周工厂名
                List<String> siFactoryName4WeekList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryName4WeekList==null||siFactoryName4WeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比良率相关数据");
                }
                for (String siFactoryName:siFactoryName4WeekList){
                    //单个工厂近4周的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("week(dasc.check_time)>=week(now())-3 ")
                            .apply("week(dasc.check_time)<=week(now())");

                    //当前工厂近4周的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂的近4周的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryName4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI SI工厂对比良率数据成功",map);
    }

    @RequestMapping(value = "getSiSellPlacePassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI出货地对比良率")
    public Result getSiSellPlacePassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //良率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> sellPlaceWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            sellPlaceWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            sellPlaceWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            sellPlaceWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            sellPlaceWrapper.eq("dasc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiSiCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("month(dasc.check_time)>="+startTime)
                        .apply("month(dasc.check_time)<="+endTime);
                //一段时间（月）内的出货地
                List<String> siSellPlaceMonthList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceMonthList==null||siSellPlaceMonthList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比良率相关数据");
                }

                for (String siSellPlace:siSellPlaceMonthList){
                    //当前出货地一段时间（月）内的良率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("month(dasc.check_time)>="+startTime)
                            .apply("month(dasc.check_time)<="+endTime);

                    //当前出货地一段时间（月）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointMonth(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地一段时间（月）内的良率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("week(dasc.check_time)>="+startTime)
                        .apply("week(dasc.check_time)<="+endTime);
                //一段时间（周）内的出货地
                List<String> siSellPlaceWeekList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceWeekList==null||siSellPlaceWeekList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比良率相关数据");
                }

                for (String siSellPlace:siSellPlaceWeekList){
                    //当前工厂一段时间（周）内的良率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("week(dasc.check_time)>="+startTime)
                            .apply("week(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（周）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（周）内的良率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiSiCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("day(dasc.check_time)>="+startTime)
                        .apply("day(dasc.check_time)<="+endTime);
                //一段时间（天）内的出货地
                List<String> siSellPlaceDayList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceDayList==null||siSellPlaceDayList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比良率相关数据");
                }

                for (String siSellPlace:siSellPlaceDayList){
                    //当前工厂一段时间（天）内的良率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("day(dasc.check_time)>="+startTime)
                            .apply("day(dasc.check_time)<="+endTime);

                    //当前出货地一段时间（天）内的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointDay(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（天）内的良率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("week(dasc.check_time)>=week(now())-3 ")
                        .apply("week(dasc.check_time)<=week(now())");
                //近4周出货地
                List<String> siSellPlace4WeekList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlace4WeekList==null||siSellPlace4WeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比良率相关数据");
                }
                for (String siSellPlace:siSellPlace4WeekList){
                    //单个出货地近4周的良率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("week(dasc.check_time)>=week(now())-3 ")
                            .apply("week(dasc.check_time)<=week(now())");

                    //当前工厂近4周的良率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地的近4周的良率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlace4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI SI工厂对比良率数据成功",map);
    }


    @RequestMapping(value = "getSiBatchPassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI工厂对比批通率")
    public Result getSiBatchPassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //良率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> factoryNameWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            factoryNameWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            factoryNameWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            factoryNameWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            factoryNameWrapper.eq("dasc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiSiCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("month(dasc.check_time)>="+startTime)
                        .apply("month(dasc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> siFactoryNameMonthList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameMonthList==null||siFactoryNameMonthList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比批通率相关数据");
                }

                for (String siFactoryName:siFactoryNameMonthList){
                    //当前工厂一段时间（月）内的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("month(dasc.check_time)>="+startTime)
                            .apply("month(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（月）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointMonth(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（月）内的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(dasc.check_time)>="+startTime)
                        .apply("week(dasc.check_time)<="+endTime);
                //一段时间（周）内的工厂名
                List<String> siFactoryNameWeekList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameWeekList==null||siFactoryNameWeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比批通率相关数据");
                }

                for (String siFactoryName:siFactoryNameWeekList){
                    //当前工厂一段时间（周）内的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("week(dasc.check_time)>="+startTime)
                            .apply("week(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（周）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（周）内的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiSiCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("day(dasc.check_time)>="+startTime)
                        .apply("day(dasc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> siFactoryNameDayList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryNameDayList==null||siFactoryNameDayList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比批通率相关数据");
                }

                for (String siFactoryName:siFactoryNameDayList){
                    //当前工厂一段时间（天）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("day(dasc.check_time)>="+startTime)
                            .apply("day(dasc.check_time)<="+endTime);

                    //当前工厂一段时间（天）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointDay(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（天）内的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryNameDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(dasc.check_time)>=week(now())-3 ")
                        .apply("week(dasc.check_time)<=week(now())");
                //近4周工厂名
                List<String> siFactoryName4WeekList = dfAoiSiCompareService.getSiFactoryName(factoryNameWrapper);
                if (siFactoryName4WeekList==null||siFactoryName4WeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂对比批通率相关数据");
                }
                for (String siFactoryName:siFactoryName4WeekList){
                    //单个工厂近4周的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.factory",siFactoryName)
                            .apply("week(dasc.check_time)>=week(now())-3 ")
                            .apply("week(dasc.check_time)<=week(now())");

                    //当前工厂近4周的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂的近4周的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("siFactoryNameList",siFactoryName4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI SI工厂对比批通率数据成功",map);
    }

    @RequestMapping(value = "getSiSellPlaceBatchPassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI出货地对比批通率")
    public Result getSiSellPlaceBatchPassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //良率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> sellPlaceWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            sellPlaceWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            sellPlaceWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            sellPlaceWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            sellPlaceWrapper.eq("dasc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiSiCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("month(dasc.check_time)>="+startTime)
                        .apply("month(dasc.check_time)<="+endTime);
                //一段时间（月）内的出货地
                List<String> siSellPlaceMonthList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceMonthList==null||siSellPlaceMonthList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比批通率相关数据");
                }

                for (String siSellPlace:siSellPlaceMonthList){
                    //当前出货地一段时间（月）内的批通率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("month(dasc.check_time)>="+startTime)
                            .apply("month(dasc.check_time)<="+endTime);

                    //当前出货地一段时间（月）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointMonth(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地一段时间（月）内的批通率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("week(dasc.check_time)>="+startTime)
                        .apply("week(dasc.check_time)<="+endTime);
                //一段时间（周）内的出货地
                List<String> siSellPlaceWeekList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceWeekList==null||siSellPlaceWeekList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比批通率相关数据");
                }

                for (String siSellPlace:siSellPlaceWeekList){
                    //当前出货地一段时间（周）内的批通率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("week(dasc.check_time)>="+startTime)
                            .apply("week(dasc.check_time)<="+endTime);

                    //当前出货地一段时间（周）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地一段时间（周）内的批通率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiSiCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("day(dasc.check_time)>="+startTime)
                        .apply("day(dasc.check_time)<="+endTime);
                //一段时间（天）内的出货地
                List<String> siSellPlaceDayList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlaceDayList==null||siSellPlaceDayList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比批通率相关数据");
                }

                for (String siSellPlace:siSellPlaceDayList){
                    //当前出货地一段时间（天）内的批通率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("day(dasc.check_time)>="+startTime)
                            .apply("day(dasc.check_time)<="+endTime);

                    //当前出货地一段时间（天）内的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointDay(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地一段时间（天）内的批通率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlaceDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiSiCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                sellPlaceWrapper
                        .apply("week(dasc.check_time)>=week(now())-3 ")
                        .apply("week(dasc.check_time)<=week(now())");
                //近4周出货地
                List<String> siSellPlace4WeekList = dfAoiSiCompareService.getSiSellPlace(sellPlaceWrapper);
                if (siSellPlace4WeekList==null||siSellPlace4WeekList.size()==0){
                    return new Result(500,"该条件下没有SI出货地对比批通率相关数据");
                }
                for (String siSellPlace:siSellPlace4WeekList){
                    //单个出货地近4周的批通率(y轴)
                    List<String> sellPlacePassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                    }
                    if (StringUtils.isNotEmpty(sellPlace)){
                        siCompareWrapper.eq("dasc.sell_place",sellPlace);
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                    }

                    siCompareWrapper
                            .eq("dasc.sell_place",siSellPlace)
                            .apply("week(dasc.check_time)>=week(now())-3 ")
                            .apply("week(dasc.check_time)<=week(now())");

                    //当前工厂近4周的批通率
                    List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiBatchPassPointWeek(siCompareWrapper,timeWrapper);
                    for (DfAoiSiCompare DfAoiSiCompare:siCompareList){
                        String passPoint = "0.00";
                        if (DfAoiSiCompare.getPassPoint()!=null){
                            passPoint = DfAoiSiCompare.getPassPoint();
                        }
                        sellPlacePassPointList.add(passPoint);
                    }
                    //当前出货地的近4周的批通率加入集合
                    passPointList.add(sellPlacePassPointList);
                }
                map.put("siSellPlaceList",siSellPlace4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI SI工厂对比批通率数据成功",map);
    }

    @RequestMapping(value = "getSiDefectPointFactory",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI工厂对比不良率和top5不良率")
    public Result getSiDefectPointFactory(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {
        Map<String, Object> map = new HashMap<>();
        //工厂（x轴）
        List<String> factoryList = new ArrayList<>();

        //不良率（y轴）
        List<Object> defectPointList = new ArrayList<>();

        //不良率top（y轴）
        List<Object> defectPointTop5List = new ArrayList<>();

        QueryWrapper<DfAoiSiCompare> defectPointWrapper = new QueryWrapper<>();
        QueryWrapper<String> defectNameWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)) {
            defectPointWrapper.eq("dasc.factory", factory);
            defectNameWrapper.eq("dasc.factory", factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)) {
            defectPointWrapper.eq("dasc.sell_place", sellPlace);
            defectNameWrapper.eq("dasc.sell_place", sellPlace);
        }
        if (StringUtils.isNotEmpty(project)) {
            defectPointWrapper.eq("dasc.`type`", project);
            defectNameWrapper.eq("dasc.`type`", project);
        }
        if (StringUtils.isNotEmpty(colour)) {
            defectPointWrapper.eq("dasc.colour", colour);
            defectNameWrapper.eq("dasc.colour", colour);
        }

        switch (timeType) {
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("month(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("month(dasc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("month(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("month(dasc.check_time)>="+startTime);
                }

                //所有工厂一段时间（月）内不良率
                List<DfAoiSiCompare> defectPointMonthList = dfAoiSiCompareService.getSiDefectPointFactory(defectPointWrapper);
                if (defectPointMonthList==null||defectPointMonthList.size()==0){
                    return new Result(500,"该条件下没有SI工厂相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointMonthList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiSiCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameMonthList = dfAoiSiCompareService.getSiDefectNameListFactory(defectNameWrapper);
                for (String defectName:defectNameMonthList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("month(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("month(dasc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("month(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("month(dasc.check_time)>="+startTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(月)所有工厂得不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListFactory(siCompareWrapper,siCompareWrapper2);
                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameMonthList);


                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("week(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("week(dasc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("week(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("week(dasc.check_time)<="+endTime);
                }

                //所有工厂一段时间（周）内不良率
                List<DfAoiSiCompare> defectPointWeekList = dfAoiSiCompareService.getSiDefectPointFactory(defectPointWrapper);
                if (defectPointWeekList==null||defectPointWeekList.size()==0){
                    return new Result(500,"该条件下没有SI工厂相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointWeekList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiSiCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameWeekList = dfAoiSiCompareService.getSiDefectNameListFactory(defectNameWrapper);
                for (String defectName:defectNameWeekList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("week(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("week(dasc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("week(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("week(dasc.check_time)<="+endTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(近四周)所有工厂得不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListFactory(siCompareWrapper,siCompareWrapper2);
                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameWeekList);

                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("day(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("day(dasc.check_time)>="+startTime);

                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("day(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("day(dasc.check_time)>="+startTime);
                }

                //所有工厂一段时间（天）内不良率
                List<DfAoiSiCompare> defectPointDayList = dfAoiSiCompareService.getSiDefectPointFactory(defectPointWrapper);
                if (defectPointDayList==null||defectPointDayList.size()==0){
                    return new Result(500,"该条件下没有SI工厂相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointDayList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiSiCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameList = dfAoiSiCompareService.getSiDefectNameListFactory(defectNameWrapper);
                for (String defectName:defectNameList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("day(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("day(dasc.check_time)>="+startTime);

                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("day(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("day(dasc.check_time)>="+startTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(天)所有工厂得不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListFactory(siCompareWrapper,siCompareWrapper2);

                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameList);
                break;

            case "近4周":
                defectPointWrapper
                        .apply("week(dasc.check_time)>=week(now())-3")
                        .apply("week(dasc.check_time)<=week(now())");
                defectNameWrapper
                        .apply("week(dasc.check_time)>=week(now())-3")
                        .apply("week(dasc.check_time)<=week(now())");
                //所有工厂近4周的不良率
                List<DfAoiSiCompare> defectPoint4WeekList = dfAoiSiCompareService.getSiDefectPointFactory(defectPointWrapper);
                if (defectPoint4WeekList == null || defectPoint4WeekList.size() == 0) {
                    return new Result(500, "该条件下没有SI工厂相关得不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare : defectPoint4WeekList) {

                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiSiCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNam4WeekeList = dfAoiSiCompareService.getSiDefectNameListFactory(defectNameWrapper);
                for (String defectName : defectNam4WeekeList) {
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)) {
                        siCompareWrapper.eq("dasc.factory", factory);
                        siCompareWrapper2.apply("dasc.factory='" + factory + "'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)) {
                        siCompareWrapper.eq("dasc.`type`", project);
                        siCompareWrapper2.apply("dasc.`type`='" + project + "'");
                    }
                    if (StringUtils.isNotEmpty(colour)) {
                        siCompareWrapper.eq("dasc.colour", colour);
                        siCompareWrapper2.apply("dasc.colour='" + colour + "'");
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name", defectName)
                            .apply("week(dasc.check_time)>=week(now())-3")
                            .apply("week(dasc.check_time)<=week(now())");

                    siCompareWrapper2
                            .apply("week(dasc.check_time)>=week(now())-3")
                            .apply("week(dasc.check_time)<=week(now())");

                    //该缺陷在一段时间内(近四周)所有工厂得不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListFactory(siCompareWrapper, siCompareWrapper2);

                    for (DfAoiSiCompare dfAoiSiCompare : oneDefectPointList) {
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint() != null) {
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList", defectNam4WeekeList);
                break;
        }
        map.put("factoryListX", factoryList);
        map.put("defectPointListY", defectPointList);
        map.put("defectPointTop5ListY", defectPointTop5List);

        return new Result(200, "获取AOI SI工厂对比不良率数据成功", map);
    }

    @RequestMapping(value = "getSiDefectPointSellPlace",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI出货地对比不良率和top5不良率")
    public Result getSiDefectPointSellPlace(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {
        Map<String, Object> map = new HashMap<>();
        //出货地（x轴）
        List<String> sellPlaceList = new ArrayList<>();

        //不良率（y轴）
        List<Object> defectPointList = new ArrayList<>();

        //不良率top（y轴）
        List<Object> defectPointTop5List = new ArrayList<>();

        QueryWrapper<DfAoiSiCompare> defectPointWrapper = new QueryWrapper<>();
        QueryWrapper<String> defectNameWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)) {
            defectPointWrapper.eq("dasc.factory", factory);
            defectNameWrapper.eq("dasc.factory", factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)) {
            defectPointWrapper.eq("dasc.sell_place", sellPlace);
            defectNameWrapper.eq("dasc.sell_place", sellPlace);
        }
        if (StringUtils.isNotEmpty(project)) {
            defectPointWrapper.eq("dasc.`type`", project);
            defectNameWrapper.eq("dasc.`type`", project);
        }
        if (StringUtils.isNotEmpty(colour)) {
            defectPointWrapper.eq("dasc.colour", colour);
            defectNameWrapper.eq("dasc.colour", colour);
        }

        switch (timeType) {
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("month(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("month(dasc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("month(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("month(dasc.check_time)>="+startTime);
                }

                //所有出货地一段时间（月）内不良率
                List<DfAoiSiCompare> defectPointMonthList = dfAoiSiCompareService.getSiDefectPointSellPlace(defectPointWrapper);
                if (defectPointMonthList==null||defectPointMonthList.size()==0){
                    return new Result(500,"该条件下没有SI工厂相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointMonthList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //出货地加入集合
                    sellPlaceList.add(dfAoiSiCompare.getSellPlace());
                }

                //所有top5缺陷名
                List<String> defectNameMonthList = dfAoiSiCompareService.getSiDefectNameListSellPlace(defectNameWrapper);
                for (String defectName:defectNameMonthList){
                    //该缺陷在一段时间内(月)所占所有出货地的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("month(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("month(dasc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("month(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("month(dasc.check_time)>="+startTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(月)所有出货地得不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListSellPlace(siCompareWrapper,siCompareWrapper2);
                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个出货地的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有出货地的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameMonthList);
                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("week(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("week(dasc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("week(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("week(dasc.check_time)<="+endTime);
                }

                //所有出货地一段时间（周）内不良率
                List<DfAoiSiCompare> defectPointWeekList = dfAoiSiCompareService.getSiDefectPointSellPlace(defectPointWrapper);
                if (defectPointWeekList==null||defectPointWeekList.size()==0){
                    return new Result(500,"该条件下没有SI出货地相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointWeekList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //出货地加入集合
                    sellPlaceList.add(dfAoiSiCompare.getSellPlace());
                }

                //所有top5缺陷名
                List<String> defectNameWeekList = dfAoiSiCompareService.getSiDefectNameListSellPlace(defectNameWrapper);
                for (String defectName:defectNameWeekList){
                    //该缺陷在一段时间内(月)所占所有出货地的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("week(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("week(dasc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("week(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("week(dasc.check_time)<="+endTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(近四周)所有出货地的不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListSellPlace(siCompareWrapper,siCompareWrapper2);
                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个出货地的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有出货地的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameWeekList);

                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("day(dasc.check_time)>="+startTime);
                    defectNameWrapper.apply("day(dasc.check_time)>="+startTime);

                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("day(dasc.check_time)<="+endTime);
                    defectNameWrapper.apply("day(dasc.check_time)>="+startTime);
                }

                //所有出货地一段时间（天）内不良率
                List<DfAoiSiCompare> defectPointDayList = dfAoiSiCompareService.getSiDefectPointSellPlace(defectPointWrapper);
                if (defectPointDayList==null||defectPointDayList.size()==0){
                    return new Result(500,"该条件下没有SI出货地相关的不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare:defectPointDayList){
                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //出货地加入集合
                    sellPlaceList.add(dfAoiSiCompare.getSellPlace());
                }

                //所有top5缺陷名
                List<String> defectNameList = dfAoiSiCompareService.getSiDefectNameListSellPlace(defectNameWrapper);
                for (String defectName:defectNameList){
                    //该缺陷在一段时间内(月)所占所有出货地的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        siCompareWrapper.eq("dasc.factory",factory);
                        siCompareWrapper2.apply("dasc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        siCompareWrapper.eq("dasc.`type`",project);
                        siCompareWrapper2.apply("dasc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        siCompareWrapper.eq("dasc.colour",colour);
                        siCompareWrapper2.apply("dasc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        siCompareWrapper.apply("day(dasc.check_time)>="+startTime);
                        siCompareWrapper2.apply("day(dasc.check_time)>="+startTime);

                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        siCompareWrapper.apply("day(dasc.check_time)<="+endTime);
                        siCompareWrapper2.apply("day(dasc.check_time)>="+startTime);
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name",defectName);

                    //该缺陷在一段时间内(天)所有出货地的不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListSellPlace(siCompareWrapper,siCompareWrapper2);

                    for (DfAoiSiCompare dfAoiSiCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个出货地的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有出货地的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameList);
                break;

            case "近4周":
                defectPointWrapper
                        .apply("week(dasc.check_time)>=week(now())-3")
                        .apply("week(dasc.check_time)<=week(now())");
                defectNameWrapper
                        .apply("week(dasc.check_time)>=week(now())-3")
                        .apply("week(dasc.check_time)<=week(now())");
                //所有出货地近4周的不良率
                List<DfAoiSiCompare> defectPoint4WeekList = dfAoiSiCompareService.getSiDefectPointSellPlace(defectPointWrapper);
                if (defectPoint4WeekList == null || defectPoint4WeekList.size() == 0) {
                    return new Result(500, "该条件下没有SI出货地相关得不良率");
                }

                for (DfAoiSiCompare dfAoiSiCompare : defectPoint4WeekList) {

                    //不良率加入集合
                    defectPointList.add(dfAoiSiCompare.getDefectPoint());
                    //出货地加入集合
                    sellPlaceList.add(dfAoiSiCompare.getSellPlace());
                }

                //所有top5缺陷名
                List<String> defectNam4WeekeList = dfAoiSiCompareService.getSiDefectNameListSellPlace(defectNameWrapper);
                for (String defectName : defectNam4WeekeList) {
                    //该缺陷在一段时间内(月)所占所有出货地的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiSiCompare> siCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiSiCompare> siCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)) {
                        siCompareWrapper.eq("dasc.factory", factory);
                        siCompareWrapper2.apply("dasc.factory='" + factory + "'");
                    }
                    if (StringUtils.isNotEmpty(sellPlace)) {
                        siCompareWrapper.eq("dasc.sell_place", sellPlace);
                        siCompareWrapper2.apply("dasc.sell_place='" + sellPlace + "'");
                    }
                    if (StringUtils.isNotEmpty(project)) {
                        siCompareWrapper.eq("dasc.`type`", project);
                        siCompareWrapper2.apply("dasc.`type`='" + project + "'");
                    }
                    if (StringUtils.isNotEmpty(colour)) {
                        siCompareWrapper.eq("dasc.colour", colour);
                        siCompareWrapper2.apply("dasc.colour='" + colour + "'");
                    }

                    siCompareWrapper
                            .eq("dasd.defect_name", defectName)
                            .apply("week(dasc.check_time)>=week(now())-3")
                            .apply("week(dasc.check_time)<=week(now())");

                    siCompareWrapper2
                            .apply("week(dasc.check_time)>=week(now())-3")
                            .apply("week(dasc.check_time)<=week(now())");

                    //该缺陷在一段时间内(近四周)所有出货地的不良率
                    List<DfAoiSiCompare> oneDefectPointList = dfAoiSiCompareService.getSiOneDefectPointListSellPlace(siCompareWrapper, siCompareWrapper2);

                    for (DfAoiSiCompare dfAoiSiCompare : oneDefectPointList) {
                        String defectPoint = "0.00";
                        if (dfAoiSiCompare.getDefectPoint() != null) {
                            defectPoint = dfAoiSiCompare.getDefectPoint();
                        }

                        //该缺陷所占一个出货地的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有出货地的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList", defectNam4WeekeList);
                break;
        }
        map.put("sellPlaceListX", sellPlaceList);
        map.put("defectPointListY", defectPointList);
        map.put("defectPointTop5ListY", defectPointTop5List);

        return new Result(200, "获取AOI SI出货地对比不良率数据成功", map);
    }


    @RequestMapping(value = "getSiDefectPointTop5",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI不良TOP5")
    public Result getSiDefectPointTop5(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {

        //所有型号的不良top5的不良名称及其不良占比
        List<Object> siTypeMapList = new ArrayList<>();

        QueryWrapper<String> siTypeWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            siTypeWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            siTypeWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            siTypeWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            siTypeWrapper.eq("dasc.colour",colour);
        }
        if (StringUtils.isNotEmpty(startTime)){
            siTypeWrapper.ge("dasc.check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            siTypeWrapper.le("dasc.check_time",endTime);
        }

        QueryWrapper<DfAoiSiCompare> siTimeRecentlyWrapper = new QueryWrapper<>();
        siTimeRecentlyWrapper
                .orderByDesc("check_time")
                .last("limit 1");
        DfAoiSiCompare siRecently = dfAoiSiCompareService.getOne(siTimeRecentlyWrapper);
        //有最新数据的时间
        String siTimeRecently = siRecently.getCheckTime();

        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            siTypeWrapper.eq("dasc.check_time",siTimeRecently);
        }

        //获取一段时间内的型号
        List<String> siTypeList = dfAoiSiCompareService.getSiTypeList(siTypeWrapper);
        if (siTypeList==null||siTypeList.size()==0){
            return new Result(500,"该条件下没有SI不良TOP5相关数据");
        }

        for (String siType:siTypeList){
            Map<String,Object> oneTypeMap = new HashMap<>();
            oneTypeMap.put("siType",siType);

            //该型号的top5缺陷名称
            List<String> defectNameTop5List = new ArrayList<>();
            //该型号的top5占比
            List<String> defectPointTop5List = new ArrayList<>();

            QueryWrapper<DfAoiSiCompare> defectPointTop5Wrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(factory)){
                defectPointTop5Wrapper.eq("dasc.factory",factory);
            }
            if (StringUtils.isNotEmpty(sellPlace)){
                defectPointTop5Wrapper.eq("dasc.sell_place",sellPlace);
            }
            if (StringUtils.isNotEmpty(project)){
                defectPointTop5Wrapper.eq("dasc.`type`",project);
            }
            if (StringUtils.isNotEmpty(colour)){
                defectPointTop5Wrapper.eq("dasc.colour",colour);
            }
            if (StringUtils.isNotEmpty(startTime)){
                defectPointTop5Wrapper.ge("dasc.check_time",startTime);
            }
            if (StringUtils.isNotEmpty(endTime)){
                defectPointTop5Wrapper.le("dasc.check_time",endTime);
            }
            if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
                defectPointTop5Wrapper.eq("dasc.check_time",siTimeRecently);
            }
            defectPointTop5Wrapper
                    .eq("dasc.`type`",siType);

            List<DfAoiSiCompare> siDefectPointTop5 = dfAoiSiCompareService.getSiDefectPointTop5List(defectPointTop5Wrapper);

            for (DfAoiSiCompare dfAoiSiCompare:siDefectPointTop5){
                //当前型号的缺陷名称加入集合
                defectNameTop5List.add(dfAoiSiCompare.getDefectName());
                //当前型号的缺陷不良占比加入集合
                defectPointTop5List.add(dfAoiSiCompare.getDefectPoint());
            }

            oneTypeMap.put("defectNameListX",defectNameTop5List);
            oneTypeMap.put("defectPointTop5ListY",defectPointTop5List);

            siTypeMapList.add(oneTypeMap);
        }
        return new Result(200,"获取一段时间内的SI不良TOP5相关数据成功",siTypeMapList);
    }

    @RequestMapping(value = "getSiPassPointAndBatch",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的SI良率和批通率")
    public Result getSiPassPointAndBatch(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("出货地")@RequestParam(required = false) String sellPlace
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {

        Map<String,Object> map = new HashMap<>();

        //型号集合（x轴）
        List<String> typeList = new ArrayList<>();

        //良率集合（y轴）
        List<String> passPointList = new ArrayList<>();

        //批通率集合（y轴）
        List<String> batchPassPointList = new ArrayList<>();

        QueryWrapper<DfAoiSiCompare> siPassPointAndBatchWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            siPassPointAndBatchWrapper.eq("dasc.factory",factory);
        }
        if (StringUtils.isNotEmpty(sellPlace)){
            siPassPointAndBatchWrapper.eq("dasc.sell_place",sellPlace);
        }
        if (StringUtils.isNotEmpty(project)){
            siPassPointAndBatchWrapper.eq("dasc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            siPassPointAndBatchWrapper.eq("dasc.colour",colour);
        }
        if (StringUtils.isNotEmpty(startTime)){
            siPassPointAndBatchWrapper.ge("dasc.check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            siPassPointAndBatchWrapper.le("dasc.check_time",endTime);
        }

        QueryWrapper<DfAoiSiCompare> siTimeRecentlyWrapper = new QueryWrapper<>();
        siTimeRecentlyWrapper
                .orderByDesc("check_time")
                .last("limit 1");
        DfAoiSiCompare siRecently = dfAoiSiCompareService.getOne(siTimeRecentlyWrapper);
        if (siRecently==null){
            return new Result(500,"最近没有相关数据");
        }

        //有最新数据的时间
        String siTimeRecently = siRecently.getCheckTime();

        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            siPassPointAndBatchWrapper.eq("dasc.check_time",siTimeRecently);
        }

        List<DfAoiSiCompare> siCompareList = dfAoiSiCompareService.getSiPassPointAndBatchList(siPassPointAndBatchWrapper);
        for (DfAoiSiCompare dfAoiSiCompare :siCompareList){

            //型号加入集合中
            typeList.add(dfAoiSiCompare.getType());
            //良率加入集合中
            passPointList.add(dfAoiSiCompare.getPassPoint());
            //批通率加入集合中
            batchPassPointList.add(dfAoiSiCompare.getBatchPassPoint());

        }

        map.put("typeListX",typeList);
        map.put("passPointListY",passPointList);
        map.put("batchPassPointListY",batchPassPointList);

        return new Result(200,"获取SI良率和批通率成功",map);
    }

}
