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
    int getIsoSensitivity();
    int getF_SS_Control();
    String getShootingMode();
    Point getFocusPoint();
    String getShutterSpeed();
    String getAperture();
    String getExpRev();
    String getWhiteBalance();
    String getFocusControlMode();
    String getImageAspect();
    String getImageFormat();
    String getFilmSimulation();
    boolean isRecModeEnable();
    int getMovieIsoSensitivity();

}
