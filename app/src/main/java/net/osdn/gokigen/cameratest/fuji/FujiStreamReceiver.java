package net.osdn.gokigen.cameratest.fuji;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.InputStreamReader;
import java.net.Socket;

class FujiStreamReceiver
{
    private final String TAG = toString();
    private final String ipAddress;
    private final int portNumber;
    private final ILiveViewImage imageViewer;
    private static final int BUFFER_SIZE = 1280 * 1024 + 8;
    private static final int WAIT_MS = 50;
    private boolean isStart = false;

    FujiStreamReceiver(String ip, int portNumber, @NonNull ILiveViewImage imageViewer)
    {
        this.ipAddress = ip;
        this.portNumber = portNumber;
        this.imageViewer = imageViewer;
    }

    void start()
    {
        isStart = true;
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Socket socket = new Socket(ipAddress, portNumber);
                    startReceive(socket);
                }
                catch (Exception e)
                {
                    Log.v(TAG, " IP : " + ipAddress + " port : " + portNumber);
                    e.printStackTrace();
                }
            }
        });
        try
        {
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void stop()
    {
        isStart = false;
    }

    private void startReceive(Socket socket)
    {
/**/
        InputStreamReader isr;
        char[] char_array;
        try
        {
            isr = new InputStreamReader(socket.getInputStream());
            char_array = new char[BUFFER_SIZE];

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.v(TAG, "===== startReceive() aborted.");
            return;
        }
/**/
        Log.v(TAG, "startReceive() start.");
        while (isStart)
        {
            try
            {
/*
                Bitmap imageData = BitmapFactory.decodeStream(socket.getInputStream());
                if (imageData != null)
                {
                    // received image
                    Log.v(TAG, "RECEIVED IMAGE.");
                }
                else
                {
                    Log.v(TAG, "IMAGE IS NULL...");
                }
*/
/**/
                int read_bytes = isr.read(char_array, 0, BUFFER_SIZE);
                imageViewer.updateImage(new ReceivedDataHolder(char_array, read_bytes));
/**/
                Thread.sleep(WAIT_MS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
/**/
        try
        {
            isr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
/**/
        Log.v(TAG, "startReceive() end.");
    }
}
