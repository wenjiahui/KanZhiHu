package kanzhihu.android.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import butterknife.InjectView;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.fragments.base.BaseFragment;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/17.
 */
public class BrowseFragment extends BaseFragment {

    private Article mArticle;

    public static Fragment newInstance(Article article) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle data = new Bundle();
        data.putParcelable(AppConstant.KEY_ARTICLE, article);
        fragment.setArguments(data);

        return fragment;
    }

    @InjectView(R.id.webview) WebView mWebView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArticle = getArguments().getParcelable(AppConstant.KEY_ARTICLE);
    }

    @Override public int getViewRec() {
        return R.layout.fragment_browse;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mArticle != null && !TextUtils.isEmpty(mArticle.link)) {
            mWebView.loadUrl(mArticle.link);
        }
    }
}
