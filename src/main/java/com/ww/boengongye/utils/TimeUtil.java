package com.ww.boengongye.utils;

import io.swagger.models.auth.In;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class TimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowTimeByNormal() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }

    /**
     * 获取当前时间,yyyy-MM-dd
     *
     * @return
     */
    public static String getNowTimeNoHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now);
    }

    /**
     * 获取当前时间,yyyyMMdd
     *
     * @return
     */
    public static String getNowTimeNoHour2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        return sdf.format(now);
    }

    /**
     * 获取当前月yyyy-MM
     *
     * @return
     */
    public static String getNowMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date now = new Date();
        return sdf.format(now);
    }

    /**
     * 获取当前月yyyyMM
     *
     * @return
     */
    public static String getNowMonth2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date now = new Date();
        return sdf.format(now);
    }

    public static String getFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first + " 07:00:00";
    }

    /**
     * 上周第一天7点
     * @return
     */
    public static String getLastweekFirstDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        c.add(Calendar.DATE,-7);
        String first = format.format(c.getTime());

        return first + " 07:00:00";
    }

    /**
     * 本周第一天7点
     * @return
     */
    public static String getThisweekFirstDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String first = format.format(c.getTime());
        return first + " 07:00:00";
    }

    /**
     * 上周最后时间
     * @return
     */
    public static String getLastWeekEndTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        c.add(Calendar.DATE,-1);
        String first = format.format(c.getTime());
        return first + " 07:00:00";
    }



    public static String getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1 + "";
    }

    public static String getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH) + "";
    }


    /**
     * 获取昨天 格式:yyyy-MM-dd
     */
    public static String getYesterday() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String last = df.format(calendar.getTime());
        return last;
    }

    /**
     * 获取之前日期
     * @param day 减去的天数
     * @return
     */

    public static String getBeforeDay(int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        String last = df.format(calendar.getTime());
        return last;
    }
    /**
     * 获取之前日期
     * @param day 减去的天数
     * @return
     */

    public static String getBeforeDayMMdd(int day) {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        String last = df.format(calendar.getTime());
        return last;
    }
    /**
     * 获取昨天 格式:MM-dd
     */
    public static String getYesterdayNoYear() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String last = df.format(calendar.getTime());
        return last;
    }
    /**
     * 获取某个时间前的时间 格式:yyyy-MM-dd
     */
    public static String getBeforeTime(int hour) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hour);
        String last = df.format(calendar.getTime());
        return last;
    }

    /**
     * 在当前时间增加时间 格式:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getValidTime(int minute) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        String last = df.format(calendar.getTime());
        return calendar.getTime();
    }


    /**
     * 获取当前时间前一小时的时间
     * @param date
     * @return java.util.Date
     */
    public static String beforeOneHourToNowDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        return df.format(calendar.getTime());
    }

    /**
     * 获取当前时间后一小时的时间
     * @param date
     * @return java.util.Date
     */
    public static String afterOneHourToNowDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return df.format(calendar.getTime());
    }

    /**
     * 获取当前时间后一小时的时间 且0分0秒
     * @param date
     * @return java.util.Date
     */
    public static String afterOneHour0_0ToNowDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return df.format(calendar.getTime()).substring(0,14) + "00:00";
    }


    /**
     * 根据年,月获取该月开始时间
     */
    public static String getFirstDayByMonthAndYear(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, 1);
        String time = format.format(ca.getTime());
        return time + " 00:00:00";
    }

    /**
     * 根据年,月获取该月接收时间
     */
    public static String getLastDayByMonthAndYear(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, 1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String time = format.format(ca.getTime());
        return time + " 23:59:59";
    }

    /**
     * 获取上个月的第一天
     *
     * @return
     */
    public static String getBeforMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first + " 00:00:00";
    }

    /**
     * 获取上个月的最后一天
     *
     * @return
     */
    public static String getBeforMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, -1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last + " 23:59:59";
    }


    /**
     * 获取上个月yyyy-MM
     *
     * @return yyyy-MM
     */
    public static String getBeforMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * 转换时间格式从yyyyMMddHHmmss转成yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String changeTimeFormat(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = oldFormat.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {

            logger.error("转换时间格式从yyyyMMddHHmmss转成yyyy-MM-dd HH:mm:ss", e);
        }
        return "";
    }


    /**
     * 流程信息使用的时间转换yyyyMMddHHmmssSSS
     *
     * @param time
     * @return
     */
    public static String changeTimeFormatByExp302(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssSSS");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            Date date = oldFormat.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {
            logger.error("流程信息使用的时间转换yyyyMMddHHmmssSSS", e);
        }
        return "";
    }

    public static String changeTimeFormat2(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date date = oldFormat.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {
            logger.error("changeTimeFormat2", e);
        }
        return "";
    }

    public static String changeTimeFormat3(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = oldFormat.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {
            logger.error("changeTimeFormat3", e);
        }
        return "";
    }

    /**
     * 转换时间格式从yyyy-MM-dd HH:mm:ss转成yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String changeTimeFormat4(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = oldFormat.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {
            logger.error("转换时间格式从yyyy-MM-dd HH:mm:ss转成yyyy-MM-dd", e);
        }
        return "";
    }

    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
        int month = Integer.parseInt(yearMonth.split("-")[1]); //月
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        // cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.MONTH, month); //设置当前月的上一个月
        // 获取某月最大天数
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        // 设置日历中月份的最大天数
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前时间加长
     *
     * @return
     */
    public static String getNowTimeLong() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
        Date now = new Date();
        return sdf.format(now);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusDay(int num, String newDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(newDate);
        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }



    /**
     * 获取单双月0双1单
     *

     * @return
     * @throws ParseException
     */
    public static Integer getBimonthly()  {
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;

        return month%2;
    }

    /**
     * 获取白班0 晚班1
     * @return
     * @throws ParseException
     */
    public static Integer getDayShift()  {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY) ;
        System.out.println(hour);
        if(hour>=19||hour<=7){
            return 1;
        }else{
            return 0;
        }

    }





    /**
     * 获取双月0 单月1
     * @return
     * @throws ParseException
     */
    public static Integer getMonthShift()  {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY) ;
        int month = calendar.get(Calendar.MONTH) ;
        System.out.println(hour);
        System.out.println(month);
        int ys=month%2;
        System.out.println(ys);
        if(ys==1){
            if(hour>=19||hour<=7){
                return 0;
            }else{
                return 1;
            }

        }else{
            if(hour>=19||hour<=7){
                return 1;
            }else{
                return 0;
            }

        }

    }

    /**
     * 转换Timestamp格式 成yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static String formatTimestamp(Timestamp time){
        // 创建一个SimpleDateFormat对象来定义日期时间的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 因为SimpleDateFormat的format方法需要Date对象，所以我们需要将Timestamp转换为Date
        // Timestamp是Date的子类，所以可以直接使用强制类型转换
        Date date = new Date(time.getTime());

        // 使用SimpleDateFormat的format方法将Date对象转换为字符串
        String formattedDate = sdf.format(date);
        return  formattedDate;
    }





    /**
     * 复制文件夹
     *
     * @param resource 源路径
     * @param target   目标路径
     */
    public static void copyFolder(String resource, String target) throws Exception {

        File resourceFile = new File(resource);
        if (!resourceFile.exists()) {
            throw new Exception("源目标路径：[" + resource + "] 不存在...");
        }
//        System.out.println("target:"+target);
        File targetFile = new File(target);
        targetFile.mkdirs();
//        System.out.println("targetffff:"+targetFile.getPath());
        if (!targetFile.exists()) {
            throw new Exception("存放的目标路径：[" + target + "] 不存在...");
        } else {

        }

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();
        int i = 0;
        for (File file : resourceFiles) {

//        		System.out.println("fileNAME:"+file.getPath());
            File file1 = new File("D:\\ww工作资料\\桌面\\测试报文\\系统生成的测试报文第四十六批" + File.separator + resourceFile.getName());
//                System.out.println("文件夹" + file1.getPath());
//                System.out.println("文件夹" + targetFile.getAbsolutePath());
//                System.out.println("文件夹" + File.separator);
//                System.out.println("文件夹" + resourceFile.getName());
            // 复制文件
            if (file.isFile()) {
//                    System.out.println("文件" + file.getName());
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                copyFile(file, targetFile1);
            }
            // 复制文件夹
            if (file.isDirectory()) {// 复制源文件夹
                String dir1 = file.getAbsolutePath();
                // 目的文件夹
                String dir2 = file1.getAbsolutePath();
                copyFolder(dir1, dir2);
            }


        }

    }

    /*
    计算两个日期之间相差的天数
     */
    public static int between_days(String a, String b) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 自定义时间格式

        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();

        Date date_a = null;
        Date date_b = null;

        try {
            date_a = simpleDateFormat.parse(a);//字符串转Date
            date_b = simpleDateFormat.parse(b);
            calendar_a.setTime(date_a);// 设置日历
            calendar_b.setTime(date_b);
        } catch (ParseException e) {
            e.printStackTrace();//格式化异常
        }

        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();

        long between_days = (time_b - time_a) / (1000 * 3600 * 24);//计算相差天数
        Long data = between_days;
        return data.intValue();
    }


    /*
   计算两个日期之间相差的分钟
    */
    public static int between_minute(String a, String b) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 自定义时间格式

        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();

        Date date_a = null;
        Date date_b = null;

        try {
            date_a = simpleDateFormat.parse(a);//字符串转Date
            date_b = simpleDateFormat.parse(b);
            calendar_a.setTime(date_a);// 设置日历
            calendar_b.setTime(date_b);
        } catch (ParseException e) {
            e.printStackTrace();//格式化异常
        }

        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();

        long between_days = (time_b - time_a) / (1000 * 60 );//计算相差分钟数
        Long data = between_days;
        return data.intValue();
    }

    /**
     * 复制文件
     *
     * @param resource
     * @param target
     */
    public static void copyFile(File resource, File target) throws IOException {
        // 输入流 --> 从一个目标读取数据
        // 输出流 --> 向一个目标写入数据


        // 文件输入流并进行缓冲
        FileInputStream inputStream = new FileInputStream(resource);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // 文件输出流并进行缓冲
        FileOutputStream outputStream = new FileOutputStream(target);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        // 缓冲数组
        // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
        byte[] bytes = new byte[1024 * 2];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        // 刷新输出缓冲流
        bufferedOutputStream.flush();
        //关闭流
        bufferedInputStream.close();
        bufferedOutputStream.close();
        inputStream.close();
        outputStream.close();

        long end = System.currentTimeMillis();

//        System.out.println("耗时：" + (end - start) / 1000 + " s");

    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 通过时间获取该月的天数，并返回天的字符串数组 1日，2日
     * 类型为 yyyy-MM
     *
     */
    public static List<String> theMonthDays(String theMonth) {
        Integer year  = Integer.parseInt(theMonth.substring(0,4));   // 获取年份
        Integer month = Integer.parseInt(theMonth.substring(5));     // 获取月份
        Integer days = 0;   //该月天数
        List<String> returnDay = new ArrayList<>();
        if ((year%4 == 0 && year%100 != 0) || year%400 == 0){
            if (month == 2){
                days = 29;
            }
        }else {
            if (month == 2){
                days = 28;
            }
        }
        if (month==1 ||month==3 ||month==5 ||month==7 ||month==8 ||month==10 ||month==12){
            days = 31;
        }else if (month==4 ||month==6 ||month==9 ||month==11){
            days = 30;
        }
        for (int i = 1; i <= days; i++) {
            returnDay.add(i + "日");
        }
        return returnDay;
    }

    // 获取某月的上个月
    public static String getLastMonth(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        if (month > 1){
            month--;
        }else {
            month = 12;
            year--;
        }
        if (month >= 10){
            return year + "-" + month;
        }else{
            return year + "-0" + month;
        }
    }

    // 获取某月的下个月
    public static String getNextMonth(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        if (month < 12){
            month++;
        }else {
            month = 1;
            year++;
        }
        if (month >= 10){
            return year + "-" + month;
        }else{
            return year + "-0" + month;
        }
    }

    // 获取某一天的第二天
    public static String getNextDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(date);
        long time = parse.getTime();
        long addTime = 1 * 24 * 60 * 60 * 1000;
        Date date1 = new Date(time + addTime);
        String result = format.format(date1);
        return result;
    }

    // 获取某一天的上一天
    public static String getLastDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(date);
        long time = parse.getTime();
        long addTime = -1 * 24 * 60 * 60 * 1000;
        Date date1 = new Date(time + addTime);
        String result = format.format(date1);
        return result;
    }

    // 获取某一天的前几天
    public static String getLastSomeDay(String date, int day_num) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(date);
        long time = parse.getTime();
        long addTime = -1 * 24 * 60 * 60 * 1000 * day_num;
        Date date1 = new Date(time + addTime);
        String result = format.format(date1);
        return result;
    }

    /**
     * 获取某个时间的 n 天后的时间
     *
     * @param dateTimeStr   指定的时间字符串（格式如：2024-12-02 07:00:00）
     * @param n             n 天后的时间（n 为负数时获取前 n 天）
     * @param pattern       时间格式（默认格式为：yyyy-MM-dd HH:mm:ss）
     * @return              n 天后的时间字符串
     */
    public static String getOffsetDate(String dateTimeStr, int n, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        LocalDateTime offsetDate = dateTime.plusDays(n);
        return offsetDate.format(formatter);
    }

    // 获取上一年该月
    public static String getLastYearMonth(String date){
        String substring = date.substring(0, 4);
        String month = date.substring(5, 7);
        int year = Integer.parseInt(substring);
        year--;
        return year + "-" + month;
    }

    // 获取某月的天数
    public static long howManyDayOfTheMonth(String date) throws ParseException {
        String nextMonth = getNextMonth(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date1 = format.parse(date);
        Date date2 = format.parse(nextMonth);
        Long gap = date2.getTime() - date1.getTime();
        return gap / 1000 / 60 / 60 / 24;
    }

    // 获取某月的天数
    public static long howManyDayOfTheYear(String date) throws ParseException {
        String nextYear = (Integer.parseInt(date) + 1) + "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date1 = format.parse(date);
        Date date2 = format.parse(nextYear);
        Long gap = date2.getTime() - date1.getTime();
        return gap / 1000 / 60 / 60 / 24;
    }



    public static String timeStamp2Date(String seconds) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    public static Date timeStamp2Date3(String seconds) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return null;
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return new Date(Long.valueOf(seconds+"000"));
    }

    /**
     * 根据一段时间获取该时间段内所有日期 正序排序
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getTwoDaysDayDes(String startDate , String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<String>();
        try {
            Date dateOne = sdf.parse(startDate);
            Date dateTwo = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOne);
            dateList.add(startDate);
            while (dateTwo.after(calendar.getTime())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dateList.add(sdf.format(calendar.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateList;
    }

    /**
     * 获取日期所在年份第几周
     * @param date 日期
     * @return
     */
    public static String getWeekOfYear(String date){
        LocalDate localDate = LocalDate.parse(date);
//        第一个参数：一周的第一天，不能为空
//        第二个参数：第一周的最小天数，从1到7
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        int weekOfYear = localDate.get(weekFields.weekOfYear());
        return weekOfYear+"";
    }

    /**
     * 根据给定的日期时间字符串和天数，返回提前减去天数后的日期时间字符串，格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param time 日期时间字符串，格式为 yyyy-MM-dd HH:mm:ss
     * @param day  需要提前减去的天数
     * @return 新的日期时间字符串
     */
    public static String subtractDays(String time, int day) {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析传入的日期时间字符串为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);

        // 执行日期时间减去天数操作
        LocalDateTime resultDateTime = localDateTime.minusDays(day);

        // 返回结果日期时间的字符串
        return resultDateTime.format(formatter);
    }

    public static void main(String[] args) throws ParseException {

        //System.out.println();
        /*System.out.println(changeTimeFormatByExp302("20210119211320382"));

        String strrr = "HT2021040002";
        System.out.println(strrr.substring(2, 8));

        System.out.println(strrr.substring(2, 8).equals(getNowMonth2()));

        int e = 23166354;
        System.out.println(e / 1000000.0);

        String a = "2017-12-01"; // 时间字符串
        String b = "2017-12-03 21:13:20";

//		Long between_dayInteger = between_days(a, b);


//        System.out.println(plusDay(10, b));
        System.out.println(getBeforeTime(-1));*/

        System.out.println(between_minute("2023-08-09 10:15:00","2023-08-09 10:25:00"));



        System.out.println(subtractDays("2024-12-11 11:22:33",5));

//        try {
//            // 创建BufferedReader对象，FileReader用于读取文件内容
//            reader = new BufferedReader(new FileReader(filePath));
//            String line;
//            // 使用readLine()方法逐行读取文件内容
//            while ((line = reader.readLine()) != null) {
//                // 对每行内容进行处理，这里只是简单地打印出来
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            // 捕获并处理IOException异常
//            e.printStackTrace();
//        } finally {
//            // 最后确保BufferedReader被关闭，以释放资源
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }


        System.out.println(getBeforeDay(0));


    }


    /**
     * 通过玻璃条码获取玻璃的生产时间
     * @param pieceName
     * @return
     */
    public static String getProductTimeByPieceName(String pieceName){
//        String pieceName = "G0MGVC0063E00006T9+9+0707516570012004912953319J";
        String keywords = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
        String str4 = pieceName.substring(3,4);
        String str5 = pieceName.substring(4,5);
        String str6 = pieceName.substring(5,6);

        Integer str4Index = keywords.indexOf(str4);
        Integer str5Index = keywords.indexOf(str5);
        Integer str6Index = keywords.indexOf(str6);

        Double str4Double = str4Index*(Math.pow(34,2));
        Double str5Double = str5Index*(Math.pow(34,1));
        Double str6Double = str6Index*(Math.pow(34,0));

        //时间序列号
        Double timeDouble = 25569+str4Double+str5Double+str6Double;

        // 将时间序列号转换为Java中的Date对象
        Date timeDate = DateUtil.getJavaDate(timeDouble);
        // 格式化Date对象为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time  = sdf.format(timeDate);

        return time;
    }

    /**
     * 获取时间所在年份第几周
     * @param timeString 日期
     * @return
     */
    public static Integer getWeekByTimeStr(String timeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(timeString, formatter);
        int weekOfYear = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        return weekOfYear;
    }

    /**
     * 计算两个时间之间相差的秒数
     * @param time1
     * @param time2
     * @return
     */
    public static Integer getSecondsDifferenceInt(String time1,String time2) {

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 将字符串解析为 LocalDateTime 对象
        LocalDateTime dateTime1 = LocalDateTime.parse(time1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(time2, formatter);

        // 计算两个日期时间之间的秒数差
        Duration duration = Duration.between(dateTime1, dateTime2);
        long secondsDifference = duration.getSeconds();

        // 将长整型的秒数差转换为整数类型
        Integer secondsDifferenceInt = (int) secondsDifference;

        return secondsDifferenceInt;
    }

    /**
     * 获取当前整点时间（分秒为0）
     * @return
     */
    public static String getNowTimeHour(){
        // 获取当前时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 设置整点时间
        LocalDateTime roundedDateTime = currentDateTime.withMinute(0).withSecond(0).withNano(0);

        // 设置格式为 "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = roundedDateTime.format(formatter);

        return formattedDate;
    }

    /**
     * 获取输入时间减去hour个小时后的时间
     * @param time
     * @param hour
     * @return
     */
    public static String getTimeAfterSubHour(String time,int hour){

        // 将字符串解析为 LocalDateTime 对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inputDateTime = LocalDateTime.parse(time, formatter);

        // 减去一个小时
        LocalDateTime resultDateTime = inputDateTime.minusHours(hour);

        // 将结果格式化为字符串
        String resultDateString = resultDateTime.format(formatter);

        return resultDateString;
    }

    /**
     * 获取当天整点时间（0分秒）
     * @return
     */
    public static String getTodayHour(){
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 获取当天的0点时间
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);

        // 使用DateTimeFormatter格式化为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String midnightString = midnight.format(formatter);

        return midnightString;
    }

    /**
     * 分钟转HH:mm:ss
     * @param minutes
     * @return
     */
    public static String convertMinutesToTime(double minutes) {
        // 检查负数并记录是否为负
        boolean isNegative = minutes < 0;
        minutes = Math.abs(minutes);  // 将分钟转为正数进行计算

        // 计算小时、分钟和秒
        int hours = (int) (minutes / 60);
        int remainingMinutes = (int) (minutes % 60);
        int seconds = (int) ((minutes - (int) minutes) * 60);

        // 格式化为 HH:mm:ss
        String time = String.format("%02d:%02d:%02d", hours, remainingMinutes, seconds);

        // 如果是负数，添加前缀 '-'
        if (isNegative) {
            return "-" + time;
        }

        return time;
    }


    /**
     * 获取上个时间直接相差的秒数
     * @param datetime1
     * @param datetime2
     * @return
     */
    public static long getTimeDifferenceInSeconds(String datetime1, String datetime2) {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析字符串为 LocalDateTime 对象
        LocalDateTime dt1 = LocalDateTime.parse(datetime1, formatter);
        LocalDateTime dt2 = LocalDateTime.parse(datetime2, formatter);

        // 计算两个时间的秒数差
        return ChronoUnit.SECONDS.between(dt1, dt2);
    }

    /**
     *     转换时间 yyyy-MM-dd HH:mm:ss 成HH:mm:ss
     */
    public static String convertToTimeOnly(Timestamp timestamp) {
        // 将 Timestamp 转换为 LocalDateTime
        LocalDateTime dateTime = timestamp.toLocalDateTime();

        // 定义输出时间的格式（HH:mm:ss）
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 格式化 LocalDateTime 为只包含时间部分的字符串
        return dateTime.format(outputFormatter);
    }

    /**
     * 获取当前时间往前一个区间的随机时间
     * @param startMinutes
     * @param endMinutes
     * @return
     */
    public static String getRandomTimeInRange(int startMinutes, int endMinutes) {
        // 获取当前时间
        Calendar now = Calendar.getInstance();

        // 随机生成一个在区间内的分钟数
        int randomMinutes = startMinutes + (int) (Math.random() * (endMinutes - startMinutes + 1));

        // 从当前时间往前推该分钟数
        now.add(Calendar.MINUTE, -randomMinutes);

        // 定义时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 返回格式化后的时间
        return sdf.format(now.getTime());
    }

    /**
     * 获取某一时间的前minute1到minute2分钟内的随机时间
     * @param inputTime
     * @param minute1
     * @param minute2
     * @return
     */
    public static String getRandomTimeInRange(String inputTime,int minute1,int minute2) throws ParseException {
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析字符串为 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(inputTime, formatter);

        // 随机生成一个在区间内的分钟数
        int randomMinutes = minute1 + (int) (Math.random() * (minute2 - minute1 + 1));

        // 减去随机分钟数
        LocalDateTime newDateTime = dateTime.minusMinutes(randomMinutes);

        // 格式化为字符串
        String newTimeString = newDateTime.format(formatter);

        return newTimeString;
    }
}
