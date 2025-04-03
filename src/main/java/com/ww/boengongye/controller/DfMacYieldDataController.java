package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfMacYieldData;
import com.ww.boengongye.service.DfMacYieldDataService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 机台产量数据 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-01-04
 */
@Controller
@RequestMapping("/dfMacYieldData")
@CrossOrigin
@ResponseBody
@Api(tags = "设备产量数据")
public class DfMacYieldDataController {
    @Autowired
    private DfMacYieldDataService dfMacYieldDataService;

    private static final Double PASS_TARGET_RATE = 0.95d;

    @ApiOperation("总良率、漏检率、过杀率、稼动率和达成率")
    @GetMapping("/getOkRate")
    public Result getOkRate(@RequestParam String date, @RequestParam String dayOrNight, @RequestParam String type,
        String machineCode, Integer processId) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.likeRight("yie.create_time", date)
                .eq("yie.day_or_night", dayOrNight)
                .eq(processId != null,"pro.id", processId)
                .eq(machineCode != null,"mac.MachineCode", machineCode);
        List<DfMacYieldData> list = dfMacYieldDataService.listOkRate(qw);
        Map<String, List> result = new HashMap<>();

        if (list.size() > 0){
            List<String> process = new ArrayList<>();

            switch (type) {
                case "cpzll": // 产品总良率
                    List<Double> produceReal = new ArrayList<>();
                    List<Double> produceTarget = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        produceReal.add(data.getProduceRealOkRate());
                        produceTarget.add(data.getProduceTargetOkRate());
                    }
                    result.put("process", process);
                    result.put("produceReal", produceReal);
                    result.put("produceTarget", produceTarget);
                    break;

                case "wgzll": // 外观总良率
                    List<Double> appearanceReal = new ArrayList<>();
                    List<Double> appearanceTarget = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        appearanceReal.add(data.getAppearanceRealOkRate());
                        appearanceTarget.add(data.getAppearanceTargetOkRate());
                    }
                    result.put("process", process);
                    result.put("appearanceReal", appearanceReal);
                    result.put("appearanceTarget", appearanceTarget);
                    break;

