package com.pbn.org.crashsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.view.View;

import com.pbn.org.crashsdk.CrashSDK;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashSDK.init(getApplicationContext());
        HandlerThread thread = new HandlerThread("test_123");
        thread.start();
        Handler h = new Handler(thread.getLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true){
                    if(i>10000){
                        break;
                    }
                    SystemClock.sleep(500);
                }
            }
        });
    }

    public void test(View view) {
        int a = 1/0;
    }
}
