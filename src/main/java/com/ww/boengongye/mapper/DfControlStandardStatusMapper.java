package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 管控标准状态 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-12
 */
public interface DfControlStandardStatusMapper extends BaseMapper<DfControlStandardStatus> {

    @Select("SELECT\n" +
            "\ts.*,\n" +
            "\td.ipqc_number,\n" +
            "\tf.factory_name,\n" +
            "\tp.NAME AS project_name,\n" +
            "\tl.NAME AS line_body_name,\n" +
            "\tw.NAME AS workstation_name,\n" +
            "\tws.NAME AS workshop_section_name \n" +
            "FROM\n" +
            "\tdf_control_standard_status s\n" +
            "\tLEFT JOIN df_audit_detail d ON s.id = d.parent_id\n" +
            "\tLEFT JOIN df_factory f ON s.factory_id = f.factory_code\n" +
            "\tLEFT JOIN df_project p ON s.project_id = p.code\n" +
            "\tLEFT JOIN line_body l ON s.line_body_id = l.code\n" +
            "\tLEFT JOIN df_workstation w ON s.workstation_id = w.code\n" +
            "\tLEFT JOIN df_workshop_section ws ON s.workshop_section_id = ws.code ${ew.customSqlSegment}  ")
    IPage<DfControlStandardStatus> listByJoinPage(IPage<DfControlStandardStatus> page, @Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);


