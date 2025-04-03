package com.ww.boengongye.threadMethod;

import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.utils.ServerInitializeUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicIpqcBoSongFenBu extends Thread {



    private DfQmsIpqcWaigTotal datas;




    public DynamicIpqcBoSongFenBu(DfQmsIpqcWaigTotal datas) {
        super();
        this.datas = datas;

    }



    @Override
    public void run() {
        ServerInitializeUtil.dfQmsIpqcWaigTotalService.DynamicIpqcBoSongFenBu(datas);
    }

}
