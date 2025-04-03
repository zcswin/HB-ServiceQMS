package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.EquipmentMaintenance;
import com.ww.boengongye.entity.EquipmentPictureVO;
import com.ww.boengongye.entity.Pictures;
import com.ww.boengongye.mapper.EquipmentMaintenanceMapper;
import com.ww.boengongye.mapper.PicturesMapper;
import com.ww.boengongye.service.EquipmentMaintenanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 96901
* @description 针对表【equipment_maintenance】的数据库操作Service实现
* @createDate 2024-12-25 14:33:23
*/
@Service
@Slf4j
public class EquipmentMaintenanceServiceImpl extends ServiceImpl<EquipmentMaintenanceMapper, EquipmentMaintenance>
    implements EquipmentMaintenanceService {


    @Autowired
    private EquipmentMaintenanceMapper equipmentMaintenanceMapper;

    @Autowired
    private PicturesMapper picturesMapper;






    public  IPage<EquipmentPictureVO> buildDynamicQuery(LocalDateTime startTime, LocalDateTime endTime,
                                                                      String triggerTimeOrder,
                                                                        String processKeyword, String processKeywordOrder,
                                                                       String machineNumberKeyword, String machineNumberKeywordOrder,
                                                                       String problemDetailKeyword, String problemDetailKeywordOrder,
                                                                       String maintenanceSuggestionKeyword, String maintenanceSuggestionKeywordOrder,
                                                                       String dataSourceKeyword, String dataSourceKeywordOrder,int pageNum, int pageSize
                                                        ) {


        QueryWrapper<EquipmentMaintenance> equipmentQueryWrapper = new QueryWrapper<>();
        // 排除 wave_graph1 和 wave_graph2 字段
        equipmentQueryWrapper.select("id", "trigger_time", "process", "machine_number", "problem_detail", "maintenance_suggestion", "data_source");

        equipmentQueryWrapper.between("trigger_time", startTime, endTime);
        // 仅当关键字不为空时添加相应的模糊查询条件
        if (processKeyword != null && !processKeyword.isEmpty()) {
            equipmentQueryWrapper.like("process", "%" + processKeyword + "%");
        }

        if (machineNumberKeyword != null && !machineNumberKeyword.isEmpty()) {
            equipmentQueryWrapper.like("machine_number", "%" + machineNumberKeyword + "%");
        }
        if (problemDetailKeyword != null && !problemDetailKeyword.isEmpty()) {
            equipmentQueryWrapper.like("problem_detail", "%" + problemDetailKeyword + "%");
        }
        if (maintenanceSuggestionKeyword != null && !maintenanceSuggestionKeyword.isEmpty()) {
            equipmentQueryWrapper.like("maintenance_suggestion", "%" + maintenanceSuggestionKeyword + "%");
        }
        if (dataSourceKeyword != null && !dataSourceKeyword.isEmpty()) {
            equipmentQueryWrapper.like("data_source", "%" + dataSourceKeyword + "%");
        }

        // 默认按照 trigger_time 字段降序排序
        // equipmentQueryWrapper.orderByDesc("trigger_time");


        // 添加排序条件


        if (triggerTimeOrder == null || triggerTimeOrder.isEmpty()){
            equipmentQueryWrapper.orderByDesc("trigger_time");
        }


        // 添加排序条件
            if (triggerTimeOrder != null && !triggerTimeOrder.isEmpty()) {
                if (triggerTimeOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("trigger_time");
                } else if (triggerTimeOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("trigger_time");
                }
            }

            if (processKeywordOrder != null && !processKeywordOrder.isEmpty()) {
                if (processKeywordOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("process");
                } else if (processKeywordOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("process");
                }
            }

            if (machineNumberKeywordOrder != null && !machineNumberKeywordOrder.isEmpty()) {
                if (machineNumberKeywordOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("machine_number");
                } else if (machineNumberKeywordOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("machine_number");
                }
            }
            if (problemDetailKeywordOrder != null && !problemDetailKeywordOrder.isEmpty()) {
                if (problemDetailKeywordOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("problem_detail");
                } else if (problemDetailKeywordOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("problem_detail");
                }
            }
            if (maintenanceSuggestionKeywordOrder != null && !maintenanceSuggestionKeywordOrder.isEmpty()) {
                if (maintenanceSuggestionKeywordOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("maintenance_suggestion");
                } else if (maintenanceSuggestionKeywordOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("maintenance_suggestion");
                }
            }
            if (dataSourceKeywordOrder != null && !dataSourceKeywordOrder.isEmpty()) {
                if (dataSourceKeywordOrder.equalsIgnoreCase("asc")) {
                    equipmentQueryWrapper.orderByAsc("data_source");
                } else if (dataSourceKeywordOrder.equalsIgnoreCase("desc")) {
                    equipmentQueryWrapper.orderByDesc("data_source");
                }


            }


            // 创建分页对象
            Page<EquipmentMaintenance> page = new Page<>(pageNum, pageSize);


            // 执行分页查询
            IPage<EquipmentMaintenance> equipmentPage = equipmentMaintenanceMapper.selectPage(page, equipmentQueryWrapper);


            log.info("查询到的 equipment_maintenance 记录数量：{}", equipmentPage.getTotal());


            List<EquipmentPictureVO> equipmentPictureVOList = new ArrayList<>();


            for (EquipmentMaintenance equipment : equipmentPage.getRecords()) {
                Long equipmentId = equipment.getId();


                // 查询 pictures 表
                QueryWrapper<Pictures> picturesQueryWrapper = new QueryWrapper<>();
                picturesQueryWrapper.eq("cen_id", equipmentId);
                List<Pictures> picturesList = picturesMapper.selectList(picturesQueryWrapper);


                log.info("equipment_maintenance 表中 id 为 {} 的记录对应的 pictures 记录数量：{}", equipmentId, picturesList.size());


                EquipmentPictureVO equipmentPictureVO = new EquipmentPictureVO();
                equipmentPictureVO.setEquipmentMaintenance(equipment);
                equipmentPictureVO.setPictures(picturesList);
                equipmentPictureVOList.add(equipmentPictureVO);
            }


            log.info("最终组合的 EquipmentPictureVO 列表的大小：{}", equipmentPictureVOList.size());


            // 将 EquipmentPictureVO 列表添加到分页结果中
            IPage<EquipmentPictureVO> resultPage = new Page<>();
            resultPage.setTotal(equipmentPage.getTotal());
            resultPage.setPages(equipmentPage.getPages());
            resultPage.setCurrent(equipmentPage.getCurrent());
            resultPage.setSize(equipmentPage.getSize());
            resultPage.setRecords(equipmentPictureVOList);


            return resultPage;


        }







    @Override
    public List<String> findDistinctProcessesByDateRange() {

        String begin ="2024-12-16 00:00:00";
        String end   ="2024-12-29 00:00:00";



        QueryWrapper<EquipmentMaintenance> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("process");
        queryWrapper.between("trigger_time", begin, end);
        queryWrapper.groupBy("process");
        queryWrapper.orderByAsc("process");
        List<EquipmentMaintenance> equipmentMaintenances = equipmentMaintenanceMapper.selectList(queryWrapper);


        // 直接查询 process 为 String 类型的列表，避免后续的类型转换和去重操作
        List<String> distinctProcesses = equipmentMaintenanceMapper.selectObjs(queryWrapper).stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toList());

        return distinctProcesses;
    }






































}
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //    public IPage<EquipmentPictureVO> findAllsByCollecttimeBetweens(LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize) {
        //        // log.info("开始分页查询，开始时间：{}，结束时间：{}，页码：{}，每页记录数：{}");
        //
        //
        //        // 查询 equipment_maintenance 表，排除 wave_graph1 和 wave_graph2 字段
        //        QueryWrapper<EquipmentMaintenance> equipmentQueryWrapper = new QueryWrapper<>();
        //        equipmentQueryWrapper.select(EquipmentMaintenance.class, info ->!info.getColumn().equals("wave_graph1") &&!info.getColumn().equals("wave_graph2"));
        //        equipmentQueryWrapper.select("id", "trigger_time", "process", "machine_number", "problem_detail", "maintenance_suggestion", "data_source");
        //        equipmentQueryWrapper.between("trigger_time", startTime, endTime);
        //
        //        // 添加时间降序排序
        //        equipmentQueryWrapper.orderByDesc("trigger_time");
        //
        //
        //
        //        // 创建分页对象
        //        Page<EquipmentMaintenance> page = new Page<>(pageNum, pageSize);
        //
        //
        //        // 执行分页查询
        //        IPage<EquipmentMaintenance> equipmentPage = equipmentMaintenanceMapper.selectPage(page, equipmentQueryWrapper);
        //
        //
        //        log.info("查询到的 equipment_maintenance 记录数量：{}", equipmentPage.getTotal());
        //
        //
        //        List<EquipmentPictureVO> equipmentPictureVOList = new ArrayList<>();
        //
        //
        //        for (EquipmentMaintenance equipment : equipmentPage.getRecords()) {
        //            Long equipmentId = equipment.getId();
        //
        //
        //            // 查询 pictures 表
        //            QueryWrapper<Pictures> picturesQueryWrapper = new QueryWrapper<>();
        //            picturesQueryWrapper.eq("cen_id", equipmentId);
        //            List<Pictures> picturesList = picturesMapper.selectList(picturesQueryWrapper);
        //
        //
        //            log.info("equipment_maintenance 表中 id 为 {} 的记录对应的 pictures 记录数量：{}", equipmentId, picturesList.size());
        //
        //
        //            EquipmentPictureVO equipmentPictureVO = new EquipmentPictureVO();
        //            equipmentPictureVO.setEquipmentMaintenance(equipment);
        //            equipmentPictureVO.setPictures(picturesList);
        //            equipmentPictureVOList.add(equipmentPictureVO);
        //        }
        //
        //
        //        log.info("最终组合的 EquipmentPictureVO 列表的大小：{}", equipmentPictureVOList.size());
        //
        //
        //        // 将 EquipmentPictureVO 列表添加到分页结果中
        //        IPage<EquipmentPictureVO> resultPage = new Page<>();
        //        resultPage.setTotal(equipmentPage.getTotal());
        //        resultPage.setPages(equipmentPage.getPages());
        //        resultPage.setCurrent(equipmentPage.getCurrent());
        //        resultPage.setSize(equipmentPage.getSize());
        //        resultPage.setRecords(equipmentPictureVOList);
        //
        //
        //        return resultPage;
        //    }
        //
        //
        //
        //
        //
        //    public IPage<EquipmentPictureVO> findAllprocess(LocalDateTime startTime, LocalDateTime endTime, String processKeyword, int pageNum, int pageSize) {
        //
        //
        //
        //        // 查询 equipment_maintenance 表，排除 wave_graph1 和 wave_graph2 字段
        //        QueryWrapper<EquipmentMaintenance> equipmentQueryWrapper = new QueryWrapper<>();
        //        equipmentQueryWrapper.select("id", "trigger_time", "process", "machine_number", "problem_detail", "maintenance_suggestion", "data_source");
        //        equipmentQueryWrapper.between("trigger_time", startTime, endTime);
        //        equipmentQueryWrapper.like("process", "%" + processKeyword + "%");
        //        equipmentQueryWrapper.like("trigger_time", "%" + processKeyword + "%");
        //        equipmentQueryWrapper.like("process", "trigger_time");
        //        equipmentQueryWrapper.like("machine_number", "%" + processKeyword + "%");
        //        equipmentQueryWrapper.like("problem_detail", "%" + processKeyword + "%");
        //        equipmentQueryWrapper.like("maintenance_suggestion", "%" + processKeyword + "%");
        //        equipmentQueryWrapper.like("data_source", "%" + processKeyword + "%");
        //        // 添加时间降序排序
        //        equipmentQueryWrapper.orderByDesc("trigger_time");
        //
        //        equipmentQueryWrapper.orderByDesc("process");
        //
        //        // 创建分页对象
        //        Page<EquipmentMaintenance> page = new Page<>(pageNum, pageSize);
        //
        //
        //        // 执行分页查询
        //        IPage<EquipmentMaintenance> equipmentPage = equipmentMaintenanceMapper.selectPage(page, equipmentQueryWrapper);
        //
        //
        //        // log.info("查询到的 equipment_maintenance 记录数量：{}", equipmentPage.getTotal());
        //
        //
        //        List<EquipmentPictureVO> equipmentPictureVOList = new ArrayList<>();
        //
        //
        //        for (EquipmentMaintenance equipment : equipmentPage.getRecords()) {
        //            Long equipmentId = equipment.getId();
        //
        //
        //            // 查询 pictures 表
        //            QueryWrapper<Pictures> picturesQueryWrapper = new QueryWrapper<>();
        //            picturesQueryWrapper.eq("cen_id", equipmentId);
        //            List<Pictures> picturesList = picturesMapper.selectList(picturesQueryWrapper);
        //
        //
        //            // log.info("equipment_maintenance 表中 id 为 {} 的记录对应的 pictures 记录数量：{}", equipmentId, picturesList.size());
        //
        //
        //            EquipmentPictureVO equipmentPictureVO = new EquipmentPictureVO();
        //            equipmentPictureVO.setEquipmentMaintenance(equipment);
        //            equipmentPictureVO.setPictures(picturesList);
        //            equipmentPictureVOList.add(equipmentPictureVO);
        //        }
        //
        //
        //        // log.info("最终组合的 EquipmentPictureVO 列表的大小：{}", equipmentPictureVOList.size());
        //
        //
        //        // 将 EquipmentPictureVO 列表添加到分页结果中
        //        IPage<EquipmentPictureVO> resultPage = new Page<>();
        //        resultPage.setTotal(equipmentPage.getTotal());
        //        resultPage.setPages(equipmentPage.getPages());
        //        resultPage.setCurrent(equipmentPage.getCurrent());
        //        resultPage.setSize(equipmentPage.getSize());
        //        resultPage.setRecords(equipmentPictureVOList);
        //
        //
        //        return resultPage;
        //    }
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //    public List<EquipmentPictureVO> findAllByCollecttimeBetweens(LocalDateTime startTime, LocalDateTime endTime) {
        //        log.info("开始查询，开始时间：{}，结束时间：{}", startTime, endTime);
        //
        //
        //        // 查询 equipment_maintenance 表，排除 wave_graph1 和 wave_graph2 字段
        //        QueryWrapper<EquipmentMaintenance> equipmentQueryWrapper = new QueryWrapper<>();
        //        equipmentQueryWrapper.select(EquipmentMaintenance.class, info -> !info.getColumn().equals("wave_graph1") && !info.getColumn().equals("wave_graph2"));
        //        equipmentQueryWrapper.select("id", "trigger_time", "process", "machine_number", "problem_detail", "maintenance_suggestion", "data_source");
        //        equipmentQueryWrapper.between("trigger_time", startTime, endTime);
        //        // 添加时间降序排序
        //        equipmentQueryWrapper.orderByDesc("trigger_time");
        //        List<EquipmentMaintenance> equipmentList = equipmentMaintenanceMapper.selectList(equipmentQueryWrapper);
        //
        //
        //
        //
        //
        //
        //        // log.info("查询到的 equipment_maintenance 记录数量：{}", equipmentList.size());
        //
        //
        //        List<EquipmentPictureVO> equipmentPictureVOList = new ArrayList<>();
        //
        //
        //        for (EquipmentMaintenance equipment : equipmentList) {
        //            Long equipmentId = equipment.getId();
        //
        //
        //            // 查询 pictures 表
        //            QueryWrapper<Pictures> picturesQueryWrapper = new QueryWrapper<>();
        //            picturesQueryWrapper.eq("cen_id", equipmentId);
        //            List<Pictures> picturesList = picturesMapper.selectList(picturesQueryWrapper);
        //
        //
        //            // log.info("equipment_maintenance 表中 id 为 {} 的记录对应的 pictures 记录数量：{}", equipmentId, picturesList.size());
        //
        //
        //            EquipmentPictureVO equipmentPictureVO = new EquipmentPictureVO();
        //            equipmentPictureVO.setEquipmentMaintenance(equipment);
        //            equipmentPictureVO.setPictures(picturesList);
        //            equipmentPictureVOList.add(equipmentPictureVO);
        //        }
        //
        //
        //        // log.info("最终组合的 EquipmentPictureVO 列表的大小：{}", equipmentPictureVOList.size());
        //
        //
        //        return equipmentPictureVOList;
        //    }
        //
        //
        //
        //
        //
        // public    List<EquipmentPictureVO>   findAllByCollecttimeBetween(LocalDateTime startTime, LocalDateTime endTime, String processKeyword){
        //
        //
        //
        //        // 查询 equipment_maintenance 表，排除 wave_graph1 和 wave_graph2 字段
        //        QueryWrapper<EquipmentMaintenance> equipmentQueryWrapper = new QueryWrapper<>();
        //        equipmentQueryWrapper.select(EquipmentMaintenance.class, info -> !info.getColumn().equals("wave_graph1") && !info.getColumn().equals("wave_graph2"));
        //        equipmentQueryWrapper.select("id", "trigger_time", "process", "machine_number", "problem_detail", "maintenance_suggestion", "data_source");
        //        equipmentQueryWrapper.between("trigger_time", startTime, endTime);
        //        equipmentQueryWrapper.like("process", "%" + processKeyword + "%");
        //     // 添加时间降序排序
        //     equipmentQueryWrapper.orderByDesc("trigger_time");
        //        List<EquipmentMaintenance> equipmentList = equipmentMaintenanceMapper.selectList(equipmentQueryWrapper);
        //
        //
        //        System.out.println(equipmentList);
        //
        //        // log.info("查询到的 equipment_maintenance 记录数量：{}", equipmentList.size());
        //
        //
        //        List<EquipmentPictureVO> equipmentPictureVOList = new ArrayList<>();
        //
        //
        //        for (EquipmentMaintenance equipment : equipmentList) {
        //            Long equipmentId = equipment.getId();
        //
        //
        //            // 查询 pictures 表
        //            QueryWrapper<Pictures> picturesQueryWrapper = new QueryWrapper<>();
        //            picturesQueryWrapper.eq("cen_id", equipmentId);
        //            List<Pictures> picturesList = picturesMapper.selectList(picturesQueryWrapper);
        //
        //
        //            // log.info("equipment_maintenance 表中 id 为 {} 的记录对应的 pictures 记录数量：{}", equipmentId, picturesList.size());
        //
        //
        //            EquipmentPictureVO equipmentPictureVO = new EquipmentPictureVO();
        //            equipmentPictureVO.setEquipmentMaintenance(equipment);
        //            equipmentPictureVO.setPictures(picturesList);
        //            equipmentPictureVOList.add(equipmentPictureVO);
        //        }
        //
        //
        //        // log.info("最终组合的 EquipmentPictureVO 列表的大小：{}", equipmentPictureVOList.size());
        //
        //
        //        return equipmentPictureVOList;
        //
        //
        //
        //
        //
        //
        //
        //
        //    }
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //





