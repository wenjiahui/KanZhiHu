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
import java.util.ArrayList;
import java.util.List;
import kanzhihu.android.AppConstant;
import kanzhihu.android.events.FetchedRssEvent;
import kanzhihu.android.events.FetchingRssEvent;
import kanzhihu.android.managers.HttpClientManager;
import kanzhihu.android.models.Item;
import kanzhihu.android.utils.AppLogger;
import kanzhihu.android.utils.HtmlUtils;
import kanzhihu.android.utils.TimeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

    }

    @Override protected boolean shouldReRunOnThrowable(Throwable throwable) {
        AppLogger.d("FetchRssJob", throwable);
        return false;
    }

    private void parseAndStore(InputStream rssStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(rssStream, "UTF-8");
        int eventType = parser.getEventType();
        List<Item> items = new ArrayList<Item>();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (AppConstant.ITEM_TAG.ITEM.equalsIgnoreCase(parser.getName())) {
                        //开始解析Item数据
                        Item item = parseItem(parser);
                        items.add(item);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        AppLogger.d("parse items size: >>>> " + items.size());
    }

    private Item parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        Item item = new Item();
        int eventType;
        boolean flag = true;
        while (flag) {
            eventType = parser.next();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    setItemValue(item, parser);
                    break;
                case XmlPullParser.END_TAG:
                    if (AppConstant.ITEM_TAG.ITEM.equalsIgnoreCase(parser.getName())) {
                        flag = false;
                    }
                    break;
            }
        }
        return item;
    }

    private void setItemValue(Item item, XmlPullParser parser) throws IOException, XmlPullParserException {
        String tag = parser.getName();
        //get the tag's value
        parser.next();
        String value = parser.getText();
        if (AppConstant.ITEM_TAG.TITLE.equalsIgnoreCase(tag)) {
            item.title = value;
        } else if (AppConstant.ITEM_TAG.LINK.equalsIgnoreCase(tag)) {
            item.link = value;
        } else if (AppConstant.ITEM_TAG.COMMENTS_LINK.equalsIgnoreCase(tag)) {
            //  <comments>http://www.kanzhihu.com/archive-2014-11-07.html#comments</comments>
            //  <slash:comments>0</slash:comments>
            //  两者的value是一样的，需要判断命名空间an
            if (TextUtils.isEmpty(parser.getNamespace())) {
                item.commentsLink = value;
            } else {
                item.comments = value;
            }
        } else if (AppConstant.ITEM_TAG.PUBDATE.equalsIgnoreCase(tag)) {
            item.pubDate = TimeUtils.getTimeMillis(value);
        } else if (AppConstant.ITEM_TAG.CREATOR.equalsIgnoreCase(tag)) {
            item.creator = value;
        } else if (AppConstant.ITEM_TAG.CATEGORY.equalsIgnoreCase(tag)) {
            item.category = value;
        } else if (AppConstant.ITEM_TAG.GUID.equalsIgnoreCase(tag)) {
            item.guid = value;
        } else if (AppConstant.ITEM_TAG.DESCRIPTION.equalsIgnoreCase(tag)) {
            item.description = value;
        } else if (AppConstant.ITEM_TAG.ENCODED.equalsIgnoreCase(tag)) {
            item.encoded = value;
            item.articles = HtmlUtils.parseArticles(value);
        } else if (AppConstant.ITEM_TAG.COMMENTS_RSS_LINK.equalsIgnoreCase(tag)) {
            item.commentRssLink = value;
        }
    }
}
