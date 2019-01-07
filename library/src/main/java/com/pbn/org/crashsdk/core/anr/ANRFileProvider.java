package com.pbn.org.crashsdk.core.anr;

import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

/**
 * function:
 *
 * @author peiboning
 * @DATE 2019/01/03
 */
public class ANRFileProvider extends FileObserver {

    public ANRFileProvider(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {

    }
}
