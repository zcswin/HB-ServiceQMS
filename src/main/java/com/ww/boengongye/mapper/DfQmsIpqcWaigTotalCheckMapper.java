package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
public interface DfQmsIpqcWaigTotalCheckMapper extends BaseMapper<DfQmsIpqcWaigTotalCheck> {

    @Select("select little_num.flaw_name name, little_num.date date, avg(if(ng_num is null or all_num is null, 0, ng_num / all_num)) * 100 as rate, if(avg(ng_num) is null, 0, avg(ng_num)) ng_num, avg(all_num) all_num from " +
            "(select ng_res_date.*, num_data.ng_num from " +
            "(select * from df_qms_ipqc_flaw_config as con,(select date_format(tol.f_time, '%m-%d') date, dense_rank() over(ORDER by date_format(tol.f_time, '%m-%d') desc) as ran " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id  " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date " +
            "order by date) as date " +
            "where (con.process like #{process} and con.project like #{project} )) ng_res_date " +   // 在 #{project} 后面加上 and date.ran <= 7 限制输出天数
            "left join (select f_sort, count(*) ng_num, date_format(tol.f_time, '%m-%d') date, dense_rank() over(order by date_format(tol.f_time,'%m-%d') desc ) as ran " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by f_sort,date " +
            "order by date) num_data on num_data.f_sort = ng_res_date.flaw_name and num_data.date = ng_res_date.date) little_num " +
            "left join (select sum(spot_check_count) all_num, date_format(tol.f_time, '%m-%d') date " +
            "from df_qms_ipqc_waig_total_check tol " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date " +
            "order by date) all_num on little_num.date = all_num.date " +
            "group by little_num.flaw_name, little_num.date")
    List<Rate> listNgItemRate(String lineBody, String project, String process, String startTime, String endTime);

