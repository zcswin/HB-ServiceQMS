package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfQmsIpqcFlawColor;
import com.ww.boengongye.service.DfQmsIpqcFlawColorService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-29
 */
@Controller
@RequestMapping("/dfQmsIpqcFlawColor")
@CrossOrigin
@ResponseBody
public class DfQmsIpqcFlawColorController {
    @Autowired
    DfQmsIpqcFlawColorService DfQmsIpqcFlawColorService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfQmsIpqcFlawColorService.list());
    }



//    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
//    public Result save(@RequestBody DfQmsIpqcFlawColor datas) {
//
//        if (null != datas.getId()) {
//            if (DfQmsIpqcFlawColorService.updateById(datas)) {
//                return new Result(200, "保存成功");
//            } else {
//                return new Result(500, "保存失败");
//            }
//        } else {
//
//            if (DfQmsIpqcFlawColorService.save(datas)) {
//                return new Result(200, "保存成功");
//            } else {
//                return new Result(500, "保存失败");
//            }
//        }
//
//    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfQmsIpqcFlawColor datas) {
        QueryWrapper<DfQmsIpqcFlawColor> ew = new QueryWrapper<>();
        ew.eq(StringUtils.isNotEmpty(datas.getName()),"name",datas.getName())
//            .eq(StringUtils.isNotEmpty(datas.getColor()),"color",datas.getColor())
        ;
        List<DfQmsIpqcFlawColor> list = DfQmsIpqcFlawColorService.list(ew);
        if (!CollectionUtils.isEmpty(list)){
            return new Result(500,"存在相同名称和颜色的数据,不能重复添加");
        }else {
            if (DfQmsIpqcFlawColorService.saveOrUpdate(datas)){
                return new Result(200,"新增或修改成功");
            }else{
                return new Result(500,"新增或修改失败");
            }
        }
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfQmsIpqcFlawColorService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name) {
        Page<DfQmsIpqcFlawColor> pages = new Page<DfQmsIpqcFlawColor>(page, limit);
        QueryWrapper<DfQmsIpqcFlawColor> ew=new QueryWrapper<DfQmsIpqcFlawColor>();
        if(null!=name&&!name.equals("")) {
            ew.like("name", name);
        }

        ew.orderByDesc("create_time");
        IPage<DfQmsIpqcFlawColor> list=DfQmsIpqcFlawColorService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
}
