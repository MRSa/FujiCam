package net.osdn.gokigen.cameratest.fuji;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FujiStreamReceiver
{
    private final int portNumber;
    private static final int WAIT_MS = 750;
    private boolean isStart = false;

    FujiStreamReceiver(int portNumber)
    {
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
                    ServerSocket listener = new ServerSocket();
                    listener.setReuseAddress(true);
                    listener.bind(new InetSocketAddress(portNumber));
                    watchMain(listener);
                }
                catch (Exception e)
                {
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

    private void watchMain(ServerSocket listener)
    {
        while (isStart)
        {
            try
            {
                Socket socket = listener.accept();
                InputStream from = socket.getInputStream();

                Thread.sleep(WAIT_MS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }




}
