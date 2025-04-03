package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfControlStandard;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfMyJob;
import com.ww.boengongye.entity.DfSize;
import com.ww.boengongye.service.DfControlStandardService;
import com.ww.boengongye.service.DfLiableManService;
import com.ww.boengongye.service.DfMyJobService;
import com.ww.boengongye.service.DfSizeService;
import com.ww.boengongye.timer.InitializeCheckRule;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.math.BigDecimal;

@Component
public class TopicSizeData {

    @Autowired
    DfControlStandardService DfControlStandardService;
    @Autowired
    DfSizeService DfSizeService;

    @Autowired
    DfMyJobService DfMyJobService;

    @Autowired
    DfLiableManService DfLiableManService;

    @Autowired
    private Environment env;

//    @JmsListener(destination = "${topic4}")
    public void recieve(TextMessage textMessage) throws JMSException {
        if(Integer.parseInt(env.getProperty("topic4Receive"))==0) {
            StopWatch stopWatch = new StopWatch();
            //计时开始
            stopWatch.start("任务一");

            System.out.println("接收到topic消息-->" + textMessage.getText());

            JSONObject jsonObject = JSONObject.fromObject(textMessage.getText());
            //把保存在xml中的json对象提取出来
            DfSize data = (DfSize) JSONObject.toBean(jsonObject, DfSize.class);


            StringBuffer key = new StringBuffer();
            key.append(data.getFactoryCode());
            key.append(data.getProcess());
            key.append(data.getControlName());
            key.append(data.getClassification());
            key.append(data.getDetail());

            if (null != InitializeCheckRule.rules.get(key.toString())) {
                if (InitializeCheckRule.rules.get(key.toString()).size() > 0) {
                    int i = 0;
                    StringBuffer result = new StringBuffer();
                    for (DfControlStandard d : InitializeCheckRule.rules.get(key.toString())) {

                        BigDecimal dv = new BigDecimal(data.getDataValue());
                        if (null != d.getMin() && null != d.getMax()) {

                            if (dv.compareTo(d.getMin()) == -1 || dv.compareTo(d.getMax()) == 1) {
                                if (i > 0) {
                                    result.append(" - ");
                                }
                                result.append("预警:" + d.getDetail() + ",值:" + data.getDataValue() + ",超出" + d.getStandard());
                                i++;
                            }
                        } else if (null != d.getMin() && null == d.getMax()) {
                            if (dv.compareTo(d.getMin()) == -1) {
                                if (i > 0) {
                                    result.append(" - ");
                                }
                                result.append("预警:" + d.getDetail() + ",值:" + data.getDataValue() + ",超出" + d.getStandard());
                                i++;
                            }
                        } else {
                            if (dv.compareTo(d.getMax()) == 1) {
                                if (i > 0) {
                                    result.append(" - ");
                                }
                                result.append("预警:" + d.getDetail() + ",值:" + data.getDataValue() + ",超出" + d.getStandard());
                                i++;
                            }
                        }
                    }
                    data.setCheckResult(result.toString());

                    if (i > 0) {
                        DfMyJob job = new DfMyJob();
                        job.setName(data.getCheckResult() + " - 请检查!");
                        job.setJobType("点检");
                        job.setResult("待处理");
                        job.setFactoryCode(data.getFactoryCode());
                        job.setClassification(data.getClassification());
                        job.setControlName(data.getControlName());
                        job.setDetail(data.getDetail());
                        job.setProcess(data.getProcess());

                        QueryWrapper<DfLiableMan> qw = new QueryWrapper<>();
                        qw.eq("factory_name", data.getFactoryCode());
                        qw.eq("process_name", data.getProcess());
                        qw.last("limit 0,1");
                        DfLiableMan lm = DfLiableManService.getOne(qw);
                        if (null != lm) {
                            job.setRecipientCode(lm.getLiableManCode());
                            job.setRecipientName(lm.getLiableManName());
                        }
                        DfMyJobService.save(job);
                    }
                }
            }
            DfSizeService.save(data);


            //业务代码结束
            //计时结束
            stopWatch.stop();
            System.out.println("耗时：" + stopWatch.getTotalTimeMillis() + "ms");
            System.out.println("耗时：" + stopWatch.getTotalTimeSeconds() + "s");
            System.out.println(stopWatch.prettyPrint());
        }
    }



}
