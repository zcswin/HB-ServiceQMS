package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
public interface DfProjectService extends IService<DfProject> {
    IPage<DfProject> listJoinIds(IPage<DfProject> page, @Param(Constants.WRAPPER) Wrapper<DfProject> wrapper);
}
