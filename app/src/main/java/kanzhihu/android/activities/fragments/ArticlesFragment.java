package kanzhihu.android.activities.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import butterknife.InjectView;
import java.util.ArrayList;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.adapter.ArticlesAdapter;
import kanzhihu.android.activities.adapter.base.ParallaxRecyclerAdapter;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.jobs.LoadArticlesTask;
import kanzhihu.android.models.Article;
import kanzhihu.android.models.Category;

/**
 * Created by Jiahui.wen on 2014/11/14.
 */
public class ArticlesFragment extends BaseFragment implements ParallaxRecyclerAdapter.OnClickEvent, Handler.Callback {
    private static final String CATEGORY_ID = "categoryId";

    private Category mCategory;

    @InjectView(R.id.recyclerView_articles) RecyclerView recyclerView;

    private ImageView mHeadView;

    private ArticlesAdapter mAdapter;

    private ArrayList<Article> articles = new ArrayList<Article>();

    private Handler mHandler;

    private LoadArticlesTask mLoadTask;

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
        setHasOptionsMenu(false);
        mHandler = new Handler(this);

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
        mHeadView = (ImageView) view.findViewById(R.id.iv_articles_screenshot);
        mAdapter.setParallaxHeader(head, recyclerView);

        mLoadTask = new LoadArticlesTask(mHandler);
        mLoadTask.execute(mCategory._id);
    }

    @Override public void onClick(View v, int position) {

    }

    @Override public boolean handleMessage(Message msg) {
        if (msg.what == AppConstant.LOAD_ARTICLES_OK) {
            articles = msg.getData().getParcelableArrayList(AppConstant.KEY_ARTICLES);
            mAdapter.setData(articles);
            return true;
        }
        return false;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        if (mLoadTask != null && mLoadTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadTask.cancel(true);
            mLoadTask = null;
        }
    }
}
