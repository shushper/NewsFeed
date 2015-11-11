package com.shushper.newsfeed.helpers;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Locale;


@RunWith(AndroidJUnit4.class)
public class TimeFormatterTest extends AndroidTestCase {

    @Test
    public void testFormatter() {
        TimeFormatter timeFormatter = new TimeFormatter(TimeFormatter.PATTERN_NEWS_FEED);

        Date date = new Date(1447226783465L);

        String formattedString = timeFormatter.format(date);

        assertNotNull(formattedString);

        if (Locale.getDefault().getLanguage().toLowerCase().equals("ru")) {
            assertEquals(formattedString, "11 ноября 2015, 12:26");
        }
    }
}
