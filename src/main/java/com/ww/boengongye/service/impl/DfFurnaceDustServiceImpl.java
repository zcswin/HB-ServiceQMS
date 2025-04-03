package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfFurnaceDust;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.mapper.DfFurnaceDustMapper;
import com.ww.boengongye.service.DfFurnaceDustService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Excelable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 炉内尘点 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-05
 */
@Service
public class DfFurnaceDustServiceImpl extends ServiceImpl<DfFurnaceDustMapper, DfFurnaceDust> implements DfFurnaceDustService {

    @Autowired
    private DfFurnaceDustMapper dfFurnaceDustMapper;

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] strings = excel.readExcelBlock(2, -1, excel.getColNumByLetter("A"), excel.getColNumByLetter("G"));
        for (String[] c : strings) {
            String testTime = c[0];
            if (null == c[1] || null == c[2] || null == c[3] || null == c[4] || null == c[5] || null == c[6]) continue;
            Integer um051 = Integer.valueOf(c[1]);
            Integer um51 = Integer.valueOf(c[2]);
            Integer um052 = Integer.valueOf(c[3]);
            Integer um52 = Integer.valueOf(c[4]);
            Integer um053 = Integer.valueOf(c[5]);
            Integer um53 = Integer.valueOf(c[6]);
            DfFurnaceDust data1 = new DfFurnaceDust();
            data1.setPosition("D6_2一层炉");
            data1.setCheckTime(Timestamp.valueOf(testTime));
            data1.setUm5(um51);
            data1.setUm05(um051);
            data1.setResult(um051 > 1000 || um51 > 10 ? "NG" : "OK");
            DfFurnaceDust data2 = new DfFurnaceDust();
            data2.setPosition("D6_2二层炉");
            data2.setCheckTime(Timestamp.valueOf(testTime));
            data2.setUm5(um52);
            data2.setUm05(um052);
            data2.setResult(um052 > 1000 || um52 > 10 ? "NG" : "OK");
            DfFurnaceDust data3 = new DfFurnaceDust();
            data3.setPosition("D6_2三层炉");
            data3.setCheckTime(Timestamp.valueOf(testTime));
            data3.setUm5(um53);
            data3.setUm05(um053);
            data3.setResult(um053 > 1000 || um53 > 10 ? "NG" : "OK");
            save(data1);
            save(data2);
            save(data3);
        }
        return strings.length * 3;
    }

    @Override
    public List<Rate3> getProcessOKRate(Wrapper<DfFurnaceDust> wrapper) {
        return dfFurnaceDustMapper.getProcessOKRate(wrapper);
    }

    @Override
    public List<Rate3> getAllPositionDustNum(Wrapper<DfFurnaceDust> wrapper) {
        return dfFurnaceDustMapper.getAllPositionDustNum(wrapper);
    }

    @Override
    public List<Rate3> getDustNumOrderByTime(Wrapper<DfFurnaceDust> wrapper, String format) {
        return dfFurnaceDustMapper.getDustNumOrderByTime(wrapper, format);
    }

    @Override
    public List<Rate4> getPositionDetail(Wrapper<DfFurnaceDust> wrapper) {
        return dfFurnaceDustMapper.getPositionDetail(wrapper);
    }
}
