package com.example.protego;

import androidx.fragment.app.DialogFragment;

//this interface is helpful to connect an event from the dialog to the host activity

public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
}
