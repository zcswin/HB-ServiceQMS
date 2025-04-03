package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 稽核NG详细 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-22
 */
public interface DfAuditDetailMapper extends BaseMapper<DfAuditDetail> {
    @Select("select COUNT(*) AS question_num,process,dp.floor AS floor,dp.create_time,\n" +
            "                        sum( IF (TIMESTAMPDIFF(MINUTE , ad.end_time , ad.report_time)<(fd.read_time_max+fd.dispose_time_max), 1, 0 ) ) AS respondPromptly,\n" +
            "                        COUNT(close_time) AS close_num,(COUNT(close_time)/COUNT(*))*100.0 AS exitRate,(sum( IF (TIMESTAMPDIFF(MINUTE , ad.end_time , ad.report_time)<(fd.read_time_max+fd.dispose_time_max), 1, 0 ) )/COUNT(*))*100 AS responseRate\n" +
            "                        from df_audit_detail as ad\n" +
            "                                    inner join df_process dp\n" +
            "                                    on dp.process_name  = ad.process\n" +
            "                                    left join df_flow_data fd\n" +
            "                                    on ad.id =fd.data_id and ad.data_type=fd.data_type\n" +
            "${ew.customSqlSegment} " +
            "                                    GROUP BY  dp.process_name,dp.floor,dp.create_time"
    )
    List<DfAuditDetail> listByProcess( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> qw);

    @Select("select COUNT(*) AS question_num,process,dp.floor AS floor,dp.create_time,ad.question_type, \n" +
            "                        sum( IF (TIMESTAMPDIFF(MINUTE , ad.end_time , ad.report_time)<(fd.read_time_max+fd.dispose_time_max), 1, 0 ) ) AS respondPromptly,\n" +
            "                        COUNT(close_time) AS close_num,(COUNT(close_time)/COUNT(*))*100.0 AS exitRate,(sum( IF (TIMESTAMPDIFF(MINUTE , ad.end_time , ad.report_time)<(fd.read_time_max+fd.dispose_time_max), 1, 0 ) )/COUNT(*))*100 AS responseRate\n" +
            "                        from df_audit_detail as ad\n" +
            "                                    inner join df_process dp\n" +
            "                                    on dp.process_name  = ad.process\n" +
            "                                    left join df_flow_data fd\n" +
            "                                    on ad.id =fd.data_id and ad.data_type=fd.data_type\n" +
            "${ew.customSqlSegment} " +
            "                                    GROUP BY  dp.process_name,dp.floor,ad.question_type,dp.create_time"
    )
    List<DfAuditDetail> listByProcessHaveQuestionType( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> qw);

