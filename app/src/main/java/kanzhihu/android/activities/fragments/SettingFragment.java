package kanzhihu.android.activities.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import de.greenrobot.event.EventBus;
import javax.inject.Inject;
import kanzhihu.android.AppConstant;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.R;
import kanzhihu.android.events.ImageModeChangeEvent;
import kanzhihu.android.modules.Injector;
import kanzhihu.android.utils.Preferences;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject Preferences mPreference;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);

        Injector.inject(this);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Preference versionPref = findPreference(AppConstant.PREF_KEY_APP_VERSION);
        if (versionPref != null) {
            versionPref.setTitle(getString(R.string.version, BuildConfig.VERSION_NAME));
        }

        showSaveDays();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (AppConstant.PREF_KEY_SAVE_DAYS.equals(key)) {
            showSaveDays();
        } else if (AppConstant.PREF_KEY_NO_IMAGE.equals(key)) {
            boolean imageMode = mPreference.getImageMode();
            mPreference.setImageMode(imageMode);
            EventBus.getDefault().post(new ImageModeChangeEvent(imageMode));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showSaveDays() {
        Preference saveDaysPref = findPreference(AppConstant.PREF_KEY_SAVE_DAYS);
        if (saveDaysPref != null) {
            int select = mPreference.getSaveDays();
            String[] days = getResources().getStringArray(R.array.pref_saveDays_values);
            int index = 0;
            for (int i = 0; i < days.length; i++) {
                if (String.valueOf(select).equals(days[i])) {
                    index = i;
                }
            }

            saveDaysPref.setTitle(
                getString(R.string.pref_saveDays, getResources().getStringArray(R.array.pref_saveDays_entries)[index]));
        }
    }
}
