package com.ww.boengongye.mq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ww.boengongye.controller.DfQmsIpqcWaigTotalController;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfRiskProduct;
import com.ww.boengongye.entity.HttpParamTime;
import com.ww.boengongye.service.DfMacStatusSizeService;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfRiskProductService;
import com.ww.boengongye.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
public class RFIDSizeResultTopicConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RFIDSizeResultTopicConsumer.class);
    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;


    @Autowired
    private DfProcessService dfProcessService;

    @Autowired
    private DfRiskProductService dfRiskProductService;
    @Autowired
    Environment env;
//    @JmsListener(destination = "${RFIDBadGlassReportTopic}")
    @JmsListener(destination = "${RFIDBadGlassReportTopic}", containerFactory = "queueListener")
    public void recieve(TextMessage textMessage) throws JMSException, JsonProcessingException {
        logger.info("推送尺寸结果给RFID:"+textMessage.getText());
        Map<String, String> headers = new HashMap<>();
        //请求RFID
        HttpParamTime param = new HttpParamTime(60);
        RFIDResult3 dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDBadGlassReportURL"), null, headers, textMessage.getText(),false), RFIDResult3.class);
        logger.info("RFID返回结果");
        logger.info(dl.toString());
        if(null!=dl.getData().getData()){
            for (DfRiskProduct dfRiskProduct:dl.getData().getData()   ){
                String machineCode = dfRiskProduct.getMachineCode();
                QueryWrapper<DfProcess> processWrapper = new QueryWrapper<>();
                processWrapper
                        .eq("first_code",machineCode.substring(0))
                        .last("limit 1");
                DfProcess dfProcess = dfProcessService.getOne(processWrapper);
                if(null!=dfProcess&&null!=dfProcess.getProcessName()){
                    dfRiskProduct.setProcess(dfProcess.getProcessName());
                }

                ObjectMapper objectMapper = new ObjectMapper();
                // 将 JSON 字符串解析为 JsonNode
                JsonNode rootNode = objectMapper.readTree(textMessage.getText());
                // 获取 "code" 字段的值
                int dataId = rootNode.path("dataId").asInt(); // 如果字段不存在，默认返回 0
                dfRiskProduct.setParentId(dataId);
                dfRiskProduct.setType("尺寸");
            }
            dfRiskProductService.saveBatch(dl.getData().getData());
        }






    }


}
