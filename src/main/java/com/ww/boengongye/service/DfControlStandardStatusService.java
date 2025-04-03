package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 管控标准状态 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-12
 */
public interface DfControlStandardStatusService extends IService<DfControlStandardStatus> {
    IPage<DfControlStandardStatus> listByJoinPage(IPage<DfControlStandardStatus> page, @Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

    List<DfControlStandardStatus> listAllTypeCount(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

	List<DfControlStandardStatus> queryDailyData(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

	List<DfControlStandardStatus> queryDateData(@Param("projectId") String projectId,@Param("dataType") String dataType,@Param("years") List<Integer> years);


	List<DfControlStandardStatus> queryEachProcessData(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper,
													   @Param("beginTime") String beginTime,
													   @Param("endTime") String endTime );


	List<DfControlStandardStatus> listCountByDay(@Param(Constants.WRAPPER) Wrapper<DfControlStandardStatus> wrapper);

	List<DfControlStandardStatus> getComparationByWeek(@Param("startTime")String startTime, @Param("endTime") String endTime);

	List<DfControlStandardStatus> getComparationByMonth(@Param("startTime")String startTime, @Param("endTime") String endTime);

	List<DfControlStandardStatus> getBaAndBielComparaion(@Param("startTime")String startTime, @Param("endTime") String endTime);
	//优化
	List<Rate3> getBaAndBielComparaion(@Param(Constants.WRAPPER)Wrapper<String> wrapper, @Param("ew_1")Wrapper<String> wrapper1, @Param("ew_2")Wrapper<String> wrapper2);
	List<Rate3> getBaComparaion(@Param(Constants.WRAPPER)Wrapper<String> wrapper);
	//优化
	List<Rate3> getComparaionMonth(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);
	//优化
	List<Rate3> getComparaionWeek(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);
	//优化
	List<Rate3> getComparaionDay(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);

	List<Rate3> getComparaionProcess(@Param(Constants.WRAPPER)Wrapper<String> wrapper,@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);

	List<Rate3> getComparaionFactory(@Param("ew_1")Wrapper<String> wrapper1,@Param("ew_2")Wrapper<String> wrapper2);
}
