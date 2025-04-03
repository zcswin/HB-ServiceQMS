package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfPrintAoiCheckService;
import com.ww.boengongye.service.DfTemporaryDataService;
import com.ww.boengongye.utils.NormalDistributionUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 移印AOI检测 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Controller
@RequestMapping("/dfPrintAoiCheck")
@CrossOrigin
@ResponseBody
@Api(tags = "移印AOI检测")
public class DfPrintAoiCheckController {

    @Autowired
    private DfPrintAoiCheckService dfPrintAoiCheckService;

    @Autowired
    private DfTemporaryDataService dfTemporaryDataService;


    @PostMapping("/importData")
    @ApiOperation("导入数据")
    public Result importData(MultipartFile file) throws Exception {
        int i = dfPrintAoiCheckService.importExcel(file);
        return new Result(200, "成功添加" + i + "条数据");
    }


    @RequestMapping(value = "getPassPointList",method = RequestMethod.GET)
    @ApiOperation("良率直方图")
    public Result getPassPointList(
            String factory,String process,String lineBody,String project,
            String startDate, String endDate
    ) throws ParseException {


        Map<String,Object> map = new HashMap<>();
        //线体集合
        List<String> lineBodyList = new ArrayList<>();
        //一次良率集合
        List<String> onePassPointList = new ArrayList<>();
        //尺寸良率集合
        List<String> dimensionPassPointList = new ArrayList<>();
        //外观良率集合
        List<String> cosmeticPassPointList = new ArrayList<>();
        //最终良率集合
        List<String> finalPassPointList = new ArrayList<>();

        QueryWrapper<DfPrintAoiCheck> checkWrapper = new QueryWrapper<>();
        QueryWrapper<DfPrintAoiCheck> checkWrapper2 = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            checkWrapper.eq("dpac.factory",factory);
            checkWrapper2.apply("dpar.factory='"+factory+"'");
        }
        if (StringUtils.isNotEmpty(lineBody)){
            checkWrapper.eq("dpac.line_body",lineBody);
            checkWrapper2.apply("dpar.line_body='"+lineBody+"'");
        }
        if (StringUtils.isNotEmpty(project)){
            checkWrapper.eq("dpac.project",project);
            checkWrapper2.apply("dpar.project='"+project+"'");
        }
        if (StringUtils.isNotEmpty(startDate)){
            startDate = startDate + " 07:00:00";
            checkWrapper.ge("dpac.check_time",startDate);
            checkWrapper2.apply("dpar.check_time>='"+startDate+"'");
        }
        if (StringUtils.isNotEmpty(endDate)){
            endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
            checkWrapper.le("dpac.check_time",endDate);
            checkWrapper2.apply("dpar.check_time<='"+endDate+"'");
        }

        List<DfPrintAoiCheck> checkList = dfPrintAoiCheckService.getPassPointList(checkWrapper,checkWrapper2);
        if (checkList==null||checkList.size()==0){
            return new Result(500,"该条件下没有相关的良率数据");
        }
        for (DfPrintAoiCheck dfPrintAoiCheck:checkList){
            lineBodyList.add(dfPrintAoiCheck.getLineBody());
            onePassPointList.add(dfPrintAoiCheck.getOnePassPoint());
            dimensionPassPointList.add(dfPrintAoiCheck.getDimensionPassPoint());
            cosmeticPassPointList.add(dfPrintAoiCheck.getCosmeticPassPoint());
            finalPassPointList.add(dfPrintAoiCheck.getFinalPassPoint());
        }

        map.put("lineBodyListX",lineBodyList);
        map.put("onePassPointListY",onePassPointList);
        map.put("dimensionPassPointListY",dimensionPassPointList);
        map.put("cosmeticPassPointListY",cosmeticPassPointList);
        map.put("finalPassPointListY",finalPassPointList);

        return new Result(200,"获取良率直方图统计成功",map);
    }


