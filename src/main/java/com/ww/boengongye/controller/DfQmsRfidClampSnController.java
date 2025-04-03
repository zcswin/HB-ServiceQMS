package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfQmsRfidClampSn;
import com.ww.boengongye.service.DfQmsRfidClampSnService;
import com.ww.boengongye.utils.Excel;
import com.ww.boengongye.utils.ExcelExportUtil;
import com.ww.boengongye.utils.ExcelExportUtil2;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-06-24
 */
@Controller
@RequestMapping("/dfQmsRfidClampSn")
@Api(tags = "RFID载具检查情况")
@ResponseBody
@CrossOrigin
public class DfQmsRfidClampSnController {

    @Autowired
    DfQmsRfidClampSnService dfQmsRfidClampSnService;

    @ApiOperation("玻璃检测记录导出")
    @GetMapping(value = "/dataExport")
    public void dataExport (
            String barCode,
            String process, String clampCode, String startTime, String endTime,
             HttpServletResponse response, HttpServletRequest request
    ) throws IOException, ParseException {

        QueryWrapper<DfQmsRfidClampSn> ew = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            ew.between("c.create_time",startTime + " 07:00:00", TimeUtil.getNextDay(endTime) + " 07:00:00");
        }

        if (StringUtils.isNotEmpty(barCode) && StringUtils.isNotEmpty(barCode)){
            ew.like("c.bar_code",barCode);
        }

        if (StringUtils.isNotEmpty(process) && StringUtils.isNotEmpty(process)){
            ew.like("c.process",process);
        }

        if (StringUtils.isNotEmpty(clampCode) && StringUtils.isNotEmpty(clampCode)){
            ew.like("c.clamp_code",clampCode);
        }
        ew.orderByDesc("clamp_code");
        List<DfQmsRfidClampSn> list = dfQmsRfidClampSnService.listByExport(ew);
        TreeSet<String> fSortSet = new TreeSet<>();


        List<Map> maps = new ArrayList<>();
        for (DfQmsRfidClampSn r : list) {



            try {
                maps.add(Excel.objectToMap(r));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


//        if (maps != null && maps.size() > 0) {
        String companyName = "抽检数据列表";
        String sheetTitle = companyName;
        String[] title = new String[]{"载具ID", "产品SN", "外观抽检", "尺寸抽检", "外观检验时间", "尺寸检验时间", "载具加工时间"};        //设置表格表头字段
        String[] properties = new String[]{"clampCode", "barCode", "isWg", "isSize", "wgTime","sizeTime", "productTime"};  // 查询对应的字段
        ExcelExportUtil excelExport2 = new ExcelExportUtil();
        excelExport2.setData(maps);
        excelExport2.setHeardKey(properties);
        excelExport2.setFontSize(14);
        excelExport2.setSheetName(sheetTitle);
        excelExport2.setTitle(sheetTitle);
        excelExport2.setHeardList(title);
        excelExport2.exportExport(request, response);

//        }
    }
}
