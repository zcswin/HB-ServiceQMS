package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFurnaceDust;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 炉内尘点 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-05
 */
public interface DfFurnaceDustService extends IService<DfFurnaceDust>, Excelable {

    List<Rate3> getProcessOKRate(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);

    List<Rate3> getAllPositionDustNum(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);

    List<Rate3> getDustNumOrderByTime(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper, String format);

    List<Rate4> getPositionDetail(@Param(Constants.WRAPPER) Wrapper<DfFurnaceDust> wrapper);

}
