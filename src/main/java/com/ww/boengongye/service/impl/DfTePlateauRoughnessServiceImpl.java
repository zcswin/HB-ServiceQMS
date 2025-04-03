package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfTePlateauRoughness;
import com.ww.boengongye.mapper.DfTePlateauRoughnessMapper;
import com.ww.boengongye.service.DfTePlateauRoughnessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台粗糙度 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-10
 */
@Service
public class DfTePlateauRoughnessServiceImpl extends ServiceImpl<DfTePlateauRoughnessMapper, DfTePlateauRoughness> implements DfTePlateauRoughnessService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        int count = 0;
        for (Map<String, String> map : maps) {
            DfTePlateauRoughness plateauRoughness = new DfTePlateauRoughness();
            plateauRoughness.setVendor(map.get("Vendor供应商"));
            plateauRoughness.setSite(map.get("Site"));
            plateauRoughness.setProject(map.get("Project项目"));
            plateauRoughness.setPhase(map.get("Phase描述"));
            plateauRoughness.setColor(map.get("Color颜色"));
            plateauRoughness.setTestName(map.get("TestName测试名称"));
            plateauRoughness.setProcessRecipe("ProcessRecipe");
            plateauRoughness.setProcessFixture(Integer.valueOf(map.get("ProcessFixture#")));
            plateauRoughness.setMeasurementDevice(map.get("MeasurementDevice"));
            plateauRoughness.setPartId(Integer.valueOf(map.get("PartID")));
            plateauRoughness.setTestLocation(Integer.valueOf(map.get("TestLocation")));
            plateauRoughness.setLogoStepHeight(map.get("LogoStepheight"));
            plateauRoughness.setLogoEdgeCrispness(map.get("Logoedgecrispness"));
            plateauRoughness.setSpec(map.get("Spec(Sq)标准"));
            plateauRoughness.setSq(Double.valueOf(map.get("Sq(Spec<2)")));
            plateauRoughness.setSsk(Double.valueOf(map.get("Ssk")));
            plateauRoughness.setSku(Double.valueOf(map.get("Sku")));
            plateauRoughness.setSal(Double.valueOf(map.get("Sal(Spec>3)")));
            plateauRoughness.setSk(Double.valueOf(map.get("Sk(Notfiltered)")));
            plateauRoughness.setSpk(Double.valueOf(map.get("Spk(Notfiltered)")));
            plateauRoughness.setSvk(Double.valueOf(map.get("Svk(Notfiltered)")));
            plateauRoughness.setSmr1(Double.valueOf(map.get("Smr1(Notfiltered)")));
            plateauRoughness.setSmr2(Double.valueOf(map.get("Smr2(Notfiltered)")));
            plateauRoughness.setSdq(Double.valueOf(map.get("Sdq(Spec<0.75)")));
            plateauRoughness.setSdr(Double.valueOf(map.get("Sdr")));
            plateauRoughness.setSsc(Double.valueOf(map.get("Ssc(Spec<1.75)")));
            plateauRoughness.setLength1(Double.valueOf(map.get("Length1")));
            plateauRoughness.setLength2(Double.valueOf(map.get("Length2")));
            plateauRoughness.setNumberOfMotifs1(Double.valueOf(map.get("Numberofmotifs1")));
            plateauRoughness.setHeightMean1(Double.valueOf(map.get("Height[Mean]1")));
            plateauRoughness.setAreaMean1(Double.valueOf(map.get("Area[Mean]1")));
            plateauRoughness.setPitchMean1(Double.valueOf(map.get("Pitch[Mean]1")));
            plateauRoughness.setMeanDiameterMean1(Double.valueOf(map.get("Meandiameter[Mean]1")));
            plateauRoughness.setNumberOfMotifs2(Double.valueOf(map.get("Numberofmotifs2")));
            plateauRoughness.setHeightMean2(Double.valueOf(map.get("Height[Mean]2")));
            plateauRoughness.setAreaMean2(Double.valueOf(map.get("Area[Mean]2")));
            plateauRoughness.setPitchMean2(Double.valueOf(map.get("Pitch[Mean]2")));
            plateauRoughness.setMeanDiameterMean2(Double.valueOf(map.get("Meandiameter[Mean]2")));
            plateauRoughness.setResult(map.get("Pass/Fail"));
            this.save(plateauRoughness);

            count++;
        }
        return count;
    }
}
