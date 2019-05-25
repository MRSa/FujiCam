package net.osdn.gokigen.cameratest.fuji;

import android.app.Activity;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import net.osdn.gokigen.cameratest.fuji.statuses.FujiStatusChecker;
import net.osdn.gokigen.cameratest.fuji.statuses.IFujiStatusNotify;
import net.osdn.gokigen.cameratest.fuji.statuses.IFujiStatusRequest;

import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraProperties.FILM_SIMULATION;
import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraProperties.IMAGE_ASPECT;
import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraPropertyValues.FILM_SIMULATION_MAX;
import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraPropertyValues.FILM_SIMULATION_MIN;
import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraPropertyValues.IMAGE_ASPECT_MAX;
import static net.osdn.gokigen.cameratest.fuji.statuses.IFujiCameraPropertyValues.IMAGE_ASPECT_MIN;

public class Connection implements IFujiStatusRequest
{
    private final String TAG = toString();
    private final MessageSequence sequence;
    private final Communication comm;
    private final FujiStatusChecker statusChecker;
    private boolean pauseRequestStatus = false;

    public Connection(@NonNull Activity activity, @NonNull ILiveViewImage imageViewer, @NonNull IFujiStatusNotify notify)
    {
        this.comm = new Communication(activity, imageViewer);
        this.sequence = new MessageSequence();
        this.statusChecker = new FujiStatusChecker(this, notify);
    }

    public boolean start_connect(boolean isBothLiveView)
    {
        boolean ret = false;

        if (connect_to_camera(isBothLiveView))
        {
            ret = requestStatus();
            if (ret)
            {
                // 定期監視の開始
                statusChecker.start();
            }
        }
        return (ret);
    }

