package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiDefect;
import com.ww.boengongye.entity.DfAoiUndetected;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI漏检记录表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
public interface DfAoiUndetectedMapper extends BaseMapper<DfAoiUndetected> {

	@Select("WITH TEMP_PIECE_LATEST AS (\n" +
			"        select * from (\n" +
			"         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
			"         FROM DF_AOI_PIECE\n" +
			"        )t\n" +
			"${ew.customSqlSegment} " +
			")\n" +
			",TEMP_FQC_LATEST AS (\n" +
			"    select * from (\n" +
			"        SELECT\n" +
			"            DF_AOI_DECIDE_LOG.*,\n" +
			"            USER.NAME,\n" +
			"            USER.ALIAS,\n" +
			"            USER.PROCESS,\n" +
			"            USER.FACTORY_ID,\n" +
			"            ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY QC_TIME DESC) NO\n" +
			"        FROM\n" +
			"            DF_AOI_DECIDE_LOG\n" +
			"        LEFT JOIN USER ON DF_AOI_DECIDE_LOG.QC_USER_CODE = USER.NAME\n" +
			"        WHERE\n" +
			"            USER.PROCESS = 'FQC'\n" +
			"    )t\n" +
			"    WHERE t.no = 1\n" +
			" )\n" +
			" ,temp_undetected_latest AS (\n" +
			"     select * FROM (\n" +
			"        select *,ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
			"        from DF_AOI_UNDETECTED\n" +
			"     )t\n" +
			"     WHERE no = 1\n" +
			")\n" +
			"-- 算出来fqc的total\n" +
			",temp_fqc_total as(\n" +
			"    select temp_fqc_latest.* ,(select count(0) from temp_fqc_latest) as total\n" +
			"    from temp_fqc_latest\n" +
			"    join temp_piece_latest on temp_fqc_latest.BAR_CODE = temp_piece_latest.BAR_CODE\n" +
			")\n" +
			"select FEATUREVALUES,count(*)/total rate\n" +
			"from temp_piece_latest\n" +
			"JOIN temp_fqc_total ON  temp_piece_latest.BAR_CODE = temp_fqc_total.BAR_CODE\n" +
			"JOIN temp_undetected_latest ON  temp_fqc_total.BAR_CODE = temp_undetected_latest.BARCODE\n" +
			"JOIN DF_AOI_DEFECT ON temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
			"GROUP BY FEATUREVALUES,total " +
			"ORDER BY rate desc " +
			"limit #{top}")
	List<Map<String, Object>> fqcNgTopRate(@Param(Constants.WRAPPER) QueryWrapper<DfAoiDefect> ew,
										   @Param("top") Integer top);
}
