package org.jhoule.remote;

import java.util.HashMap;

/**
 * Created by jhoule on 10/27/2014.
 */
public  class UniversalRemote extends RemoteImpl implements Remote {

    protected class RemoteMap extends HashMap<Class, Remote>
    {

    }

    @Override
    public void pressButton(Button aButton) {
        // TODO: use class of button to choose correct remote
        // TODO: call pressButton on the returned remote
    }
}
