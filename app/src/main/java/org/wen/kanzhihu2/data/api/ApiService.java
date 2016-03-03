package org.wen.kanzhihu2.data.api;

import org.wen.kanzhihu2.data.api.response.CheckNewResponse;
import org.wen.kanzhihu2.data.api.response.GetPostResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Jiahui.wen on 15-11-5.
 */
public interface ApiService {

    /**
     * 获取摘要列表
     *
     * @param timestamp 当前时间戳
     */
    @GET("getposts/{timestamp}")
    Observable<GetPostResponse> getPosts(@Path("timestamp") long timestamp);

    /**
     * 检查「看知乎」首页在指定时间之后有没有更新
     *
     * @param timestamp 时间戳
     */
    @GET("checknew/{timestamp}")
    Observable<CheckNewResponse> checkNew(@Path("timestamp") long timestamp);
}
