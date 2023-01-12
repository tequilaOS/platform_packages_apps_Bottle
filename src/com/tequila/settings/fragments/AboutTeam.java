package com.tequila.settings.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class AboutTeam extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_about);

        findPreference("about_rom").setLayoutResource(R.layout.about_tequila);
        findPreference("telegram").setLayoutResource(R.layout.top_level_preference_top);
        findPreference("website").setLayoutResource(R.layout.top_level_preference_middle);
        findPreference("github").setLayoutResource(R.layout.top_level_preference_middle);
        findPreference("donate").setLayoutResource(R.layout.top_level_preference_bottom);
        findPreference("ejbtrd").setLayoutResource(R.layout.top_level_preference_top);
        findPreference("cfdddd").setLayoutResource(R.layout.top_level_preference_middle);
        findPreference("dvrkplayer").setLayoutResource(R.layout.top_level_preference_bottom);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }
}