    @Select("select t2.* " +
            "from     " +
            "(select flaw_name name, avg(if(ng_num is null, 0, ng_num) / all_num) rate from      " +
            "(select * from (select *      " +
            "from df_qms_ipqc_flaw_config      " +
            "where process like #{process} and project like #{project}) item     " +
            "left join      " +
            "(select f_sort, count(*) ng_num " +
            "from df_qms_ipqc_waig_detail_check det     " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id     " +
            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime}     " +
            "group by f_sort) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng,      " +
            "(select sum(spot_check_count) all_num     " +
            "from df_qms_ipqc_waig_total_check     " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime}) all_num     " +
            "group by name     " +
            "order by rate desc     " +
            "limit 10) t     " +
            "left join df_qms_ipqc_flaw_color clr on t.name = clr.name   " +
            "left join  " +
            "(select little_num.flaw_name name, little_num.date date, avg(if(ng_num is null or all_num is null, 0, ng_num / all_num)) * 100 as rate, if(avg(ng_num) is null, 0, avg(ng_num)) ng_num, avg(all_num) all_num from     " +
            "(select ng_res_date.*, num_data.ng_num from     " +
            "(select * from df_qms_ipqc_flaw_config as con,(select date_format(tol.f_time, '%m-%d') date, dense_rank() over(ORDER by date_format(tol.f_time, '%m-%d') desc) as ran     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id      " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_time between #{startTime} and #{endTime}     " +
            "group by date     " +
            "order by date) as date     " +
            "where (con.process like #{process} and con.project like #{project} )) ng_res_date " +
            "left join (select f_sort, count(*) ng_num, date_format(tol.f_time, '%m-%d') date, dense_rank() over(order by date_format(tol.f_time,'%m-%d') desc ) as ran     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id     " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime}     " +
            "group by f_sort,date     " +
            "order by date) num_data on num_data.f_sort = ng_res_date.flaw_name and num_data.date = ng_res_date.date) little_num     " +
            "left join (select sum(spot_check_count) all_num, date_format(tol.f_time, '%m-%d') date     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime}     " +
            "group by date     " +
            "order by date) all_num on little_num.date = all_num.date     " +
            "group by little_num.flaw_name, little_num.date ) t2 on t.name = t2.name")
    List<Rate> listNgItemRateSortByNgTop(String lineBody, String project, String process, String startTime, String endTime);  // 根据NGTop排序

    @Select("select t2.* " +
            "from     " +
            "(select name, sum(rate) rate, row_number() over(order by sum(rate) desc) ran from " +
            "(select flaw_name name,  all_num.f_seq ,avg(if(ng_num is null, 0, ng_num) / all_num) rate from      " +
            "(select * from (select *      " +
            "from df_qms_ipqc_flaw_config      " +
            "where process like #{process} and project like #{project}) item     " +
            "left join      " +
            "(select f_sort,f_seq, count(*) ng_num from     " +
            "df_qms_ipqc_waig_detail_check det     " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id     " +
            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} " +
            "group by f_sort, f_seq) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng left join " +
            "(select f_seq,sum(spot_check_count) all_num     " +
            "from df_qms_ipqc_waig_total_check     " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq " +
            "group by name, f_seq     " +
            "order by rate desc     " +
            ") t  " +
            "group by name " +
            "order by rate desc " +
            "limit 10) t " +
            "left join df_qms_ipqc_flaw_color clr on t.name = clr.name   " +
            "left join  " +
            "(select little_num.flaw_name name, little_num.date date, avg(if(ng_num is null or all_num is null, 0, ng_num / all_num)) * 100 as rate, if(avg(ng_num) is null, 0, avg(ng_num)) ng_num, avg(all_num) all_num from     " +
            "(select ng_res_date.*, num_data.ng_num from     " +
            "(select * from df_qms_ipqc_flaw_config as con,(select date_format(tol.f_time, '%m-%d') date, dense_rank() over(ORDER by date_format(tol.f_time, '%m-%d') desc) as ran     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id      " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date     " +
            "order by date) as date     " +
            "where (con.process like #{process} and con.project like #{project} )) ng_res_date " +
            "left join (select f_sort, count(*) ng_num, date_format(tol.f_time, '%m-%d') date, dense_rank() over(order by date_format(tol.f_time,'%m-%d') desc ) as ran     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id     " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by f_sort,date     " +
            "order by date) num_data on num_data.f_sort = ng_res_date.flaw_name and num_data.date = ng_res_date.date) little_num     " +
            "left join (select sum(spot_check_count) all_num, date_format(tol.f_time, '%m-%d') date     " +
            "from df_qms_ipqc_waig_total_check tol     " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date     " +
            "order by date) all_num on little_num.date = all_num.date     " +
            "group by little_num.flaw_name, little_num.date ) t2 on t.name = t2.name order by date, t.ran")
        // 全天
    List<Rate> listNgItemRateSortByNgTop2(String lineBody, String project, String process, String startTime, String endTime);  // 根据NGTop排序

    @Select("select t2.*   " +
            "from       " +
            "(select name, sum(rate) rate, row_number() over(order by sum(rate) desc) ran from   " +
            "(select flaw_name name,  all_num.f_seq ,avg(if(ng_num is null, 0, ng_num) / all_num) rate from        " +
            "(select * from (select *        " +
            "from df_qms_ipqc_flaw_config        " +
            "where process like #{process} and project like #{project}) item       " +
            "left join        " +
            "(select f_sort,f_seq, count(*) ng_num from       " +
            "df_qms_ipqc_waig_detail_check det       " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id       " +
            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'   " +
            "group by f_sort, f_seq) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng left join   " +
            "(select f_seq,sum(spot_check_count) all_num       " +
            "from df_qms_ipqc_waig_total_check       " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq   " +
            "group by name, f_seq       " +
            "order by rate desc       " +
            ") t    " +
            "group by name   " +
            "order by rate desc   " +
            "limit 10) t   " +
            "left join    " +
            "(select little_num.flaw_name name, little_num.date date,little_num.day_or_night, avg(if(ng_num is null or all_num is null, 0, ng_num / all_num)) * 100 as rate, if(avg(ng_num) is null, 0, avg(ng_num)) ng_num, avg(all_num) all_num from       " +
            "(select ng_res_date.*, num_data.ng_num from       " +
            "(select * from df_qms_ipqc_flaw_config as con,(select DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date " +
            "from df_qms_ipqc_waig_total_check tol       " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id        " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'   " +
            "group by date) as date , (select 'A' as day_or_night union select 'B' as day_ornight) as day_ornight " +
            "where (con.process like #{process} and con.project like #{project} )) ng_res_date   " +
            "left join (select f_sort, count(*) ng_num, DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date, if(HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night " +
            "from df_qms_ipqc_waig_total_check tol       " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id       " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'  " +
            "group by f_sort,date, day_or_night) num_data on num_data.f_sort = ng_res_date.flaw_name and num_data.date = ng_res_date.date and num_data.day_or_night = ng_res_date.day_or_night) little_num       " +
            "left join (select  DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date,  " +
            " if(HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night,  " +
            " sum(spot_check_count) all_num  " +
            "from df_qms_ipqc_waig_total_check tol " +
            "where tol.f_bigpro like #{project} and tol.f_seq like #{process} and f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date, day_or_night) all_num on little_num.date = all_num.date and little_num.day_or_night = all_num.day_or_night " +
            "group by little_num.flaw_name, little_num.date, little_num.day_or_night) t2 on t.name = t2.name order by date, t.ran")
        // 分白班晚班
    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project, String process, String startTime, String endTime);  // 根据NGTop排序

    @Select("select t2.*   " +
            "from       " +
            "(select name, sum(rate) rate, row_number() over(order by sum(rate) desc) ran from   " +
            "(select flaw_name name,  all_num.f_seq ,avg(if(ng_num is null, 0, ng_num) / all_num) rate from        " +
            "(select * from (select *        " +
            "from df_qms_ipqc_flaw_config        " +
            "where process like #{process} and project like #{project}) item       " +
            "left join        " +
            "(select f_sort,f_seq, count(*) ng_num from       " +
            "df_qms_ipqc_waig_detail_check det       " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id " +
            "join df_process dp ON dp.process_name = tol.f_seq " +
            "where dp.floor like #{floor} and tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'   " +
            "group by f_sort, f_seq) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng left join   " +
            "(select f_seq,sum(spot_check_count) all_num       " +
            "from df_qms_ipqc_waig_total_check       " +
            "join df_process dp ON dp.process_name =f_seq " +
            "where dp.floor like #{floor} and f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq   " +
            "group by name, f_seq       " +
            "order by rate desc       " +
            ") t    " +
            "group by name   " +
            "order by rate desc   " +
            "limit 10) t   " +
            "left join    " +
            "(select little_num.flaw_name name, little_num.date date,little_num.day_or_night, avg(if(ng_num is null or all_num is null, 0, ng_num / all_num)) * 100 as rate, if(avg(ng_num) is null, 0, avg(ng_num)) ng_num, avg(all_num) all_num from       " +
            "(select ng_res_date.*, num_data.ng_num from       " +
            "(select * from df_qms_ipqc_flaw_config as con,(select DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date " +
            "from df_qms_ipqc_waig_total_check tol " +
            "join df_process dp ON dp.process_name = tol.f_seq " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id        " +
            "where dp.floor like #{floor} and tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'   " +
            "group by date) as date , (select 'A' as day_or_night union select 'B' as day_ornight) as day_ornight " +
            "where (con.process like #{process} and con.project like #{project} )) ng_res_date   " +
            "left join (select f_sort, count(*) ng_num, DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date, if(HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night " +
            "from df_qms_ipqc_waig_total_check tol       " +
            "join df_process dp ON dp.process_name = tol.f_seq " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id       " +
            "where dp.floor like #{floor} and tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime}  and det.f_sort != '破片'  " +
            "group by f_sort,date, day_or_night) num_data on num_data.f_sort = ng_res_date.flaw_name and num_data.date = ng_res_date.date and num_data.day_or_night = ng_res_date.day_or_night) little_num       " +
            "left join (select  DATE_FORMAT(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date,  " +
            " if(HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night,  " +
            " sum(spot_check_count) all_num  " +
            "from df_qms_ipqc_waig_total_check tol " +
            "join df_process dp ON dp.process_name = tol.f_seq " +
            "where dp.floor like #{floor} and tol.f_bigpro like #{project} and tol.f_seq like #{process} and f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} " +
            "group by date, day_or_night) all_num on little_num.date = all_num.date and little_num.day_or_night = all_num.day_or_night " +
            "group by little_num.flaw_name, little_num.date, little_num.day_or_night) t2 on t.name = t2.name order by date, t.ran")
        // 分白班晚班
    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project, String floor, String process, String startTime, String endTime);  // 根据NGTop排序

    /*@Select("select date_format(DATE_SUB(f_time, INTERVAL 7 HOUR), '%m-%d') date, sum(spot_check_count - affect_count) / sum(spot_check_count) rate    " +
            "from df_qms_ipqc_waig_total_check    " +
            "${ew.customSqlSegment} " +
            "group by date " +
            "order by date ")*/  // 去掉破片前
    @Select("select date_format(date, '%m-%d') date ,rate from(select date_format(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%y-%m-%d') date, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate,sum(spot_check_count) allNum    " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id   " +
            "${ew.customSqlSegment} " +
            "group by date " +
            "order by date)t ")
    // 去掉破片后
    List<Rate> listOkRateNear15Day(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select t.*, clr.color 'date' " +
            "from " +
            "(select flaw_name name, avg(if(ng_num is null, 0, ng_num) / all_num) rate from  " +
            "(select * from (select *  " +
            "from df_qms_ipqc_flaw_config  " +
            "where process like #{process} and project like #{project}) item " +
            "left join  " +
            "(select f_sort, count(*) ng_num from " +
            "df_qms_ipqc_waig_detail_check det " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id " +
            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} " +
            "group by f_sort) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng,  " +
            "(select sum(spot_check_count) all_num " +
            "from df_qms_ipqc_waig_total_check " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime}) all_num " +
            "group by name " +
            "order by rate desc " +
            "limit 10) t " +
            "left join df_qms_ipqc_flaw_color clr on t.name = clr.name ")
    List<Rate> listNgTop10(String lineBody, String project, String process, String startTime, String endTime);

    @Select(" select t.*, clr.color 'date'     " +
            "from    " +
            "(select name, sum(rate) rate from " +
            "(select flaw_name name,  all_num.f_seq ,avg(if(ng_num is null, 0, ng_num) / all_num) rate from      " +
            "(select * from (select *      " +
            "from df_qms_ipqc_flaw_config      " +
            "where process like #{process} and project like #{project}) item     " +
            "left join      " +
            "(select f_sort,f_seq, count(*) ng_num from     " +
            "df_qms_ipqc_waig_detail_check det     " +
            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id     " +
            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} " +
            "group by f_sort, f_seq) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng left join " +
            "(select f_seq,sum(spot_check_count) all_num     " +
            "from df_qms_ipqc_waig_total_check     " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq " +
            "group by name, f_seq     " +
            "order by rate desc     " +
            ") t  " +
            "group by name " +
            "order by rate desc " +
            "limit 10) t " +
            "left join df_qms_ipqc_flaw_color clr on t.name = clr.name  ")
    List<Rate> listNgTop102(String lineBody, String project, String process, String startTime, String endTime);

