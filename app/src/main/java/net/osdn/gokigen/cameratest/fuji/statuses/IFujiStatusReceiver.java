package net.osdn.gokigen.cameratest.fuji.statuses;

import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

/**
 *   カメラのステータスを受信する
 *
 */
public interface IFujiStatusReceiver
{
    void start();
    void stop();
    void statusReceived(ReceivedDataHolder data);
}
