package kanzhihu.android.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import kanzhihu.android.models.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Jiahui.wen on 2014/11/10.
 */
public class HtmlUtils {
    public static List<Article> parseArticles(String html) throws UnsupportedEncodingException {
        html = html.replaceAll("<p>", "");
        List<Article> articles = new ArrayList<Article>();

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("li");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Article article = parseArticle(element);
            articles.add(article);
        }

        return articles;
    }

    private static Article parseArticle(Element element) {
        Article article = new Article();
        Elements links = element.select("a[href]");
        for (int i = 0; i < links.size(); i++) {
            Element linkE = links.get(i);
            if (i == 0) {
                article.link = linkE.attr("href");
                article.title = linkE.text();
            } else if (i == 1) {
                article.writerLink = linkE.attr("href");
            } else if (i == 2) {
                article.writer = linkE.text();
            }
        }

        Element picEs = element.select("img").first();
        article.imageLink = picEs.attr("src");

        // 0 >>>> 刘十五: (1176)我以为我那么帅，她不会报警的。[阅读全文]
        // 1 >>>> (1176)
        // 2 >>>> [阅读全文]
        Elements contents = element.select("span");
        if (contents.size() > 2) {

            Element content = contents.get(0);
            String summaryText = content.text();
            article.summary = summaryText.substring(summaryText.indexOf(")") + 1, summaryText.indexOf("["));

            Element agree = contents.get(1);
            article.agreeCount = Integer.parseInt(agree.text().substring(1, agree.text().length() - 1));
        }

        return article;
    }
}
