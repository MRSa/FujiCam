package net.osdn.gokigen.cameratest.fuji.preference;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.Preference;

import net.osdn.gokigen.cameratest.ConfirmationDialog;
import net.osdn.gokigen.cameratest.IApplicationControl;
import net.osdn.gokigen.cameratest.R;

public class PowerOffController implements Preference.OnPreferenceClickListener, ConfirmationDialog.Callback
{
    private final Context context;
    private final IApplicationControl appControl;
    private String preferenceKey = null;

    PowerOffController(@NonNull Context context, @NonNull IApplicationControl control)
    {
        this.context = context;
        this.appControl = control;
    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        if (!preference.hasKey())
        {
            return (false);
        }

        preferenceKey = preference.getKey();
        if (preferenceKey.contains(IPreferencePropertyAccessor.EXIT_APPLICATION))
        {

            // 確認ダイアログの生成と表示
            ConfirmationDialog dialog = ConfirmationDialog.newInstance(context);
            dialog.show(R.string.dialog_title_confirmation, R.string.dialog_message_power_off, this);
            return (true);
        }
        return (false);
    }

    /**
     *
     *
     */
    @Override
    public void confirm()
    {
        try
        {
            if (preferenceKey.contains(IPreferencePropertyAccessor.EXIT_APPLICATION))
            {
                // カメラの電源をOFFにしたうえで、アプリケーションを終了する。
                appControl.exitApplication();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
