package com.example.stramitapp.zebraconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BarcodeHandler {

    private static final String TAG = "BarcodeHandler";

    // Zebra DataWedge intent actions
    private static final String ACTION_BARCODE_SCANNED = "com.symbol.datawedge.api.RESULT_ACTION";
    private static final String EXTRA_DATA = "com.symbol.datawedge.data_string";
    private static final String EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type";

    // TC58 specific actions (if different)
    private static final String TC58_BARCODE_ACTION = "TC58BarcodeScanned";

    private final Context context;

    private final MutableLiveData<String> _barcodeDataLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> _barcodeLabelTypeLiveData = new MutableLiveData<>();

    private boolean isRegistered = false;

    public BarcodeHandler(Context context) {
        this.context = context;
    }

    // BroadcastReceiver to receive barcode scans
    private final BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive triggered - Action: " + (intent != null ? intent.getAction() : "null"));

            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    for (String key : extras.keySet()) {
                        Log.d(TAG, "Extra: " + key + " : " + (extras.get(key) != null ? extras.get(key) : "NULL"));
                    }
                }
            }

            if (intent != null && (ACTION_BARCODE_SCANNED.equals(intent.getAction()) ||
                    TC58_BARCODE_ACTION.equals(intent.getAction()))) {

                String barcodeData = intent.getStringExtra(EXTRA_DATA);
                if (barcodeData == null) {
                    barcodeData = "";
                }
                String labelType = intent.getStringExtra(EXTRA_LABEL_TYPE);
                if (labelType == null) {
                    labelType = "";
                }

                Log.d(TAG, "Barcode scanned: " + barcodeData + ", Type: " + labelType);

                if (!barcodeData.isEmpty()) {
                    _barcodeDataLiveData.postValue(barcodeData);
                    _barcodeLabelTypeLiveData.postValue(labelType);
                }
            }
        }
    };

    // Register the broadcast receiver
    public void registerBarcodeReceiver() {
        if (!isRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_BARCODE_SCANNED);
            filter.addAction(TC58_BARCODE_ACTION);
            // Add more general actions to catch any barcode broadcasts
            filter.addAction("com.zebra.android.barcodedata");
            filter.addAction("android.intent.ACTION_DECODE_DATA");

            try {
                if (android.os.Build.VERSION.SDK_INT >= 33) {
                    context.registerReceiver(barcodeReceiver, filter, Context.RECEIVER_EXPORTED);
                } else {
                    context.registerReceiver(barcodeReceiver, filter);
                }
                isRegistered = true;
                Log.d(TAG, "Barcode receiver registered successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error registering receiver: " + e.getMessage());
            }
        }
    }

    // Unregister the broadcast receiver
    public void unregisterBarcodeReceiver() {
        if (isRegistered) {
            try {
                context.unregisterReceiver(barcodeReceiver);
                isRegistered = false;
                Log.d(TAG, "Barcode receiver unregistered");
            } catch (Exception e) {
                Log.e(TAG, "Error unregistering receiver: " + e.getMessage());
            }
        }
    }

    // Clear the barcode data
    public void clearBarcodeData() {
        _barcodeDataLiveData.setValue("");
        _barcodeLabelTypeLiveData.setValue("");
    }

    public LiveData<String> getBarcodeDataLiveData() {
        return _barcodeDataLiveData;
    }

    public LiveData<String> getBarcodeLabelTypeLiveData() {
        return _barcodeLabelTypeLiveData;
    }
}