package com.ww.boengongye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAoiSl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-10
 */
public interface DfAoiSlService extends IService<DfAoiSl> {

	List<DfAoiSl> importSL(MultipartFile file) throws Exception;
}
