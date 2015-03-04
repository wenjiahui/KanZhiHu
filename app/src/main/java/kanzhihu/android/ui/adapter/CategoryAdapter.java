package kanzhihu.android.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import kanzhihu.android.R;
import kanzhihu.android.events.ListItemClickEvent;
import kanzhihu.android.models.Category;
import kanzhihu.android.ui.adapter.base.CursorRecyclerViewAdapter;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public class CategoryAdapter extends CursorRecyclerViewAdapter {

    public CategoryAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        Category category = Category.fromCursor(cursor);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.mTitle.setText(category.title);
        holder.mDescription.setText(category.description);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_category, viewGroup, false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.tv_title)
        public TextView mTitle;

        @InjectView(R.id.tv_description)
        public TextView mDescription;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
            v.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            EventBus.getDefault().post(new ListItemClickEvent(getPosition()));
        }
    }
}
