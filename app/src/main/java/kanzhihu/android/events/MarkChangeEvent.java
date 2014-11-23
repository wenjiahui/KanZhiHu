package kanzhihu.android.events;

/**
 * Created by Jiahui.wen on 2014/11/21.
 */
public class MarkChangeEvent {

    public int position;

    public boolean isChecked;

    public MarkChangeEvent() {
    }

    public MarkChangeEvent(int position, boolean isChecked) {
        this.position = position;
        this.isChecked = isChecked;
    }
}
