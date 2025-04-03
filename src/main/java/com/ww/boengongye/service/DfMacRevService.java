package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfMacRev;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
public interface DfMacRevService extends IService<DfMacRev> {
    List<Integer> deleteTimeOut();
}
