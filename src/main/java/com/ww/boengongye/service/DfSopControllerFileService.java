package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfSopControllerFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * SOP/SIP/MSOP控制文件 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
public interface DfSopControllerFileService extends IService<DfSopControllerFile> {
    ImportExcelResult importOrder(String filePath, MultipartFile file ) throws Exception;
}
