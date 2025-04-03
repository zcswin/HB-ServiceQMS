package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfFacaConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfFacaConfigService extends IService<DfFacaConfig> {
    ImportExcelResult importOrder(MultipartFile file)throws Exception;
}
