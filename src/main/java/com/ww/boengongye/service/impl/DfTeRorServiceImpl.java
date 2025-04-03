package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.entity.DfTeRor;
import com.ww.boengongye.mapper.DfTeRorMapper;
import com.ww.boengongye.service.DfTeRorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ror测试 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-14
 */
@Service
public class DfTeRorServiceImpl extends ServiceImpl<DfTeRorMapper, DfTeRor> implements DfTeRorService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] titleArray = {"","", "value"};
        List<Map<String, String>> common = excel.readExcelContentDIYFromTo(4, 8, titleArray);
        String[] strs = new String[5];
        for (int i = 0; i < common.size(); i++) {
            strs[i] = common.get(i).get("value");
        }
        String testCode = strs[0];
        LocalDateTime submitDate = null == strs[1] ? null : LocalDateTime.parse(strs[1] + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String machineCode = strs[2];
        String operator = strs[3];
        LocalDateTime testDate = null == strs[4] ? null : LocalDateTime.parse(strs[4] + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        int count = 0;
        String[] titleArray2 = {"", "样本号", "2D码", "S/F", "InstronMachine", "弯曲位移", "压缩载荷", "弯曲应力", "厚度",
                "表面应力1", "离子交换深度1", "表面应力2", "离子交换深度2", "中心应力"};
        List<DfTeRor> list = new ArrayList<>();
        List<Map<String, String>> maps = excel.readExcelContentDIYFromTo(16, 47, titleArray2);
        for (Map<String, String> map : maps) {
            DfTeRor data = new DfTeRor();
            data.setTestCode(testCode);
            data.setSubmitDate(submitDate);
            data.setMachineCode(machineCode);
            data.setOperator(operator);
            data.setTestDate(testDate);

            data.setSampleId(null == map.get("样本号") ? null : Integer.valueOf(map.get("样本号")));
            data.setSn2dCode(map.get("2D码"));
            data.setSf(null == map.get("S/F") ? null : Double.valueOf(map.get("S/F")));
            data.setInstronMachineCode(map.get("InstronMachine"));
            data.setDisplacement(null == map.get("弯曲位移") ? null : Double.valueOf(map.get("弯曲位移")));
            data.setBreakingLoad(null == map.get("压缩载荷") ? null : Double.valueOf(map.get("压缩载荷")));
            data.setBreakingStress(null == map.get("弯曲应力") ? null : Double.valueOf(map.get("弯曲应力")));
            data.setActualThickness(null == map.get("厚度") ? null : Double.valueOf(map.get("厚度")));
            data.setCs1(null == map.get("表面应力1") ? null : Double.valueOf(map.get("表面应力1")));
            data.setDol1(null == map.get("离子交换深度1") ? null : Double.valueOf(map.get("离子交换深度1")));
            data.setCs2(null == map.get("表面应力2") ? null : Double.valueOf(map.get("表面应力2")));
            data.setDol2(null == map.get("离子交换深度2") ? null : Double.valueOf(map.get("离子交换深度2")));
            data.setCt(null == map.get("中心应力") ? null : Double.valueOf(map.get("中心应力")));
            data.setFaceUp("Plateau up");
            if (data.getActualThickness() >= 0.52 && data.getActualThickness() <= 0.58 &&
                    data.getCs1() >= 80 && data.getCs1() <= 170 &&
                    data.getDol1() >= 110 && data.getDol1() <= 130 &&
                    data.getCs2() >= 610 && data.getCs2() <= 710 &&
                    data.getDol2() >= 4.2 && data.getDol2() <= 5 &&
                    data.getCt() >= 64 && data.getCt() <= 130)
                data.setResult("OK");
            else data.setResult("NG");

            list.add(data);
            count++;
        }

        common = excel.readExcelContentDIYFromTo(70, 74, titleArray);
        for (int i = 0; i < common.size(); i++) {
            strs[i] = common.get(i).get("value");
        }
        testCode = strs[0];
        submitDate = null == strs[1] ? null : LocalDateTime.parse(strs[1] + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        machineCode = strs[2];
        operator = strs[3];
        testDate = null == strs[4] ? null : LocalDateTime.parse(strs[4] + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        maps = excel.readExcelContentDIYFromTo(82, 113, titleArray2);
        for (Map<String, String> map : maps) {
            DfTeRor data = new DfTeRor();
            data.setTestCode(testCode);
            data.setSubmitDate(submitDate);
            data.setMachineCode(machineCode);
            data.setOperator(operator);
            data.setTestDate(testDate);

            data.setSampleId(null == map.get("样本号") ? null : Integer.valueOf(map.get("样本号")));
            data.setSn2dCode(map.get("2D码"));
            data.setSf(null == map.get("S/F") ? null : Double.valueOf(map.get("S/F")));
            data.setInstronMachineCode(map.get("InstronMachine"));
            data.setDisplacement(null == map.get("弯曲位移") ? null : Double.valueOf(map.get("弯曲位移")));
            data.setBreakingLoad(null == map.get("压缩载荷") ? null : Double.valueOf(map.get("压缩载荷")));
            data.setBreakingStress(null == map.get("弯曲应力") ? null : Double.valueOf(map.get("弯曲应力")));
            data.setActualThickness(null == map.get("厚度") ? null : Double.valueOf(map.get("厚度")));
            data.setCs1(null == map.get("表面应力1") ? null : Double.valueOf(map.get("表面应力1")));
            data.setDol1(null == map.get("离子交换深度1") ? null : Double.valueOf(map.get("离子交换深度1")));
            data.setCs2(null == map.get("表面应力2") ? null : Double.valueOf(map.get("表面应力2")));
            data.setDol2(null == map.get("离子交换深度2") ? null : Double.valueOf(map.get("离子交换深度2")));
            data.setCt(null == map.get("中心应力") ? null : Double.valueOf(map.get("中心应力")));
            data.setFaceUp("Plateau down");
            if (data.getActualThickness() >= 0.52 && data.getActualThickness() <= 0.58 &&
                    data.getCs1() >= 80 && data.getCs1() <= 170 &&
                    data.getDol1() >= 110 && data.getDol1() <= 130 &&
                    data.getCs2() >= 610 && data.getCs2() <= 710 &&
                    data.getDol2() >= 4.2 && data.getDol2() <= 5 &&
                    data.getCt() >= 64 && data.getCt() <= 130)
                data.setResult("OK");
            else data.setResult("NG");

            list.add(data);
            count++;
        }

        saveBatch(list);

        return count;
    }
}
