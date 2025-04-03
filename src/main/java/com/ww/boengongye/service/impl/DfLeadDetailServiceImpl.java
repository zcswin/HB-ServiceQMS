package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.mapper.DfLeadCheckItemInfosMapper;
import com.ww.boengongye.mapper.DfLeadDetailMapper;
import com.ww.boengongye.service.DfLeadCheckItemInfosService;
import com.ww.boengongye.service.DfLeadDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Service
public class DfLeadDetailServiceImpl extends ServiceImpl<DfLeadDetailMapper, DfLeadDetail> implements DfLeadDetailService {

    @Autowired
    private DfLeadDetailMapper dfLeadDetailMapper;

    @Autowired
    private DfLeadCheckItemInfosMapper dfLeadCheckItemInfosMapper;

    @Autowired
    private DfLeadCheckItemInfosService dfLeadCheckItemInfosService;

    @Override
    public int importExcel(MultipartFile file) throws Exception {
//        String color = file.getOriginalFilename().split("\\.")[0].split("_")[1];
        String color = "BLUE";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hhmmss");
        ExcelImportUtil excel = new ExcelImportUtil(file);
        String[][] strings = excel.readExcelBlock(1, -1, 1, 77);
        String[] item = new String[]{"", "", "", "", "", "", ""," "," ", "",
                "S孔底倒角X_1","S孔底倒角X_2","S孔底倒角X_3","S孔底倒角X_4","MIC孔底倒角X_1","MIC孔底倒角X_2","MIC孔底倒角X_3",
                "MIC孔底倒角X_4","ML孔底倒角X_1","ML孔底倒角X_2","ML孔底倒角X_3","ML孔底倒角X_4","MW孔底倒角X_5","MW孔底倒角X_6",
                "MW孔底倒角X_7","MW孔底倒角X_8","ML孔真圆度","MW孔真圆度","MIC孔真圆度","S孔真圆度","ML孔直径","MW孔直径",
                "MIC孔直径","S孔直径","ML孔位置度","MW孔位置度","MIC孔位置度","S孔位置度","孔ML中心到产品中心X","孔ML中心到产品中心Y",
                "孔MW中心到产品中心X","孔MW中心到产品中心Y","孔MIC中心到产品中心X","孔MIC中心到产品中心Y","孔S中心到产品中心X",
                "孔S中心到产品中心Y","外形长_1","外形长_2","外形长_3","外形宽_1","外形宽_2","外形宽_3","外形底倒角_P35",
                "外形底倒角_P21","外形底倒角_P01","外形底倒角_P19","外形底倒角_P03","外形底倒角_P17","外形底倒角_P31",
                "外形底倒角_P07","外形底倒角_P28","外形底倒角_P10","外形底倒角_P25","外形底倒角_P13","孔崩_S","孔崩_ML","孔崩_MW",
                "孔崩_MIC","孔划_S","孔砂轮线_S","孔未亮_S","孔划_MW","孔砂轮线_MW","孔未亮_MW","孔划_ML","孔砂轮线_ML","孔未亮_ML"};
        Map<String, Integer> snResLeadId = new HashMap<>();
        for (int i = 8; i < strings.length; i++) {
            Date parse = sd.parse(strings[i][0]);
            Timestamp time = new Timestamp(parse.getTime());
            String workPosition = strings[i][1] + strings[i][6];
            String sn = strings[i][4];
            Integer bin = Integer.valueOf(strings[i][7]);
            String result = strings[i][9];

            DfLeadDetail data = new DfLeadDetail();
            data.setCheckTime(time);
            data.setBin(bin);
            data.setWorkPosition(workPosition);
            data.setSn(sn);
            data.setResult(result);
            data.setMachineCode("1#");
            if (snResLeadId.containsKey(sn)) {
                data.setCheckType(2);
            } else {
                data.setCheckType(1);
            }
            data.setColor(color);
            dfLeadDetailMapper.insert(data);
            Integer leadId = data.getId();
            if (snResLeadId.containsKey(sn)) {
                Integer id = snResLeadId.get(sn);
                dfLeadDetailMapper.deleteById(id);

                QueryWrapper<DfLeadCheckItemInfos> qw = new QueryWrapper<>();
                qw.eq("check_id", id);
                dfLeadCheckItemInfosMapper.delete(qw);
            }
            snResLeadId.put(sn, leadId);

            List<DfLeadCheckItemInfos> itemList = new ArrayList<>();


            for (int j = 10; j <= 25; j++) {
                Double usl = Double.valueOf(strings[3][j]);

                Double normal = Double.valueOf(strings[4][j]);
                Double lsl = Double.valueOf(strings[5][j]);
                Double checkValue = Double.valueOf(strings[i][j]);
                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfLeadCheckItemInfos itemInfos = new DfLeadCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(time);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setItemName(item[j]);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(normal);
                itemInfos.setUsl(usl);
                itemInfos.setCheckType(2); // 倒角
                itemInfos.setCheckId(leadId);
                //dfLeadCheckItemInfosMapper.insert(itemInfos);
                itemList.add(itemInfos);
            }

            for (int j = 52; j <= 63; j++) {
                Double usl = Double.valueOf(strings[3][j]);
                Double normal = Double.valueOf(strings[4][j]);
                Double lsl = Double.valueOf(strings[5][j]);
                Double checkValue = Double.valueOf(strings[i][j]);
                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfLeadCheckItemInfos itemInfos = new DfLeadCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(time);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setItemName(item[j]);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(normal);
                itemInfos.setUsl(usl);
                itemInfos.setCheckType(2); // 倒角
                itemInfos.setCheckId(leadId);
                //dfLeadCheckItemInfosMapper.insert(itemInfos);
                itemList.add(itemInfos);
            }

            for (int j = 26; j <= 51; j++) {
                Double usl = Double.valueOf(strings[3][j]);
                Double normal = Double.valueOf(strings[4][j]);
                Double lsl = Double.valueOf(strings[5][j]);
                Double checkValue = Double.valueOf(strings[i][j]);
                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfLeadCheckItemInfos itemInfos = new DfLeadCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(time);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setItemName(item[j]);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(normal);
                itemInfos.setUsl(usl);
                itemInfos.setCheckType(1); // 尺寸
                itemInfos.setCheckId(leadId);
                //dfLeadCheckItemInfosMapper.insert(itemInfos);
                itemList.add(itemInfos);
            }

            for (int j = 64; j <= 76; j++) {
                Double usl = Double.valueOf(strings[3][j]);
                Double normal = Double.valueOf(strings[4][j]);
                Double lsl = Double.valueOf(strings[5][j]);
                Double checkValue = Double.valueOf(strings[i][j]);
                String checkResult;
                if (checkValue > usl || checkValue < lsl) {
                    checkResult = "NG";
                } else {
                    checkResult = "OK";
                }
                DfLeadCheckItemInfos itemInfos = new DfLeadCheckItemInfos();
                itemInfos.setCheckResult(checkResult);
                itemInfos.setCheckTime(time);
                itemInfos.setCheckValue(checkValue);
                itemInfos.setItemName(item[j]);
                itemInfos.setLsl(lsl);
                itemInfos.setStandardValue(normal);
                itemInfos.setUsl(usl);
                itemInfos.setCheckType(3); // 外观
                itemInfos.setCheckId(leadId);
                //dfLeadCheckItemInfosMapper.insert(itemInfos);
                itemList.add(itemInfos);
            }
            dfLeadCheckItemInfosService.saveBatch(itemList);

        }
        /*for (Map.Entry<String"," DfLeadDetail> entry : snResLead.entrySet()) {
            dfLeadDetailMapper.insert(entry.getValue());
        }*/

        return strings.length;
    }

