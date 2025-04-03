package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfBigScreenMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-05-16
 */
public interface DfBigScreenMenuMapper extends BaseMapper<DfBigScreenMenu> {

    @Select("select m.* from df_big_screen_menu m join df_user_relation_big_screen_menu u on m.id=u.menu_id    ${ew.customSqlSegment}  ")
    List<DfBigScreenMenu> listByBigScreen(@Param(Constants.WRAPPER) Wrapper<DfBigScreenMenu> wrapper);

}
