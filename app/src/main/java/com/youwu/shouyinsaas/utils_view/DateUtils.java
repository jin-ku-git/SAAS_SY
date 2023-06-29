package com.youwu.shouyinsaas.utils_view;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.utils.KLog;

public class DateUtils {

    /**
     * 格式化日期
     * **/
    public static String formatData(long time,String format){
        if(time <= 0){
            return "未知时间";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    /**
     * 格式化日期
     * **/
    public static String formatData(Date date,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long getDataTime(String data){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TFORMATE_YMD);
        try {
            Date date = dateFormat.parse(data);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    /**
     * 获取当前时间的起点（00:00:00）
     *
     * @param currentTime
     * @return
     */
    public static long getTodayStart(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间的小时值
     *
     * @param currentTime
     * @return
     */
    public static int getHour(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间的分钟值
     *
     * @param currentTime
     * @return
     */
    public static int getMinute(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间的分钟值
     *
     * @param currentTime
     * @return
     */
    public static int getSecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当前时间的分钟值+毫秒值
     *
     * @param currentTime
     * @return
     */
    public static int getMinuteMillisecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MINUTE) * 60 * 1000 + calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @param currentTime
     * @return
     */
    public static int getMillisecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 获取当前时间的终点（23:59:59）
     *
     * @param currentTime
     * @return
     */
    public static long getTodayEnd(long currentTime) {
        return getTodayStart(currentTime) + 24 * 60 * 60 * 1000L - 1000;
    }

    /**
     * 获取指定时间的年月日
     *
     * @param currentTime
     * @return
     */
    public static String getDateByCurrentTiem(long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
    }

    /**
     * 获取指定时间的年月日
     *
     * @param currentTime
     * @return
     */
    public static String getDateTime(long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
    }

    /**
     * 根据下标获取HH:mm格式的时间
     *
     * @param timeIndex
     * @return
     */
    public static String getHourMinute(int timeIndex) {
        return format.format(timeIndex * 60 * 1000 + 16 * 60 * 60 * 1000);
    }

    /**
     * 获取指定日期的时间（如：10:11:12）
     *
     * @param currentTime
     * @return
     */
    public static String getTime(long currentTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(currentTime));
    }

    /**
     * 根据当前的秒数计算时间
     *
     * @param currentSecond
     * @return
     */
    public static String getTimeByCurrentSecond(int currentSecond) {
        currentSecond = currentSecond / 60;
        int minute = currentSecond % 60;
        int hour = currentSecond / 60;
        if (hour >= 24) {
            hour = hour % 24;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 根据当前的秒数计算时间
     *
     * @param currentSecond
     * @return
     */
    public static String getTimeByCurrentHours(int currentSecond) {
        currentSecond = currentSecond * 10;
        currentSecond = currentSecond / 60;
        int minute = currentSecond % 60;
        int hour = currentSecond / 60;
        if (hour >= 24) {
            hour = hour % 24;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean isCurrentTimeArea(long nowTime, long beginTime, long endTime) {
        return nowTime >= beginTime && nowTime <= endTime;
    }


    /**
     * @Description: 某个时间距当前日期的时间差天和小时
     * @Author MengXY
     * @Emil xiangyongmeng@4d-bios.com
     * @Date 2019/1/14
     * @Params
     * @Return  [ ]
     */
    public static String getDiffTime(String date1,String date2) {
        String str = "00:00";
        try {
            long diff = 0;
            long day,hour,min;
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date dta = dateFormat.parse(date1);
            Date dtb = dateFormat.parse(date2);
            long time1 = dta.getTime();
            long time2 = dtb.getTime();
            KLog.e("time1:"+time1+"time2:"+time2);

            if(time1<time2) {
                diff = time2 - time1;
            } else if(time1 > time2){
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            if(day == 1 && hour == 0 && min == 0){
                KLog.e("1天");
                return "24:00";
            }
            if(hour < 10){
                str = "0"+hour;
            }else {
                str = hour+"";
            }
            str+= ":";
            if(min < 10){
                str += "0"+min;
            }else {
                str += min+"";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @Description: 某个时间距当前日期的时间差天和小时
     * @Author MengXY
     * @Emil xiangyongmeng@4d-bios.com
     * @Date 2019/1/14
     * @Params
     * @Return  [ ]
     */
    public static int[] getDistanceDay(long date) {
        if(date == 0){
            return new int[]{0,0,0};
        }
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String now = df.format(new Date());
//        Date one;
//        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
//            one = df.parse(now);
//            two = df.parse(date);
            long time1 = new Date().getTime();
            long time2 = new Date(date).getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] diffdate = new int[]{(int)day,(int)hour,(int)min};
        return diffdate;
    }



    //Calendar 转化 String
    public static  String calendarToStr(Calendar calendar,String format) {

//    Calendar calendat = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(calendar.getTime());
    }


    //String 转化Calendar
    public static Calendar strToCalendar(String str,String format) {

//    String str = "2012-5-27";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        Calendar calendar = null;
        try {
            date = sdf.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    //    Date 转化String
    public static String dateTostr(Date date,String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
//    String dateStr = sdf.format(new Date());
        String dateStr = sdf.format(date);
        return dateStr;
    }


    //  String 转化Date
    public static Date strToDate(String str,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //Date 转化Calendar
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    //Calendar转化Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


    // String 转成    Timestamp

    public static Timestamp strToTimeStamp(String str) {

//    Timestamp ts = Timestamp.valueOf("2012-1-14 08:11:00");
        return Timestamp.valueOf(str);
    }


    //Date 转 TimeStamp
    public static Timestamp dateToTimeStamp(Date date,String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        String time = df.format(new Date());

        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

}