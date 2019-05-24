package net.osdn.gokigen.cameratest.fuji.preference;

public interface IPreferencePropertyAccessor
{
    String EXIT_APPLICATION = "exit_application";

    String CONNECTION_METHOD = "connection_method";
    String CONNECTION_METHOD_DEFAULT_VALUE = "FUJI_X";

    String CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW = "capture_both_camera_and_live_view";

    String FUJIX_DISPLAY_CAMERA_VIEW = "fujix_display_camera_view";

    String FUJIX_FOCUS_XY = "fujix_focus_xy";
    String FUJIX_FOCUS_XY_DEFAULT_VALUE = "7,7";

    String FUJIX_LIVEVIEW_WAIT = "fujix_liveview_wait";
    String FUJIX_LIVEVIEW_WAIT_DEFAULT_VALUE = "80";

}
