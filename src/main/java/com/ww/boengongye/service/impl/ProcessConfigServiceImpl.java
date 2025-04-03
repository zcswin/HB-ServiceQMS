package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.entity.ProcessConfig;
import com.ww.boengongye.mapper.ProcessConfigMapper;
import com.ww.boengongye.service.ProcessConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
@Service
public class ProcessConfigServiceImpl extends ServiceImpl<ProcessConfigMapper, ProcessConfig> implements ProcessConfigService {

    @Autowired
    ProcessConfigMapper ProcessConfigMapper;

    @Override
    public IPage<ProcessConfig> listByJoinPage(IPage<ProcessConfig> page, Wrapper<ProcessConfig> wrapper) {
        return ProcessConfigMapper.listByJoinPage(page, wrapper);
    }

    @Override
    public ImportExcelResult importOrder(Long userId, MultipartFile file,int factoryId,int lineBodyId) throws Exception {
        int successCount=0;
        int failCount=0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();



        List<ProcessConfig> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            ProcessConfig tc=new ProcessConfig();
            tc.setProjectName(map.get("项目名称"));
            tc.setProjectNumber(map.get("项目编号"));
            tc.setFactoryId(factoryId);

            return tc;
        }).collect(Collectors.toList());

        if(orderDetails.size()>0) {
            for(ProcessConfig c:orderDetails) {
                ProcessConfigMapper.insert(c);
            }


        }


        ImportExcelResult ter=new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }
}
