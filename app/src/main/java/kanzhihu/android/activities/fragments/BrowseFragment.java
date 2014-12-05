package kanzhihu.android.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.activities.presenters.BrowsePresenter;
import kanzhihu.android.activities.presenters.impl.BrowsePresenterImpl;
import kanzhihu.android.activities.views.BrowseView;
import kanzhihu.android.models.Article;
import kanzhihu.android.utils.ShareUtils;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public class BrowseFragment extends BaseFragment implements BrowseView, ObservableScrollViewCallbacks {

    private Article mArticle;
    private BrowsePresenter mPresenter;
    private MenuItem mMarkItem;
    private ShareActionProvider mShareActionProvider;

    public static Fragment newInstance(Article article) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle data = new Bundle();
        data.putParcelable(AppConstant.KEY_ARTICLE, article);
        fragment.setArguments(data);

        return fragment;
    }

    @InjectView(R.id.webview) ObservableWebView mWebView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mArticle = getArguments().getParcelable(AppConstant.KEY_ARTICLE);
    }

    @Override public int getViewRec() {
        return R.layout.fragment_browse;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new BrowsePresenterImpl(this, mArticle);

        if (mArticle != null && !TextUtils.isEmpty(mArticle.link)) {
            mWebView.loadUrl(mArticle.link);
        }
        mWebView.setScrollViewCallbacks(this);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_browse, menu);

        mMarkItem = menu.findItem(R.id.action_mark);
        setMarkStatue(mArticle.marked > 0);

        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareHistoryFileName(null);
        setShareIntent();
    }

    private void setShareIntent() {
        Intent shareIntent = ShareUtils.getShareIntent(mArticle);
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_mark) {
            mPresenter.onMarkChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override public void onDownMotionEvent() {

    }

    @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    @Override public Activity getContext() {
        return getActivity();
    }

    @Override public boolean getVisiable() {
        return isVisible();
    }

    @Override public void onImageModeChange(boolean imageVisiable) {

    }

    @Override public void setMarkStatue(boolean marked) {
        if (mMarkItem != null) {
            mMarkItem.setIcon(marked ? R.drawable.ic_mark_marked_hl : R.drawable.ic_mark_normal);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDestory();
    }
}
