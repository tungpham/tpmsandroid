package com.android.morephone.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ethan on 4/6/17.
 */

public class DateUtils {


    public static Date getDate(String date) {
        SimpleDateFormat in = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");

        try {
            Date time = in.parse(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateExpire(long date) {
        SimpleDateFormat out = new SimpleDateFormat("MMM d, HH:mm a");
        Date time = new Date(date);
        return out.format(time);

    }

    public static long elapsedDay(long different){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        return different / daysInMilli;
    }

    public static long elapsedHour(long different){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        different = different % daysInMilli;

        return different / hoursInMilli;
    }

    public static long elapsedMin(long different){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        return different / minutesInMilli;
    }

    public static long elapsedSecond(long different){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        different = different % daysInMilli;

        different = different % hoursInMilli;

        different = different % minutesInMilli;

        return different / secondsInMilli;
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    public static Date getDateMessage(String date) {
//        2017-08-04T07:57:46.000Z
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MMM-dEHH, d MMM yyyy HH:mm:ss Z");

        try {
            Date time = in.parse(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
