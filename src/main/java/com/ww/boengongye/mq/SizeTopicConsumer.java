package com.ww.boengongye.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.DfMacPosition;
import com.ww.boengongye.entity.DfMacProcess;
import com.ww.boengongye.entity.DfMacRev;
import com.ww.boengongye.entity.SizeQueueData;
import com.ww.boengongye.threadMethod.SvaeSizeData;
import com.ww.boengongye.threadMethod.SvaeStatusData;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.concurrent.*;

@Component
public class SizeTopicConsumer {


    @Autowired
    com.ww.boengongye.service.DfMacRevService DfMacRevService;

    @Autowired
    com.ww.boengongye.service.DfMacPositionService DfMacPositionService;
    @Autowired
    com.ww.boengongye.service.DfMacProcessService DfMacProcessService;
    @Autowired
    com.ww.boengongye.service.DfSizeDetailService DfSizeDetailService;



    static Environment env = ApplicationContextProvider.getBean(Environment.class);

    BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<Runnable>(Integer.parseInt(env.getProperty("sizeQueue")));
    ExecutorService saceSizePool = new ThreadPoolExecutor(Integer.parseInt(env.getProperty("sizePool")),
            Integer.parseInt(env.getProperty("sizeMaxPool")), 0L, TimeUnit.MILLISECONDS, workingQueue);



    @JmsListener(destination = "${topicSize}", containerFactory = "queueListener")
    public void recieve(TextMessage textMessage) throws JMSException {

//        System.out.println("接收到尺寸消息-->"+textMessage.getText());
        Thread t1 = new SvaeSizeData(textMessage.getText());
        saceSizePool.execute(t1);
//        try {
//            DfSizeDetailService.saveMqData(textMessage.getText());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

    }


}
