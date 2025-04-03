package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfTeC6Cuc3Ums6;
import com.ww.boengongye.entity.DfTeCompressiveStrength;
import com.ww.boengongye.mapper.DfTeC6Cuc3Ums6Mapper;
import com.ww.boengongye.service.DfTeC6Cuc3Ums6Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Excelable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Native;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * C6_CNC3_UMS6检测数据 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-06
 */
@Service
public class DfTeC6Cuc3Ums6ServiceImpl extends ServiceImpl<DfTeC6Cuc3Ums6Mapper, DfTeC6Cuc3Ums6> implements DfTeC6Cuc3Ums6Service {

    @Autowired
    private DfTeC6Cuc3Ums6Mapper dfTeC6Cuc3Ums6Mapper;

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] titleArray = {"时间","索引","结果","平台长","平台宽","平台中心_X","平台中心_Y","平台左边距","平台左边距2",
                "平台顶边距","平台顶边距2","长边外形面倒角","F03","P1","P2","P3","P4","P5","厚度极差","机台号","检测结果","机台状态"};
        List<Map<String, String>> maps = excel.readExcelContentDIY(8, titleArray);
        List<DfTeC6Cuc3Ums6> list = new ArrayList<>();
        int count = 0;
        for (Map<String, String> map : maps) {
            DfTeC6Cuc3Ums6 data = new DfTeC6Cuc3Ums6();

            data.setTestTime(null == map.get("时间") ? null : LocalDateTime.parse(map.get("时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.setIndexing(null == map.get("索引") ? null : Integer.valueOf(map.get("索引")));
            data.setResult(map.get("结果"));
            data.setPlatformLength(null == map.get("平台长") ? null : Double.valueOf(map.get("平台长")));
            data.setPlatformWidth(null == map.get("平台宽") ? null : Double.valueOf(map.get("平台宽")));
            data.setPlatformCenterX(null == map.get("平台中心_X") ? null : Double.valueOf(map.get("平台中心_X")));
            data.setPlatformCenterY(null == map.get("平台中心_Y") ? null : Double.valueOf(map.get("平台中心_Y")));
            data.setPlatformLeftMargin(null == map.get("平台左边距") ? null : Double.valueOf(map.get("平台左边距")));
            data.setPlatformLeftMargin2(null == map.get("平台左边距2") ? null : Double.valueOf(map.get("平台左边距2")));
            data.setPlatformTopMargin(null == map.get("平台顶边距") ? null : Double.valueOf(map.get("平台顶边距")));
            data.setPlatformTopMargin2(null == map.get("平台顶边距2") ? null : Double.valueOf(map.get("平台顶边距2")));
            data.setLongsideChamfer(null == map.get("长边外形面倒角") ? null : Double.valueOf(map.get("长边外形面倒角")));
            data.setF03(null == map.get("F03") ? null : Double.valueOf(map.get("F03")));
            data.setP1(null == map.get("P1") ? null : Double.valueOf(map.get("P1")));
            data.setP2(null == map.get("P2") ? null : Double.valueOf(map.get("P2")));
            data.setP3(null == map.get("P3") ? null : Double.valueOf(map.get("P3")));
            data.setP4(null == map.get("P4") ? null : Double.valueOf(map.get("P4")));
            data.setP5(null == map.get("P5") ? null : Double.valueOf(map.get("P5")));
            data.setThicknessRange(null == map.get("厚度极差") ? null : Double.valueOf(map.get("厚度极差")));
            data.setMachineCode(map.get("机台号"));
            data.setTestResult(map.get("检测结果"));
            data.setMachineStatus(map.get("机台状态"));

            list.add(data);
            count++;
        }
        saveBatch(list);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return count;
    }

    @Override
    public List<DfTeC6Cuc3Ums6> listTop10NG(Wrapper<DfTeC6Cuc3Ums6> wrapper) {
        return dfTeC6Cuc3Ums6Mapper.listTop10NG(wrapper);
    }
}
