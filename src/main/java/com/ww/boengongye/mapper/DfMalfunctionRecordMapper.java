package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfInventory;
import com.ww.boengongye.entity.DfMalfunctionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 设备故障记录 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
public interface DfMalfunctionRecordMapper extends BaseMapper<DfMalfunctionRecord> {
    @Select("SELECT m.*, fac.factory_name FROM df_malfunction_record m " +
            "left join df_factory fac on m.factory_code = fac.factory_code " +
            "${ew.customSqlSegment}")
    IPage<DfMalfunctionRecord> listJoinIds(IPage<DfMalfunctionRecord> page, @Param(Constants.WRAPPER) Wrapper<DfMalfunctionRecord> wrapper);
}
