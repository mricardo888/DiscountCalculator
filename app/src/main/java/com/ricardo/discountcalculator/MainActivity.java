package com.ricardo.discountcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText originalPriceInput, discount1Input, discount2Input;
    private AutoCompleteTextView discount1TypeView, discount2TypeView;
    private TextView result1TextView, result2TextView, result3TextView;
    private View cardResults; // Reference to card_results view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupDiscountTypeDropdowns();
        setupCalculateButton();
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
        cardResults = findViewById(R.id.card_results); // Initialize card_results view
    }

    private void setupDiscountTypeDropdowns() {
        String[] items = {"折", "減"};
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
        boolean isPercentage = discountTypeView.getText().toString().contains("折");
        double finalPrice = isPercentage ? calculatePercentageDiscount(originalPrice, discountValue) : originalPrice - discountValue;

        return new DiscountResult(finalPrice, originalPrice - finalPrice, isPercentage ? (int) discountValue + "折" : "$" + discountValue);
    }

    private double calculatePercentageDiscount(double originalPrice, double discount) {
        return discount >= 10 ? originalPrice * (discount / 100.0) : originalPrice * (discount / 10.0);
    }

    private void displayResults(DiscountResult discount1, DiscountResult discount2, double originalPrice) {
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

        result1TextView.setText(discount1 != null ? "折扣1 (" + discount1.description + "): " + currencyFormat.format(discount1.finalPrice) : "");
        result2TextView.setText(discount2 != null ? "折扣2 (" + discount2.description + "): " + currencyFormat.format(discount2.finalPrice) : "");

        if (discount1 != null && discount2 != null) {
            if (discount1.finalPrice < discount2.finalPrice) {
                result3TextView.setText("結論: 折扣1 更優惠 (省" + currencyFormat.format(discount1.savedAmount) + ")");
            } else if (discount2.finalPrice < discount1.finalPrice) {
                result3TextView.setText("結論: 折扣2 更優惠 (省" + currencyFormat.format(discount2.savedAmount) + ")");
            } else {
                result3TextView.setText("結論: 兩種折扣相等 (省" + currencyFormat.format(discount1.savedAmount) + ")");
            }
        } else if (discount1 != null) {
            result3TextView.setText(getSavingsText(discount1, originalPrice));
        } else if (discount2 != null) {
            result3TextView.setText(getSavingsText(discount2, originalPrice));
        }
    }

    private String getSavingsText(DiscountResult discount, double originalPrice) {
        return "節省: " + new DecimalFormat("$#,##0.00").format(discount.savedAmount) + " (" + String.format("%.2f%%", (discount.savedAmount / originalPrice) * 100) + ")";
    }

    private void showToast() {
        Toast.makeText(this, "請輸入原價", Toast.LENGTH_SHORT).show();
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
