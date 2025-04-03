package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfKnifStatus;
import com.ww.boengongye.entity.DfKnifeUseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 刀具使用信息 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-11-13
 */
public interface DfKnifeUseInfoMapper extends BaseMapper<DfKnifeUseInfo> {
    @Select("select \n" +
            "DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') str1 \n" +
            ",SUM(IF(life_act < 150,1,0)) inte1\n" +
            ",SUM(IF(life_act = 150,1,0)) inte2\n" +
            "from df_knife_use_info\n" +
            " ${ew.customSqlSegment} " +
            "group by str1")
    List<Rate3> getKnifeLifeDistribution(@Param(Constants.WRAPPER) Wrapper<DfKnifeUseInfo> wrapper);
}
