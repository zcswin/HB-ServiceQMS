package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfRiskProduct;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfRiskProductService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 风险品 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-10-25
 */
@Controller
@RequestMapping("/dfRiskProduct")
@ResponseBody
@CrossOrigin
@Api(tags = "风险品")
public class DfRiskProductController {
    @Autowired
    private DfRiskProductService dfRiskProductService;

    @Autowired
    private DfProcessService dfProcessService;

    @GetMapping("/getRiskProductList")
    @ApiOperation("获取风险品列表")
    public Result getRiskProductList(String process,String machineCode,String startDate,String endDate,Integer page,Integer limit){
        Page<DfRiskProduct> pages = new Page<>(page,limit);

        String startTime = startDate + " 00:00:00";
        String endTime = endDate + " 23:59:59";

        QueryWrapper<DfRiskProduct> qw = new QueryWrapper<>();
        qw
                .eq(StringUtils.isNotEmpty(process),"dqiwt.f_seq",process)
                .eq(StringUtils.isNotEmpty(machineCode),"drp.machine_code",machineCode)
                .ge(StringUtils.isNotEmpty(startDate),"drp.productive_time",startTime)
                .le(StringUtils.isNotEmpty(endDate),"drp.productive_time",endTime);

        IPage<DfRiskProduct> list = dfRiskProductService.getDfRiskProductList(pages,qw);
        if (list.getRecords()==null||list.getRecords().size()==0){
            return new Result(200,"该条件下没有风险品相关数据");
        }

        return new Result(200,"获取风险品列表成功",list.getRecords(),(int) list.getTotal());
    }


    @GetMapping("/getRiskProductListByParentId")
    @ApiOperation("通过parentId获取风险品列表")
    public Result getRiskProductListByParentId(Integer parentId){
        QueryWrapper<DfRiskProduct> qw = new QueryWrapper<>();

        qw.eq("drp.parent_id",parentId);
        List<DfRiskProduct> list = dfRiskProductService.ListDfRiskProduct(qw);

        return new Result(200,"通过parentId获取风险品列表成功",list);
    }


    @PostMapping("/save")
    @ApiOperation("保存")
    public Result save(@RequestBody List<DfRiskProduct> dates){
        for (DfRiskProduct dfRiskProduct:dates){
            String machineCode = dfRiskProduct.getMachineCode();
//            String[] strArray  = machineCode.split("-");
//            String process = strArray[0];
//            String number = strArray[1];

            QueryWrapper<DfProcess> processWrapper = new QueryWrapper<>();

            processWrapper
                    .eq("first_code",machineCode.substring(0))
                    .last("limit 1");
            DfProcess dfProcess = dfProcessService.getOne(processWrapper);
            if(null!=dfProcess&&null!=dfProcess.getProcessName()){
                dfRiskProduct.setProcess(dfProcess.getProcessName());
            }
            dfRiskProduct.setType("外观");
//            String firstCode = dfProcess.getFirstCode();
//            dfRiskProduct.setMachineCode(firstCode+number);
        }

       if (!dfRiskProductService.saveBatch(dates)){
           return new Result(200,"保存失败");
       }
       return new Result(200,"保存成功");
    }
}
