package com.ww.boengongye.timer;

import com.ww.boengongye.entity.DfDataSet;
import com.ww.boengongye.service.DfDataSetService;
import com.ww.boengongye.service.DfMacRevService;
import com.ww.boengongye.service.DfMacStatusDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import smartbi.catalogtree.ICatalogElement;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.service.catalog.CatalogService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class SynBIDataSet {
    @Value("${bi.url}")
    private String url;

    @Value("${bi.account}")
    private String account;

    @Value("${bi.password}")
    private String password;

    @Autowired
    private DfDataSetService dfDataSetService;


//    @Async
//    @Scheduled(initialDelay = 10000,fixedDelay = 500000)
//    @Scheduled(initialDelay = 10000000,fixedDelay = 500000)
    public void autoGenInverDatas(){
        System.out.println("定時任務執行111");
        ClientConnector conn = new ClientConnector(url);
        conn.open(account, password); // 以管理员身份登录

        List<DfDataSet>saveDatas=new ArrayList<>();

        CatalogService catalogService = new CatalogService(conn);
//        System.out.println(catalogService.getRootElements());
//        System.out.println(catalogService.getChildElements("PUBLIC_ANALYSIS"));
//        System.out.println(catalogService.getChildElements("I2c94d8ab0182666c666c01150182680bfe642882"));
//        System.out.println(catalogService.getCatalogElementById("532d016a639973e908f591362b8b897e"));


        List<? extends ICatalogElement> datas= catalogService.getChildElements("PUBLIC_ANALYSIS");
        for( ICatalogElement d:datas){
            System.out.println(d.getAlias());
            System.out.println(d.getId());
            System.out.println(d.getCustomImage());
            DfDataSet df=new DfDataSet();
            df.setAlias(d.getAlias());
            df.setDataDesc(d.getDesc());
            df.setDataType(d.getType());
            df.setName(d.getName());
            df.setDataDesc(d.getDesc());
            df.setDataOrder(d.getOrder());
            df.setId(d.getId());
            if(d.isDetectChild()){
                df.setDetectChild(1);
            }else{
                df.setDetectChild(0);
            }
            if(d.isHasChild()){
                df.setHasChild(1);
            }else{
                df.setHasChild(0);
            }
            if(d.isHiddenInBrowse()){
                df.setHiddenInBrowse(1);
            }else{
                df.setHiddenInBrowse(0);
            }
            if(d.isShowOnPad()){
                df.setShowOnPad(1);
            }else{
                df.setShowOnPad(0);
            }
            if(d.isShowOnPC()){
                df.setShowOnPc(1);
            }else{
                df.setShowOnPc(0);
            }
            if(d.isShowOnPhone()){
                df.setShowOnPhone(1);
            }else{
                df.setShowOnPhone(0);
            }
            saveDatas.add(df);

            if(d.isHasChild()){
                System.out.println(d.getId());
                saveDatas.addAll(listSon(d.getId(),catalogService))   ;
            }

        }


        dfDataSetService.saveOrUpdateBatch(saveDatas);


        conn.close();
    }

    public List<DfDataSet> listSon(String id, CatalogService catalogService){
        List<DfDataSet> saveDatas=new ArrayList<>();
        List<? extends ICatalogElement> datas= catalogService.getChildElements(id);
        for( ICatalogElement d:datas){

            System.out.println(d.getAlias());
            System.out.println(d.getId());
            System.out.println(d.getCustomImage());
            DfDataSet df=new DfDataSet();
            df.setParentId(id);
            df.setAlias(d.getAlias());
            df.setDataDesc(d.getDesc());
            df.setDataType(d.getType());
            df.setName(d.getName());
            df.setDataDesc(d.getDesc());
            df.setDataOrder(d.getOrder());
            df.setId(d.getId());
            if(d.isDetectChild()){
                df.setDetectChild(1);
            }else{
                df.setDetectChild(0);
            }
            if(d.isHasChild()){
                df.setHasChild(1);
            }else{
                df.setHasChild(0);
            }
            if(d.isHiddenInBrowse()){
                df.setHiddenInBrowse(1);
            }else{
                df.setHiddenInBrowse(0);
            }
            if(d.isShowOnPad()){
                df.setShowOnPad(1);
            }else{
                df.setShowOnPad(0);
            }
            if(d.isShowOnPC()){
                df.setShowOnPc(1);
            }else{
                df.setShowOnPc(0);
            }
            if(d.isShowOnPhone()){
                df.setShowOnPhone(1);
            }else{
                df.setShowOnPhone(0);
            }
            saveDatas.add(df);

            if (d.isHasChild()){
                saveDatas.addAll(listSon(d.getId(),catalogService))   ;
            }
        }

        return  saveDatas;
    }
}
