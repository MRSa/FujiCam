package net.osdn.gokigen.cameratest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import net.osdn.gokigen.cameratest.camtest.CamTest;
import net.osdn.gokigen.cameratest.pages.SectionsPagerAdapter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

public class MainActivity extends AppCompatActivity
{
    /////// OpenCV ///////  : license https://opencv.org/license/
    static
    {
        System.loadLibrary("opencv_java4");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private final String TAG = toString();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CamTest testTarget;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTarget = new CamTest(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), testTarget);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                testTarget.connect();
            }
        });

        // パーミッション群のオプトイン
        final int REQUEST_NEED_PERMISSIONS = 1010;
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET,
                    },
                    REQUEST_NEED_PERMISSIONS);
        }
        initializeClass();
        prepareClass();
        onReadyClass();
    }

    /**
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        prepareClass();
        onReadyClass();
    }

    /**
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_wifi_settings)
        {
            try
            {
                // Wifi 設定画面を表示する
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                return (true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return (true);
        }
        if (id == R.id.action_exit)
        {
            // アプリケーションを終了する
            exitApplication();
            return (true);
        }
        if (id == R.id.action_disconnect)
        {
            testTarget.disconnect();
            return (true);
        }
        if (id == R.id.action_settings)
        {
            testTarget.settings();
            return (true);
        }

/*
        if (id == R.id.action_reset)
        {
            testTarget.resetConnection();
            return (true);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_up_value)
        {
            testTarget.valueUp();
            return (true);
        }
        if (id == R.id.action_down_value)
        {
            testTarget.valueDown();
            return (true);
        }
*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * クラスの初期化 (instantiate)
     */
    private void initializeClass()
    {
        try
        {
            Log.v(TAG, "Initialize ...");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * クラスの準備 (prepare)
     */
    private void prepareClass()
    {
        try
        {
            Log.v(TAG, "Prepare ...");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * クラスの準備完了 (onReady)
     */
    private void onReadyClass()
    {
        try
        {
            Log.v(TAG, "on Ready ...");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void exitApplication()
    {
        try
        {
            testTarget.disconnect();
            finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(TAG, "OpenCV library found inside package. Using it!");
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
