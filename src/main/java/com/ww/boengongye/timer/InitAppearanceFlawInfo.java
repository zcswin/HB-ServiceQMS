package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.AgvInfoService;
import com.ww.boengongye.utils.HttpUtil;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

//鼎皇AGV 位置获取
@Component
@EnableScheduling // 1.开启定时任务
public class InitAppearanceFlawInfo {
    @Autowired
    Environment env;


    @Resource
    private RedisTemplate redisTemplate;



    @Autowired
    private com.ww.boengongye.service.DfQmsIpqcWaigDetailService dfQmsIpqcWaigDetailService;
    @Autowired
    private com.ww.boengongye.service.DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Autowired
    private com.ww.boengongye.service.DfProjectService dfProjectService;

    public static Map<String,Double> flawProcessProb=new HashMap<>();//缺陷发生概率

//    @Async
    @Scheduled(initialDelay = 60000,fixedDelay = 120000)
    public void getAGVPosition() {
        if(env.getProperty("IPQCCNC3BoSongFenBu","N").equals("Y")){
            List<DfProject>projectList=dfProjectService.list();
            if(projectList.size()>0){

                for(DfProject p:projectList){//计算工序缺陷占比
                    if(!p.getName().equals("C27")){
                        QueryWrapper<DfQmsIpqcWaigTotal> qw=new QueryWrapper<>();
                        qw.eq("f_bigpro",p.getName());
                        DfQmsIpqcWaigTotal total=dfQmsIpqcWaigTotalService.getTotalAndNgCount(qw);
                        List<DfQmsIpqcWaigDetail>flawProcessData=dfQmsIpqcWaigDetailService.listProcessInfo(p.getName());
                        if(flawProcessData.size()>0){
                            for(DfQmsIpqcWaigDetail s:flawProcessData){
                                System.out.println(p.getName()+":"+s.getCount()+":"+total.getSpotCheckCount());
                                // 泊松分布的参数λ
                                double lambda = Double.parseDouble(s.getCount()+".0")/ Double.parseDouble(total.getSpotCheckCount()+".0");

                                // 创建一个PoissonDistribution实例
                                PoissonDistribution poisson = new PoissonDistribution(lambda);

                                // 计算在给定时间内事件发生k次的概率
                                int k =  s.getCount();
                                double probability = poisson.probability(k);


//                                BigDecimal bd = new BigDecimal(Double.toString(probability));
//
//                                // 设置小数点后保留两位，并进行四舍五入
//                                BigDecimal roundedValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//
//                                // 将结果转换为字符串输出
//                                String formattedValue2 = roundedValue.toPlainString();

                                redisTemplate.opsForValue().set(p.getName()+":"+s.getfSort()+":"+s.getfBigArea()+":"+s.getfSmArea()+":"+s.getFSeq() , probability);
//                                System.out.println("Double.toString：" + Double.toString(probability));
//                                System.out.println("lambda：" + lambda);
//                                System.out.println("probability：" + probability);
//                        System.out.println("在给定时间内事件发生" + k + "的概率2为：" + format4(probability));
//                        System.out.println("在给定时间内事件发生" + k + "的概率3为：" + formattedValue2);
//                                System.out.println(p.getName()+s.getfSort()+s.getfBigArea()+s.getfSmArea()+s.getFSeq()+ "的概率为：" + probability);

                                // 如果你想计算累积概率（即事件发生k次或更少的概率）
//                        double cumulativeProbability = poisson.cumulativeProbability(k);
//
//                        System.out.println("在给定时间内事件发生" + k + "次或更少的累积概率为：" + cumulativeProbability);
                            }
                        }
                    }

                }

            }
        }



    }

    public static String format4(double value) {
        /*
         * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
         */
        return new Formatter().format("%.2f", value).toString();
    }
    public static void main(String[] args) {
        // 泊松分布的参数λ
        double lambda = 0.019444444444444445;

        // 创建一个PoissonDistribution实例
        PoissonDistribution poisson = new PoissonDistribution(lambda);

        // 计算在给定时间内事件发生k次的概率
        int k = 7;
        double probability = poisson.probability(k);

//        System.out.println("在给定时间内事件发生" + k + "次的概率为：" + probability);
//        System.out.println("Double.toString(probability)" + Double.toString(probability));

        BigDecimal bd = new BigDecimal(Double.toString(probability));

        // 设置小数点后保留两位，并进行四舍五入
        BigDecimal roundedValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        // 将结果转换为字符串输出
        String formattedValue2 = roundedValue.toPlainString();
//        System.out.println("formattedValue:" + formattedValue2);

        // 如果你想计算累积概率（即事件发生k次或更少的概率）
        double cumulativeProbability = poisson.cumulativeProbability(k);

//        System.out.println("在给定时间内事件发生" + k + "次或更少的累积概率为：" + cumulativeProbability);



    }
}
