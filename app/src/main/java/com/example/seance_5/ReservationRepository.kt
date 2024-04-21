package com.example.seance_5

import java.util.Date

class ReservationRepository(private val reservationDao: ReservationDao){
    fun addReservation(res: Reservation){
        reservationDao.addReservation(res)
    }
    fun getAllReservation():List<Reservation>{
        return reservationDao.getAllReservation()
    }
    fun getReservationCount():Int{
        return reservationDao.getReservationCount()
    }
    fun getReservationByDate(date: Date):Reservation{
        return reservationDao.getReservationByDate(date)
    }
}