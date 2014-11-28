package kanzhihu.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.SearchFragment;

public class SearchActivity extends BaseActivity {

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
        setContentView(R.layout.activity_search);

        bMarkView = getIntent().getBooleanExtra(AppConstant.ACTION_MODE_MARK_VIEW, false);
        if (bMarkView) {
            setTitle(R.string.action_my_collections);
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, SearchFragment.newInstance(bMarkView)).commit();
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
