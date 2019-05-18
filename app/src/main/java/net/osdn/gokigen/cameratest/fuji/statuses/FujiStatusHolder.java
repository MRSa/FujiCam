package net.osdn.gokigen.cameratest.fuji.statuses;

import android.graphics.Point;
import android.util.SparseIntArray;

import java.util.Locale;

class FujiStatusHolder implements IFujiStatus, IFujiCameraProperties, IFujiCameraPropertyValues
{
    private final String TAG = toString();
    private SparseIntArray statusHolder;

    /**
     * コンストラクタ
     */
    FujiStatusHolder() {
        statusHolder = new SparseIntArray();
        statusHolder.clear();
    }

    void updateValue(int id, byte data0, byte data1, byte data2, byte data3) {
        int value = ((((int) data3) & 0xff) << 24) + ((((int) data2) & 0xff) << 16) + ((((int) data1) & 0xff) << 8) + (((int) data0) & 0xff);

        //Log.v(TAG, "updateValue() : " + id + "[" + value + "]");
        statusHolder.put(id, value);
    }

    @Override
    public int getValue(int statusId) {
        try {
            return (statusHolder.get(statusId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (0);
    }

    @Override
    public boolean isFocusLocked() {
        try {
            int status = statusHolder.get(FOCUS_LOCK);
            if (status == 1) {
                return (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (false);
    }

    @Override
    public int getBatteryLevel()
    {
        int level = -1;
        int status = 0;
        try {
            status = statusHolder.get(BATTERY_LEVEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (status == 0)
        {
            try {
                status = statusHolder.get(BATTERY_LEVEL_2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ((status == BATTERY_CRITICAL) || (status == BATTERY_126S_CRITICAL)) {
            level = 0;
        } else if (status == BATTERY_126S_ONE_BAR) {
            level = 20;
        } else if ((status == BATTERY_ONE_BAR) || (status == BATTERY_126S_TWO_BAR)) {
            level = 40;
        } else if (status == BATTERY_126S_THREE_BAR) {
            level = 60;
        } else if ((status == BATTERY_TWO_BAR) || (status == BATTERY_126S_FOUR_BAR)) {
            level = 80;
        } else if ((status == BATTERY_FULL) || (status == BATTERY_126S_FULL)) {
            level = 100;
        }
        return (level);
    }

    @Override
    public boolean isDeviceError()
    {
        try {
            int status = statusHolder.get(DEVICE_ERROR);
            if (status != 0) {
                return (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (false);
    }

    @Override
    public int getFlashStatus()
    {
        try {
            return (statusHolder.get(FLASH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }

    @Override
    public int getSelfTimerMode()
    {
        int value = -1;
        try {
            int status = statusHolder.get(SELF_TIMER);
            switch (status)
            {
                case TIMER_OFF:
                    value = 0;
                    break;
                case TIMER_1SEC:
                    value = 1;
                    break;
                case TIMER_2SEC:
                    value = 2;
                    break;
                case TIMER_5SEC:
                    value = 5;
                    break;
                case TIMER_10SEC:
                    value = 10;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }


    @Override
    public int getRemainImageSpace()
    {
        try {
            return (statusHolder.get(SDCARD_REMAIN_SIZE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }

    @Override
    public int getMovieImageSpace()
    {
        try {
            return (statusHolder.get(MOVIE_REMAINING_TIME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }


    @Override
    public String getShootingMode()
    {
        String mode = "?";
        try {
            int value = statusHolder.get(SHOOTING_MODE);
            if (value == SHOOTING_MANUAL)
            {
                mode = "M";
            }
            else if (value == SHOOTING_PROGRAM)
            {
                mode = "P";
            }
            else if (value == SHOOTING_APERTURE)
            {
                mode = "A";
            }
            else if (value == SHOOTING_SHUTTER)
            {
                mode = "S";
            }
            else if (value == SHOOTING_AUTO)
            {
                mode = "a";
            }
            else if (value == SHOOTING_CUSTOM)
            {
                mode = "C";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (mode);
    }

    @Override
    public Point getFocusPoint()
    {
        try {
            int status = statusHolder.get(FOCUS_POINT);
            int y = (status & 0xff);
            int x = ((status & 0xff00) >>> 8);
            return (new Point(x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Point());
    }

    @Override
    public int getIsoSensitivity()
    {
        try {
            int status = statusHolder.get(ISO);
            return ((0x0000ffff & status));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (0);
    }

    @Override
    public String getShutterSpeed()
    {
        try
        {
            int status = statusHolder.get(SHUTTER_SPEED);
            if ((0x80000000 & status) != 0)
            {
                int value = 0x0fffffff & status;
                return ("1/" + (value / 1000));
            }
            else
            {
                return (status + "");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ("--");
    }

    @Override
    public String getExpRev()
    {
        try {
            int status = statusHolder.get(EXPOSURE_COMPENSATION);
            float value = ((float) status / 1000.0f);
            return (String.format(Locale.ENGLISH, "%+1.1f", value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("");
    }

    @Override
    public String getAperture()
    {
        try {
            int status = statusHolder.get(APERTURE);
            float value = ((float) status / 100.0f);
            return (String.format(Locale.ENGLISH, "F%1.1f", value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("F--");
    }

    @Override
    public String getWhiteBalance()
    {
        String value = "";
        try {
            int status = statusHolder.get(WHITE_BALANCE);
            if (status == WHITE_BALANCE_AUTO)
            {
                value = "Auto";
            }
            else if (status == WHITE_BALANCE_FINE)
            {
                value = "Fine";
            }
            else if (status == WHITE_BALANCE_INCANDESCENT)
            {
                value = "Incandescent";
            }
            else if (status == WHITE_BALANCE_FLUORESCENT_1)
            {
                value = "Fluorescent 1";
            }
            else if (status == WHITE_BALANCE_FLUORESCENT_2)
            {
                value = "Fluorescent 2";
            }
            else if (status == WHITE_BALANCE_FLUORESCENT_3)
            {
                value = "Fluorescent 3";
            }
            else if (status == WHITE_BALANCE_SHADE)
            {
                value = "Shade";
            }
            else if (status == WHITE_BALANCE_UNDERWATER)
            {
                value = "Underwater";
            }
            else if (status == WHITE_BALANCE_TEMPERATURE)
            {
                value = "Kelvin";
            }
            else if (status == WHITE_BALANCE_CUSTOM)
            {
                value = "Custom";
            }
            else
            {
                value = "Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }


    @Override
    public int getF_SS_Control()
    {
        try {
            return (statusHolder.get(F_SS_CONTROL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }

    @Override
    public String getFocusControlMode()
    {
        String value = "";
        try {
            int status = statusHolder.get(FOCUS_MODE);
            if (status == FOCUS_MANUAL)
            {
                value = "M";
            }
            else if (status == FOCUS_SINGLE_AUTO)
            {
                value = "S";
            }
            else if (status == FOCUS_CONTINUOUS_AUTO)
            {
                value = "C";
            }
            else
            {
                value = "?";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }

    @Override
    public String getImageAspect()
    {
        String value = "";
        try {
            int status = statusHolder.get(IMAGE_ASPECT);
            if (status == IMAGE_ASPECT_S_3x2)
            {
                value = "S:3x2";
            }
            else if (status == IMAGE_ASPECT_S_16x9)
            {
                value = "S:16x9";
            }
            else if (status == IMAGE_ASPECT_S_1x1)
            {
                value = "S:1x1";
            }
            else if (status == IMAGE_ASPECT_M_3x2)
            {
                value = "M:3x2";
            }
            else if (status == IMAGE_ASPECT_M_16x9)
            {
                value = "M:16x9";
            }
            else if (status == IMAGE_ASPECT_M_1x1)
            {
                value = "M:1x1";
            }
            else if (status == IMAGE_ASPECT_L_3x2)
            {
                value = "L:3x2";
            }
            else if (status == IMAGE_ASPECT_L_16x9)
            {
                value = "L:16x9";
            }
            else if (status == IMAGE_ASPECT_L_1x1)
            {
                value = "L:1x1";
            }
            else
            {
                value = "?";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }

    @Override
    public String getImageFormat()
    {
        String value = "";
        try {
            int status = statusHolder.get(IMAGE_FORMAT);
            if (status == IMAGE_FORMAT_FINE)
            {
                value = "JPG[F]";
            }
            else if (status == IMAGE_FORMAT_NORMAL)
            {
                value = "JPG[N]";
            }
            else if (status == IMAGE_FORMAT_FINE_RAW)
            {
                value = "JPG[F]+RAW";
            }
            else if (status == IMAGE_FORMAT_NORMAL_RAW)
            {
                value = "JPG[N]+RAW";
            }
            else
            {
                value = "???";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }

    @Override
    public String getFilmSimulation()
    {
        String value = "";
        try {
            int status = statusHolder.get(FILM_SIMULATION);
            switch (status)
            {
                case FILM_SIMULATION_PROVIA:
                    value = "PROVIA";
                    break;
                case FILM_SIMULATION_VELVIA:
                    value = "VELVIA";
                    break;
                case FILM_SIMULATION_ASTIA:
                    value = "ASTIA";
                    break;
                case FILM_SIMULATION_MONOCHROME:
                    value = "MONO";
                    break;
                case FILM_SIMULATION_SEPIA:
                    value = "SEPIA";
                    break;
                case FILM_SIMULATION_PRO_NEG_HI:
                    value = "NEG_HI";
                    break;
                case FILM_SIMULATION_PRO_NEG_STD:
                    value = "NEG_STD";
                    break;
                case FILM_SIMULATION_MONOCHROME_Y_FILTER:
                    value = "MONO_Y";
                    break;
                case FILM_SIMULATION_MONOCHROME_R_FILTER:
                    value = "MONO_R";
                    break;
                case FILM_SIMULATION_MONOCHROME_G_FILTER:
                    value = "MONO_G";
                    break;
                case FILM_SIMULATION_CLASSIC_CHROME:
                    value = "CLASSIC CHROME";
                    break;
                case FILM_SIMULATION_ACROS:
                    value = "ACROS";
                    break;
                case FILM_SIMULATION_ACROS_Y:
                    value = "ACROS_Y";
                    break;
                case FILM_SIMULATION_ACROS_R:
                    value = "ACROS_R";
                    break;
                case FILM_SIMULATION_ACROS_G:
                    value = "ACROS_G";
                    break;
                case FILM_SIMULATION_ETERNA:
                    value = "ETERNA";
                    break;
                default:
                    value = "???";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (value);
    }

    @Override
    public boolean isRecModeEnable()
    {
        try {
            int status = statusHolder.get(RECMODE_ENABLE);
            if (status == 1)
            {
                return (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (false);
    }

    @Override
    public int getMovieIsoSensitivity()
    {
        try {
            return (statusHolder.get(MOVIE_ISO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }


}
