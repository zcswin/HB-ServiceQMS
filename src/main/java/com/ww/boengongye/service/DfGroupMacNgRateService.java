package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupClose;
import com.ww.boengongye.entity.DfGroupMacNgRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 小组机台超时率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
public interface DfGroupMacNgRateService extends IService<DfGroupMacNgRate> {

    void generateDataByDateTime(String dateTime) throws ParseException;

    List<Rate3> listGroupMacNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfGroupMacNgRate> wrapper);

}
