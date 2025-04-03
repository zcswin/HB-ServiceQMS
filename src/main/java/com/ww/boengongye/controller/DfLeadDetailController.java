package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfInspectionEquipmentDefectService;
import com.ww.boengongye.service.DfLeadDetailService;
import com.ww.boengongye.service.DfYieldWarnService;
import com.ww.boengongye.utils.NormalDistributionUtil;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ww.boengongye.service.DfYieldWarnService;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Controller
@RequestMapping("/dfLeadDetail")
@CrossOrigin
@ResponseBody
@Api(tags = "Lead数据")
public class DfLeadDetailController {

    @Autowired
    private DfLeadDetailService dfLeadDetailService;

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private DfYieldWarnService dfYieldWarnService;

	@Autowired
	private DfInspectionEquipmentDefectService dfInspectionEquipmentDefectService;

    @PostMapping("/importData")
    @ApiOperation("导入数据")
    public Result importData(MultipartFile[] files) throws Exception {
        int i = 0;
        for (MultipartFile f : files) {
            System.out.println(f.getOriginalFilename());
            i = dfLeadDetailService.importExcel(f);

        }
        return new Result(200, "成功添加" + i + "条数据");
    }

    @GetMapping("/listLeadNum")
    @ApiOperation("产能数量趋势图（每两小时）")
    public Result listLeadNum(String factory, String lineBody, String process,
                              @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        Rate4 rate = dfLeadDetailService.listLeadNum(qw);
        Integer[] numList = new Integer[]{rate.getInte1(), rate.getInte2(), rate.getInte3(), rate.getInte4(), rate.getInte5(),
                rate.getInte6(), rate.getInte7(), rate.getInte8(), rate.getInte9(), rate.getInte10(), rate.getInte11(), rate.getInte12()};
        String[] timeList = new String[]{"7:00", "9:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00", "1:00", "3:00", "5:00"};
        Map<String, Object> result = new HashMap<>();
        result.put("num", numList);
        result.put("time", timeList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listOKRate")
    @ApiOperation("工厂一次/综合良率对比")
    public Result listOKRate(String factory, String lineBody, String process,
                              @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfLeadDetailService.listOKRate(qw);
        List<String> factoryList = new ArrayList<>();
        List<Double> oneRateList = new ArrayList<>();
        List<Double> mutilRateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            oneRateList.add(rate.getDou1());
            mutilRateList.add(rate.getDou2());
        }
        factoryList.add("J10-1");
        Map<String, Object> result = new HashMap<>();
        result.put("factory", factoryList);
        result.put("oneRate", oneRateList);
        result.put("mutilRate", mutilRateList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listAllOKRateTop")
    @ApiOperation("工厂不良TOP10分布对比")
    public Result listAllOKRateTop(String factory, String lineBody, String process,
                             @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfLeadDetailService.listAllOkRateTop(qw);
        List<String> factoryList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        List<Double> rateList = new ArrayList<>();
        for (Rate3 rate : rates) {
            numList.add(rate.getInte1());
            rateList.add(rate.getDou1());
        }
        factoryList.add("J10-1");
        Map<String, Object> result = new HashMap<>();
        result.put("factory", factoryList);
        result.put("num", numList);
        result.put("rate", rateList);
        return new Result(200, "查询成功", result);
    }

//    @GetMapping("/listWorkPositionOKRate")
//    @ApiOperation("工位间良率差异对比")
//    public Result listWorkPositionOKRate(String factory, String lineBody, String process,
//                                   @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
//        qw.between("check_time", startTime, endTime);
//        List<Rate3> rates = dfLeadDetailService.listWorkPositionOKRate(qw);
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
//        Map<String, Integer> positionResIndex = new HashMap<>();
//        positionResIndex.put("A1", 0);
//        positionResIndex.put("A2", 1);
//        positionResIndex.put("B3", 2);
//        positionResIndex.put("B4", 3);
//        String[] positionList = new String[]{"A1", "A2", "B3", "B4"};
//        for (Rate3 rate : rates) {
//            String machine = rate.getStr1();
//            String position = rate.getStr2();
//            Double okRate = rate.getDou1();
//            rateData[positionResIndex.get(position)][macResIndex.get(machine)] = okRate;
//        }
//
//        Map<String, Object> result = new HashMap<>();
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
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw
                .between("check_time", startTime, endTime)
                .eq("floor","4F");
        List<Rate3> rates = dfLeadDetailService.listWorkPositionOKRate(qw);
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

    @GetMapping("/listLeadNum2")
    @ApiOperation("产能数量趋势图（日期分组）")
    public Result listLeadNum2(String factory, String lineBody, String process,
                              @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime);
        List<Rate3> rates = dfLeadDetailService.listAllNumGroupByDate(qw);
        List<Object> dateList = new ArrayList<>();
        List<Object> dayList = new ArrayList<>();
        List<Object> nightList = new ArrayList<>();
        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            dayList.add(rate.getInte1());
            nightList.add(rate.getInte2());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("date", dateList);
        result.put("day", dayList);
        result.put("night", nightList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listItemNgRateTop")
    @ApiOperation("工厂不良TOP10分布对比2")
    public Result listItemNgRateTop(String factory, String lineBody, String process,
                                    @RequestParam String startDate, @RequestParam String endDate,
                                    Integer checkType, String machineCode, Integer oneOrMutil) throws ParseException {

        String key = "lead:top10:" + process + ":" + checkType + ":" + machineCode + ":" + oneOrMutil + ":" + startDate + "_" + endDate + ":";

        if (redisUtil.hasKey(key)) {
            return new Result(200, "查询成功", redisUtil.hmget(key));
        } else {
            String startTime = startDate + " 07:00:00";
            String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
            QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
            qw.between("det.check_time", startTime, endTime).eq("det.floor","4F");
            if (null != checkType) {
                if (checkType == 1) {  // 尺寸 倒角
                    qw.and(wrapper -> wrapper.eq("item.check_type", 1).or().eq("item.check_type", 2));
                } else if (checkType == 2){  // 外观
                    qw.eq("item.check_type", 3);
                }
            }
            qw.eq(!"".equals(machineCode) && null != machineCode, "det.machine_code", machineCode);
            qw.eq(null != oneOrMutil, "det.check_type", oneOrMutil);  // 1一次 / 2综合检测
            String selectNameString =
                    "SUBSTRING(\n" +
                    "    SUBSTRING_INDEX(item.item_name, '(', 2),\n" +
                    "    LOCATE('(', item.item_name) + 1,\n" +
                    "    LOCATE(')', item.item_name) - LOCATE('(', item.item_name) - 1\n" +
                    "  ) as 'str1'";
            List<Rate3> rates = dfLeadDetailService.listItemNgRateTop(qw,selectNameString);

			QueryWrapper<DfInspectionEquipmentDefect> ew = new QueryWrapper<>();
			ew.eq("device_type","LEAD");
			List<DfInspectionEquipmentDefect> list = dfInspectionEquipmentDefectService.list(ew);


			List<Object> itemList = new ArrayList<>();
            List<Object> ngRateList = new ArrayList<>();
            List<Object> checkTypeList = new ArrayList<>();
            for (Rate3 rate : rates) {
				Optional<DfInspectionEquipmentDefect> first = list.stream()
						.filter(one -> rate.getStr1()
						.equalsIgnoreCase(one.getTestEnName())).findFirst();
				if (first.isPresent()){
					rate.setStr1(first.get().getTestCnName());
				}
				itemList.add(rate.getStr1());
                ngRateList.add(rate.getDou1());
                checkTypeList.add(rate.getStr2());
            }
            Map<String, Object> result = new HashMap<>();
            result.put("item", itemList);
            result.put("ngRate", ngRateList);
            result.put("type", checkTypeList);
            redisUtil.hmset(key, result);
            return new Result(200, "查询成功", result);
        }

    }

    @GetMapping("/listMachineOneAndMutilOkRate")
    @ApiOperation("工厂一次/综合良率对比2")
    public Result listMachineOneAndMutilOkRate(String factory, String lineBody, String process,
                                    @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw
                .between("check_time", startTime, endTime)
                .eq("floor","4F")
                .eq("machine_code","19");
        List<Rate3> rates = dfLeadDetailService.listMachineOneAndMutilOkRate(qw);
        List<Object> machineList = new ArrayList<>();
        List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
        List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
        for (Rate3 rate : rates) {
            machineList.add(rate.getStr1());
            oneOkRateList.add(rate.getDou1());
            mutilOkRateList.add(rate.getDou2());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("machine", machineList);
        result.put("oneOkRate", oneOkRateList);
        result.put("mutilOkRate", mutilOkRateList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listOkRateGroupByDate")
    @ApiOperation("白夜班良率趋势对比")
    public Result listOkRateGroupByDate(String factory, String lineBody, String process,
                                               @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw
                .between("check_time", startTime, endTime)
                .eq("floor","4F");
        List<Rate3> rates = dfLeadDetailService.listOkRateGroupByDate(qw);
        List<Object> dateList = new ArrayList<>();
        List<Object> dayList = new ArrayList<>();
        List<Object> nightList = new ArrayList<>();
        List<Object> targetList = new ArrayList<>();

        //TZ-目标良率
        QueryWrapper<DfYieldWarn> yieldWarnWrapper = new QueryWrapper<>();
        yieldWarnWrapper
                .eq("`type`","LEAD")
                .eq("name","目标良率")
                .last("limit 1");
        DfYieldWarn dfYieldWarn = dfYieldWarnService.getOne(yieldWarnWrapper);


        for (Rate3 rate : rates) {
            dateList.add(rate.getStr1());
            dayList.add(rate.getDou1());
            nightList.add(rate.getDou2());
            targetList.add(dfYieldWarn.getPrewarningValue());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("date", dateList);
        result.put("day", dayList);
        result.put("night", nightList);
        result.put("targetListY",targetList);
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/getItemNormalDistributionByItem")
    @ApiOperation("获取测试项的正态分布图")
    public Result getItemNormalDistributionByItem(@RequestParam String item,
                                                  @RequestParam String startDate,
                                                  @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("det.check_time", startTime, endTime)
                        .eq("item.item_name", item);
        List<DfLeadCheckItemInfos> checkItemInfos = dfLeadDetailService.listItemInfosJoinDetail(qw);
        Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<String, Double> itemResStandardValue = new HashMap<>();
        Map<String, Double> itemResUsl = new HashMap<>();
        Map<String, Double> itemResLsl = new HashMap<>();
        Map<String, Integer> itemResOkNum = new HashMap<>();
        Map<String, Integer> itemResAllNum = new HashMap<>();

        for (DfLeadCheckItemInfos checkItemInfo : checkItemInfos) {
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

    @GetMapping("/getScatterPlotByItem")
    @ApiOperation("获取散点图")
    public Result getScatterPlotByItem(@RequestParam String item,
                                                  @RequestParam String startDate,
                                                  @RequestParam String endDate) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("det.check_time", startTime, endTime)
                .eq("item.item_name", item);
        List<DfLeadCheckItemInfos> checkItemInfos = dfLeadDetailService.listItemInfosJoinDetail(qw);
        Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<String, Double> itemResStandardValue = new HashMap<>();
        Map<String, Double> itemResUsl = new HashMap<>();
        Map<String, Double> itemResLsl = new HashMap<>();
        Double yMin = Double.MAX_VALUE;
        Double yMax = Double.MIN_VALUE;
        double total = 0d;
        List<Double> points = new ArrayList<>();

        for (DfLeadCheckItemInfos checkItemInfo : checkItemInfos) {
            String process = checkItemInfo.getItemName();
            Double checkValue = checkItemInfo.getCheckValue();
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
            yMin = yMin > checkValue ? checkValue : yMin;
            yMax = yMax < checkValue ? checkValue : yMax;
            total += checkValue;
            points.add(checkValue);
        }
        double mean = total / checkItemInfos.size();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : itemResCheckValue.entrySet()) {
            Map<String, Object> itemData = new HashMap<>();
            String process = entry.getKey();
            itemData.put("name", process);
            itemData.put("standard", itemResStandardValue.get(process));
            itemData.put("usl", itemResUsl.get(process));
            itemData.put("lsl", itemResLsl.get(process));
            itemData.put("points", points);
            itemData.put("yMin", yMin); // 最小值
            itemData.put("yMax", yMax); // 最大值
            itemData.put("mean", mean); // 均值
            result.add(itemData);
        }
        return new Result(200, "查询成功", result);
    }

    @GetMapping("/listAll")
    @ApiOperation("获取详情")
    public Result listAll(@RequestParam String startDate,
                                       @RequestParam String endDate,
                                       String color) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw.between("check_time", startTime, endTime)
                .eq(!"".equals(color) && null != color, "color", color);
        List<DfLeadDetail> list = dfLeadDetailService.list(qw);

        return new Result(200, "查询成功", list);
    }

    @GetMapping("/listDateOneAndMutilOkRate")
    @ApiOperation("工厂一次/综合良率对比2(时间分组)")
    public Result listDateOneAndMutilOkRate(String factory, String lineBody,
                                            @RequestParam String startDate, @RequestParam String endDate
            ,String project,String color,String device
    ) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
        qw
                .between("check_time", startTime, endTime)
                .eq("floor","4F");
        List<Rate3> rates = dfLeadDetailService.listDateOneAndMutilOkRate(qw);
        List<Object> timeList = new ArrayList<>();
        List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
        List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
        for (Rate3 rate : rates) {
            timeList.add(rate.getStr1());
            oneOkRateList.add(rate.getDou1());
            mutilOkRateList.add(rate.getDou2());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("time", timeList);
        result.put("oneOkRate", oneOkRateList);
        result.put("mutilOkRate", mutilOkRateList);
        return new Result(200, "查询成功", result);
    }


}
