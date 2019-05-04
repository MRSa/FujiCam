package net.osdn.gokigen.cameratest.camtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import net.osdn.gokigen.cameratest.R;
import net.osdn.gokigen.cameratest.fuji.Connection;
import net.osdn.gokigen.cameratest.fuji.ILiveViewImage;
import net.osdn.gokigen.cameratest.fuji.ReceivedDataHolder;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class CamTest implements View.OnClickListener, ILiveViewImage
{
    private String TAG = toString();
    private final Activity activity;
    private TextView textview;
    private Connection connection;
    private FileOutputStream outputStream = null;
    private FileWriter fileWriter = null;

    public CamTest(@NonNull Activity activity)
    {
        this.activity = activity;
        this.connection = new Connection(this);
    }

    public void connect()
    {
        Log.v(TAG, "connect request");
        try
        {
            prepareFile();

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
                doShutter();
                break;
            case R.id.button2:
                readImageFile("sampledata1.bin");
                showMessageText("show 'sampledata1.bin'.");
                break;
            case R.id.button3:
                readImageFile("sampledata2.bin");
                showMessageText("show 'sampledata2.bin'.");
                break;
            case R.id.button4:
                readImageFile("sampledata3.bin");
                showMessageText("show 'sampledata3.bin'.");
                break;
            default:
                showMessageText("Unknown : " + id);
                break;
        }
    }

    private void doShutter()
    {
        Log.v(TAG, "execute shutter");
        try
        {
            Snackbar.make(activity.findViewById(R.id.constraintLayout), R.string.shutter, Snackbar.LENGTH_SHORT).show();
            showMessageText("SHUTTER");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean ret = connection.execute_shutter();
                    if (!ret)
                    {
                        showMessageText("SHUTTER FAILURE...");
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

    @Override
    public void updateImage(ReceivedDataHolder receivedData)
    {
        try
        {
            final byte[] dataValue = receivedData.getData();
            byte[] startJpegMarker = {(byte)0xff, (byte)0xd8};
            byte[] endJpegMarker   = {(byte)0xff, (byte)0xd9};

            Log.v(TAG, "Image : "+ dataValue.length + " bytes.");

            // ダミーの記録ファイルが開いていたらファイルに書いておく。
            outputFile(receivedData);

            ///////  Bitmap画像を作る... //////
            final Bitmap imageData = BitmapFactory.decodeByteArray(dataValue, 18, (dataValue.length - 18));
            if (imageData == null)
            {
                Log.v(TAG, "imageData is null...");
            }
            else
            {
                Log.v(TAG, "imageData : " + imageData.getByteCount() + " bytes.");
            }

            //////  画像を更新する
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        // ビットマップイメージを表示する。
                        ImageView view = activity.findViewById(R.id.imageView);
                        view.setImageBitmap(imageData);
                        view.invalidate();
                    }
                    catch (Throwable e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateImage(final Bitmap bitmap)
    {
        try
        {
            Log.v(TAG, "bitmap : " + bitmap.getByteCount() + " bytes.");

            //////  画像を更新する
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        // ビットマップイメージを表示する。
                        ImageView view = activity.findViewById(R.id.imageView);
                        view.setImageBitmap(bitmap);
                        view.invalidate();
                    }
                    catch (Throwable e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    private void outputFile(ReceivedDataHolder receivedData)
    {
        try
        {
            if (outputStream != null)
            {
                final byte[] byteData = receivedData.getData();
                outputStream.write(byteData, 0, byteData.length);
            }
            if (fileWriter != null)
            {
                final char[] charData = receivedData.getCharData();
                fileWriter.write(charData, 0, charData.length);
                fileWriter.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void prepareFile()
    {
        boolean useStream = true;
        try
        {
            final String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/AirA01a/";
            final String outputFileName = "camtest.bin";
            File filepath = new File(directoryPath.toLowerCase(), outputFileName.toLowerCase());
            if (useStream) {
                outputStream = new FileOutputStream(filepath);
                fileWriter = null;
            } else {
                outputStream = null;
                fileWriter = new FileWriter(filepath);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            outputStream = null;
            fileWriter = null;
        }
    }

    private void readImageFile(final String readFileName)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                readImageFileImpl(readFileName);
            }
        });
        try
        {
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void readImageFileImpl(final String readFileName)
    {
        try
        {
            Log.v(TAG, "readImageFileImpl() : " + readFileName);
            final String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/AirA01a/";
            File filepath = new File(directoryPath.toLowerCase(), readFileName.toLowerCase());
            FileInputStream istr = new FileInputStream(filepath);
            final Bitmap imageData = BitmapFactory.decodeStream(istr);
            istr.close();
            if (imageData == null)
            {
                Log.v(TAG, "readImageFileImpl() : bitmap is NULL.");
            }
            else
            {
                Log.v(TAG, "readImageFileImpl() : bitmap is " + imageData.getByteCount() + " bytes.");
            }

            //////  画像表示を更新する　//////
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        // ビットマップイメージを表示する。
                        ImageView view = activity.findViewById(R.id.information_view);
                        view.setImageBitmap(imageData);
                        view.invalidate();
                    }
                    catch (Throwable e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
