package com.ww.boengongye.controller;

import com.ww.boengongye.entity.DfCuttingTime;
import com.ww.boengongye.entity.DfMacPosition;
import com.ww.boengongye.entity.DfMacProcess;
import com.ww.boengongye.entity.DfMacRev;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class ToolHDTopicConsumer {


    @Autowired
    com.ww.boengongye.service.DfMacRevService DfMacRevService;

    @Autowired
    com.ww.boengongye.service.DfMacPositionService DfMacPositionService;
    @Autowired
    com.ww.boengongye.service.DfCuttingTimeService DfCuttingTimeService;

//    @JmsListener(destination = "${topic3}")
//    public void recieve(TextMessage textMessage) throws JMSException {
//
//        System.out.println("接收到topic3333333消息-->"+textMessage.getText());
//        JSONObject jsonObject = JSONObject.fromObject(textMessage.getText().toLowerCase());
//        DfCuttingTime data=(DfCuttingTime) JSONObject.toBean(jsonObject, DfCuttingTime.class);
//        DfCuttingTimeService.save(data);
//
//    }


}
