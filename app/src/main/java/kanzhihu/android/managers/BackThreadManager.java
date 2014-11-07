package kanzhihu.android.managers;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import kanzhihu.android.App;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.utils.AppLogger;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class BackThreadManager {

    private static JobManager instance;

    public static JobManager getJobManager() {
        if (instance == null) {
            initJobManager();
        }
        return instance;
    }

    private static void initJobManager() {
        Configuration configuration = new Configuration.Builder(App.getAppContext()).customLogger(new CustomLogger() {

            @Override
            public boolean isDebugEnabled() {
                return BuildConfig.DEBUG;
            }

            @Override
            public void d(String text, Object... args) {
                AppLogger.d(String.format(text, args));
            }

            @Override
            public void e(Throwable t, String text, Object... args) {
                AppLogger.e(String.format(text, args), t);
            }

            @Override
            public void e(String text, Object... args) {
                AppLogger.e(String.format(text, args));
            }
        }).minConsumerCount(1)//always keep at least one consumer alive
            .maxConsumerCount(3)//up to 3 consumers at a time
            .loadFactor(3)//3 jobs per consumer
            .consumerKeepAlive(120)//wait 2 minute
            .build();
        instance = new JobManager(App.getAppContext(), configuration);
    }

    private BackThreadManager() {

    }
}
