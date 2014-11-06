package kanzhihu.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class App extends Application {

    private static Context mApplicationContext;

    public static Context getAppContext() {
        return mApplicationContext;
    }

    @Override public void onCreate() {
        super.onCreate();

        mApplicationContext = this.getApplicationContext();
    }
}
