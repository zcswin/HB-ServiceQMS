package com.ww.boengongye.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.entity.testentity;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.threadMethod.SvaeStatusData;
import com.ww.boengongye.utils.TimeUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TopicConsumer {

    @Autowired
    DfMacStatusDetailService DfMacStatusDetailService;
    @Autowired
    DfMacStatusService DfMacStatusService;
//    @Value("${statusPool}")
//    private Integer statusPool;
    static Environment env = ApplicationContextProvider.getBean(Environment.class);
    BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<Runnable>(Integer.parseInt(env.getProperty("statusQueue")));
    ExecutorService updateStatusPool = new ThreadPoolExecutor(Integer.parseInt(env.getProperty("statusPool")),
            Integer.parseInt(env.getProperty("statusMaxPool")), 0L, TimeUnit.MILLISECONDS, workingQueue);

    @JmsListener(destination = "${topic1}")
    public void recieve(TextMessage textMessage) throws JMSException {
//        System.out.println("接收到topic消息-->" + textMessage.getText());


        Thread t1 = new SvaeStatusData(textMessage.getText());
        updateStatusPool.execute(t1);





//        JSONObject jsonObject = JSONObject.fromObject(textMessage.getText().toLowerCase());
//        //把保存在xml中的json对象提取出来
//        DfMacStatusDetail data = (DfMacStatusDetail) JSONObject.toBean(jsonObject, DfMacStatusDetail.class);
////        System.out.println("接收到topic消息111-->" + textMessage.getText());
////        System.out.println("接收到topic消息222-->" + data.getMachinecode());
//        data.setMachineCode(data.getMachineCode().toUpperCase());
////        data.setCreate_time(Timestamp.valueOf(TimeUtil.timeStamp2Date(data.getPub_time()+"")));
//        DfMacStatusDetailService.save(data);

//        QueryWrapper<DfMacStatus> qw=new QueryWrapper<>();
//        qw.eq("MachineCode",data.getMachinecode());
//        qw.last("limit 0,1");
//        DfMacStatus ds=DfMacStatusService.getOne(qw);
//        if(null!=ds&&null!=ds.getStatusidCur()&&ds.getStatusidCur()!=data.getStatusid_cur()){
//            ds.setStatusidCur(data.getStatusid_cur());
//            DfMacStatusService.updateById(ds);
//
//        }else {
//

//          syn(data);
//        }
//        lock.lock();// 加锁预防并发操作同一条信息
//        try {
//            QueryWrapper<DfMacStatus> qw = new QueryWrapper<>();
//            qw.eq("MachineCode", data.getMachineCode());
//            qw.last("limit 0,1");
//            DfMacStatus ds = DfMacStatusService.getOne(qw);
//            if (null != ds) {
//                if (null != ds.getStatusidCur() && ds.getStatusidCur() != data.getStatusidCur()) {
//                    System.out.println("更新");
//                    ds.setStatusidCur(data.getStatusidCur());
//
//                    ds.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                    DfMacStatusService.updateById(ds);
//                }
//
//
//            }
//            else {
//
//                DfMacStatus dss = new DfMacStatus();
//                dss.setStatusidCur(data.getStatusid_cur());
//                dss.setMachineCode(data.getMachinecode());
//                DfMacStatusService.save(dss);
//
//            }
//        } finally {
//            lock.unlock();
//        }

    }

    private static Lock lock = new ReentrantLock();

    //    public  synchronized void syn(DfMacStatusDetail data) {
    public void syn(DfMacStatusDetail data) {
//        lock.lock();// 加锁预防并发操作同一条信息
//        try {
        QueryWrapper<DfMacStatus> qw = new QueryWrapper<>();
        qw.eq("MachineCode", data.getMachineCode());
        qw.last("limit 0,1");
        DfMacStatus ds = DfMacStatusService.getOne(qw);
        if (null != ds && null != ds.getStatusidCur() && ds.getStatusidCur() != data.getStatusidCur()) {
            System.out.println("更新");
            ds.setStatusidCur(data.getStatusidCur());

            ds.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            DfMacStatusService.updateById(ds);

//            } else {

//                DfMacStatus dss = new DfMacStatus();
//                dss.setStatusidCur(data.getStatusid_cur());
//                dss.setMachineCode(data.getMachinecode());
//                DfMacStatusService.save(dss);


        }
//        } finally {
//            lock.unlock();
//        }


    }

}
