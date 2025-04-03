package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * EMD检测 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-13
 */
public interface DfEmdCheckItemInfosService extends IService<DfEmdCheckItemInfos> {

	Rate4 listLeadNum(QueryWrapper<DfEmdCheckItemInfos> qw);

	List<Rate3> listOKRate(QueryWrapper<DfLeadDetail> qw);

	List<Rate3> listAllNumGroupByDate(QueryWrapper<DfLeadDetail> qw);

	int importExcel2(MultipartFile file,String[] split) throws Exception;

	List<Rate3> listOkRateGroupByDate(QueryWrapper<DfLeadDetail> qw);

	List<Rate3> listWorkPositionOKRate(QueryWrapper<DfEmdCheckItemInfos> qw);

	List<Rate3> listWorkPositionOKRate2(QueryWrapper<DfLeadDetail> qw);

	int importExcel3(MultipartFile file, String[] split) throws Exception;

	List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfEmdCheckItemInfos> qw);
}
