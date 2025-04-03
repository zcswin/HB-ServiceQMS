package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiPosition;
import com.ww.boengongye.service.DfAoiPositionService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-01
 */
@Controller
@RequestMapping("/dfAoiPosition")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI位置绑定")
public class DfAoiPositionController {
    @Autowired
    DfAoiPositionService dfAoiPositionService;

    @GetMapping(value = "/create")
    public Result createPosition(String project,String bigArea,double xPiece,double yPiece,String code){
        QueryWrapper<DfAoiPosition>qw=new QueryWrapper<>();
        qw.eq("big_area",bigArea);
        qw.eq("project",project);
        qw.last("limit 1");
        DfAoiPosition b=dfAoiPositionService.getOne(qw);
        System.out.println(b.toString());
        double width=b.getWidth()/xPiece;
        double height=b.getHeight()/yPiece;
        double x=0;
        double y=0;
        List<DfAoiPosition>saveList=new ArrayList<>();
        int xCount=1;
        for(int i=1;i<=(xPiece*yPiece);i++){

            DfAoiPosition s=new DfAoiPosition();
            s.setBigArea(bigArea);
            s.setProject(project);
            s.setArea(code+(i));
            s.setType("small");
            s.setX1(x+((xCount-1)*width));
            s.setY1(y);
            s.setX2(x+((xCount)*width));
            s.setY2(y);
            s.setX3(x);
            s.setY3(y+height);
            s.setX4(x+((xCount)*width));
            s.setY4(y+height);
            if(xCount-xPiece==0){
                y+=height;
                xCount=1;
            }else{
                xCount++;
            }
            saveList.add(s);
        }

        dfAoiPositionService.saveBatch(saveList);


        return new Result(200,"创建成功");
    }


    @GetMapping(value = "/createOther")
    public Result createOther(String project,String bigArea,String smallArea,String code){
        QueryWrapper<DfAoiPosition>qw=new QueryWrapper<>();
        qw.eq("big_area","用户面");
        qw.eq("project",project);
        qw.last("limit 1");
        DfAoiPosition b=dfAoiPositionService.getOne(qw);
        System.out.println(b.toString());

        QueryWrapper<DfAoiPosition>qw2=new QueryWrapper<>();
        qw2.eq("big_area",bigArea);
        if(null!=smallArea&&!smallArea.equals("")){
            qw2.eq("area",smallArea);
        }

        qw2.eq("project",project);
        qw2.last("limit 1");
        DfAoiPosition data=dfAoiPositionService.getOne(qw2);
        double x=b.getWidth()/2;
        double y=b.getHeight()/2;


        if(bigArea.equals("Logo")){


            data.setX1(x- data.getpLeft());
            data.setY1(y-data.getpTop());
            data.setX2(x+ data.getpRight());
            data.setY2(y-data.getpTop());

            data.setX3(x- data.getpLeft());
            data.setY3(y+data.getpBottom());
            data.setX4(x+ data.getpRight());
            data.setY4(y+data.getpBottom());

        }else if(bigArea.equals("孔")){
            //以圆心计算
            data.setX1(x- data.getCententX()-(data.getWidth()/2));
            data.setY1(y-data.getCententY()- data.getHeight()/2 );
            data.setX2(x- data.getCententX()+ (data.getWidth()/2));
            data.setY2(y-data.getCententY()- data.getHeight()/2);

            data.setX3(x- data.getCententX()-(data.getWidth()/2));
            data.setY3(y-data.getCententY());
            data.setX4(x- data.getCententX()+ (data.getWidth()/2));
            data.setY4(y-data.getCententY());
        }

        dfAoiPositionService.updateById(data);

        return new Result(200,"创建成功");
    }
}
