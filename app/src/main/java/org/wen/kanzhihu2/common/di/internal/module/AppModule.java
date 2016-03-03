package org.wen.kanzhihu2.common.di.internal.module;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.wen.kanzhihu2.App;
import org.wen.kanzhihu2.common.Navigator;

/**
 * Created by Jiahui.wen on 15-11-4.
 */
@Module
public class AppModule {

    private App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApp;
    }

    @Singleton
    @Provides
    public Navigator provideNavigator() {
        return new Navigator();
    }
}
