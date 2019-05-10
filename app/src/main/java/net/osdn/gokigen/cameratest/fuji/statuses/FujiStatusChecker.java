package net.osdn.gokigen.cameratest.fuji.statuses;

import android.util.Log;

import androidx.annotation.NonNull;

import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

public class FujiStatusChecker implements IFujiStatusReceive
{
    private final String TAG = toString();
    private final IFujiStatusRequest comm;
    private final IFujiStatusNotify notify;
    private final FujiStatusHolder statusHolder;
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
                try
                {
                    threadIsRunning = true;
                    while (threadIsRunning)
                    {
                        comm.requestStatus();
                        Thread.sleep(WAIT_MS);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                threadIsRunning = false;
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
            Log.v(TAG, "status Received. " + data.length + " bytes.");

/*
RX [00] 7a 00 00 00 02 00 15 10
RX [01] b1 00 00 00 12 00 1b d2
RX [02] 00 00 00 00 0c 50 02 00
RX [03] 00 00 12 50 00 00 00 00
RX [04] 29 d2 67 04 00 00 2a d2
RX [05] a1 06 00 00 0e 50 03 00
RX [06] 00 00 01 50 03 00 00 00
RX [07] 7c d1 04 04 02 03 09 d2
RX [08] 01 00 00 00 10 50 b3 fe
RX [09] ff ff 05 50 02 00 00 00
RX [10] 28 d0 00 00 00 00 0a 50
RX [11] 01 80 00 00 41 d2 0a 00
RX [12] 00 00 18 d0 04 00 00 00
RX [13] 07 50 90 01 00 00 01 d0
RX [14] 03 00 00 00 2a d0 40 06
RX [15] 00 80 0c 00 00 00 03 00
RX [16] 01 20 b1 00 00 00
*/

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
