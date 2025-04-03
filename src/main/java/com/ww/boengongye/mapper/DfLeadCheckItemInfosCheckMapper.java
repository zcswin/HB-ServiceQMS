package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadCheckItemInfosCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfLeadCheckItemInfosCheckMapper extends BaseMapper<DfLeadCheckItemInfosCheck> {

    @Select("select\n" +
            "dlcii.check_result \n" +
            ",dlcii.check_value \n" +
            ",dlcii.standard_value\n" +
            ",dlcii.lsl\n" +
            ",dlcii.usl \n" +
            "from df_lead_detail_check dld \n" +
            "inner join df_lead_check_item_infos_check dlcii\n" +
            "on dlcii.check_id = dld.id" +
            " ${ew.customSqlSegment} ")
    List<DfLeadCheckItemInfosCheck> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfLeadCheckItemInfosCheck> wrapper);
}
