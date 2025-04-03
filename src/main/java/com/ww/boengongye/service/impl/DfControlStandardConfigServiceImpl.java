package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.mapper.DfControlStandardConfigMapper;
import com.ww.boengongye.service.DfControlStandardConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 管控标准配置 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Service
public class DfControlStandardConfigServiceImpl extends ServiceImpl<DfControlStandardConfigMapper, DfControlStandardConfig> implements DfControlStandardConfigService {

    @Autowired
    DfControlStandardConfigMapper DfControlStandardConfigMapper;


    public ImportExcelResult importOrder(String factory,String project,String process,String line, MultipartFile file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfControlStandardConfig> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfControlStandardConfig tc = new DfControlStandardConfig();
            tc.setDataType(map.get("管控类型名称"));
            tc.setDataClassify(map.get("分类"));
            tc.setDetail(map.get("细项"));
            tc.setStandardValue(map.get("标准值/方法"));
            tc.setFrequency(map.get("抽检频率"));
            tc.setMachine(map.get("设备"));
            tc.setFactory(factory);
            tc.setProject(project);
            tc.setProcess(process);
            tc.setLinebody(line);
            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            for (DfControlStandardConfig c : orderDetails) {
                QueryWrapper<DfControlStandardConfig>qw=new QueryWrapper<>();
                qw.eq("data_type",c.getDataType());
                qw.eq("data_classify",c.getDataClassify());
                qw.eq("detail",c.getDetail());
                qw.eq("factory",c.getFactory());
                qw.eq("project",c.getProject());
                qw.eq("process",c.getProcess());
                qw.eq("linebody",c.getLinebody());
                qw.last("limit 1");
                DfControlStandardConfig dd=DfControlStandardConfigMapper.selectOne(qw);
                if(null!=dd){
                    c.setId(dd.getId());
                    c.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    DfControlStandardConfigMapper.updateById(c);
                }else{
                    DfControlStandardConfigMapper.insert(c);
                }

            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

    @Override
    public   List<DfControlStandardConfig> listByExport(int id) {
        return DfControlStandardConfigMapper.listByExport(id);
    }

    @Override
    public IPage<DfControlStandardConfig> listByJoinPage(IPage<DfControlStandardConfig> page, Wrapper<DfControlStandardConfig> wrapper, String batchId) {
        return DfControlStandardConfigMapper.listByJoinPage(page, wrapper,batchId);
    }

    @Transactional
    @Override
    public int upload(MultipartFile file, String factory, String lineBody, String process, String project) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfControlStandardConfig> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            DfControlStandardConfig data = new DfControlStandardConfig();
            data.setFactory(factory);
            data.setLinebody(lineBody);
            data.setProject(project);
            data.setProcess(process);
            data.setRoutingId(65);
            data.setDataType(map.get("管控类型名称"));
            data.setDataClassify(map.get("管控类型名称"));
            data.setDetail(map.get("细项"));
            data.setStandardValue(map.get("标准值/方法"));
            data.setFrequency(map.get("抽检频率"));
            data.setMachine(map.get("设备"));
            list.add(data);
        }
        saveBatch(list);
        return list.size();
    }

    @Override
    public List<DfControlStandardConfig> listDataType() {
        return DfControlStandardConfigMapper.listDataType();
    }
}
