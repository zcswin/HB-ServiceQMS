package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TZ测量 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
public interface DfTzDetailService extends IService<DfTzDetail>, Excelable {
    
    Rate4 listTzNum(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    List<Rate3> listAllNumGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);


    List<Rate3> listMachineOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    List<Rate3> listDateOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    List<Rate3> listOKRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    List<Rate3> listOkRateGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);


    List<Rate3> listItemNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper,String selectNameString);

    List<Rate3> listAllOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);


    List<Rate3> listWorkPositionOKRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    List<DfTzCheckItemInfos> listItemInfosJoinDetail(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

//    List<Rate3> getNgRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);
//
//    List<Rate3> getNgDetailRateTop10(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);
}
