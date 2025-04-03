package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.EquipmentMaintenance;
import com.ww.boengongye.entity.EquipmentPictureVO;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author 96901
* @description 针对表【equipment_maintenance】的数据库操作Service
* @createDate 2024-12-25 14:33:23
*/
public interface EquipmentMaintenanceService extends IService<EquipmentMaintenance> {


  public   List<String> findDistinctProcessesByDateRange();



     public  IPage<EquipmentPictureVO> buildDynamicQuery(LocalDateTime startTime, LocalDateTime endTime,
                                                         String triggerTimeOrder,
                                                         String processKeyword, String processKeywordOrder,
                                                         String machineNumberKeyword, String machineNumberKeywordOrder,
                                                         String problemDetailKeyword, String problemDetailKeywordOrder,
                                                         String maintenanceSuggestionKeyword, String maintenanceSuggestionKeywordOrder,
                                                         String dataSourceKeyword, String dataSourceKeywordOrder,int pageNum, int pageSize
     );




}
