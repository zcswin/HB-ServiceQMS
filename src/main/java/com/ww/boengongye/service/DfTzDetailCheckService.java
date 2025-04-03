package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.entity.DfTzDetailCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TZ测量 服务类
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfTzDetailCheckService extends IService<DfTzDetailCheck> {
    List<Rate3> getNgRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetailCheck> wrapper);

    List<Rate3> getNgDetailRateTop10(@Param(Constants.WRAPPER) Wrapper<DfTzDetailCheck> wrapper);
}
