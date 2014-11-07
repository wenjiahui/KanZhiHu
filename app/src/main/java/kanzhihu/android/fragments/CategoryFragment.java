package kanzhihu.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import kanzhihu.android.R;
import kanzhihu.android.events.FetchedRssEvent;
import kanzhihu.android.events.FetchingRssEvent;
import kanzhihu.android.fragments.base.BaseFragment;
import kanzhihu.android.jobs.FetchRssJob;
import kanzhihu.android.managers.BackThreadManager;

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
        BackThreadManager.getJobManager().addJob(new FetchRssJob());
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(FetchingRssEvent event) {

    }

    public void onEventMainThread(FetchedRssEvent event) {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
