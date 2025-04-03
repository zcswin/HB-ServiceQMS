package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * AOI缺陷表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Controller
@RequestMapping("/dfAoiDefect")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI缺陷")
public class DfAoiDefectController {
    private static final Logger logger = LoggerFactory.getLogger(DfAoiDefectController.class);

    @Autowired
    private DfAoiDefectService dfAoiDefectService;

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private DfAoiDecideLogService dfAoiDecideLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private DfAoiUndetectedService dfAoiUndetectedService;

    @Autowired
    private DfAoiDefectSmallClassService dfAoiDefectSmallClassService;

    @Autowired
    private DfAoiDefectClassService dfAoiDefectClassService;


    /**
     * 通过玻璃条码判断改玻璃是否被FQC检测过
     * @param pieceName
     * @return
     */
    @GetMapping("getDfAoiDecideLogList")
    @ApiOperation("通过玻璃条码判断改玻璃是否被FQC检测过")
    public Result getDfAoiDecideLogList(String pieceName){

        QueryWrapper<DfAoiPiece> pieceWrapper = new QueryWrapper<>();
        pieceWrapper
                .select("bar_code")
                .eq("name",pieceName)
                .last("limit 1");
        DfAoiPiece dfAoiPiece = dfAoiPieceService.getOne(pieceWrapper);
        if (dfAoiPiece==null||dfAoiPiece.getBarCode()==null){
            return new Result(200,"该玻璃条码没有被AOI检测过",2);
        }

        QueryWrapper<DfAoiDecideLog> decideLogWrapper = new QueryWrapper<>();
        decideLogWrapper
                .eq("bar_code",dfAoiPiece.getBarCode())
                .last("limit 1");
        DfAoiDecideLog dfAoiDecideLog = dfAoiDecideLogService.getOne(decideLogWrapper);
        if (dfAoiDecideLog==null||dfAoiDecideLog.getId()==null||dfAoiDecideLog.getId()==0){
            return new Result(200,"该玻璃条码没有被FQC检测过",0);
        }
        return new Result(200,"该玻璃条码被FQC检测过",1);
    }


    /**
     * 通过明码获取相关AOI缺陷信息
     *
     * @param pieceName
     * @return
     */
    @RequestMapping(value = "listBySearch",method = RequestMethod.GET)
    @ApiOperation("扫描玻璃条码展示该玻璃的缺陷列表")
    public Result listBySearch(String pieceName) {
        QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
        ew.eq("dap.name", pieceName);
        List<DfAoiDefect> list = dfAoiDefectService.getDefectByBarCode(ew);
        if (list == null || list.size() == 0) {
            return new Result(200, "获取AOI缺陷失败");
        }
        return new Result(200, "获取AOI缺陷成功", list);
    }

