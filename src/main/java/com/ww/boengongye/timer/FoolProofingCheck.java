package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.ServerInitializeUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@EnableScheduling // 1.开启定时任务
public class FoolProofingCheck {


    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    Environment env;


    @Scheduled(initialDelay = 10000,fixedDelay = 10000)
    public void testNg() throws ParseException {

//        System.out.println("定时检查防呆数据");
        RedisUtils redis=new RedisUtils();
        Set<String> datas= redisTemplate.keys("FP*");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        System.out.println(redisTemplate.keys("FP*"));
        int overTime=10;
        if(null!=env.getProperty("FoolProofingTime")){
            overTime=Integer.parseInt(env.getProperty("FoolProofingTime"));
        }
        for (String key : datas) {
//            System.out.println("keys:"+key);
                Object v = valueOperations.get(key);
                if(null!=v){
                    SizeQueueData fdDatas = new Gson().fromJson(v.toString(), SizeQueueData.class);
                    if(null!=fdDatas){
                        long diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - sd.parse(fdDatas.getFoolProofingTime()).getTime();
                        if (diff / 1000 > overTime) {
                            redisTemplate.delete(key);
                            try {
                                ServerInitializeUtil.dfSizeDetailService.saveMqData(v.toString(),"foolProofig");
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }
            }

        }

}
