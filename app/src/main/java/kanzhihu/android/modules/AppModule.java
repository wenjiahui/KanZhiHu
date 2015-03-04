package kanzhihu.android.modules;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import kanzhihu.android.managers.ActivityManager;
import kanzhihu.android.managers.NotifyManager;
import kanzhihu.android.utils.Preferences;

/**
 * Created by Jiahui.wen on 15-1-25.
 */
@Module(
    complete = false,
    library = true)
public class AppModule {

    private Application app;

    public AppModule(Application application) {
        this.app = application;
    }

    @Provides @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides @Singleton
    public ActivityManager provideActivityManager() {
        return new ActivityManager();
    }

    @Provides @Singleton
    public NotifyManager provideNotifyManager() {
        return new NotifyManager();
    }

    @Provides @Singleton
    public Preferences providePreference(Application app) {
        return new Preferences(app);
    }
}
