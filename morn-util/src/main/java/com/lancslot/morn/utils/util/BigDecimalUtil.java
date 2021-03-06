package com.lancslot.morn.utils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalUtil {
    private static final Logger logger = LoggerFactory.getLogger(BigDecimalUtil.class);

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Object value1, Object value2) {
        if (value1 == null) {
            value1 = "0";
        }
        if (value2 == null) {
            value2 = "0";
        }

        BigDecimal b1 = new BigDecimal(Double.valueOf(value1.toString()));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2.toString()));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(String value1, String value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static BigDecimal divide(Object value1, Object value2, int scale) {
        Object divide = divide(value1, value2, scale, BigDecimal.ZERO, BigDecimal.ROUND_HALF_UP);
        return (BigDecimal) divide;
    }

    public static Object divide(Object value1, Object value2, int scale, Object defaultValue) {
        Object divide = divide(value1, value2, scale, defaultValue, BigDecimal.ROUND_HALF_UP);
        return divide;
    }

    /**
     * 除法。接收默认值，和指定四舍五入模式
     *
     * @param value1
     * @param value2
     * @param scale
     * @param defaultValue
     * @param roundingMode
     * @return
     */
    public static Object divide(Object value1, Object value2, int scale, Object defaultValue, Integer roundingMode) {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            scale = 2;
        }
        if (value1 == null || value2 == null || value2.equals("0")) {
            return defaultValue != null ? defaultValue : BigDecimal.ZERO;
        }
        try {
            BigDecimal b1 = new BigDecimal(value1.toString());
            BigDecimal b2 = new BigDecimal(value2.toString());
            return b1.divide(b2, scale, roundingMode == null ? BigDecimal.ROUND_HALF_UP : roundingMode);
        } catch (Exception e) {
            logger.warn("{},{},{}", value1, value2, e.getMessage());
        }
        return defaultValue != null ? defaultValue : BigDecimal.ZERO;
    }

    public static void main(String[] args) {
        String cv = "0.4533";
        System.out.println(toPercent(cv,4));
    }

    public static BigDecimal toPercent(Object growth, int i) {
        MathContext mc = new MathContext(i);
        BigDecimal b1 = new BigDecimal(growth.toString());

        BigDecimal multiply = b1.multiply(new BigDecimal(100), mc);
        return multiply;
    }

}