//    @Select(" select t.*, clr.color 'date'     " +
//            "from    " +
//            "(select name, sum(rate) rate from " +
//            "(select flaw_name name,  all_num.f_seq ,avg(if(ng_num is null, 0, ng_num) / all_num) rate from      " +
//            "(select * from (select *      " +
//            "from df_qms_ipqc_flaw_config      " +
//            "where process like #{process} and project like #{project}) item     " +
//            "left join      " +
//            "(select f_sort,f_seq, count(*) ng_num from     " +
//            "df_qms_ipqc_waig_detail_check det     " +
//            "left join df_qms_ipqc_waig_total_check tol on tol.id = det.f_parent_id " +
//            "where tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and det.f_sort != '破片' " +
//            "group by f_sort, f_seq) ng_num on ng_num.f_sort = item.flaw_name) item_res_ng left join " +
//            "(select f_seq,sum(spot_check_count) all_num     " +
//            "from df_qms_ipqc_waig_total_check     " +
//            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq " +
//            "group by name, f_seq     " +
//            "order by rate desc     " +
//            ") t  " +
//            "group by name " +
//            "order by rate desc " +
//            "limit 10) t " +
//            "left join df_qms_ipqc_flaw_color clr on t.name = clr.name  ")  // 加上早晚班判断

    @Select("select t.*, clr.color 'date'     \n" +
            "                        from    \n" +
            "                        (select name, (ng_num / all_num) rate,ng_num,all_num from \n" +
            "   \n" +
            "                        (select f_sort name, sum(ng_num) ng_num , sum(all_num) all_num from      \n" +
            "             \n" +
            "                        ( SELECT  \n" +
            "                         det.f_sort  , tol.f_seq ,count(det.f_sort) as ng_num  \n" +
            "                       \n" +
            "                         FROM \n" +
            "                         df_qms_ipqc_waig_detail_check det  \n" +
            "                         LEFT JOIN df_qms_ipqc_waig_total_check tol ON tol.id = det.f_parent_id \n" +
            "                         JOIN df_process dp ON dp.process_name = tol.f_seq    " +
            "where dp.floor like #{floor} and tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and det.f_sort != '破片' " +
            "   GROUP BY det.f_sort, tol.f_seq) \n" +
            "            \n" +
            "           item_res_ng left join \n" +
            "                        (select f_seq,sum(spot_check_count) all_num     \n" +
            "                        from df_qms_ipqc_waig_total_check     " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "  group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq \n" +
            "                        group by name     \n" +
            "                        ) t  \n" +
            "                        group by name \n" +
            "                        order by rate desc \n" +
            "                        limit 10) t \n" +
            "                        left join df_qms_ipqc_flaw_color clr on t.name = clr.name   ")
        // 加上早晚班判断 //5楼只查5楼数据
    List<Rate> listNgTop103(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);



    @Select("select t.*, clr.color 'date'     \n" +
            "                        from    \n" +
            "                        (select name, (ng_num / all_num) rate,ng_num,all_num from \n" +
            "   \n" +
            "                        (select f_sort name, sum(ng_num) ng_num , sum(all_num) all_num from      \n" +
            "             \n" +
            "                        ( SELECT  \n" +
            "                         det.f_sort  , tol.f_seq ,count(det.f_sort) as ng_num  \n" +
            "                       \n" +
            "                         FROM \n" +
            "                         df_qms_ipqc_waig_detail_check det  \n" +
            "                         LEFT JOIN df_qms_ipqc_waig_total_check tol ON tol.id = det.f_parent_id \n" +
            "                         JOIN df_process dp ON dp.process_name = tol.f_seq    " +
            "where dp.floor like #{floor} and tol.f_seq like #{process} and f_sort like #{fsort} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and det.f_sort != '破片' " +
            "   GROUP BY det.f_sort, tol.f_seq) \n" +
            "            \n" +
            "           item_res_ng left join \n" +
            "                        (select f_seq,sum(spot_check_count) all_num     \n" +
            "                        from df_qms_ipqc_waig_total_check     " +
            "where f_seq like #{process} and f_bigpro like #{project}  and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "  group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq \n" +
            "                        group by name     \n" +
            "                        ) t  \n" +
            "                        group by name \n" +
            "                        order by rate desc \n" +
            "                        limit 10) t \n" +
            "                        left join df_qms_ipqc_flaw_color clr on t.name = clr.name   ")
        // 加上早晚班判断 //5楼只查5楼数据
    List<Rate> WaiguanlistNgTop10(String lineBody, String project, String floor, String process,String fsort, String startTime, String endTime, Integer startHour, Integer endHour);




    /*@Select("select pro_res_rate.*, @total := @total * ok_rate as rate from " +
            "(select process_name name, sort, if(ok_rate is null, 1, ok_rate) ok_rate from  " +
            "(select process_name, sort " +
            "from df_process " +
            "where sort is not null and process_name != 'SSB' ) pro " +
            "left join  " +
            "(select f_seq, ok_rate from " +
            "(select f_seq, sum(spot_check_count - affect_count) / sum(spot_check_count) ok_rate " +
            "from df_qms_ipqc_waig_total_check tol " +
            "${ew.customSqlSegment} " +
            "group by f_seq) t) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.f_seq " +
            "order by sort) pro_res_rate, (select @total := 1) as temp")*/  // 不要破片前
    @Select("select pro_res_rate.*, @total := @total * ok_rate as rate from " +
            "(select process_name name, sort, if(ok_rate is null, 1, ok_rate) ok_rate from  " +
            "(select process_name, sort " +
            "from df_process " +
            "where sort is not null and process_name != 'SSB' and floor = '4F' ) pro " +
            "left join  " +
            "(select f_seq, ok_rate from " +
            "(select f_seq, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) ok_rate  " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id " +
            "${ew.customSqlSegment} " +
            "group by f_seq) t) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.f_seq " +
            "order by sort) pro_res_rate, (select @total := 1) as temp")
    // 不要破片后
    List<Rate> listAlwaysOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select f_mac, f_bigpro, f_fac, f_seq, f_line  " +
            "from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} " +
            "group by f_mac, f_bigpro, f_fac, f_seq, f_line")
    List<DfQmsIpqcWaigTotalCheck> listMachineCode(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select f_fac, sum(if(status = 'OK', 1, 0)) id  " +
            "from df_qms_ipqc_waig_total_check " +
            "where (f_mac, f_fac, f_time) in  " +
            "(select f_mac, f_fac, min(f_time) f_time " +
            "from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} " +
            "group by f_mac, f_fac)" +
            "group by f_fac")
    List<DfQmsIpqcWaigTotalCheck> getFaiPassRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select process_name name, f_sort date, ng_num / all_num rate  " +
            "from (select process_name, sort, f_sort, ng_num from df_process pro " +
            "left join (select f_sort, f_seq, sum(affect_count) ng_num " +
            "from df_qms_ipqc_waig_detail_check det " +
            "left join df_qms_ipqc_waig_total_check tol on det.f_parent_id = tol.id " +
            "${ew.customSqlSegment} " +
            "GROUP BY f_sort, f_seq ) ng_data on ng_data.f_seq = pro.process_name and sort is not null) t_ng " +
            "left join (select f_fac, f_bigpro, f_line, f_seq , sum(spot_check_count) all_num " +
            "from df_qms_ipqc_waig_total_check tol  " +
            "${ew.customSqlSegment} " +
            "GROUP BY f_fac, f_bigpro, f_line, f_seq ) t_all on t_ng.process_name = t_all.f_seq " +
            "order by t_ng.sort")
    List<Rate> listAllProcessItemNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select pro_res_rate.*, @total := @total * ok_rate as always_ok_rate from  " +
            "(select process_name process, " +
            "if(all_num is null, 0, all_num) all_num, " +
            "if(ok_num is null, 0, ok_num) ok_num, " +
            "if(ng_num is null, 0, ng_num) ng_num, " +
            "if(ok_rate is null, 1, ok_rate) ok_rate from  " +
            "(select process_name, sort     " +
            "from df_process     " +
            "where sort is not null) pro  " +
            "left join  " +
            "(select *, ok_num/all_num ok_rate from  " +
            "(select f_seq, sum(spot_check_count) all_num, sum(spot_check_count - affect_count) ok_num, sum(affect_count) ng_num " +
            "from df_qms_ipqc_waig_total_check tol " +
            "${ew.customSqlSegment} " +
            "group by f_seq) t) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.f_seq " +
            "order by sort) pro_res_rate, (select @total := 1) as temp ")
    List<ProcessResRate> listProcessAlwaysNumRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    /*@Select("select pro.process_name name, if(ok_num/all_num is null, 1, ok_num/all_num) rate " +
            "from df_process pro left join " +
            "(select f_seq, sum(spot_check_count) all_num, sum(spot_check_count - affect_count) ok_num " +
            "from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} " +
            "group by f_seq) pro_res_num  on pro.process_name = pro_res_num.f_seq " +
            "where sort is not null " +
            "order by sort ")*/
    @Select("select pro.process_name name, if(ok_num/all_num is null, '', ok_num/all_num) rate " +
            "from df_process pro left join " +
            "(select f_seq, sum(spot_check_count) all_num, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) ok_num " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id " +
            "${ew.customSqlSegment} " +
            "group by f_seq) pro_res_num  on pro.process_name = pro_res_num.f_seq " +
            "where sort is not null and pro.floor like #{floor} and pro.project like #{project}  " +
            "order by sort ")
    // 不要破片后  加上楼层
    List<Rate> listAllProcessOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper, String floor,String project);

    @Select("select sum(spot_check_count - affect_count) / sum(spot_check_count) rate " +
            "from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} ")
    Rate getAppearRealOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select sum(spot_check_count) spot_check_count,sum(affect_count) affect_count " +
            "from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} ")
    DfQmsIpqcWaigTotalCheck getTotalAndNgCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);


