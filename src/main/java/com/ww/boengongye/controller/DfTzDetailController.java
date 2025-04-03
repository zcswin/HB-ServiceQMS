package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * TZ测量 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
@Controller
@RequestMapping("/dfTzDetail")
@CrossOrigin
@ResponseBody
@Api(tags = "TZ测量")
public class DfTzDetailController {

    @Autowired
    private DfTzDetailService dfTzDetailService;

    @Autowired
    private DfTemporaryDataService dfTemporaryDataService;

    @Autowired
    private DfYieldWarnService dfYieldWarnService;

    @Autowired
    private DfSizeContRelationService dfSizeContRelationService;

    @Autowired
    private DfSizeContStandService dfSizeContStandService;

//    @Autowired
//    private DfSizeCheckItemInfosService dfSizeCheckItemInfosService;

    @Autowired
    private DfTzDetailCheckService dfTzDetailCheckService;

    @Autowired
    private DfTzCheckItemInfosCheckService dfTzCheckItemInfosCheckService;

    @Autowired
    private DfLeadCheckItemInfosCheckService dfLeadCheckItemInfosCheckService;

    @Autowired
    private DfSizeCheckItemInfosCheckService dfSizeCheckItemInfosCheckService;

    @Autowired
    private RedisUtils redisUtils;


    @PostMapping("/importData")
    @ApiOperation("导入数据")
    public Result importData(MultipartFile file) throws Exception {
        int i = dfTzDetailService.importExcel(file);
        return new Result(200, "成功添加" + i + "条数据");
    }


    @GetMapping("/listLeadNum")
    @ApiOperation("产能数量趋势图（每两小时）")
    public Result listLeadNum(String factory, String lineBody,
                              @RequestParam String startDate, @RequestParam String endDate,
                              String project,String color,String device
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        Rate4 rate = dfTzDetailService.listTzNum(qw);
        if(rate==null){
            return new Result(200, "该条件下没有产能数量趋势图（每两小时）相关数据", result);
        }
        Integer[] numList = new Integer[]{rate.getInte1(), rate.getInte2(), rate.getInte3(), rate.getInte4(), rate.getInte5(),
                rate.getInte6(), rate.getInte7(), rate.getInte8(), rate.getInte9(), rate.getInte10(), rate.getInte11(), rate.getInte12()};
        String[] timeList = new String[]{"7:00", "9:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00", "1:00", "3:00", "5:00"};

        result.put("num", numList);
        result.put("time", timeList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listLeadNum2")
    @ApiOperation("产能数量趋势图（日期分组）")
    public Result listLeadNum2(String factory, String lineBody,
                               @RequestParam String startDate, @RequestParam String endDate
                                ,String project,String color,String device
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfTzDetailService.listAllNumGroupByDate(qw);
        if (rates==null||rates.size()==0){
            return new Result(200, "该条件下没有产能数量趋势图（日期分组）相关数据", result);
        }
        List<Object> dateList = new ArrayList<>();
        List<Object> dayList = new ArrayList<>();
        List<Object> nightList = new ArrayList<>();
        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            dayList.add(rate.getInte1());
            nightList.add(rate.getInte2());
        }

        result.put("date", dateList);
        result.put("day", dayList);
        result.put("night", nightList);
        return new Result(200, "查询成功", result);
    }



    @GetMapping("/listMachineOneAndMutilOkRate")
    @ApiOperation("工厂一次/综合良率对比2(机台分组)")
    public Result listMachineOneAndMutilOkRate(String factory, String lineBody,
                                               @RequestParam String startDate, @RequestParam String endDate
                                                ,String project,String color,String device
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfTzDetailService.listMachineOneAndMutilOkRate(qw);
        if (rates==null||rates.size()==0){
            return new Result(200, "该条件下没有工厂一次/综合良率对比2(机台分组)相关数据", result);
        }

        List<Object> machineList = new ArrayList<>();
        List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
        List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
        for (Rate3 rate : rates) {
            machineList.add(rate.getStr1());
            oneOkRateList.add(rate.getDou1());
            mutilOkRateList.add(rate.getDou2());
        }

        result.put("machine", machineList);
        result.put("oneOkRate", oneOkRateList);
        result.put("mutilOkRate", mutilOkRateList);
        return new Result(200, "查询成功", result);
    }


    @GetMapping("/listDateOneAndMutilOkRate")
    @ApiOperation("工厂一次/综合良率对比2(时间分组)")
    public Result listDateOneAndMutilOkRate(String factory, String lineBody,
                                            @RequestParam String startDate, @RequestParam String endDate
                                            ,String project,String color,String device
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfTzDetailService.listDateOneAndMutilOkRate(qw);
        if (rates==null||rates.size()==0){
            return new Result(200,"该条件下没有工厂一次/综合良率对比2(时间分组)相关数据",result);
        }

        List<Object> timeList = new ArrayList<>();
        List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
        List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
        for (Rate3 rate : rates) {
            timeList.add(rate.getStr1());
            oneOkRateList.add(rate.getDou1());
            mutilOkRateList.add(rate.getDou2());
        }

        result.put("time", timeList);
        result.put("oneOkRate", oneOkRateList);
        result.put("mutilOkRate", mutilOkRateList);
        return new Result(200, "查询成功", result);
    }

//    @GetMapping("/listOKRate")
//    @ApiOperation("工厂一次/综合良率对比")
//    public Result listOKRate(String factory, String lineBody,
//                             @RequestParam String startDate, @RequestParam String endDate
//                            ,String project,String color,String device
//    ) throws ParseException {
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
//        qw.between("check_time", startTime, endTime);
//        List<Rate3> rates = dfTzDetailService.listOKRate(qw);
//        List<String> factoryList = new ArrayList<>();
//        List<Double> oneRateList = new ArrayList<>();
//        List<Double> mutilRateList = new ArrayList<>();
//        for (Rate3 rate : rates) {
//            oneRateList.add(rate.getDou1());
//            mutilRateList.add(rate.getDou2());
//        }
//        factoryList.add("J10-1");
//        Map<String, Object> result = new HashMap<>();
//        result.put("factory", factoryList);
//        result.put("oneRate", oneRateList);
//        result.put("mutilRate", mutilRateList);
//        return new Result(200, "查询成功", result);
//    }

    @GetMapping("/listOkRateGroupByDate")
    @ApiOperation("白夜班良率趋势对比")
    public Result listOkRateGroupByDate(String factory, String lineBody,
                                        @RequestParam String startDate, @RequestParam String endDate
                                        ,String project,String color,String device
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfTzDetailService.listOkRateGroupByDate(qw);
        if (rates==null||rates.size()==0){
            return new Result(200,"该条件下没有白夜班良率趋势对比相关数据",result);
        }

        List<Object> dateList = new ArrayList<>();
        List<Object> dayList = new ArrayList<>();
        List<Object> nightList = new ArrayList<>();
        List<Object> targetList = new ArrayList<>();

        //TZ-目标良率
        QueryWrapper<DfYieldWarn> yieldWarnWrapper = new QueryWrapper<>();
        yieldWarnWrapper
                .eq("`type`","TZ")
                .eq("name","目标良率")
                .last("limit 1");
        DfYieldWarn dfYieldWarn = dfYieldWarnService.getOne(yieldWarnWrapper);


        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            dayList.add(rate.getDou1());
            nightList.add(rate.getDou2());
            targetList.add(dfYieldWarn.getPrewarningValue());
        }

        result.put("date", dateList);
        result.put("day", dayList);
        result.put("night", nightList);
        result.put("targetListY",targetList);
        return new Result(200, "查询成功", result);
    }


