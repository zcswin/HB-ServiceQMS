package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfSizeContStand;
import com.ww.boengongye.entity.DfTeInkOpticalDensity;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfSizeContStandMapper;
import com.ww.boengongye.service.DfSizeContStandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 尺寸管控标准 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
@Service
public class DfSizeContStandServiceImpl extends ServiceImpl<DfSizeContStandMapper, DfSizeContStand> implements DfSizeContStandService {

    @Autowired
    DfSizeContStandMapper dfSizeContStandMapper;

    @Transactional
    public int importExcel2(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        int testEndCul = 40;
        String process = "覆膜";
        String[] title = new String[500];
        String[][] table = new String[500][150];  // 存储测量项 和上下限
        for (int i = 0; i < 150; i++) {
            title[i] = i + "";
        }
        List<Map<String, String>> maps = excel.readExcelContentDIY(0, title);

        int j = 0;
        for (Map<String, String> map : maps) {
            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                table[j][i++] = entry.getValue();
            }

            j++;
            if (j >= 500) {
                break;
            }
        }

        for (int i = 0; i < 500; i++) {
            for (int k = 0; k < 150; k++) {
                if (null != table[i][k])
                    System.out.print(table[i][k]);
            }
            System.out.println();
        }

        List<DfSizeContStand> standList = new ArrayList<>();
        for (int i = 1; i < testEndCul; i++) {
            DfSizeContStand stand = new DfSizeContStand();
            stand.setTestItem(table[0][i]);
            stand.setFai(table[1][i]);
            stand.setStandard(Double.valueOf(table[2][i]));
            stand.setUpperToler(null == table[3][i] ? null : Double.valueOf(table[3][i]));
            stand.setLowerToler(null == table[4][i] ? null : Double.valueOf(table[4][i]));
            stand.setUpperLimit(null == table[5][i] ? null : Double.valueOf(table[5][i]));
            stand.setLowerLimit(null == table[6][i] ? null : Double.valueOf(table[6][i]));
            stand.setIsolaUpperLimit(Double.valueOf(table[7][i]));
            stand.setIsolaLowerLimit(Double.valueOf(table[8][i]));
            stand.setFactory(null);
            stand.setProcess(process);
            stand.setLine(null);
            stand.setKeyPoint("0");
            System.out.println(stand);
            standList.add(stand);
        }

        saveBatch(standList);




        return standList.size();
    }

    public ImportExcelResult  importOrder( MultipartFile file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfSizeContStand> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfSizeContStand tc = new DfSizeContStand();
            tc.setTestItem(map.get("尺寸描述"));
            tc.setProject(map.get("项目"));
            System.out.println(tc.getTestItem());
            tc.setStandard(Double.valueOf(map.get("标准")));
//            tc.setUpperToler(Double.valueOf(map.get("正公差")));
//            tc.setLowerToler(Double.valueOf(map.get("负公差")));
            tc.setUpperToler(Double.valueOf(map.get("公差")));
            tc.setLowerToler(Double.valueOf(map.get("公差"))-Double.valueOf(map.get("公差"))-Double.valueOf(map.get("公差")));
            tc.setUpperLimit(Double.valueOf(map.get("调机上限")));
            tc.setLowerLimit(Double.valueOf(map.get("调机下限")));
            tc.setIsolaUpperLimit(Double.valueOf(map.get("隔离上限")));
            tc.setIsolaLowerLimit(Double.valueOf(map.get("隔离下限")));

            tc.setFai(map.get("FAI"));
            tc.setProcess(map.get("工序"));
            tc.setKeyPoint(map.get("是否重点项"));
            tc.setSort(Integer.parseInt(map.get("序号")));
            tc.setColor(map.get("颜色"));
            tc.setIsUse(1);


            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            for (DfSizeContStand c : orderDetails) {
                QueryWrapper<DfSizeContStand> qw=new QueryWrapper<>();
                qw.eq("process",c.getProcess());
                qw.eq("test_item",c.getTestItem());
                qw.eq("project",c.getProject());
                qw.eq("color",c.getColor());

                qw.last("limit 1");
                DfSizeContStand dd=dfSizeContStandMapper.selectOne(qw);
                if(null!=dd){
                    c.setId(dd.getId());
                    c.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    dfSizeContStandMapper.updateById(c);
                }else{
                    dfSizeContStandMapper.insert(c);
                }

            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }
}
