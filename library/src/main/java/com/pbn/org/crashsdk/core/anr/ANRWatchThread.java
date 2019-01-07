package com.pbn.org.crashsdk.core.anr;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.io.File;

/**
 * function:
 *
 * @author peiboning
 * @DATE 2019/01/03
 */
public class ANRWatchThread extends Thread {
    public static final String ANR_PATH = "/data/anr/traces.txt";
    private Handler handler;
    private volatile int single = 0;
    private long watchInterval = 5000L;
    private volatile long lastModified;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            single = single + 1;
        }
    };

    public ANRWatchThread(){
        setName("ANRWatchThread");
        handler = new Handler(Looper.getMainLooper());
        lastModified = getLastModified();
    }

    @Override
    public void run() {
        while (true){
            int key;
            do{
                key = single;
                handler.post(runnable);
                SystemClock.sleep(watchInterval);
            }while (key != single);


            if(isAnrFileAvailable()){
                int checkTimes = 0;
                while (key == single){
                    if(isAnrModified()){
                        checkTimes++;
                        lastModified = getLastModified();
                        ANRWatchdog.getInstance().postMsgTocollectANRInfo();
                    }
                }
            }
        }


    }

    private boolean isAnrModified(){
        long modifiedTime = getLastModified();
        return modifiedTime!=0 && lastModified!=0&& lastModified!=modifiedTime;
    }

    private boolean isAnrFileAvailable(){
        File file = new File(ANR_PATH);
        return file.exists() && file.canRead();
    }

    private long getLastModified() {
        File file = new File(ANR_PATH);
        return file.exists() ? file.lastModified() : 0;

    }
}
