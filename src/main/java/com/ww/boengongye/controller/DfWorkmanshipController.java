package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfWorkmanship;
import com.ww.boengongye.service.DfWorkmanshipService;
import com.ww.boengongye.utils.ExportExcelUtil;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  工艺
 * </p>
 *
 * @author zhao
 * @since 2022-09-26
 */
@Controller
@RequestMapping("/dfWorkmanship")
@ResponseBody
@CrossOrigin
public class DfWorkmanshipController {
    @Autowired
    private DfWorkmanshipService dfWorkmanshipService;

    @GetMapping("/listAll")
    public Result listAll(@RequestParam("factoryCode") String factoryCode,
                          @RequestParam("lineCode") String lineCode,
                          @RequestParam("projectName") String projectName){
        LambdaQueryWrapper<DfWorkmanship> qw = new LambdaQueryWrapper<>();
        if (null != factoryCode && !"".equals(factoryCode)) qw.eq(DfWorkmanship::getFactoryCode, factoryCode);
        if (null != lineCode && !"".equals(lineCode)) qw.eq(DfWorkmanship::getLineCode, lineCode);
        if (null != projectName && !"".equals(projectName)) qw.eq(DfWorkmanship::getProjectName, projectName);
        List<DfWorkmanship> list = dfWorkmanshipService.list(qw);

        return new Result(200, "查询成功", list);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfWorkmanship dfWorkmanship) {
        boolean bool = dfWorkmanshipService.save(dfWorkmanship);
        if (bool) {
            return Result.INSERT_SUCCESS;
        } else {
            return Result.INSERT_FAILED;
        }
    }

    @GetMapping("/delete")
    public Result delete(Integer[] ids) {
        List<Integer> list = Arrays.asList(ids);
        boolean bool = dfWorkmanshipService.removeByIds(new ArrayList<>(list));
        if (bool) {
            return Result.DELETE_SUCCESS;
        } else {
            return Result.DELETE_FAILED;
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfWorkmanship dfWorkmanship) {
        boolean bool = dfWorkmanshipService.updateById(dfWorkmanship);

        if (bool) {
            return Result.UPDATE_SUCCESS;
        } else {
            return Result.UPDATE_FAILED;
        }
    }

    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        ExportExcelUtil excelUtil = new ExportExcelUtil();
        excelUtil.downLoadExcelMould(response, "项目工艺模板");
    }

    @PostMapping("/importExcel")
    public Result importExcel(MultipartFile file, String factoryCode, String lineCode) throws Exception {
        int count = dfWorkmanshipService.importExcel(file, factoryCode, lineCode);
        return new Result(200, "成功插入" + count + "条数据");
    }
}
