package kanzhihu.android.activities.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.BrowseActivity;
import kanzhihu.android.activities.adapter.ArticlesAdapter;
import kanzhihu.android.activities.adapter.base.ParallaxRecyclerAdapter;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.activities.presenters.ArticlesPresenter;
import kanzhihu.android.activities.presenters.impl.ArticlesPresenterImpl;
import kanzhihu.android.activities.views.ArticlesView;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;
import kanzhihu.android.utils.HardwareUtils;
import kanzhihu.android.utils.PreferenceUtils;
import kanzhihu.android.utils.UrlBuilder;

/**
 * Created by Jiahui.wen on 2014/11/14.
 */
public class ArticlesFragment extends BaseFragment implements ParallaxRecyclerAdapter.OnClickEvent, ArticlesView {
    private static final String CATEGORY_ID = "categoryId";

    private Category mCategory;

    @InjectView(R.id.recyclerView_articles) RecyclerView recyclerView;

    private ImageView mHeadView;

    private ArticlesAdapter mAdapter;

    private ArrayList<Article> articles = new ArrayList<Article>();

    private ArticlesPresenter mPresenter;

    public static ArticlesFragment newInstance(Category category) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_ID, category);
        fragment.setArguments(args);
        return fragment;
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

        mAdapter = new ArticlesAdapter(articles);
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

        mPresenter = new ArticlesPresenterImpl(this, mCategory);
        mPresenter.loadArticles();

        Picasso.with(App.getAppContext()).load(UrlBuilder.getScreenShotUrl(mCategory, true)).into(mHeadView);
    }

    @Override public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        if (refreshItem != null) {
            refreshItem.setVisible(false);
        }
    }

    @Override public void onClick(View v, int position) {
        Article article = mAdapter.getItem(position);
        if (PreferenceUtils.external_open()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.link));
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), BrowseActivity.class);
            intent.putExtra(AppConstant.KEY_ARTICLE, article);
            startActivity(intent);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDestory();
    }

    @Override public void onLoadArticlesFinished(ArrayList<Article> articles) {
        mAdapter.setData(articles);
    }
}
