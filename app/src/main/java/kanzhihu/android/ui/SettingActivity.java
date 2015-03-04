package kanzhihu.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import java.util.Arrays;
import java.util.List;
import kanzhihu.android.R;
import kanzhihu.android.modules.SettingModule;
import kanzhihu.android.ui.fragments.SettingFragment;

public class SettingActivity extends InjectableActivity {

    public static void goSetting(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, SettingFragment.newInstance()).commit();
        }
    }

    @Override public List<Object> listModules() {
        return Arrays.<Object>asList(new SettingModule());
    }

    @Override public int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
