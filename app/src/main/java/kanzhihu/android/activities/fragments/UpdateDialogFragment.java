package kanzhihu.android.activities.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;
import kanzhihu.android.AppConstant;
import kanzhihu.android.R;
import kanzhihu.android.listeners.DialogSelectListener;
import kanzhihu.android.utils.Preferences;

public class UpdateDialogFragment extends DialogFragment {

    private DialogSelectListener mListener;

    @Inject Preferences mPreference;

    public void setListener(DialogSelectListener mListener) {
        this.mListener = mListener;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity()).icon(R.drawable.ic_launcher)
            .title(R.string.app_name)
            .content(R.string.find_new_app_version)
            .positiveText(R.string.confirm)
            .negativeText(R.string.cancel)
            .neutralText(R.string.ignore)
            .callback(new MaterialDialog.FullCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    if (mListener != null) {
                        mListener.onConfirm();
                    }
                }

                @Override
                public void onNeutral(MaterialDialog dialog) {
                    if (mListener != null) {
                        int version = mPreference.getInt(AppConstant.KEY_NEW_VERSION, -1);
                        mPreference.setInt(AppConstant.KEY_IGNORE_VERSION, version);
                        mListener.onCancel();
                    }
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    if (mListener != null) {
                        mListener.onCancel();
                    }
                }
            });

        return builder.build();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}
