package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfControlStandardConfigBatch;
import com.ww.boengongye.service.impl.ExportDataService;
import com.ww.boengongye.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2025-03-13
 */
@Controller
@RequestMapping("/dfControlStandardConfigBatch")
@ResponseBody
@CrossOrigin
public class DfControlStandardConfigBatchController {
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigBatchService dfControlStandardConfigBatchService;
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigService dfControlStandardConfigService;

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardConfigBatchController.class);

    @GetMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", dfControlStandardConfigBatchService.list());
    }
    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit,String project,String process,String line, String content) {
//        try {
        Page<DfControlStandardConfigBatch> pages = new Page<DfControlStandardConfigBatch>(page, limit);
        QueryWrapper<DfControlStandardConfigBatch> ew = new QueryWrapper<DfControlStandardConfigBatch>();
        if(StringUtils.isNotEmpty(content)){
            ew.and(wrapper -> wrapper.like("project", content))
                    .or(wrapper -> wrapper.like("line_body", content))
                    .or(wrapper -> wrapper.like("batch_code", content))
            ;
        }
        ew.like(StringUtils.isNotEmpty(project),"project",project)
                .like(StringUtils.isNotEmpty(line),"line_body",line);

        ew.orderByDesc("create_time");
        IPage<DfControlStandardConfigBatch> list = dfControlStandardConfigBatchService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());

    }
    @GetMapping(value = "/delete")
    public Object delete(String batchCode) {

        QueryWrapper<DfControlStandardConfigBatch>qw=new QueryWrapper<>();
        qw.eq("batch_code",batchCode);
        if(dfControlStandardConfigBatchService.remove(qw)){

            QueryWrapper<DfControlStandardConfig>qw2=new QueryWrapper<>();
            qw2.eq("batch_code",batchCode);
            dfControlStandardConfigService.remove(qw2);
            return new Result(0, "操作成功");
        }
        return new Result(0, "操作失败");

    }



}
