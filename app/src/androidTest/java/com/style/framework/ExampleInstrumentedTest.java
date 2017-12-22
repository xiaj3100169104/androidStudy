package com.style.framework;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.style.bean.User;
import com.style.manager.AccountManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.style.framework", appContext.getPackageName());
    }

    @Test
    public void userId() throws Exception {
        User u = AccountManager.getInstance().getCurrentUser();
        Log.i("userId", u.userId);
        assertEquals("18202823096",u.userId);
    }
}
