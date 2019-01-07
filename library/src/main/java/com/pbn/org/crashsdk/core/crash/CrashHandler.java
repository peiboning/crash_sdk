package com.pbn.org.crashsdk.core.crash;

import android.util.Log;

import com.pbn.org.crashsdk.core.utils.FileUtils;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * function:
 * 异常处理类
 * @author peiboning
 * @DATE 2019/01/02
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler sInstance = null;

    public static CrashHandler getInstance() {
        if (null == sInstance) {
            synchronized (CrashHandler.class) {
                if (null == sInstance) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    private Thread.UncaughtExceptionHandler mOriginHandler;
    private ICrashFilter mCrashFilter;
    private CrashHandler() {
        mOriginHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setCrashFilter(ICrashFilter crashFilter){
        mCrashFilter = crashFilter;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try{
            dealException(t, e);
        }catch (Throwable throwable){

        }finally {
            if(null != mOriginHandler){
                mOriginHandler.uncaughtException(t, e);
            }
        }
    }

    private void dealException(Thread t, Throwable e) throws Exception {
        if(null != mCrashFilter){
            if(mCrashFilter.filter(t, e)){
                catchCrashInfo(t, e);
            }
        }
    }

    private void catchCrashInfo(Thread t, Throwable e) throws Exception {
        JSONObject info = CrashInfo.obtain(t, e);
        FileUtils.writeStrToDisk(info.toString());
        Log.e("errorInfo", "\n" + info.toString());
    }
}