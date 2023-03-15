package com.tequila.settings.fragments;

import android.os.Bundle;
import android.os.UserHandle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.content.ContentResolver;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.development.SystemPropPoker;

import com.tequila.support.preferences.SystemSettingListPreference;
import com.tequila.support.preferences.SystemSettingSwitchPreference;

public class StatusbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    //Variable
      // Battery
         private static final String BATTERY_STYLE = "status_bar_battery_style";
         private static final String SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
         private static final String SHOW_BATTERY_PERCENT_INSIDE = "status_bar_show_battery_percent_inside";
         private static final String SHOW_BATTERY_PERCENT_CHARGING = "status_bar_show_battery_percent_charging";
         private SystemSettingListPreference mBatteryStyle;
         private SystemSettingSwitchPreference mBatteryPercent;
         private SystemSettingSwitchPreference mBatteryPercentInside;
         private SystemSettingSwitchPreference mBatteryPercentCharging;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_statusbar);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mBatteryPercent = (SystemSettingSwitchPreference) findPreference(SHOW_BATTERY_PERCENT);
        final boolean percentEnabled = Settings.System.getIntForUser(resolver, SHOW_BATTERY_PERCENT, 0, UserHandle.USER_CURRENT) == 1;
        mBatteryPercent.setChecked(percentEnabled);
        mBatteryPercent.setOnPreferenceChangeListener(this);
        mBatteryPercentInside = findPreference(SHOW_BATTERY_PERCENT_INSIDE);
        mBatteryPercentInside.setEnabled(percentEnabled);
        final boolean percentInside = Settings.System.getIntForUser(resolver,
                SHOW_BATTERY_PERCENT_INSIDE, 0, UserHandle.USER_CURRENT) == 1;
        mBatteryPercentInside.setChecked(percentInside);
        mBatteryPercentInside.setOnPreferenceChangeListener(this);

        mBatteryStyle = (SystemSettingListPreference) findPreference(BATTERY_STYLE);
        int value = Settings.System.getIntForUser(resolver, BATTERY_STYLE, 0, UserHandle.USER_CURRENT);
        mBatteryStyle.setValue(Integer.toString(value));
        mBatteryStyle.setSummary(mBatteryStyle.getEntry());
        mBatteryStyle.setOnPreferenceChangeListener(this);
        updatePercentEnablement(value != 2);
        mBatteryPercentCharging = findPreference(SHOW_BATTERY_PERCENT_CHARGING);
        updatePercentChargingEnablement(value, percentEnabled, percentInside);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mBatteryStyle) {
            int value = Integer.valueOf((String) newValue);
            int index = mBatteryStyle.findIndexOfValue((String) newValue);
            mBatteryStyle.setSummary(mBatteryStyle.getEntries()[index]);
            Settings.System.putIntForUser(resolver,
                    BATTERY_STYLE, value, UserHandle.USER_CURRENT);
            updatePercentEnablement(value != 2);
            updatePercentChargingEnablement(value, null, null);
            return true;
        } else if (preference == mBatteryPercent) {
            boolean enabled = (boolean) newValue;
            Settings.System.putInt(resolver,
                    SHOW_BATTERY_PERCENT, enabled ? 1 : 0);
            mBatteryPercentInside.setEnabled(enabled);
            updatePercentChargingEnablement(null, enabled, null);
            return true;
        } else if (preference == mBatteryPercentInside) {
            boolean enabled = (boolean) newValue;
            Settings.System.putInt(resolver,
                    SHOW_BATTERY_PERCENT_INSIDE, enabled ? 1 : 0);
            // we already know style isn't text and percent is enabled
            mBatteryPercentCharging.setEnabled(enabled);
            return true;
        }
	return false;
    }

    private void updatePercentEnablement(boolean enabled) {
        mBatteryPercent.setEnabled(enabled);
        mBatteryPercentInside.setEnabled(enabled && mBatteryPercent.isChecked());
    }

    private void updatePercentChargingEnablement(Integer style, Boolean percent, Boolean inside) {
        if (style == null) style = Integer.valueOf(mBatteryStyle.getValue());
        if (percent == null) percent = mBatteryPercent.isChecked();
        if (inside == null) inside = mBatteryPercentInside.isChecked();
        mBatteryPercentCharging.setEnabled(style != 2 && (!percent || inside));
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }
}
