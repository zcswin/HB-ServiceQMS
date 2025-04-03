package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfSizeItemNgRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸NG TOP Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-07-04
 */
public interface DfSizeItemNgRateMapper extends BaseMapper<DfSizeItemNgRate> {

    @Select("select pro_all_num.project, pro_all_num.color, pro_all_num.process, pro_ng_num.item_name item_name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num ng_rate from         " +
            "(select det.project, det.item_name color, det.process, item.item_name, sum(if(item.check_result='NG',1,0)) ng_num         " +
            "from df_size_check_item_infos item         " +
            "left join df_size_detail det on item.check_id = det.id         " +
            "where det.create_time between #{startTime} and #{endTime} and item.key_point = 1          " +
            "group by det.project, det.item_name, det.process, item.item_name ) pro_ng_num         " +
            "left join          " +
            "(select project, item_name color, process, count(*) all_num         " +
            "from df_size_detail         " +
            "where create_time between #{startTime} and #{endTime}         " +
            "group by project, item_name, process) pro_all_num on pro_ng_num.process = pro_all_num.process and pro_ng_num.project = pro_all_num.project and pro_ng_num.color = pro_all_num.color order by ng_rate desc")
    List<DfSizeItemNgRate> listSizeItemNgRate(String startTime, String endTime);

    @Select("select pro_all_num.process, pro_ng_num.item_name item_name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num ng_rate, pro_ng_num.date as 'create_time' ,  pro_all_num.day_or_night from           " +
            "            (select t.process, t.item_name, t.date, t.day_or_night, sum(if(t.check_result='NG',1,0)) ng_num from  " +
            "            (select det.process, item.item_name,  DATE_FORMAT(DATE_SUB(det.create_time, INTERVAL 7 HOUR),'%Y-%m-%d 07:00:01') date, if(HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night, item.check_result " +
            "            from df_size_check_item_infos item           " +
            "            left join df_size_detail det on item.check_id = det.id           " +
            "            where item.key_point = 1) t " +
            "            group by t.process, t.item_name, t.date, t.day_or_night)  pro_ng_num           " +
            "            left join            " +
            "            (select *, count(*) all_num from  " +
            "            (select process, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%Y-%m-%d 07:00:01') date, if(HOUR(DATE_SUB(create_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night " +
            "            from df_size_detail) t " +
            "            group by process, date, day_or_night) pro_all_num on pro_ng_num.process = pro_all_num.process and pro_ng_num.date = pro_all_num.date and pro_ng_num.day_or_night = pro_all_num.day_or_night where pro_all_num.process is not null order by pro_ng_num.date")
    List<DfSizeItemNgRate> listAllProcessNgItemNgRate();
}
