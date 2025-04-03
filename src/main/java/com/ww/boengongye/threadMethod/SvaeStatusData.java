package com.ww.boengongye.threadMethod;

import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.utils.ServerInitializeUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class SvaeStatusData extends Thread {

    @Autowired
    DfMacStatusService DfMacStatusService;

    private String datas;




    public SvaeStatusData(String datas) {
        super();
        this.datas = datas;

    }



    @Override
    public void run() {
        ServerInitializeUtil.dfMacStatusService.updateStatus(datas);
    }

}
