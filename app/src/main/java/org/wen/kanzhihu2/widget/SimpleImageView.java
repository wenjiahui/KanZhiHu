package org.wen.kanzhihu2.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 二次封装的ImageView，方便切换图片加载框架。
 *
 * Created by Jiahui.wen on 16-3-4.
 */
public class SimpleImageView extends SimpleDraweeView {

    public SimpleImageView(Context context) {
        super(context);
    }

    public SimpleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SimpleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return SimpleImageView.class.getName();
    }
}
