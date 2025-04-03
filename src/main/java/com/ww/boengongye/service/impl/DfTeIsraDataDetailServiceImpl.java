package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.entity.DfTeIsraDataDetail;
import com.ww.boengongye.mapper.DfTeIsraDataDetailMapper;
import com.ww.boengongye.service.DfTeIsraDataDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 17.4-ISRA Data 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Service
public class DfTeIsraDataDetailServiceImpl extends ServiceImpl<DfTeIsraDataDetailMapper, DfTeIsraDataDetail> implements DfTeIsraDataDetailService {

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] titleArray = {"", "OY", "BGWY", "PCY", "O", "OY2", "BGWY2", "PCY2", "O2", "B", "S", "OON"
                , "1CHT", "1CFR", "1CFL", "1FHSTR", "1FHSBR", "1FHSTL", "1FHSML", "1FHSBL", "3ACBR", "3ACBL", "3ACTR", "4AGT", "4BGB"
                , "5CWC", "7ASL", "7ASR", "7BST", "7BSB", "8EPCR", "8FPCB", "8GPCC", "8HPCM", "8IPSC", "9CRD", "9DRD", "11ACP"
                , "11BCT", "11CCML", "11DCMR", "11ECBL", "11FCBR", "11GPTR", "11HPML", "11IPMR", "11JPBL", "11KPBR", "11ICM", "11MPM"
                , "12AF", "12PF", "20AL", "20BW"};
        List<Map<String, String>> maps = excel.readExcelContentDIY(14, titleArray);
        List<DfTeC6Cuc3Ums6> list = new ArrayList<>();
        int count = 0;
        for (Map<String, String> map : maps) {
            System.out.println(map);
            count++;
        }
        //saveBatch(list);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return count;
    }
}
