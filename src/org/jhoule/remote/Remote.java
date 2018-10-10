package org.jhoule.remote;

import java.util.*;

/**
 * Created by jhoule on 10/27/2014.
 */
public interface Remote<B extends  Button> {

    public static class ButtonMacro extends ArrayList<Button>
    {
        String mName;

        public ButtonMacro(String aName)
        {
            super();
            mName = aName;
        }
    }

    public static class ButtonMap extends LinkedHashMap<String, Button> implements Map<String, Button>
    {
        public ButtonMap()
        {
            super();
        }

        public ButtonMap(int aInitialCapacity)
        {
            super(aInitialCapacity);
        }

        @Override
        public Button put(String key, Button value) {

            if (key == null || value == null || ! key.equals(value.getName()))
            {
                throw new IllegalArgumentException("Key and name must be matching, non-null Strings.");
            }
            return super.put(key, value);
        }

        public Button put(Button value)
        {
            if (value == null)
            {
                throw new IllegalArgumentException("Button name must be non-null");
            }
            return put(value.getName(), value);
        }

        public Button get(Button.NAME aName)
        {
            return get(aName.toString());
        }

        public void putAll(Collection<? extends Button> aColl)
        {
            if (aColl == null)
            {
                return;
            }

            for (Button b: aColl)
            {
                this.put(b);
            }
        }
    }

    public abstract void pressButton(String aButtonName);

    public abstract void pressMute();

    public abstract void pressPower();

}
