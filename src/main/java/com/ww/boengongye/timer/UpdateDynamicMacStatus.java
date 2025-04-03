package com.ww.boengongye.timer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.DynamicIpqcMac;
import com.ww.boengongye.entity.SizeQueueData;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.ServerInitializeUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling // 1.开启定时任务
public class UpdateDynamicMacStatus {




    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    Environment env;


    @Scheduled(cron = "0 30 7,19 * * ?")
    public void UpdateDynamicMacStatus1()  {

            RedisUtils redis = new RedisUtils();
            Set<String> appearanceDatas = redisTemplate.keys("IpqcAppearance*");
            ValueOperations valueOperations = redisTemplate.opsForValue();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<String> list = new ArrayList<>(appearanceDatas);
        for (String key : list) {
                Object v = valueOperations.get(key);
                if (null != v) {
                    DynamicIpqcMac data = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                    if (null != data) {
                        data.setOperationStatus("停机");
                        data.setLastOperationTime(TimeUtil.getNowTimeByNormal());
                        data.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
//                        data.setStatus("常规");
//                        data.setRuleName("QCP抽检频率");
//                        data.setFrequency(Double.valueOf(env.getProperty("IpqcTimeStandardNormal")));
                        data.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//                        data.setTotalCount(0);
//                        data.setNgCount(0);
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        redisTemplate.opsForValue().set("IpqcAppearance:" + data.getMachineCode(), gson.toJson(data));
                    }
                }

        }


    }
}
