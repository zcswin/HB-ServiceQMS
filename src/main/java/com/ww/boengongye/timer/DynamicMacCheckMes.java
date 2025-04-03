package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMachine;
import com.ww.boengongye.entity.DynamicIpqcMac;
import com.ww.boengongye.entity.SizeQueueData;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfMachineService;
import com.ww.boengongye.utils.DynamicIpqcUtil;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.ServerInitializeUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling // 1.开启定时任务
public class DynamicMacCheckMes {
    @Autowired
    DfMacStatusService dfMacStatusService;

    @Autowired
    DfMachineService dfMachineService;
    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    Environment env;

    @Scheduled(initialDelay = 60000,fixedDelay = 120000)
//    @Scheduled(initialDelay = 10000,fixedDelay = 20000)
    public void saveMacStatus() throws ParseException {

        if(env.getProperty("SendDynamicMacCheckMes","N").equals("Y")){
           if(null!=env.getProperty("IPQCProcess")&&!env.getProperty("IPQCProcess","").equals("")) {
            String[] processList=env.getProperty("IPQCProcess","").split(",");

            Map<String,String>macMap=new HashMap<>();

            for(String p:processList){
                QueryWrapper<DfMachine>qw=new QueryWrapper<>();
                qw.eq("process_code",p);
                List<DfMachine>ml=dfMachineService.list(qw);
                if(ml.size()>0){
                    for(DfMachine m:ml){
                        macMap.put(m.getCode(),"");
                    }
                }


            }

               RedisUtils redis=new RedisUtils();
               Set<String> appearanceDatas= redisTemplate.keys("IpqcAppearance*");
               ValueOperations valueOperations = redisTemplate.opsForValue();
               SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

               Map<String,String> appearanceMap=new HashMap<>();
               Map<String,String> sizeMap=new HashMap<>();
               Map<String,String> machineProcessMap=new HashMap<>();
               List<DfMachine>machines=dfMachineService.list();
               if(machines.size()>0){
                   for(DfMachine m:machines){
                       machineProcessMap.put(m.getCode(),m.getProcessCode());
                   }
               }
               //外观抽检任务
               for (String key : appearanceDatas) {
                   if(macMap.containsKey(key.split(":")[1])){ //判断是否开启动态ipqc
                       Object v = valueOperations.get(key);
                       if(null!=v){
                           DynamicIpqcMac data = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                           if(null!=data){
                                 if(StringUtils.isEmpty(data.getOperationStatus())||(StringUtils.isNotEmpty(data.getOperationStatus())&&!data.getOperationStatus().equals("停机")&&!data.getOperationStatus().equals("风险批全检"))){
                                   String lastTime=null!=data.getLastSendMesTime()?data.getLastSendMesTime():data.getUpdateTimeStr();
                                    long diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - sd.parse(lastTime).getTime();
//                                    System.out.println(diff  / 1000);
                                    double diffTime=diff/1000/60 - (data.getFrequency()*60);
                                    //提前10分钟提醒
                                    if (diffTime>-11&&diffTime<7200) {
                                        if(machineProcessMap.containsKey(data.getMachineCode())){
                                            String macKey=machineProcessMap.get(data.getMachineCode());
                                            if(appearanceMap.containsKey(macKey)){
                                                String value=appearanceMap.get(macKey)+","+data.getMachineCode();
                                                appearanceMap.put(macKey,value);
                                            }else {
                                                appearanceMap.put(macKey,data.getMachineCode());
                                            }

                                            data.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
                                            data.setInfoStatusTime(TimeUtil.getNowTimeByNormal());
                                            data.setInfoStatus("抽检");
                                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                            redisTemplate.opsForValue().set("IpqcAppearance:"+data.getMachineCode(),gson.toJson(data));
                                        }
                                    }
                                }


                           }
                       }
                   }

               }

               for (Map.Entry<String, String> entry : appearanceMap.entrySet()) {
                   DynamicIpqcUtil.sendMacCheckMes("外观","动态IPQC抽检提示",entry.getKey(),entry.getValue(),"CPK","");
               }


           }


//            Set<String> sizeDatas= redisTemplate.keys("IpqcSize*");

            //尺寸抽检任务
//            for (String key : sizeDatas) {
//                Object v = valueOperations.get(key);
//                if(null!=v){
//                    DynamicIpqcMac data = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
//                    if(null!=data){
//
//                        String lastTime=null!=data.getLastSendMesTime()?data.getLastSendMesTime():data.getUpdateTimeStr();
//                        long diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() - sd.parse(lastTime).getTime();
//                        System.out.println(diff  / 1000);
//                        double diffTime=diff/1000/60 - (data.getFrequency()*60);
//                        //提前5分钟提醒
//                        if (diffTime>-6&&diffTime<7200) {
//                            if(machineProcessMap.containsKey(data.getMachineCode())){
//                                String macKey=machineProcessMap.get(data.getMachineCode());
//                                if(appearanceMap.containsKey(macKey)){
//                                    String value=appearanceMap.get(macKey)+","+data.getMachineCode();
//                                    appearanceMap.put(macKey,value);
//                                }else {
//                                    appearanceMap.put(macKey,data.getMachineCode());
//                                }
//                                data.setLastSendMesTime(TimeUtil.getNowTimeByNormal());
//                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                                redisTemplate.opsForValue().set("IpqcSize:"+data.getMachineCode(),gson.toJson(data));
//                            }
//                        }
//

//
//
//
//                    }
//                }
//            }
            //                        for (Map.Entry<String, String> entry : appearanceMap.entrySet()) {
//                            DynamicIpqcUtil.sendMacCheckMes("尺寸","动态IPQC抽检提示",entry.getKey().split("-")[0],entry.getValue(),"CPK","");
//                        }
        }

    }


}