    /**
     * 通过明码获取相关AOI缺陷信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "listByPieceName",method = RequestMethod.POST)
    @ApiOperation("扫描玻璃二维码展示该玻璃的缺陷列表")
    public Result listByPieceName(@RequestBody Map<String,String> map) {
        QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
        ew.eq("dap.name", map.get("pieceName"));
        List<DfAoiDefect> list = dfAoiDefectService.getDefectByPieceName(ew);
        if (list == null || list.size() == 0) {
            return new Result(200, "该玻璃没有缺陷");
        }
        return new Result(200, "获取AOI缺陷成功", list);
    }


    /**
     * FQC进行玻璃检测
     * @param defectId
     * @param userCode
     * @param pieceName
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ApiOperation("FQC进行玻璃检测")
    public Result update(String defectId, String userCode, String pieceName) {

        //获取当前时间
        Timestamp updateTime = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name", userCode);
        User user = userService.getOne(userQueryWrapper);

        //通过pieceName获取barCode
        QueryWrapper<DfAoiPiece> pieceWrapper = new QueryWrapper<>();
        pieceWrapper
                .select("bar_code")
                .eq("name",pieceName)
                .last("limit 1");
        DfAoiPiece dfAoiPieceOld = dfAoiPieceService.getOne(pieceWrapper);
        if (dfAoiPieceOld==null||dfAoiPieceOld.getBarCode()==null){
            return new Result(200,"该玻璃条码没有被AOI检测过",2);
        }
        String barCode = dfAoiPieceOld.getBarCode();

        //获取最新AOI玻璃单片信息对象
        QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper = new QueryWrapper<>();
        dfAoiPieceQueryWrapper.eq("bar_code", barCode);
        dfAoiPieceQueryWrapper.orderByDesc("`time`");
        List<DfAoiPiece> dfAoiPieceList = dfAoiPieceService.getDfAoiPieceListByBarCode(dfAoiPieceQueryWrapper);
        if (dfAoiPieceList == null || dfAoiPieceList.size() == 0) {
            return new Result(500, "获取判定信息中的AOI玻璃单片信息失败");
        }
        DfAoiPiece dfAoiPiece = dfAoiPieceList.get(0);

        //获取QC判定结果
        String qcResult = "OK";
        if (defectId!=null&&!"".equals(defectId)) {
            qcResult = "NG";
        }

        //修改AOI玻璃检测结果
        dfAoiPiece.setReResult(qcResult);
        dfAoiPiece.setReTime(String.valueOf(updateTime));
        dfAoiPiece.setUsername(user.getName());
        if (!dfAoiPieceService.updateById(dfAoiPiece)) {
            return new Result(500, "AOI玻璃单片信息检测失败");
        }

        //添加AOI玻璃检测记录
        DfAoiDecideLog dfAoiDecideLog = new DfAoiDecideLog();
        dfAoiDecideLog.setBarCode(barCode);
        dfAoiDecideLog.setDefectId(defectId);
        dfAoiDecideLog.setAoiResult(dfAoiPiece.getQualityid());
        dfAoiDecideLog.setQcResult(qcResult);
        dfAoiDecideLog.setQcTime(updateTime);
        dfAoiDecideLog.setQcUserCode(user.getName());
        dfAoiDecideLog.setQcUserName(user.getAlias());
        dfAoiDecideLog.setMachineCode(dfAoiPiece.getMachineCode());
        dfAoiDecideLog.setCreateTime(Timestamp.valueOf(dfAoiPiece.getTime()));
        if (!dfAoiDecideLogService.save(dfAoiDecideLog)) {
            return new Result(500, "AOI缺陷检测记录添加失败");
        }

        return new Result(200, "AOI缺陷检测成功");
    }


    /**
     * OQC进行玻璃检测
     * @param defectId
     * @param userCode
     * @param pieceName
     * @return
     */
    @RequestMapping(value = "updateEscape",method = RequestMethod.GET)
    @ApiOperation("OQC进行玻璃检测")
    public Result updateEscape(String defectId, String userCode, String pieceName) {
        //获取当前时间
        Timestamp updateTime = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name", userCode);
        User user = userService.getOne(userQueryWrapper);

        //通过pieceName获取barCode
        QueryWrapper<DfAoiPiece> pieceWrapper = new QueryWrapper<>();
        pieceWrapper
                .select("bar_code")
                .eq("name",pieceName)
                .last("limit 1");
        DfAoiPiece dfAoiPieceOld = dfAoiPieceService.getOne(pieceWrapper);

        if (dfAoiPieceOld==null||dfAoiPieceOld.getBarCode()==null){
            return new Result(200,"该玻璃条码没有被AOI检测过",2);
        }
        String barCode = dfAoiPieceOld.getBarCode();

        //获取最新AOI玻璃单片信息对象
        QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper = new QueryWrapper<>();
        dfAoiPieceQueryWrapper.eq("bar_code", barCode);
        dfAoiPieceQueryWrapper.orderByDesc("`time`");
        List<DfAoiPiece> dfAoiPieceList = dfAoiPieceService.getDfAoiPieceListByBarCode(dfAoiPieceQueryWrapper);
        if (dfAoiPieceList == null || dfAoiPieceList.size() == 0) {
            return new Result(500, "获取判定信息中的AOI玻璃单片信息失败");
        }
        DfAoiPiece dfAoiPiece = dfAoiPieceList.get(0);

        //获取QC判定结果
        String qcResult = "OK";
        if (defectId!=null&&!"".equals(defectId)) {
            qcResult = "NG";

            //获取该玻璃FQC检测记录
            QueryWrapper<DfAoiDecideLog> dfAoiDecideLogQueryWrapper = new QueryWrapper<>();
            dfAoiDecideLogQueryWrapper.eq("bar_code",barCode);
            dfAoiDecideLogQueryWrapper.last("limit 1");
            DfAoiDecideLog dfAoiDecideLog = dfAoiDecideLogService.getOne(dfAoiDecideLogQueryWrapper);

            if (null!=dfAoiDecideLog&&(dfAoiDecideLog.getQcResult()=="OK"||"OK".equals(dfAoiDecideLog.getQcResult()))){

                //保存该玻璃OQC漏检记录
                DfAoiUndetected dfAoiUndetected = new DfAoiUndetected();
                dfAoiUndetected.setBarcode(barCode);
                dfAoiUndetected.setDefectid(defectId);
                dfAoiUndetected.setFqcUser(dfAoiDecideLog.getQcUserCode());
                dfAoiUndetected.setOqcUse(userCode);
                dfAoiUndetected.setCreateTime(updateTime);

                if (!dfAoiUndetectedService.save(dfAoiUndetected)){
                    return new Result(500,"AOI玻璃漏检记录保存失败");
                }
            }
        }

        //修改AOI玻璃检测结果
        dfAoiPiece.setReResult(qcResult);
        dfAoiPiece.setReTime(String.valueOf(updateTime));
        dfAoiPiece.setUsername(user.getName());
        if (!dfAoiPieceService.updateById(dfAoiPiece)) {
            return new Result(500, "AOI玻璃单片信息修改失败");
        }


        //添加AOI玻璃检测记录
        DfAoiDecideLog dfAoiDecideLog = new DfAoiDecideLog();
        dfAoiDecideLog.setBarCode(barCode);
        dfAoiDecideLog.setDefectId(defectId);
        dfAoiDecideLog.setAoiResult(dfAoiPiece.getQualityid());
        dfAoiDecideLog.setQcResult(qcResult);
        dfAoiDecideLog.setQcTime(updateTime);
        dfAoiDecideLog.setQcUserCode(user.getName());
        dfAoiDecideLog.setQcUserName(user.getAlias());
        dfAoiDecideLog.setMachineCode(dfAoiPiece.getMachineCode());
        dfAoiDecideLog.setCreateTime(Timestamp.valueOf(dfAoiPiece.getTime()));
        if (!dfAoiDecideLogService.save(dfAoiDecideLog)) {
            return new Result(500, "AOI缺陷检测记录添加失败");
        }

        return new Result(200, "AOI缺陷检测成功");
    }


