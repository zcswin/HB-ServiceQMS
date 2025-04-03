package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.entity.DfQoaBomSon;
import com.ww.boengongye.entity.DfQoaBomSon;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfQoaBomSonMapper;
import com.ww.boengongye.service.DfQoaBomSonService;
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
 * QA-BOM- 收集产品生产过程需参考的文件-子流程 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-02
 */
@Service
public class DfQoaBomSonServiceImpl extends ServiceImpl<DfQoaBomSonMapper, DfQoaBomSon> implements DfQoaBomSonService {
    @Autowired
    com.ww.boengongye.mapper.DfQoaBomSonMapper DfQoaBomSonMapper;

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




        List<DfQoaBomSon> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfQoaBomSon tc = new DfQoaBomSon();
//            tc.setFactoryName(map.get("工厂"));
            QueryWrapper<DfFactory> fqw=new QueryWrapper<>();
            fqw.eq("factory_name",map.get("工厂"));
            fqw.last("limit 0,1");
            DfFactory df=DfFactoryMapper.selectOne(fqw);
//            if(null!=df&&null!=df.getFactoryCode()){
//                tc.setFactoryCode(df.getFactoryCode());
//            }
//            tc.setProject(map.get("项目"));
//            tc.setChangeType(map.get("变更类型"));
//            tc.setApplyModel(map.get("适用型号"));
//            tc.setOccasionApplicable(map.get("适用场合"));
//            tc.setFileName(map.get("文件名称"));
//            tc.setFileType(map.get("文件类型"));
//            tc.setVersion(map.get("版本号"));
//            tc.setMakingDerpartment(map.get("制定部门"));
//            tc.setDocumentMaker(map.get("文件制定人"));
//            tc.setReleaseDate(map.get("发布日期"));
////            tc.setFileUpload(map.get("文件上传"));
//            tc.setConfirmor(map.get("确认人"));
//            tc.setRegistrationTime(map.get("登记时间"));

//            tc.set(map.get(""));
            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            File file2 = ResourceUtils.getFile(filePath);
            InputStream inputStream = new FileInputStream(file2);
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
            Map<String, XSSFPictureData> map= ExcelImg.getPictures(xssfWorkbook.getSheetAt(0));
            int i=1;
//            for (DfQoaBomSon c : orderDetails) {
//                File folder = new File(env.getProperty("uploadPath")  + "/"+"sopControllerFile"+ "/"+ c.factoryCode +  "/");
//                if (!folder.exists()) {// 检查文件夹是否存在
//                    folder.mkdirs();// 不存在则创建
//                }
//                String mapKey=i+"-13";//指定行和列
//                XSSFPictureData xssfPictureData= map.get(mapKey);
//                byte[] data =xssfPictureData.getData();
//                String uuid= CommunalUtils.getUUID();
//                String savePath=env.getProperty("uploadPath") + "/"+"sopControllerFile"+ "/"+ c.factoryCode  + "/"+ uuid +".png";
//                FileOutputStream out = new FileOutputStream(savePath);
//                out.write(data);
//                out.close();
//                c.setUploadPath( "sopControllerFile"+ "/"+c.factoryCode  + "/"+ uuid +".png");
//
//                DfQoaBomSonMapper.insert(c);
//                i++;
//            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

}
