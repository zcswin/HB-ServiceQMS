package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupClose;
import com.ww.boengongye.entity.DfGroupOkRate;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfGroupCloseMapper;
import com.ww.boengongye.mapper.DfGroupOkRateMapper;
import com.ww.boengongye.service.DfGroupCloseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小组两小时时间段良率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
@Service
public class DfGroupCloseServiceImpl extends ServiceImpl<DfGroupCloseMapper, DfGroupClose> implements DfGroupCloseService {

    @Autowired
    private DfGroupCloseMapper dfGroupCloseMapper;

    @Autowired
    private DfGroupService dfGroupService;

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Override
    public void generateDataByDateTime(String dateTime) throws ParseException {
        System.out.println("===========更新小组关闭率（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
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

        QueryWrapper<DfGroupClose> closeQW = new QueryWrapper<>();
        closeQW.between("aud.create_time", littleStartTime, littleEndTime)
                .eq("aud.data_type", "尺寸");
        List<Rate3> rates = dfGroupCloseMapper.listMachineCloseNum(closeQW);

        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取小组当前时间段的关闭数和开启数
        Map<Integer, Integer> groupIdResOpenNum = new HashMap<>();
        Map<Integer, Integer> groupIdResCloseNum = new HashMap<>();
        for (Rate3 rate : rates) {
            String machineCode = rate.getStr1();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResOpenNum.merge(groupId, rate.getInte1(), Integer::sum);
            groupIdResCloseNum.merge(groupId, rate.getInte2(), Integer::sum);
        }

        // 获取小组良率数据
        List<DfGroupClose> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResOpenNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupClose data = new DfGroupClose();
            Integer groupOpenNum = entry.getValue();  // 小组开启数
            Integer groupCloseNum = groupIdResCloseNum.get(groupId) == null ? 0 : groupIdResCloseNum.get(groupId);  // 小组关闭数
            Double closeRate = groupCloseNum.doubleValue() / groupOpenNum;
            data.setGroupId(groupId);
            data.setOpenNum(groupOpenNum);
            data.setCloseNum(groupCloseNum);
            data.setCloseRate(closeRate);
            data.setTestTime(Timestamp.valueOf(testTime));
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸
            list.add(data);
        }
        saveBatch(list);

        System.out.println("===========更新小组关闭率（结束）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    @Override
    public List<Rate3> listMachineCloseNum(Wrapper<DfGroupClose> wrapper) {
        return dfGroupCloseMapper.listMachineCloseNum(wrapper);
    }

    @Override
    public List<Rate3> listCloseRate(Wrapper<DfGroupClose> wrapper) {
        return dfGroupCloseMapper.listCloseRate(wrapper);
    }

    @Override
    public List<Rate3> listGroupCloseRateTop(Wrapper<DfGroupClose> wrapper) {
        return dfGroupCloseMapper.listGroupCloseRateTop(wrapper);
    }

}
