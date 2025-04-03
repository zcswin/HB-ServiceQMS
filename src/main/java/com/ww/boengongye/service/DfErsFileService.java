package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfErsFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * ERS文件 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
public interface DfErsFileService extends IService<DfErsFile> {
    ImportExcelResult importOrder(String filePath, MultipartFile file ) throws Exception;
}