    @Select("select distinct(c.data_type) as create_name,count(s.id) as id from " +
            " df_control_standard_status s join df_control_standard_config c on s.control_standrad_id=c.id " +
            " ${ew.customSqlSegment} ")
    List<DfControlStandardStatus> listAllTypeCount(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

	// 查询每日统计数据
	@Select("SELECT \n" +
			" config.project as project_id,\n " +
			"    config.data_type AS create_name, \n" +
			"date_format( date_sub( status.create_time, INTERVAL 7 HOUR ), '%Y-%m-%d' ) date, \n" +
			"    COUNT(*) AS totalCount, \n" +
			"    SUM(CASE WHEN status.data_status = 'OK' THEN 1 ELSE 0 END) AS okCount, \n" +
			"    SUM(CASE WHEN status.data_status = 'NG' THEN 1 ELSE 0 END) AS ngCount \n" +
			"FROM " +
			"    df_control_standard_status AS status \n" +
			"JOIN \n" +
			"    df_control_standard_config AS config \n" +
			"ON " +
			"   STATUS.control_standrad_id=config.id  \n" +
			" ${ew.customSqlSegment} "+
			"GROUP BY " +
			" config.project, config.data_type, date \n" )
	List<DfControlStandardStatus> queryDailyData(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

	@Select(
			"SELECT\n" +
					"    DATE_FORMAT(DATE_SUB(STATUS.create_time, INTERVAL 7 HOUR), '%Y-%m') AS yearMonth,\n" +
					"    COUNT(*) AS totalCount,\n" +
					"    SUM(CASE WHEN STATUS.data_status = 'OK' THEN 1 ELSE 0 END) AS okCount,\n" +
					"    SUM(CASE WHEN STATUS.data_status != 'OK' THEN 1 ELSE 0 END) AS ngCount\n" +
					"FROM\n" +
					"    df_control_standard_status AS STATUS\n" +
					"JOIN df_control_standard_config AS config\n" +
					"    ON STATUS.control_standrad_id = config.id\n" +
					"WHERE\n" +
					"    STATUS.project_id LIKE #{projectId}\n" +
					"    AND config.data_type LIKE #{dataType}\n" +
					"    AND YEAR(STATUS.create_time) IN\n" +
					"    <foreach item=\"year\" collection=\"years\" open=\"(\" close=\")\" separator=\",\">\n" +
					"        #{year}\n" +
					"    </foreach>\n" +
					"GROUP BY\n" +
					"    YEAR(STATUS.create_time), MONTH(STATUS.create_time)"
	)
	List<DfControlStandardStatus> queryDateData(@Param("projectId") String projectId,@Param("dataType") String dataType,@Param("years") List<Integer> years);






    @Select("SELECT \n" +
            "  dp.process_name as processCode, \n" +
            "  dp.sort, \n" +
            "  dp.process_drl as processDrl, \n" +
            "  COALESCE(config.totalCount, 0) AS totalCount,\n" +
            "  COALESCE(config.okCount, 0) AS okCount,\n" +
            "  COALESCE(config.ngCount, 0) AS ngCount,\n" +
            "  CASE \n" +
            "    WHEN COALESCE(config.totalCount, 0) = 0 THEN 0.0 \n" +
            "    ELSE config.okCount * 1.0 / config.totalCount \n" +
            "  END AS okRate \n" +
            "FROM \n" +
            "  df_process dp \n" +
            "LEFT JOIN (\n" +
            "  SELECT\n" +
            "    config.process,\n" +
            "    COUNT(status.control_standrad_id) AS totalCount,\n" +
            "    SUM(CASE WHEN status.data_status = 'OK' THEN 1 ELSE 0 END) AS okCount,\n" +
            "    SUM(CASE WHEN status.data_status != 'OK' THEN 1 ELSE 0 END) AS ngCount\n" +
            "  FROM \n" +
            "    df_control_standard_config AS config\n" +
            "  LEFT JOIN\n" +
            "    df_control_standard_status AS status\n" +
            "      ON status.control_standrad_id = config.id\n" +
            "         AND status.create_time BETWEEN #{beginTime} AND #{endTime}\n" +
            "  GROUP BY \n" +
            "    config.process\n" +
            ") config ON config.process = dp.process_name\n" +
            " ${ew.customSqlSegment} \n" +
         //   "GROUP BY \n" +
         //   "  dp.process_name, dp.process_drl, dp.sort \n" +
            "ORDER BY dp.sort")
    List<DfControlStandardStatus> queryEachProcessData(
            @Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime );







	@Select("select count(s.id) as id,date_format(s.create_time,'%Y-%m-%d') create_time from " +
			" df_control_standard_status s join df_control_standard_config c on s.control_standrad_id=c.id " +
			" ${ew.customSqlSegment} " +
			" GROUP BY date_format(s.create_time,'%Y-%m-%d') " +
			" ORDER BY create_time ASC")
	List<DfControlStandardStatus> listCountByDay(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);


	@Select("select week factory_id , SUM(result) project_id \n" +
			"FROM(\n" +
			"select\n" +
			"YEARWEEK(report_time) week \n" +
			",IF(sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0)) = 0,0,0.4*sum(if(end_time is not null, 1, 0)) /((sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0))))) result\n" +
			"FROM df_audit_detail \n" +
			"where report_time BETWEEN #{startTime} and #{endTime}\n" +
			"and data_type != '设备状态'\n" +
			"GROUP BY YEARWEEK(report_time)\n" +
			"\n" +
			"union ALL\n" +
			"\n" +
			"SELECT\n" +
			"YEARWEEK(report_time) as week,\n" +
			"IF(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0))+sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))+sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) = 0,0,\n" +
			"\t0.4*sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) / \n" +
			"\t(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) \n" +
			"\t+ sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))\n" +
			"\t+ sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)))) AS result\n" +
			"FROM df_audit_detail aud\n" +
			"left join df_flow_data flow on flow.data_id = aud.id\n" +
			"where report_time BETWEEN #{startTime} and #{endTime}\n" +
			"and aud.data_type != '设备状态'\n" +
			"GROUP BY YEARWEEK(report_time)\n" +
			"\n" +
			"union all\n" +
			"\n" +
			"select t1.`week` , 0.2*(t1.id-t2.id)/t1.id result from \n" +
			"(\n" +
			"select count(s.id) as id,YEARWEEK(s.create_time) week \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN #{startTime} and #{endTime}\n" +
			"GROUP BY YEARWEEK(s.create_time)\n" +
			") t1\n" +
			"\n" +
			"left join \n" +
			"\n" +
			"(\n" +
			"select count(s.id) as id,YEARWEEK(s.create_time) week \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN #{startTime} and #{endTime} \n" +
			"and s.data_status = 'NG'\n" +
			"GROUP BY YEARWEEK(s.create_time)\n" +
			") t2\n" +
			"on t1.week = t2.week\n" +
			") result\n" +
			"GROUP BY week\n" +
			"ORDER BY week")
	List<DfControlStandardStatus> getComparationByWeek(String startTime, String endTime);


	@Select("select month factory_id , SUM(result) project_id \n" +
			"FROM(\n" +
			"select\n" +
			"DATE_FORMAT(report_time,'%Y%m') month \n" +
			",IF(sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0)) = 0,0,0.4*sum(if(end_time is not null, 1, 0)) /((sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0))))) result\n" +
			"FROM df_audit_detail \n" +
			"where report_time BETWEEN concat(#{startTime},'-01') and concat(LAST_DAY(concat(#{endTime},'-01')),' 23:59:59')\n" +
			"and data_type != '设备状态'\n" +
			"GROUP BY DATE_FORMAT(report_time,'%Y%m')\n" +
			"\n" +
			"union ALL\n" +
			"\n" +
			"SELECT\n" +
			"DATE_FORMAT(report_time,'%Y%m') as month,\n" +
			"IF(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0))+sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))+sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) = 0,0,\n" +
			"\t0.4*sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) / \n" +
			"\t(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) \n" +
			"\t+ sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))\n" +
			"\t+ sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)))) AS result\n" +
			"FROM df_audit_detail aud\n" +
			"left join df_flow_data flow on flow.data_id = aud.id\n" +
			"where report_time BETWEEN concat(#{startTime},'-01') and concat(LAST_DAY(concat(#{endTime},'-01')),' 23:59:59')\n" +
			"and aud.data_type != '设备状态'\n" +
			"GROUP BY DATE_FORMAT(report_time,'%Y%m')\n" +
			"\n" +
			"union all\n" +
			"\n" +
			"select t1.`month` , 0.2*(t1.id-t2.id)/t1.id result from \n" +
			"(\n" +
			"select count(s.id) as id,DATE_FORMAT(create_time,'%Y%m') month \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN concat(#{startTime},'-01') and concat(LAST_DAY(concat(#{endTime},'-01')),' 23:59:59')\n" +
			"GROUP BY DATE_FORMAT(create_time,'%Y%m')\n" +
			") t1\n" +
			"\n" +
			"left join \n" +
			"\n" +
			"(\n" +
			"select count(s.id) as id,DATE_FORMAT(create_time,'%Y%m') month \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN concat(#{startTime},'-01') and concat(LAST_DAY(concat(#{endTime},'-01')),' 23:59:59')\n" +
			"and s.data_status = 'NG'\n" +
			"GROUP BY DATE_FORMAT(create_time,'%Y%m')\n" +
			") t2\n" +
			"on t1.month = t2.month\n" +
			") result\n" +
			"GROUP BY month\n" +
			"ORDER BY month")
	List<DfControlStandardStatus> getComparationByMonth(@Param("startTime")String startTime, @Param("endTime") String endTime);


	@Select("select day factory_id , SUM(result) project_id , 99.09 as workshop_section_id\n" +
			"FROM(\n" +
			"select\n" +
			"DATE_FORMAT(report_time,'%Y-%m-%d') day \n" +
			",IF(sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0)) = 0,0,0.4*sum(if(end_time is not null, 1, 0)) /((sum(if(end_time is null,1,0)) + sum(if(end_time is not null, 1, 0))))) result\n" +
			"FROM df_audit_detail \n" +
			"where report_time BETWEEN #{startTime} and #{endTime}\n" +
			"and data_type != '设备状态'\n" +
			"GROUP BY DATE_FORMAT(report_time,'%Y-%m-%d')\n" +
			"\n" +
			"union ALL\n" +
			"\n" +
			"SELECT\n" +
			"DATE_FORMAT(report_time,'%Y-%m-%d') as day,\n" +
			"IF(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0))+sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))+sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)) = 0,0,\n" +
			"\t0.4*sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) / \n" +
			"\t(sum(if(aud.end_time is not null and flow.start_timeout is null,1 ,0)) \n" +
			"\t+ sum(if(aud.end_time is not null and flow.start_timeout is not null,1 ,0))\n" +
			"\t+ sum(if(aud.end_time is null and flow.start_timeout is not null,1 ,0)))) AS result\n" +
			"FROM df_audit_detail aud\n" +
			"left join df_flow_data flow on flow.data_id = aud.id\n" +
			"where report_time BETWEEN #{startTime} and #{endTime}\n" +
			"and aud.data_type != '设备状态'\n" +
			"GROUP BY DATE_FORMAT(report_time,'%Y-%m-%d')\n" +
			"\n" +
			"union all\n" +
			"\n" +
			"select t1.`day` , 0.2*(t1.id-t2.id)/t1.id result from \n" +
			"(\n" +
			"select count(s.id) as id,DATE_FORMAT(create_time,'%Y-%m-%d') day \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN #{startTime} and #{endTime}\n" +
			"GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d')\n" +
			") t1\n" +
			"\n" +
			"left join \n" +
			"\n" +
			"(\n" +
			"select count(s.id) as id,DATE_FORMAT(create_time,'%Y-%m-%d') day \n" +
			"from df_control_standard_status s \n" +
			"where s.create_time BETWEEN #{startTime} and #{endTime}\n" +
			"and s.data_status = 'NG'\n" +
			"GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d')\n" +
			") t2\n" +
			"on t1.day = t2.day\n" +
			") result\n" +
			"GROUP BY day\n" +
			"ORDER BY day\n")
	List<DfControlStandardStatus> getBaAndBielComparaion(@Param("startTime")String startTime, @Param("endTime") String endTime);
//	优化
	@Select("with time_new as(\n" +
			"\tselect date_format(c.datelist,'%Y-%c') checkTime\n" +
			"\tfrom calendar c\n" +
			" ${ew.customSqlSegment} " +
			"\tgroup by date_format(c.datelist,'%Y-%c')\n" +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%c') \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%c') \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dcsc.create_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by DATE_FORMAT(dcsc.create_time,'%Y-%c')\n" +
			")\n" +
			"select \n" +
			"time_new.checkTime str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			",dacs.score dou2 \n" +
			"from time_new\n" +
			"left join close_temp\n" +
			"on close_temp.checkTime = time_new.checkTime\n" +
			"left join dfd_new\n" +
			"on dfd_new.checkTime = time_new.checkTime\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.checkTime = time_new.checkTime\n" +
			"left join df_apple_check_score dacs \n" +
			"on dacs.check_time = time_new.checkTime")
	List<Rate3> getBaAndBielComparaion(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);


	@Select("with time_new as(\n" +
			"\tselect date_format(c.datelist,'%Y-%c') checkTime\n" +
			"\tfrom calendar c\n" +
			" ${ew.customSqlSegment} " +
			"\tgroup by date_format(c.datelist,'%Y-%c')\n" +
			")\n" +
			"select\n" +
			"dacs.check_time str1\n" +
			",dacs.score dou1\n" +
			"from time_new\n" +
			"left join df_apple_check_score dacs\n" +
			"on dacs.check_time = time_new.checkTime\n" +
			"having dou1 is not null")
	List<Rate3> getBaComparaion(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


	@Select("with time_new as(\n" +
			"\tselect date_format(c.datelist,'%Y-%c') checkTime\n" +
			"\tfrom calendar c\n" +
			" ${ew.customSqlSegment} " +
			"\tgroup by date_format(c.datelist,'%Y-%c')\n" +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%c') \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%c') \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dcsc.create_time,'%Y-%c') checkTime\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by DATE_FORMAT(dcsc.create_time,'%Y-%c')\n" +
			")\n" +
			"select \n" +
			"time_new.checkTime str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			"from time_new\n" +
			"left join close_temp\n" +
			"on close_temp.checkTime = time_new.checkTime\n" +
			"left join dfd_new\n" +
			"on dfd_new.checkTime = time_new.checkTime\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.checkTime = time_new.checkTime ")
	List<Rate3> getComparaionMonth(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);

