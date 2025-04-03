package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfControlStandardStatusMapper;
import com.ww.boengongye.service.DfControlStandardStatusService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 管控标准状态 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-12
 */
@Service
public class DfControlStandardStatusServiceImpl extends ServiceImpl<DfControlStandardStatusMapper, DfControlStandardStatus> implements DfControlStandardStatusService {

    @Autowired
    DfControlStandardStatusMapper DfControlStandardStatusMapper;

    @Override
    public IPage<DfControlStandardStatus> listByJoinPage(IPage<DfControlStandardStatus> page, Wrapper<DfControlStandardStatus> wrapper) {
        return DfControlStandardStatusMapper.listByJoinPage(page, wrapper);
    }

    @Override
    public List<DfControlStandardStatus> listAllTypeCount(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper) {
        return DfControlStandardStatusMapper.listAllTypeCount(wrapper);
    }


    @Override
    public List<DfControlStandardStatus> queryDailyData(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper) {
        return DfControlStandardStatusMapper.queryDailyData(wrapper);
    }

    @Override
    public List<DfControlStandardStatus> queryDateData(@Param("projectId") String projectId,@Param("dataType") String dataType,@Param("years") List<Integer> years) {
        return DfControlStandardStatusMapper.queryDateData( projectId, dataType, years);
    }

    @Override
    public List<DfControlStandardStatus> queryEachProcessData(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper, @Param("beginTime") String beginTime,
                                                              @Param("endTime") String endTime ) {
        return DfControlStandardStatusMapper.queryEachProcessData(wrapper,beginTime,endTime);
    }


    @Override
    public List<DfControlStandardStatus> listCountByDay(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper) {
        return DfControlStandardStatusMapper.listCountByDay(wrapper);
    }

    @Override
    public List<DfControlStandardStatus> getComparationByWeek(@Param("startTime") String startTime,@Param("endTime") String endTime) {
        return DfControlStandardStatusMapper.getComparationByWeek(startTime,endTime);
    }

    @Override
    public List<DfControlStandardStatus> getComparationByMonth(@Param("startTime")String startTime, @Param("endTime") String endTime) {
        return DfControlStandardStatusMapper.getComparationByMonth(startTime,endTime);
    }

    @Override
    public List<DfControlStandardStatus> getBaAndBielComparaion(@Param("startTime")String startTime, @Param("endTime") String endTime) {
        return DfControlStandardStatusMapper.getBaAndBielComparaion(startTime,endTime);
    }

    @Override
    public List<Rate3> getBaAndBielComparaion(Wrapper<String> wrapper, Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getBaAndBielComparaion(wrapper,wrapper1,wrapper2);
    }

    @Override
    public List<Rate3> getBaComparaion(Wrapper<String> wrapper) {
        return DfControlStandardStatusMapper.getBaComparaion(wrapper);
    }

    @Override
    public List<Rate3> getComparaionMonth(Wrapper<String> wrapper, Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getComparaionMonth(wrapper,wrapper1,wrapper2);
    }

    @Override
    public List<Rate3> getComparaionWeek(Wrapper<String> wrapper, Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getComparaionWeek(wrapper,wrapper1,wrapper2);
    }

    @Override
    public List<Rate3> getComparaionDay(Wrapper<String> wrapper, Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getComparaionDay(wrapper,wrapper1,wrapper2);
    }

    @Override
    public List<Rate3> getComparaionProcess(Wrapper<String> wrapper, Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getComparaionProcess(wrapper,wrapper1,wrapper2);
    }

    @Override
    public List<Rate3> getComparaionFactory(Wrapper<String> wrapper1, Wrapper<String> wrapper2) {
        return DfControlStandardStatusMapper.getComparaionFactory(wrapper1,wrapper2);
    }

}
