package kanzhihu.android.events;

/**
 * Created by Jiahui.wen on 2014/12/5.
 *
 * 查看知乎答案的作者
 */
public class ViewAuthorEvent {
    public int position;

    public ViewAuthorEvent() {
    }

    public ViewAuthorEvent(int position) {
        this.position = position;
    }
}
