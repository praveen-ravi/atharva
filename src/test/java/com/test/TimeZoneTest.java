package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 16733 on 08/03/17.
 */
public class TimeZoneTest {
    public static void main(String args[]) throws ParseException {

        Calendar cal=Calendar.getInstance();
        long timeInMilli=cal.getTime().getTime();
        long alteredTime = timeInMilli+ (TimeZone.getTimeZone("EST").getRawOffset()-cal.getTimeZone().getRawOffset());
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        cal2.setTimeInMillis(alteredTime);
        SimpleDateFormat format=new SimpleDateFormat("DD/MM/YYYY HH:MM", Locale.UK);
        Date endTime= format.parse(cal2.get(Calendar.DAY_OF_MONTH)+"/"+cal2.get(Calendar.MONTH)+"/"+cal2.get(Calendar.YEAR)+" 15:10");
        System.out.println(cal2.getTime()+" "+endTime);


    }
}
