package com.ww.boengongye.utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 计算类
 */
public class MathUtils {

    private static void intervalFrequency(int[] interval, double increment, double min, List<Double> data) {  // 区间频次 直方图
        for (double datum : data) {
            int v = (int)((datum - min) / increment);
            v = v < 0 ? 0 : Math.min(v, interval.length - 1);
            interval[v]++;
        }
    }

    /**
     * 获取正态分布图
     * @param data
     * @param mean
     * @param stdDev
     * @param mapData
     */
    public static void normalDistribution(List<Double> data,Double mean,Double stdDev, Map<String, Object> mapData) {  // 将xy轴和密度曲线数据放到数组中
        double min = (double)mapData.get("lsl");
        double max = (double)mapData.get("usl");
        NormalDistribution normalDist = new NormalDistribution(mean, stdDev);
        int groupNum = 20; // 组数
        double[] xValues = new double[groupNum];
        int[] yValues = new int[groupNum];
        double[] normalData = new double[groupNum];
        double increment = (max - min) / groupNum;
        double current = min;
        for (int i = 0; i < groupNum; i++) {  // 正态分布图的密度曲线
            normalData[i] = normalDist.density(current); // generate the normal distribution of each data point
            xValues[i] = current;
            current += increment;
        }

        intervalFrequency(yValues, increment, min, data);  // 获取直方图
        mapData.put("x", xValues);
        mapData.put("y1", yValues);
        mapData.put("y2", normalData);
    }

    /**
     * 计算集合的平均值
     * @param data
     * @return
     */
    public static double calculateMean(List<Double> data) {
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * 计算集合的方差
     * @param data
     * @param mean
     * @return
     */
    public static double calculateVariance(List<Double> data, double mean) {
        return data.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .average()
                .orElse(0.0);
    }

    /**
     * 计算标准差
     * @param variance
     * @return
     */
    public static double calculateStandardDeviation(double variance) {
        return Math.sqrt(variance);
    }

    /**
     * 计算 CPK 值
     * @param usl
     * @param lsl
     * @param mean
     * @param stdDev
     * @return
     */
    public static double calculateCPK(double usl, double lsl, double mean, double stdDev) {
        double cpkUpper = (usl - mean) / (3 * stdDev);
        double cpkLower = (mean - lsl) / (3 * stdDev);
        return Math.min(cpkUpper, cpkLower);
    }
}
