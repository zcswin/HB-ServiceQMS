package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfBadItem;
import com.ww.boengongye.entity.DfProcessRelationBaditem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工序_不良项配置 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
public interface DfProcessRelationBaditemService extends IService<DfProcessRelationBaditem> {

    List<DfBadItem> listSelectedBadItem(@Param("process_id")Integer processId);

    List<DfBadItem> listUnselectedBadItem(@Param("process_id")Integer processId);

}
