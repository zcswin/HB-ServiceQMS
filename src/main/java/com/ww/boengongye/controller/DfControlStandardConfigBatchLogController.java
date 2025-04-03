package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfControlStandardConfigBatch;
import com.ww.boengongye.entity.DfControlStandardConfigBatchLog;
import com.ww.boengongye.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2025-03-13
 */
@Controller
@RequestMapping("/dfControlStandardConfigBatchLog")
@ResponseBody
@CrossOrigin
public class DfControlStandardConfigBatchLogController {
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigBatchLogService dfControlStandardConfigBatchLogService;
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigService dfControlStandardConfigService;

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardConfigBatchLogController.class);

    @GetMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", dfControlStandardConfigBatchLogService.list());
    }
    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit,String project,String lineBody, String content,String batchCode) {
//        try {
        Page<DfControlStandardConfigBatchLog> pages = new Page<DfControlStandardConfigBatchLog>(page, limit);
        QueryWrapper<DfControlStandardConfigBatchLog> ew = new QueryWrapper<DfControlStandardConfigBatchLog>();
        if(StringUtils.isNotEmpty(content)){
            ew.and(wrapper -> wrapper.like("project", content))
                    .or(wrapper -> wrapper.like("line_body", content))
                    .or(wrapper -> wrapper.like("batch_code", content))
            ;
        }
        ew.like(StringUtils.isNotEmpty(project),"project",project)
                .like(StringUtils.isNotEmpty(lineBody),"line_body",lineBody);

        ew.orderByDesc("create_time");
        IPage<DfControlStandardConfigBatchLog> list = dfControlStandardConfigBatchLogService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());

    }
    @PostMapping(value = "/saveOrUpdate")
    public Result save(@RequestBody DfControlStandardConfigBatchLog datas) {

        QueryWrapper<DfControlStandardConfig>qw=new QueryWrapper<DfControlStandardConfig>();
        qw.eq("batch_code",datas.getBatchCode());
        List<DfControlStandardConfig>batchData=dfControlStandardConfigService.list(qw);

            if (dfControlStandardConfigBatchLogService.save(datas)) {

                for(DfControlStandardConfig d:batchData){
                    d.setProject(datas.getProject());
                    d.setLinebody(datas.getLineBody());
                    d.setId(null);
                    d.setBatchCode("");
                }
                dfControlStandardConfigService.saveBatch(batchData);

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }


    }
}
