package org.jhoule.remote;

import java.util.Map;

/**
 * Created by jhoule on 10/27/2014.
 */
public abstract class RemoteImpl implements Remote {

    protected static ButtonMap mButtons;
    protected static ButtonMap mKeypad;

    public abstract void pressButton(Button aButton);

    public void pressPower()
    {
        pressButton(mButtons.get(Button.NAME.POWER.toString()));
    }

    public void pressButtonWithLabel(String aLabel)
    {
        for (Button button: mButtons.values())
        {
            if (button.getLabel().equalsIgnoreCase(aLabel))
            {
                pressButton(button);
                return;
            }
        }
    }

    public void pressButton(String aButtonName)
    {
        if (aButtonName == null || aButtonName.trim().length() == 0)
        {
            return;
        }

        Button b = mButtons.get(aButtonName);
        if (b == null)
        {
            System.err.println("Button unknown: " + aButtonName);
            return;
        }

        pressButton(b);
    }

    public void pressButton(Button.NAME aButtonName)
    {
        if (aButtonName == null)
        {
            return;
        }

        Button b = mButtons.get(aButtonName);

        if (b == null)
        {
            System.err.println("Button name was null");
            return;
        }

        pressButton(b);
    }

    public void performMacro(ButtonMacro aMacro)
    {
        if (aMacro == null)
        {
            return;
        }

        for (Button b: aMacro)
        {
            pressButton(b);
        }
    }

    public void pressMute()
    {
        pressButton(Button.NAME.MUTE);
    }

    public void volDown()
    {
        volDown(1);
    }

    public void volUp()
    {
        volUp(1);
    }

    public void volDown(int aAmount)
    {
        for (int i = aAmount; i> 0; i--)
        {
            pressButton(Button.NAME.VOLUME_DOWN);
        }
    }

    public void volUp(int aAmount)
    {
        for (int i = aAmount; i> 0; i--)
        {
            pressButton(Button.NAME.VOLUME_UP);
        }
    }

    public void ListButtons()
    {
        System.out.println("Buttons:");
        System.out.println("===================");

        for (Button b: mButtons.values())
        {
            System.out.println(b.getName() + " : " + b.getLabel());
        }

        System.out.println("===================");
    }
}
