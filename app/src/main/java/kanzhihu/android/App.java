package kanzhihu.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import kanzhihu.android.managers.ActivityManager;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class App extends Application {

    private static Context mApplicationContext;

    public static Context getAppContext() {
        return mApplicationContext;
    }

    private ActivityManager mCallBack;

    @Override public void onCreate() {
        super.onCreate();

        mCallBack = new ActivityManager();
        registerActivityLifecycleCallbacks(mCallBack);

        mApplicationContext = this.getApplicationContext();
    }

    public Activity topActivity() {
        return mCallBack.topActivity();
    }

    @Override public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mCallBack);
        mCallBack.gc();
    }
}