    @PostMapping(value = "/save")
    public Result save(@RequestBody DfAoiDefect datas) {
        QueryWrapper<DfAoiPiece> pqw = new QueryWrapper<>();
        pqw.eq("frameid", datas.getFrameid());//用frameid字段带过来
        pqw.orderByDesc("time");
        pqw.last("limit 1");
        DfAoiPiece piece= dfAoiPieceService.getOne(pqw);
        if(piece==null){
            return new Result(200, "该玻璃不存在");
        }

        QueryWrapper<DfAoiDefectSmallClass> defectSmallClassWrapper = new QueryWrapper<>();
        defectSmallClassWrapper
                .eq("name",datas.getFeaturevalues())
                .orderByDesc("create_time")
                .last("limit 1");
        DfAoiDefectSmallClass dfAoiDefectSmallClass = dfAoiDefectSmallClassService.getOne(defectSmallClassWrapper);
        if (dfAoiDefectSmallClass==null){
            return new Result(200,"该缺陷数据不存在");
        }

        QueryWrapper<DfAoiDefectClass> defectClassWrapper = new QueryWrapper<>();
        defectClassWrapper
                .eq("name",dfAoiDefectSmallClass.getClassName())
                .last("limit 1");
        DfAoiDefectClass dfAoiDefectClass = dfAoiDefectClassService.getOne(defectClassWrapper);

        datas.setClassCode(dfAoiDefectClass.getCode());
        datas.setMajorDefect(dfAoiDefectSmallClass.getMajorDefect());
        datas.setClassName(dfAoiDefectSmallClass.getClassName());
        String defectId =  UUID.randomUUID().toString().replace("-", "");
        datas.setCheckId(piece.getId());
        datas.setFrameid(piece.getFrameid());
        datas.setDefectid(defectId);
        if (!dfAoiDefectService.save(datas)) {
            return new Result(500, "保存失败");
        }
        return new Result(200, "保存成功");
    }


//    /**
//     * 修改所有相关AOI缺陷
//     * @param dfAoiDefectList
//     * @param userCode
//     * @param barCode
//     * @return
//     */
//    @RequestMapping(value = "update")
//    public Result update(@RequestBody List<DfAoiDefect> dfAoiDefectList,String userCode,String barCode){
//        //获取当前时间
//        Timestamp updateTime = Timestamp.valueOf(TimeUtil.getNowTimeByNormal());
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.eq("name",userCode);
//        User user = userService.getOne(userQueryWrapper);
//
//        //获取最新AOI玻璃单片信息对象
//        QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper = new QueryWrapper<>();
//        dfAoiPieceQueryWrapper.eq("bar_code",barCode);
//        dfAoiPieceQueryWrapper.orderByDesc("`time`");
//        List<DfAoiPiece> dfAoiPieceList = dfAoiPieceService.getDfAoiPieceListByBarCode(dfAoiPieceQueryWrapper);
//        if (dfAoiPieceList==null||dfAoiPieceList.size()==0){
//            return new Result(500,"获取判定信息中的AOI玻璃单片信息失败");
//        }
//        DfAoiPiece dfAoiPiece = dfAoiPieceList.get(0);
//
//        //获取QC判定结果
//        String qcResult = "OK";
//        for (DfAoiDefect dfAoiDefect:dfAoiDefectList) {
//            dfAoiDefect.setUpdateName(user.getAlias());
//            dfAoiDefect.setUpdateTime(updateTime);
//            dfAoiDefect.setReTime(String.valueOf(updateTime));
//            if (dfAoiDefect.getReResult()=="NG"||"NG".equals(dfAoiDefect.getReResult())){
//                qcResult = "NG";
//            }
//        }
//
//        if (!dfAoiDefectService.updateBatchById(dfAoiDefectList)) {
//            return new Result(500,"AOI缺陷不良信息修改失败");
//        }
//
//        dfAoiPiece.setReResult(qcResult);
//        dfAoiPiece.setReTime(String.valueOf(updateTime));
//        dfAoiPiece.setUsername(user.getName());
//        if (!dfAoiPieceService.updateById(dfAoiPiece)){
//            return new Result(500,"AOI玻璃单片信息检测失败");
//        }
//
//
//        DfAoiDecideLog dfAoiDecideLog = new DfAoiDecideLog();
//        dfAoiDecideLog.setBarCode(barCode);
//        dfAoiDecideLog.setAoiResult(dfAoiPiece.getQualityid());
//        dfAoiDecideLog.setQcResult(qcResult);
//        dfAoiDecideLog.setQcTime(updateTime);
//        dfAoiDecideLog.setQcUserCode(user.getName());
//        dfAoiDecideLog.setQcUserName(user.getAlias());
//        dfAoiDecideLog.setMachineCode(dfAoiPiece.getMachineCode());
//        dfAoiDecideLog.setCreateTime(Timestamp.valueOf(dfAoiPiece.getTime()));
//        if (!dfAoiDecideLogService.save(dfAoiDecideLog)){
//            return new Result(500,"AOI缺陷检测记录添加失败");
//        }
//
//        return new Result(200,"AOI缺陷检测成功");
//    }

