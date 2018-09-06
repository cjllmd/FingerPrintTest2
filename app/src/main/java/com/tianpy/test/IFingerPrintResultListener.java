package com.tianpy.test;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by leo on 2018/8/31.
 */

public interface IFingerPrintResultListener {

    void onHardWareNotSupport();

    void onInSecurity();

    void onNoEnroll();

    void onSupport();

    void onAuthenticateStart();

    void onAuthenticateError(int errMsgId, CharSequence errString);

    void onAuthenticateFailed();

    void onAuthenticateHelp(int helpMsgId, CharSequence helpString);

    void onAuthenticateSucceeded(FingerprintManagerCompat.AuthenticationResult result);
}
