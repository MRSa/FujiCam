package net.osdn.gokigen.cameratest.fuji;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class Communication
{
    private final String TAG = toString();
    private static final int BUFFER_SIZE = 1024 * 1024 + 8;

    private static final int CONTROL_PORT = 55740;
    private static final int ASYNC_RESPONSE_PORT = 55741;
    private static final int STREAM_PORT = 55742;
    private static final String CAMERA_IP = "192.168.0.1";

    private Socket socket = null;
    private DataOutputStream dos = null;
    private BufferedReader bufferedReader = null;

    private final FujiStreamReceiver stream;
    private final FujiAsyncResponseReceiver response;

    Communication(@NonNull ILiveViewImage imageViewer)
    {
        this.stream = new FujiStreamReceiver(CAMERA_IP, STREAM_PORT, imageViewer);
        this.response = new FujiAsyncResponseReceiver(CAMERA_IP, ASYNC_RESPONSE_PORT);
    }

    void connect_socket()
    {
        try
        {
            socket = new Socket(CAMERA_IP, CONTROL_PORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void disconnect_socket()
    {
        // ストリームを閉じる
        try
        {
            dos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dos = null;
        try
        {
            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        bufferedReader = null;

        try
        {
            socket.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        socket = null;
        System.gc();
    }

    void send_to_camera(byte[] byte_array)
    {
        //Log.v(TAG, "send_to_camera() : " + byte_array.length + " bytes.");
        try
        {
            dos = new DataOutputStream(socket.getOutputStream());

            // 最初に４バイトのレングス長をつけて送る
            byte[] sendData = new byte[byte_array.length + 4];

            sendData[0] = (byte) (byte_array.length + 4);
            sendData[1] = 0x00;
            sendData[2] = 0x00;
            sendData[3] = 0x00;
            System.arraycopy(byte_array,0,sendData,4, byte_array.length);

            // ログに送信メッセージを出力する
            dump_bytes("SEND[" + sendData.length + "] ", sendData);

            // (データを)送信
            dos.write(sendData);
            dos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ReceivedDataHolder receive_from_camera()
    {
/*
        InputStreamReader isr;
        char[] char_array = new char[BUFFER_SIZE];
        try
        {
            //Log.v(TAG, "receive_from_camera() : start.");
            isr  = new InputStreamReader(socket.getInputStream());
            int read_bytes = isr.read(char_array, 0, BUFFER_SIZE);
            Log.v(TAG, "receive_from_camera() : " + read_bytes + " bytes.");
            return (new ReceivedDataHolder(char_array, read_bytes));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (new ReceivedDataHolder(char_array, 0));
*/
        try
        {
            byte[] byte_array = new byte[BUFFER_SIZE];
            InputStream is = socket.getInputStream();
            if (is != null)
            {
                int read_bytes = is.read(byte_array, 0, BUFFER_SIZE);
                Log.v(TAG, "receive_from_camera() : " + read_bytes + " bytes.");
                return (new ReceivedDataHolder(byte_array, read_bytes));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (new ReceivedDataHolder(new byte[0], 0));
    }

    private void dump_bytes(String header, byte[] data)
    {
        int index = 0;
        StringBuffer message;
        message = new StringBuffer();
        for (byte item : data)
        {
            index++;
            message.append(String.format("%02x ", item));
            if (index >= 8)
            {
                Log.v(TAG, header + " " + message);
                index = 0;
                message = new StringBuffer();
            }
        }
        if (index != 0)
        {
            Log.v(TAG, header + " " + message);
        }
        System.gc();
    }

    void start_stream()
    {
        stream.start();
    }
    void stop_stream()
    {
        stream.stop();
    }

    void start_response()
    {
        response.start();
    }
    void stop_response()
    {
        response.stop();
    }
}
