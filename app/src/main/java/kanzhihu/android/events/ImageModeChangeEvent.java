package kanzhihu.android.events;

/**
 * Created by Jiahui.wen on 2014/12/5.
 *
 * 是否显示图片的模式改变
 */
public class ImageModeChangeEvent {

    public boolean imageVisiable;

    public ImageModeChangeEvent() {
    }

    public ImageModeChangeEvent(boolean imageVisiable) {
        this.imageVisiable = imageVisiable;
    }
}
