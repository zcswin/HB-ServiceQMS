package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfDustMonitor;
import com.ww.boengongye.mapper.DfDustMonitorMapper;
import com.ww.boengongye.service.DfDustMonitorService;
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
 * 落尘监控状况表 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-19
 */
@Service
public class DfDustMonitorServiceImpl extends ServiceImpl<DfDustMonitorMapper, DfDustMonitor> implements DfDustMonitorService {

    @Autowired
    private DfDustMonitorMapper dfDustMonitorMapper;
}
