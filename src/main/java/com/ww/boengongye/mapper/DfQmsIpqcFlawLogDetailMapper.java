package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcFlawLogDetail;
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
 * @since 2024-06-21
 */
public interface DfQmsIpqcFlawLogDetailMapper extends BaseMapper<DfQmsIpqcFlawLogDetail> {


	@Select("select \n" +
			"bar_code as barCode,\n" +
			"log.process,\n" +
			"log.flaw_name flawName,\n" +
			"log.create_time as scanningTime,\n" +
			"GROUP_CONCAT(CONCAT(detail.process,'-',machine_code,'(',product_time,')') order by product_time asc SEPARATOR '->') as processThrough\n" +
			"from df_qms_ipqc_flaw_log log \n" +
			"LEFT JOIN df_qms_ipqc_flaw_log_detail detail on log.id = detail.parent_id\n" +
			"${ew.customSqlSegment} " +
			"GROUP BY bar_code,log.process,log.flaw_name,log.create_time")
	List<Map> exportRfidDetail(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcFlawLogDetail> ew);
}
