package kanzhihu.android.modules;

import android.app.Application;
import android.net.Uri;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import javax.inject.Singleton;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.activities.adapter.ArticlesAdapter;
import kanzhihu.android.activities.adapter.SearchAdapter;
import kanzhihu.android.activities.fragments.ArticlesFragment;
import kanzhihu.android.activities.fragments.SearchFragment;
import kanzhihu.android.activities.fragments.SettingFragment;
import kanzhihu.android.activities.fragments.UpdateDialogFragment;
import kanzhihu.android.activities.presenters.impl.ArticlesPresenterImpl;
import kanzhihu.android.activities.presenters.impl.CategoryPresenterImpl;
import kanzhihu.android.activities.presenters.impl.QueryPresenterImpl;
import kanzhihu.android.jobs.CheckVersionJob;
import kanzhihu.android.jobs.DeleteOldArticlesJob;
import kanzhihu.android.jobs.DownloadAppJob;
import kanzhihu.android.jobs.FetchRssJob;
import kanzhihu.android.managers.NotifyManager;
import kanzhihu.android.managers.UpdateManager;
import timber.log.Timber;

/**
 * Created by Jiahui.wen on 15-1-25.
 */
@Module(
    injects = {
        App.class, CategoryPresenterImpl.class, CheckVersionJob.class, DownloadAppJob.class, FetchRssJob.class,
        ArticlesFragment.class, ArticlesAdapter.class, SearchAdapter.class, QueryPresenterImpl.class,
        ArticlesPresenterImpl.class, SettingFragment.class, UpdateDialogFragment.class, SearchFragment.class,
        DeleteOldArticlesJob.class
    },
    includes = { AppModule.class },
    library = true)
public class DataModule {

    @Provides @Singleton
    public JobManager provideJobManager(Application app) {
        return new JobManager(app, createConfig(app));
    }

    @Provides @Singleton
    public UpdateManager provideUpdateManager(JobManager jobManager, NotifyManager notifyManager) {
        return new UpdateManager(jobManager, notifyManager);
    }

    @Provides @Singleton
    public OkHttpClient provideOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectionPool(new ConnectionPool(3, 3000));
        try {
            File cacheDir = new File(app.getCacheDir(), "http");
            client.setCache(new Cache(cacheDir, AppConstant.DISK_CACHE_SIZE));
        } catch (IOException e) {
            Timber.e("unable to build http disk cache");
        }
        return client;
    }

    @Provides @Singleton
    public Picasso providePicasso(Application app, OkHttpClient okHttpClient) {
        return new Picasso.Builder(app).downloader(new OkHttpDownloader(okHttpClient)).listener(new Picasso.Listener() {
            @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Timber.e(exception, " Failed to load image uri: " + uri);
            }
        }).build();
    }

    private static Configuration createConfig(Application app) {
        Configuration configuration = new Configuration.Builder(app).customLogger(new CustomLogger() {

            @Override
            public boolean isDebugEnabled() {
                return BuildConfig.DEBUG;
            }

            @Override
            public void d(String text, Object... args) {
                Timber.d(text, args);
            }

            @Override
            public void e(Throwable t, String text, Object... args) {
                Timber.e(t, text, args);
            }

            @Override
            public void e(String text, Object... args) {
                Timber.e(text, args);
            }
        }).minConsumerCount(1)//always keep at least one consumer alive
            .maxConsumerCount(Runtime.getRuntime().availableProcessors()).loadFactor(3)//3 jobs per consumer
            .consumerKeepAlive(120)//wait 2 minute
            .build();

        return configuration;
    }
}
