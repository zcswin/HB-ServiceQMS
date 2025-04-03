package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfRework;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
public interface DfProjectMapper extends BaseMapper<DfProject> {
    @Select("SELECT r.*, fac.factory_name,p.name as line_name FROM df_project r " +
            "left join df_factory fac on r.factory_code = fac.factory_code " +
            "left join line_body p on r.line_code = p.code " +
            "${ew.customSqlSegment}")
    IPage<DfProject> listJoinIds(IPage<DfProject> page, @Param(Constants.WRAPPER) Wrapper<DfProject> wrapper);
}
