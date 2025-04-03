package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProcessProjectConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-04-26
 */
public interface DfProcessProjectConfigMapper extends BaseMapper<DfProcessProjectConfig> {
    @Select("select DISTINCT( process_name) AS process_name,sort,project FROM df_process_project_config " +
            "${ew.customSqlSegment} " )
    List<DfProcessProjectConfig> listDistinct(@Param(Constants.WRAPPER) Wrapper<DfProcessProjectConfig> wrapper);
}
