package com.ww.boengongye.service.impl;


import com.ww.boengongye.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 * @desc:数据导出服务

 * @author: chao

 * @time: 2018.6.11

 */

@Service

public class ExportDataService {

    @Autowired

    ExportExcelUtil exportExcelUtil;

    /*导出用户数据表*/

    public void exportDataToEx(HttpServletResponse response, ArrayList titleKeyList, Map titleMap, List src_list) {

        try {

            exportExcelUtil.expoerDataExcel(response, titleKeyList, titleMap, src_list);

        } catch (Exception e) {

            System.out.println("Exception: " + e.toString());

        }

    }

}