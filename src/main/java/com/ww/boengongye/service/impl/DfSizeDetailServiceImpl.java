
package com.ww.boengongye.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ww.boengongye.controller.ScadaLockController;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.RFID.VbCode;
import com.ww.boengongye.entity.RFID.VbCodeResult;
import com.ww.boengongye.mapper.DfSizeContStandMapper;
import com.ww.boengongye.mapper.DfSizeDetailMapper;
import com.ww.boengongye.mq.RFIDSizeResultTopicConsumer;
import com.ww.boengongye.service.*;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.*;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.baomidou.mybatisplus.core.toolkit.SystemClock.now;


/**
 * <p>
 * 尺寸数据 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
@Service
public class DfSizeDetailServiceImpl extends ServiceImpl<DfSizeDetailMapper, DfSizeDetail> implements DfSizeDetailService {
    private static final Logger logger = LoggerFactory.getLogger(DfSizeDetailServiceImpl.class);
    @Autowired
    private DfSizeDetailMapper dfSizeDetailMapper;
    @Autowired
    private DfSizeContStandMapper dfSizeContStandMapper;
    @Autowired
    private DfMacRetestService dfMacRetestService;
    @Autowired
    private DfSizeNgRateService dfSizeNgRateService;
    @Autowired
    private DfGroupService dfGroupService;
    @Autowired
    private DfGroupAdjustmentService dfGroupAdjustmentService;
    @Autowired
    private DfGroupMacOvertimeService dfGroupMacOvertimeService;
    @Autowired
    private DfFaiPassRateService dfFaiPassRateService;
    @Autowired
    private DfSizeFailService dfSizeFailService;

    @Autowired
    private DfSizeCheckItemInfosService dfSizeCheckItemInfosService;

    @Autowired
    private DfSizeNgDataService dfSizeNgDataService;

    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;

    @Autowired
    @Qualifier("defaultJmsTemplate")
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    @Qualifier(value = "topic3")
    private Destination topic;
    @Autowired
    private DfLiableManService dfLiableManService;

    @Autowired
    private DfAuditDetailService dfAuditDetailService;

    @Autowired
    private DfFlowDataService dfFlowDataService;

    @Autowired
    private DfAuditDetailNodeService dfAuditDetailNodeService;

    @Autowired
    private DfFlowOpinionService dfFlowOpinionService;
    @Autowired
    com.ww.boengongye.service.DfApprovalTimeService dfApprovalTimeService;

    @Autowired
    com.ww.boengongye.service.DfProcessService DfProcessService;
    @Autowired
    com.ww.boengongye.service.DfAoiPieceService dfAoiPieceService;

    @Autowired
    private DfLeadDetailService dfLeadDetailService;

    @Autowired
    private DfLeadCheckItemInfosService dfLeadCheckItemInfosService;

    @Autowired
    private DfGrindingInfoService dfGrindingInfoService;

    @Autowired
    private DfMacChangeService dfMacChangeService;

    @Autowired
    private DfClampService dfClampService;

    @Autowired
    private DfMachineService dfMachineService;

    @Autowired
    private DfRiskProductService dfRiskProductService;
    @Autowired
    private DfScadaLockMacDataService dfScadaLockMacDataService;


    @Autowired
    private Environment env;

    @Autowired
    private RedisUtils redisUtil;

    @Resource
    private RedisTemplate redisTemplate;

    // 获取vb数据接口
    @Value("${FindVbCodeAPI}")
    private String FindVbCodeAPI;


    // 管控参数表--行   测试项
    private static final int APPEAR_LENGTH_1 = 0;
    private static final int APPEAR_LENGTH_2 = 1;
    private static final int APPEAR_LENGTH_3 = 2;
    private static final int APPEAR_WIDTH_1 = 3;
    private static final int APPEAR_WIDTH_2 = 4;
    private static final int APPEAR_WIDTH_3 = 5;
    private static final int MI_HOLE_DIAMETER = 6;
    private static final int MI_HOLE_ROUNDNESS = 7;
    private static final int MI_HOLE_CENTER_DISTANCE_X = 8;
    private static final int MI_HOLE_CENTER_DISTANCE_Y = 9;
    private static final int DB_HOLE_DIAMETER = 10;
    private static final int DB_HOLE_ROUNDNESS = 11;
    private static final int DB_HOLE_CENTER_DISTANCE_X = 12;
    private static final int DB_HOLE_CENTER_DISTANCE_Y = 13;
    private static final int S_HOLE_DIAMETER = 14;
    private static final int S_HOLE_ROUNDNESS = 15;
    private static final int S_HOLE_CENTER_DISTANCE_X = 16;
    private static final int S_HOLE_CENTER_DISTANCE_Y = 17;
    private static final int MIC_HOLE_DIAMETER = 18;
    private static final int MIC_HOLE_ROUNDNESS = 19;
    private static final int MIC_HOLE_CENTER_DISTANCE_X = 20;
    private static final int MIC_HOLE_CENTER_DISTANCE_Y = 21;

    // 管控参数表--列    管控值
    private static final int STANDARD = 0;
    private static final int UPPER_TOLER = 1;
    private static final int LOWER_TOLER = 2;
    private static final int UPPER_LIMIT = 3;
    private static final int LOWER_LIMIT = 4;
    private static final int ISOLA_UPPER_LIMIT = 5;
    private static final int ISOLA_LOWER_LIMIT = 6;

    private static final LocalTime H7 = LocalTime.parse("07:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H9 = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H11 = LocalTime.parse("11:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H13 = LocalTime.parse("13:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H15 = LocalTime.parse("15:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H17 = LocalTime.parse("17:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H19 = LocalTime.parse("19:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H21 = LocalTime.parse("21:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H23 = LocalTime.parse("23:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H1 = LocalTime.parse("01:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H3 = LocalTime.parse("03:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private static final LocalTime H5 = LocalTime.parse("05:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private Map<String, Integer> machineMap = new HashMap<>(); // 存储机台在数组中的位置 <机台号,数组下标>
    private int[][] machineOvertimeTable = new int[500][12];  // 机台超时表
    private int machineIndex = 0; // 机台在表中的下标

    // 超时时间段
    private static final String[] OVERTIMEARRAY = new String[]{"7-9", "9-11", "11-13", "13-15", "15-17", "17-19", "19-21", "21-23", "23-1", "1-3", "3-5", "5-7"};
    // 超时测试时间
    private static final String[] OVERTIMETESTTIME = new String[]{" 09:00:00", " 11:00:00", " 13:00:00", " 15:00:00", " 17:00:00", " 19:00:00", " 21:00:00", " 23:00:00", " 01:00:00", " 03:00:00", " 05:00:00", " 07:00:00"};

    // 调机能力
    private static final Integer QUARANTINE = 0; // 隔离下标
    private static final Integer ADJUSTMENT = 1; // 调机下标
    private static final Integer NORMAL = 2; // 正常下标
    private static Lock lock = new ReentrantLock();


    @Override
    @Transactional
    /*public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);

        String[] titleArray = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17",
                "18","19","20","21","22","23"};
        List<Map<String, String>> controlMap = excel.readExcelContentDIYFromTo(1, 7, titleArray);
        double[][] contStandsTable = new double[22][7];
        int k = 0;
        for (Map<String, String> map : controlMap) {
            contStandsTable[0][k] = Double.valueOf(map.get("2"));
            contStandsTable[1][k] = Double.valueOf(map.get("3"));
            contStandsTable[2][k] = Double.valueOf(map.get("4"));
            contStandsTable[3][k] = Double.valueOf(map.get("5"));
            contStandsTable[4][k] = Double.valueOf(map.get("6"));
            contStandsTable[5][k] = Double.valueOf(map.get("7"));
            contStandsTable[6][k] = Double.valueOf(map.get("8"));
            contStandsTable[7][k] = Double.valueOf(map.get("9"));
            contStandsTable[8][k] = Double.valueOf(map.get("10"));
            contStandsTable[9][k] = Double.valueOf(map.get("11"));
            contStandsTable[10][k] = Double.valueOf(map.get("12"));
            contStandsTable[11][k] = Double.valueOf(map.get("13"));
            contStandsTable[12][k] = Double.valueOf(map.get("14"));
            contStandsTable[13][k] = Double.valueOf(map.get("15"));
            contStandsTable[14][k] = Double.valueOf(map.get("16"));
            contStandsTable[15][k] = Double.valueOf(map.get("17"));
            contStandsTable[16][k] = Double.valueOf(map.get("18"));
            contStandsTable[17][k] = Double.valueOf(map.get("19"));
            contStandsTable[18][k] = Double.valueOf(map.get("20"));
            contStandsTable[19][k] = Double.valueOf(map.get("21"));
            contStandsTable[20][k] = Double.valueOf(map.get("22"));
            contStandsTable[21][k] = Double.valueOf(map.get("23"));
            k++;
        }
        for (int i = 0; i < contStandsTable.length; i++) {
            for (int i1 = 0; i1 < contStandsTable[0].length; i1++) {
                System.out.print("[" + contStandsTable[i][i1] + "]");
            }
            System.out.println();
        }


        String[] title = new String[]{"时间","外形长1","外形长2","外形长3","外形宽1","外形宽2","外形宽3",
                "MI孔直径","MI孔真圆度","MI孔中心距X","MI孔中心距Y",
                "DB孔直径","DB孔真圆度","DB孔中心距X","DB孔中心距Y",
                "S孔直径","S孔真圆度","S孔中心距X","S孔中心距Y",
                "MIC孔直径","MIC孔真圆度","MIC孔中心距X","MIC孔中心距Y",
                "机台","状态","备注"};
        List<Map<String, String>> maps = excel.readExcelContentDIY(11, title);
        List<DfSizeDetail> list = new ArrayList<>();

        Timestamp todayTime = Timestamp.valueOf(maps.get(0).get("时间"));
        //LocalDateTime.parse(maps.get(0).get("时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String month = "单月";
        String today;
        String dayOrNight = "夜班";
        if (todayTime != null) {
            if (todayTime.getMonth() % 2 != 0) {
                month = "双月";
            }
            if (todayTime.toLocalDateTime().toLocalTime().compareTo(H7) >= 0 && todayTime.toLocalDateTime().toLocalTime().compareTo(H19) < 0) {  // 在7点到19点
                dayOrNight = "白班";
            }
            today = todayTime.toLocalDateTime().toLocalDate().toString();
        } else {
            return -1;
        }
        String factory = "J6-1";
        String project = "C16";
        String process = "CNC2";
        String linebody = "Line1";

        String secondDay = TimeUtil.getNextDay(today);


        // 设备和小组的关系
        Map<String, Integer> machineResGroup = dfGroupService.getMachineCode(month, dayOrNight, process);

        // 小组对应机台状态表
        Map<Integer, Integer> groupResMacStatNumRow = new HashMap<>();
        // 机台状态表
        int[][] macStatNumRow = new int[100][3];
        // 小组下标
        int groupIndex = 0;

        // 获得首检开机的机台
        Set<String> faiOpenMachine = new HashSet<>();
        // 获取首检通过的机台
        Set<String> faiPassMachine = new HashSet<>();

        // 获取NG的机台
        List<String> ngMac = new ArrayList<>();

        // 获取机台复测数据
        List<DfMacRetest> retestsList = new ArrayList<>();
        // 获取机台NG的时间
        Map<String, Timestamp> macResNgTime = new HashMap<>();

        // 获取NG率
        int[] ngRate = new int[22];

        List<DfSizeFail> sizeFailList = new ArrayList<>();

        for (Map<String, String> map : maps) {
            DfSizeDetail data = new DfSizeDetail();
            data.setTestTime(null == map.get("时间") ? null : Timestamp.valueOf(map.get("时间")));
            data.setAppearLength1(map.get("外形长1") == null ? null : Double.valueOf(map.get("外形长1")));
            data.setAppearLength2(map.get("外形长2") == null ? null : Double.valueOf(map.get("外形长2")));
            data.setAppearLength3(map.get("外形长3") == null ? null : Double.valueOf(map.get("外形长3")));
            data.setAppearWidth1(map.get("外形宽1") == null ? null : Double.valueOf(map.get("外形宽1")));
            data.setAppearWidth2(map.get("外形宽2") == null ? null : Double.valueOf(map.get("外形宽2")));
            data.setAppearWidth3(map.get("外形宽3") == null ? null : Double.valueOf(map.get("外形宽3")));
            data.setMiHoleDiameter(map.get("MI孔直径") == null ? null : Double.valueOf(map.get("MI孔直径")));
            data.setDbHoleDiameter(map.get("DB孔直径") == null ? null : Double.valueOf(map.get("DB孔直径")));
            data.setSHoleDiameter(map.get("S孔直径") == null ? null : Double.valueOf(map.get("S孔直径")));
            data.setMicHoleDiameter(map.get("MIC孔直径") == null ? null : Double.valueOf(map.get("MIC孔直径")));
            data.setMiHoleRoundness(map.get("MI孔真圆度") == null ? null : Double.valueOf(map.get("MI孔真圆度")));
            data.setDbHoleRoundness(map.get("DB孔真圆度") == null ? null : Double.valueOf(map.get("DB孔真圆度")));
            data.setSHoleRoundness(map.get("S孔真圆度") == null ? null : Double.valueOf(map.get("S孔真圆度")));
            data.setMicHoleRoundness(map.get("MIC孔真圆度") == null ? null : Double.valueOf(map.get("MIC孔真圆度")));
            data.setMiHoleCenterDistanceX(map.get("MI孔中心距X") == null ? null : Double.valueOf(map.get("MI孔中心距X")));
            data.setDbHoleCenterDistanceX(map.get("DB孔中心距X") == null ? null : Double.valueOf(map.get("DB孔中心距X")));
            data.setSHoleCenterDistanceX(map.get("S孔中心距X") == null ? null : Double.valueOf(map.get("S孔中心距X")));
            data.setMicHoleCenterDistanceX(map.get("MIC孔中心距X") == null ? null : Double.valueOf(map.get("MIC孔中心距X")));
            data.setMiHoleCenterDistanceY(map.get("MI孔中心距Y") == null ? null : Double.valueOf(map.get("MI孔中心距Y")));
            data.setDbHoleCenterDistanceY(map.get("DB孔中心距Y") == null ? null : Double.valueOf(map.get("DB孔中心距Y")));
            data.setSHoleCenterDistanceY(map.get("S孔中心距Y") == null ? null : Double.valueOf(map.get("S孔中心距Y")));
            data.setMicHoleCenterDistanceY(map.get("MIC孔中心距Y") == null ? null : Double.valueOf(map.get("MIC孔中心距Y")));
            data.setMachineCode(getMachineCode(map.get("机台")));
            data.setStatus(map.get("状态"));
            data.setRemarks(map.get("备注"));
            String machineStatus = judgeResult(contStandsTable, data);
            data.setMachineStatus(machineStatus);
            data.setResult(data.getMachineStatus().equals("隔离") ? "NG" : "OK");
            data.setFactory(factory);
            data.setProcess(process);
            data.setProject(project);
            data.setLinebody(linebody);
            data.setDayOrNight(dayOrNight);
            System.out.println(data);
            list.add(data);

            // NG率更新
            if (judgeOneItem(data.getAppearLength1(), APPEAR_LENGTH_1, contStandsTable) == 2) ngRate[0]++;
            if (judgeOneItem(data.getAppearLength2(), APPEAR_LENGTH_2, contStandsTable) == 2) ngRate[1]++;
            if (judgeOneItem(data.getAppearLength3(), APPEAR_LENGTH_3, contStandsTable) == 2) ngRate[2]++;
            if (judgeOneItem(data.getAppearWidth1(), APPEAR_WIDTH_1, contStandsTable) == 2) ngRate[3]++;
            if (judgeOneItem(data.getAppearWidth2(), APPEAR_WIDTH_2, contStandsTable) == 2) ngRate[4]++;
            if (judgeOneItem(data.getAppearWidth3(), APPEAR_WIDTH_3, contStandsTable) == 2) ngRate[5]++;
            if (judgeOneItem(data.getMiHoleDiameter(), MI_HOLE_DIAMETER, contStandsTable) == 2) ngRate[6]++;
            if (judgeOneItem(data.getMiHoleRoundness(), MI_HOLE_ROUNDNESS, contStandsTable) == 2) ngRate[7]++;
            if (judgeOneItem(data.getMiHoleCenterDistanceX(), MI_HOLE_CENTER_DISTANCE_X, contStandsTable) == 2) ngRate[8]++;
            if (judgeOneItem(data.getMiHoleCenterDistanceY(), MI_HOLE_CENTER_DISTANCE_Y, contStandsTable) == 2) ngRate[9]++;
            if (judgeOneItem(data.getDbHoleDiameter(), DB_HOLE_DIAMETER, contStandsTable) == 2) ngRate[10]++;
            if (judgeOneItem(data.getDbHoleRoundness(), DB_HOLE_ROUNDNESS, contStandsTable) == 2) ngRate[11]++;
            if (judgeOneItem(data.getDbHoleCenterDistanceX(), DB_HOLE_CENTER_DISTANCE_X, contStandsTable) == 2) ngRate[12]++;
            if (judgeOneItem(data.getDbHoleCenterDistanceY(), DB_HOLE_CENTER_DISTANCE_Y, contStandsTable) == 2) ngRate[13]++;
            if (judgeOneItem(data.getSHoleDiameter(), S_HOLE_DIAMETER, contStandsTable) == 2) ngRate[14]++;
            if (judgeOneItem(data.getSHoleRoundness(), S_HOLE_ROUNDNESS, contStandsTable) == 2) ngRate[15]++;
            if (judgeOneItem(data.getSHoleCenterDistanceX(), S_HOLE_CENTER_DISTANCE_X, contStandsTable) == 2) ngRate[16]++;
            if (judgeOneItem(data.getSHoleCenterDistanceY(), S_HOLE_CENTER_DISTANCE_Y, contStandsTable) == 2) ngRate[17]++;
            if (judgeOneItem(data.getMicHoleDiameter(), MIC_HOLE_DIAMETER, contStandsTable) == 2) ngRate[18]++;
            if (judgeOneItem(data.getMicHoleRoundness(), MIC_HOLE_ROUNDNESS, contStandsTable) == 2) ngRate[19]++;
            if (judgeOneItem(data.getMicHoleCenterDistanceX(), MIC_HOLE_CENTER_DISTANCE_X, contStandsTable) == 2) ngRate[20]++;
            if (judgeOneItem(data.getMicHoleCenterDistanceY(), MIC_HOLE_CENTER_DISTANCE_Y, contStandsTable) == 2) ngRate[21]++;
            if (judgeOneItem(data.getAppearLength1(), APPEAR_LENGTH_1, contStandsTable) != 0) ngRate[0]++;
            if (judgeOneItem(data.getAppearLength2(), APPEAR_LENGTH_2, contStandsTable) != 0) ngRate[1]++;
            if (judgeOneItem(data.getAppearLength3(), APPEAR_LENGTH_3, contStandsTable) != 0) ngRate[2]++;
            if (judgeOneItem(data.getAppearWidth1(), APPEAR_WIDTH_1, contStandsTable) != 0) ngRate[3]++;
            if (judgeOneItem(data.getAppearWidth2(), APPEAR_WIDTH_2, contStandsTable) != 0) ngRate[4]++;
            if (judgeOneItem(data.getAppearWidth3(), APPEAR_WIDTH_3, contStandsTable) != 0) ngRate[5]++;
            if (judgeOneItem(data.getMiHoleDiameter(), MI_HOLE_DIAMETER, contStandsTable) != 0) ngRate[6]++;
            if (judgeOneItem(data.getMiHoleRoundness(), MI_HOLE_ROUNDNESS, contStandsTable) != 0) ngRate[7]++;
            if (judgeOneItem(data.getMiHoleCenterDistanceX(), MI_HOLE_CENTER_DISTANCE_X, contStandsTable) != 0) ngRate[8]++;
            if (judgeOneItem(data.getMiHoleCenterDistanceY(), MI_HOLE_CENTER_DISTANCE_Y, contStandsTable) != 0) ngRate[9]++;
            if (judgeOneItem(data.getDbHoleDiameter(), DB_HOLE_DIAMETER, contStandsTable) != 0) ngRate[10]++;
            if (judgeOneItem(data.getDbHoleRoundness(), DB_HOLE_ROUNDNESS, contStandsTable) != 0) ngRate[11]++;
            if (judgeOneItem(data.getDbHoleCenterDistanceX(), DB_HOLE_CENTER_DISTANCE_X, contStandsTable) != 0) ngRate[12]++;
            if (judgeOneItem(data.getDbHoleCenterDistanceY(), DB_HOLE_CENTER_DISTANCE_Y, contStandsTable) != 0) ngRate[13]++;
//            if (judgeOneItem(data.getSHoleDiameter(), S_HOLE_DIAMETER, contStandsTable) != 0) ngRate[14]++;
//            if (judgeOneItem(data.getSHoleRoundness(), S_HOLE_ROUNDNESS, contStandsTable) != 0) ngRate[15]++;
//            if (judgeOneItem(data.getSHoleCenterDistanceX(), S_HOLE_CENTER_DISTANCE_X, contStandsTable) != 0) ngRate[16]++;
//            if (judgeOneItem(data.getSHoleCenterDistanceY(), S_HOLE_CENTER_DISTANCE_Y, contStandsTable) != 0) ngRate[17]++;
            if (judgeOneItem(data.getMicHoleDiameter(), MIC_HOLE_DIAMETER, contStandsTable) != 0) ngRate[18]++;
            if (judgeOneItem(data.getMicHoleRoundness(), MIC_HOLE_ROUNDNESS, contStandsTable) != 0) ngRate[19]++;
            if (judgeOneItem(data.getMicHoleCenterDistanceX(), MIC_HOLE_CENTER_DISTANCE_X, contStandsTable) != 0) ngRate[20]++;
            if (judgeOneItem(data.getMicHoleCenterDistanceY(), MIC_HOLE_CENTER_DISTANCE_Y, contStandsTable) != 0) ngRate[21]++;


            if (ngMac.contains(data.getMachineCode())) {
                // 获取响应时间
                Timestamp lastNgTime = macResNgTime.get(data.getMachineCode());
                Timestamp thisTestTime = data.getTestTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(data.getMachineCode());
                macRetest.setRetestResult(data.getResult());
                macRetest.setCreateTime(data.getTestTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(factory);
                macRetest.setProcess(process);
                macRetest.setProject(project);
                macRetest.setLinebody(linebody);
                macRetest.setDayOrNight(dayOrNight);
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    ngMac.remove(macRetest.getMachineCode());
                    macResNgTime.remove(macRetest.getMachineCode());
                } else {
                    macResNgTime.put(macRetest.getMachineCode(), data.getTestTime());
                }
            }
            // 获取NG机台
            if ("NG".equals(data.getResult()) && !ngMac.contains(data.getMachineCode())) {
                ngMac.add(data.getMachineCode());
                macResNgTime.put(data.getMachineCode(), data.getTestTime());
            }

            // 存储首检机台
            if ("FAI".equals(data.getStatus())) {
                faiOpenMachine.add(data.getMachineCode());
                if ("正常".equals(machineStatus) || "调机".equals(machineStatus)) {
                    faiPassMachine.add(data.getMachineCode());
                }
            }


            // 统计小组的调机能力
            Integer group = machineResGroup.get(data.getMachineCode());
            group = null == group ? 0 : group;
            if (!groupResMacStatNumRow.containsKey(group)) {
                groupResMacStatNumRow.put(group, groupIndex++);
            }


            switch (data.getMachineStatus()) {
                case "隔离": macStatNumRow[groupResMacStatNumRow.get(group)][QUARANTINE]++; break;
                case "调机": macStatNumRow[groupResMacStatNumRow.get(group)][ADJUSTMENT]++; break;
                case "正常": macStatNumRow[groupResMacStatNumRow.get(group)][NORMAL]++; break;
            }

            // 生成机台超时数据
            if (!machineMap.containsKey(data.getMachineCode())) {
                machineMap.put(data.getMachineCode(), machineIndex++);
            }
            if (null != data.getTestTime()) {
                updateTable(data.getMachineCode(), data.getTestTime().toLocalDateTime().toLocalTime());
            }

            save(data);

            Integer pid = data.getId();
            DfSizeFail dfSizeFail;
            dfSizeFail = judgeOneBigOrSmall(data.getAppearLength1(), APPEAR_LENGTH_1, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getAppearLength2(), APPEAR_LENGTH_2, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getAppearLength3(), APPEAR_LENGTH_3, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getAppearWidth1(), APPEAR_WIDTH_1, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getAppearWidth2(), APPEAR_WIDTH_2, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getAppearWidth3(), APPEAR_WIDTH_3, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}

            dfSizeFail = judgeOneBigOrSmall(data.getMiHoleDiameter(), MI_HOLE_DIAMETER, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMiHoleRoundness(), MI_HOLE_ROUNDNESS, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMiHoleCenterDistanceX(), MI_HOLE_CENTER_DISTANCE_X, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMiHoleCenterDistanceY(), MI_HOLE_CENTER_DISTANCE_Y, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}

            dfSizeFail = judgeOneBigOrSmall(data.getDbHoleDiameter(), DB_HOLE_DIAMETER, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getDbHoleRoundness(), DB_HOLE_ROUNDNESS, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getDbHoleCenterDistanceX(), DB_HOLE_CENTER_DISTANCE_X, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getDbHoleCenterDistanceY(), DB_HOLE_CENTER_DISTANCE_Y, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}

            dfSizeFail = judgeOneBigOrSmall(data.getSHoleDiameter(), S_HOLE_DIAMETER, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getSHoleRoundness(), S_HOLE_ROUNDNESS, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getSHoleCenterDistanceX(), S_HOLE_CENTER_DISTANCE_X, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getSHoleCenterDistanceY(), S_HOLE_CENTER_DISTANCE_Y, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}

            dfSizeFail = judgeOneBigOrSmall(data.getMicHoleDiameter(), MIC_HOLE_DIAMETER, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMicHoleRoundness(), MIC_HOLE_ROUNDNESS, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMicHoleCenterDistanceX(), MIC_HOLE_CENTER_DISTANCE_X, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}
            dfSizeFail = judgeOneBigOrSmall(data.getMicHoleCenterDistanceY(), MIC_HOLE_CENTER_DISTANCE_Y, contStandsTable);
            if (null != dfSizeFail) { dfSizeFail.setParentId(pid); dfSizeFail.setCreateTime(data.getTestTime()); sizeFailList.add(dfSizeFail);}

        }



        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for (int i : ngRate) {
            System.out.println(i);
        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        DfSizeNgRate ngData = new DfSizeNgRate();
        Integer testNum = list.size();
        ngData.setAppearLength1(ngRate[0] / testNum.doubleValue() );
        ngData.setAppearLength2(ngRate[1] / testNum.doubleValue() );
        ngData.setAppearLength3(ngRate[2] / testNum.doubleValue() );
        ngData.setAppearWidth1(ngRate[3] / testNum.doubleValue() );
        ngData.setAppearWidth2(ngRate[4] / testNum.doubleValue() );
        ngData.setAppearWidth3(ngRate[5] / testNum.doubleValue() );
        ngData.setMiHoleDiameter(ngRate[6] / testNum.doubleValue() );
        ngData.setMiHoleRoundness(ngRate[7] / testNum.doubleValue() );
        ngData.setMiHoleCenterDistanceX(ngRate[8] / testNum.doubleValue() );
        ngData.setMiHoleCenterDistanceY(ngRate[9] / testNum.doubleValue() );
        ngData.setDbHoleDiameter(ngRate[10] / testNum.doubleValue() );
        ngData.setDbHoleRoundness(ngRate[11] / testNum.doubleValue() );
        ngData.setDbHoleCenterDistanceX(ngRate[12] / testNum.doubleValue() );
        ngData.setDbHoleCenterDistanceY(ngRate[13] / testNum.doubleValue() );
        ngData.setSHoleDiameter(ngRate[14] / testNum.doubleValue() );
        ngData.setSHoleRoundness(ngRate[15] / testNum.doubleValue() );
        ngData.setSHoleCenterDistanceX(ngRate[16] / testNum.doubleValue() );
        ngData.setSHoleCenterDistanceY(ngRate[17] / testNum.doubleValue() );
        ngData.setMicHoleDiameter(ngRate[18] / testNum.doubleValue() );
        ngData.setMicHoleRoundness(ngRate[19] / testNum.doubleValue() );
        ngData.setMicHoleCenterDistanceX(ngRate[20] / testNum.doubleValue() );
        ngData.setMicHoleCenterDistanceY(ngRate[21] / testNum.doubleValue() );
        ngData.setFactory(factory);
        ngData.setProcess(process);
        ngData.setProject(project);
        ngData.setLinebody(linebody);
        ngData.setDayOrNight(dayOrNight);
        ngData.setCreateTime(todayTime);
        System.out.println(ngData);

        System.out.println("################################");
        for (DfMacRetest macRetest : retestsList) {
            System.out.println(macRetest);
        }
        System.out.println("################################");



        // 小组有多少台机器开机
        Map<Integer, Integer> groupResMachineNum = new HashMap<>();
        for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
            String machine = entry.getKey();
            Integer group = machineResGroup.get(machine);
            groupResMachineNum.merge(group, 1, Integer::sum);
        }
        // 获取开机台数
        int allOpenNum = 0;
        for (Map.Entry<Integer, Integer> entry : groupResMachineNum.entrySet()) {
            if (null == entry.getKey()) continue;
            System.out.println("小组id为" + entry.getKey() + ":" + entry.getValue());
            allOpenNum += entry.getValue();
        }

        // 获取所有小组的机台总数
        Map<Integer, Integer> groupMacNum = dfGroupService.getGroupMacNum(month, dayOrNight);
        // 获取首检开机数
        int faiOpenNum = faiOpenMachine.size();
        // 获取首检通过数
        int faiPassNum = faiPassMachine.size();
        // 获取所有机台总数
        int macNum = 0;
        for (Map.Entry<Integer, Integer> entry : groupMacNum.entrySet()) { macNum += entry.getValue(); }
        DfFaiPassRate faiPassRate = new DfFaiPassRate();
        faiPassRate.setFactory(factory);
        faiPassRate.setProcess(process);
        faiPassRate.setProject(project);
        faiPassRate.setLinebody(linebody);
        faiPassRate.setDayOrNight(dayOrNight);
        faiPassRate.setAllMacNum(macNum);
        faiPassRate.setOpenMacNum(allOpenNum);
        faiPassRate.setFaiOpenNum(faiOpenNum);
        faiPassRate.setFaiPassNum(faiPassNum);
        faiPassRate.setCreateTime(todayTime);
        System.out.println("******************************");
        System.out.println(faiPassRate);
        System.out.println("******************************");

        // 小组调机能力
        List<DfGroupAdjustment> groupAdjustmentList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupResMacStatNumRow.entrySet()) {
            Integer group = entry.getKey();
            if (0 == group) continue;
            Integer index = entry.getValue();
            DfGroupAdjustment adjustment = new DfGroupAdjustment();
            adjustment.setGroupId(group);
            adjustment.setAllMacNum(groupMacNum.get(group));
            adjustment.setQuarantineNum(macStatNumRow[index][QUARANTINE]);
            adjustment.setAdjustmentNum(macStatNumRow[index][ADJUSTMENT]);
            adjustment.setNormalNum(macStatNumRow[index][NORMAL]);
            adjustment.setUnusedNum(groupMacNum.get(group) - groupResMachineNum.get(group));
            adjustment.setAllTestNum(adjustment.getQuarantineNum() + adjustment.getAdjustmentNum() + adjustment.getNormalNum() + adjustment.getUnusedNum());
            adjustment.setQuarantineRate(adjustment.getQuarantineNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setAdjustmentRate(adjustment.getAdjustmentNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setNormalRate(adjustment.getNormalNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setUnusedRate(adjustment.getUnusedNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setDayOrNight(dayOrNight);
            adjustment.setFactory(factory);
            adjustment.setProcess(process);
            adjustment.setProject(project);
            adjustment.setLinebody(linebody);
            if ("白班" == dayOrNight){
                adjustment.setCreateTime(Timestamp.valueOf(today + " 18:59:58"));
            } else {
                adjustment.setCreateTime(Timestamp.valueOf(secondDay + " 18:59:58"));
            }
            System.out.println(adjustment);
            groupAdjustmentList.add(adjustment);
        }

        // 小组超时率
        List<DfGroupMacOvertime> groupMacOvertimeList = new ArrayList<>();
        if (dayOrNight.equals("白班")) {
            for (int i = 0; i < 6; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(today + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
//                    data.setGroupId(groupId);
//                    data.setAllMacNum(allMacNum);
//                    data.setOvertimeMacNum(overtimeMacNum);
//                    data.setOvertimeRate(overtimeRate);
//                    data.setTestTime(Timestamp.valueOf(testTime));
//                    data.setIntheTime(OVERTIMEARRAY[i]);
//                    data.setDayOrNight(dayOrNight);
//                    data.setFactory(factory);
//                    data.setProcess(process);
//                    data.setProject(project);
//                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
        } else {
            for (int i = 6; i < 8; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(today + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
//                    data.setGroupId(groupId);
//                    data.setAllMacNum(allMacNum);
//                    data.setOvertimeMacNum(overtimeMacNum);
//                    data.setOvertimeRate(overtimeRate);
//                    data.setTestTime(Timestamp.valueOf(testTime));
//                    data.setIntheTime(OVERTIMEARRAY[i]);
//                    data.setDayOrNight(dayOrNight);
//                    data.setFactory(factory);
//                    data.setProcess(process);
//                    data.setProject(project);
//                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
            for (int i = 8; i < 12; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(secondDay + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
//                    data.setGroupId(groupId);
//                    data.setAllMacNum(allMacNum);
//                    data.setOvertimeMacNum(overtimeMacNum);
//                    data.setOvertimeRate(overtimeRate);
//                    data.setTestTime(Timestamp.valueOf(testTime));
//                    data.setIntheTime(OVERTIMEARRAY[i]);
//                    data.setDayOrNight(dayOrNight);
//                    data.setFactory(factory);
//                    data.setProcess(process);
//                    data.setProject(project);
//                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            // 记录这个时间段小组的不超时机台
            Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
            for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                String machine = entry.getKey();
                Integer group = machineResGroup.get(machine);
                groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
            }
            for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                System.out.println("机台id为" + entry.getKey() + ":" + "不超时为:" + entry.getValue());
            }
        }

        for (int i = 0; i < machineIndex; i++) {
            for (int j = 0; j < machineOvertimeTable[0].length; j++) {
                System.out.print(machineOvertimeTable[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }

        // 添加数据
        saveBatch(list);
        dfMacRetestService.saveBatch(retestsList);
        dfSizeNgRateService.save(ngData);
        dfGroupAdjustmentService.saveBatch(groupAdjustmentList);
        dfGroupMacOvertimeService.saveBatch(groupMacOvertimeList);
        dfFaiPassRateService.save(faiPassRate);
        dfSizeFailService.saveBatch(sizeFailList);

        return list.size();
    }*/

    /*private void updateTable(String machineCode, LocalTime testTime) {
        if (testTime.compareTo(H7) >= 0 && testTime.compareTo(H9) < 0) {  // 在7点到9点
            machineOvertimeTable[machineMap.get(machineCode)][0] = 1;
        } else if (testTime.compareTo(H9) >= 0 && testTime.compareTo(H11) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][1] = 1;
        } else if (testTime.compareTo(H11) >= 0 && testTime.compareTo(H13) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][2] = 1;
        } else if (testTime.compareTo(H13) >= 0 && testTime.compareTo(H15) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][3] = 1;
        } else if (testTime.compareTo(H15) >= 0 && testTime.compareTo(H17) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][4] = 1;
        } else if (testTime.compareTo(H17) >= 0 && testTime.compareTo(H19) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][5] = 1;
        } else if (testTime.compareTo(H19) >= 0 && testTime.compareTo(H21) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][6] = 1;
        } else if (testTime.compareTo(H21) >= 0 && testTime.compareTo(H23) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][7] = 1;
        } else if (testTime.compareTo(H23) >= 0 && testTime.compareTo(H1) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][8] = 1;
        } else if (testTime.compareTo(H1) >= 0 && testTime.compareTo(H3) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][9] = 1;
        } else if (testTime.compareTo(H3) >= 0 && testTime.compareTo(H5) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][10] = 1;
        } else if (testTime.compareTo(H5) >= 0 && testTime.compareTo(H7) < 0) {
            machineOvertimeTable[machineMap.get(machineCode)][11] = 1;
        }
    }*/

    // 判断所有项机器状态
    /*private String judgeResult(double[][] contStandsTable, DfSizeDetail dfSizeDetail) {
        int result = 0;
        result = judgeOneItem(dfSizeDetail.getAppearLength1(), APPEAR_LENGTH_1, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getAppearLength2(), APPEAR_LENGTH_2, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getAppearLength3(), APPEAR_LENGTH_3, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getAppearWidth1(), APPEAR_WIDTH_1, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getAppearWidth2(), APPEAR_WIDTH_2, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getAppearWidth3(), APPEAR_WIDTH_3, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMiHoleDiameter(), MI_HOLE_DIAMETER, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getDbHoleDiameter(), DB_HOLE_DIAMETER, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getSHoleDiameter(), S_HOLE_DIAMETER, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMicHoleDiameter(), MIC_HOLE_DIAMETER, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMiHoleRoundness(), MI_HOLE_ROUNDNESS, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getDbHoleRoundness(), DB_HOLE_ROUNDNESS, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getSHoleRoundness(), S_HOLE_ROUNDNESS, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMicHoleRoundness(), MIC_HOLE_ROUNDNESS, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMiHoleCenterDistanceX(), MI_HOLE_CENTER_DISTANCE_X, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getDbHoleCenterDistanceX(), DB_HOLE_CENTER_DISTANCE_X, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getSHoleCenterDistanceX(), S_HOLE_CENTER_DISTANCE_X, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMicHoleCenterDistanceX(), MIC_HOLE_CENTER_DISTANCE_X, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMiHoleCenterDistanceY(), MI_HOLE_CENTER_DISTANCE_Y, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getDbHoleCenterDistanceY(), DB_HOLE_CENTER_DISTANCE_Y, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getSHoleCenterDistanceY(), S_HOLE_CENTER_DISTANCE_Y, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        result = judgeOneItem(dfSizeDetail.getMicHoleCenterDistanceY(), MIC_HOLE_CENTER_DISTANCE_Y, contStandsTable) > result ? ++result : result; if (result == 2) return "隔离";
        if (result == 0) {
            return "正常";
        } else {
            return "调机";
        }
    }*/


    // 判断一项的机器状态
    /*private int judgeOneItem(Double itemValue, int tableIndex, double[][] contStandsTable) {
        if (null == itemValue) return 2;
        int result = 0;
        if (itemValue > contStandsTable[tableIndex][UPPER_LIMIT] &&
                itemValue <= contStandsTable[tableIndex][ISOLA_UPPER_LIMIT] ||
                itemValue < contStandsTable[tableIndex][LOWER_LIMIT] &&
                        itemValue >= contStandsTable[tableIndex][ISOLA_LOWER_LIMIT]) {
            result = 1;
        } else if (itemValue > contStandsTable[tableIndex][ISOLA_UPPER_LIMIT] ||
                itemValue < contStandsTable[tableIndex][ISOLA_LOWER_LIMIT]) {
            result = 2;
        }
        return result;
    }*/

    // 判断一项的偏大偏小
    /*private DfSizeFail judgeOneBigOrSmall(double itemValue, int tableIndex, double[][] contStandsTable) {
        if (itemValue <= contStandsTable[tableIndex][ISOLA_UPPER_LIMIT] && itemValue >= contStandsTable[tableIndex][ISOLA_LOWER_LIMIT]) {
            return null;   // OK
        }
        String type = null;
        switch (tableIndex) {
            case 0: type = "外形长1"; break;
            case 1: type = "外形长2"; break;
            case 2: type = "外形长3"; break;
            case 3: type = "外形宽1"; break;
            case 4: type = "外形宽2"; break;
            case 5: type = "外形宽3"; break;
            case 6: type = "MI孔直径"; break;
            case 7: type = "MI孔真圆度"; break;
            case 8: type = "MI孔中心距X"; break;
            case 9: type = "MI孔中心距Y"; break;
            case 10: type = "DB孔直径"; break;
            case 11: type = "DB孔真圆度"; break;
            case 12: type = "DB孔中心距X"; break;
            case 13: type = "DB孔中心距Y"; break;
            case 14: type = "S孔直径"; break;
            case 15: type = "S孔真圆度"; break;
            case 16: type = "S孔中心距X"; break;
            case 17: type = "S孔中心距Y"; break;
            case 18: type = "MIC孔直径"; break;
            case 19: type = "MIC孔真圆度"; break;
            case 20: type = "MIC孔中心距X"; break;
            case 21: type = "MIC孔中心距Y"; break;
        }
        String bigOrSmall;
        Double dValue;
        if (itemValue > contStandsTable[tableIndex][STANDARD]) {
            bigOrSmall = type + "偏大";
            dValue = itemValue - contStandsTable[tableIndex][STANDARD];
        } else {
            bigOrSmall = type + "偏小";
            dValue = contStandsTable[tableIndex][STANDARD] - itemValue;
        }
        String stand = contStandsTable[tableIndex][STANDARD] + "±" + contStandsTable[tableIndex][UPPER_TOLER];
        DfSizeFail result = new DfSizeFail();
//        result.setDiffValue(dValue);
        result.setStandard(stand);
        result.setBadCondition(bigOrSmall);
        result.setBadType(type);
//        result.setPractical(itemValue);
        return result;
    }*/

    // 获取机台的机台号
    /*String getMachineCode(String code) {
        if (null == code) return null;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) >= 48 && code.charAt(i) <= 57) {
                while (i < code.length() && code.charAt(i) >= 48 && code.charAt(i) <= 57) {
                    result.append(code.charAt(i++));
                }
                return new String(result);
            }
        }
        return null;
    }*/

    public List<DfSizeDetail> listAllByFactory(String factory, String process, String project, String linebody, String dayOrNight,
                                               String startDate, String endDate) {
        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != dayOrNight, "day_or_night", dayOrNight)
                .between(null != startDate && null != endDate, "test_time", startDate, endDate);
        return dfSizeDetailMapper.listAllByFactory(qw);
    }

    @Override
    public DfSizeDetail getOkRate(String factory, String process, String project, String linebody, String dayOrNight,
                                  String startDate, String endDate) {
        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != dayOrNight, "day_or_night", dayOrNight)
                .between(null != startDate && null != endDate, "test_time", startDate, endDate);
        return dfSizeDetailMapper.getOkRate(qw);
    }

    @Override
    public IPage<DfSizeDetail> listJoinIds(IPage<DfSizeDetail> page, Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listJoinIds(page, wrapper);
    }

    /**
     * @param content
     * @param type    mq:update 防呆:foolProofig
     * @return
     */
    @Override
    public String saveMqData(String content, String type) throws ParseException {

        if(env.getProperty("logSizeData","N").equals("Y")){
            logger.info("进入尺寸");
            logger.info(content);
        }

        SizeQueueData datas = new Gson().fromJson(content, SizeQueueData.class);


        if (null == datas.getDataType()) {
            datas.setDataType(105);
        }

        // 判断是否为LEAD数据
        if (datas.getDataType() == 101) {
            System.out.println("收到LEAD数据");
            DfLeadMq leadMq = new Gson().fromJson(content, DfLeadMq.class);
            DfLeadDetail detail = new DfLeadDetail();
            detail.setCheckTime(Timestamp.valueOf(leadMq.getCheckTime()));
//            System.out.println(leadMq.getCheckTime());
            detail.setSn(leadMq.getSn());
            detail.setMachineCode(leadMq.getMachineCode());
            detail.setWorkPosition(leadMq.getDetnCH() + leadMq.getCheckDevCode());
            detail.setResult(leadMq.getResult());
            if ("点检".equals(leadMq.getCheckType()) || "正常".equals(leadMq.getCheckType())) {
                detail.setCheckType(1);  // 1: 一次
            } else if ("复测".equals(leadMq.getCheckType())) {
                QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
                qw.eq("sn", leadMq.getSn());
                dfLeadDetailService.remove(qw);
                detail.setCheckType(2);  // 2: 重复
            } else {
                return "未识别的类型";
            }
            dfLeadDetailService.save(detail);
            System.out.println(detail);
            Integer checkId = detail.getId();
            List<DfLeadCheckItemInfos> infoList = new ArrayList<>();

            // 存储倒角、尺寸、外观测试项名称
            String[] chamferNames = {"S孔底倒角X_1", "S孔底倒角X_2", "S孔底倒角X_3", "S孔底倒角X_4", "MIC孔底倒角X_1", "MIC孔底倒角X_2", "MIC孔底倒角X_3",
                    "MIC孔底倒角X_4", "ML孔底倒角X_1", "ML孔底倒角X_2", "ML孔底倒角X_3", "ML孔底倒角X_4", "MW孔底倒角X_5", "MW孔底倒角X_6",
                    "MW孔底倒角X_7", "MW孔底倒角X_8", "外形底倒角_P35", "外形底倒角_P21", "外形底倒角_P01", "外形底倒角_P19", "外形底倒角_P03", "外形底倒角_P17", "外形底倒角_P31",
                    "外形底倒角_P07", "外形底倒角_P28", "外形底倒角_P10", "外形底倒角_P25", "外形底倒角_P13"};
            String[] sizeNames = {"ML孔真圆度", "MW孔真圆度", "MIC孔真圆度", "S孔真圆度", "ML孔直径", "MW孔直径",
                    "MIC孔直径", "S孔直径", "ML孔位置度", "MW孔位置度", "MIC孔位置度", "S孔位置度", "孔ML中心到产品中心X", "孔ML中心到产品中心Y",
                    "孔MW中心到产品中心X", "孔MW中心到产品中心Y", "孔MIC中心到产品中心X", "孔MIC中心到产品中心Y", "孔S中心到产品中心X",
                    "孔S中心到产品中心Y", "外形长_1", "外形长_2", "外形长_3", "外形宽_1", "外形宽_2", "外形宽_3"};
            String[] appearNames = {"孔崩_S", "孔崩_ML", "孔崩_MW",
                    "孔崩_MIC", "孔划_S", "孔砂轮线_S", "孔未亮_S", "孔划_MW", "孔砂轮线_MW", "孔未亮_MW", "孔划_ML", "孔砂轮线_ML", "孔未亮_ML"};
            Set<String> chamferSet = new HashSet<>();
            Set<String> sizeSet = new HashSet<>();
            Set<String> appearSet = new HashSet<>();
            chamferSet.addAll(Arrays.asList(chamferNames));
            sizeSet.addAll(Arrays.asList(sizeNames));
            appearSet.addAll(Arrays.asList(appearNames));

            for (DfLeadCheckItemInfos checkItemInfo : leadMq.getCheckItemInfos()) {
                checkItemInfo.setCheckTime(Timestamp.valueOf(leadMq.getCheckTime()));
                checkItemInfo.setCheckId(checkId);
                if (chamferSet.contains(checkItemInfo.getItemName())) checkItemInfo.setCheckType(2);
                else if (sizeSet.contains(checkItemInfo.getItemName())) checkItemInfo.setCheckType(1);
                else if (appearSet.contains(checkItemInfo.getItemName())) checkItemInfo.setCheckType(3);
                infoList.add(checkItemInfo);
            }
            if (infoList.size() > 0) {
                dfLeadCheckItemInfosService.saveBatch(infoList);
            }
            return "";
        }

        //判断是尺寸还是AOI
        if (datas.getDataType() == 103) {
            dfAoiPieceService.saveMqData(content);
            return "";
        }

//        if(InitializeCheckRule.sizeStatus.containsKey(datas.getMachineCode())){
        QueryWrapper<DfProcess> pqw = new QueryWrapper<>();
        if (datas.getProcessNO().equals("单面磨")) {
            pqw.eq("process_name", "单面磨底");
            datas.setProcessNO("单面磨底");
        }else if(datas.getProcessNO().equals("Logo贴膜")|| datas.getProcessNO().equals("logo贴膜")) {
            pqw.eq("process_name", "覆膜");
            datas.setProcessNO("覆膜");
        } else {
            pqw.eq("process_name", datas.getProcessNO());
        }
        if (datas.getItemName().equals("C100-JT")) {
            datas.setItemName("C100-白色");
        }
        pqw.select("first_code");
        pqw.last("limit 1");
        DfProcess process = DfProcessService.getOne(pqw);
//        if (null != process && null != process.getFirstCode() && process.getFirstCode().equals(datas.getMachineCode().substring(0, 1))) {
            StringBuilder questionName = new StringBuilder();
            StringBuilder gkbz = new StringBuilder();
            StringBuilder xcsj = new StringBuilder();


            Timestamp workTime = null;  // 加工时间
            String rfid = null;  // RFID架子编号
            Integer knife_first_life = null;  // 刀具1寿命
            Integer knife_second_life = null;  // 刀具2寿命
            Integer knife_third_life = null;  // 刀具3寿命
            Integer knife_fourth_life = null;  // 刀具4寿命
            Integer change_knife_status = null;  // 有无换刀 (0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)
            Integer debug_status = 0;  // 有无调机(0=无调机；1=测量异常调机后首件)
            Integer change_class_status = null;  // 有无换班(0=无换班；1=换班后首件)
            Integer change_clamp_status = 0;  // 有无换夹治具(0=无换夹治具；1=换夹治具后首件)
            Integer machine_life = null;  // 机台寿命

            // 通过调用接口获取玻璃的加工时间
            HashMap<String, String> body = new HashMap<>();
            body.put("vbCode", datas.getSn());
            List<Map<String, String>> response = getResponse(body);
            if ( null !=  response) {
                for (Map<String, String> item : response) {
                    if(null!=item){
                        if (null!=item.get("procedureStepName")&&item.get("procedureStepName").contains("CNC") && null!=item.get("procedureStepName")&&item.get("procedureStepName").contains("上机")) {
                            System.out.println("得到上机数据:" + item);
                            workTime = item.get("operateTime") != null ? Timestamp.valueOf(item.get("operateTime")) : null;
                            rfid = item.get("rfid");
                        }
                    }

                }
            }
            // 获取刀具寿命
            QueryWrapper<DfGrindingInfo> g1qw = new QueryWrapper<>();
            g1qw.eq("machine_code", datas.getMachineCode()).eq("n_index_tool", 1).orderByDesc("id").last("limit 1");
            DfGrindingInfo first = dfGrindingInfoService.getOne(g1qw);
            QueryWrapper<DfGrindingInfo> g2qw = new QueryWrapper<>();
            g2qw.eq("machine_code", datas.getMachineCode()).eq("n_index_tool", 2).orderByDesc("id").last("limit 1");
            DfGrindingInfo second = dfGrindingInfoService.getOne(g2qw);
            QueryWrapper<DfGrindingInfo> g3qw = new QueryWrapper<>();
            g3qw.eq("machine_code", datas.getMachineCode()).eq("n_index_tool", 3).orderByDesc("id").last("limit 1");
            DfGrindingInfo third = dfGrindingInfoService.getOne(g3qw);
            QueryWrapper<DfGrindingInfo> g4qw = new QueryWrapper<>();
            g4qw.eq("machine_code", datas.getMachineCode()).eq("n_index_tool", 4).orderByDesc("id").last("limit 1");
            DfGrindingInfo fourth = dfGrindingInfoService.getOne(g4qw);
            if ( null!=  first) { knife_first_life = first.getNTotalUsagePro(); }
            if ( null != second) { knife_second_life = second.getNTotalUsagePro(); }
            if ( null != third) { knife_third_life = third.getNTotalUsagePro(); }
            if ( null != fourth) { knife_fourth_life = fourth.getNTotalUsagePro(); }

            // 获取有无换刀, 先获取上一刀的数据
            QueryWrapper<DfGrindingInfo> lastGrindingQW = new QueryWrapper<>();
            lastGrindingQW.eq("machine_code", datas.getMachineCode()).orderByDesc("id").last("limit 1");
            DfGrindingInfo lastGrinding = dfGrindingInfoService.getOne(lastGrindingQW);
            if (null != lastGrinding) {
                change_knife_status = lastGrinding.getChangeKnifeStatus();
            }

            // 获取有无调机
            System.out.println("获取有无调机");
            QueryWrapper<DfMacChange> macChangeQW = new QueryWrapper<>();
            macChangeQW.eq("machine_code", datas.getMachineCode())
                    .eq("is_use", 0)
                    .orderByDesc("id")
                    .last("limit 1");
            DfMacChange macChangeOne = dfMacChangeService.getOne(macChangeQW);
            if (macChangeOne != null) {
                macChangeOne.setIsUse(1);
                dfMacChangeService.updateById(macChangeOne);
                System.out.println("更改了机台调机数据,id:" + macChangeOne.getId());
                debug_status = 1;
            }

            // 获取有无换班
            String today = workTime != null ? workTime.toString().substring(0,10) : null;
            String startTime;
            String endTime = null;
            if (null != today) {
                endTime = workTime.toString();
                if (workTime.getHours() < 7) {
                    startTime = TimeUtil.getLastDay(today) + " 19:00:00";
                } else if (workTime.getHours() < 19){
                    startTime = today + " 07:00:00";
                } else {
                    startTime = today + " 19:00:00";
                }
                QueryWrapper<DfSizeDetail> sizeQW = new QueryWrapper<>();
                sizeQW.eq("machine_code", datas.getMachineCode())
                        .between("work_time", startTime, endTime);

                Integer the_class_count = dfSizeDetailMapper.selectCount(sizeQW);
                if (the_class_count > 0) {
                    change_class_status = 0;
                } else {
                    change_class_status = 1;
                }
            }

            // 获取有无换夹治具
            System.out.println("获取有无换夹治具");
            QueryWrapper<DfClamp> clampQW = new QueryWrapper<>();
            clampQW.eq("machine", datas.getMachineCode())
                    .eq("is_use", 0)
                    .orderByDesc("id")
                    .last("limit 1");
            DfClamp clampOne = dfClampService.getOne(clampQW);
            if (null !=clampOne ) {
                clampOne.setIsUse(1);
                dfClampService.updateById(clampOne);
                System.out.println("更改了夹治具数据,id:" + clampOne.getId());
                change_clamp_status = 1;
            }

            // 获取机台寿命
            System.out.println("获取机台寿命");
            QueryWrapper<DfMachine> machineQW = new QueryWrapper<>();
            machineQW.eq("code", datas.getMachineCode())
                    .last("limit 1");
            DfMachine machineOne = dfMachineService.getOne(machineQW);
            if ( null !=  machineOne) {
                machine_life = machineOne.getMachineLife();
            }


            //轴号
            String bearing="1";
            //分割增加轴号之后的机台号
            if(datas.getMachineCode().indexOf("-")!=-1){
                bearing=datas.getMachineCode().split("-")[1];
                datas.setMachineCode(datas.getMachineCode().split("-")[0]);
            }

//        System.out.println(datas.toString());
            DfSizeDetail dd = new DfSizeDetail();
            dd.setBearing(datas.getMachineCode()+"-"+bearing);
            dd.setWorkTime(workTime);
            dd.setFixtureId(rfid);
            dd.setKnifeFirstLife(knife_first_life);
            dd.setKnifeSecondLife(knife_second_life);
            dd.setKnifeThirdLife(knife_third_life);
            dd.setKnifeFourthLife(knife_fourth_life);
            dd.setChangeKnifeStatus(change_knife_status);
            dd.setDebugStatus(debug_status);
            dd.setChangeClassStatus(change_class_status);
            dd.setChangeClampStatus(change_clamp_status);
            dd.setMachineLife(machine_life);
            dd.setWorkShopCode(datas.getWorkShopCode());
            dd.setUnitCode(datas.getUnitCode());
            dd.setTester(datas.getTester());
            dd.setSn(datas.getSn());
            dd.setShiftName(datas.getShiftName());
            dd.setItemName(datas.getItemName());
            dd.setMacType(datas.getMacType());
            dd.setCheckDevCode(datas.getCheckDevCode());
            dd.setCheckType(datas.getCheckType());
            dd.setFactory(datas.getFactoryCode());
            dd.setTestTime(Timestamp.valueOf(datas.getCheckTime()));
            dd.setCurTime(datas.getCurrentTime());
            dd.setRemarks(datas.getRemark());
            dd.setResult(datas.getCheckResult());
            dd.setCheckId(datas.getId());
            dd.setGroupCode(datas.getGroupCode());
            dd.setProcess(datas.getProcessNO());
            dd.setMachineCode(datas.getMachineCode());
            dd.setProject(datas.getItemName().split("-")[0]);
            if(null!=datas.getErrCode()){
                dd.setErrCode(datas.getErrCode());
            }


            // 获取砂轮信息并改状态为已使用
            System.out.println("获取砂轮信息并改状态为已使用");
            QueryWrapper<DfGrindingInfo> grindingQW = new QueryWrapper<>();
            grindingQW.eq("machine_code", datas.getMachineCode())
                    .eq("n_status_tool", 1)
                    .eq("is_use", 0)
                    .orderByDesc("id")
                    .last("limit 1");
            DfGrindingInfo one = dfGrindingInfoService.getOne(grindingQW);
            if ( null != one ) {
                one.setIsUse(1);
                dfGrindingInfoService.updateById(one);
//                System.out.println("更改了砂轮数据,id:" + one.getId());
            }

            //防呆
            Boolean fdType = false;
            int ngPhase=0;//NG阶段复测数据
//            Boolean fullCheck=false;//是否发送全检
//            Boolean sendProduct=false;//是否直接发送给生产,适用于NG阶段2
            Boolean riskSkip=false;//风险品跳过发送审批单流程
            //取消防呆24-10-31
            ValueOperations valueOperations = redisTemplate.opsForValue();
            //获取NG阶段
            SizeQueueData phaseData=(SizeQueueData) valueOperations.get("sizePhase:"+datas.getMachineCode()+"-"+bearing);
            if(null!=phaseData&&null!=phaseData.getNgPhase()){
                ngPhase=Integer.parseInt(phaseData.getNgPhase());
            }


            List<DfSizeCheckItemInfos> saveList = new ArrayList<>();
            List<DfSizeNgData> saveNgList = new ArrayList<>();
            List<DfSizeFail> saveSfList = new ArrayList<>();
            int allStatus = 2;
            int count = 0;
            if (null != datas.getCheckItemInfos()) {
                for (DfSizeCheckItemInfos d : datas.getCheckItemInfos()) {
                    //设置风险品类型,检测类型:1抽检;2-调机;3-开机首检;4-调机后首件;5-确认片;6-IPQC
                    if(null!=datas.getCheckType()){
                        if(datas.getCheckType().equals("3")||datas.getCheckType().equals("4")){
                            d.setCheckType("FAI");
                        }else {
                            d.setCheckType("CPK");
                        }
                    }
                    if(ngPhase>0){
                        d.setCheckType("风险品");
                    }

                    d.setItemName(d.getItemName().replaceAll("\\n", "").trim());

                    if (InitializeCheckRule.sizeContStand.containsKey(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName())) {
                        d.setCheckId(dd.getCheckId());

                        d.setLsl(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaLowerLimit());
                        d.setUsl(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaUpperLimit());
                        d.setStandardValue(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getStandard());

                        if (d.getCheckValue() > InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaUpperLimit() || d.getCheckValue() < InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaLowerLimit()) {
                            DfSizeFail sf = new DfSizeFail();
                            sf.setBadType(d.getItemName());
                            sf.setStandard(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getStandard() + "");
                            sf.setPractical(d.getCheckValue());
                            //公差
                            sf.setTolerance("±" + InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getUpperToler());
                            if (null != InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getFai()) {
                                sf.setFai(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getFai());
                            }

                            if (d.getCheckValue() > InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaUpperLimit()) {
                                d.setBadCondition(d.getItemName() + "偏大");
                                sf.setDiffValue(d.getCheckValue() - InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaUpperLimit());
                                sf.setBadCondition(d.getItemName() + "偏大");
                            } else {
                                d.setBadCondition(d.getItemName() + "偏小");
                                sf.setDiffValue(InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getIsolaLowerLimit() - d.getCheckValue());
                                sf.setBadCondition(d.getItemName() + "偏小");
                            }
                            if (InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getKeyPoint().equals("1")) {
                                allStatus = 3;
                                d.setKeyPoint("1");
//                                System.out.println("触发NG项");
                                if (count > 0) {
                                    questionName.append(";");
                                    gkbz.append(";");
                                    xcsj.append(";");
                                } else {
                                    questionName.append(dd.getProcess() + ";" + dd.getMachineCode() + "机;");
                                }

                                questionName.append(d.getBadCondition());
                                gkbz.append(d.getItemName() + ":" + InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getStandard() + "±" + InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getUpperToler());
                                xcsj.append(d.getItemName() + ":" + d.getCheckValue());
                                count++;
                            }
                            saveSfList.add(sf);

                            d.setCheckResult("NG");


                        } else {
                            if (InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getKeyPoint().equals("1")) {
                                d.setKeyPoint("1");
                            }
                            d.setCheckResult("OK");
                        }
                        saveList.add(d);

//转移到下方判断是否存入缓存
//                        updateDataToLimit50Cache(dd.getProject(), dd.getProcess(), dd.getItemName(), d.getItemName(),
//                                dd.getMachineCode(), dd.getTestTime(), d.getCheckValue());


                    }
                }
            }


            if (allStatus == 2) {
                dd.setResult("OK");

                int tjStatus = 0;
                if (null != datas.getCheckItemInfos()) {
                    for (DfSizeCheckItemInfos d : datas.getCheckItemInfos()) {
                        if (InitializeCheckRule.sizeContStand.containsKey(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName())) {

                            if (d.getCheckValue() > InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getUpperLimit() || d.getCheckValue() < InitializeCheckRule.sizeContStand.get(dd.getProject() + dd.getItemName() + datas.getProcessNO() + d.getItemName()).getLowerLimit()) {

                                tjStatus = 1;


                            }

                        }
                    }
                }

                if (tjStatus == 0) {
                    dd.setInfoResult("OK");
                } else {
                    dd.setInfoResult("TJ");
                }

            } else {
                dd.setResult("NG");
                dd.setInfoResult("NG");
            }

            //判断是不是风险品

            //检查是否为风险品
//            QueryWrapper<DfRiskProduct>riskQw=new QueryWrapper<>();
//            riskQw.eq("bar_code",dd.getSn());
//            riskQw.eq("type","尺寸");
//            riskQw.gt("create_time",TimeUtil.getBeforeDay(5));//最长延迟5天
//            riskQw.last("limit 1");
//            DfRiskProduct riskData=dfRiskProductService.getOne(riskQw);



            //判断是否是风险品,风险品不触发流程
            if (ngPhase==0&&env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())) {
                QueryWrapper<DfRiskProduct>riskQw=new QueryWrapper<>();
                //只获取一天内的数据
                riskQw.eq("bar_code",datas.getSn());
                riskQw.gt("create_time",TimeUtil.getBeforeDay(1));
                riskQw.last("limit 1");
                int risk=dfRiskProductService.count(riskQw);
                if(risk>0){
                    riskSkip=true;
                }
            }

            if (!riskSkip&&type.equals("upload")) {//根据模式判断是否防呆
                Object v = valueOperations.get("FP" + dd.getMachineCode());
                if (null != v) {
                    SizeQueueData fdDatas = new Gson().fromJson(v.toString(), SizeQueueData.class);
                    if (null != fdDatas) {
//                        if(env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())&&allStatus == 3){
//                            if(fdDatas.getNgPhase().equals("1")){
//                                fullCheck=true;
//                                fdType = true;
//                            }else  if(fdDatas.getNgPhase().equals("2")){
//
//                                if(null!=riskData){//如果玻璃NG了,并且为风险品,就直接发给生产
//                                    sendProduct=true;//直接发送给生产
                                    redisTemplate.delete("FP" + dd.getMachineCode());
//                                }
//                            }
//                        }else {
//                            redisTemplate.delete("FP" + dd.getMachineCode());
//                        }


                    } else {
                        fdType = true;


                    }
                } else {
                    fdType = true;
//                    if(null!=riskData){
//                        riskSkip=true;//如果已经没有防呆信息还有风险品数据,则只保存数据和发送结果给RFID
//                    }
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            int interceptAuditId=0;//风险品拦截单的id
//            if (allStatus == 3 && fdType == true&&sendProduct==false&&riskSkip==false) {
//            if (allStatus == 3 && fdType == true) {
            if (allStatus == 3 && fdType == true&&!env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                datas.setFoolProofingTime(TimeUtil.getNowTimeByNormal());
//                if(fullCheck){
//                    datas.setNgPhase("2");
//                }else{
//                    datas.setNgPhase("1");
//                }
                valueOperations.set("FP" + dd.getMachineCode(), gson.toJson(datas));

                logger.info("防呆:" + dd.getMachineCode());


                //推送结果给RFID,防呆也推给RFID 2024-11-12 谢志思
//                if (null != dd.getErrCode() && !dd.getErrCode().equals("1")&&null != dd.getSn() && !dd.getSn().equals("") && dd.getSn().length() > 15) {
                if (!riskSkip&&null != dd.getSn() && !dd.getSn().equals("") && dd.getSn().length() > 15) {
                    RFIDBadGlassReportData rfidData = new RFIDBadGlassReportData();
                    RFIDBadGlassReport rd = new RFIDBadGlassReport();
                    rd.setGlassCode(dd.getSn());

                    if (dd.getResult().equals("OK")) {
                        rd.setGlassState("03");
                    } else {
                        rd.setGlassState("01");
                    }

                    List<RFIDBadGlassReport> dl = new ArrayList<>();
                    dl.add(rd);
                    rfidData.setType("尺寸");
                    rfidData.setCodeList(dl);
                    rfidData.setProcedureName(dd.getProcess().equals("单面磨") ? "单面磨底" : dd.getProcess());
                    rfidData.setProductCode(dd.getProject());
                    rfidData.setDataId(interceptAuditId);//把尺寸id传导mq
                    try {
                        jmsMessagingTemplate.convertAndSend(env.getProperty("RFIDBadGlassReportTopic"), mapper.writeValueAsString(rfidData));
                        logger.info("防呆发送RFID尺寸玻璃码:" + dd.getSn());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                String isSaveSizeData="Y";
                if (!riskSkip&&ngPhase==0&&allStatus != 2&&env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())){
                    isSaveSizeData="N";
                }
                if(isSaveSizeData.equals("Y")){
                    dfSizeDetailMapper.insert(dd);
                }


                //根据配置文件判断是否发送审批单
                if (!riskSkip&&ngPhase==0&&env.getProperty("sizeCheck").equals("0")&&(env.getProperty("sizeCheckProcess","ALL").indexOf(datas.getProcessNO())==-1||env.getProperty("sizeCheckProcess","ALL").equals("ALL"))) {

                    if (allStatus != 2) {
                        if(env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())){
                            datas.setNgPhase("1");
//


                            //防呆也发送ng信息
                            QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                            sqw.eq("type", "sizeFoolProofing");
                            sqw.eq("problem_level", "2");
//                sqw.last("limit 1");
                            if (TimeUtil.getBimonthly() == 0) {
                                sqw.like("bimonthly", "双月");
                            } else {
                                sqw.like("bimonthly", "单月");
                            }
                            sqw.like("process_name", dd.getProcess());
                            List<DfLiableMan> lm = dfLiableManService.list(sqw);
                            if (lm.size() > 0) {
                                StringBuilder manCode = new StringBuilder();
                                StringBuilder manName = new StringBuilder();
                                int manCount = 0;
                                for (DfLiableMan l : lm) {
                                    if (manCount > 0) {
                                        manCode.append(",");
                                        manName.append(",");
                                    }
                                    manCode.append(l.getLiableManCode());
                                    manName.append(l.getLiableManName());
                                    manCount++;
                                }
                                DfAuditDetail aud = new DfAuditDetail();
//                                if(null!=datas.getNgPhase()){
//                                    aud.setNgPhase(datas.getNgPhase());
//                                }
                                aud.setMacCode(dd.getMachineCode());
                                aud.setLine(env.getProperty("LineBody", "Line-23"));
                                aud.setParentId(dd.getId());
                                aud.setDataType("风险隔离全检");
                                aud.setDepartment(dd.getProcess());
                                aud.setAffectMac("1");
                                aud.setAffectNum(1.0);
                                aud.setControlStandard(gkbz.toString());
                                aud.setImpactType("尺寸");
                                aud.setIsFaca("1");
                                aud.setColor(dd.getItemName());
                                //问题名称和现场实际调换
                                aud.setQuestionName(xcsj.toString());
                                aud.setScenePractical(questionName.toString());
                                aud.setProcess(dd.getProcess());
                                aud.setProjectName(datas.getItemName().split("-")[0]);
                                aud.setProject(datas.getItemName().split("-")[0]);
                                QueryWrapper<DfLiableMan> sqw2 = new QueryWrapper<>();
                                sqw2.eq("type", "sizeInitiator");
                                sqw2.eq("problem_level", "2");
                                sqw2.like("process_name", dd.getProcess());
                                sqw2.last("limit 1");
                                if (TimeUtil.getDayShift() == 1) {
                                    sqw2.like("bimonthly", "双月");
                                } else {
                                    sqw2.like("bimonthly", "单月");
                                }
                                DfLiableMan rpm = dfLiableManService.getOne(sqw2);
                                if (null != rpm) {
                                    aud.setReportMan(rpm.getLiableManName());
                                    aud.setCreateName(rpm.getLiableManName());
                                    aud.setCreateUserId(rpm.getLiableManCode());
                                }else{
                                    aud.setCreateName("系统");
                                    aud.setReportMan("系统");
                                }

                                aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                aud.setIpqcNumber(TimeUtil.getNowTimeLong());
                                //由于MQ没有返回对应的检测类型,现在默认为过程检CPK
                                if (null != dd.getCheckType() && !dd.getCheckType().equals("")) {
                                    if (dd.getCheckType().equals("1")) {
                                        aud.setQuestionType("CPK");
                                    } else if (dd.getCheckType().equals("3")) {
                                        aud.setQuestionType("FAI");
                                    } else {
                                        aud.setQuestionType("CPK");
                                    }
                                } else {
                                    aud.setQuestionType("CPK");
                                }


                                aud.setDecisionLevel("Level2");
//                    aud.setHandlingSug("15min内测量复测"+(fullCheck?"(全检)":""));
                                aud.setHandlingSug("15min内测量复测");
                                aud.setResponsible2(manName.toString());
                                aud.setResponsibleId2(manCode.toString());

                                dfAuditDetailService.save(aud);
                                interceptAuditId=aud.getId();
                                DfFlowData fd = new DfFlowData();
                                if(null!=datas.getNgPhase()){
                                    fd.setNgPhase(datas.getNgPhase());
                                }
                                fd.setFlowLevel(2);
                                fd.setDataType(aud.getDataType());
                                fd.setFlowType(aud.getDataType());
                                fd.setName("IPQC_尺寸检测_"+dd.getMachineCode()+"_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
                                fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
                                fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
                                fd.setCreateName(aud.getCreateName());
                                fd.setCreateUserId(aud.getCreateUserId());

                                fd.setNowLevelUser(aud.getResponsibleId2());
                                fd.setNowLevelUserName(aud.getResponsible2());
                                fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

                                fd.setFlowLevelName(aud.getDecisionLevel());
                                QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                                atQw.eq("type", "尺寸");
                                atQw.last("limit 1");
                                DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
                                if (null != at) {
                                    if (fd.getFlowLevelName().equals("Level1")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel1());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                                    } else if (fd.getFlowLevelName().equals("Level2")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel2());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                                    } else if (fd.getFlowLevelName().equals("Level3")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel3());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                                    }
                                }
                                //设置显示人
                                fd.setShowApprover(fd.getNowLevelUserName());
                                dfFlowDataService.save(fd);
                            }

                            //锁机
                            Map<String, Object> data = new HashMap<>();
                            data.put("Type_Data", 32);
                            data.put("MachineCode", datas.getMachineCode());
                            data.put("nType_CMD_Src", 4);
                            data.put("nIndex_CH", 0);
//                    if (status.equals("锁机")) {
                        data.put("MsgLevel", 444446);
//                    } else {
//                            data.put("MsgLevel", 544446);
//                    }
                            data.put("MsgTitle", "");
                            data.put("MsgTxt", "");
                            data.put("nResult", 0);
                            data.put("CmdCRCKey", "");
                            data.put("pub_time", System.currentTimeMillis() / 1000);

                            String logData = JSON.toJSONString(data);
                            DfScadaLockMacData dfScadaLockMacData = new DfScadaLockMacData();
                            dfScadaLockMacData.setMachineCode(datas.getMachineCode());
                            dfScadaLockMacData.setMacStatus("锁机");
                            dfScadaLockMacData.setLogData(logData);
                            dfScadaLockMacDataService.save(dfScadaLockMacData);

                            try {
                                jmsMessagingTemplate.convertAndSend(topic, mapper.writeValueAsString(data));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }



                        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                        sqw.eq("type", "size");
                        sqw.eq("problem_level", "2");
//                sqw.last("limit 1");
                        if (TimeUtil.getBimonthly() == 0) {
                            sqw.like("bimonthly", "双月");
                        } else {
                            sqw.like("bimonthly", "单月");
                        }
                        sqw.like("process_name", dd.getProcess());
                        List<DfLiableMan> lm = dfLiableManService.list(sqw);
                        if (lm.size() > 0) {
                            StringBuilder manCode = new StringBuilder();
                            StringBuilder manName = new StringBuilder();
                            int manCount = 0;
                            for (DfLiableMan l : lm) {
                                if (manCount > 0) {
                                    manCode.append(",");
                                    manName.append(",");
                                }
                                manCode.append(l.getLiableManCode());
                                manName.append(l.getLiableManName());
                                manCount++;
                              }
                            DfAuditDetail aud = new DfAuditDetail();
                            if(env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())){
                            aud.setNgPhase("1");
                            }
                            aud.setMacCode(dd.getMachineCode());
                            aud.setnNumTool(dd.getMachineCode()+"-"+bearing);
                            aud.setSn(datas.getSn());
                            aud.setLine(env.getProperty("LineBody", "Line-23"));
                            aud.setParentId(dd.getId());
                            aud.setDataType("尺寸");
                            aud.setDepartment(dd.getProcess());
                            aud.setAffectMac("1");
                            aud.setAffectNum(1.0);
                            aud.setControlStandard(gkbz.toString());
                            aud.setImpactType("尺寸");
                            aud.setIsFaca("0");
                            aud.setColor(dd.getItemName());
                            //问题名称和现场实际调换
                            aud.setQuestionName(xcsj.toString());
                            aud.setScenePractical(questionName.toString());
                            aud.setProcess(dd.getProcess());
                            aud.setProjectName(datas.getItemName().split("-")[0]);
                            aud.setProject(datas.getItemName().split("-")[0]);
                            QueryWrapper<DfLiableMan> sqw2 = new QueryWrapper<>();
                            sqw2.eq("type", "sizeInitiator");
                            sqw2.eq("problem_level", "2");
                            sqw2.like("process_name", dd.getProcess());
                            sqw2.last("limit 1");
                            if (TimeUtil.getDayShift() == 1) {
                                sqw2.like("bimonthly", "双月");
                            } else {
                                sqw2.like("bimonthly", "单月");
                            }
                            DfLiableMan rpm = dfLiableManService.getOne(sqw2);
                            if (null != rpm) {
                                aud.setReportMan(rpm.getLiableManName());
                                aud.setCreateName(rpm.getLiableManName());
                                aud.setCreateUserId(rpm.getLiableManCode());
                            }else{
                                aud.setCreateName("系统");
                            }

                            aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                            aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                            aud.setIpqcNumber(TimeUtil.getNowTimeLong());
                            //由于MQ没有返回对应的检测类型,现在默认为过程检CPK
                            if (null != dd.getCheckType() && !dd.getCheckType().equals("")) {
                                if (dd.getCheckType().equals("1")) {
                                    aud.setQuestionType("CPK");
                                } else if (dd.getCheckType().equals("3")) {
                                    aud.setQuestionType("FAI");
                                } else {
                                    aud.setQuestionType("CPK");
                                }
                            } else {
                                aud.setQuestionType("CPK");
                            }


                            aud.setDecisionLevel("Level2");
                            aud.setHandlingSug("全检风险批");
                            aud.setResponsible2(manName.toString());
                            aud.setResponsibleId2(manCode.toString());

//                    aud.setCreateName(lm.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
                            dfAuditDetailService.save(aud);

                            if(env.getProperty("sizeFullCheckProcess").contains(dd.getProcess())){
                                //保存任务单id用于后续绑定流程节点
                                datas.setAuditId(aud.getId());
                                //保存ng阶段到Redis
                                valueOperations.set("sizePhase:"+datas.getMachineCode()+"-"+bearing,datas);
                                saveNode(ngPhase,"NG",aud.getId());
                            }
                            interceptAuditId=aud.getId();//非风险品时也保存到尺寸ng单中
                            DfFlowData fd = new DfFlowData();
                            fd.setFlowLevel(2);
                            fd.setDataType(aud.getDataType());
                            fd.setFlowType(aud.getDataType());
                            fd.setName("IPQC_尺寸检测_"+aud.getProcess()+"_"+dd.getMachineCode()+"_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
                            fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
                            fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
                            fd.setCreateName(aud.getCreateName());
                            fd.setCreateUserId(aud.getCreateUserId());

                            fd.setNowLevelUser(aud.getResponsibleId2());
                            fd.setNowLevelUserName(aud.getResponsible2());
                            fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

                            fd.setFlowLevelName(aud.getDecisionLevel());
                            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                            atQw.eq("type", "尺寸");
                            atQw.last("limit 1");
                            DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
                            if (null != at) {
                                if (fd.getFlowLevelName().equals("Level1")) {
                                    fd.setReadTimeMax(at.getReadTimeLevel1());
                                    fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                                } else if (fd.getFlowLevelName().equals("Level2")) {
                                    fd.setReadTimeMax(at.getReadTimeLevel2());
                                    fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                                } else if (fd.getFlowLevelName().equals("Level3")) {
                                    fd.setReadTimeMax(at.getReadTimeLevel3());
                                    fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                                }
                            }
                            //设置显示人
                            fd.setShowApprover(fd.getNowLevelUserName());
                            dfFlowDataService.save(fd);
                        }
                    }


                }


//                System.out.println(datas.getMachineCode());
                //方案三 ,如果不发起审批单机台状态也不更新
                if ((env.getProperty("sizeCheckProcess","ALL").indexOf(datas.getProcessNO())==-1||env.getProperty("sizeCheckProcess","ALL").equals("ALL"))) {
                    DfMacStatusSize sz2 = InitializeCheckRule.sizeStatus.get(datas.getMachineCode());
                    if(null!=sz2){
                        sz2.setStatusidCur(allStatus);
                        sz2.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                        InitializeCheckRule.sizeStatus.put(datas.getMachineCode(), sz2);
                    }


                }


                if (null != saveList && saveList.size() > 0&&isSaveSizeData.equals("Y")) {//判断复测数据不插入缓存,因为复测数据需要覆盖,覆盖操作复杂
                    for (DfSizeCheckItemInfos sci : saveList) {
                        sci.setCheckId(dd.getId() + "");
                        if (null != sci.getCheckResult() && null != sci.getKeyPoint()) {
                            if (sci.getCheckResult().equals("NG") && sci.getKeyPoint().equals("1")) {
                                DfSizeNgData ngData = new DfSizeNgData();
                                ngData.setCheckId(sci.getCheckId());
                                ngData.setCheckTime(Timestamp.valueOf(sci.getCheckTime()));
                                ngData.setCheckValue(sci.getCheckValue());
                                ngData.setLsl(sci.getLsl());
                                ngData.setUsl(sci.getUsl());
                                ngData.setItemName(sci.getItemName());
                                ngData.setBadCondition(sci.getBadCondition());

                                saveNgList.add(ngData);

                            }
                        }
                        // 更新该不良项近50组数据
                        if ("1".equals(sci.getKeyPoint())) {
                            // 尺寸看板下方
                            updateItemDataToLimit50Cache(dd.getProject(), dd.getProcess(), dd.getItemName(), sci.getItemName(),
                                    dd.getTestTime(), sci.getCheckValue(), sci.getCheckType());
                            // 单项测试项
                            // 如果机台号是大写字母开头才更新机台单项测试项
                            if (dd.getBearing().matches("[A-Z].*")) {
                                updateDataToLimit50Cache(dd.getProject(), dd.getProcess(), dd.getItemName(), sci.getItemName(),
                                        dd.getBearing(), dd.getTestTime(), sci.getCheckValue());
                            }
                        }

                    }
                }
                if (null != saveSfList && saveSfList.size() > 0) {
                    for (DfSizeFail f : saveSfList) {
                        if(isSaveSizeData.equals("Y")){
                            f.setParentId(dd.getId());
                        }else{
                            //需要复测的时候
                            f.setParentId(datas.getAuditId());
                            f.setType("warm");
                        }

                    }
                }
                if (null != saveList && saveList.size() > 0&&isSaveSizeData.equals("Y")) {
                    dfSizeCheckItemInfosService.saveBatch(saveList);
                }
                if (null != saveNgList && saveNgList.size() > 0) {
                    if(isSaveSizeData.equals("Y")) {
                        dfSizeNgDataService.saveBatch(saveNgList);
                    }
                }
                if (null != saveSfList && saveSfList.size() > 0) {
                    dfSizeFailService.saveBatch(saveSfList);
                }

                //mq 方案二
                // 如果不发起审批单机台状态也不更新
                if ((env.getProperty("sizeCheckProcess","ALL").indexOf(datas.getProcessNO())==-1||env.getProperty("sizeCheckProcess","ALL").equals("ALL"))) {

                    DfMacStatusSize sz = new DfMacStatusSize();
                    sz.setMachineCode(datas.getMachineCode());
                    sz.setStatusidCur(allStatus);
//        sz.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

                    try {
                        jmsMessagingTemplate.convertAndSend(env.getProperty("sizeStatusTopic"), mapper.writeValueAsString(sz));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

                List<SizeCheckYield> putList = new ArrayList<>();
                Object v = valueOperations.get("SizeMacYield:" + datas.getMachineCode());
                if (null != v) {
                    SizeCheckYieldList fdDatas = new Gson().fromJson(v.toString(), SizeCheckYieldList.class);
                    putList.addAll(fdDatas.getData());
                }
                SizeCheckYield scy = new SizeCheckYield();
                scy.setId(dd.getId());
                scy.setResult(dd.getResult());
                scy.setQuestion(questionName.toString());
                putList.add(scy);
                int deleteCount = putList.size() - 24;
                for (int i = 0; i < deleteCount; i++) {
                    putList.remove(i);
                }
                SizeCheckYieldList scyl = new SizeCheckYieldList();
                scyl.setData(putList);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                valueOperations.set("SizeMacYield:" + dd.getMachineCode(), gson.toJson(scyl));//存入缓存
                int ngCount = 0;
                StringBuilder saveQquestionName = new StringBuilder();
                for (SizeCheckYield d : putList) {
                    if (d.getResult().equals("NG")) {

                        if (ngCount > 0) {
                            saveQquestionName.append(",");
                        }
                        saveQquestionName.append(d.getQuestion());
                        ngCount++;
                    }
                }
                if (ngCount > 1) {
                    //ng时才发起
                    if (isSaveSizeData.equals("Y")&&!riskSkip&&ngPhase==0&&allStatus != 2    &&(env.getProperty("sizeCheck24Pcs","N").equals("Y"))) {
                        QueryWrapper<DfAuditDetail> qw = new QueryWrapper<>();
                        qw.eq("question_type", "良率报警");
                        qw.eq("data_type", "尺寸");
                        qw.eq("mac_code", dd.getMachineCode());
                        qw.isNull("end_time");
                        qw.last("limit 1");
                        DfAuditDetail already = dfAuditDetailService.getOne(qw);
                        if (null == already) {
                            QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                            sqw.eq("type", "size");
                            sqw.eq("problem_level", "2");
//                sqw.last("limit 1");
                            if (TimeUtil.getBimonthly() == 0) {
                                sqw.like("bimonthly", "双月");
                            } else {
                                sqw.like("bimonthly", "单月");
                            }
                            sqw.like("process_name", dd.getProcess());
                            List<DfLiableMan> lm = dfLiableManService.list(sqw);
                            if (lm.size() > 0) {
                                StringBuilder manCode = new StringBuilder();
                                StringBuilder manName = new StringBuilder();
                                int manCount = 0;
                                for (DfLiableMan l : lm) {
                                    if (manCount > 0) {
                                        manCode.append(",");
                                        manName.append(",");
                                    }
                                    manCode.append(l.getLiableManCode());
                                    manName.append(l.getLiableManName());
                                    manCount++;
                                }
                                DfAuditDetail aud = new DfAuditDetail();
                                DfFlowData fd = new DfFlowData();
                                aud.setLine(dd.getLinebody());
                                aud.setParentId(dd.getId());
                                aud.setDataType("尺寸");
                                aud.setQuestionType("良率报警");
                                aud.setControlStandard("机台良率预警:24片中NG" + ngCount + "片");
                                aud.setQuestionName("机台良率预警:机台" + dd.getMachineCode() + "_24片中NG" + ngCount + "片,");
                                aud.setScenePractical("机台良率预警:机台" + dd.getMachineCode() + "_24片中NG" + ngCount + "片," + "_" + TimeUtil.getYesterdayNoYear() + ",请及时处理");
                                fd.setName("机台良率预警:机台" + dd.getMachineCode() + "_24片中NG" + ngCount + "片," + TimeUtil.getYesterdayNoYear());
                                aud.setDepartment(dd.getProcess());
                                aud.setAffectMac("1");
//                        aud.setAffectNum(1.0);
                                aud.setMacCode(dd.getMachineCode());
                                aud.setImpactType("尺寸");
                                aud.setIsFaca("0");
                                aud.setProcess(dd.getProcess());
                                aud.setProjectName(dd.getProject());
                                aud.setReportMan("系统");
                                aud.setCreateName("系统");
                                aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                aud.setIpqcNumber(TimeUtil.getNowTimeLong());
                                aud.setDecisionLevel("Level1");
                                aud.setHandlingSug("全检风险批");
                                aud.setResponsible(manName.toString());
                                aud.setResponsibleId(manCode.toString());
                                dfAuditDetailService.save(aud);

                                fd.setFlowLevel(1);
                                fd.setDataType(aud.getDataType());
                                fd.setFlowType(aud.getDataType());
                                fd.setDataId(aud.getId());
                                fd.setStatus("待确认");
                                fd.setCreateName(aud.getCreateName());
                                fd.setCreateUserId(aud.getCreateUserId());
                                fd.setNowLevelUser(aud.getResponsibleId());
                                fd.setNowLevelUserName(aud.getResponsible());
                                fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                                fd.setFlowLevelName(aud.getDecisionLevel());
                                QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                                atQw.eq("type", "尺寸");
                                atQw.last("limit 1");
                                DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
                                if (null != at) {
                                    if (fd.getFlowLevelName().equals("Level1")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel1());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                                    } else if (fd.getFlowLevelName().equals("Level2")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel2());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                                    } else if (fd.getFlowLevelName().equals("Level3")) {
                                        fd.setReadTimeMax(at.getReadTimeLevel3());
                                        fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                                    }
                                }
                                //设置显示人
                                fd.setShowApprover(fd.getNowLevelUserName());
                                dfFlowDataService.save(fd);
                            }
                        }
                    }

                }
                //目前只做CNC3
                if (isSaveSizeData.equals("Y")&&!riskSkip&&ngPhase==0&&env.getProperty("ISSizeIPQC", "N").equals("Y")
                        && ((env.getProperty("IPQCProcess", "all").contains(dd.getProcess())||env.getProperty("IPQCProcess", "all").equals("all") )
                        && (env.getProperty("IPQCMac", "all").contains(dd.getMachineCode()) || env.getProperty("IPQCMac", "all").equals("all")))) {
                    //动态ipqc收严时间(小时)
                    double IpqcTimeStandardTighten=Double.valueOf(env.getProperty("IpqcTimeStandardTighten","1.5"));
                    //动态ipqc正常时间(小时)
                    double IpqcTimeStandardNormal=Double.valueOf(env.getProperty("IpqcTimeStandardNormal","3"));
                    //动态ipqc放宽时间阶段一(小时)
                    double IpqcTimeStandardRelax1=Double.valueOf(env.getProperty("IpqcTimeStandardRelax1","6"));
                    //动态ipqc放宽时间阶段二(小时)
                    double IpqcTimeStandardRelax2=Double.valueOf(env.getProperty("IpqcTimeStandardRelax2","12"));
                    String checkType = "抽检";
                    if (dd.getCheckType().equals("2")) {
                        checkType = "调机";
                    } else if (dd.getCheckType().equals("3")) {
                        checkType = "开机首检";
                    }
                    if (redisTemplate.hasKey("IpqcSize:" + datas.getMachineCode())) {
                        Object v2 = valueOperations.get("IpqcSize:" + datas.getMachineCode());
                        if (null != v2) {
                            DynamicIpqcMac dim = new Gson().fromJson(v2.toString(), DynamicIpqcMac.class);

                            if (!dim.getRuleName().equals("耗材寿命衰减")) {

                                if (dd.getInfoResult().equals("NG")) {
                                    dim.setNgCount(dim.getNgCount() + 1);
                                }
                                dim.setTotalCount(dim.getTotalCount() + 1);

                                //判断是否连续ok
                                if (dd.getInfoResult().equals("NG")) {
                                    dim.setSizeOkCount(0);
                                    int isWarmCode = 0;//0否 1是
                                    VbCode alreadyVbCode = new VbCode();
                                    //判断是否风险批玻璃
                                    if (redisTemplate.hasKey("IpqcSizeWarmCode:" + datas.getMachineCode())) {
                                        Object vb = valueOperations.get("IpqcSizeWarmCode:" + datas.getMachineCode());
                                        if (null != vb) {
                                            alreadyVbCode = new Gson().fromJson(vb.toString(), VbCode.class);
                                            for (String c : alreadyVbCode.getVbCodeList()) {
                                                if (c.equals(dd.getSn())) {
                                                    isWarmCode = 1;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (dim.getRuleName().equals("⻛险批次全检") && isWarmCode == 1) {

                                        //总数量累加
                                        alreadyVbCode.setNowCount(alreadyVbCode.getNowCount() + 1);
                                        alreadyVbCode.setNgCount(alreadyVbCode.getNgCount() + 1);
//                                        if (alreadyVbCode.getNowCount() >= Integer.parseInt(env.getProperty("RiskLotCount", "21")) && alreadyVbCode.getNgCount() > 2) {
                                        if (alreadyVbCode.getNgCount() > 2) {
                                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "突发性⼤量不良", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                            dim.setSenMesCount(1);
                                            dim.setNowCount(0);
                                            dim.setFrequency(IpqcTimeStandardTighten);
                                            dim.setSpecifiedCount(2);
                                            dim.setRuleName("突发性⼤量不良");
                                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                            //清空缓存
                                            redisTemplate.delete("IpqcSizeWarmCode:" + dd.getMachineCode());
                                        } else {
                                            redisTemplate.opsForValue().set("IpqcSizeWarmCode:" + dd.getMachineCode(), gson.toJson(alreadyVbCode));

                                        }
//
                                    } else if (dim.getRuleName().equals("更换耗材")) {
                                        dim.setSenMesCount(1);
                                        dim.setNowCount(0);
                                        dim.setSpecifiedCount(2);
                                        dim.setFrequency(IpqcTimeStandardTighten);
                                    } else {
                                        if (dim.getNgCount() / dim.getTotalCount() * 100 >= Integer.parseInt(env.getProperty("IpqcMqcYield", "10"))) {
                                            //发通知
                                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "连续性机台异常", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                            dim.setSenMesCount(1);
                                            dim.setNowCount(0);
//                                            dim.setTotalCount(2);
                                            dim.setSpecifiedCount(2);
                                            dim.setFrequency(IpqcTimeStandardTighten);
                                            dim.setRuleName("连续性机台异常");
                                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                        } else {
                                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "⻛险批次全检", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                            dim.setSenMesCount(1);
                                            dim.setNowCount(0);
//                                            dim.setTotalCount(1);
                                            dim.setSpecifiedCount(1);
                                            dim.setFrequency(IpqcTimeStandardTighten);
                                            dim.setRuleName("⻛险批次全检");
                                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                            //获取同批次玻璃码
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            HashMap<String, Object> params = new HashMap<>();
                                            params.put("vbCode", dd.getSn());

                                            HashMap<String, String> headers = new HashMap<>();
                                            String json2 = "";
                                            try {
                                                json2 = objectMapper.writeValueAsString(params);
                                            } catch (JsonProcessingException e) {

                                            }
                                            System.out.println(json2);
                                            //发送请求到RFID 08-02注释RFID
                                            VbCodeResult dl2 = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDWarmCode"), null, headers, json2, false), VbCodeResult.class);
                                            if (null != dl2) {
                                                if (null != dl2.getCode() && dl2.getCode() == 1000) {//请求成功,暂存风险批数据
                                                    if (null != dl2.getData() && null != dl2.getData().getVbCodeList() &&
                                                            !dl2.getData().getVbCodeList().isEmpty()) {
                                                        VbCode vbCode = new VbCode();
                                                        vbCode.setNgCount(1);
                                                        vbCode.setNowCount(1);
                                                        vbCode.setVbCodeList(dl2.getData().getVbCodeList());
                                                        redisTemplate.opsForValue().set("IpqcSizeWarmCode:" + dd.getMachineCode(), gson.toJson(vbCode));
                                                    }
                                                }
                                            }


                                        }
                                    }
                                    DynamicIpqcUtil.clearSizeData(redisTemplate, dd.getMachineCode(), gson);//清空缓存测试项数据
                                } else {
                                    //todo
                                    dim.setSizeOkCount(dim.getSizeOkCount() + 1);
                                    if (dim.getSizeOkCount() >= 32) {
                                        if (DynamicIpqcUtil.getCpk(datas, redisTemplate, checkType, dd.getId(), dim, gson) < 1) {//判断cpk
                                            dim.setNowCount(1);
//                                        dim.setTotalCount(1);
                                            dim.setSpecifiedCount(1);
                                            dim.setSenMesCount(1);
                                            dim.setRuleName("制程能⼒达标且稳定");
                                            if (dim.getFrequency() == IpqcTimeStandardRelax1) {
                                                DynamicIpqcUtil.sendMes(IpqcTimeStandardRelax2, "尺寸", "制程能⼒达标且稳定", "放宽", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                                dim.setFrequency(IpqcTimeStandardRelax2);
                                            } else {
                                                DynamicIpqcUtil.sendMes(IpqcTimeStandardRelax1, "尺寸", "制程能⼒达标且稳定", "放宽", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                                dim.setFrequency(IpqcTimeStandardRelax1);
                                            }
                                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                            dim.setSizeOkCount(0);
                                        }


                                    } else {
                                        if (DynamicIpqcUtil.getCpk(datas, redisTemplate, checkType, dd.getId(), dim, gson) > 0) {//判断cpk

                                            dim.setSizeOkCount(0);
                                        }
                                        dim.setSizeOkCount(dim.getSizeOkCount() + 1);
                                        //判断是否放宽
                                        dim.setNowCount(dim.getNowCount() + 1);


                                        if (dim.getRuleName().equals("⻛险批次全检")) {

                                            if (redisTemplate.hasKey("IpqcSizeWarmCode:" + datas.getMachineCode())) {
                                                Object vb = valueOperations.get("IpqcSizeWarmCode:" + datas.getMachineCode());
                                                if (null != vb) {
                                                    VbCode vbCode = new Gson().fromJson(vb.toString(), VbCode.class);
                                                    //总数量累加
                                                    vbCode.setNowCount(vbCode.getNowCount() + 1);
                                                    if (vbCode.getNowCount() >= Integer.parseInt(env.getProperty("RiskLotCount", "21")) && vbCode.getNgCount() < 3) {
                                                        DynamicIpqcUtil.sendMes(IpqcTimeStandardNormal, "尺寸", "恢复为QCP抽检频率", "放宽", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                                        dim.setFrequency(IpqcTimeStandardNormal);
                                                        dim.setNowCount(1);
                                                        dim.setSpecifiedCount(1);
                                                        dim.setSenMesCount(1);
                                                        dim.setRuleName("QCP抽检频率");
                                                        dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                                        //清空缓存
                                                        redisTemplate.delete("IpqcSizeWarmCode:" + dd.getMachineCode());
                                                    } else if (vbCode.getNowCount() >= Integer.parseInt(env.getProperty("RiskLotCount", "21")) && vbCode.getNgCount() > 2) {
                                                        DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "突发性⼤量不良", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                                        dim.setSenMesCount(1);
                                                        dim.setNowCount(0);
                                                        dim.setFrequency(IpqcTimeStandardTighten);
                                                        dim.setSpecifiedCount(2);
                                                        dim.setRuleName("突发性⼤量不良");
                                                        dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                                        //清空缓存
                                                        redisTemplate.delete("IpqcSizeWarmCode:" + dd.getMachineCode());
                                                    } else {
                                                        redisTemplate.opsForValue().set("IpqcSizeWarmCode:" + dd.getMachineCode(), gson.toJson(vbCode));

                                                    }


                                                }
                                            }


                                        } else if (dim.getNowCount() >= dim.getSpecifiedCount() && dim.getFrequency() < 2) {
                                            DynamicIpqcUtil.sendMes(IpqcTimeStandardNormal, "尺寸", "恢复为QCP抽检频率", "放宽", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                            dim.setFrequency(IpqcTimeStandardNormal);
                                            dim.setNowCount(1);
//                                        dim.setTotalCount(1);
                                            dim.setSpecifiedCount(1);
                                            dim.setSenMesCount(1);
                                            dim.setRuleName("QCP抽检频率");
                                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                        }


                                    }


                                }
                                dim.setUpdateTime(new Date().getTime());
                                dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());

                                redisTemplate.opsForValue().set("IpqcSize:" + dd.getMachineCode(), gson.toJson(dim));

                            }
                        }
                    } else {
                        DynamicIpqcMac dim = new DynamicIpqcMac();
                        dim.setMachineCode(dd.getMachineCode());
                        if (dd.getInfoResult().equals("NG")) {
                            dim.setNgCount(1);
                        }

                        dim.setTotalCount(1);
                        if (dd.getInfoResult().equals("NG")) {
                            //判断ng率是否超过
                            if (dim.getNgCount() / dim.getTotalCount() * 100 >= Integer.parseInt(env.getProperty("IpqcMqcYield", "10"))) {
                                //发通知
                                DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "连续性机台异常", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                dim.setFrequency(IpqcTimeStandardTighten);
                                dim.setNowCount(0);
//                                dim.setTotalCount(2);
                                dim.setSpecifiedCount(2);
                                dim.setRuleName("连续性机台异常");

                            } else {
                                DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten, "尺寸", "⻛险批次全检", "收严", dd.getProcess(), dd.getMachineCode(), dd.getId(), dd.getProject(), "动态IPQC", "");
                                dim.setSenMesCount(1);
                                dim.setNowCount(0);
//                                dim.setTotalCount(1);
                                dim.setSpecifiedCount(1);
                                dim.setFrequency(IpqcTimeStandardTighten);
                                dim.setRuleName("⻛险批次全检");

                            }
                            dim.setSenMesCount(1);
                            dim.setSizeOkCount(0);
                            dim.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                        } else {
                            //todo
//                                    dim.setAppearanceOkCount(datas.getSpotCheckCount());
                            dim.setNowCount(1);
//                            dim.setTotalCount(1);
                            dim.setSpecifiedCount(1);
                            dim.setSenMesCount(1);
                            dim.setFrequency(IpqcTimeStandardNormal);
                            dim.setRuleName("QCP抽检频率");
                            if (DynamicIpqcUtil.getCpk(datas, redisTemplate, checkType, dd.getId(), dim, gson) < 1) {//判断cpk
                                dim.setSizeOkCount(1);
                            } else {
                                dim.setSizeOkCount(0);
                            }

                        }
                        dim.setCpkCount(0);
                        dim.setFourPointOverOne(0);
                        dim.setTwoPointOverTwo(0);
                        dim.setZugammenCount(0);
                        dim.setSpcOkCount(0);

                        dim.setUpdateTime(new Date().getTime());
                        dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                        redisTemplate.opsForValue().set("IpqcSize:" + dd.getMachineCode(), gson.toJson(dim));

                    }


                }


                //更新ng阶段
//                if(allStatus==3&&ngPhase>0&&ngPhase<4){
                if(allStatus==3&&ngPhase>0){
                    ngPhase=ngPhase+1;
                    phaseData.setNgPhase(ngPhase+"");
                    //保存ng阶段到Redis
                    valueOperations.set("sizePhase:"+datas.getMachineCode()+"-"+bearing,phaseData);


                    //保存节点信息
                    saveNode(ngPhase,"NG",phaseData.getAuditId());
//                    if(ngPhase==4){
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("Type_Data", 32);
//                        data.put("MachineCode", datas.getMachineCode());
//                        data.put("nType_CMD_Src", 4);
//                        data.put("nIndex_CH", 0);
////                    if (status.equals("锁机")) {
////                        data.put("MsgLevel", 444446);
////                    } else {
//                        data.put("MsgLevel", 544446);
////                    }
//                        data.put("MsgTitle", "");
//                        data.put("MsgTxt", "");
//                        data.put("nResult", 0);
//                        data.put("CmdCRCKey", "");
//                        data.put("pub_time", System.currentTimeMillis() / 1000);
//
//                        String logData = JSON.toJSONString(data);
//                        DfScadaLockMacData dfScadaLockMacData = new DfScadaLockMacData();
//                        dfScadaLockMacData.setMachineCode(datas.getMachineCode());
//                        dfScadaLockMacData.setMacStatus("解锁");
//                        dfScadaLockMacData.setLogData(logData);
//                        dfScadaLockMacDataService.save(dfScadaLockMacData);
//
//                        try {
//                            jmsMessagingTemplate.convertAndSend(topic, mapper.writeValueAsString(data));
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
                //清空ng阶段
                if(allStatus!=3&&ngPhase>0){
                    ngPhase=ngPhase+1;
                    redisTemplate.delete("sizePhase:" + datas.getMachineCode()+"-"+bearing);

                    Map<String, Object> data = new HashMap<>();
                    data.put("Type_Data", 32);
                    data.put("MachineCode", datas.getMachineCode());
                    data.put("nType_CMD_Src", 4);
                    data.put("nIndex_CH", 0);
//                    if (status.equals("锁机")) {
//                        data.put("MsgLevel", 444446);
//                    } else {
                        data.put("MsgLevel", 544446);
//                    }
                    data.put("MsgTitle", "");
                    data.put("MsgTxt", "");
                    data.put("nResult", 0);
                    data.put("CmdCRCKey", "");
                    data.put("pub_time", System.currentTimeMillis() / 1000);

                    String logData = JSON.toJSONString(data);
                    DfScadaLockMacData dfScadaLockMacData = new DfScadaLockMacData();
                    dfScadaLockMacData.setMachineCode(datas.getMachineCode());
                    dfScadaLockMacData.setMacStatus("解锁");
                    dfScadaLockMacData.setLogData(logData);
                    dfScadaLockMacDataService.save(dfScadaLockMacData);
                    //保存节点信息
                    saveNode(ngPhase,"OK",phaseData.getAuditId());
                    try {
                        jmsMessagingTemplate.convertAndSend(topic, mapper.writeValueAsString(data));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    if(ngPhase<=3){
                        //自动回复Faca
                        updateAuditFACA(ngPhase,phaseData.getAuditId());
                    }


                }
                //推送结果给RFID
//                if (null != dd.getErrCode() && !dd.getErrCode().equals("1")&&null != dd.getSn() && !dd.getSn().equals("") && dd.getSn().length() > 15) {
                if (null != dd.getSn() && !dd.getSn().equals("") && dd.getSn().length() > 15) {
                    RFIDBadGlassReportData rfidData = new RFIDBadGlassReportData();
                    RFIDBadGlassReport rd = new RFIDBadGlassReport();
                    rd.setGlassCode(dd.getSn());

                    if (dd.getResult().equals("OK")) {
                        rd.setGlassState("03");
                    } else {
                        rd.setGlassState("01");
                    }

                    List<RFIDBadGlassReport> dl = new ArrayList<>();
                    dl.add(rd);
                    rfidData.setType("尺寸");
                    rfidData.setCodeList(dl);
                    rfidData.setProcedureName(dd.getProcess().equals("单面磨") ? "单面磨底" : dd.getProcess());
                    rfidData.setProductCode(dd.getProject());
                    rfidData.setDataId(interceptAuditId);//把尺寸id传导mq
                    try {
                        jmsMessagingTemplate.convertAndSend(env.getProperty("RFIDBadGlassReportTopic"), mapper.writeValueAsString(rfidData));
                        logger.info("正常发送RFID尺寸玻璃码:" + dd.getSn());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
//        } else {
//            System.out.println("机台与工序不一致:" + datas.getProcessNO() + "  " + datas.getMachineCode());
//        }


        return "";
    }

    //保存节点信息方法
    public void saveNode(int ngPhase,String result,int auditId){
        DfAuditDetailNode node=new DfAuditDetailNode();
        node.setResult(result);
        node.setParentId(auditId);
        if(ngPhase==0){
            node.setProcessNode("第一片");
        }else if(ngPhase==2||ngPhase==1){
            node.setProcessNode("复测");
        }
//        else if(ngPhase==3){
//            node.setProcessNode("临近一片");
//        }else if(ngPhase==4){
//            node.setProcessNode("机台内最后一片");
//        }
        else if(ngPhase==3){
            node.setProcessNode("机台内最后一片");
        }
        else{
            node.setProcessNode("首检");
        }
        dfAuditDetailNodeService.save(node);
    }

    //自动回复FACA方法
    public void updateAuditFACA(int ngPhase,int auditId){
        UpdateWrapper<DfAuditDetail>uw=new UpdateWrapper<>();
        if(ngPhase==1){
            uw.set("fa","测量问题导致NG");
            uw.set("ca","复测OK");
        }else if(ngPhase==2||ngPhase==3){
            uw.set("fa","NG为偶发性不良,属于正常范围");
            uw.set("ca","风险料架全检");
        }
        uw.set("end_time",TimeUtil.getNowTimeByNormal());
        uw.eq("id",auditId);

        dfAuditDetailService.update(uw);
        UpdateWrapper<DfFlowData>fuw=new UpdateWrapper<>();
        fuw.set("status","已关闭");
        fuw.set("submit_time",TimeUtil.getNowTimeByNormal());
        fuw.set("submit_name","系统");
        fuw.eq("data_id",auditId);
        dfFlowDataService.update(fuw);
        //生成节点信息
        QueryWrapper<DfFlowData>fqw=new QueryWrapper<>();
        fqw.eq("data_id",auditId);
        fqw.last("limit 1");
        DfFlowData flowData=dfFlowDataService.getOne(fqw);
        if(null!=flowData){
            DfFlowOpinion op=new DfFlowOpinion();
            op.setFlowDataId(flowData.getId());
            op.setOpinion("已解决问题");
            op.setSender("系统");
            dfFlowOpinionService.save(op);
        }

    }


    @Override
    public int importExcel2(MultipartFile file) throws Exception {
        return 0;
    }

    /*@Override
    @Transactional
    public int importExcel2(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        int testEndCul = 20;
        String[] title = new String[500];
        String[][] table = new String[500][100];  // 存储测量项 和上下限
        for (int i = 0; i < 100; i++) {
            title[i] = i + "";
        }
        List<Map<String, String>> maps = excel.readExcelContentDIY(0, title);

        int j = 0;
        for (Map<String, String> map : maps) {
            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                table[j][i++] = entry.getValue();
            }

            j++;
            if (j >= 500) {
                break;
            }
        }

        Timestamp todayTime = Timestamp.valueOf(table[10][0]);
        //LocalDateTime.parse(maps.get(0).get("时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String month = "单月";
        String today;
        String dayOrNight = "夜班";
        if (todayTime != null) {
            if (todayTime.getMonth() % 2 != 0) {
                month = "双月";
            }
            if (todayTime.toLocalDateTime().toLocalTime().compareTo(H7) >= 0 && todayTime.toLocalDateTime().toLocalTime().compareTo(H19) < 0) {  // 在7点到19点
                dayOrNight = "白班";
            }
            today = todayTime.toLocalDateTime().toLocalDate().toString();
        } else {
            return -1;
        }
        String factory = "J6-1";
        String project = "C27";
        String process = "CNC3";
        String linebody = "Line1";

        String secondDay = TimeUtil.getNextDay(today);

        // 设备和小组的关系
        Map<String, Integer> machineResGroup = dfGroupService.getMachineCode(month, dayOrNight, process);

        // 小组对应机台状态表
        Map<Integer, Integer> groupResMacStatNumRow = new HashMap<>();
        // 机台状态表
        int[][] macStatNumRow = new int[500][3];

        // 获得首检开机的机台
        Set<String> faiOpenMachine = new HashSet<>();
        // 获取首检通过的机台
        Set<String> faiPassMachine = new HashSet<>();

        // 获取NG的机台
        List<String> ngMac = new ArrayList<>();

        // 小组下标
        int groupIndex = 0;

        // 获取机台复测数据
        List<DfMacRetest> retestsList = new ArrayList<>();
        // 获取机台NG的时间
        Map<String, Timestamp> macResNgTime = new HashMap<>();



        for (int i = 10; i < 500; i++) {
            List<DfSizeFail> sizeFailList = new ArrayList<>();
            if (table[i][0] == null) {
                break;
            }
            List<DfSizeCheckItemInfos> itemList = new ArrayList<>();
            String result = "OK";
            boolean isIsland = false;
            boolean isAdjust = false;
            String machineStatus = null;
            for (int k = 2; k < testEndCul; k++) {

                System.out.println(k);
                DfSizeCheckItemInfos item = new DfSizeCheckItemInfos();
                Double usl = Double.valueOf(table[6][k]);
                Double conUpper = Double.valueOf(table[2][k]);
                Double standardValue = Double.valueOf(table[1][k]);
                Double conLower = Double.valueOf(table[3][k]);
                Double lsl = Double.valueOf(table[7][k]);
                System.out.println(table[i][k]);
                if (null == table[i][k]) {
                    table[i][k]=0+"";
                }
                Double value = Double.valueOf(table[i][k]);
                String itemName = table[0][k];
                item.setUsl(usl);
                item.setControlUpperLimit(conUpper);
                item.setStandardValue(standardValue);
                item.setControlLowerLimit(conLower);
                item.setLsl(lsl);
                item.setCheckValue(value);
                item.setCheckTime(table[i][0]);
                item.setCreateTime(null == table[i][0] ? null : Timestamp.valueOf(table[i][0]));
                item.setItemName(itemName);
                if (value >= conLower && value <= conUpper) {
                    item.setCheckResult("OK");
                } else if (value < conLower && value >= lsl ||
                        value > conUpper && value <= usl) {
                    item.setCheckResult("OK");
                    isAdjust = true;
                } else {
                    item.setCheckResult("NG");
                    isIsland = true;
                    result = "NG";
                    DfSizeFail dfSizeFail = new DfSizeFail();
                    dfSizeFail.setBadType(itemName);
                    dfSizeFail.setStandard(standardValue + "±" + table[4][k]);
                    dfSizeFail.setPractical(value);
                    if (value > usl) {
                        dfSizeFail.setBadCondition(itemName + "偏大");
                        dfSizeFail.setDiffValue(value - usl);
                    } else {
                        dfSizeFail.setBadCondition(itemName + "偏小");
                        dfSizeFail.setDiffValue(lsl - value);
                    }
                    dfSizeFail.setCreateTime(null == table[i][0] ? null : Timestamp.valueOf(table[i][0]));
                    sizeFailList.add(dfSizeFail);
                }
                itemList.add(item);
                System.out.println(item);
            }

            DfSizeDetail sizeDetail = new DfSizeDetail();

            if (isIsland) machineStatus = "隔离";
            else if (isAdjust) machineStatus = "调机";
            else machineStatus = "正常";
            sizeDetail.setCreateTime(null == table[i][0] ? null : Timestamp.valueOf(table[i][0]));
            sizeDetail.setMachineCode(getMachineCode(table[i][1]));
            sizeDetail.setMachineStatus(machineStatus);
            sizeDetail.setStatus(table[i][testEndCul+1]);
            sizeDetail.setResult(result);
            sizeDetail.setFactory(factory);
            sizeDetail.setProject(project);
            sizeDetail.setProcess(process);
            sizeDetail.setLinebody(linebody);
            sizeDetail.setDayOrNight(dayOrNight);
            sizeDetail.setTestTime(null == table[i][0] ? null : Timestamp.valueOf(table[i][0]));
            System.out.println(sizeDetail);

            save(sizeDetail);
            for (DfSizeCheckItemInfos itemInfos : itemList) {
                itemInfos.setCheckId(sizeDetail.getId().toString());
            }
            dfSizeCheckItemInfosService.saveBatch(itemList);
            for (DfSizeFail dfSizeFail : sizeFailList) {
                dfSizeFail.setParentId(sizeDetail.getId());
            }
            dfSizeFailService.saveBatch(sizeFailList);

            // 生成机台超时数据
            if (!machineMap.containsKey(sizeDetail.getMachineCode())) {
                machineMap.put(sizeDetail.getMachineCode(), machineIndex++);
            }
            if (null != sizeDetail.getTestTime()) {
                updateTable(sizeDetail.getMachineCode(), sizeDetail.getTestTime().toLocalDateTime().toLocalTime());
            }

            // 统计小组的调机能力
            Integer group = machineResGroup.get(sizeDetail.getMachineCode());
            group = null == group ? 0 : group;
            if (!groupResMacStatNumRow.containsKey(group)) {
                groupResMacStatNumRow.put(group, groupIndex++);
            }


            // 存储首检机台
            if ("FAI".equals(sizeDetail.getStatus())) {
                faiOpenMachine.add(sizeDetail.getMachineCode());
                if ("正常".equals(machineStatus) || "调机".equals(machineStatus)) {
                    faiPassMachine.add(sizeDetail.getMachineCode());
                }
            }

            // 存储首检机台
            if ("FAI".equals(sizeDetail.getStatus())) {
                faiOpenMachine.add(sizeDetail.getMachineCode());
                if ("正常".equals(machineStatus) || "调机".equals(machineStatus)) {
                    faiPassMachine.add(sizeDetail.getMachineCode());
                }
            }


            switch (sizeDetail.getMachineStatus()) {
                case "隔离": macStatNumRow[groupResMacStatNumRow.get(group)][QUARANTINE]++; break;
                case "调机": macStatNumRow[groupResMacStatNumRow.get(group)][ADJUSTMENT]++; break;
                case "正常": macStatNumRow[groupResMacStatNumRow.get(group)][NORMAL]++; break;
            }

            if (ngMac.contains(sizeDetail.getMachineCode())) {
                // 获取响应时间
                Timestamp lastNgTime = macResNgTime.get(sizeDetail.getMachineCode());
                Timestamp thisTestTime = sizeDetail.getTestTime();
                Long mill = thisTestTime.getTime() - lastNgTime.getTime();
                Double responseTime = mill.doubleValue() / 1000 / 3600;
                // 包含在NG里面 就要加到表里面
                DfMacRetest macRetest = new DfMacRetest();
                macRetest.setMachineCode(sizeDetail.getMachineCode());
                macRetest.setRetestResult(sizeDetail.getResult());
                macRetest.setCreateTime(sizeDetail.getTestTime());
                macRetest.setResponseTime(responseTime);
                macRetest.setFactory(factory);
                macRetest.setProcess(process);
                macRetest.setProject(project);
                macRetest.setLinebody(linebody);
                macRetest.setDayOrNight(dayOrNight);
                retestsList.add(macRetest);
                if ("OK".equals(macRetest.getRetestResult())) {
                    ngMac.remove(macRetest.getMachineCode());
                    macResNgTime.remove(macRetest.getMachineCode());
                } else {
                    macResNgTime.put(macRetest.getMachineCode(), sizeDetail.getTestTime());
                }
            }
            // 获取NG机台
            if ("NG".equals(sizeDetail.getResult()) && !ngMac.contains(sizeDetail.getMachineCode())) {
                ngMac.add(sizeDetail.getMachineCode());
                macResNgTime.put(sizeDetail.getMachineCode(), sizeDetail.getTestTime());
            }


            System.out.println();
        }

        for (DfMacRetest macRetest : retestsList) {
            System.out.println("获取复测通过率");
            System.out.println(macRetest);
        }


        // 小组有多少台机器开机
        Map<Integer, Integer> groupResMachineNum = new HashMap<>();
        for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
            String machine = entry.getKey();
            Integer group = machineResGroup.get(machine);
            groupResMachineNum.merge(group, 1, Integer::sum);
        }
        // 获取开机台数
        int allOpenNum = 0;
        for (Map.Entry<Integer, Integer> entry : groupResMachineNum.entrySet()) {
            if (null == entry.getKey()) continue;
            System.out.println("小组id为" + entry.getKey() + ":" + entry.getValue());
            allOpenNum += entry.getValue();
        }

        // 小组超时率
        List<DfGroupMacOvertime> groupMacOvertimeList = new ArrayList<>();
        if (dayOrNight.equals("白班")) {
            for (int i = 0; i < 6; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(today + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
                    data.setGroupId(groupId);
                    data.setAllMacNum(allMacNum);
                    data.setOvertimeMacNum(overtimeMacNum);
                    data.setOvertimeRate(overtimeRate);
                    data.setTestTime(Timestamp.valueOf(testTime));
                    data.setIntheTime(OVERTIMEARRAY[i]);
                    data.setDayOrNight(dayOrNight);
                    data.setFactory(factory);
                    data.setProcess(process);
                    data.setProject(project);
                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
        } else {
            for (int i = 6; i < 8; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(today + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
                    data.setGroupId(groupId);
                    data.setAllMacNum(allMacNum);
                    data.setOvertimeMacNum(overtimeMacNum);
                    data.setOvertimeRate(overtimeRate);
                    data.setTestTime(Timestamp.valueOf(testTime));
                    data.setIntheTime(OVERTIMEARRAY[i]);
                    data.setDayOrNight(dayOrNight);
                    data.setFactory(factory);
                    data.setProcess(process);
                    data.setProject(project);
                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
            for (int i = 8; i < 12; i++) {
                // 记录这个时间段小组的不超时机台
                Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
                for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                    //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                    String machine = entry.getKey();
                    Integer group = machineResGroup.get(machine);
                    groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
                }
                for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                    Integer groupId = entry.getKey();
                    if (null == groupId) continue;
                    Integer allMacNum = groupResMachineNum.get(groupId);
                    Integer overtimeMacNum = allMacNum - entry.getValue();
                    Double overtimeRate = overtimeMacNum.doubleValue() / allMacNum.doubleValue();
                    LocalDateTime testTime = LocalDateTime.parse(secondDay + OVERTIMETESTTIME[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    DfGroupMacOvertime data = new DfGroupMacOvertime();
                    data.setGroupId(groupId);
                    data.setAllMacNum(allMacNum);
                    data.setOvertimeMacNum(overtimeMacNum);
                    data.setOvertimeRate(overtimeRate);
                    data.setTestTime(Timestamp.valueOf(testTime));
                    data.setIntheTime(OVERTIMEARRAY[i]);
                    data.setDayOrNight(dayOrNight);
                    data.setFactory(factory);
                    data.setProcess(process);
                    data.setProject(project);
                    data.setLinebody(linebody);
                    System.out.println(data);
                    groupMacOvertimeList.add(data);
                }
            }
        }
        for (DfGroupMacOvertime overtime : groupMacOvertimeList) {
            System.out.println("超时输出");
            System.out.println(overtime);
        }
        for (int i = 0; i < 12; i++) {
            // 记录这个时间段小组的不超时机台
            Map<Integer, Integer> groupResNoOverTimeNum = new HashMap<>();
            for (Map.Entry<String, Integer> entry : machineMap.entrySet()) {
                //groupResOverTimeNum.put()machineOvertimeTable[entry.getValue()][i];
                String machine = entry.getKey();
                Integer group = machineResGroup.get(machine);
                groupResNoOverTimeNum.merge(group, machineOvertimeTable[entry.getValue()][i], Integer::sum);
            }
            for (Map.Entry<Integer, Integer> entry : groupResNoOverTimeNum.entrySet()) {
                System.out.println("小组id为" + entry.getKey() + ":" + "不超时为:" + entry.getValue());
            }
        }

        // 获取所有小组的机台总数
        Map<Integer, Integer> groupMacNum = dfGroupService.getGroupMacNum(month, dayOrNight);
        // 获取首检开机数
        int faiOpenNum = faiOpenMachine.size();
        // 获取首检通过数
        int faiPassNum = faiPassMachine.size();
        // 获取所有机台总数
        int macNum = 0;
        for (Map.Entry<Integer, Integer> entry : groupMacNum.entrySet()) { macNum += entry.getValue(); }
        DfFaiPassRate faiPassRate = new DfFaiPassRate();
        faiPassRate.setFactory(factory);
        faiPassRate.setProcess(process);
        faiPassRate.setProject(project);
        faiPassRate.setLinebody(linebody);
        faiPassRate.setDayOrNight(dayOrNight);
        faiPassRate.setAllMacNum(macNum);
        faiPassRate.setOpenMacNum(allOpenNum);
        faiPassRate.setFaiOpenNum(faiOpenNum);
        faiPassRate.setFaiPassNum(faiPassNum);
        faiPassRate.setCreateTime(todayTime);
        System.out.println("******************************");
        System.out.println(faiPassRate);
        System.out.println("******************************");

        // 小组调机能力
        List<DfGroupAdjustment> groupAdjustmentList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupResMacStatNumRow.entrySet()) {
            Integer group = entry.getKey();
            if (0 == group) continue;
            Integer index = entry.getValue();
            DfGroupAdjustment adjustment = new DfGroupAdjustment();
            adjustment.setGroupId(group);
            adjustment.setAllMacNum(groupMacNum.get(group));
            adjustment.setQuarantineNum(macStatNumRow[index][QUARANTINE]);
            adjustment.setAdjustmentNum(macStatNumRow[index][ADJUSTMENT]);
            adjustment.setNormalNum(macStatNumRow[index][NORMAL]);
            adjustment.setUnusedNum(groupMacNum.get(group) - groupResMachineNum.get(group));
            adjustment.setAllTestNum(adjustment.getQuarantineNum() + adjustment.getAdjustmentNum() + adjustment.getNormalNum() + adjustment.getUnusedNum());
            adjustment.setQuarantineRate(adjustment.getQuarantineNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setAdjustmentRate(adjustment.getAdjustmentNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setNormalRate(adjustment.getNormalNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setUnusedRate(adjustment.getUnusedNum().doubleValue() / adjustment.getAllTestNum().doubleValue());
            adjustment.setDayOrNight(dayOrNight);
            adjustment.setFactory(factory);
            adjustment.setProcess(process);
            adjustment.setProject(project);
            adjustment.setLinebody(linebody);
            if ("白班" == dayOrNight){
                adjustment.setCreateTime(Timestamp.valueOf(today + " 18:59:58"));
            } else {
                adjustment.setCreateTime(Timestamp.valueOf(secondDay + " 18:59:58"));
            }
            System.out.println("调机能力输出");
            System.out.println(adjustment);
            groupAdjustmentList.add(adjustment);
        }

        for (int i = 0; i < machineIndex; i++) {
            for (int k = 0; k < machineOvertimeTable[0].length; k++) {
                System.out.print(machineOvertimeTable[i][k]);
                System.out.print(" ");
            }
            System.out.println();
        }

        dfMacRetestService.saveBatch(retestsList);
        dfGroupAdjustmentService.saveBatch(groupAdjustmentList);
        dfGroupMacOvertimeService.saveBatch(groupMacOvertimeList);
        dfFaiPassRateService.save(faiPassRate);


        return maps.size();

    }*/

    @Override
    public List<DfSizeDetail> listMachineCode(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listMachineCode(wrapper);
    }

    @Override
    public List<DfSizeDetail> getFaiPassRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.getFaiPassRate(wrapper);
    }

    @Override
    public List<Rate> listDateOkRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listDateOkRate(wrapper);
    }

    @Override
    public List<Rate> listDateOkRate2(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listDateOkRate2(wrapper);
    }

    @Override
    public Rate getSizeRealRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.getSizeRealRate(wrapper);
    }

    @Override
    public List<Rate> listAlwaysOkRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listAlwaysOkRate(wrapper);
    }

    @Override
    public List<Rate> listAlwaysOkRate2(Wrapper<DfSizeDetail> wrapper, String project) {
        return dfSizeDetailMapper.listAlwaysOkRate2(wrapper, project);
    }

    @Override
    public List<Rate> listProcessNgRateByNgItem(String ngItem, String startTime, String endTime, Integer
            startHour, Integer endHour) {
        return dfSizeDetailMapper.listProcessNgRateByNgItem(ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listProcessNgRateByNgItem2(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listProcessNgRateByNgItem2(wrapper);
    }

    @Override
    public List<Rate> listMacNgRateByNgItemAndProcess(String process, String project, String color, String
            ngItem, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfSizeDetailMapper.listMacNgRateByNgItemAndProcess(process, project, color, ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listAllProcessOkRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listAllProcessOkRate(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOkRate2(Wrapper<DfSizeDetail> wrapper,String project) {
        return dfSizeDetailMapper.listAllProcessOkRate2(wrapper, project);
    }

    @Override
    public List<Rate> listDateNgRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listDateNgRate(wrapper);
    }

    @Override
    public List<Rate> listItemNgRate(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.listItemNgRate(wrapper);
    }

    @Override
    public List<Rate3> listOpenRate(Wrapper<DfSizeDetail> wrapper, String process) {
        return dfSizeDetailMapper.listOpenRate(wrapper, process);
    }

    @Override
    public Rate getAllProcessYield(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.getAllProcessYield(wrapper);
    }

    @Override
    public Rate getLineYield(Wrapper<DfSizeDetail> wrapper) {
        return dfSizeDetailMapper.getLineYield(wrapper);
    }

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        return 0;
    }

    @Override
    public List<DfSizeDetail> weekOfPoorTOP3Warning(QueryWrapper<DfSizeDetail> ew) {
        return dfSizeDetailMapper.weekOfPoorTOP3Warning(ew);
    }

    @Override
    public List<Rate3> twoWeekOfPoorTOP3Warning() {
        return dfSizeDetailMapper.twoWeekOfPoorTOP3Warning();
    }

    @Override
    public List<Map<String, Object>> getSizeDetailInfoList(QueryWrapper<Map<String, Object>> qw) {
        return dfSizeDetailMapper.getSizeDetailInfoList(qw);
    }

    @Override
    public List<Rate3> getMachineOkNumList(QueryWrapper<Rate3> qw) {
        return dfSizeDetailMapper.getMachineOkNumList(qw);
    }

    @Override
    public List<Map<String, Object>> getQmsWaigInfoList(QueryWrapper<Map<String, Object>> qw) {
        return dfSizeDetailMapper.getQmsWaigInfoList(qw);
    }

    @Override
    public List<Rate3> getQmsWaigMachineOkNumList(QueryWrapper<Rate3> qw) {
        return dfSizeDetailMapper.getQmsWaigMachineOkNumList(qw);
    }

    @Override
    public List<Map<String, Object>> exportInspectionTableForProcess(QueryWrapper<DfSizeDetail> sizeWrapper) {
        return dfSizeDetailMapper.exportInspectionTableForProcess(sizeWrapper);
    }

    @Override
    public List<Map<String, Object>> exportInspectionTableForProcessBymachineId(QueryWrapper<DfSizeDetail> sizeWrapper) {
        return dfSizeDetailMapper.exportInspectionTableForProcessBymachineId(sizeWrapper);
    }

    @Override
    public List<Map<String, Object>> exportNgClassificationScale(QueryWrapper<DfSizeDetail> sizeWrapper) {
        return dfSizeDetailMapper.exportNgClassificationScale(sizeWrapper);
    }

    public List<Map<String, String>> getResponse(HashMap<String, String> map){
        try {
            String urlString = FindVbCodeAPI; // 替换为实际URL
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法，默认是GET
            connection.setRequestMethod("POST");

            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json");

            // 允许向服务器发送数据
            connection.setDoOutput(true);

            // 创建要发送的 JSON 数据
            JSONObject jsonInput = new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                jsonInput.put(entry.getKey(), entry.getValue());
            }

            // 获取连接的输出流，并将 JSON 数据写入其中
            OutputStream os = connection.getOutputStream();
            os.write(jsonInput.toString().getBytes());
            os.flush();
            os.close();

            // 开始连接
            connection.connect();

            // 获取状态码
            int status = connection.getResponseCode();

            // 如果成功处理响应
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                // 读取服务器响应内容
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();

                // 假设响应内容存储在 content 变量中
                String jsonResponse = content.toString();

                // 将 JSON 响应内容解析为 JSONObject
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // 将 JSONObject 转换为 Map
                Map<String, Object> resultMap = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    resultMap.put(key, jsonObject.get(key));
                }
                // 拿到data的值(data的值是一个数组)
                ObjectMapper objectMapper = new ObjectMapper();
                if (resultMap.get("data") != null) {
                    String json = resultMap.get("data").toString();
                    System.out.println(json);
                    List<Map<String, String>> list = objectMapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});
                    return list;
                } else {
                    System.out.println("接口返回data字段无数据");
                    return null;
                }

            } else {
                System.out.println("GET request not worked");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新机台近50组测试数据的缓存
     */
    private void updateDataToLimit50Cache(String project,
                                           String process,
                                           String color,
                                           String itemName,
                                           String dCode,
                                           Timestamp checkTime,
                                           Double checkValue) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            checkTime = new Timestamp(now());
            // 使用 Calendar 获取时间戳的小时部分
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(checkTime.getTime());
            String time = sdf.format(checkTime.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);  // 获取小时（24小时制）
            String dayOrNight;
            // 判断是否在白班时间段（7:00 - 19:00）
            if (hour >= 7 && hour < 19) {
                dayOrNight = "A";
            } else {
                dayOrNight = "B";
            }
            // 获取标准
            DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
            Double lsl = standardData.getIsolaLowerLimit();
            Double usl = standardData.getIsolaUpperLimit();
            Double standardValue = standardData.getStandard();
            // 添加当班的数据
            String key = "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":" + dayOrNight;
            updateMacLimit50ByKey(key, dCode, lsl, usl, checkValue, standardValue, time);
            // 添加全班的数据
            String key2 = "size:limit50JoinMac:" + project + ":" + process + ":" + color + ":" + itemName + ":" + dCode + ":AB";
            updateMacLimit50ByKey(key2, dCode, lsl, usl, checkValue, standardValue, time);
        } catch (Exception e) {
            System.out.println("更新机台近50组测试数据报错，报错如下：");
            e.printStackTrace();
        }
    }

    private void updateMacLimit50ByKey(String key,
                                       String dCode,
                                       Double lsl,
                                       Double usl,
                                       Double checkValue,
                                       Double standardValue,
                                       String checkTime) {
        Set<String> keys = redisUtil.keys(key);
        Gson gson = new Gson();
        // 如果缓存有这个数据
        if (keys.size() > 0) {
            for (String s : keys) {
                String json = (String)redisUtil.get(s);
                Map<String, List<Object>> map = gson.fromJson(json, new TypeToken<Map<String, List<Object>>>(){}.getType());
                List<Object> uslList = map.get("usl");
                List<Object> lslList = map.get("lsl");
                List<Object> standardValueList = map.get("standardValue");
                List<Object> checkValueList = map.get("checkValue");
                List<Object> checkTimeList = map.get("checkTime");
                // 如果和上一条的数据重复，就更新时间
//                if ((Double)map.get("checkValue").get(map.get("checkValue").size() - 1) - checkValue == 0) {
//                    // 更新时间
//                    map.get("checkTime").set(map.get("checkTime").size() - 1, checkTime);
//                    break;
//                }
                checkValueList.add(checkValue);
                standardValueList.add(standardValue);
                uslList.add(usl);
                lslList.add(lsl);
                checkTimeList.add(checkTime);
                if (checkValueList.size() > 50) checkValueList.remove(0);
                if (standardValueList.size() > 50) standardValueList.remove(0);
                if (uslList.size() > 50) uslList.remove(0);
                if (lslList.size() > 50) lslList.remove(0);
                if (checkTimeList.size() > 50) checkTimeList.remove(0);
                boolean isOk = true;

                for (int i = 0; i < checkValueList.size(); i++) {
                    if ((Double)uslList.get(i) < (Double)checkValueList.get(i) || (Double)lslList.get(i) > (Double)checkValueList.get(i)) {
                        isOk = false;
                        break;
                    }
                }
                List<Object> checkResult = new ArrayList<>();
                List<Object> checkMas = new ArrayList<>();
                if (isOk) {
                    checkResult.add("OK");
                    checkMas.add("该机台处于正常波动范围内");
                } else {
                    checkResult.add("NG");
                    checkMas.add("该机台超出正常波动范围内");
                }
                map.put("checkResult", checkResult);
                map.put("checkMas", checkMas);

                String replaceJson = gson.toJson(map);
                // 替换最新的结果
                redisUtil.set(key, replaceJson);
                break;
            }

        } else {  // 缓存没有这个数据的话就加
            Map<String, List<Object>> valueMap  = new HashMap<>();
            List<Object> checkValueList = new ArrayList<>();
            List<Object> standardValueList = new ArrayList<>();
            List<Object> uslList = new ArrayList<>();
            List<Object> lslList = new ArrayList<>();
            List<Object> dCodeNameList = new ArrayList<>();
            List<Object> checkTimeList = new ArrayList<>();
            List<Object> checkResult = new ArrayList<>();
            List<Object> checkMas = new ArrayList<>();
            dCodeNameList.add(dCode);
            checkValueList.add(checkValue);
            standardValueList.add(standardValue);
            uslList.add(usl);
            lslList.add(lsl);
            checkTimeList.add(checkTime);
            if (usl < checkValue || lsl < checkValue) {
                checkResult.add("NG");
                checkMas.add("该机台超出正常波动范围内");
            } else {
                checkResult.add("OK");
                checkMas.add("该机台处于正常波动范围内");
            }
            valueMap.put("checkValue", checkValueList);
            valueMap.put("standardValue", standardValueList);
            valueMap.put("usl", uslList);
            valueMap.put("lsl", lslList);
            valueMap.put("name", dCodeNameList);
            valueMap.put("checkTime", checkTimeList);
            valueMap.put("checkResult", checkResult);
            valueMap.put("checkMas", checkMas);
            String replaceJson = gson.toJson(valueMap);
            // 添加数据
            redisUtil.set(key, replaceJson);
        }
    }

    /**
     * 更新尺寸测量项近50组测试数据的缓存
     */
    private void updateItemDataToLimit50Cache(String project,
                                              String process,
                                              String color,
                                              String itemName,
                                              Timestamp checkTime,
                                              Double checkValue,
                                              String checkType) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            checkTime = new Timestamp(now());
            // 使用 Calendar 获取时间戳的小时部分
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(checkTime.getTime());
            String time = sdf.format(checkTime.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);  // 获取小时（24小时制）
            String dayOrNight;
            // 判断是否在白班时间段（7:00 - 19:00）
            if (hour >= 7 && hour < 19) {
                dayOrNight = "A";
            } else {
                dayOrNight = "B";
            }
            // 添加当班的数据
            updateItemDataToLimit50CacheByDayOrNight(project, color, process, itemName, dayOrNight, checkValue, time, checkType);
            // 添加全班的数据
            updateItemDataToLimit50CacheByDayOrNight(project, color, process, itemName, "AB", checkValue, time, checkType);
        } catch (Exception e) {
            System.out.println("更新尺寸测量项近50组测试数据的缓存报错，报错如下：");
            e.printStackTrace();
        }
    }

    public void updateItemDataToLimit50CacheByDayOrNight(String project, String color, String process,
                                                         String itemName, String dayOrNight, Double checkValue,
                                                         String checkTime, String checkType) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // redis的键
        String key = "size:limit50:" + project + ":" + color + ":" + process + ":" + dayOrNight + ":";
        // 获取键的值，即各个不良项
        List<Map<String, List<Object>>> itemList = redisUtil.getListFromRedis(key);
        // 更新对应的不良项
        if (itemList == null || itemList.size() == 0) {
            itemList = new ArrayList<>();
        } else {
            for (Map<String, List<Object>> itemDetail : itemList) {
                List<Object> nameList = itemDetail.get("name");
                String name = nameList.get(0).toString();
                // 如果不良项名称没有对上就退出来，看下一个
                if (!name.equals(itemName)) continue;
                // 前一片和后一片一样的话就更新时间
//                if ((Double) itemDetail.get("checkValue").get(itemDetail.get("checkValue").size() - 1) - checkValue == 0) {
//                    // 更新时间 和 类型
//                    itemDetail.get("checkTime").set(itemDetail.get("checkTime").size() - 1, checkTime);
//                    itemDetail.get("checkType").set(itemDetail.get("checkType").size() - 1, checkType);
//                    redisUtil.delete(key);
//                    redisUtil.saveListToRedis(key, itemList);
//                    System.out.println("更新键：" + key);
//                    return;
//                }
                // 标准使用数据库的标准
                DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
                Object lslLimit = standardData.getIsolaLowerLimit();
                Object uslLimit = standardData.getIsolaUpperLimit();
                Object standardLimit = standardData.getStandard();
                // 更新这个不良项的内容
                itemDetail.get("checkValue").add(checkValue);
                itemDetail.get("standardValue").add(standardLimit);
                itemDetail.get("usl").add(uslLimit);
                itemDetail.get("lsl").add(lslLimit);
                itemDetail.get("checkTime").add(checkTime);
//                itemDetail.get("checkType").add(sdf.format(checkType));
                itemDetail.get("checkType").add(checkType);
                // 如果超过50组，就删除掉最老的一组
                if (itemDetail.get("checkValue").size() > 50) {
                    itemDetail.get("checkValue").remove(0);
                    itemDetail.get("standardValue").remove(0);
                    itemDetail.get("usl").remove(0);
                    itemDetail.get("lsl").remove(0);
                    itemDetail.get("checkTime").remove(0);
                    itemDetail.get("checkType").remove(0);
                }
                // 拿最新的标准作为所有的标准
                int len = itemDetail.get("lsl").size();
                // 标准使用数据库的标准
                List<Object> lslList = new ArrayList<>();
                List<Object> uslList = new ArrayList<>();
                List<Object> standardValueList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    lslList.add(lslLimit);
                    uslList.add(uslLimit);
                    standardValueList.add(standardLimit);
                }
                itemDetail.put("lsl", lslList);
                itemDetail.put("usl", uslList);
                itemDetail.put("standardValue", standardValueList);

                redisUtil.delete(key);
                redisUtil.saveListToRedis(key, itemList);
                System.out.println("更新键：" + key);
                return;
            }
        }

        // 如果缓存中没有这个不良项的数据，就加进去
        Map<String, List<Object>> valueMap  = new HashMap<>();
        List<Object> checkValueList = new ArrayList<>();
        List<Object> standardValueList = new ArrayList<>();
        List<Object> uslList = new ArrayList<>();
        List<Object> lslList = new ArrayList<>();
        List<Object> itemNameList = new ArrayList<>();
        List<Object> checkTimeList = new ArrayList<>();
        List<Object> checkTypeList = new ArrayList<>();
        // 标准使用数据库的标准
        DfSizeContStand standardData = InitializeCheckRule.sizeContStand.get(project + color + process + itemName);
        Object lslLimit = standardData.getIsolaLowerLimit();
        Object uslLimit = standardData.getIsolaUpperLimit();
        Object standardLimit = standardData.getStandard();
        itemNameList.add(itemName);
        checkValueList.add(checkValue);
        standardValueList.add(standardLimit);
        uslList.add(uslLimit);
        lslList.add(lslLimit);
        checkTimeList.add(checkTime);
        checkTypeList.add(checkType);
        valueMap.put("checkValue", checkValueList);
        valueMap.put("standardValue", standardValueList);
        valueMap.put("usl", uslList);
        valueMap.put("lsl", lslList);
        valueMap.put("name", itemNameList);
        valueMap.put("checkTime", checkTimeList);
        valueMap.put("checkType", checkTypeList);
        itemList.add(valueMap);

        redisUtil.delete(key);
        redisUtil.saveListToRedis(key, itemList);
        System.out.println("更新键：" + key);
    }



}
