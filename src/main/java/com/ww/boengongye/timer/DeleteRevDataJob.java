package com.ww.boengongye.timer;

import com.ww.boengongye.entity.DfMacRev;
import com.ww.boengongye.service.DfMacRevService;
import com.ww.boengongye.service.DfMacStatusDetailService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class DeleteRevDataJob {

    @Autowired
    private DfMacRevService dfMacRevService;

    @Autowired
    private DfMacStatusDetailService dfMacStatusDetailService;

//    @Async
//    @Scheduled(cron ="0 */3 * * * ?")
//    public void autoGenInverDatas(){
//        System.out.println("定時任務執行");
//        List<Integer> list = dfMacStatusDetailService.deleteTimeOut();
//        dfMacStatusDetailService.removeByIds(list);
//        List<Integer> list1 = dfMacRevService.deleteTimeOut();
//        dfMacRevService.removeByIds(list1);
//    }
    
}
