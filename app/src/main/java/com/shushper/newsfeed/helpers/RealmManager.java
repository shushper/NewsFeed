package com.shushper.newsfeed.helpers;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;


/**
 * Позволяет создавать и хранить разные конфигурации realm.
 */
public class RealmManager {
    private final String TAG = RealmManager.class.getSimpleName();

    private static RealmManager ourInstance = new RealmManager();

    private RealmConfiguration mDefaultConfiguration;

    public static RealmManager getInstance() {
        return ourInstance;
    }

    private RealmManager() {
    }

    public void initialize(Context context) {
        Log.i(TAG, "initialize");

        mDefaultConfiguration = new RealmConfiguration.Builder(context)
                .schemaVersion(0)
                .migration(new DefaultMigration())
                .build();

        Realm.setDefaultConfiguration(mDefaultConfiguration);
    }


    public class DefaultMigration implements RealmMigration {
        @Override
        public long execute(Realm realm, long version) {
            return 0;
        }

    }
}
