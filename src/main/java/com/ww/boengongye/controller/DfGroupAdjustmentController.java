package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfGroupAdjustment;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.service.DfGroupAdjustmentService;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.service.DfMacStatusSizeService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 小组调机能力 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
@Controller
@RequestMapping("/dfGroupAdjustment")
@CrossOrigin
@ResponseBody
@Api(tags = "小组调机能力")
public class DfGroupAdjustmentController {

    @Autowired
    private DfGroupAdjustmentService dfGroupAdjustmentService;

    @Autowired
    private DfGroupService dfGroupService;

    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;

    @GetMapping("/listBestRate")
    public Result listBestRate(String factory, String process, String project, String linebody,
                               String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:00";
        List<DfGroupAdjustment> dayData = dfGroupAdjustmentService.listBestRate(factory, process, project, linebody, "白班", startDate, endDate, testType);
        List<DfGroupAdjustment> nightData = dfGroupAdjustmentService.listBestRate(factory, process, project, linebody, "夜班", startDate, endDate, testType);


        Map<String, Object> result = new HashMap<>();
        result.put("白班", dayData);
        result.put("夜班", nightData);
        return new Result(200, "查询成功", result);

    }

    @GetMapping("/listBestRate2")
    public Result listBestRate2(String factory, String process, String project, String linebody,
                               String startDate, String endDate, Integer testType) throws ParseException {
        if (null != endDate) endDate = TimeUtil.getNextDay(endDate) + " 07:00:00";
        if (null != startDate) startDate = startDate + " 07:00:00";
        List<DfGroupAdjustment> dayData = dfGroupAdjustmentService.listBestRate(factory, process, project, linebody, "白班", startDate, endDate, testType);
        List<DfGroupAdjustment> nightData = dfGroupAdjustmentService.listBestRate(factory, process, project, linebody, "夜班", startDate, endDate, testType);

        // 找到小组实时机台状态个数
        Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
        } else if (now.getHours() < 19) {
            dayOrNight = "白班";
        } else {
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }
        Map<String, String> macResRespon = dfGroupService.getMacResRespon(month, dayOrNight);
        List<DfMacStatusSize> list = dfMacStatusSizeService.list();
        Map<String, int[]> responResNum = new HashMap<>();
        for (DfMacStatusSize dfMacStatusSize : list) {
            String machineCode = dfMacStatusSize.getMachineCode();
            String respon = macResRespon.get(machineCode);
            Integer status = dfMacStatusSize.getStatusidCur();
            if (!responResNum.containsKey(respon)) {
                int[] num = new int[4];
                Arrays.fill(num, 0);
                switch (status) {
                    case 3: num[0]++; break;
                    case 1: num[1]++; break;
                    case 2: num[2]++; break;
                    case 4: num[3]++; break;
                }
                responResNum.put(respon, num);
            } else {
                int[] num = responResNum.get(respon);
                switch (status) {
                    case 3: num[0]++; break;
                    case 1: num[1]++; break;
                    case 2: num[2]++; break;
                    case 4: num[3]++; break;
                }
            }
        }

        Map<String, List<Object>> dayMap = new HashMap<>();
        Map<String, List<Object>> nightMap = new HashMap<>();
        List<Object> dayQuarantineRate = new ArrayList<>();
        List<Object> dayAdjustmentRate = new ArrayList<>();
        List<Object> dayNormalRate = new ArrayList<>();
        List<Object> dayUnusedRate = new ArrayList<>();
        List<Object> dayQuarantineNum = new ArrayList<>();
        List<Object> dayAdjustmentNum = new ArrayList<>();
        List<Object> dayNormalNum = new ArrayList<>();
        List<Object> dayUnusedNum = new ArrayList<>();
        List<Object> dayRespon = new ArrayList<>();
        List<Object> nightQuarantineRate = new ArrayList<>();
        List<Object> nightAdjustmentRate = new ArrayList<>();
        List<Object> nightNormalRate = new ArrayList<>();
        List<Object> nightUnusedRate = new ArrayList<>();
        List<Object> nightQuarantineNum = new ArrayList<>();
        List<Object> nightAdjustmentNum = new ArrayList<>();
        List<Object> nightNormalNum = new ArrayList<>();
        List<Object> nightUnusedNum = new ArrayList<>();
        List<Object> nightRespon = new ArrayList<>();
        for (DfGroupAdjustment dayDatum : dayData) {
            String respon = dayDatum.getGroupRespon();
            int[] num = responResNum.get(respon);
            dayQuarantineRate.add(dayDatum.getQuarantineRate());
            dayAdjustmentRate.add(dayDatum.getAdjustmentRate());
            dayNormalRate.add(dayDatum.getNormalRate());
            dayUnusedRate.add(dayDatum.getUnusedRate());
            dayRespon.add(respon);
            dayQuarantineNum.add(null == num ? 0 : num[0]);
            dayAdjustmentNum.add(null == num ? 0 : num[1]);
            dayNormalNum.add(null == num ? 0 : num[2]);
            dayUnusedNum.add(null == num ? 0 : num[3]);
        }
        dayMap.put("quarantineRate", dayQuarantineRate);  // 隔离率
        dayMap.put("adjustmentRate", dayAdjustmentRate);  // 调机率
        dayMap.put("normalRate", dayNormalRate);  // 正常率
        dayMap.put("unusedRate", dayUnusedRate);  // 闲置率
        dayMap.put("quarantineNum", dayQuarantineNum);  // 隔离数
        dayMap.put("adjustmentNum", dayAdjustmentNum);  // 调机数
        dayMap.put("normalNum", dayNormalNum);  // 正常数
        dayMap.put("unusedNum", dayUnusedNum);  // 闲置率
        dayMap.put("respon", dayRespon);

        for (DfGroupAdjustment dayDatum : nightData) {
            String respon = dayDatum.getGroupRespon();
            int[] num = responResNum.get(respon);
            nightQuarantineRate.add(dayDatum.getQuarantineRate());
            nightAdjustmentRate.add(dayDatum.getAdjustmentRate());
            nightNormalRate.add(dayDatum.getNormalRate());
            nightUnusedRate.add(dayDatum.getUnusedRate());
            nightRespon.add(respon);
            nightQuarantineNum.add(null == num ? 0 : num[0]);
            nightAdjustmentNum.add(null == num ? 0 : num[1]);
            nightNormalNum.add(null == num ? 0 : num[2]);
            nightUnusedNum.add(null == num ? 0 : num[3]);
        }
        nightMap.put("quarantineRate", nightQuarantineRate);
        nightMap.put("adjustmentRate", nightAdjustmentRate);
        nightMap.put("normalRate", nightNormalRate);
        nightMap.put("unusedRate", nightUnusedRate);
        nightMap.put("quarantineNum", nightQuarantineNum);
        nightMap.put("adjustmentNum", nightAdjustmentNum);
        nightMap.put("normalNum", nightNormalNum);
        nightMap.put("unusedNum", nightUnusedNum);
        nightMap.put("respon", nightRespon);

        Map<String, Object> result = new HashMap<>();
        result.put("day", dayMap);
        result.put("night", nightMap);
        return new Result(200, "查询成功", result);

    }

}
