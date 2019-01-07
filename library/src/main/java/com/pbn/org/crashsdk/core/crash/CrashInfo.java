package com.pbn.org.crashsdk.core.crash;

import com.pbn.org.crashsdk.CrashSDK;
import com.pbn.org.crashsdk.core.utils.ProcessUitls;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * function:
 *
 * @author peiboning
 * @DATE 2019/01/02
 */
public class CrashInfo {
    public static JSONObject obtain(Thread t, Throwable e) throws Exception{
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        Throwable tempThrowable = e.getCause();
        if(null != tempThrowable){
            tempThrowable.printStackTrace(pw);
        }
        tempThrowable = e.getCause();
        if(null != tempThrowable){
            tempThrowable.printStackTrace(pw);
        }
        JSONObject object = new JSONObject();
        object.put("crash_info", sw.toString());
        object.put("crash_time", System.currentTimeMillis());
        object.put("process_name", ProcessUitls.getProcessName());
        object.put("is_main_process", ProcessUitls.isProcessMain()?1:0);
        ProcessUitls.collectMemoryInfo(object);
        ProcessUitls.collectThreadInfo(object);
        return object;
    }
}
