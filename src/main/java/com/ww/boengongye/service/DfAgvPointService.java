package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfAgvPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-14
 */
public interface DfAgvPointService extends IService<DfAgvPoint> {
    ImportExcelResult importOrder( MultipartFile file ) throws Exception;
}
