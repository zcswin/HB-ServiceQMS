package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
public interface DfQmsIpqcWaigDetailCheckService extends IService<DfQmsIpqcWaigDetailCheck> {
    List<DfQmsIpqcWaigDetailCheck> listByJoin(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetailCheck> wrapper);

    List<DfQmsIpqcWaigDetailCheck> listBySmAreaCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetailCheck> wrapper);

    DfQmsIpqcWaigDetailCheck getSumAffectCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetailCheck> wrapper);

    List<DfSizeCheckItemInfos> fullApperanceNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate3> getFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper, @Param("ew_2")Wrapper<Rate3> wrapper2);

    List<Rate3> getAfterFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper,@Param("ew_2")Wrapper<Rate3> wrapper2);

    List<Rate4> getAlarmMessage(Double alarmValue);

    List<Rate4> getWarnMessage(Double alarmValue,Double warnValue);

    List<DfQmsIpqcWaigDetailCheck> listSmallAreaInfo(@Param("project") String project);


    List<DfQmsIpqcWaigDetailCheck> listProcessInfo(@Param("project") String project);
}
