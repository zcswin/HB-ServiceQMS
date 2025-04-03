package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProcessNeedMac;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfProcessNeedMac;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 工序需求机台数量表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
public interface DfProcessNeedMacMapper extends BaseMapper<DfProcessNeedMac> {
    @Select("SELECT pn.*, fac.factory_name,p.process_name FROM df_process_need_mac pn " +
            "left join df_factory fac on pn.factory_code = fac.factory_code " +
            "left join df_process p on pn.process_code = p.process_code " +
            "${ew.customSqlSegment}")
    IPage<DfProcessNeedMac> listJoinIds(IPage<DfProcessNeedMac> page, @Param(Constants.WRAPPER) Wrapper<DfProcessNeedMac> wrapper);
}
