package net.osdn.gokigen.cameratest.camtest;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import net.osdn.gokigen.cameratest.R;
import net.osdn.gokigen.cameratest.fuji.Connection;

import androidx.annotation.NonNull;

public class CamTest implements View.OnClickListener
{
    private String TAG = toString();
    private final Activity activity;
    private TextView textview;
    private Connection connection;
    public CamTest(@NonNull Activity activity)
    {
        this.activity = activity;
        this.connection = new Connection();
    }

    public void connect()
    {
        Log.v(TAG, "connect request");
        try
        {
            Snackbar.make(activity.findViewById(R.id.constraintLayout), R.string.connect, Snackbar.LENGTH_SHORT).show();

            showMessageText("START CONNECT");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean ret = connection.start_connect();
                    if (!ret)
                    {
                        showMessageText("CONNECT FAILURE...");
                    }
                }
            });
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void settings()
    {
        Log.v(TAG, "settings menu");

        showMessageText("BBBB");
    }

    private void showMessageText(final String message)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (textview == null) {
                        textview = activity.findViewById(R.id.show_information);
                    }
                    if (textview != null) {
                        textview.setText(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Log.v(TAG, "onClick : " + v.getId());
        int id = v.getId();
        switch (id)
        {
            case R.id.button1:
                showMessageText("Button1");
                break;
            case R.id.button2:
                showMessageText("Button2");
                break;
            case R.id.button3:
                showMessageText("Button3");
                break;
            default:
                showMessageText("Unknown : " + id);
                break;
        }
    }
}
