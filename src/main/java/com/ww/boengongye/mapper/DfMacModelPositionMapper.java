package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfMacModelPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-08-08
 */
public interface DfMacModelPositionMapper extends BaseMapper<DfMacModelPosition> {
    @Select("select m.* ,s.StatusID_Cur as status,s.pub_time as statusTime from df_mac_model_position m join df_mac_status_appearance s on m.MachineCode=s.MachineCode")
    List<DfMacModelPosition> listJoinAppearance();
}
