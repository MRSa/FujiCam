package net.osdn.gokigen.cameratest.fuji.statuses;

import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

public interface IFujiStatusReceive
{
    void start();
    void stop();
    void statusReceived(ReceivedDataHolder data);
}
