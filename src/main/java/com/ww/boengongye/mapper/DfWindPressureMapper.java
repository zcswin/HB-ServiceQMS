package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfPrintAoiCheckDetail;
import com.ww.boengongye.entity.DfWindPressure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 风压点检表 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-18
 */
public interface DfWindPressureMapper extends BaseMapper<DfWindPressure> {

    @Select("with time_new as(\n" +
            "\tselect date_format(c.datelist,'%c/%e') checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            ")\n" +
            ",dwp_new as(\n" +
            "\tselect date_format(dwp.check_time,'%c/%e') checkTime,dwp.check_value checkValue\n" +
            "\tfrom df_wind_pressure dwp\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            "select " +
            "time_new.checkTime str1" +
            ",dwp_new.checkValue dou1\n" +
            "from time_new\n" +
            "left join dwp_new\n" +
            "on dwp_new.checkTime = time_new.checkTime " +
            "where dwp_new.checkValue is not null")
    //单个风压点
    List<Rate3> getOneSpotWindPressureList(@Param(Constants.WRAPPER) Wrapper<DfWindPressure> wrapper,@Param("ew_2")Wrapper<String> wrapper2);

}
