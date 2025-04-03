package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.DfLiableManService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 责任人 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
@Service
public class DfLiableManServiceImpl extends ServiceImpl<DfLiableManMapper, DfLiableMan> implements DfLiableManService {

    @Autowired
    private DfLiableManMapper dfLiableManMapper;

    @Override
    @Transactional
    public int importExcel(MultipartFile file, String factoryName, String processName) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        int count = 0;
        for (Map<String, String> map : maps) {
            DfLiableMan liableMan = new DfLiableMan();
            liableMan.setFactoryName(factoryName);
            liableMan.setSectionName(map.get("工段名称"));
            liableMan.setStationName(map.get("工段名称"));
            liableMan.setProcessName(processName);
            liableMan.setProblemLevel(map.get("问题等级"));
            liableMan.setLiableManName(map.get("责任人"));
            liableMan.setLiableManCode(map.get("责任人工号"));
            liableMan.setDayOrNight(map.get("班次"));
            liableMan.setCreateMan(map.get("创建人"));
            dfLiableManMapper.insert(liableMan);
            count++;
        }
        return count;
    }
}
