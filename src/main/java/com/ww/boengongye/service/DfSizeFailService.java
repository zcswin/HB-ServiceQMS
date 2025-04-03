package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeFail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfSizeFailService extends IService<DfSizeFail> {
    List<DfSizeFail> listKeyPoint(@Param(Constants.WRAPPER) Wrapper<DfSizeFail> wrapper);
}
