package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfAoiDefect;
import com.ww.boengongye.entity.DfAoiPiece;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfAoiDefectMapper;
import com.ww.boengongye.service.DfAoiDefectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI缺陷表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Service
public class DfAoiDefectServiceImpl extends ServiceImpl<DfAoiDefectMapper, DfAoiDefect> implements DfAoiDefectService {

    @Autowired
    DfAoiDefectMapper dfAoiDefectMapper;

    @Override
    public List<DfAoiDefect> getDefectByBarCode(Wrapper<DfAoiDefect> wrapper) {
        return dfAoiDefectMapper.getDefectByBarCode(wrapper);
    }

    @Override
    public List<DfAoiDefect> getDefectByPieceName(Wrapper<DfAoiDefect> wrapper) {
        return dfAoiDefectMapper.getDefectByPieceName(wrapper);
    }

    @Override
    public List<DfAoiDefect> getAllDefectList(IPage<DfAoiDefect> page,Wrapper<DfAoiDefect> wrapper,Wrapper<DfAoiDefect> wrapper2) {
        return dfAoiDefectMapper.getAllDefectList(page,wrapper,wrapper2);
    }

    @Override
    public Integer getTotalPieceNumber(Wrapper<Integer> wrapper) {
        return dfAoiDefectMapper.getTotalPieceNumber(wrapper);
    }

    @Override
    public Integer getDefectNumber(Wrapper<Integer> wrapper) {
        return dfAoiDefectMapper.getDefectNumber(wrapper);
    }

    @Override
    public List<DfAoiDefect> getAllDefectMappingList(Wrapper<DfAoiDefect> wrapper, Wrapper<DfAoiDefect> wrapper2) {
        return dfAoiDefectMapper.getAllDefectMappingList(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiDefect> listItemInfo(Wrapper<DfAoiDefect> wrapper, Wrapper<DfAoiDefect> wrapper2) {
        return dfAoiDefectMapper.listItemInfo(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiDefect> listFeaturevaluesInfo(Wrapper<DfAoiDefect> wrapper, Wrapper<DfAoiDefect> wrapper2) {
        return dfAoiDefectMapper.listFeaturevaluesInfo(wrapper,wrapper2);
    }

    @Override
    public List<Map<String, Object>> fqcNgTopRate(QueryWrapper<DfAoiDefect> ew, Integer top) {
        return dfAoiDefectMapper.fqcNgTopRate(ew,top);
    }

    @Override
    public Rate3 getPieceDefectPoint(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiDefectMapper.getPieceDefectPoint(wrapper);
    }

    @Override
    public List<DfAoiDefect> getAllDefectAndWeightList(Wrapper<DfAoiPiece> wrapper, Wrapper<DfAoiDefect> wrapper2) {
        return dfAoiDefectMapper.getAllDefectAndWeightList(wrapper,wrapper2);
    }
}
