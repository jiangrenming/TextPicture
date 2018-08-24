package main.demo.mobads.baidu.com.androidutils;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jiangrenming on 2018/8/22.
 */

public class ResultLock {
    private CountDownLatch latch = new CountDownLatch(1);
    private boolean result = false;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
