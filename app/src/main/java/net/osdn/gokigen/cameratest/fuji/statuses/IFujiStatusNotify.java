package net.osdn.gokigen.cameratest.fuji.statuses;

/**
 *   カメラのステータスが変わったことを通知する
 *
 */
public interface IFujiStatusNotify
{
    void statusUpdated(final IFujiStatus cameraStatus);
}
