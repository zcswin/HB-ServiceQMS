package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 车间维护 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
public interface DfAoiCarProtectMapper extends BaseMapper<DfAoiCarProtect> {
    @Select("select dacp.*,df.factory_name ,df.factory_code " +
            "from df_aoi_car_protect dacp " +
            "left join df_factory df " +
            "on dacp.factory_id =df.id " +
            "${ew.customSqlSegment}")
    IPage<DfAoiCarProtect> listJoinPage(IPage<DfAoiCarProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiCarProtect> wrapper);
}
