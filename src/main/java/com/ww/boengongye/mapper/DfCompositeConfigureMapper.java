package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfCompositeConfigure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 综合配置 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
public interface DfCompositeConfigureMapper extends BaseMapper<DfCompositeConfigure> {

    @Select("select dcc.* " +
            "from df_composite_configure dcc " +
            "${ew.customSqlSegment}")
    IPage<DfCompositeConfigure> listJoinPage(IPage<DfCompositeConfigure> page, @Param(Constants.WRAPPER)Wrapper<DfCompositeConfigure> wrapper);
}
