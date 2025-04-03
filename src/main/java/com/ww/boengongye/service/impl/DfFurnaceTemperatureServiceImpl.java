package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfFurnaceTemperature;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfFurnaceTemperatureMapper;
import com.ww.boengongye.service.DfFurnaceTemperatureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 炉温表 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-10-16
 */
@Service
public class DfFurnaceTemperatureServiceImpl extends ServiceImpl<DfFurnaceTemperatureMapper, DfFurnaceTemperature> implements DfFurnaceTemperatureService {

    @Autowired
    private DfFurnaceTemperatureMapper dfFurnaceTemperatureMapper;


    @Override
    public List<String> getFurnaceNameList(Wrapper<DfFurnaceTemperature> wrapper) {
        return dfFurnaceTemperatureMapper.getFurnaceNameList(wrapper);
    }

    @Override
    public List<Rate3> getOneFurnaceCheckValueList(Wrapper<DfFurnaceTemperature> wrapper, Wrapper<DfFurnaceTemperature> wrapper2) {
        return dfFurnaceTemperatureMapper.getOneFurnaceCheckValueList(wrapper,wrapper2);
    }
}
