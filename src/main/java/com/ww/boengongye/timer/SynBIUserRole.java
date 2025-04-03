package com.ww.boengongye.timer;

import com.ww.boengongye.entity.DfDataSet;
import com.ww.boengongye.entity.Station;
import com.ww.boengongye.entity.UserDepartment;
import com.ww.boengongye.service.DfDataSetService;
import com.ww.boengongye.service.StationService;
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

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync
public class SynBIUserRole {
    @Value("${bi.url}")
    private String url;

    @Value("${bi.account}")
    private String account;

    @Value("${bi.password}")
    private String password;

    @Autowired
    private com.ww.boengongye.service.StationService StationService;
    @Autowired
    private com.ww.boengongye.service.UserDepartmentService UserDepartmentService;

//    @Async
//    @Scheduled(initialDelay = 10000,fixedDelay = 10000)
//    @Scheduled(initialDelay = 100000000,fixedDelay = 10000)
    public void autoGenInverDatas(){
        System.out.println("同步角色");
        ClientConnector conn = new ClientConnector(url);
        conn.open(account, password); // 以管理员身份登录
        List<Station>saveDatas=new ArrayList<>();
        UserManagerService us=new UserManagerService(conn);
        List<? extends IRole>  datas= us.getAllRoles();

        for( IRole d:datas){
            Station df=new Station();
            df.setAlias(d.getAlias());
            df.setDataDesc(d.getDesc());
            df.setName(d.getName());
            df.setDataDesc(d.getDesc());
            df.setId(d.getId());
            saveDatas.add(df);
        }


//        List<UserDepartment>dps=UserDepartmentService.list();
//        for(UserDepartment u:dps){
//            List<? extends IRoleForGroup>  datas2= us.getAssignedRolesOfDepartment(u.getId());
//            System.out.println("组名:"+u.getName());
//            System.out.println(datas2.size());
//            for( IRoleForGroup d:datas2){
//                System.out.println(d.getRole().getName());
//                Station df=new Station();
//                df.setAlias(d.getRole().getAlias());
//                df.setDataDesc(d.getRole().getDesc());
//                df.setName(d.getRole().getName());
//                df.setDataDesc(d.getRole().getDesc());
//                df.setId(d.getRole().getId());
//                df.setGroupId(u.getId());
//                saveDatas.add(df);
//            }
//        }
        StationService.saveOrUpdateBatch(saveDatas);
        conn.close();
    }


}
