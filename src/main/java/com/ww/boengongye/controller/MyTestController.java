package com.ww.boengongye.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/test")
@Api(tags = "测试")
public class MyTestController {

    @lombok.Data
    @AllArgsConstructor
    public class Response {
        private int code;
        private String message;
        private List<Data> data;

        // getters and setters ...
    }
    @lombok.Data
    @AllArgsConstructor
    public class Data {
        private String vbCode;
        private String rfid;
        private String machineCode;
        private String procedureName;
        private String procedureStepName;
        private String operateTime;

        // getters and setters ...
    }

    @PostMapping("/getData")
    @ResponseBody
    public Response getData(@RequestBody Map<String, String> params) {
        // 这里可以根据需要创建并返回一个Response对象
        return new Response(1000, null, Arrays.asList(
                new Data("HF3GTH000CW000039W+G", "580900000000000000000007", "C1-1", "CNC1", "CNC1下机", "2024-05-25 15:17:31"), 
                new Data("HF3GTH000CW000039W+G", "580900000000000000000003", "C1-1", "CNC1", "CNC1上机", "2024-05-25 15:17:04"), 
                new Data("HF3GTH000CW000039W+G", "580900000000000000000003", "QXCJ", "粗磨清洗后插架", "粗磨清洗下机", "2024-05-25 15:16:49")
        ));
    }
}