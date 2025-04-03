package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfWorkmanship;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.mapper.DfWorkmanshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-26
 */
public interface DfWorkmanshipService extends IService<DfWorkmanship> {

    int importExcel(MultipartFile file, String factoryCode, String lineCode) throws Exception;
}
