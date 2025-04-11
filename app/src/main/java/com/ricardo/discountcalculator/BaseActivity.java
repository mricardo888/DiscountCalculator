package com.ricardo.discountcalculator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Base activity class that ensures consistent language settings
 * All activities in the app should extend this class
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Apply language settings before calling super.onCreate()
        String savedLanguage = LanguageManager.getLanguage(this);
        LanguageManager.applyLanguage(this, savedLanguage);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Apply language configuration to activity context
        String lang = LanguageManager.getLanguage(newBase);
        Context context = LanguageManager.updateBaseContextLocale(newBase, lang);
        super.attachBaseContext(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Re-apply language configuration if system configuration changes
        String lang = LanguageManager.getLanguage(this);
        LanguageManager.applyLanguage(this, lang);
    }
}