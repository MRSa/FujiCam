package net.osdn.gokigen.cameratest.camtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import net.osdn.gokigen.cameratest.fuji.statuses.IFujiStatus;

public class InformationView extends AppCompatImageView
{
    private final String TAG = toString();
    private Point focusPoint;
    private int sd_remain_size;
    private String shooting_mode = null;
    private boolean focus_lock;
    private boolean isDeviceError;
    private int battery_level;
    private int iso = 0;
    private String shutter_speed = "";
    private String aperture = "";
    private String expRev = "";
    private String whiteBalance = "";
    private String focusControlMode = "";
    private String imageAspect = "";
    private String imageFormat = "";
    private String filmSimulation = "";
    private int f_ss_Control = -1;


    public InformationView(Context context) {
        super(context);
        initComponent(context);
    }

    public InformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public InformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    private void initComponent(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawCanvas(canvas);
    }

    private void drawCanvas(Canvas canvas)
    {
        try
        {
            // Clears the canvas.
            canvas.drawARGB(255, 0, 0, 0);

            // Rotates the image.
            int centerX = canvas.getWidth() / 2;
            int centerY = canvas.getHeight() / 2;
            int startX = 90;

            Paint framePaint = new Paint();
            framePaint.setStyle(Paint.Style.STROKE);
            framePaint.setColor(Color.WHITE);
            framePaint.setTextSize(24);


            if (shooting_mode == null)
            {
                String message = "NOT CONNECTED";
                canvas.drawText(message, centerX, centerY, framePaint);
                Log.v(TAG, message);
                return;
            }

            String message = shooting_mode + " REMAIN : " + sd_remain_size  + " ISO : " + iso  + " BATT: ";
            if (battery_level < 0)
            {
                message = message + "???";
            }
            else
            {
                message = message + battery_level + "% ";
            }
            message = message + " " + shutter_speed + " " + aperture + "  " + expRev + " : cnt:" + f_ss_Control;
            canvas.drawText(message, startX, centerY - 50, framePaint);
            Log.v(TAG, message);


            message = "WB: " + whiteBalance + " ";
            if (focusPoint != null)
            {
                message = message + "FOCUS : [" + focusPoint.x + "," + focusPoint.y + "] ";
            }
            if (focus_lock)
            {
                message = message + " (LOCKED)";
            }
            if (isDeviceError)
            {
                message = message + " ERROR";
            }
            message = message + " [" + focusControlMode + "] ";
            canvas.drawText(message, startX, centerY, framePaint);
            Log.v(TAG, message);


            message = imageAspect + " " + imageFormat + " " + "[" + filmSimulation + "]" + " ";
            canvas.drawText(message, startX, centerY + 50, framePaint);
            Log.v(TAG, message);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *   表示エリアの情報を更新する
     *
     *
     */
    public void drawInformation(IFujiStatus cameraStatus)
    {
        focusPoint = cameraStatus.getFocusPoint();
        sd_remain_size = cameraStatus.getRemainImageSpace();
        shooting_mode = cameraStatus.getShootingMode();
        focus_lock = cameraStatus.isFocusLocked();
        battery_level = cameraStatus.getBatteryLevel();
        isDeviceError = cameraStatus.isDeviceError();
        iso = cameraStatus.getIsoSensitivity();
        shutter_speed = cameraStatus.getShutterSpeed();
        aperture = cameraStatus.getAperture();
        expRev = cameraStatus.getExpRev();
        whiteBalance = cameraStatus.getWhiteBalance();
        f_ss_Control = cameraStatus.getF_SS_Control();
        focusControlMode = cameraStatus.getFocusControlMode();
        imageAspect = cameraStatus.getImageAspect();
        imageFormat = cameraStatus.getImageFormat();
        filmSimulation = cameraStatus.getFilmSimulation();
    }

}
