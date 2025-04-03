package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.entity.DfTeInkOpticalDensity;
import com.ww.boengongye.mapper.DfTeInkOpticalDensityMapper;
import com.ww.boengongye.service.DfTeInkOpticalDensityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 油墨密度 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Service
public class DfTeInkOpticalDensityServiceImpl extends ServiceImpl<DfTeInkOpticalDensityMapper, DfTeInkOpticalDensity> implements DfTeInkOpticalDensityService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] titleArray = {"","索引","Ink1","Ink2","Ink3","Ink4"};
        List<Map<String, String>> maps = excel.readExcelContentDIY(12, titleArray);
        List<DfTeInkOpticalDensity> list = new ArrayList<>();
        int count = 0;
        for (Map<String, String> map : maps) {

            DfTeInkOpticalDensity data = new DfTeInkOpticalDensity();
            data.setNumber(null == map.get("索引") ? null : Integer.valueOf(map.get("索引")));
            data.setInk1(null == map.get("Ink1") ? null : Double.valueOf(map.get("Ink1")));
            data.setInk2(null == map.get("Ink2") ? null : Double.valueOf(map.get("Ink2")));
            data.setInk3(null == map.get("Ink3") ? null : Double.valueOf(map.get("Ink3")));
            data.setInk4(null == map.get("Ink1") ? null : Double.valueOf(map.get("Ink4")));
            if (data.getInk1() > 4.5 && data.getInk2() > 4.5 && data.getInk3() > 4.5 && data.getInk4() > 4.5) {
                data.setResult("OK");
            } else data.setResult("NG");
            data.setSubmitDate(LocalDateTime.now());

            list.add(data);
            count++;
        }
        saveBatch(list);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return count;
    }
}