    @Override
    public Rate4 listLeadNum(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listLeadNum(wrapper);
    }

    @Override
    public List<Rate3> listOKRate(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listOKRate(wrapper);
    }

    @Override
    public List<Rate3> listAllOkRateTop(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listAllOkRateTop(wrapper);
    }

    @Override
    public List<Rate3> listWorkPositionOKRate(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listWorkPositionOKRate(wrapper);
    }

    @Override
    public List<Rate3> listOkRateGroupByDate(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listOkRateGroupByDate(wrapper);
    }

    @Override
    public List<Rate3> listAllNumGroupByDate(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listAllNumGroupByDate(wrapper);
    }

    @Override
    public List<Rate3> listItemNgRateTop(Wrapper<DfLeadDetail> wrapper,String selectNameString) {
        return dfLeadDetailMapper.listItemNgRateTop(wrapper,selectNameString);
    }

    @Override
    public List<Rate3> listMachineOneAndMutilOkRate(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listMachineOneAndMutilOkRate(wrapper);
    }

    @Override
    public List<DfLeadCheckItemInfos> listItemInfosJoinDetail(Wrapper<DfLeadDetail> wrapper) {
        return dfLeadDetailMapper.listItemInfosJoinDetail(wrapper);
    }

    @Override
    public List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfLeadDetail> qw) {
        return dfLeadDetailMapper.listDateOneAndMutilOkRate(qw);
    }
}
