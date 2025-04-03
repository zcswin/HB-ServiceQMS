package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcFlawConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-29
 */
public interface DfQmsIpqcFlawConfigService extends IService<DfQmsIpqcFlawConfig>, Excelable {
    List<DfQmsIpqcFlawConfig> listByJoin(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);
    List<DfQmsIpqcFlawConfig> listDistinct(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);
    List<DfQmsIpqcFlawConfig> listDistinctArea(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

    List<DfQmsIpqcFlawConfig> listDistinctAreaAndProcess(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcFlawConfig> wrapper);

    int importOrder(MultipartFile file) throws Exception;
}
