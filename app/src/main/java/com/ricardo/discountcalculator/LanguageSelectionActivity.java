package com.ricardo.discountcalculator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

public class LanguageSelectionActivity extends BaseActivity {
    private static final String TAG = "LanguageSelection";
    private RadioGroup radioGroupLanguage;
    private RadioButton radioEnglish;
    private RadioButton radioChinese;
    private Button btnApplyLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selection);

        // Configure action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.language);
        }

        // Initialize views
        radioGroupLanguage = findViewById(R.id.radio_group_language);
        radioEnglish = findViewById(R.id.radio_english);
        radioChinese = findViewById(R.id.radio_chinese);
        btnApplyLanguage = findViewById(R.id.btn_apply_language);

        // Set the current language selection
        String currentLanguage = LanguageManager.getLanguage(this);
        Log.d(TAG, "Current language: " + currentLanguage);

        if (currentLanguage.equals(LanguageManager.LANGUAGE_ENGLISH)) {
            radioEnglish.setChecked(true);
        } else {
            radioChinese.setChecked(true);
        }

        // Set up the apply button
        btnApplyLanguage.setOnClickListener(v -> {
            String selectedLanguage;

            if (radioEnglish.isChecked()) {
                selectedLanguage = LanguageManager.LANGUAGE_ENGLISH;
            } else {
                selectedLanguage = LanguageManager.LANGUAGE_CHINESE;
            }

            Log.d(TAG, "Selected language: " + selectedLanguage);

            // Apply and save language selection
            try {
                // Save language preference first
                LanguageManager.setLanguage(this, selectedLanguage);

                // Create a new context with the updated locale
                Context newContext = LanguageManager.applyLanguage(this, selectedLanguage);

                // Show toast confirmation
                Toast.makeText(this, "Language changed to " +
                                (selectedLanguage.equals(LanguageManager.LANGUAGE_ENGLISH) ? "English" : "Chinese"),
                        Toast.LENGTH_SHORT).show();

                // Restart app to apply changes
                LanguageManager.restartApp(this);
            } catch (Exception e) {
                Log.e(TAG, "Error changing language: " + e.getMessage());
                Toast.makeText(this, "Error changing language", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}