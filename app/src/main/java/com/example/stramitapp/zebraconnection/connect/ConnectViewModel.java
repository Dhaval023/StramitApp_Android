package com.example.stramitapp.zebraconnection.connect;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.ReaderDevice;
import com.example.stramitapp.zebraconnection.RFIDHandler;

import java.util.List;

public class ConnectViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<ReaderDevice>> readersLiveData = new MutableLiveData<>();
    private boolean hasRefreshed = false;

   // private final String TAG = "CONNECT_VIEW_MODEL";
    public ConnectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is connect fragment");
       // refreshReaders();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ReaderDevice>> getReaders() {
        return readersLiveData;
    }

    public void refreshReaders() {
        try {
            RFIDHandler.tryGetAvailableReaders();
            if (RFIDHandler.availableRFIDReaderList != null) {
                Log.d(RFIDHandler.TAG, "RFIDHandler.availableRFIDReaderList size: " + RFIDHandler.availableRFIDReaderList.size());
                if (!RFIDHandler.availableRFIDReaderList.isEmpty()) {
                    for (int i = 0; i < RFIDHandler.availableRFIDReaderList.size(); i++) {
                        Log.d(RFIDHandler.TAG, "Reader[" + i + "]: " + RFIDHandler.availableRFIDReaderList.get(i).getName());
                    }
                }
                readersLiveData.setValue(RFIDHandler.availableRFIDReaderList);
            } else {
                Log.d(RFIDHandler.TAG, "RFIDHandler.availableRFIDReaderList is not initialized");
            }
        } catch (InvalidUsageException e) {
            Log.d(RFIDHandler.TAG, "Error in getting available readers: " + e.getMessage() + " " + e.getVendorMessage());
            e.printStackTrace();
        }
    }
    public boolean hasRefreshed() {
        return hasRefreshed;
    }

}