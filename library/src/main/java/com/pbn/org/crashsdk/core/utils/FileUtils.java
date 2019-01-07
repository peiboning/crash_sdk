package com.pbn.org.crashsdk.core.utils;

import android.os.SystemClock;

import com.pbn.org.crashsdk.CrashSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * function:
 *
 * @author peiboning
 * @DATE 2019/01/03
 */
public class FileUtils {
    public static void writeStrToDisk(String content){
        File f = new File(CrashSDK.sContext.getExternalCacheDir().getAbsoluteFile(), "crash_info");
        if(!f.exists()){
            f.mkdirs();
        }

        File realFile = new File(f, System.currentTimeMillis()+".json");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(realFile);
            out.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
