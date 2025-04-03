package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfECodeProtect;
import com.ww.boengongye.entity.DfECodeProtect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * E-Code维护 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
public interface DfECodeProtectMapper extends BaseMapper<DfECodeProtect> {

    @Select("select decp.*,dp.name projectName " +
            "from df_e_code_protect decp " +
            "left join df_project dp " +
            "on decp.project_Id =dp.id " +
            "${ew.customSqlSegment}")
    IPage<DfECodeProtect> listJoinPage(IPage<DfECodeProtect> page, @Param(Constants.WRAPPER) Wrapper<DfECodeProtect> wrapper);



}
