package com.ww.boengongye.timer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.AgvInfoService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.HttpUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//鼎皇AGV 位置获取
@Component
@EnableScheduling // 1.开启定时任务
public class DHAGVSavePosition {
    @Autowired
    Environment env;


    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    AgvInfoService agvInfoService;

//    @Async
    @Scheduled(initialDelay = 10000,fixedDelay = 1500)
    public void getAGVPosition() {
        if (env.getProperty("SaveAgvPosition", "N").equals("Y")) {
            HashMap<String, String> headers = new HashMap<>();
            HashMap<String, Object> params = new HashMap<>();
            params.put("areaId", Integer.parseInt(env.getProperty("AGVAreaId", "2")));
            params.put("deviceType", 0);

            String json = "";
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                json = objectMapper.writeValueAsString(params);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //请求AGV位置
            AgvResult dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("AGVURL"), null, headers, json, false), AgvResult.class);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            List<AgvInfo>saveData=new ArrayList<>();
            for (AgvResultDetail dd : dl.getData()) {

                AgvInfo a=new AgvInfo();
                a.setDeviceCode(dd.getDeviceCode());
                a.setPayLoad(dd.getPayLoad());
                a.setX(dd.getDevicePostionRec().get(0));
                a.setY(dd.getDevicePostionRec().get(1));
                a.setDevicePosition(dd.getDevicePosition());
                a.setBattery(dd.getBattery());
                a.setDeviceName(dd.getDeviceName());
                a.setDeviceStatus(dd.getDeviceStatus());
                a.setState(dd.getState());
                a.setOritation(dd.getOritation());
                a.setSpeed(dd.getSpeed());
                saveData.add(a);
            }

            if(saveData.size()>0){
                agvInfoService.saveBatch(saveData);
            }

        }


    }
}
