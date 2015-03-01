package kanzhihu.android.ui.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import com.path.android.jobqueue.JobManager;
import de.greenrobot.event.EventBus;
import javax.inject.Inject;
import kanzhihu.android.R;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.CategoryTable;
import kanzhihu.android.events.ReadArticlesEvent;
import kanzhihu.android.models.Category;
import kanzhihu.android.modules.IInject;
import kanzhihu.android.ui.adapter.CategoryAdapter;
import kanzhihu.android.ui.adapter.base.CursorRecyclerViewAdapter;
import kanzhihu.android.ui.fragments.base.BaseFragment;
import kanzhihu.android.ui.presenters.CategoryPresenter;
import kanzhihu.android.ui.presenters.impl.CategoryPresenterImpl;
import kanzhihu.android.ui.views.CategoryView;
import kanzhihu.android.utils.AssertUtils;
import kanzhihu.android.utils.Preferences;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class CategoryFragment extends BaseFragment implements CategoryView, LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @InjectView(R.id.swiperefreshlayout) SwipeRefreshLayout mSwipelayout;

    @Inject JobManager mJobManager;

    @Inject Preferences mPreference;

    private CursorRecyclerViewAdapter mAdapter;
    private CategoryPresenter mPresenter;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IInject) {
            IInject injector = (IInject) activity;
            injector.inject(this);
        } else {
            throw new IllegalArgumentException("activity must implements IInject");
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public int getViewRec() {
        return R.layout.fragment_category;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new CategoryPresenterImpl(this, mJobManager, mPreference);

        mSwipelayout.setProgressBackgroundColor(R.color.window_background);
        mSwipelayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CategoryAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.loadDataFromDB(this);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_category, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mPresenter.fetchRss();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDestory();
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ZhihuProvider.CATEGORY_CONTENT_URI, null, null, null,
            CategoryTable.PUBLISH_DATE + " desc");
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        mPresenter.loadDataFromDBComplete(data);
    }

    @Override public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override public void showArticles(int position) {
        Cursor cursor = mAdapter.getCursor();
        AssertUtils.requireNonNull(cursor, "category cursor must not null");
        if (cursor.moveToPosition(position)) {
            Category category = Category.fromCursor(cursor);
            EventBus.getDefault().post(new ReadArticlesEvent(category));
        }
    }

    @Override public void showFetchRssUI() {
        mSwipelayout.setRefreshing(true);
    }

    @Override public void hideFetchRssUI() {
        mSwipelayout.setRefreshing(false);
    }

    @Override public Activity getContext() {
        return getActivity();
    }

    @Override public boolean getVisiable() {
        return isVisible();
    }

    @Override public void switchImageMode(boolean imageVisiable) {

    }
}
