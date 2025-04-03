package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate;
import com.ww.boengongye.entity.Rate2;
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
 * @since 2023-03-11
 */
public interface DfSizeCheckItemInfosMapper extends BaseMapper<DfSizeCheckItemInfos> {

    @Select("SELECT item.item_name , sum(if(item.check_result='NG',1,0))/ count(item.item_name) ng_rate " +
            "FROM `df_size_check_item_infos` item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} " +
            "group by item_name " +
            "order by ng_rate desc " +
            "limit 10")
    List<DfSizeCheckItemInfos> listByFactory(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("SELECT sum(if(item.check_result='OK',1,0))/ count(check_result) ng_rate " +
            "FROM `df_size_check_item_infos` item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment}")
    DfSizeCheckItemInfos getOKRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("select * from  " +
            " (select t.*, pro.sort from      " +
            " (select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by det.test_time desc) ran  , item.check_result, det.test_time check_time, item.check_type " +
            " from df_size_check_item_infos item  " +
            " left join df_size_detail det on item.check_id = det.id " +
            " where det.project = #{project} and det.item_name = #{color} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour}  and det.process = #{process} AND item.key_point = 1 " +
            " order by det.test_time desc) t  " +
            " left join ( SELECT * FROM df_size_cont_stand WHERE process = #{process} and project = #{project} and color = #{color} ) pro ON t.item_name = pro.test_item  " +
            " where pro.key_point = 1  " +
            " AND pro.is_use = 1) temp " +
            " where ran <= 60 " +
            " order by ran desc, sort ")
    List<DfSizeCheckItemInfos> listJoinDetailLimit50(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour);

    // 近50组OK的数据
    @Select("select * from  " +
            " (select t.*, pro.sort from      " +
            " (select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by det.test_time desc) ran  , item.check_result, det.test_time check_time " +
            " from df_size_check_item_infos item  " +
            " left join df_size_detail det on item.check_id = det.id and det.result = \"OK\" " +
            " where det.project = #{project} and det.item_name = #{color} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour}  and det.process = #{process} AND item.key_point = 1 " +
            " order by det.test_time desc) t  " +
            " left join ( SELECT * FROM df_size_cont_stand WHERE process = #{process} and project = #{project} and color = #{color} ) pro ON t.item_name = pro.test_item  " +
            " where pro.key_point = 1  " +
            " AND pro.is_use = 1) temp " +
            " where ran <= 70 " +
            " order by ran desc, sort ")
    List<DfSizeCheckItemInfos> listJoinDetailLimit50OkData(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour);

    /*@Select("select * from  " +
            " (select t.*, pro.sort from      " +
            " (select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by item.create_time desc) ran  , item.check_result   " +
            " from df_size_check_item_infos item     " +
            " left join df_size_detail det on item.check_id = det.id " +
            " where det.process = #{process} AND item.key_point = 1 and det.create_time BETWEEN #{startTime} and #{endTime} " +
            " order by item.create_time desc) t  " +
            " left join ( SELECT * FROM df_size_cont_stand WHERE process = #{process} ) pro ON t.item_name = pro.test_item  " +
            " where pro.key_point = 1  " +
            " AND pro.is_use = 1) temp " +
            " order by ran desc, sort ")*/
    /*@Select(" select * from      " +
            "              (select t.*, pro.sort from          " +
            "              (select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by item.create_time desc) ran  , item.check_result       " +
            "              from df_size_detail det         " +
            "              left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id     " +
            "              where item.create_time BETWEEN #{startTime} and #{endTime} and det.process = #{process} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} AND item.key_point = 1  ) t      " +
            "              left join ( SELECT * FROM df_size_cont_stand WHERE process = #{process} and key_point = 1 and sort is not null order by sort limit #{startLine},#{limit}) pro ON t.item_name = pro.test_item      " +
            "              where pro.key_point = 1      " +
            "              AND pro.is_use = 1) temp     " +
            "              order by sort  ")*/    // 优化后的SQL   按照标准表排序
    @Select(" select * from \n" +
            "(select t.*, pro.sort from     \n" +
            "(select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by item.create_time desc) ran  , item.check_result  \n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id\n" +
            "where item.create_time BETWEEN #{startTime} and #{endTime} and det.process = #{process} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} AND item.key_point = 1  ) t \n" +
            "left join ( select item_name, row_number() over(order by ok_rate) sort from \n" +
            "(select item.item_name, sum(if(item.check_result = 'OK', 1, 0)) / count(*) ok_rate\n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id\n" +
            "where item.create_time BETWEEN #{startTime} and #{endTime} and det.process = #{process} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} AND item.key_point = 1\n" +
            "group by item.item_name) t\n" +
            "limit #{startLine},#{limit}) pro ON t.item_name = pro.item_name \n" +
            "where pro.sort is not null) temp\n" +
            "order by sort")    // 优化后的SQL   按照良率排序
    List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcess(String process,String project,String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit); // 用于获取 单工序 正态分布数据  加上分页
    @Select("<script>  select * from \n" +
            "(select t.*, pro.sort from     \n" +
            "(select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by item.create_time desc) ran  , item.check_result  \n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id\n" +
            "where det.test_time BETWEEN #{startTime} and #{endTime} and det.process = #{process} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} AND item.key_point = 1 AND item.item_name = #{itmeName} " +
            "<if test = 'machineCode != null and machineCode != &quot;&quot;'>" +
            "AND det.machine_code = #{machineCode} " +
            "</if>" +
            " ) t \n" +
            "left join ( select item_name, row_number() over(order by ok_rate) sort from \n" +
            "(select item.item_name, sum(if(item.check_result = 'OK', 1, 0)) / count(*) ok_rate\n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id\n" +
            "where det.test_time BETWEEN #{startTime} and #{endTime} and det.process = #{process} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} AND item.key_point = 1 AND item.item_name = #{itmeName}" +
            "<if test = 'machineCode != null and machineCode != &quot;&quot;'>" +
            "AND det.machine_code = #{machineCode} " +
            "</if>" +
            "  \n" +
            "group by item.item_name) t\n" +
            "limit #{startLine},#{limit}) pro ON t.item_name = pro.item_name \n" +
            "where pro.sort is not null) temp\n" +
            "order by sort </script>")    // 优化后的SQL   按照良率排序
    List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcessAndItem(String process,String project,String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit,String itmeName,String machineCode); // 用于获取 单工序 正态分布数据  加上分页

    @Select(" select * from      " +
            "  (select concat(t.process, '-', t.item_name) item_name, t.check_value, t.standard_value, t.usl, t.lsl, t.ran, t.check_result, t.process, pro_stand.sort, pro.sort pro_sort from          " +
            "  (select item.item_name, item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by det.process,item.item_name order by item.create_time desc) ran  , item.check_result       " +
            "  from df_size_check_item_infos item         " +
            "  left join df_size_detail det on item.check_id = det.id     " +
            "  where  item.key_point = 1  " +
            "  order by item.create_time desc) t      " +
            "  left join df_size_cont_stand pro_stand ON t.item_name = pro_stand.test_item and pro_stand.process = t.process  " +
            " left join df_process pro on pro.process_name = pro_stand.process " +
            "  where pro_stand.key_point = 1      " +
            "  AND pro_stand.is_use = 1) temp     " +
            "  where ran <= 50      " +
            "  order by pro_sort, ran desc, sort  ")
    List<DfSizeCheckItemInfos> listJoinDetailLimit50_2();  // 用于获取 全工序 正态分布数据

    /*@Select(" select * from      " +
            "  (select concat(t.process, '-', t.item_name) item_name, t.check_value, t.standard_value, t.usl, t.lsl, t.ran, t.check_result, t.process, pro_stand.sort, pro.sort pro_sort from          " +
            "  (select item.item_name, item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by det.process,item.item_name order by item.create_time desc) ran  , item.check_result       " +
            "  from df_size_check_item_infos item         " +
            "  left join df_size_detail det on item.check_id = det.id     " +
            "  where  item.key_point = 1  and det.create_time BETWEEN #{startTime} and #{endTime} " +
            "  order by item.create_time desc) t      " +
            "  left join df_size_cont_stand pro_stand ON t.item_name = pro_stand.test_item and pro_stand.process = t.process  " +
            " left join df_process pro on pro.process_name = pro_stand.process " +
            "  where pro_stand.key_point = 1      " +
            "  AND pro_stand.is_use = 1) temp     " +
            "  order by pro_sort, ran desc, sort  ")*/
    /*@Select("select * from          " +
            "               (select concat(t.process, '-', t.item_name) item_name, t.check_value, t.standard_value, t.usl, t.lsl, t.ran, t.check_result, t.process, pro_stand.sort, pro.sort pro_sort , dense_rank() over(order by pro.sort, pro_stand.sort, concat(t.process, '-', t.item_name)) lmt from              " +
            "               (select item.item_name, item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by det.process,item.item_name order by item.create_time desc) ran  , item.check_result           " +
            "               from df_size_detail det             " +
            "               left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id         " +
            "               where   item.create_time BETWEEN #{startTime} and #{endTime} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and  item.key_point = 1    " +
            "               ) t          " +
            "               left join df_size_cont_stand pro_stand ON t.item_name = pro_stand.test_item and pro_stand.process = t.process      " +
            "              left join df_process pro on pro.process_name = pro_stand.process     " +
            "               where pro_stand.key_point = 1          " +
            "               AND pro_stand.is_use = 1) temp         " +
            "               where lmt between #{startLine} and #{endLine} " +
            "               order by pro_sort, sort   ")  // 优化后SQL  // 按照 标准表排序
    List<DfSizeCheckItemInfos> listJoinDetailByTime(String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer endLine);  // 用于获取 全工序 正态分布数据  添加分页*/
    @Select("select t.*, t2.sort from \n" +
            "(select concat(det.project ,'-', det.process, '-', item.item_name) item_name, item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , row_number() over(partition by det.process,item.item_name order by det.test_time desc) ran  , item.check_result \n" +
            "from df_size_detail det   \n" +
            "left join df_size_check_item_infos item on det.id = item.check_id     and det.process like #{process}      \n" +
            "where   det.test_time BETWEEN #{startTime} and #{endTime} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and  item.key_point = 1     \n" +
            ") t\n" +
            "left join (select item_name, row_number() over(order by ok_rate) sort from(\n" +
            "select concat(det.project ,'-', det.process, '-', item.item_name) item_name, sum(if(item.check_result = 'OK',1,0)) / count(*) ok_rate\n" +
            "from df_size_detail det   \n" +
            "left join df_size_check_item_infos item on det.id = item.check_id     and det.process like #{process}     \n" +
            "where   det.test_time BETWEEN #{startTime} and #{endTime} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and  item.key_point = 1     \n" +
            "group by item_name) t1 limit #{startLine},#{limit}) t2 on t.item_name = t2.item_name\n" +
            "where sort is not null\n" +
            "order by sort")
    List<DfSizeCheckItemInfos> listJoinDetailByTime(String process, String project,String color,String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit);  // 用于获取 全工序 正态分布数据  添加分页

    /*@Select("SELECT distinct name, rate FROM  " +
            " (select item.item_name name, sum(if(item.check_result='NG', 1, 0)) / count(*) rate     " +
            " from df_size_check_item_infos item     " +
            " left join df_size_detail det on item.check_id = det.id     " +
            " where det.process like #{process} AND item.create_time between #{startTime} and #{endTime} and item.key_point = 1 " +
            " group by item.item_name) item_rate  " +
            " left join (select * from df_size_cont_stand where process like #{process}) pro on item_rate.name = pro.test_item " +
            " where pro.key_point = 1 and pro.is_use = 1 " +
            " order by rate desc     " +
            " limit 10 ")*/    // 尺寸NG TOP10先前的算法
    /*@Select("select name, sum(rate) rate from  " +
            "(select pro_ng_num.item_name name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num rate from     " +
            "             (select det.process, item.item_name, sum(if(item.check_result='NG',1,0)) ng_num     " +
            "             from df_size_check_item_infos item     " +
            "             left join df_size_detail det on item.check_id = det.id     " +
            "             where process like #{process} and det.create_time between #{startTime} and #{endTime} and item.key_point = 1      " +
            "             group by det.process, item.item_name ) pro_ng_num     " +
            "             left join      " +
            "             (select process, count(*) all_num     " +
            "             from df_size_detail     " +
            "             where process like #{process} and create_time between #{startTime} and #{endTime}     " +
            "             group by process) pro_all_num on pro_ng_num.process = pro_all_num.process order by rate desc ) t " +
            "             group by name " +
            "             order by rate desc ")*/  // NG率相加算法
    @Select("select name, sum(rate) rate from      " +
            "(select pro_ng_num.item_name name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num rate from         " +
            "(select det.process, item.item_name, sum(if(item.check_result='NG',1,0)) ng_num         " +
            "from df_size_detail det         " +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id         " +
            "where item.create_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and process like #{process} and item.key_point = 1          " +
            "group by det.process, item.item_name ) pro_ng_num         " +
            "left join          " +
            "(select process, count(*) all_num         " +
            "from df_size_detail         " +
            "where create_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(create_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and process like #{process}         " +
            "group by process) pro_all_num on pro_ng_num.process = pro_all_num.process order by rate desc ) t     " +
            "group by name     " +
            "order by rate desc  ")   // 优化后SQL NG率相加算法
    List<Rate> listSizeNgRate(String process, String startTime, String endTime, Integer startHour, Integer endHour);  // 加上白班晚班

//    @Select("select t.name, sum(t.rate) rate from " +
//            "(select process, item_name name, sum(ng_num) / sum(all_num) rate " +
//            "from df_size_item_ng_rate " +
//            "${ew.customSqlSegment} " +
//            "group by process, item_name) t " +
//            "group by t.name " +
//            "order by rate desc")  // 通过新表查询数据尺寸NG TOP
    @Select("select item_name name, sum(ng_num) / sum(all_num) rate " +
            "from df_size_item_ng_rate " +
            "${ew.customSqlSegment} " +
            "group by name " +
            "order by rate desc")  // 通过新表查询数据尺寸NG TOP
    List<Rate> listSizeNgRate3(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper) ;

    @Select("select item_name name, sum(ng_num) ng_num, sum(all_num) all_num, sum(ng_rate) rate from  " +
            "(SELECT process, item_name, sum(ng_num) ng_num, sum(all_num) all_num, avg(ng_rate) ng_rate  " +
            "FROM `df_size_item_ng_rate` " +
            "where create_time between #{startTime} and #{endTime} and process like #{process} " +
            "group by process, item_name) t " +
            "group by item_name " +
            "order by rate desc " +
            "limit 10")
    List<Rate> listSizeNgRate2(String process, String startTime, String endTime);  // 利用中间表查询尺寸NG TOP

    @Select("select * from " +
            "(select det.machine_code d_code, item.usl usl, item.check_value check_value, item.standard_value standard_value, " +
            "item.lsl lsl, item.check_result check_result, item.create_time, row_number() over(partition by det.machine_code order by det.create_time desc) ran " +
            "from df_size_detail det " +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id " +
            "left join df_process pro on pro.process_name = det.process " +
            "${ew.customSqlSegment} and det.machine_code like concat(pro.first_code,'%') " +
            "order by det.id desc) t " +
            "where t.ran <= 50 " +
            "order by t.d_code, t.ran desc")
    List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachine(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("select * from   " +
            "(select det.machine_code d_code, item.usl usl, item.check_value check_value, item.standard_value standard_value,   " +
            "item.lsl lsl, item.check_result check_result, item.create_time, row_number() over(partition by det.machine_code order by det.create_time desc) ran   " +
            "from df_size_detail det   " +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id   " +
            "left join df_process pro on pro.process_name = det.process   " +
            "${ew.customSqlSegment} and det.machine_code like concat(pro.first_code,'%')   " +
            "order by det.id desc) t " +
            "left join  " +
            "(select det.machine_code, row_number() over(order by sum(if(item.check_result='NG',1,0)) / count(*) desc, det.machine_code) sort from df_size_detail det " +
            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on item.check_id = det.id " +
            "left join df_process pro on det.process like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} " +
            "group by det.machine_code) mac_res_sort on t.d_code = mac_res_sort.machine_code " +
            "where t.ran <= 50   " +
            "order by mac_res_sort.sort, t.ran desc")   // 根据不良率排序机台近50组数据
    List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachineSortByNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("select * from (\n" +
            "select det.project, det.process, det.item_name color, item.item_name, det.bearing d_code, item.usl usl, item.check_value check_value, item.standard_value standard_value,    \n" +
            "item.lsl lsl, item.check_result check_result, det.test_time check_time, row_number() over(partition by det.project, det.process, det.item_name,item.item_name,det.machine_code order by det.test_time desc) ran    \n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item on det.id = item.check_id    \n" +
            "${ew.customSqlSegment} \n" +
            "order by ran desc) t where t.ran <= 70")
    List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    // 获取OK的近50组数据（机台）
    @Select("select * from (\n" +
            "select det.project, det.process, det.item_name color, item.item_name, det.machine_code d_code, item.usl usl, item.check_value check_value, item.standard_value standard_value,    \n" +
            "item.lsl lsl, item.check_result check_result, det.test_time check_time, row_number() over(partition by det.project, det.process, det.item_name,item.item_name,det.machine_code order by det.test_time desc) ran    \n" +
            "from df_size_detail det    \n" +
            "left join df_size_check_item_infos item on det.id = item.check_id and det.result = 'OK' and item.key_point = 1  \n" +
            "left join df_process pro on pro.process_name = det.process    \n" +
            "${ew.customSqlSegment} and det.machine_code like concat(pro.first_code,'%')\n" +
            "order by ran desc) t where t.ran <= 70")
    List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50OkData(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

//    @Select("select * from (\n" +
//            "select det.project, det.process, det.item_name color, item.item_name, item.usl usl, item.check_value check_value, item.standard_value standard_value,    \n" +
//            "item.lsl lsl, item.check_result check_result, item.create_time, row_number() over(partition by det.project, det.process, det.item_name,item.item_name order by det.create_time desc) ran    \n" +
//            "from df_size_detail det    \n" +
//            "left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id    \n" +
//            "where HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR)) between 0 and 23\n" +
//            "order by ran desc) t where t.ran <= 50")
//    List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select(" select t.item_name, t.check_value, t.standard_value, t.usl, t.lsl from      " +
            " (select item.item_name,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , det.process, row_number() over(partition by item.item_name order by item.create_time desc) ran     " +
            " from df_size_check_item_infos item     " +
            " left join df_size_detail det on item.check_id = det.id     " +
            " ${ew.customSqlSegment} " +
            " order by item.create_time desc) t     " +
            " where ran <= 50     " +
            " order by item_name, check_value")
    List<DfSizeCheckItemInfos> listItemCheckValueLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select(" select * from " +
            " (select t.*, pro.sort from     " +
            " (select det.process,item.check_value check_value,item.standard_value standard_value, item.usl usl,item.lsl lsl , row_number() over(partition by det.process order by item.create_time desc) ran  , item.check_result  " +
            " from df_size_check_item_infos item    " +
            " left join df_size_detail det on item.check_id = det.id " +
            " ${ew.customSqlSegment} " +
            " order by item.create_time desc) t " +
            " left join ( SELECT * FROM df_process ) pro ON t.process = pro.process_name ) temp " +
            " order by sort, ran desc ")
    List<DfSizeCheckItemInfos> listJoinDetailAndProcess(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    /*@Select("select ok_rate.* from " +
            "(select item_name name, sum(if(check_result='NG',1,0))/count(*) rate,  sum(if(check_result='NG',1,0)) ng_num, count(*) all_num, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%m-%d') date " +
            "from df_size_check_item_infos force index(idx_df_size_check_item_infos_create_time) " +
            "${ew.customSqlSegment} " +
            "group by item_name, date) ok_rate " +
            "left join (select * from df_size_cont_stand where process = 'CNC0') stand on stand.test_item = ok_rate.name " +
            "order by stand.sort is null, stand.sort, ok_rate.date")*/
    @Select("select t.*, data.rate, data.ng_num, data.all_num from ( " +
            "select * from  " +
            "(select item_name name, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%m-%d') date " +
            "from df_size_check_item_infos force index(idx_df_size_check_item_infos_create_time)   " +
            "${ew.customSqlSegment} " +
            "group by item_name, date) t1, (select 'A' day_or_night union select 'B' day_or_night) t2 ) t " +
            "left join  " +
            "(select item_name name, sum(if(check_result='NG',1,0))/count(*) rate,  sum(if(check_result='NG',1,0)) ng_num, count(*) all_num, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%m-%d') date, if(HOUR(DATE_SUB(create_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night " +
            "from df_size_check_item_infos force index(idx_df_size_check_item_infos_create_time)   " +
            "${ew.customSqlSegment} " +
            "group by item_name, date, day_or_night) data on t.name = data.name and t.date = data.date and t.day_or_night = data.day_or_night " +
            "left join (select * from df_size_cont_stand where process = 'CNC0') stand on stand.test_item = t.name " +
            "order by stand.sort is null, stand.sort, t.date ")
    List<Rate> listItemOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("select t.*, data.rate, data.ng_num, data.all_num from (   " +
            "select * from    " +
            "(select item_name name, DATE_FORMAT(DATE_SUB(create_time, INTERVAL 7 HOUR),'%m-%d') date " +
            "from df_size_item_ng_rate " +
            "${ew.customSqlSegment} " +
            "group by item_name, date) t1, (select 'A' day_or_night union select 'B' day_or_night) t2 ) t   " +
            "left join    " +
            "(select item_name name, sum(ng_num) / sum(all_num) rate, sum(ng_num) ng_num, sum(all_num) all_num, DATE_FORMAT(create_time,'%m-%d') date, day_or_night " +
            "from df_size_item_ng_rate " +
            "${ew.customSqlSegment} " +
            "group by item_name, date, day_or_night " +
            ") data on t.name = data.name and t.date = data.date and t.day_or_night = data.day_or_night   " +
            "order by t.date, t.name ") // 使用新表统计各测量项的不良率
    List<Rate> listItemOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select(" select DISTINCT(p.machine_code ) as machine_code from  df_size_check_item_infos t join   df_size_detail p on t.check_id=p.id  ${ew.customSqlSegment}  ")
    List<DfSizeCheckItemInfos> listJoin(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select("select pro.process_name name, t.item_name date, t.ng_rate rate, t.ran, pro.sort " +
            "from df_process pro " +
            "left join (select det.process, item.item_name,  " +
            "sum(if(item.check_result = 'NG', 1, 0)) / count(*) ng_rate,  " +
            "row_number() over(partition by det.process order by sum(if(item.check_result = 'NG', 1, 0)) / count(*) desc) ran " +
            "from df_size_check_item_infos item  " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} " +
            "group by det.process, item.item_name) t on pro.process_name = t.process " +
            "where (ran <= 5 or ran is null) and sort is not null " +
            "order by sort, ran")
    List<Rate> listAllProcessSizeNgRateTop5(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);  // 各工序不良项比较TOP5

    @Select("select process name, sum(if(result='OK',1,0)) / count(*) rate from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by process ")
    List<Rate> listAllProcessOKRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper); // 各工序的良率

    /*@Select("select pro_date.date, pro_date.process name, ng_rate.all_num, ng_rate.ng_num, if(ng_rate.rate is null, 0, ng_rate.rate) rate  from  " +
            "(select * from( " +
            "select distinct DATE_FORMAT(det.create_time,'%m-%d') date from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment}) t1, " +
            "(select distinct det.process from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment}) t2) pro_date " +
            "left join (select pro_ng.process name, pro_ng.date, all_ng.ng_num all_num, pro_ng.ng_num ng_num, pro_ng.ng_num / all_ng.ng_num rate from ( " +
            "select DATE_FORMAT(det.create_time,'%m-%d') date, count(*) ng_num from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment}  " +
            "group by date) all_ng " +
            "left join ( " +
            "select det.process,DATE_FORMAT(det.create_time,'%m-%d') date, count(*) ng_num from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment}  " +
            "group by det.process, date) pro_ng on all_ng.date = pro_ng.date " +
            "order by pro_ng.date) ng_rate on pro_date.process = ng_rate.name and ng_rate.date = pro_date.date  " +
            "left join df_process_project_config con on con.process_name = pro_date.process " +
            "order by con.sort")*/
    /*@Select(" select pro_date.date, pro_date.process name, ng_rate.all_num, ng_rate.ng_num, if(ng_rate.rate is null, 0, ng_rate.rate) rate, con.sort  from      " +
            " (select * from(     " +
            " select distinct DATE_FORMAT(det.create_time,'%m-%d') date from df_size_detail det     " +
            " left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id     " +
            " where item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}) t1,     " +
            " (select distinct det.process from df_size_detail det     " +
            " left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id     " +
            " where item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}) t2) pro_date     " +
            " left join (select pro_ng.process name, pro_ng.date, all_ng.ng_num all_num, pro_ng.ng_num ng_num, pro_ng.ng_num / all_ng.ng_num rate from (     " +
            " select DATE_FORMAT(det.create_time,'%m-%d') date, count(*) ng_num from df_size_detail det     " +
            " left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id     " +
            " where item.create_time between #{startTime} and #{endTime} and item.item_name = #{itemName}      " +
            " group by date) all_ng     " +
            " left join (     " +
            " select det.process,DATE_FORMAT(det.create_time,'%m-%d') date, count(*) ng_num from df_size_detail det     " +
            " left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id     " +
            " where item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}      " +
            " group by det.process, date) pro_ng on all_ng.date = pro_ng.date     " +
            " ) ng_rate on pro_date.process = ng_rate.name and ng_rate.date = pro_date.date      " +
            " left join df_process_project_config con on con.process_name = pro_date.process     " +
            " order by con.sort, pro_date.date") //  优化后SQL*/
    @Select(" select pro_date.date, pro_date.process name, pro_date.day_or_night, ng_rate.all_num, ng_rate.ng_num, if(ng_rate.rate is null, 0, ng_rate.rate) rate  from        " +
            "             (select * from(       " +
            "             select distinct DATE_FORMAT(DATE_SUB(det.create_time, INTERVAL 7 HOUR),'%m-%d') date from df_size_detail det       " +
            "             left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id       " +
            "             where det.project like #{project} and det.item_name like #{color} and item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}) t1,       " +
            "             (select distinct det.process from df_size_detail det       " +
            "             left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id       " +
            "             where det.project like #{project} and det.item_name like #{color} and item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}) t2, " +
            "\t\t\t\t\t\t (select 'A' day_or_night union select 'B' day_or_night) t3) pro_date       " +
            "             left join (select pro_ng.process name, pro_ng.date,pro_ng.day_or_night, all_ng.ng_num all_num, pro_ng.ng_num ng_num, pro_ng.ng_num / all_ng.ng_num rate from (       " +
            "             select *, count(*) ng_num from ( " +
            "select DATE_FORMAT(DATE_SUB(det.create_time, INTERVAL 7 HOUR),'%m-%d') date, if(HOUR(DATE_SUB(det.create_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night from df_size_detail det " +
            "             left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id       " +
            "             where det.project like #{project} and det.item_name like #{color} and item.create_time between #{startTime} and #{endTime} and item.item_name = #{itemName}   )    t " +
            "\t\t\t\t\t\t\tgroup by date,day_or_night) all_ng       " +
            "             left join (       " +
            "             select *, count(*) ng_num from( " +
            "select det.process,DATE_FORMAT(DATE_SUB(det.create_time, INTERVAL 7 HOUR),'%m-%d') date, if(HOUR(DATE_SUB(item.create_time, INTERVAL 7 HOUR)) < 12, 'A', 'B') day_or_night from df_size_detail det       " +
            "             left join df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) on det.id = item.check_id       " +
            "             where det.project like #{project} and det.item_name like #{color} and item.create_time between #{startTime} and #{endTime} and item.check_result = #{checkResult} and item.item_name = #{itemName}  )  t    " +
            "             group by process, date, day_or_night) pro_ng on all_ng.date = pro_ng.date and all_ng.day_or_night = pro_ng.day_or_night " +
            "             ) ng_rate on pro_date.process = ng_rate.name and ng_rate.date = pro_date.date and ng_rate.day_or_night = pro_date.day_or_night       " +
            "             order by pro_date.date")  // 加上白夜班
    List<Rate> listProcessNgRateByItem(String project,String color,String startTime, String endTime, String checkResult, String itemName); // 获取该不良项不同工序的占比


    @Select("select * from ( " +
            "select item.* from df_size_check_item_infos item force index(idx_df_size_check_item_infos_create_time) " +
            "left join df_size_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} " +
            "order by item.create_time desc " +
            "limit 50) t " +
            "order by create_time")
    List<DfSizeCheckItemInfos> listCheckItemValueLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    @Select({"<script> " +
            "select sort,process_name process, IF(SUM(ok)/sum(total) IS NOT NULL,SUM(ok)/sum(total),1) result\n" +
            "FROM\n" +
            "(\n" +
            "\tselect sort,process_name,ok,total\n" +
            "\tfrom df_process dp\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tselect f_seq,SUM(IF(f_result = 'OK' OR f_result = 'ok',1,0)) ok\n" +
            "\t\t,count(f_result) total\n" +
            "\t\tFROM\n" +
            "\t\t(\n" +
            "\t\t\tselect f_parent_id,f_result\n" +
            "\t\t\tFROM df_qms_ipqc_waig_detail dqiwd\n" +
            "\t\t\twhere dqiwd.f_time between #{startTime} and #{endTime}\n" +
            "\t\t)t1\n" +
            "\t\tLEFT JOIN df_qms_ipqc_waig_total dqipwt\n" +
            "\t\ton t1.f_parent_id = dqipwt.id\n" +
            "\t\twhere 1=1 " +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND f_bigpro = #{item} " +
            "</if>" +
            "\t\tGROUP BY dqipwt.f_seq\n" +
            "\t) t\n" +
            "\tON dp.process_name = t.f_seq\n" +
            "\twhere sort is not null and dp.process_name LIKE #{process}\n" +
            "\n" +
            "\tunion ALL\n" +
            "\n" +
            "\tselect sort,process_name,ok,total\n" +
            "\tfrom df_process dp\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tselect process,SUM(IF(check_result = 'OK',1,0)) ok\n" +
            "\t\t,count(check_result) total\n" +
            "\t\tFROM\n" +
            "\t\t(\n" +
            "\t\t\tselect check_id,check_result\n" +
            "\t\t\tFROM df_size_check_item_infos dscii\n" +
            "\t\t\twhere dscii.create_time between #{startTime} and #{endTime}\n" +
            "\t\t)t1\n" +
            "\t\tLEFT JOIN df_size_detail dsd\n" +
            "\t\ton t1.check_id = dsd.id\n" +
            "\t\twhere 1=1 " +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND factory = #{factoryId} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND project = #{item} " +
            "</if>" +
            "\t\tGROUP BY dsd.process\n" +
            "\t) t\n" +
            "\tON dp.process_name = t.process\n" +
            "\twhere sort is not null and dp.process_name LIKE #{process}\n" +
            ") t_union \n" +
            "group by sort,process_name\n" +
            "ORDER BY sort" +
            "</script>"})
    List<DfSizeCheckItemInfos> listTotalprocessyieldtrendchart(@Param("factoryId") String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select({"<script> " +
            "WITH temp_df_size_detail AS(\n" +
            "\tselect count(id) as size_total from df_size_detail t1 where 1 = 1\n" +
            "\tand t1.test_time between #{startTime} and #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND factory = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND process = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND project = #{item} " +
            "</if>" +
            "),\n" +
            "\n" +
            "temp_df_qms_ipqc_waig_total AS(\n" +
            "\tselect sum(spot_check_count) as waig_total from df_qms_ipqc_waig_total t2 where 1 =1 \n" +
            "\tand t2.f_time between #{startTime} and #{endTime}\n" +
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
            "),\n" +
            "\n" +
            "temp_df_data AS(\n" +
            "\tselect t.item_name,sum(IF(t.check_result = 'NG',1,0)) ng_num\n" +
            "\t,(select size_total from temp_df_size_detail) as size_total\n" +
            "\t,(select waig_total from temp_df_qms_ipqc_waig_total) as waig_total\n" +
            "\tFROM df_size_check_item_infos t\n" +
            "\tJOIN df_size_detail t2 on t.check_id = t2.id " +
            "\twhere t.key_point = '1'\n" +
            "\tand t.create_time between #{startTime} and #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND t2.factory = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND t2.process = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND t2.linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND t2.project = #{item} " +
            "</if>" +
            "\tGROUP BY item_name\n" +
            ")\n" +
            "\n" +
            "SELECT *,ng_num / GREATEST(size_total,waig_total) ng_rate\n" +
            "FROM temp_df_data\n" +
            "ORDER BY ng_rate DESC\n" +
            "limit 5 " +
            "</script>"})
    List<DfSizeCheckItemInfos> fullSizeNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select({"<script> " +
            "select sort,process_name process,create_time month_day, IF(SUM(ok)/sum(total) IS NOT NULL,SUM(ok)/sum(total),1) result\n" +
            "FROM\n" +
            "(\n" +
            "\tselect sort,process_name,t.create_time,ok,total\n" +
            "\tfrom df_process dp\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tselect f_seq,SUM(IF(f_result = 'OK' OR f_result = 'ok',1,0)) ok,DATE_FORMAT(t1.f_time,'%m-%d') create_time\n" +
            "\t\t,count(f_result) total\n" +
            "\t\tFROM\n" +
            "\t\t(\n" +
            "\t\t\tselect f_parent_id,f_result,f_time\n" +
            "\t\t\tFROM df_qms_ipqc_waig_detail dqiwd\n" +
            "\t\t\twhere dqiwd.f_time between #{startTime} and #{endTime}\n" +
            "\t\t)t1\n" +
            "\t\tLEFT JOIN df_qms_ipqc_waig_total dqipwt\n" +
            "\t\ton t1.f_parent_id = dqipwt.id\n" +
            "\t\twhere 1=1 \n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND f_bigpro = #{item} " +
            "</if>" +
            "\t\tGROUP BY dqipwt.f_seq ,DATE_FORMAT(t1.f_time,'%m-%d')\n" +
            "\t) t\n" +
            "\tON dp.process_name = t.f_seq\n" +
            "\twhere dp.process_name LIKE #{process}\n" +
            "\n" +
            "\tunion ALL\n" +
            "\n" +
            "\tselect sort,process_name,t.create_time,ok,total\n" +
            "\tfrom df_process dp\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tselect process,SUM(IF(check_result = 'OK',1,0)) ok,DATE_FORMAT(t1.create_time,'%m-%d') create_time\n" +
            "\t\t,count(check_result) total\n" +
            "\t\tFROM\n" +
            "\t\t(\n" +
            "\t\t\tselect check_id,check_result,create_time\n" +
            "\t\t\tFROM df_size_check_item_infos dscii\n" +
            "\t\t\twhere dscii.create_time BETWEEN #{startTime} and #{endTime}\n" +
            "\t\t)t1\n" +
            "\t\tLEFT JOIN df_size_detail dsd\n" +
            "\t\ton t1.check_id = dsd.id\n" +
            "\t\twhere 1=1\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND factory = #{factoryId} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND project = #{item} " +
            "</if>" +
            "\t\tGROUP BY dsd.process,DATE_FORMAT(t1.create_time,'%m-%d')\n" +
            "\t) t\n" +
            "\tON dp.process_name = t.process\n" +
            "\twhere dp.process_name LIKE #{process}\n" +
            ") t_union \n" +
            "group by sort,process_name,create_time\n" +
            "ORDER BY sort " +
            "</script>"})
    List<DfSizeCheckItemInfos> listSingleprocessyieldtrendchart(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select("")
    List<DfSizeCheckItemInfos> inputOutputQuantity(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select({"<script> " +
            "SELECT sort,dp.process_name process,COALESCE(result,1) result\n" +
            "FROM\n" +
            "df_process dp\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "\tSELECT dsd.process , SUM(IF(check_result = 'OK',1,0)) ok, count(*) total, SUM(IF(check_result = 'OK',1,0))/count(*) result\n" +
            "\tFROM \n" +
            "\t(\n" +
            "\t\tSELECT check_id,check_result\n" +
            "\t\tFROM df_size_check_item_infos dscii\n" +
            "\t\tWHERE create_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t)t\n" +
            "\tJOIN df_size_detail dsd ON dsd.ID = t.check_id\n" +
            "\twhere 1=1 " +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND dsd.factory = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND dsd.process = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND dsd.linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND dsd.project = #{item} " +
            "</if>" +
            "\tGROUP BY dsd.process\n" +
            ")t1\n" +
            "on dp.process_name = t1.process\n" +
            "where dp.sort is not null\n" +
            "ORDER BY sort" +
            "</script>"})
    List<DfSizeCheckItemInfos> detailPageSizeYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select({"<script> " +
            "\t\tselect dp.process_name name, item_name date, ng_rate rate\n" +
            "\t\tFROM\n" +
            "\t\t(\n" +
            "\t\t\tSELECT process,item_name,ng_rate\n" +
            "\t\t\tFROM\n" +
            "\t\t\t(\n" +
            "\t\t\t\t\tSELECT * ,row_number() over(partition by process order by ng_rate desc) row_num \n" +
            "\t\t\t\t\tFROM\n" +
            "\t\t\t\t\t(\n" +
            "\t\t\t\t\t\tSELECT t1.process,item_name,ng_num,size_total,COALESCE(waig_total,0) waig_total,ng_num / GREATEST(size_total,waig_total) ng_rate\n" +
            "\t\t\t\t\t\tFROM\n" +
            "\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\tselect dsd.process,t.item_name,sum(IF(t.check_result = 'NG',1,0)) ng_num\n" +
            "\t\t\t\t\t\t\tfrom \n" +
            "\t\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\t\tselect check_id,item_name,check_result\n" +
            "\t\t\t\t\t\t\t\tfrom df_size_check_item_infos dscii\n" +
            "\t\t\t\t\t\t\t\twhere dscii.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t\t\t\t\t\t\t) t\n" +
            "\t\t\t\t\t\t\tJOIN df_size_detail dsd\n" +
            "\t\t\t\t\t\t\ton t.check_id = dsd.id\n" +
            "where 1=1 " +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND dsd.factory = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND dsd.process = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND dsd.linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND dsd.project = #{item} " +
            "</if>" +
            "\t\t\t\t\t\t\tGROUP BY dsd.process,t.item_name\n" +
            "\t\t\t\t\t\t)t1\n" +
            "\t\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\tSELECT process,count(*) size_total\n" +
            "\t\t\t\t\t\t\tFROM\n" +
            "\t\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\t\tselect check_id\n" +
            "\t\t\t\t\t\t\t\tfrom df_size_check_item_infos dscii\n" +
            "\t\t\t\t\t\t\t\twhere dscii.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t\t\t\t\t\t\t)t\n" +
            "\t\t\t\t\t\t\tJOIN df_size_detail dsd on dsd.ID = t.check_id\n" +
            "\t\t\t\t\t\t\tGROUP BY process\n" +
            "\t\t\t\t\t\t)t\n" +
            "\t\t\t\t\t\ton t1.process = t.process\n" +
            "\t\t\t\t\t\tLEFT JOIN\n" +
            "\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\tselect f_seq,sum(spot_check_count) waig_total\n" +
            "\t\t\t\t\t\t\tFROM df_qms_ipqc_waig_total\n" +
            "\t\t\t\t\t\t\twhere \n" +
            "\t\t\t\t\t\t\tf_time BETWEEN #{startTime} AND #{endTime}\n" +
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
            "\t\t\t\t\t\t\tGROUP BY f_seq\n" +
            "\t\t\t\t\t\t)t2\n" +
            "\t\t\t\t\t\tON t1.process = t2.f_seq\n" +
            "\t\t\t\t\t\tORDER BY ng_rate desc\n" +
            "\t\t\t\t\t)t\n" +
            "\t\t\t)t\n" +
            "\t\t\twhere row_num &lt;= 5\n" +
            "\t\t)data\n" +
            "\t\tRIGHT JOIN df_process dp\n" +
            "\t\tON data.process = dp.process_name\n" +
            "\t\twhere dp.process_name is not null\n" +
            "</script>"})
    List<Rate> listAllProcessSizeNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    @Select("select * from \n" +
            "(select process_name process, item_name, size_all_ok_num / all_num ok_rate, size_item_ng_num / all_num item_ng_rate, row_number() over(partition by process_name order by size_item_ng_num / all_num desc) ran, t.sort from \n" +
            "(select pro.process_name, if(app.all_num > size.all_num, app.all_num, size.all_num) all_num, pro.sort from df_process pro\n" +
            "left join (select f_seq, sum(spot_check_count) all_num from df_qms_ipqc_waig_total tol where f_time between #{startTime} and #{endTime} group by f_seq) app on pro.process_name = app.f_seq\n" +
            "left join (select process, count(*) all_num from df_size_detail where create_time BETWEEN #{startTime} and #{endTime} group by process) size on pro.process_name = size.process) t\n" +
            "\n" +
            "left join \n" +
            "(select process, sum(if(result != 'NG', 1, 0)) size_all_ok_num from df_size_detail \n" +
            "where create_time BETWEEN #{startTime} and #{endTime}\n" +
            "group by process) t1 on t.process_name = t1.process\n" +
            "\n" +
            "left join \n" +
            "(select det.process, item.item_name, sum(if(item.check_result = 'NG', 1, 0)) size_item_ng_num from df_size_check_item_infos item\n" +
            "left join df_size_detail det on item.check_id = det.id\n" +
            "where item.create_time BETWEEN #{startTime} and #{endTime}  and item.key_point = 1\n" +
            "group by det.process, item.item_name) t2 on t.process_name = t2.process) t\n" +
            "where ran <= 5\n" +
            "order by t.sort, ran")
    List<Rate2> listAllProcessSizeNgRateTop5V3(String startTime, String endTime);

    @Select("select *\n" +
            "FROM (\n" +
            "    SELECT df_size_check_item_infos.ITEM_NAME,USL,LSL,STANDARD_VALUE,CHECK_VALUE,CONTROL_UPPER_LIMIT,CONTROL_LOWER_LIMIT,\n" +
            "       row_number() OVER (PARTITION BY df_size_check_item_infos.ITEM_NAME ORDER BY df_size_check_item_infos.CHECK_TIME) as rk\n" +
            "from df_size_detail\n" +
            "JOIN df_size_check_item_infos on DF_SIZE_DETAIL.id = df_size_check_item_infos.check_id\n" +
            "${ew.customSqlSegment} " +
            ") t\n" +
            "where rk <= 50")
	List<DfSizeCheckItemInfos> machineSizeAccuracyControlChart(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);


    @Select("select * FROM (\n" +
            "    SELECT\n" +
            "         DF_SIZE_DETAIL.MACHINE_CODE,\n" +
            "         USL,\n" +
            "         LSL,\n" +
            "         STANDARD_VALUE,\n" +
            "         CHECK_VALUE,\n" +
            "         CONTROL_UPPER_LIMIT,\n" +
            "         CONTROL_LOWER_LIMIT,\n" +
            "         ROW_NUMBER() OVER (PARTITION BY DF_SIZE_DETAIL.MACHINE_CODE ORDER BY DF_SIZE_CHECK_ITEM_INFOS.CHECK_TIME) AS RK\n" +
            "     FROM\n" +
            "         DF_SIZE_DETAIL\n" +
            "             JOIN DF_SIZE_CHECK_ITEM_INFOS ON DF_SIZE_DETAIL.ID = DF_SIZE_CHECK_ITEM_INFOS.CHECK_ID\n" +
            "${ew.customSqlSegment} " +
            ") t\n" +
            "where rk <= 50")
    List<DfSizeCheckItemInfos> machineSizeControlChart(QueryWrapper<DfSizeCheckItemInfos> ew);
    @Select("SELECT  " +
            "det.machine_code, " +
            "    item.usl AS usl," + // -- 规格上限
            "    item.lsl AS lsl, " + //-- 规格下限
            "    AVG(item.check_value) AS mean_value, " + //-- 样本均值
            "    VAR_POP(item.check_value) AS variance, " + //-- 样本方差
            "    STDDEV_POP(item.check_value) AS stddev, " + //-- 样本标准差
            "    LEAST( " +
            "        (item.usl - AVG(item.check_value)) / (3 * STDDEV_POP(item.check_value)), " +
            "        (AVG(item.check_value) - item.lsl) / (3 * STDDEV_POP(item.check_value)) " +
            "    ) AS cpk, " + //-- Cpk
            " (AVG(item.check_value) - item.standard_value) / ((item.usl - item.lsl) / 2) AS ca "+ //-- Ca
            "FROM  " +
            "    df_size_check_item_infos item " +
            "JOIN  " +
            "    df_size_detail det ON item.check_id = det.id  ${ew.customSqlSegment} " +
            "GROUP BY  " +
            "    det.machine_code,item.usl, item.lsl,item.standard_value") // -- 按规格上下限分组
    List<DfSizeCheckItemInfos>tzInfo(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);


    @Select("SELECT  " +
            "det.process, " +
            "    item.usl AS usl," + // -- 规格上限
            "    item.lsl AS lsl, " + //-- 规格下限

            "    LEAST( " +
            "        (item.usl - AVG(item.check_value)) / (3 * STDDEV_POP(item.check_value)), " +
            "        (AVG(item.check_value) - item.lsl) / (3 * STDDEV_POP(item.check_value)) " +
            "    ) AS cpk " + //-- Cpk

            "FROM  " +
            "    df_size_check_item_infos item " +
            "JOIN  " +
            "    df_size_detail det ON item.check_id = det.id  ${ew.customSqlSegment} " +
            "GROUP BY  " +
            "    det.process,item.usl, item.lsl,item.standard_value") // -- 按规格上下限分组
    List<DfSizeCheckItemInfos>tzInfoByProcess(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);


    @Select("SELECT   " +
            "            det.process,  " +
            "                r.qcp_usl AS usl, " +
            "                r.qcp_lsl AS lsl, " +
            "       " +
            "                LEAST(  " +
            "                    (r.qcp_usl - AVG(item.check_value)) / (3 * STDDEV_POP(item.check_value)),  " +
            "                    (AVG(item.check_value) - r.qcp_lsl) / (3 * STDDEV_POP(item.check_value))  " +
            "                ) AS cpk   " +
            "      " +
            "            FROM   " +
            "                df_size_check_item_infos item  " +
            "            JOIN   " +
            "                df_size_detail det ON item.check_id = det.id    join df_size_cont_relation r on r.ipqc_name=item.item_name   ${ew.customSqlSegment}" +
            "            GROUP BY   " +
            "                det.process,r.qcp_usl, r.qcp_lsl") // -- 按规格上下限分组
    List<DfSizeCheckItemInfos>tzInfoCpkByProcess(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);


//    @Select("with size_new as(\n" +
//            "\tselect \n" +
//            "\tdsd.machine_code\n" +
//            "\t,dsd.`result`\n" +
//            "\t,dscii.check_result \n" +
//            "\t,dscii.check_value \n" +
//            "\t,dscii.standard_value\n" +
//            "\t,dscii.lsl\n" +
//            "\t,dscii.usl \n" +
//            "\tfrom df_size_detail dsd \n" +
//            "\tinner join df_size_check_item_infos dscii \n" +
//            "\ton dscii.check_id = dsd.id \n" +
//            " ${ew.customSqlSegment} " +
//            ")\n" +
//            ",machine_temp as (\n" +
//            "\tselect \n" +
//            "\tmachine_code \n" +
//            "\t,sum(if(`result` = 'NG',1,0)) ngNum\n" +
//            "\t,count(0) total\n" +
//            "\t,round(sum(if(`result` = 'NG',1,0))/count(0),2) ngRate\n" +
//            "\tfrom size_new\n" +
//            "\tgroup by machine_code\n" +
//            "\torder by machine_code\n" +
//            ")\n" +
//            "select \n" +
//            "sn.*,mt.ngNum,mt.total,mt.ngRate\n" +
//            "from size_new sn\n" +
//            "inner join machine_temp mt\n" +
//            "on mt.machine_code = sn.machine_code")
//    List<Map<String,Object>> getDetailData(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);
//
//    @Select("select dscii.*\n" +
//            "from df_size_detail dsd \n" +
//            "inner join df_size_check_item_infos dscii \n" +
//            "on dscii.check_id = dsd.id" +
//            " ${ew.customSqlSegment} ")
//    List<DfSizeCheckItemInfos> getSizeCheckItemInfosList(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

}