    @Select("select ad.*,dcsc.detail,fd.flow_level_name,fd.start_timeout,fd.overtime_status,fd.now_level_user ,fd.now_level_user_name " +

//            ",fd.level1_read_time,fd.level2_read_time,fd.level3_read_time,fd.read_time_max,fd.dispose_time_max,fd.status,fd.level1_affirm_time,fd.level2_affirm_time,fd.level3_affirm_time,fd.fa_ca_read_time,fd.show_approver, " +

            ",fd.level1_read_time,ad.project_name as projectName,ad.line as fLine,fd.level2_read_time,fd.level3_read_time,fd.read_time_max,fd.dispose_time_max,fd.status,fd.level1_affirm_time,fd.level2_affirm_time,fd.level3_affirm_time,fd.fa_ca_read_time,fd.show_approver, " +

            "date_format( date_sub( ad.report_time, INTERVAL 7 HOUR ), '%Y-%m-%d' ) date,\n"+
            "CASE \n"+
            "WHEN HOUR ( \n"+
            "date_sub( ad.report_time, INTERVAL 7 HOUR )) >= 7 \n"+
            "AND HOUR ( \n"+
            "date_sub( ad.report_time, INTERVAL 7 HOUR )) < 19 THEN \n"+
            "'白班' ELSE '晚班' \n"+
            "END AS shift \n"+
            "from df_audit_detail as ad  " +
            "inner join df_process dp \n" +
            "on dp.process_name  = ad.process   " +
            "left join df_flow_data fd " +
            "on ad.id =fd.data_id and ad.data_type=fd.data_type  " +
            "left join df_control_standard_config dcsc " +
            "on dcsc.id = ad.parent_id and ad.data_type = '稽查' " +
            "${ew.customSqlSegment}  ")
    List<DfAuditDetail> listByBigScreen( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("SELECT sum(if(end_time is null,1,0)) as id, sum(if(end_time is not null, 1, 0)) as parent_id " +
            "FROM `df_audit_detail` " +
            "inner join df_process dp \n" +
            "on dp.process_name  = process " +
            "${ew.customSqlSegment} ")
    DfAuditDetail getEndNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("select data_type, count(data_type) as id " +
            "from df_audit_detail " +
            "inner join df_process dp \n" +
            "on dp.process_name  = process " +
            "${ew.customSqlSegment} " +
            "GROUP BY data_type ")
    List<DfAuditDetail> getProjectClassNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("select question_type, count(question_type) as id " +
            "from df_audit_detail " +
            "inner join df_process dp \n" +
            "on dp.process_name  = process " +
            "${ew.customSqlSegment} " +
            "group by question_type ")
    List<DfAuditDetail> getQuestionNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);


//    @Select("select question_type,project_name, count(question_type) as id " +
//            "date_format(date_sub(dad.report_time,INTERVAL 7 HOUR ),'%Y-%m-%d') date,\n"+
//            "from df_audit_detail dad" +
//            "inner join df_process dp \n" +
//            "on dp.process_name  =dad.process " +
//            "${ew.customSqlSegment} " +
//            "group by question_type,date,project_name ")
//    List<DfAuditDetail> getQuestionNumJc(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

//    @Select("SELECT " +
//            "dad.question_type, " +
//            "dad.project_name, " +
//        //    "COUNT(dad.question_type) AS id, " +
//            "DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d') AS date, " +
//            "(COUNT(dad.question_type) / total.total_count) AS proportion, " +  // 计算占比
//            "total.total_count " +  // 获取总记录数
//            "FROM df_audit_detail dad " +
//            "INNER JOIN df_process dp ON dp.process_name = dad.process " +
//            "LEFT JOIN ( " +  // 计算每天的总记录数
//            "SELECT " +
//            "DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d') AS date, " +
//            "COUNT(*) AS total_count " +
//            "FROM df_audit_detail dad " +
//            "INNER JOIN df_process dp ON dp.process_name = dad.process " +
//            "${ew.customSqlSegment} "+
//            "GROUP BY DATE_FORMAT(DATE_SUB(report_time, INTERVAL 7 HOUR), '%Y-%m-%d') " +
//            ") AS total " +
//            "ON DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d') = total.date " +  // 连接子查询
//            "${ew.customSqlSegment} "+
//            "GROUP BY " +
//            "dad.question_type, " +
//            "dad.project_name, " +
//            "DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d'), " +  // 按 question_type, project_name 和 date 分组
//            "total.total_count"
//    )
//    List<DfAuditDetail> getQuestionNumJc(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);


    @Select("SELECT " +
            "  dad.question_type, " +
            "  dad.project_name, " +
            "  DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d') AS date, " +
            "  COUNT(*) AS total_count " +
            "FROM df_audit_detail dad " +
            "INNER JOIN df_process dp ON dp.process_name = dad.process " +
            "LEFT JOIN df_flow_data flow ON flow.data_id = dad.id " +
            "${ew.customSqlSegment} " +  // 支持动态 SQL
            "GROUP BY " +
            "  DATE_FORMAT(DATE_SUB(dad.report_time, INTERVAL 7 HOUR), '%Y-%m-%d'), " +
            "  dad.question_type, " +
            "  dad.project_name " +
            "ORDER BY date ")
    List<DfAuditDetail> getQuestionNumJc(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);





