package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfFlowDrawingFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 流程图纸 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
public interface DfFlowDrawingFileService extends IService<DfFlowDrawingFile> {
    ImportExcelResult importOrder(String filePath, MultipartFile file ) throws Exception;
}
