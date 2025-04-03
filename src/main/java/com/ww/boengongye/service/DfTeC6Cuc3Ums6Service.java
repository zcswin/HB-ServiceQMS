package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * C6_CNC3_UMS6检测数据 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-12-06
 */
public interface DfTeC6Cuc3Ums6Service extends IService<DfTeC6Cuc3Ums6>, Excelable {
    List<DfTeC6Cuc3Ums6> listTop10NG(@Param(Constants.WRAPPER) Wrapper<DfTeC6Cuc3Ums6> wrapper);
}
