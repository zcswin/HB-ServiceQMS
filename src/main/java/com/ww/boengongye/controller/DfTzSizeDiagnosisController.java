package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/dfTzSizeDiagnosis")
@CrossOrigin
@ResponseBody
@Api(tags = "天准尺寸数据诊断")
public class DfTzSizeDiagnosisController {

    @Autowired
    private DfSizeDetailCheckService dfSizeDetailCheckService;

    @Autowired
    private DfSizeCheckItemInfosCheckService dfSizeCheckItemInfosCheckService;

    @Autowired
    private DfLeadDetailCheckService dfLeadDetailCheckService;

    @Autowired
    private DfLeadCheckItemInfosCheckService dfLeadCheckItemInfosCheckService;

    @Autowired
    private DfTzDetailCheckService dfTzDetailCheckService;

    @Autowired
    private DfTzCheckItemInfosCheckService dfTzCheckItemInfosCheckService;

    @Autowired
    private DfSizeContRelationService dfSizeContRelationService;

//    @PostMapping("/importData")
//    @ApiOperation("导入数据")
//    @Transactional(rollbackFor = Exception.class)
//    public Result importData(MultipartFile file) throws Exception {
//
//        Map<String,String> colorMap = new HashMap<>();
//        colorMap.put("0000E9A","C98B-透明");
//        colorMap.put("0000DPN","C98B-黑");
//        colorMap.put("0000K03","C98B-绿");
//        colorMap.put("0000DPQ","C98B-蓝");
//        colorMap.put("0000K04","C98B-粉");
//
//        Map<String, DfSizeContRelation> contRelationMap = new HashMap<>();
//        QueryWrapper<DfSizeContRelation> contRelationQw = new QueryWrapper<>();
//        contRelationQw
//                .eq("type","TZ")
//                .in("ipqc_name","外形长1","外形长2","外形长3","外形宽1","外形宽2","外形宽3");
//
//        List<DfSizeContRelation> contRelationList = dfSizeContRelationService.list(contRelationQw);
//        if (contRelationList == null || contRelationList.size() == 0){
//            return new Result(500, "当前条件下没有相关QCP数据");
//        }
//
//        for (DfSizeContRelation relationItem : contRelationList){
//            String relationItemKey = relationItem.getFactory()+"_"+relationItem.getProject()+"_"+relationItem.getColor()+"_"+relationItem.getProcess()+"_"+relationItem.getName();
//            contRelationMap.put(relationItemKey,relationItem);
//        }
//
//        Integer leadNum = 0;
//        Integer tzNum = 0;
//
//        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ExcelImportUtil excel = new ExcelImportUtil(file);
//
//        //所有工作表名称
//        List<String> sheetNameList = excel.getAllExcelSheetNameList();
//
//        for (String sheetName : sheetNameList){
//            if("LEAD详细数据".equals(sheetName)){
//                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 69,"LEAD详细数据");
//                Map<String, Integer> snResId = new HashMap<>();
//
//                Map<Integer,String> ipqcNameMap = new HashMap<>();
//                ipqcNameMap.put(47,"FAI_8_1(Length)");
//                ipqcNameMap.put(48,"FAI_8_2(Length)");
//                ipqcNameMap.put(49,"FAI_8_3(Length)");
//                ipqcNameMap.put(66,"FAI_9_1(Width)");
//                ipqcNameMap.put(67,"FAI_9_2(Width)");
//                ipqcNameMap.put(68,"FAI_9_3(Width)");
//
//                for (int i = 1; i < strings.length; i++) {
//                    if (strings[i][3]==null||strings[i][11] == null
//                            ||strings[i][47] == null||strings[i][48] == null
//                            ||strings[i][49] == null||strings[i][66] == null
//                            ||strings[i][67] == null||strings[i][68] == null
//                    ){
//                        continue;
//                    }
//
//                    Date parse = sd.parse(strings[i][3]);
//                    //测试时间
//                    Timestamp checkTime = new Timestamp(parse.getTime());
//                    //测试结果
//                    String result = "OK";
//                    if ("failed".equalsIgnoreCase(strings[i][4])){
//                        result = "NG";
//                    }
//                    //工厂
//                    String factory = strings[i][8];
//                    //测试条码
//                    String barcode = strings[i][11];
//                    //颜色
//                    String color = "";
//                    if(colorMap.containsKey(barcode.substring(barcode.length()-7))){
//                        color = colorMap.get(barcode.substring(barcode.length()-7));
//                    }
//                    //线体
//                    String lineBody = strings[i][16];
//                    //线体数组
//                    String[] lineBodyArray = lineBody.split("-");
//                    //楼层
//                    String floor = lineBodyArray[1];
//                    //项目
//                    String project = "C98B";
//
//                    if(!"48".equals(lineBodyArray[2].substring(lineBodyArray[2].length() - 2))){
//                        continue;
//                    }
//
//                    DfLeadDetailCheck data = new DfLeadDetailCheck();
//                    data.setCheckTime(checkTime);
//                    data.setSn(barcode);
//                    data.setMachineCode("19");
//                    data.setResult(result);
//                    data.setFactory(factory);
//                    data.setProject(project);
//                    data.setColor(color);
//                    data.setFloor(floor);
//
//                    if (snResId.containsKey(barcode)) {
//                        Integer id = snResId.get(barcode);
//                        dfLeadDetailCheckService.removeById(id);
//
//                        QueryWrapper<DfLeadCheckItemInfosCheck> qw = new QueryWrapper<>();
//                        qw.eq("check_id", id);
//                        dfLeadCheckItemInfosCheckService.remove(qw);
//
//                        data.setCheckType(2);
//                    } else {
//                        data.setCheckType(1);
//                    }
//                    dfLeadDetailCheckService.save(data);
//                    Integer checkId = data.getId();
//                    snResId.put(barcode, checkId);
//
//                    List<DfLeadCheckItemInfosCheck> list = new ArrayList<>();
//                    for (int j = 47; j <= 49; j++) {
//                        String checkName = ipqcNameMap.get(j);
//                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_LEAD_"+checkName);
//                        if (null == contRelationData) {
//                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_LEAD_"+checkName);
//                        }
//                        Double standardValue = contRelationData.getQcpStandard();
//                        Double usl = contRelationData.getQcpUsl();
//                        Double lsl = contRelationData.getQcpLsl();
//                        Double checkValue = Double.valueOf(strings[i][j]);
//
//                        String checkResult = "OK";
//                        if (checkValue > usl || checkValue < lsl) {
//                            checkResult = "NG";
//                        }
//
//                        DfLeadCheckItemInfosCheck itemInfos = new DfLeadCheckItemInfosCheck();
//                        itemInfos.setCheckResult(checkResult);
//                        itemInfos.setCheckTime(checkTime);
//                        itemInfos.setCheckValue(checkValue);
//                        itemInfos.setItemName(checkName);
//                        itemInfos.setLsl(lsl);
//                        itemInfos.setStandardValue(standardValue);
//                        itemInfos.setUsl(usl);
//                        itemInfos.setCheckId(checkId);
//                        itemInfos.setCheckType(2); // 尺寸
//                        list.add(itemInfos);
//                    }
//
//                    for (int j = 66; j <= 68; j++) {
//                        String checkName = ipqcNameMap.get(j);
//                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_LEAD_"+checkName);
//                        if (null == contRelationData) {
//                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_LEAD_"+checkName );
//                        }
//                        Double standardValue = contRelationData.getQcpStandard();
//                        Double usl = contRelationData.getQcpUsl();
//                        Double lsl = contRelationData.getQcpLsl();
//                        Double checkValue = Double.valueOf(strings[i][j]);
//
//                        String checkResult = "OK";
//                        if (checkValue > usl || checkValue < lsl) {
//                            checkResult = "NG";
//                        }
//
//                        DfLeadCheckItemInfosCheck itemInfos = new DfLeadCheckItemInfosCheck();
//                        itemInfos.setCheckResult(checkResult);
//                        itemInfos.setCheckTime(checkTime);
//                        itemInfos.setCheckValue(checkValue);
//                        itemInfos.setItemName(checkName);
//                        itemInfos.setLsl(lsl);
//                        itemInfos.setStandardValue(standardValue);
//                        itemInfos.setUsl(usl);
//                        itemInfos.setCheckId(checkId);
//                        itemInfos.setCheckType(2); // 尺寸
//                        list.add(itemInfos);
//                    }
//                    dfLeadCheckItemInfosCheckService.saveBatch(list);
//                    leadNum++;
//                }
//
//            }else if("TZ天准详细数据".equals(sheetName)){
//                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 192,"TZ天准详细数据");
//                Map<String, Integer> snResId = new HashMap<>();
//
//                Map<Integer,String> ipqcNameMap = new HashMap<>();
//                ipqcNameMap.put(186,"FAI8_1(Part Length)");
//                ipqcNameMap.put(187,"FAI8_2(Part Length)");
//                ipqcNameMap.put(188,"FAI8_3(Part Length)");
//                ipqcNameMap.put(189,"FAI9_1(Part Width)");
//                ipqcNameMap.put(190,"FAI9_2(Part Width)");
//                ipqcNameMap.put(191,"FAI9_3(Part Width)");
//
//                for (int i = 1; i < strings.length; i++) {
//                    if (strings[i][3]==null||strings[i][11] == null
//                            ||strings[i][186] == null||strings[i][187] == null
//                            ||strings[i][188] == null||strings[i][189] == null
//                            ||strings[i][190] == null||strings[i][191] == null
//                    ){
//                        continue;
//                    }
//
//                    Date parse = sd.parse(strings[i][3]);
//                    //测试时间
//                    Timestamp checkTime = new Timestamp(parse.getTime());
//                    //班次
//                    String classes = "A班";
//
//                    LocalDateTime checkTimeLocal = checkTime.toLocalDateTime();
//                    if (checkTimeLocal.getHour() < 7 || checkTimeLocal.getHour() > 19){
//                        classes = "B班";
//                    }
//                    //测试结果
//                    String result = "OK";
//                    if ("failed".equalsIgnoreCase(strings[i][4])){
//                        result = "NG";
//                    }
//                    //工厂
//                    String factory = strings[i][8];
//                    //测试条码
//                    String barcode = strings[i][11];
//                    //颜色
//                    String color = "";
//                    if(colorMap.containsKey(barcode.substring(barcode.length()-7))){
//                        color = colorMap.get(barcode.substring(barcode.length()-7));
//                    }
//                    //线体
//                    String lineBody = strings[i][16];
//                    //线体数组
//                    String[] lineBodyArray = lineBody.split("-");
//                    //楼层
//                    String floor = lineBodyArray[1];
//                    //项目
//                    String project = "C98B";
//
//                    if(!"48".equals(lineBodyArray[2].substring(lineBodyArray[2].length() - 2))){
//                       continue;
//                    }
//                    String pos = strings[i][23];
//                    String gb = strings[i][24];
//                    //信道
//                    String channel = strings[i][30];
//
//                    DfTzDetailCheck data = new DfTzDetailCheck();
//                    data.setCheckTime(checkTime);
//                    data.setBarcode(barcode);
//                    data.setResult(result);
//                    data.setChannel(channel);
//                    data.setClasses(classes);
//                    data.setLineBody(lineBody);
//                    data.setPos(Integer.valueOf(pos));
//                    data.setGb(gb);
//                    data.setColor(color);
//                    data.setFactory(factory);
//                    data.setProject(project);
//                    data.setDevice("72");
//                    data.setMachineCode("72");
//                    data.setFloor(floor);
//
//                    if (snResId.containsKey(barcode)) {
//                        Integer id = snResId.get(barcode);
//                        dfTzDetailCheckService.removeById(id);
//
//                        QueryWrapper<DfTzCheckItemInfosCheck> qw = new QueryWrapper<>();
//                        qw.eq("check_id", id);
//                        dfTzCheckItemInfosCheckService.remove(qw);
//
//                        data.setCheckType(2);
//                    } else {
//                        data.setCheckType(1);
//                    }
//                    dfTzDetailCheckService.save(data);
//                    Integer checkId = data.getId();
//                    snResId.put(barcode, checkId);
//
//                    List<DfTzCheckItemInfosCheck> list = new ArrayList<>();
//                    for (int j = 186; j <= 191; j++) {
//                        String checkName = ipqcNameMap.get(j);
//                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_TZ_"+checkName);
//                        if (null == contRelationData) {
//                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_TZ_"+checkName );
//                        }
//                        Double standardValue = contRelationData.getQcpStandard();
//                        Double usl = contRelationData.getQcpUsl();
//                        Double lsl = contRelationData.getQcpLsl();
//
//                        Double checkValue = Double.valueOf(strings[i][j]);
//                        String checkResult = "OK";
//                        if (checkValue > usl || checkValue < lsl) {
//                            checkResult = "NG";
//                        }
//
//                        DfTzCheckItemInfosCheck itemInfos = new DfTzCheckItemInfosCheck();
//                        itemInfos.setCheckResult(checkResult);
//                        itemInfos.setCheckTime(checkTime);
//                        itemInfos.setCheckValue(checkValue);
//                        itemInfos.setCheckName(checkName);
//                        itemInfos.setItemName(checkName);
//                        itemInfos.setDescribes(checkName);
//                        itemInfos.setLsl(lsl);
//                        itemInfos.setStandardValue(standardValue);
//                        itemInfos.setUsl(usl);
//                        itemInfos.setCheckId(checkId);
//                        itemInfos.setCheckType(2); // 尺寸
//                        list.add(itemInfos);
//                    }
//                    dfTzCheckItemInfosCheckService.saveBatch(list);
//                    tzNum++;
//                }
//            }
//        }
//
//        return new Result(200, MessageFormat.format("成功添加LEAD数据{0}条，TZ数据{1}条", leadNum, tzNum));
//    }