    @Select("SELECT " +
            "DATE(aud.report_time) AS date,  -- 按日期分组，显示日期\n" +
            "COUNT(*) AS totalPoints,  -- 问题点数：计算该日期的总记录数\n" +
            "sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) >= 60, 1, 0)) AS timeoutPoints,  -- 超时的记录数\n" +
            "sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) < 60, 1, 0)) AS onTimePoints,  -- 时效内的记录数\n" +
            "-- 计算超时比例\n" +
            "(sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) >= 60, 1, 0)) / COUNT(*)) * 100 AS timeoutPercentage,  \n" +
            "-- 计算时效内比例\n" +
            "(sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) < 60, 1, 0)) / COUNT(*)) * 100 AS onTimePercentage\n" +
            "FROM\n" +
            "`df_audit_detail` aud\n" +
            "INNER JOIN df_process dp ON dp.process_name = aud.process\n" +
            "LEFT JOIN df_flow_data flow ON flow.data_id = aud.id\n" +
            "${ew.customSqlSegment}  -- 动态 SQL 片段\n" +
            "GROUP BY\n" +
            "DATE(aud.report_time)  -- 按报告时间的日期部分进行分组\n" +
            "ORDER BY\n" +
            "DATE(aud.report_time) DESC;  -- 按日期降序排列结果")
    List<DfAuditDetail> getTimeoutData(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);


    @Select("SELECT " +
            "DATE(aud.report_time) AS date,  -- 按日期分组，显示日期\n" +
            "process, "+
            "COUNT(*) AS totalPoints,  -- 问题点数：计算该日期的总记录数\n" +
            "sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) >= 60, 1, 0)) AS timeoutPoints,  -- 超时的记录数\n" +
            "sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) < 60, 1, 0)) AS onTimePoints,  -- 时效内的记录数\n" +
            "-- 计算超时比例\n" +
            "(sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) >= 60, 1, 0)) / COUNT(*)) * 100 AS timeoutPercentage,  \n" +
            "-- 计算时效内比例\n" +
            "(sum(IF(TIMESTAMPDIFF(MINUTE, aud.report_time, aud.end_time) < 60, 1, 0)) / COUNT(*)) * 100 AS onTimePercentage\n" +
            "FROM\n" +
            "`df_audit_detail` aud\n" +
            "INNER JOIN df_process dp ON dp.process_name = aud.process\n" +
            "LEFT JOIN df_flow_data flow ON flow.data_id = aud.id\n" +
            "${ew.customSqlSegment}  -- 动态 SQL 片段\n" +
            "GROUP BY\n" +
            "DATE(aud.report_time) , -- 按报告时间的日期部分进行分组\n" +
            "process \n"+
            "ORDER BY\n" +
            "DATE(aud.report_time) DESC;  -- 按日期降序排列结果")
    List<DfAuditDetail> getProcessTimeoutData(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);




    @Select("SELECT " +
            "DATE(aud.report_time) AS date,  -- 按日期分组，显示日期\n" +
            "COUNT(*) AS totalPoints,  -- 问题点数：计算该日期的总记录数\n" +
            "SUM(IF(aud.decision_level = 'Level1', 1, 0)) AS Level1,  -- 统计 Level1 的问题点数\n" +
            "(SUM(IF(aud.decision_level = 'Level1', 1, 0)) / COUNT(*)) * 100 AS Level1Percentage,  -- Leve1 占比\n" +
            "SUM(IF(aud.decision_level = 'Level2', 1, 0)) AS Level2,  -- 统计 Level2 的问题点数\n" +
            "(SUM(IF(aud.decision_level = 'Level2', 1, 0)) / COUNT(*)) * 100 AS Level2Percentage,  -- Leve2 占比\n" +
            "SUM(IF(aud.decision_level = 'Level3', 1, 0)) AS Level3,  -- 统计 Level3 的问题点数\n" +
            "(SUM(IF(aud.decision_level = 'Level3', 1, 0)) / COUNT(*)) * 100 AS Level3Percentage  -- Leve3 占比\n" +
            "FROM\n" +
            "`df_audit_detail` aud\n" +
            "INNER JOIN df_process dp ON dp.process_name = aud.process\n" +
            "LEFT JOIN df_flow_data flow ON flow.data_id = aud.id\n" +
            "${ew.customSqlSegment}  -- 动态 SQL 片段\n" +
            "GROUP BY\n" +
            "DATE(aud.report_time)  -- 按报告时间的日期部分进行分组\n" +
            "ORDER BY\n" +
            "DATE(aud.report_time) DESC;  -- 按日期降序排列结果")
    List<DfAuditDetail> getAuditLevelSummary(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);




