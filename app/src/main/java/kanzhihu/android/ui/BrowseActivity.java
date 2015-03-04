package kanzhihu.android.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.models.Article;
import kanzhihu.android.ui.fragments.BrowseFragment;

public class BrowseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Article article = getIntent().getParcelableExtra(AppConstant.KEY_ARTICLE);
        getSupportActionBar().setTitle(article.title);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, BrowseFragment.newInstance(article)).commit();
        }
    }

    @Override public int getLayoutRes() {
        return R.layout.activity_browse;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingActivity.goSetting(this);
        } else if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_my_mark) {
            SearchActivity.goMarkView(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
