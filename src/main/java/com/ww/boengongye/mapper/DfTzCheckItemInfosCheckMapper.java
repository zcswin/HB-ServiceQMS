package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.ww.boengongye.entity.DfTzCheckItemInfosCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfTzDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * TZ测量明细表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfTzCheckItemInfosCheckMapper extends BaseMapper<DfTzCheckItemInfosCheck> {
    @Select("select \n" +
            "dtcii.check_result \n" +
            ",dtcii.check_value \n" +
            ",dtcii.standard_value\n" +
            ",dtcii.lsl\n" +
            ",dtcii.usl \n" +
            "from df_tz_detail_check dtd \n" +
            "inner join df_tz_check_item_infos_check dtcii \n" +
            "on dtcii.check_id = dtd.id " +
            " ${ew.customSqlSegment} ")
    List<DfTzCheckItemInfosCheck> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfTzCheckItemInfosCheck> wrapper);
}
