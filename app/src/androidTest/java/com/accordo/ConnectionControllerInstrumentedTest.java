package com.accordo;

import android.content.Context;
import android.util.Log;

import com.accordo.controller.ConnectionController;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ConnectionControllerInstrumentedTest {
    private final String TAG = "MYTAG";
    @Test
    public void testRegister() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ConnectionController cc = new ConnectionController(appContext);

        CountDownLatch lock = new CountDownLatch(1);

        cc.register(response -> {
                    Log.d(TAG, "Response OK: " + response.toString());
                    lock.countDown();
                },
                error -> {
                    Log.d(TAG, "Response KO: " + error.toString());
                    fail("Register should always be OK");
                    lock.countDown();
                });
        lock.await();
    }
}