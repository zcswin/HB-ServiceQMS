package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfSizeContStand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 尺寸管控标准 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
public interface DfSizeContStandService extends IService<DfSizeContStand> {
    int importExcel2(MultipartFile file) throws Exception;
     ImportExcelResult importOrder(MultipartFile file)throws Exception;
}
