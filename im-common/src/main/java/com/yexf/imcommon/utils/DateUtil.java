package com.yexf.imcommon.utils;


import com.yexf.imcommon.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMdd = "yyyyMMdd";

    /**
     * 解析时间字符串为Date
     *
     * @param time   时间字符串
     * @param format 解析字符串
     */
    public static Date parse(String time, String format) {
        if (StringUtils.isBlank(time) || StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("date parse failed");
        }
    }


    /**
     * 指定日期加上 num天
     *
     * @param date 日期
     * @param num  加的天数
     */
    public static Date addDays(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return cal.getTime();
    }

    /**
     * 指定日期加上 num小时
     *
     * @param date 日期
     * @param num  加的小时数
     */
    public static Date addHours(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, num);
        return calendar.getTime();
    }

    /**
     * 指定日期加上 num分钟
     *
     * @param date 日期
     * @param num  加的分钟数
     */
    public static Date addMinute(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, num);
        return calendar.getTime();
    }


    /**
     * 格式化时间
     *
     * @param date   传入时间
     * @param format 格式化字符串
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * UTC时间转为美东时间，区分夏令时
     */
    public static Date utc2NewYork(Date date) {
        boolean isDaylightOffset = isDaylightOffset(date.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isDaylightOffset) {
            calendar.add(Calendar.HOUR, -4);
        } else {
            calendar.add(Calendar.HOUR, -5);
        }
        return calendar.getTime();
    }


    /**
     * 判断是否是夏令时
     * 关于美东时间
     * 地区：美国 纽约
     * 时区：UTC/GMT -5.00 (西五区)
     * 夏令时：美国东部标准时间正处于夏令时中，时区+1，与北京时间时差-1
     * 夏令时开始时间：2022-3-13 3:00:00
     * 夏令时结束时间：2022-11-6 2:00:00
     * 夏令时 =UTC-4 非夏令时 UTC-5
     */
    private static boolean isDaylightOffset(long instant) {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        return !zone.isStandardOffset(instant);
    }


    /**
     * 获取美东时间和标准时间的时间差(小时)
     *
     * @param newYorkTime 美东时间
     * @return 时间差（小时）夏令时为4 非夏令时为5
     */
    private static int getUTCAndNewYorkDiff(LocalDateTime newYorkTime) {
        if (isDaylightOffset(newYorkTime.toDate().getTime())) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * 美东时间转换为UTC时间
     *
     * @param newYorkTime 美东时间，这里就不校验时区了，自己传参保证下
     * @return utc时间
     */
    public static Date newYork2UTC(LocalDateTime newYorkTime) {
        return newYorkTime.plusHours(getUTCAndNewYorkDiff(newYorkTime)).toDate();
    }

//    /**
//     * 获取美国东部时间的周开始和结束时间，再换算成UTC时间
//     */
//    public static StartAndEndDateDTO getStartAndEndOfWeekInNewYork() {
//        //西方周日是一周的开始，取美东时间,西五区
//        DateTimeZone zone = DateTimeZone.forID("America/New_York");
//        //当前时间
//        LocalDateTime now = LocalDateTime.now(zone);
//        int dayOfWeek = now.getDayOfWeek();
//        //如果今天是周日，那么startTime = 今天，结束时间是下周六
//        //如果今天不是周日，那么startTime = 上周日，结束时间是本周六
//        Date startTime;
//        Date endTime;
//        if (dayOfWeek == DateTimeConstants.SUNDAY) {
//            startTime = newYork2UTC(
//                    now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).
//                            withDayOfWeek(DateTimeConstants.SUNDAY)
//            );
//            now = now.plusWeeks(1);
//            endTime = newYork2UTC(
//                    now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).
//                            withDayOfWeek(DateTimeConstants.SUNDAY)
//            );
//        } else {
//            endTime = newYork2UTC(now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).
//                    withDayOfWeek(DateTimeConstants.SUNDAY));
//            now = now.plusWeeks(-1);
//            startTime = newYork2UTC(now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).
//                    withDayOfWeek(DateTimeConstants.SUNDAY));
//        }
//        return new StartAndEndDateDTO(startTime, endTime);
//    }
}