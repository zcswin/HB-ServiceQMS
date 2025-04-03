package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfEmdCheckItemInfos;
import com.ww.boengongye.entity.DfLeadDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * EMD检测 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-09-13
 */
public interface DfEmdCheckItemInfosMapper extends BaseMapper<DfEmdCheckItemInfos> {

	@Select("select " +
			"sum(if(hour(check_time) between 5 and 6, 1, 0)) inte1, " +
			"sum(if(hour(check_time) between 7 and 8, 1, 0)) inte2, " +
			"sum(if(hour(check_time) between 9 and 10, 1, 0)) inte3, " +
			"sum(if(hour(check_time) between 11 and 12, 1, 0)) inte4, " +
			"sum(if(hour(check_time) between 13 and 14, 1, 0)) inte5, " +
			"sum(if(hour(check_time) between 15 and 16, 1, 0)) inte6, " +
			"sum(if(hour(check_time) between 17 and 18, 1, 0)) inte7, " +
			"sum(if(hour(check_time) between 19 and 20, 1, 0)) inte8, " +
			"sum(if(hour(check_time) between 21 and 22, 1, 0)) inte9, " +
			"sum(if(hour(check_time) = 23 or hour(check_time) = 0 , 1, 0)) inte10, " +
			"sum(if(hour(check_time) between 1 and 2, 1, 0)) inte11, " +
			"sum(if(hour(check_time) between 3 and 4, 1, 0)) inte12 " +
			"from df_emd_detail " +
			"${ew.customSqlSegment} ")
	Rate4 listLeadNum(@Param("ew") QueryWrapper<DfEmdCheckItemInfos> qw);


	@Select("SELECT  " +
			"DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
			"sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) inte1, " +
			"sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) inte2 " +
			"FROM `df_emd_detail` " +
			"${ew.customSqlSegment} " +
			"group by str1 " +
			"order by str1")
	List<Rate3> listAllNumGroupByDate(@Param("ew") QueryWrapper<DfLeadDetail> qw);

	@Select("select " +
			"sum(if(check_type = 1 and work_result = '合格', 1, 0)) / count(*) dou1," +
			"sum(if(work_result = '合格', 1, 0)) / count(*) dou2 " +
			"from df_emd_detail " +
			"${ew.customSqlSegment} ")
	List<Rate3> listOKRate(@Param("ew") QueryWrapper<DfLeadDetail> qw);


	@Select("SELECT  " +
			"DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
			"sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11 and work_result = '合格', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) dou1, " +
			"sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23 and work_result = '合格', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) dou2 " +
			"FROM `df_emd_detail` " +
			"${ew.customSqlSegment} " +
			"group by str1 " +
			"order by str1")
	List<Rate3> listOkRateGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

	@Select("SELECT machine_code str1, " +
			"sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11 and work_result = '合格', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) dou1 " +
			"FROM `df_emd_detail` " +
			"${ew.customSqlSegment} ")
	List<Rate3> listWorkPositionOKRate(@Param("ew") QueryWrapper<DfEmdCheckItemInfos> qw);


	@Select("select  machine str1, machine_code str2, " +
			"sum(if(work_result = '合格', 1, 0)) / count(*) dou1 " +
			"from df_emd_detail " +
			"${ew.customSqlSegment} " +
			"group by machine, machine_code " +
			"order by machine, machine_code")
	List<Rate3> listWorkPositionOKRate2(@Param("ew") QueryWrapper<DfLeadDetail> qw);


	@Select("select DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
			"FORMAT(sum(if(check_type = 1 and work_result = '合格', 1, 0))/count(0),2) dou1," +
			"FORMAT(sum(if(work_result = '合格', 1, 0))/count(0),2) dou2 " +
			"from df_emd_detail " +
			"${ew.customSqlSegment} " +
			"group by str1")
	List<Rate3> listDateOneAndMutilOkRate(@Param("ew") QueryWrapper<DfEmdCheckItemInfos> qw);
}
