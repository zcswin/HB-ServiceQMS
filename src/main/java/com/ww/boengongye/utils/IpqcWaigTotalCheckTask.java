package com.ww.boengongye.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DailyCheckSummaryMapper;
import com.ww.boengongye.mapper.DefectSummaryMapper;
import com.ww.boengongye.service.*;
import com.ww.boengongye.service.impl.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ww.boengongye.utils.ExcelExportUtil2.distinctByKey;

@Component
public class IpqcWaigTotalCheckTask {
    private static final Logger logger = LoggerFactory.getLogger(IpqcWaigTotalCheckTask.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private DailyCheckSummaryMapper dailyCheckSummaryMapper;
    @Resource
    private DefectSummaryMapper defectSummaryMapper;

    DfQmsIpqcWaigTotalCheckService dfQmsIpqcWaigTotalCheckService = BeanUtils.getBean(DfQmsIpqcWaigTotalCheckServiceImpl.class);

    DailyCheckSummaryService dailyCheckSummaryService = BeanUtils.getBean(DailyCheckSummaryServiceImpl.class);

    DefectSummaryService defectSummaryService = BeanUtils.getBean(DefectSummaryServiceImpl.class);

    DfQmsIpqcFlawConfigService dfQmsIpqcFlawConfigService = BeanUtils.getBean(DfQmsIpqcFlawConfigServiceImpl.class);

    DfProcessProjectConfigService dfProcessProjectConfigService = BeanUtils.getBean(DfProcessProjectConfigServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    // 每日凌晨2点执行（调整为北京时间时区）
    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Shanghai")
    public void dailyStatistics() {
        try {
            // 1. 计算昨日数据
            LocalDate yesterday = LocalDate.now().minusDays(1);

            calculateDailyStats(yesterday);

        } catch (Exception e) {
            logger.error("定时任务执行异常", e);
        }
    }

    //统计周数据
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 0 2 ? * MON", zone = "Asia/Shanghai")
    //@Scheduled(cron = "0/1 * * * * ?", zone = "Asia/Shanghai")  // 每秒执行一次
    public void executeWeeklyJob() {
        // 获取上周的周一
        LocalDate lastMonday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1);
        generateWeeklyStats(lastMonday);

        // 添加异常处理逻辑
        try {
            // 可以在此处添加数据校验逻辑
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    //统计月数据
    @Scheduled(cron = "0 0 2 1 * ?") // 每月1号2点执行
    //@Scheduled(cron = "0/1 * * * * ?", zone = "Asia/Shanghai")  // 每秒执行一次
    public void autoGenerateMonthlyReport() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        generateMonthlyStats(lastMonth.withDayOfMonth(1));
    }

    @Transactional(rollbackFor = Exception.class)
    //统计年数据
    @Scheduled(cron = "0 0 2 1 1 ?") // 每年1月1日2点执行
    //@Scheduled(cron = "0/1 * * * * ?", zone = "Asia/Shanghai")  // 每秒执行一次
    public void autoGenerateYearlyReport() {
        LocalDate date = LocalDate.parse("2025-01-01");
        LocalDate lastYear = date.minusYears(1);
        generateYearlyStats(lastYear.withDayOfYear(1));  // 获取上一年的第一天
    }




    @Transactional
    public void generateWeeklyStats(LocalDate weekStart) {

        List<String> defect = getStrings();

        LocalDate weekEnd = weekStart.plusDays(6);
        String periodLabel = String.format("%04d-WK%02d",
                weekStart.getYear(),
                weekStart.get(WeekFields.ISO.weekOfWeekBasedYear()));



        // 第一部分: 查询原始数据
        String type1 = "f1";
        List<DailyCheckSummary> dailyList = getDailyCheckSummaryData(weekStart, weekEnd, type1);

        // 预加载缺陷数据
        loadDefectsForDailyData(dailyList);

        List<Map<String, Object>> shiftData = aggregateDataWeek(dailyList,defect,type1);
        String statisticType = "week";
        storeAllDailyData(weekStart, null, null, shiftData, defect, type1,statisticType,periodLabel,weekStart,weekEnd);


        // 第二部分: 查询原始数据 f2
        String type = "f2";
        List<DailyCheckSummary> dailyData = getDailyCheckSummaryData(weekStart, weekEnd, type);

        // 聚合工序数据和缺陷数据
        Map<DailyCheckSummary, DailyCheckSummary> processMap = aggregateProcessData(dailyData);
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = aggregateDefectData(dailyData);

        // 保存聚合数据
        saveAggregatedData(processMap, defectMap, weekStart, weekEnd, periodLabel,type,"week");

        // 第三部分: 查询原始数据 f3
        String type3 = "f3";
        List<DailyCheckSummary> dailyProcessDataAll = getDailyCheckSummaryData(weekStart, weekEnd, type3);

        // 聚合工序数据和缺陷数据
        Map<DailyCheckSummary, DailyCheckSummary> processMapAll = aggregateProcessData(dailyProcessDataAll);
        Map<DailyCheckSummary, Map<String, Integer>> defectMapAll = aggregateDefectData(dailyProcessDataAll);

        // 保存聚合数据
        saveAggregatedData(processMapAll, defectMapAll, weekStart, weekEnd, periodLabel,type3,"week");
    }

    private List<String> getStrings() {
        // 创建查询包装器并添加非空条件（如果支持）
        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();
        qw.isNotNull("flaw_name"); // 假设数据库字段名为flaw_name，根据实际情况调整

        // 获取缺陷配置（已过滤空flawName）
        List<DfQmsIpqcFlawConfig> dflist = dfQmsIpqcFlawConfigService.listDistinctArea(qw);

        // 使用流处理进行分组统计和过滤
        Map<String, List<DfQmsIpqcFlawConfig>> flawNameGroups = dflist.stream()
                .collect(Collectors.groupingBy(DfQmsIpqcFlawConfig::getFlawName));

        // 提取唯一缺陷名称对应的配置项，并直接映射为缺陷列表
        List<String> defect = flawNameGroups.values().stream()
                .filter(group -> group.size() == 1)  // 保留唯一出现的分组
                .flatMap(group -> group.stream().map(DfQmsIpqcFlawConfig::getFlawName))
                .collect(Collectors.toList());
        // 获取周范围
        return defect;
    }

    // 获取指定数据类型的 DailyCheckSummary 数据
    private List<DailyCheckSummary> getDailyCheckSummaryData(LocalDate start, LocalDate end, String dataType) {
        return dailyCheckSummaryMapper.selectList(
                new QueryWrapper<DailyCheckSummary>()
                        .select("*")
                        .between("date", start, end)
                        .eq("data_type", dataType)
                        .eq("statistic_type", "day")
        );
    }

    // 按业务维度分组，不按班次分组
    private Map<String, List<DailyCheckSummary>> groupByBizKeyAndShift(List<DailyCheckSummary> dailyList) {
        return dailyList.stream()
                .collect(Collectors.groupingBy(this::getBizKey)); // 仅按业务维度分组
    }


    // 聚合工序数据
    private Map<DailyCheckSummary, DailyCheckSummary> aggregateProcessData(List<DailyCheckSummary> dailyData) {
        Map<DailyCheckSummary, DailyCheckSummary> processMap = new HashMap<>();
        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);

            processMap.compute(key, (k, v) -> {
                if (v == null) {
                    v = createDefaultDailyCheckSummary(k);
                }
                accumulateProcessData(v, daily);
                return v;
            });
        }
        return processMap;
    }

    // 聚合缺陷数据
    private Map<DailyCheckSummary, Map<String, Integer>> aggregateDefectData(List<DailyCheckSummary> dailyData) {
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = new HashMap<>();
        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);

