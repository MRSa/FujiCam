package net.osdn.gokigen.cameratest.fuji.preference;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import net.osdn.gokigen.cameratest.IApplicationControl;
import net.osdn.gokigen.cameratest.R;

public class FujiPreferenceFragment extends PreferenceFragmentCompat
{
    private PowerOffController powerOffController = null;

    /**
     *
     *
     */
    public static FujiPreferenceFragment newInstance(@NonNull Context context, @NonNull IApplicationControl control)
    {
        FujiPreferenceFragment instance = new FujiPreferenceFragment();
        instance.prepare(context, control);

        // パラメータはBundleにまとめておく
        Bundle arguments = new Bundle();
        //arguments.putString("title", title);
        //arguments.putString("message", message);
        instance.setArguments(arguments);

        return (instance);
    }

    private void prepare(@NonNull Context context, @NonNull IApplicationControl control)
    {
        powerOffController = new PowerOffController(context, control);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences_fuji_x, rootKey);

        ListPreference connectionMethod = (ListPreference) findPreference(IPreferencePropertyAccessor.CONNECTION_METHOD);
        connectionMethod.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue + " ");
                return (true);
            }
        });
        connectionMethod.setSummary(connectionMethod.getValue() + " ");

        findPreference("exit_application").setOnPreferenceClickListener(powerOffController);

    }
}
