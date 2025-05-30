package com.example.learningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class UpgradeActivity extends AppCompatActivity {

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private PaymentsClient paymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        // Initialize Google Pay API client
        paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // Change to ENVIRONMENT_PRODUCTION for real payment
                        .build());

        Button btnPurchaseStarter = findViewById(R.id.btnPurchaseStarter);
        btnPurchaseStarter.setOnClickListener(v -> requestPayment());

        // TODO: Add listeners for other buttons if needed
    }

    private void requestPayment() {
        PaymentDataRequest request = createPaymentDataRequest();
        if (request != null) {
            Task<PaymentData> task = paymentsClient.loadPaymentData(request);
            AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private PaymentDataRequest createPaymentDataRequest() {
        try {
            JSONObject paymentDataRequestJson = GooglePayUtils.getPaymentDataRequest();
            return PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        } catch (JSONException e) {
            Log.e("UpgradeActivity", "Error creating payment data request JSON", e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    // You can process paymentData here, e.g., get payment token
                    Toast.makeText(this, "Payment success!", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
                    break;
                case WalletConstants.RESULT_ERROR:
                    Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Handle other cases if needed
                    break;
            }
        }
    }
}
