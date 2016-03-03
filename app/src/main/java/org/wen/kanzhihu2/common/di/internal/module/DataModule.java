package org.wen.kanzhihu2.common.di.internal.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.squareup.moshi.Moshi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by Jiahui.wen on 15-11-4.
 */
@Module(includes = { ApiModule.class })
public class DataModule {

    @Singleton
    @Provides
    public SharedPreferences providePreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Singleton
    @Provides
    public Moshi provideMoshi() {
        return new Moshi.Builder().build();
    }
}
