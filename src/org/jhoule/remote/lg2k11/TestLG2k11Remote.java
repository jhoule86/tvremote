package org.jhoule.remote.lg2k11;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestLG2k11Remote {

    private static LG2k11Remote mRemote;

    static {
        try {
            mRemote = new MyLGRemote();
        } catch (Throwable t) {
            fail("could not start up remote: " + t.toString());
        }

        boolean on = mRemote.isTVAvailable();
        if (!on) {
            fail("TV is not on");
        }

        try {
            mRemote.establishSession();
        } catch (Exception e) {
            fail("Unable to establish session: " + e.toString());
        }
    }

    @Test
    public void testPowerOff() {
        mRemote.pressPower();
    }

    @Test
    public void testMute()
    {
        mRemote.pressMute();

    }

    @Test
    public void testVolDown2()
    {
        mRemote.volDown(2);
    }

    @Test
    public void testVolDown4()
    {
        mRemote.volDown(4);
    }

    @Test
    public void testISOn()
    {
        // static block will actually test. If we get here, TV is on.
        System.out.println("TV is on");
    }
    
    @Test
    public void testExit()
    {
        mRemote.pressExit();
    }

}
