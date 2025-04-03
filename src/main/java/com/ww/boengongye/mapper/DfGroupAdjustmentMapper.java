package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupAdjustment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeNgRate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 小组调机能力 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
public interface DfGroupAdjustmentMapper extends BaseMapper<DfGroupAdjustment> {

    @Select("select f_group groupName, respon groupRespon,  " +
            "avg(quarantine_rate) as quarantine_rate, " +
            "avg(adjustment_rate) as adjustment_rate, " +
            "avg(normal_rate) as normal_rate, " +
            "avg(unused_rate) as unused_rate " +
            "from df_group_adjustment aj " +
            "left join df_group gp on aj.group_id = gp.id " +
            "${ew.customSqlSegment} " +
            "group by group_id " +
            "order by quarantine_rate, adjustment_rate, normal_rate, unused_rate " +
            "limit 5")
    List<DfGroupAdjustment> listBestRate(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);
}