                case "cczll": // 尺寸总良率
                    List<Double> sizeReal = new ArrayList<>();
                    List<Double> sizeTarget = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        sizeReal.add(data.getSizeRealOkRate());
                        sizeTarget.add(data.getSizeTargetOkRate());
                    }
                    result.put("process", process);
                    result.put("sizeReal", sizeReal);
                    result.put("sizeTarget", sizeTarget);
                    break;

                case "gsl": // 过杀率
                    List<Double> overkill = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        overkill.add(data.getOverkillRate());
                    }
                    result.put("process", process);
                    result.put("overkill", overkill);
                    break;

                case "jdlhdcl": // 稼动率和达成率
                    List<Double> crop = new ArrayList<>();
                    List<Double> achievement = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        crop.add(data.getCropRate());
                        achievement.add(data.getAchievementRate());
                    }
                    result.put("process", process);
                    result.put("crop", crop);
                    result.put("achievement", achievement);
                    break;

                case "ljl": // 漏检率
                    List<Double> undetected = new ArrayList<>();
                    for (DfMacYieldData data : list) {
                        process.add(data.getProcess());
                        undetected.add(data.getUndetectedRate());
                    }
                    result.put("process", process);
                    result.put("undetected", undetected);
                    break;
            }

        }
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("不良率")
    @GetMapping("/getNgRate")
    public Result getNgRate(String machineCode, Integer processId) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.eq(processId != null, "pro.id", processId)
                .eq(machineCode != null, "mac.MachineCode", machineCode);
        List<DfMacYieldData> list = dfMacYieldDataService.listNgRate(qw);
        Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            String date = list.get(0).getDate();
            List<String> dateList = new ArrayList<>();
            dateList.add(date);

            for (DfMacYieldData data : list) {
                if(!data.getDate().equals(date)) {
                    date = data.getDate();
                    dateList.add(date);
                }
            }
            result.put("date", dateList);

            date = list.get(0).getDate();
            Double[] pp = new Double[dateList.size()];
            Double[] lb = new Double[dateList.size()];
            Double[] sb = new Double[dateList.size()];
            Double[] kb = new Double[dateList.size()];
            Double[] bb = new Double[dateList.size()];
            Double[] qt = new Double[dateList.size()];
            int index = 0;
            for (DfMacYieldData data : list) {
                if(!data.getDate().equals(date)) {
                    date = data.getDate();
                    index++;
                }
                switch (data.getNgType()) {
                    case "破片":
                        pp[index] = data.getNgRate();
                        break;

                    case "亮边":
                        lb[index] = data.getNgRate();
                        break;

                    case "烧边":
                        sb[index] = data.getNgRate();
                        break;

                    case "空崩":
                        kb[index] = data.getNgRate();
                        break;

                    case "崩边":
                        bb[index] = data.getNgRate();
                        break;

                    case "其他":
                        qt[index] = data.getNgRate();
                        break;

                }
            }
            result.put("pp", pp);
            result.put("lb", lb);
            result.put("sb", sb);
            result.put("kb", kb);
            result.put("bb", bb);
            result.put("qt", qt);

        }
        return new Result(200, "查询成功", result);
    }

    /*@ApiOperation("过杀率")
    @GetMapping("/getOverkillRate")
    public Result getOverkillRate(@RequestParam String date) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.likeRight("create_time", date)
                .groupBy("process, date")
                .select("process, DATE_FORMAT(create_time,'%Y-%m-%d') as date, AVG(overkill_rate) as overkill_rate");
        List<DfMacYieldData> list = dfMacYieldDataService.list(qw);
        return new Result(200, "查询成功", list);
    }*/

    /*@ApiOperation("漏检率")
    @GetMapping("/getUndetectedRate")
    public Result getUndetectedRate(@RequestParam String date) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.likeRight("create_time", date)
                .groupBy("process, date")
                .select("process, DATE_FORMAT(create_time,'%Y-%m-%d') as date, AVG(undetected_rate) as undetected_rate");
        List<DfMacYieldData> list = dfMacYieldDataService.list(qw);
        return new Result(200, "查询成功", list);
    }*/

    @ApiOperation("OEE")
    @GetMapping("/getOEE")
    public Result getOEE(@RequestParam String date, String machineCode, Integer processId) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.likeRight("yie.create_time", date)
                .eq(processId != null, "pro.id", processId)
                .eq(machineCode != null, "mac.MachineCode", machineCode);
        List<DfMacYieldData> list = dfMacYieldDataService.listOEE(qw);
        return new Result(200, "查询成功", list);
    }

    /*@ApiOperation("稼动率和达成率")
    @GetMapping("/getCropAchieveRate")
    public Result getCropAchieveRate(@RequestParam String date) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.likeRight("create_time", date)
                .groupBy("process , date")
                .select("process, DATE_FORMAT(create_time,'%Y-%m-%d') as date, AVG(crop_rate) as crop_rate, AVG(achievement_rate) as achievement_rate");
        List<DfMacYieldData> list = dfMacYieldDataService.list(qw);
        return new Result(200, "查询成功", list);
    }*/

    @ApiOperation("总直通率")
    @GetMapping("/getPassThroughRate")
    public Result getPassThroughRate(String machineCode, Integer processId) {
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.eq(processId != null, "pro.id", processId)
                .eq(machineCode != null, "mac.MachineCode", machineCode);
        List<DfMacYieldData> list = dfMacYieldDataService.listPassThroughRate(qw);
        Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            List<String> date = new ArrayList<>();
            List<Double> passRate = new ArrayList<>();
            for (DfMacYieldData data : list) {
                date.add(data.getDate());
                passRate.add(data.getPassThroughRate());
            }
            result.put("date", date);
            result.put("passRate", passRate);
            Double[] passTargetRate = new Double[date.size()];
            Arrays.fill(passTargetRate, PASS_TARGET_RATE);
            result.put("passTargetRate", passTargetRate);
        }


        return new Result(200, "查询成功", result);
    }

    @ApiOperation("设备详情")
    @GetMapping("/getMacDetail")
    public Result getMacDetail (@RequestParam String machineCode) {
        String dayOrNight;
        if (LocalTime.now().getHour() >= 6 && LocalTime.now().getHour() < 18) {
            dayOrNight = "白班";
        } else {
            dayOrNight = "晚班";
        }
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.eq("mac_code", machineCode)
                .likeRight("create_time", LocalDate.now())
                .eq("day_or_night", dayOrNight);
        List<DfMacYieldData> list = dfMacYieldDataService.getMacDetail(qw);
        Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            DfMacYieldData dfMacYieldData = list.get(0);
            result.put("machineCode", dfMacYieldData.getMacCode());
            result.put("statusCurDetail", dfMacYieldData.getStatusCurDetail());
            result.put("statusCur", dfMacYieldData.getStatusCur());
            result.put("okRate", (dfMacYieldData.getAppearanceRealOkRate() + dfMacYieldData.getProduceRealOkRate() + dfMacYieldData.getSizeRealOkRate()) / 3);
            if (dfMacYieldData.getStatusUpdateTime() != null) {
                DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                result.put("statusUpdateTime", fm.format(dfMacYieldData.getStatusUpdateTime()));
            } else {
                result.put("statusUpdateTime", null);
            }
        }
        return new Result(200, "查询成功", result);
    }

    @ApiOperation("工序详情")
    @GetMapping("/getProDetail")
    public Result getProDetail (@RequestParam Integer processId) {
        String dayOrNight;
        if (LocalTime.now().getHour() >= 6 && LocalTime.now().getHour() < 18) {
            dayOrNight = "白班";
        } else {
            dayOrNight = "晚班";
        }
        QueryWrapper<DfMacYieldData> qw = new QueryWrapper<>();
        qw.eq("pro.id", processId)
                .likeRight("yie.create_time", LocalDate.now())
                .eq("yie.day_or_night", dayOrNight);
        List<DfMacYieldData> list = dfMacYieldDataService.getProDetail(qw);
        Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            DfMacYieldData dfMacYieldData = list.get(0);
            result.put("processCode", dfMacYieldData.getProcessCode());
            result.put("statusCurDetail", dfMacYieldData.getStatusCurDetail());
            result.put("statusCur", dfMacYieldData.getStatusCur());
            result.put("okRate", (dfMacYieldData.getAppearanceRealOkRate() + dfMacYieldData.getProduceRealOkRate() + dfMacYieldData.getSizeRealOkRate()) / 3);
            if (dfMacYieldData.getStatusUpdateTime() != null) {
                DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                result.put("statusUpdateTime", fm.format(dfMacYieldData.getStatusUpdateTime()));
            } else {
                result.put("statusUpdateTime", null);
            }
        }
        return new Result(200, "查询成功", result);
    }
}
