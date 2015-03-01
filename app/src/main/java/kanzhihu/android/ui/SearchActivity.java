package kanzhihu.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Arrays;
import java.util.List;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.modules.SearchModule;
import kanzhihu.android.ui.fragments.SearchFragment;

public class SearchActivity extends InjectableActivity {

    public static void goSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void goMarkView(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(AppConstant.ACTION_MODE_MARK_VIEW, true);
        context.startActivity(intent);
    }

    /**
     * true: 收藏模式： 显示收藏文章 <br/>
     * false: 查询模式：显示所有文章。
     */
    private boolean bMarkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bMarkView = getIntent().getBooleanExtra(AppConstant.ACTION_MODE_MARK_VIEW, false);
        if (bMarkView) {
            getSupportActionBar().setTitle(R.string.action_my_collections);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, SearchFragment.newInstance(bMarkView)).commit();
        }
    }

    @Override public List<Object> listModules() {
        return Arrays.<Object>asList(new SearchModule());
    }

    @Override public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //hide refresh button
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_my_mark).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            SettingActivity.goSetting(this);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