	@Select("with time_new as(\n" +
			"\tselect date_format(c.datelist,'%Y-%U') checkTime\n" +
			"\tfrom calendar c\n" +
			" ${ew.customSqlSegment} " +
			"\tgroup by date_format(c.datelist,'%Y-%U')\n" +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%U') checkTime\n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%U') \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%U') checkTime\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%U') \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dcsc.create_time,'%Y-%U') checkTime\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by DATE_FORMAT(dcsc.create_time,'%Y-%U')\n" +
			")\n" +
			"select \n" +
			"time_new.checkTime str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			"from time_new\n" +
			"left join close_temp\n" +
			"on close_temp.checkTime = time_new.checkTime\n" +
			"left join dfd_new\n" +
			"on dfd_new.checkTime = time_new.checkTime\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.checkTime = time_new.checkTime")
	List<Rate3> getComparaionWeek(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);

	@Select("with time_new as(\n" +
			"\tselect date_format(c.datelist,'%Y-%m-%d') checkTime\n" +
			"\tfrom calendar c\n" +
			" ${ew.customSqlSegment} " +
			"\tgroup by date_format(c.datelist,'%Y-%m-%d')\n" +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%m-%d') checkTime\n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%m-%d') \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dad.report_time,'%Y-%m-%d') checkTime\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY DATE_FORMAT(dad.report_time,'%Y-%m-%d') \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tDATE_FORMAT(dcsc.create_time,'%Y-%m-%d') checkTime\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by DATE_FORMAT(dcsc.create_time,'%Y-%m-%d')\n" +
			")\n" +
			"select \n" +
			"time_new.checkTime str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			"from time_new\n" +
			"left join close_temp\n" +
			"on close_temp.checkTime = time_new.checkTime\n" +
			"left join dfd_new\n" +
			"on dfd_new.checkTime = time_new.checkTime\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.checkTime = time_new.checkTime")
	List<Rate3> getComparaionDay(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);

	@Select("with process_new as(\n" +
			"\tselect dp.process_name process\n" +
			"\tfrom df_process dp \n" +
			" ${ew.customSqlSegment} " +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tdad.process\n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tgroup by dad.process \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tdad.process \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY dad.process \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tdcsc.process\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by dcsc.process \n" +
			")\n" +
			"select \n" +
			"process_new.process str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			"from process_new\n" +
			"left join close_temp\n" +
			"on close_temp.process = process_new.process\n" +
			"left join dfd_new\n" +
			"on dfd_new.process = process_new.process\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.process = process_new.process")
	List<Rate3> getComparaionProcess(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);


	@Select("with factory_new as(\n" +
			"\tselect df.factory_name factory\n" +
			"\tfrom df_factory df\n" +
			")\n" +
			",close_temp as(\n" +
			"\tselect \n" +
			"\tif(dad.factory is null,'J10-1',dad.factory) factory  \n" +
			"\t,sum(if(dad.end_time is not null,1,0)) closeNumber\n" +
			"\t,count(0) total\n" +
			"\t,sum(if(dad.end_time is not null,1,0))/count(0)*100 dou1\n" +
			"\tFROM df_audit_detail dad \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY dad.factory \n" +
			")\n" +
			",dfd_new as (\n" +
			"\tselect \n" +
			"\tif(dad.factory is null,'J10-1',dad.factory) factory  \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0)) inte1 \n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)) inte2\n" +
			"\t,sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t/(sum(if(dad.end_time is not null and dfd.start_timeout is null,1,0))\n" +
			"\t+sum(if(dad.end_time is not null and dfd.start_timeout is not null,1,0)))*100 dou1\n" +
			"\tfrom df_audit_detail dad \n" +
			"\tinner join df_flow_data dfd \n" +
			"\ton dfd.data_id = dad.id \n" +
			" ${ew_1.customSqlSegment} " +
			"\tGROUP BY dad.factory \n" +
			")\n" +
			",dcsc_new as(\n" +
			"\tselect \n" +
			"\tif(dcsc.factory is null,'J10-1',dcsc.factory) factory\n" +
			"\t,sum(if(dcss.data_status='OK',1,0)) inte1  \n" +
			"\t,count(0) inte2\n" +
			"\t,sum(if(dcss.data_status='OK',1,0))/count(0)*100 dou1\n" +
			"\tfrom df_control_standard_config dcsc\n" +
			"\tinner join df_control_standard_status dcss \n" +
			"\ton dcss.control_standrad_id = dcsc.id \n" +
			" ${ew_2.customSqlSegment} " +
			"\tgroup by dcsc.factory \n" +
			")\n" +
			"select \n" +
			"factory_new.factory str1\n" +
			",if(close_temp.dou1 is null,0,close_temp.dou1)*0.4\n" +
			"+if(dfd_new.dou1 is null,0,dfd_new.dou1)*0.4\n" +
			"+if(dcsc_new.dou1 is null,0,dcsc_new.dou1)*0.2 dou1\n" +
			"from factory_new\n" +
			"left join close_temp\n" +
			"on close_temp.factory = factory_new.factory\n" +
			"left join dfd_new\n" +
			"on dfd_new.factory = factory_new.factory\n" +
			"left join dcsc_new\n" +
			"on dcsc_new.factory = factory_new.factory")
	List<Rate3> getComparaionFactory(@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);
}
