package net.osdn.gokigen.cameratest.fuji.statuses;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.SparseIntArray;

import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_APERTURE;
import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_AUTO;
import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_CUSTOM;
import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_MANUAL;
import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_PROGRAM;
import static net.osdn.gokigen.cameratest.fuji.PropertyValues.SHOOTING_SHUTTER;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.BATTERY_LEVEL;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.BATTERY_LEVEL_2;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.DEVICE_ERROR;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.FLASH;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.FOCUS_LOCK;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.FOCUS_POINT;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.MOVIE_REMAINING_TIME;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.SDCARD_REMAIN_SIZE;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.SELF_TIMER;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.SHOOTING_MODE;

class FujiStatusHolder implements IFujiStatus
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
        if ((status == 1) || (status == 6)) {
            level = 0;
        } else if (status == 7) {
            level = 20;
        } else if ((status == 2) || (status == 8)) {
            level = 40;
        } else if (status == 9) {
            level = 60;
        } else if ((status == 3) || (status == 10)) {
            level = 80;
        } else if ((status == 4) || (status == 11)) {
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
        try {
            return (statusHolder.get(SELF_TIMER));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (-1);
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
}
