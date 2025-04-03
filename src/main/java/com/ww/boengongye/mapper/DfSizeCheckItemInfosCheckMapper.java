package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.DfSizeCheckItemInfosCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.DfSizeDetailCheck;
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
 * @since 2025-01-12
 */
public interface DfSizeCheckItemInfosCheckMapper extends BaseMapper<DfSizeCheckItemInfosCheck> {

    @Select("with size_new as(\n" +
            "\tselect \n" +
            "\tdsd.machine_code\n" +
            "\t,dsd.`result`\n" +
            "\t,dscii.check_result \n" +
            "\t,dscii.check_value \n" +
            "\t,dscii.standard_value\n" +
            "\t,dscii.lsl\n" +
            "\t,dscii.usl \n" +
            "\tfrom df_size_detail_check dsd \n" +
            "\tinner join df_size_check_item_infos_check dscii \n" +
            "\ton dscii.check_id = dsd.id \n" +
            " ${ew.customSqlSegment} " +
            ")\n" +
            ",machine_temp as (\n" +
            "\tselect \n" +
            "\tmachine_code \n" +
            "\t,sum(if(`result` = 'NG',1,0)) ngNum\n" +
            "\t,count(0) total\n" +
            "\t,round(sum(if(`result` = 'NG',1,0))/count(0),2) ngRate\n" +
            "\tfrom size_new\n" +
            "\tgroup by machine_code\n" +
            "\torder by machine_code\n" +
            ")\n" +
            "select \n" +
            "sn.*,mt.ngNum,mt.total,mt.ngRate\n" +
            "from size_new sn\n" +
            "inner join machine_temp mt\n" +
            "on mt.machine_code = sn.machine_code")
    List<Map<String,Object>> getDetailData(@Param(Constants.WRAPPER) Wrapper<DfSizeDetailCheck> wrapper);

    @Select("select dscii.*\n" +
            "from df_size_detail_check dsd \n" +
            "inner join df_size_check_item_infos_check dscii \n" +
            "on dscii.check_id = dsd.id" +
            " ${ew.customSqlSegment} ")
    List<DfSizeCheckItemInfosCheck> getSizeCheckItemInfosList(@Param(Constants.WRAPPER) Wrapper<DfSizeDetailCheck> wrapper);

}
