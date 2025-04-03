package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfTzCheckItemInfosMapper;
import com.ww.boengongye.mapper.DfTzDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * TZ测量 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
@Service
public class DfTzDetailServiceImpl extends ServiceImpl<DfTzDetailMapper, DfTzDetail> implements DfTzDetailService {

    @Autowired
    private DfTzDetailMapper dfTzDetailMapper;

    @Autowired
    private DfTzCheckItemInfosMapper dfTzCheckItemInfosMapper;

    @Autowired
    DfTzCheckItemInfosService dfTzCheckItemInfosService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] strings = excel.readExcelBlock(1, -1, 1, 197);
        String[] item = new String[]{"", "", "", "", "", "", "","","", "",
                "","", "Logo_L_SCI","Logo_A_SCI","Logo_B_SCI","Body_L_SCI","Body_A_SCI","Body_B_SCI","Plateau_L_SCI","Plateau_A_SCI",
                "Plateau_B_SCI","Logo_DL_SCI", "Logo_DA_SCI","Logo_DB_SCI","Logo_DE_SCI","Body_DL_SCI","Body_DA_SCI","Body_DB_SCI","Body_DE_SCI","Plateau_DL_SCI",
                "Plateau_DA_SCI","Plateau_DB_SCI","Plateau_DE_SCI","MLogo_L_SCI","MLogo_A_SCI","MLogo_B_SCI","MBody_L_SCI","MBody_A_SCI","MBody_B_SCI","MPlateau_L_SCI",
                "MPlateau_A_SCI","MPlateau_B_SCI","Logo_L_SCE","Logo_A_SCE","Logo_B_SCE","Body_L_SCE","Body_A_SCE","Body_B_SCE","Plateau_L_SCE","Plateau_A_SCE",
                "Plateau_B_SCE","Logo_DL_SCE","Logo_DA_SCE","Logo_DB_SCE","Logo_DE_SCE","Body_DL_SCE","Body_DA_SCE","Body_DB_SCE","Body_DE_SCE","Plateau_DL_SCE",
                "Plateau_DA_SCE","Plateau_DB_SCE","Plateau_DE_SCE","MLogo_L_SCE","MLogo_A_SCE","MLogo_B_SCE","MBody_L_SCE","MBody_A_SCE","MBody_B_SCE","MPlateau_L_SCE",
                "MPlateau_A_SCE","MPlateau_B_SCE","光泽度","M光泽度","开口\"MIC\"直径","开口\"S\"定位","开口\"S\"直径","开口\"MW\"定位","开口\"MW\"直径","外形长",
                "外形长","外形长","外形长","外形长","外形宽","外形宽","外形宽","外形宽","外形宽","开口\"MIC\"定位",
                "开口\"ML\"定位","开口\"ML\"直径","产品外形轮廓度","凸台宽","凸台左边到玻璃左边-X","凸台顶边到玻璃顶边-Y","凸台长","平台线轮廓度-右上","平台线轮廓度-右下","平台线轮廓度-左下",
                "平台线轮廓度-左上","ML孔真圆度","MIC孔真圆度","外形轮廓度-右上角","外形轮廓度-右下角","外形轮廓度-左下角","外形轮廓度-左上角","MW孔真圆度","S孔真圆度","logo左到基准B距离",
                "logo右到基准B距离","logo底到基准C距离","logo轮廓度","logo顶到基准C距离","产品外形轮廓度","产品外形轮廓度","平台线轮廓度-右上","平台线轮廓度-右上","平台线轮廓度-右下","平台线轮廓度-右下",
                "平台线轮廓度-左下","平台线轮廓度-左下","平台线轮廓度-左上","平台线轮廓度-左上","外形轮廓度-右上角","外形轮廓度-右上角","外形轮廓度-右下角","外形轮廓度-右下角","外形轮廓度-左下角","外形轮廓度-左下角",
                "外形轮廓度-左上角","外形轮廓度-左上角","logo轮廓度","logo轮廓度","闪光孔溢墨","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度",
                "白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","白片+油墨厚度","边缘积油厚度",
                "边缘积油厚度","边缘积油厚度","边缘积油厚度","边缘积油厚度","边缘积油厚度","边缘积油厚度","边缘积油厚度","边缘积油厚度","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)",
                "玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","白片+油墨+平台厚度",
                "白片+油墨+平台厚度","S孔积油位置凸台总厚","S 孔积油厚度","ML孔积油厚度","ML孔积油厚度","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)",
                "玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)",
                "玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)","玻璃弧面轮廓度（DBC)"
               };
        Map<String, Integer> snResLeadId = new HashMap<>();

        for (int i = 8; i < strings.length; i++) {
            if (strings[i][0]==null||strings[i][1]==null){
                continue;
            }

            Date parse = sd.parse(strings[i][0]);
            Timestamp checkTime = new Timestamp(parse.getTime());
            String barcode = strings[i][1];
            String batchBarcode = strings[i][2];
            String result = strings[i][3];
            String channel = strings[i][4];
            String classes = strings[i][5];
            String stationClass = strings[i][6];
            String lineBody = strings[i][7];
            String rowId = strings[i][8];
            String pos = strings[i][9];
            String gb = strings[i][10];
            String color = strings[i][11];

            DfTzDetail data = new DfTzDetail();
            data.setCheckTime(checkTime);
            data.setBarcode(barcode);
            data.setBatchBarcode(batchBarcode);
            data.setResult(result);
            data.setChannel(channel);
            data.setClasses(classes);
            data.setStationClass(stationClass);
            data.setLineBody(lineBody);
            data.setRowId(rowId);
            data.setPos(Integer.valueOf(pos));
            data.setGb(gb);
            data.setColor(color);
            data.setProject("C27");
            data.setMachineCode("19#");

//            if (snResLeadId.containsKey(barcode)) {
//                data.setCheckType(2);
//            } else {
//                data.setCheckType(1);
//            }
//            dfTzDetailMapper.insert(data);
//            Integer tzId = data.getId();
//
//            if (snResLeadId.containsKey(barcode)) {
//                Integer id = snResLeadId.get(barcode);
//                dfTzDetailMapper.deleteById(id);
//
//                QueryWrapper<DfTzCheckItemInfos> qw = new QueryWrapper<>();
//                qw.eq("check_id", id);
//                dfTzCheckItemInfosMapper.delete(qw);
//            }

            if (snResLeadId.containsKey(barcode)) {
                Integer id = snResLeadId.get(barcode);
                dfTzDetailMapper.deleteById(id);

                QueryWrapper<DfTzCheckItemInfos> qw = new QueryWrapper<>();
                qw.eq("check_id", id);
                dfTzCheckItemInfosMapper.delete(qw);

                data.setCheckType(2);
            } else {
                data.setCheckType(1);
            }
            dfTzDetailMapper.insert(data);
            Integer tzId = data.getId();

            snResLeadId.put(barcode, tzId);

            List<DfTzCheckItemInfos> list = new ArrayList<>();

            for (int j = 12; j <= 73; j++) {
                String checkName = strings[0][j];
                String describes = strings[1][j];
                Double standardValue =Double.valueOf(strings[2][j]);
                Double upperTolerance =Double.valueOf(strings[3][j]);
                Double lowerTolerance =Double.valueOf(strings[4][j]);
                Double usl = Double.valueOf(strings[5][j]);
                Double lsl = Double.valueOf(strings[6][j]);
                Double checkValue = Double.valueOf(strings[i][j]);

                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfTzCheckItemInfos itemInfos = new DfTzCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(checkTime);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setCheckName(checkName);
                itemInfos.setItemName(item[j]);
                itemInfos.setDescribes(describes);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(standardValue);
                itemInfos.setUsl(usl);
                itemInfos.setCheckId(tzId);
                itemInfos.setCheckType(1); // 外观
                itemInfos.setUpperTolerance(upperTolerance);
                itemInfos.setLowerTolerance(lowerTolerance);
                list.add(itemInfos);
//                dfTzCheckItemInfosMapper.insert(itemInfos);
            }

            for (int j = 74; j <= 196; j++) {
                String checkName = strings[0][j];
                String describes = strings[1][j];
                Double standardValue =Double.valueOf(strings[2][j]);
                Double upperTolerance =Double.valueOf(strings[3][j]);
                Double lowerTolerance =Double.valueOf(strings[4][j]);
                Double usl = Double.valueOf(strings[5][j]);
                Double lsl = Double.valueOf(strings[6][j]);
                Double checkValue = Double.valueOf(strings[i][j]);

                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfTzCheckItemInfos itemInfos = new DfTzCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(checkTime);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setCheckName(checkName);
                itemInfos.setItemName(item[j]);
                itemInfos.setDescribes(describes);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(standardValue);
                itemInfos.setUsl(usl);
                itemInfos.setCheckId(tzId);
                itemInfos.setCheckType(2); // 尺寸
                itemInfos.setUpperTolerance(upperTolerance);
                itemInfos.setLowerTolerance(lowerTolerance);
                list.add(itemInfos);
//                dfTzCheckItemInfosMapper.insert(itemInfos);
            }
            dfTzCheckItemInfosService.saveBatch(list);

        }
        return strings.length;
    }

    @Override
    public Rate4 listTzNum(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listTzNum(wrapper);
    }

    @Override
    public List<Rate3> listAllNumGroupByDate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listAllNumGroupByDate(wrapper);
    }


    @Override
    public List<Rate3> listMachineOneAndMutilOkRate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listMachineOneAndMutilOkRate(wrapper);
    }

    @Override
    public List<Rate3> listDateOneAndMutilOkRate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listDateOneAndMutilOkRate(wrapper);
    }


    @Override
    public List<Rate3> listOKRate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listOKRate(wrapper);
    }

    @Override
    public List<Rate3> listOkRateGroupByDate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listOkRateGroupByDate(wrapper);
    }


    @Override
    public List<Rate3> listItemNgRateTop(Wrapper<DfTzDetail> wrapper,String selectNameString) {
        return dfTzDetailMapper.listItemNgRateTop(wrapper,selectNameString);
    }

    @Override
    public List<Rate3> listAllOkRateTop(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listAllOkRateTop(wrapper);
    }


    @Override
    public List<Rate3> listWorkPositionOKRate(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listWorkPositionOKRate(wrapper);
    }

    @Override
    public List<DfTzCheckItemInfos> listItemInfosJoinDetail(Wrapper<DfTzDetail> wrapper) {
        return dfTzDetailMapper.listItemInfosJoinDetail(wrapper);
    }

//    @Override
//    public List<Rate3> getNgRate(Wrapper<DfTzDetail> wrapper) {
//        return dfTzDetailMapper.getNgRate(wrapper);
//    }
//
//    @Override
//    public List<Rate3> getNgDetailRateTop10(Wrapper<DfTzDetail> wrapper) {
//        return dfTzDetailMapper.getNgDetailRateTop10(wrapper);
//    }
}
