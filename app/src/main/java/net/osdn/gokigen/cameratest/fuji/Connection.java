package net.osdn.gokigen.cameratest.fuji;

import android.util.Log;

public class Connection
{
    private final String TAG = toString();
    private final MessageSequence sequence;
    private final Communication comm;

    public Connection()
    {
        this.comm = new Communication();
        this.sequence = new MessageSequence();
    }

    public boolean start_connect()
    {
        boolean ret = false;

        ret = connect_to_camera();

        return (ret);
    }


    private boolean connect_to_camera()
    {

        try
        {
            int indexNumber = 0;
            comm.connect_socket();

            comm.send_to_camera(sequence.registration_message());

            byte[] rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;
/*
            応答エラーの場合は この値が返ってくるはず  = {0x05, 0x00, 0x00, 0x00, 0x19, 0x20, 0x00, 0x00};
*/
            comm.send_to_camera(sequence.start_message());
            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            //indexNumber++;
/*
            応答OKの場合は、8バイト ({0x03, 0x00, 0x01, 0x20} + {0x10, 0x02, 0x00, 0x00} )が応答されるはず

*/


/*
            auto const msg4_1 =
                make_static_message(message_type::two_part, 0x01, 0xdf, 0x00, 0x00);
            auto const msg4_2 = make_static_message_followup(msg4_1, 0x05, 0x00);
            fuji_twopart_message(sockfd, msg4_1, msg4_2);
*/


            return (true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }


    private void dump_bytes(int indexNumber, byte[] data)
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
                Log.v(TAG, " RX [" + indexNumber + "] " + message);
                index = 0;
                message = new StringBuffer();
            }
        }
        if (index != 0)
        {
            Log.v(TAG, " RX [" + indexNumber + "] "  + message);
        }
        System.gc();
    }
}
