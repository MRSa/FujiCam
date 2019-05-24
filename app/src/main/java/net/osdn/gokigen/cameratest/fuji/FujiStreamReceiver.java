package net.osdn.gokigen.cameratest.fuji;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.io.InputStream;
import java.net.Socket;

import static net.osdn.gokigen.cameratest.fuji.preference.IPreferencePropertyAccessor.FUJIX_LIVEVIEW_WAIT;
import static net.osdn.gokigen.cameratest.fuji.preference.IPreferencePropertyAccessor.FUJIX_LIVEVIEW_WAIT_DEFAULT_VALUE;

class FujiStreamReceiver
{
    private final String TAG = toString();
    private final String ipAddress;
    private final int portNumber;
    private final ILiveViewImage imageViewer;
    private int waitMs = 80;
    private static final int BUFFER_SIZE = 1280 * 1024 + 8;
    private static final int ERROR_LIMIT = 30;
    private boolean isStart = false;

    FujiStreamReceiver(@NonNull Activity activity, String ip, int portNumber, @NonNull ILiveViewImage imageViewer)
    {
        this.ipAddress = ip;
        this.portNumber = portNumber;
        this.imageViewer = imageViewer;

        try
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            String waitMsStr = preferences.getString(FUJIX_LIVEVIEW_WAIT, FUJIX_LIVEVIEW_WAIT_DEFAULT_VALUE);
            int wait = Integer.parseInt(waitMsStr);
            if ((wait >= 10)&&(wait <= 800))
            {
                waitMs = wait;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            waitMs = 80;
        }
        Log.v(TAG, "LOOP WAIT : " + waitMs + " ms");
    }

    void start()
    {
        if (isStart)
        {
            // すでに受信スレッド動作中なので抜ける
            return;
        }
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
        int errorCount = 0;
        InputStream isr;
        byte[] byteArray;
        try
        {
            isr = socket.getInputStream();
            byteArray = new byte[BUFFER_SIZE];
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.v(TAG, "===== startReceive() aborted.");
            return;
        }
        while (isStart)
        {
            try
            {
                int read_bytes = isr.read(byteArray, 0, BUFFER_SIZE);
                imageViewer.updateImage(new ReceivedDataHolder(byteArray, read_bytes));
                Thread.sleep(waitMs);
                errorCount = 0;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                errorCount++;
            }
            if (errorCount > ERROR_LIMIT)
            {
                // エラーが連続でたくさん出たらループをストップさせる
                isStart = false;
            }
        }
        try
        {
            isr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