    @PostMapping("/importData")
    @ApiOperation("导入数据")
    @Transactional(rollbackFor = Exception.class)
    public Result importData(MultipartFile file) throws Exception {

        Map<String,String> colorMap = new HashMap<>();
        colorMap.put("0000E9A","C98B-透明");
        colorMap.put("0000DPN","C98B-黑");
        colorMap.put("0000K03","C98B-绿");
        colorMap.put("0000DPQ","C98B-蓝");
        colorMap.put("0000K04","C98B-粉");

        Map<String, DfSizeContRelation> contRelationMap = new HashMap<>();
        QueryWrapper<DfSizeContRelation> contRelationQw = new QueryWrapper<>();
        contRelationQw
                .eq("type","TZ")
                .in("ipqc_name","外形长1","外形长2","外形长3","外形宽1","外形宽2","外形宽3");

        List<DfSizeContRelation> contRelationList = dfSizeContRelationService.list(contRelationQw);
        if (contRelationList == null || contRelationList.size() == 0){
            return new Result(500, "当前条件下没有相关QCP数据");
        }

        for (DfSizeContRelation relationItem : contRelationList){
            String relationItemKey = relationItem.getFactory()+"_"+relationItem.getProject()+"_"+relationItem.getColor()+"_"+relationItem.getProcess()+"_"+relationItem.getName();
            if ("CNC2".equals(relationItem.getProcess()) || "SPM".equals(relationItem.getProcess())){
                relationItemKey = relationItem.getFactory()+"_"+relationItem.getProject()+"_"+relationItem.getColor()+"_"+relationItem.getProcess()+"_"+relationItem.getIpqcName();
            }
            contRelationMap.put(relationItemKey,relationItem);
        }
        Integer spmNum = 0;
        Integer cnc2Num = 0;
        Integer leadNum = 0;
        Integer tzNum = 0;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);

