package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupClose;
import com.ww.boengongye.entity.DfGroupMacNgRate;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfGroupMacNgRateMapper;
import com.ww.boengongye.service.DfGroupMacNgRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小组机台超时率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Service
public class DfGroupMacNgRateServiceImpl extends ServiceImpl<DfGroupMacNgRateMapper, DfGroupMacNgRate> implements DfGroupMacNgRateService {

    @Autowired
    private DfGroupMacNgRateMapper dfGroupMacNgRateMapper;

    @Autowired
    private DfGroupService dfGroupService;

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Override
    public void generateDataByDateTime(String dateTime) throws ParseException {
        System.out.println("===========更新小组机台NG率（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        //Timestamp now = new Timestamp((Timestamp.valueOf(TimeUtil.getNowTimeByNormal()).getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
        Timestamp now = Timestamp.valueOf(dateTime);
        //Timestamp now = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        String today = now.toString().substring(0, 10);
        String startTime;
        String endTime = now.toString();
        String dayOrNight = "夜班";
        if (now.getHours() < 7) {
            startTime = TimeUtil.getLastDay(today) + " 19:00:00";
        } else if (now.getHours() < 19) {
            startTime = today + " 07:00:00";
            dayOrNight = "白班";
        } else {
            startTime = today + " 19:00:00";
        }
        String month = "双月";
        Timestamp realDay = new Timestamp(now.getTime() - 1000 * 3600 * 7 - 1000 * 5); // 当前时间减去7小时，得到实际工作日
        if (realDay.getMonth() % 2 == 0) {
            month = "单月";
        }

        System.out.println("开始时间：" + startTime);
        System.out.println("结束时间：" + endTime);


        // 获取当前时间段的机台数据
        String littleStartTime;
        String littleEndTime = now.toString();

        String testTime;
        switch (now.getHours()) {

            case 6:
                littleStartTime = TimeUtil.getLastDay(today) + " 19:00:00";
                testTime = TimeUtil.getLastDay(today) + " 19:00:01";
                break;

            case 18:
                littleStartTime = today + " 07:00:00";
                testTime = today + " 07:00:01";
                break;

            default:
                littleStartTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                testTime = today + " 00:00:00";
        }

        QueryWrapper<DfSizeDetail> littleqw = new QueryWrapper<>();  //  -- 尺寸
        littleqw.between("create_time", littleStartTime, littleEndTime);
        littleqw.select("machine_code", "result");
        List<DfSizeDetail> littleMachineCode = dfSizeDetailService.list(littleqw);

        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取机台检测的总数和NG数
        Map<String, Integer> macResNgNum = new HashMap<>();
        Map<String, Integer> macResAllNum = new HashMap<>();
        for (DfSizeDetail dfSizeDetail : littleMachineCode) {
            String machineCode = dfSizeDetail.getMachineCode();
            macResAllNum.merge(machineCode, 1, Integer::sum);
            if ("NG".equals(dfSizeDetail.getResult())) {
                macResNgNum.merge(machineCode, 1, Integer::sum);
            }
        }

        // 获取机台不良率数据
        List<DfGroupMacNgRate> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : macResAllNum.entrySet()) {
            String machineCode = entry.getKey();
            Integer groupId = macResGroupId.get(machineCode);
            if (null == groupId) continue;
            DfGroupMacNgRate data = new DfGroupMacNgRate();
            Integer macAllNum = entry.getValue();  // 机台检测数
            Integer macNgNum = macResNgNum.get(machineCode) == null ? 0 : macResNgNum.get(machineCode);  // 机台NG数
            Double macNgRate = macNgNum.doubleValue() / macAllNum;
            data.setGroupId(groupId);
            data.setCheckNgNum(macNgNum);
            data.setCheckAllNum(macAllNum);
            data.setCheckNgRate(macNgRate);
            data.setTestTime(Timestamp.valueOf(testTime));
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸
            data.setMachineCode(machineCode);
            list.add(data);
        }
        saveBatch(list);

        System.out.println("===========更新小组机台NG率（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    @Override
    public List<Rate3> listGroupMacNgRateTop(Wrapper<DfGroupMacNgRate> wrapper) {
        return dfGroupMacNgRateMapper.listGroupMacNgRateTop(wrapper);
    }
}
