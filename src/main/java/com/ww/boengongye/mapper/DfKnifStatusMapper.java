package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfKnifStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfKnifStatusType6;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 砂轮刀具状态 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-09-21
 */
public interface DfKnifStatusMapper extends BaseMapper<DfKnifStatus> {

    @Select("select \n" +
            "DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') str1 \n" +
            ",SUM(IF(tool_cut_num < 150,1,0)) inte1\n" +
            ",SUM(IF(tool_cut_num = 150,1,0)) inte2\n" +
            "from df_knif_status\n" +
            " ${ew.customSqlSegment} " +
            "group by str1")
    List<Rate3> getKnifeLifeDistribution(@Param(Constants.WRAPPER) Wrapper<DfKnifStatus> wrapper);
}
