package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfAgvPointMapper;
import com.ww.boengongye.service.DfAgvPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
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
 * @since 2023-09-14
 */
@Service
public class DfAgvPointServiceImpl extends ServiceImpl<DfAgvPointMapper, DfAgvPoint> implements DfAgvPointService {

  @Autowired
  DfAgvPointMapper dfAgvPointMapper;

    public ImportExcelResult importOrder( MultipartFile file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfAgvPoint> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfAgvPoint tc = new DfAgvPoint();
            tc.setName(map.get("name"));
            AVGXY datas = new Gson().fromJson(map.get("coordinate"), AVGXY.class);
            tc.setAgvX(Double.valueOf(datas.getX()));
            tc.setAgvY(Double.valueOf(datas.getY()));
            tc.setX(((tc.getAgvX()+12632)/140*-1));
            tc.setY(((tc.getAgvY()-15781)/140*1));

            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            for (DfAgvPoint c : orderDetails) {
                QueryWrapper<DfAgvPoint> qw=new QueryWrapper<>();
                qw.eq("name",c.getName());

                qw.last("limit 1");
                DfAgvPoint dd=dfAgvPointMapper.selectOne(qw);
                if(null!=dd){
                    c.setId(dd.getId());

                    dfAgvPointMapper.updateById(c);
                }else{
                    dfAgvPointMapper.insert(c);
                }

            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

}
