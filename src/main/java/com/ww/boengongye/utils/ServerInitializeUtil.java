package com.ww.boengongye.utils;

import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.service.DfSizeDetailService;

public class ServerInitializeUtil {
    public static DfMacStatusService dfMacStatusService = ApplicationContextProvider.getBean(DfMacStatusService.class);
    public static DfSizeDetailService dfSizeDetailService = ApplicationContextProvider.getBean(DfSizeDetailService.class);
    public static DfAoiPieceService dfAoiPieceService = ApplicationContextProvider.getBean(DfAoiPieceService.class);
    public static DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService = ApplicationContextProvider.getBean(DfQmsIpqcWaigTotalService.class);
}
