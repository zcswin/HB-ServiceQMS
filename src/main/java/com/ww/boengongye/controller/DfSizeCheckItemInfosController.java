package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.SystemClock.now;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Controller
@RequestMapping("/dfSizeCheckItemInfos")
@ResponseBody
@CrossOrigin
@Api(tags = "尺寸受检信息")
public class DfSizeCheckItemInfosController {

    @Autowired
    private RedisUtils redisUtil;


    @Autowired
    DfSizeCheckItemInfosService DfSizeCheckItemInfosService;

    @Autowired
    DfSizeContRelationService dfSizeContRelationService;

    @Autowired
    private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Autowired
    private DfProcessService dfProcessService;

    @Autowired
    private DfProjectService dfProjectService;

    @Autowired
    private DfProjectColorService dfProjectColorService;

    @Autowired
    private DfTzSuggestService dfTzSuggestService;

    @Autowired
    private DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    private DfTzFaCaSuggestService dfTzFaCaSuggestService;
    DecimalFormat df = new DecimalFormat("#.00");

    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page , int limit, String checkId) {
        Page<DfSizeCheckItemInfos> pages = new Page<DfSizeCheckItemInfos>(page, limit);
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
            ew.eq("check_id", checkId);
        ew.orderByDesc("check_time");
        IPage<DfSizeCheckItemInfos> list=DfSizeCheckItemInfosService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

    @GetMapping("listByFactory")
    public Result listByFactory(String factory, String process, String project, String linebody, String dayOrNight,
                                String startDate, String endDate) {
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "det.factory", factory)
                .eq(null != process && !"".equals(process), "det.process", process)
                .eq(null != project && !"".equals(project), "det.project", project)
                .eq(null != linebody && !"".equals(linebody), "det.linebody", linebody)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "det.day_or_night", dayOrNight)
                .between(null != startDate && null != endDate, "item.create_time", startDate, endDate);
        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listByFactory(qw);
        return new Result(200, "查询成功", dfSizeCheckItemInfos);
    }

    @GetMapping("getOKRate")
    public Result getOKRate(String factory, String process, String project, String linebody, String dayOrNight,
                                String startDate, String endDate) {
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "det.factory", factory)
                .eq(null != process && !"".equals(process), "det.process", process)
                .eq(null != project && !"".equals(project), "det.project", project)
                .eq(null != linebody && !"".equals(linebody), "det.linebody", linebody)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "det.day_or_night", dayOrNight)
                .between(null != startDate && null != endDate, "item.create_time", startDate, endDate);
        DfSizeCheckItemInfos dfSizeCheckItemInfos = DfSizeCheckItemInfosService.getOKRate(qw);
        return new Result(200, "查询成功", dfSizeCheckItemInfos);
    }

    @RequestMapping(value = "/listByMachineCode")
    public Result listByMachineCode( String machineCode) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        ew.inSql("check_id", "select t.check_id from(select check_id from df_size_detail where result='NG' and machine_code='"+machineCode+"' order by test_time desc limit 0,1) as t");
        ew.eq("check_result","NG");
        ew.eq("key_point","1");

        //        ew.orderByDesc("check_time");
        return new Result(0, "查询成功",DfSizeCheckItemInfosService.list(ew));

    }

    @RequestMapping(value = "/listByCheckId")
    public Result listByCheckId( String checkId) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        ew.eq("check_id", checkId);
        ew.eq("check_result","NG");

        //        ew.orderByDesc("check_time");
        return new Result(0, "查询成功",DfSizeCheckItemInfosService.list(ew));

    }

    @GetMapping("/listInfosLimit50")
    public Result listInfosLimit50(String project, String color, String process, String dayOrNight) throws ParseException {
        // redis 定时生成数据，在定时类 com/ww/boengongye/timer/SizeBoard.java : getNear50Data

        List<String> projectList = new ArrayList<>();
        List<String> colorList = new ArrayList<>();
        List<String> processList = new ArrayList<>();
        if ("".equals(process) || null == process) {
            List<DfProcess> processes = dfProcessService.list();
            for (DfProcess dfProcess : processes) {
                processList.add(dfProcess.getProcessName());
            }
        } else {
            processList.add(process);
        }

        if ("".equals(project) || null == project) {
            List<DfProject> projects = dfProjectService.list();
            for (DfProject dfProject : projects) {
                projectList.add(dfProject.getName());
            }
        } else {
            projectList.add(project);
        }

        if ("".equals(color) || null == color) {
            List<DfProjectColor> colors = dfProjectColorService.list();
            for (DfProjectColor dfProjectColor : colors) {
                colorList.add(dfProjectColor.getColor());
            }
        } else {
            colorList.add(color);
        }

        if ("".equals(dayOrNight) || null == dayOrNight) dayOrNight = "AB";
        List<Object> result = new ArrayList<>();

        for (String projectName : projectList) {
            for (String processName : processList) {
                for (String colorName : colorList) {
                    String key = "size:limit50:" + projectName + ":" + colorName + ":" + processName + ":" + dayOrNight + ":";
                    List<Map<String, List<Object>>> data = redisUtil.getListFromRedis(key); // 用redis获取
                    if (null != data) {
                        result.addAll(data);
                    }
                }
            }
        }

        return new Result(200, "查询成功", result);

    }

    @ApiOperation("NG尺寸缺陷汇总")
    @GetMapping("/listSizeNgRate")
    public Result listSizeNgRate(String project, String color, String process, @RequestParam String startDate, @RequestParam String endDate, String dayOrNight) throws ParseException {
        if (null == process || "".equals(process)) process = "%%";
        if (null == project || "".equals(project)) project = "%%";
        if (null == color || "".equals(color)) color = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw.eq(!"".equals(dayOrNight), "day_or_night", dayOrNight)
                .between("create_time", startTime, endTime)
                .like("process", process)
                .like("color", color)
                .like("project", project);
        List<Rate> rates = DfSizeCheckItemInfosService.listSizeNgRate3(qw);  // 用新表查询得到NG TOP
        //List<Rate> rates = DfSizeCheckItemInfosService.listSizeNgRate(process, startTime, endTime, startHour, endHour);
        //List<Rate> rates = DfSizeCheckItemInfosService.listSizeNgRate2(process, startTime, endTime);
        List<String> itemNameList = new ArrayList<>();
        List<Double> ngRateList = new ArrayList<>();
        for (Rate rate : rates) {
            itemNameList.add(rate.getName());
            ngRateList.add(rate.getRate());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("itemNameList", itemNameList);
        result.put("ngRateList", ngRateList);
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("获取该测试项所有机台数据")
    @GetMapping("/listJoinDetailLimit50ByMachine")
    public Result listJoinDetailLimit50ByMachine(String project,String color,@RequestParam String process, @RequestParam String itemName, String dayOrNight) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        // 获取当天到前7天的数据
        String startTime = TimeUtil.getLastSomeDay(TimeUtil.getNowTimeNoHour(), 7);
        String endTime = TimeUtil.getNowTimeByNormal();
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<DfSizeCheckItemInfos>();
        qw
                .eq(StringUtils.isNotEmpty(project),"det.project",project)
                .eq(StringUtils.isNotEmpty(color),"det.item_name",color)
                .between( "item.create_time", startTime, endTime)
                .between( "HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                //.ne("WEEKDAY(item.create_time)", 6)   // 去掉周日的数据
                .eq(null != process && !"".equals(process), "det.process", process)
                .eq(null != itemName && !"".equals(itemName), "item.item_name", itemName);
        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailLimit50ByMachine(qw);
        Map<String, Map<String, List<Object>>> dCodeResValueMap = new LinkedHashMap<>();
        Map<String, String> dCodeResCheckResult = new HashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String dCode = dfSizeCheckItemInfo.getdCode();
            String checkResult = dfSizeCheckItemInfo.getCheckResult();
            if (!dCodeResValueMap.containsKey(dCode)) {
                Map<String, List<Object>> valueMap  = new HashMap<>();
                List<Object> checkValue = new ArrayList<>();
                List<Object> standardValue = new ArrayList<>();
                List<Object> usl = new ArrayList<>();
                List<Object> lsl = new ArrayList<>();
                List<Object> dCodeNameList = new ArrayList<>();
                List<Object> checkTimeList = new ArrayList<>();
                dCodeNameList.add(dCode);
                checkValue.add(dfSizeCheckItemInfo.getCheckValue());
                standardValue.add(dfSizeCheckItemInfo.getStandardValue());
                usl.add(dfSizeCheckItemInfo.getUsl());
                lsl.add(dfSizeCheckItemInfo.getLsl());
                checkTimeList.add(sdf.format(dfSizeCheckItemInfo.getCreateTime()));
                valueMap.put("checkValue", checkValue);
                valueMap.put("standardValue", standardValue);
                valueMap.put("usl", usl);
                valueMap.put("lsl", lsl);
                valueMap.put("name", dCodeNameList);
                valueMap.put("checkTime", checkTimeList);
                dCodeResValueMap.put(dCode, valueMap);
                dCodeResCheckResult.put(dCode, checkResult);
            } else {
                Map<String, List<Object>> valueMap = dCodeResValueMap.get(dCode);
                valueMap.get("checkValue").add(dfSizeCheckItemInfo.getCheckValue());
                valueMap.get("standardValue").add(dfSizeCheckItemInfo.getStandardValue());
                valueMap.get("usl").add(dfSizeCheckItemInfo.getUsl());
                valueMap.get("lsl").add(dfSizeCheckItemInfo.getLsl());
                valueMap.get("checkTime").add(sdf.format(dfSizeCheckItemInfo.getCreateTime()));
                if (checkResult.equals("NG") && dCodeResCheckResult.get(dCode).equals("OK")) {
                    dCodeResCheckResult.put(dCode, checkResult);
                }
            }

        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : dCodeResValueMap.entrySet()) {
            Map<String, List<Object>> data = entry.getValue();
            String dCode = entry.getKey();
            String checkResult = dCodeResCheckResult.get(dCode);
            List<Object> checkResultList = new ArrayList<>();  // 结果
            checkResultList.add(checkResult);
            List<Object> checkMasList = new ArrayList<>();   // 结果信息
            data.put("checkResult", checkResultList);
            if (checkResult.equals("NG")) {
                checkMasList.add("该机台超出正常波动范围内");
            } else {
                checkMasList.add("该机台处于正常波动范围内");
            }
            data.put("checkMas", checkMasList);
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);

    }

    @ApiOperation("获取该测试项所有机台数据（根据不良率排序）")
    @GetMapping("/listJoinDetailLimit50ByMachineSortByNgRate")
    public Result listJoinDetailLimit50ByMachineSortByNgRate(@RequestParam String process,
                                                             String project,
                                                             String color,
                                                             @RequestParam String itemName,
//                                                             @RequestParam String startDate,
//                                                             @RequestParam String endDate,
                                                             String dayOrNight) throws ParseException {

        if (!"A".equals(dayOrNight) && !"B".equals(dayOrNight)) dayOrNight = "AB";
        if ("".equals(process) || null == process) process = "*";
        if ("".equals(project) || null == project) project = "*";
        if ("".equals(color) || null == color) color = "*";
        if ("".equals(itemName) || null == itemName) itemName = "*";
        // 所有机台
        String dCode = "*";
        String key = "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight;
        System.out.println(key);
        Set<String> keys = redisUtil.keys(key);
        Gson gson = new Gson();
        List<Map<String, List<Object>>> result = new ArrayList<>();
        for (String s : keys) {
            String json = (String)redisUtil.get(s);
            String[] split = s.split(":");
            String projectRedis = split[2];
            String processRedis = split[3];
            String colorRedis = split[4];
            String itemNameRedis = split[5];
            Map<String, List<Object>> map = gson.fromJson(json, new TypeToken<Map<String, List<Object>>>(){}.getType());
            // 有50条数据  或者是 C100项目就展示
            if (map.get("checkValue").size() >= 50 || "C100".equals(projectRedis)) {
                // 拿最新的标准作为所有的标准
                int len = map.get("lsl").size();
                DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(projectRedis + colorRedis + processRedis + itemNameRedis);
                Object lsl = standardData.getIsolaLowerLimit();
                Object usl = standardData.getIsolaUpperLimit();
                Object standardValue = standardData.getStandard();
                List<Object> lslList = new ArrayList<>();
                List<Object> uslList = new ArrayList<>();
                List<Object> standardValueList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    lslList.add(lsl);
                    uslList.add(usl);
                    standardValueList.add(standardValue);
                }
                boolean isOk = true;
                for (Object checkValue : map.get("checkValue")) {
                    if ((Double)checkValue > (Double)usl || (Double)checkValue < (Double)lsl) {
                        isOk = false;
                    }
                }
                List<Object> checkResult = new ArrayList<>();
                List<Object> checkMas = new ArrayList<>();
                if (isOk) {
                    checkResult.add("OK");
                    checkMas.add("该机台处于正常波动范围内");
                } else {
                    checkResult.add("NG");
                    checkMas.add("该机台超出正常波动范围内");
                }

                map.put("checkResult", checkResult);
                map.put("checkMas", checkMas);
                map.put("lsl", lslList);
                map.put("usl", uslList);
                map.put("standardValue", standardValueList);
                result.add(map);
            }
        }
        result.sort((map1, map2) -> {
            String name1 = map1.get("name").get(0).toString();
            String name2 = map2.get("name").get(0).toString();
            return name1.compareTo(name2);
        });
        List<Map<String, List<Object>>> result2 = new ArrayList<>();

        // CNC2只展示40个曲线图
        if ("CNC2".equals(process)) {
            for (int i = 0; i < 40 && i < result.size(); i++) {
                if (null != result.get(i)) {
                    result2.add(result.get(i));
                }
            }
            return new Result(200, "查询成功", result2);
        }




//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        int startHour, endHour;
//        if ("A".equals(dayOrNight)) {
//            startHour = 0;
//            endHour = 11;
//        } else if ("B".equals(dayOrNight)) {
//            startHour = 12;
//            endHour = 23;
//        } else {
//            startHour = 0;
//            endHour = 23;
//        }
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<DfSizeCheckItemInfos>();
//        qw
//                .between( "item.create_time", startTime, endTime)
//                .between( "HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR))", startHour, endHour)
//                //.ne("WEEKDAY(item.create_time)", 6)   // 去掉周日的数据
//                .eq(null != process && !"".equals(process), "det.process", process)
//                .eq(StringUtils.isNotEmpty(project),"det.project",project)
//                .eq(StringUtils.isNotEmpty(color),"det.item_name",color)
//                .eq(null != itemName && !"".equals(itemName), "item.item_name", itemName);
//        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailLimit50ByMachineSortByNgRate(qw);
//        Map<String, Map<String, List<Object>>> dCodeResValueMap = new LinkedHashMap<>();
//        Map<String, String> dCodeResCheckResult = new HashMap<>();
//
//        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
//            String dCode = dfSizeCheckItemInfo.getdCode();
//            String checkResult = dfSizeCheckItemInfo.getCheckResult();
//            if (!dCodeResValueMap.containsKey(dCode)) {
//                Map<String, List<Object>> valueMap  = new HashMap<>();
//                List<Object> checkValue = new ArrayList<>();
//                List<Object> standardValue = new ArrayList<>();
//                List<Object> usl = new ArrayList<>();
//                List<Object> lsl = new ArrayList<>();
//                List<Object> dCodeNameList = new ArrayList<>();
//                List<Object> checkTimeList = new ArrayList<>();
//                dCodeNameList.add(dCode);
//                checkValue.add(dfSizeCheckItemInfo.getCheckValue());
//                standardValue.add(dfSizeCheckItemInfo.getStandardValue());
//                usl.add(dfSizeCheckItemInfo.getUsl());
//                lsl.add(dfSizeCheckItemInfo.getLsl());
//                checkTimeList.add(sdf.format(dfSizeCheckItemInfo.getCreateTime()));
//                valueMap.put("checkValue", checkValue);
//                valueMap.put("standardValue", standardValue);
//                valueMap.put("usl", usl);
//                valueMap.put("lsl", lsl);
//                valueMap.put("name", dCodeNameList);
//                valueMap.put("checkTime", checkTimeList);
//                dCodeResValueMap.put(dCode, valueMap);
//                dCodeResCheckResult.put(dCode, checkResult);
//            } else {
//                Map<String, List<Object>> valueMap = dCodeResValueMap.get(dCode);
//                valueMap.get("checkValue").add(dfSizeCheckItemInfo.getCheckValue());
//                valueMap.get("standardValue").add(dfSizeCheckItemInfo.getStandardValue());
//                valueMap.get("usl").add(dfSizeCheckItemInfo.getUsl());
//                valueMap.get("lsl").add(dfSizeCheckItemInfo.getLsl());
//                valueMap.get("checkTime").add(sdf.format(dfSizeCheckItemInfo.getCreateTime()));
//                if (checkResult.equals("NG") && dCodeResCheckResult.get(dCode).equals("OK")) {
//                    dCodeResCheckResult.put(dCode, checkResult);
//                }
//            }
//
//        }
//
//        List<Object> result = new ArrayList<>();
//        for (Map.Entry<String, Map<String, List<Object>>> entry : dCodeResValueMap.entrySet()) {
//            Map<String, List<Object>> data = entry.getValue();
//            String dCode = entry.getKey();
//            String checkResult = dCodeResCheckResult.get(dCode);
//            List<Object> checkResultList = new ArrayList<>();  // 结果
//            checkResultList.add(checkResult);
//            List<Object> checkMasList = new ArrayList<>();   // 结果信息
//            data.put("checkResult", checkResultList);
//            if (checkResult.equals("NG")) {
//                checkMasList.add("该机台超出正常波动范围内");
//            } else {
//                checkMasList.add("该机台处于正常波动范围内");
//            }
//            data.put("checkMas", checkMasList);
//            result.add(entry.getValue());
//        }

        return new Result(200, "查询成功", result);

    }

    @ApiOperation("获取该测试项所有机台数据（根据不良率排序）存缓存")
    @GetMapping("/listJoinDetailLimit50ByMachineSortByNgRateToCache")
    public Result listJoinDetailLimit50ByMachineSortByNgRateToCache(@RequestParam String project) throws ParseException {
        String[] dayOrNights = new String[]{"A", "B", "AB"};
        List<String> result = new ArrayList<>();
        for (String dayOrNight : dayOrNights) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int startHour, endHour;
            if ("A".equals(dayOrNight)) {
                startHour = 0;
                endHour = 11;
            } else if ("B".equals(dayOrNight)) {
                startHour = 12;
                endHour = 23;
            } else {
                startHour = 0;
                endHour = 23;
            }
            QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<DfSizeCheckItemInfos>();
            qw.between( "HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq("det.project", project);
//                .eq("det.process", "CNC2")
//                .eq("det.item_name", "C27-蓝")
//                .eq("item.item_name", "外形宽3")
//                .eq("det.machine_code", "I002")
            List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listMachineJoinDetailLimit50(qw);
            Map<String, Map<String, List<Object>>> keyResValueMap = new LinkedHashMap<>();
            Map<String, String> keyResCheckResult = new HashMap<>();
            for (int i = 0; i < dfSizeCheckItemInfos.size(); i++) {
                DfSizeCheckItemInfos dfSizeCheckItemInfo = dfSizeCheckItemInfos.get(i);
                String dCode = dfSizeCheckItemInfo.getdCode();
                // 如果机台号不是大写字母开头就去掉
                if (!dCode.matches("[A-Z].*")) {
                    continue;
                }
                project = dfSizeCheckItemInfo.getProject();
                String process = dfSizeCheckItemInfo.getProcess();
                String color = dfSizeCheckItemInfo.getColor();
                String itemName = dfSizeCheckItemInfo.getItemName();
                String key = project + "_" + process + "_" + color + "_" + itemName + "_" + dCode + "_" + dayOrNight;
                DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
                if (null == itemName || null == project || null == color || null == process || null == standardData) continue;
                String checkResult = dfSizeCheckItemInfo.getCheckResult();
                if (!keyResValueMap.containsKey(key)) {
                    if (null == dCode || null == dfSizeCheckItemInfo.getCheckValue() ||
                            null == dfSizeCheckItemInfo.getCheckTime()) {
                        continue;
                    }
                    Map<String, List<Object>> valueMap  = new HashMap<>();
                    List<Object> checkValue = new ArrayList<>();
                    List<Object> standardValue = new ArrayList<>();
                    List<Object> usl = new ArrayList<>();
                    List<Object> lsl = new ArrayList<>();
                    List<Object> dCodeNameList = new ArrayList<>();
                    List<Object> checkTimeList = new ArrayList<>();
                    dCodeNameList.add(dCode);
                    checkValue.add(dfSizeCheckItemInfo.getCheckValue());
                    standardValue.add(dfSizeCheckItemInfo.getStandardValue());
                    usl.add(standardData.getIsolaUpperLimit());
                    lsl.add(standardData.getIsolaLowerLimit());
                    checkTimeList.add(sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                    valueMap.put("checkValue", checkValue);
                    valueMap.put("standardValue", standardValue);
                    valueMap.put("usl", usl);
                    valueMap.put("lsl", lsl);
                    valueMap.put("name", dCodeNameList);
                    valueMap.put("checkTime", checkTimeList);
                    keyResValueMap.put(key, valueMap);
                    keyResCheckResult.put(key, checkResult);
                } else {
                    Map<String, List<Object>> valueMap = keyResValueMap.get(key);
                    // 如果和上一条的数据重复，就更新时间
//                    if ((Double)valueMap.get("checkValue").get(valueMap.get("checkValue").size() - 1) - dfSizeCheckItemInfo.getCheckValue() == 0) {
//                        // 更新时间
//                        valueMap.get("checkTime").set(valueMap.get("checkTime").size() - 1, sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
//                        continue;
//                    }
                    valueMap.get("checkValue").add(dfSizeCheckItemInfo.getCheckValue());
                    valueMap.get("standardValue").add(dfSizeCheckItemInfo.getStandardValue());
                    valueMap.get("usl").add(standardData.getIsolaUpperLimit());
                    valueMap.get("lsl").add(standardData.getIsolaLowerLimit());
                    valueMap.get("checkTime").add(sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                    // 如果超过50组，就删除掉最老的一组
                    if (valueMap.get("checkValue").size() > 50) {
                        valueMap.get("checkValue").remove(0);
                        valueMap.get("standardValue").remove(0);
                        valueMap.get("usl").remove(0);
                        valueMap.get("lsl").remove(0);
                        valueMap.get("checkTime").remove(0);
                    }
                }
            }

            for (Map.Entry<String, Map<String, List<Object>>> entry : keyResValueMap.entrySet()) {

                Map<String, List<Object>> data = entry.getValue();
                List<Object> uslList = data.get("usl");
                List<Object> lslList = data.get("lsl");
                List<Object> checkValueList = data.get("checkValue");
                boolean isOk = true;

                for (int i = 0; i < checkValueList.size(); i++) {
                    if ((Double)uslList.get(i) < (Double)checkValueList.get(i) || (Double)lslList.get(i) > (Double)checkValueList.get(i)) {
                        isOk = false;
                        break;
                    }
                }
                List<Object> checkResult = new ArrayList<>();
                List<Object> checkMas = new ArrayList<>();
                if (isOk) {
                    checkResult.add("OK");
                    checkMas.add("该机台处于正常波动范围内");
                } else {
                    checkResult.add("NG");
                    checkMas.add("该机台超出正常波动范围内");
                }
                data.put("checkResult", checkResult);
                data.put("checkMas", checkMas);

                String key = entry.getKey();
                String[] splitKey = key.split("_");
                project = splitKey[0];
                String process = splitKey[1];
                String color = splitKey[2];
                String itemName = splitKey[3];
                String dCode = splitKey[4];
                Gson gson = new Gson();
                String json = gson.toJson(data);
                redisUtil.set("size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight, json);
                System.out.println("插入redis：" + "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight);
                result.add("size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight);
//            redisUtil.hmset("size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode, data);
            }
        }


        return new Result(200, "redis缓存插入成功", result);

    }

    @ApiOperation("添加近50组机台数据")
    @GetMapping("/insertDataToLimit50Cache")
    public Result insertDataToLimit50Cache(String project,
                                           String process,
                                           String color,
                                           String itemName,
                                           String dCode,
                                           Timestamp checkTime,
                                           Double lsl,
                                           Double usl,
                                           Double checkValue,
                                           Double standardValue) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            checkTime = new Timestamp(now());
            // 使用 Calendar 获取时间戳的小时部分
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(checkTime.getTime());
            String time = sdf.format(checkTime.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);  // 获取小时（24小时制）
            String dayOrNight;
            // 判断是否在白班时间段（7:00 - 19:00）
            if (hour >= 7 && hour < 19) {
                dayOrNight = "A";
            } else {
                dayOrNight = "B";
            }

            // 添加当班的数据
            String key = "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight;
            updateMacLimit50ByKey(key, dCode, lsl, usl, checkValue, standardValue, time);
            // 添加全班的数据
            String key2 = "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":AB";
            updateMacLimit50ByKey(key2, dCode, lsl, usl, checkValue, standardValue, time);
        } catch (Exception e) {
            System.out.println("更新机台近50组测试数据报错，报错如下：");
            e.printStackTrace();
        }
        return new Result(200, "redis缓存插入成功");
    }

    private void updateMacLimit50ByKey(String key,
                                       String dCode,
                                       Double lsl,
                                       Double usl,
                                       Double checkValue,
                                       Double standardValue,
                                       String checkTime) {
        Set<String> keys = redisUtil.keys(key);
        Gson gson = new Gson();
        // 如果缓存有这个数据
        if (keys.size() > 0) {
            for (String s : keys) {
                String json = (String)redisUtil.get(s);
                Map<String, List<Object>> map = gson.fromJson(json, new TypeToken<Map<String, List<Object>>>(){}.getType());
                List<Object> uslList = map.get("usl");
                List<Object> lslList = map.get("lsl");
                List<Object> standardValueList = map.get("standardValue");
                List<Object> checkValueList = map.get("checkValue");
                List<Object> checkTimeList = map.get("checkTime");
                checkValueList.add(checkValue);
                standardValueList.add(standardValue);
                uslList.add(usl);
                lslList.add(lsl);
                checkTimeList.add(checkTime);
                if (checkValueList.size() > 50) checkValueList.remove(0);
                if (standardValueList.size() > 50) standardValueList.remove(0);
                if (uslList.size() > 50) uslList.remove(0);
                if (lslList.size() > 50) lslList.remove(0);
                if (checkTimeList.size() > 50) checkTimeList.remove(0);
                boolean isOk = true;

                for (int i = 0; i < checkValueList.size(); i++) {
                    if ((Double)uslList.get(i) < (Double)checkValueList.get(i) || (Double)lslList.get(i) > (Double)checkValueList.get(i)) {
                        isOk = false;
                        break;
                    }
                }
                List<Object> checkResult = new ArrayList<>();
                List<Object> checkMas = new ArrayList<>();
                if (isOk) {
                    checkResult.add("OK");
                    checkMas.add("该机台处于正常波动范围内");
                } else {
                    checkResult.add("NG");
                    checkMas.add("该机台超出正常波动范围内");
                }
                map.put("checkResult", checkResult);
                map.put("checkMas", checkMas);

                String replaceJson = gson.toJson(map);
                // 替换最新的结果
                redisUtil.set(key, replaceJson);
                break;
            }

        } else {  // 缓存没有这个数据的话就加
            Map<String, List<Object>> valueMap  = new HashMap<>();
            List<Object> checkValueList = new ArrayList<>();
            List<Object> standardValueList = new ArrayList<>();
            List<Object> uslList = new ArrayList<>();
            List<Object> lslList = new ArrayList<>();
            List<Object> dCodeNameList = new ArrayList<>();
            List<Object> checkTimeList = new ArrayList<>();
            List<Object> checkResult = new ArrayList<>();
            List<Object> checkMas = new ArrayList<>();
            dCodeNameList.add(dCode);
            checkValueList.add(checkValue);
            standardValueList.add(standardValue);
            uslList.add(usl);
            lslList.add(lsl);
            checkTimeList.add(checkTime);
            if (usl < checkValue || lsl < checkValue) {
                checkResult.add("NG");
                checkMas.add("该机台超出正常波动范围内");
            } else {
                checkResult.add("OK");
                checkMas.add("该机台处于正常波动范围内");
            }
            valueMap.put("checkValue", checkValueList);
            valueMap.put("standardValue", standardValueList);
            valueMap.put("usl", uslList);
            valueMap.put("lsl", lslList);
            valueMap.put("name", dCodeNameList);
            valueMap.put("checkTime", checkTimeList);
            String replaceJson = gson.toJson(valueMap);
            // 添加数据
            redisUtil.set(key, replaceJson);
        }
    }

    @ApiOperation("获取该工序该不良项近50组数据")
    @GetMapping("/listCheckItemValueLimit50")
    public Result listCheckItemValueLimit50(@RequestParam String process, @RequestParam String itemName, String dayOrNight) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        // 获取当天到前7天的数据
        String startTime = TimeUtil.getLastSomeDay(TimeUtil.getNowTimeNoHour(), 7);
        String endTime = TimeUtil.getNowTimeByNormal();
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw.between("item.create_time", startTime, endTime)
                .between( "HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR))", startHour, endHour)
                .eq(!"".equals(process), "det.process", process)
                .eq(!"".equals(itemName), "item.item_name", itemName);
        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listCheckItemValueLimit50(qw);

        List<Object> checkValue = new ArrayList<>();
        List<Object> standardValue = new ArrayList<>();
        List<Object> usl = new ArrayList<>();
        List<Object> lsl = new ArrayList<>();
        List<Object> checkTimeList = new ArrayList<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            checkValue.add(dfSizeCheckItemInfo.getCheckValue());
            standardValue.add(dfSizeCheckItemInfo.getStandardValue());
            usl.add(dfSizeCheckItemInfo.getUsl());
            lsl.add(dfSizeCheckItemInfo.getLsl());
            checkTimeList.add(sdf.format(dfSizeCheckItemInfo.getCreateTime()));
        }
        Map<String, List<Object>> result = new HashMap<>();
        result.put("checkValue", checkValue);
        result.put("standardValue", standardValue);
        result.put("usl", usl);
        result.put("lsl", lsl);
        result.put("checkTime", checkTimeList);

        return new Result<>(200, "查询成功", result);
    }

    @ApiOperation("获取正态分布图数据")
    @GetMapping("/getNormalDistribution")
    public Result getNormalDistribution(String machineCode,String testItemName,String process,String project,String color, @RequestParam String startDate, @RequestParam String endDate, String dayOrNight,
                                        @RequestParam Integer page, @RequestParam Integer limit) throws ParseException {
        // 先查看redis中是否存在这些数据
        List<Map<Object, Object>> result = new ArrayList<>();
        String redisColor = StringUtils.isEmpty(color) ? "*" : color;
        String redisProcess = StringUtils.isEmpty(process) ? "*" : process;
        String redisProject = StringUtils.isEmpty(project) ? "*" : project;
        String redisDayOrNight = StringUtils.isEmpty(dayOrNight) ? "AB" : dayOrNight;
        Set<String> keys = redisUtil.keys("size:normalDistribution:" + redisColor + ":" + redisProject + ":" + redisProcess + ":" + "*" + ":" + startDate + ":" + endDate + ":" + redisDayOrNight);
        // 分页
        Integer startLine = (page - 1) * limit;
        Integer endLine = startLine + limit;
        if (keys.size() > 0) {
            List<String> keyList = new ArrayList<>(keys);
            for (int i = startLine; i < endLine && i < keys.size(); i++) {
                String key = keyList.get(i);
                Map<Object, Object> map = redisUtil.hmget(key);
                result.add(map);
            }
            if (result.size() == 0) {
                return new Result(200, "查无数据", -1, result);
            }
            return new Result(200, "查询成功", 0, result);
        }

        if (null == project || "".equals(project)) project = "%%";
        if (null == color || "".equals(color)) color = "%%";
        if (null == process || "".equals(process)) process = "%%";
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }

        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos;
        if(null != process && !"".equals(process)&& StringUtils.isNotEmpty(testItemName)&& StringUtils.isNotEmpty(machineCode)){
            // 查机台
            dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailByTimeAndProcessAndItem(process,project,color, startTime, endTime, startHour, endHour, startLine, limit,testItemName,machineCode);  // 单工序,单测试项
        }else if(null != process && !"".equals(process)&& StringUtils.isNotEmpty(testItemName)){
            dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailByTimeAndProcessAndItem(process,project,color, startTime, endTime, startHour, endHour, startLine, limit,testItemName,null);  // 单工序,单测试项
        }
//        else  if (null != process && !"".equals(process)) {
//            dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailByTimeAndProcess(process,project,color, startTime, endTime, startHour, endHour, startLine, limit);  // 单工序
//        }
        else {

            // 查询mysql
            List<Map<String, Object>> result2 = DfSizeCheckItemInfosService.getNormalDistributionData(process, project, color, startTime, endTime, startHour, endHour, startLine, limit);
            for (int i = 0; i < result2.size(); i++) {
                // 查询到数据之后添加到redis中
                Map<String, Object> stringObjectMap = result2.get(i);
                String[] names = stringObjectMap.get("name").toString().split("-");
                project = names[0];
                process = names[1];
                String item = names[2];
                // 存入redis
                redisUtil.hmset("size:normalDistribution:" + color + ":" + project + ":" + process + ":" + item + ":" + startDate + ":" + endDate + ":" + dayOrNight, stringObjectMap, 60 * 60);
                if (i < startLine || i >= endLine) {
                    result2.remove(i);
                }
            }
            if (limit > result2.size()) {  // 没有更多了
                return new Result(200, "查询成功", -1, result2);
            } else {
                return new Result(200, "查询成功", 0, result2);
            }
//            dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailByTime(process, project,color,startTime, endTime, startHour, endHour, startLine, limit);  // 全工序
        }
        Map<Object, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<Object, Double> itemResStandardValue = new HashMap<>();
        Map<Object, Double> itemResUsl = new HashMap<>();
        Map<Object, Double> itemResLsl = new HashMap<>();
        Map<Object, Integer> itemResOkNum = new HashMap<>();
        Map<Object, Integer> itemResAllNum = new HashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String itemName = dfSizeCheckItemInfo.getItemName();
            String ngItem = itemName.split("-")[2];
            Double checkValue = dfSizeCheckItemInfo.getCheckValue();
            itemResAllNum.merge(itemName, 1, Integer::sum);
            if (dfSizeCheckItemInfo.getCheckResult().equals("OK")) itemResOkNum.merge(itemName, 1, Integer::sum);
            if (!itemResCheckValue.containsKey(itemName)) {
                DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + ngItem);
                List<Double> list = new ArrayList<>();
                list.add(checkValue);
                itemResCheckValue.put(itemName, list);
                itemResStandardValue.put(itemName, dfSizeCheckItemInfo.getStandardValue());
                itemResUsl.put(itemName, standardData.getIsolaUpperLimit());
                itemResLsl.put(itemName, standardData.getIsolaLowerLimit());
            } else {
                itemResCheckValue.get(itemName).add(checkValue);
            }
        }

        for (Map.Entry<Object, List<Double>> entry : itemResCheckValue.entrySet()) {
            Map<Object, Object> itemData = new HashMap<>();
            Object itemName = entry.getKey();
            itemData.put("name", itemName);
            itemData.put("standard", itemResStandardValue.get(itemName));
            itemData.put("usl", itemResUsl.get(itemName));
            itemData.put("lsl", itemResLsl.get(itemName));

            Integer okNum = itemResOkNum.get(itemName) == null ? 0 : itemResOkNum.get(itemName);
            Integer allNum = itemResAllNum.get(itemName) == null ? 0 : itemResAllNum.get(itemName);
            //itemData.put("良率OK", itemResOkNum.get(itemName));
            itemData.put("良率", okNum.doubleValue() / allNum);
            itemData.put("抽检数", allNum);
            //itemData.put("良率ALL", itemResAllNum.get(itemName));
            normalDistribution2(convertToDoubleArray(entry.getValue().toArray()), itemData);
            result.add(itemData);
        }



        if (limit > result.size()) {  // 没有更多了
            return new Result(200, "查询成功", -1, result);
        } else {
            return new Result(200, "查询成功", 0, result);
        }
    }




    @ApiOperation("获取该不良项所有工序正态分布图数据")
    @GetMapping("/getItemNormalDistribution")
    public Result getItemNormalDistribution(String project,String color,@RequestParam String itemName, @RequestParam String startDate, @RequestParam String endDate) {
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(project),"det.project",project)
                .eq(StringUtils.isNotEmpty(color),"det.item_name",color)
                .eq(!"".equals(itemName), "item.item_name", itemName)
                .between(!"".equals(startDate) && !"".equals(endDate), "item.create_time", startDate+" 00:00:00", endDate+" 23:59:59")
                //.ne("WEEKDAY(item.create_time)", 6)   // 去掉周日的数据
                .eq("item.key_point", 1);
        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailAndProcess(qw);
        Map<Object, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<Object, Double> itemResStandardValue = new HashMap<>();
        Map<Object, Double> itemResUsl = new HashMap<>();
        Map<Object, Double> itemResLsl = new HashMap<>();
        Map<Object, Integer> itemResOkNum = new HashMap<>();
        Map<Object, Integer> itemResAllNum = new HashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String process = dfSizeCheckItemInfo.getProcess();
            Double checkValue = dfSizeCheckItemInfo.getCheckValue();
            itemResAllNum.merge(process, 1, Integer::sum);
            if (dfSizeCheckItemInfo.getCheckResult().equals("OK")) itemResOkNum.merge(process, 1, Integer::sum);
            if (!itemResCheckValue.containsKey(process)) {
                List<Double> list = new ArrayList<>();
                list.add(checkValue);
                itemResCheckValue.put(process, list);
                itemResStandardValue.put(process, dfSizeCheckItemInfo.getStandardValue());
                itemResUsl.put(process, dfSizeCheckItemInfo.getUsl());
                itemResLsl.put(process, dfSizeCheckItemInfo.getLsl());
            } else {
                itemResCheckValue.get(process).add(checkValue);
            }
        }

        List<Map<Object, Object>> result = new ArrayList<>();
        for (Map.Entry<Object, List<Double>> entry : itemResCheckValue.entrySet()) {
            Map<Object, Object> itemData = new HashMap<>();
            Object process = entry.getKey();
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
            normalDistribution2(convertToDoubleArray(entry.getValue().toArray()), itemData);
            result.add(itemData);
        }
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("尺寸全工序各测量项每日良率")
    @GetMapping("/listAllProcessItemOkRate")
    public Result listAllProcessItemOkRate(String project, String color, @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        QueryWrapper<DfSizeCheckItemInfos> qw = new QueryWrapper<>();
        qw.between(!"".equals(startDate) && !"".equals(endDate), "create_time",
                        startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
                .eq(!"".equals(project) && null != project, "project", project)
                .eq(!"".equals(color) && null != color, "color", color);
                //.ne("WEEKDAY(create_time)", 6)  // 去掉周日的数据
        //.eq("key_point", 1)  // 使用新表之后不用这个key_point
        //List<Rate> rates = DfSizeCheckItemInfosService.listItemOkRate(qw);
        List<Rate> rates = DfSizeCheckItemInfosService.listItemOkRate2(qw);
        Map<String, Map<String, List<Object>>> itemResData = new LinkedHashMap<>();
//        List<Object> dateList = new ArrayList<>();
        for (Rate rate : rates) {
            String itemName = rate.getName();
            String date = rate.getDate();
//            if (!dateList.contains(date)) {
//                dateList.add(date);
//            }
            /*if (!itemResData.containsKey(itemName)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                List<Object> ngNumList = new ArrayList<>();
                ngNumList.add(rate.getNgNum());
                List<Object> allNumList = new ArrayList<>();
                allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                itemValue.put("rateList", rateList);
                itemValue.put("ngNumList", ngNumList);
                itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                itemValue.get("rateList").add(rate.getRate());
                itemValue.get("ngNumList").add(rate.getNgNum());
                itemValue.get("allNumList").add(rate.getAllNum());
            }*/

            // 分白夜班
            if (!itemResData.containsKey(itemName)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> dateList = new ArrayList<>();
                dateList.add(date);
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                //List<Object> ngNumList = new ArrayList<>();
                //ngNumList.add(rate.getNgNum());
                //List<Object> allNumList = new ArrayList<>();
                //allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                itemValue.put("date", dateList);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.put("dayRateList", rateList);
                    itemValue.put("nightRateList", new ArrayList<>());
                } else {  // 晚班
                    itemValue.put("dayRateList", new ArrayList<>());
                    itemValue.put("nightRateList", rateList);
                }
                //itemValue.put("rateList", rateList);
                //itemValue.put("ngNumList", ngNumList);
                //itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                List<Object> dateList = itemValue.get("date");
                if (!dateList.contains(date)) {
                    dateList.add(date);
                }
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.get("dayRateList").add(rate.getRate());
                } else {  // 晚班
                    itemValue.get("nightRateList").add(rate.getRate());
                }
                //itemValue.get("rateList").add(rate.getRate());
                //itemValue.get("ngNumList").add(rate.getNgNum());
                //itemValue.get("allNumList").add(rate.getAllNum());
            }
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResData.entrySet()) {
            result.add(entry.getValue());
        }
        return new Result(200, "查询成功", result);

    }



    @RequestMapping(value = "/listByCheckItem")
    public Result listByCheckItem( String itemName,String process,String startTime,String endTime) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        if(null!=itemName&&!itemName.equals("")&&!itemName.equals("undefined")){
            ew.eq("t.item_name",itemName);
        }
        if(null!=process&&!process.equals("")&&!process.equals("undefined")){
            ew.eq("p.process",process);
        }
        if(null!=startTime&&!startTime.equals("")&&!startTime.equals("undefined")&&null!=endTime&&!endTime.equals("")&&!endTime.equals("undefined")){
            ew.between("p.test_time",startTime,endTime);
        }

        return new Result(0, "查询成功",DfSizeCheckItemInfosService.listJoin(ew));

    }

    @GetMapping("/listAllProcessNgItemTop5")
    @ApiOperation("获取所有工序不良项TOP5")
    public Result listAllProcessNgItemTop5(String startDate, String endDate) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        List<Rate2> sizeRates = DfSizeCheckItemInfosService.listAllProcessSizeNgRateTop5V3(startTime, endTime);
        Map<String, Map<String, List<Object>>> processResData = new LinkedHashMap<>();
        for (Rate2 rate : sizeRates) {
            String process = rate.getProcess();
            if (!processResData.containsKey(process)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                if (null != rate.getItemNgRate()) rateList.add(rate.getItemNgRate());
                List<Object> name = new ArrayList<>();
                if (null != process) name.add(process);
                List<Object> itemNameList = new ArrayList<>();
                if (null != rate.getItemName()) itemNameList.add(rate.getItemName());
                List<Object> sizeOkRateList = new ArrayList<>();
                if (null != rate.getOkRate()) sizeOkRateList.add(rate.getOkRate());
                itemValue.put("process", name); // 添加工序
                itemValue.put("sizeOkRate", sizeOkRateList); // 添加工序良率
                itemValue.put("sizeItemName", itemNameList);  // 不良项名称
                itemValue.put("sizeItemNgRate", rateList);  // 不良项不良率
                itemValue.put("appearOkRate", new ArrayList<>());
                itemValue.put("appearItemName", new ArrayList<>());
                itemValue.put("appearItemNgRate", new ArrayList<>());
                processResData.put(process, itemValue);
            } else {
                Map<String, List<Object>> processValue = processResData.get(process);
                if (null != rate.getItemNgRate()) processValue.get("sizeItemNgRate").add(rate.getItemNgRate());
                if (null != rate.getItemName()) processValue.get("sizeItemName").add(rate.getItemName());
            }
        }

        List<Rate2> appearRates = dfQmsIpqcWaigTotalService.listAllProcessItemNgRateTop5V3(startTime, endTime);
        for (Rate2 rate : appearRates) {
            String process = rate.getProcess();
            Map<String, List<Object>> processValue = processResData.get(process);
            if (null != processValue){
                if (null != rate.getItemNgRate()) processValue.get("appearItemNgRate").add(rate.getItemNgRate());
                if (null != rate.getItemName()) processValue.get("appearItemName").add(rate.getItemName());
                if (null != rate.getOkRate() && processValue.get("appearOkRate").size() == 0) processValue.get("appearOkRate").add(rate.getOkRate());
            }

        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : processResData.entrySet()) {
            if (entry.getValue().get("appearOkRate").size() != 0 || entry.getValue().get("sizeOkRate").size() != 0) {
                result.add(entry.getValue());
            }
        }

        return new Result(200, "查询成功", result);
    }

    @ApiOperation("尺寸各工序该不良项的每日占比")
    @GetMapping("/listProcessNgRateByItem")
    public Result listProcessNgRateByItem(String project,String color,@RequestParam String item, @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        if (null == project || "".equals(project)) project = "%%";
        if (null == color || "".equals(color)) color = "%%";
        List<Rate> rates = DfSizeCheckItemInfosService.listProcessNgRateByItem(project,color,startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00", "NG", item);
        Map<String, Map<String, List<Object>>> itemResData = new LinkedHashMap<>();

        for (Rate rate : rates) {
            String itemName = rate.getName();
            String date = rate.getDate();

            /*if (!itemResData.containsKey(itemName)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                List<Object> ngNumList = new ArrayList<>();
                ngNumList.add(rate.getNgNum());
                List<Object> allNumList = new ArrayList<>();
                allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                itemValue.put("rateList", rateList);
                itemValue.put("ngNumList", ngNumList);
                itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                itemValue.get("rateList").add(rate.getRate());
                itemValue.get("ngNumList").add(rate.getNgNum());
                itemValue.get("allNumList").add(rate.getAllNum());
            }*/

            // 分白夜班
            if (!itemResData.containsKey(itemName)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                name.add(itemName);
                List<Object> dateList = new ArrayList<>();
                dateList.add(date);
                //List<Object> ngNumList = new ArrayList<>();
                //ngNumList.add(rate.getNgNum());
                //List<Object> allNumList = new ArrayList<>();
                //allNumList.add(rate.getAllNum());
                itemValue.put("name", name);
                itemValue.put("date", dateList);
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.put("dayRateList", rateList);
                    itemValue.put("nightRateList", new ArrayList<>());
                } else {  // 晚班
                    itemValue.put("dayRateList", new ArrayList<>());
                    itemValue.put("nightRateList", rateList);
                }
                //itemValue.put("rateList", rateList);
                //itemValue.put("ngNumList", ngNumList);
                //itemValue.put("allNumList", allNumList);
                itemResData.put(itemName, itemValue);
            } else {
                Map<String, List<Object>> itemValue = itemResData.get(itemName);
                List<Object> dateList = itemValue.get("date");
                if (!dateList.contains(date)) {
                    dateList.add(date);
                }
                if ("A".equals(rate.getDayOrNight())) {  // 白班
                    itemValue.get("dayRateList").add(rate.getRate());
                } else {  // 晚班
                    itemValue.get("nightRateList").add(rate.getRate());
                }
                //itemValue.get("rateList").add(rate.getRate());
                //itemValue.get("ngNumList").add(rate.getNgNum());
                //itemValue.get("allNumList").add(rate.getAllNum());
            }
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResData.entrySet()) {
            result.add(entry.getValue());
        }
        return new Result(200, "查询成功", result);

    }

    private void normalDistribution(double[] data, Map<String, Object> mapData) {  // 将xy轴和密度曲线数据放到数组中
        //double[] data = {}; // your array of data
        Arrays.sort(data);
        double mean = calculateMean(data); // calculate the mean of the data
        double stdDev = calculateStdDev(data, mean); // calculate the standard deviation of the data
        if (stdDev == 0) {
            return;
        }
        NormalDistribution normalDist = new NormalDistribution(mean, stdDev); // create a normal distribution object
        double[] xValues = new double[10];
        double[] yValues = new double[10];
        double x = mean - 3 * stdDev;
        double step = 6 * stdDev / 10;
        for (int i = 0; i < 10; i++) {  // 柱状图的xy轴
            xValues[i] = x;
            yValues[i] = normalDist.density(x);
            x += step;
        }
        double[] normalData = new double[10];
        //double increment = (data[data.length-1] - data[0]) / 10;
        double increment = (data[data.length-1] - data[0]) / 10;
        double current = data[0];
        for (int i = 0; i < 10; i++) {  // 正态分布图的密度曲线
            normalData[i] = normalDist.density(current); // generate the normal distribution of each data point
            xValues[i] = current;
            current += increment;
        }
        mapData.put("x", xValues);
        mapData.put("y1", yValues);
        mapData.put("y2", normalData);
    }

    // helper method to calculate the mean of an array of data
    private static double calculateMean(double[] data) {
        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.length;
    }

    // helper method to calculate the standard deviation of an array of data
    private static double calculateStdDev(double[] data, double mean) {
        double sum = 0.0;
        for (double d : data) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / data.length);
    }

    public double[] convertToDoubleArray(Object[] objArray) {
        double[] doubleArray = new double[objArray.length]; // create a new double array with the same length as the input Object array
        for (int i = 0; i < objArray.length; i++) {
            doubleArray[i] = (double) objArray[i]; // cast each Object element to double and add it to the new double array
        }
        return doubleArray; // return the new double array
    }

    private static void intervalFrequency(int[] interval, double increment, double min, double[] data) {  // 区间频次 直方图
        for (double datum : data) {
            int v = (int)((datum - min) / increment);
            v = v < 0 ? 0 : Math.min(v, interval.length - 1);
            interval[v]++;
        }
    }

    public static double round(double v,int scale) {  // 保留小数
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static void normalDistribution2(double[] data, Map<Object, Object> mapData) {  // 将xy轴和密度曲线数据放到数组中
        //double[] data = {}; // your array of data
        if (data.length == 0) return;
        Arrays.sort(data);
        if (data.length > 10) {
            double[] data2 = Arrays.copyOfRange(data, 5, data.length - 5);
            data = data2;
        }

        double min = (double)mapData.get("lsl");
        double max = (double)mapData.get("usl");
        //double min = Math.min(data[0], (double)mapData.get("lsl"));  // 以数据和上下限的最大最小
        //double max = Math.max(data[data.length-1], (double)mapData.get("usl"));
        //double min = (double)mapData.get("lsl");  // 以上下限为最大最小
        //double max = (double)mapData.get("usl");
        double mean = calculateMean(data); // calculate the mean of the data 均值
        double stdDev = calculateStdDev(data, mean); // calculate the standard deviation of the data 标准差
        if (stdDev == 0) {
            return;
        }
        NormalDistribution normalDist = new NormalDistribution(mean, stdDev); // create a normal distribution object
        int groupNum = 20; // 组数
        double[] xValues = new double[groupNum];
        int[] yValues = new int[groupNum];
        double[] normalData = new double[groupNum];
        //double increment = (data[data.length-1] - data[0]) / groupNum;
        //double increment = ((double)mapData.get("usl") - (double)mapData.get("lsl")) / groupNum;
        double increment = (max - min) / groupNum;
        //double current = data[0];
        //double current = (double)mapData.get("lsl");
        double current = min;
        for (int i = 0; i < groupNum; i++) {  // 正态分布图的密度曲线
            normalData[i] = normalDist.density(current); // generate the normal distribution of each data point
            xValues[i] = current;
            current += increment;
        }
        //intervalFrequency(yValues, increment, data[0], data);  // 获取直方图
        //intervalFrequency(yValues, increment, (double)mapData.get("lsl"), data);  // 获取直方图
        intervalFrequency(yValues, increment, min, data);  // 获取直方图
        mapData.put("x", xValues);
        mapData.put("y1", yValues);
        mapData.put("y2", normalData);
    }

    @ApiOperation("全⼯序良率趋势图")
    @GetMapping(value = "/listTotalprocessyieldtrendchart")
    public Result listTotalprocessyieldtrendchart(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        if (StringUtils.isEmpty(process)) process = "%%";
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        } else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list= DfSizeCheckItemInfosService.listTotalprocessyieldtrendchart(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> name = new ArrayList<>();
        ArrayList<Object> result = new ArrayList<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            name.add(dfSizeCheckItemInfos.getProcess());
            result.add(dfSizeCheckItemInfos.getResult());
        }
        map.put("process",name);
        map.put("result",result);
        return new Result(0, "查询成功",map);
    }

    @ApiOperation("全制程直通")
    @GetMapping(value = "/throughTheWholeProcess")
    public Result throughTheWholeProcess(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        if (StringUtils.isEmpty(process)) process = "%%";
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        }else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list= DfSizeCheckItemInfosService.listTotalprocessyieldtrendchart(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> name = new ArrayList<>();
        ArrayList<Object> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.####");
        Double temp = Double.valueOf(1);
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            name.add(dfSizeCheckItemInfos.getProcess());
            temp = temp* dfSizeCheckItemInfos.getResult();
            result.add(df.format(temp));
        }
        map.put("process",name);
        map.put("result",result);
        return new Result(0, "查询成功",map);
    }

    @ApiOperation("全尺寸NG-TOP5")
    @GetMapping(value = "/fullSizeNGTop5")
    public Result fullSizeNGTop5(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        }else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list= DfSizeCheckItemInfosService.fullSizeNGTop5(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> name = new ArrayList<>();
        ArrayList<Object> result = new ArrayList<>();
        Double temp = Double.valueOf(1);
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            name.add(dfSizeCheckItemInfos.getItemName());
            result.add(dfSizeCheckItemInfos.getNgRate());
        }
        map.put("process",name);
        map.put("result",result);
        return new Result(0, "查询成功",map);
    }

    @GetMapping("/listAllProcessNgItemTop5V2")
    @ApiOperation("获取所工序尺寸外观良率不良汇总第二版")
    public Result listAllProcessNgItemTop5V2(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        }else {
            endTime = endTime + " 23:59:59";
        }
        List<Rate> sizeRates = DfSizeCheckItemInfosService.listAllProcessSizeNgRateTop5V2(factoryId,process,lineBody,item,startTime,endTime);
        Map<String, Map<String, List<Object>>> processResData = new LinkedHashMap<>();
        for (Rate rate : sizeRates) {
            process = rate.getName();
            if (!processResData.containsKey(process)) {
                Map<String, List<Object>> itemValue = new HashMap<>();
                List<Object> rateList = new ArrayList<>();
                if (null != rate.getRate()) rateList.add(rate.getRate());
                List<Object> name = new ArrayList<>();
                if (null != process) name.add(process);
                List<Object> itemNameList = new ArrayList<>();
                if (null != rate.getDate()) itemNameList.add(rate.getDate());
                itemValue.put("process", name); // 添加工序
                itemValue.put("sizeOkRate", new ArrayList<>());
                itemValue.put("sizeItemName", itemNameList);  // 不良项名称
                itemValue.put("sizeItemNgRate", rateList);  // 不良项不良率
                itemValue.put("appearOkRate", new ArrayList<>());
                itemValue.put("appearItemName", new ArrayList<>());
                itemValue.put("appearItemNgRate", new ArrayList<>());
                processResData.put(process, itemValue);
            } else {
                Map<String, List<Object>> processValue = processResData.get(process);
                if (null != rate.getRate()) processValue.get("sizeItemNgRate").add(rate.getRate());
                if (null != rate.getDate()) processValue.get("sizeItemName").add(rate.getDate());
            }
        }
        QueryWrapper<DfSizeCheckItemInfos> sizeQw2 = new QueryWrapper<>();
        sizeQw2.between(!startTime.equals("") && null!=startTime && !endTime.equals("") && null!=endTime,
                "create_time", startTime, endTime);
        List<Rate> sizeOkRates = DfSizeCheckItemInfosService.listAllProcessOKRate(sizeQw2);  // 所有尺寸良率
        for (Rate rate : sizeOkRates) {
            process = rate.getName();
            Map<String, List<Object>> processValue = processResData.get(process);
            processValue.get("sizeOkRate").add(rate.getRate());  // 尺寸良率
        }

        List<Rate> appearRates = dfQmsIpqcWaigTotalService.listAllProcessItemNgRateTop5V2(factoryId,process,lineBody,item,startTime,endTime);
        for (Rate rate : appearRates) {
            process = rate.getName();
            Map<String, List<Object>> processValue = processResData.get(process);
            if (null != rate.getRate()) processValue.get("appearItemNgRate").add(rate.getRate());
            if (null != rate.getDate()) processValue.get("appearItemName").add(rate.getDate());
        }
        QueryWrapper<DfQmsIpqcWaigTotal> appearQw2 = new QueryWrapper<>();
        appearQw2.between(!startTime.equals("") && null!=startTime && !endTime.equals("") && null!=endTime,
                "f_time", startTime, endTime)
                .isNotNull("");
        appearQw2.isNotNull("f_seq");
        List<Rate> appearOkRates = dfQmsIpqcWaigTotalService.listAllProcessOkRate2(appearQw2);  // 所有尺寸良率
        for (Rate rate : appearOkRates) {
            process = rate.getName();
            Map<String, List<Object>> processValue = processResData.get(process);
            processValue.get("appearOkRate").add(rate.getRate());  // 尺寸良率
        }

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : processResData.entrySet()) {
            result.add(entry.getValue());
        }

        return new Result(200, "查询成功", result);
    }


    @ApiOperation("单⼯序良率趋势图")
    @GetMapping(value = "/listSingleprocessyieldtrendchart")
    public Result listSingleprocessyieldtrendchart(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        QueryWrapper<DfSizeCheckItemInfos> ew=new QueryWrapper<DfSizeCheckItemInfos>();
        if (StringUtils.isEmpty(process)) process = "%%";
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        } else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list= DfSizeCheckItemInfosService.listSingleprocessyieldtrendchart(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> x = new ArrayList<>();
        ArrayList<Object> y = new ArrayList<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            x.add(dfSizeCheckItemInfos.getMonthDay());
            y.add(dfSizeCheckItemInfos.getResult());
        }
        map.put("x",x);
        map.put("y",y);
        return new Result(200, "查询成功",map);
    }

    @ApiOperation("单⼯序投入产出数量")
    @GetMapping(value = "/inputOutputQuantity")
    public Result inputOutputQuantity(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        List<DfSizeCheckItemInfos> list = DfSizeCheckItemInfosService.inputOutputQuantity(factoryId,process,lineBody,item,startTime,endTime);
        return null;
    }

    @ApiOperation("明细页尺寸良率")
    @GetMapping(value = "/detailPageSizeYield")
    public Result detailPageSizeYield(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        } else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list = DfSizeCheckItemInfosService.detailPageSizeYield(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            ArrayList<Object> array = new ArrayList<>();
            array.add(dfSizeCheckItemInfos.getProcess());
            array.add(dfSizeCheckItemInfos.getResult());
            map.put(dfSizeCheckItemInfos.getSort(), array);
        }
        return new Result(200, "查询成功",map);
    }

    @ApiOperation("明细页外观良率")
    @GetMapping(value = "/detailPageApparenceYield")
    public Result detailPageApparenceYield(String factoryId,String process,String lineBody,String item,String startTime,String endTime) {
        if (StringUtils.isEmpty(startTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH,-1);
            startTime = format.format(calendar.getTime());
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = TimeUtil.getNowTimeByNormal();
        } else {
            endTime = endTime + " 23:59:59";
        }
        List<DfSizeCheckItemInfos> list = dfQmsIpqcWaigTotalService.detailPageApparenceYield(factoryId,process,lineBody,item,startTime,endTime);
        HashMap<String, Object> map = new HashMap<>();
        for (DfSizeCheckItemInfos dfSizeCheckItemInfos : list) {
            ArrayList<Object> array = new ArrayList<>();
            array.add(dfSizeCheckItemInfos.getProcess());
            array.add(dfSizeCheckItemInfos.getResult());
            map.put(dfSizeCheckItemInfos.getSort(), array);
        }
        return new Result(200, "查询成功",map);
    }

    @ApiOperation("三星_尺寸_机台尺寸精度管控图(clazz传A,B分别代表A,B班)")
    @GetMapping(value = "/machineSizeAccuracyControlChart")
    public Result machineSizeAccuracyControlChart(String factory,String process,String lineBody,
                                                  String machineCode,String clazz,
                                                  String project,@RequestParam String startDate,@RequestParam String endDate) throws ParseException {
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        String startTime = startDate + " 07:00:01";
        QueryWrapper<DfSizeCheckItemInfos> ew = new QueryWrapper<>();
        ew.between("DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME",startTime,endTime)
            .eq(StringUtils.isNotEmpty(factory),"FACTORY",factory)
            .eq(StringUtils.isNotEmpty(process),"PROCESS",process)
            .eq(StringUtils.isNotEmpty(lineBody),"LINEBODY",lineBody)
            .eq(StringUtils.isNotEmpty(machineCode),"MACHINE_CODE",machineCode)
            .eq(StringUtils.isNotEmpty(project),"project",project)
            .apply("A".equals(clazz),"hour(date_sub(DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME,INTERVAL 7 HOUR )) BETWEEN 0 AND 11")
            .apply("B".equals(clazz),"hour(date_sub(DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME,INTERVAL 7 HOUR )) BETWEEN 12 AND 23");
        List<DfSizeCheckItemInfos> list = DfSizeCheckItemInfosService.machineSizeAccuracyControlChart(ew);
        Map<String, List<DfSizeCheckItemInfos>> collect = list.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(DfSizeCheckItemInfos::getItemName));
        ArrayList<Object> resultList = new ArrayList<>();
        for (Map.Entry<String, List<DfSizeCheckItemInfos>> entry : collect.entrySet()) {
            HashMap<Object, Object> map = new HashMap<>();
            ArrayList<Object> lsl = new ArrayList<>();
            ArrayList<Object> usl = new ArrayList<>();
            ArrayList<Object> standard = new ArrayList<>();
            ArrayList<Object> controlLsl = new ArrayList<>();
            ArrayList<Object> controlUsl = new ArrayList<>();
            ArrayList<Object> value = new ArrayList<>();
            map.put("result","OK");
            map.put("name",entry.getKey());
            entry.getValue().stream().forEach(item->{
                lsl.add(item.getLsl());
                usl.add(item.getUsl());
                standard.add(item.getStandardValue());
                controlLsl.add(item.getControlLowerLimit());
                controlUsl.add(item.getControlUpperLimit());
                value.add(item.getCheckValue());
                if (item.getCheckValue() < item.getLsl() || item.getCheckValue() > item.getUsl()){
                    map.put("result","NG");
                }
                map.put("lsl", lsl);
                map.put("usl", usl);
                map.put("standard", standard);
                map.put("controlLsl", controlLsl);
                map.put("controlUsl", controlUsl);
                map.put("value", value);
                resultList.add(map);
            });
        }
        return new Result(200, "查询成功",resultList);
    }

    @ApiOperation("三星_尺寸_点击进入各机台管制图(最新50片)")
    @GetMapping(value = "/machineSizeControlChart")
    public Result machineSizeControlChart(String factory,String process,String lineBody,
                                                  String machineCode,String clazz,
                                                  String project,@RequestParam String startDate,@RequestParam String endDate,
                                                  @RequestParam String itemName) throws ParseException {
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        String startTime = startDate + " 07:00:01";
        QueryWrapper<DfSizeCheckItemInfos> ew = new QueryWrapper<>();
        ew.between("DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME",startTime,endTime)
                .eq(StringUtils.isNotEmpty(itemName),"DF_SIZE_CHECK_ITEM_INFOS.ITEM_NAME",itemName)
                .eq(StringUtils.isNotEmpty(factory),"FACTORY",factory)
                .eq(StringUtils.isNotEmpty(process),"PROCESS",process)
                .eq(StringUtils.isNotEmpty(lineBody),"LINEBODY",lineBody)
//                .eq(StringUtils.isNotEmpty(machineCode),"MACHINE_CODE",machineCode)
                .eq(StringUtils.isNotEmpty(project),"project",project)
                .apply("A".equals(clazz),"hour(date_sub(DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME,INTERVAL 7 HOUR )) BETWEEN 0 AND 11")
                .apply("B".equals(clazz),"hour(date_sub(DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME,INTERVAL 7 HOUR )) BETWEEN 12 AND 23");
        List<DfSizeCheckItemInfos> list = DfSizeCheckItemInfosService.machineSizeControlChart(ew);
        Map<String, List<DfSizeCheckItemInfos>> collect = list.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(DfSizeCheckItemInfos::getMachineCode));
        ArrayList<Object> resultList = new ArrayList<>();
        for (Map.Entry<String, List<DfSizeCheckItemInfos>> entry : collect.entrySet()) {
            HashMap<Object, Object> map = new HashMap<>();
            ArrayList<Object> lsl = new ArrayList<>();
            ArrayList<Object> usl = new ArrayList<>();
            ArrayList<Object> standard = new ArrayList<>();
            ArrayList<Object> controlLsl = new ArrayList<>();
            ArrayList<Object> controlUsl = new ArrayList<>();
            ArrayList<Object> value = new ArrayList<>();
            map.put("result","OK");
            map.put("name",entry.getKey());
            entry.getValue().stream().forEach(item->{
                lsl.add(item.getLsl());
                usl.add(item.getUsl());
                standard.add(item.getStandardValue());
                controlLsl.add(item.getControlLowerLimit());
                controlUsl.add(item.getControlUpperLimit());
                value.add(item.getCheckValue());
                if (item.getCheckValue() < item.getLsl() || item.getCheckValue() > item.getUsl()){
                    map.put("result","NG");
                }
                map.put("lsl", lsl);
                map.put("usl", usl);
                map.put("standard", standard);
                map.put("controlLsl", controlLsl);
                map.put("controlUsl", controlUsl);
                map.put("value", value);
                resultList.add(map);
            });
        }
        return new Result(200, "查询成功",resultList);
    }


    @ApiOperation("获取机台的均值,方差,CPK数据")
    @GetMapping(value = "/listMacTzInfo")
    public Result listMacTzInfo(String process,
                                          String machineCode,
                                @RequestParam String itemName,
                                          String project,@RequestParam String startDate,@RequestParam String endDate
                                         )  {

        QueryWrapper<DfSizeCheckItemInfos>qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(process),"det.process",process);
        qw.eq(StringUtils.isNotEmpty(project),"det.project",project);
        qw.eq(StringUtils.isNotEmpty(machineCode),"det.machine_code",machineCode);
        qw.eq(StringUtils.isNotEmpty(project),"item.item_name",itemName);
        qw.between("det.test_time",startDate,endDate);

        List<DfSizeCheckItemInfos> datas=DfSizeCheckItemInfosService.tzInfo(qw);
        List<DfTzFaCaSuggest> facaDatas= new ArrayList<>();
        if(datas.size()>0){
            for (DfSizeCheckItemInfos d:datas){
                if(null!=d.getCa()&&null!=d.getCpk()){
                    DecimalFormat df = new DecimalFormat("#.##");
                    d.setCa(Double.valueOf(df.format(d.getCa())));
                    d.setCpk(Double.valueOf(df.format(d.getCpk())));
                    DfTzSuggest caResult=dfTzSuggestService.getResult(d.getCa(),"Ca");
                    DfTzSuggest cpkResult=dfTzSuggestService.getResult(d.getCpk(),"Cpk");
                    if(null!=caResult&&null!=caResult.getResult()&&null!=cpkResult&&null!=cpkResult.getResult()){
                        QueryWrapper<DfTzFaCaSuggest>qwr=new QueryWrapper<>();
                        qwr.eq("ca",caResult.getResult());
                        qwr.eq("cpk",cpkResult.getResult());
                        qwr.last("limit 1");
                        DfTzFaCaSuggest facaResult=dfTzFaCaSuggestService.getOne(qwr);

                        if(null!=facaResult){//组织标题文字
                            facaResult.setTitile(process+"中"+machineCode+"机的"+itemName+"的Ca("+d.getCa()+")"+caResult.getResult()+" ,Cpk("+d.getCpk()+")"+cpkResult.getResult());
                            facaDatas.add(facaResult);
                        }
                    }
                }



            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("caCpk", datas);
        result.put("suggest", facaDatas);

        return new Result(200, "查询成功",result);

    }

//    public DfTzFaCaSuggest checkCaCpk(DfSizeCheckItemInfos d){
//        QueryWrapper<DfTzSuggest>qw=new QueryWrapper<>();
//        qw.eq("name","Ca");
//        qw.orderByAsc("sort");
//        List<DfTzSuggest>caList=dfTzSuggestService.list(qw);
//        if(caList.size()>0){
//            for(DfTzSuggest c:caList){
//               if(c.getType().equals("single")) {
//                   if()
//               }
//            }
//        }
//    }


    @ApiOperation("获取TZ工序的缺陷的管控标准")
    @GetMapping(value = "/getTzProcessInfo")
    public Result getTzProcessInfo(String process,
                                @RequestParam String itemName,
                                String project,@RequestParam String startDate,@RequestParam String endDate
    )  {
        List<String>titles=new ArrayList<>();
        List<String>qcpUsl=new ArrayList<>();
        qcpUsl.add("QCP");
        qcpUsl.add("USL");
        List<String>qcpLsl=new ArrayList<>();
        qcpLsl.add("QCP");
        qcpLsl.add("LSL");
        List<String>innerUsl=new ArrayList<>();
        innerUsl.add("内控");
        innerUsl.add("USL");
        List<String>innerLsl=new ArrayList<>();
        innerLsl.add("内控");
        innerLsl.add("LSL");
        List<String>systemUsl=new ArrayList<>();
        systemUsl.add("系统建议");
        systemUsl.add("USL");
        List<String>systemLsl=new ArrayList<>();
        systemLsl.add("系统建议");
        systemLsl.add("LSL");
        List<String>realCpk=new ArrayList<>();
        realCpk.add("实际");
        realCpk.add("CPK");
        List<String>innerCpk=new ArrayList<>();
        innerCpk.add("内控");
        innerCpk.add("CPK");

        QueryWrapper<DfSizeContRelation>qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(process),"process",process);
        qw.eq(StringUtils.isNotEmpty(project),"project",project);
        qw.eq(StringUtils.isNotEmpty(project),"ipqc_name",itemName);
        qw.last("limit 1");
        List<DfSizeContRelation> data=dfSizeContRelationService.list(qw);
        if(null!=data&&data.size()>0){
            for(DfSizeContRelation d:data){
                titles.add(d.getName());
                titles.add(d.getProcess());
                QueryWrapper<DfSizeCheckItemInfos>qw2=new QueryWrapper<>();
                qw2.eq(StringUtils.isNotEmpty(process),"det.process",d.getProcess());
                qw2.eq(StringUtils.isNotEmpty(project),"det.project",project);
                qw2.eq(StringUtils.isNotEmpty(project),"item.item_name",itemName);
                qw2.between("det.test_time",startDate,endDate);
                //获取实际cpk
                List<DfSizeCheckItemInfos> cpk=DfSizeCheckItemInfosService.tzInfoByProcess(qw2);
                if(null!=cpk&&cpk.size()>0){
                    if(null!=cpk.get(0).getCpk()){
                        d.setCpk(cpk.get(0).getCpk());
                    }

                }
                //获取内控cpk
                List<DfSizeCheckItemInfos> innerCpkData=DfSizeCheckItemInfosService.tzInfoCpkByProcess(qw2);
                if(null!=innerCpkData&&innerCpkData.size()>0){
                    if(null!=innerCpkData.get(0).getCpk()) {
                        d.setInnerCpk(innerCpkData.get(0).getCpk());
                    }
                }

                qcpUsl.add(d.getQcpUsl().toString());
                qcpLsl.add(d.getQcpLsl().toString());
                innerUsl.add(d.getInnerUsl().toString());
                innerLsl.add(d.getInnerLsl().toString());

                systemUsl.add(d.getInnerUsl().toString());
                systemLsl.add(d.getInnerLsl().toString());
                if(null!=d.getCpk()) {
                    realCpk.add(d.getCpk().toString());
                }else{
                    realCpk.add("0");
                }

                if(null!=d.getInnerCpk()) {
                    innerCpk.add(d.getInnerCpk().toString());
                }else{
                    innerCpk.add("0");
                }

            }


        }
        List<List<String>>dataResult=new ArrayList<>();
        dataResult.add(qcpUsl);
        dataResult.add(qcpLsl);
        dataResult.add(innerUsl);
        dataResult.add(innerLsl);
        dataResult.add(systemUsl);
        dataResult.add(systemLsl);
        dataResult.add(realCpk);
        dataResult.add(innerCpk);
        Map<String,Object>result=new HashMap<>();
        result.put("tableTitleList",titles);
        result.put("tableData",dataResult);
        result.put("processData",data);



        return new Result(200, "查询成功",result);

    }

    @ApiOperation("获取尺寸近50组数据")
    @GetMapping(value = "/getNear50Data")
    public void getNear50Data(@RequestParam String project) throws ParseException {
        System.out.println("更新limit50");
        QueryWrapper<DfProcessProjectConfig> qw = new QueryWrapper<>();
        qw.like("type", "尺寸");
        List<DfProcessProjectConfig> processes = dfProcessProjectConfigService.list(qw);
        List<DfProject> projects = dfProjectService.list();
        List<DfProjectColor> colors = dfProjectColorService.list();
        String[] dayOrNight = new String[]{"A", "B", "AB"};
        for (DfProjectColor color : colors) {
            for (DfProcessProjectConfig process : processes) {
                for (String dn : dayOrNight) {
                    if (process.getProject().contains(project) && color.getProjectName().contains(project)) {
                        getInfosLimit50(project, color.getColor(), process.getProcessName(), dn);
                    }
                }
            }
        }

    }

    public void getInfosLimit50(String project, String color, String process, String dayOrNight) throws ParseException {
        System.out.println(project + color + process + dayOrNight);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int startHour, endHour;
        if ("A".equals(dayOrNight)) {
            startHour = 0;
            endHour = 11;
        } else if ("B".equals(dayOrNight)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        // 获取当天到前7天的数据
        String startTime = TimeUtil.getLastSomeDay(TimeUtil.getNowTimeNoHour(), 14);
        String endTime = TimeUtil.getNowTimeByNormal();

        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = DfSizeCheckItemInfosService.listJoinDetailLimit50(project, color, process, startTime, endTime, startHour, endHour);
        Map<String, Map<String, List<Object>>> itemResValueMap = new LinkedHashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String itemName = dfSizeCheckItemInfo.getItemName();
            // 标准使用数据库的标准
            DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
            Object lslLimit = standardData.getIsolaLowerLimit();
            Object uslLimit = standardData.getIsolaUpperLimit();
            Object standardLimit = standardData.getStandard();
            if (!itemResValueMap.containsKey(itemName)) {
                Map<String, List<Object>> valueMap  = new HashMap<>();
                List<Object> checkValue = new ArrayList<>();
                List<Object> standardValue = new ArrayList<>();
                List<Object> usl = new ArrayList<>();
                List<Object> lsl = new ArrayList<>();
                List<Object> itemNameList = new ArrayList<>();
                List<Object> checkTimeList = new ArrayList<>();
                List<Object> checkTypeList = new ArrayList<>();
                itemNameList.add(itemName);
                checkValue.add(dfSizeCheckItemInfo.getCheckValue());
                standardValue.add(standardLimit);
                usl.add(uslLimit);
                lsl.add(lslLimit);
                checkTimeList.add(sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                checkTypeList.add(dfSizeCheckItemInfo.getCheckType());
                valueMap.put("checkValue", checkValue);
                valueMap.put("standardValue", standardValue);
                valueMap.put("usl", usl);
                valueMap.put("lsl", lsl);
                valueMap.put("name", itemNameList);
                valueMap.put("checkTime", checkTimeList);
                valueMap.put("checkType", checkTypeList);
                itemResValueMap.put(itemName, valueMap);
            } else {
                Map<String, List<Object>> valueMap = itemResValueMap.get(itemName);
                // 前一片和后一片一样的话就更新时间
//                if ((Double) valueMap.get("checkValue").get(valueMap.get("checkValue").size() - 1) - dfSizeCheckItemInfo.getCheckValue() == 0) {
//                    // 更新时间 和 类型
//                    valueMap.get("checkTime").set(valueMap.get("checkTime").size() - 1, sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
//                    valueMap.get("checkType").set(valueMap.get("checkType").size() - 1, dfSizeCheckItemInfo.getCheckType());
//                    continue;
//                }
                valueMap.get("checkValue").add(dfSizeCheckItemInfo.getCheckValue());
                valueMap.get("standardValue").add(standardLimit);
                valueMap.get("usl").add(uslLimit);
                valueMap.get("lsl").add(lslLimit);
                valueMap.get("checkTime").add(sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                valueMap.get("checkType").add(dfSizeCheckItemInfo.getCheckType());
                // 如果超过50组，就删除掉最老的一组
                if (valueMap.get("checkValue").size() > 50) {
                    valueMap.get("checkValue").remove(0);
                    valueMap.get("standardValue").remove(0);
                    valueMap.get("usl").remove(0);
                    valueMap.get("lsl").remove(0);
                    valueMap.get("checkTime").remove(0);
                    valueMap.get("checkType").remove(0);
                }
            }

        }

        List<Map<String, List<Object>>> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResValueMap.entrySet()) {
            Map<String, List<Object>> map = entry.getValue();
            // 拿最新的标准作为所有的标准
            int len = map.get("lsl").size();
            String ngItem = (String) map.get("name").get(0);
            // 标准使用数据库的标准
            DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + ngItem);
            Object lsl = standardData.getIsolaLowerLimit();
            Object usl = standardData.getIsolaUpperLimit();
            Object standardValue = standardData.getStandard();
            List<Object> lslList = new ArrayList<>();
            List<Object> uslList = new ArrayList<>();
            List<Object> standardValueList = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                lslList.add(lsl);
                uslList.add(usl);
                standardValueList.add(standardValue);
            }
            map.put("lsl", lslList);
            map.put("usl", uslList);
            map.put("standardValue", standardValueList);
            result.add(map);
        }

        String key = "size:limit50:" + project + ":" + color + ":" + process + ":" + dayOrNight + ":";
        System.out.println("插入redis:" + key);
        if (result.size() > 0) {
            redisUtil.delete(key);
            System.out.println("成功插入redis:" + key);
            redisUtil.saveListToRedis(key, result);
        }
    }

    @ApiOperation("插入正太分布数据")
    @GetMapping(value = "/insertNormalDistributionData")
    public Result getNormalDistributionCache() throws ParseException {
        System.out.println("正太分布图数据获取" + TimeUtil.getNowTimeByNormal());
        // 获取当天到前7天的数据
        String startTime = TimeUtil.getLastSomeDay(TimeUtil.getNowTimeNoHour(), 7);
        String endTime = TimeUtil.getNowTimeNoHour();

        List<Map<String, Object>> result;
        List<DfProjectColor> colors = dfProjectColorService.list();
        List<String> r = new ArrayList<>();
        for (DfProjectColor color : colors) {
            String c = color.getColor();
            result = DfSizeCheckItemInfosService.getNormalDistributionData(
                    "%%", "%%", c, startTime + " 07:00:00", TimeUtil.getNextDay(endTime) + " 07:00:00", 0, 23, 0, 9999
            );
            for (Map<String, Object> stringObjectMap : result) {
                String[] names = stringObjectMap.get("name").toString().split("-");
                String project = names[0];
                String process = names[1];
                String item = names[2];
                String key = "size:normalDistribution:" + c + ":" + project + ":" + process + ":" + item + ":" + startTime + ":" + endTime + ":AB";
                redisUtil.hmset(key, stringObjectMap, 60 * 60);
                System.out.println("正太分布图插入redis中：" + key);
                r.add("size:normalDistribution:" + c + ":" + project + ":" + process + ":" + item + ":" + startTime + ":" + endTime + ":AB");
            }
        }
        return new Result(200, "成功插入", r);
    }

//    @ApiOperation("更新工序近50组尺寸数据（存缓存）")
//    @GetMapping(value = "/updateNear50DataCache")
//    public void updateNear50DataCache() throws ParseException {
//        System.out.println("更新工序近50组尺寸数据（存缓存）");
//        QueryWrapper<DfProcessProjectConfig> qw = new QueryWrapper<>();
//        qw.like("type", "尺寸");
//        List<DfProcessProjectConfig> processes = dfProcessProjectConfigService.list(qw);
//        List<DfProject> projects = dfProjectService.list();
//        List<DfProjectColor> colors = dfProjectColorService.list();
//        String[] dayOrNights = new String[]{"A", "B", "AB"};
//        for (DfProject project : projects) {
//            for (DfProjectColor color : colors) {
//                for (DfProcessProjectConfig process : processes) {
//                    for (String dn : dayOrNight) {
//                        if (process.getProject().contains(project.getName()) && color.getProjectName().contains(project.getName())) {
//                            getInfosLimit50(project.getName(), color.getColor(), process.getProcessName(), dn);
//                        }
//                    }
//                }
//            }
//        }
//
//    }




}