//    @Select("select all_num.f_seq name, all_num.all_num, ng_item.ng_num ng_num, ng_item.ng_num/all_num.all_num rate from " +
//            "(select det.f_sort, tol.f_seq, count(*) ng_num " +
//            "from df_qms_ipqc_waig_detail_check det " +
//            "left join df_qms_ipqc_waig_total_check tol on det.f_parent_id = tol.id " +
//            "where det.f_sort = #{ngItem} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
//            "group by det.f_sort, tol.f_seq) ng_item " +
//            "left join  " +
//            "(select f_seq, sum(spot_check_count) all_num  " +
//            "from df_qms_ipqc_waig_total_check " +
//            "where f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
//            "group by f_seq) all_num on ng_item.f_seq = all_num.f_seq order by rate desc")

    //新增只查询5楼工序需求
    @Select("SELECT  " +
            " all_num.f_seq NAME,  " +
            " all_num.all_num,  " +
            " ng_item.ng_num ng_num,  " +
            " ng_item.ng_num / all_num.all_num rate   " +
            "FROM  " +
            " (  " +
            " SELECT  " +
            "  f_sort,  " +
            "  f_seq,  " +
            "   " +
            "  count(*) ng_num   " +
            " FROM  " +
            " (select  det.f_sort,  " +
            "  tol.f_seq,  " +
            "  det.f_product_id,det.f_parent_id from   " +
            "  df_qms_ipqc_waig_detail_check det  " +
            "  LEFT JOIN df_qms_ipqc_waig_total_check tol ON det.f_parent_id = tol.id  " +
            "  JOIN df_process dp ON dp.process_name = tol.f_seq " +
            "where dp.floor like #{floor} and det.f_sort = #{ngItem} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "   ) ttt  " +
            " GROUP BY  " +
            " f_sort,  " +
            " f_seq   " +
            " ) ng_item  " +
            " LEFT JOIN ( SELECT f_seq, sum( spot_check_count ) all_num FROM df_qms_ipqc_waig_total_check " +
            " where f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "   " +
            "  GROUP BY f_seq ) all_num ON ng_item.f_seq = all_num.f_seq   " +
            "ORDER BY  " +
            " rate DESC")
    List<Rate> listProcessNgRateByNgItem(String lineBody, String project, String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    //    @Select("select all_num.f_mac name, all_num.all_num, ng_item.ng_num ng_num, ng_item.ng_num/all_num.all_num rate from " +
//            "(select det.f_sort, tol.f_mac, count(*) ng_num " +
//            "from df_qms_ipqc_waig_detail_check det " +
//            "left join df_qms_ipqc_waig_total_check tol on det.f_parent_id = tol.id " +
//            "where det.f_sort = #{ngItem} and tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
//            "group by det.f_sort, tol.f_mac) ng_item " +
//            "left join  " +
//            "(select f_mac, sum(spot_check_count) all_num  " +
//            "from df_qms_ipqc_waig_total_check " +
//            "where f_bigpro like #{project} and f_seq like #{process} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
//            "group by f_mac) all_num on ng_item.f_mac = all_num.f_mac order by rate desc ")
    @Select("select all_num.f_mac name, all_num.all_num, ng_item.ng_num ng_num, ng_item.ng_num/all_num.all_num rate from " +
            "(select  f_sort,  f_mac, count(*) ng_num " +
            "from (select   det.f_sort,   " +
            "    tol.f_mac,   " +
            "    det.f_product_id,det.f_parent_id from  df_qms_ipqc_waig_detail_check det " +
            "left join df_qms_ipqc_waig_total_check tol on det.f_parent_id = tol.id " +
            "join df_process dp ON dp.process_name = tol.f_seq " +
            "where dp.floor like #{floor} and det.f_sort = #{ngItem} and tol.f_bigpro like #{project} and tol.f_seq like #{process} and tol.f_line like #{lineBody} and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "    ) ttt group by  f_sort, f_mac) ng_item " +
            "left join  " +
            "(select f_mac, sum(spot_check_count) all_num  " +
            "from df_qms_ipqc_waig_total_check " +
            "where f_bigpro like #{project} and f_seq like #{process} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "group by f_mac) all_num on ng_item.f_mac = all_num.f_mac order by rate desc ")
