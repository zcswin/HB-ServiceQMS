package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.LineBody;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
public interface LineBodyService extends IService<LineBody> {
    IPage<LineBody> listJoinIds(IPage<LineBody> page, @Param(Constants.WRAPPER) Wrapper<LineBody> wrapper);
}