    @GetMapping("/listItemNgRateTop")
    @ApiOperation("工厂不良TOP10分布对比2")
    public Result listItemNgRateTop(String factory, String lineBody,
                                    @RequestParam String startDate, @RequestParam String endDate
                                    ,String project,String color,String device,
                                    @RequestParam Integer checkType
    ) throws ParseException {
        QueryWrapper<DfTemporaryData> dataWrapper = new QueryWrapper<>();
        dataWrapper
                .eq("`method`","listItemNgRateTop")
                .eq("name",checkType)
                .eq("start_time",startDate)
                .eq("end_time",endDate);
        List<DfTemporaryData> list = dfTemporaryDataService.list(dataWrapper);
        if (list!=null&&list.size()!=0){
            List<Object> itemList = new ArrayList<>();
            List<Object> ngRateList = new ArrayList<>();
            List<Object> ngNumList = new ArrayList<>();
            for (DfTemporaryData data:list){
                itemList.add(data.getStr1());
                ngNumList.add(data.getInte1());
                ngRateList.add(data.getDou1());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("item", itemList);
            result.put("ngNum",ngNumList);
            result.put("ngRate", ngRateList);
            return new Result(200, "查询成功", result);
        }


        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("dtd.check_time", startTime, endTime);

        String selectNameString = "";
        if (null != checkType) {
            if (checkType == 1) {  // 外观
                selectNameString = "item.item_name as 'name'";
                qw.eq("item.check_type", 1);
            } else if (checkType == 2){  //尺寸
                selectNameString =
                        "SUBSTRING(\n" +
                        "    SUBSTRING_INDEX(item.item_name, '(', 2),\n" +
                        "    LOCATE('(', item.item_name) + 1,\n" +
                        "    LOCATE(')', item.item_name) - LOCATE('(', item.item_name) - 1\n" +
                        "  ) as 'name'";
                qw.eq("item.check_type", 2);
            }
        }
        List<Rate3> rates = dfTzDetailService.listItemNgRateTop(qw,selectNameString);

//        //导入临时数据
//        for(Rate3 rate3:rates){
//            DfTemporaryData dfTemporaryData = new DfTemporaryData();
//            dfTemporaryData.setMethod("listItemNgRateTop");
//            dfTemporaryData.setName(checkType.toString());
//            dfTemporaryData.setStr1(rate3.getStr1());
//            dfTemporaryData.setInte1(rate3.getInte1().toString());
//            dfTemporaryData.setDou1(rate3.getDou1().toString());
//            dfTemporaryData.setStartTime(startDate);
//            dfTemporaryData.setEndTime(endDate);
//
//            dfTemporaryDataService.save(dfTemporaryData);
//        }



        List<Object> itemList = new ArrayList<>();
        List<Object> ngRateList = new ArrayList<>();
        List<Object> ngNumList = new ArrayList<>();
        for (Rate3 rate : rates) {
            itemList.add(rate.getStr1());
            ngNumList.add(rate.getInte1());
            ngRateList.add(rate.getDou1());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("item", itemList);
        result.put("ngNum",ngNumList);
        result.put("ngRate", ngRateList);
        return new Result(200, "查询成功", result);
    }

//    @GetMapping("/listItemNgRateTopImport")
//    @ApiOperation("工厂不良TOP10分布对比（录入临时数据）")
//    public Result listItemNgRateTopImport(String factory, String lineBody,
//                                    @RequestParam String startDate, @RequestParam String endDate
//            ,String project,String color,String device,
//                                    @RequestParam Integer checkType
//    ) throws ParseException {
//        QueryWrapper<DfTemporaryData> dataWrapper = new QueryWrapper<>();
//        dataWrapper
//                .eq("`method`","listItemNgRateTop")
//                .eq("name",checkType)
//                .eq("start_time",startDate)
//                .eq("end_time",endDate);
//        List<DfTemporaryData> list = dfTemporaryDataService.list(dataWrapper);
//        if (list!=null&&list.size()!=0){
//            List<Object> itemList = new ArrayList<>();
//            List<Object> ngRateList = new ArrayList<>();
//            List<Object> ngNumList = new ArrayList<>();
//            for (DfTemporaryData data:list){
//                itemList.add(data.getStr1());
//                ngNumList.add(data.getInte1());
//                ngRateList.add(data.getDou1());
//            }
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("item", itemList);
//            result.put("ngNum",ngNumList);
//            result.put("ngRate", ngRateList);
//            return new Result(200, "查询成功", result);
//        }
//
//
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
//        qw.between("dtd.check_time", startTime, endTime);
//
//        if (null != checkType) {
//            if (checkType == 1) {  // 外观
//                qw.eq("item.check_type", 1)
//                        .apply("SUBSTRING_INDEX(SUBSTRING_INDEX(item.check_name, '_', -2), '_', 1) in ('DL','DA','DB','DE','Gloss')");
//            } else if (checkType == 2){  //尺寸
//                qw.eq("item.check_type", 2);
//            }
//        }
//        List<Rate3> rates = dfTzDetailService.listItemNgRateTop(qw);
//
//        //导入临时数据
//        for(Rate3 rate3:rates){
//            DfTemporaryData dfTemporaryData = new DfTemporaryData();
//            dfTemporaryData.setMethod("listItemNgRateTop");
//            dfTemporaryData.setName(checkType.toString());
//            dfTemporaryData.setStr1(rate3.getStr1());
//            dfTemporaryData.setInte1(rate3.getInte1().toString());
//            dfTemporaryData.setDou1(rate3.getDou1().toString());
//            dfTemporaryData.setStartTime(startDate);
//            dfTemporaryData.setEndTime(endDate);
//
//            dfTemporaryDataService.save(dfTemporaryData);
//        }
//
//
//
//        List<Object> itemList = new ArrayList<>();
//        List<Object> ngRateList = new ArrayList<>();
//        List<Object> ngNumList = new ArrayList<>();
//        for (Rate3 rate : rates) {
//            itemList.add(rate.getStr1());
//            ngNumList.add(rate.getInte1());
//            ngRateList.add(rate.getDou1());
//        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("item", itemList);
//        result.put("ngNum",ngNumList);
//        result.put("ngRate", ngRateList);
//        return new Result(200, "查询成功", result);
//    }


    @GetMapping("/getItemNormalDistributionByItem")
    @ApiOperation("获取测试项的正态分布图")
    public Result getItemNormalDistributionByItem(String factory, String lineBody,
                                                  @RequestParam String startDate,@RequestParam String endDate
                                                  ,String project,String color,String device,
                                                  @RequestParam String item
    ) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("dtd.check_time", startTime, endTime)
                .eq("item.item_name", item);
        List<DfTzCheckItemInfos> checkItemInfos = dfTzDetailService.listItemInfosJoinDetail(qw);
        Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<String, Double> itemResStandardValue = new HashMap<>();
        Map<String, Double> itemResUsl = new HashMap<>();
        Map<String, Double> itemResLsl = new HashMap<>();
        Map<String, Integer> itemResOkNum = new HashMap<>();
        Map<String, Integer> itemResAllNum = new HashMap<>();

        for (DfTzCheckItemInfos checkItemInfo : checkItemInfos) {
            String process = checkItemInfo.getItemName();
            Double checkValue = checkItemInfo.getCheckValue();
            itemResAllNum.merge(process, 1, Integer::sum);
            if (checkItemInfo.getCheckResult().equals("OK")) itemResOkNum.merge(process, 1, Integer::sum);
            if (!itemResCheckValue.containsKey(process)) {
                List<Double> list = new ArrayList<>();
                list.add(checkValue);
                itemResCheckValue.put(process, list);
                itemResStandardValue.put(process, checkItemInfo.getStandardValue());
                itemResUsl.put(process, checkItemInfo.getUsl());
                itemResLsl.put(process, checkItemInfo.getLsl());
            } else {
                itemResCheckValue.get(process).add(checkValue);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : itemResCheckValue.entrySet()) {
            Map<String, Object> itemData = new HashMap<>();
            String process = entry.getKey();
            itemData.put("name", process);
            itemData.put("standard", itemResStandardValue.get(process));
            itemData.put("usl", itemResUsl.get(process));
            itemData.put("lsl", itemResLsl.get(process));

            Integer okNum = itemResOkNum.get(process) == null ? 0 : itemResOkNum.get(process);
            Integer allNum = itemResAllNum.get(process) == null ? 0 : itemResAllNum.get(process);
            //itemData.put("良率OK", itemResOkNum.get(itemName));
            itemData.put("良率", okNum.doubleValue() / allNum);
            itemData.put("allNum", allNum);
            //itemData.put("良率ALL", itemResAllNum.get(itemName));
            NormalDistributionUtil.normalDistribution2(NormalDistributionUtil.convertToDoubleArray(entry.getValue().toArray()), itemData);
            result.add(itemData);
        }
        return new Result(200, "查询成功", result);
    }


//    @GetMapping("/listWorkPositionOKRate")
//    @ApiOperation("工位间良率差异对比")
//    public Result listWorkPositionOKRate(String factory, String lineBody,
//                                         @RequestParam String startDate, @RequestParam String endDate
//                                        ,String project,String color,String device
//    ) throws ParseException {
//        Map<String, Object> result = new HashMap<>();
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
//        qw.between("check_time", startTime, endTime);
//        List<Rate3> rates = dfTzDetailService.listWorkPositionOKRate(qw);
//        if (rates==null||rates.size()==0){
//            return new Result(200,"该条件下没有工位间良率差异对比相关数据",result);
//        }
//
//        Map<String, Integer> macResIndex = new HashMap<>();
//        List<String> macList = new ArrayList<>();
//        int macIndex = 0;
//        for (Rate3 rate : rates) {
//            if (!macResIndex.containsKey(rate.getStr1())) {
//                macList.add(rate.getStr1());
//                macResIndex.put(rate.getStr1(), macIndex++);
//            }
//        }
//        Double[][] rateData = new Double[4][macResIndex.size()];
//        String[] positionList = new String[]{"1号位", "2号位", "3号位", "4号位"};
//        for (Rate3 rate : rates) {
//            String machine = rate.getStr1();
//            Integer pos = Integer.valueOf(rate.getStr2());
//            Double okRate = rate.getDou1();
//            rateData[pos][macResIndex.get(machine)] = okRate;
//        }
//
//        result.put("position", positionList);
//        result.put("machine", macList);
//        result.put("A1", rateData[0]);
//        result.put("A2", rateData[1]);
//        result.put("B3", rateData[2]);
//        result.put("B4", rateData[3]);
//
//        return new Result(200, "查询成功", result);
//    }

    @GetMapping("/listWorkPositionOKRate")
    @ApiOperation("工位间良率差异对比")
    public Result listWorkPositionOKRate(String factory, String lineBody, String process,
                                         @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfTzDetailService.listWorkPositionOKRate(qw);
        rates = rates.stream().filter(obj -> obj.getStr1() != null && StringUtils.isNotEmpty(obj.getStr1())).collect(Collectors.toList());
//        Map<String, Integer> macResIndex = new HashMap<>();
        List<String> macList = new ArrayList<>();
//        int macIndex = 0;
        for (Rate3 rate : rates) {
//            if (!macResIndex.containsKey(rate.getStr1())) {
            macList.add(rate.getStr1());
//                macResIndex.put(rate.getStr1(), macIndex++);
//            }
        }
        Double[][] rateData = new Double[1][macList.size()];
//        Map<String, Integer> positionResIndex = new HashMap<>();
//        positionResIndex.put("A1", 0);
//        positionResIndex.put("A2", 1);
//        positionResIndex.put("B3", 2);
//        positionResIndex.put("B4", 3);
//        String[] positionList = new String[]{"A1", "A2", "B3", "B4"};
        for (int i = 0; i < rates.size(); i++) {
            rateData[0][i] =rates.get(i).getDou1();
        }

        Map<String, Object> result = new HashMap<>();
//        result.put("position", positionList);
        result.put("machine", macList);
        result.put("position", rateData[0]);
//        result.put("A2", rateData[1]);
//        result.put("B3", rateData[2]);
//        result.put("B4", rateData[3]);

        return new Result(200, "查询成功", result);
    }


    @GetMapping("/listAll")
    @ApiOperation("获取详情")
    public Result listAll(String factory, String lineBody,
                          @RequestParam String startDate, @RequestParam String endDate
                            ,String project,String color,String device
    ) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime)
                .eq(!"".equals(color) && null != color, "color", color);
        List<DfTzDetail> list = dfTzDetailService.list(qw);

        return new Result(200, "查询成功",list,list.size());
    }


//    @GetMapping("/getNgRate")
//    @ApiOperation("TZ不良走势")
//    public Result getNgRate(
//            String factory,String project,String color,
//            @RequestParam String startDate, @RequestParam String endDate
//    ) throws ParseException {
//
//        Map<String,Object> resultMap = new HashMap<>();
//        List<Object> timeList = new ArrayList<>();
//        List<Object> ngRateList = new ArrayList<>();
//
////        String startTime = startDate + " 07:00:00";
////        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";
//
//        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
//        qw
//                .eq(StringUtils.isNotEmpty(factory),"factory",factory)
//                .eq(StringUtils.isNotEmpty(project),"project",project)
//                .eq(StringUtils.isNotEmpty(color),"color", color)
//                .between("check_time", startTime, endTime);
//
//        List<Rate3> list = dfTzDetailService.getNgRate(qw);
//        if(list == null || list.size() == 0){
//            resultMap.put("timeList",timeList);
//            resultMap.put("ngRateList",ngRateList);
//            return new Result(500, "当前条件下没有相关TZ数据",resultMap);
//        }
//
//        for(Rate3 rate : list){
//            timeList.add(rate.getStr1());
//            ngRateList.add(rate.getDou1());
//        }
//        resultMap.put("timeList",timeList);
//        resultMap.put("ngRateList",ngRateList);
//
//        return new Result(200, "获取TZ不良走势数据成功",resultMap);
//    }
//
//    @GetMapping("/getNgDetailRateTop10")
//    @ApiOperation("TZ不良Top10")
//    public Result getNgDetailRateTop10(
//            String factory,String project,String color,
//            @RequestParam String startDate, @RequestParam String endDate
//    ){
//        String redisKey = "qms:TZTOP10:"+factory+"_"+project+"_"+color+"_"+startDate+"_"+endDate;
//
//        if(redisUtils.hasKey(redisKey)){
//            String resultMapStr = redisUtils.get(redisKey).toString();
//            Object resultMap = JSONObject.parseObject(resultMapStr);
//            return new Result(200, "获取TZ不良Top10数据成功", resultMap);
//
//        }
//
//        Map<String,Object> resultMap = new HashMap<>();
//        List<Object> defectList = new ArrayList<>();
//        List<Object> ngRateList = new ArrayList<>();
//
////        String startTime = startDate + " 07:00:00";
////        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";
//
//        QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
//        qw
//                .eq(StringUtils.isNotEmpty(factory),"dtd.factory",factory)
//                .eq(StringUtils.isNotEmpty(project),"dtd.project",project)
//                .eq(StringUtils.isNotEmpty(color), "dtd.color", color)
//                .between("dtd.check_time", startTime, endTime);
//
//        List<Rate3> list = dfTzDetailService.getNgDetailRateTop10(qw);
//        if(list == null || list.size() == 0){
//            resultMap.put("defectList",defectList);
//            resultMap.put("ngRateList",ngRateList);
//            return new Result(500, "当前条件下没有相关TZ数据",resultMap);
//        }
//
//        for(Rate3 rate : list){
//            defectList.add(rate.getStr1());
//            ngRateList.add(rate.getDou1());
//        }
//        resultMap.put("defectList",defectList);
//        resultMap.put("ngRateList",ngRateList);
//
//        redisUtils.set(redisKey, JSON.toJSONString(resultMap),86400);
//
//        return new Result(200, "获取TZ不良走势数据成功",resultMap);
//    }
//
//    @GetMapping("/DefectDataAnalysis")
//    @ApiOperation("TZ不良项数据分析")
//    public Result DefectDataAnalysis(
//            @RequestParam String factory,
//            @RequestParam String project,
//            @RequestParam String color,
//            @RequestParam String startDate,
//            @RequestParam String endDate,
//            @RequestParam String defectName
//    ) throws ParseException {
//
//        String redisKey = "qms:TZDefectDataAnalysis:"+factory+"_"+project+"_"+color+"_"+startDate+"_"+endDate+"_"+defectName;
//
//        if(redisUtils.hasKey(redisKey)){
//            String resultMapStr = redisUtils.get(redisKey).toString();
//            Object[][] resultArray = JSONObject.parseObject(resultMapStr,new TypeReference<Object[][]>(){});
//            return new Result(200, "获取TZ不良Top10数据成功", resultArray);
//
//        }
//
//        Map<String,Integer> processIndexMap = new HashMap<>();
//        processIndexMap.put("CNC2",2);
//        processIndexMap.put("SPM",3);
//        processIndexMap.put("LEAD",4);
//        processIndexMap.put("TZ",5);
//
//        Object[][] resultArray = new Object[10][6];
//        //表头
//        resultArray[0] = new Object[]{defectName,"控制限","CNC2","SPM","LEAD","TZ"};
//        //QCP上限
//        resultArray[1] = new Object[]{"QCP控制限","USL","/","/","/","/"};
//        //QCP下限
//        resultArray[2] = new Object[]{"QCP控制限","LSL","/","/","/","/"};
//        //实际控制上限
//        resultArray[3] = new Object[]{"实际控制限","USL","/","/","/","/"};
//        //实际控制下限
//        resultArray[4] = new Object[]{"实际控制限","LSL","/","/","/","/"};
//        //建议控制上限
//        resultArray[5] = new Object[]{"建议控制限","USL","/","/","/","/"};
//        //建议控制下限
//        resultArray[6] = new Object[]{"建议控制限","LSL","/","/","/","/"};
//        ////CPK
//        resultArray[7] = new Object[]{"CPK","实际","/","/","/","/"};
//        //正太分布
//        resultArray[8] = new Object[]{"正太分布","/","/","/","/","/"};
//        //异常机台
//        resultArray[9] = new Object[]{"异常机台","/","/","/","/","/"};
//
//        //TZ时间
////        String startTime = startDate + " 07:00:00";
////        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";
//        //LEAD时间
//        String startTimeLead = TimeUtil.getOffsetDate(startTime,-2,"yyyy-MM-dd HH:mm:ss");
//        String endTimeLead = TimeUtil.getOffsetDate(endTime,-2,"yyyy-MM-dd HH:mm:ss");
//        //SPM时间
//        String startTimeSpm = TimeUtil.getOffsetDate(startTimeLead,-2,"yyyy-MM-dd HH:mm:ss");
//        String endTimeSpm = TimeUtil.getOffsetDate(endTimeLead,-2,"yyyy-MM-dd HH:mm:ss");
//        //CNC2时间
//        String startTimeCnc2 = TimeUtil.getOffsetDate(startTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");
//        String endTimeCnc2 = TimeUtil.getOffsetDate(endTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");
//
//        //LEAD不良名
//        String leadDefectName = "";
//        //TZ不良名
//        String tzDefectName = "";
//
//        QueryWrapper<DfSizeContRelation> relationQw = new QueryWrapper<>();
//        relationQw
//                .eq("factory",factory)
//                .eq("project",project)
//                .eq("color",color)
//                .eq("ipqc_name",defectName);
//        List<DfSizeContRelation> relationList = dfSizeContRelationService.list(relationQw);
//        if (relationList == null || relationList.size() == 0){
//            return new Result(500, "当前条件下没有相关QCP数据");
//        }
//        for (DfSizeContRelation relationItem : relationList) {
//            if (!processIndexMap.containsKey(relationItem.getProcess())){
//                continue;
//            }
//            Integer processIndex = processIndexMap.get(relationItem.getProcess());
//            resultArray[1][processIndex] = relationItem.getQcpUsl();
//            resultArray[2][processIndex] = relationItem.getQcpLsl();
//
//            if("LEAD".equals(relationItem.getProcess())){
//                leadDefectName = relationItem.getName();
//            }
//            if("TZ".equals(relationItem.getProcess())){
//                tzDefectName = relationItem.getName();
//            }
//        }
//
//        resultArray[0][0] = tzDefectName+" "+defectName;
//
//        QueryWrapper<DfSizeContStand> sizeStandQw = new QueryWrapper<>();
//        sizeStandQw
//                .eq("project",project)
//                .eq("color",color)
//                .eq("test_item",defectName);
//
//        List<DfSizeContStand> sizeStandList = dfSizeContStandService.list(sizeStandQw);
//        if (sizeStandList == null || sizeStandList.size() == 0){
//            return new Result(500, "当前条件下没有相关实际控制限和建议控制限数据");
//        }
//        for (DfSizeContStand sizeStandItem : sizeStandList) {
//            if (!processIndexMap.containsKey(sizeStandItem.getProcess())){
//                continue;
//            }
//            Integer processIndex = processIndexMap.get(sizeStandItem.getProcess());
//            resultArray[3][processIndex] = sizeStandItem.getUpperLimit();
//            resultArray[4][processIndex] = sizeStandItem.getLowerLimit();
//            resultArray[5][processIndex] = sizeStandItem.getUpperLimit();
//            resultArray[6][processIndex] = sizeStandItem.getLowerLimit();
//        }
//
//        //CNC2数据
//        QueryWrapper<DfSizeDetail> cnc2Qw = new QueryWrapper<>();
//        cnc2Qw
//                .eq("dsd.process","CNC2")
//                .eq("dsd.factory",factory)
//                .eq("dsd.project",project)
//                .eq("dsd.item_name",color)
//                .eq("dscii.item_name",defectName)
//                .between("dsd.test_time",startTimeCnc2,endTimeCnc2);
//        List<Map<String,Object>> cnc2DataList = dfSizeCheckItemInfosService.getDetailData(cnc2Qw);
//        if (cnc2DataList != null && cnc2DataList.size() != 0){
//            Map<String, Object> itemData = new HashMap<>();
//
//            Map<String,String> machineMap = new HashMap<>();
//            List<String> machineList = new ArrayList<>();
//            List<Double> checkValueList = new ArrayList<>();
//            Double usl = Double.valueOf(cnc2DataList.get(0).get("usl").toString());
//            Double lsl = Double.valueOf(cnc2DataList.get(0).get("lsl").toString());
//            Double standardValue = Double.valueOf(cnc2DataList.get(0).get("check_value").toString());
//            Integer okNum = 0;
//            Integer total = cnc2DataList.size();
//            for (Map<String, Object> item : cnc2DataList){
//                String machineName = item.get("machine_code").toString();
//                Double ngRate = Double.valueOf(item.get("ngRate").toString());
//                if (!machineMap.containsKey(item.get("machine_code")) && ngRate > 0){
//                    machineMap.put(machineName,machineName +" "+ ngRate.toString()+"%");
//                    machineList.add(machineName +" "+ ngRate.toString()+"%");
//                }
//                if("OK".equals(item.get("check_result"))){
//                    okNum++;
//                }
//                checkValueList.add(Double.valueOf(item.get("check_value").toString()));
//            }
//            //平均值
//            Double mean = MathUtils.calculateMean(checkValueList);
//            //方差
//            Double variance = MathUtils.calculateVariance(checkValueList, mean);
//            //标准差
//            Double stdDev = MathUtils.calculateStandardDeviation(variance);
//            //CPK
//            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
//            //良率
//            Double okRate = okNum.doubleValue() / total;
//            itemData.put("name", defectName);
//            itemData.put("standard", standardValue);
//            itemData.put("usl", usl);
//            itemData.put("lsl", lsl);
//
//            itemData.put("okRate", String.format("%.2f", okRate * 100));
//            itemData.put("total", total);
//            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);
//
//            resultArray[7][2] = cpk;
//            resultArray[8][2] = itemData;
//            resultArray[9][2] = machineList.size()>0?machineList:"/";
//        }
//
//        //SPM数据
//        QueryWrapper<DfSizeDetail> spmQw = new QueryWrapper<>();
//        spmQw
//                .eq("dsd.process","SPM")
//                .eq("dsd.factory",factory)
//                .eq("dsd.project",project)
//                .eq("dsd.item_name",color)
//                .eq("dscii.item_name",defectName)
//                .between("dsd.test_time",startTimeSpm,endTimeSpm);
//        List<Map<String,Object>> spmDataList = dfSizeCheckItemInfosService.getDetailData(spmQw);
//        if (spmDataList != null && spmDataList.size() != 0){
//            Map<String, Object> itemData = new HashMap<>();
//
//            Map<String,String> machineMap = new HashMap<>();
//            List<String> machineList = new ArrayList<>();
//            List<Double> checkValueList = new ArrayList<>();
//            Double usl = Double.valueOf(spmDataList.get(0).get("usl").toString());
//            Double lsl = Double.valueOf(spmDataList.get(0).get("lsl").toString());
//            Double standardValue = Double.valueOf(spmDataList.get(0).get("check_value").toString());
//            Integer okNum = 0;
//            Integer total = spmDataList.size();
//            for (Map<String, Object> item : spmDataList){
//                String machineName = item.get("machine_code").toString();
//                Double ngRate = Double.valueOf(item.get("ngRate").toString());
//                if (!machineMap.containsKey(item.get("machine_code")) && ngRate > 0){
//                    machineMap.put(machineName,machineName +" "+ ngRate.toString()+"%");
//                    machineList.add(machineName +" "+ ngRate.toString()+"%");
//                }
//                if("OK".equals(item.get("check_result"))){
//                    okNum++;
//                }
//                checkValueList.add(Double.valueOf(item.get("check_value").toString()));
//            }
//            //平均值
//            Double mean = MathUtils.calculateMean(checkValueList);
//            //方差
//            Double variance = MathUtils.calculateVariance(checkValueList, mean);
//            //标准差
//            Double stdDev = MathUtils.calculateStandardDeviation(variance);
//            //CPK
//            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
//            //良率
//            Double okRate = okNum.doubleValue() / total;
//            itemData.put("name", defectName);
//            itemData.put("standard", standardValue);
//            itemData.put("usl", usl);
//            itemData.put("lsl", lsl);
//
//            itemData.put("okRate", String.format("%.2f", okRate * 100));
//            itemData.put("total", total);
//            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);
//
//            resultArray[7][3] = cpk;
//            resultArray[8][3] = itemData;
//            resultArray[9][3] = machineList.size()>0?machineList:"/";
//        }
//
//        //LEAD数据
//        QueryWrapper<DfSizeDetail> leadQw = new QueryWrapper<>();
//        leadQw
//                .eq("dld.factory",factory)
//                .eq("dld.project",project)
//                .eq("dld.color",color)
//                .eq("dlcii.item_name",leadDefectName)
//                .between("dld.check_time",startTimeLead,endTimeLead);
//        List<DfLeadCheckItemInfos> leadDataList = dfLeadCheckItemInfosService.getDetailData(leadQw);
//        if (leadDataList != null && leadDataList.size() != 0){
//            Map<String, Object> itemData = new HashMap<>();
//
//            List<Double> checkValueList = new ArrayList<>();
//            Double usl = leadDataList.get(0).getUsl();
//            Double lsl = leadDataList.get(0).getLsl();
//            Double standardValue = leadDataList.get(0).getStandardValue();
//            Integer okNum = 0;
//            Integer total = leadDataList.size();
//            for (DfLeadCheckItemInfos item : leadDataList){
//                if("OK".equals(item.getCheckResult())){
//                    okNum++;
//                }
//                checkValueList.add(item.getCheckValue());
//            }
//            //平均值
//            Double mean = MathUtils.calculateMean(checkValueList);
//            //方差
//            Double variance = MathUtils.calculateVariance(checkValueList, mean);
//            //标准差
//            Double stdDev = MathUtils.calculateStandardDeviation(variance);
//            //CPK
//            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
//            //良率
//            Double okRate = okNum.doubleValue() / total;
//            itemData.put("name", defectName);
//            itemData.put("standard", standardValue);
//            itemData.put("usl", usl);
//            itemData.put("lsl", lsl);
//
//            itemData.put("okRate", String.format("%.2f", okRate * 100));
//            itemData.put("total", total);
//            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);
//
//            resultArray[7][4] = cpk;
//            resultArray[8][4] = itemData;
//        }
//
//        //TZ数据
//        QueryWrapper<DfTzDetail> tzQw = new QueryWrapper<>();
//        tzQw
//                .eq("dtd.factory",factory)
//                .eq("dtd.project",project)
//                .eq("dtd.color",color)
//                .eq("dtcii.item_name",tzDefectName)
//                .between("dtd.check_time",startTime,endTime);
//        List<DfTzCheckItemInfos> tzDataList = dfTzCheckItemInfosService.getDetailData(tzQw);
//        if (tzDataList != null && tzDataList.size() != 0){
//            Map<String, Object> itemData = new HashMap<>();
//
//            List<Double> checkValueList = new ArrayList<>();
//            Double usl = tzDataList.get(0).getUsl();
//            Double lsl = tzDataList.get(0).getLsl();
//            Double standardValue = tzDataList.get(0).getStandardValue();
//            Integer okNum = 0;
//            Integer total = tzDataList.size();
//            for (DfTzCheckItemInfos item : tzDataList){
//                if("OK".equals(item.getCheckResult())){
//                    okNum++;
//                }
//                checkValueList.add(item.getCheckValue());
//            }
//            //平均值
//            Double mean = MathUtils.calculateMean(checkValueList);
//            //方差
//            Double variance = MathUtils.calculateVariance(checkValueList, mean);
//            //标准差
//            Double stdDev = MathUtils.calculateStandardDeviation(variance);
//            //CPK
//            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
//            //良率
//            Double okRate = okNum.doubleValue() / total;
//            itemData.put("name", defectName);
//            itemData.put("standard", standardValue);
//            itemData.put("usl", usl);
//            itemData.put("lsl", lsl);
//
//            itemData.put("okRate", String.format("%.2f", okRate * 100));
//            itemData.put("total", total);
//            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);
//
//            resultArray[7][5] = cpk;
//            resultArray[8][5] = itemData;
//        }
//
//        redisUtils.set(redisKey, JSON.toJSONString(resultArray),86400);
//
//        return new Result(200, "获取数据分析成功",resultArray);
//    }

