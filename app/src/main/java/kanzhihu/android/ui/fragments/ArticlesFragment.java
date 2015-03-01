package kanzhihu.android.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import javax.inject.Inject;
import kanzhihu.android.R;
import kanzhihu.android.database.ShareActionProvider;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;
import kanzhihu.android.modules.IInject;
import kanzhihu.android.ui.adapter.ArticlesAdapter;
import kanzhihu.android.ui.adapter.base.ParallaxRecyclerAdapter;
import kanzhihu.android.ui.fragments.base.BaseFragment;
import kanzhihu.android.ui.presenters.ArticlesPresenter;
import kanzhihu.android.ui.presenters.impl.ArticlesPresenterImpl;
import kanzhihu.android.ui.views.ArticlesView;
import kanzhihu.android.utils.HardwareUtils;
import kanzhihu.android.utils.Preferences;
import kanzhihu.android.utils.ShareUtils;
import kanzhihu.android.utils.UrlBuilder;

/**
 * Created by Jiahui.wen on 2014/11/14.
 */
public class ArticlesFragment extends BaseFragment implements ArticlesView, ParallaxRecyclerAdapter.OnClickEvent {
    private static final String CATEGORY_ID = "categoryId";

    private Category mCategory;

    @InjectView(R.id.recyclerView_articles) RecyclerView recyclerView;

    @Inject Picasso picasso;

    @Inject Preferences mPreference;

    private ImageView mHeadView;

    private ArticlesAdapter mAdapter;

    private ArrayList<Article> articles = new ArrayList<Article>();

    private ArticlesPresenter mPresenter;

    private ShareActionProvider mShareActionProvider;
    private Article mShareArticle;
    private MenuItem mShareMenu;

    public static ArticlesFragment newInstance(Category category) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_ID, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IInject) {
            IInject injector = (IInject) activity;
            injector.inject(this);
        } else {
            throw new IllegalArgumentException("activity must implements IInject");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mCategory = getArguments().getParcelable(CATEGORY_ID);
        }
    }

    @Override public int getViewRec() {
        return R.layout.fragment_articles;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        mAdapter = new ArticlesAdapter(articles, picasso);
        recyclerView.setAdapter(mAdapter);
        View head = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_article_header, recyclerView, false);
        mHeadView = (ImageView) head.findViewById(R.id.iv_articles_screenshot);
        mAdapter.setParallaxHeader(head, recyclerView);
        mAdapter.setOnClickEvent(this);

        //设置图片的大小充满顶部
        Point point = HardwareUtils.getScrenSize(getActivity());
        int width = point.x;
        int height = width * 245 / 520;
        mHeadView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        mPresenter = new ArticlesPresenterImpl(this, mCategory, mPreference);
        mPresenter.loadArticles();

        switchImageMode(mPreference.imageMode());
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);

        mShareMenu = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareMenu);
        mShareActionProvider.setShareHistoryFileName(null);

        if (mShareArticle == null) {
            mShareMenu.setVisible(false);
        }
    }

    @Override public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        if (refreshItem != null) {
            refreshItem.setVisible(false);
        }
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
    }

    @Override public void onClick(View v, int position) {
        Article article = mAdapter.getItem(position);
        mPresenter.readArticle(article);
    }

    @Override public Activity getContext() {
        return getActivity();
    }

    @Override public Article getArticle(int position) {
        Article article;
        if (mAdapter.isHeaderExist()) {
            article = mAdapter.getItem(position - 1);
        } else {
            article = mAdapter.getItem(position);
        }
        return article;
    }

    @Override public void onLoadArticlesFinished(ArrayList<Article> articles) {
        mAdapter.setData(articles);
    }

    @Override public void articleChanged(int position) {
        if (mAdapter.isHeaderExist()) {
            mAdapter.notifyItemChanged(position + 1);
        } else {
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override public boolean getVisiable() {
        return isVisible();
    }

    @Override public void switchImageMode(boolean imageVisiable) {
        mHeadView.setVisibility(imageVisiable ? View.VISIBLE : View.GONE);
        mAdapter.setImageMode(imageVisiable);
        if (imageVisiable) {
            picasso.load(UrlBuilder.getScreenShotUrl(mCategory, true)).into(mHeadView);
        }
    }

    @Override public void createShareView(Article article) {
        mShareArticle = article;
        mShareMenu.setVisible(true);

        Intent shareIntent = ShareUtils.getShareIntent(mShareArticle);
        mShareActionProvider.setShareIntent(shareIntent);
        mShareActionProvider.showPopup();
    }

    @Override public void closeShareView() {
        mShareArticle = null;
        mShareMenu.setVisible(false);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDestory();
    }
}
