package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.DfSizeOkRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸良率汇总 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-15
 */
public interface DfSizeOkRateMapper extends BaseMapper<DfSizeOkRate> {

    @Select("select process, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%Y-%m-%d 07:00:01') date,  " +
            "sum(if(result = 'OK',1,0)) ok_num, count(*) all_num, sum(if(result = 'OK',1,0)) / count(*) ok_rate " +
            "from df_size_detail " +
            "group by process, date " +
            "order by date")
    List<DfSizeOkRate> updateDate();

    @Select("select project, item_name color, process, sum(if(result = 'OK',1,0)) ok_num, count(*) all_num, sum(if(result = 'OK',1,0)) / count(*) ok_rate " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by project, item_name, process")
    List<DfSizeOkRate> listSizeOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeOkRate> wrapper);
}
