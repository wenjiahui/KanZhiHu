package org.wen.kanzhihu2.data.api.response;

import java.util.List;
import org.wen.kanzhihu2.data.Article;

/**
 * Created by Jiahui.wen on 15-11-5.
 */
public class GetPostResponse extends BaseResponse {

    public int count;
    public List<Article> articles;
}
