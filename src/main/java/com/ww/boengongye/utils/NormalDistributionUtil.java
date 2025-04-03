package com.ww.boengongye.utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Arrays;
import java.util.Map;

public class NormalDistributionUtil {

    public static void normalDistribution2(double[] data, Map<String, Object> mapData) {  // 将xy轴和密度曲线数据放到数组中
        //double[] data = {}; // your array of data
        if (data.length == 0) return;
        Arrays.sort(data);
        if (data.length > 10) {
            double[] data2 = Arrays.copyOfRange(data, 5, data.length - 5);
            data = data2;
        }

        double min = (double)mapData.get("lsl");
        double max = (double)mapData.get("usl");
        //double min = Math.min(data[0], (double)mapData.get("lsl"));  // 以数据和上下限的最大最小
        //double max = Math.max(data[data.length-1], (double)mapData.get("usl"));
        //double min = (double)mapData.get("lsl");  // 以上下限为最大最小
        //double max = (double)mapData.get("usl");
        double mean = calculateMean(data); // calculate the mean of the data 均值
        double stdDev = calculateStdDev(data, mean); // calculate the standard deviation of the data 标准差
        if (stdDev == 0) {
            return;
        }
        NormalDistribution normalDist = new NormalDistribution(mean, stdDev); // create a normal distribution object
        int groupNum = 20; // 组数
        Double[] xValues = new Double[groupNum];
        Integer[] yValues = new Integer[groupNum];
        Double[] normalData = new Double[groupNum];
        //double increment = (data[data.length-1] - data[0]) / groupNum;
        //double increment = ((double)mapData.get("usl") - (double)mapData.get("lsl")) / groupNum;
        double increment = (max - min) / groupNum;
        //double current = data[0];
        //double current = (double)mapData.get("lsl");
        double current = min;
        for (int i = 0; i < groupNum; i++) {  // 正态分布图的密度曲线
            normalData[i] = normalDist.density(current); // generate the normal distribution of each data point
            xValues[i] = current;
            current += increment;
        }
        //intervalFrequency(yValues, increment, data[0], data);  // 获取直方图
        //intervalFrequency(yValues, increment, (double)mapData.get("lsl"), data);  // 获取直方图
        intervalFrequency(yValues, increment, min, data);  // 获取直方图
        mapData.put("x", Arrays.asList(xValues));
        mapData.put("y1", Arrays.asList(yValues));
        mapData.put("y2", Arrays.asList(normalData));
    }

    // helper method to calculate the mean of an array of data
    private static double calculateMean(double[] data) {
        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.length;
    }

    // helper method to calculate the standard deviation of an array of data
    private static double calculateStdDev(double[] data, double mean) {
        double sum = 0.0;
        for (double d : data) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / data.length);
    }

    public static double[] convertToDoubleArray(Object[] objArray) {
        double[] doubleArray = new double[objArray.length]; // create a new double array with the same length as the input Object array
        for (int i = 0; i < objArray.length; i++) {
            doubleArray[i] = (double) objArray[i]; // cast each Object element to double and add it to the new double array
        }
        return doubleArray; // return the new double array
    }

    private static void intervalFrequency(Integer[] interval, double increment, double min, double[] data) {  // 区间频次 直方图
        for (double datum : data) {
            int v = (int)((datum - min) / increment);
            v = v < 0 ? 0 : Math.min(v, interval.length - 1);
            interval[v] = (interval[v] == null ? 0 : interval[v]) + 1;
        }
    }
}
