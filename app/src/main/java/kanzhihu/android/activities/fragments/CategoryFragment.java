package kanzhihu.android.activities.fragments;

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
import de.greenrobot.event.EventBus;
import kanzhihu.android.R;
import kanzhihu.android.activities.adapter.CategoryAdapter;
import kanzhihu.android.activities.adapter.base.CursorRecyclerViewAdapter;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.activities.presenters.CategoryPresenter;
import kanzhihu.android.activities.presenters.impl.CategoryPresenterImpl;
import kanzhihu.android.activities.views.CategoryView;
import kanzhihu.android.database.ZhihuProvider;
import kanzhihu.android.database.table.CategoryTable;
import kanzhihu.android.events.ReadArticlesEvent;
import kanzhihu.android.models.Category;
import kanzhihu.android.utils.AssertUtils;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class CategoryFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, CategoryView {

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swiperefreshlayout) SwipeRefreshLayout mSwipelayout;

    private CursorRecyclerViewAdapter mAdapter;
    private CategoryPresenter mPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public int getViewRec() {
        return R.layout.fragment_category;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new CategoryPresenterImpl(this);
        mPresenter.bindEvent();

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CategoryAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.loadDataFromDB(this);
        mPresenter.init();
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

        mPresenter.unBindEvent();
        mPresenter.unloadDataFromDb();
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
}
