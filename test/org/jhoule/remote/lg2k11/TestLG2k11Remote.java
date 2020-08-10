package org.jhoule.remote.lg2k11;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Properties;

import org.jhoule.remote.lg2k11.LG2k11Remote.SETTING;
import org.junit.BeforeClass;

public class TestLG2k11Remote {

    private static String HOST_ADDR = "192.168.1.200";
    private static String AUTH_CODE = "ABCDEF";

    private static LG2k11Remote mRemote;

    @BeforeClass
    public static void setup() {
        Properties settings = new Properties();
        settings.setProperty(SETTING.HOST, HOST_ADDR);
        settings.setProperty(SETTING.CODE, AUTH_CODE);
        try {
            mRemote = new LG2k11Remote(settings);
        } catch (Throwable t) {
            fail("could not start up remote: " + t.toString());
        }

        boolean on = mRemote.isDeviceAvailable();
        if (!on) {
            fail("TV is not on");
        }
    }

    // @Test
    // public void testPowerOff() {
    // mRemote.pressPower();
    // }

    @Test
    public void testMute() {
        mRemote.pressMute();

    }

    @Test
    public void testVolDown2() {
        mRemote.volDown(2);
    }

    @Test
    public void testVolDown4() {
        mRemote.volDown(4);
    }

    @Test
    public void testISOn() {
        // static block will actually test. If we get here, TV is on.
        System.out.println("TV is on");
    }

    @Test
    public void testExitByValue() {
        mRemote.pressButton(91);
    }

    @Test
    public void testShowAuthCode() {
        mRemote.showAuthCode();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mRemote.pressExit();
    }

}
