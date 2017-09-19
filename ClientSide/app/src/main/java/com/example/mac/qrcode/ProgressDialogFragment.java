package com.example.mac.qrcode;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by mac on 14/10/16.
 */


    public class ProgressDialogFragment extends DialogFragment {
        public static ProgressDialogFragment newInstance(int msgId) {
            ProgressDialogFragment fragment = new ProgressDialogFragment();

            Bundle args = new Bundle();
            args.putInt("msgId", msgId);

            fragment.setArguments(args);

            return fragment;
        }

        public ProgressDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int msgId = getArguments().getInt("msgId");
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getActivity().getResources().getString(msgId));
            return dialog;
        }

}
