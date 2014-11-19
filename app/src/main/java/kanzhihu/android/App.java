package kanzhihu.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.crashlytics.android.Crashlytics;
import de.greenrobot.event.EventBus;
import java.io.File;
import kanzhihu.android.events.DownloadedApkEvent;
import kanzhihu.android.jobs.DeleteOldArticlesJob;
import kanzhihu.android.managers.ActivityManager;
import kanzhihu.android.managers.BackThreadManager;
import kanzhihu.android.managers.NotifyManager;
import kanzhihu.android.managers.UpdateManager;
import kanzhihu.android.utils.Cache;
import kanzhihu.android.utils.FileUtils;
import kanzhihu.android.utils.PreferenceUtils;

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

    private ActivityManager mCallBack;

    @Override public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

        mCallBack = new ActivityManager();
        registerActivityLifecycleCallbacks(mCallBack);

        mApplicationContext = this.getApplicationContext();

        if (PreferenceUtils.isAutoUpdate()) {
            //检查服务器app的版本号
            UpdateManager.CheckVersion();
        }

        BackThreadManager.getJobManager().addJobInBackground(new DeleteOldArticlesJob());

        EventBus.getDefault().register(this);
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
        NotifyManager.getManager().unregisterEventbus();
    }
}
