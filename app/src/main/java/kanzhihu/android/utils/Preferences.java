package kanzhihu.android.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class Preferences {

    private boolean mImageMode = false;
    private SharedPreferences mPreference;

    public Preferences(Application app) {
        mPreference = PreferenceManager.getDefaultSharedPreferences(app);

        setImageMode(mPreference.getBoolean(AppConstant.PREF_KEY_NO_IMAGE, AppConstant.IMAGE_MODE));
    }

    public boolean imageMode() {
        return mImageMode;
    }

    public void setImageMode(boolean mImageMode) {
        this.mImageMode = mImageMode;
    }

    public SharedPreferences getPreference() {
        return mPreference;
    }

    /**
     * 设置保存多少天的文章
     */
    public int getSaveDays() {
        return Integer.parseInt(mPreference.getString(AppConstant.PREF_KEY_SAVE_DAYS,
            App.getAppContext().getResources().getString(R.string.pref_save_days_default_value)));
    }

    /**
     * 配置信息，是否自动更新
     *
     * @return true 自动更新
     */
    public boolean isAutoUpdate() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_AUTO_UPDATE, true);
    }

    /**
     * 是否自动拉取rss数据
     */
    public boolean isAutoFetchRss() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_AUTO_REFRESH, false);
    }

    /**
     * 是否使用外部浏览器查看文章
     */
    public boolean external_open() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_BROWSER, true);
    }

    /**
     * 是否自动加载图片
     */
    public boolean getImageMode() {
        return getPreference().getBoolean(AppConstant.PREF_KEY_NO_IMAGE, AppConstant.IMAGE_MODE);
    }

    public String getString(String key, String defaultValue) {
        return getPreference().getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return getPreference().getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        getPreference().edit().putInt(key, value).apply();
    }
}
