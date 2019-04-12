package net.osdn.gokigen.cameratest.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.osdn.gokigen.cameratest.R;
import net.osdn.gokigen.cameratest.camtest.CamTest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestViewFragment extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private CamTest testTarget;
    public TestViewFragment()
    {
        super();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    static TestViewFragment newInstance(int sectionNumber, CamTest testTarget)
    {
        TestViewFragment fragment = new TestViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.testTarget = testTarget;
        fragment.setArguments(args);
        return (fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        try
        {
            if (testTarget != null)
            {
                Button btn1 = rootView.findViewById(R.id.button1);
                Button btn2 = rootView.findViewById(R.id.button2);
                Button btn3 = rootView.findViewById(R.id.button3);
                btn1.setOnClickListener(testTarget);
                btn2.setOnClickListener(testTarget);
                btn3.setOnClickListener(testTarget);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (rootView);
    }
}
