package net.osdn.gokigen.cameratest.pages;

import android.content.Context;

import net.osdn.gokigen.cameratest.IApplicationControl;
import net.osdn.gokigen.cameratest.camtest.CamTest;
import net.osdn.gokigen.cameratest.fuji.preference.FujiPreferenceFragment;
import net.osdn.gokigen.cameratest.logcat.LogCatFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private final Context context;
    private final CamTest testTarget;
    private final IApplicationControl appControl;
    private LogCatFragment logCatFragment = null;
    private FujiPreferenceFragment preferenceFragment = null;
    private TestViewFragment testViewFragment = null;
    public SectionsPagerAdapter(@NonNull Context context, FragmentManager fm, CamTest testTarget, IApplicationControl control)
    {
        super(fm);
        this.context = context;
        this.testTarget = testTarget;
        this.appControl = control;
    }

    @Override
    public @NonNull Fragment getItem(int position)
    {
        if (position == 2)
        {
            if (logCatFragment == null)
            {
                logCatFragment = LogCatFragment.newInstance();
            }
            return (logCatFragment);
        }
        else if (position == 1)
        {
            if (preferenceFragment == null)
            {
                preferenceFragment = FujiPreferenceFragment.newInstance(context, appControl);
            }
            return (preferenceFragment);
        }
        if (testViewFragment == null)
        {
            testViewFragment = TestViewFragment.newInstance((position + 1), testTarget);
        }
        return (testViewFragment);
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
