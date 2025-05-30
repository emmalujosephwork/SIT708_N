package com.example.learningapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePayUtils {

    public static JSONObject getPaymentDataRequest() throws JSONException {
        JSONObject tokenizationSpec = new JSONObject()
                .put("type", "PAYMENT_GATEWAY")
                .put("parameters", new JSONObject()
                        .put("gateway", "example") // Replace "example" with real gateway like "stripe"
                        .put("gatewayMerchantId", "exampleGatewayMerchantId"));

        JSONArray allowedCardNetworks = new JSONArray()
                .put("VISA")
                .put("MASTERCARD");

        JSONArray allowedAuthMethods = new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS");

        JSONObject cardPaymentMethod = new JSONObject()
                .put("type", "CARD")
                .put("parameters", new JSONObject()
                        .put("allowedAuthMethods", allowedAuthMethods)
                        .put("allowedCardNetworks", allowedCardNetworks))
                .put("tokenizationSpecification", tokenizationSpec);

        JSONObject paymentDataRequest = new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0)
                .put("allowedPaymentMethods", new JSONArray().put(cardPaymentMethod))
                .put("transactionInfo", new JSONObject()
                        .put("totalPrice", "1.00") // Sample amount for testing
                        .put("totalPriceStatus", "FINAL")
                        .put("currencyCode", "USD"))
                .put("merchantInfo", new JSONObject()
                        .put("merchantName", "Example Merchant"));

        return paymentDataRequest;
    }
}
