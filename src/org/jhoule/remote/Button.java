package org.jhoule.remote;

/**
 * Created by jhoule on 10/27/2014.
 */
public interface Button<T> {

    static enum NAME
    {
        POWER,
        VOLUME_UP,
        VOLUME_DOWN,
        MUTE,
        CHANNEL_UP,
        CHANNEL_DOWN,
        ENERGY_SAVING,
        AV_MODE,
        INPUT,
        LIVE_TV,
        NUM_0,
        NUM_1,
        NUM_2,
        NUM_3,
        NUM_4,
        NUM_5,
        NUM_6,
        NUM_7,
        NUM_8,
        NUM_9,
        NUM_DASH,
        FLASHBACK,
        FAVORITE,
        RATIO,
        BACK,
        INFO,
        EXIT,
        MEDIA_STOP,
        MEDIA_PLAY,
        MEDIA_PAUSE,
        MEDIA_REWIND,
        MEDIA_FAST,
        SIMPLINK,
        PREMIUM,
        HOME,
        MENU,
        Q_MENU,
        SLEEP,
        LEFT,
        RIGHT,
        UP,
        DOWN,
        ENTER
    }

    static enum COLOR
    {
        black, white;
    }

    static enum SHAPE
    {
        square, circle, rectangle;
    }

//    Object getVirtualValue();
    String getName();
    String getLabel();

//    COLOR getColor();
//    COLOR getTextColor();

//    SHAPE getSHAPE();

//    TODO: ICON?

    public T getVirtualValue();

    public abstract class MuteButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.MUTE.toString();
        }

        @Override
        public String getLabel() {
            return NAME.MUTE.toString();
        }
    }

    public abstract class PowerButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.POWER.toString();
        }

        @Override
        public String getLabel() {
            return NAME.POWER.toString();
        }
    }

    public abstract class VolDownButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.VOLUME_DOWN.toString();
        }

        @Override
        public String getLabel() {
            return NAME.VOLUME_DOWN.toString();
        }
    }

    public abstract class VolUpButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.VOLUME_UP.toString();
        }

        @Override
        public String getLabel() {
            return NAME.VOLUME_UP.toString();
        }
    }

    public abstract class HomeButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.HOME.toString();
        }

        @Override
        public String getLabel() {
            return NAME.HOME.toString();
        }
    }

    public abstract class SleepButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.SLEEP.toString();
        }

        @Override
        public String getLabel() {
            return NAME.SLEEP.toString();
        }
    }

    public abstract class LeftButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.LEFT.toString();
        }

        @Override
        public String getLabel() {
            return NAME.LEFT.toString();
        }
    }

    public abstract class RightButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.RIGHT.toString();
        }

        @Override
        public String getLabel() {
            return NAME.RIGHT.toString();
        }
    }

    public abstract class UpButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.UP.toString();
        }

        @Override
        public String getLabel() {
            return NAME.UP.toString();
        }
    }

    public abstract class DownButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.DOWN.toString();
        }

        @Override
        public String getLabel() {
            return NAME.DOWN.toString();
        }
    }

    public abstract class EnterButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.ENTER.toString();
        }

        @Override
        public String getLabel() {
            return NAME.ENTER.toString();
        }
    }
    
    public abstract class ExitButton<T> implements Button<T> {

        @Override
        public String getName() {
            return NAME.EXIT.toString();
        }

        @Override
        public String getLabel() {
            return NAME.EXIT.toString();
        }
    }


    public abstract class KeypadButton<T> implements Button<T>
    {
        int mNum = 0;
        public KeypadButton(int aNum)
        {
            mNum = aNum;
        }

        public int getNumber()
        {
            return mNum;
        }

        @Override
        public String getName() {
            return Integer.toString(mNum);
        }

        @Override
        public String getLabel() {
            return Integer.toString(mNum);
        }
    }
}
