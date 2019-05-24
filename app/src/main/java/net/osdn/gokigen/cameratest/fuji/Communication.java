package net.osdn.gokigen.cameratest.fuji;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

class Communication
{
    private final String TAG = toString();
    private static final int SEQUENCE_START_NUMBER = 1;

    private static final int BUFFER_SIZE = 1024 * 1024 + 8;
    private static final int CONTROL_PORT = 55740;
    private static final int ASYNC_RESPONSE_PORT = 55741;
    private static final int STREAM_PORT = 55742;
    private static final String CAMERA_IP = "192.168.0.1";

    private Socket socket = null;
    private DataOutputStream dos = null;
    private BufferedReader bufferedReader = null;
    private int sequenceNumber = SEQUENCE_START_NUMBER;

    private boolean isDumpReceiveLog = true;

    private final FujiStreamReceiver stream;
    private final FujiAsyncResponseReceiver response;

    Communication(@NonNull Activity activity, @NonNull ILiveViewImage imageViewer)
    {
        this.stream = new FujiStreamReceiver(activity, CAMERA_IP, STREAM_PORT, imageViewer);
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
        // ストリームを全部閉じる
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
        sequenceNumber = SEQUENCE_START_NUMBER;
        System.gc();
    }

    void send_to_camera(byte[] byte_array, boolean useSeqNumber)
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

            if (useSeqNumber)
            {
                sendData[8]  = (byte) ((0x000000ff & sequenceNumber));
                sendData[9]  = (byte) (((0x0000ff00 & sequenceNumber) >>> 8) & 0x000000ff);
                sendData[10] = (byte) (((0x00ff0000 & sequenceNumber) >>> 16) & 0x000000ff);
                sendData[11] = (byte) (((0xff000000 & sequenceNumber) >>> 24) & 0x000000ff);
                if (isDumpReceiveLog)
                {
                    Log.v(TAG, "SEQ No. : " + sequenceNumber);
                }

                // シーケンス番号を増やす
                sequenceNumber++;
            }
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
        try
        {
            byte[] byte_array = new byte[BUFFER_SIZE];
            InputStream is = socket.getInputStream();
            if (is != null)
            {
                int read_bytes = is.read(byte_array, 0, BUFFER_SIZE);
                if (isDumpReceiveLog)
                {
                    Log.v(TAG, "receive_from_camera() : " + read_bytes + " bytes.");
                }
                return (new ReceivedDataHolder(byte_array, read_bytes));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (new ReceivedDataHolder(new byte[0], 0));
    }

    void dumpReceiveLog(boolean enable)
    {
        isDumpReceiveLog = enable;
    }

    private void dump_bytes(String header, byte[] data)
    {
        if (!isDumpReceiveLog)
        {
            // 受信ログを出さない
            return;
        }

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
