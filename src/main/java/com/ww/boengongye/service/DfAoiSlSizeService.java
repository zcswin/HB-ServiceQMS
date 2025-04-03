package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfAoiSlSize;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * df_aoi尺寸表 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
public interface DfAoiSlSizeService extends IService<DfAoiSlSize> {

	int importExcel(MultipartFile file) throws Exception;

}
