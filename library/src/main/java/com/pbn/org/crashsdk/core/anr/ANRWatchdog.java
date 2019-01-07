package com.pbn.org.crashsdk.core.anr;

import android.os.Build;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


import org.json.JSONObject;

import java.io.File;
import java.util.regex.Pattern;

/**
 * function:
 *  监控入口类
 * @author peiboning
 * @DATE 2019/01/03
 */
public class ANRWatchdog {

    private static final int COLLECT_ANR_INFO = 1000;

    private static ANRWatchdog sInstance = null;
    private HandlerThread thread;
    private Handler handler;
    private ANRWatchThread anrWatchThread;
    public static ANRWatchdog getInstance() {
        if (null == sInstance) {
            synchronized (ANRWatchdog.class) {
                if (null == sInstance) {
                    sInstance = new ANRWatchdog();
                }
            }
        }
        return sInstance;
    }

    private ANRWatchdog() {
        thread = new HandlerThread("crash_sdk_ANRWatchdog_task");
        thread.start();
        handler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                if(COLLECT_ANR_INFO == what){
                    collectANRInfo();
                }
            }
        };
    }

    public void start(){
        if(Build.VERSION.SDK_INT < 21){
            ANRFileProvider provider = new ANRFileProvider("/data/anr/", FileObserver.CLOSE_WRITE);
            provider.startWatching();
        }else{
            anrWatchThread = new ANRWatchThread();
            anrWatchThread.start();
        }
    }

    public void postMsgTocollectANRInfo() {
        handler.obtainMessage(COLLECT_ANR_INFO);
    }
    public void collectANRInfo() {

        //TODO 解析trace文件，或者直接上传trace文件
        File file = new File(ANRWatchThread.ANR_PATH);
        if(file.exists() && file.canRead()){
            JSONObject anrInfoObj = new JSONObject();
            String anr_title_p_str = "-{5}\\spid\\s\\d+\\sat\\s\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+\\s-{5}";
            String anr_end_p_str = "-{5}\\send\\s\\d+\\s-{5}";
            Pattern anr_title_pattern = Pattern.compile(anr_title_p_str);

        }
    }
}