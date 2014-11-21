package kanzhihu.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.SearchFragment;

public class SearchActivity extends ActionBarActivity {

    public static void goSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, SearchFragment.newInstance()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //hide refresh button
        menu.findItem(R.id.action_search).setVisible(false);
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
