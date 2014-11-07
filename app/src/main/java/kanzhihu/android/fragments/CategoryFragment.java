package kanzhihu.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.InjectView;
import kanzhihu.android.R;
import kanzhihu.android.fragments.base.BaseFragment;

/**
 * Created by Jiahui.wen on 2014/11/6.
 */
public class CategoryFragment extends BaseFragment {

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override public int getViewRec() {
        return R.layout.fragment_category;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
