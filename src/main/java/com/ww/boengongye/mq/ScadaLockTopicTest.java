package com.ww.boengongye.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.threadMethod.SvaeStatusData;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@Component
public class ScadaLockTopicTest {

//    @Autowired
//    DfMacStatusDetailService DfMacStatusDetailService;
//    @Autowired
//    DfMacStatusService DfMacStatusService;
//
//    static Environment env = ApplicationContextProvider.getBean(Environment.class);

//    @JmsListener(destination = "${SCADALockMac}")
//    public void recieve(TextMessage textMessage) throws JMSException {
//        System.out.println("接收到锁机topic消息-->" + textMessage.getText());
//
//
//    }


}
