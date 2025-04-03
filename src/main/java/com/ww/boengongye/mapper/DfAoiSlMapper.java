package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiSl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-09-10
 */
public interface DfAoiSlMapper extends BaseMapper<DfAoiSl> {

	@Select("select max(if(ng_type = 'dl',number_of_repetitions,0)) as inte1 , " +
			"max(if(ng_type = 'da',number_of_repetitions,0)) as inte2 , " +
			"max(if(ng_type = 'db',number_of_repetitions,0)) as inte3 " +
			"from df_aoi_sl")
	Rate3 selectNumberOfRepetitions(@Param("ew") QueryWrapper<DfAoiSl> ew);
}
