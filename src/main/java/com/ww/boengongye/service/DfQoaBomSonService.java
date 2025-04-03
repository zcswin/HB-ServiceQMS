package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfQoaBomSon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * QA-BOM- 收集产品生产过程需参考的文件-子流程 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-12-02
 */
public interface DfQoaBomSonService extends IService<DfQoaBomSon> {
    ImportExcelResult importOrder(String filePath, MultipartFile file ) throws Exception;
}
