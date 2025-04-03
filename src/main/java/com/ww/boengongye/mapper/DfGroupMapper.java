package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//import java.security.acl.Group;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
public interface DfGroupMapper extends BaseMapper<DfGroup> {

    @Select("select grp.*, pro.first_code " +
            "from df_group grp " +
            "left join df_process pro on grp.process = pro.process_name " +
            "${ew.customSqlSegment} ")
    List<DfGroup> listByProcess(@Param(Constants.WRAPPER) Wrapper<DfGroup> wrapper);

}
