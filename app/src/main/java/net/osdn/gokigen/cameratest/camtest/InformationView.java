package net.osdn.gokigen.cameratest.camtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import net.osdn.gokigen.cameratest.fuji.statuses.IFujiStatus;

public class InformationView extends AppCompatImageView
{
    String message = "AAAAA";

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

        canvas.drawText(message, centerX, centerY, framePaint);
    }

    /**
     *   表示エリアの情報を更新する
     *
     *
     */
    public void drawInformation(IFujiStatus cameraStatus)
    {


    }

}
