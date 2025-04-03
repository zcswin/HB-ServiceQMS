package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfQcpStandard;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfQcpStandardMapper;
import com.ww.boengongye.service.DfQcpStandardService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * QCP标准 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Service
public class DfQcpStandardServiceImpl extends ServiceImpl<DfQcpStandardMapper, DfQcpStandard> implements DfQcpStandardService {
    @Autowired
    DfQcpStandardMapper DfQcpStandardMapper;


    @Autowired
    private Environment env;

    @Override
    public ImportExcelResult importOrder(String filePath, MultipartFile file) throws Exception {
        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfQcpStandard> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfQcpStandard tc = new DfQcpStandard();
            tc.setCategory(map.get("类别"));
            tc.setModel(map.get("型号"));
            tc.setBuild(map.get("Build"));
            tc.setTitle(map.get("Title"));
            tc.setRev(map.get("Rev"));
            tc.setDate(null == map.get("Date") ? null:map.get("Date"));
            tc.setDrawing(map.get("Drawing"));
            tc.setDrawRev(map.get("DrawRev"));
            tc.setProcess(map.get("Process"));
            tc.setProcessToolingChanges(map.get("ProcessToolingChanges"));
            tc.setComment(map.get("comment"));
            tc.setMqeApprove(map.get("MqeApprove"));
            tc.setApproveDate(null == map.get("ApproveDate") ? null:(map.get("ApproveDate")));
            tc.setRemarks(map.get("备注"));
            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            File file2 = ResourceUtils.getFile(filePath);
            InputStream inputStream = new FileInputStream(file2);
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
//            Map<String, XSSFPictureData> map= ExcelImg.getPictures(xssfWorkbook.getSheetAt(0));
            int i=1;
            for (DfQcpStandard c : orderDetails) {
                File folder = new File(env.getProperty("uploadPath") + "qcp/");
                if (!folder.exists()) {// 检查文件夹是否存在
                    folder.mkdirs();// 不存在则创建
                }
//                String mapKey=i+"-14";//指定行和列
//                XSSFPictureData xssfPictureData= map.get(mapKey);
//                byte[] data =xssfPictureData.getData();
//                String fileName = "qcp/"  + CommunalUtils.getUUID() + ".png";
//                FileOutputStream out = new FileOutputStream(env.getProperty("uploadPath") + fileName);
//                out.write(data);
//                out.close();
//                c.setUploadPath(fileName);
//                c.setRealPath(fileName);
                DfQcpStandardMapper.insert(c);
            }
		}
        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

    @Override
    public List<Map<String, Object>> listByExport(QueryWrapper<DfQcpStandard> qw) {
        return DfQcpStandardMapper.listByExport(qw);
    }
}
