package com.pbn.org.crashsdk;

import android.content.Context;

import com.pbn.org.crashsdk.core.crash.CrashHandler;
import com.pbn.org.crashsdk.core.crash.ICrashFilter;

/**
 * function:
 *  SDK初始类
 * @author peiboning
 * @DATE 2019/01/02
 */
public class CrashSDK {
    public static Context sContext;
    public static void init(Context context){
        sContext = context.getApplicationContext();
        CrashHandler.getInstance();
        CrashHandler.getInstance().setCrashFilter(new ICrashFilter() {
            @Override
            public boolean filter(Thread thread, Throwable throwable) {
                return true;
            }
        });
    }
}
