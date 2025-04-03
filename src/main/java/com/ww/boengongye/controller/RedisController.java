package com.ww.boengongye.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/redis")
@ResponseBody
@CrossOrigin
public class RedisController {
    /**
     * 需要注入redis模板
     *
     * 对于RedisTemplate的泛型情况,
     * 可以使用<String, String>
     *       <Object, Object>
     *       或者不写泛型
     *
     *  注意,属性的名称必须为redisTemplate,因为按名称注入,框架创建的对象就是这个名字的
     */
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    DfSizeDetailService dfSizeDetailService;

    // 添加数据到redis
    @PostMapping("/redis/addstring")
    public String addToRedis(String name, String value) {

        // 操作Redis中的string类型的数据,先获取ValueOperation
        ValueOperations valueOperations = redisTemplate.opsForValue();

        DfSizeDetail size=dfSizeDetailService.getById(728582);
        // 添加数据到redis
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        valueOperations.set(name, gson.toJson(size));
        return "向redis添加string类型的数据";
    }

//    // 添加数据到redis
//    @PostMapping("/redis/test2")
//    public String test2(String name, String value) {
//
//        DfSizeDetail size=dfSizeDetailService.getById(2740775);
//
//        // 放入redis
////        redisUtil.set(name, size);
////        // 从redis中获取
////        System.out.println("获取到数据：" + redisUtil.get(name));
////        System.out.println("获取到数据：" + redisUtil.get(name).toString());
////        DfSizeDetail datas = new Gson().fromJson(redisUtil.get(name), DfSizeDetail.class);
//
//        byte[] byteKey = SerializationUtils.serialize(name);
//        byte[] byteValue = SerializationUtils.serialize(size);
//        redisTemplate.opsForValue().set(byteKey, byteKey);
//
//        //    将key转换为字节数组类型
//
////    jedis去redis中获取value
//        byte[] vv =  redisTemplate.opsForValue().get(byteKey);
////    将value反s序列化
//        DfSizeDetail user = (DfSizeDetail) SerializationUtils.deserialize(vv);
//        System.out.println(user);
//        return "key是" + name + ",它的值是:" + user.toString();
//    }



    // 从redis获取数据
    @GetMapping("/redis/getk")
    public String getData(String key) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object v = valueOperations.get(key);
//        Object v2 = valueOperations.get("size2");
        if(null!=v){
            System.out.println(v.toString());
            DfSizeDetail datas = new Gson().fromJson(v.toString(), DfSizeDetail.class);
            System.out.println(datas.getMachineCode());
            return "key是" + key + ",它的值是:" + datas.toString();
        }
        return "key是" + key + ",无值" ;
    }

    @PostMapping("/redis/{k}/{v}")
    public String addStringKV(@PathVariable String k,
                              @PathVariable String v) {

        // 使用StringRedisTemplate对象
        stringRedisTemplate.opsForValue().set(k,v);
        return "使用StringRedisTemplate对象添加";
    }

    @GetMapping("/redis/{k}")
    public String getStringValue(@PathVariable String k) {

        // 获取String类型的value
        String v = stringRedisTemplate.opsForValue().get(k);
        return "从redis中通过" + k + "获取到string类型的v=" + v;
    }

}
