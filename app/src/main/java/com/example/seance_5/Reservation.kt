package com.example.seance_5

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Reservations")
data class Reservation(
//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey
    var reservationId: Int = 0,
    @ColumnInfo(name = "title")
    var title: String,
    var entry_date: Date?,
    var exit_date: Date?,
    var place : String,
    var price : Double,
    var qrCode: String

)