        //所有工作表名称
        List<String> sheetNameList = excel.getAllExcelSheetNameList();

        for (String sheetName : sheetNameList){
            if("SPM详细数据".equals(sheetName)){
                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 29,"SPM详细数据");

                for (int i = 1; i < strings.length; i++) {
                    if (StringUtils.isEmpty(strings[i][27])||StringUtils.isEmpty(strings[i][1])
                            ||StringUtils.isEmpty(strings[i][2])||StringUtils.isEmpty(strings[i][16])
                    ){
                        continue;
                    }

                    //机台号
                    String machineCode = strings[i][27];
                    //备注
                    String remarks = "C98B-透明";
                    //结果
                    String result = strings[i][1];
                    //测试时间
                    Date parse = sd.parse(strings[i][2]);
                    Timestamp checkTime = new Timestamp(parse.getTime());
                    //工厂
                    String factory = "J10-1";
                    //项目
                    String project = "C98B";
                    //工序
                    String process = "SPM";
                    //颜色
                    String color = "C98B-透明";
                    //SN
                    String sn = strings[i][16];

                    DfSizeDetailCheck data = new DfSizeDetailCheck();
                    data.setMachineCode(machineCode);
                    data.setRemarks(remarks);
                    data.setResult(result);
                    data.setTestTime(checkTime);
                    data.setFactory(factory);
                    data.setProject(project);
                    data.setProcess(process);
                    data.setItemName(color);
                    data.setSn(sn);
                    data.setCheckType("1");
                    dfSizeDetailCheckService.save(data);
                    Integer checkId = data.getId();

                    DfSizeCheckItemInfosCheck detailData = new DfSizeCheckItemInfosCheck();
                    detailData.setCheckId(checkId);
                    detailData.setCheckResult(result);
                    detailData.setCheckTime(checkTime.toString());
                    detailData.setCheckValue(Double.valueOf(strings[i][3]));
                    detailData.setItemName(strings[i][12]);

                    DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_SPM_"+ strings[i][12]);
                    if (null == contRelationData) {
                        System.out.println("==========="+factory+"_"+project+"_"+color+"_SPM_"+ strings[i][12]);
                    }

                    Double standardValue = contRelationData.getQcpStandard();
                    Double usl = contRelationData.getQcpUsl();
                    Double lsl = contRelationData.getQcpLsl();

                    detailData.setStandardValue(standardValue);
                    detailData.setUsl(usl);
                    detailData.setLsl(lsl);
                    dfSizeCheckItemInfosCheckService.save(detailData);
                    spmNum++;
                }
            }else if("CNC2详细数据".equals(sheetName)){
                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 29,"CNC2详细数据");
                for (int i = 1; i < strings.length; i++) {
                    if (StringUtils.isEmpty(strings[i][26])||StringUtils.isEmpty(strings[i][1])
                            ||StringUtils.isEmpty(strings[i][2])||StringUtils.isEmpty(strings[i][16])
                    ){
                        continue;
                    }
                    //机台号
                    String machineCode = strings[i][26];
                    //备注
                    String remarks = "C98B-透明";
                    //结果
                    String result = strings[i][1];
                    //测试时间
                    Date parse = sd.parse(strings[i][2]);
                    Timestamp checkTime = new Timestamp(parse.getTime());
                    //工厂
                    String factory = "J10-1";
                    //项目
                    String project = "C98B";
                    //工序
                    String process = "CNC2";
                    //颜色
                    String color = "C98B-透明";
                    //SN
                    String sn = strings[i][16];

                    DfSizeDetailCheck data = new DfSizeDetailCheck();
                    data.setMachineCode(machineCode);
                    data.setRemarks(remarks);
                    data.setResult(result);
                    data.setTestTime(checkTime);
                    data.setFactory(factory);
                    data.setProject(project);
                    data.setProcess(process);
                    data.setItemName(color);
                    data.setSn(sn);
                    data.setCheckType("1");
                    dfSizeDetailCheckService.save(data);
                    Integer checkId = data.getId();

                    DfSizeCheckItemInfosCheck detailData = new DfSizeCheckItemInfosCheck();
                    detailData.setCheckId(checkId);
                    detailData.setCheckResult(result);
                    detailData.setCheckTime(checkTime.toString());
                    detailData.setCheckValue(Double.valueOf(strings[i][3]));
                    detailData.setItemName(strings[i][12]);

                    DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_CNC2_"+ strings[i][12]);
                    if (null == contRelationData) {
                        System.out.println("==========="+factory+"_"+project+"_"+color+"_CNC2_"+ strings[i][12]);
                    }

                    Double standardValue = contRelationData.getQcpStandard();
                    Double usl = contRelationData.getQcpUsl();
                    Double lsl = contRelationData.getQcpLsl();

                    detailData.setStandardValue(standardValue);
                    detailData.setUsl(usl);
                    detailData.setLsl(lsl);
                    dfSizeCheckItemInfosCheckService.save(detailData);
                    cnc2Num++;
                }

            }else if("LEAD详细数据".equals(sheetName)){
                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 71,"LEAD详细数据");
                Map<String, Integer> snResId = new HashMap<>();

                Map<Integer,String> ipqcNameMap = new HashMap<>();
                ipqcNameMap.put(49,"FAI_8_1(Length)");
                ipqcNameMap.put(50,"FAI_8_2(Length)");
                ipqcNameMap.put(51,"FAI_8_3(Length)");
                ipqcNameMap.put(68,"FAI_9_1(Width)");
                ipqcNameMap.put(69,"FAI_9_2(Width)");
                ipqcNameMap.put(70,"FAI_9_3(Width)");

                for (int i = 1; i < strings.length; i++) {
                    if (strings[i][3]==null||strings[i][11] == null
                            ||strings[i][49] == null||strings[i][50] == null
                            ||strings[i][51] == null||strings[i][68] == null
                            ||strings[i][69] == null||strings[i][70] == null
                    ){
                        continue;
                    }

                    Date parse = sd.parse(strings[i][3]);
                    //测试时间
                    Timestamp checkTime = new Timestamp(parse.getTime());
                    //测试结果
                    String result = "OK";
                    if ("failed".equalsIgnoreCase(strings[i][4])){
                        result = "NG";
                    }
                    //工厂
                    String factory = strings[i][9];
                    //测试条码
                    String barcode = strings[i][12];
                    //颜色
                    String color = "";
                    if(colorMap.containsKey(barcode.substring(barcode.length()-7))){
                        color = colorMap.get(barcode.substring(barcode.length()-7));
                    }
                    //线体
                    String lineBody = strings[i][17];
                    //线体数组
                    String[] lineBodyArray = lineBody.split("-");
                    //楼层
                    String floor = lineBodyArray[1];
                    //项目
                    String project = "C98B";

                    if(!"48".equals(lineBodyArray[2].substring(lineBodyArray[2].length() - 2))){
                        continue;
                    }

                    DfLeadDetailCheck data = new DfLeadDetailCheck();
                    data.setCheckTime(checkTime);
                    data.setSn(barcode);
                    data.setMachineCode("19");
                    data.setResult(result);
                    data.setFactory(factory);
                    data.setProject(project);
                    data.setColor(color);
                    data.setFloor(floor);

                    if (snResId.containsKey(barcode)) {
                        Integer id = snResId.get(barcode);
                        dfLeadDetailCheckService.removeById(id);

                        QueryWrapper<DfLeadCheckItemInfosCheck> qw = new QueryWrapper<>();
                        qw.eq("check_id", id);
                        dfLeadCheckItemInfosCheckService.remove(qw);

                        data.setCheckType(2);
                    } else {
                        data.setCheckType(1);
                    }
                    dfLeadDetailCheckService.save(data);
                    Integer checkId = data.getId();
                    snResId.put(barcode, checkId);

                    List<DfLeadCheckItemInfosCheck> list = new ArrayList<>();
                    for (int j = 49; j <= 51; j++) {
                        String checkName = ipqcNameMap.get(j);
                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_LEAD_"+checkName);
                        if (null == contRelationData) {
                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_LEAD_"+checkName);
                        }
                        Double standardValue = contRelationData.getQcpStandard();
                        Double usl = contRelationData.getQcpUsl();
                        Double lsl = contRelationData.getQcpLsl();
                        Double checkValue = Double.valueOf(strings[i][j]);

                        String checkResult = "OK";
                        if (checkValue > usl || checkValue < lsl) {
                            checkResult = "NG";
                        }

                        DfLeadCheckItemInfosCheck itemInfos = new DfLeadCheckItemInfosCheck();
                        itemInfos.setCheckResult(checkResult);
                        itemInfos.setCheckTime(checkTime);
                        itemInfos.setCheckValue(checkValue);
                        itemInfos.setItemName(checkName);
                        itemInfos.setLsl(lsl);
                        itemInfos.setStandardValue(standardValue);
                        itemInfos.setUsl(usl);
                        itemInfos.setCheckId(checkId);
                        itemInfos.setCheckType(2); // 尺寸
                        list.add(itemInfos);
                    }

                    for (int j = 68; j <= 70; j++) {
                        String checkName = ipqcNameMap.get(j);
                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_LEAD_"+checkName);
                        if (null == contRelationData) {
                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_LEAD_"+checkName );
                        }
                        Double standardValue = contRelationData.getQcpStandard();
                        Double usl = contRelationData.getQcpUsl();
                        Double lsl = contRelationData.getQcpLsl();
                        Double checkValue = Double.valueOf(strings[i][j]);

                        String checkResult = "OK";
                        if (checkValue > usl || checkValue < lsl) {
                            checkResult = "NG";
                        }

                        DfLeadCheckItemInfosCheck itemInfos = new DfLeadCheckItemInfosCheck();
                        itemInfos.setCheckResult(checkResult);
                        itemInfos.setCheckTime(checkTime);
                        itemInfos.setCheckValue(checkValue);
                        itemInfos.setItemName(checkName);
                        itemInfos.setLsl(lsl);
                        itemInfos.setStandardValue(standardValue);
                        itemInfos.setUsl(usl);
                        itemInfos.setCheckId(checkId);
                        itemInfos.setCheckType(2); // 尺寸
                        list.add(itemInfos);
                    }
                    dfLeadCheckItemInfosCheckService.saveBatch(list);
                    leadNum++;
                }

            }else if("TZ尺寸详细数据".equals(sheetName)){
                String[][] strings = excel.readExcelBlockBySheetName(1, -1, 1, 192,"TZ尺寸详细数据");
                Map<String, Integer> snResId = new HashMap<>();

                Map<Integer,String> ipqcNameMap = new HashMap<>();
                ipqcNameMap.put(186,"FAI8_1(Part Length)");
                ipqcNameMap.put(187,"FAI8_2(Part Length)");
                ipqcNameMap.put(188,"FAI8_3(Part Length)");
                ipqcNameMap.put(189,"FAI9_1(Part Width)");
                ipqcNameMap.put(190,"FAI9_2(Part Width)");
                ipqcNameMap.put(191,"FAI9_3(Part Width)");

                for (int i = 1; i < strings.length; i++) {
                    if (strings[i][3]==null||strings[i][11] == null
                            ||strings[i][186] == null||strings[i][187] == null
                            ||strings[i][188] == null||strings[i][189] == null
                            ||strings[i][190] == null||strings[i][191] == null
                    ){
                        continue;
                    }

                    Date parse = sd.parse(strings[i][3]);
                    //测试时间
                    Timestamp checkTime = new Timestamp(parse.getTime());
                    //班次
                    String classes = "A班";

                    LocalDateTime checkTimeLocal = checkTime.toLocalDateTime();
                    if (checkTimeLocal.getHour() < 7 || checkTimeLocal.getHour() > 19){
                        classes = "B班";
                    }
                    //测试结果
                    String result = "OK";
                    if ("failed".equalsIgnoreCase(strings[i][4])){
                        result = "NG";
                    }
                    //工厂
                    String factory = strings[i][8];
                    //测试条码
                    String barcode = strings[i][11];
                    //颜色
                    String color = "";
                    if(colorMap.containsKey(barcode.substring(barcode.length()-7))){
                        color = colorMap.get(barcode.substring(barcode.length()-7));
                    }
                    //线体
                    String lineBody = strings[i][16];
                    //线体数组
                    String[] lineBodyArray = lineBody.split("-");
                    //楼层
                    String floor = lineBodyArray[1];
                    //项目
                    String project = "C98B";

                    if(!"48".equals(lineBodyArray[2].substring(lineBodyArray[2].length() - 2))){
                        continue;
                    }
                    String pos = strings[i][23];
                    String gb = strings[i][24];
                    //信道
                    String channel = strings[i][30];

                    DfTzDetailCheck data = new DfTzDetailCheck();
                    data.setCheckTime(checkTime);
                    data.setBarcode(barcode);
                    data.setResult(result);
                    data.setChannel(channel);
                    data.setClasses(classes);
                    data.setLineBody(lineBody);
                    data.setPos(Integer.valueOf(pos));
                    data.setGb(gb);
                    data.setColor(color);
                    data.setFactory(factory);
                    data.setProject(project);
                    data.setDevice("72");
                    data.setMachineCode("72");
                    data.setFloor(floor);

                    if (snResId.containsKey(barcode)) {
                        Integer id = snResId.get(barcode);
                        dfTzDetailCheckService.removeById(id);

                        QueryWrapper<DfTzCheckItemInfosCheck> qw = new QueryWrapper<>();
                        qw.eq("check_id", id);
                        dfTzCheckItemInfosCheckService.remove(qw);

                        data.setCheckType(2);
                    } else {
                        data.setCheckType(1);
                    }
                    dfTzDetailCheckService.save(data);
                    Integer checkId = data.getId();
                    snResId.put(barcode, checkId);

                    List<DfTzCheckItemInfosCheck> list = new ArrayList<>();
                    for (int j = 186; j <= 191; j++) {
                        String checkName = ipqcNameMap.get(j);
                        DfSizeContRelation contRelationData = contRelationMap.get(factory+"_"+project+"_"+color+"_TZ_"+checkName);
                        if (null == contRelationData) {
                            System.out.println("==========="+barcode + factory+"_"+project+"_"+color+"_TZ_"+checkName );
                        }
                        Double standardValue = contRelationData.getQcpStandard();
                        Double usl = contRelationData.getQcpUsl();
                        Double lsl = contRelationData.getQcpLsl();

                        Double checkValue = Double.valueOf(strings[i][j]);
                        String checkResult = "OK";
                        if (checkValue > usl || checkValue < lsl) {
                            checkResult = "NG";
                        }

                        DfTzCheckItemInfosCheck itemInfos = new DfTzCheckItemInfosCheck();
                        itemInfos.setCheckResult(checkResult);
                        itemInfos.setCheckTime(checkTime);
                        itemInfos.setCheckValue(checkValue);
                        itemInfos.setCheckName(checkName);
                        itemInfos.setItemName(checkName);
                        itemInfos.setDescribes(checkName);
                        itemInfos.setLsl(lsl);
                        itemInfos.setStandardValue(standardValue);
                        itemInfos.setUsl(usl);
                        itemInfos.setCheckId(checkId);
                        itemInfos.setCheckType(2); // 尺寸
                        list.add(itemInfos);
                    }
                    dfTzCheckItemInfosCheckService.saveBatch(list);
                    tzNum++;
                }
            }
        }

        return new Result(200, MessageFormat.format("成功添加SPM数据{0}条，CNC2数据{1}条，LEAD数据{2}条，TZ数据{3}条",spmNum, cnc2Num, leadNum, tzNum));
    }
}
