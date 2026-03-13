package com.example.stramitapp.ui.search_asset

import java.io.Serializable

/**
 * Holds every field that Xamarin's AssetInfoPage shows in Info-mode.
 * Your SearchViewModel must map every field from the API/DB Asset response.
 */
data class SearchResultItem(
    val assetId: Int = 0,

    // ── Standard fields ──────────────────────────────────────────────────────
    val locationName: String? = null,       // location label on card + detail
    val barcode: String? = null,            // ID field
    val companyAssetId: String? = null,     // Product SKU
    val title: String? = null,              // Product Description
    val tag: String? = null,                // RFID Tag
    val serialNumber: String? = null,

    val assetValue: String? = null,         // Quantity  ← Asset.assetValue
    val weight: String? = null,             // Asset.weight
    val weightUom: String? = null,          // Asset.weightUom  (shown as "Height" in Xamarin)

    val longDesc: String? = null,
    val purchaseDate: String? = null,       // "Departure Date" in Xamarin
    val createDate: String? = null,         // "Received Date"  in Xamarin

    val assetTypeName: String? = null,
    val partName: String? = null,
    val categoryName: String? = null,
    val statusName: String? = null,
    val conditionName: String? = null,

    // ── Custom fields 1-36  (Asset.custom1 … Asset.custom36) ─────────────────
    val custom1: String? = null,    // Length
    val custom2: String? = null,    // Width
    val custom3: String? = null,    // Girth
    val custom4: String? = null,    // Colour
    val custom5: String? = null,    // Number of Bends
    val custom6: String? = null,    // Pack Number
    val custom7: String? = null,    // Total Pack Number
    val custom8: String? = null,    // Split Number
    val custom9: String? = null,    // Supplier Reference
    val custom10: String? = null,   // Supplier Number
    val custom11: String? = null,   // Docket Number
    val custom12: String? = null,   // PO Number
    val custom13: String? = null,   // M3 CO
    val custom14: String? = null,   // Customer Name
    val custom15: String? = null,   // Customer Reference
    val custom16: String? = null,   // Delivery Number
    val custom17: String? = null,   // Drop Number
    val custom18: String? = null,   // Shipment Number
    val custom19: String? = null,   // Route
    val custom20: String? = null,   // Delivery Instruction
    val custom21: String? = null,   // Address
    val custom22: String? = null,   // M3 DO
    val custom23: String? = null,   // Quantity (alt)
    val custom24: String? = null,   // QuantityUOM
    val custom25: String? = null,   // LengthUOM  → shown appended to custom1
    val custom26: String? = null,   // HeightUOM  → shown appended to weightUom
    val custom27: String? = null,   // WidthUOM   → shown appended to custom2
    val custom28: String? = null,   // WeightUOM  → shown appended to weight
    val custom29: String? = null,   // GirthUOM   → shown appended to custom3
    val custom30: String? = null,   // ColourUOM  → shown appended to custom4
    val custom31: String? = null,   // Package Structure
    val custom32: String? = null,   // Manufacturing Instruction
    val custom33: String? = null,   // Schedule Number
    val custom34: String? = null,   // Mark Number
    val custom35: String? = null,   // Pack Description
    val custom36: String? = null    // Package Number
) : Serializable