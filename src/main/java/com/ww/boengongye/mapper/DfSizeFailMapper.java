package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfSizeFail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfSizeFailMapper extends BaseMapper<DfSizeFail> {

    @Select("SELECT f.* FROM `df_size_fail` f left join df_size_detail d on f.parent_id=d.id left join df_size_cont_stand s on f.bad_type=s.test_item and d.process=s.process and d.project=s.project and d.item_name=s.color ${ew.customSqlSegment}")
    List<DfSizeFail> listKeyPoint(@Param(Constants.WRAPPER) Wrapper<DfSizeFail> wrapper);
}
