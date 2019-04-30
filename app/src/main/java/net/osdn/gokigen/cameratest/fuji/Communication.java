package net.osdn.gokigen.cameratest.fuji;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

class Communication
{
    private final String TAG = toString();
    private static final int BUFFER_SIZE = 131072 + 4;

    private static final int CONTROL_PORT = 55740;
    private static final int ASYNC_RESPONSE_PORT = 55741;
    private static final int STREAM_PORT = 55742;

    private String camera_ip = "192.168.0.1";
    private int camera_port = CONTROL_PORT;

    private Socket socket = null;
    private DataOutputStream dos = null;
    //private DataInputStream dis = null;
    BufferedReader bufferedReader = null;


    Communication()
    {


    }

    void connect_socket()
    {
        try
        {
            socket = new Socket(camera_ip, camera_port);
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

/*
        try
        {
            dis.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dis = null;
*/
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
        Log.v(TAG, "send_to_camera() : " + byte_array.length + " bytes.");
        try
        {
            dos = new DataOutputStream(socket.getOutputStream());
            //bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //dis = new DataInputStream(socket.getInputStream());

            // 最初に４バイトのレングス長をつけて送る
            byte[] sendData = new byte[byte_array.length + 4];

/*
            sendData[0] = 0x00;
            sendData[1] = 0x00;
            sendData[2] = 0x00;
            sendData[3] = (byte) (byte_array.length + 4);
*/
            sendData[0] = (byte) (byte_array.length + 4);
            sendData[1] = 0x00;
            sendData[2] = 0x00;
            sendData[3] = 0x00;

            System.arraycopy(byte_array,0,sendData,4, byte_array.length);

            Log.v(TAG, "send_to_camera() : WRITE " + sendData.length + " bytes.");
            dump_bytes("WRITE ", sendData);

            // (データを)送信
            dos.write(sendData);
            dos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ReceivedData receive_from_camera()
    {
        int receive_bytes = 0;
        InputStreamReader isr = null;
        byte[] byte_array = new byte[BUFFER_SIZE];
        try
        {
            Log.v(TAG, "receive_from_camera() : start.");
             isr  = new InputStreamReader(socket.getInputStream());

            int data = isr.read();
            Log.v(TAG, "receive_from_camera() : #1 ");

            int read_bytes = 0;
            //while (data != -1)
            while ((data >= 0)&&(data <= 255))
            {
                Log.v(TAG, "receive_from_camera() : #2 (" + read_bytes + ") " + data);

                byte_array[read_bytes] = (byte) data;
                read_bytes++;
                data = isr.read();
            }
            receive_bytes = read_bytes;

            //isr.close();

            /*
            dis = new DataInputStream(socket.getInputStream());
            //BufferedInputStream stream = new BufferedInputStream(is);
            while (receive_bytes < BUFFER_SIZE)
            {
                int data = dis.read();
                if (data < 0)
                {
                    break;
                }
                //byte data = dis.readByte();
                byte_array[receive_bytes] = (byte )data;
                receive_bytes++;
            }
*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
/*
        if (receive_bytes > 0)
        {
            dump_bytes("RECEIVE ", byte_array);
        }
*/
        Log.v(TAG, "receive_from_camera() : received " + receive_bytes + " bytes.");
        if (receive_bytes < 0)
        {
            return (new ReceivedData(new byte[0], 0));
        }
        return (new ReceivedData(byte_array, receive_bytes));
    }


    byte[] receive_from_camera_keep()
    {
        int receive_bytes = 0;
        byte[] byte_array = new byte[BUFFER_SIZE];
        try
        {
            Log.v(TAG, "receive_from_camera() : start.");
            if (bufferedReader == null)
            {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Log.v(TAG, " bufferedReader is null...");
                //return (new byte[0]);
            }
            Log.v(TAG, "receive_from_camera() : #1 ");

            //bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int data = bufferedReader.read();

            int read_bytes = 0;
            while (data != -1)
            {
                Log.v(TAG, "receive_from_camera() : #3 (" + read_bytes + ") " + data);

                byte_array[read_bytes] = (byte) data;
                read_bytes++;
                data = bufferedReader.read();
            }
            receive_bytes = read_bytes;

            /*
            dis = new DataInputStream(socket.getInputStream());
            //BufferedInputStream stream = new BufferedInputStream(is);
            while (receive_bytes < BUFFER_SIZE)
            {
                int data = dis.read();
                if (data < 0)
                {
                    break;
                }
                //byte data = dis.readByte();
                byte_array[receive_bytes] = (byte )data;
                receive_bytes++;
            }
*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
/*
        if (receive_bytes > 0)
        {
            dump_bytes("RECEIVE ", byte_array);
        }
*/
        Log.v(TAG, "receive_from_camera() : received " + receive_bytes + " bytes.");
        if (receive_bytes < 4)
        {
            return (new byte[0]);
        }
        return (Arrays.copyOfRange(byte_array, 4, (byte_array.length - 3)));
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


/*
    boolean send_message(byte[] byte_array)
    {
        send_to_camera(byte_array);

    }
*/
/*    bool fuji_message(native_socket const sockfd, uint32_t const id, void const* message,
                      size_t size) {
        fuji_send(sockfd, message, size);

        uint8_t buffer[8];
        size_t receivedBytes = fuji_receive_log(sockfd, buffer);

        if (!is_success_response(id, buffer, receivedBytes)) {
            log(LOG_DEBUG, string_format("received %zd bytes ", receivedBytes).append(hex_format(buffer, receivedBytes)));
            return false;
        }

        return true;
    }
*/

/*
bool is_success_response(uint32_t const id, void const* buffer,
                         size_t const size) {
  if (size != 8) return false;

  struct response_success {
    uint8_t const type[4] = {0x03, 0x00, 0x01, 0x20};
    uint32_t id;
  };

  response_success success = {};
  success.id = id;
  bool const result = memcmp(&success, buffer, 8) == 0;
  if (!result) {
    log(LOG_WARN, std::string("expected: ").append(hex_format(&success, 8)));
    log(LOG_WARN, std::string("actual: ").append(hex_format(buffer, 8)));
  }
  return result;
}

 */


}