//    @RequestMapping(value = "getHolePassAndDefectPoint",method = RequestMethod.GET)
//    @ApiOperation("平台仿型图+各孔最高比例")
//    public Result getHolePassAndDefectPoint(
//            String factory,String process,String lineBody,String project,
//            String startDate, String endDate
//    ){
//        Map<String,Object> map = new HashMap<>();
//
//        Map<String,Object> mapAV = new HashMap<>();
//
//        QueryWrapper<DfPrintAoiCheck> checkWrapper = new QueryWrapper<>();
//
//        if (StringUtils.isNotEmpty(factory)){
//            checkWrapper.eq("dpac.factory",factory);
//        }
//        if (StringUtils.isNotEmpty(lineBody)){
//            checkWrapper.eq("dpac.line_body",lineBody);
//        }
//        if (StringUtils.isNotEmpty(project)){
//            checkWrapper.eq("dpac.project",project);
//        }
//        if (StringUtils.isNotEmpty(startDate)){
//            checkWrapper.ge("dpac.check_time",startDate);
//        }
//        if (StringUtils.isNotEmpty(endDate)){
//            checkWrapper.le("dpac.check_time",endDate);
//        }
//
//        //所有孔的良率
//        List<Rate3> holeList = dfPrintAoiCheckService.getHolePossPoint(checkWrapper);
//        if (holeList==null||holeList.size()==0){
//            return new Result(500,"该条件没有平台仿型图+各孔最高比例相关数据");
//        }
//        for (Rate3 hole:holeList){
//            Map<String,Object> holeMap = new HashMap<>();
//
//            //不良名top3
//            List<Object> defectNameList = new ArrayList<>();
//
//            //不良率top3
//            List<Object> defectPointList = new ArrayList<>();
//
//            QueryWrapper<DfPrintAoiCheckDetail> checkDetailWrapper = new QueryWrapper<>();
//            checkDetailWrapper
//                    .apply("dpacd.check_type = 1")
//                    .apply("substring_index(dpacd.check_name,'_',-1) = '"+hole.getStr1()+"'");
//
//            //当前孔的不良top3
//            List<Rate3> holeDefectTop3List = dfPrintAoiCheckService.getHoleDefectPointTop3(checkWrapper,checkDetailWrapper);
//            for (Rate3 defect:holeDefectTop3List){
//                defectNameList.add(defect.getStr1());
//                defectPointList.add(defect.getDou1());
//            }
//
//            holeMap.put("passPoint",hole.getDou1());
//            holeMap.put("defectNameList",defectNameList);
//            holeMap.put("defectPointList",defectPointList);
//            map.put(hole.getStr1(),holeMap);
//        }
//
//        return new Result(200,"获取平台仿型图+各孔最高比例数据成功",map);
//    }

    @RequestMapping(value = "getHolePassAndDefectPoint",method = RequestMethod.GET)
    @ApiOperation("平台仿型图+各孔最高比例")
    public Result getHolePassAndDefectPoint(
            String factory,String process,String lineBody,String project,
            @RequestParam String startDate,@RequestParam String endDate
    ) throws ParseException {

        QueryWrapper<DfTemporaryData> dataWrapper = new QueryWrapper<>();
        dataWrapper
                .eq("`method`","getHolePassAndDefectPoint")
                .eq("start_time",startDate)
                .eq("end_time",endDate);
        List<DfTemporaryData> list = dfTemporaryDataService.list(dataWrapper);
        if (list!=null&&list.size()!=0){
            Map<String,Object> map = new HashMap<>();
            Map<String,List<DfTemporaryData>> holeMap = list.stream()
                    .collect(Collectors.groupingBy(data ->data.getStr1()));

            for (Map.Entry<String, List<DfTemporaryData>> entry : holeMap.entrySet()){
                Map<String,Object> oneHoleMap = new HashMap<>();

                String hole = entry.getKey();
                List<DfTemporaryData> defectTop3List = entry.getValue();

                oneHoleMap.put("passPoint",defectTop3List.get(0).getDou1());
                oneHoleMap.put("defectTop3List",defectTop3List);
                map.put(hole,oneHoleMap);
            }
            return new Result(200,"获取平台仿型图+各孔最高比例数据成功",map);
        }



        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        Map<String,Map<String,Object>> map = new HashMap<>();

        QueryWrapper<DfPrintAoiCheck> checkWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            checkWrapper.eq("dpac.factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            checkWrapper.eq("dpac.line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(project)){
            checkWrapper.eq("dpac.project",project);
        }
        if (StringUtils.isNotEmpty(startDate)){
            checkWrapper.ge("dpac.check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endDate)){
            checkWrapper.le("dpac.check_time",endTime);
        }

        //所有孔的良率
        List<Rate3> holeList = dfPrintAoiCheckService.getHolePossPoint(checkWrapper);
        if (holeList==null||holeList.size()==0){
            return new Result(200,"该条件没有平台仿型图+各孔最高比例相关数据");
        }
        for (Rate3 holePass:holeList){
            Map<String,Object> oneHoleMap = new HashMap<>();
            oneHoleMap.put("passPoint",holePass.getDou1());
            map.put(holePass.getStr1(),oneHoleMap);
        }


        //获取所有孔的不良Top3
        List<Rate3> allHoleDefectTop3List = dfPrintAoiCheckService.getAllHoleDefectPointTop3(checkWrapper);
        Map<String,List<Rate3>> holeMap = allHoleDefectTop3List.stream()
                .collect(Collectors.groupingBy(rate3 ->rate3.getStr1()));
        for (Map.Entry<String, List<Rate3>> entry : holeMap.entrySet()){
            String hole = entry.getKey();
            Map<String,Object> oneHoleMap = map.get(hole);

            List<Rate3> defectTop3List = entry.getValue();

//            //录入临时表
//            for (Rate3 rate3 :defectTop3List){
//                DfTemporaryData dfTemporaryData = new DfTemporaryData();
//                dfTemporaryData.setMethod("getHolePassAndDefectPoint");
//                dfTemporaryData.setName(hole);
//                dfTemporaryData.setDou1(oneHoleMap.get("passPoint").toString());
//                dfTemporaryData.setStr1(hole);
//                dfTemporaryData.setStr2(rate3.getStr2());
//                dfTemporaryData.setDou2(rate3.getDou2().toString());
//                dfTemporaryData.setStartTime(startDate);
//                dfTemporaryData.setEndTime(endDate);
//                dfTemporaryDataService.save(dfTemporaryData);
//            }

            oneHoleMap.put("defectTop3List",defectTop3List);
            map.put(hole,oneHoleMap);
        }

        return new Result(200,"获取平台仿型图+各孔最高比例数据成功",map);
    }


    @RequestMapping(value = "getDimensionPointList",method = RequestMethod.GET)
    @ApiOperation("尺寸NG占比与分布")
    public Result getDimensionPointList(
            String factory,String process,String lineBody,String project,
            @RequestParam String startDate,@RequestParam String endDate
    ) throws ParseException {

        QueryWrapper<DfTemporaryData> dataWrapper = new QueryWrapper<>();
        dataWrapper
                .eq("`method`","getDimensionPointList")
                .eq("start_time",startDate)
                .eq("end_time",endDate);
        List<DfTemporaryData> list = dfTemporaryDataService.list(dataWrapper);

        if (list!=null&&list.size()!=0){
            List<Object> dimensionList = new ArrayList<>();
            List<Object> dimensionNgPointList = new ArrayList<>();
            LinkedHashSet<Object> lineBodylist = new LinkedHashSet<>();
            List<Object> dimensionPassPointList = new ArrayList<>();
            for (DfTemporaryData data:list){
                dimensionList.add(data.getStr2());
                dimensionNgPointList.add(data.getDou2());
                lineBodylist.add(data.getStr1());
                dimensionPassPointList.add(data.getDou1());
            }
            Map<String,Object> map = new HashMap<>();
            map.put("dimensionList",dimensionList);
            map.put("dimensionNgPointList",dimensionNgPointList);
            map.put("lineBodylist",lineBodylist);
            map.put("dimensionPassPointList",dimensionPassPointList);
            return new Result(200,"获取尺寸NG占比与分布数据成功",map);
        }


        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        Map<String,Object> map = new HashMap<>();

        //尺寸名
        String[] dimensionList = new String[]{"溢墨宽度","溢墨厚度","油墨深度"};

        //尺寸NG总数
        Integer dimensionNgTotal = 0;

        //尺寸NG数
        List<Integer> dimensionNgNumberList = new ArrayList<>();

        //尺寸NG率
        List<Object> dimensionNgPointList = new ArrayList<>();

        //线体
        LinkedHashSet<Object> lineBodylist = new LinkedHashSet<>();

        //尺寸良率
        List<List<Object>> dimensionPassPointList = new ArrayList<>();

        for (String dimension:dimensionList){
            //该尺寸在各个线体的良率
            List<Object> passPointList = new ArrayList<>();

            QueryWrapper<DfPrintAoiCheck> checkWrapper = new QueryWrapper<>();
            QueryWrapper<DfPrintAoiCheckDetail> checkDetailWrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(factory)){
                checkWrapper.eq("dpac.factory",factory);
            }
            if (StringUtils.isNotEmpty(lineBody)){
                checkWrapper.eq("dpac.line_body",lineBody);
            }
            if (StringUtils.isNotEmpty(project)){
                checkWrapper.eq("dpac.project",project);
            }
            if (StringUtils.isNotEmpty(startDate)){
                checkWrapper.ge("dpac.check_time",startTime);
            }
            if (StringUtils.isNotEmpty(endDate)){
                checkWrapper.le("dpac.check_time",endTime);
            }


            checkDetailWrapper
                    .apply("dpacd.check_type = 2")
                    .apply("dpacd.item_name like '%"+dimension+"%'");

            //该尺寸的NG数以及NG率
            List<Rate3> oneDimensionNgPointList = dfPrintAoiCheckService.getNgPointList(checkWrapper,checkDetailWrapper);
            if (oneDimensionNgPointList==null||oneDimensionNgPointList.size()==0){
                return new Result(200,"该条件下没有尺寸NG占比与分布相关数据");
            }

            //该尺寸的NG数
            Integer dimensionNgNumber = 0;
            for (Rate3 oneDimension:oneDimensionNgPointList){
                dimensionNgTotal+=oneDimension.getInte1();
                dimensionNgNumber+=oneDimension.getInte1();
            }
            dimensionNgNumberList.add(dimensionNgNumber);

            //该尺寸在各个线体中的良率
            List<Rate3> oneDimensionPassPointList = dfPrintAoiCheckService.getOnePassPointList(checkWrapper,checkDetailWrapper);
            if (oneDimensionPassPointList==null||oneDimensionPassPointList.size()==0){
                return new Result(500,"该条件下没有尺寸NG占比与分布相关数据");
            }
            for (Rate3 oneDimension:oneDimensionPassPointList){
                lineBodylist.add(oneDimension.getStr1());
                passPointList.add(oneDimension.getDou2());
            }

            dimensionPassPointList.add(passPointList);
        }

        for (Integer dimensionNgNumber:dimensionNgNumberList){
            String ngPoint = String.format("%.2f", (float) dimensionNgNumber / (float) dimensionNgTotal * 100);
            dimensionNgPointList.add(ngPoint);
        }


//        //录入临时数据
//        for(int i = 0;i<dimensionList.length;i++){
//            DfTemporaryData dfTemporaryData = new DfTemporaryData();
//            dfTemporaryData.setMethod("getDimensionPointList");
//            dfTemporaryData.setName(dimensionList[i]);
//            dfTemporaryData.setStr1("Line-23");
//            dfTemporaryData.setStr2(dimensionList[i]);
//            dfTemporaryData.setDou1(dimensionPassPointList.get(i).get(0).toString());
//            dfTemporaryData.setDou2(dimensionNgPointList.get(i).toString());
//            dfTemporaryData.setStartTime(startDate);
//            dfTemporaryData.setEndTime(endDate);
//            dfTemporaryDataService.save(dfTemporaryData);
//        }

        map.put("dimensionList",dimensionList);
        map.put("dimensionNgPointList",dimensionNgPointList);
        map.put("lineBodylist",lineBodylist);
        map.put("dimensionPassPointList",dimensionPassPointList);

        return new Result(200,"获取尺寸NG占比与分布数据成功",map);
    }

    @RequestMapping(value = "getCosmeticPointList",method = RequestMethod.GET)
    @ApiOperation("外观NG占比与分布")
    public Result getCosmeticPointList(
            String factory,String process,String lineBody,String project,
            @RequestParam String startDate,@RequestParam String endDate
    ) throws ParseException {

        QueryWrapper<DfTemporaryData> dataWrapper = new QueryWrapper<>();
        dataWrapper
                .eq("`method`","getCosmeticPointList")
                .eq("start_time",startDate)
                .eq("end_time",endDate);
        List<DfTemporaryData> list = dfTemporaryDataService.list(dataWrapper);
        if (list!=null&&list.size()!=0) {
            List<Object> cosmeticList = new ArrayList<>();
            List<Object> cosmeticNgPointList = new ArrayList<>();
            LinkedHashSet<Object> lineBodylist = new LinkedHashSet<>();
            List<Object> cosmeticPassPointList = new ArrayList<>();
            for (DfTemporaryData data:list){
                cosmeticList.add(data.getStr2());
                cosmeticNgPointList.add(data.getDou2());
                lineBodylist.add(data.getStr1());
                cosmeticPassPointList.add(data.getDou1());
            }
            Map<String,Object> map = new HashMap<>();
            map.put("cosmeticList",cosmeticList);
            map.put("cosmeticNgPointList",cosmeticNgPointList);
            map.put("lineBodylist",lineBodylist);
            map.put("dimensionPassPointList",cosmeticPassPointList);
            return new Result(200,"获取外观NG占比与分布数据成功",map);
        }


        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        Map<String,Object> map = new HashMap<>();

        //外观名
        String[] cosmeticList = new String[]{"划伤","擦伤","毛丝","颗粒","厚度不均","缺墨","锯齿","平台溢墨","漏印"};

        //外观NG总数
        Integer cosmeticNgTotal = 0;

        //外观NG数
        List<Integer> cosmeticNgNumberList = new ArrayList<>();

        //外观NG率
        List<Object> cosmeticNgPointList = new ArrayList<>();

        //线体
        LinkedHashSet<Object> lineBodyList = new LinkedHashSet<>();

        //外观良率
        List<List<Object>> cosmeticPassPointList = new ArrayList<>();

        for (String cosmetic:cosmeticList){
            //该尺寸在各个线体的良率
            List<Object> passPointList = new ArrayList<>();

            QueryWrapper<DfPrintAoiCheck> checkWrapper = new QueryWrapper<>();
            QueryWrapper<DfPrintAoiCheckDetail> checkDetailWrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(factory)){
                checkWrapper.eq("dpac.factory",factory);
            }
            if (StringUtils.isNotEmpty(lineBody)){
                checkWrapper.eq("dpac.line_body",lineBody);
            }
            if (StringUtils.isNotEmpty(project)){
                checkWrapper.eq("dpac.project",project);
            }
            if (StringUtils.isNotEmpty(startDate)){
                checkWrapper.ge("dpac.check_time",startTime);
            }
            if (StringUtils.isNotEmpty(endDate)){
                checkWrapper.le("dpac.check_time",endTime);
            }

            checkDetailWrapper
                    .apply("dpacd.check_type = 1")
                    .apply("dpacd.item_name like '%"+cosmetic+"%'");

            //该外观的NG数以及NG率
            List<Rate3> oneCosmeticNgPointLsit = dfPrintAoiCheckService.getNgPointList(checkWrapper,checkDetailWrapper);
            if (oneCosmeticNgPointLsit==null||oneCosmeticNgPointLsit.size()==0){
                return new Result(200,"该条件下没有外观NG占比与分布相关数据");
            }

            //该外观的NG数
            Integer cosmeticNgNumber = 0;
            for (Rate3 oneCosmetic:oneCosmeticNgPointLsit){
                cosmeticNgTotal+=oneCosmetic.getInte1();
                cosmeticNgNumber+=oneCosmetic.getInte1();
            }
            cosmeticNgNumberList.add(cosmeticNgNumber);

            //该外观在各个线体中的良率
            List<Rate3> oneCosmeticPointList = dfPrintAoiCheckService.getOnePassPointList(checkWrapper,checkDetailWrapper);
            if (oneCosmeticPointList==null||oneCosmeticPointList.size()==0){
                return new Result(500,"该条件下没有外观NG占比与分布相关数据");
            }
            for (Rate3 oneCosmetic:oneCosmeticPointList){
                lineBodyList.add(oneCosmetic.getStr1());
                passPointList.add(oneCosmetic.getDou2());
            }
            cosmeticPassPointList.add(passPointList);
        }

        for (Integer cosmeticNgNumber:cosmeticNgNumberList){
            String ngPoint = String.format("%.2f", (float) cosmeticNgNumber / (float) cosmeticNgTotal * 100);
            cosmeticNgPointList.add(ngPoint);
        }

