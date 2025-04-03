package com.ww.boengongye.controller;

import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAoiBatchService;
import com.ww.boengongye.service.DfAoiDefectService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.utils.AOIUploadResult;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/api")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI上传")
public class AOIUpdateController {
    @Autowired
    DfAoiPieceService dfAoiPieceService;


    @Autowired
    DfAoiDefectService dfAoiDefectService;

    @PostMapping(value = "/User/UploadApi")
    public AOIUploadResult save(@RequestBody AOIUpload datas) {
        if (null!=datas) {

            datas.getPieces().setReTime(datas.getPieces().getrE_time());
            datas.getPieces().setColor("蓝色");
            datas.getPieces().setProject("C27");
            datas.getPieces().setMachine("1#");
            datas.getPieces().setFactory("J10-1");
            datas.getPieces().setLineBody("Line-23");
            dfAoiPieceService.save(datas.getPieces());

            HashMap<String,AOIClasses> cm=new HashMap<>();
            if(null!=datas.getClasses()&&datas.getClasses().size()>0) {
                for (AOIClasses c : datas.getClasses()) {
                    cm.put(c.getClassid(),c);
                }
            }

            HashMap<String,AOIDefectImages> ci=new HashMap<>();
            if(null!=datas.getDefectimages()&&datas.getDefectimages().size()>0) {
                for (AOIDefectImages c : datas.getDefectimages()) {
                    ci.put(c.getDefectid(),c);
                }
            }

            if(null!=datas.getDefect()&&datas.getDefect().size()>0){
                for(DfAoiDefect d:datas.getDefect()){
                    d.setReResult(d.getrE_result());
                    d.setReTime(d.getrE_time());
                    d.setCheckId(datas.getPieces().getId());
                   if(cm.containsKey(d.getClassid())){
                       d.setClassName(cm.get(d.getClassid()).getClassname());
                   }

                    if(ci.containsKey(d.getDefectid())){
                       d.setBase64str(ci.get(d.getDefectid()).getBase64str());
                       d.setChannelkey(ci.get(d.getDefectid()).getChannelkey());
                       d.setImageid(ci.get(d.getDefectid()).getImageid());
                    }


                }
                dfAoiDefectService.saveBatch(datas.getDefect());
            }


            return new AOIUploadResult("1", "数据上传成功");
        } else {
            return new AOIUploadResult("2", "数据上传失败");
        }

    }
}
