package com.example.bankapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    private final SimpleDateFormat sqlDateFormat;
    private final SimpleDateFormat sqlDateTimeFormatter;
    private final SimpleDateFormat readableDateFormat;
    private final SimpleDateFormat readableDateTimeFormat;

    private static TimeManager tm;
    public static TimeManager getInstance() {
        if (tm == null) {
            tm = new TimeManager();
        }
        return tm;
    }

    public TimeManager() {
        sqlDateFormat = new SimpleDateFormat("yy-MM-dd");
        sqlDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        readableDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        readableDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    public long today() {
        return new Date().getTime();
    }

    public long parseDate(String date) {
        try {
            return sqlDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getDateAdvancedByMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTimeInMillis();
    }

    public String getDateTime(long time) {
        return sqlDateTimeFormatter.format(new Date(time));
    }

    public String getReadableDate(long time) {
        return readableDateFormat.format(new Date(time));
    }

    public String getReadableDateTime(long time) {
        return readableDateTimeFormat.format(new Date(time));
    }

}
