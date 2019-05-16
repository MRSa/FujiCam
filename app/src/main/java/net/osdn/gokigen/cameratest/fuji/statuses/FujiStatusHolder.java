package net.osdn.gokigen.cameratest.fuji.statuses;

import android.util.Log;
import android.util.SparseIntArray;

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
}
