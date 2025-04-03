package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFlowDrawingFile;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.entity.DfFlowDrawingFile;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfFlowDrawingFileMapper;
import com.ww.boengongye.service.DfFlowDrawingFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.ExcelImg;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程图纸 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Service
public class DfFlowDrawingFileServiceImpl extends ServiceImpl<DfFlowDrawingFileMapper, DfFlowDrawingFile> implements DfFlowDrawingFileService {
    @Autowired
    com.ww.boengongye.mapper.DfFlowDrawingFileMapper DfFlowDrawingFileMapper;

    @Autowired
    com.ww.boengongye.mapper.DfFactoryMapper DfFactoryMapper;

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




        List<DfFlowDrawingFile> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfFlowDrawingFile tc = new DfFlowDrawingFile();
            tc.setFactoryName(map.get("工厂"));
            QueryWrapper<DfFactory> fqw=new QueryWrapper<>();
            fqw.eq("factory_name",map.get("工厂"));
            fqw.last("limit 0,1");
            DfFactory df=DfFactoryMapper.selectOne(fqw);
            if(null!=df&&null!=df.getFactoryCode()){
                tc.setFactoryCode(df.getFactoryCode());
            }
            tc.setCategory(map.get("业务类别"));
            tc.setChangeScope(map.get("变更范围"));
            tc.setProjectCode(map.get("内部项目代码"));
            tc.setChangeVersionCode(map.get("变更前后版本号"));
            tc.setApplicationStage(map.get("适用阶段"));
            tc.setChangeContent(map.get("变更内容"));
            tc.setChangeDate(map.get("变更日期"));
            tc.setDownloadDate(map.get("Sift下载日期"));

            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            File file2 = ResourceUtils.getFile(filePath);
            InputStream inputStream = new FileInputStream(file2);
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
            Map<String, XSSFPictureData> map= ExcelImg.getPictures(xssfWorkbook.getSheetAt(0));
            int i=1;
            for (DfFlowDrawingFile c : orderDetails) {
                File folder = new File(env.getProperty("uploadPath")  + "/"+"flowDrawingFile"+ "/"+ c.factoryCode +  "/");
                if (!folder.exists()) {// 检查文件夹是否存在
                    folder.mkdirs();// 不存在则创建
                }
                String mapKey=i+"-9";//指定行和列
                XSSFPictureData xssfPictureData= map.get(mapKey);
                byte[] data =xssfPictureData.getData();
                String uuid= CommunalUtils.getUUID();
                String savePath=env.getProperty("uploadPath") + "/"+"flowDrawingFile"+ "/"+ c.factoryCode  + "/"+ uuid +".png";
                FileOutputStream out = new FileOutputStream(savePath);
                out.write(data);
                out.close();
                c.setUploadPath( "flowDrawingFile"+ "/"+c.factoryCode  + "/"+ uuid +".png");

                DfFlowDrawingFileMapper.insert(c);
                i++;
            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

}
