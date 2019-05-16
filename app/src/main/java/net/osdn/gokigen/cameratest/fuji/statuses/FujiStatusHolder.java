package net.osdn.gokigen.cameratest.fuji.statuses;


import android.util.SparseIntArray;

import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.BATTERY_LEVEL;
import static net.osdn.gokigen.cameratest.fuji.statuses.Properties.FOCUS_LOCK;

class FujiStatusHolder implements IFujiStatus
{
    private final String TAG = toString();
    private SparseIntArray statusHolder;

    /**
     *   コンストラクタ
     */
    FujiStatusHolder()
    {
        statusHolder = new SparseIntArray();
        statusHolder.clear();
    }

    void updateValue(int id, byte data0, byte data1, byte data2, byte data3)
    {
        int value = ((((int) data3)&0xff) << 24) +  ((((int) data2)&0xff) << 16) +  ((((int) data1)&0xff) << 8) + (((int) data0) & 0xff);

        //Log.v(TAG, "updateValue() : " + id + "[" + value + "]");
        statusHolder.put(id, value);
    }

    @Override
    public int getValue(int statusId)
    {
        try
        {
            return (statusHolder.get(statusId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (0);
    }

    @Override
    public boolean isFocusLocked()
    {
        try
        {
            int status = statusHolder.get(FOCUS_LOCK);
            if (status == 1)
            {
                return (true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    @Override
    public int getBatteryLevel()
    {
        int level = -1;
        try
        {
            int status = statusHolder.get(BATTERY_LEVEL);
            if ((status == 1)||(status == 6))
            {
                level = 0;
            }
            else if (status == 7)
            {
                level = 20;
            }
            else if ((status == 2)||(status == 8))
            {
                level = 40;
            }
            else if (status == 9)
            {
                level = 60;
            }
            else if ((status == 3)||(status == 10))
            {
                level = 80;
            }
            else if ((status == 4)||(status == 11))
            {
                level = 100;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (level);
    }


}
