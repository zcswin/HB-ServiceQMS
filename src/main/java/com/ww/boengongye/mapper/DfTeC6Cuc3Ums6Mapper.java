package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * C6_CNC3_UMS6检测数据 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-12-06
 */
public interface DfTeC6Cuc3Ums6Mapper extends BaseMapper<DfTeC6Cuc3Ums6> {

    @Select("select machine_code, count(*) as indexing from df_te_c6_cuc3_ums6 ${ew.customSqlSegment} " +
            "group by machine_code order by indexing desc limit 10 ")
    List<DfTeC6Cuc3Ums6> listTop10NG(@Param(Constants.WRAPPER) Wrapper<DfTeC6Cuc3Ums6> wrapper);
}
