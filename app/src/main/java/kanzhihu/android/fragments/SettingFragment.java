package kanzhihu.android.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import kanzhihu.android.AppConstant;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.R;
import kanzhihu.android.utils.PreferenceUtils;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
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
        if (key.equals(AppConstant.PREF_KEY_SAVE_DAYS)) {
            showSaveDays();
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
            int select = PreferenceUtils.getSaveDays();

            saveDaysPref.setTitle(getString(R.string.pref_saveDays,
                getResources().getStringArray(R.array.pref_saveDays_entries)[select]));
        }
    }
}
