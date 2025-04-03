package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.User;
import com.ww.boengongye.service.DfAoiSeatProtectService;
import com.ww.boengongye.service.DfFactoryService;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.RemoteException;
import smartbi.sdk.service.user.UserManagerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Controller
@RequestMapping("/user")
@ResponseBody
@CrossOrigin
@Api(tags = "用户")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    com.ww.boengongye.service.UserService UserService;

    @Autowired
    com.ww.boengongye.service.UserRelationStationService UserRelationStationService;

    @Autowired
    com.ww.boengongye.service.StationService StationService;

    @Autowired
    DfFactoryService dfFactoryService;

    @Autowired
    DfAoiSeatProtectService dfAoiSeatProtectService;

    @Value("${bi.url}")
    private String url;

    @Value("${bi.account}")
    private String account;

    @Value("${bi.password}")
    private String password;


    @PostMapping(value = "/login")
    public Result login(@RequestBody User datas) {
        System.out.println("111111111112222222222");
        QueryWrapper<User> qw = new QueryWrapper<>();
//                qw.orderByDesc("EmployeeID");
        qw.eq("name", datas.getName());
        try {
            qw.eq("password", Base64Utils.encrypt(datas.getPassword()))
                    .last("limit 1");

        } catch (Exception ex) {
            logger.error("login", ex);
        }
        User user = UserService.getOne(qw);

        if (user==null||user.getId()==null){
            return new Result(500, "接口异常");
        }

        if ("FQC".equals(user.getProcess())||"OQC".equals(user.getProcess())){
            QueryWrapper<DfAoiSeatProtect> seatProtectWrapper = new QueryWrapper<>();
            seatProtectWrapper
                    .eq("user_id",user.getId())
                    .last("limit 1");
            DfAoiSeatProtect dfAoiSeatProtect = dfAoiSeatProtectService.getOne(seatProtectWrapper);
            if (dfAoiSeatProtect==null||dfAoiSeatProtect.getId()==null){
                return new Result(200, "该员工没有配置工位等相关信息，无法登录");
            }
        }

        return new Result(200, "登录成功", user);
    }


    @GetMapping (value = "/getByAccount")
    public Result getByAccount(String account) {
        QueryWrapper<User>qw=new QueryWrapper<>();
        qw.eq("name",account);
        qw.last("limit 0,1");
        User u=UserService.getOne(qw);
        u.setPassword("");
        return new Result(200, "查询成功",u );
    }
    @GetMapping (value = "/getById")
    public Result getById(String id) {
        QueryWrapper<User>qw=new QueryWrapper<>();
        qw.eq("id",id);
        qw.last("limit 0,1");
        User u=UserService.getOne(qw);
        u.setPassword("");
        return new Result(200, "查询成功",u );
    }
    @GetMapping (value = "/listAll")
    public Result listAll() {
        List<User> data = UserService.list();
        if (data.size() > 0) {
            for (User r : data) {
                r.setPassword("");
            }
        }
        return new Result(200, "查询成功", data);
    }

    @GetMapping(value = "/listAllTour")
    public Result listAllTour() {
        QueryWrapper<User> qw = new QueryWrapper<>();
//        qw.eq("permission","tour");
        List<User> data = UserService.list(qw);
        if (data.size() > 0) {
            for (User r : data) {
                r.setPassword("");
            }
        }
        return new Result(200, "查询成功", data);
    }

    @PostMapping(value = "/saveOrUpdate")
    public Result save(@RequestBody User datas) {
        System.out.println(datas.toString());
//        try {
//        Field[] f = User.class.getDeclaredFields();
//        //给TAnnals对象赋值
//        for (int i = 0; i < f.length; i++) {
//            //获取属相名
//            String attributeName = f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
//            try {
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod = User.class.getMethod("set" + methodName, String.class);
//                if (!methodName.equals("IntroductionLand")) {
//                    //执行该set方法
//                    setMethod.invoke(datas, XXSFilter.checkStr(XXSFilter.getFieldValueByFieldName(attributeName, datas)));
//                }
//
//            } catch (NoSuchMethodException e) {
//                logger.error("接口异常", e);
//            } catch (IllegalAccessException e) {
//                logger.error("接口异常", e);
//            } catch (InvocationTargetException e) {
//                logger.error("接口异常", e);
//            }
//        }
//        ClientConnector conn = new ClientConnector(url);
//        conn.open(account, password); // 以管理员身份登录
//        // 第一次调用必须建立一个连接，后续调用则不必再建连接
//        boolean ret = conn.open(account, password);
        try {
//            if (ret) {
                // 创建用户管理服务
//                UserManagerService userManagerService = new UserManagerService(conn);
                if (null != datas.getId()) {
//                    boolean updateRet = userManagerService.updateUser(datas.getId(), datas.getAlias(), datas.getDataDesc(), datas.getPassword(),  datas.enabled == 1 ? true : false);
//                    if (updateRet) {
                        datas.setPassword(Base64Utils.encrypt(datas.getPassword()));
                        if (UserService.updateById(datas)) {
                            return new Result(200, "保存成功");
                        } else {
                            return new Result(500, "保存失败");
                        }
//                    }

                } else {

//                    String userId1 = userManagerService.createUser(datas.getDepartmentId(), datas.getName(), datas.getAlias(), datas.getDataDesc(), datas.getPassword(), datas.enabled == 1 ? true : false);
//                    datas.setId(userId1);
                    datas.setId(CommunalUtils.getUUID());
                    if (null != datas.getPassword() && !datas.getPassword().equals("")) {
                        try {
                            datas.setPassword(Base64Utils.encrypt(datas.getPassword()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            datas.setPassword(Base64Utils.encrypt("123456"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    datas.setCreateTime(TimeUtil.getNowTimeByNormal());
                    if (UserService.save(datas)) {
                        return new Result(200, "保存成功");
                    } else {
                        return new Result(500, "保存失败");
                    }
//                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭应用连接器
//            conn.close();
        }
        return new Result(500, "保存失败");

//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    // 根据id删除
    @GetMapping(value = "/delete")
    public Result delete(String id) {
//        try {
//        ClientConnector conn = new ClientConnector(url);
//        conn.open(account, password); // 以管理员身份登录
//        // 第一次调用必须建立一个连接，后续调用则不必再建连接
//        boolean ret = conn.open(account, password);
        try {
//            if (ret) {
                // 创建用户管理服务
//                UserManagerService userManagerService = new UserManagerService(conn);
//                if (userManagerService.deleteUser(id)) {
                    if (UserService.removeById(id)) {
                        UpdateWrapper<UserRelationStation> uw = new UpdateWrapper<>();
                        uw.eq("user_id", id);
                        UserRelationStationService.remove(uw);
                        return new Result(200, "删除成功");
                    }
//                }

//            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            // 关闭应用连接器
//            conn.close();
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String name,String departmentId,String process) {
//        try {
        Page<User> pages = new Page<User>(page, limit);
        QueryWrapper<User> ew = new QueryWrapper<User>();
        if (null != name && !name.equals("")) {
            ew.like("u.alias", name);
        }

        if (null != departmentId && !departmentId.equals("")) {
            ew.like("u.department_id", departmentId);
        }

        if (StringUtils.isNotEmpty(process)){
            ew.like("u.process",process);
        }

        ew.orderByDesc("u.create_time");

        IPage<User> list = UserService.listJoinPage(pages, ew);
        if (list.getRecords().size() > 0) {
            for (User u : list.getRecords()) {
                u.setPassword("");
                QueryWrapper<Station> qw = new QueryWrapper<>();
                qw.inSql("id", "select station_id from user_relation_station where user_id='" + u.getId() + "'");
                List<Station> stations = StationService.list(qw);
                if (stations.size() > 0) {
                    String str = "";
                    int i = 0;
                    for (Station s : stations) {
                        if (i > 0) {
                            str += "、";
                        }
                        str += s.getName();
                        i++;
                    }
                    u.setPermission(str);
                }

            }

        }
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/getUserListBySearch")
    public Result getUserListBySearch(Integer page, Integer limit, String keywords,@RequestParam("factoryIds") List<String> factoryIds){
        Page<User> pages = new Page<>(page,limit);
        QueryWrapper<User> ew = new QueryWrapper<>();

        if (factoryIds!=null&&factoryIds.size()!=0){
            ew.in("u.factory_id",factoryIds);
        }

        if (keywords != null && !"".equals(keywords)) {
            ew.and(wrapper -> wrapper
                    .like("u.name", keywords)
                    .or().like("u.alias", keywords)
                    .or().like("df.factory_name", keywords)
            );
        }

        IPage<User> list = UserService.getUserListBySearch(pages,ew);
        return new Result(200,"查询成功",list.getRecords(), (int) list.getTotal());
    }

//    /**
//     * 添加或修改
//     * @param datas
//     * @return
//     */
//    @RequestMapping(value = "/saveOrUpdateNew")
//    public Result saveOrUpdateNew(@RequestBody User datas){
//        if (datas.getId()!=null){
//            if (UserService.updateById(datas)){
//                return new Result(200,"修改成功");
//            }
//            return new Result(500,"修改失败");
//        }else {
//            if (UserService.save(datas)){
//                return new Result(200,"添加成功");
//            }
//            return new Result(500,"添加失败");
//        }
//    }


    /**
     * 导出
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("导出用户信息")
    @RequestMapping(value = "downloadExcel",method = RequestMethod.GET)
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<User> datas = UserService.getAllList();

        List<Map> maps = new ArrayList<>();

        for (User user : datas) {
            try {
                maps.add(Excel.objectToMap(user));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (maps != null && maps.size() > 0) {
//            String companyName = "用户管理表";
//            String sheetTitle = companyName;
            String[] title = new String[]{"工号", "姓名", "考核等级", "入职时间", "劳动关系",
                    "所处车间", "是否有效", "最新修改时间", "修改人","备注"};        //设置表格表头字段

            String[] properties = new String[]{"name", "alias", "grade", "createTime",
                    "laborRelation", "factoryName", "isUse", "updateTime", "updateName","remark"};  // 查询对应的字段
            ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
            excelExport2.setData(maps);
            excelExport2.setHeardKey(properties);
            excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
            excelExport2.setHeardList(title);
            excelExport2.exportExport(request, response);
        }
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("导入用户数据")
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "file", required = false)MultipartFile file,String userName) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取工位维护信息失败");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
            return new Result(500, "请上传xlsx或xls格式的文件");
        }

        ExcelImportUtil excel = new ExcelImportUtil(file);

        //获取excel表中所有的数据
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        List<User> list = new ArrayList<>();

        for (Map<String, String> map : maps) {
            User user = new User();
            user.setName(map.get("工号"));
            user.setAlias(map.get("姓名"));
            user.setGrade(map.get("考核等级"));
            user.setLaborRelation(map.get("劳动关系"));

            //获取车间id
            QueryWrapper<DfFactory> factoryWrapper = new QueryWrapper<>();
            factoryWrapper.eq("factory_name",map.get("所处车间"));
            DfFactory dfFactory = dfFactoryService.getOne(factoryWrapper);
            user.setFactoryId(dfFactory.getId());

            user.setIsUse(map.get("是否有效"));
            user.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            user.setUpdateName(userName);
            user.setRemark(map.get("备注"));

            //判断该用户数据是否存在
            QueryWrapper<User> ew = new QueryWrapper<>();
            ew.eq("name",map.get("工号"));
            User userOld = UserService.getOne(ew);
            if (userOld==null||userOld.getId()==null){
                user.setId(CommunalUtils.getUUID());
                user.setCreateTime(TimeUtil.getNowTimeByNormal());
                user.setCreateName(userName);
                if (!UserService.save(user)){
                    return new Result(500,"用户"+userOld.getAlias()+"的信息添加失败");
                }
            }else {
                //判断车间是否被修改
                if (userOld.getFactoryId()!=user.getFactoryId()){
                    return new Result(500,"用户"+userOld.getAlias()+"的所属车间不可修改");
                }
                user.setId(userOld.getId());
                if (!UserService.updateById(user)){
                    return new Result(500,"用户"+userOld.getAlias()+"的信息修改失败");
                }
            }
        }
        return new Result(200, "导入用户信息成功");
    }

    @GetMapping("getAoiUserByUserCode")
    @ApiOperation("通过员工工号获取AOI员工信息")
    public Result getAoiUserByUserCode(String userCode){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("u.name",userCode);
        User user = UserService.getAoiUserByUserCode(userQueryWrapper);

        return new Result(200,"获取AOI员工信息成功",user);
    }
}
