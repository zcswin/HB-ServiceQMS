//package com.ww.boengongye.controller;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ww.boengongye.entity.*;
//import com.ww.boengongye.mapper.DfTzCheckItemInfosMapper;
//import com.ww.boengongye.service.*;
//import com.ww.boengongye.utils.RedisUtils;
//import com.ww.boengongye.utils.Result;
//import com.ww.boengongye.utils.TxtToObject;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.json.JsonParseException;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * trace大数据平台同步数据表 前端控制器
// * </p>
// *
// * @author guangyao
// * @since 2024-04-22
// */
//@Controller
//@RequestMapping("/dfTraceSyncData")
//@CrossOrigin
//@ResponseBody
//@Slf4j
//@Api(tags = "trace大数据平台")
//public class DfTraceSyncDataController {
//
//    @Autowired
//    private DfTraceSyncDataService dfTraceSyncDataService;
//
//    @Autowired
//    private DfTzDetailService dfTzDetailService;
//
//    @Autowired
//    private DfTzCheckItemInfosService dfTzCheckItemInfosService;
//
//    @Autowired
//    private DfIsmCheckItemInfosService dfIsmCheckItemInfosService;
//
//    @Autowired
//    private DfLeadDetailService dfLeadDetailService;
//
//    @Autowired
//    private DfLeadCheckItemInfosService dfLeadCheckItemInfosService;
//
//    @Autowired
//    private DfTzCheckItemInfosMapper dfTzCheckItemInfosMapper;
//
//    @Autowired
//    private RedisUtils redisUtils;
//
//    @Value("${LineBody}")
//    private String lineBody;
//
//    private static final SimpleDateFormat[] SUPPORTED_INPUT_FORMATTERS = {
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    };
//    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    /**
//     * 用法:
//     * String dateTime1 = "2024-04-12 06:15:24.818000";
//     * String dateTime2 = "2024-03-29 10:19:16";
//     *
//     * String formattedDateTime1 = DateTimeUtil.formatDateTime(dateTime1);
//     * String formattedDateTime2 = DateTimeUtil.formatDateTime(dateTime2);
//     *
//     * System.out.println(formattedDateTime1); // 输出: 2024-04-12 06:15:24
//     * System.out.println(formattedDateTime2); // 输出: 2024-03-29 10:19:16
//     * @param dateTimeString
//     * @return
//     */
//    public static String formatDateTime(String dateTimeString) {
//        for (SimpleDateFormat sdf : SUPPORTED_INPUT_FORMATTERS) {
//            try {
//                SimpleDateFormat standard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = sdf.parse(dateTimeString);
//                return standard.format(date);
//            }  catch (ParseException e) {
//                //尝试下一种格式
//			}
//		}
//        // 如果所有格式都无法解析,则返回原始字符串
//        return dateTimeString;
//    }
//
//
//    /**
//     * 一小时一次
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/saveTzTraceSynData")
//    @ApiOperation("获取trace表中TZ数据并保存")
//    @Transactional
//    @Scheduled(fixedRate = 3600000) //一小时
//    public Result saveTzTraceSynData(){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        //隔一小时取最新的3000条数据
//        QueryWrapper<DfTraceSyncData> qw = new QueryWrapper<>();
//        qw
//                .eq("process_code","tz")
//                .orderByAsc("created_time")
//                .last("limit 3000");
//        List<DfTraceSyncData> dfTraceSyncDataList = dfTraceSyncDataService.list(qw);
//
//        //定义一个Trance待删除的idList
//        List<String> idList = new ArrayList<>();
//        //定义了一个异常情况Trace添加失败的集合
//        List<String> failList = new ArrayList<>();
//
//        String barcode = "";
//        String itemId = "";
//
//        //计数器
//        int count = dfTraceSyncDataList.size();
//        //成功数
//        int successCount = 0;
//        //失败数
//        int failCount = 0;
//        //开始时间
//        LocalDateTime start = LocalDateTime.now();
//        log.info("添加ism数据成功:开始时间:{}",start);
//
//        for(DfTraceSyncData item:dfTraceSyncDataList){
//            try {
//                //条码
//                barcode = item.getCode();
//                //id
//                itemId = item.getId();
//                //班别
//                String classes = "A班";
//                //检测时间
//                String createdTimeStr = item.getCreatedTime();
//                String checkTimeStr = formatDateTime(createdTimeStr);
//                LocalDateTime dateTime = LocalDateTime.parse(checkTimeStr, formatter);
//
//                Timestamp checkTime = Timestamp.valueOf(dateTime);
//
//                //测试结果
//                String leadResult = "pass".equals(item.getEvent()) ? "OK" : "NG";
//                //原始信息（json）
//                String logs = item.getLogs();
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonNode = objectMapper.readTree(logs);
//
//                JsonNode resultsNode = jsonNode.at("/data/insight/results");
//                JsonNode testStationAttributes = jsonNode.at("/data/insight/test_station_attributes");
//                String positionId = testStationAttributes.get("fixture_id").asText();
//
//                List<DfLeadCheckItemInfos> dfLeadCheckItemInfosList = new ArrayList<>();
//                if (resultsNode.isArray()) {
//                    for (JsonNode resultNode : resultsNode) {
//                        String test = resultNode.get("test").asText();
//                        String result = resultNode.get("result").asText();
//
//                        String valueString = resultNode.get("value") == null ? "" : resultNode.get("value").asText();
//                        Double value = parseStringToDouble(valueString);
//                        String lowerString = resultNode.get("lower_limit") == null ? "" : resultNode.get("lower_limit").asText();
//                        Double lower_limit = parseStringToDouble(lowerString);
//                        String upperString = resultNode.get("upper_limit") == null ? "" : resultNode.get("upper_limit").asText();
//                        Double upper_limit = parseStringToDouble(upperString);
//
//
//                        DfLeadCheckItemInfos dfLeadCheckItemInfos = new DfLeadCheckItemInfos();
//                        dfLeadCheckItemInfos.setCheckResult(result.equals("pass") ? "OK" : "NG");
//                        dfLeadCheckItemInfos.setCheckTime(checkTime);
//                        dfLeadCheckItemInfos.setCheckValue(Double.isNaN(value) ? null : value);
//                        dfLeadCheckItemInfos.setItemName(test);
//                        dfLeadCheckItemInfos.setLsl(Double.isNaN(lower_limit) ? null : lower_limit);
//                        dfLeadCheckItemInfos.setUsl(Double.isNaN(upper_limit) ? null : upper_limit);
//                        dfLeadCheckItemInfos.setCheckType(1); //1尺寸；2外观
//                        dfLeadCheckItemInfosList.add(dfLeadCheckItemInfos);
//                    }
//                }
//
//                DfLeadDetail dfLeadDetail = new DfLeadDetail();
//                dfLeadDetail.setCheckTime(checkTime);
//                dfLeadDetail.setSn(barcode);
//                dfLeadDetail.setResult(leadResult);
//                dfLeadDetail.setWorkPosition(positionId);
//                dfLeadDetail.setMachineCode(item.getMachineId());
//
//                if (redisUtils.hasKey(barcode)) {
//                    dfLeadDetail.setCheckType(2);
//
//                    Integer id = Integer.parseInt(redisUtils.get(barcode).toString());
//                    dfLeadDetailService.removeById(id);
//
//                    QueryWrapper<DfLeadCheckItemInfos> dfLeadCheckItemInfosQw = new QueryWrapper<>();
//                    dfLeadCheckItemInfosQw.eq("check_id", id);
//                    dfLeadCheckItemInfosService.remove(dfLeadCheckItemInfosQw);
//                } else {
//                    dfLeadDetail.setCheckType(1);
//                }
//                dfLeadDetailService.save(dfLeadDetail);
//                Integer leadId = dfLeadDetail.getId();
//
//                for (DfLeadCheckItemInfos dfLeadCheckItemInfos : dfLeadCheckItemInfosList) {
//                    dfLeadCheckItemInfos.setCheckId(leadId);
//                }
//                dfLeadCheckItemInfosService.saveBatch(dfLeadCheckItemInfosList);
//
//                ++successCount;
//                //添加到集合并放到map中
//                idList.add(itemId);
//                redisUtils.set(barcode,leadId, 3 * 24 * 60 * 60);
//            }catch (Exception e){
//                ++failCount;
//                failList.add(itemId);
//            }finally {
//                //打印进度条
//                printProgressBar((successCount + failCount) * 100 / count,"插入");
//            }
//        }
//
//        HashMap<String, Object> resultMap = new HashMap<>();
//        resultMap.put("成功" , idList);
//        resultMap.put("失败" , failList);
//
//        if (!CollectionUtils.isEmpty(idList)){
//            //分批删除,一次删除100条
//            int batchSize = 100;
//            int total = idList.size();
//
//            int batches = (int) Math.ceil((double) total / batchSize);
//            for (int i = 0; i < batches; i++) {
//                int fromIndex = i * batchSize;
//                int toIndex = Math.min((i + 1) * batchSize, total);
//
//                List<String> batchIds = idList.subList(fromIndex, toIndex);
//                dfTraceSyncDataService.removeByIds(batchIds);
//                //打印删除进度条
//                printProgressBar( (i + 1) * 100 / batches , "删除");
//            }
//        }
//
//        //结束时间
//        LocalDateTime end = LocalDateTime.now();
//        long minutes = Duration.between(start, end).toMinutes();
//        Object[] obj = {start,end,minutes,count,successCount,failCount};
//        log.info("添加ism数据成功:开始时间:{},结束时间:{},耗时:{}分钟,总数:{},成功{},失败{}",obj);
//        return new Result(200, "添加ism数据成功,耗时:" + minutes + "分钟",resultMap,successCount);
//    }
//
//
//
//    @GetMapping("/saveTzTraceSynData")
//    @ApiOperation("获取trace表中TZ数据并保存")
//    @Scheduled(fixedRate = 3600000) //一小时
//    @Transactional
////    @Scheduled(fixedRate = 30000)//测试
//    public Result saveTzTraceSynData() throws Exception {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Map<String, Integer> snResTzIdMap = new HashMap<>();
//
//        QueryWrapper<DfTraceSyncData> qw = new QueryWrapper<>();
//        qw.eq("process_code","tz");
//        List<DfTraceSyncData> dfTraceSyncDataList = dfTraceSyncDataService.list(qw);
//        List<String> idList = dfTraceSyncDataList.stream().map(DfTraceSyncData::getId).collect(Collectors.toList());
//
////        List<DfTraceSyncData> list = dfTraceSyncDataList.subList(0,3);
//
//        //计数器
//        int count = 0;
//        String barcode = "";
//        String itemId = "";
//
//        for(DfTraceSyncData item:dfTraceSyncDataList){
//            //条码
//            barcode = item.getCode();
//            //id
//            itemId = item.getId();
//            //班别
//            String classes = "A班";
//            //测试时间
//            Timestamp checkTime = Timestamp.valueOf(item.getCreatedTime());
//            LocalTime time = checkTime.toLocalDateTime().toLocalTime();
//            LocalTime startTime = LocalTime.parse("07:00:00");
//            LocalTime endTime = LocalTime.parse("19:00:00");
//
//            if(time.isBefore(startTime)||time.isAfter(endTime)){
//                classes = "B班";
//            }
//            //测试结果
//            String tzResult = "pass".equals(item.getEvent())?"OK":"NG";
//            //原始信息（json）
//            String logs = item.getLogs();
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(logs);
//
//            JsonNode resultsNode = jsonNode.at("/data/insight/results");
//
//            List<DfTzCheckItemInfos> dfTzCheckItemInfosList = new ArrayList<>();
//            try {
//                if (resultsNode.isArray()) {
//                    for (JsonNode resultNode : resultsNode) {
//
//                        String test = resultNode.get("test").asText();
//                        String units = resultNode.get("units").asText();
//                        String result = resultNode.get("result").asText();
//
//                        double value = resultNode.get("value").asDouble();
//
//                        double lower_limit = resultNode.get("lower_limit").asDouble();
//                        double upper_limit = resultNode.get("upper_limit").asDouble();
//
//                        DfTzCheckItemInfos dfTzCheckItemInfos = new DfTzCheckItemInfos();
//                        dfTzCheckItemInfos.setCheckResult(result.equals("pass")?"OK":"NG");
//                        dfTzCheckItemInfos.setCheckTime(checkTime);
//                        dfTzCheckItemInfos.setCheckValue(Double.isNaN(value)?null:value);
//                        dfTzCheckItemInfos.setCheckName(test);
//                        dfTzCheckItemInfos.setItemName(test);
//                        dfTzCheckItemInfos.setDescribes(test);
//                        dfTzCheckItemInfos.setLsl(Double.isNaN(lower_limit)?null:lower_limit);
//                        dfTzCheckItemInfos.setUsl(Double.isNaN(upper_limit)?null:upper_limit);
//                        dfTzCheckItemInfos.setCheckType(units.equals("mm")?2:1); //1外观；2尺寸
//                        dfTzCheckItemInfosList.add(dfTzCheckItemInfos);
//
//                    }
//                }
//            } catch (Exception e) {
//                continue;
//            }
//
//            DfTzDetail dfTzDetail = new DfTzDetail();
//            dfTzDetail.setCheckTime(checkTime);
//            dfTzDetail.setBarcode(barcode);
//            dfTzDetail.setResult(tzResult);
//            dfTzDetail.setClasses(classes);
//            dfTzDetail.setProject("C27");
//            dfTzDetail.setLineBody(lineBody);
//            dfTzDetail.setDevice(item.getMachineId());
//            dfTzDetail.setMachineCode(item.getMachineId());
////                dfTzDetail.setPos(item.getMachineId());
//
//
//            if (snResTzIdMap.containsKey(barcode)) {
//                dfTzDetail.setCheckType(2);
//
//                Integer id = snResTzIdMap.get(barcode);
//                dfTzDetailService.removeById(id);
//
//                QueryWrapper<DfTzCheckItemInfos> dfTzCheckItemInfosQw = new QueryWrapper<>();
//                dfTzCheckItemInfosQw.eq("check_id", id);
//                dfTzCheckItemInfosService.remove(dfTzCheckItemInfosQw);
//            } else {
//                dfTzDetail.setCheckType(1);
//            }
//            dfTzDetailService.save(dfTzDetail);
//            Integer tzId = dfTzDetail.getId();
//
//            snResTzIdMap.put(barcode, tzId);
//
//            for(DfTzCheckItemInfos dfTzCheckItemInfos:dfTzCheckItemInfosList){
//                dfTzCheckItemInfos.setCheckId(tzId);
//            }
//            try {
//                dfTzCheckItemInfosService.saveBatch(dfTzCheckItemInfosList);
//            } catch (Exception e) {
//                continue;
//            }
//            count++;
//            System.out.println("录入TZ" + count + "条");
//        }
//
//        if (!CollectionUtils.isEmpty(idList)){
//            //分批删除,一次删除100条
//            int batchSize = 100;
//            int total = idList.size();
//
//            int batches = (int) Math.ceil((double) total / batchSize);
//            for (int i = 0; i < batches; i++) {
//                int fromIndex = i * batchSize;
//                int toIndex = Math.min((i + 1) * batchSize, total);
//
//                List<String> batchIds = idList.subList(fromIndex, toIndex);
//                dfTraceSyncDataService.removeByIds(batchIds);
//            }
//        }
//        return new Result(200, "添加TZ数据成功,录入:" + count + "条");
//    }
//
//
//    /**
//     * 一小时一次
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/saveLeadTraceSynData")
//    @ApiOperation("获取trace表中LEAD数据并保存")
//    @Transactional
//    @Scheduled(fixedRate = 3600000) //一小时
//    public Result saveLeadTraceSynData(){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        //隔一小时取最新的3000条数据
//        QueryWrapper<DfTraceSyncData> qw = new QueryWrapper<>();
//        qw
//                .eq("process_code","lead")
//                .orderByAsc("created_time")
//                .last("limit 3000");
//        List<DfTraceSyncData> dfTraceSyncDataList = dfTraceSyncDataService.list(qw);
//
//        //定义一个Trance待删除的idList
//        List<String> idList = new ArrayList<>();
//        //定义了一个异常情况Trace添加失败的集合
//        List<String> failList = new ArrayList<>();
//
//        String barcode = "";
//        String itemId = "";
//
//        //计数器
//        int count = dfTraceSyncDataList.size();
//        //成功数
//        int successCount = 0;
//        //失败数
//        int failCount = 0;
//        //开始时间
//        LocalDateTime start = LocalDateTime.now();
//        log.info("添加ism数据成功:开始时间:{}",start);
//
//        for(DfTraceSyncData item:dfTraceSyncDataList){
//            try {
//                //条码
//                barcode = item.getCode();
//                //id
//                itemId = item.getId();
//                //检测时间
//                String createdTimeStr = item.getCreatedTime();
//                String checkTimeStr = formatDateTime(createdTimeStr);
//                LocalDateTime dateTime = LocalDateTime.parse(checkTimeStr, formatter);
//
//                Timestamp checkTime = Timestamp.valueOf(dateTime);
//
//                //测试结果
//                String leadResult = "pass".equals(item.getEvent()) ? "OK" : "NG";
//                //原始信息（json）
//                String logs = item.getLogs();
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonNode = objectMapper.readTree(logs);
//
//                JsonNode resultsNode = jsonNode.at("/data/insight/results");
//                JsonNode testStationAttributes = jsonNode.at("/data/insight/test_station_attributes");
//                String positionId = testStationAttributes.get("fixture_id").asText();
//
//                List<DfLeadCheckItemInfos> dfLeadCheckItemInfosList = new ArrayList<>();
//                if (resultsNode.isArray()) {
//                    for (JsonNode resultNode : resultsNode) {
//                        String test = resultNode.get("test").asText();
//                        String result = resultNode.get("result").asText();
//
//                        String valueString = resultNode.get("value") == null ? "" : resultNode.get("value").asText();
//                        Double value = parseStringToDouble(valueString);
//                        String lowerString = resultNode.get("lower_limit") == null ? "" : resultNode.get("lower_limit").asText();
//                        Double lower_limit = parseStringToDouble(lowerString);
//                        String upperString = resultNode.get("upper_limit") == null ? "" : resultNode.get("upper_limit").asText();
//                        Double upper_limit = parseStringToDouble(upperString);
//
//
//                        DfLeadCheckItemInfos dfLeadCheckItemInfos = new DfLeadCheckItemInfos();
//                        dfLeadCheckItemInfos.setCheckResult(result.equals("pass") ? "OK" : "NG");
//                        dfLeadCheckItemInfos.setCheckTime(checkTime);
//                        dfLeadCheckItemInfos.setCheckValue(Double.isNaN(value) ? null : value);
//                        dfLeadCheckItemInfos.setItemName(test);
//                        dfLeadCheckItemInfos.setLsl(Double.isNaN(lower_limit) ? null : lower_limit);
//                        dfLeadCheckItemInfos.setUsl(Double.isNaN(upper_limit) ? null : upper_limit);
//                        dfLeadCheckItemInfos.setCheckType(1); //1尺寸；2外观
//                        dfLeadCheckItemInfosList.add(dfLeadCheckItemInfos);
//                    }
//                }
//
//                DfLeadDetail dfLeadDetail = new DfLeadDetail();
//                dfLeadDetail.setCheckTime(checkTime);
//                dfLeadDetail.setSn(barcode);
//                dfLeadDetail.setResult(leadResult);
//                dfLeadDetail.setWorkPosition(positionId);
//                dfLeadDetail.setMachineCode(item.getMachineId());
//
//                if (redisUtils.hasKey(barcode)) {
//                    dfLeadDetail.setCheckType(2);
//
//                    Integer id = Integer.parseInt(redisUtils.get(barcode).toString());
//                    dfLeadDetailService.removeById(id);
//
//                    QueryWrapper<DfLeadCheckItemInfos> dfLeadCheckItemInfosQw = new QueryWrapper<>();
//                    dfLeadCheckItemInfosQw.eq("check_id", id);
//                    dfLeadCheckItemInfosService.remove(dfLeadCheckItemInfosQw);
//                } else {
//                    dfLeadDetail.setCheckType(1);
//                }
//                dfLeadDetailService.save(dfLeadDetail);
//                Integer leadId = dfLeadDetail.getId();
//
//                for (DfLeadCheckItemInfos dfLeadCheckItemInfos : dfLeadCheckItemInfosList) {
//                    dfLeadCheckItemInfos.setCheckId(leadId);
//                }
//                dfLeadCheckItemInfosService.saveBatch(dfLeadCheckItemInfosList);
//
//                ++successCount;
//                //添加到集合并放到map中
//                idList.add(itemId);
//                redisUtils.set(barcode,leadId, 3 * 24 * 60 * 60);
//            }catch (Exception e){
//                ++failCount;
//                failList.add(itemId);
//            }finally {
//                //打印进度条
//                printProgressBar((successCount + failCount) * 100 / count,"插入");
//            }
//        }
//
//        HashMap<String, Object> resultMap = new HashMap<>();
//        resultMap.put("成功" , idList);
//        resultMap.put("失败" , failList);
//
//        if (!CollectionUtils.isEmpty(idList)){
//            //分批删除,一次删除100条
//            int batchSize = 100;
//            int total = idList.size();
//
//            int batches = (int) Math.ceil((double) total / batchSize);
//            for (int i = 0; i < batches; i++) {
//                int fromIndex = i * batchSize;
//                int toIndex = Math.min((i + 1) * batchSize, total);
//
//                List<String> batchIds = idList.subList(fromIndex, toIndex);
//                dfTraceSyncDataService.removeByIds(batchIds);
//                //打印删除进度条
//                printProgressBar( (i + 1) * 100 / batches , "删除");
//            }
//        }
//
//        //结束时间
//        LocalDateTime end = LocalDateTime.now();
//        long minutes = Duration.between(start, end).toMinutes();
//        Object[] obj = {start,end,minutes,count,successCount,failCount};
//        log.info("添加ism数据成功:开始时间:{},结束时间:{},耗时:{}分钟,总数:{},成功{},失败{}",obj);
//        return new Result(200, "添加ism数据成功,耗时:" + minutes + "分钟",resultMap,successCount);
//    }
//
//    @GetMapping("/saveIsmTraceSynData")
//    @ApiOperation("获取trace表中Ism数据并保存")
//    @Transactional
//    @Scheduled(fixedRate = 3600000) //一小时
//    public Result saveIsmTraceSynData() {
//        Map<String, Integer> snResIsmIdMap = new HashMap<>();
//
//        QueryWrapper<DfTraceSyncData> qw = new QueryWrapper<>();
//        qw
//                .eq("process_code","ism")
//                .orderByAsc("created_time")
//                .last("limit 3000");
//        List<DfTraceSyncData> dfTraceSyncDataList = dfTraceSyncDataService.list(qw);
//        //定义一个Trance待删除的idList
//        List<String> idList = new ArrayList<>();
//        //定义了一个异常情况Trace添加失败的集合
//        List<String> failList = new ArrayList<>();
//
//        //计数器
//        int count = dfTraceSyncDataList.size();
//        //成功数
//        int successCount = 0;
//        //失败数
//        int failCount = 0;
//        String id = "";
//        //开始时间
//        LocalDateTime start = LocalDateTime.now();
//
//        for (DfTraceSyncData item : dfTraceSyncDataList) {
//            try {
//                //id
//                id = item.getId();
//                //条码
//                String barcode = item.getCode();
//                //测试时间
//                Timestamp checkTime = Timestamp.valueOf(item.getCreatedTime());
//                //测试结果
//                String ismResult = "pass".equals(item.getEvent()) ? "OK" : "NG";
//                //原始信息（json）
//                String logs = item.getLogs();
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonNode = objectMapper.readTree(logs);
//
//                JsonNode resultsNode = jsonNode.at("/data/insight/results");
//                JsonNode testStationAttributesNode = jsonNode.at("/data/insight/test_station_attributes");
//                JsonNode testAttributes = jsonNode.at("/data/insight/test_attributes");
//
//                DfIsmCheckItemInfos dfIsmCheckItemInfos = new DfIsmCheckItemInfos();
//
//                dfIsmCheckItemInfos.setCheckTime(checkTime);
//                //            dfIsmCheckItemInfos.setRowId();
//                dfIsmCheckItemInfos.setPosition(testStationAttributesNode.get("fixture_id").asText());
//                dfIsmCheckItemInfos.setResult(ismResult);
//                dfIsmCheckItemInfos.setPrivateMark(barcode);
//                //            dfIsmCheckItemInfos.setDivideMaterialsDifferentParts();
//                //            dfIsmCheckItemInfos.setPrivateMarkResult();
//                //            dfIsmCheckItemInfos.setNetworkValidationResults();
//                //            dfIsmCheckItemInfos.setTrackUrca();
//                //            dfIsmCheckItemInfos.setAbnormalThickness();
//                dfIsmCheckItemInfos
//                        .setStressResults("pass".equals(testAttributes.get("test_result").asText()) ? "OK" : "NG");
//                dfIsmCheckItemInfos.setIsmRollbackTime(checkTime);
//                //            dfIsmCheckItemInfos.setIsmRollbackModel();
//                //            dfIsmCheckItemInfos.setIsmRollbackError();
//                //            dfIsmCheckItemInfos.setNgType();
//                dfIsmCheckItemInfos.setMachineId(item.getMachineId());
//
//                if (resultsNode.isArray()) {
//                    for (JsonNode resultNode : resultsNode) {
//                        String test = resultNode.get("test").asText();
//                        //解析数值类型的值如果是数字字符串那么返回Double,非数字字符串返回null
//                        String valueStirng = resultNode.get("value") == null ? "" : resultNode.get("value").asText();
//                        Double value = parseStringToDouble(valueStirng);
//
//                        switch (test) {
//                            //cs
//                            case "cs_surface":
//                                dfIsmCheckItemInfos.setCs(Double.isNaN(value) ? null : value);
//                                break;
//                            //csk
//                            case "cs_knee":
//                                dfIsmCheckItemInfos.setCsk(Double.isNaN(value) ? null : value);
//                                break;
//                            //dol
//                            case "second_iox_depth":
//                                dfIsmCheckItemInfos.setDol(Double.isNaN(value) ? null : value);
//                                break;
//                            //doc
//                            case "depth_of_compression":
//                                dfIsmCheckItemInfos.setDoc(Double.isNaN(value) ? null : value);
//                                break;
//                            //ct
//                            case "central_tension":
//                                dfIsmCheckItemInfos.setCt(Double.isNaN(value) ? null : value);
//                                break;
//                            //ctOrTa
//                            case "ct/ta":
//                                dfIsmCheckItemInfos.setCtOrTa(Double.isNaN(value) ? null : value);
//                                break;
//                            //tact
//                            case "ta*ct":
//                                dfIsmCheckItemInfos.setTact(Double.isNaN(value) ? null : value);
//                                break;
//                            //gt
//                            case "glass_thickness":
//                                dfIsmCheckItemInfos.setGGt(Double.isNaN(value) ? null : value);
//                                break;
//                        }
//                    }
//                }
//
//                if (redisUtils.hasKey(barcode)) {
//                    dfIsmCheckItemInfos.setCheckType(2);
//                    Integer removedId = snResIsmIdMap.get(barcode);
//                    dfIsmCheckItemInfosService.removeById(removedId);
//                } else {
//                    dfIsmCheckItemInfos.setCheckType(1);
//                }
//
//                dfIsmCheckItemInfosService.save(dfIsmCheckItemInfos);
//                Integer ismId = dfIsmCheckItemInfos.getId();
//
//                ++successCount;
//                //添加到集合并放到map中
//                idList.add(id);
//                snResIsmIdMap.put(barcode, ismId);
////                System.out.println("ism添加第" + ++count + "条数据,id:" + id);
//            } catch (Exception e) {
//                ++failCount;
//                failList.add(id);
////                System.out.println("ism添加第" + ++count + "条数据失败,id:" + id);
//            }finally {
//                //打印插入进度条
//                printProgressBar((successCount + failCount) * 100 / count , "插入");
//            }
//        }
//
//        HashMap<String, Object> resultMap = new HashMap<>();
//        resultMap.put("成功" , idList);
//        resultMap.put("失败" , failList);
//
//        if (!CollectionUtils.isEmpty(idList)){
//            //分批删除,一次删除100条
//            int batchSize = 100;
//            int total = idList.size();
//
//            int batches = (int) Math.ceil((double) total / batchSize);
//            for (int i = 0; i < batches; i++) {
//                int fromIndex = i * batchSize;
//                int toIndex = Math.min((i + 1) * batchSize, total);
//
//                List<String> batchIds = idList.subList(fromIndex, toIndex);
//                dfTraceSyncDataService.removeByIds(batchIds);
//                //打印删除进度条
//                printProgressBar( (i + 1) / batches , "删除");
//            }
//        }
//        //结束时间
//        LocalDateTime end = LocalDateTime.now();
//        long minutes = Duration.between(start, end).toMinutes();
//        Object[] obj = {start,end,minutes,count,successCount,failCount};
//        log.info("添加ism数据成功:开始时间:{},结束时间:{},耗时:{}分钟,总数:{},成功:{},失败:{}",obj);
//        return new Result(200, "添加ism数据成功,耗时:" + minutes + "分钟",resultMap,successCount);
//    }
//
//
//
//    /**
//     * 将字符串类型的数字转成Double类型,成功返回Double,失败返回Null
//     * @param value
//     * @return
//     */
//    public static Double parseStringToDouble(String value){
//        try {
//            return Double.parseDouble(value);
//        }catch (NumberFormatException e){
//            return Double.NaN;
//        }
//    }
//
//    /**
//     * 打印进度条
//     * @param percentage
//     * @param processingType
//     */
//    public static void printProgressBar(int percentage , String processingType) {
//        System.out.print("\r" + processingType + "[");
//        int completedLength = (percentage * 50) / 100;
//        for (int i = 0; i < 50; i++) {
//            if (i < completedLength) {
//                System.out.print("#");
//            } else {
//                System.out.print(" ");
//            }
//        }
//        System.out.print("] " + percentage + "%");
//    }
//
//}