    @GetMapping("fqcNgTopRate")
    @ApiOperation("线体看板明细 - FQC不良TOP(N)及占比")
    public Object fqcNgTopRate(@ApiParam("Top几") @RequestParam Integer top,
                             @ApiParam("厂别") @RequestParam(required = false) String factory,
                             @ApiParam("线体") @RequestParam(required = false) String lineBody,
                             @ApiParam("时间-开始") @RequestParam String startDate,
                             @ApiParam("时间-结束") @RequestParam String endDate,
                             @ApiParam("项目") @RequestParam(required = false) String project,
                             @ApiParam("颜色") @RequestParam(required = false) String color) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
        ew.eq("no",1);
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
            ew.between("TIME", startTime, endTime);
        }
        ew.eq(StringUtils.isNotEmpty(factory), "FACTORY",factory)
                .eq(StringUtils.isNotEmpty(lineBody), "LINE_BODY",lineBody)
                .eq(StringUtils.isNotEmpty(project), "PROJECT",project)
                .eq(StringUtils.isNotEmpty(color), "COLOR",color);

        List<Map<String,Object>> list = dfAoiDefectService.fqcNgTopRate(ew,top);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> listQx = new ArrayList<>();
        ArrayList<Object> listRate = new ArrayList<>();
        for (Map<String, Object> one : list) {
            listQx.add(one.get("FEATUREVALUES"));
            listRate.add(one.get("rate"));
        }
        map.put("listQx",listQx);
        map.put("listRate",listRate);
        return new Result<>(200,"查询成功",map);
    }


}
