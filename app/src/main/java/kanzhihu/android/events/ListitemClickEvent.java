package kanzhihu.android.events;

/**
 * Created by Jiahui.wen on 2014/11/13.
 *
 * 点击RecyclerView的item触发的事件
 */
public class ListitemClickEvent {

    public int position = -1;

    public ListitemClickEvent() {
    }

    public ListitemClickEvent(int position) {
        this.position = position;
    }
}
