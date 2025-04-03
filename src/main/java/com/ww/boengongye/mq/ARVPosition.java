package com.ww.boengongye.mq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.ARVPositionData;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ARVPosition {

    @Resource
    private RedisTemplate redisTemplate;

//    @JmsListener(destination = "${ARVPosition}")
@JmsListener(destination = "${ARVPosition}", containerFactory = "jtJmsListenerContainerFactory")
    private void consume(final String msg) {
       System.out.println("ARV");
       System.out.println(msg);
        ARVPositionData datas = new Gson().fromJson(msg, ARVPositionData.class);
        if(datas.getType_Data()==801){
            datas.setX(((Double.parseDouble(datas.getPosition_x()) + (-22 * 155)) / 155 * -1) - 50);
            datas.setY(((Double.parseDouble(datas.getPosition_y()) + (-85 * 155)) / 155));
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            valueOperations.set("ARV"+datas.getCarCode(), gson.toJson(datas));
        }else if(datas.getType_Data()==802){
            datas.setX((((Double.parseDouble(datas.getX_position())* 7.3)  +325.48)*-1)+5 - 50);
            datas.setY(((Double.parseDouble(datas.getY_position())* 7.3)-5 +97.77 ));
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            valueOperations.set("XSARV"+datas.getCarCode(), gson.toJson(datas));
        }

    }
}
