package kanzhihu.android.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import kanzhihu.android.App;
import kanzhihu.android.R;

public class ToastUtils {

    private static Style Crouton_Alert_Style;

    static {
        Crouton_Alert_Style = new Style.Builder().setBackgroundColor(R.color.toast_background)
            .setHeightDimensionResId(R.dimen.toast_height)
            .setTextSize(13)
            .setPaddingInPixels(0)
            .build();
    }

    private ToastUtils() {
    }

    private static void show(Context context, @StringRes int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(@StringRes int resId) {
        Toast.makeText(App.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(@StringRes int resId) {
        Toast.makeText(App.getAppContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void alert(String messaeg) {
        Crouton.makeText(App.getInstance().topActivity(), messaeg, Crouton_Alert_Style).show();
    }

    public static void alert(@StringRes int messaegRes) {
        Crouton.makeText(App.getInstance().topActivity(), messaegRes, Crouton_Alert_Style).show();
    }
}
