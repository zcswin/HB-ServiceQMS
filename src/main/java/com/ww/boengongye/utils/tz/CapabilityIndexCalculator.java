package com.ww.boengongye.utils.tz;

public class CapabilityIndexCalculator {

    /**
     * 计算能力偏移指数 (Ca)
     *
     * @param xBar 样本均值 (X̄)
     * @param mu 目标值 (μ)
     * @param usl 规格上限 (USL)
     * @param lsl 规格下限 (LSL)
     * @return 能力偏移指数 (Ca)
     */
    public static double calculateCa(double xBar, double mu, double usl, double lsl) {
        if (usl <= lsl) {
            throw new IllegalArgumentException("规格上限 (USL) 必须大于规格下限 (LSL)");
        }

        double denominator = (usl - lsl) / 2.0; // 分母: (USL - LSL) / 2
        return (xBar - mu) / denominator;      // 计算 Ca
    }

    public static void main(String[] args) {
        // 示例数据
        double xBar = 50.0; // 样本均值
        double mu = 48.0;   // 目标值
        double usl = 60.0;  // 规格上限
        double lsl = 40.0;  // 规格下限

        // 计算能力偏移指数
        double ca = calculateCa(xBar, mu, usl, lsl);

        // 输出结果
        System.out.println("能力偏移指数 (Ca): " + ca);
    }
}
