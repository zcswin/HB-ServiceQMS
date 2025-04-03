package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAoiSlThick;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.SlCloseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 丝印-厚度 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
public interface DfAoiSlThickService extends IService<DfAoiSlThick> {

	int importExcel(MultipartFile file) throws Exception;

	List<SlCloseEntity> getCloseData1(QueryWrapper<DfAoiSlThick> ew);

	List<SlCloseEntity> getCloseDataZ(QueryWrapper<DfAoiSlThick> ew);

	List<Rate3> getworstLine(QueryWrapper<DfAoiSlThick> ew);

	List<Rate3> getCloseUp(QueryWrapper<DfAoiSlThick> ew);

	int importExcel2(MultipartFile file) throws Exception;
}
