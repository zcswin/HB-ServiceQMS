package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFacaConfig;
import com.ww.boengongye.entity.DfSizeContStand;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfFacaConfigMapper;
import com.ww.boengongye.service.DfFacaConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Service
public class DfFacaConfigServiceImpl extends ServiceImpl<DfFacaConfigMapper, DfFacaConfig> implements DfFacaConfigService {

    @Autowired
    DfFacaConfigMapper dfFacaConfigMapper;

    public ImportExcelResult importOrder(MultipartFile file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfFacaConfig> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfFacaConfig tc = new DfFacaConfig();
            tc.setType("FA");
            tc.setContent(map.get("FA"));
            tc.setProcess(map.get("工序"));
            tc.setBadItem(map.get("不良项"));
            tc.setQuestionType(map.get("分类"));
            tc.setReceiptType(map.get("类型"));
            tc.setWeight(null == map.get("权重") ? null : Double.valueOf(map.get("权重")));

            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            Set<DfFacaConfig> set = new LinkedHashSet<>(orderDetails);
            for (DfFacaConfig c : set) {
//                QueryWrapper<DfFacaConfig> qw=new QueryWrapper<>();
//                qw.eq("process",c.getProcess());
//                qw.eq("test_item",c.getTestItem());
//
//                qw.last("limit 1");
//                DfFacaConfig dd=dfFacaConfigMapper.selectOne(qw);
//                if(null!=dd){
//                    c.setId(dd.getId());
//                    c.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                    dfFacaConfigMapper.updateById(c);
//                }else{
                    dfFacaConfigMapper.insert(c);
//                }

            }


        }


        List<DfFacaConfig> orderDetails2 = maps.stream().filter(Objects::nonNull).map(map -> {
            DfFacaConfig tc = new DfFacaConfig();
            tc.setType("CA");
            tc.setContent(map.get("CA"));
            tc.setProcess(map.get("工序"));
            tc.setBadItem(map.get("不良项"));
            tc.setQuestionType(map.get("分类"));
            tc.setReceiptType(map.get("类型"));
            tc.setWeight(null == map.get("权重") ? null : Double.valueOf(map.get("权重")));

            return tc;
        }).collect(Collectors.toList());

        if (orderDetails2.size() > 0) {
            Set<DfFacaConfig> set = new LinkedHashSet<>(orderDetails2);
            for (DfFacaConfig c : set) {
//                QueryWrapper<DfFacaConfig> qw=new QueryWrapper<>();
//                qw.eq("process",c.getProcess());
//                qw.eq("test_item",c.getTestItem());
//
//                qw.last("limit 1");
//                DfFacaConfig dd=dfFacaConfigMapper.selectOne(qw);
//                if(null!=dd){
//                    c.setId(dd.getId());
//                    c.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                    dfFacaConfigMapper.updateById(c);
//                }else{
                dfFacaConfigMapper.insert(c);
//                }

            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }
}
