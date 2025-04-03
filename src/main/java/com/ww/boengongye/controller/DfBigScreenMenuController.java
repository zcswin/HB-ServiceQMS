package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfBigScreenMenu;
import com.ww.boengongye.entity.DfYieldWarn;
import com.ww.boengongye.entity.Menu;
import com.ww.boengongye.entity.User;
import com.ww.boengongye.service.DfBigScreenMenuService;
import com.ww.boengongye.service.MenuService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
 * @since 2023-05-16
 */
@Controller
@RequestMapping("/dfBigScreenMenu")
@CrossOrigin
@ResponseBody
@Api(tags = "大屏菜单权限")
public class DfBigScreenMenuController {
    @Autowired
    private DfBigScreenMenuService DfBigScreenMenuService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;


    @ApiOperation("可发起列表")
    @GetMapping("/getByAccount")
    public Result getByAccount(@RequestParam String userAccount,String type,String floor) {
        List<DfBigScreenMenu>datas =new ArrayList<>();
        QueryWrapper<User>uw=new QueryWrapper<>();
        uw.eq("name",userAccount);
        uw.last("limit 1");
        User user=userService.getOne(uw);
        if(null!=user){
            QueryWrapper<Menu>mqw=new QueryWrapper<>();
//            mqw.inSql("id","select parent_id from menu where id in(  select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"'))");

            mqw.and(wrapper -> wrapper.inSql("id", "select id  from menu " +
                            "where id in(select menu_id FROM station_relation_menu " +
                            "WHERE station_id IN ( SELECT station_id FROM user_relation_station " +
                            "WHERE user_id = '"+user.getId()+"' )) and parent_id!=0")
            );


            mqw.eq("is_use","1");
            mqw.eq("menu_type","大屏展示");
            mqw.orderByAsc("sort");
            List<Menu> menus=menuService.list(mqw);
            if(menus.size()>0){
                for(Menu m:menus){
                    DfBigScreenMenu dm=new DfBigScreenMenu();
                    dm.setName(m.getTitle());
                    if(null!=type&&null!=m.getUseType()&&m.getUseType().equals(type)){
                        dm.setUrl(m.getJump());
                    }else{
                        dm.setUrl(m.getWebUrl());
                    }

                    datas.add(dm);
                }
            }
        }
//    QueryWrapper<DfBigScreenMenu>qw=new QueryWrapper<>();
//    qw.eq("u.user_account",userAccount);
//    qw.orderByAsc("m.sort");
//       datas= DfBigScreenMenuService.listByBigScreen(qw);

        if((null==datas||datas.size()==0)&&null!=floor&&!floor.equals("")){
            QueryWrapper<DfBigScreenMenu>qw2=new QueryWrapper<>();
            qw2.like("floor",floor);
            qw2.orderByAsc("sort");
            datas= DfBigScreenMenuService.list(qw2);
        }
        if(null==datas||datas.size()==0){
            QueryWrapper<DfBigScreenMenu>qw2=new QueryWrapper<>();
            qw2.eq("type","all");
            qw2.orderByAsc("sort");
            datas= DfBigScreenMenuService.list(qw2);
        }
    return new Result(200, "查询成功", datas);

    }


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("获取大屏菜单权限列表")
    public Result listBySearch(int page,int limit,String keywords){
        Page<DfBigScreenMenu> pages = new Page<>(page,limit);

        QueryWrapper<DfBigScreenMenu> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("name",keywords)
                    .or().like("url",keywords)
                    .or().like("`type`",keywords)
                    .or().like("floor",keywords)
            );
        }
        IPage<DfBigScreenMenu> list = DfBigScreenMenuService.page(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改大屏菜单权限数据")
    public Result saveOrUpdate(@RequestBody DfBigScreenMenu datas){
        if (datas.getId()!=null){
            if (DfBigScreenMenuService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (DfBigScreenMenuService.save(datas)){
                return new Result(200,"添加成功");
            }
            return new Result(500,"添加失败");
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ApiOperation("删除大屏菜单权限信息")
    public Result delete(String id){
        if (DfBigScreenMenuService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

}
