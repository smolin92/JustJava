package com.example.android.justjava;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    final int COFFEE_PRICE = 5;
    final int WHIPPED_CREAM_PRICE = 1;
    final int CHOCOLATE_PRICE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText customerNameField = findViewById(R.id.customer_name);
        String customerName = customerNameField.getText().toString();

        boolean whippedCream = wantsTopping(R.id.whipped_cream);
        boolean chocolate = wantsTopping(R.id.chocolate);
        composeEmail(new String[] {"android_orders@justjava.com"},
                "Just Java order for " + customerName,
                createOrderSummary(
                customerName,
                calculatePrice(whippedCream, chocolate),
                whippedCream,
                chocolate));
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream wants whipped cream or not
     * @param addChocolate wants chocolate or not
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int cupTotal = COFFEE_PRICE;
        if(addWhippedCream){
            cupTotal += WHIPPED_CREAM_PRICE;
        }
        if(addChocolate){
            cupTotal += CHOCOLATE_PRICE;
        }
        return quantity * cupTotal;
    }

    /**
     * Summarizes the order
     *
     * @param customerName name of the customer
     * @param price of the order
     * @param wantsWhippedCream true/false
     * @param wantsChocolate true/false
     *
     * @return order summary for printing
     */
    private String createOrderSummary(String customerName, int price,
                                      boolean wantsWhippedCream, boolean wantsChocolate) {
        String message = getString(R.string.order_summary_name, customerName);
        message += "\n" + getString(R.string.order_summary_whipped_cream, wantsWhippedCream);
        message += "\n" + getString(R.string.order_summary_chocolate, wantsChocolate);
        message += "\n" + getString(R.string.order_summary_quantity, quantity);
        message += "\n" + getString(R.string.order_summary_total,
                NumberFormat.getCurrencyInstance().format(price));
        message += "\n" + getString(R.string.thank_you);

        return message;
    }

    /**
     * Collects value from a given checkbox
     *
     * @param id for the topping
     *
     * @return boolean for that topping
     */
    private boolean wantsTopping(int id) {
        CheckBox wantsTopping = findViewById(id);
        return wantsTopping.isChecked();
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Increment the quantity
     */
    public void increment(View view){
        if(quantity >= 100) {
            Toast.makeText(getApplicationContext(),
                    "Cannot order more than 100 cups.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * Decrement the quantity
     */
    public void decrement(View view){
        if(quantity <= 1) {
            Toast.makeText(getApplicationContext(),
                    "Cannot order less than 1 cup.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}