package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfIqcDetail;
import com.ww.boengongye.service.DfIqcDetailService;
import com.ww.boengongye.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/details")
public class DfIqcDetailController {

    @Autowired
    private DfIqcDetailService dfIqcDetailService;

    // 新增
    @PostMapping
    public Result<?> add(@RequestBody DfIqcDetail detail) {
        boolean success = dfIqcDetailService.save(detail);
        return success ? Result.INSERT_SUCCESS : Result.INSERT_FAILED;
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean success = dfIqcDetailService.removeById(id);
        return success ? Result.DELETE_SUCCESS : Result.DELETE_FAILED;
    }

    // 修改
    @PutMapping
    public Result<?> update(@RequestBody DfIqcDetail detail) {
        boolean success = dfIqcDetailService.updateById(detail);
        return success ? Result.UPDATE_SUCCESS : Result.UPDATE_FAILED;
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        DfIqcDetail detail = dfIqcDetailService.getById(id);
        if(detail != null){
            return new Result<>(200, "查询成功", detail);
        } else {
            return new Result<>(404, "未找到记录");
        }
    }

    // 查询所有
    @GetMapping
    public Result<?> list() {
        List<DfIqcDetail> list = dfIqcDetailService.list();
        return new Result<>(200, "查询成功", list);
    }


    @GetMapping("/DfIqcDetailData")
    public Result listWithJoin(@RequestParam(required = false) String keyword) {
        // 构建动态条件
        LambdaQueryWrapper<DfIqcDetail> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(DfIqcDetail::getMaterialCode, keyword)
                    .or().like(DfIqcDetail::getMaterialName, keyword);
        }

        // 执行查询
        List<DfIqcDetail> list =dfIqcDetailService.listDetailWithJoin(wrapper);
        return new Result<>(200, "查询成功", list);
    }
}
