package com.dvsnier.crash;

/**
 * IUncaughtExceptionHandler
 * Created by dovsnier on 2018/6/28.
 */
public interface IUncaughtExceptionHandler {

    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    void uncaughtException(Thread t, Throwable e);
}
