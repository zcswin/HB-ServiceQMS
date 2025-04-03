package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfPrintAoiRecheck;
import com.ww.boengongye.entity.DfPrintAoiRecheckDetail;
import com.ww.boengongye.mapper.DfPrintAoiRecheckDetailMapper;
import com.ww.boengongye.mapper.DfPrintAoiRecheckMapper;
import com.ww.boengongye.service.DfPrintAoiRecheckDetailService;
import com.ww.boengongye.service.DfPrintAoiRecheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 移印AOI人工复查 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Service
public class DfPrintAoiRecheckServiceImpl extends ServiceImpl<DfPrintAoiRecheckMapper, DfPrintAoiRecheck> implements DfPrintAoiRecheckService {

    @Autowired
    private DfPrintAoiRecheckMapper dfPrintAoiRecheckMapper;

    @Autowired
    private DfPrintAoiRecheckDetailMapper dfPrintAoiRecheckDetailMapper;

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);

        //所有数据
        String[][] strings = excel.readExcelBlock(1, -1, 1, 22,1);

        //缺陷名称
        String[] defectNameList = new String[]{
                "","","","","","","","","","",
                "孔缺墨","油墨不均","孔擦伤/脏污","毛丝","颗粒","孔划伤","油墨锯齿","凸台面溢墨","漏印"
        };

        for (int i = 4;i<strings.length;i++){
            String factory = strings[i][0];
            String project = strings[i][1];
            String lineBody = strings[i][2];
            String colour = strings[i][3];
            Date parse = sd.parse(strings[i][4]);
            Timestamp checkTime = new Timestamp(parse.getTime());
            String type1 = strings[i][5];
            String type2 = strings[i][6];
            Integer inputNumber = 0;
            if (strings[i][7]!=null){
                inputNumber = (int)Double.parseDouble(strings[i][7]);
            }
            Integer recheckOkNumber = 0;
            if (strings[i][8]!=null){
                recheckOkNumber = (int)Double.parseDouble(strings[i][8]);
            }
            Integer recheckNgNumber = 0;
            if (strings[i][8]!=null){
                recheckNgNumber = (int)Double.parseDouble(strings[i][9]);
            }
            String overkillOrEscape = "0.00";
            if (strings[i][19]!=null){
                overkillOrEscape =strings[i][19];
            }
            String onePassPoint = "0.00";
            if (strings[i][20]!=null){
                onePassPoint =strings[i][20];
            }
            String finalPassPoint = "0.00";
            if (strings[i][21]!=null){
                finalPassPoint =strings[i][21];
            }

            DfPrintAoiRecheck dfPrintAoiRecheck = new DfPrintAoiRecheck();
            dfPrintAoiRecheck.setFactory(factory);
            dfPrintAoiRecheck.setProject(project);
            dfPrintAoiRecheck.setLineBody(lineBody);
            dfPrintAoiRecheck.setColour(colour);
            dfPrintAoiRecheck.setCheckTime(checkTime);
            dfPrintAoiRecheck.setType1(type1);
            dfPrintAoiRecheck.setType2(type2);
            dfPrintAoiRecheck.setInputNumber(inputNumber);
            dfPrintAoiRecheck.setRecheckOkNumber(recheckOkNumber);
            dfPrintAoiRecheck.setRecheckNgNumber(recheckNgNumber);
            dfPrintAoiRecheck.setOverkillOrEscape(overkillOrEscape);
            dfPrintAoiRecheck.setOnePassPoint(onePassPoint);
            dfPrintAoiRecheck.setFinalPassPoint(finalPassPoint);

            dfPrintAoiRecheckMapper.insert(dfPrintAoiRecheck);

            if (strings[i][7]==null||strings[i][7]=="0"){
                continue;
            }
            for (int j=10;j<=18;j++){
                if (strings[i][j]!=null&&!"0".equals(strings[i][j])&&!"0.0".equals(strings[i][j])){
                    Integer checkId = dfPrintAoiRecheck.getId();
                    String defectName = defectNameList[j];
                    Integer defectNumber = (int)Double.parseDouble(strings[i][j]);
                    DfPrintAoiRecheckDetail dfPrintAoiRecheckDetail = new DfPrintAoiRecheckDetail();
                    dfPrintAoiRecheckDetail.setCheckId(checkId);
                    dfPrintAoiRecheckDetail.setDefectName(defectName);
                    dfPrintAoiRecheckDetail.setDefectNumber(defectNumber);
                    dfPrintAoiRecheckDetail.setCheckTime(checkTime);
                    dfPrintAoiRecheckDetailMapper.insert(dfPrintAoiRecheckDetail);
                }
            }
        }
        return strings.length;
    }

    @Override
    public List<DfPrintAoiRecheck> getOverkillOrEscapeList(Wrapper<DfPrintAoiRecheck> wrapper) {
        return dfPrintAoiRecheckMapper.getOverkillOrEscapeList(wrapper);
    }

    @Override
    public List<DfPrintAoiRecheckDetail> getEscapeDetailList(Wrapper<DfPrintAoiRecheckDetail> wrapper) {
        return dfPrintAoiRecheckMapper.getEscapeDetailList(wrapper);
    }
}
