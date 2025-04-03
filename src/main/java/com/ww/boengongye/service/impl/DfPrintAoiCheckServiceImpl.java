package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfPrintAoiCheckDetailMapper;
import com.ww.boengongye.mapper.DfPrintAoiCheckMapper;
import com.ww.boengongye.service.DfPrintAoiCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 移印AOI检测 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Service
public class DfPrintAoiCheckServiceImpl extends ServiceImpl<DfPrintAoiCheckMapper, DfPrintAoiCheck> implements DfPrintAoiCheckService {

    @Autowired
    private DfPrintAoiCheckMapper dfPrintAoiCheckMapper;

    @Autowired
    private DfPrintAoiCheckDetailMapper dfPrintAoiCheckDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);
//        String[][] strings = excel.readExcelBlock(1, -1, 1, 120);
//        String[] item = new String[]{"", "", "", "", "", "", "","","", "",
//                "大孔划伤","大孔擦伤", "大孔毛丝","大孔颗粒","大孔厚度不均","大孔缺墨","大孔锯齿","大孔平台溢墨","大孔漏印","VR划伤",
//                "WV划伤","AV划伤", "VR擦伤","WV擦伤","AV擦伤","VR毛丝","WV毛丝","AV毛丝","VR颗粒","WV颗粒",
//                "AV颗粒","VR厚度不均","WV厚度不均","AV厚度不均","VR缺墨","WV缺墨","AV缺墨","VR锯齿","WV锯齿","AV锯齿",
//                "AV平台溢墨","WV平台溢墨","VR平台溢墨","VR漏印","MV漏印","AV漏印","VR溢墨宽度","VR溢墨宽度","VR溢墨宽度","VR溢墨宽度",
//                "VR溢墨宽度","WV溢墨宽度","WV溢墨宽度","WV溢墨宽度","WV溢墨宽度","WV溢墨宽度","AV溢墨宽度","AV溢墨宽度","AV溢墨宽度","AV溢墨宽度",
//                "AV溢墨宽度","VR油墨深度","VR油墨深度","VR油墨深度","VR油墨深度","VR油墨深度","WV油墨深度","WV油墨深度","WV油墨深度","WV油墨深度",
//                "WV油墨深度","AV油墨深度","AV油墨深度","AV油墨深度","AV油墨深度","AV油墨深度","S孔划伤","S孔擦伤","S孔毛丝","S孔颗粒",
//                "S孔厚度不均","S孔缺墨","S孔锯齿","S孔平台溢墨","S孔漏印","S划伤","S擦伤","S毛丝","S颗粒","S厚度不均",
//                "S缺墨","S锯齿","S平台溢墨","S漏印","S溢墨宽度","S溢墨宽度","S溢墨宽度","S溢墨宽度","S溢墨宽度","S油墨深度",
//                "S油墨深度","S油墨深度","S油墨深度","S油墨深度","WV溢墨厚度","WV溢墨厚度","WV溢墨厚度","WV溢墨厚度","AV溢墨厚度","AV溢墨厚度",
//                "AV溢墨厚度","AV溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度","VR溢墨厚度"
//        };
        String[][] strings = excel.readExcelBlock(1, -1, 1, 49);
        String[] item = new String[]{"", "", "", "", "", "", "","","", "",
                "大孔划伤","大孔擦伤","大孔毛丝","大孔颗粒","大孔厚度不均","大孔缺墨","大孔锯齿","大孔平台溢墨","大孔漏印","ML划伤",
                "MW划伤","ML擦伤","MW擦伤","ML毛丝","MW毛丝","ML颗粒","MW颗粒","ML厚度不均","MW厚度不均","ML缺墨",
                "MV缺墨","ML锯齿","MW锯齿","ML平台溢墨","MW平台溢墨","ML漏印","MW漏印","ML溢墨宽度","MV溢墨宽度","ML油墨深度",
                "MV油墨深度","ML溢墨厚度","ML溢墨厚度","ML溢墨厚度","ML溢墨厚度","MW溢墨厚度","MW溢墨厚度","MW溢墨厚度","MW溢墨厚度"
        };
        Map<String, Integer> snResLeadId = new HashMap<>();

        for (int i = 3; i < strings.length; i++) {
            Date parse = sd.parse(strings[i][0]);
            Timestamp checkTime = new Timestamp(parse.getTime());
            String productType = strings[i][1];
            String[]parts = productType.split("_");
            String project = parts[0];
            String colour = parts[1];
            String virBarcode = strings[i][2];
            String qrCode = strings[i][3];
            String printMachine = strings[i][4];
            Integer hole = Integer.parseInt(strings[i][5]);
            String checkResult = strings[i][6];
            String bin = strings[i][7];
            String failItem = strings[i][8];
            Double ct = Double.valueOf(strings[i][9]);

            DfPrintAoiCheck data = new DfPrintAoiCheck();
            data.setCheckTime(checkTime);
            data.setProject(project);
            data.setColour(colour);
            data.setLineBody("Line-23");
            data.setVirBarcode(virBarcode);
            data.setQrCode(qrCode);
            data.setPrintMachine(printMachine);
            data.setHole(hole);
            data.setCheckResult(checkResult);
            data.setBin(bin);
            data.setFailItem(failItem);
            data.setCt(ct);

            if (snResLeadId.containsKey(virBarcode)) {
                data.setCheckType(2);
            } else {
                data.setCheckType(1);
            }
            dfPrintAoiCheckMapper.insert(data);
            Integer checkId = data.getId();

            if (snResLeadId.containsKey(virBarcode)) {
                Integer id = snResLeadId.get(virBarcode);
                dfPrintAoiCheckMapper.deleteById(id);

                QueryWrapper<DfPrintAoiCheckDetail> qw = new QueryWrapper<>();
                qw.eq("check_id", id);
                dfPrintAoiCheckDetailMapper.delete(qw);
            }

            snResLeadId.put(virBarcode, checkId);

//            for (int j = 10; j <= 45; j++) {
            for (int j = 10; j <= 36; j++) {
                String checkName = strings[2][j];
                Double checkValue = Double.valueOf(strings[i][j]);

                String checkResultDetail;
                if (checkValue==0) {
                    checkResultDetail = "OK";
                } else {
                    checkResultDetail = "NG";
                }
                DfPrintAoiCheckDetail itemInfos = new DfPrintAoiCheckDetail();
                itemInfos.setCheckId(checkId);
                itemInfos.setCheckTime(checkTime);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setCheckResult(checkResultDetail);
                itemInfos.setCheckName(checkName);
                itemInfos.setItemName(item[j]);
                itemInfos.setCheckType(1); // 外观
                dfPrintAoiCheckDetailMapper.insert(itemInfos);
            }

//            for (int j = 46; j <= 75; j++) {
//                String checkName = strings[2][j];
//                Double lsl = Double.valueOf(strings[0][j]);
//                Double usl = Double.valueOf(strings[1][j]);
//                Double checkValue = Double.valueOf(strings[i][j]);
//
//                String checkResultDetail;
//                if (checkValue>=lsl&&checkValue<=usl) {
//                    checkResultDetail = "OK";
//                } else {
//                    checkResultDetail = "NG";
//                }
//                DfPrintAoiCheckDetail itemInfos = new DfPrintAoiCheckDetail();
//                itemInfos.setCheckId(checkId);
//                itemInfos.setCheckTime(checkTime);
//                itemInfos.setCheckValue(checkValue);
//                itemInfos.setCheckResult(checkResultDetail);
//                itemInfos.setCheckName(checkName);
//                itemInfos.setItemName(item[j]);
//                itemInfos.setLsl(lsl);
//                itemInfos.setUsl(usl);
//                itemInfos.setCheckType(2); // 尺寸
//                dfPrintAoiCheckDetailMapper.insert(itemInfos);
//            }
//
//            for (int j = 76; j <= 93; j++) {
//                String checkName = strings[2][j];
//                Double checkValue = Double.valueOf(strings[i][j]);
//
//                String checkResultDetail;
//                if (checkValue==0) {
//                    checkResultDetail = "OK";
//                } else {
//                    checkResultDetail = "NG";
//                }
//                DfPrintAoiCheckDetail itemInfos = new DfPrintAoiCheckDetail();
//                itemInfos.setCheckId(checkId);
//                itemInfos.setCheckTime(checkTime);
//                itemInfos.setCheckValue(checkValue);
//                itemInfos.setCheckResult(checkResultDetail);
//                itemInfos.setCheckName(checkName);
//                itemInfos.setItemName(item[j]);
//                itemInfos.setCheckType(1); // 外观
//                dfPrintAoiCheckDetailMapper.insert(itemInfos);
//            }

//            for (int j = 94; j <= 119; j++) {
            for (int j = 37; j <= 48; j++) {
                String checkName = strings[2][j];
                Double lsl = Double.valueOf(strings[0][j]);
                Double usl = Double.valueOf(strings[1][j]);
                Double checkValue = Double.valueOf(strings[i][j]);

                String checkResultDetail;
                if (checkValue>=lsl&&checkValue<=usl) {
                    checkResultDetail = "OK";
                } else {
                    checkResultDetail = "NG";
                }
                DfPrintAoiCheckDetail itemInfos = new DfPrintAoiCheckDetail();
                itemInfos.setCheckId(checkId);
                itemInfos.setCheckTime(checkTime);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setCheckResult(checkResultDetail);
                itemInfos.setCheckName(checkName);
                itemInfos.setItemName(item[j]);
                itemInfos.setLsl(lsl);
                itemInfos.setUsl(usl);
                itemInfos.setCheckType(2); // 尺寸
                dfPrintAoiCheckDetailMapper.insert(itemInfos);
            }
        }
        return strings.length;
    }


    @Override
    public List<DfPrintAoiCheck> getPassPointList(Wrapper<DfPrintAoiCheck> wrapper, Wrapper<DfPrintAoiCheck> wrapper2) {
        return dfPrintAoiCheckDetailMapper.getPassPointList(wrapper,wrapper2);
    }

    @Override
    public List<Rate3> getHolePossPoint(Wrapper<DfPrintAoiCheck> wrapper) {
        return dfPrintAoiCheckDetailMapper.getHolePossPoint(wrapper);
    }

    @Override
    public List<Rate3> getHoleDefectPointTop3(Wrapper<DfPrintAoiCheck> wrapper, Wrapper<DfPrintAoiCheckDetail> wrapper2) {
        return dfPrintAoiCheckDetailMapper.getHoleDefectPointTop3(wrapper,wrapper2);
    }

    @Override
    public List<Rate3> getAllHoleDefectPointTop3(Wrapper<DfPrintAoiCheck> wrapper) {
        return dfPrintAoiCheckDetailMapper.getAllHoleDefectPointTop3(wrapper);
    }

    @Override
    public List<Rate3> getNgPointList(Wrapper<DfPrintAoiCheck> wrapper, Wrapper<DfPrintAoiCheckDetail> wrapper2) {
        return dfPrintAoiCheckDetailMapper.getNgPointList(wrapper,wrapper2);
    }

    @Override
    public List<Rate3> getOnePassPointList(Wrapper<DfPrintAoiCheck> wrapper, Wrapper<DfPrintAoiCheckDetail> wrapper2) {
        return dfPrintAoiCheckDetailMapper.getOnePassPointList(wrapper,wrapper2);
    }

    @Override
    public List<String> getAllHoleList() {
        return dfPrintAoiCheckDetailMapper.getAllHoleList();
    }

    @Override
    public List<DfPrintAoiCheckDetail> listItemInfosJoinDetail(Wrapper<DfPrintAoiCheckDetail> wrapper) {
        return dfPrintAoiCheckDetailMapper.listItemInfosJoinDetail(wrapper);
    }

    @Override
    public Rate3 getMinLslAndMaxUsl(Wrapper<DfPrintAoiCheckDetail> wrapper) {
        return dfPrintAoiCheckDetailMapper.getMinLslAndMaxUsl(wrapper);
    }


}