    @Select("SELECT " +
            "DATE(aud.report_time) AS date,  -- 按日期分组，显示日期\n" +
            "COUNT(*) AS totalPoints,  -- 问题点数：计算该日期的总记录数\n" +
            "SUM(IF(aud.end_time IS NOT NULL, 1, 0)) AS closedRecords,  -- 统计已关闭的记录数\n" +
            "SUM(IF(aud.end_time IS NULL, 1, 0)) AS openRecords  -- 统计未关闭的记录数\n" +
            "FROM\n" +
            "`df_audit_detail` aud\n" +
            "INNER JOIN df_process dp ON dp.process_name = aud.process\n" +
            "LEFT JOIN df_flow_data flow ON flow.data_id = aud.id\n" +
            "${ew.customSqlSegment}  -- 动态 SQL 片段\n" +
            "GROUP BY\n" +
            "DATE(aud.report_time)  -- 按报告时间的日期部分进行分组\n" +
            "ORDER BY\n" +
            "DATE(aud.report_time) DESC;  -- 按日期降序排列结果")
    List<DfAuditDetail> getAuditSummaryData(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);



    @Select("SELECT sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) as id,   " +
            "sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0)) as project_name, " +
            "sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) as department " +
            "FROM `df_audit_detail` aud " +
            "inner join df_process dp \n" +
            "on dp.process_name  = aud.process " +
            "left join df_flow_data flow on flow.data_id = aud.id " +
            "${ew.customSqlSegment} ")
    DfAuditDetail getTimeout(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("SELECT\n" +
            "\tsum( IF (TIMESTAMPDIFF(MINUTE ,aud.report_time, aud.end_time )>60, 1, 0 ) ) AS id,\n" +
            "\tsum( IF (TIMESTAMPDIFF(MINUTE , aud.report_time,  aud.end_time)<=60, 1, 0 ) ) AS project_name\n" +
            " ,sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) as department " +
            "\t\n" +
            "FROM\n" +
            "\t`df_audit_detail` aud \n" +
            "\tINNER JOIN df_process dp ON dp.process_name = aud.process " +
            "left join df_flow_data flow on flow.data_id = aud.id " +
            " ${ew.customSqlSegment} ")
    DfAuditDetail getTimeout2(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("select  count(d.id) as id " +
            "from df_audit_detail d join df_qms_ipqc_waig_total t on d.parent_id=t.id " +
            "${ew.customSqlSegment} "  )
   int getNgCountByMacCode(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);


    @Select("select w.f_mac as machineCode,fd.flow_level_name,fd.start_timeout,fd.overtime_status from df_audit_detail as ad  left join df_flow_data fd on ad.id =fd.data_id and ad.data_type=fd.data_type left join df_qms_ipqc_waig_total w on ad.parent_id=w.id  ${ew.customSqlSegment}  ")
    List<DfAuditDetail> listByCheckOverTime( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("select appear.f_barcode from df_audit_detail det " +
            "left join df_qms_ipqc_waig_total appear on det.parent_id = appear.id " +
            "${ew.customSqlSegment} " )
    DfQmsIpqcWaigTotal getAppearSnCodeById(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("select size.sn from df_audit_detail det " +
            "left join df_size_detail size on det.parent_id = size.id " +
            "${ew.customSqlSegment} ")
    DfSizeDetail getSizeSnCodeById(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("SELECT sum(if(end_time is null,1,0)) as id, sum(if(end_time is not null, 1, 0)) as parent_id,date_format(report_time,'%Y-%m-%d') report_time " +
            "FROM df_audit_detail " +
            "${ew.customSqlSegment} " +
            " GROUP BY date_format(report_time,'%Y-%m-%d') " +
            " ORDER BY report_time ASC")
    List<DfAuditDetail> getEndNumByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    @Select("SELECT sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) as id, " +
            "sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0)) as project_name, " +
            "sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) as department, " +
            "date_format(report_time,'%Y-%m-%d') as report_time " +
            "FROM df_audit_detail aud " +
            "left join df_flow_data flow on flow.data_id = aud.id " +
            "${ew.customSqlSegment} " +
            "GROUP BY date_format(report_time,'%Y-%m-%d') " +
            "ORDER BY report_time ASC")
    List<DfAuditDetail> getTimeoutByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);


    @Select("select \n" +
            "project_name as processName,\n" +
            "process,\n" +
            "mac_code as macCode,\n" +
            "data_type,\n" +
            "report_time as reportTime,\n" +
            "fa,\n" +
            "ca\n" +
            "from df_audit_detail\n" +
            "${ew.customSqlSegment} " +
            "ORDER BY processName,macCode asc")
    List<Map> exportAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper);


    @Select("select \n" +
            "d.data_type as dataType,\n"+
            "project_name as projectName,\n" +
            "d.process,\n" +
            "date_format( date_sub( d.report_time, INTERVAL 7 HOUR ), '%Y-%m-%d' ) date,\n"+
            "CASE \n"+
            "WHEN HOUR ( \n"+
            "date_sub( d.report_time, INTERVAL 7 HOUR )) >= 7 \n"+
            "AND HOUR ( \n"+
            "date_sub( d.report_time, INTERVAL 7 HOUR )) < 19 THEN \n"+
            "'白班' ELSE '晚班' \n"+
            "END AS shift, \n"+
            "d.line as fLine,\n"+
            "d.mac_code as fMac,\n"+
            "question_Type as questionType,\n"+
            "question_Name as questionName,\n"+
            "d.create_Name as createName,\n"+
            "report_time as reportTime,\n"+
            "end_time as endTime,\n"+
            "responsible,\n"+
            "responsible2,\n"+
            "responsible3,\n"+
            "responsible4,\n"+
            "decision_Level as decisionLevel,\n"+
            "scene_Practical as scenePractical,\n"+
            "problem_Detial as problemDetial,\n"+
            "affect_mac as affectMac,\n"+
            "handling_Sug as handlingSug,\n"+
            "fd.flow_level_name,fd.start_timeout,fd.overtime_status,fd.now_level_user ,fd.now_level_user_name " +
            ",fd.level1_read_time,fd.level2_read_time,fd.level3_read_time,fd.read_time_max,fd.dispose_time_max,fd.status,fd.level1_affirm_time,fd.level2_affirm_time,fd.level3_affirm_time,fd.fa_ca_read_time,fd.show_approver, " +
            "fa,\n" +
            "ca\n" +
            "from df_audit_detail d \n" +
            "inner join df_process dp\n" +
            "on dp.process_name  = d.process\n" +
            "left join df_flow_data fd " +
            "on d.id =fd.data_id and d.data_type=fd.data_type\n" +
            "LEFT JOIN df_control_standard_config dcsc ON dcsc.id = d.parent_id \n"+
            "${ew.customSqlSegment} ")
    List<DfAuditDetail> exportExcleAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper);


}
