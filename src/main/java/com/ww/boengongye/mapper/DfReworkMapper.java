package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRework;
import com.ww.boengongye.entity.DfRework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 返工表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-12-19
 */
public interface DfReworkMapper extends BaseMapper<DfRework> {
    @Select("SELECT r.*, fac.factory_name,p.process_name FROM df_rework r " +
            "left join df_factory fac on r.factory_code = fac.factory_code " +
            "left join df_process p on r.process = p.process_code " +
            "${ew.customSqlSegment}")
    IPage<DfRework> listJoinIds(IPage<DfRework> page, @Param(Constants.WRAPPER) Wrapper<DfRework> wrapper);
}
