package com.example.bankapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    SimpleDateFormat simpleDateFormat;

    private static TimeManager tm;
    public static TimeManager getInstance() {
        if (tm == null) {
            tm = new TimeManager();
        }
        return tm;
    }

    public TimeManager() {
        simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
    }

    public boolean isSameDay(long time1, long time2) {
        System.out.println("_LOG: "+simpleDateFormat.format(new Date(time1)) + "  "+simpleDateFormat.format(new Date(time2)));
        return simpleDateFormat.format(new Date(time1)).equals(simpleDateFormat.format(new Date(time2)));
    }

    public long today() {
        return new Date().getTime();
    }

    public long parseDate(String date) {
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getDateAdvancedByMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return date + cal.getTimeInMillis();
    }

}
