package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiSeatProtect;
import com.ww.boengongye.entity.DfAoiSeatProtect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工位维护 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-04
 */
public interface DfAoiSeatProtectMapper extends BaseMapper<DfAoiSeatProtect> {
    @Select("select dasp.*,u.name userCode,u.alias userName," +
            "df.factory_name factoryName,lb.name lineBobyName,u.process userProcess," +
            "u.grade userGrade,u.create_time userCreateTime,u.labor_relation userLaborRelation " +
            "from df_aoi_seat_protect dasp " +
            "left join `user` u " +
            "on dasp.user_id =u.id " +
            "left join df_factory df " +
            "on dasp.factory_id =df.id " +
            "left join line_body lb " +
            "on dasp.line_boby_id =lb.id " +
            "${ew.customSqlSegment}")
    IPage<DfAoiSeatProtect> listJoinPage(IPage<DfAoiSeatProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiSeatProtect> wrapper);


    @Select("select dasp.*,u.name userCode,u.alias userName," +
            "df.factory_name factoryName,lb.name lineBobyName,u.process userProcess," +
            "u.grade userGrade,u.create_time userCreateTime,u.labor_relation userLaborRelation " +
            "from df_aoi_seat_protect dasp " +
            "left join `user` u " +
            "on dasp.user_id =u.id " +
            "left join df_factory df " +
            "on dasp.factory_id =df.id " +
            "left join line_body lb " +
            "on dasp.line_boby_id =lb.id ")
    List<DfAoiSeatProtect> getAllList();


    @Select("select dasp.*,u.name userCode,u.alias userName,u.process userProcess,u.grade userGrade " +
            "from df_aoi_seat_protect dasp " +
            "left join `user` u " +
            "on dasp.user_id =u.id "+
            "${ew.customSqlSegment}")
    DfAoiSeatProtect getSeatProtectByUserCode(@Param(Constants.WRAPPER) Wrapper<DfAoiSeatProtect> wrapper);

}
