package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.controller.DfFlowDataController;
import com.ww.boengongye.entity.DfDrawingFile;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfDrawingFileMapper;
import com.ww.boengongye.mapper.DfFactoryMapper;
import com.ww.boengongye.service.DfDrawingFileService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 收集各阶段生产需参考的DFM文件 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-25
 */
@Service
public class DfDrawingFileServiceImpl extends ServiceImpl<DfDrawingFileMapper, DfDrawingFile> implements DfDrawingFileService {
  
    @Autowired
    DfDrawingFileMapper DfDrawingFileMapper;

    @Autowired
    DfFactoryMapper DfFactoryMapper;

    @Autowired
    private Environment env;

    @Autowired
    private DfFlowDataController dfFlowDataController;

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




        List<DfDrawingFile> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfDrawingFile tc = new DfDrawingFile();
//            tc.setFactoryName(map.get("工厂"));
//            QueryWrapper<DfFactory>fqw=new QueryWrapper<>();
//            fqw.eq("factory_name",map.get("工厂"));
//            fqw.last("limit 0,1");
//            DfFactory df=DfFactoryMapper.selectOne(fqw);
//            if(null!=df&&null!=df.getFactoryCode()){
//                tc.setFactoryCode(df.getFactoryCode());
//            }
            tc.setProject(map.get("项目"));
            tc.setCategory(map.get("类别"));
            tc.setApplicationStage(map.get("适用范围/阶段"));
            tc.setProperty(map.get("属性"));
            tc.setDfmName(map.get("DFM名称"));
            tc.setChangeVersion(map.get("变更DFM版本"));
            tc.setChangeDrawingVersion(map.get("变更DFM图纸版本"));
            tc.setChangeTime(map.get("变更日期"));
            tc.setRegistrationTime(map.get("上传Teams时间"));
//            tc.setUploadPath(map.get("上传Teams路径"));
//            tc.setApprovalTime(Timestamp.valueOf(map.get("客户审批时间")));
//            tc.setChangeContent(map.get("变更内容"));
//            tc.setConfirmor(map.get("确认人"));
//            tc.setRegistrationTime(Timestamp.valueOf(map.get("登记时间")));
            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
//            File file2 = ResourceUtils.getFile(filePath);
//            InputStream inputStream = new FileInputStream(file2);
//            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
//            Map<String, XSSFPictureData> map= ExcelImg.getPictures(xssfWorkbook.getSheetAt(0));
//            int i=1;
            for (DfDrawingFile c : orderDetails) {
//                File folder = new File(env.getProperty("uploadPath") + "dfm/");
//                if (!folder.exists()) {// 检查文件夹是否存在
//                    folder.mkdirs();// 不存在则创建
//                }
//                String mapKey=i+"-14";//指定行和列
//                XSSFPictureData xssfPictureData= map.get(mapKey);
//                byte[] data =xssfPictureData.getData();
//                String uuid=CommunalUtils.getUUID();
//                String savePath=env.getProperty("uploadPath") + "dfm/"+ uuid +".png";
//                FileOutputStream out = new FileOutputStream(savePath);
//                out.write(data);
//                out.close();
//                c.setUploadPath(savePath);

                DfDrawingFileMapper.insert(c);
//                i++;
            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

    @Override
    public List<Map<String, Object>> listByExport(QueryWrapper<DfDrawingFile> qw) {
        return DfDrawingFileMapper.listByExport(qw);
    }
}
