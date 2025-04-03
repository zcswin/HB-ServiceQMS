package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.DfFlowAuditor;
import com.ww.boengongye.service.DfFlowAuditorService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-21
 */
@Controller
@RequestMapping("/dfFlowAuditor")
@CrossOrigin
@ResponseBody
@Api(tags = "可发起人员")
public class DfFlowAuditorController {
    @Autowired
    private DfFlowAuditorService dfFlowAuditorService;

    @ApiOperation("可发起列表")
    @GetMapping("/getByAccount")
    public Result getByAccount(@RequestParam String userAccount) {
        QueryWrapper<DfFlowAuditor> qw = new QueryWrapper<>();
        qw.eq(null != userAccount, "man_code", userAccount);
        DfFlowAuditor auditor = dfFlowAuditorService.getOne(qw);
        if (null == auditor) {
            return new Result(200, "无发起权限");
        } else {
            String type = auditor.getType();
            String process = auditor.getProcess();
            String level = auditor.getLevel();
            String[] types = type.split(",");
            String[] processes = process.split(",");
            String[] levels = level.split(",");
            Map<String, String[]> result = new HashMap<>();
            result.put("types", types);
            result.put("processes", processes);
            result.put("levels", levels);
            return new Result(200, "有发起权限", result);
        }
    }

    @ApiOperation("审批权限")
    @GetMapping("/isApproval")
    public Result isApproval(@RequestParam String userAccount) {

        QueryWrapper<DfFlowAuditor> finishQw = new QueryWrapper<>();
        finishQw.eq("man_code", userAccount)
                .eq("approval", "Y");
        List<DfFlowAuditor> finishList = dfFlowAuditorService.list(finishQw);
        if (finishList.size() > 0) {
            return new Result(200, "有审批权限", true);
        } else {
            return new Result(200, "无审批权限", false);
        }
    }




    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfFlowAuditor datas) {

        if (null != datas.getId()) {
            if (dfFlowAuditorService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (dfFlowAuditorService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (dfFlowAuditorService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String manCode,String manName,String process ,String type ,String level ,String approval) {
        Page<DfFlowAuditor> pages = new Page<DfFlowAuditor>(page, limit);
        QueryWrapper<DfFlowAuditor> ew=new QueryWrapper<DfFlowAuditor>();
        if(null!=manCode&&!manCode.equals("")) {
            ew.like("man_code", manCode);
        }
        if(null!=manName&&!manName.equals("")) {
            ew.like("man_name", manName);
        }

        if(null!=process&&!process.equals("")) {
            ew.like("process", process);
        }

        if(null!=type&&!type.equals("")) {
            ew.like("type", type);
        }

        if(null!=level&&!level.equals("")) {
            ew.like("level", level);
        }
        if(null!=approval&&!approval.equals("")) {
            ew.like("approval", approval);
        }
        ew.orderByDesc("create_time");
        IPage<DfFlowAuditor> list=dfFlowAuditorService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
    @PostMapping(value = "/importData")
    @ApiOperation("导入数据")
    @Transactional(rollbackFor = Exception.class)
    public Result importData(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfFlowAuditor> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            QueryWrapper<DfFlowAuditor>qw=new QueryWrapper<>();
            qw.eq("man_code",map.get("账号"));
            qw.last("limit 1");
            DfFlowAuditor data=dfFlowAuditorService.getOne(qw);
            if(null==data){
                data = new DfFlowAuditor();
            }
            data.setManCode(map.get("账号"));
            data.setManName(map.get("用户名"));
            data.setType(map.get("可发起问题类型"));
            data.setProcess(map.get("可发起工序"));
            data.setLevel(map.get("可发起等级"));
            data.setApproval(map.get("是否等级三审批人").equals("是")?"Y":"N");
            list.add(data);
            dfFlowAuditorService.saveOrUpdate(data);
        }


//        if (!dfFlowAuditorService.saveBatch(list)){
//            return new Result(200,"导入数据失败");
//        }
        return new Result(200,"成功导入"+list.size()+"条数据");
    }
}
