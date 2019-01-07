package com.pbn.org.crashsdk.core.crash;

/**
 * function:
 *
 * @author peiboning
 * @DATE 2019/01/02
 */
public interface ICrashFilter {
    boolean filter(Thread thread, Throwable throwable);
}
