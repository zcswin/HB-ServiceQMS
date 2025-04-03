package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfDustMonitorService;
import com.ww.boengongye.service.DfDynamicIpqcAppearanceLogService;
import com.ww.boengongye.service.DfMachineService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2025-01-05
 */
@Controller
@RequestMapping("/dfDynamicIpqcAppearanceLog")
@CrossOrigin
@ResponseBody
@Api(tags = "外观动态状态记录")
public class DfDynamicIpqcAppearanceLogController {

    @Autowired
    private DfDynamicIpqcAppearanceLogService dfDynamicIpqcAppearanceLogService;

    @Autowired
    private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Autowired
    private DfMachineService dfMachineService;
    @Resource
    private RedisTemplate redisTemplate;


//    @GetMapping("/listByMachineCode2")
//    @ApiOperation("根据机台号获取时序图")
//    public Result listByMachineCode(String machineCode) {
//        List<List<String>> allTime = new ArrayList<>();
//        List<Map<String, Object>> allData = new ArrayList<>();
//
//        Map<String, String> color = new HashMap<>();
//        color.put("保养", "#fecdc6");
//        color.put("生产", "#87fa4d");
//        color.put("首检", "#75fbea");
//        color.put("抽检", "#fe9401");
//        color.put("风险批全检", "#004d7e");
//
//        List<Map<String, String>> legendData = new ArrayList<>();
//        Map<String, String> legendColor1 = new HashMap<>();
//        legendColor1.put("name", "保养");
//        legendColor1.put("color", "#fecdc6");
//        Map<String, String> legendColor2 = new HashMap<>();
//        legendColor2.put("name", "生产");
//        legendColor2.put("color", "#87fa4d");
//        Map<String, String> legendColor3 = new HashMap<>();
//        legendColor3.put("name", "首检");
//        legendColor3.put("color", "#75fbea");
//        Map<String, String> legendColor4 = new HashMap<>();
//        legendColor4.put("name", "抽检");
//        legendColor4.put("color", "#fe9401");
//        Map<String, String> legendColor5 = new HashMap<>();
//        legendColor5.put("name", "风险批全检");
//        legendColor5.put("color", "#004d7e");
//
//
//        legendData.add(legendColor1);
//        legendData.add(legendColor2);
//        legendData.add(legendColor3);
//        legendData.add(legendColor4);
//        legendData.add(legendColor5);
//
//
//        Map<Integer, Integer> listIndex = new HashMap<>();
//        listIndex.put(4, 0);
//        listIndex.put(3, 1);
//        listIndex.put(2, 2);
//        listIndex.put(1, 3);
//        listIndex.put(0, 4);
//
//        for (int i = 4; i >= 0; i--) {
//            QueryWrapper<DfDynamicIpqcAppearanceLog> qw = new QueryWrapper<>();
//            qw.eq("machine_code", machineCode);
//            qw.between("start_date", TimeUtil.getBeforeDay(i) + " 07:00:00", TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
//            qw.orderByAsc("end_date");
//            List<DfDynamicIpqcAppearanceLog> logs = dfDynamicIpqcAppearanceLogService.list(qw);
//            List<String> dayTimes = new ArrayList<>();
//
//            Map<String, Object> firstData = new HashMap<>();
//            firstData.put("statusColor", color.get("保养"));
//            firstData.put("status", "保养");
//            firstData.put("time", "00:07:00");
//            firstData.put("endTime", TimeUtil.getBeforeDay(i) + " 00:07:30");
//            firstData.put("startTime", TimeUtil.getBeforeDay(i) + " 00:07:00");
//            firstData.put("day", TimeUtil.getBeforeDay(i));
//            firstData.put("dayNum", i);
//            List<Integer> secondList = new ArrayList<>();
//            secondList.add(0);
//            secondList.add(0);
//            secondList.add(0);
//            secondList.add(0);
//            secondList.add(0);
//            secondList.set(listIndex.get(i), 1800);
//            firstData.put("data", secondList);
//            allData.add(firstData);
//
//            dayTimes.add("00:07:00");
//            if (logs.size() > 0) {
//                for (DfDynamicIpqcAppearanceLog d : logs) {
//                    Map<String, Object> fData = new HashMap<>();
//                    fData.put("statusColor", color.get(d.getStatus()));
//                    fData.put("status", d.getStatus());
//                    fData.put("time", TimeUtil.convertToTimeOnly(d.getStartDate()));
//                    fData.put("endTime", d.getEndDate());
//                    fData.put("startTime", d.getStartDate());
//                    fData.put("day", TimeUtil.getBeforeDay(i));
//                    fData.put("dayNum", i);
//                    List<Integer> fsecondList = new ArrayList<>();
//                    fsecondList.add(0);
//                    fsecondList.add(0);
//                    fsecondList.add(0);
//                    fsecondList.add(0);
//                    fsecondList.add(0);
//                    fsecondList.set(listIndex.get(i), d.getDurationTime());
//                    fData.put("data", fsecondList);
//                    allData.add(fData);
//                    dayTimes.add(TimeUtil.convertToTimeOnly(d.getStartDate()));
//                }
//                long dayLastTime = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.formatTimestamp(logs.get(logs.size() - 1).getEndDate()), TimeUtil.getBeforeDay(i) + " 23:59:59");
//                if (dayLastTime > 0 && i != 0) {
//                    Map<String, Object> endData = new HashMap<>();
//                    endData.put("statusColor", color.get("生产"));
//                    endData.put("status", "生产");
//                    endData.put("time", "23:59:59");
//                    endData.put("endTime", TimeUtil.getBeforeDay(i) + " 23:59:59");
//                    endData.put("startTime", logs.get(logs.size() - 1).getEndDate());
//                    endData.put("day", TimeUtil.getBeforeDay(i));
//                    endData.put("dayNum", i);
//                    List<Integer> endSecondList = new ArrayList<>();
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.set(listIndex.get(i), (int) dayLastTime);
//                    endData.put("data", endSecondList);
//                    allData.add(endData);
//                    dayTimes.add("23:59:59");
//                }
//
//
//            } else {
//                long dayLastTime = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.getBeforeDay(i) + " 00:07:30", TimeUtil.getBeforeDay(i) + " 23:59:59");
//                if (dayLastTime > 0 && i != 0) {
//                    Map<String, Object> endData = new HashMap<>();
//                    endData.put("statusColor", color.get("生产"));
//                    endData.put("status", "生产");
//                    endData.put("time", "23:59:59");
//                    endData.put("endTime", TimeUtil.getBeforeDay(i) + " 23:59:59");
//                    endData.put("startTime", TimeUtil.getBeforeDay(i) + " 00:07:30");
//                    endData.put("day", TimeUtil.getBeforeDay(i));
//                    endData.put("dayNum", i);
//                    List<Integer> endSecondList = new ArrayList<>();
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.add(0);
//                    endSecondList.set(listIndex.get(i), (int) dayLastTime);
//                    endData.put("data", endSecondList);
//                    allData.add(endData);
//                    dayTimes.add("23:59:59");
//                }
//            }
//
//            allTime.add(dayTimes);
//
//        }
//
//        //补充生产的数据
//        List<Map<String, Object>> resultList = new ArrayList<>();
//        for (int i = 0; i < allData.size(); i++) {
//            // 添加当前对象到结果列表
//            resultList.add(allData.get(i));
//
//            // 如果当前对象不是最后一个,同一天的数据
//            if (i < allData.size() - 1 && allData.get(i).get("day").equals(allData.get(i + 1).get("day")) && !Objects.equals(allData.get(i + 1).get("status").toString(), "生产")) {
//                long diff = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.formatTimestamp(Timestamp.valueOf(allData.get(i).get("endTime").toString())), TimeUtil.formatTimestamp(Timestamp.valueOf(allData.get(i + 1).get("startTime").toString())));
//                if (diff > 0) {
//                    Map<String, Object> productData = new HashMap<>();
//                    productData.put("statusColor", color.get("生产"));
//                    productData.put("status", "生产");
//                    productData.put("time", TimeUtil.convertToTimeOnly(Timestamp.valueOf(allData.get(i).get("endTime").toString())));
////                    endData.put("endTime",TimeUtil.getBeforeDay(i)+" 23:59:59");
////                    endData.put("day",TimeUtil.getBeforeDay(i));
//                    List<Integer> productSecondList = new ArrayList<>();
//                    productSecondList.add(0);
//                    productSecondList.add(0);
//                    productSecondList.add(0);
//                    productSecondList.add(0);
//                    productSecondList.add(0);
//                    productSecondList.set(listIndex.get(Integer.parseInt(allData.get(i).get("dayNum").toString())), (int) diff);
//                    productData.put("data", productSecondList);
//                    resultList.add(productData); // 在当前和下一个对象之间插入
//                }
//
//
//            }
//        }
//
//        //组装返回对象
//        Map<String, Object> result = new HashMap<>();
//        result.put("seriesData", resultList);
//        result.put("time", allTime);
//        result.put("legendData", legendData);
//        result.put("machineCode", machineCode);
//
//
//        return new Result(200, "查询成功", result);
//    }
//

