package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTePlateauGloss;
import com.ww.boengongye.mapper.DfTePlateauGlossMapper;
import com.ww.boengongye.service.DfTePlateauGlossService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台光泽度 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-14
 */
@Service
public class DfTePlateauGlossServiceImpl extends ServiceImpl<DfTePlateauGlossMapper, DfTePlateauGloss> implements DfTePlateauGlossService {

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        int count = 0;
        for (Map<String, String> map : maps) {
            DfTePlateauGloss plateauGloss = new DfTePlateauGloss();
            plateauGloss.setVendor(map.get("Vendor供应商"));
            plateauGloss.setSite(map.get("Site"));
            plateauGloss.setProject(map.get("Project项目"));
            plateauGloss.setPhase(map.get("Phase描述"));
            plateauGloss.setLine(map.get("Line"));
            plateauGloss.setDate(null == map.get("Date") ? null : LocalDateTime.parse(map.get("Date") + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            plateauGloss.setShift(map.get("Shift"));
            plateauGloss.setConfig(map.get("Config"));
            plateauGloss.setColor(map.get("Color颜色"));
            plateauGloss.setTestName(map.get("TestName测试名称"));
            plateauGloss.setLastProcessbeforeMeasurement(map.get("LastProcessbeforeMeasurement工艺"));
            plateauGloss.setDwelltimesinceProcess(map.get("DwelltimesinceProcess"));
            plateauGloss.setKeyProcessMachineName(map.get("KeyProcessMachineName"));
            plateauGloss.setKeyProcessMachineID(Integer.valueOf(map.get("KeyProcessMachine#")));
            plateauGloss.setProcessRecipe(map.get("ProcessRecipe"));
            plateauGloss.setProcessFixtureID(Integer.valueOf(map.get("ProcessFixture#")));
            plateauGloss.setMeasurementDevice(map.get("MeasurementDevice"));
            plateauGloss.setMeasurementDeviceID(Integer.valueOf(map.get("MeasurementDevice#")));
            plateauGloss.setPartID(Integer.valueOf(map.get("PartID")));
            plateauGloss.setTestLocation(Integer.valueOf(map.get("TestLocation测量点位")));
            plateauGloss.setMeasure1(Double.valueOf(map.get("Measure1第一次")));
            plateauGloss.setMeasure2(Double.valueOf(map.get("Measure2第二次")));
            plateauGloss.setMeasure3(Double.valueOf(map.get("Measure3第三次")));
            plateauGloss.setMeasure4(Double.valueOf(map.get("Measure4第四次")));
            plateauGloss.setMeasure5(Double.valueOf(map.get("Measure5第五次")));
            plateauGloss.setGlossAverage(Double.valueOf(map.get("GlossAverage平均值")));
            plateauGloss.setSpec(map.get("Spec标准"));
            plateauGloss.setResult(map.get("Failure/Pass"));
            this.save(plateauGloss);
            count++;
        }
        return count;
    }
}
