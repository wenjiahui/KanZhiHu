package kanzhihu.android.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import kanzhihu.android.AppConstant;
import kanzhihu.android.events.FetchedRssEvent;
import kanzhihu.android.events.FetchingRssEvent;
import kanzhihu.android.managers.HttpClientManager;
import kanzhihu.android.utils.AppLogger;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class FetchRssJob extends Job {

    public FetchRssJob() {
        super(new Params(Priority.MID).requireNetwork());
    }

    @Override public void onAdded() {
        EventBus.getDefault().post(new FetchingRssEvent());
    }

    @Override public void onRun() throws Throwable {

        Request request = new Request.Builder().url(AppConstant.RSS_URL).build();

        Response response = HttpClientManager.request(request);
        String rss;
        try {
            rss = response.body().string();
        } finally {
            response.body().close();
        }
        parseAndStore(rss);

        EventBus.getDefault().post(new FetchedRssEvent());
    }

    @Override protected void onCancel() {

    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        AppLogger.d("FetchRssJob", throwable);
        return false;
    }

    private void parseAndStore(String rss) {

    }
}
