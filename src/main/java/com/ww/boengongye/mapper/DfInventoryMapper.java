package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.ww.boengongye.entity.DfInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 消耗品库存表
 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
public interface DfInventoryMapper extends BaseMapper<DfInventory> {
    @Select("SELECT i.*, fac.factory_name,l.name as line_body_name FROM df_inventory i " +
            "left join df_factory fac on i.factory_code = fac.factory_code " +
            "left join line_body l on i.line_body_code = l.code " +
            "${ew.customSqlSegment}")
    IPage<DfInventory> listJoinIds(IPage<DfInventory> page, @Param(Constants.WRAPPER) Wrapper<DfInventory> wrapper);
}
