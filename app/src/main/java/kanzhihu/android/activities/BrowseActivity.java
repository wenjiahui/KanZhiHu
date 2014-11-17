package kanzhihu.android.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.BrowseFragment;
import kanzhihu.android.models.Article;

public class BrowseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Article article = getIntent().getParcelableExtra(AppConstant.KEY_ARTICLE);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, BrowseFragment.newInstance(article)).commit();
        }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
