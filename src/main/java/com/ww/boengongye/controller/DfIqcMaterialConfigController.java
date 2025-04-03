package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.boengongye.entity.DfIqcMaterialConfig;
import com.ww.boengongye.service.DfIqcMaterialConfigService;
import com.ww.boengongye.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class DfIqcMaterialConfigController {

    @Autowired
    private DfIqcMaterialConfigService dfIqcMaterialConfigService;

    // 新增
    @PostMapping
    public Result<?> add(@RequestBody DfIqcMaterialConfig config) {
        boolean success = dfIqcMaterialConfigService.save(config);
        return success ? Result.INSERT_SUCCESS : Result.INSERT_FAILED;
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean success = dfIqcMaterialConfigService.removeById(id);
        return success ? Result.DELETE_SUCCESS : Result.DELETE_FAILED;
    }

    // 修改
    @PutMapping
    public Result<?> update(@RequestBody DfIqcMaterialConfig config) {
        boolean success = dfIqcMaterialConfigService.updateById(config);
        return success ? Result.UPDATE_SUCCESS : Result.UPDATE_FAILED;
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        DfIqcMaterialConfig config = dfIqcMaterialConfigService.getById(id);
        if (config != null) {
            return new Result<>(200, "查询成功", config);
        } else {
            return new Result<>(404, "未找到记录");
        }
    }

    // 查询所有
    @GetMapping
    public Result<?> list() {
        List<DfIqcMaterialConfig> list = dfIqcMaterialConfigService.list();
        return new Result<>(200, "查询成功", list);
    }

    @GetMapping("/DfIqcMaterialConfigData")
    public Result<?> listWithJoin(@RequestParam(required = false) String materialType) {
        // 构建动态条件
        LambdaQueryWrapper<DfIqcMaterialConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(materialType)) {
            wrapper.eq(DfIqcMaterialConfig::getMaterialType, materialType);
        }

        // 执行查询
        List<DfIqcMaterialConfig> list = dfIqcMaterialConfigService.listConfigWithJoin(wrapper);
        return new Result<>(200, "查询成功", list);
    }
}
