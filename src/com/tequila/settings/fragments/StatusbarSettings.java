package com.tequila.settings.fragments;

import android.os.Bundle;
import android.os.UserHandle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.content.ContentResolver;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.development.SystemPropPoker;

public class StatusbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    //Variable
      //Combined Signal
         private static final String KEY_COMBINED_SIGNAL_ICONS = "enable_combined_signal_icons";
         private static final String SYS_COMBINED_SIGNAL_ICONS = "persist.sys.enable.combined_signal_icons";
         private SwitchPreference mCombinedSignalIcons;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mCombinedSignalIcons = (SwitchPreference) findPreference(KEY_COMBINED_SIGNAL_ICONS);
        mCombinedSignalIcons.setChecked(SystemProperties.getBoolean(SYS_COMBINED_SIGNAL_ICONS, false));
        mCombinedSignalIcons.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mCombinedSignalIcons) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                Settings.Secure.ENABLE_COMBINED_SIGNAL_ICONS, value ? 1 : 0, UserHandle.USER_CURRENT);
            SystemProperties.set(SYS_COMBINED_SIGNAL_ICONS, value ? "true" : "false");
            SystemPropPoker.getInstance().poke();
            return true;
         }
	return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }
}