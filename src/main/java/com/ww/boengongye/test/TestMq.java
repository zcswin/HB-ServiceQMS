package com.ww.boengongye.test;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Destination;

public class TestMq {
    @Autowired
    @Qualifier("defaultJmsTemplate")
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Test
    public void product_Scheduled_Topic(){
//        if(Integer.parseInt(env.getProperty("topic4Sw"))==0){
//            double one =  ((Math.random() * 9 + 1) * Math.pow(10, 2 - 1));
//            BigDecimal two = new BigDecimal(one);
//            double three = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
//        jmsMessagingTemplate.convertAndSend("size_data", "{\"id\":1,\"project\":null,\"machineCode\":\"N20\",\"process\":\"3D抛光\",\"rfid\":\"123\",\"dataValue\":10000.0,\"createName\":null,\"factoryCode\":\"C16\",\"controlName\":\"参数\",\"classification\":\"工艺参数\",\"detail\":\"加工时间/片(混合物)\",\"checkResult\":null}");
//            jmsMessagingTemplate.convertAndSend("size_data", "{\"id\":1,\"project\":null,\"machineCode\":\"N20\",\"process\":\"3D抛光\",\"rfid\":\"123\",\"dataValue\":"+three+",\"createName\":null,\"factoryCode\":\"C16\",\"controlName\":\"PQP\",\"classification\":\"尺寸\",\"detail\":\"白片厚度\",\"checkResult\":null}");
        Destination testQueueDestination = new ActiveMQQueue("Report_ProductGague_J6-3_J6-3-9");
        jmsMessagingTemplate.convertAndSend(testQueueDestination, "{\n" +
                "    \"CheckDevCode\": \"002\",\n" +
                "    \"CheckItemInfos\": [\n" +
                "        {\n" +
                "            \"CheckResult\": \"NG\",\n" +
                "            \"CheckTime\": \"2023-03-22 20:41:05\",\n" +
                "            \"CheckValue\": 1529.8,\n" +
                "            \"ComDirection\": \"\",\n" +
                "            \"CompRatio\": 1.000,\n" +
                "            \"CompValue\": 0.000,\n" +
                "            \"ControlLowerLimit\": 0.000,\n" +
                "            \"ControlUpperLimit\": 0.000,\n" +
                "            \"DCode\": \"M617\",\n" +
                "            \"FloatValue\": 0.000,\n" +
                "            \"FocusType\": 0,\n" +
                "            \"ItemName\": \"\\n外形长1    \",\n" +
                "            \"LSL\": 29.740,\n" +
                "            \"Remark\": \"Remark\",\n" +
                "            \"RepairType\": 0,\n" +
                "            \"SN\": \"C42G\",\n" +
                "            \"StandardValue\": 29.820,\n" +
                "            \"ToolCode\": \"T1\",\n" +
                "            \"ToolFlag\": \"\",\n" +
                "            \"TrendValue\": 0.000,\n" +
                "            \"USL\": 29.900\n" +
                "        }\n" +
                "    ],\n" +
                "    \"CheckResult\": \"NG\",\n" +
                "    \"CheckTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"CheckType\": 1,\n" +
                "    \"CurrentTime\": \"2023-03-22 22:41:05\",\n" +
                "    \"FactoryCode\": \"J6-3\",\n" +
                "    \"GroupCode\": \"G001\",\n" +
                "    \"ID\": \"1c39014ae19d48f1828e6abb6b49b0b3\",\n" +
                "    \"ItemName\": \"C42G-YN-CNC2-1\",\n" +
                "    \"MacType\": \"PVG400\",\n" +
                "    \"MachineCode\": \"G001\",\n" +
                "    \"ProcessNO\": \"CNC0\",\n" +
                "    \"Remark\": \"C42G\",\n" +
                "    \"SN\": \"HDWGXQ0074L00009HR\",\n" +
                "    \"ShiftName\": \"\",\n" +
                "    \"Tester\": \"A\",\n" +
                "    \"UnitCode\": \"C001\",\n" +
                "    \"WorkShopCode\": \"J6-3-9\"\n" +
                "}");
//        jmsMessagingTemplate.convertAndSend("size_data", "{\"id\":1,\"project\":null,\"machineCode\":\"N20\",\"process\":\"3D抛光\",\"rfid\":\"123\",\"dataValue\":100.0,\"createName\":null,\"factoryCode\":\"C16\",\"controlName\":\"PQP\",\"classification\":\"尺寸\",\"detail\":\"白片厚度\",\"checkResult\":null}");


//            DfSize data=new DfSize();
//        }


    }

}
