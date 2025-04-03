package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.entity.DfTzDetailCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * TZ测量 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfTzDetailCheckMapper extends BaseMapper<DfTzDetailCheck> {
    @Select("select \n" +
            "date_format(date_sub(check_time,interval 7 hour),'%Y-%m-%d') str1\n" +
            ",sum(if(`result` = 'NG',1,0)) inte1\n" +
            ",count(0) inte2\n" +
            ",round(sum(if(`result` = 'NG',1,0))/count(0) * 100,2) dou1\n" +
            "from df_tz_detail_check\n" +
            " ${ew.customSqlSegment} " +
            "group by str1\n" +
            "order by str1 asc")
    List<Rate3> getNgRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetailCheck> wrapper);

    // TZ不良Top10
    @Select("with defect_name as(\n" +
            "\tselect ipqc_name ,name\n" +
            "\tfrom df_size_cont_relation\n" +
            " where `type` = 'TZ' and process = 'TZ'" +
            ")\n" +
            "select \n" +
            "dn.ipqc_name str1\n" +
            ",sum(if(dtcii.check_result = 'NG',1,0)) inte1\n" +
            ",count(0) inte2\n" +
            ",round(sum(if(dtcii.check_result = 'NG',1,0))/count(0) * 100,4) dou1 \n" +
            "from df_tz_detail_check dtd \n" +
            "inner join df_tz_check_item_infos_check dtcii \n" +
            "on dtcii.check_id = dtd.id \n" +
            "inner join defect_name dn \n" +
            "on dn.name = dtcii.check_name \n" +
            " ${ew.customSqlSegment} " +
            "group by dn.ipqc_name\n" +
            "having dou1 > 0\n" +
            "order by dou1 desc")
    List<Rate3> getNgDetailRateTop10(@Param(Constants.WRAPPER) Wrapper<DfTzDetailCheck> wrapper);
}
