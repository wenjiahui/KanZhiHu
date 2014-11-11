package kanzhihu.android.managers;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.Stack;

/**
 * Created by Jiahui.wen on 2014/11/11.
 */
public class ActivityManager implements Application.ActivityLifecycleCallbacks {

    private Stack<Activity> mActivities;

    public ActivityManager() {
        this.mActivities = new Stack<Activity>();
    }

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.push(activity);
    }

    @Override public void onActivityStarted(Activity activity) {

    }

    @Override public void onActivityResumed(Activity activity) {

    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {

    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }

    public Activity topActivity() {
        return mActivities.peek();
    }

    public void gc() {
        if (mActivities.size() > 0) {
            mActivities.clear();
        }
        mActivities = null;
    }
}
