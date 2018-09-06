package com.tianpy.test.fingerprinttest;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tianpy.test.FingerPrintMgr;
import com.tianpy.test.IFingerPrintResultListener;

public class MainActivity extends AppCompatActivity {
    MsgDailog alertDialog;
    private FingerPrintMgr fingerPrintMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                fingerPrintMgr=FingerPrintMgr.getInstance(MainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerPrintMgr.fingerPrintVerify(new IFingerPrintResultListener() {
                        @Override
                        public void onHardWareNotSupport() {
                            Log.d("finger","硬件不支持");
                            noticeShow("硬件不支持");
                        }

                        @Override
                        public void onInSecurity() {
                            Log.d("finger","fail");
                        }

                        @Override
                        public void onNoEnroll() {
                            Log.d("finger","未设置指纹");noticeShow("未设置指纹");
                        }

                        @Override
                        public void onSupport() {
                            showDailog();
                            Log.d("finger","");
                        }

                        @Override
                        public void onAuthenticateStart() {
                            Log.d("finger","");
                        }

                        @Override
                        public void onAuthenticateError(int errMsgId, CharSequence errString) {
                            Log.d("finger",errMsgId+errString.toString()+"");
                            noticeShow(errMsgId+errString.toString()+"");
                        }

                        @Override
                        public void onAuthenticateFailed() {
                            Log.d("finger","fail");noticeShow("fail");
                            dismiss();
                        }

                        @Override
                        public void onAuthenticateHelp(int helpMsgId, CharSequence helpString) {
                            Log.d("finger",helpMsgId+helpString.toString()+"");noticeShow(helpMsgId+helpString.toString()+"");
                        }

                        @Override
                        public void onAuthenticateSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                            Log.d("finger",result.toString());
                            noticeShow("success");
                            dismiss();
                        }
                    });
                }else{
                    noticeShow("硬件不支持");
                }
            }
        });
    }


    public void noticeShow(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }


    public void showDailog(){

            if(alertDialog==null){
                alertDialog=new MsgDailog(MainActivity.this);
                alertDialog.setTitle("指纹认证");
                alertDialog.setMessage("请按指纹键进行认证");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(MsgDailog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FingerPrintMgr.getInstance(MainActivity.this).cancelAuthenticate();;
                    }
                });
            }
            if(alertDialog.isShowing())
                return;
            alertDialog.show();


    }

    public void dismiss(){
        if(alertDialog!=null){
            alertDialog.dismiss();;
            alertDialog=null;
        }
    }
}
