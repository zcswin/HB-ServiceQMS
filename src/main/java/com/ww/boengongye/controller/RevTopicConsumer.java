package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacProcessService;
import com.ww.boengongye.service.DfMacRevService;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.utils.TimeUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.Timestamp;

@Component
public class RevTopicConsumer {


    @Autowired
    com.ww.boengongye.service.DfMacRevService DfMacRevService;

    @Autowired
    com.ww.boengongye.service.DfMacPositionService DfMacPositionService;
    @Autowired
    com.ww.boengongye.service.DfMacProcessService DfMacProcessService;

//    @JmsListener(destination = "${topic2}")
//    public void recieve(TextMessage textMessage) throws JMSException {
//
//        System.out.println("接收到topic2222222消息-->"+textMessage.getText());
//        JSONObject jsonObject = JSONObject.fromObject(textMessage.getText().toLowerCase());
//
//        if(jsonObject.get("type_data").toString().equals("20")){
//            DfMacRev data = (DfMacRev) JSONObject.toBean(jsonObject, DfMacRev.class);
//            data.setMachinecode(data.getMachinecode().toUpperCase());
////            data.setCreate_time(Timestamp.valueOf(TimeUtil.timeStamp2Date(data.getPub_time()+"")));
////            System.out.println("時間111-->"+data.getCreate_time());
//            DfMacRevService.save(data);
//        }else if(jsonObject.get("type_data").toString().equals("14")){
//            DfMacPosition data = (DfMacPosition) JSONObject.toBean(jsonObject, DfMacPosition.class);
//            data.setMachinecode(data.getMachinecode().toUpperCase());
//            DfMacPositionService.save(data);
//        }else if(jsonObject.get("type_data").toString().equals("19")){
//            DfMacProcess data = (DfMacProcess) JSONObject.toBean(jsonObject, DfMacProcess.class);
//            data.setMachinecode(data.getMachinecode().toUpperCase());
//            DfMacProcessService.save(data);
//        }else{
//            System.out.println("新編號-->"+jsonObject.get("type_data").toString());
//        }
//
//
//
//
//    }


}
