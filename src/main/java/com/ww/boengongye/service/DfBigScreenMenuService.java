package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfBigScreenMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-05-16
 */
public interface DfBigScreenMenuService extends IService<DfBigScreenMenu> {
    List<DfBigScreenMenu> listByBigScreen(@Param(Constants.WRAPPER) Wrapper<DfBigScreenMenu> wrapper);

}
