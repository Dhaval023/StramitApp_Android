package com.example.stramitapp.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stramitapp.model.Shipment

@Dao
interface ShipmentDao {

    @Query("SELECT * FROM tbl_shipment WHERE shipment_number = :shipmentNumber LIMIT 1")
    suspend fun checkIfShipmentNumberExists(shipmentNumber: String): Shipment?

    @Query("""
        SELECT * FROM tbl_shipment
        WHERE shipment_number LIKE '%' || :shipmentNumber || '%'
    """)
    suspend fun searchShipments(shipmentNumber: String): List<Shipment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shipments: List<Shipment>)

    @Query("SELECT * FROM tbl_shipment")
    suspend fun getAll(): List<Shipment>

    @Query("DELETE FROM tbl_shipment")
    suspend fun deleteAll()
}