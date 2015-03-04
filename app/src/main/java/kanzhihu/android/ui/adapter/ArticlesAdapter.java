package kanzhihu.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.List;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.events.ListitemClickEvent;
import kanzhihu.android.events.MarkChangeEvent;
import kanzhihu.android.events.ShareArticleEvent;
import kanzhihu.android.events.ViewAuthorEvent;
import kanzhihu.android.models.Article;
import kanzhihu.android.ui.adapter.base.ParallaxRecyclerAdapter;

/**
 * Created by Jiahui.wen on 2014/11/15.
 */
public class ArticlesAdapter extends ParallaxRecyclerAdapter<Article>
    implements ParallaxRecyclerAdapter.RecyclerAdapterMethods {

    private boolean mImageMode;

    private Picasso picasso;

    public ArticlesAdapter(List<Article> data, Picasso picasso) {
        super(data);
        implementRecyclerAdapterMethods(this);
        this.picasso = picasso;
    }

    public void setImageMode(boolean imageVisiable) {
        mImageMode = imageVisiable;
        notifyDataSetChanged();
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

        if (article.idRead()) {
            holder.mTitle.setTextColor(AppConstant.TITLE_READ_COLOR);
            holder.mContent.setTextColor(AppConstant.CONTENT_READ_COLOR);
        } else {
            holder.mTitle.setTextColor(AppConstant.TITLE_UNREAD_COLOR);
            holder.mContent.setTextColor(AppConstant.CONTENT_UNREAD_COLOR);
        }

        holder.mAuthor.setText(article.writer);
        holder.mAgree.setText(String.valueOf(article.agreeCount));

        holder.unRegisterCheckedChangedListener();
        holder.mMarked.setChecked(article.marked > 0);
        holder.registerCheckedChangedListener();

        if (mImageMode) {
            picasso.load(String.format(AppConstant.IMAGE_LINK, article.imageLink)).into(holder.mAvatar);
        }
        holder.mAvatar.setVisibility(mImageMode ? View.VISIBLE : View.GONE);
    }

    @Override public int getItemCountImpl() {
        return getData() == null ? 0 : getData().size();
    }

    public static class ArticleHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

        @InjectView(R.id.tv_title)
        public TextView mTitle;

        @InjectView(R.id.tv_content)
        public TextView mContent;

        @InjectView(R.id.tv_author)
        public TextView mAuthor;

        @InjectView(R.id.tv_agree)
        public TextView mAgree;

        @InjectView(R.id.iv_article_img)
        public ImageView mAvatar;

        @InjectView(R.id.cb_mark)
        public CheckBox mMarked;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            //长按
            itemView.setOnLongClickListener(this);
            //点击浏览文章
            itemView.setOnClickListener(this);
            //点击作者，查看作者个人主页
            mAuthor.setOnClickListener(this);
            //点击作者头像，查看作者个人主页
            mAvatar.setOnClickListener(this);
        }

        public void registerCheckedChangedListener() {
            mMarked.setOnCheckedChangeListener(this);
        }

        public void unRegisterCheckedChangedListener() {
            mMarked.setOnCheckedChangeListener(null);
        }

        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_author:
                case R.id.iv_article_img:
                    EventBus.getDefault().post(new ViewAuthorEvent(getPosition()));
                    break;
                default:
                    EventBus.getDefault().post(new ListitemClickEvent(getPosition()));
            }
        }

        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            EventBus.getDefault().post(new MarkChangeEvent(getPosition(), isChecked));
        }

        @Override public boolean onLongClick(View v) {
            EventBus.getDefault().post(new ShareArticleEvent(getPosition()));
            return true;
        }
    }
}
