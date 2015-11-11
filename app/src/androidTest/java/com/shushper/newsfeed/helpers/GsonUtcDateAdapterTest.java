package com.shushper.newsfeed.helpers;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.shushper.newsfeed.api.model.News;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import io.realm.RealmObject;


@RunWith(AndroidJUnit4.class)
public class GsonUtcDateAdapterTest extends AndroidTestCase {

    @Test
    public void adapterTest() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Date.class, new GsonUtcDateAdapter())
                .create();


        String json = "{\n" +
                "  \"createdAt\":\"2015-11-11T07:26:23.465Z\"\n" +
                "}";

        JsonElement element = gson.fromJson(json, JsonElement.class);

        News news = gson.fromJson(element, News.class);
        Date date = news.getCreatedAt();


        Log.d("Date millisecs", "milliseconds = " + date.getTime());

        assertNotNull(date);
        assertEquals(date.getTime(), 1447226783465L);
    }

}
