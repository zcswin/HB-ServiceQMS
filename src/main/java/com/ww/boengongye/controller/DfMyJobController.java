package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMyJob;
import com.ww.boengongye.service.DfMyJobService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 指派任务表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-10
 */
@Controller
@RequestMapping("/dfMyJob")
@ResponseBody
@CrossOrigin
public class DfMyJobController {
    @Autowired
    DfMyJobService DfMyJobService;


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfMyJob datas) {
        if (null != datas.getId()) {
            if (DfMyJobService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (DfMyJobService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfMyJobService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }

    @RequestMapping(value = "/getJobCount")
    public Result getJobCount( String recipientCode) {
        QueryWrapper<DfMyJob> ew = new QueryWrapper<DfMyJob>();
        if (null != recipientCode && !recipientCode.equals("")) {
            ew.eq("recipient_code", recipientCode);
        }
        ew.eq("result", "待处理");
        return new Result(200, "查询成功", DfMyJobService.count(ew));
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String type, String recipientCode) {
//        try {
        Page<DfMyJob> pages = new Page<DfMyJob>(page, limit);
        QueryWrapper<DfMyJob> ew = new QueryWrapper<DfMyJob>();
        if (null != type && !type.equals("")) {
            ew.like("job_type", type);
        }
        if (null != recipientCode && !recipientCode.equals("")) {
            ew.eq("recipient_code", recipientCode);
        }
        ew.orderByDesc("result");
        ew.orderByDesc("create_time");

        IPage<DfMyJob> list = DfMyJobService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

}
