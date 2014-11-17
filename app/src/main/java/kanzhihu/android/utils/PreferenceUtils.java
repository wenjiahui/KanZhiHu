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

    /**
     * 设置保存多少天的文章
     */
    public static int getSaveDays() {
        return Integer.parseInt(PreferenceUtils.getString(AppConstant.PREF_KEY_SAVE_DAYS,
            App.getAppContext().getResources().getString(R.string.pref_save_days_default_value)));
    }

    /**
     * 配置信息，是否自动更新
     *
     * @return true 自动更新
     */
    public static boolean isAutoUpdate() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_AUTO_UPDATE, true);
    }

    /**
     * 是否自动拉取rss数据
     */
    public static boolean isAutoFetchRss() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_AUTO_REFRESH, false);
    }

    /**
     * 是否使用外部浏览器查看文章
     */
    public static boolean external_open() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_BROWSER, false);
    }

    public static String getString(String key, String defaultValue) {
        return getPreference().getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getPreference().getInt(key, defaultValue);
    }
}
