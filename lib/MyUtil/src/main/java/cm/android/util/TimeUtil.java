package cm.android.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间Util类
 */
public class TimeUtil {

    public static final int ONE_SECOND = 1000;

    private static final SimpleDateFormat format1 = new SimpleDateFormat(
            "yyyy-MM-dd HH.mm.ss.SSS");

    private static final SimpleDateFormat format2 = new SimpleDateFormat(
            "yyyyMMdd");

    private static final long MS_OF_ONE_DAY = (24 * 60 * 60 * 1000);

    /**
     * 获取当前日期并转换成字符串
     *
     * @param formart 转换格式
     */
    public static String getDate2String(String formart) {
        return getDate2String(new Date(), formart);
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        DecimalFormat df = new DecimalFormat("00");
        Calendar c = Calendar.getInstance();
        String currentTime = c.get(Calendar.YEAR)// + "-"
                + df.format((c.get(Calendar.MONTH) + 1)) // + "-"
                + df.format(c.get(Calendar.DAY_OF_MONTH))// + "-"
                + df.format(c.get(Calendar.HOUR_OF_DAY)) // + "-"
                + df.format(c.get(Calendar.MINUTE))// + "-"
                + df.format(c.get(Calendar.SECOND));
        return currentTime;
    }

    /**
     * 获取当前日期并转换成YYYY-MM-dd HH:mm:ss 格式的字符串
     */
    public static String getDate2String() {
        return getDate2String(new Date());
    }

    /**
     * 将日期转换成字符串
     *
     * @param date    转换日期
     * @param formart 转换格式
     */
    public static String getDate2String(Date date, String formart) {
        SimpleDateFormat sdf = new SimpleDateFormat(formart);
        return sdf.format(date);
    }

    /**
     * 将日期转换成YYYY-MM-dd HH:mm:ss 格式的字符串
     *
     * @param date 转换日期
     */
    public static String getDate2String(Date date) {
        return getDate2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取但前日期月份
     */
    public static int getMonth() {
        return getMonth(new Date());
    }

    /**
     * 获取日期月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    /**
     * 获取日期年份
     */
    public static int getYear() {
        return getYear(new Date());
    }

    /**
     * 获取日期年份
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前日期在月中的天数
     */
    public static int getDayOfMonth() {
        return getDayOfMonth(new Date());
    }

    /**
     * 获取日期在月份中的天数
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前日期的的星期数
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(new Date());
    }

    /**
     * 获取日期的星期数
     */
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前日期是一年中第几天
     */
    public static int getDayOfYear() {
        return getDayOfYear(new Date());
    }

    /**
     * 获取日期在一年中的天数
     */
    public static int getDayOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取下一个月
     */
    public static int getNextMonth() {
        return getNextMonth(new Date());
    }

    /**
     * 获取下一个月
     */
    public static int getNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        return c.get(Calendar.MONTH);
    }

