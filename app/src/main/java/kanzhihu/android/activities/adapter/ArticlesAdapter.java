package kanzhihu.android.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import java.util.List;
import kanzhihu.android.App;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.activities.adapter.base.ParallaxRecyclerAdapter;
import kanzhihu.android.models.Article;

/**
 * Created by Jiahui.wen on 2014/11/15.
 */
public class ArticlesAdapter extends ParallaxRecyclerAdapter<Article>
    implements ParallaxRecyclerAdapter.RecyclerAdapterMethods {

    public ArticlesAdapter(List<Article> data) {
        super(data);
        implementRecyclerAdapterMethods(this);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, int i) {
        return new ArticleHolder(
            LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_article, viewGroup, false));
    }

    @Override public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, int i) {
        Article article = getItem(i);
        ArticleHolder holder = (ArticleHolder) viewHolder;

        holder.mTitle.setText(article.title);
        holder.mContent.setText(article.summary);
        Picasso.with(App.getAppContext())
            .load(String.format(AppConstant.IMAGE_LINK, article.imageLink))
            .into(holder.mAvatar);
    }

    @Override public int getItemCountImpl() {
        return getData() == null ? 0 : getData().size();
    }

    static class ArticleHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_title)
        public TextView mTitle;

        @InjectView(R.id.tv_content)
        public TextView mContent;

        @InjectView(R.id.iv_article_img)
        public ImageView mAvatar;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
