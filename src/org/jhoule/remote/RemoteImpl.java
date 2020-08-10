package org.jhoule.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by jhoule on 10/27/2014.
 */
public abstract class RemoteImpl implements Remote {

    public static class ButtonMacro extends ArrayList<Button> {
        
        private static final long serialVersionUID = 1L;
        String mName;

        public ButtonMacro(String aName) {
            super();
            mName = aName;
        }
    }

    public static class ButtonMap extends LinkedHashMap<String, Button>{
        
        private static final long serialVersionUID = 1L;

        public ButtonMap() {
            super();
        }

        public ButtonMap(int aInitialCapacity) {
            super(aInitialCapacity);
        }

        @Override
        public Button put(String key, Button value) {

            return super.put(key, value);
        }

        public Button put(Button value) {
            if (value == null || value.getName() == null || value.getName().length() < 1) {
                throw new IllegalArgumentException("Button name must be non-null");
            }
            return put(value.getName().toUpperCase(), value);
        }

        public Button get(String key) {
            if (key == null) {
                return null;
            }

            Button b = super.get(key);

            if (b == null) {
                b = super.get(key.toUpperCase());
            }

            return b;
        }

        public void addAll(Collection<? extends Button> aColl) {
            if (aColl == null) {
                return;
            }

            for (Button b : aColl) {
                this.put(b);
            }
        }
    }

    protected static ButtonMap mButtons;
    protected static ButtonMap mKeypad;

    final String quit = "REMOTE_OFF";

    public abstract void pressButton(Button aButton);

    public void pressPower() {
        pressButton(mButtons.get(Button.NAME.POWER.toString()));
    }

    public void pressButton(String aButtonName) {

        pressButton(aButtonName, 1);
    }


    public void pressButton(String aButtonName, int count) {
        if (aButtonName == null || aButtonName.trim().length() < 1 || count < 1) {
            return;
        }

        Button b = mButtons.get(aButtonName);

        if (b == null) {
            System.err.println("Button not found: " + aButtonName.toString());
            return;
        }

        for (int i = count; i > 0; i--) {
            pressButton(b);
        }

    }

    public void performMacro(ButtonMacro aMacro) {
        if (aMacro == null) {
            return;
        }

        for (Button b : aMacro) {
            pressButton(b);
        }
    }

    public void pressMute() {
        pressButton(Button.NAME.MUTE.toString());
    }

    public void volDown() {
        volDown(1);
    }

    public void volUp() {
        volUp(1);
    }

    public void volDown(int aAmount) {
        pressButton(Button.NAME.VOLUME_DOWN.toString(), aAmount);
    }

    public void volUp(int aAmount) {

        pressButton(Button.NAME.VOLUME_UP.toString(), aAmount);
    }

    @Override
    public void pressExit() {
        pressButton(Button.NAME.EXIT.toString());
    }
}
