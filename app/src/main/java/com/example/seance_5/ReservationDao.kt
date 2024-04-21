package com.example.seance_5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface ReservationDao {
    @Insert
    fun addReservation(res: Reservation)
    @Query("SELECT * FROM Reservations")
    fun getAllReservation():List<Reservation>
    @Query("SELECT COUNT(*) FROM Reservations")
    fun getReservationCount():Int
    @Query("SELECT * FROM Reservations WHERE entry_date = :date")
    fun getReservationByDate(date: Date):Reservation

}