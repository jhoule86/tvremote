package org.jhoule.remote.lg2k11;


public class TurnOff {

    private static LG2k11Remote mRemote;

    static {
        try {
            mRemote = new MyLGRemote();
        } catch (Throwable t) {
            throw new RuntimeException("could not start up remote: " + t.toString());
        }

        boolean on = mRemote.isTVAvailable();
        if (!on) {
            throw new RuntimeException("TV is not on");
        }

        try {
            mRemote.establishSession();
        } catch (Exception e) {
            throw new RuntimeException("Unable to establish session: " + e.toString());
        }
    }

    public static void main (String[] args)
    {
        mRemote.pressPower();
    }

}