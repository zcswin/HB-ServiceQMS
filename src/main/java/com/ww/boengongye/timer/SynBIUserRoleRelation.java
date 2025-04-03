package com.ww.boengongye.timer;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.Station;
import com.ww.boengongye.entity.UserRelationStation;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.service.user.UserManagerService;
import smartbi.user.IRole;
import smartbi.user.IUser;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class SynBIUserRoleRelation {
    @Value("${bi.url}")
    private String url;

    @Value("${bi.account}")
    private String account;

    @Value("${bi.password}")
    private String password;

    @Autowired
    private com.ww.boengongye.service.StationService StationService;
    @Autowired
    private com.ww.boengongye.service.UserRelationStationService UserRelationStationService;


//    @Async
//    @Scheduled(initialDelay = 10000,fixedDelay = 10000)
//    @Scheduled(initialDelay = 100000000,fixedDelay = 10000)
    public void autoGenInverDatas(){
        System.out.println("同步用户角色关系");
        ClientConnector conn = new ClientConnector(url);
        conn.open(account, password); // 以管理员身份登录
        UserManagerService userManagerService = new UserManagerService(conn);
        List<Station>stations=StationService.list();
        List<UserRelationStation> saveDatas=new ArrayList<>();
        if(stations.size()>0){
            for(Station d:stations){
                System.out.println(d.getName());
                List<? extends IUser>users=userManagerService.getUsersOfRole(d.getId());
                System.out.println(users.size());
                if(users.size()>0){
                    for(IUser u:users){
                        UserRelationStation urs=new UserRelationStation();
                        urs.setStationId(d.getId());
                        urs.setUserId(u.getId());
                        urs.setCreateTime(TimeUtil.getNowTimeByNormal());
                        saveDatas.add(urs);
                    }
                }

            }
        }

        if(saveDatas.size()>0){
            UpdateWrapper<UserRelationStation>uw=new UpdateWrapper<>();
            uw.ge("id",0);
            UserRelationStationService.remove(uw);
            UserRelationStationService.saveBatch(saveDatas);
        }


        conn.close();
    }


}
