package org.jhoule.remote;

/**
 * Created by jhoule on 10/27/2014.
 */
public interface Remote {


    public abstract void pressButton(String aButtonName);

    public abstract void pressMute();

    public abstract void pressPower();

    public abstract void pressExit();

    /**
     * Check if device is accepting input
     * @return true if the device is accepting input, false otherwise
     */
    public abstract boolean isDeviceAvailable();

    public abstract boolean setup();

    public abstract boolean shutDown();
}