//只查5楼
    List<Rate> listMacNgRateByNgItemAndProcess(String process, String lineBody, String project, String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    @Select("select * from  " +
            " (select process_name name, f_sort date, ng_num / all_num rate, row_number() over(partition by process_name order by ng_num / all_num desc) ran " +
            "from (select process_name, sort, f_sort, ng_num from df_process pro     " +
            "left join (select f_sort, f_seq, sum(affect_count) ng_num     " +
            "from df_qms_ipqc_waig_detail_check det     " +
            "left join df_qms_ipqc_waig_total_check tol on det.f_parent_id = tol.id     " +
            "${ew.customSqlSegment} " +
            "GROUP BY f_sort, f_seq ) ng_data on ng_data.f_seq = pro.process_name and sort is not null) t_ng     " +
            "left join (select f_fac, f_bigpro, f_line, f_seq , sum(spot_check_count) all_num     " +
            "from df_qms_ipqc_waig_total_check tol      " +
            "${ew.customSqlSegment} " +
            "GROUP BY f_fac, f_bigpro, f_line, f_seq ) t_all on t_ng.process_name = t_all.f_seq     " +
            "order by t_ng.sort ) t where ran <= 5")
    List<Rate> listAllProcessItemNgRateTop5(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("select f_seq name, sum(spot_check_count - affect_count) / sum(spot_check_count) rate from df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} " +
            "group by f_seq")
    List<Rate> listAllProcessOkRate2(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("SELECT SUM(spot_check_count)  as  spot_check_count ,sum(affect_count) as  affect_count FROM df_qms_ipqc_waig_total_check " +
            "${ew.customSqlSegment} ")
    DfQmsIpqcWaigTotalCheck getProcessYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);


    @Select("select t1.*, t2.rate from ( " +
            "select * from ( " +
            "select DATE_FORMAT(DATE_SUB(f_time,INTERVAL 7 HOUR),'%m-%d') date from df_qms_ipqc_waig_total_check tol " +
            "${ew.customSqlSegment} ) t, " +
            "(select 'A' day_or_night union select 'B' day_or_night) day_or_night " +
            "group by date, day_or_night) t1 " +
            "left join  " +
            "(select DATE_FORMAT(DATE_SUB(f_time,INTERVAL 7 HOUR),'%m-%d') date, if(hour(DATE_SUB(f_time,INTERVAL 7 HOUR)) > 11, 'B', 'A') day_or_night, sum(affect_count) / sum(spot_check_count) rate from df_qms_ipqc_waig_total_check tol " +
            "${ew.customSqlSegment}  " +
            "GROUP BY date, day_or_night) t2 on t1.date = t2.date and t1.day_or_night = t2.day_or_night " +
            "ORDER BY date")
    List<Rate> listDateNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper); // 机台每日良率

    @Select({"<script> " +
            "SELECT sort,dp.process_name process,COALESCE(result,1) result\n" +
            "FROM\n" +
            "df_process dp\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "\t\tselect f_seq,SUM(spot_check_count - affect_count)/SUM(spot_check_count) result\n" +
            "\t\tFROM df_qms_ipqc_waig_total_check\n" +
            "\t\twhere f_time BETWEEN #{startTime} AND #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND f_seq = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND f_bigpro = #{item} " +
            "</if>" +
            "\t\tGROUP BY f_seq\n" +
            ")t1\n" +
            "on dp.process_name = t1.f_seq\n" +
            "where dp.sort is not null\n" +
            "ORDER BY sort" +
            "</script>"})
    List<DfSizeCheckItemInfos> detailPageApparenceYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime);


    @Select({"<script> " +
            "select dp.process_name name, item_name date, ng_rate rate\n" +
            "FROM\n" +
            "(\n" +
            "\tSELECT process,item_name,ng_rate\n" +
            "\tFROM\n" +
            "\t(\n" +
            "\t\t\tSELECT * ,row_number() over(partition by process order by ng_rate desc) row_num \n" +
            "\t\t\tFROM\n" +
            "\t\t\t(\n" +
            "\t\t\t\tSELECT t1.process,item_name,ng_num,size_total,COALESCE(waig_total,0) waig_total,ng_num / GREATEST(size_total,waig_total) ng_rate\n" +
            "\t\t\t\tFROM\n" +
            "\t\t\t\t(\n" +
            "\t\t\t\t\tselect dqiwt.f_seq process ,t.f_sort item_name , sum(IF(t.f_result = 'fail',1,0)) ng_num\n" +
            "\t\t\t\t\tfrom \n" +
            "\t\t\t\t\t(\n" +
            "\t\t\t\t\t\tselect f_parent_id,f_sort,f_result\n" +
            "\t\t\t\t\t\tfrom df_qms_ipqc_waig_detail_check dqiwd\n" +
            "\t\t\t\t\t\twhere dqiwd.f_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t\t\t\t\t) t\n" +
            "\t\t\t\t\tJOIN df_qms_ipqc_waig_total_check dqiwt\n" +
            "\t\t\t\t\ton dqiwt.id = t.f_parent_id\n" +
            "where 1=1 " +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND dqiwt.f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND dqiwt.f_seq = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND dqiwt.f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND dqiwt.f_bigpro = #{item} " +
            "</if>" +
            "\t\t\t\t\tGROUP BY dqiwt.f_seq,t.f_sort\n" +
            "\t\t\t\t)t1\n" +
            "\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t(\n" +
            "\t\t\t\t\tSELECT f_seq process , sum(spot_check_count) waig_total\n" +
            "\t\t\t\t\tFROM df_qms_ipqc_waig_total_check\n" +
            "\t\t\t\t\twhere f_time BETWEEN #{startTime} AND #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND f_seq = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND f_bigpro = #{item} " +
            "</if>" +
            "\t\t\t\t\tGROUP BY f_seq\n" +
            "\t\t\t\t)t\n" +
            "\t\t\t\ton t1.process = t.process\n" +
            "\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t(\n" +
            "\t\t\t\t\tSELECT process,count(*) size_total\n" +
            "\t\t\t\t\tFROM\n" +
            "\t\t\t\t\t(\n" +
            "\t\t\t\t\t\tselect check_id\n" +
            "\t\t\t\t\t\tfrom df_size_check_item_infos dscii\n" +
            "\t\t\t\t\t\twhere dscii.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t\t\t\t\t)t\n" +
            "\t\t\t\t\tJOIN df_size_detail dsd on dsd.ID = t.check_id\n" +
            "\t\t\t\t\tGROUP BY process\n" +
            "\t\t\t\t)t2\n" +
            "\t\t\t\tON t1.process = t2.process\n" +
            "\t\t\t\tORDER BY ng_rate desc\n" +
            "\t\t\t)t\n" +
            "\t)t\n" +
            "\twhere row_num &lt;= 5\n" +
            ")data\n" +
            "RIGHT JOIN df_process dp\n" +
            "ON data.process = dp.process_name\n" +
            "where dp.process_name is not null\n" +
            "</script>"})
    List<Rate> listAllProcessItemNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select("select * from \n" +
            "(select process_name process, f_sort item_name, app_all_ok_num / all_num ok_rate, app_item_ng_num / all_num item_ng_rate, row_number() over(partition by process_name order by app_item_ng_num / all_num desc) ran, t.sort from \n" +
            "(select pro.process_name, if(app.all_num > size.all_num, app.all_num, size.all_num) all_num, pro.sort from df_process pro\n" +
            "left join (select f_seq, sum(spot_check_count) all_num from df_qms_ipqc_waig_total_check tol where f_time between #{startTime} and #{endTime} group by f_seq) app on pro.process_name = app.f_seq\n" +
            "left join (select process, count(*) all_num from df_size_detail where create_time BETWEEN #{startTime} and #{endTime} group by process) size on pro.process_name = size.process) t\n" +
            "\n" +
            "left join \n" +
            "(select f_seq, sum(spot_check_count - affect_count) app_all_ok_num from df_qms_ipqc_waig_total_check \n" +
            "where f_time between #{startTime} and #{endTime}\n" +
            "group by f_seq) t3 on t.process_name = t3.f_seq\n" +
            "\n" +
            "left join \n" +
            "(select tol.f_seq, item.f_sort, sum(tol.affect_count) app_item_ng_num from df_qms_ipqc_waig_detail_check item \n" +
            "left join df_qms_ipqc_waig_total_check tol on item.f_parent_id = tol.id\n" +
            "where tol.f_time between #{startTime} and #{endTime}\n" +
            "group by tol.f_seq, item.f_sort) t4 on t.process_name = t4.f_seq) t\n" +
            "where ran <= 5\n" +
            "order by t.sort, ran")
    List<Rate2> listAllProcessItemNgRateTop5V3(String startTime, String endTime);

    @Select("select week(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') date, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate    " +
            "from df_qms_ipqc_waig_total_check tol " +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id   " +
            "${ew.customSqlSegment} " +
            "group by date " +
            "order by date ")
        // 去掉破片后
    List<Rate> listCNC0OkRateOneMonth(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcWaigTotalCheck> qw);


    @Select({"<script> " +
            "with temp_lead as(\n" +
            "\tselect week(DATE_SUB(check_time,INTERVAL 7 HOUR),'%m-%d') week,sum(IF(result = 'OK',1,0)) / count(*) rate_lead\n" +
            "\tfrom df_lead_detail\n" +
            "\twhere check_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\tGROUP BY week\n" +
            "),\n" +
            "temp_jq1 as(\n" +
            "\tselect week(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') week, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate_jq1\n" +
            "\tfrom df_qms_ipqc_waig_total_check tol \n" +
            "\tleft join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id\n" +
            "\twhere tol.f_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\tAND tol.f_seq = #{process}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'project != null and project != &quot;&quot;'>" +
            "AND f_bigpro = #{project} " +
            "</if>" +
            "\tgroup by week\n" +
            "),\n" +
            "temp_process as(\n" +
            "\tselect week(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') week, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate_process\n" +
            "\tfrom df_qms_ipqc_waig_total_check tol \n" +
            "\tleft join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id\n" +
            "\twhere tol.f_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\tAND tol.f_seq = #{process}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'project != null and project != &quot;&quot;'>" +
            "AND f_bigpro = #{project} " +
            "</if>" +
            "\tgroup by week\n" +
            "),\n" +
            "temp_week as(\n" +
            "\tselect DISTINCT WEEK(datelist,1) week FROM \n" +
            "\tcalendar\n" +
            "\tWHERE datelist BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            "\n" +
            "select temp_week.week str1,rate_lead dou1,rate_jq1 dou2,rate_process dou3\n" +
            "FROM temp_week\n" +
            "LEFT JOIN temp_lead ON temp_week.week = temp_lead.week\n" +
            "LEFT JOIN temp_jq1 ON temp_week.week = temp_jq1.week\n" +
            "LEFT JOIN temp_process ON temp_week.week = temp_process.week " +
            "</script>"})
    List<Rate3> listProcessOkRateOneMonth(
            @Param("factoryId") String factoryId
            , @Param("project") String project
            , @Param("startTime") String startTime
            , @Param("endTime") String endTime
            , @Param("process") String process);

    @Select("with temp_jq1 as(\n" +
            "\tselect week(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') week, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate_jq1\n" +
            "\tfrom df_qms_ipqc_waig_total_check tol \n" +
            "\tleft join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id\n" +
            "${ew.customSqlSegment}" +
            "\tgroup by week\n" +
            "),\n" +
            "temp_process as(\n" +
            "\tselect week(DATE_SUB(tol.f_time, INTERVAL 7 HOUR), '%m-%d') week, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) rate_process\n" +
            "\tfrom df_qms_ipqc_waig_total_check tol \n" +
            "\tleft join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id\n" +
            "${ew_2.customSqlSegment}" +
            "\tgroup by week\n" +
            ")" +
            "select * " +
            "from temp_jq1 " +
            "left join temp_process on temp_jq1.week = temp_process.week")
    List<Map<String, Object>> test(@Param(Constants.WRAPPER) QueryWrapper<DfCarveUniversal> qw1,
                                   @Param("ew_2") QueryWrapper<DfCarveUniversal> qw2);


    @Select("select dd.rate from (\n" +
            "select pro_res_rate.*, @total := @total * ok_rate as rate from \n" +
            "(select process_name name, sort, if(ok_rate is null or ok_rate=0, 1, ok_rate) ok_rate from  \n" +
            "(select process_name, sort \n" +
            "from df_process \n" +
            "where sort is not null and process_name != 'SSB' ) pro \n" +
            "left join  \n" +
            "(select f_seq, ok_rate from \n" +
            "(select f_seq, sum(spot_check_count - if(det.f_sort != '破片',affect_count,0)) / sum(spot_check_count) ok_rate  \n" +
            "from df_qms_ipqc_waig_total_check tol \n" +
            "left join df_qms_ipqc_waig_detail_check det on tol.id = det.f_parent_id \n" +
            "${ew.customSqlSegment}" +
            "group by f_seq) t) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.f_seq \n" +
            "order by sort) pro_res_rate, (select @total := 1) as temp) dd order by rate asc limit 1")
    Rate getAllYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("SELECT (sum(spot_check_count-affect_count)/sum(spot_check_count)*100) as rate FROM `df_qms_ipqc_waig_total_check` wg join df_machine mac on wg.f_mac=mac.code ${ew.customSqlSegment} ")
    Rate getLineYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    @Select("with temp_date_machine as (\n" +
            "\tselect DATE_FORMAT(DATE_SUB(F_TIME,INTERVAL 7 HOUR),'%m-%d') date,F_MAC,sum(if(STATUS='NG',1,0))/COUNT(*) rate\n" +
            "\tFROM DF_QMS_IPQC_WAIG_TOTAL_CHECK total\n" +
            "\twhere F_TIME between date_sub(now(),INTERVAL 7 DAY ) and now()\n" +
            "\t    and weekday(DATE_SUB(F_TIME,INTERVAL 7 HOUR)) != 6\n" +
            "\tGROUP BY date,F_MAC\n" +
            ")\n" +
            ",temp_rn as(\n" +
            "\tselect * ,row_number() OVER (PARTITION BY date ORDER BY rate DESC ) as no\n" +
            "\tfrom temp_date_machine\n" +
            ")\n" +
            "select F_MAC\n" +
            "from temp_rn\n" +
            "where no <= 3\n" +
            "GROUP BY F_MAC\n" +
            "HAVING count(*) >=6;")
    List<DfQmsIpqcWaigTotalCheck> weekOfPoorTOP3Warning(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);


    @Select("with waig_defect as(\n" +
            "\tselect \n" +
            "\tdqiwt.f_seq process\n" +
            "\t,dqiwd.f_sort defectName\n" +
            "\t,date_format(date_sub(dqiwd.f_time,interval 7 hour),'%Y-%m-%d') checkTime\n" +
            "\t,sum(if(dqiwd.f_result='fail',1,0)) ngNumber\n" +
            "\tfrom df_qms_ipqc_waig_detail_check dqiwd \n" +
            "\tinner join df_qms_ipqc_waig_total_check dqiwt\n" +
            "\ton dqiwt.id = dqiwd.f_parent_id \n" +
            "\twhere dqiwd.f_time between date_sub(now(),interval 14 day) and now()\n" +
            "\tand weekday(date_sub(dqiwd.f_time,interval 7 hour)) != 6\n" +
            "\tand dqiwt.f_seq is not null\n" +
            "\tand dqiwd.f_sort is not null\n" +
            "\tgroup by dqiwt.f_seq,dqiwd.f_sort,date_format(date_sub(dqiwd.f_time,interval 7 hour),'%Y-%m-%d') \n" +
            ")\n" +
            ",defect_top as(\n" +
            "\tselect *\n" +
            "\t,row_number()over(partition by checkTime order by ngNumber desc) num\n" +
            "\tfrom waig_defect\n" +
            ")\n" +
            "select \n" +
            "process str1\n" +
            ",defectName str2\n" +
            "from defect_top\n" +
            "where num<=3\n" +
            "group by process,defectName\n" +
            "having count(0)>=12 ")
    List<Rate3> twoWeekOfPoorTOP3Warning();


