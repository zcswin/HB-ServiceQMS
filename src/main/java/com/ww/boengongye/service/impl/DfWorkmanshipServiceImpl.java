package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfWorkmanship;
import com.ww.boengongye.mapper.DfWorkmanshipMapper;
import com.ww.boengongye.service.DfWorkmanshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-26
 */
@Service
public class DfWorkmanshipServiceImpl extends ServiceImpl<DfWorkmanshipMapper, DfWorkmanship> implements DfWorkmanshipService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file, String factoryCode, String lineCode) throws Exception {
        ExcelImportUtil excelImportUtil = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excelImportUtil.readExcelContent();
        List<DfWorkmanship> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            DfWorkmanship dfWorkmanship = new DfWorkmanship();
            dfWorkmanship.setFactoryCode(factoryCode);
            dfWorkmanship.setLineCode(lineCode);
            dfWorkmanship.setProjectCode(map.get("项目编号"));
            dfWorkmanship.setProjectName(map.get("项目名称"));
            list.add(dfWorkmanship);
        }
        saveBatch(list);
        return list.size();
    }

}
