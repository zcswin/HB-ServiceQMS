package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfProcessProjectConfigService;
import com.ww.boengongye.service.DfProjectColorService;
import com.ww.boengongye.service.DfProjectService;
import com.ww.boengongye.service.DfSizeCheckItemInfosService;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class SizeBoard {


    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private DfSizeCheckItemInfosService dfSizeCheckItemInfosService;

    @Autowired
    private DfProcessProjectConfigService dfProcessProjectConfigService;

    @Autowired
    private DfProjectService dfProjectService;

    @Autowired
    private DfProjectColorService dfProjectColorService;

    // 统计尺寸看板下端的管制图，每十分钟运行一次
//    @Async
    //@Scheduled(cron ="30 59 0,2,4,6,8,10,12,14,16,18,20,22 * * ?")
//    @Scheduled(cron ="55 9/10 * * * ?")
//    @Scheduled(cron ="0 0 0/1 * * ? ")
    public void getNear50Data() throws ParseException {
        System.out.println("更新limit50");
        QueryWrapper<DfProcessProjectConfig> qw = new QueryWrapper<>();
        qw.like("type", "尺寸");
        List<DfProcessProjectConfig> processes = dfProcessProjectConfigService.list(qw);
        List<DfProject> projects = dfProjectService.list();
        List<DfProjectColor> colors = dfProjectColorService.list();
        String[] dayOrNight = new String[]{"A", "B", "AB"};
        for (DfProject project : projects) {
            for (DfProjectColor color : colors) {
                for (DfProcessProjectConfig process : processes) {
                    for (String dn : dayOrNight) {
                        if (process.getProject().contains(project.getName()) && color.getProjectName().contains(project.getName())) {
                            listInfosLimit50(project.getName(), color.getColor(), process.getProcessName(), dn);
                        }
                    }
                }
            }
        }

    }

    public void listInfosLimit50(String project, String color, String process, String dayOrNight) throws ParseException {
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

        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos = dfSizeCheckItemInfosService.listJoinDetailLimit50(project, color, process, startTime, endTime, startHour, endHour);
        Map<String, Map<String, List<Object>>> itemResValueMap = new LinkedHashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String itemName = dfSizeCheckItemInfo.getItemName();
            // 标准使用数据库的标准
            DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
            Object lslLimit = standardData.getIsolaLowerLimit();
            Object uslLimit = standardData.getIsolaUpperLimit();
            Object standardLimit = standardData.getIsolaUpperLimit();
            if (!itemResValueMap.containsKey(itemName)) {
                Map<String, List<Object>> valueMap  = new HashMap<>();
                List<Object> checkValue = new ArrayList<>();
                List<Object> standardValue = new ArrayList<>();
                List<Object> usl = new ArrayList<>();
                List<Object> lsl = new ArrayList<>();
                List<Object> itemNameList = new ArrayList<>();
                List<Object> checkTimeList = new ArrayList<>();
                itemNameList.add(itemName);
                checkValue.add(dfSizeCheckItemInfo.getCheckValue());
                standardValue.add(standardLimit);
                usl.add(uslLimit);
                lsl.add(lslLimit);
                checkTimeList.add(sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                valueMap.put("checkValue", checkValue);
                valueMap.put("standardValue", standardValue);
                valueMap.put("usl", usl);
                valueMap.put("lsl", lsl);
                valueMap.put("name", itemNameList);
                valueMap.put("checkTime", checkTimeList);
                itemResValueMap.put(itemName, valueMap);
            } else {
                Map<String, List<Object>> valueMap = itemResValueMap.get(itemName);
                // 前一片和后一片一样的话就更新时间
                if ((Double) valueMap.get("checkValue").get(valueMap.get("checkValue").size() - 1) - dfSizeCheckItemInfo.getCheckValue() == 0) {
                    // 更新时间
                    valueMap.get("checkTime").set(valueMap.get("checkTime").size() - 1, sdf.format(Timestamp.valueOf(dfSizeCheckItemInfo.getCheckTime())));
                    continue;
                }
                valueMap.get("checkValue").add(dfSizeCheckItemInfo.getCheckValue());
                valueMap.get("standardValue").add(standardLimit);
                valueMap.get("usl").add(uslLimit);
                valueMap.get("lsl").add(lslLimit);
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

        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Object>>> entry : itemResValueMap.entrySet()) {
            Map<String, List<Object>> map = entry.getValue();
            // 拿最新的标准作为所有的标准
            int len = map.get("lsl").size();
            // 标准使用数据库的标准
            String ngItem = (String) map.get("name").get(0);
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
        if (result.size() > 0) {
            redisUtil.delete(key);
            redisUtil.lSet(key, result);
        }
    }

    @Async
    @Scheduled(cron ="0 0 0/1 * * ?")
    public void getNormalDistributionCache() throws ParseException {
        System.out.println("正太分布图数据获取" + TimeUtil.getNowTimeByNormal());
        // 获取当天到前7天的数据
        String startTime = TimeUtil.getLastSomeDay(TimeUtil.getNowTimeNoHour(), 7);
        String endTime = TimeUtil.getNowTimeNoHour();

        List<Map<String, Object>> result;
        List<DfProjectColor> colors = dfProjectColorService.list();
        for (DfProjectColor color : colors) {
            String c = color.getColor();
            result = dfSizeCheckItemInfosService.getNormalDistributionData(
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
            }
        }
    }
}
