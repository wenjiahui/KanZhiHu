package kanzhihu.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.path.android.jobqueue.JobManager;
import com.tencent.bugly.crashreport.CrashReport;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import java.io.File;
import javax.inject.Inject;
import kanzhihu.android.events.DownloadedApkEvent;
import kanzhihu.android.events.NetworkErrorEvent;
import kanzhihu.android.jobs.DeleteOldArticlesJob;
import kanzhihu.android.managers.ActivityManager;
import kanzhihu.android.managers.NotifyManager;
import kanzhihu.android.managers.UpdateManager;
import kanzhihu.android.modules.AppModule;
import kanzhihu.android.modules.DataModule;
import kanzhihu.android.modules.Injector;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.FileUtils;
import kanzhihu.android.utils.Preferences;
import kanzhihu.android.utils.ToastUtils;
import timber.log.Timber;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class App extends Application {

    private static Context mApplicationContext;

    public static Context getAppContext() {
        return mApplicationContext;
    }

    public static App getInstance() {
        return (App) mApplicationContext;
    }

    @Inject ActivityManager mCallBack;

    @Inject JobManager mJobManager;

    @Inject UpdateManager mUpdateManager;

    @Inject NotifyManager mNotifyManager;

    @Inject Preferences mPreference;

    @Override public void onCreate() {
        super.onCreate();

        mApplicationContext = this.getApplicationContext();

        Injector.setGraph(ObjectGraph.create(new AppModule(this), new DataModule()));
        Injector.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initCrashAnalysic();

        registerActivityLifecycleCallbacks(mCallBack);

        if (mPreference.isAutoUpdate()) {
            //检查服务器app的版本号
            mUpdateManager.CheckVersion();
        }

        mJobManager.addJobInBackground(new DeleteOldArticlesJob());

        EventBus.getDefault().register(this);
    }

    private void initCrashAnalysic() {
        String appId = "900001876";   //上Bugly(bugly.qq.com)注册产品获取的AppId

        CrashReport.initCrashReport(this, appId, BuildConfig.DEBUG);  //初始化SDK
    }

    public Activity topActivity() {
        return mCallBack.topActivity();
    }

    @Override public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mCallBack);
        mCallBack.gc();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        Cache.clear();
    }

    /**
     * 使用EventBus监听下载apk的事件。
     *
     * @param event 完成apk下载的事件
     */
    public void onEventMainThread(DownloadedApkEvent event) {
        File apkfile = new File(FileUtils.getCachePath(), AppConstant.APK_NAME);
        if (apkfile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            topActivity().startActivity(intent);
        }
        //最新版的apk已经下载完毕，目前不需要继续监听事件。
        EventBus.getDefault().unregister(this);
        mNotifyManager.unregisterEventbus();
    }

    /**
     * 检测全局App的网络无连接事件
     */
    public void onEventMainThread(NetworkErrorEvent event) {
        if (event.messageRes > 0) {
            ToastUtils.alert(event.messageRes);
        } else if (!TextUtils.isEmpty(event.message)) {
            ToastUtils.alert(event.message);
        }
    }
}
