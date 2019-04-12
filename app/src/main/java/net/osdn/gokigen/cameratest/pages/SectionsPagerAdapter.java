package net.osdn.gokigen.cameratest.pages;

import net.osdn.gokigen.cameratest.camtest.CamTest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private final CamTest testTarget;
    public SectionsPagerAdapter(FragmentManager fm, CamTest testTarget)
    {
        super(fm);
        this.testTarget = testTarget;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a TestViewFragment (defined as a static inner class below).
        return TestViewFragment.newInstance((position + 1), testTarget);
    }

    @Override
    public int getCount()
    {
        // Show only one page.
        return 1;
    }
}
