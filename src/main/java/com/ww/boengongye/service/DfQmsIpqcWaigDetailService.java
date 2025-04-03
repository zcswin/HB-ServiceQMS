package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetail;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
public interface DfQmsIpqcWaigDetailService extends IService<DfQmsIpqcWaigDetail> {
    List<DfQmsIpqcWaigDetail> listByJoin(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);

    List<DfQmsIpqcWaigDetail> listBySmAreaCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);

    DfQmsIpqcWaigDetail getSumAffectCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);

	List<DfSizeCheckItemInfos> fullApperanceNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate3> getFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper,@Param("ew_2")Wrapper<Rate3> wrapper2);

    List<Rate3> getAfterFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper,@Param("ew_2")Wrapper<Rate3> wrapper2);

	List<Rate4> getAlarmMessage(Double alarmValue);

	List<Rate4> getWarnMessage(Double alarmValue,Double warnValue);

    List<DfQmsIpqcWaigDetail> listSmallAreaInfo(@Param("project") String project);


    List<DfQmsIpqcWaigDetail> listProcessInfo(@Param("project") String project);
}
