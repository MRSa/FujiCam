package net.osdn.gokigen.cameratest.fuji;

import android.util.Log;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class FujiStreamReceiver
{
    private final String TAG = toString();
    private final String ipAddress;
    private final int portNumber;
    private static final int WAIT_MS = 750;
    private boolean isStart = false;

    FujiStreamReceiver(String ip, int portNumber)
    {
        this.ipAddress = ip;
        this.portNumber = portNumber;
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

                    /*
                    ServerSocket listener = new ServerSocket();
                    listener.setReuseAddress(true);
                    listener.bind(new InetSocketAddress(portNumber));
                    watchMain(listener);
                    */
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
        InputStream from = null;
        Log.v(TAG, "startReceive() start.");
        while (isStart)
        {
            try
            {
                from = socket.getInputStream();
                int value = 0;
                int count = 0;
                while ((value = from.read()) != -1)
                {
                    Log.v(TAG, " READ [" + count + "] " + value);
                    count++;
                }
                Thread.sleep(WAIT_MS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (from != null)
        {
            try
            {
                from.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Log.v(TAG, "startReceive() end.");
    }

    private void watchMain(ServerSocket listener)
    {
        InputStream from = null;
        Log.v(TAG, "watchMain() start.");
        while (isStart)
        {
            try
            {
                Socket socket = listener.accept();
                from = socket.getInputStream();
                int value = 0;
                int count = 0;
                while ((value = from.read()) != -1)
                {
                    Log.v(TAG, " READ [" + count + "] " + value);
                    count++;
                }
                Thread.sleep(WAIT_MS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (from != null)
        {
            try
            {
                from.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Log.v(TAG, "watchMain() end.");
    }

}
