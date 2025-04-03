package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLossColourConfigure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 * 漏检率颜色配置 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
public interface DfLossColourConfigureMapper extends BaseMapper<DfLossColourConfigure> {

    @Select("select dlcc.* " +
            "from df_loss_colour_configure dlcc " +
            "${ew.customSqlSegment}")
    IPage<DfLossColourConfigure> listJoinPage(IPage<DfLossColourConfigure> page, @Param(Constants.WRAPPER) Wrapper<DfLossColourConfigure> wrapper);
}
