package com.ww.boengongye.controller;


//import com.ww.boengongye.testbi.Test;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.RFID.ClampVbCode;
import com.ww.boengongye.entity.RFID.ClampVbCodeResult;
import com.ww.boengongye.entity.RFID.VbCode;
import com.ww.boengongye.entity.RFID.VbCodeResult;
import com.ww.boengongye.service.DfMacStatusSizeService;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfRiskProductService;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验类型 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-21
 */
@Controller
@RequestMapping("/Test")
@ResponseBody
@CrossOrigin
@Api(tags = "测试接口")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(DfColorController.class);

//    @Autowired
//    private static Test test = new Test();

//    @PostMapping("/upload")
//    public Result upload(MultipartFile file) throws Exception {
//        int count = test.importExcel(file);
//        return new Result(200, "成功添加" + count + "条数据");
//    }

    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;


    @Autowired
    private DfProcessService dfProcessService;

    @Autowired
    private DfRiskProductService dfRiskProductService;
    @Autowired
    Environment env;

    @PostMapping("/add")
    public String add() {
        Map<String, String> headers = new HashMap<>();
//        headers.put("X-Access-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODcxNjM4MDcsInVzZXJuYW1lIjoibGlqaW5nIn0.BUeqjiBQrH0DCfciRQsWo1P1XOoIxXNuNaM27vjAmys");
        DfProcess a=new DfProcess();
        a.setProcessName("testccc");
        String url = "http://127.0.0.1:15005/dfProcess/insert";
        return HttpUtil.postJson(url, null, headers, JSONObject.toJSONString(a),false);
    }


    @PostMapping("/testMacCode")
    public Object testMacCode(@RequestBody HttpParamTime p) {

        List<String> d1=new ArrayList<>();
        d1.add("H001");
        d1.add("J001");
        d1.add("N001");
        d1.add("H005");
        d1.add("H006");
        d1.add("H007");
        d1.add("I026");
        RFIDMachineCodeList data1=new RFIDMachineCodeList();
        data1.setMachineCodeList(d1);
        List<String> d2=new ArrayList<>();
        d2.add("G001");
        d2.add("G002");
        d2.add("G003");
        d2.add("G004");
        d2.add("G005");
        d2.add("G006");
        RFIDMachineCodeList data2=new RFIDMachineCodeList();
        data2.setMachineCodeList(d2);
        if(p.getWaitTime()==60){
            return new RFIDResult(1000,"请求成功",data1);
        }else{
            return new RFIDResult(1000,"请求成功",data2);
        }

    }

    @PostMapping("/testClampCode")
    public Object testClampCode() {
        List<RFIDRecord> data=new ArrayList<>();
        RFIDRecord d1=new RFIDRecord();
        d1.setProcedureName("CNC1");
        d1.setMachineCode("J001");
        data.add(d1);
        RFIDRecord d2=new RFIDRecord();
        d2.setProcedureName("CNC2");
        d2.setMachineCode("I001");
        data.add(d2);
        RFIDRecord d3=new RFIDRecord();
        d3.setProcedureName("CNC3");
        d3.setProcedureStepName("CNC3-下机");
        d3.setMachineCode("I001");
        d3.setOperateTime("2024-09-14 02:00:00");
        data.add(d3);
            return new Result(1000,"请求成功",data);


    }



    @PostMapping("/testVbCode2")
    public Object testVbCode2(@RequestBody HttpParamTime p) {

        List<String> d1=new ArrayList<>();
        d1.add("1");
        d1.add("2");
        d1.add("3");
        d1.add("4");
        d1.add("5");
        d1.add("6");
        d1.add("8");
        d1.add("9");
        d1.add("10");
        d1.add("11");
        d1.add("12");
        d1.add("13");
        d1.add("14");
        d1.add("15");
        d1.add("16");
        d1.add("17");
        d1.add("18");
        d1.add("19");
        d1.add("20");
        d1.add("21");
        VbCode dd=new VbCode();
        dd.setVbCodeList(d1);
        return new VbCodeResult(1000,"请求成功",dd);


    }
    @PostMapping("/testVbCode")
    public Object testVbCode(@RequestBody HttpParamTime p) {

        List<String> d1=new ArrayList<>();
        d1.add("1");
        d1.add("2");
        d1.add("3");
        d1.add("4");
        d1.add("5");
        d1.add("6");
        d1.add("8");
        d1.add("9");
        d1.add("10");
        d1.add("11");
        d1.add("12");
        d1.add("13");
        d1.add("14");
        d1.add("15");
        d1.add("16");
        d1.add("17");
        d1.add("18");
        d1.add("19");
        d1.add("20");
        d1.add("21");
        VbCode dd=new VbCode();
        dd.setVbCodeList(d1);
        return new VbCodeResult(1000,"请求成功",dd);


    }

    @PostMapping("/testSizeResult")
    public Object testSizeResult(@RequestBody RFIDBadGlassReportData p) {
        System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
            System.out.println(p.getCodeList().get(0).glassCode);

            return new RFIDResult(1000,"请求成功",null);


    }

	@PostMapping("/testUserMachineInfos")
	public Object testSizeResult() {
		System.out.println("ZZZZZZZZZZZZZZZZ");
		UserMachineInfos infos = new UserMachineInfos();
		infos.setCode("xxx");
		infos.setMessage("yyy");
		DfUserMachineInfo info1 = new DfUserMachineInfo(null, null, "CNC0", "9527", "丁成", "2023-10-18 04:10:10");
		DfUserMachineInfo info2 = new DfUserMachineInfo(null, null, "CNC3", "9999", "张三", "2023-10-19 16:10:09");
		ArrayList<DfUserMachineInfo> list = new ArrayList<>();
		list.add(info1);
		list.add(info2);
		infos.setData(list);
		return infos;
	}
    @PostMapping("/testRfidRISK")
    public Object testRfidRISK(@RequestBody RFIDBadGlassReportData p) {
        System.out.println(p.toString());
        String str="{\n" +
                "  \"code\": 1000,\n" +
                "  \"message\": null,\n" +
                "  \"data\": {\n" +
                "    \"data\": [\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NG0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NH0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NR0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NV0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000NW0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000T80000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TB0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TC0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TD0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TF0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TG0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TQ0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TW0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000TX0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000U40000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000U70000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000U90000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000UV0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000UW0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"sourceBadCode\": \"HF3HD6000NT0000E9A+9\",\n" +
                "        \"productiveTime\": \"2025-01-12 14:54:24\",\n" +
                "        \"barCode\": \"HF3HD6000UY0000E9A+9\",\n" +
                "        \"machineCode\": \"H015\",\n" +
                "        \"address\": \"8\",\n" +
                "        \"result\": \"WARN\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        // 创建一个 Map 来模拟整个 JSON 数据结构
        Map<String, Object> response = new HashMap<>();

        // 设置 code 和 message
        response.put("code", 1000);
        response.put("message", null);

        // 创建一个数据 Map，用来存储 "data"
        Map<String, Object> dataMap = new HashMap<>();

        // 创建一个 List 用来存储 "data" 中的每一项
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 模拟数据项 1
        Map<String, Object> item1 = new HashMap<>();
        item1.put("sourceBadCode", "HF3HD5001E70000E9A+9");
        item1.put("productiveTime", "2025-01-12 08:13:36");
        item1.put("barCode", "HF3HD5000SW0000E9A+9");
        item1.put("machineCode", "H013");
        item1.put("address", "7");
        item1.put("result", "WARN");
        dataList.add(item1);

        // 模拟数据项 2
        Map<String, Object> item2 = new HashMap<>();
        item2.put("sourceBadCode", "HF3HD5001E70000E9A+9");
        item2.put("productiveTime", "2025-01-12 08:13:36");
        item2.put("barCode", "HF3HD5000SX0000E9A+9");
        item2.put("machineCode", "H013");
        item2.put("address", "7");
        item2.put("result", "WARN");
        dataList.add(item2);

        // 可以继续添加更多的数据项...

        // 将 List 放入 dataMap
        dataMap.put("data", dataList);

        // 将 dataMap 放入响应 Map
        response.put("data", dataMap);

//        return str;
        return response;
    }
    @GetMapping("/testRfidRISK2")
    public Object testRfidRISK2(String  code) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        RFIDBadGlassReportData rfidData = new RFIDBadGlassReportData();
        RFIDBadGlassReport rd = new RFIDBadGlassReport();
        rd.setGlassCode(code);


            rd.setGlassState("01");


        List<RFIDBadGlassReport> dl = new ArrayList<>();
        dl.add(rd);
        rfidData.setType("尺寸");
        rfidData.setCodeList(dl);
        rfidData.setProcedureName("CNC1");
        rfidData.setProductCode("C09B");
        rfidData.setDataId(999);//把尺寸id传导mq

    Map<String, String> headers = new HashMap<>();
    //请求RFID
    HttpParamTime param = new HttpParamTime(60);
    RFIDResult3 dl2 = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDBadGlassReportURL"), null, headers,  mapper.writeValueAsString(rfidData),false), RFIDResult3.class);
        logger.info("RFID返回结果");
        logger.info(dl.toString());
        if(null!=dl2.getData().getData()){
        for (DfRiskProduct dfRiskProduct:dl2.getData().getData()   ){
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

            dfRiskProduct.setParentId(999);
            dfRiskProduct.setType("尺寸");
        }
        dfRiskProductService.saveBatch(dl2.getData().getData());
    }
        return new Result(200,"操作成功",dl.toString());
    }
}
