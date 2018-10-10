package org.jhoule.remote.lg2k11;

import org.jhoule.remote.Button;
import org.jhoule.remote.Button.*;

/**
 * Created by jhoule on 12/23/2014.
 */
public class LG2k11Button {

    public static class LGPowerButton extends PowerButton<Integer>
    {

        @Override
        public Integer getVirtualValue() {
            return 8;
        }


        /**
         * The LG remote cannot turn on TVs, only turn them off.
         * @return the String "TV OFF"
         */
        @Override
        public String getLabel() {
            return "TV OFF";
        }
    }


    /* ----------------- Volume and mute ----------------------------------------------*/
    public static class LGMuteButton extends MuteButton<Integer>
    {


        @Override
        public Integer getVirtualValue() {
            return 9;
        }


        @Override
        public String getLabel() {
            return "Mute";
        }
    }

    public static class LGVolDownButton extends VolDownButton<Integer>
    {


        @Override
        public Integer getVirtualValue() {
            return 3;
        }



        @Override
        public String getLabel() {
            return "VOL_DN";
        }
    }

    public static class LGVolUpButton extends VolUpButton<Integer>
    {

        @Override
        public Integer getVirtualValue()
        {
            return 2;
        }


        /**
         * The LG remote cannot turn on TVs, only turn them off.
         * @return the String "TV OFF"
         */
        @Override
        public String getLabel() {
            return "VOL_UP";
        }
    }

    /* ----------------- End Volume and mute ------------------------------------*/

    /* ----------------- Navigation ---------------------------------------------*/

    public static class LGGBlueButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "BLUE";
        }

        @Override
        public String getLabel() {
            return "Blue";
        }

        @Override
        public Integer getVirtualValue() {
            return 97;
        }
    }

    public static class LGGYellowButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "YELLOW";
        }

        @Override
        public String getLabel() {
            return "Yellow";
        }

        @Override
        public Integer getVirtualValue() {
            return 99;
        }
    }


    public static class LGGRedButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "RED";
        }

        @Override
        public String getLabel() {
            return "Red";
        }

        @Override
        public Integer getVirtualValue() {
            return 114;
        }
    }

    public static class LGGreenButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "GREEN";
        }

        @Override
        public String getLabel() {
            return "Green";
        }

        @Override
        public Integer getVirtualValue() {
            return 113;
        }
    }

    public static class LGGHomeButton extends HomeButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 67;
        }
    }

    public static class LGLeftButton extends LeftButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 7;
        }
    }

    public static class LGRightButton extends RightButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 6;
        }
    }

    public static class LGUpButton extends UpButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 64;
        }
    }

    public static class LGDownButton extends DownButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 65;
        }
    }

    /**
     * Implemnets "Enter" / "OK" / "Select" button
     */
    public static class LGEnterButton extends EnterButton<Integer>
    {
        @Override
        public Integer getVirtualValue() {
            return 65;
        }
    }

    /* ----------------- End Navigation ----------------------------------------*/

    /* ----------------- keypad ------------------------------------------------*/
    public static class LGKeypadButton extends KeypadButton<Integer>
    {
        LGKeypadButton(int aNum)
        {
            super(aNum);
        }

        @Override
        public Integer getVirtualValue() {
            return getNumber() + 16;
        }
    }

    /**
     * Implements "Enter" / "OK" / "Select" button
     */
    public static class LGDashButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "-";
        }

        @Override
        public String getLabel() {
            return "-";
        }

        @Override
        public Integer getVirtualValue() {
            return 76;
        }
    }

    /* ----------------- END keypad ------------------------------------------*/

    /* ----------------- navigation ------------------------------------------------*/

    /**
     * Implements Stop button
     */
    public static class LGStopButton implements Button<Integer>
    {
        @Override
        public String getName() {
            return "STOP";
        }

        @Override
        public String getLabel() {
            return "Stop";
        }

        @Override
        public Integer getVirtualValue() {
            return 177;
        }
    }

    /* ----------------- END navigation ------------------------------------------*/


}