    @GetMapping("/listByMachineCode")
    @ApiOperation("根据机台号获取时序图")
    public Result listByMachineCode2(String machineCode) {
        List<List<String>> allTime = new ArrayList<>();
        List<Map<String, Object>> allData = new ArrayList<>();

        Map<String, String> color = new HashMap<>();
        color.put("保养", "#fecdc6");
        color.put("生产", "#87fa4d");
        color.put("首检", "#75fbea");
        color.put("抽检", "#fe9401");
        color.put("风险批全检", "#004d7e");
        color.put("停机", "#ffffff");


        List<Map<String, String>> legendData = new ArrayList<>();
        Map<String, String> legendColor1 = new HashMap<>();
        legendColor1.put("name", "保养");
        legendColor1.put("color", "#fecdc6");
        Map<String, String> legendColor2 = new HashMap<>();
        legendColor2.put("name", "生产");
        legendColor2.put("color", "#87fa4d");
        Map<String, String> legendColor3 = new HashMap<>();
        legendColor3.put("name", "首检");
        legendColor3.put("color", "#75fbea");
        Map<String, String> legendColor4 = new HashMap<>();
        legendColor4.put("name", "抽检");
        legendColor4.put("color", "#fe9401");
        Map<String, String> legendColor5 = new HashMap<>();
        legendColor5.put("name", "风险批全检");
        legendColor5.put("color", "#004d7e");

        Map<String, String> legendColor6 = new HashMap<>();
        legendColor6.put("name", "停机");
        legendColor6.put("color", "#ffffff");


        legendData.add(legendColor1);
        legendData.add(legendColor2);
        legendData.add(legendColor3);
        legendData.add(legendColor4);
        legendData.add(legendColor5);
        legendData.add(legendColor6);


        Map<Integer, Integer> listIndex = new HashMap<>();
        listIndex.put(4, 0);
        listIndex.put(3, 1);
        listIndex.put(2, 2);
        listIndex.put(1, 3);
        listIndex.put(0, 4);

        for (int i = 4; i >= 0; i--) {
            QueryWrapper<DfDynamicIpqcAppearanceLog> qw = new QueryWrapper<>();
            qw.eq("machine_code", machineCode);
            qw.between("start_date", TimeUtil.getBeforeDay(i) + " 07:00:00", TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
            qw.orderByAsc("end_date");
            List<DfDynamicIpqcAppearanceLog> logs = dfDynamicIpqcAppearanceLogService.list(qw);
            List<String> dayTimes = new ArrayList<>();

            Map<String, Object> firstData = new HashMap<>();
            firstData.put("statusColor", color.get("保养"));
            firstData.put("status", "保养");
            firstData.put("time", "07:00:00");
            firstData.put("endTime", TimeUtil.getBeforeDay(i) + " 07:30:00");
            firstData.put("startTime", TimeUtil.getBeforeDay(i) + " 07:00:00");
            firstData.put("day", TimeUtil.getBeforeDay(i));
            firstData.put("dayNum", i);
            List<Integer> secondList = new ArrayList<>();
            secondList.add(0);
            secondList.add(0);
            secondList.add(0);
            secondList.add(0);
            secondList.add(0);
            secondList.set(listIndex.get(i), 1800);
            firstData.put("data", secondList);
            allData.add(firstData);

            dayTimes.add("00:07:00");
            if (logs.size() > 0) {
                for (DfDynamicIpqcAppearanceLog d : logs) {
                    Map<String, Object> fData = new HashMap<>();
                    fData.put("statusColor", color.get(d.getStatus()));
                    fData.put("status", d.getStatus());
                    fData.put("time", TimeUtil.convertToTimeOnly(d.getStartDate()));
                    fData.put("endTime", TimeUtil.formatTimestamp(d.getEndDate()));
                    fData.put("startTime", TimeUtil.formatTimestamp(d.getStartDate()));
                    fData.put("day", TimeUtil.getBeforeDay(i));
                    fData.put("dayNum", i);
                    List<Integer> fsecondList = new ArrayList<>();
                    fsecondList.add(0);
                    fsecondList.add(0);
                    fsecondList.add(0);
                    fsecondList.add(0);
                    fsecondList.add(0);
                    fsecondList.set(listIndex.get(i), d.getDurationTime());
                    fData.put("data", fsecondList);
                    allData.add(fData);
                    dayTimes.add(TimeUtil.convertToTimeOnly(d.getStartDate()));
                }
                long dayLastTime = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.formatTimestamp(logs.get(logs.size() - 1).getEndDate()), TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
                if (dayLastTime > 0 && i != 0) {
                    Map<String, Object> endData = new HashMap<>();
                    endData.put("statusColor", color.get("生产"));
                    endData.put("status", "生产");
//                    endData.put("time", "23:59:59");
                    endData.put("time", "07:00:00");
                    endData.put("endTime", TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
                    endData.put("startTime", logs.get(logs.size() - 1).getEndDate());
                    endData.put("day", TimeUtil.getBeforeDay(i - 1));
                    endData.put("dayNum", i);
                    List<Integer> endSecondList = new ArrayList<>();
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.set(listIndex.get(i), (int) dayLastTime);
                    endData.put("data", endSecondList);
                    allData.add(endData);
                    dayTimes.add("23:59:59");
                }


            } else {
                long dayLastTime = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.getBeforeDay(i) + " 07:30:00", TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
                if (dayLastTime > 0 && i != 0) {
                    Map<String, Object> endData = new HashMap<>();
                    endData.put("statusColor", color.get("生产"));
                    endData.put("status", "生产");
//                    endData.put("time", "23:59:59");
                    endData.put("time", "07:00:00");
                    endData.put("endTime", TimeUtil.getBeforeDay(i - 1) + " 07:00:00");
                    endData.put("startTime", TimeUtil.getBeforeDay(i) + " 07:30:00");
                    endData.put("day", TimeUtil.getBeforeDay(i - 1));
                    endData.put("dayNum", i);
                    List<Integer> endSecondList = new ArrayList<>();
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.add(0);
                    endSecondList.set(listIndex.get(i), (int) dayLastTime);
                    endData.put("data", endSecondList);
                    allData.add(endData);
                    dayTimes.add("23:59:59");
                }
            }

            allTime.add(dayTimes);

        }

        //补充生产的数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < allData.size(); i++) {
            // 添加当前对象到结果列表
            resultList.add(allData.get(i));

            // 如果当前对象不是最后一个,同一天的数据
            if (i < allData.size() - 1 && allData.get(i).get("dayNum").equals(allData.get(i + 1).get("dayNum")) && !Objects.equals(allData.get(i + 1).get("status").toString(), "生产")) {
//                long diff=TimeUtil.getTimeDifferenceInSeconds(TimeUtil.formatTimestamp( Timestamp.valueOf(allData.get(i).get("endTime").toString())),TimeUtil.formatTimestamp( Timestamp.valueOf(allData.get(i+1).get("startTime").toString())));
//                System.out.println(allData.get(i).get("endTime").toString());
//                System.out.println(allData.get(i + 1).get("startTime").toString());
                long diff = TimeUtil.getTimeDifferenceInSeconds(allData.get(i).get("endTime").toString(), allData.get(i + 1).get("startTime").toString());
                System.out.println(diff);
                if (diff > 0) {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("statusColor", color.get("生产"));
                    productData.put("status", "生产");
//                    productData.put("time",TimeUtil.convertToTimeOnly(Timestamp.valueOf(allData.get(i).get("endTime").toString()) ));
                    productData.put("time", allData.get(i).get("endTime"));
//                    endData.put("endTime",TimeUtil.getBeforeDay(i)+" 23:59:59");
//                    endData.put("day",TimeUtil.getBeforeDay(i));
                    List<Integer> productSecondList = new ArrayList<>();
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.set(listIndex.get(Integer.parseInt(allData.get(i).get("dayNum").toString())), (int) diff);
                    productData.put("data", productSecondList);
                    resultList.add(productData); // 在当前和下一个对象之间插入
                }


            }

            //补充当前时间段
            if (i == allData.size() - 1) {
                long diff = TimeUtil.getTimeDifferenceInSeconds(TimeUtil.formatTimestamp(Timestamp.valueOf(allData.get(i).get("endTime").toString())), TimeUtil.getNowTimeByNormal());
                if (diff > 0) {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("statusColor", color.get("生产"));
                    productData.put("status", "生产");
                    productData.put("time", TimeUtil.convertToTimeOnly(Timestamp.valueOf(allData.get(i).get("endTime").toString())));
//                    endData.put("endTime",TimeUtil.getBeforeDay(i)+" 23:59:59");
//                    endData.put("day",TimeUtil.getBeforeDay(i));
                    List<Integer> productSecondList = new ArrayList<>();
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.add(0);
                    productSecondList.set(listIndex.get(Integer.parseInt(allData.get(i).get("dayNum").toString())), (int) diff);
                    productData.put("data", productSecondList);
                    resultList.add(productData);
                }
            }
        }

        //组装返回对象
        Map<String, Object> result = new HashMap<>();
        result.put("seriesData", resultList);
        result.put("time", allTime);
        result.put("legendData", legendData);
        result.put("machineCode", machineCode);


        return new Result(200, "查询成功", result);
    }


    @GetMapping("/updateMAC")
    @ApiOperation("")
    public Result updateMAC(String time, String project, String process) {
        QueryWrapper<DfQmsIpqcWaigTotal> qw = new QueryWrapper<>();
        qw.eq("f_seq", process);
        qw.eq("f_bigpro", project);
        qw.like("f_test_category", "首检");
        qw.gt("f_time", time);
        List<DfQmsIpqcWaigTotal> datas = dfQmsIpqcWaigTotalService.list(qw);
        RedisUtils redis = new RedisUtils();
        Set<String> appearanceDatas = redisTemplate.keys("IpqcAppearance*");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (datas.size() > 0) {
            for (DfQmsIpqcWaigTotal d : datas) {
                Object v = valueOperations.get("IpqcAppearance:"+d.getfMac());
                if (null != v) {
                    DynamicIpqcMac dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                    if (null != dim) {
                        if (null!=dim.getOperationStatus()&&dim.getOperationStatus().equals("停机")) {



                            DfDynamicIpqcAppearanceLog statusLog = new DfDynamicIpqcAppearanceLog();
                            statusLog.setStatus("首检");
                            statusLog.setProcess(d.getfSeq());
                            statusLog.setProject(d.getfBigpro());
                            statusLog.setDurationTime((int) TimeUtil.getTimeDifferenceInSeconds(dim.getLastOperationTime(), TimeUtil.formatTimestamp(d.getfTime())));
                            statusLog.setMachineCode(d.getfMac());
                            statusLog.setStartDate(Timestamp.valueOf(dim.getLastOperationTime()));
                            statusLog.setEndDate(d.getfTime());
                            dfDynamicIpqcAppearanceLogService.save(statusLog);
                            dim.setInfoStatus("");
                            dim.setInfoStatusTime("");
                            dim.setOperationStatus("");
                            dim.setLastOperationTime("");
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            redisTemplate.opsForValue().set("IpqcAppearance:" + dim.getMachineCode(), gson.toJson(dim));


                        }
                    }


                }
            }
        }


        return new Result(200, "查询成功");

    }
    @GetMapping("/insertStopMac")
    @ApiOperation("补充停机机台")
    public Result insertStopMac(String project, String process) {
            QueryWrapper<DfMachine>qw=new QueryWrapper<>();
            qw.eq("project",project);
            qw.eq("process_code",process);
     List<DfMachine>   macs=dfMachineService.list(qw);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            for(DfMachine d:macs){
                if (!redisTemplate.hasKey("IpqcAppearance:" + d.getCode())) {

                        DynamicIpqcMac dim = new DynamicIpqcMac();
                            dim.setOperationStatus("停机");
                            dim.setLastOperationTime(TimeUtil.getNowTimeByNormal());
                            dim.setStatus("常规");
                            dim.setNgCount(0);
                            dim.setTotalCount(0);
                            dim.setAppearanceOkCount(0);
                            dim.setSpecifiedCount(0);
                            dim.setSizeOkCount(0);
                            dim.setZugammenCount(0);
                            dim.setSpcOkCount(0);
                            dim.setCpkCount(0);
                            dim.setFourPointOverOne(0);
                            dim.setTwoPointOverTwo(0);
                            dim.setZugammenCount(0);
                            dim.setNowCount(0);
                            dim.setFrequency(3.0);

                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                            dim.setRuleName("QCP抽检频率");
                            dim.setMachineCode(d.getCode());

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            redisTemplate.opsForValue().set("IpqcAppearance:" + d.getCode(), gson.toJson(dim));




                }
            }

            return new Result(500, "操作失败");
        }

    @ApiOperation("调整IPQC机台持续时间")
    @GetMapping(value = "/updateIPQCMacDurationTime")
    @Transactional(rollbackFor = Exception.class)
    public Object updateIPQCMacDurationTime(
            String machineCode,String process,String project,
            String startDate, String endDate
    ) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        QueryWrapper<DfDynamicIpqcAppearanceLog> qw = new QueryWrapper<>();

        qw
                .eq(StringUtils.isNotEmpty(machineCode), "machine_code", machineCode)
                .eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(project), "project", project)
                .between("start_date", startTime, endTime);
        List<DfDynamicIpqcAppearanceLog> list = dfDynamicIpqcAppearanceLogService.list(qw);

