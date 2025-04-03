package com.ww.boengongye.mapper;

//import com.baomidou.dynamic.datasource.annotation.DS;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFlowData;
import com.ww.boengongye.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT u.*,d.name as departmentName " +
            "FROM `user` u " +
            "left join user_department d " +
            "on u.department_id=d.id   " +
            "${ew.customSqlSegment}")
    IPage<User> listJoinPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

    @Select("select u.*,df.factory_name " +
            "from `user` u " +
            "left join df_factory df " +
            "on u.factory_id =df.id " +
            "${ew.customSqlSegment}")
    IPage<User> getUserListBySearch(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

    @Select("select u.*,df.factory_name " +
            "from `user` u " +
            "left join df_factory df " +
            "on u.factory_id =df.id ")
    List<User> getAllList();

    @Select("select u.name ,u.alias ,u.process,u.grade,dasp.classCategory \n" +
            "from `user` u \n" +
            "left join df_aoi_seat_protect dasp \n" +
            "on dasp.user_id = u.id " +
            "${ew.customSqlSegment}")
    User getAoiUserByUserCode(@Param(Constants.WRAPPER)Wrapper<User> wrapper);

}
