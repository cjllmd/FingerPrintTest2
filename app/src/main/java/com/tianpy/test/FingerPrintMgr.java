package com.tianpy.test;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by leo on 2018/8/31.
 */

public class FingerPrintMgr {
    private static FingerPrintMgr mFingerPrintMgr;
    private FingerprintManagerCompat mFingerprintManager;
    private KeyguardManager mKeyManager;
    private CancellationSignal mCancellationSignal;
    private Context mContext;

    private FingerPrintMgr(Context ctx) {
        mContext = ctx;
        mFingerprintManager = FingerprintManagerCompat.from(mContext);
        mKeyManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);

    }

    public static FingerPrintMgr getInstance(Context context){
        if(mFingerPrintMgr==null){
            mFingerPrintMgr=new FingerPrintMgr(context);
        }
        return mFingerPrintMgr;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void fingerPrintVerify(final IFingerPrintResultListener listener) {
        if (listener != null) {
            if (!isHardwareDetected()) {
                return;
            }
            if (!isHasEnrolledFingerprints()) {
                listener.onNoEnroll();
                return;
            }
            if (!isKeyguardSecure()) {
                listener.onInSecurity();
                return;
            }
            listener.onSupport();

            listener.onAuthenticateStart();
            if (mCancellationSignal == null) {
                mCancellationSignal = new CancellationSignal();
            }
            FingerprintManagerCompat.CryptoObject cryptoObject=new CryptoObjectCreator(new CryptoObjectCreator.ICryptoObjectCreateListener() {
                @Override
                public void onDataPrepared(FingerprintManagerCompat.CryptoObject cryptoObject) {
                    auth(listener, cryptoObject);
                }
            }).getCryptoObject();

        }

    }
    private void auth(final IFingerPrintResultListener listener, FingerprintManagerCompat.CryptoObject cryptoObject){
        try {
            mFingerprintManager.authenticate(cryptoObject, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {


                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    listener.onAuthenticateError(errMsgId, errString);
                }

                @Override
                public void onAuthenticationFailed() {
                    listener.onAuthenticateFailed();
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    listener.onAuthenticateHelp(helpMsgId, helpString);

                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    listener.onAuthenticateSucceeded(result);
                }

            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isHasEnrolledFingerprints() {
        try {
            return mFingerprintManager.hasEnrolledFingerprints();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHardwareDetected() {
        try {
            return mFingerprintManager.isHardwareDetected();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isKeyguardSecure() {
        try {
            return mKeyManager.isKeyguardSecure();
        } catch (Exception e) {
            return false;
        }

    }

    public void cancelAuthenticate() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }
}
