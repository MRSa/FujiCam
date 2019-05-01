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

        if (connect_to_camera())
        {
            ret = get_current_settings();
        }
        return (ret);
    }


    private boolean connect_to_camera()
    {

        try
        {
            int indexNumber = 0;
            comm.connect_socket();

            comm.send_to_camera(sequence.registration_message());

            ReceivedData rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;
/*
            応答エラーの場合は この値が返ってくるはず  = {0x05, 0x00, 0x00, 0x00, 0x19, 0x20, 0x00, 0x00};
*/

            // start_messageを送信
            comm.send_to_camera(sequence.start_message());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

/**/
            //  なんだろう？？ (必要なようだが）
            comm.send_to_camera(sequence.start_message2());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;
/**/

            // two_part messageを発行 (その１)
            comm.send_to_camera(sequence.start_message3_1());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

            // two_part messageを発行 (その２)
            comm.send_to_camera(sequence.start_message3_2());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

            // remote mode
            comm.send_to_camera(sequence.start_message4());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;


            // two_part messageを発行 (その１)
            comm.send_to_camera(sequence.start_message5_1());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

            // two_part messageを発行 (その２)
            comm.send_to_camera(sequence.start_message5_2());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;


            // ????
            comm.send_to_camera(sequence.start_message6());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;


            // ????
            comm.send_to_camera(sequence.start_message7());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

            // ????
            comm.send_to_camera(sequence.start_message8());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;

            // ????
            comm.send_to_camera(sequence.start_message9());

            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;


/*
            comm.send_to_camera(sequence.start_message2());

            //byte[] rx_bytes = comm.receive_from_camera();
            rx_bytes = comm.receive_from_camera();
            dump_bytes(indexNumber, rx_bytes);
            indexNumber++;
*/
/*
            応答OKの場合は、8バイト ({0x03, 0x00, 0x01, 0x20} + {0x10, 0x02, 0x00, 0x00} )が応答されるはず

*/


/*
            auto const msg4_1 =
                make_static_message(message_type::two_part, 0x01, 0xdf, 0x00, 0x00);
            auto const msg4_2 = make_static_message_followup(msg4_1, 0x05, 0x00);
            fuji_twopart_message(sockfd, msg4_1, msg4_2);
*/

            // 別のポートもオープンしておく
            comm.start_stream();
            comm.start_response();

            Log.v(TAG, "connect_to_camera DONE.");

            return (true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    private boolean get_current_settings()
    {
        try
        {
            comm.send_to_camera(sequence.status_request_message());

            ReceivedData rx_bytes = comm.receive_from_camera();
            dump_bytes(12, rx_bytes);

            // なんで２回...　でもやってみる
            rx_bytes = comm.receive_from_camera();
            dump_bytes(13, rx_bytes);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }



    private void dump_bytes(int indexNumber,ReceivedData data)
    {
        int index = 0;
        StringBuffer message;
        message = new StringBuffer();
        for (byte item : data.getData())
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

    public boolean execute_shutter()
    {
        try
        {
            comm.send_to_camera(sequence.execute_shutter_message());

            ReceivedData rx_bytes = comm.receive_from_camera();
            dump_bytes(14, rx_bytes);

            // なんで２回...　でもやってみる
            rx_bytes = comm.receive_from_camera();
            dump_bytes(15, rx_bytes);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }
}
