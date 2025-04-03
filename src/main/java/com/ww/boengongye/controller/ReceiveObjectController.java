package com.ww.boengongye.controller;

import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAoiDefectClassService;
import com.ww.boengongye.service.DfAoiDefectService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TxtToObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/receiveObject")
@ResponseBody
@CrossOrigin
public class ReceiveObjectController {

    private static final Logger logger = LoggerFactory.getLogger(LineBodyController.class);

    @Autowired
    private DfAoiDefectClassService dfAoiDefectClassService;

    @Autowired
    private DfAoiDefectService dfAoiDefectService;

    @Autowired
    private DfAoiPieceService dfAoiPieceService;


    @RequestMapping(value = "save")
    public Result saveReceiveObject(){
//        String folderPath ="D:\\WangWenSanWei\\资料\\数据库\\20230811\\trace-log0808\\trace-log0808";
//        List<String> fileList = TxtToObject.getAllTxtFiles(folderPath);
//        for (String filePath:fileList) {
            String filePath = "D:\\WangWenSanWei\\资料\\数据库\\20230811\\trace-log0808\\trace-log0808\\HDWGXQ00AEM00009HR+9+0200512057112805211358107J1691482862606.txt";
            ReceiveObject receiveObject = TxtToObject.txtToObject(filePath);
            List<DfAoiDefectClass> classes = receiveObject.getClasses();
            if (classes!=null&&classes.size()>0){
                for (DfAoiDefectClass dfAoiDefectClass :classes) {
//                    if (!dfAoiDefectClassService.save(dfAoiDefectClass)){
//                        return new Result(500,filePath+"文件中classid为"+ dfAoiDefectClass.getCode()+"对象数据导入失败");
//                    }
                }
            }
            List<Defect> defects =  receiveObject.getDefect();
            if (defects!=null&&defects.size()>0){
                List<Defectimages> defectimages = receiveObject.getDefectimages();
                for (Defect defect:defects) {
                    for (Defectimages defectimage: defectimages) {
                        if (defect.getDefectid().equals(defectimage.getDefectid())){
                            DfAoiDefect dfAoiDefect = new DfAoiDefect();

                            dfAoiDefect.setFrameid(defect.getFrameid());
                            dfAoiDefect.setDefectid(defect.getDefectid());
                            dfAoiDefect.setClassid(defect.getClassid());
                            dfAoiDefect.setFeaturevalues(defect.getFeaturevalues());
                            dfAoiDefect.setAOIxcenter(defect.getAOIxcenter());
                            dfAoiDefect.setAOIycenter(defect.getAOIycenter());
                            dfAoiDefect.setAttribute(defect.getAttribute());
                            dfAoiDefect.setSeverityid(defect.getSeverityid());
                            dfAoiDefect.setQualityid(defect.getQualityid());

                            dfAoiDefect.setBase64str(defectimage.getBase64str());
                            dfAoiDefect.setChannelkey(defectimage.getChannelkey());
                            dfAoiDefect.setImageid(defectimage.getImageid());

//                            if (!dfAoiDefectService.save(dfAoiDefect)){
//                                return new Result(500,filePath+"文件中id为"+dfAoiDefect.getId()+"对象数据导入失败");
//                            }
                        }
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
//                if (!dfAoiPieceService.save(dfAoiPiece)){
//                    return new Result(500,filePath+"文件中id为"+dfAoiPiece.getId()+"对象数据导入失败");
//                }
            }
//        }
        return new Result(200,filePath+"文件下的txt数据导入成功");
//        return new Result(200,folderPath+"文件下的txt数据导入成功");
    }



}