    /**
     * 获取上一个月
     */
    public static int getUpMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.get(Calendar.MONTH);
    }

    /**
     * 获取上一个月
     */
    public static int getUpMonth() {
        return getUpMonth(new Date());
    }

    /**
     * 获取一周的开始日期
     */
    public static int getFirstDayOfWeek(Date date) {
        int n = getDayOfWeek(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - n + 1);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取一周的最后一天
     */
    public static int getLastDayOfWeek(Date date) {
        int n = getDayOfWeek(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        System.out.println(getDate2String(c.getTime()));
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + (7 - n));
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 在日期上加days天
     */
    public static Date addDay(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    /**
     * 获取明天日期
     */
    public static Date getTomorrow(Date date) {
        return addDay(date, 1);
    }

    /**
     * 获取明天日期
     */
    public static Date getTomorrow() {
        return getTomorrow(new Date());
    }

    /**
     * 获取昨天日期
     */
    public static Date getYestoday(Date date) {
        return addDay(date, -1);
    }

    /**
     * 获取昨天日期
     */
    public static Date getYestoday() {
        return getYestoday(new Date());
    }

    /**
     * 获取两日期之间相差的天数
     */
    public static long getDiffOfDays(Date starDate, Date endDate) {
        return getDiffOfHours(starDate, endDate) / 24;
    }

    /**
     * 获取两日期之间相差的小时
     */
    public static long getDiffOfHours(Date starDate, Date endDate) {
        return getDiffOfMinute(starDate, endDate) / 60;
    }

    public static long getDiffOfMinute(Date starDate, Date endDate) {
        return getDiffOfSecond(starDate, endDate) / 60;
    }

    public static long getDiffOfSecond(Date starDate, Date endDate) {
        return getDiffOfMillis(starDate, endDate) / 1000;
    }

    public static long getDiffOfMillis(Date starDate, Date endDate) {
        Calendar star = Calendar.getInstance();
        star.setTime(starDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        long diff = star.getTimeInMillis() - end.getTimeInMillis();
        long diffMillis = (Math.abs(diff));
        return diffMillis;
    }

    /**
     * 获取量日期之间相差的月数
     */
    public static int getDiffOfMonth(Date starDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(starDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int diffYear = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        int diffMonth = end.get(Calendar.MONTH) + 1
                - (start.get(Calendar.MONTH));
        return diffYear * 12 + diffMonth;
    }

    /**
     * 获取两日期间相差的年数
     */
    public static int getDiffOfYears(Date starDate, Date endDate) {
        Calendar star = Calendar.getInstance();
        star.setTime(starDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int diffYear = star.get(Calendar.YEAR) - end.get(Calendar.YEAR);
        return Math.abs(diffYear);
    }

    /**
     * 获取月中的天数
     */
    public static int getMonthDays(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月中的天数
     */
    public static int getMonthDays() {
        return getMonthDays(new Date());
    }

    /**
     * 获取下月天数
     */
    public static int getNextMonthDays(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取下月天数
     */
    public static int getNextMonthDays() {
        return getNextMonthDays(new Date());
    }

    /**
     * 获取上月天数
     */
    public static int getUpMonthDays(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取上月天数
     */
    public static int getUpMonthDays() {
        return getUpMonthDays(new Date());
    }

    /**
     * 把时间字符串转换成Date
     */
    public static Date parseDate(String date, String datePartten)
            throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(datePartten);
        Date d = df.parse(date);
        return d;
    }

    /**
     * 把时间字符串转换成Date
     */
    public static Date parseDate(String date) throws Exception {
        String[] datePartten = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
                "yyyy/MM/dd HH:mm;ss", "yyyy/MM/dd", "yyyyMMddHHmmss",
                "yyyyMMdd"};
        return parseDate(date, datePartten);
    }

    /**
     * 把时间字符串转换成Date
     */
    public static Date parseDate(String date, String[] dateParttens)
            throws Exception {
        SimpleDateFormat df = null;
        Date d = null;
        boolean isParse = false;
        for (String partten : dateParttens) {
            df = new SimpleDateFormat(partten);
            try {
                d = df.parse(date);
                isParse = true;
                break;
            } catch (ParseException e) {
                isParse = false;
            }
        }
        if (!isParse) {
            throw new Exception();
        }
        return d;
    }

    /**
     * 是时间的基础上+
     *
     * @param field 请使用Calendar的常量
     */
    public static Date add(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * UTC日期时间
     */
    public static String getCurrentUtcTime() {
        Date l_datetime = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");
        formatter.setTimeZone(l_timezone);
        String l_utc_date = formatter.format(l_datetime);
        return l_utc_date;
    }

    public static String getTimeZone() {
        int timeZone = TimeZone.getDefault().getOffset(
                System.currentTimeMillis());
        timeZone /= (60 * 60 * 1000);// 毫秒换算成小时
        return String.valueOf(timeZone);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getFormatTime1(Calendar c) {
        if (null == c) {
            return "null";
        }
        DecimalFormat df = new DecimalFormat("00");
        String strCurrTime = c.get(Calendar.YEAR) + "-"
                + df.format((c.get(Calendar.MONTH) + 1)) + "-"
                + df.format(c.get(Calendar.DAY_OF_MONTH)) + " "
                + df.format(c.get(Calendar.HOUR_OF_DAY)) + ":"
                + df.format(c.get(Calendar.MINUTE)) + ":"
                + df.format(c.get(Calendar.SECOND));

        return strCurrTime;
    }

    /**
     * yyyyMMddHHmmss
     */
    public static String getFormatTime2(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        String strFileName = getFormatTime3(c);
        return strFileName;
    }

    /**
     * yyyyMMddHHmmss
     */
    public static String getFormatTime3(Calendar c) {
        DecimalFormat df = new DecimalFormat("00");
        String strFileName = c.get(Calendar.YEAR)
                + df.format((c.get(Calendar.MONTH) + 1))
                + df.format(c.get(Calendar.DAY_OF_MONTH))
                + df.format(c.get(Calendar.HOUR_OF_DAY))
                + df.format(c.get(Calendar.MINUTE))
                + df.format(c.get(Calendar.SECOND));

        return strFileName;
    }

    public static String genTimeStampStrIncludeMillionSecond() {
        return format1.format(Calendar.getInstance().getTime());
    }

    public static String genTimeStampStrIncludeDay() {
        return format2.format(Calendar.getInstance().getTime());
    }

    public static int intervalDayBetweenDate(Calendar cl1, Calendar cl2) {
        long interval = 0;

        if (cl1.after(cl2)) {
            interval = cl1.getTimeInMillis() - cl2.getTimeInMillis();
        } else {
            interval = cl2.getTimeInMillis() - cl1.getTimeInMillis();
        }

        return (int) (interval / MS_OF_ONE_DAY);
    }

    public static boolean isInSameDay(Calendar cl1, Calendar cl2) {
        if (cl1.get(Calendar.DAY_OF_YEAR) == cl2.get(Calendar.DAY_OF_YEAR)
                && cl1.get(Calendar.YEAR) == cl2.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    public static boolean overOneMin(Calendar cl1, Calendar cl2) {
        if ((cl2.getTimeInMillis() - cl1.getTimeInMillis()) > 60 * 1000) {
            return true;
        }
        return false;
    }

}
