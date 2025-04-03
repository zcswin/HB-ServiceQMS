package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfFlowDataUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-28
 */
public interface DfFlowDataUserMapper extends BaseMapper<DfFlowDataUser> {

    @Select("select distinct flow_data_id, u.name user_account " +
            "from df_flow_opinion op " +
            "left join user u on u.id = op.sender_id " +
            "where sender_id is not null and u.name is not null and op.create_time between #{startTime} and #{endTime}")
    List<DfFlowDataUser> insertDataFromOpinion(String startTime, String endTime);
}
