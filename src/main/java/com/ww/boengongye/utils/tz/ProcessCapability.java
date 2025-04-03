package com.ww.boengongye.utils.tz;

public class ProcessCapability {
    /**
     * 计算 Cpk 值
     *
     * @param usl 规格上限 (USL)
     * @param lsl 规格下限 (LSL)
     * @param mean 样本均值 (X̄)
     * @param stdDev 样本标准差 (σ)
     * @return Cpk 值
     */
    public static double calculateCpk(double usl, double lsl, double mean, double stdDev) {
        // 如果标准差为 0，无法计算 Cpk
        if (stdDev == 0) {
            throw new IllegalArgumentException("Standard deviation (σ) cannot be zero.");
        }

        // 计算两个分量
        double cpu = (usl - mean) / (3 * stdDev); // 上规格能力
        double cpl = (mean - lsl) / (3 * stdDev); // 下规格能力

        // 返回最小值
        return Math.min(cpu, cpl);
    }

    public static void main(String[] args) {
        // 示例数据
        double usl = 10.0;   // 规格上限
        double lsl = 5.0;    // 规格下限
        double mean = 7.5;   // 样本均值
        double stdDev = 0.5; // 样本标准差

        // 计算 Cpk
        double cpk = calculateCpk(usl, lsl, mean, stdDev);

        // 打印结果
        System.out.println("Cpk 值为: " + cpk);
    }
}