        if (list == null || list.size() == 0){
            return new Result(500, "当前条件没有相关IPQC机台数据");
        }

        List<DfDynamicIpqcAppearanceLog> updateList = new ArrayList<>();

        for (DfDynamicIpqcAppearanceLog dfDynamicIpqcAppearanceLog : list) {
            //开始时间
            Timestamp startCheckTime = dfDynamicIpqcAppearanceLog.getStartDate();
            //结束时间
            Timestamp endCheckTime = dfDynamicIpqcAppearanceLog.getEndDate();

            // 计算时间差（以分钟为单位）
            long timeDifferenceInMinutes = Math.abs(endCheckTime.getTime() - startCheckTime.getTime()) / (60 * 1000);

            // 如果时间差超过 40 分钟
            if (timeDifferenceInMinutes > 40) {
                // 随机生成 20 到 40 分钟之间的调整值
                long adjustment = 20 + (long) (Math.random() * 21);
                endCheckTime = new Timestamp(startCheckTime.getTime() + TimeUnit.MINUTES.toMillis(adjustment)) ;

                Integer durationTime = Math.round(adjustment * 60);

                dfDynamicIpqcAppearanceLog.setDurationTime(durationTime);
                dfDynamicIpqcAppearanceLog.setEndDate(endCheckTime);
                dfDynamicIpqcAppearanceLog.setCreateTime(endCheckTime);
                updateList.add(dfDynamicIpqcAppearanceLog);
            }
        }
        if (updateList.size() == 0){
            return new Result(500, "当前条件没有需要调整的IPQC机台数据");
        }

