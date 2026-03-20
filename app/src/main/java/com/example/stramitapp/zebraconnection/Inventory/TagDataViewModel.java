package com.example.stramitapp.zebraconnection.Inventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zebra.rfid.api3.TagData;

public class TagDataViewModel extends ViewModel {

    private final MutableLiveData<TagData[]> inventoryItem =
            new MutableLiveData<>();

    public LiveData<TagData[]> getInventoryItem() {
        return inventoryItem;
    }


    public void setTagItems(TagData[] item) {
        inventoryItem.postValue(null);  // force re-trigger even if same tags scanned
        inventoryItem.postValue(item);  // thread-safe — works from background thread
    }
}