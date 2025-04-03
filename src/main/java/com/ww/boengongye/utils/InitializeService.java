package com.ww.boengongye.utils;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.config.ApplicationContextProvider;
import com.ww.boengongye.entity.DfAoiPosition;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfSizeContStand;
import com.ww.boengongye.entity.DfStatusCode;
import com.ww.boengongye.service.DfControlStandardService;
import com.ww.boengongye.service.DfMacStatusSizeService;
import com.ww.boengongye.service.DfSizeNgTypeService;
import com.ww.boengongye.service.DfStatusCodeService;
import com.ww.boengongye.timer.InitializeCheckRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;



@Component
public class InitializeService implements ApplicationListener<ContextRefreshedEvent> {



//	private static TRuleParameterService tRuleParameterService = ApplicationContextProvider
//			.getBean(TRuleParameterService.class);



	private static DfControlStandardService dfControlStandardService= ApplicationContextProvider
				.getBean(com.ww.boengongye.service.DfControlStandardService.class);

	private static DfSizeNgTypeService dfSizeNgTypeService= ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfSizeNgTypeService.class);

	public static com.ww.boengongye.service.DfSizeContStandService dfSizeContStandService= ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfSizeContStandService.class);

	private static com.ww.boengongye.service.DfMacStatusSizeService dfMacStatusSizeService= ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfMacStatusSizeService.class);

	private static com.ww.boengongye.service.DfStatusCodeService dfStatusCodeService= ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfStatusCodeService.class);
	private static com.ww.boengongye.service.DfAoiPositionService dfAoiPositionService= ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfAoiPositionService.class);



	public	static com.ww.boengongye.service. DfLiableManService dfLiableManService=ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfLiableManService.class);

	public	static com.ww.boengongye.service. DfAuditDetailService dfAuditDetailService=ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfAuditDetailService.class);

	public	static com.ww.boengongye.service. DfFlowDataService dfFlowDataService=ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfFlowDataService.class);


	public	static com.ww.boengongye.service. DfFlowDataUserService dfFlowDataUserService=ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfFlowDataUserService.class);

	public	static com.ww.boengongye.service.DfApprovalTimeService dfApprovalTimeService=ApplicationContextProvider
			.getBean(com.ww.boengongye.service.DfApprovalTimeService.class);


	// 调用配置文件数据
	public static Environment env = ApplicationContextProvider.getBean(Environment.class);



	//AOI定位
	public static List<DfAoiPosition> aoiPosition=new ArrayList<>();

	/**
	 * 服务启动后自动初始化
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {

			System.out.println("项目初始化" );
			List<DfStatusCode> statusCode=dfStatusCodeService.list();
			if(statusCode.size()>0){
				for(DfStatusCode s:statusCode){
					InitializeCheckRule.statusName.put(s.getStatusCode(),s.getName());
				}
			}

//			List<DfMacStatusSize>sizeSta=dfMacStatusSizeService.list();
//			if(sizeSta.size()>0){
//				for(DfMacStatusSize s:sizeSta){
//					InitializeCheckRule.sizeStatus.put(s.getMachineCode(),s);
//				}
//			}

			InitializeCheckRule.sizeContStand=new HashMap<>();
			QueryWrapper<DfSizeContStand> scqw=new QueryWrapper<>();
			scqw.eq("is_use",1);
			List<DfSizeContStand> sizeCont=dfSizeContStandService.list(scqw);
			if(sizeCont.size()>0){
				for(DfSizeContStand d:sizeCont){
					InitializeCheckRule.sizeContStand.put(d.getProject()+d.getColor()+d.getProcess()+d.getTestItem(),d);
//					System.out.println(d.getProcess());
				}
			}

			QueryWrapper<DfAoiPosition> positionWrapper = new QueryWrapper<>();
			positionWrapper
//                .select("big_area","area","x1","x2","y1","y3","tier")
					.eq("project","C27")
					.isNotNull("tier")
					.orderByAsc("tier");

			aoiPosition = dfAoiPositionService.list(positionWrapper);

			System.out.println("项目初始化完毕" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
