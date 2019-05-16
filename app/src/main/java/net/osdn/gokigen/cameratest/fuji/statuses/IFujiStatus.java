package net.osdn.gokigen.cameratest.fuji.statuses;

public interface IFujiStatus
{
    int getValue(int statusId);

    boolean isFocusLocked();
    int getBatteryLevel();
}
