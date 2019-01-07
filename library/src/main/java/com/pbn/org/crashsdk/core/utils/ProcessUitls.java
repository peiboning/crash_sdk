package com.pbn.org.crashsdk.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;

import com.pbn.org.crashsdk.CrashSDK;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * function:
 *  进程相关的接口
 * @author peiboning
 * @DATE 2019/01/03
 */
public class ProcessUitls {
    private static Set<String> excludeThreads = new HashSet<>();
    static {
        excludeThreads.add("ThreadPlus");
        excludeThreads.add("ApiDispatcher");
        excludeThreads.add("ApiLocalDispatcher");
        excludeThreads.add("AsyncLoader");
        excludeThreads.add("AsyncTask");
        excludeThreads.add("Binder");
        excludeThreads.add("PackageProcessor");
        excludeThreads.add("SettingsObserver");
        excludeThreads.add("WifiManager");
        excludeThreads.add("JavaBridge");
        excludeThreads.add("Compiler");
        excludeThreads.add("Signal Catcher");
        excludeThreads.add("GC");
        excludeThreads.add("ReferenceQueueDaemon");
        excludeThreads.add("FinalizerDaemon");
        excludeThreads.add("FinalizerWatchdogDaemon");
        excludeThreads.add("CookieSyncManager");
        excludeThreads.add("RefQueueWorker");
        excludeThreads.add("CleanupReference");
        excludeThreads.add("VideoManager");
        excludeThreads.add("DBHelper-AsyncOp");
        excludeThreads.add("InstalledAppTracker2");
        excludeThreads.add("AppData-AsyncOp");
        excludeThreads.add("IdleConnectionMonitor");
        excludeThreads.add("LogReaper");
        excludeThreads.add("ActionReaper");
        excludeThreads.add("Okio Watchdog");
        excludeThreads.add("CheckWaitingQueue");
    }
    private static String sProcessName;
    public static String getProcessName(){
        if(TextUtils.isEmpty(sProcessName)){
            try{
                int myPid = Process.myPid();
                ActivityManager am = (ActivityManager) CrashSDK.sContext.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo>appInfos = am.getRunningAppProcesses();
                if(null != appInfos && appInfos.size()>0){
                    for(ActivityManager.RunningAppProcessInfo info : appInfos){
                        if(myPid == info.pid){
                            sProcessName = info.processName;
                            break;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            if(TextUtils.isEmpty(sProcessName)){
                try{
                    String path = "/proc/" + Process.myPid() + "/cmdline";
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(new FileInputStream(path), "iso-8859-1"));
                    StringBuilder sb = new StringBuilder();
                    int len = -1;
                    while ((len = bufferedReader.read())>0){
                        sb.append((char)len);
                    }
                    sProcessName = sb.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return sProcessName;
    }

    public static boolean isProcessMain(){
        String pName = getProcessName();
        if(!TextUtils.isEmpty(pName) && pName.contains(":")){
            return false;
        }
        if(!TextUtils.isEmpty(pName) && pName.equals(CrashSDK.sContext.getPackageName())){
            return true;
        }

        return false;

    }

    public static void collectMemoryInfo(JSONObject object){
        if(null == object){
            return;
        }
        try{
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);

            JSONObject debugMemoryInfo = new JSONObject();
            debugMemoryInfo.put("dalvikPrivateDirty", memoryInfo.dalvikPrivateDirty);
            debugMemoryInfo.put("dalvikPss", memoryInfo.dalvikPss);
            debugMemoryInfo.put("dalvikSharedDirty", memoryInfo.dalvikSharedDirty);
            debugMemoryInfo.put("nativePrivateDirty", memoryInfo.nativePrivateDirty);
            debugMemoryInfo.put("nativePss", memoryInfo.nativePss);
            debugMemoryInfo.put("nativeSharedDirty", memoryInfo.nativeSharedDirty);
            debugMemoryInfo.put("otherPrivateDirty", memoryInfo.otherPrivateDirty);
            debugMemoryInfo.put("otherPss", memoryInfo.otherPss);
            debugMemoryInfo.put("otherSharedDirty", memoryInfo.otherSharedDirty);
            debugMemoryInfo.put("totalPrivateClean", memoryInfo.getTotalPrivateClean());
            debugMemoryInfo.put("totalPrivateDirty", memoryInfo.getTotalPrivateDirty());
            debugMemoryInfo.put("totalPss", memoryInfo.getTotalPss());
            debugMemoryInfo.put("totalSharedClean", memoryInfo.getTotalSharedClean());
            debugMemoryInfo.put("totalSharedDirty", memoryInfo.getTotalSharedDirty());
            debugMemoryInfo.put("totalSwappablePss", memoryInfo.getTotalSwappablePss());
            object.put("debug_memory_info", debugMemoryInfo);

            ActivityManager.MemoryInfo aMemoryInfo = new ActivityManager.MemoryInfo();
            ActivityManager am = (ActivityManager) CrashSDK.sContext.getSystemService(Context.ACTIVITY_SERVICE);
            am.getMemoryInfo(aMemoryInfo);
            JSONObject aMemoryInfoObj = new JSONObject();
            aMemoryInfoObj.put("availMem", aMemoryInfo.availMem);
            aMemoryInfoObj.put("lowMemory", aMemoryInfo.lowMemory);
            aMemoryInfoObj.put("threshold", aMemoryInfo.threshold);
            aMemoryInfoObj.put("totalMem", aMemoryInfo.totalMem);
            object.put("sys_memory_info", aMemoryInfoObj);

            JSONObject appMemoryInfo = new JSONObject();
            appMemoryInfo.put("native_heap_size", Debug.getNativeHeapSize());
            appMemoryInfo.put("native_heap_alloc_size", Debug.getNativeHeapAllocatedSize());
            appMemoryInfo.put("native_heap_free_size", Debug.getNativeHeapFreeSize());
            Runtime var7 = Runtime.getRuntime();
            appMemoryInfo.put("max_memory", var7.maxMemory());
            appMemoryInfo.put("free_memory", var7.freeMemory());
            appMemoryInfo.put("total_memory", var7.totalMemory());
            if (am != null) {
                appMemoryInfo.put("memory_class", am.getMemoryClass());
                appMemoryInfo.put("large_memory_class", am.getLargeMemoryClass());
            }

            object.put("app_memory_info", appMemoryInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void collectThreadInfo(JSONObject object) {
        if(null == object){
            return;
        }

        try{
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            if(null != map && map.size()>0){
                JSONObject threadInfo = new JSONObject();
                threadInfo.put("thread_all_count", map.size());
                JSONArray ts = new JSONArray();

                Set<Thread> kes = map.keySet();
                JSONObject temp = null;
                for(Thread t : kes){
                    if(excludeThreads.contains(t.getName())){
                        continue;
                    }
                    temp = new JSONObject();
                    temp.put("t_name", t.getName());
                    StackTraceElement[] s = map.get(t);
                    if(null != s && s.length > 0){
                        StringBuilder sb = new StringBuilder();
                        JSONArray arr = new JSONArray();
                        for (int i = 0;i<s.length;i++){
                            StackTraceElement stack = s[i];
                            sb.append(stack.getClassName()).append(stack.getMethodName())
                                    .append("(")
                                    .append(stack.getLineNumber())
                                    .append(")");
                            arr.put(sb.toString());
                        }
                        temp.put("info", arr);
                    }
                    ts.put(temp);
                }
                threadInfo.put("t_stack", ts);
                object.put("tr_stacks", threadInfo);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
