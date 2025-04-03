package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.entity.DfTeThermalCycling;
import com.ww.boengongye.mapper.DfTeThermalCyclingMapper;
import com.ww.boengongye.service.DfTeThermalCyclingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试_冷热循环+百格 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-27
 */
@Service
public class DfTeThermalCyclingServiceImpl extends ServiceImpl<DfTeThermalCyclingMapper, DfTeThermalCycling> implements DfTeThermalCyclingService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] title = new String[]{"", "B", "", "", "", "", "", "", "", "", "", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "",
                "", "", "", "", "", "", "", "", "", "", "AH"};
        List<Map<String, String>> maps = excel.readExcelContentDIYFromTo(4, 18, title);
        List<DfTeThermalCycling> list = new ArrayList<>();
        int count = 0;
        for (Map<String, String> map : maps) {

            DfTeThermalCycling data = new DfTeThermalCycling();
            data.setQRcode(map.get("B"));
            data.setSn(Integer.valueOf(map.get("L")));
            data.setLocation(map.get("M"));
            data.setPreL(Double.valueOf(map.get("N")));
            data.setPreA(Double.valueOf(map.get("O")));
            data.setPreB(Double.valueOf(map.get("P")));
            data.setPostL(Double.valueOf(map.get("Q")));
            data.setPostA(Double.valueOf(map.get("R")));
            data.setPostB(Double.valueOf(map.get("S")));
            data.setShiftL(Double.valueOf(map.get("T")));
            data.setShiftA(Double.valueOf(map.get("U")));
            data.setShiftB(Double.valueOf(map.get("V")));
            data.setShiftE94(Double.valueOf(map.get("AH")));
            data.setResult("OK");

            list.add(data);
            count++;
        }
        saveBatch(list);


        return count;
    }
}
