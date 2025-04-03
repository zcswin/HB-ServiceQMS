package com.ww.boengongye.utils.tz;

public class MeanCalculator {
    /**
     * 计算均值 (μ)
     *
     * @param data 样本数据数组
     * @return 均值 μ
     */
    public static double calculateMean(double[] data) {
        // 检查数组是否为空或无元素
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data array cannot be null or empty.");
        }

        // 求和
        double sum = 0.0;
        for (double x_n : data) { // 遍历每个样本值 x_n (保持 n 的语义不变)
            sum += x_n;
        }

        // 计算均值 μ
        return sum / data.length;
    }

    public static void main(String[] args) {
        // 示例数据
        double[] data = {1.5, 2.3, 3.7, 4.0, 5.8}; // 样本数据

        // 计算均值
        double mean = calculateMean(data);

        // 打印结果
        System.out.println("均值 μ 为: " + mean);
    }
}
