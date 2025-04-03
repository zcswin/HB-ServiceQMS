package com.ww.boengongye.utils.tz;

public class VarianceCalculator {
    /**
     * 计算方差 (σ²)
     *
     * @param data 样本数据数组
     * @return 方差 σ²
     */
    public static double calculateVariance(double[] data) {
        // 检查数组是否为空或无元素
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("数据数组不能为空或为空！");
        }

        int N = data.length; // 样本总数

        // 计算均值 μ
        double sum = 0.0;
        for (double x_i : data) {
            sum += x_i;
        }
        double mean = sum / N;

        // 计算方差 σ²
        double varianceSum = 0.0;
        for (double x_i : data) {
            varianceSum += Math.pow(x_i - mean, 2); // (x_i - μ)²
        }

        return varianceSum / N; // σ² = 1/N * Σ(x_i - μ)²
    }

    public static void main(String[] args) {
        // 示例数据
        double[] data = {2, 4, 6, 8, 10}; // 样本数据

        // 计算方差
        double variance = calculateVariance(data);

        // 打印结果
        System.out.println("方差 σ² 为: " + variance);
    }
}
