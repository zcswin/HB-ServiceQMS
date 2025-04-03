package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProject;
import com.ww.boengongye.entity.LineBody;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
public interface LineBodyMapper extends BaseMapper<LineBody> {
    @Select("SELECT r.*, fac.factory_name FROM line_body r " +
            "left join df_factory fac on r.factory_code = fac.factory_code " +
            "${ew.customSqlSegment}")
    IPage<LineBody> listJoinIds(IPage<LineBody> page, @Param(Constants.WRAPPER) Wrapper<LineBody> wrapper);
}
