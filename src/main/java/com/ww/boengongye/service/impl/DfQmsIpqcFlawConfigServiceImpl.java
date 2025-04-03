package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfQmsIpqcFlawConfig;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfQmsIpqcFlawConfigMapper;
import com.ww.boengongye.service.DfQmsIpqcFlawConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-29
 */
@Service
public class DfQmsIpqcFlawConfigServiceImpl extends ServiceImpl<DfQmsIpqcFlawConfigMapper, DfQmsIpqcFlawConfig> implements DfQmsIpqcFlawConfigService {

    @Autowired
    DfQmsIpqcFlawConfigMapper DfQmsIpqcFlawConfigMapper;

    @Override
    public List<DfQmsIpqcFlawConfig> listByJoin(Wrapper<DfQmsIpqcFlawConfig> wrapper) {
        return DfQmsIpqcFlawConfigMapper.listByJoin(wrapper);
    }

    @Override
    public List<DfQmsIpqcFlawConfig> listDistinct(Wrapper<DfQmsIpqcFlawConfig> wrapper) {
        return DfQmsIpqcFlawConfigMapper.listDistinct(wrapper);
    }

    @Override
    public List<DfQmsIpqcFlawConfig> listDistinctArea(Wrapper<DfQmsIpqcFlawConfig> wrapper) {
        return DfQmsIpqcFlawConfigMapper.listDistinctArea(wrapper);
    }

    @Override
    public List<DfQmsIpqcFlawConfig> listDistinctAreaAndProcess(Wrapper<DfQmsIpqcFlawConfig> wrapper) {
        return DfQmsIpqcFlawConfigMapper.listDistinctAreaAndProcess(wrapper);
    }

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        List<Map<String, Object>> maps = this.listMaps();
        Set<DfQmsIpqcFlawConfig> allData = new HashSet<>();
        for (Map<String, Object> map : maps) {
            DfQmsIpqcFlawConfig data = new DfQmsIpqcFlawConfig();
            data.setTestType(map.get("test_type").toString());
            data.setProcess(map.get("process").toString());
            data.setProject(map.get("project").toString());
            data.setBigArea(map.get("big_area").toString());
            data.setFlawName(map.get("flaw_name").toString());

            allData.add(data);
        }

        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[] titles = new String[]{"外观类型","工序","型号","大区域","缺陷明细"};
        List<Map<String, String>> maps2 = excel.readExcelContentDIY(1, titles);
        List<DfQmsIpqcFlawConfig> addList = new ArrayList<>();
        for (Map<String, String> map : maps2) {
            DfQmsIpqcFlawConfig data = new DfQmsIpqcFlawConfig();
            data.setTestType(map.get("外观类型"));
            data.setProcess(map.get("工序"));
            data.setProject(map.get("型号"));
            data.setBigArea(map.get("大区域"));
            data.setFlawName(map.get("缺陷明细"));

            if (!allData.contains(data)) {
                addList.add(data);
                System.out.println(data);
            }
        }
        this.saveBatch(addList);
        return addList.size();
    }

    @Override
    public int importOrder(MultipartFile file) throws Exception {

        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfQmsIpqcFlawConfig> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfQmsIpqcFlawConfig tc = new DfQmsIpqcFlawConfig();
            tc.setTestType(map.get("外观类型"));
            tc.setProcess(map.get("工序"));
            tc.setProject(map.get("型号"));
            tc.setBigArea(map.get("大区域"));
            tc.setFlawName(map.get("缺陷明细"));

            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            Set<DfQmsIpqcFlawConfig> set = new LinkedHashSet<>(orderDetails);
            for (DfQmsIpqcFlawConfig c : set) {
                this.save(c);
            }


        }







        return orderDetails.size();
    }
}
