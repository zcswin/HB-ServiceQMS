package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfIqcDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DfIqcDetailMapper extends BaseMapper<DfIqcDetail> {

    @Select("SELECT\n" +
            "  d.*,\n" +
            "  f.factory_name,\n" +           // 关联工厂表
            "  s.supplier_address\n" +        // 关联供应商表
            "FROM\n" +
            "  df_iqc_detail d\n" +
            "  LEFT JOIN df_factory f ON d.factory = f.factory_code\n" +
            "  LEFT JOIN df_supplier s ON d.supplier = s.supplier_code\n" +
            "  ${ew.customSqlSegment}")       // 动态条件
    List<DfIqcDetail> listDetailWithJoin(@Param(Constants.WRAPPER) Wrapper<DfIqcDetail> wrapper);
}