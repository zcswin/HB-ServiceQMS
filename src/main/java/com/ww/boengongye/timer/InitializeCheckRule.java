package com.ww.boengongye.timer;

import com.ww.boengongye.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class InitializeCheckRule {


    @Autowired
    private com.ww.boengongye.service.DfControlStandardService DfControlStandardService;
    @Autowired
    private com.ww.boengongye.service.DfMacStatusSizeService DfMacStatusSizeService;
    @Autowired
    private com.ww.boengongye.service.DfMacStatusService DfMacStatusService;

    @Autowired
    private com.ww.boengongye.service.DfStatusCodeService DfStatusCodeService;


    public static Map<String,List<DfControlStandard>> rules=new HashMap<>();
    public static Map<String,String> sizeNgType=new HashMap<>();//尺寸NG项

    public static Map<String,DfSizeContStand> sizeContStand=new HashMap<>();//尺寸检测项项
    public static Map<String,DfMacStatusSize> sizeStatus=new HashMap<>();//尺寸状态数据
    public static Map<String,DfMacStatus> macStatus=new HashMap<>();//设备状态数据
    public static Map<Integer,String> statusName=new HashMap<>();//状态名称数据



    @Async
    @Scheduled(initialDelay = 10,fixedDelay = 1000000000)
    public void initializeRule(){
        System.out.println("初始化管控标准和NG尺寸项,AOI定位数据");

//        sizeNgType=new HashMap<>();
//        QueryWrapper<DfSizeNgType>sqw=new QueryWrapper<>();
//        sqw.eq("is_use",0);
//        List<DfSizeNgType> sizeNg=DfSizeNgTypeService.list(sqw);
//        if(sizeNg.size()>0){
//            for(DfSizeNgType d:sizeNg){
//                sizeNgType.put(d.getProcess()+d.getName(),"");
//                System.out.println(d.getProcess());
//            }
//        }
        List<DfStatusCode> statusCode=DfStatusCodeService.list();
        if(statusCode.size()>0){
            for(DfStatusCode s:statusCode){
                statusName.put(s.getStatusCode(),s.getName());
            }
        }
//
        List<DfMacStatusSize>sizeSta=DfMacStatusSizeService.list();
        if(sizeSta.size()>0){
            for(DfMacStatusSize s:sizeSta){
//                System.out.println(s.getMachineCode());
                sizeStatus.put(s.getMachineCode(),s);
            }
        }

        List<DfMacStatus>macSta=DfMacStatusService.list();
        if(macSta.size()>0){
            for(DfMacStatus s:macSta){
                System.out.println(s.getMachineCode());
                macStatus.put(s.getMachineCode(),s);
            }
        }


//
//        sizeContStand=new HashMap<>();
//        QueryWrapper<DfSizeContStand>scqw=new QueryWrapper<>();
//        scqw.eq("is_use",1);
//        List<DfSizeContStand> sizeCont=DfSizeContStandService.list(scqw);
//        if(sizeCont.size()>0){
//            for(DfSizeContStand d:sizeCont){
//                sizeContStand.put(d.getProcess()+d.getTestItem(),d);
//                System.out.println(d.getProcess());
//            }
//        }


        //初始化管控标准
        rules=new HashMap<>();
        List<DfControlStandard> rs=DfControlStandardService.list();
        if(rs.size()>0){
            StringBuffer key=new StringBuffer();
            for(DfControlStandard d:rs){
                if(null!=d.getMin()&&null!=d.getMax()){
                    key.setLength(0);
                    key.append(d.getFactoryCode());
                    key.append(d.getProcessName());
                    key.append(d.getControlName());
                    key.append(d.getClassification());
                    key.append(d.getDetail());
                    if(rules.containsKey(key.toString())){
                        rules.get(key.toString()).add(d);
                    }else{
                        List<DfControlStandard> datas=new ArrayList<>();
                        datas.add(d);
                        rules.put(key.toString(),datas);
                    }
                }

            }
        }





//        System.out.println("通过Map.keySet遍历key和value：");
//        for (String keys: InitializeCheckRule.rules.keySet()) {
//            System.out.println("key= "+ keys + " and value= " + InitializeCheckRule.rules.get(keys));
//        }

    }


}
