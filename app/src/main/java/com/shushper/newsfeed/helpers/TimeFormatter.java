package com.shushper.newsfeed.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeFormatter {

    public final static String PATTERN_SERVER_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public final static String PATTERN_NEWS_FEED = "dd MMMM yyyy, HH:mm";


    private SimpleDateFormat mDateFormat;

    public TimeFormatter(String pattern) {

        mDateFormat = new SimpleDateFormat();
        mDateFormat.applyPattern(pattern);
    }

    public String format(Date date) {
        return mDateFormat.format(date);
    }
}
