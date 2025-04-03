package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupOkRate;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfGroupOkRateMapper;
import com.ww.boengongye.service.DfGroupOkRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
public class DfGroupOkRateServiceImpl extends ServiceImpl<DfGroupOkRateMapper, DfGroupOkRate> implements DfGroupOkRateService {

    @Autowired
    private DfGroupOkRateMapper dfGroupOkRateMapper;

    @Autowired
    private DfGroupService dfGroupService;

    @Autowired
    private DfSizeDetailService dfSizeDetailService;

    @Override
    public void generateDataByDateTime(String dateTime) throws ParseException {
        System.out.println("===========更新小组检测良率（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
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
        String inTheTime; // 时间段

        // 需要更新的时间
        String updateStartTestTime;
        String updateEndTestTime;
        // 往前推六个小时
        String bigStartTime;
        switch (now.getHours()) {
            case 23:
            case 0:
                littleStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 21:00:00";
                updateEndTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 19:00:00";
                inTheTime = "23-1";
                break;
            case 1:
            case 2:
                littleStartTime = today + " 01:00:00";
                updateStartTestTime = TimeUtil.getLastDay(today) + " 23:00:00";
                updateEndTestTime = today + " 01:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 21:00:00";
                inTheTime = "1-3";
                break;
            case 3:
            case 4:
                littleStartTime = today + " 03:00:00";
                updateStartTestTime = today + " 01:00:00";
                updateEndTestTime = today + " 03:00:00";
                bigStartTime = TimeUtil.getLastDay(today) + " 23:00:00";
                inTheTime = "3-5";
                break;
            case 5:
            case 6:
                littleStartTime = today + " 05:00:00";
                updateStartTestTime = today + " 03:00:00";
                updateEndTestTime = today + " 05:00:00";
                bigStartTime = today + " 01:00:00";
                inTheTime = "5-7";
                break;
            case 7:
            case 8:
                littleStartTime = today + " 07:00:00";
                updateStartTestTime = today + " 05:00:00";
                updateEndTestTime = today + " 07:00:00";
                bigStartTime = today + " 03:00:00";
                inTheTime = "7-9";
                break;
            case 9:
            case 10:
                littleStartTime = today + " 09:00:00";
                updateStartTestTime = today + " 07:00:00";
                updateEndTestTime = today + " 09:00:00";
                bigStartTime = today + " 05:00:00";
                inTheTime = "9-11";
                break;
            case 11:
            case 12:
                littleStartTime = today + " 11:00:00";
                updateStartTestTime = today + " 09:00:00";
                updateEndTestTime = today + " 11:00:00";
                bigStartTime = today + " 07:00:00";
                inTheTime = "11-13";
                break;
            case 13:
            case 14:
                littleStartTime = today + " 13:00:00";
                updateStartTestTime = today + " 11:00:00";
                updateEndTestTime = today + " 13:00:00";
                bigStartTime = today + " 09:00:00";
                inTheTime = "13-15";
                break;
            case 15:
            case 16:
                littleStartTime = today + " 15:00:00";
                updateStartTestTime = today + " 13:00:00";
                updateEndTestTime = today + " 15:00:00";
                bigStartTime = today + " 11:00:00";
                inTheTime = "15-17";
                break;
            case 17:
            case 18:
                littleStartTime = today + " 17:00:00";
                updateStartTestTime = today + " 15:00:00";
                updateEndTestTime = today + " 17:00:00";
                bigStartTime = today + " 13:00:00";
                inTheTime = "17-19";
                break;
            case 19:
            case 20:
                littleStartTime = today + " 19:00:00";
                updateStartTestTime = today + " 17:00:00";
                updateEndTestTime = today + " 19:00:00";
                bigStartTime = today + " 15:00:00";
                inTheTime = "19-21";
                break;
            case 21:
            case 22:
                littleStartTime = today + " 21:00:00";
                updateStartTestTime = today + " 19:00:00";
                updateEndTestTime = today + " 21:00:00";
                bigStartTime = today + " 17:00:00";
                inTheTime = "21-23";
                break;
            default:
                littleStartTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateStartTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                updateEndTestTime = TimeUtil.afterOneHour0_0ToNowDate(now);
                bigStartTime = null;
                inTheTime = "0-0";
        }
        QueryWrapper<DfSizeDetail> littleqw = new QueryWrapper<>();  //  -- 尺寸
        littleqw.between("create_time", littleStartTime, littleEndTime);
        littleqw.select("machine_code", "result");
        List<DfSizeDetail> littleMachineCode = dfSizeDetailService.list(littleqw);

        // 机台对应小组的id
        Map<String, Integer> macResGroupId = dfGroupService.getMacResGroupId(month, dayOrNight);

        // 获取小组当前时间段的OK数和总数
        Map<Integer, Integer> groupIdResOKNum = new HashMap<>();
        Map<Integer, Integer> groupIdResAllNum = new HashMap<>();
        for (DfSizeDetail littleData : littleMachineCode) {
            String machineCode = littleData.getMachineCode();
            Integer groupId = macResGroupId.get(machineCode);
            groupIdResAllNum.merge(groupId, 1, Integer::sum);
            if ("OK".equals(littleData.getResult())) {
                groupIdResOKNum.merge(groupId, 1, Integer::sum);
            }
        }

        // 获取小组良率数据
        List<DfGroupOkRate> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : groupIdResAllNum.entrySet()) {
            Integer groupId = entry.getKey();
            if (null == groupId) continue;
            DfGroupOkRate data = new DfGroupOkRate();
            Integer groupAllNum = entry.getValue();  // 小组检测数
            Integer groupOKNum = groupIdResOKNum.get(groupId) == null ? 0 : groupIdResOKNum.get(groupId);  // 小组时间段OK数
            Double okRate = groupOKNum.doubleValue() / groupAllNum;
            data.setGroupId(groupId);
            data.setAllCheckNum(groupAllNum);
            data.setOkCheckNum(groupOKNum);
            data.setOkRate(okRate);
            data.setIntheTime(inTheTime);
            data.setTestTime(now);
            data.setDayOrNight(dayOrNight);
            data.setTestType(1);  // 1-尺寸


            list.add(data);
        }
        saveBatch(list);

        System.out.println("===========更新小组检测良率（开始）===========" + Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
    }

    @Override
    public List<Rate3> listGroupOkRate(Wrapper<DfGroupOkRate> wrapper) {
        return dfGroupOkRateMapper.listGroupOkRate(wrapper);
    }

    @Override
    public List<Rate3> listGroupOkRateTop(Wrapper<DfGroupOkRate> wrapper) {
        return dfGroupOkRateMapper.listGroupOkRateTop(wrapper);
    }
}
