package org.jhoule.remote.lg2k11;

/**
 * Example remote used by tests
 */
public class MyLGRemote extends LG2k11Remote {

    private static final String mIp = "192.168.1.200";
    private static final String mPrivateKey = "ABCDEF";

    public MyLGRemote()
    {
        super(mIp, mPrivateKey);
    }
}
