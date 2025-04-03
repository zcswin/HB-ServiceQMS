package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 员工调机排名--尺寸NG率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
public interface DfAdSizeNgRateMapper extends BaseMapper<DfAdSizeNgRate> {

    @Select("select item.item_name, count(item.item_name) num_test, sum(if(item.check_result='NG',1,0)) num_ng, " +
            "sum(if(item.check_result='NG',1,0)) / count(item.item_name) rate_ng,  " +
            "det.factory factory, det.process process, det.project project, det.linebody linebody, det.day_or_night day_or_night " +
            "from df_size_check_item_infos item  " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} " +
            "group by item_name,det.factory, det.process, det.project, det.linebody, det.day_or_night ")
    List<DfAdSizeNgRate> listNgRateByItemInfos(@Param(Constants.WRAPPER) Wrapper<DfAdSizeNgRate> wrapper);

    @Select("select item_name, avg(rate_ng) rate_ng " +
            "from df_ad_size_ng_rate " +
            "${ew.customSqlSegment} " +
            "group by item_name " +
            "order by rate_ng desc " +
            "limit 10 ")
    List<DfAdSizeNgRate> listNgTop10(@Param(Constants.WRAPPER) Wrapper<DfAdSizeNgRate> wrapper);


    // 外观NG项NG率
    @Select("select pro_ngitem_num.*, pro_allnum.all_num as num_test, pro_ngitem_num.num_ng / pro_allnum.all_num as rate_ng from  " +
            "(select tol.f_seq as process, det.f_sort as item_name, tol.f_bigpro as project, tol.f_fac as factory, tol.f_line as linebody, count(distinct f_sort, f_parent_id) as num_ng " +
            "from df_qms_ipqc_waig_detail det " +
            "left join df_qms_ipqc_waig_total tol on det.f_parent_id = tol.id " +
            "where det.f_time between #{startTime} and #{endTime} " +
            "group by det.f_sort, tol.f_seq, tol.f_bigpro, tol.f_fac, tol.f_line) pro_ngitem_num " +
            "left join  " +
            "(select f_seq, f_bigpro, f_fac, f_line, count(*) all_num " +
            "from df_qms_ipqc_waig_total  " +
            "where f_time between #{startTime} and #{endTime} " +
            "group by f_seq, f_bigpro, f_fac, f_line) pro_allnum on  " +
            "pro_ngitem_num.process = pro_allnum.f_seq and  " +
            "pro_ngitem_num.project = pro_allnum.f_bigpro and  " +
            "pro_ngitem_num.factory = pro_allnum.f_fac and  " +
            "pro_ngitem_num.linebody = pro_allnum.f_line")
    List<DfAdSizeNgRate> listAppearNgInfos(String startTime, String endTime);
}
