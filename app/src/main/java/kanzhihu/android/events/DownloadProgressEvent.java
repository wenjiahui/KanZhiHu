package kanzhihu.android.events;

/**
 * Created by Jiahui.wen on 2014/11/12.
 */
public class DownloadProgressEvent implements Cloneable {

    public int progress;

    @Override protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
