package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTeCompressiveStrength;
import com.ww.boengongye.entity.DfTePlateauRoughness;
import com.ww.boengongye.mapper.DfTeCompressiveStrengthMapper;
import com.ww.boengongye.service.DfTeCompressiveStrengthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-16
 */
@Service
public class DfTeCompressiveStrengthServiceImpl extends ServiceImpl<DfTeCompressiveStrengthMapper, DfTeCompressiveStrength> implements DfTeCompressiveStrengthService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        int count = 0;
        for (Map<String, String> map : maps) {
            DfTeCompressiveStrength strength = new DfTeCompressiveStrength();
            strength.setNo(null == map.get("序号") ? null : Integer.valueOf(map.get("序号")));
            strength.setCreateDate(null == map.get("日期/时间") ? null : LocalDate.parse(map.get("日期/时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            strength.setDol(null == map.get("DOL") ? null : Double.valueOf(map.get("DOL")));
            strength.setDoc(null == map.get("DOC") ? null : Double.valueOf(map.get("DOC")));
            strength.setCs(null == map.get("CS") ? null : Double.valueOf(map.get("CS")));
            strength.setCsk(null == map.get("CSK") ? null : Double.valueOf(map.get("CSK")));
            strength.setCt(null == map.get("CT") ? null : Double.valueOf(map.get("CT")));
            save(strength);
            count++;
        }
        return count;
    }

}
