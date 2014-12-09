package kanzhihu.android.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.greenrobot.event.EventBus;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.ArticlesFragment;
import kanzhihu.android.activities.fragments.CategoryFragment;
import kanzhihu.android.events.ReadArticlesEvent;
import kanzhihu.android.utils.ToastUtils;

public class MainActivity extends BaseActivity {

    private long exitTime = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new CategoryFragment()).commit();
        }
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(ReadArticlesEvent event) {
        Fragment articlesFragment = ArticlesFragment.newInstance(event.category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event.category.title);
        getFragmentManager().beginTransaction().add(R.id.container, articlesFragment).addToBackStack("").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingActivity.goSetting(this);
        } else if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_search) {
            SearchActivity.goSearch(this);
        } else if (id == R.id.action_my_mark) {
            SearchActivity.goMarkView(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            if ((System.currentTimeMillis() - exitTime) > AppConstant.APP_EXIT_TIME_INTERVAL) {
                ToastUtils.showShort(R.string.one_more_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                super.onBackPressed();
            }
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
