package org.wen.kanzhihu2;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowManager;
import io.fabric.sdk.android.Fabric;
import org.wen.kanzhihu2.common.di.internal.component.AppComponent;
import org.wen.kanzhihu2.common.di.internal.component.DaggerAppComponent;
import org.wen.kanzhihu2.common.di.internal.module.AppModule;
import timber.log.Timber;

/**
 * Created by Jiahui.wen on 15-10-31.
 */
public class App extends Application {

    public static App from(Context context) {
        return (App) context.getApplicationContext();
    }

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        FlowManager.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initializeInjector();
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
