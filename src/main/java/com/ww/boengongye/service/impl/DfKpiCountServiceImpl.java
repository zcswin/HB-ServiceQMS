package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfKpiCount;
import com.ww.boengongye.mapper.DfKpiCountMapper;
import com.ww.boengongye.service.DfKpiCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * KPI计算 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-01
 */
@Service
public class DfKpiCountServiceImpl extends ServiceImpl<DfKpiCountMapper, DfKpiCount> implements DfKpiCountService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfKpiCount> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            DfKpiCount data = new DfKpiCount();
            data.setFactory(map.get("FactoryID工厂"));
            data.setLine(map.get("LineID线体"));
            data.setProject(map.get("ProjectID项目"));
            data.setWorkshop(map.get("WorkShopID工段").equals("null") || map.get("WorkShopID工段").equals("NULL") ? null : map.get("WorkShopID工段"));
            data.setWorkstation(map.get("WorkStationID工站").equals("null") || map.get("WorkStationID工站").equals("NULL") ? null : map.get("WorkStationID工站"));
            data.setProcess(map.get("ProcessID工序"));
            data.setType(null == map.get("Type类别") ? null : Integer.valueOf(map.get("Type类别")));
            data.setCheckRate(null == map.get("CheckRate稽核率") ? null : Double.valueOf(map.get("CheckRate稽核率")));
            data.setCreateUserCode(null == map.get("CreateUser发起人工号") ? null : Integer.valueOf(map.get("CreateUser发起人工号")));
            data.setOkNum(null == map.get("OKNumOK数量") ? null : Integer.valueOf(map.get("OKNumOK数量")));
            data.setNgNum(null == map.get("NGNumNG数量") ? null : Integer.valueOf(map.get("NGNumNG数量")));
            data.setTypeCheck(map.get("TypeCheck稽核类别"));
            data.setCreateTime(null == map.get("CreateTime发起时间") ? null : LocalDateTime.parse(map.get("CreateTime发起时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            list.add(data);
        }
        saveBatch(list);
        return list.size();
    }
}
