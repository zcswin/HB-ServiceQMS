package com.ww.boengongye.controller;
import com.ww.boengongye.config.MqttProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
public class SendController {

//    @Autowired
//    private MqttProviderConfig providerClient;
//
//    @RequestMapping("/sendMessage")
//    @ResponseBody
//    public String sendMessage(int qos,String topic,String message){
//        try {
//            if (!providerClient.client.isConnected()){
//                providerClient.client.connect();
//            }
//            providerClient.publish(qos, true, topic, message);
//            return "发送成功";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "发送失败";
//        }
//    }

}
