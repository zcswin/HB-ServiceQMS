package com.ww.boengongye.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;
import javax.jms.Queue;

@Controller
@ResponseBody
@CrossOrigin
public class ProducerController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @RequestMapping (value ="/queue/test")
    public String sendQueue( String str) {
        this.sendMessage(this.queue, str);
        return "success";
    }
//
    // 发送消息，destination是发送到的队列，message是待发送的消息
    private void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
//    @Autowired
//    private org.springframework.jms.core.JmsTemplate JmsTemplate;


}
