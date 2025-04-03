package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacYieldData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 机台产量数据 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-01-04
 */
public interface DfMacYieldDataMapper extends BaseMapper<DfMacYieldData> {

    @Select("select DATE_FORMAT(yie.create_time,'%Y-%m-%d') date, pro.process_name as process,  " +
            "avg(yie.produce_real_ok_rate) as produce_real_ok_rate, " +
            "avg(yie.produce_target_ok_rate) as produce_target_ok_rate, " +
            "avg(yie.appearance_real_ok_rate) as appearance_real_ok_rate, " +
            "avg(yie.appearance_target_ok_rate) as appearance_target_ok_rate, " +
            "avg(yie.size_real_ok_rate) as size_real_ok_rate, " +
            "avg(yie.size_target_ok_rate) as size_target_ok_rate, " +
            "avg(yie.overkill_rate) as overkill_rate, " +
            "avg(yie.crop_rate) as crop_rate, " +
            "avg(yie.achievement_rate) as achievement_rate, " +
            "avg(yie.undetected_rate) as undetected_rate " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position mac on mac.MachineCode = yie.mac_code " +
            "left join df_process pro on mac.area = pro.id " +
            "${ew.customSqlSegment} " +
            "group by date, pro.process_name")
    List<DfMacYieldData> listOkRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    @Select({"<script> " +
            "select yie.ng_type, AVG(yie.ng_rate) as ng_rate, DATE_FORMAT(yie.create_time,'%Y') as date " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position mac on mac.MachineCode = yie.mac_code " +
            "left join df_process pro on mac.area = pro.id " +
            "where yie.create_time like '2022%' " +
            "<if test='ew != null'> " +
            "            <if test='ew.nonEmptyOfWhere'> " +
            "                AND " +
            "            </if> " +
            "            ${ew.sqlSegment} " +
            "        </if> " +
            "group by date, yie.ng_type " +
            "union " +
            "select yie.ng_type, AVG(yie.ng_rate) as ng_rate, DATE_FORMAT(yie.create_time,'%Y-%m') as date " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position mac on mac.MachineCode = yie.mac_code " +
            "left join df_process pro on mac.area = pro.id " +
            "where yie.create_time like '2022%' " +
            "<if test='ew != null'> " +
            "            <if test='ew.nonEmptyOfWhere'> " +
            "                AND " +
            "            </if> " +
            "            ${ew.sqlSegment} " +
            "        </if> " +
            "group by date, yie.ng_type " +
            "</script>"})
    List<DfMacYieldData> listNgRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    @Select("select DATE_FORMAT(yie.create_time,'%Y-%m-%d') date, " +
            "avg(yie.AR) as AR, " +
            "avg(yie.QR) as QR, " +
            "avg(yie.PR) as PR, " +
            "avg(yie.OEE) as OEE " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position mac on mac.MachineCode = yie.mac_code " +
            "left join df_process pro on mac.area = pro.id " +
            "${ew.customSqlSegment} " +
            "group by date")
    List<DfMacYieldData> listOEE(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    @Select("select DATE_FORMAT(yie.create_time,'%Y-%m') date, " +
            "avg(yie.pass_through_rate) as pass_through_rate " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position mac on mac.MachineCode = yie.mac_code " +
            "left join df_process pro on mac.area = pro.id " +
            "${ew.customSqlSegment} " +
            "GROUP BY date " +
            "order by date")
    List<DfMacYieldData> listPassThroughRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    @Select("select yie.*, sdet.name as statusCurDetail, stat.name as statusCur, macsta.pub_time as statusUpdateTime " +
            "from df_mac_yield_data yie " +
            "left join df_mac_status sta on yie.mac_code = sta.MachineCode " +
            "left join df_status_code sdet on sta.StatusID_Cur = sdet.status_code " +
            "left join df_status_code stat on stat.status_code = sdet.parent_status_id " +
            "left join df_mac_status macsta on macsta.MachineCode = yie.mac_code " +
            "${ew.customSqlSegment} ")
    List<DfMacYieldData> getMacDetail(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    @Select("select AVG(produce_real_ok_rate) as produce_real_ok_rate ,AVG(appearance_real_ok_rate) as appearance_real_ok_rate ,AVG(size_real_ok_rate) as size_real_ok_rate , pro.process_code,  " +
            "stadet.name as statusCurDetail, sta.name as statusCur, pro.update_time as statusUpdateTime " +
            "from df_mac_yield_data yie " +
            "left join df_mac_model_position macmod on yie.mac_code = macmod.MachineCode " +
            "left join df_process pro on pro.id = macmod.area " +
            "left join df_process_status_param stadet on pro.process_status = stadet.id " +
            "left join df_process_status_param sta on stadet.parent_status_id = sta.id " +
            "${ew.customSqlSegment} " +
            "group by pro.id")
    List<DfMacYieldData> getProDetail(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);
}
