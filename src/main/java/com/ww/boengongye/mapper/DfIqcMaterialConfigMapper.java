package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfIqcMaterialConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DfIqcMaterialConfigMapper extends BaseMapper<DfIqcMaterialConfig> {


    @Select("SELECT\n" +
            "  c.*,\n" +
            "  p.description AS process_desc\n" +  // 关联工序表
            "FROM\n" +
            "  df_iqc_material_config c\n" +
            "  LEFT JOIN df_iqc_detail  p ON c.code = p.material_code\n" +
            "  ${ew.customSqlSegment}")
        // 动态条件
    List<DfIqcMaterialConfig> listConfigWithJoin(@Param(Constants.WRAPPER) Wrapper<DfIqcMaterialConfig> wrapper);
}