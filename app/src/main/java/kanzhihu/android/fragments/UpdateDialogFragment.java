package kanzhihu.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import kanzhihu.android.R;
import kanzhihu.android.listeners.DialogSelectListener;

public class UpdateDialogFragment extends DialogFragment {

    private DialogSelectListener mListener;

    public void setListener(DialogSelectListener mListener) {
        this.mListener = mListener;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.find_new_app_version);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onConfirm();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onCancel();
                }
            }
        });
        return builder.create();
    }

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //    return inflater.inflate(R.layout.fragment_update_dialog, container, false);
    //}

    @Override public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}
