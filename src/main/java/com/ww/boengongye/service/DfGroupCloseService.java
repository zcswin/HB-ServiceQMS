package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupClose;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 小组两小时时间段良率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
public interface DfGroupCloseService extends IService<DfGroupClose> {

    void generateDataByDateTime(String dateTime) throws ParseException;

    List<Rate3> listMachineCloseNum(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);

    List<Rate3> listCloseRate(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);

    List<Rate3> listGroupCloseRateTop(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);

}
