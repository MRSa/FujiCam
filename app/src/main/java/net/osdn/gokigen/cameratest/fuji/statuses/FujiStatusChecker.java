package net.osdn.gokigen.cameratest.fuji.statuses;

import android.util.Log;

import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

public class FujiStatusChecker implements IFujiStatusReceive
{
    private final String TAG = toString();
    private final IFujiStatusRequest comm;
    private boolean threadIsRunning = false;
    private final int WAIT_MS = 400;

    public FujiStatusChecker(IFujiStatusRequest comm)
    {
        this.comm = comm;
    }

    @Override
    public void statusReceived(ReceivedDataHolder data)
    {
        statusReceivedImpl(data.getData());
    }

    @Override
    public void statusReceived2nd(ReceivedDataHolder data)
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
        try
        {
            Log.v(TAG, "status Received. " + data.length + " bytes.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
