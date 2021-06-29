package com.accordo;

import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ConnectionControllerInstrumentedTest {
    /*private final String TAG = "MYTAG";
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
    }*/
}