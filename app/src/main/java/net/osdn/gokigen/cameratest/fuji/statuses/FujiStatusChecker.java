package net.osdn.gokigen.cameratest.fuji.statuses;

import android.util.Log;

import androidx.annotation.NonNull;

import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

public class FujiStatusChecker implements IFujiStatusReceiver
{
    private final String TAG = toString();
    private final IFujiStatusRequest comm;
    private final IFujiStatusNotify notify;
    private final FujiStatusHolder statusHolder;
    private static final int  ERROR_LIMIT = 10;
    private boolean threadIsRunning = false;
    private final int WAIT_MS = 400;

    public FujiStatusChecker(@NonNull IFujiStatusRequest comm, @NonNull IFujiStatusNotify notify)
    {
        this.comm = comm;
        this.notify = notify;
        this.statusHolder = new FujiStatusHolder();
    }

    @Override
    public void statusReceived(ReceivedDataHolder data)
    {
        statusReceivedImpl(data.getData());
    }

    @Override
    public void start()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {

                int errorCount = 0;
                threadIsRunning = true;
                while (threadIsRunning)
                {
                    try
                    {
                        comm.requestStatus();
                        Thread.sleep(WAIT_MS);
                        errorCount = 0;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        errorCount++;
                    }
                    if (errorCount > ERROR_LIMIT)
                    {
                        threadIsRunning = false;
                    }
                }
                Log.v(TAG, "--- FINISH STATUS WATCH ---");
            }
        });
        try
        {
            if (!threadIsRunning)
            {
                Log.v(TAG, "--- START STATUS WATCH ---");
                thread.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getValue(int statusId)
    {
        return (statusHolder.getValue(statusId));
    }

    @Override
    public void stop()
    {
        threadIsRunning = false;
    }

    private void statusReceivedImpl(byte[] data)
    {
        boolean isStatusUpdated = false;
        try
        {
            if (data.length < 14)
            {
                Log.v(TAG, "received status length is short. (" + data.length + " bytes.)");
                return;
            }

            int nofStatus = (data[13] * 256) + data[12];
            //Log.v(TAG, "status Received. " + data.length + " bytes. [status : " + nofStatus + "]");

            int statusCount = 0;
            int index = 14;
            while ((statusCount < nofStatus)&&(index < data.length))
            {
                int dataId = ((((int)data[index + 1]) & 0xff) * 256) + (((int) data[index]) & 0xff);
                statusHolder.updateValue(dataId, data[index + 2], data[index + 3], data[index +4], data[index + 5]);
                index = index + 6;
                statusCount++;
                isStatusUpdated = true;
            }
            if (isStatusUpdated)
            {
                notify.statusUpdated(statusHolder);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