//    @Select("WITH temp_main as(\n" +
//            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,sum(spot_check_count) spot_check_count,\n" +
//            "       sum(spot_check_count - affect_count) as okNum,sum(spot_check_count - affect_count)/sum(spot_check_count) as okRate,\n" +
//            "       sum(SPOT_CHECK_COUNT) as ngNum,F_TEST_MAN ,f_user\n" +
//            "    from DF_QMS_IPQC_WAIG_TOTAL_CHECK total\n" +
//            "${ew.customSqlSegment} " +
//            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,F_TEST_MAN,F_USER\n" +
//            ")\n" +
//            ", temp_slave AS (\n" +
//            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,F_TEST_MAN,f_user,F_SORT,count(0) num\n" +
//            "    from DF_QMS_IPQC_WAIG_TOTAL_CHECK total\n" +
//            "    left join df_qms_ipqc_waig_detail_check detail ON total.ID = detail.F_PARENT_ID\n" +
//            "${ew.customSqlSegment} " +
//            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,F_TEST_MAN,F_USER,f_user,F_SORT\n" +
//            ")\n" +
//            "select main.date,main.F_BIGPRO,main.f_seq,main.F_FAC,main.F_COLOR,spot_check_count,okNum,okRate,ngNum,main.F_TEST_MAN,main.f_user,F_SORT,num from(\n" +
//            "    temp_main main\n" +
//            "    JOIN temp_slave slave\n" +
//            "    ON main.date = slave.date\n" +
//            "    AND main.F_BIGPRO = slave.F_BIGPRO\n" +
//            "    AND main.f_seq = slave.f_seq\n" +
//            "    AND main.F_FAC = slave.F_FAC\n" +
//            "    AND main.F_COLOR = slave.F_COLOR\n" +
//            "    AND main.F_TEST_MAN = slave.F_TEST_MAN\n" +
//            "    AND main.f_user = slave.F_USER\n" +
//            ")\n" +
//            "ORDER BY date DESC;")
//    List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);

/*    //优化
    @Select("WITH temp_main as(\n" +
            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,sum(spot_check_count) spot_check_count,\n" +
            "       sum(spot_check_count - affect_count) as okNum,sum(spot_check_count - affect_count)/sum(spot_check_count) as okRate,\n" +
            "       sum(affect_count) as ngNum,F_TEST_MAN \n" +
            "    from DF_QMS_IPQC_WAIG_TOTAL_CHECK total\n" +
            "   inner join df_process dp on dp.process_name = total.f_seq " +
            "   ${ew.customSqlSegment} " +
            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,F_TEST_MAN\n" +
            "    having spot_check_count is not null " +
            ")\n" +
            ", temp_slave AS (\n" +
            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,F_TEST_MAN,F_SORT,count(0) num\n" +
            "    from DF_QMS_IPQC_WAIG_TOTAL_CHECK total\n" +
            "   inner join df_process dp on dp.process_name = total.f_seq " +
            "    left join df_qms_ipqc_waig_detail_check detail ON total.ID = detail.F_PARENT_ID\n" +
            "   ${ew.customSqlSegment} " +
            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,F_TEST_MAN,F_SORT\n" +
//            "    having F_SORT is not null" +
            ")\n" +
            "select main.date,main.F_BIGPRO,main.f_seq,main.F_FAC,main.F_COLOR,main.f_test_category,spot_check_count,okNum,okRate,ngNum,main.F_TEST_MAN,F_SORT,num from(\n" +
            "    temp_main main\n" +
            "    JOIN temp_slave slave\n" +
            "    ON main.date = slave.date\n" +
            "    AND main.F_BIGPRO = slave.F_BIGPRO\n" +
            "    AND main.f_seq = slave.f_seq\n" +
            "    AND main.F_FAC = slave.F_FAC\n" +
            "    AND main.F_COLOR = slave.F_COLOR\n" +
            "    AND main.f_test_category = slave.f_test_category\n" +
            "    AND main.F_TEST_MAN = slave.F_TEST_MAN\n" +
            ")\n" +
            "ORDER BY date DESC;")
    List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);*/

    //优化
