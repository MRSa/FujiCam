package net.osdn.gokigen.cameratest.camtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import net.osdn.gokigen.cameratest.fuji.statuses.Properties;
import net.osdn.gokigen.cameratest.fuji.statuses.IFujiStatus;

public class InformationView extends AppCompatImageView
{
    private final String TAG = toString();
    private int focusPoint;
    private int sd_remain_size;
    private int shooting_mode;
    private boolean focus_lock;
    private int battery_level;
    private int iso;

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
        // Clears the canvas.
        canvas.drawARGB(255, 0, 0, 0);

        // Rotates the image.
        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        Paint framePaint = new Paint();
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setColor(Color.WHITE);

        String message = "SD : " + sd_remain_size + " SHT : " + shooting_mode  + " ISO : " + iso  + " BATT: ";
        if (battery_level < 0)
        {
            message = message + "???";
        }
        else
        {
            message = message + battery_level + "% ";
        }
        canvas.drawText(message, centerX, centerY - 50, framePaint);
        Log.v(TAG, message);


        message = "FOCUS : " + focusPoint;
        if (focus_lock)
        {
            message = message + " (LOCKED)";
        }
        canvas.drawText(message, centerX, centerY, framePaint);
        Log.v(TAG, message);

    }

    /**
     *   表示エリアの情報を更新する
     *
     *
     */
    public void drawInformation(IFujiStatus cameraStatus)
    {
        focusPoint = cameraStatus.getValue(Properties.FOCUS_POINT);
        sd_remain_size = cameraStatus.getValue(Properties.SDCARD_REMAIN_SIZE);
        shooting_mode = cameraStatus.getValue(Properties.SHOOTING_MODE);
        focus_lock = cameraStatus.isFocusLocked();
        battery_level = cameraStatus.getBatteryLevel();
        iso = cameraStatus.getValue(Properties.ISO);
    }

}