        dfDynamicIpqcAppearanceLogService.updateBatchById(updateList);
        return new Result(200, MessageFormat.format("调整了{0}条IPQC机台时间数据", updateList.size()));
    }


    @ApiOperation("补录IPQC机台数据")
    @GetMapping(value = "/supplementIPQCMacTimeData")
    @Transactional(rollbackFor = Exception.class)
    public Object listPcsWgBim(
            String machineCode,String process,String project,String hour,
            String startDate, String endDate
    ) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        if (StringUtils.isNotEmpty(hour)){
            Integer hourInt = Integer.parseInt(hour);
            if (hourInt > 24){
                return new Result(500, "小时数不能超过24");
            }
            startTime = String.format("%s %02d:00:00", startDate, hourInt);
            endTime = String.format("%s %02d:59:59", startDate, hourInt);
        }

        QueryWrapper<DfQmsIpqcWaigTotal> qw = new QueryWrapper<>();

        qw
                .eq(StringUtils.isNotEmpty(machineCode), "f_mac", machineCode)
                .eq(StringUtils.isNotEmpty(process), "f_seq", process)
                .eq(StringUtils.isNotEmpty(project), "f_bigpro", project)
                .between("f_time", startTime, endTime);
        List<DfQmsIpqcWaigTotal> list = dfQmsIpqcWaigTotalService.list(qw);

        if (list == null || list.size() == 0){
            return new Result(500, "当前条件没有相关外观机数据");
        }

        List<DfDynamicIpqcAppearanceLog> saveList = new ArrayList<>();

        for (DfQmsIpqcWaigTotal datas : list) {
            DfDynamicIpqcAppearanceLog statusLog=new DfDynamicIpqcAppearanceLog();

            statusLog.setProcess(datas.getfSeq());
            statusLog.setProject(datas.getfBigpro());
            statusLog.setMachineCode(datas.getfMac());

            String fTime = dateFormat.format(datas.getfTime());

            String randomTime=null;
            if(datas.getfTestCategory().contains("首检")){
                statusLog.setStatus("首检");
                randomTime= TimeUtil.getRandomTimeInRange(fTime,10,20);
            }else  if(datas.getStatus().equals("NG")){
                statusLog.setStatus("风险批全检");
                randomTime= TimeUtil.getRandomTimeInRange(fTime,20,40);
            }else{
                statusLog.setStatus("抽检");
                randomTime= TimeUtil.getRandomTimeInRange(fTime,10,20);
            }
            statusLog.setDurationTime((int)TimeUtil.getTimeDifferenceInSeconds(randomTime,fTime));
            statusLog.setMachineCode(datas.getfMac());
            statusLog.setStartDate(Timestamp.valueOf(randomTime));
            statusLog.setEndDate(Timestamp.valueOf(fTime));
            statusLog.setCreateTime(statusLog.getEndDate());
            saveList.add(statusLog);
        }
        dfDynamicIpqcAppearanceLogService.saveBatch(saveList);
        return new Result(200, MessageFormat.format("补录IPQC机台数据{0}条", saveList.size()));
    }





    @PostMapping("/insert")
    public Result insert(@RequestBody(required = false) DfDynamicIpqcAppearanceLog data) {
        data.setDurationTime(TimeUtil.getSecondsDifferenceInt(TimeUtil.formatTimestamp(data.getStartDate()),TimeUtil.formatTimestamp(data.getEndDate())));
        dfDynamicIpqcAppearanceLogService.save(data);
        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        dfDynamicIpqcAppearanceLogService.removeById(id);
        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody(required = false) DfDynamicIpqcAppearanceLog data) {
        data.setDurationTime(TimeUtil.getSecondsDifferenceInt(TimeUtil.formatTimestamp(data.getStartDate()),TimeUtil.formatTimestamp(data.getEndDate())));
        dfDynamicIpqcAppearanceLogService.updateById(data);
        return Result.UPDATE_SUCCESS;
    }


    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String machineCode,String project,String process,String startTime,String endTime) {
        Page<DfDynamicIpqcAppearanceLog> pages = new Page<DfDynamicIpqcAppearanceLog>(page, limit);
        QueryWrapper<DfDynamicIpqcAppearanceLog> ew=new QueryWrapper<DfDynamicIpqcAppearanceLog>();

        if(null!=machineCode&&!machineCode.equals("")) {
            ew.eq("machine_code", machineCode);
        }
        if(null!=process&&!process.equals("")) {
            ew.like("process", process);
        }
        if(null!=project&&!project.equals("")) {
            ew.like("project", project);
        }
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("create_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("create_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("create_time", endTime);
        }

        ew.orderByDesc("create_time");



        IPage<DfDynamicIpqcAppearanceLog> list=dfDynamicIpqcAppearanceLogService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }




}
