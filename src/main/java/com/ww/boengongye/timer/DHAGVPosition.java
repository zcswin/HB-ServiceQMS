package com.ww.boengongye.timer;

import ch.qos.logback.core.util.COWArrayList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.utils.*;
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
import java.util.*;

//鼎皇AGV 位置获取
@Component
@EnableScheduling // 1.开启定时任务
public class DHAGVPosition {
    @Autowired
    Environment env;


    @Resource
    private RedisTemplate redisTemplate;

    @Async
    @Scheduled(initialDelay = 10000,fixedDelay = 1500)
    public void getAGVPosition() {
        if (env.getProperty("AGVLock", "N").equals("Y")) {
            System.out.println("获取AGV位置");
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
            System.out.println(json);
            //请求AGV位置
            AgvResult dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("AGVURL"), null, headers, json, false), AgvResult.class);
            System.out.println(dl.toString());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            DecimalFormat df = new DecimalFormat("#.00");
            //获取arv列表
            Set<String> xsarvs = redisTemplate.keys("XSARV*");
            Set<String> arvs = redisTemplate.keys("ARV*");
            List<String> datas = new ArrayList<>();
            for (String key : arvs) {
                datas.add(key);
            }
            for (String key : xsarvs) {
                datas.add(key);
            }
            for (AgvResultDetail dd : dl.getData()) {
                System.out.println("dd");
                System.out.println(dd.toString());
                ARVPositionData beforePosition = new ARVPositionData();
                //获取上一个位置
                Object before = valueOperations.get("DHAGVBF:" + dd.getDeviceName());
                System.out.println(1111);
                if (null != before) {
                    System.out.println(before.toString());
                    beforePosition = new Gson().fromJson(before.toString(), ARVPositionData.class);
                }
                //把当前存储位置设置成上一个位置
                Object v2 = valueOperations.get("DHAGV:" + dd.getDeviceName());
                if (null != v2) {
                    ARVPositionData v2Data=new Gson().fromJson(v2.toString(), ARVPositionData.class);
                    valueOperations.set("DHAGVBF:" + dd.getDeviceName(), gson.toJson(v2Data));
                }
                //存储当前坐标
                ARVPositionData position = new ARVPositionData();
                position.setMachineCode(dd.getDeviceName());
                //转换坐标
                position.setX(Double.parseDouble(df.format((((dd.getDevicePostionRec().get(0) + 12632) / 140 * -1) - 54))));
                position.setY(Double.parseDouble(df.format((((dd.getDevicePostionRec().get(1) - 15781) / 140) + 5))));
                valueOperations.set("DHAGV:" + dd.getDeviceName(), gson.toJson(position));

                //判断方向
                if (null != beforePosition&&null!=beforePosition.getX()&&null!=beforePosition.getY()) {
                    String direction = "";
                    if ((position.getX() - beforePosition.getX()) > 1) {//右,设置偏差1过滤
                        direction = "right";
                    } else if ((position.getX() - beforePosition.getX()) < -1) {
                        direction = "left";
                    }

                    if (!direction.equals("")) {
                        if ((position.getY() - beforePosition.getY()) > 1) {//下
                            direction = "below";
                        } else if ((position.getY() - beforePosition.getY()) < -1) {
                            direction = "up";
                        }
                    }

                    if (!direction.equals("")) {

                        if ((position.getX() - beforePosition.getX()) > 0) {//右,设置偏差0
                            direction = "right";
                        } else if ((position.getX() - beforePosition.getX()) < 0) {
                            direction = "left";
                        }
                        if (!direction.equals("")) {
                            if ((position.getY() - beforePosition.getY()) > 0) {//下
                                direction = "below";
                            } else if ((position.getY() - beforePosition.getY()) < 0) {
                                direction = "up";
                            }
                        }
                    }

                        int totalLock=0;
                        for (String key : datas) {
                            Object v = valueOperations.get(key);
                            if (null != v) {
                                int isSendStop = 0;//发送停车指令
                                ARVPositionData xsARV = new Gson().fromJson(v.toString(), ARVPositionData.class);
                                //判定坐标在同一轨道
                                if (CommunalUtils.changePositive(xsARV.getX() - position.getX()) < (Double.valueOf(env.getProperty("AGVWarningXYValue")) * 7) || CommunalUtils.changePositive(xsARV.getY() - position.getY()) < (Double.valueOf(env.getProperty("AGVWarningXYValue")) * 7)) {
                                    //判定是X轴还是Y轴
                                    if (CommunalUtils.changePositive(xsARV.getX() - position.getX()) < (Double.valueOf(env.getProperty("AGVWarningXYValue")) * 7)) {
                                        //Y轴接近
                                        if (CommunalUtils.changePositive(xsARV.getY() - position.getY()) < (Double.valueOf(env.getProperty("AGVWarningValue")) * 7)) {

                                            if (position.getY() - xsARV.getY() > 0) {//上
                                                if (direction.equals("up")) {
                                                    //发送停止指令
                                                    isSendStop = 1;
                                                    totalLock=1;
                                                }
                                            } else if (position.getY() - xsARV.getY() == 0) {
                                                //发送停止指令
                                                isSendStop = 1;
                                                totalLock=1;
                                            } else {
                                                if (direction.equals("below")) {
                                                    //发送停止指令
                                                    isSendStop = 1;
                                                    totalLock=1;
                                                }
                                            }

                                        }
                                    } else {
                                        //X轴接近
                                        if (CommunalUtils.changePositive(xsARV.getX() - position.getX()) < (Double.valueOf(env.getProperty("AGVWarningValue")) * 7)) {
                                            if (position.getX() - xsARV.getX() > 0) {//左
                                                if (direction.equals("left")) {
                                                    //发送停止指令
                                                    isSendStop = 1;
                                                    totalLock=1;
                                                }
                                            } else if (position.getX() - xsARV.getX() == 0) {
                                                //发送停止指令
                                                isSendStop = 1;
                                                totalLock=1;
                                            } else {
                                                if (direction.equals("right")) {
                                                    //发送停止指令
                                                    isSendStop = 1;
                                                    totalLock=1;
                                                }
                                            }
                                        }
                                    }
                                }
                                //发送停车请求
                                if (isSendStop == 1) {
                                    if (!redisTemplate.hasKey("AGVLock:" + dd.getDeviceCode())) {//判断是否已锁定
                                        HashMap<String, Object> stopParams = new HashMap<>();
                                        stopParams.put("areaId", Integer.parseInt(env.getProperty("AGVAreaId", "2")));
                                        stopParams.put("deviceNumber", dd.getDeviceCode());
                                        stopParams.put("all", 0);
                                        stopParams.put("controlWay", 0);

                                        String json2 = "";
                                        try {
                                            json2 = objectMapper.writeValueAsString(stopParams);
                                        } catch (JsonProcessingException e) {
                                            throw new RuntimeException(e);
                                        }
                                        System.out.println(json2);
                                        //发送停车请求到AGV
                                        AgvResult dl2 = new Gson().fromJson(HttpUtil.postJson(env.getProperty("AGVControlURL"), null, headers, json2, false), AgvResult.class);
                                        if (dl2.getCode() == 1000) {//请求成功,保存锁定状态

                                            AGVLock lc = new AGVLock();
                                            lc.setMachineCode(dd.getDeviceCode());
                                            lc.setDirection(direction);
                                            lc.setX(dd.getDevicePostionRec().get(0));
                                            lc.setY(dd.getDevicePostionRec().get(1));
                                            lc.setType("AGV");
                                            lc.setLockTime(TimeUtil.getNowTimeByNormal());
                                            valueOperations.set("AGVLock:" + dd.getDeviceCode(), gson.toJson(datas));
                                        }

                                    }

                                    //退出遍历
                                    break;
                                }

                            }
                        }

                    if (redisTemplate.hasKey("AGVLock:" + dd.getDeviceCode())&&totalLock==0) {//解锁
                        HashMap<String, Object> stopParams = new HashMap<>();
                        stopParams.put("areaId", Integer.parseInt(env.getProperty("AGVAreaId", "2")));
                        stopParams.put("deviceNumber", dd.getDeviceCode());
                        stopParams.put("all", 0);
                        stopParams.put("controlWay", 1);//恢复

                        String json2 = "";
                        try {
                            json2 = objectMapper.writeValueAsString(stopParams);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(json2);
                        //发送停车请求到AGV
                        AgvResult dl2 = new Gson().fromJson(HttpUtil.postJson(env.getProperty("AGVControlURL"), null, headers, json2, false), AgvResult.class);
                        if (dl2.getCode() == 1000) {//请求成功,接除锁定状态
                            redisTemplate.delete("AGVLock:" + dd.getDeviceCode());
                        }

                    }

                }


            }

        }


    }
}
