package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfQoaBomMain;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * QA-BOM- 收集产品生产过程需参考的文件-主流程 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-02
 */
@Controller
@RequestMapping("/dfQoaBomMain")
@ResponseBody
@CrossOrigin
public class DfQoaBomMainController {
    @Autowired
    com.ww.boengongye.service.DfQoaBomMainService DfQoaBomMainService;

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(DfQoaBomMainController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfQoaBomMainService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfQoaBomMain datas) {

        if (null != datas.getId()) {
            if (DfQoaBomMainService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfQoaBomMainService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfQoaBomMainService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String productionPhase,String factoryCode,String fileName,String fileVersion) {
//        try {
        Page<DfQoaBomMain> pages = new Page<DfQoaBomMain>(page, limit);
        QueryWrapper<DfQoaBomMain> ew=new QueryWrapper<DfQoaBomMain>();
        if(null!=productionPhase&&!productionPhase.equals("")) {
            ew.like("production_phase", productionPhase);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("factory_code", factoryCode);
        }
        if(null!=fileName&&!fileName.equals("")) {
            ew.like("file_name", fileName);
        }
        if(null!=fileVersion&&!fileVersion.equals("")) {
            ew.like("file_version", fileVersion);
        }
        ew.orderByDesc("create_time");
        IPage<DfQoaBomMain> list=DfQoaBomMainService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }



}
