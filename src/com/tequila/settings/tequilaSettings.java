package com.tequila.settings;

import android.os.Bundle;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class tequilaSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings);

        findPreference("about_fragment").setLayoutResource(R.layout.tequila_settings_layout);
        findPreference("statusbar_fragment").setLayoutResource(R.layout.top_level_preference_top);
        findPreference("lockscreen_fragment").setLayoutResource(R.layout.top_level_preference_bottom);
        findPreference("btn_fragment").setLayoutResource(R.layout.top_level_preference_alone);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }

}