//    @Select("WITH temp_main as(  \n" +
//            "                   select total.confirmor,total.type,date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,total.f_time,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_Type,f_line,sum(spot_check_count) spot_check_count,  \n" +
//            "                    sum(spot_check_count - affect_count) as okNum,sum(spot_check_count - affect_count)/sum(spot_check_count) as okRate,  \n" +
//            "                    sum(affect_count) as ngNum,F_TEST_MAN,  \n" +
//            "                     CASE \n"+
//            "                    WHEN HOUR ( \n"+
//            "                    date_sub( total.F_TIME, INTERVAL 7 HOUR )) >= 7 \n"+
//            "                    AND HOUR ( \n"+
//            "                    date_sub( total.F_TIME, INTERVAL 7 HOUR )) < 19 THEN \n"+
//            "                    '白班' ELSE '晚班' \n"+
//            "                   END AS shift \n"+
//            "                   from DF_QMS_IPQC_WAIG_TOTAL_CHECK total  \n" +
//            "                  inner join df_process dp on dp.process_name = total.f_seq  \n" +
//            "                  ${ew.customSqlSegment}  \n" +
//            "                   GROUP BY total.confirmor,total.type,date,total.f_time,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_line,f_Type,F_TEST_MAN,shift  \n" +
//            "                   having spot_check_count is not null  \n" +
//            "               )  \n" +
//            "               , temp_slave AS (  \n" +
//            "                   select total.confirmor,total.type,date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,total.f_time,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,F_TEST_MAN,F_SORT,f_stage,f_test_type,f_mac,f_Type\n" +
//            "               ,f_line,count(0) num,  \n" +
//            "                   CASE \n"+
//            "                   WHEN HOUR ( \n"+
//            "                   date_sub( total.F_TIME, INTERVAL 7 HOUR )) >= 7 \n"+
//            "                   AND HOUR ( \n"+
//            "                   date_sub( total.F_TIME, INTERVAL 7 HOUR )) < 19 THEN \n"+
//            "                   '白班' ELSE '晚班' \n"+
//            "                   END AS shift \n"+
//            "                   from DF_QMS_IPQC_WAIG_TOTAL_CHECK total  \n" +
//            "                  inner join df_process dp on dp.process_name = total.f_seq  \n" +
//            "                   left join df_qms_ipqc_waig_detail_check detail ON total.ID = detail.F_PARENT_ID  \n" +
//            "                  ${ew.customSqlSegment}  \n" +
//            "                   GROUP BY total.confirmor,total.type,date,total.f_time,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_Type,f_line,\n" +
//            "                    F_TEST_MAN,F_SORT,shift  \n" +
//            "                   having F_SORT is not null \n" +
//            "               )  \n" +
//            "               select main.confirmor,main.type,main.date,main.f_time,main.F_BIGPRO,main.f_seq,main.F_FAC,main.F_COLOR,main.f_test_category,spot_check_count,okNum,okRate,ngNum,main.F_TEST_MAN,main.f_stage,main.f_test_type,main.f_mac,main.f_Type,main.f_line,F_SORT,num,main.shift from(  \n" +
//            "                   temp_main main  \n" +
//            "                   JOIN temp_slave slave  \n" +
//            "                   ON main.date = slave.date  \n" +
//            "                   AND main.F_BIGPRO = slave.F_BIGPRO  \n" +
//            "                   AND main.f_seq = slave.f_seq  \n" +
//            "                   AND main.F_FAC = slave.F_FAC  \n" +
//            "                   AND main.F_COLOR = slave.F_COLOR  \n" +
//            "                   AND main.f_test_category = slave.f_test_category  \n" +
//            "                   AND main.F_TEST_MAN = slave.F_TEST_MAN  \n" +
//            "                   and main.f_time = SLAVE.f_time \n"+
//            "                   and main.type = '抽检' "+
//            "               )  \n" +
//            "               ORDER BY date DESC;")
//    List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);

    //优化
