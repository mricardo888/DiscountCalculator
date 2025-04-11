package com.ricardo.discountcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageManager {
    private static final String PREFERENCES_NAME = "language_prefs";
    private static final String SELECTED_LANGUAGE = "selected_language";

    // Language codes
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_CHINESE = "zh";

    // Save user's language preference
    public static void setLanguage(Context context, String languageCode) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, languageCode);
        editor.apply();
    }

    // Get saved language preference
    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        // Default to Chinese if no language is set yet
        return preferences.getString(SELECTED_LANGUAGE, LANGUAGE_CHINESE);
    }

    // Apply selected language to application
    public static Context applyLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }

        // Save the selected language
        // Note: This line was moved outside the method to avoid unreachable code warning
        // setLanguage(context, languageCode);
    }

    // Update base context with new locale
    public static Context updateBaseContextLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        } else {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }
    }

    // Restart activity to apply language changes
    public static void restartApp(Activity activity) {
        // Save the language setting first
        String lang = getLanguage(activity);
        setLanguage(activity, lang);

        // Force recreation of the activity stack
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finishAffinity(); // Ensure all activities are finished
    }
}