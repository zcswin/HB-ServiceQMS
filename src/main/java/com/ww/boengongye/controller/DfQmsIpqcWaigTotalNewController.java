package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetailNew;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotalNew;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Excel;
import com.ww.boengongye.utils.ExcelExportUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
@Controller
@RequestMapping("/dfQmsIpqcWaigTotalNew")
@Api(tags = "外观数据2")
@ResponseBody
@CrossOrigin
public class DfQmsIpqcWaigTotalNewController {


    @Autowired
    DfProcessProjectConfigService dfProcessProjectConfigService;
    @Autowired
    com.ww.boengongye.service.DfAuditDetailService DfAuditDetailService;

    @Autowired
    private DfQmsIpqcWaigTotalNewService dfQmsIpqcWaigTotalNewService;

    @Autowired
    private DfControlStandardStatusService dfControlStandardStatusService;

    @Value("${config.factoryId}")
    private String fac;


    @Autowired
    private DfQmsIpqcWaigDetailNewService dfQmsIpqcWaigDetailNewService;

    @Autowired
    private DfProcessService dfProcessService;

    @GetMapping(value = "/listByMachineAndTime")
    public Object listByMachineAndTime(String machineCode, String time) throws ParseException {
        if (machineCode.indexOf("-") != -1) {
            String[] code = machineCode.split("-");
            QueryWrapper<DfProcess> qw = new QueryWrapper<>();
            qw.eq("process_name", code[0]);
            qw.last("limit 0,1");
            DfProcess process = dfProcessService.getOne(qw);
            machineCode = process.getFirstCode() + code[1];
        }
        QueryWrapper<DfQmsIpqcWaigTotalNew> qw = new QueryWrapper<>();
        qw.eq("f_mac", machineCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        qw.apply("UNIX_TIMESTAMP(f_time) >= UNIX_TIMESTAMP('" + TimeUtil.beforeOneHourToNowDate(sdf.parse(time)) + "')");
        qw.apply("UNIX_TIMESTAMP(f_time) <= UNIX_TIMESTAMP('" + TimeUtil.afterOneHourToNowDate(sdf.parse(time)) + "')");
        qw.orderByDesc("f_time");
        return new Result(0, "查询成功", dfQmsIpqcWaigTotalNewService.list(qw));
    }

    @GetMapping(value = "/listAll")
    public Object listAll() {
        return new Result(0, "查询成功", dfQmsIpqcWaigTotalNewService.list());
    }

    @GetMapping(value = "/getById")
    public Object getById(int id) {
        return new Result(0, "查询成功", dfQmsIpqcWaigTotalNewService.getById(id));
    }

    @PostMapping(value = "/saveOrUpdate")
    public Result save(@RequestBody DfQmsIpqcWaigTotalNew datas) {
        if (null != datas.getId()) {
            if (dfQmsIpqcWaigTotalNewService.updateById(datas)) {
                QueryWrapper<DfQmsIpqcWaigDetailNew> qw = new QueryWrapper<>();
                qw.eq("f_parent_id", datas.getId());
                dfQmsIpqcWaigDetailNewService.remove(qw);

                List<DfQmsIpqcWaigDetailNew> sl = new ArrayList<>();
                for (DfQmsIpqcWaigDetailNew d : datas.getDetailList()) {
                    d.setFParentId(datas.getId());
                    sl.add(d);
                }
                if (sl.size() > 0) {
                    dfQmsIpqcWaigDetailNewService.saveBatch(sl);
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {
            if (dfQmsIpqcWaigTotalNewService.save(datas)) {
                List<DfQmsIpqcWaigDetailNew> sl = new ArrayList<>();
                for (DfQmsIpqcWaigDetailNew d : datas.getDetailList()) {
                    d.setFParentId(datas.getId());
                    sl.add(d);
                }

                if (sl.size() > 0) {
                    dfQmsIpqcWaigDetailNewService.saveBatch(sl);
                }

                return new Result(200, "保存成功",datas.getId());
            } else {
                return new Result(500, "保存失败");
            }
        }
    }

    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin ( int page, int limit, String name, String factoryId, String lineBodyId, String
            type, String machineCode, String process, String startTime, String endTime, String testMan,String project){

        Page<DfQmsIpqcWaigTotalNew> pages = new Page<>(page, limit);
        QueryWrapper<DfQmsIpqcWaigTotalNew> ew = new QueryWrapper<>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }
        if (null != type && !type.equals("")) {
            ew.eq("type", type);
        }
        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.eq("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }
        ew.eq(StringUtils.isNotEmpty(project),"f_bigpro",project);
        ew.orderByDesc("f_time");
        IPage<DfQmsIpqcWaigTotalNew> list = dfQmsIpqcWaigTotalNewService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }


    @GetMapping(value = "/getTotalAndNgCount")
    public Result getTotalAndNgCount (String name, String factoryId, String lineBodyId, String type, String
            machineCode, String process, String startTime, String endTime, String testMan){

        QueryWrapper<DfQmsIpqcWaigTotalNew> ew = new QueryWrapper<>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }
        if (null != type && !type.equals("")) {
            ew.eq("type", type);
        }
        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.eq("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }
        return new Result(0, "查询成功", dfQmsIpqcWaigTotalNewService.getTotalAndNgCount(ew));

    }


    // 获取测试员名称
    @GetMapping(value = "/listTestMan")
    public Result listTestMan (String id){
        QueryWrapper<DfQmsIpqcWaigTotalNew> ew = new QueryWrapper<>();
        ew.isNotNull("f_test_man");
        ew.ne("f_test_man", "null");
        ew.select("distinct(f_test_man)");
        return new Result(0, "查询成功", dfQmsIpqcWaigTotalNewService.list(ew));
    }

    // 根据id删除
    @GetMapping(value = "/delete")
    @Transactional(rollbackFor = Exception.class)
    public Result delete (String id){

        if (dfQmsIpqcWaigTotalNewService.removeById(id)) {
            QueryWrapper<DfQmsIpqcWaigDetailNew> qw = new QueryWrapper<>();
            qw.eq("f_parent_id", id);
            dfQmsIpqcWaigDetailNewService.remove(qw);
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }

    @GetMapping(value = "/downloadExcel")
    @ApiOperation("外观数据导出")
    @ResponseBody
    public void downloadExcel (String type, String name, String factoryId, String lineBodyId,
                               String machineCode, String process, String startTime, String endTime,
                               String testMan, HttpServletResponse response, HttpServletRequest request
    ) throws IOException {

        QueryWrapper<DfQmsIpqcWaigTotalNew> ew = new QueryWrapper<>();
        if (null != name && !name.equals("")) {
            ew.like("f_barcode", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f_fac", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("f_line", lineBodyId);
        }

        if (null != machineCode && !machineCode.equals("")) {
            ew.like("f_mac", machineCode);
        }
        if (null != process && !process.equals("")) {
            ew.like("f_seq", process);
        }
        if (null != type && !type.equals("")) {
            ew.like("type", type);
        }
        if (null != testMan && !testMan.equals("")) {
            ew.like("f_test_man", testMan);
        }

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            ew.between("f_time", startTime, endTime);
        } else if (null != startTime && !startTime.equals("") && (null == endTime || endTime.equals(""))) {
            ew.ge("f_time", startTime);
        } else if (null != endTime && !endTime.equals("") && (null == startTime || startTime.equals(""))) {
            ew.le("f_time", endTime);
        }

        ew.orderByDesc("f_time");
        List<DfQmsIpqcWaigTotalNew> datas = dfQmsIpqcWaigTotalNewService.list(ew);

        List<Map> maps = new ArrayList<>();
        for (DfQmsIpqcWaigTotalNew r : datas) {
            try {
                maps.add(Excel.objectToMap(r));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String companyName = "外观数据列表";
        String sheetTitle = companyName;
        String[] title = new String[]{"外观类型", "工厂", "工序", "生产阶段", "项目", "线体", "颜色", "检验类型", "检验员", "测试类别", "机台号", "产品条码", "抽检总数", "NG数", "创建时间"};        //设置表格表头字段
        String[] properties = new String[]{"fType", "fFac", "fSeq", "fStage",
                "fBigpro", "fLine", "fColor", "fTestType", "fTestMan", "fTestCategory", "fMac", "fBarcode", "spotCheckCount", "affectCount", "fTime"};  // 查询对应的字段
        ExcelExportUtil excelExport2 = new ExcelExportUtil();
        excelExport2.setData(maps);
        excelExport2.setHeardKey(properties);
        excelExport2.setFontSize(14);
        excelExport2.setSheetName(sheetTitle);
        excelExport2.setTitle(sheetTitle);
        excelExport2.setHeardList(title);
        excelExport2.exportExport(request, response);
    }


}