/*    @Select("WITH temp_main as(  \n" +
            "                   select total.type,date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,total.f_time,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_Type,f_line,sum(spot_check_count) spot_check_count,  \n" +
            "                      sum(spot_check_count - affect_count) as okNum,sum(spot_check_count - affect_count)/sum(spot_check_count) as okRate,  \n" +
            "                      sum(affect_count) as ngNum,F_TEST_MAN,  \n" +
            "CASE \n" +
            "WHEN HOUR ( \n" +
            "date_sub( total.F_TIME, INTERVAL 7 HOUR )) >= 7 \n" +
            "AND HOUR ( \n" +
            "date_sub( total.F_TIME, INTERVAL 7 HOUR )) < 19 THEN \n" +
            "'白班' ELSE '晚班' \n" +
            "END AS shift \n" +
            "                   from DF_QMS_IPQC_WAIG_TOTAL_CHECK total  \n" +
            "                  inner join df_process dp on dp.process_name = total.f_seq  \n" +
            "                  ${ew.customSqlSegment}  \n" +
            "                   GROUP BY total.type,date,total.f_time,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_line,f_Type,F_TEST_MAN,shift  \n" +
            "                   having spot_check_count is not null  \n" +
            "               )  \n" +
            "               , temp_slave AS (  \n" +
            "                   select total.type,date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,total.f_time,F_BIGPRO,f_seq,F_FAC,F_COLOR,f_test_category,F_TEST_MAN,F_SORT,f_stage,f_test_type,f_mac,f_Type\n" +
            "\t\t\t\t\t\t\t\t\t ,f_line,count(0) num,  \n" +
            "CASE \n" +
            "WHEN HOUR ( \n" +
            "date_sub( total.F_TIME, INTERVAL 7 HOUR )) >= 7 \n" +
            "AND HOUR ( \n" +
            "date_sub( total.F_TIME, INTERVAL 7 HOUR )) < 19 THEN \n" +
            "'白班' ELSE '晚班' \n" +
            "END AS shift \n" +
            "                   from DF_QMS_IPQC_WAIG_TOTAL_CHECK total  \n" +
            "                  inner join df_process dp on dp.process_name = total.f_seq  \n" +
            "                   left join df_qms_ipqc_waig_detail_check detail ON total.ID = detail.F_PARENT_ID  \n" +
            "                  ${ew.customSqlSegment}  \n" +
            "                   GROUP BY total.type,date,total.f_time,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,f_test_category,f_stage,f_test_type,f_mac,f_Type,f_line,\n" +
            "\t\t\t\t\t\t\t\t\t F_TEST_MAN,F_SORT,shift  \n" +
            "                   having F_SORT is not null \n" +
            "               )  \n" +
            "               select main.type,main.date,main.f_time,main.F_BIGPRO,main.f_seq,main.F_FAC,main.F_COLOR,main.f_test_category,spot_check_count,okNum,okRate,ngNum,main.F_TEST_MAN,main.f_stage,main.f_test_type,main.f_mac,main.f_Type,main.f_line,F_SORT,num,main.shift from(  \n" +
            "                   temp_main main  \n" +
            "                   JOIN temp_slave slave  \n" +
            "                   ON main.date = slave.date  \n" +
            "                   AND main.F_BIGPRO = slave.F_BIGPRO  \n" +
            "                   AND main.f_seq = slave.f_seq  \n" +
            "                   AND main.F_FAC = slave.F_FAC  \n" +
            "                   AND main.F_COLOR = slave.F_COLOR  \n" +
            "                   AND main.f_test_category = slave.f_test_category  \n" +
            "                   AND main.F_TEST_MAN = slave.F_TEST_MAN  \n" +
            "                   and main.f_time = SLAVE.f_time \n" +
            "and main.type = '抽检' " +
            "               )  \n" +
            "               ORDER BY date DESC;")
    List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);*/

    @Select("SELECT COUNT(*) FROM DF_QMS_IPQC_WAIG_TOTAL_CHECK total ${ew.customSqlSegment}")
    int Count(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcWaigTotalCheck> qw);

    @Select("   SELECT total.type, date_format(total.F_TIME, '%Y-%m-%d') AS date, total.f_time,dp.sort AS sort, F_BIGPRO, f_seq, F_FAC, F_COLOR, f_test_category, F_TEST_MAN, F_SORT, f_stage, f_test_type, f_mac, f_Type, \n" +
            "          f_line, COUNT(0) AS num, \n" +
            "SUM(DISTINCT spot_check_count) AS spot_check_count," +
            "SUM(DISTINCT spot_check_count - affect_count) AS okNum," +
            "CASE \n" +
            "  WHEN SUM(DISTINCT spot_check_count) = 0 THEN 0 \n" +
            "  ELSE SUM(DISTINCT spot_check_count - affect_count) / SUM(DISTINCT spot_check_count)\n" +
            "END AS okRate," +
            "SUM(DISTINCT affect_count) AS ngNum," +
            "          CASE \n" +
            "            WHEN HOUR(total.F_TIME) >= 7 AND HOUR(total.F_TIME) < 19 THEN '白班' \n" +
            "            ELSE '晚班' \n" +
            "          END AS shift \n" +
            "   FROM (select * from DF_QMS_IPQC_WAIG_TOTAL_CHECK total  ${ew.customSqlSegment} LIMIT #{pageOffset}, #{pageSize}) total \n" +
            "   INNER JOIN (\n" +
            "    SELECT DISTINCT process_name,sort,project,floor \n" +
            "    FROM df_process where project like #{project} \n" +
            ") dp ON dp.process_name = total.f_seq  \n" +
            "LEFT JOIN df_qms_ipqc_waig_detail_CHECK detail ON total.ID = detail.F_PARENT_ID "+
            "   and dp.floor = #{floor} and dp.project like #{project} " +
            "   ${ew.customSqlSegment} \n" +
            "   GROUP BY total.type,dp.sort, date, total.f_time, total.F_BIGPRO, total.f_seq, total.F_FAC, total.F_COLOR, f_test_category, f_stage, f_test_type, f_mac, f_Type, f_line, F_TEST_MAN, F_SORT, shift \n" +
           // "   HAVING F_SORT IS NOT NULL and spot_check_count IS NOT NULL  \n" +
            "  HAVING total.type = '抽检' \n" +
            "ORDER BY date DESC \n")
    List<DfQmsIpqcWaigTotalCheck> listWaigExcelDataPage(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew,
                                                   @Param("pageOffset") int pageOffset,
                                                   @Param("pageSize") int pageSize,
                                                   @Param("floor") String floor,
                                                   @Param("project") String project);






    @Select("select *,ROUND( 1 - (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(f_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tf_bigpro as project,\n" +
            "\t'外观' as type,\n" +
            "\tf_seq as process,\n" +
            "\tsum(spot_check_count) as total,\n" +
//            "\tsum(if(affect_count = 'NG',1,0)) as ng\n" +
            "\tsum(affect_count ) as ng\n" +
            "\tfrom df_qms_ipqc_waig_total_check\n" +
            "${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportInspectionTableForProcess(@Param("ew") QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);

    @Select("select *,ROUND( 1 - (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(f_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tf_bigpro as project,\n" +
            "\t'外观' as type,\n" +
            "\tf_seq as process,\n" +
            "\tf_mac as machineCode,\n" +
            "\tsum(spot_check_count) as total,\n" +
            "\tsum(affect_count ) as ng\n" +
            "\tfrom df_qms_ipqc_waig_total_check\n" +
            "${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportInspectionTableForProcessBymachineId(@Param("ew") QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);

    @Select("select *,ROUND( (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(df_qms_ipqc_waig_detail_check.f_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tf_bigpro as project,\n" +
            "\t'外观' as type,\n" +
            "\tf_seq as process,\n" +
            "\tdf_qms_ipqc_waig_detail_check.f_sort as item,\n" +
            "\tsum(spot_check_count) as total,\n" +
            "\tsum(if(f_result = 'fail',1,0)) as ng\n" +
            "\tfrom df_qms_ipqc_waig_total_check\n" +
            "\tLEFT JOIN df_qms_ipqc_waig_detail_check ON df_qms_ipqc_waig_total_check.id = df_qms_ipqc_waig_detail_check.f_parent_id\n" +
            "${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportNgClassificationScale(@Param("ew") QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);


    @Select("select\n" +
            "f_mac str1\n" +
            ",sum(spot_check_count) inte1\n" +
            ",count(affect_count) inte2 \n" +
            "from df_qms_ipqc_waig_total_check  \n" +
            " ${ew.customSqlSegment} " +
            "group by f_mac ")
    List<Rate3> getMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    @Select("select * from (SELECT\n" +
            "\ttol.*,\n" +
            "\tde.f_product_id,\n" +
            "\tde.f_sort,\n" +
            "\trfid.product_time,\n" +
            "\tdf.handling_sug\n" +
            "\t\n" +
            "FROM\n" +
            "\tdf_qms_ipqc_waig_detail_check de\n" +
            "\tLEFT JOIN df_qms_ipqc_waig_total_check tol ON de.f_parent_id = tol.id LEFT\n" +
//            "\tJOIN df_qms_rfid_clamp_sn rfid ON de.f_product_id = rfid.bar_code \n" +
            "\tJOIN df_qms_rfid_clamp_sn rfid ON de.f_product_id = rfid.bar_code and tol.f_seq=rfid.process\n" +
            "\tLEFT JOIN df_audit_detail df ON tol.id = df.parent_id AND df.impact_type = '外观'\n" +
            "\tAND df.report_man = '系统' AND df.handling_sug IS NOT NULL  ${ ew.customSqlSegment }) as t")
    List<Map<String, Object>> getWGDetailInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String, Object>> qw);



    @Select("select t.*, clr.color 'date'         " +
            "                        from        " +
            "                        (select name, (ng_num / all_num) rate,ng_num,all_num from     " +
            "       " +
            "                        (select f_mac name, sum(ng_num) ng_num , sum(all_num) all_num from          " +
            "                 " +
            "                        ( SELECT      " +
            "                         tol.f_mac  , tol.f_seq ,count(tol.f_mac) as ng_num      " +
            "                           " +
            "                         FROM     " +
            "                         df_qms_ipqc_waig_detail_Check det      " +
            "                         LEFT JOIN df_qms_ipqc_waig_total_Check tol ON tol.id = det.f_parent_id     " +
            "                         JOIN df_process dp ON dp.process_name = tol.f_seq    " +
            "where dp.floor like #{floor} and tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and det.f_sort != '破片' " +
            "   GROUP BY tol.f_mac, tol.f_seq)     " +
            "                " +
            "           item_res_ng left join     " +
            "                        (select f_seq,sum(spot_check_count) all_num         " +
            "                        from df_qms_ipqc_waig_total_Check    " +
            "where f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "  group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq     " +
            "                        group by name         " +
            "                        ) t      " +
            "                        group by name     " +
            "                        order by rate desc     " +
            "                        limit 5) t     " +
            "                        left join df_qms_ipqc_flaw_color clr on t.name = clr.name   ")
        // 加上早晚班判断 //5楼只查5楼数据
    List<Rate> listNgTop5Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);


    @Select("select t.*, clr.color 'date'         " +
            "                        from        " +
            "                        (select name, (ng_num / all_num) rate,ng_num,all_num from     " +
            "       " +
            "                        (select f_sort name, sum(ng_num) ng_num , sum(all_num) all_num from          " +
            "                 " +
            "                        ( SELECT      " +
            "                         det.f_sort  , tol.f_seq ,count(det.f_sort) as ng_num      " +
            "                           " +
            "                         FROM     " +
            "                         df_qms_ipqc_waig_detail_Check det      " +
            "                         LEFT JOIN df_qms_ipqc_waig_total_Check tol ON tol.id = det.f_parent_id     " +
            "                         JOIN df_process dp ON dp.process_name = tol.f_seq    " +
            "where dp.floor like #{floor} and tol.f_mac like #{fmac} and tol.f_seq like #{process} and tol.f_bigpro like #{project} and tol.f_line like #{lineBody} and tol.status = 'NG' and tol.f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and det.f_sort != '破片' " +
            "   GROUP BY det.f_sort, tol.f_seq)     " +
            "                " +
            "           item_res_ng left join     " +
            "                        (select f_seq,sum(spot_check_coun" +
            "t) all_num         " +
            "                        from df_qms_ipqc_waig_total_Check    " +
            "where f_mac like #{fmac} and f_seq like #{process} and f_bigpro like #{project} and f_line like #{lineBody} and f_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(f_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "  group by f_seq) all_num on item_res_ng.f_seq = all_num.f_seq     " +
            "                        group by name         " +
            "                        ) t      " +
            "                        group by name     " +
            "                        order by rate desc     " +
            "                        limit 10) t     " +
            "                        left join df_qms_ipqc_flaw_color clr on t.name = clr.name   ")
        // 加上早晚班判断 //5楼只查5楼数据
    List<Rate> listNgTop10Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour, String fmac);

    @Select("SELECT f_time, okRate,spot_check_count,okNum,ngNum " +
            "FROM ( " +
            "    SELECT " +
            "        tol.f_time, " +
            "SUM(spot_check_count) spot_check_count, "+
            "SUM(affect_count) ngNum, "+
            "SUM(spot_check_count - affect_count) okNum, "+
            "        SUM(spot_check_count - IF(det.f_sort != '破片', affect_count, 0)) / SUM(spot_check_count) okRate " +
            "    FROM df_qms_ipqc_waig_total_check tol " +
            "    LEFT JOIN df_qms_ipqc_waig_detail_check det ON tol.id = det.f_parent_id " +
            "    WHERE tol.f_time BETWEEN #{startTime} AND #{endTime} " +
            "      AND HOUR(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) BETWEEN #{startHour} AND #{endHour} " +
            "      AND tol.f_bigpro = #{projectName} " +
            "      AND tol.f_seq = #{process} "+
            "      AND WEEKDAY(DATE_SUB(tol.f_time, INTERVAL 7 HOUR)) != 6 " +  // 过滤掉周日
            "      AND spot_check_count IS NOT NULL " +  // 确保 spot_check_count 不是 NULL
            "    GROUP BY f_time " +
            "    ORDER BY f_time " +
            ") t ")
    List<DfQmsIpqcWaigTotalCheck> listOkRate(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("startHour") int startHour,
            @Param("endHour") int endHour,
            @Param("projectName") String projectName,
            @Param("process") String process

    );
}
