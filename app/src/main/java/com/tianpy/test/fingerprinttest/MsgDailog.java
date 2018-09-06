package com.tianpy.test.fingerprinttest;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by leo on 2018/9/3.
 */

public class MsgDailog extends AlertDialog {
    protected MsgDailog(@NonNull Context context) {
        super(context);
    }

    protected MsgDailog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MsgDailog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
