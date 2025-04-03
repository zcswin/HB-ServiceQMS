package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiDefect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAoiPiece;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI缺陷表 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
public interface DfAoiDefectService extends IService<DfAoiDefect> {
    List<DfAoiDefect> getDefectByBarCode(@Param(Constants.WRAPPER) Wrapper<DfAoiDefect> wrapper);

    List<DfAoiDefect> getDefectByPieceName(@Param(Constants.WRAPPER) Wrapper<DfAoiDefect> wrapper);

    List<DfAoiDefect> getAllDefectList(IPage<DfAoiDefect> page, @Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper, @Param("ew_2")Wrapper<DfAoiDefect> wrapper2);

    Integer getTotalPieceNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    Integer getDefectNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    List<DfAoiDefect> getAllDefectMappingList(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);

    List<DfAoiDefect> listItemInfo(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);

    List<DfAoiDefect> listFeaturevaluesInfo(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);

    /**
     * 通用接口查询Fqc缺陷Top及占比
     * @param ew
     * @param top
     * @return
     */
	List<Map<String, Object>> fqcNgTopRate(QueryWrapper<DfAoiDefect> ew,Integer top);

    Rate3 getPieceDefectPoint(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    List<DfAoiDefect> getAllDefectAndWeightList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);
}
