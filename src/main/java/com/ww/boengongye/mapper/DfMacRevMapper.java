package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfMacRev;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Mapper
public interface DfMacRevMapper extends BaseMapper<DfMacRev> {

    @Select("select t.id from ( " +
            "select *, ROW_NUMBER() OVER (PARTITION BY MachineCode order BY pub_time desc) as 'rank' from df_mac_rev) t " +
            "where t.rank > 20 ")
    List<Integer> deleteTimeOut();
}
