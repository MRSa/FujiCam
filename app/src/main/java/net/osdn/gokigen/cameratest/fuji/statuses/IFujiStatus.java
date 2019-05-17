package net.osdn.gokigen.cameratest.fuji.statuses;

import android.graphics.Point;

public interface IFujiStatus
{
    int getValue(int statusId);

    boolean isDeviceError();
    boolean isFocusLocked();
    int getBatteryLevel();
    int getFlashStatus();
    int getSelfTimerMode();
    int getRemainImageSpace();
    int getMovieImageSpace();
    String getShootingMode();
    Point getFocusPoint();
}
