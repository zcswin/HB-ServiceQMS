package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
public interface DfLeadDetailService extends IService<DfLeadDetail>, Excelable {

    Rate4 listLeadNum(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listOKRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listAllOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listWorkPositionOKRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listOkRateGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listAllNumGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<Rate3> listItemNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper,String selectNameString);

    List<Rate3> listMachineOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    List<DfLeadCheckItemInfos> listItemInfosJoinDetail(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

	List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfLeadDetail> qw);
}
