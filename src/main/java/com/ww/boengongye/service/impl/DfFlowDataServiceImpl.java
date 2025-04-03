package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfFlowData;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.mapper.DfFlowDataMapper;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程数据 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Service
public class DfFlowDataServiceImpl extends ServiceImpl<DfFlowDataMapper, DfFlowData> implements DfFlowDataService {

    @Autowired
    DfFlowDataMapper DfFlowDataMapper;


    @Autowired
    DfLiableManMapper dfLiableManMapper;
//    DfLiableManService dfLiableManService;

//    @Autowired
//    DfFlowDataService dfFlowDataService;


    @Override
    public IPage<DfFlowData> listBacklog(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listBacklog(page, wrapper);
    }

    @Override
    public IPage<DfFlowData> listHaveDone(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listHaveDone(page, wrapper);
    }

    @Override
    public IPage<DfFlowData> listOvertime(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listOvertime(page, wrapper);
    }

    @Override
    public DfFlowData getBacklogCount(Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.getBacklogCount(wrapper);
    }

    @Override
    public DfFlowData getHaveDoneCount(Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.getHaveDoneCount(wrapper);
    }

    @Override
    public IPage<DfFlowData> listByMan(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listByMan(page, wrapper);
    }

    @Override
    public DfAuditDetail getJoinAudit(Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.getJoinAudit(wrapper);
    }

    @Override
    public int updateOverTimeById(int id) {
        return DfFlowDataMapper.updateOverTimeById(id);
    }

    @Override
    public List<DfFlowData> listJoinUser(Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listJoinUser(wrapper);
    }

    @Override
    public List<DfFlowData> listOverTimeLevelUp(Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listOverTimeLevelUp(wrapper);
    }

    @Override
    public List<DfFlowData> listUpOutTimeLevelByAccount(String account) {
        return DfFlowDataMapper.listUpOutTimeLevelByAccount(account);
    }

    @Override
    public IPage<DfFlowData> listOvertimeByMatter(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listOvertimeByMatter(page, wrapper);
    }

    @Override
    public List<DfFlowData> listUpOutTimeLevelByUserName(String userName) {
        return DfFlowDataMapper.listUpOutTimeLevelByUserName(userName);
    }

    @Override
    public IPage<DfFlowData> listClosedByUserId(IPage<DfFlowData> page, String userId) {
        return DfFlowDataMapper.listClosedByUserId(page, userId);
    }


    @Override
    public IPage<DfFlowData> listJoinAudit(IPage<DfFlowData> page, Wrapper<DfFlowData> wrapper) {
        return DfFlowDataMapper.listJoinAudit(page, wrapper);
    }

    /**
     *
     * @param title 通知标题
     * @param type 文件变更类型
     * @param dataId 文件变更记录id
     * @return
     */
    public boolean createFlowData(String title,String type,int dataId){
        DfFlowData risk=new DfFlowData();
        risk.setName(title);
        risk.setFlowType(type);
        risk.setDataType("文件履历变更");
        risk.setStatus("待确认");
        risk.setFlowLevelName("Level1");
        risk.setFlowLevel(1);
        risk.setDataId(dataId);
        QueryWrapper<DfLiableMan> qw=new QueryWrapper<>();
        qw.eq("type",type);
        qw.eq("bimonthly", TimeUtil.getDayShift()==0?"双月":"单月");

//        List<DfLiableMan> lm=dfLiableManService.list(qw);
        List<DfLiableMan> lm=dfLiableManMapper.selectList(qw);
        if(lm.size()>0){
            StringBuilder name=new StringBuilder();
            StringBuilder code=new StringBuilder();
            int count=0;
            for(DfLiableMan l:lm){
                if(count>0){
                    name.append(",");
                    code.append(",");
                }
                name.append(l.getLiableManName());
                code.append(l.getLiableManCode());
                count++;
            }
            risk.setNowLevelUserName(name.toString());
            risk.setNowLevelUser(code.toString());
        }

        if (DfFlowDataMapper.insert(risk)>0){
            return true;
        }
        return false;
//        return DfFlowDataMapper.insert(risk);
    }

    @Override
    public Result createFlowDataFileUpdate(String fileName,String type,Integer dataId) {
        String title = fileName + "_" + type + "_更新通知";
        boolean ok = createFlowData(title, type, dataId);
        if (ok) {
            return new Result(200, "更新成功");
        } else {
            return new Result(500, "更新失败");
        }

    }


}
