package kanzhihu.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.SettingFragment;

public class SettingActivity extends Activity {

    public static void goSetting(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, SettingFragment.newInstance()).commit();
        }
    }
}
