package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfFlowDataUser;
import com.ww.boengongye.mapper.DfFlowDataUserMapper;
import com.ww.boengongye.service.DfFlowDataUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-28
 */
@Service
public class DfFlowDataUserServiceImpl extends ServiceImpl<DfFlowDataUserMapper, DfFlowDataUser> implements DfFlowDataUserService {

    @Autowired
    private DfFlowDataUserMapper dfFlowDataUserMapper;

    @Override
    public List<DfFlowDataUser> insertDataFromOpinion(String startTime, String endTime) {
        return dfFlowDataUserMapper.insertDataFromOpinion(startTime, endTime);
    }
}
