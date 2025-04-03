package com.ww.boengongye.testbi;

import com.ww.boengongye.entity.DfTeCompressiveStrength;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Excelable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Test implements Excelable {
    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] title = new String[]{"", "2D码", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
                "X", "Y", "Z", "AA", "AB", "AC"};
        List<Map<String, String>> maps = excel.readExcelContentDIYFromTo(4, 18, title);
        int count = 0;
        for (Map<String, String> map : maps) {
            System.out.println(map);
            count++;
        }
        return count;
    }
}
