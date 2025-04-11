package com.ricardo.discountcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private TextInputEditText originalPriceInput, discount1Input, discount2Input;
    private AutoCompleteTextView discount1TypeView, discount2TypeView;
    private TextView result1TextView, result2TextView, result3TextView;
    private View cardResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make sure the action bar is showing
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(getString(R.string.app_name));
        } else {
            Log.e(TAG, "ActionBar is null - check your theme");
        }

        setupViews();
        setupDiscountTypeDropdowns();
        setupCalculateButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Make sure the language menu item is visible
        MenuItem languageItem = menu.findItem(R.id.menu_language);
        if (languageItem != null) {
            languageItem.setVisible(true);
            languageItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            Log.d(TAG, "Language menu item configured to be always visible");
        } else {
            Log.e(TAG, "Could not find language menu item");
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Additional check to ensure menu item is visible when menu is prepared
        MenuItem languageItem = menu.findItem(R.id.menu_language);
        if (languageItem != null) {
            languageItem.setVisible(true);
            languageItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_language) {
            // Open language selection activity
            Log.d(TAG, "Language menu item clicked");
            Intent intent = new Intent(this, LanguageSelectionActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        originalPriceInput = findViewById(R.id.input_float_1);
        discount1Input = findViewById(R.id.input_float_2_1);
        discount2Input = findViewById(R.id.input_float_2_2);
        discount1TypeView = findViewById(R.id.spinner_select_1);
        discount2TypeView = findViewById(R.id.spinner_select_2);
        result1TextView = findViewById(R.id.result_1);
        result2TextView = findViewById(R.id.result_2);
        result3TextView = findViewById(R.id.result_3);
        cardResults = findViewById(R.id.card_results);
    }

    private void setupDiscountTypeDropdowns() {
        String[] items = getResources().getStringArray(R.array.options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, items);

        discount1TypeView.setAdapter(adapter);
        discount1TypeView.setText(items[0], false);
        discount2TypeView.setAdapter(adapter);
        discount2TypeView.setText(items[0], false);
    }

    private void setupCalculateButton() {
        findViewById(R.id.btn_calculate).setOnClickListener(v -> {
            calculateDiscounts();
        });

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            clearText();
            cardResults.setVisibility(View.GONE);
        });
    }

    private void clearText() {
        originalPriceInput.setText("");
        discount1Input.setText("");
        discount2Input.setText("");
        result1TextView.setText("");
        result2TextView.setText("");
        result3TextView.setText("");
    }

    private void calculateDiscounts() {
        String originalPriceText = originalPriceInput.getText().toString();
        String discount1Text = discount1Input.getText().toString();
        String discount2Text = discount2Input.getText().toString();
        if (originalPriceText.isEmpty() || (discount1Text.isEmpty() && discount2Text.isEmpty())) {
            showToast();
            return;
        }

        double originalPrice = Double.parseDouble(originalPriceText);

        DiscountResult discount1 = getDiscountResult(originalPrice, discount1Text, discount1TypeView);
        DiscountResult discount2 = getDiscountResult(originalPrice, discount2Text, discount2TypeView);
        cardResults.setVisibility(View.VISIBLE);
        displayResults(discount1, discount2, originalPrice);
    }

    private DiscountResult getDiscountResult(double originalPrice, String discountText, AutoCompleteTextView discountTypeView) {
        if (discountText.isEmpty()) return null;

        double discountValue = Double.parseDouble(discountText);
        String currentLanguage = LanguageManager.getLanguage(this);
        String discountType = discountTypeView.getText().toString();

        boolean isPercentage;
        double finalPrice;
        String description;

        if (currentLanguage.equals(LanguageManager.LANGUAGE_ENGLISH)) {
            // English calculation logic
            isPercentage = discountType.contains("%");
            if (isPercentage) {
                finalPrice = originalPrice * (1 - (discountValue / 100.0));
                description = discountValue + "% off";
            } else {
                finalPrice = originalPrice - discountValue;
                description = "$" + discountValue + " off";
            }
        } else {
            // Chinese calculation logic
            isPercentage = discountType.contains("折");
            if (isPercentage) {
                finalPrice = discountValue >= 10 ? originalPrice * (discountValue / 100.0) : originalPrice * (discountValue / 10.0);
                description = (int) discountValue + "折";
            } else {
                finalPrice = originalPrice - discountValue;
                description = "$" + discountValue;
            }
        }

        return new DiscountResult(finalPrice, originalPrice - finalPrice, description);
    }

    private void displayResults(DiscountResult discount1, DiscountResult discount2, double originalPrice) {
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

        // Use resource strings instead of hardcoded text
        if (discount1 != null) {
            result1TextView.setText(getString(R.string.discount_1) + " (" + discount1.description + "): " +
                    currencyFormat.format(discount1.finalPrice));
        } else {
            result1TextView.setText("");
        }

        if (discount2 != null) {
            result2TextView.setText(getString(R.string.discount_2) + " (" + discount2.description + "): " +
                    currencyFormat.format(discount2.finalPrice));
        } else {
            result2TextView.setText("");
        }

        if (discount1 != null && discount2 != null) {
            if (discount1.finalPrice < discount2.finalPrice) {
                result3TextView.setText(getString(R.string.result_better_1) + " " +
                        currencyFormat.format(discount1.savedAmount) + ")");
            } else if (discount2.finalPrice < discount1.finalPrice) {
                result3TextView.setText(getString(R.string.result_better_2) + " " +
                        currencyFormat.format(discount2.savedAmount) + ")");
            } else {
                result3TextView.setText(getString(R.string.result_equal) + " " +
                        currencyFormat.format(discount1.savedAmount) + ")");
            }
        } else if (discount1 != null) {
            result3TextView.setText(getSavingsText(discount1, originalPrice));
        } else if (discount2 != null) {
            result3TextView.setText(getSavingsText(discount2, originalPrice));
        }
    }

    private String getSavingsText(DiscountResult discount, double originalPrice) {
        return getString(R.string.savings) + " " + new DecimalFormat("$#,##0.00").format(discount.savedAmount) +
                " (" + String.format("%.2f%%", (discount.savedAmount / originalPrice) * 100) + ")";
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }

    private static class DiscountResult {
        final double finalPrice;
        final double savedAmount;
        final String description;

        DiscountResult(double finalPrice, double savedAmount, String description) {
            this.finalPrice = finalPrice;
            this.savedAmount = savedAmount;
            this.description = description;
        }
    }
}