package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAoiDecideLog;
import com.ww.boengongye.entity.DfAoiDetermine;
import com.ww.boengongye.entity.DfAoiPiece;
import com.ww.boengongye.entity.DfECodeProtect;
import com.ww.boengongye.service.DfAoiDecideLogService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.service.DfECodeProtectService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * AOI判定信息
 */
@Controller
@RequestMapping("/dfAoiDetermine")
@ResponseBody
@CrossOrigin
@Api(tags = "Aoi判定信息")
public class DfAoiDetermineController {

    private static final Logger logger = LoggerFactory.getLogger(DfAoiDetermineController.class);

    @Autowired
    private DfECodeProtectService dfECodeProtectService;

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private DfAoiDecideLogService dfAoiDecideLogService;


    @RequestMapping(value = "getDfAoiDetermine",method = RequestMethod.GET)
    @ApiOperation("判定信息")
    public Result getDfAoiDetermine(String pieceName) {
        //生产时间
        String productTime = TimeUtil.getProductTimeByPieceName(pieceName);
        String productWeek ="WK"+TimeUtil.getWeekByTimeStr(productTime);

        //获取最新AOI玻璃单片信息对象
        QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper = new QueryWrapper<>();
        dfAoiPieceQueryWrapper
                .eq("name",pieceName)
                .orderByDesc("`time`");
        List<DfAoiPiece> dfAoiPieceList = dfAoiPieceService.list(dfAoiPieceQueryWrapper);
        if (dfAoiPieceList==null||dfAoiPieceList.size()==0){
            return new Result(500,"获取判定信息中的AOI玻璃单片信息失败");
        }
        DfAoiPiece dfAoiPiece = dfAoiPieceList.get(0);
        //玻璃码
        String barCode = dfAoiPiece.getBarCode();

        QueryWrapper<DfAoiDetermine> dfAoiDetermineQueryWrapper = new QueryWrapper<>();
        dfAoiDetermineQueryWrapper
                .eq("dap_new.num",1)
                .eq("dap_new.bar_code",barCode);
        DfAoiDetermine dfAoiDetermine = dfAoiPieceService.getDfAoiDetermineByBarCode(dfAoiDetermineQueryWrapper);

        //获取最新判定记录对象
        QueryWrapper<DfAoiDecideLog> dfAoiDecideLogQueryWrapper = new QueryWrapper<>();
        dfAoiDecideLogQueryWrapper.eq("bar_code",barCode);
        dfAoiDecideLogQueryWrapper.orderByDesc("qc_time");
        List<DfAoiDecideLog> dfAoiDecideLogList = dfAoiDecideLogService.list(dfAoiDecideLogQueryWrapper);
        String reResult = null;
        if (dfAoiDecideLogList!=null&&dfAoiDecideLogList.size()>0){
            DfAoiDecideLog dfAoiDecideLog = dfAoiDecideLogList.get(0);
            reResult = dfAoiDecideLog.getQcResult();
        }

        dfAoiDetermine.setAoiNumber(String.valueOf(dfAoiPieceList.size()));
        dfAoiDetermine.setNewResult(dfAoiPiece.getQualityid());
        dfAoiDetermine.setReResult(reResult);
        dfAoiDetermine.setProductWeek(productWeek);
        dfAoiDetermine.setFrameid(dfAoiPiece.getFrameid());

        return new Result(200, "获取Aoi判定信息成功",dfAoiDetermine);
    }


}
