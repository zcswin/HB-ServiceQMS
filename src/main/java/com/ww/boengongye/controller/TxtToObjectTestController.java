package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAoiDefectClassService;
import com.ww.boengongye.service.DfAoiDefectService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.service.DfAoiPositionService;
import com.ww.boengongye.utils.TxtToObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/TxtToObjectTestController")
@ResponseBody
@CrossOrigin
@Api(tags = "导入AOI数据，txt转对象")
public class TxtToObjectTestController {


    @Autowired
    private DfAoiDefectClassService dfAoiDefectClassService;

    @Autowired
    private DfAoiDefectService dfAoiDefectService;

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private DfAoiPositionService dfAoiPositionService;


//    /**
//     * 删除文件下所有txt文件的最后一行
//     */
//    @GetMapping(value = "deleteLastLineInFile")
//    @ApiOperation("删除文件下所有txt文件的最后一行")
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteLastLineInFile(){
//        String folderPath ="D:\\WangWenSanWei\\资料\\excel模板\\AOI";
//        List<String> fileList = TxtToObject.getAllTxtFiles(folderPath);
//        for (String filePath:fileList) {
//            TxtToObject.deleteLastLineInFile(filePath);
//            System.out.println(filePath+"删除成功");
//        }
//    }


    @GetMapping(value = "saveReceiveObject")
    @ApiOperation("导入AOI玻璃和缺陷数据")
    @Transactional(rollbackFor = Exception.class)
    public void saveReceiveObject(String folderPath){
//        folderPath ="D:\\WangWenSanWei\\资料\\excel模板\\AOI\\27435439012edd029d3de5e2f75a6588\\AOI数据 10_19\\12-19数据.zip\\A班\\2023-10-12";
        List<String> fileList = TxtToObject.getAllTxtFiles(folderPath);

        //所有缺陷大类
        List<DfAoiDefectClass> dfAoiDefectClassesList = dfAoiDefectClassService.list();

        for (String filePath:fileList) {
//            String filePath = "D:\\WangWenSanWei\\资料\\数据库\\20230811\\trace-log0808\\trace-log0808\\HDWGXQ00AEM00009HR+9+0200512057112805211358107J1691482862606.txt";
            ReceiveObject receiveObject = TxtToObject.txtToObject(filePath);
            List<DfAoiDefectClass> classes = receiveObject.getClasses();
            if (classes!=null&&classes.size()>0){
                for (DfAoiDefectClass dfAoiDefectClass :classes) {
                    if (!dfAoiDefectClassService.save(dfAoiDefectClass)){
                        System.out.println(filePath+"文件中classid为"+ dfAoiDefectClass.getCode()+"对象数据导入失败");
                        return;
                    }
                }
            }
            DfAoiPiece dfAoiPiece = receiveObject.getPieces();
            if (dfAoiPiece!=null&&dfAoiPiece.getPieceid()!=null){
                String name =  dfAoiPiece.getName();
                int plusIndex = name.indexOf("+");
                if (plusIndex!=-1){
                    String barCode = name.substring(0,plusIndex);
                    dfAoiPiece.setBarCode(barCode);
                }
                dfAoiPiece.setColor("蓝色");
                dfAoiPiece.setProject("C27");
                dfAoiPiece.setMachine("1#");
                dfAoiPiece.setFactory("J10-1");
                dfAoiPiece.setLineBody("Line-23");

                if (!dfAoiPieceService.save(dfAoiPiece)){
                    System.out.println(filePath+"文件中id为"+dfAoiPiece.getId()+"对象数据导入失败");
                    return;
                }
            }

            List<Defect> defects =  receiveObject.getDefect();
            if (defects!=null&&defects.size()>0){
                List<Defectimages> defectimages = receiveObject.getDefectimages();
                for (Defect defect:defects) {
                    for (Defectimages defectimage: defectimages) {
                        if (defect.getDefectid().equals(defectimage.getDefectid())){
                            DfAoiDefect dfAoiDefect = new DfAoiDefect();
                            dfAoiDefect.setCheckId(dfAoiPiece.getId());
                            dfAoiDefect.setFrameid(defect.getFrameid());
                            dfAoiDefect.setDefectid(defect.getDefectid());
                            dfAoiDefect.setClassid(defect.getClassid());
                            dfAoiDefect.setFeaturevalues(defect.getFeaturevalues());

                            DfAoiDefectClass dfAoiDefectClass = dfAoiDefectClassesList.stream().filter(
                                    entity -> dfAoiDefect.getFeaturevalues().contains(entity.getName())
                            ).findFirst().orElse(null);
                            String classCode = "6";
                            String className = "其他";
                            if (dfAoiDefectClass!=null){
                                classCode = dfAoiDefectClass.getCode();
                                className = dfAoiDefectClass.getName();
                            }
                            dfAoiDefect.setClassCode(classCode);
                            dfAoiDefect.setClassName(className);

                            dfAoiDefect.setAOIxcenter(defect.getAOIxcenter());
                            dfAoiDefect.setAOIycenter(defect.getAOIycenter());
                            dfAoiDefect.setAttribute(defect.getAttribute());
                            dfAoiDefect.setSeverityid(defect.getSeverityid());
                            dfAoiDefect.setQualityid(defect.getQualityid());

                            dfAoiDefect.setBase64str(defectimage.getBase64str());
                            dfAoiDefect.setChannelkey(defectimage.getChannelkey());
                            dfAoiDefect.setImageid(defectimage.getImageid());

                            if (!dfAoiDefectService.save(dfAoiDefect)){
                                System.out.println(filePath+"文件中id为"+dfAoiDefect.getId()+"对象数据导入失败");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    @GetMapping(value = "updatePieces")
    @ApiOperation("修改缺陷的区域")
    @Transactional(rollbackFor = Exception.class)
    public void updatePieces(){
        QueryWrapper<DfAoiDefect> defectWrapper = new QueryWrapper<>();
        defectWrapper.select("id","AOIxcenter","AOIycenter");

        List<DfAoiDefect> dfAoiDefectList = dfAoiDefectService.list(defectWrapper);


        QueryWrapper<DfAoiPosition> positionWrapper = new QueryWrapper<>();
        positionWrapper
                .select("big_area","area","x1","x2","y1","y3","tier")
                .eq("project","C27")
                .isNotNull("tier")
                .orderByAsc("tier");

        List<DfAoiPosition> dfAoiPositionList = dfAoiPositionService.list(positionWrapper);

        for (DfAoiDefect dfAoiDefect:dfAoiDefectList){
            if (dfAoiDefect.getId()<=240){
                continue;
            }
            Double x = Double.valueOf(dfAoiDefect.getAOIxcenter());
            Double y = Double.valueOf(dfAoiDefect.getAOIycenter());

            for (DfAoiPosition dfAoiPosition : dfAoiPositionList){
                Double x1 = dfAoiPosition.getX1();
                Double x2 = dfAoiPosition.getX2();
                Double y1 = dfAoiPosition.getY1();
                Double y3 = dfAoiPosition.getY3();

                if (x>x1&&x<x2&&y>y1&&y<y3){
                    String bigArea = dfAoiPosition.getBigArea();
                    String area = dfAoiPosition.getArea();
                    dfAoiDefect.setBigArea(bigArea);
                    dfAoiDefect.setArea(area);

                    if (!dfAoiDefectService.updateById(dfAoiDefect)){
                        System.out.println("ID为"+dfAoiDefect.getId()+"的缺陷修改失败");
                        return;
                    }
                    break;
                }
            }
        }
    }

}
