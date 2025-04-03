package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcFlawConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-29
 */
public interface DfQmsIpqcFlawConfigMapper extends BaseMapper<DfQmsIpqcFlawConfig> {

    @Select("select f.*,c.color" +
            " from df_qms_ipqc_flaw_config f left join df_qms_ipqc_flaw_color c on f.flaw_name=c.name  " +
            "${ew.customSqlSegment} ")
    List<DfQmsIpqcFlawConfig> listByJoin(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

    @Select("select distinct(flaw_name) as flawName" +
            " from df_qms_ipqc_flaw_config " +
            "${ew.customSqlSegment} ")
    List<DfQmsIpqcFlawConfig> listDistinct(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

    @Select("select distinct(flaw_name) as flawName,big_area as bigArea" +
            " from df_qms_ipqc_flaw_config " +
            "${ew.customSqlSegment} ")
    List<DfQmsIpqcFlawConfig> listDistinctArea(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

    @Select("select distinct(flaw_name) as flawName, big_area, process" +
            " from df_qms_ipqc_flaw_config " +
            "${ew.customSqlSegment} ")
    List<DfQmsIpqcFlawConfig> listDistinctAreaAndProcess(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

}
