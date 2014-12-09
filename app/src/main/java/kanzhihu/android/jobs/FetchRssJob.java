package kanzhihu.android.jobs;

import android.text.TextUtils;
import android.util.Xml;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.events.FetchedRssEvent;
import kanzhihu.android.events.NetworkErrorEvent;
import kanzhihu.android.managers.HttpClientManager;
import kanzhihu.android.managers.NetworkManager;
import kanzhihu.android.models.Category;
import kanzhihu.android.utils.AppLogger;
import kanzhihu.android.utils.HtmlUtils;
import kanzhihu.android.utils.PersistUtils;
import kanzhihu.android.utils.TimeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by Jiahui.wen on 2014/11/7.
 */
public class FetchRssJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;

    public FetchRssJob() {
        super(new Params(Priority.HIGH).groupBy("fetch_rss"));
        id = jobCounter.incrementAndGet();
    }

    @Override public void onAdded() {

    }

    @Override public void onRun() throws Throwable {
        if (!NetworkManager.isConnected()) {
            EventBus.getDefault().post(new NetworkErrorEvent(R.string.network_error));
            throw new IllegalAccessException("network not avaliable");
        }

        if (id != jobCounter.get()) {
            return;
        }

        Request request = new Request.Builder().url(AppConstant.RSS_URL).build();

        Response response = HttpClientManager.request(request);
        InputStream rssStream;
        try {
            rssStream = response.body().byteStream();
            parseAndStore(rssStream);
        } finally {
            response.body().close();
        }

        EventBus.getDefault().post(new FetchedRssEvent());
    }

    @Override protected void onCancel() {
        EventBus.getDefault().post(new FetchedRssEvent());
    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            EventBus.getDefault().post(new NetworkErrorEvent(R.string.network_error));
        }
        return false;
    }

    public void parseAndStore(InputStream rssStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(rssStream, "UTF-8");
        int eventType = parser.getEventType();
        List<Category> categories = new ArrayList<Category>();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (AppConstant.ITEM_TAG.ITEM.equalsIgnoreCase(parser.getName())) {
                        //开始解析Item数据
                        Category category = parseItem(parser);
                        categories.add(category);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        AppLogger.d("parse items size: >>>> " + categories.size());
        PersistUtils.store(categories);
    }

    private Category parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        Category category = new Category();
        int eventType;
        boolean flag = true;
        while (flag) {
            eventType = parser.next();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    setItemValue(category, parser);
                    break;
                case XmlPullParser.END_TAG:
                    if (AppConstant.ITEM_TAG.ITEM.equalsIgnoreCase(parser.getName())) {
                        flag = false;
                    }
                    break;
            }
        }
        return category;
    }

    private void setItemValue(Category category, XmlPullParser parser) throws IOException, XmlPullParserException {
        String tag = parser.getName();
        //get the tag's value
        parser.next();
        String value = parser.getText();
        if (AppConstant.ITEM_TAG.TITLE.equalsIgnoreCase(tag)) {
            category.title = value;
        } else if (AppConstant.ITEM_TAG.LINK.equalsIgnoreCase(tag)) {
            category.link = value;
        } else if (AppConstant.ITEM_TAG.COMMENTS_LINK.equalsIgnoreCase(tag)) {
            //  <comments>http://www.kanzhihu.com/archive-2014-11-07.html#comments</comments>
            //  <slash:comments>0</slash:comments>
            //  两者的value是一样的，需要判断命名空间an
            if (TextUtils.isEmpty(parser.getNamespace())) {
                category.commentsLink = value;
            } else {
                category.comments = value;
            }
        } else if (AppConstant.ITEM_TAG.PUBDATE.equalsIgnoreCase(tag)) {
            category.pubDate = TimeUtils.getTimeMillis(value);
        } else if (AppConstant.ITEM_TAG.CREATOR.equalsIgnoreCase(tag)) {
            category.creator = value;
        } else if (AppConstant.ITEM_TAG.GUID.equalsIgnoreCase(tag)) {
            category.guid = value;
        } else if (AppConstant.ITEM_TAG.DESCRIPTION.equalsIgnoreCase(tag)) {
            category.description = value;
        } else if (AppConstant.ITEM_TAG.ENCODED.equalsIgnoreCase(tag)) {
            category.encoded = value;
            category.articles = HtmlUtils.parseArticles(value);
        } else if (AppConstant.ITEM_TAG.COMMENTS_RSS_LINK.equalsIgnoreCase(tag)) {
            category.commentRssLink = value;
        } else if (AppConstant.ITEM_TAG.CATEGORY.equalsIgnoreCase(tag)) {
            category.categoryName = value;
        }
    }
}
