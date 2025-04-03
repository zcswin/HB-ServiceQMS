package com.ww.boengongye.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.boengongye.entity.DfScadaLockMacData;
import com.ww.boengongye.service.DfScadaLockMacDataService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.Destination;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/scada")
@Api(tags = "SCADA")
@ResponseBody
@CrossOrigin
public class ScadaLockController {

    @Autowired
    @Qualifier("defaultJmsTemplate")
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    @Qualifier(value = "topic3")
    private Destination topic;
    @Autowired
    private Environment env;

    @Autowired
    private DfScadaLockMacDataService dfScadaLockMacDataService;


    @ApiOperation("发送锁机/解锁")
    @GetMapping(value = "/sendLockMac")
    public  Result sendLockMac(String machineCode, String status) {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("Type_Data", 32);
        data.put("MachineCode", machineCode);
        data.put("nType_CMD_Src", 4);
        data.put("nIndex_CH", 0);
        if (status.equals("锁机")) {
            data.put("MsgLevel", 444446);
        } else {
            data.put("MsgLevel", 544446);
        }


        data.put("MsgTitle", "");
        data.put("MsgTxt", "");
        data.put("nResult", 0);
        data.put("CmdCRCKey", "");
        data.put("pub_time", System.currentTimeMillis() / 1000);

        String logData = JSON.toJSONString(data);
        DfScadaLockMacData dfScadaLockMacData = new DfScadaLockMacData();
        dfScadaLockMacData.setMachineCode(machineCode);
        dfScadaLockMacData.setMacStatus(status);
        dfScadaLockMacData.setLogData(logData);
        dfScadaLockMacDataService.save(dfScadaLockMacData);

        try {
            jmsMessagingTemplate.convertAndSend(topic, mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Result(0, "查询成功");

    }
}
