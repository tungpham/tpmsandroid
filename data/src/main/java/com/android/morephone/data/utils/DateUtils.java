package com.android.morephone.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
