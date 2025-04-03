package com.ww.boengongye.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfMacStatusSizeService;
import com.ww.boengongye.threadMethod.SvaeStatusData;
import com.ww.boengongye.timer.InitializeCheckRule;
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

@Component
public class SizeStatusTopicConsumer {


    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;


    @JmsListener(destination = "${sizeStatusTopic}", containerFactory = "queueListener")
    public void recieve(TextMessage textMessage) throws JMSException {
        System.out.println("进入尺寸状态Mq更新方法");
        DfMacStatusSize datas = new Gson().fromJson(textMessage.getText(), DfMacStatusSize.class);
//        if(InitializeCheckRule.sizeStatus.containsKey(datas.getMachineCode())) {
            UpdateWrapper<DfMacStatusSize> uw = new UpdateWrapper<>();
            uw.eq("MachineCode", datas.getMachineCode());
            uw.set("StatusID_Cur", datas.getStatusidCur());
            uw.set("pub_time", Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            dfMacStatusSizeService.update(uw);
//        }

    }


}
