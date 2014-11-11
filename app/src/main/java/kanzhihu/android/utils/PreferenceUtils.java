package kanzhihu.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class PreferenceUtils {

    public static SharedPreferences getPreference() {
        return App.getAppContext().getSharedPreferences(AppConstant.KEY_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static int getSaveDays() {
        return Integer.parseInt(PreferenceUtils.getString(AppConstant.PREF_KEY_SAVE_DAYS,
            App.getAppContext().getResources().getString(R.string.pref_save_days_default_value)));
    }

    public static String getString(String key, String defaultValue) {
        return getPreference().getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getPreference().getInt(key, defaultValue);
    }
}
