package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.DfWindPressure;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfWindPressureMapper;
import com.ww.boengongye.service.DfWindPressureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 风压点检表 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-18
 */
@Service
public class DfWindPressureServiceImpl extends ServiceImpl<DfWindPressureMapper, DfWindPressure> implements DfWindPressureService {

    @Autowired
    private DfWindPressureMapper dfWindPressureMapper;

    @Override
    public List<Rate3> getOneSpotWindPressureList(Wrapper<DfWindPressure> wrapper, Wrapper<String> wrapper2) {
        return dfWindPressureMapper.getOneSpotWindPressureList(wrapper,wrapper2);
    }
}