            defectMap.compute(key, (k, v) -> {
                if (v == null) v = new HashMap<>();

                // 防止 daily.getFSort() 或 daily.getNum() 为 null
                String fSort = daily.getFSort();
                String numStr = daily.getNum();
                if (fSort != null && numStr != null) {
                    try {
                        int num = Integer.parseInt(numStr);
                        v.merge(fSort, num, Integer::sum);
                    } catch (NumberFormatException e) {
                        // 如果 numStr 不是有效的数字，处理异常（可以记录日志或设定默认值）
                        System.err.println("无效的数字格式: " + numStr);
                    }
                }
                return v;
            });
        }
        return defectMap;
    }



    // 创建默认的 DailyCheckSummary 实例
    private DailyCheckSummary createDefaultDailyCheckSummary(DailyCheckSummary key) {
        DailyCheckSummary v = new DailyCheckSummary();
        v.setFStage(key.getFStage());
        v.setFBigpro(key.getFBigpro());
        v.setFColor(Optional.ofNullable(key.getFColor()).orElse("默认颜色"));
        v.setFFac(key.getFFac());
        v.setFType(key.getFType());
        v.setShift("各工序合计");
        v.setFSeq(key.getFSeq());
        v.setFTestCategory(key.getFTestCategory());
        v.setFLine(key.getFLine());
        v.setFMac(Optional.ofNullable(key.getFMac()).orElse("未指定机台"));
        v.setFTestType(key.getFTestType());
        v.setFTestMan(Optional.ofNullable(key.getFTestMan()).orElse("系统汇总"));
        v.setSpotCheckCount(0);
        v.setOkNum(0);
        v.setNgNum(0);
        v.setOkRate(0.0);
        v.setNgRate(0.0);
        return v;
    }

    // 累加工序数据
    private void accumulateProcessData(DailyCheckSummary v, DailyCheckSummary daily) {
        v.setSpotCheckCount(v.getSpotCheckCount() + Optional.ofNullable(daily.getSpotCheckCount()).orElse(0));
        v.setOkNum(v.getOkNum() + Optional.ofNullable(daily.getOkNum()).orElse(0));
        v.setNgNum(v.getNgNum() + Optional.ofNullable(daily.getNgNum()).orElse(0));
    }




    public void generateMonthlyStats(LocalDate monthStart) {

        List<String> defect = getStrings();

        // 获取月范围
        LocalDate monthEnd = YearMonth.from(monthStart).atEndOfMonth();
        String periodLabel = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        String statisticType = "day";
        String dataType = "f1";
        // 第一部分 - 班次统计
        processShiftStatsMonth(monthStart, monthEnd, periodLabel,statisticType,dataType,defect,"month");

        dataType = "f2";
        // 第二部分 - 工序统计
        processProcessStatsMonth(monthStart, monthEnd, periodLabel,statisticType,dataType,"month");

        dataType = "f3";
        // 第三部分 - 全厂统计
        processFactoryStatsMonth(monthStart, monthEnd, periodLabel,statisticType,dataType,"month");
    }



    public void generateYearlyStats(LocalDate yearStart) {
        List<String> defect = getStrings();

        // 获取年范围
        LocalDate yearEnd = Year.from(yearStart).atDay(1).plusYears(1).minusDays(1);
        String periodLabel = yearStart.format(DateTimeFormatter.ofPattern("yyyy"));

        String statisticType = "month";
        String dataType = "f1";
        // 第一部分 - 班次统计
        processShiftStats(yearStart, yearEnd, periodLabel, statisticType,dataType,defect,"year");

        dataType = "f2";

        // 第二部分 - 工序统计
        processProcessStatsYear(yearStart, yearEnd, periodLabel, statisticType,dataType,"year");

        dataType = "f3";

        // 第三部分 - 全厂统计
        processFactoryStatsYear(yearStart, yearEnd, periodLabel, statisticType,dataType,"year");

    }



    // 通用处理方法（分批处理）
    private void processShiftStats(LocalDate start, LocalDate end, String periodLabel, String statisticType, String dataType, List<String> defect, String dateType) {

        // 生成年月列表（自动处理跨年）
        List<String> yearMonths = new ArrayList<>();
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            int fromMonth = (year == startYear) ? start.getMonthValue() : 1;
            int toMonth = (year == endYear) ? end.getMonthValue() : 12;

            for (int month = fromMonth; month <= toMonth; month++) {
                yearMonths.add(String.format("%d-%02d", year, month));
            }
        }

        // 每批次处理的数量
        final int BATCH_SIZE = 10;

        // 按批次处理
        for (int i = 0; i < yearMonths.size(); i += BATCH_SIZE) {
            // 获取当前批次的年月列表
            List<String> batchYearMonths = yearMonths.subList(i, Math.min(i + BATCH_SIZE, yearMonths.size()));

            // 查询当前批次的数据
            List<DailyCheckSummary> dailyList = dailyCheckSummaryMapper.selectList(
                    new QueryWrapper<DailyCheckSummary>()
                            .in("type_num", batchYearMonths)
                            .eq("data_type", dataType)
                            .eq("statistic_type", statisticType)
            );

            // 预加载缺陷数据
            loadDefectsForDailyData(dailyList);

            // 聚合数据
            List<Map<String, Object>> shiftData = aggregateDataWeek(dailyList, defect, dataType);

            // 存储处理结果
            storeAllDailyData(start, null, null, shiftData, defect, dataType,dateType, periodLabel, start, end);
        }
    }




    private void processShiftStatsMonth(LocalDate start, LocalDate end, String periodLabel, String statisticType, String dataType, List<String> defect, String dateType) {
        // 设置分页参数，假设每次查询1000条数据
        int batchSize = 1000;
        int pageNum = 1;
        List<DailyCheckSummary> dailyList;

        try {
            // 分页查询数据
            do {
                // 通过数据库分页查询返回数据
                Page<DailyCheckSummary> page = new Page<>(pageNum, batchSize);
                QueryWrapper<DailyCheckSummary> queryWrapper = new QueryWrapper<DailyCheckSummary>()
                        .between("date", start, end)
                        .eq("data_type", dataType)
                        .eq("statistic_type", statisticType)
                        .in("shift", "白班", "晚班");

                // 查询分页数据
                dailyList = dailyCheckSummaryMapper.selectPage(page, queryWrapper).getRecords();

                // 如果查询到的数据量小于批次大小，避免subList越界
                if (dailyList.size() < batchSize) {
                    // 处理最后一批数据
                    storeBatchData(dailyList, defect, dataType, periodLabel,start,end);
                    break; // 数据已完全处理，退出循环
                }

                // 处理当前批次的数据
                storeBatchData(dailyList, defect, dataType, periodLabel,start,end);

                // 增加分页号
                pageNum++;
            } while (dailyList.size() == batchSize); // 如果返回的记录数等于批次大小，继续分页查询

        } catch (Exception e) {
            logger.error("批量处理数据异常", e);
        }
    }

    private void storeBatchData(List<DailyCheckSummary> dailyList, List<String> defect, String dataType, String periodLabel,LocalDate start,LocalDate end) {
        try {
            // 预加载缺陷数据
            loadDefectsForDailyData(dailyList);

            // 聚合数据
            List<Map<String, Object>> shiftData = aggregateDataWeek(dailyList, defect, dataType);

            // 存储批次数据
            storeAllDailyData(start, null, null, shiftData, defect, dataType, "month", periodLabel, start, end);
        } catch (Exception e) {
            logger.error("存储批次数据异常", e);
        }
    }






    private void processProcessStatsMonth(LocalDate start, LocalDate end, String periodLabel,String statisticType,String dataType,String dateType) {
        // 查询数据
        List<DailyCheckSummary> dailyData = dailyCheckSummaryMapper.selectList(
                new QueryWrapper<DailyCheckSummary>()
                        .between("date", start, end)
                        .eq("data_type", dataType)
                        .eq("statistic_type", statisticType)
        );

        // 使用复合键聚合
        Map<DailyCheckSummary, DailyCheckSummary> processMap = new HashMap<>();
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = new HashMap<>();

        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);
            aggregateProcessData(processMap, defectMap, daily, key);
        }

        // 保存数据
        saveAggregatedData(processMap, defectMap, start, end, periodLabel, dataType,dateType);
    }

    private void processFactoryStatsMonth(LocalDate start, LocalDate end, String periodLabel,String statisticType,String dataType,String dateType) {
        // 查询数据
        List<DailyCheckSummary> dailyData = dailyCheckSummaryMapper.selectList(
                new QueryWrapper<DailyCheckSummary>()
                        .between("date", start, end)
                        .eq("data_type", dataType)
                        .eq("statistic_type", statisticType)
        );

        // 使用复合键聚合
        Map<DailyCheckSummary, DailyCheckSummary> processMap = new HashMap<>();
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = new HashMap<>();

        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);
            aggregateFactoryData(processMap, defectMap, daily, key);
        }

        // 保存数据
        saveAggregatedData(processMap, defectMap, start, end, periodLabel, dataType,dateType);
    }

    private void processProcessStatsYear(LocalDate start, LocalDate end, String periodLabel,String statisticType,String dataType,String dateType) {
        // 生成年月列表（自动处理跨年）
        List<String> yearMonths = new ArrayList<>();
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            int fromMonth = (year == startYear) ? start.getMonthValue() : 1;
            int toMonth = (year == endYear) ? end.getMonthValue() : 12;

            for (int month = fromMonth; month <= toMonth; month++) {
                yearMonths.add(String.format("%d-%02d", year, month));
            }
        }
        // 查询数据
        List<DailyCheckSummary> dailyData = dailyCheckSummaryMapper.selectList(
                new QueryWrapper<DailyCheckSummary>()
                        .in("type_num", yearMonths)
                        .eq("data_type", dataType)
                        .eq("statistic_type", statisticType)
        );

        // 使用复合键聚合
        Map<DailyCheckSummary, DailyCheckSummary> processMap = new HashMap<>();
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = new HashMap<>();

        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);
            aggregateProcessData(processMap, defectMap, daily, key);
        }

        // 保存数据
        saveAggregatedData(processMap, defectMap, start, end, periodLabel, dataType,dateType);
    }

    private void processFactoryStatsYear(LocalDate start, LocalDate end, String periodLabel,String statisticType,String dataType,String dateType) {
        // 生成年月列表（自动处理跨年）
        List<String> yearMonths = new ArrayList<>();
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            int fromMonth = (year == startYear) ? start.getMonthValue() : 1;
            int toMonth = (year == endYear) ? end.getMonthValue() : 12;

            for (int month = fromMonth; month <= toMonth; month++) {
                yearMonths.add(String.format("%d-%02d", year, month));
            }
        }
        // 查询数据
        List<DailyCheckSummary> dailyData = dailyCheckSummaryMapper.selectList(
                new QueryWrapper<DailyCheckSummary>()
                        .in("type_num", yearMonths)
                        .eq("data_type", dataType)
                        .eq("statistic_type", statisticType)
        );

        // 使用复合键聚合
        Map<DailyCheckSummary, DailyCheckSummary> processMap = new HashMap<>();
        Map<DailyCheckSummary, Map<String, Integer>> defectMap = new HashMap<>();

        for (DailyCheckSummary daily : dailyData) {
            DailyCheckSummary key = buildDimensionKey(daily);
            aggregateFactoryData(processMap, defectMap, daily, key);
        }

        // 保存数据
        saveAggregatedData(processMap, defectMap, start, end, periodLabel, dataType,dateType);
    }




    // 核心工具方法
    private DailyCheckSummary buildDimensionKey(DailyCheckSummary daily) {
        return new DailyCheckSummary(
                daily.getFStage(),
                daily.getFBigpro(),
                daily.getFColor(),
                daily.getFFac(),
                daily.getFType(),
                daily.getShift(),
                daily.getFSeq(),
                daily.getFTestCategory(),
                daily.getFLine(),
                daily.getFMac(),
                daily.getFTestType(),
                daily.getFTestMan()
        );
    }

    private void aggregateProcessData(Map<DailyCheckSummary, DailyCheckSummary> processMap,
                                      Map<DailyCheckSummary, Map<String, Integer>> defectMap,
                                      DailyCheckSummary daily, DailyCheckSummary key) {
        processMap.compute(key, (k, v) -> {
            if (v == null) {
                v = createBaseSummary(k);
                v.setShift("各工序合计");
            }
            return updateSummaryValues(v, daily);
        });

        defectMap.compute(key, (k, v) -> {
            // 如果 v 为 null，初始化为新的 HashMap
            if (v == null) v = new HashMap<>();

            // 确保 daily.getFSort() 和 daily.getNum() 不为 null
            String fSort = daily.getFSort();
            String numStr = daily.getNum();

            if (fSort != null && numStr != null) {
                // 使用 parseInt 之前确认 numStr 不是 null
                try {
                    int num = Integer.parseInt(numStr);
                    // merge 操作，确保不会因空指针异常而失败
                    v.merge(fSort, num, Integer::sum);
                } catch (NumberFormatException e) {
                    // 处理数字格式异常
                    e.printStackTrace();
                }
            }

            return v;
        });

    }

    private void aggregateFactoryData(Map<DailyCheckSummary, DailyCheckSummary> processMap,
                                      Map<DailyCheckSummary, Map<String, Integer>> defectMap,
                                      DailyCheckSummary daily, DailyCheckSummary key) {
        processMap.compute(key, (k, v) -> {
            if (v == null) {
                v = createBaseSummary(k);
                v.setShift("全厂合计");
                v.setFSeq("总合计");
                v.setFMac("ALL_MACHINES");
            }
            return updateSummaryValues(v, daily);
        });

        defectMap.compute(key, (k, v) -> {
            // 如果 v 为 null，初始化为新的 HashMap
            if (v == null) {
                v = new HashMap<>();
            }

            // 获取 daily 中的 FSort 和 Num 值
            String fSort = daily.getFSort();
            String numStr = daily.getNum();

            // 确保 FSort 和 Num 不为 null 且 Num 是有效数字
            if (fSort != null && numStr != null && !numStr.trim().isEmpty()) {
                try {
                    // 尝试将 Num 转换为整数
                    int num = Integer.parseInt(numStr);
                    // 使用 merge 方法合并数量
                    v.merge(fSort, num, Integer::sum);
                } catch (NumberFormatException e) {
                    // 如果 numStr 不是有效的数字，记录错误或跳过
                    System.err.println("Invalid number format for: " + numStr);
                }
            }

            return v;
        });

    }

    private DailyCheckSummary createBaseSummary(DailyCheckSummary k) {
        DailyCheckSummary summary = new DailyCheckSummary();
        // 复制基础字段
        org.springframework.beans.BeanUtils.copyProperties(k, summary);
        // 初始化数值字段
        summary.setSpotCheckCount(0);
        summary.setOkNum(0);
        summary.setNgNum(0);
        summary.setOkRate(0.0);
        summary.setNgRate(0.0);
        return summary;
    }

    private DailyCheckSummary updateSummaryValues(DailyCheckSummary summary, DailyCheckSummary daily) {
        summary.setSpotCheckCount(summary.getSpotCheckCount() +
                Optional.ofNullable(daily.getSpotCheckCount()).orElse(0));
        summary.setOkNum(summary.getOkNum() +
                Optional.ofNullable(daily.getOkNum()).orElse(0));
        summary.setNgNum(summary.getNgNum() +
                Optional.ofNullable(daily.getNgNum()).orElse(0));
        return summary;
    }

    private void saveAggregatedData(Map<DailyCheckSummary, DailyCheckSummary> processMap,
                                    Map<DailyCheckSummary, Map<String, Integer>> defectMap,
                                    LocalDate start, LocalDate end,
                                    String periodLabel, String type,String statisticType) {
        processMap.forEach((dimension, monthly) -> {
            try {
                // 设置统计信息
                monthly.setStatisticType(statisticType);
                monthly.setStatisticDate(start);
                monthly.setEndDate(end);
                monthly.setTypeNum(periodLabel);
                monthly.setDataType(type);

                // 计算比率
                if (monthly.getSpotCheckCount() > 0) {
                    monthly.setOkRate(calcRate(monthly.getOkNum(), monthly.getSpotCheckCount()));
                    monthly.setNgRate(calcRate(monthly.getNgNum(), monthly.getSpotCheckCount()));
                }

                // 保存主记录
                dailyCheckSummaryService.save(monthly);

                // 处理缺陷数据
                Map<String, Integer> defects = defectMap.getOrDefault(dimension, Collections.emptyMap());
                if (!defects.isEmpty() && monthly.getNgNum() > 0) {
                    List<DefectSummary> defectList = defects.entrySet().stream()
                            .filter(entry -> entry.getKey() != null && entry.getValue() > 0)
                            .map(entry -> createDefectRecord(monthly.getId(), entry))
                            .collect(Collectors.toList());

                    defectSummaryService.saveBatch(defectList, 500);
                }
            } catch (Exception e) {
                logger.error("月数据保存失败 - 维度: {} | 错误: {}", dimension, e.getMessage());
            }
        });
    }

    private DefectSummary createDefectRecord(Integer summaryId, Map.Entry<String, Integer> entry) {
        DefectSummary ds = new DefectSummary();
        ds.setProcessSummaryId(summaryId);
        ds.setDefectName(entry.getKey().trim());
        ds.setDefectCount(entry.getValue());
        ds.setDefectRate(Math.round((entry.getValue().doubleValue() / ds.getDefectCount()) * 10000) / 100.0);
        return ds;
    }



    private String getBizKey(DailyCheckSummary d) {
        return Stream.of(
                        d.getFStage(),
                        d.getFBigpro(),
                        d.getFColor(),
                        d.getFFac(),
                        d.getFType(),
                        d.getFSeq(),
                        d.getFTestCategory(),
                        d.getFLine(),
                        d.getFMac(),
                        d.getFTestType(),
                        d.getFTestMan()
                )
                .map(field -> Objects.toString(field, ""))
                .collect(Collectors.joining("|"));
    }

    // 加载缺陷数据
    private void loadDefectsForDailyData(List<DailyCheckSummary> dailyList) {
        List<Integer> dailyIds = dailyList.stream()
                .map(DailyCheckSummary::getId)
                .collect(Collectors.toList());

        if (!dailyIds.isEmpty()) {
            // 获取缺陷数据
            List<DefectSummary> defects = defectSummaryMapper.selectList(
                    new QueryWrapper<DefectSummary>()
                            .in("process_summary_id", dailyIds)
            );

            // 建立缺陷映射关系
            Map<Integer, List<DefectSummary>> defectMap = defects.stream()
                    .collect(Collectors.groupingBy(DefectSummary::getProcessSummaryId));

            // 为每个 DailyCheckSummary 设置缺陷列表
            dailyList.forEach(d ->
                    d.setDefect(defectMap.getOrDefault(d.getId(), Collections.emptyList())) // 确保返回 List<DefectSummary>
            );
        }
    }


    private void processDefects(Map<String, List<DailyCheckSummary>> shiftMap, Integer mainId) {
        // 合并所有班次缺陷
        Map<String, Integer> totalDefects = new HashMap<>();

        shiftMap.forEach((shiftType, dailyList) -> {
            // 处理每个班次
            Map<String, Integer> shiftDefects = dailyList.stream()
                    .flatMap(d -> d.getDefect().stream()) // 获取defect列表
                    .collect(Collectors.groupingBy(
                            defect -> defect.getDefectName(), // 获取DefectSummary的缺陷名称
                            Collectors.summingInt(DefectSummary::getDefectCount) // 累加DefectSummary的数量
                    ));

            // 保存班次缺陷
            saveShiftDefects(mainId, shiftType, shiftDefects);

            // 累加到总缺陷
            shiftDefects.forEach((k, v) -> totalDefects.merge(k, v, Integer::sum));
        });

        // 保存合计缺陷
        saveShiftDefects(mainId, "合计", totalDefects);
    }


    private void saveShiftDefects(Integer mainId, String shiftType, Map<String, Integer> defects) {
        // 获取总检数（用于计算缺陷率）
        Integer totalChecked = dailyCheckSummaryMapper.selectById(mainId).getSpotCheckCount();

        defects.forEach((defectName, count) -> {
            DefectSummary defect = new DefectSummary();
            defect.setProcessSummaryId(mainId);
            defect.setDefectName(defectName);
            defect.setDefectCount(count);
            defect.setDefectRate(calcDefectRate(count, totalChecked));

            defectSummaryMapper.insert(defect);
        });
    }

    private Double calcDefectRate(Integer defectCount, Integer totalChecked) {
        return totalChecked != null && totalChecked > 0 ?
                Math.round((double) defectCount / totalChecked * 10000) / 100.0 : 0.0;
    }

    private DailyCheckSummary saveWeeklyMain(Map<String, List<DailyCheckSummary>> shiftMap,
                                        String periodLabel,
                                        LocalDate start, LocalDate end,String type) {
        // 获取样本数据
        DailyCheckSummary sample = shiftMap.values().iterator().next().get(0);

        // 创建周统计主记录
        DailyCheckSummary weekly = new DailyCheckSummary();
        org.springframework.beans.BeanUtils.copyProperties(sample, weekly);

        // 清除不需要的字段
        weekly.setId(null);
        weekly.setDate(null);
        weekly.setTypeNum(periodLabel);

        // 设置统计标识
        weekly.setStatisticType("week");
        weekly.setDataType(type);
        weekly.setStatisticDate(start);
        weekly.setEndDate(end);

        // 计算数值字段
        calculateWeeklyValues(shiftMap, weekly);

        // 保存主记录
        dailyCheckSummaryMapper.insert(weekly);
        return weekly;
    }

    private void calculateWeeklyValues(Map<String, List<DailyCheckSummary>> shiftMap, DailyCheckSummary weekly) {
        // 合并所有班次数据
        List<DailyCheckSummary> allShifts = shiftMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 累加数值字段
        weekly.setSpotCheckCount(allShifts.stream().mapToInt(DailyCheckSummary::getSpotCheckCount).sum());
        weekly.setOkNum(allShifts.stream().mapToInt(DailyCheckSummary::getOkNum).sum());
        weekly.setNgNum(allShifts.stream().mapToInt(DailyCheckSummary::getNgNum).sum());

        // 计算比率
        weekly.setOkRate(calcRate(weekly.getOkNum(), weekly.getSpotCheckCount()));
        weekly.setNgRate(calcRate(weekly.getNgNum(), weekly.getSpotCheckCount()));
    }


    // 计算比率
    private double calcRate(int numerator, int denominator) {
        return denominator > 0 ?
                Math.round((double) numerator / denominator * 10000) / 100.0 : 0.0;
    }


    public void calculateDailyStats(LocalDate date) {

        QueryWrapper<DfQmsIpqcWaigTotalCheck> ew = new QueryWrapper<>(); // 创建查询包装器
        String endDate = date + " 23:00:00";

        String startDate = date + " 07:00:00";

        ew.between("total.f_time", startDate, endDate);


        int totalCount = dfQmsIpqcWaigTotalCheckService.Count(ew);
        String floor = "4F";

        List<List<DfQmsIpqcWaigTotalCheck>> datalist = getOptimizedDataWithCustomExecutor(ew, totalCount, floor);
        List<DfQmsIpqcWaigTotalCheck> list = datalist.get(0); // 获取外观数据

        // 创建查询包装器
        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();
        QueryWrapper<DfProcessProjectConfig> dw = new QueryWrapper<>();
        dw.orderByAsc("sort");
        // 获取缺陷配置
        List<DfQmsIpqcFlawConfig> dflist = dfQmsIpqcFlawConfigService.listDistinctArea(qw);
        dflist.removeIf(flawConfig -> StringUtils.isEmpty(flawConfig.getFlawName()));
        // 统计缺陷名称出现次数，重复的全部去掉
        Map<String, Long> flawNameCount = dflist.stream()
                .collect(Collectors.groupingBy(DfQmsIpqcFlawConfig::getFlawName, Collectors.counting()));
        dflist.removeIf(flawConfig -> flawNameCount.get(flawConfig.getFlawName()) > 1);
        // 按大区分组（用于后续动态生成缺陷列）
        Map<String, List<DfQmsIpqcFlawConfig>> collect1 = dflist.stream()
                .collect(Collectors.groupingBy(DfQmsIpqcFlawConfig::getBigArea));


        // 获取工序数据
        ArrayList<String> dpNames = new ArrayList<>();
        List<DfProcessProjectConfig> dplistall = dfProcessProjectConfigService.listDistinct(dw);
        //去重
        List<DfProcessProjectConfig> dplist = dplistall.stream()
                .filter(distinctByKey(DfProcessProjectConfig::getProcessName))
                .collect(Collectors.toList());
        dplist.parallelStream()
                .filter(Objects::nonNull) // 过滤掉 null
                .forEach(m -> {
                    if (m != null && m.getProcessName() != null) {
                        dpNames.add(m.getProcessName());
                    }
                });
        ArrayList<String> defect = new ArrayList<>(); // 缺陷列表
        // 添加缺陷名称到标题
        collect1.forEach((bigArea, flawConfigs) -> {
            flawConfigs.forEach(flawConfig -> {
                String flawName = flawConfig.getFlawName();
                if (StringUtils.isNotEmpty(flawName)) {
                    defect.add(flawName);           // 添加到 defect 集合中
                }
            });
        });

        // 第一部分：汇总并存储每天的数据
        for (DfProcessProjectConfig processConfig : dplist) {
            String type = "f1";
            String processName = processConfig.getProcessName();

            // 处理白班和晚班数据
            storeShiftData(date, null, processName, defect, type, list);
        }

        // 第二部分：处理合计数据并存储
        for (DfProcessProjectConfig proc : dplist) {
            String type2 = "f2";
            String processName = proc.getProcessName();
            storeShiftData(date, null, processName, defect, type2, list);
        }


        String type3 = "f3";
        // 第三部分：总体合计（统计所有数据的总和）
        storeShiftData(date, null, null, defect, type3, list);
    }


    // 公共方法：处理单个班次的数据
    private void storeShiftData(LocalDate date, String shift, String processName, List<String> defect, String type, List<DfQmsIpqcWaigTotalCheck> list) {
        if ("f1".equals(type)||"f2".equals(type)) {
            List<Map<String, Object>> shiftData = aggregateDayDataf1(list, date.toString(), processName, shift, defect, type);
            storeAllDailyData(date, shift, processName, shiftData, defect, type,"day","",null,null);
        } else if ("f3".equals(type)) {
            List<Map<String, Object>> shiftData = aggregateDayDataf3(list, date.toString(), processName, shift, defect, type);
            storeAllDailyData(date, shift, processName, shiftData, defect, type,"day","",null,null);
        }

    }


    public List<List<DfQmsIpqcWaigTotalCheck>> getOptimizedDataWithCustomExecutor(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew, int totalCount, String floor) {
        // 创建一个固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1); // 线程池大小为1

        // 创建 CompletableFuture 使用指定的线程池
        CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> futureList = CompletableFuture.supplyAsync(() -> {
            try {

                return getPagedData(ew, totalCount, floor);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executorService);

        // 等待任务完成并获取结果
        List<DfQmsIpqcWaigTotalCheck> list = futureList.join();

        // 合并结果（这里只返回单一的查询结果列表）
        List<List<DfQmsIpqcWaigTotalCheck>> result = new ArrayList<>();
        result.add(list);

        // 关闭线程池
        executorService.shutdown();

        return result;
    }

    public List<DfQmsIpqcWaigTotalCheck> getPagedData(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew, int totalCount, String floor) throws InterruptedException, ExecutionException {
        List<DfQmsIpqcWaigTotalCheck> allData = new ArrayList<>();
        int pageSize = 2000;  // 每页查询2000条记录
        ExecutorService executor = Executors.newFixedThreadPool(4);  // 使用4个线程池进行分页查询并发处理
        DfQmsIpqcWaigTotalCheckService dfQmsIpqcWaigTotalCheckService = BeanUtils.getBean(DfQmsIpqcWaigTotalCheckServiceImpl.class);

        int totalPages = (totalCount + pageSize - 1) / pageSize;  // 计算总页数

        // 使用CompletableFuture提交所有任务
        List<CompletableFuture<List<DfQmsIpqcWaigTotalCheck>>> futures = new ArrayList<>();
        for (int page = 1; page <= totalPages; page++) {
            final int pageOffset = (page - 1) * pageSize;
            CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return dfQmsIpqcWaigTotalCheckService.listWaigExcelDataPage(ew, pageOffset, pageSize, floor, "%%");
                } catch (Exception e) {
                    // 捕获每个查询任务中的异常，并打印日志
                    System.err.println("Error fetching data for page " + pageOffset + ": " + e.getMessage());
                    return new ArrayList<>();  // 返回一个空的列表以确保继续执行
                }
            }, executor);
            futures.add(future);
        }

        // 等待所有任务完成并收集结果
        for (CompletableFuture<List<DfQmsIpqcWaigTotalCheck>> future : futures) {
            try {
                List<DfQmsIpqcWaigTotalCheck> pageData = future.get();  // 获取结果
                if (pageData != null && !pageData.isEmpty()) {
                    allData.addAll(pageData);
                }
            } catch (ExecutionException e) {
                // 捕获线程执行中的异常
                System.err.println("ExecutionException occurred while processing data: " + e.getMessage());
            } catch (InterruptedException e) {
                // 捕获线程被中断的异常
                Thread.currentThread().interrupt();  // 恢复中断状态
                System.err.println("Thread was interrupted: " + e.getMessage());
            }
        }

        executor.shutdown();  // 关闭线程池
        return allData;
    }


    private List<Map<String, Object>> aggregateDayDataf1(List<DfQmsIpqcWaigTotalCheck> list, String day, String processName, String shift, List<String> defectList, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        // 过滤出符合条件的记录
        List<DfQmsIpqcWaigTotalCheck> filtered = list.stream()
                .filter(t -> (processName == null || processName.equals(t.getfSeq())))
                .filter(t -> (day == null || day.equals(t.getDate())))
                .collect(Collectors.toList());


        // 按多个字段进行分组
        Map<String, List<DfQmsIpqcWaigTotalCheck>> collect = filtered.stream().collect(Collectors.groupingBy(total -> {
            return total.getfStage() + "," + total.getfBigpro() + "," + total.getfColor() + "," + total.getfFac() + ","
                    + total.getfType() + "," + total.getDate() + "," + total.getShift() + "," + total.getfSeq()
                    + "," + total.getfTestCategory() + "," + total.getfLine() + "," + total.getfMac() + "," + total.getfTestType() + "," + total.getfTestMan() + "," + total.getfSort();
        }));
        // 新增逻辑：当 collect 为空时生成默认数据
//        if ("f2".equals(type)) {
//            if (collect.isEmpty()) {
//                // 构建默认 keyParts（字段值与查询条件关联）
//                String[] defaultKeyParts = new String[13];
//                defaultKeyParts[5] = day;                // date
//                defaultKeyParts[7] = processName;        // fSeq（工序）
//                //  defaultKeyParts[11] = type;              // fTestType（类型参数）
//
//                // 生成白班/晚班数据（仅当 type=f1 时）
//                if ("f1".equals(type)) {
//                    processShift(result, defaultKeyParts, Collections.emptyList(), defectList, "白班");
//                    processShift(result, defaultKeyParts, Collections.emptyList(), defectList, "晚班");
//                }
//                // 生成合计行
//                processTotal(
//                        result,
//                        defaultKeyParts,
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        defectList
//                );
//            }
//        }



        // 2. 处理每个分组
        collect.forEach((key, iems) -> {
            // 按班次分组
            Map<String, List<DfQmsIpqcWaigTotalCheck>> shiftGroups = iems.stream()
                    .collect(Collectors.groupingBy(DfQmsIpqcWaigTotalCheck::getShift));
            String[] keyParts = key.split(","); // 提取键的各部分

            // 3. 处理白班、晚班、合计
            if ("f1".equals(type)) {
                processShift(result, keyParts, shiftGroups.getOrDefault("白班", Collections.emptyList()), defectList, "白班");
                processShift(result, keyParts, shiftGroups.getOrDefault("晚班", Collections.emptyList()), defectList, "晚班");
            }
            processTotal(result, keyParts,
                    shiftGroups.getOrDefault("白班", Collections.emptyList()),
                    shiftGroups.getOrDefault("晚班", Collections.emptyList()),
                    defectList);
        });

        return result;
    }

    private List<Map<String, Object>> aggregateDayDataf3(List<DfQmsIpqcWaigTotalCheck> list, String day, String processName, String shift, List<String> defectList, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        // 过滤出符合条件的记录
        List<DfQmsIpqcWaigTotalCheck> filtered = list.stream()
                .filter(t -> (day == null || day.equals(t.getDate())))
                .collect(Collectors.toList());

        // 按多个字段进行分组
        Map<String, List<DfQmsIpqcWaigTotalCheck>> collect = filtered.stream().collect(Collectors.groupingBy(total -> {
            return total.getfStage() + "," + total.getfBigpro() + "," + total.getfColor() + "," + total.getfFac() + ","
                    + total.getfType() + "," + total.getDate()
                    + "," + total.getfTestCategory() + "," + total.getfLine() + "," + total.getfMac() + "," + total.getfTestType() + "," + total.getfTestMan() + "," + total.getfSort();
        }));

//        if (collect.isEmpty()) {
//            // 构建默认 keyParts（字段值与查询条件关联）
//            String[] defaultKeyParts = new String[13];
//            defaultKeyParts[5] = day;                // date
//            defaultKeyParts[7] = processName;        // fSeq（工序）
//            // 生成合计行
//            processTotal3(
//                    result,
//                    defaultKeyParts,
//                    Collections.emptyList(),
//                    defectList
//            );
//        }



        // 2. 处理每个分组
        collect.forEach((key, items) -> {
            String[] keyParts = key.split(","); // 提取键的各部分

            // 计算所有记录的合计，不按班别区分
            processTotal3(result, keyParts, items, defectList);
        });

        return result;
    }

    private List<Map<String, Object>> aggregateDataWeek(List<DailyCheckSummary> list, List<String> defectList, String type) {
        List<Map<String, Object>> result = new ArrayList<>();


        // 按多个字段进行分组
        Map<String, List<DailyCheckSummary>> collect = list.stream().collect(Collectors.groupingBy(total -> {
            return total.getFStage() + "," + total.getFBigpro() + "," + total.getFColor() + "," + total.getFFac() + ","
                    + total.getFType() + "," + total.getDate() + "," + total.getShift() + "," + total.getFSeq()
                    + "," + total.getFTestCategory() + "," + total.getFLine() + "," + total.getFMac() + "," + total.getFTestType() + "," + total.getFTestMan() + "," + total.getFSort();
        }));

        // 2. 处理每个分组
        collect.forEach((key, iems) -> {
            // 按班次分组
            Map<String, List<DailyCheckSummary>> shiftGroups = iems.stream()
                    .collect(Collectors.groupingBy(DailyCheckSummary::getShift));
            String[] keyParts = key.split(","); // 提取键的各部分

            // 3. 处理白班、晚班、合计
            if ("f1".equals(type)) {
                processShiftWeek(result, keyParts, shiftGroups.getOrDefault("白班", Collections.emptyList()), defectList, "白班");
                processShiftWeek(result, keyParts, shiftGroups.getOrDefault("晚班", Collections.emptyList()), defectList, "晚班");
            }
            processTotalWeek(result, keyParts,
                    shiftGroups.getOrDefault("白班", Collections.emptyList()),
                    shiftGroups.getOrDefault("晚班", Collections.emptyList()),
                    defectList);
        });

        return result;
    }





    // 生成合计行
    private void processTotal(List<Map<String, Object>> result, String[] keyParts,
                              List<DfQmsIpqcWaigTotalCheck> dayShift,
                              List<DfQmsIpqcWaigTotalCheck> nightShift,
                              List<String> defects) {
        Map<String, Object> totalMap = new LinkedHashMap<>();
        // 基础信息
        totalMap.put("fStage", keyParts[0]);  // 阶段
        totalMap.put("fBigpro", keyParts[1]);  // 项目
        totalMap.put("fColor", keyParts[2]);  // 颜色
        totalMap.put("fFac", keyParts[3]);  // 厂别
        totalMap.put("fType", keyParts[4]);  // 抽检类型
        totalMap.put("date", keyParts[5]);  // 日期
        totalMap.put("shift", "合计");  // 班次
        totalMap.put("fSeq", keyParts[7]);  // 工序
        totalMap.put("fTestCategory", keyParts[8]);  // 测试类别
        totalMap.put("fLine", keyParts[9]);  // 线体
        totalMap.put("fMac", keyParts[10]);  // 机台号
        totalMap.put("fTestType", keyParts[11]);  // 检验类型
        totalMap.put("fTestMan", keyParts[12]);  // 检验员

        // 合并数据
        int totalCheck = dayShift.stream().mapToInt(DfQmsIpqcWaigTotalCheck::getSpotCheckCount).sum() +
                nightShift.stream().mapToInt(DfQmsIpqcWaigTotalCheck::getSpotCheckCount).sum();
        int okTotal = dayShift.stream().mapToInt(t -> Integer.parseInt(t.getOkNum())).sum() +
                nightShift.stream().mapToInt(t -> Integer.parseInt(t.getOkNum())).sum();
        int ngTotal = dayShift.stream().mapToInt(t -> Integer.parseInt(t.getNgNum())).sum() +
                nightShift.stream().mapToInt(t -> Integer.parseInt(t.getNgNum())).sum();

        totalMap.put("spotCheckCount", totalCheck);
        totalMap.put("okNum", okTotal);
        totalMap.put("ngNum", ngTotal);
        totalMap.put("okRate", totalCheck == 0 ? "0.00" : String.format("%.2f", okTotal * 100.0 / totalCheck));
        totalMap.put("ngRate", totalCheck == 0 ? "0.00" : String.format("%.2f", ngTotal * 100.0 / totalCheck));


        // 合并缺陷
        defects.forEach(defect -> {
            int dayCount = dayShift.stream()
                    .filter(t -> defect.equals(t.getfSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            int nightCount = nightShift.stream()
                    .filter(t -> defect.equals(t.getfSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            totalMap.put(defect + "_count", dayCount + nightCount);
            totalMap.put(defect + "_rate", totalCheck == 0 ? "0.00" :
                    String.format("%.2f", (dayCount + nightCount) * 100.0 / totalCheck));
        });

        result.add(totalMap);
    }



    // 生成合计行
    private void processTotalWeek(List<Map<String, Object>> result, String[] keyParts,
                                  List<DailyCheckSummary> dayShift,
                                  List<DailyCheckSummary> nightShift,
                                  List<String> defects) {
        Map<String, Object> totalMap = new LinkedHashMap<>();
        // 基础信息
        totalMap.put("fStage", keyParts[0]);  // 阶段
        totalMap.put("fBigpro", keyParts[1]);  // 项目
        totalMap.put("fColor", keyParts[2]);  // 颜色
        totalMap.put("fFac", keyParts[3]);  // 厂别
        totalMap.put("fType", keyParts[4]);  // 抽检类型
        totalMap.put("date", keyParts[5]);  // 日期
        totalMap.put("shift", "合计");  // 班次
        totalMap.put("fSeq", keyParts[7]);  // 工序
        totalMap.put("fTestCategory", keyParts[8]);  // 测试类别
        totalMap.put("fLine", keyParts[9]);  // 线体
        totalMap.put("fMac", keyParts[10]);  // 机台号
        totalMap.put("fTestType", keyParts[11]);  // 检验类型
        totalMap.put("fTestMan", keyParts[12]);  // 检验员

        // 合并数据
        int totalCheck = dayShift.stream().mapToInt(DailyCheckSummary::getSpotCheckCount).sum() +
                nightShift.stream().mapToInt(DailyCheckSummary::getSpotCheckCount).sum();
        int okTotal = dayShift.stream().mapToInt(t -> t.getOkNum()).sum() +
                nightShift.stream().mapToInt(t -> t.getOkNum()).sum();
        int ngTotal = dayShift.stream().mapToInt(t -> t.getNgNum()).sum() +
                nightShift.stream().mapToInt(t -> t.getNgNum()).sum();

        totalMap.put("spotCheckCount", totalCheck);
        totalMap.put("okNum", okTotal);
        totalMap.put("ngNum", ngTotal);
        totalMap.put("okRate", totalCheck == 0 ? "0.00" : String.format("%.2f", okTotal * 100.0 / totalCheck));
        totalMap.put("ngRate", totalCheck == 0 ? "0.00" : String.format("%.2f", ngTotal * 100.0 / totalCheck));


        // 合并缺陷
        defects.forEach(defect -> {
            int dayCount = dayShift.stream()
                    .filter(t -> defect.equals(t.getFSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            int nightCount = nightShift.stream()
                    .filter(t -> defect.equals(t.getFSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            totalMap.put(defect + "_count", dayCount + nightCount);
            totalMap.put(defect + "_rate", totalCheck == 0 ? "0.00" :
                    String.format("%.2f", (dayCount + nightCount) * 100.0 / totalCheck));
        });

        result.add(totalMap);
    }


    // 生成合计行
    private void processTotal3(List<Map<String, Object>> result, String[] keyParts,
                               List<DfQmsIpqcWaigTotalCheck> allShifts, List<String> defects) {
        Map<String, Object> totalMap = new LinkedHashMap<>();
        // 基础信息
        totalMap.put("fStage", keyParts[0]);  // 阶段
        totalMap.put("fBigpro", keyParts[1]);  // 项目
        totalMap.put("fColor", keyParts[2]);  // 颜色
        totalMap.put("fFac", keyParts[3]);  // 厂别
        totalMap.put("fType", keyParts[4]);  // 抽检类型
        totalMap.put("date", keyParts[5]);  // 日期
        totalMap.put("shift", "全工序");  // 班次
        totalMap.put("fSeq", "总合计");  // 工序
        totalMap.put("fTestCategory", keyParts[6]);  // 测试类别
        totalMap.put("fLine", keyParts[7]);  // 线体
        totalMap.put("fMac", keyParts[8]);  // 机台号
        totalMap.put("fTestType", keyParts[9]);  // 检验类型
        totalMap.put("fTestMan", keyParts[10]);  // 检验员

        // 合并数据，直接对所有班次的数据进行计算
        int totalCheck = allShifts.stream().mapToInt(DfQmsIpqcWaigTotalCheck::getSpotCheckCount).sum();
        int okTotal = allShifts.stream().mapToInt(t -> Integer.parseInt(t.getOkNum())).sum();
        int ngTotal = allShifts.stream().mapToInt(t -> Integer.parseInt(t.getNgNum())).sum();

        totalMap.put("spotCheckCount", totalCheck);
        totalMap.put("okNum", okTotal);
        totalMap.put("ngNum", ngTotal);
        totalMap.put("okRate", totalCheck == 0 ? "0.00" : String.format("%.2f", okTotal * 100.0 / totalCheck));
        totalMap.put("ngRate", totalCheck == 0 ? "0.00" : String.format("%.2f", ngTotal * 100.0 / totalCheck));

        // 合并缺陷数据
        defects.forEach(defect -> {
            int defectCount = allShifts.stream()
                    .filter(t -> defect.equals(t.getfSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            totalMap.put(defect + "_count", defectCount);
            totalMap.put(defect + "_rate", totalCheck == 0 ? "0.00" :
                    String.format("%.2f", defectCount * 100.0 / totalCheck));
        });

        result.add(totalMap);
    }


    // 处理单个班次数据
    private void processShift(List<Map<String, Object>> result, String[] keyParts,
                              List<DfQmsIpqcWaigTotalCheck> items, List<String> defects, String shift) {
        // if (items.isEmpty()) return;

        Map<String, Object> dataMap = new LinkedHashMap<>();
        // 设置基本信息
        dataMap.put("fStage", keyParts[0]);  // 阶段
        dataMap.put("fBigpro", keyParts[1]);  // 项目
        dataMap.put("fColor", keyParts[2]);  // 颜色
        dataMap.put("fFac", keyParts[3]);  // 厂别
        dataMap.put("fType", keyParts[4]);  // 抽检类型
        dataMap.put("date", keyParts[5]);  // 日期
        dataMap.put("shift", shift);  // 班次
        dataMap.put("fSeq", keyParts[7]);  // 工序
        dataMap.put("fTestCategory", keyParts[8]);  // 测试类别
        dataMap.put("fLine", keyParts[9]);  // 线体
        dataMap.put("fMac", keyParts[10]);  // 机台号
        dataMap.put("fTestType", keyParts[11]);  // 检验类型
        dataMap.put("fTestMan", keyParts[12]);  // 检验员

        // 统计数值
        int totalCheck = items.stream().mapToInt(DfQmsIpqcWaigTotalCheck::getSpotCheckCount).sum();
        int okCount = items.stream().mapToInt(t -> Integer.parseInt(t.getOkNum())).sum();
        int ngCount = items.stream().mapToInt(t -> Integer.parseInt(t.getNgNum())).sum();

        dataMap.put("spotCheckCount", totalCheck);
        dataMap.put("okNum", okCount);
        dataMap.put("ngNum", ngCount);
        dataMap.put("okRate", totalCheck == 0 ? "0.00" : String.format("%.2f", okCount * 100.0 / totalCheck));
        dataMap.put("ngRate", totalCheck == 0 ? "0.00" : String.format("%.2f", ngCount * 100.0 / totalCheck));


        // 缺陷数据
        defects.forEach(defect -> {
            int count = items.stream()
                    .filter(t -> defect.trim().equals(t.getfSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            dataMap.put(defect + "_count", count);
            dataMap.put(defect + "_rate", totalCheck == 0 ? "0.00" : String.format("%.2f", count * 100.0 / totalCheck));
        });

        result.add(dataMap);
    }


    // 处理单个班次数据
    private void processShiftWeek(List<Map<String, Object>> result, String[] keyParts,
                                  List<DailyCheckSummary> items, List<String> defects, String shift) {
        // if (items.isEmpty()) return;

        Map<String, Object> dataMap = new LinkedHashMap<>();
        // 设置基本信息
        dataMap.put("fStage", keyParts[0]);  // 阶段
        dataMap.put("fBigpro", keyParts[1]);  // 项目
        dataMap.put("fColor", keyParts[2]);  // 颜色
        dataMap.put("fFac", keyParts[3]);  // 厂别
        dataMap.put("fType", keyParts[4]);  // 抽检类型
        dataMap.put("date", keyParts[5]);  // 日期
        dataMap.put("shift", shift);  // 班次
        dataMap.put("fSeq", keyParts[7]);  // 工序
        dataMap.put("fTestCategory", keyParts[8]);  // 测试类别
        dataMap.put("fLine", keyParts[9]);  // 线体
        dataMap.put("fMac", keyParts[10]);  // 机台号
        dataMap.put("fTestType", keyParts[11]);  // 检验类型
        dataMap.put("fTestMan", keyParts[12]);  // 检验员

        // 统计数值
        int totalCheck = items.stream().mapToInt(DailyCheckSummary::getSpotCheckCount).sum();
        int okCount = items.stream().mapToInt(t -> t.getOkNum()).sum();
        int ngCount = items.stream().mapToInt(t -> t.getNgNum()).sum();

        dataMap.put("spotCheckCount", totalCheck);
        dataMap.put("okNum", okCount);
        dataMap.put("ngNum", ngCount);
        dataMap.put("okRate", totalCheck == 0 ? "0.00" : String.format("%.2f", okCount * 100.0 / totalCheck));
        dataMap.put("ngRate", totalCheck == 0 ? "0.00" : String.format("%.2f", ngCount * 100.0 / totalCheck));


        // 缺陷数据
        defects.forEach(defect -> {
            int count = items.stream()
                    .filter(t -> defect.trim().equals(t.getFSort()))
                    .mapToInt(t -> Integer.parseInt(t.getNum()))
                    .sum();
            dataMap.put(defect + "_count", count);
            dataMap.put(defect + "_rate", totalCheck == 0 ? "0.00" : String.format("%.2f", count * 100.0 / totalCheck));
        });

        result.add(dataMap);
    }




    /**
     * 将两个班次数据合并，求和各数值，同时计算良品率和不良率。
     */
    private Map<String, Object> combineData(Map<String, Object> data1, Map<String, Object> data2) {
        Map<String, Object> result = new HashMap<>();

        // 计算合并后的总数
        int spot1 = (Integer) data1.getOrDefault("spotCheckCount", 0);
        int spot2 = (Integer) data2.getOrDefault("spotCheckCount", 0);
        int spot = spot1 + spot2;

        int ok1 = (Integer) data1.getOrDefault("okNum", 0);
        int ok2 = (Integer) data2.getOrDefault("okNum", 0);
        int ok = ok1 + ok2;

        int ng1 = (Integer) data1.getOrDefault("ngNum", 0);
        int ng2 = (Integer) data2.getOrDefault("ngNum", 0);
        int ng = ng1 + ng2;

        // 计算良品率和不良率
        double okRate = spot == 0 ? 0 : (ok * 100.0 / spot);
        double ngRate = spot == 0 ? 0 : (ng * 100.0 / spot);

        // 将结果放入Map中
        result.put("spotCheckCount", spot);
        result.put("okNum", ok);
        result.put("ngNum", ng);
        result.put("okRate", String.format("%.2f", okRate));
        result.put("ngRate", String.format("%.2f", ngRate));  // 添加不良率

        // 合并缺陷数据（数值相加）
        for (String key : data1.keySet()) {
            if (key.startsWith("占比") || key.equals("spotCheckCount") || key.equals("okNum") || key.equals("ngNum") || key.equals("okRate") || key.equals("ngRate"))
                continue;
            if (data1.get(key) instanceof Integer) {
                int sum = (Integer) data1.getOrDefault(key, 0) + (Integer) data2.getOrDefault(key, 0);
                result.put(key, sum);
            }
        }

        // 文本字段采用data1中的
        result.put("fBigpro", data1.get("fBigpro"));
        result.put("fColor", data1.get("fColor"));
        result.put("fFac", data1.get("fFac"));
        result.put("fType", data1.get("fType"));
        result.put("fMac", data1.get("fMac"));
        result.put("fTestMan", data1.get("fTestMan"));
        result.put("confirmor", data1.get("confirmor"));
        result.put("statisticDate", data1.get("date") + " 07:00:00");
        result.put("endDate", data1.get("date") + " 23:00:00");
        result.put("dataType", "f1");
        return result;
    }

    private void storeDailyData(LocalDate date, String shift, String processName, Map<String, Object> dayData, List<String> defectList, String type) {
        // 插入主表
        String mainSql = "INSERT INTO daily_summary (" +
                "date, f_time , sort, f_bigpro, f_seq, f_fac, f_color, " +
                "f_test_category , f_test_man,f_stage,f_test_type,f_mac,f_type,f_line," +
                "spot_check_count, ok_num, ok_rate, ng_num, ng_rate," +
                "shift, statistic_type, statistic_date, end_date " +
                ",data_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(mainSql, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setDate(index++, Date.valueOf(date));
            ps.setTimestamp(index++, (Timestamp) dayData.get("fTime"));
            ps.setTimestamp(index++, (Timestamp) dayData.get("sort"));
            ps.setString(index++, (String) dayData.get("fBigpro"));
            ps.setString(index++, processName);
            ps.setString(index++, (String) dayData.get("fFac"));
            ps.setString(index++, (String) dayData.get("fColor"));
            ps.setString(index++, (String) dayData.get("fTestCategory"));
            ps.setString(index++, (String) dayData.get("fTestMan"));
            ps.setString(index++, (String) dayData.get("fStage"));
            ps.setString(index++, (String) dayData.get("fTestType"));
            ps.setString(index++, (String) dayData.get("fMac"));
            ps.setString(index++, (String) dayData.get("fType"));
            ps.setString(index++, (String) dayData.get("fLine"));
            Integer spotCheckCount = (Integer) dayData.get("spotCheckCount");
            ps.setInt(index++, (spotCheckCount != null) ? spotCheckCount : 0); // 如果为null，则设置为 0
            Integer okNum = (Integer) dayData.get("okNum");
            ps.setInt(index++, (okNum != null) ? okNum : 0); // 如果为null，则设置为 0


            String okRateString = (String) dayData.get("okRate");

            // 如果 okRate 为 null 或为空字符串，则设置为默认值 "0.0"
            if (okRateString == null || okRateString.trim().isEmpty()) {
                ps.setBigDecimal(index++, new BigDecimal("0.0"));  // 设置默认值 0.0
            } else {
                // 确保 okRateString 是有效的字符串，可以转换为 BigDecimal
                ps.setBigDecimal(index++, new BigDecimal(okRateString));
            }


            Integer ngNum = (Integer) dayData.get("ngNum");
            ps.setInt(index++, (ngNum != null) ? ngNum : 0); // 如果为null，则设置为 0

            String ngRateString = (String) dayData.get("ngRate");

            // 如果 okRate 为 null 或为空字符串，则设置为默认值 "0.0"
            if (ngRateString == null || ngRateString.trim().isEmpty()) {
                ps.setBigDecimal(index++, new BigDecimal("0.0"));  // 设置默认值 0.0
            } else {
                // 确保 ngRateString 是有效的字符串，可以转换为 BigDecimal
                ps.setBigDecimal(index++, new BigDecimal(ngRateString));
            }


            ps.setString(index++, shift);
            ps.setString(index++, "day");
            ps.setDate(index++, Date.valueOf(date));
            ps.setDate(index++, Date.valueOf(date));
            ps.setString(index++, type);
            return ps;
        }, keyHolder);

        // 获取主表ID
        Long dailyId = keyHolder.getKey().longValue();

        // 批量插入缺陷数据
        String defectSql = "INSERT INTO defect_summary (process_summary_id, defect_name, defect_count, defect_rate) " +
                "VALUES (?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (String defect : defectList) {
            String defectKey = defect.trim();
            Integer count = (Integer) dayData.get(defectKey + "_count");
            String rateStr = (String) dayData.get(defectKey + "_rate");
            double rate = 0.0;
            if (StringUtils.isNotEmpty(rateStr)) {
                String replace = rateStr.replace("%", "");
                rate = Double.parseDouble(replace);
            }


            if (count != null && count > 0) {
                batchArgs.add(new Object[]{dailyId, defectKey, count, rate});
            }
        }

        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(defectSql, batchArgs);
        }
    }

    private void storeAllDailyData(LocalDate date, String shift, String processName,
                                   List<Map<String, Object>> dailyDataList,
                                   List<String> defectList,String type,
                                   String statisticType,String periodLabel,LocalDate startDate,LocalDate endDate) {
        for (Map<String, Object> dayData : dailyDataList) {
            // 构造主表实体
            DailyCheckSummary summary = new DailyCheckSummary();
            summary.setDate(date);
            Timestamp timestamp = (Timestamp) dayData.get("fTime");
            summary.setFTime(timestamp != null ? timestamp.toLocalDateTime() : null);
            summary.setSort((Integer) dayData.get("sort"));
            summary.setFBigpro((String) dayData.get("fBigpro"));
            summary.setFSeq(processName);  // 工序
            summary.setFFac((String) dayData.get("fFac"));
            summary.setFColor((String) dayData.get("fColor"));
            summary.setFTestCategory((String) dayData.get("fTestCategory"));
            summary.setFTestMan((String) dayData.get("fTestMan"));
            summary.setFStage((String) dayData.get("fStage"));
            summary.setFTestType((String) dayData.get("fTestType"));
            summary.setFMac((String) dayData.get("fMac"));
            summary.setFType((String) dayData.get("fType"));
            summary.setFLine((String) dayData.get("fLine"));
            // 数量及比率处理，若为 null 则默认设置为 0 或 0.0
            Integer spotCheckCount = (Integer) dayData.get("spotCheckCount");
            summary.setSpotCheckCount(spotCheckCount != null ? spotCheckCount : 0);
            Integer okNum = (Integer) dayData.get("okNum");
            summary.setOkNum(okNum != null ? okNum : 0);
            String okRateString = (String) dayData.get("okRate");
            double okRate = StringUtils.isBlank(okRateString) ? 0.0 : Double.parseDouble(okRateString);
            summary.setOkRate(okRate);
            Integer ngNum = (Integer) dayData.get("ngNum");
            summary.setNgNum(ngNum != null ? ngNum : 0);
            String ngRateString = (String) dayData.get("ngRate");
            double ngRate = StringUtils.isBlank(ngRateString) ? 0.0 : Double.parseDouble(ngRateString);
            summary.setNgRate(ngRate);

            summary.setShift((String) dayData.get("shift"));
            summary.setTypeNum(periodLabel);
            summary.setStatisticType(statisticType);
            summary.setStatisticDate(startDate);
            summary.setEndDate(endDate);
            summary.setDataType(type);

            // 使用 MyBatis-Plus 的 save 方法插入记录
            dailyCheckSummaryService.save(summary);

            // 插入后 summary 对象中应包含生成的主键 ID
            Integer dailyId = summary.getId();

            // 构造并批量插入缺陷数据
            List<DefectSummary> defectSummaries = new ArrayList<>();
            for (String defect : defectList) {
                String defectKey = defect.trim();
                Integer count = (Integer) dayData.get(defectKey + "_count");
                String rateStr = (String) dayData.get(defectKey + "_rate");
                double rate = 0.0;
                if (StringUtils.isNotBlank(rateStr)) {
                    rate = Double.parseDouble(rateStr.replace("%", ""));
                }
                if (count != null && count > 0) {
                    DefectSummary ds = new DefectSummary();
                    ds.setProcessSummaryId(dailyId);
                    ds.setDefectName(defectKey);
                    ds.setDefectCount(count);
                    ds.setDefectRate(rate);
                    defectSummaries.add(ds);
                }
            }
            if (!defectSummaries.isEmpty()) {
                defectSummaryService.saveBatch(defectSummaries);
            }
        }
    }


}