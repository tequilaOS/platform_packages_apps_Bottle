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

import com.tequila.support.preferences.SystemSettingListPreference;
import com.tequila.support.preferences.SystemSettingSwitchPreference;

public class StatusbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    //Variable
      //Combined Signal
         private static final String KEY_COMBINED_SIGNAL_ICONS = "enable_combined_signal_icons";
         private static final String SYS_COMBINED_SIGNAL_ICONS = "persist.sys.enable.combined_signal_icons";
         private SwitchPreference mCombinedSignalIcons;

      // Battery
         private static final String BATTERY_STYLE = "status_bar_battery_style";
         private static final String SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
         private static final String SHOW_BATTERY_PERCENT_INSIDE = "status_bar_show_battery_percent_inside";
         private SystemSettingListPreference mBatteryStyle;
         private SystemSettingSwitchPreference mBatteryPercent;
         private SystemSettingSwitchPreference mBatteryPercentInside;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mCombinedSignalIcons = (SwitchPreference) findPreference(KEY_COMBINED_SIGNAL_ICONS);
        mCombinedSignalIcons.setChecked(SystemProperties.getBoolean(SYS_COMBINED_SIGNAL_ICONS, false));
        mCombinedSignalIcons.setOnPreferenceChangeListener(this);

        mBatteryPercentInside = (SystemSettingSwitchPreference)
                findPreference(SHOW_BATTERY_PERCENT_INSIDE);
        mBatteryPercent = (SystemSettingSwitchPreference)
                findPreference(SHOW_BATTERY_PERCENT);
        enabled = Settings.System.getIntForUser(resolver,
                SHOW_BATTERY_PERCENT, 0, UserHandle.USER_CURRENT) == 1;
        mBatteryPercent.setChecked(enabled);
        mBatteryPercent.setOnPreferenceChangeListener(this);
        mBatteryPercentInside.setEnabled(enabled);

        mBatteryStyle = (SystemSettingListPreference)
                findPreference(BATTERY_STYLE);
        int value = Settings.System.getIntForUser(resolver,
                BATTERY_STYLE, 0, UserHandle.USER_CURRENT);
        mBatteryStyle.setValue(Integer.toString(value));
        mBatteryStyle.setSummary(mBatteryStyle.getEntry());
        mBatteryStyle.setOnPreferenceChangeListener(this);
        updatePercentEnablement(value != 2);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mCombinedSignalIcons) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                Settings.Secure.ENABLE_COMBINED_SIGNAL_ICONS, value ? 1 : 0, UserHandle.USER_CURRENT);
            SystemProperties.set(SYS_COMBINED_SIGNAL_ICONS, value ? "true" : "false");
            SystemPropPoker.getInstance().poke();
            return true;
         } else if (preference == mBatteryStyle) {
            int value = Integer.valueOf((String) objValue);
            int index = mBatteryStyle.findIndexOfValue((String) objValue);
            mBatteryStyle.setSummary(mBatteryStyle.getEntries()[index]);
            Settings.System.putIntForUser(resolver,
                    BATTERY_STYLE, value, UserHandle.USER_CURRENT);
            updatePercentEnablement(value != 2);
            return true;
        } else if (preference == mBatteryPercent) {
            boolean enabled = (boolean) objValue;
            Settings.System.putInt(resolver,
                    SHOW_BATTERY_PERCENT, enabled ? 1 : 0);
            mBatteryPercentInside.setEnabled(enabled);
            return true;
        }
	return false;
    }

    private void updatePercentEnablement(boolean enabled) {
        mBatteryPercent.setEnabled(enabled);
        mBatteryPercentInside.setEnabled(enabled && mBatteryPercent.isChecked());
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }
}