package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.Experiment;
import com.ww.boengongye.mapper.ExperimentMapper;
import com.ww.boengongye.service.ExperimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-04-28
 */
@Service
public class ExperimentServiceImpl extends ServiceImpl<ExperimentMapper, Experiment> implements ExperimentService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] mes = excel.readExcelBlock(4, 8, 2, 2);
        String testNo = mes[0][0];
        Timestamp submitTime = null == mes[1][0] ? null : Timestamp.valueOf(mes[1][0] + " 00:00:00");
        String machineCode = mes[2][0];
        String operator = mes[3][0];
        Timestamp testTime = null == mes[4][0] ? null : Timestamp.valueOf(mes[4][0] + " 00:00:00");
        String[][] values = excel.readExcelBlock(17, 48, 6, 6);
        List<Experiment> data = new ArrayList<>();
        for (String[] value : values) {
            Experiment e = new Experiment();
            e.setValue(null == value[0] ? null : Double.valueOf(value[0]));
            e.setTestNo(testNo);
            e.setSubmitTime(submitTime);
            e.setMachineCode(machineCode);
            e.setOperator(operator);
            e.setTestTime(testTime);
            e.setType("BPOL PU(N)");
            data.add(e);
        }
        saveBatch(data);
        return 0;
    }
}
