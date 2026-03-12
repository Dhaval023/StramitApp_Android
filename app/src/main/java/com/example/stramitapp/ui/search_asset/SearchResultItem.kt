package com.example.stramitapp.ui.search_asset

import java.io.Serializable

data class SearchResultItem(
    val locationName: String = "",   // kn_textview
    val companyAssetId: String = "", // code_textview
    val custom13: String = "",       // p_acc_textview — M3CO
    val barcode: String = "",        // bgpb_textview
    val custom18: String = "",       // id_textview — Shipment Number
    val assetId: Int = 0
) : Serializable