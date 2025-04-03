package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfAoiSlThick;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.SlCloseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 丝印-厚度 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
public interface DfAoiSlThickMapper extends BaseMapper<DfAoiSlThick> {

	@Select("select * FROM (\n" +
			"    -- 展示厚度表格信息\n" +
			"SELECT\n" +
			"    DF_AUDIT_DETAIL.CREATE_TIME,\n" +
			"    df_flow_data.STATUS,\n" +
			"    df_aoi_sl_thick.color,\n" +
			"    df_aoi_sl_thick.number_of_repetitions,\n" +
			"    DF_AUDIT_DETAIL.DATA_TYPE,\n" +
			"    QUESTION_NAME,\n" +
			"    CASE when hour(DATE_SUB(CHECK_TIME, INTERVAL 7 HOUR)) BETWEEN 0 AND  11 THEN '白班' ELSE '晚班' END clazz,\n" +
			"    df_aoi_sl_thick.FACTORY,df_aoi_sl_thick.PROCESS,MODEL,LINE_BODY,\n" +
			"    USER_NAME,CHECK_TIME,END_TIME,FA,CA,responsible,df_flow_data.REMARK\n" +
			"from DF_AOI_SL_THICK\n" +
			"join df_audit_detail\n" +
			"ON DF_AOI_SL_THICK.ID = DF_AUDIT_DETAIL.PARENT_ID AND DF_AUDIT_DETAIL.DATA_TYPE = '丝印厚度'\n" +
			"JOIN df_flow_data ON DF_FLOW_DATA.DATA_ID = df_audit_detail.ID\n" +
			"union all\n" +
			"-- 展示颜色表格信息\n" +
			"select\n" +
			"    DF_AUDIT_DETAIL.CREATE_TIME,\n" +
			"    df_flow_data.STATUS,\n" +
			"    DF_AOI_SL.color,\n" +
			"    DF_AOI_SL.NUMBER_OF_REPETITIONS,\n" +
			"    DF_AUDIT_DETAIL.DATA_TYPE,\n" +
			"    QUESTION_NAME,\n" +
			"    CASE when hour(DATE_SUB(TIME, INTERVAL 7 HOUR)) BETWEEN 0 AND  11 THEN '白班' ELSE '晚班' END clazz,\n" +
			"    DF_AOI_SL.FACTORY,DF_AOI_SL.PROCESS,MODEL,LINE_BODY,\n" +
			"    PRODUCE_DRI,TIME,END_TIME,FA,CA,responsible,df_flow_data.REMARK\n" +
			"FROM DF_AOI_SL\n" +
			"join df_audit_detail\n" +
			"ON DF_AOI_SL.ID = DF_AUDIT_DETAIL.PARENT_ID AND DF_AUDIT_DETAIL.DATA_TYPE = '丝印颜色'\n" +
			"JOIN df_flow_data ON DF_FLOW_DATA.DATA_ID = df_audit_detail.ID\n" +
			") as t\n" +
			"${ew.customSqlSegment} ")
	List<SlCloseEntity> getCloseData1(@Param("ew") QueryWrapper<DfAoiSlThick> ew);

	@Select("")
	List<SlCloseEntity> getCloseDataZ(QueryWrapper<DfAoiSlThick> ew);

	@Select("SELECT line str1, sum(if(END_TIME IS NOT NULL,1,0 )) / count(*)  dou1 \n" +
			"from DF_AUDIT_DETAIL\n" +
			"${ew.customSqlSegment} " +
			"GROUP BY LINE\n" +
			"ORDER BY dou1 desc\n" +
			"LIMIT 5")
	List<Rate3> getworstLine(@Param("ew") QueryWrapper<DfAoiSlThick> ew);

	@Select("SELECT date_format(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR), '%m-%d') str1,\n" +
			"       sum(if(HOUR(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) inte1,\n" +
			"       sum(if(HOUR(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) inte2,\n" +
			"       sum(if(HOUR(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR)) between 0 and 11 AND END_TIME IS NOT NULL, 1, 0) ) / count(*) dou1,\n" +
			"       sum(if(HOUR(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR)) between 12 and 23 AND END_TIME IS NOT NULL, 1, 0) ) / count(*) dou2,\n" +
			"       sum(if(HOUR(DATE_SUB(REPORT_TIME, INTERVAL 7 HOUR)) between 0 and 23 and end_time is not null , 1 , 0) ) / count(*) dou3 " +
			"from DF_AUDIT_DETAIL\n" +
			"${ew.customSqlSegment} " +
			"GROUP BY str1\n" +
			"ORDER BY str1")
	List<Rate3> getCloseUp(@Param("ew") QueryWrapper<DfAoiSlThick> ew);
}
