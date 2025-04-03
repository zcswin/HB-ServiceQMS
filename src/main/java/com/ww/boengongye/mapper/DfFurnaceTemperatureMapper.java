package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFurnaceTemperature;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 炉温表 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-10-16
 */
public interface DfFurnaceTemperatureMapper extends BaseMapper<DfFurnaceTemperature> {


    @Select("select \n" +
            "dft.name\n" +
            "from df_furnace_temperature dft\n" +
            "${ew.customSqlSegment} " +
            "group by dft.name")
    List<String> getFurnaceNameList(@Param(Constants.WRAPPER)Wrapper<DfFurnaceTemperature> wrapper);

    @Select("with time_new as(\n" +
            "\tselect \n" +
            "\tdft.check_time checkTime\n" +
            "\tfrom df_furnace_temperature dft \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dft.check_time \n" +
            ")\n" +
            ",dft_new as(\n" +
            "\tselect\n" +
            "\tdft.check_time checkTime\n" +
            "\t,dft.check_value checkValue\n" +
            "\tfrom df_furnace_temperature dft \n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            "select \n" +
            "time_new.checkTime str1\n" +
            ",dft_new.checkValue dou1\n" +
            "from time_new\n" +
            "left join dft_new\n" +
            "on dft_new.checkTime = time_new.checkTime")
    List<Rate3> getOneFurnaceCheckValueList(@Param(Constants.WRAPPER)Wrapper<DfFurnaceTemperature> wrapper,@Param("ew_2")Wrapper<DfFurnaceTemperature> wrapper2);
}