    @GetMapping("/getNgRate")
    @ApiOperation("TZ不良走势")
    public Result getNgRate(
            String factory,String project,String color,
            @RequestParam String startDate, @RequestParam String endDate
    ) throws ParseException {

        Map<String,Object> resultMap = new HashMap<>();
        List<Object> timeList = new ArrayList<>();
        List<Object> ngRateList = new ArrayList<>();

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";

        QueryWrapper<DfTzDetailCheck> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(factory),"factory",factory)
                .eq(StringUtils.isNotEmpty(project),"project",project)
                .eq(StringUtils.isNotEmpty(color),"color", color)
                .between("check_time", startTime, endTime);

        List<Rate3> list = dfTzDetailCheckService.getNgRate(qw);
        if(list == null || list.size() == 0){
            resultMap.put("timeList",timeList);
            resultMap.put("ngRateList",ngRateList);
            return new Result(500, "当前条件下没有相关TZ数据",resultMap);
        }

        for(Rate3 rate : list){
            timeList.add(rate.getStr1());
            ngRateList.add(rate.getDou1());
        }
        resultMap.put("timeList",timeList);
        resultMap.put("ngRateList",ngRateList);

        return new Result(200, "获取TZ不良走势数据成功",resultMap);
    }

    @GetMapping("/getNgDetailRateTop10")
    @ApiOperation("TZ不良Top10")
    public Result getNgDetailRateTop10(
            String factory,String project,String color,
            @RequestParam String startDate, @RequestParam String endDate
    ) throws ParseException {
        String redisKey = "qms:TZTOP10:"+factory+"_"+project+"_"+color+"_"+startDate+"_"+endDate;

        if(redisUtils.hasKey(redisKey)){
            String resultMapStr = redisUtils.get(redisKey).toString();
            Object resultMap = JSONObject.parseObject(resultMapStr);
            return new Result(200, "获取TZ不良Top10数据成功", resultMap);

        }

        Map<String,Object> resultMap = new HashMap<>();
        List<Object> defectList = new ArrayList<>();
        List<Object> ngRateList = new ArrayList<>();

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";

        QueryWrapper<DfTzDetailCheck> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(factory),"dtd.factory",factory)
                .eq(StringUtils.isNotEmpty(project),"dtd.project",project)
                .eq(StringUtils.isNotEmpty(color), "dtd.color", color)
                .between("dtd.check_time", startTime, endTime);

        List<Rate3> list = dfTzDetailCheckService.getNgDetailRateTop10(qw);
        if(list == null || list.size() == 0){
            resultMap.put("defectList",defectList);
            resultMap.put("ngRateList",ngRateList);
            return new Result(500, "当前条件下没有相关TZ数据",resultMap);
        }

        for(Rate3 rate : list){
            defectList.add(rate.getStr1());
            ngRateList.add(rate.getDou1());
        }
        resultMap.put("defectList",defectList);
        resultMap.put("ngRateList",ngRateList);

        redisUtils.set(redisKey, JSON.toJSONString(resultMap),60 * 60 * 24 * 30);

        return new Result(200, "获取TZ不良走势数据成功",resultMap);
    }

    @GetMapping("/DefectDataAnalysis")
    @ApiOperation("TZ不良项数据分析")
    public Result DefectDataAnalysis(
            @RequestParam String factory,
            @RequestParam String project,
            @RequestParam String color,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String defectName
    ) throws ParseException {

        String redisKey = "qms:TZDefectDataAnalysis:"+factory+"_"+project+"_"+color+"_"+startDate+"_"+endDate+"_"+defectName;

        if(redisUtils.hasKey(redisKey)){
            String resultMapStr = redisUtils.get(redisKey).toString();
            Object[][] resultArray = JSONObject.parseObject(resultMapStr,new TypeReference<Object[][]>(){});
            return new Result(200, "获取TZ不良Top10数据成功", resultArray);

        }

        Map<String,Map<String,Double>> suggestCNC2Map = new HashMap<>();
        suggestCNC2Map.put("外形长1", new HashMap<String,Double>(){{put("USL", 158.816);put("LSL", 158.756);}});
        suggestCNC2Map.put("外形长2", new HashMap<String,Double>(){{put("USL", 158.797);put("LSL", 158.737);}});
        suggestCNC2Map.put("外形长3", new HashMap<String,Double>(){{put("USL", 158.809);put("LSL", 158.732);}});
        suggestCNC2Map.put("外形宽1", new HashMap<String,Double>(){{put("USL", 75.806);put("LSL", 75.746);}});
        suggestCNC2Map.put("外形宽2", new HashMap<String,Double>(){{put("USL", 75.783);put("LSL", 75.723);}});
        suggestCNC2Map.put("外形宽3", new HashMap<String,Double>(){{put("USL", 75.783);put("LSL", 75.723);}});

        Map<String,Map<String,Double>> suggestSPMMap = new HashMap<>();
        suggestSPMMap.put("外形长1", new HashMap<String,Double>(){{put("USL", 158.789);put("LSL", 158.729);}});
        suggestSPMMap.put("外形长2", new HashMap<String,Double>(){{put("USL", 158.77);put("LSL", 158.71);}});
        suggestSPMMap.put("外形长3", new HashMap<String,Double>(){{put("USL", 158.765);put("LSL", 158.705);}});
        suggestSPMMap.put("外形宽1", new HashMap<String,Double>(){{put("USL", 75.769);put("LSL", 75.709);}});
        suggestSPMMap.put("外形宽2", new HashMap<String,Double>(){{put("USL", 75.746);put("LSL", 75.686);}});
        suggestSPMMap.put("外形宽3", new HashMap<String,Double>(){{put("USL", 75.746);put("LSL", 75.686);}});

        if (!suggestCNC2Map.containsKey(defectName) || !suggestSPMMap.containsKey(defectName)){
            return new Result(500, "当前条件没有相关不良项的建议控制限",null);
        }

        Map<String,Double> suggestCNC2Data = suggestCNC2Map.get(defectName);
        Map<String,Double> suggestSPMData = suggestSPMMap.get(defectName);

        Map<String,Integer> processIndexMap = new HashMap<>();
        processIndexMap.put("CNC2",2);
        processIndexMap.put("SPM",3);
        processIndexMap.put("LEAD",4);
        processIndexMap.put("TZ",5);

        Object[][] resultArray = new Object[11][6];
        //表头
        resultArray[0] = new Object[]{defectName,"控制限","CNC2","SPM","LEAD","TZ"};
        //QCP上限
        resultArray[1] = new Object[]{"QCP规格线","USL","/","/","/","/"};
        //QCP下限
        resultArray[2] = new Object[]{"QCP规格线","LSL","/","/","/","/"};
        //实际控制上限
        resultArray[3] = new Object[]{"实际控制限","UCL","/","/","/","/"};
        //实际控制下限
        resultArray[4] = new Object[]{"实际控制限","LCL","/","/","/","/"};
        //建议控制上限
        resultArray[5] = new Object[]{"建议控制限","UCL",suggestCNC2Data.get("USL"),suggestSPMData.get("USL"),"/","/"};
        //建议控制下限
        resultArray[6] = new Object[]{"建议控制限","LCL",suggestCNC2Data.get("LSL"),suggestSPMData.get("LSL"),"/","/"};
        //实际CPK
        resultArray[7] = new Object[]{"CPK","实际","/","/","/","/"};
        //建议CPK
        resultArray[8] = new Object[]{"CPK","建议","/","/","/","/"};
        //正太分布
        resultArray[9] = new Object[]{"正太分布","/","/","/","/","/"};
        //异常机台
        resultArray[10] = new Object[]{"异常机台","/","/","/","/","/"};

        //TZ时间
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";
        //LEAD时间
        String startTimeLead = TimeUtil.getOffsetDate(startTime,-2,"yyyy-MM-dd HH:mm:ss");
        String endTimeLead = TimeUtil.getOffsetDate(endTime,-2,"yyyy-MM-dd HH:mm:ss");
        //SPM时间
        String startTimeSpm = TimeUtil.getOffsetDate(startTimeLead,-2,"yyyy-MM-dd HH:mm:ss");
        String endTimeSpm = TimeUtil.getOffsetDate(endTimeLead,-2,"yyyy-MM-dd HH:mm:ss");
        //CNC2时间
        String startTimeCnc2 = TimeUtil.getOffsetDate(startTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");
        String endTimeCnc2 = TimeUtil.getOffsetDate(endTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");

        //LEAD不良名
        String leadDefectName = "";
        //TZ不良名
        String tzDefectName = "";

        QueryWrapper<DfSizeContRelation> relationQw = new QueryWrapper<>();
        relationQw
                .eq("factory",factory)
                .eq("project",project)
                .eq("color",color)
                .eq("ipqc_name",defectName);
        List<DfSizeContRelation> relationList = dfSizeContRelationService.list(relationQw);
        if (relationList == null || relationList.size() == 0){
            return new Result(500, "当前条件下没有相关QCP数据");
        }
        for (DfSizeContRelation relationItem : relationList) {
            if (!processIndexMap.containsKey(relationItem.getProcess())){
                continue;
            }
            Integer processIndex = processIndexMap.get(relationItem.getProcess());
            resultArray[1][processIndex] = relationItem.getQcpUsl();
            resultArray[2][processIndex] = relationItem.getQcpLsl();

            if ("CNC2".equals(relationItem.getProcess())||"SPM".equals(relationItem.getProcess())){
                resultArray[3][processIndex] = relationItem.getQcpUsl();
                resultArray[4][processIndex] = relationItem.getQcpLsl();
            }

            if("LEAD".equals(relationItem.getProcess())){
                leadDefectName = relationItem.getName();
            }
            if("TZ".equals(relationItem.getProcess())){
                tzDefectName = relationItem.getName();
            }
        }

        resultArray[0][0] = tzDefectName+" "+defectName;

//        QueryWrapper<DfSizeContStand> sizeStandQw = new QueryWrapper<>();
//        sizeStandQw
//                .eq("project",project)
//                .eq("color",color)
//                .eq("test_item",defectName);
//
//        List<DfSizeContStand> sizeStandList = dfSizeContStandService.list(sizeStandQw);
//        if (sizeStandList == null || sizeStandList.size() == 0){
//            return new Result(500, "当前条件下没有相关实际控制限和建议控制限数据");
//        }
//        for (DfSizeContStand sizeStandItem : sizeStandList) {
//            if (!processIndexMap.containsKey(sizeStandItem.getProcess())){
//                continue;
//            }
//            Integer processIndex = processIndexMap.get(sizeStandItem.getProcess());
//            resultArray[3][processIndex] = sizeStandItem.getUpperLimit();
//            resultArray[4][processIndex] = sizeStandItem.getLowerLimit();
//        }

        //CNC2数据
        QueryWrapper<DfSizeDetailCheck> cnc2Qw = new QueryWrapper<>();
        cnc2Qw
                .eq("dsd.process","CNC2")
                .eq("dsd.factory",factory)
                .eq("dsd.project",project)
                .eq("dsd.item_name",color)
                .eq("dscii.item_name",defectName)
                .between("dsd.test_time",startTimeCnc2,endTimeCnc2);
        List<Map<String,Object>> cnc2DataList = dfSizeCheckItemInfosCheckService.getDetailData(cnc2Qw);
        if (cnc2DataList != null && cnc2DataList.size() != 0){
            Map<String, Object> itemData = new HashMap<>();

            Map<String,String> machineMap = new HashMap<>();
            List<String> machineList = new ArrayList<>();
            List<Double> checkValueList = new ArrayList<>();
//            Double usl = Double.valueOf(cnc2DataList.get(0).get("usl").toString());
//            Double lsl = Double.valueOf(cnc2DataList.get(0).get("lsl").toString());
//            Double standardValue = Double.valueOf(cnc2DataList.get(0).get("check_value").toString());
            Double usl = (Double) resultArray[1][2];
            Double lsl = (Double) resultArray[2][2];
            Double standardValue = (usl + lsl) / 2;
            Integer okNum = 0;
            Integer total = cnc2DataList.size();
            for (Map<String, Object> item : cnc2DataList){
                String machineName = item.get("machine_code").toString();
                Double ngRate = Double.valueOf(item.get("ngRate").toString());
                if (!machineMap.containsKey(item.get("machine_code")) && ngRate > 0){
                    machineMap.put(machineName,machineName +" "+ ngRate.toString()+"%");
                    machineList.add(machineName +" "+ ngRate.toString()+"%");
                }
                if("OK".equals(item.get("check_result"))){
                    okNum++;
                }
                checkValueList.add(Double.valueOf(item.get("check_value").toString()));
            }
            //平均值
            Double mean = MathUtils.calculateMean(checkValueList);
            //方差
            Double variance = MathUtils.calculateVariance(checkValueList, mean);
            //标准差
            Double stdDev = MathUtils.calculateStandardDeviation(variance);
            //CPK
            Double actualCpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
            //CPK
            Double suggestCpk = MathUtils.calculateCPK(suggestCNC2Data.get("USL"), suggestCNC2Data.get("LSL"), mean, stdDev);
            //良率
            Double okRate = okNum.doubleValue() / total;
            itemData.put("name", defectName);
            itemData.put("standard", standardValue);
            itemData.put("usl", usl);
            itemData.put("lsl", lsl);

            itemData.put("okRate", String.format("%.2f", okRate * 100));
            itemData.put("total", total);
            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);

            resultArray[7][2] = actualCpk;
            resultArray[8][2] = suggestCpk;
            resultArray[9][2] = itemData;
            resultArray[10][2] = machineList.size()>0?machineList:"/";
        }

        //SPM数据
        QueryWrapper<DfSizeDetailCheck> spmQw = new QueryWrapper<>();
        spmQw
                .eq("dsd.process","SPM")
                .eq("dsd.factory",factory)
                .eq("dsd.project",project)
                .eq("dsd.item_name",color)
                .eq("dscii.item_name",defectName)
                .between("dsd.test_time",startTimeSpm,endTimeSpm);
        List<Map<String,Object>> spmDataList = dfSizeCheckItemInfosCheckService.getDetailData(spmQw);
        if (spmDataList != null && spmDataList.size() != 0){
            Map<String, Object> itemData = new HashMap<>();

            Map<String,String> machineMap = new HashMap<>();
            List<String> machineList = new ArrayList<>();
            List<Double> checkValueList = new ArrayList<>();
//            Double usl = Double.valueOf(spmDataList.get(0).get("usl").toString());
//            Double lsl = Double.valueOf(spmDataList.get(0).get("lsl").toString());
//            Double standardValue = Double.valueOf(spmDataList.get(0).get("check_value").toString());
            Double usl = (Double) resultArray[1][3];
            Double lsl = (Double) resultArray[2][3];
            Double standardValue = (usl + lsl) / 2;
            Integer okNum = 0;
            Integer total = spmDataList.size();
            for (Map<String, Object> item : spmDataList){
                String machineName = item.get("machine_code").toString();
                Double ngRate = Double.valueOf(item.get("ngRate").toString());
                if (!machineMap.containsKey(item.get("machine_code")) && ngRate > 0){
                    machineMap.put(machineName,machineName +" "+ ngRate.toString()+"%");
                    machineList.add(machineName +" "+ ngRate.toString()+"%");
                }
                if("OK".equals(item.get("check_result"))){
                    okNum++;
                }
                checkValueList.add(Double.valueOf(item.get("check_value").toString()));
            }
            //平均值
            Double mean = MathUtils.calculateMean(checkValueList);
            //方差
            Double variance = MathUtils.calculateVariance(checkValueList, mean);
            //标准差
            Double stdDev = MathUtils.calculateStandardDeviation(variance);
            //CPK
            Double actualCpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
            //CPK
            Double suggestCpk = MathUtils.calculateCPK(suggestSPMData.get("USL"), suggestSPMData.get("LSL"), mean, stdDev);
            //良率
            Double okRate = okNum.doubleValue() / total;
            itemData.put("name", defectName);
            itemData.put("standard", standardValue);
            itemData.put("usl", usl);
            itemData.put("lsl", lsl);

            itemData.put("okRate", String.format("%.2f", okRate * 100));
            itemData.put("total", total);
            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);

            resultArray[7][3] = actualCpk;
            resultArray[8][3] = suggestCpk;
            resultArray[9][3] = itemData;
            resultArray[10][3] = machineList.size()>0?machineList:"/";
        }

        //LEAD数据
        QueryWrapper<DfLeadCheckItemInfosCheck> leadQw = new QueryWrapper<>();
        leadQw
                .eq("dld.factory",factory)
                .eq("dld.project",project)
                .eq("dld.color",color)
                .eq("dlcii.item_name",leadDefectName)
                .between("dld.check_time",startTimeLead,endTimeLead);
        List<DfLeadCheckItemInfosCheck> leadDataList = dfLeadCheckItemInfosCheckService.getDetailData(leadQw);
        if (leadDataList != null && leadDataList.size() != 0){
            Map<String, Object> itemData = new HashMap<>();

            List<Double> checkValueList = new ArrayList<>();
//            Double usl = leadDataList.get(0).getUsl();
//            Double lsl = leadDataList.get(0).getLsl();
//            Double standardValue = leadDataList.get(0).getStandardValue();
            Double usl = (Double) resultArray[1][4];
            Double lsl = (Double) resultArray[2][4];
            Double standardValue = (usl + lsl) / 2;
            Integer okNum = 0;
            Integer total = leadDataList.size();
            for (DfLeadCheckItemInfosCheck item : leadDataList){
                if("OK".equals(item.getCheckResult())){
                    okNum++;
                }
                checkValueList.add(item.getCheckValue());
            }
            //平均值
            Double mean = MathUtils.calculateMean(checkValueList);
            //方差
            Double variance = MathUtils.calculateVariance(checkValueList, mean);
            //标准差
            Double stdDev = MathUtils.calculateStandardDeviation(variance);
            //CPK
            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
            //良率
            Double okRate = okNum.doubleValue() / total;
            itemData.put("name", defectName);
            itemData.put("standard", standardValue);
            itemData.put("usl", usl);
            itemData.put("lsl", lsl);

            itemData.put("okRate", String.format("%.2f", okRate * 100));
            itemData.put("total", total);
            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);

            resultArray[7][4] = cpk;
            resultArray[9][4] = itemData;
        }

        //TZ数据
        QueryWrapper<DfTzCheckItemInfosCheck> tzQw = new QueryWrapper<>();
        tzQw
                .eq("dtd.factory",factory)
                .eq("dtd.project",project)
                .eq("dtd.color",color)
                .eq("dtcii.item_name",tzDefectName)
                .between("dtd.check_time",startTime,endTime);
        List<DfTzCheckItemInfosCheck> tzDataList = dfTzCheckItemInfosCheckService.getDetailData(tzQw);
        if (tzDataList != null && tzDataList.size() != 0){
            Map<String, Object> itemData = new HashMap<>();

            List<Double> checkValueList = new ArrayList<>();
//            Double usl = tzDataList.get(0).getUsl();
//            Double lsl = tzDataList.get(0).getLsl();
//            Double standardValue = tzDataList.get(0).getStandardValue();
            Double usl = (Double) resultArray[1][5];
            Double lsl = (Double) resultArray[2][5];
            Double standardValue = (usl + lsl) / 2;
            Integer okNum = 0;
            Integer total = tzDataList.size();
            for (DfTzCheckItemInfosCheck item : tzDataList){
                if("OK".equals(item.getCheckResult())){
                    okNum++;
                }
                checkValueList.add(item.getCheckValue());
            }
            //平均值
            Double mean = MathUtils.calculateMean(checkValueList);
            //方差
            Double variance = MathUtils.calculateVariance(checkValueList, mean);
            //标准差
            Double stdDev = MathUtils.calculateStandardDeviation(variance);
            //CPK
            Double cpk = MathUtils.calculateCPK(usl, lsl, mean, stdDev);
            //良率
            Double okRate = okNum.doubleValue() / total;
            itemData.put("name", defectName);
            itemData.put("standard", standardValue);
            itemData.put("usl", usl);
            itemData.put("lsl", lsl);

            itemData.put("okRate", String.format("%.2f", okRate * 100));
            itemData.put("total", total);
            MathUtils.normalDistribution(checkValueList,mean,stdDev, itemData);

            resultArray[7][5] = cpk;
            resultArray[9][5] = itemData;
        }

        redisUtils.set(redisKey, JSON.toJSONString(resultArray),60 * 60 * 24 * 30);

        return new Result(200, "获取数据分析成功",resultArray);
    }

    @GetMapping("/getFACAData")
    @ApiOperation("获取FACA诊断")
    public Result getFACAData(
            @RequestParam String factory,
            @RequestParam String project,
            @RequestParam String color,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String defectName
    ) throws ParseException {

        Map<String,Object> resultMap = new HashMap<>();

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        String startTime = startDate + " 00:00:00";
//        String endTime = endDate + " 23:59:59";

//        //CNC2时间
//        String startTimeCnc2 = TimeUtil.getOffsetDate(startTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");
//        String endTimeCnc2 = TimeUtil.getOffsetDate(endTimeSpm,-1,"yyyy-MM-dd HH:mm:ss");

        QueryWrapper<DfSizeDetailCheck> qw = new QueryWrapper<>();
        qw
                .eq("dsd.process","CNC2")
                .eq("dsd.factory",factory)
                .eq("dsd.project",project)
                .eq("dsd.item_name",color)
                .eq("dscii.item_name",defectName)
                .eq("dscii.check_result","NG")
                .between("dsd.test_time",startTime,endTime)
                .orderByDesc("dscii.check_time");

        List<DfSizeCheckItemInfosCheck> list = dfSizeCheckItemInfosCheckService.getSizeCheckItemInfosList(qw);
        if(list == null || list.size() == 0){
            return new Result(500, "当前条件下没有相关FACA诊断数据",null);
        }

        return new Result(200, "获取FACA诊断数据成功",list.get(0));
    }
}
