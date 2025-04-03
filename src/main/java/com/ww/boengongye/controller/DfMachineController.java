package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.DfMachine;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 设备 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-08
 */
@Controller
@RequestMapping("/dfMachine")
@CrossOrigin
@ResponseBody
@Api(tags = "设备")
public class DfMachineController {

    @Autowired
    private DfMachineService dfMachineService;

    @Autowired
    private DfMacStatusService dfMacStatusService;
    @Autowired
    private DfMacStatusAppearanceService dfMacStatusAppearanceService;
    @Autowired
    private DfMacStatusSizeService dfMacStatusSizeService;
    @Autowired
    private DfMacModelPositionService dfMacModelPositionService;

    @GetMapping("/listAll")
    public Result listAll(int page,int limit ,String processName) {
        Page<DfMachine> pages = new Page<>(page,limit);
        LambdaQueryWrapper<DfMachine> qw = new LambdaQueryWrapper<>();
        if (null != processName && !"".equals(processName)) qw.like(DfMachine::getProcessCode, processName);
        IPage<DfMachine> list = dfMachineService.page(pages,qw);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal() );
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfMachine datas) {

            if(null!=datas.getId()){
                DfMachine already=dfMachineService.getById(datas.getId());
                if (dfMachineService.saveOrUpdate(datas)){
                    if(null!=already&&(already.getPersonCode()!=datas.getProcessCode()|already.getCode()!=datas.getCode())){
                        UpdateWrapper<DfMacStatus>uw1=new UpdateWrapper<>();
                        uw1.set("MachineCode",datas.getCode());
                        uw1.eq("MachineCode",already.getCode());
                        dfMacStatusService.update(uw1);

                        UpdateWrapper<DfMacStatusSize>uw2=new UpdateWrapper<>();
                        uw2.set("MachineCode",datas.getCode());
                        uw2.set("process",datas.getProcessCode());
                        uw2.eq("MachineCode",already.getCode());
                        dfMacStatusSizeService.update(uw2);

                        UpdateWrapper<DfMacStatusAppearance>uw3=new UpdateWrapper<>();
                        uw3.set("MachineCode",datas.getCode());
                        uw3.eq("MachineCode",already.getCode());
                        dfMacStatusAppearanceService.update(uw3);

                        UpdateWrapper<DfMacModelPosition>uw4=new UpdateWrapper<>();
                        uw4.set("MachineCode",datas.getCode());
                        uw4.eq("MachineCode",already.getCode());
                        dfMacModelPositionService.update(uw4);


                    }
                    return new Result(200,"新增或修改成功");
                }
            }else{
                QueryWrapper<DfMachine> ew = new QueryWrapper<>();
                ew.eq(StringUtils.isNotEmpty(datas.getCode()),"code",datas.getCode());
                List<DfMachine> list = dfMachineService.list(ew);
                if (!CollectionUtils.isEmpty(list)) {
                    return new Result(500, "存在相同工序,项目,大区域,小区域的数据,不能重复添加");

                }
                if (dfMachineService.saveOrUpdate(datas)){
                    DfMacStatus ms=new DfMacStatus();
                    ms.setMachineCode(datas.getCode());
                    ms.setStatusidCur(-1);
                    ms.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    dfMacStatusService.save(ms);

                    DfMacStatusSize mss=new DfMacStatusSize();
                    mss.setMachineCode(datas.getCode());
                    mss.setStatusidCur(-1);
                    mss.setProcess(datas.getProcessCode());
                    mss.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    dfMacStatusSizeService.save(mss);

                    DfMacStatusAppearance msa=new DfMacStatusAppearance();
                    msa.setMachineCode(datas.getCode());
                    msa.setStatusidCur(-1);
                    msa.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    dfMacStatusAppearanceService.save(msa);

                    DfMacModelPosition mmp=new DfMacModelPosition();
                    mmp.setMachineCode(datas.getCode());
                    mmp.setPositionX(0.0);
                    mmp.setPositionY(0.0);
                    mmp.setPositionZ(0.0);
                    dfMacModelPositionService.save(mmp);

                    return new Result(200,"新增或修改成功");
                }

            }

                    return new Result(500,"新增或修改失败");
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        DfMachine already=dfMachineService.getById(id);
        if (dfMachineService.removeById(id)) {
            QueryWrapper<DfMacStatus>uw1=new QueryWrapper<>();
            uw1.eq("MachineCode",already.getCode());
            dfMacStatusService.remove(uw1);

            QueryWrapper<DfMacStatusSize>uw2=new QueryWrapper<>();
            uw2.eq("MachineCode",already.getCode());
            dfMacStatusSizeService.remove(uw2);

            QueryWrapper<DfMacStatusAppearance>uw3=new QueryWrapper<>();
            uw3.eq("MachineCode",already.getCode());
            dfMacStatusAppearanceService.remove(uw3);

            QueryWrapper<DfMacModelPosition>uw4=new QueryWrapper<>();
            uw4.eq("MachineCode",already.getCode());
            dfMacModelPositionService.remove(uw4);
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String code,String process) {
        Page<DfMachine> pages = new Page<DfMachine>(page, limit);
        QueryWrapper<DfMachine> ew=new QueryWrapper<DfMachine>();
        if(null!=code&&!code.equals("")) {
            ew.like("code", code);
        }

        if(null!=process&&!process.equals("")) {
            ew.like("process_code", process);
        }


        ew.orderByDesc("create_time");
        IPage<DfMachine> list=dfMachineService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }




}
