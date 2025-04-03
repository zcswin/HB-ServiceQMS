package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-29
 */
public interface DfLeadCheckItemInfosMapper extends BaseMapper<DfLeadCheckItemInfos> {

//    @Select("select\n" +
//            "dlcii.check_result \n" +
//            ",dlcii.check_value \n" +
//            ",dlcii.standard_value\n" +
//            ",dlcii.lsl\n" +
//            ",dlcii.usl \n" +
//            "from df_lead_detail dld \n" +
//            "inner join df_lead_check_item_infos dlcii\n" +
//            "on dlcii.check_id = dld.id" +
//            " ${ew.customSqlSegment} ")
//    List<DfLeadCheckItemInfos> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> wrapper);
}
