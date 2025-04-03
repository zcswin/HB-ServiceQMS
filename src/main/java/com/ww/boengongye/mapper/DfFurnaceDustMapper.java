package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFurnaceDust;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 炉内尘点 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-09-05
 */
public interface DfFurnaceDustMapper extends BaseMapper<DfFurnaceDust> {

    @Select("SELECT position str1,  " +
            "sum(if(um05 > 1000, 0, 1))/ count(*) dou1,  " +
            "sum(if(um5 > 10, 0, 1))/ count(*) dou2,   " +
            "sum(if(um05 > 1000 or um5 > 10, 0, 1)) / count(*) dou3 " +
            "FROM `df_furnace_dust` " +
            "${ew.customSqlSegment} " +
            "group by position ")
    List<Rate3> getProcessOKRate(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);

    @Select("select position str1, sum(um05) inte1, sum(um5) inte2 from df_furnace_dust " +
            "${ew.customSqlSegment} " +
            "group by position")
    List<Rate3> getAllPositionDustNum(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);

    @Select("select * from ( " +
            "select date_format(check_time, #{format}) str1, sum(um05) inte1, sum(um5) inte2 " +
            "from df_furnace_dust " +
            "${ew.customSqlSegment} " +
            "group by str1 " +
            "order by str1 desc " +
            "limit 100 ) tabl " +
            "order by str1")
    List<Rate3> getDustNumOrderByTime(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper, String format);

    @Select("select t1.position str1, t1.check_time str2, t1.um05 inte1, t1.um5 inte2, t2.* from ( " +
            "select * from df_furnace_dust " +
            "where (position, check_time) in( " +
            "select position, max(check_time) from df_furnace_dust  " +
            "${ew.customSqlSegment} " +
            "group by position) ) t1 " +
            "left join  " +
            "(select position, max(check_time), min(um05) inte3, max(um05) inte4, avg(um05) dou1, min(um5) inte5, max(um5) inte6, avg(um5) dou2 " +
            "from df_furnace_dust " +
            "${ew.customSqlSegment} " +
            "group by position) t2 on t1.position = t2.position")
    List<Rate4> getPositionDetail(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);
}
