package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfProblemCa;
import com.ww.boengongye.entity.DfProblemFa;
import com.ww.boengongye.mapper.DfProblemFaMapper;
import com.ww.boengongye.service.DfProblemCaService;
import com.ww.boengongye.service.DfProblemFaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-06-06
 */
@Service
public class DfProblemFaServiceImpl extends ServiceImpl<DfProblemFaMapper, DfProblemFa> implements DfProblemFaService {

    @Autowired
    private DfProblemCaService dfProblemCaService;

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        String projectType = "尺寸";
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] block = excel.readExcelBlock2(3, 6, 2, 5);
        List<DfProblemFa> faList = new ArrayList<>();
        List<DfProblemCa> caList = new ArrayList<>();
        for (String[] row : block) {

            for (String reason : row[2].split("\n")) {
                DfProblemFa fa = new DfProblemFa();
                fa.setProjectType(projectType);
                fa.setType(row[0]);
                fa.setProblem(row[1]);
                //fa.setFa(reason.split("、")[1]);
                fa.setFa(reason);
                faList.add(fa);
                System.out.println("Fa:" + fa);
            }

            for (String reason : row[3].split("\n")) {
                DfProblemCa ca = new DfProblemCa();
                ca.setProjectType(projectType);
                ca.setType(row[0]);
                ca.setProblem(row[1]);
                ca.setCa(reason.split("\\.")[1]);
                caList.add(ca);
                System.out.println("Ca:" + ca);
            }
        }
        saveBatch(faList);
        dfProblemCaService.saveBatch(caList);


        return faList.size();
    }
}
