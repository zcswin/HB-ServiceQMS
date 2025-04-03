package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfRouting;
import com.ww.boengongye.entity.DfRoutingRelationProcess;
import com.ww.boengongye.entity.DfWorkmanship;
import com.ww.boengongye.mapper.DfRoutingMapper;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfRoutingRelationProcessService;
import com.ww.boengongye.service.DfRoutingService;
import com.ww.boengongye.service.DfWorkmanshipService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Controller
@RequestMapping("/dfRouting")
@ResponseBody
@CrossOrigin
@Api(tags = "工艺路线")
public class DfRoutingController {

    @Autowired
    private DfRoutingService dfRoutingService;

    @Autowired
    private DfProcessService dfProcessService;

    @Autowired
    private DfRoutingRelationProcessService dfRoutingRelationProcessService;

    @GetMapping("/listAll")
    public Result listAll(String factoryCode, String projectCode, String sectionCode, String stationCode, String lineBodyCode) {

        QueryWrapper<DfRouting> qw = new QueryWrapper<>();
        if (factoryCode != null) qw.eq("rou.factory_code", factoryCode);
//        if (projectCode != null) qw.eq("rou.project_code", projectCode);
        if (sectionCode != null) qw.eq("rou.section_code", sectionCode);
        if (stationCode != null) qw.eq("rou.station_code", stationCode);
        if (stationCode != null) qw.eq("rou.line_body_code", lineBodyCode);
        qw.orderByAsc("rou.id", "rel.sort");
        List<DfRouting> routings = dfRoutingService.listJoinProcess(qw);
        List<DfRouting> result = new ArrayList<>();
        Map<Integer, List<String>> map = new HashMap<>();
        for (DfRouting routing : routings) {
            Integer id = routing.getId();
            List<String> list;
            if (map.containsKey(id)) {
                list = map.get(id);
                //routings.remove(routing);
            } else {
                list = new ArrayList<>();
                result.add(routing);
            }
            list.add(routing.getProcessName());
            map.put(id, list);
        }
        for (DfRouting routing : result) {
            routing.setProcess(map.get(routing.getId()));
        }

        return new Result(200, "查询成功", result);
    }
    @GetMapping("/listAll2")
    public Result listAll2() {
        return new Result(200, "查询成功", dfRoutingService.list());
    }

    @GetMapping("/getByRoutingId")
    public Result getByRoutingId(@RequestParam("routingId") Integer routingId) {
        if (routingId == null) return new Result(500,"id为空");
        else {
            QueryWrapper<DfRouting> qw = new QueryWrapper<>();
            qw.eq("rou.id", routingId);
            DfRouting dfRouting = dfRoutingService.listJoinProcess(qw).get(0);
            List<DfProcess> dfProcesses = dfProcessService.listByRouting(routingId);
            Map<String, Object> map = new HashMap<>();
            map.put("routing", dfRouting);
            map.put("process", dfProcesses);
            return new Result(200,"查询成功",map);
        }
    }

    @GetMapping("/listByControlStandard")
    public Result listByControlStandard(String factoryCode, String sectionCode, String stationCode, String lineBodyBode, String projectCode) {
        QueryWrapper<DfRouting> qw = new QueryWrapper<>();
        if (factoryCode != null) {
            qw.eq("factory_code", factoryCode);
        }
        if (sectionCode != null && !sectionCode.equals("undefined")) {
            qw.eq("section_code", sectionCode);
        }
        if (stationCode != null&&!stationCode.equals("undefined")) {
            qw.eq("station_code", stationCode);
        }
        if (lineBodyBode != null) {
            qw.eq("line_code", lineBodyBode);
        }
        if (projectCode != null) {
            qw.eq("project_code", projectCode);
        }

        return new Result(200, "查询成功", dfRoutingService.list(qw));
    }

    @Transactional
    @PostMapping("/insert")
    public Result insert(@RequestBody Map<String, Object> map) {
        DfRouting dfRouting = new DfRouting();
        dfRouting.setRoutingCode((String)map.get("routingCode"));
        dfRouting.setStationCode((String)map.get("stationCode"));
        dfRouting.setFactoryCode((String)map.get("factoryCode"));
        dfRouting.setSectionCode((String)map.get("sectionCode"));
        dfRouting.setRoutingName((String)map.get("routingName"));
        dfRouting.setProjectCode((String)map.get("projectCode"));
        dfRoutingService.save(dfRouting);

        List<Integer> processIds = (List)map.get("processIds");
        LambdaQueryWrapper<DfRouting> qw = new LambdaQueryWrapper<>();
        qw.eq(DfRouting::getRoutingCode, dfRouting.getRoutingCode());
        DfRouting one = dfRoutingService.getOne(qw);
        Integer routingId = one.getId();
        List<DfRoutingRelationProcess> relationList = new ArrayList<>();
        for (int i = 0; i < processIds.size(); i++) {
            DfRoutingRelationProcess relation = new DfRoutingRelationProcess();
            relation.setRoutingId(routingId);
            relation.setProcessId(processIds.get(i));
            relation.setSort(i + 1);
            relationList.add(relation);
        }
        dfRoutingRelationProcessService.saveBatch(relationList);

        return Result.INSERT_SUCCESS;
    }

    @GetMapping("/delete")
    public Result delete(Integer[] ids) {
        List<Integer> list = Arrays.asList(ids);
        dfRoutingService.removeByIds(list);

        LambdaQueryWrapper<DfRoutingRelationProcess> qw = new LambdaQueryWrapper<>();
        qw.in(DfRoutingRelationProcess::getRoutingId, list);
        dfRoutingRelationProcessService.remove(qw);

        return Result.DELETE_SUCCESS;
    }

    @PostMapping("/update")
    public Result update(@RequestBody Map<String, Object> map) {
        DfRouting dfRouting = new DfRouting();
        dfRouting.setId((Integer)map.get("id"));
        dfRouting.setRoutingCode((String)map.get("routingCode"));
        dfRouting.setStationCode((String)map.get("stationCode"));
        dfRouting.setFactoryCode((String)map.get("factoryCode"));
        dfRouting.setSectionCode((String)map.get("sectionCode"));
        dfRouting.setRoutingName((String)map.get("routingName"));
        dfRouting.setProjectCode((String)map.get("projectCode"));
        dfRoutingService.updateById(dfRouting);

        List<Integer> processIds = (List)map.get("processIds");
        LambdaQueryWrapper<DfRoutingRelationProcess> qw = new LambdaQueryWrapper<>();
        qw.eq(DfRoutingRelationProcess::getRoutingId, dfRouting.getId());
        dfRoutingRelationProcessService.remove(qw);
        List<DfRoutingRelationProcess> relationList = new ArrayList<>();
        for (int i = 0; i < processIds.size(); i++) {
            DfRoutingRelationProcess relation = new DfRoutingRelationProcess();
            relation.setRoutingId(dfRouting.getId());
            relation.setProcessId(processIds.get(i));
            relation.setSort(i + 1);
            relationList.add(relation);
        }
        dfRoutingRelationProcessService.saveBatch(relationList);

        return Result.UPDATE_SUCCESS;
    }
}