    private boolean connect_to_camera(boolean isBothLiveView)
    {
        try
        {
            ReceivedDataHolder rx_bytes;
            comm.connect_socket();

            comm.send_to_camera(sequence.registration_message(), false, false);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(0, rx_bytes);
            Thread.sleep(50);

            // 応答エラーかどうかをチェックする
            if (rx_bytes.getData().length == 8)
            {
                byte[] receiveData = rx_bytes.getData();
                if ((receiveData[0] == 0x05)&&(receiveData[1] == 0x00)&&(receiveData[2] == 0x00)&&(receiveData[3] == 0x00)&&
                    (receiveData[4] == 0x19)&&(receiveData[5] == 0x20)&&(receiveData[6] == 0x00)&&(receiveData[7] == 0x00))
                {
                    // 応答エラー...
                    return (false);
                }
                return (false);
            }

            // start_messageを送信
            comm.send_to_camera(sequence.start_message(), true, true);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(1, rx_bytes);
            Thread.sleep(50);

            //  なんだろう？？ (送信が必要なようだが）
            comm.send_to_camera(sequence.start_message2(), true, true);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(2, rx_bytes);
            Thread.sleep(50);

            // two_part messageを発行 (その１)
            comm.send_to_camera(sequence.start_message3_1(), true, true);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(3, rx_bytes);
            Thread.sleep(50);

            if (rx_bytes.getData().length <= 50)
            {
                // two_part messageを発行 (その２)
                comm.send_to_camera(sequence.start_message3_2(), false, false);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(4, rx_bytes);
                Thread.sleep(50);
            }

            // remote mode
            comm.send_to_camera(sequence.start_message4(), true, true);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(5, rx_bytes);
            Thread.sleep(50);

            if (!isBothLiveView)
            {
                // two_part messageを発行 (その１)
                comm.send_to_camera(sequence.start_message5_1(), true, false);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(6, rx_bytes);
                Thread.sleep(50);

                // two_part messageを発行 (その２)
                comm.send_to_camera(sequence.start_message5_2(), true, true);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(7, rx_bytes);
                Thread.sleep(50);

                // ????
                comm.send_to_camera(sequence.start_message6(), true, true);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(8, rx_bytes);
                Thread.sleep(50);

                // ????
                comm.send_to_camera(sequence.query_camera_capabilities(), true, true);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(9, rx_bytes);
                Thread.sleep(50);

                // ????
                comm.send_to_camera(sequence.start_message8(), true, true);
                rx_bytes = comm.receive_from_camera();
                dump_bytes(10, rx_bytes);
                Thread.sleep(50);
            }

            // リモート制御の開始！
            comm.send_to_camera(sequence.camera_remote_message(), true, true);

            // 応答OKの場合は、8バイト ({0x03, 0x00, 0x01, 0x20} + {0x10, 0x02, 0x00, 0x00} )が応答されるはず
            rx_bytes = comm.receive_from_camera();
            dump_bytes(11, rx_bytes);
            Thread.sleep(150);


            // 別のポートもオープンして動作を行う。 (1500ms程度待つといけるみたいだ...)
            Thread.sleep(2000);
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

    public void reset_to_camera()
    {
        try
        {
            comm.send_to_camera(sequence.reset_message(), true, true);
            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            dump_bytes(0, rx_bytes);
            statusChecker.stop();
            Thread.sleep(150);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        try
        {
            statusChecker.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            comm.stop_stream();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            comm.stop_response();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            comm.disconnect_socket();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean requestStatus()
    {
        if (pauseRequestStatus)
        {
            // ステータス更新を一時停止する。
            return (false);
        }
        try
        {
            comm.send_to_camera(sequence.status_request_message(), true, true);
            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            if (rx_bytes.getData().length > 0)
            {
                // 受信したステータス情報を渡す
                statusChecker.statusReceived(rx_bytes);
            }
            dump_bytes(12, rx_bytes);

            return (true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    private void dump_bytes(int indexNumber, ReceivedDataHolder data)
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

    public boolean execute_focus_point(PointF point)
    {
        try
        {
            byte x = (byte) (0x000000ff & Math.round(point.x));
            byte y = (byte) (0x000000ff & Math.round(point.y));
            Log.v(TAG, "DRIVE AF (" + x + "," + y + ")");

            comm.send_to_camera(sequence.execute_focus_lock(x, y), true, true);

            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            dump_bytes(16, rx_bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    public boolean execute_change_film_simulation()
    {
        try
        {
            // 現在の値を入手
            int currentValue = statusChecker.getValue(FILM_SIMULATION);
            currentValue++;
            if (currentValue > FILM_SIMULATION_MAX)
            {
                currentValue = FILM_SIMULATION_MIN;
            }
            return (updateProperty(FILM_SIMULATION, currentValue, true));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    public boolean execute_change_image_aspect()
    {
        try
        {
            // 現在の値を入手
            int currentValue = statusChecker.getValue(IMAGE_ASPECT);
            currentValue++;
            if (currentValue > IMAGE_ASPECT_MAX)
            {
                currentValue = IMAGE_ASPECT_MIN;
            }
            return (updateProperty(IMAGE_ASPECT, currentValue, true));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    public boolean execute_query_camera_capability()
    {
        pauseRequestStatus = true;
        try
        {
            comm.send_to_camera(sequence.query_camera_capabilities(), true, true);
            Thread.sleep(50);
            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            dump_bytes(21, rx_bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        pauseRequestStatus = false;
        return (false);
    }


    public boolean execute_unlock_focus()
    {
        try
        {
            comm.send_to_camera(sequence.execute_focus_unlock(), true, true);

            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            dump_bytes(17, rx_bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    public boolean execute_shutter()
    {
        try
        {
            comm.send_to_camera(sequence.execute_shutter_message(), true, true);

            ReceivedDataHolder rx_bytes = comm.receive_from_camera();
            dump_bytes(14, rx_bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (false);
    }

    private boolean updateProperty(int commandCode, int setValue, boolean isShort)
    {
        pauseRequestStatus = true;
        ReceivedDataHolder rx_bytes;
        try {
            byte high = (byte) ((0x0000ff00 & commandCode) >> 8);
            byte low = (byte) (0x000000ff & commandCode);

            byte data0 = (byte)((0xff000000 & setValue) >> 24);
            byte data1 = (byte)((0x00ff0000 & setValue) >> 16);
            byte data2 = (byte)((0x0000ff00 & setValue) >> 8);
            byte data3 = (byte)((0x000000ff & setValue));

            // two_part messageを発行 (その１)
            comm.send_to_camera(sequence.update_property_1(high, low), true, false);
            Thread.sleep(50);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(19, rx_bytes);
/*
            // two_part messageを発行 (その１)  :  ダミーでもう一発...!
            comm.send_to_camera(sequence.update_property_1(high, low), true, false);
            Thread.sleep(50);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(15, rx_bytes);
*/
            // two_part messageを発行 (その２)
            comm.send_to_camera((isShort) ? sequence.update_property_2(data2, data3) : sequence.update_property_2(data0, data1, data2, data3), true, true);
            Thread.sleep(50);
            rx_bytes = comm.receive_from_camera();
            dump_bytes(20, rx_bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        pauseRequestStatus = false;
        return (false);
    }
}
