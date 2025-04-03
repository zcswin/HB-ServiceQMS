package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.ww.boengongye.entity.DfECodeProtect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 不良代码维护 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
public interface DfDefectCodeProtectMapper extends BaseMapper<DfDefectCodeProtect> {

    @Select("select ddcp.*,dp.name projectName " +
            "from df_defect_code_protect ddcp " +
            "left join df_project dp " +
            "on ddcp.project_id = dp.id " +
            "${ew.customSqlSegment}")
    IPage<DfDefectCodeProtect> listJoinPage(IPage<DfDefectCodeProtect> page, @Param(Constants.WRAPPER) Wrapper<DfDefectCodeProtect> wrapper);

    @Select("select ddcp.*,dp.name projectName " +
            "from df_defect_code_protect ddcp " +
            "left join df_project dp " +
            "on ddcp.project_id = dp.id")
    List<DfDefectCodeProtect> getAllList();
}



