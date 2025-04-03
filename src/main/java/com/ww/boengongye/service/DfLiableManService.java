package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfLiableMan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 责任人 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
public interface DfLiableManService extends IService<DfLiableMan> {

    int importExcel(MultipartFile file, String factoryName, String processName) throws Exception;

}
