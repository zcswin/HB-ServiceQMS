package com.ww.boengongye.threadMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.utils.ServerInitializeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

public class SvaeSizeData extends Thread {

    @Autowired
    DfMacStatusService DfMacStatusService;

    private String datas;




    public SvaeSizeData(String datas) {
        super();
        this.datas = datas;

    }



    @Override
    public void run() {
        try {

            ServerInitializeUtil.dfSizeDetailService.saveMqData(datas,"upload");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
