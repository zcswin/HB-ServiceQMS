package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.entity.ProcessConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
public interface ProcessConfigService extends IService<ProcessConfig> {
    IPage<ProcessConfig> listByJoinPage(IPage<ProcessConfig> page , @Param(Constants.WRAPPER) Wrapper<ProcessConfig> wrapper);

    ImportExcelResult importOrder(Long userId, MultipartFile file,int factoryId,int lineBodyId) throws Exception;
}