//        //录入临时数据
//        for (int i = 0;i<cosmeticList.length;i++){
//            DfTemporaryData dfTemporaryData = new DfTemporaryData();
//            dfTemporaryData.setMethod("getCosmeticPointList");
//            dfTemporaryData.setStr1("Line-23");
//            dfTemporaryData.setStr2(cosmeticList[i]);
//            dfTemporaryData.setDou1(cosmeticPassPointList.get(i).get(0).toString());
//            dfTemporaryData.setDou2(cosmeticNgPointList.get(i).toString());
//            dfTemporaryData.setStartTime(startDate);
//            dfTemporaryData.setEndTime(endDate);
//            dfTemporaryDataService.save(dfTemporaryData);
//        }

        map.put("cosmeticList",cosmeticList);
        map.put("cosmeticNgPointList",cosmeticNgPointList);
        map.put("lineBodyList",lineBodyList);
        map.put("cosmeticPassPointList",cosmeticPassPointList);

        return new Result(200,"获取外观NG占比与分布数据成功",map);
    }


    @GetMapping("/getItemNormalDistributionByItem")
    @ApiOperation("获取尺寸的正态分布图")
    public Result getItemNormalDistributionByItem(
            String factory,String processes,String lineBody,String project,
            String startDate, String endDate,String dimension
    ) throws ParseException {
        List<String> holeList = dfPrintAoiCheckService.getAllHoleList();
        if(holeList==null||holeList.size()==0){
            return new Result(500,"该尺寸没有正态分布图的相关数据");
        }
        QueryWrapper<DfPrintAoiCheckDetail> checkWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            checkWrapper.eq("dpac.factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            checkWrapper.eq("dpac.line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(project)){
            checkWrapper.eq("dpac.project",project);
        }
        if (StringUtils.isNotEmpty(startDate)){
            startDate = startDate + " 07:00:00";
            checkWrapper.ge("dpac.check_time",startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
            checkWrapper.le("dpac.check_time",endDate);
        }
        checkWrapper
                .eq("dpacd.check_type",2)
                .like("dpacd.item_name",dimension);

        List<DfPrintAoiCheckDetail> checkItemInfos = dfPrintAoiCheckService.listItemInfosJoinDetail(checkWrapper);
        Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
//        Map<String, Double> itemResStandardValue = new HashMap<>();
        Map<String, Double> itemResUsl = new HashMap<>();
        Map<String, Double> itemResLsl = new HashMap<>();
        Map<String, Integer> itemResOkNum = new HashMap<>();
        Map<String, Integer> itemResAllNum = new HashMap<>();

        for (DfPrintAoiCheckDetail checkItemInfo : checkItemInfos) {
            String process = checkItemInfo.getItemName();
            Double checkValue = checkItemInfo.getCheckValue();
            itemResAllNum.merge(process, 1, Integer::sum);
            if (checkItemInfo.getCheckResult().equals("OK")) itemResOkNum.merge(process, 1, Integer::sum);
            if (!itemResCheckValue.containsKey(process)) {
                List<Double> list = new ArrayList<>();
                list.add(checkValue);
                itemResCheckValue.put(process, list);
//                itemResStandardValue.put(process, checkItemInfo.getStandardValue());
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
            for (String hole:holeList){
                if (process.contains(hole)){
                    itemData.put("hole",hole);
                }
            }
//            itemData.put("standard", itemResStandardValue.get(process));
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


        List<Object> allHoleList = new ArrayList<>();
        Map<String, Object> itemData = new HashMap<>();
        Rate3 allHole = dfPrintAoiCheckService.getMinLslAndMaxUsl(checkWrapper);
        //汇总下限
        Double allLsl = allHole.getDou1();
        //汇总上限
        Double allUsl = allHole.getDou2();
        //汇总OK数
        Integer allOkNum = 0;
        //汇总数
        Integer allNum = 0;
        //汇总检测值
        List<Double> list = new ArrayList<>();

        for (DfPrintAoiCheckDetail checkItemInfo : checkItemInfos) {
            Double checkValue = checkItemInfo.getCheckValue();
            allNum++;
            if (checkItemInfo.getCheckResult().equals("OK")){
                allOkNum++;
            }
            list.add(checkValue);
        }
        itemData.put("lsl",allLsl);
        itemData.put("usl",allUsl);
        itemData.put("良率",allOkNum.doubleValue()/allNum);
        itemData.put("allNum",allNum);
        NormalDistributionUtil.normalDistribution2(NormalDistributionUtil.convertToDoubleArray(list.toArray()), itemData);
        allHoleList.add(itemData);

        Map<String,Object> map = new HashMap<>();
        map.put("allHoleList",allHoleList);
        map.put("holeList",result);

        return new Result(200, "查询成功", map);
    }
}
