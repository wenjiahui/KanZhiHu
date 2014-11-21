package kanzhihu.android.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.BrowseActivity;
import kanzhihu.android.activities.adapter.SearchAdapter;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.activities.presenters.QueryPresenter;
import kanzhihu.android.activities.presenters.impl.QueryPresenterImpl;
import kanzhihu.android.activities.views.QueryView;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.PreferenceUtils;

/**
 * Created by Jiahui.wen on 2014/11/19.
 */
public class SearchFragment extends BaseFragment implements QueryView {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private SearchView mSearchView;

    private SearchAdapter mAdapter;

    private QueryPresenter mPresenter;

    @InjectView(R.id.recyclerView_mark) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public int getViewRec() {
        return R.layout.fragment_search;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(mPresenter.getQueryTextListener());

        MenuItemCompat.setOnActionExpandListener(searchItem, mPresenter.getActionExpandListener());
        MenuItemCompat.expandActionView(searchItem);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SearchAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new QueryPresenterImpl(this);
        mPresenter.loadInitData();
        mPresenter.bindEvent();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unBindEvent();
    }

    @Override public void onSearchViewClosed(MenuItem menuItem) {
        getActivity().onBackPressed();
    }

    @Override public Activity getContext() {
        return getActivity();
    }

    @Override public void swapCursor(Cursor cursor) {
        mAdapter.changeCursor(cursor);
    }

    @Override public boolean getVisiable() {
        return isVisible();
    }

    @Override public void showArticle(int position) {
        if (mAdapter.getCursor().moveToPosition(position)) {
            Article article = Article.fromCursor(mAdapter.getCursor());
            if (PreferenceUtils.external_open()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.link));
                getActivity().startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), BrowseActivity.class);
                intent.putExtra(AppConstant.KEY_ARTICLE, article);
                startActivity(intent);
            }
        }
    }

    @Override public void onQueryTextChange(String newText) {
        mAdapter.setCurFilter(newText);
    }
}
