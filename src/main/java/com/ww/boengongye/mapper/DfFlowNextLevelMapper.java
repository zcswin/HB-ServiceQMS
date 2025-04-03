package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFlowData;
import com.ww.boengongye.entity.DfFlowNextLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 流程的下一级配置 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
public interface DfFlowNextLevelMapper extends BaseMapper<DfFlowNextLevel> {

    @Select("select b.id as blockId,b.name as blockName ,l.* from  df_flow_next_level l join df_flow_block b on l.flow_type =b.flow_type and l.next_level=b.sort ${ew.customSqlSegment} ")
    List<DfFlowNextLevel>listNextLevel( @Param(Constants.WRAPPER) Wrapper<DfFlowNextLevel> wrapper);
}
