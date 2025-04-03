package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfTzDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * TZ明细表 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
public interface DfTzCheckItemInfosMapper extends BaseMapper<DfTzCheckItemInfos> {

//    @Select("select \n" +
//            "dtcii.check_result \n" +
//            ",dtcii.check_value \n" +
//            ",dtcii.standard_value\n" +
//            ",dtcii.lsl\n" +
//            ",dtcii.usl \n" +
//            "from df_tz_detail dtd \n" +
//            "inner join df_tz_check_item_infos dtcii \n" +
//            "on dtcii.check_id = dtd.id " +
//            " ${ew.customSqlSegment} ")
//    List<DfTzCheckItemInfos> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfTzDetail> wrapper);

}
