package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfMacModelPosition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-08-08
 */
public interface DfMacModelPositionService extends IService<DfMacModelPosition> {
    List<DfMacModelPosition> listJoinAppearance();
}
