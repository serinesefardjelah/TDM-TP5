package com.example.seance_5

import android.app.Application

class MyApplication: Application() {
    private val dataBase by lazy { ReservationDataBase.getInstance(this) }
    private val reservationDao by lazy { dataBase.getReservationDao() }
    val reservationRepository by lazy { ReservationRepository(reservationDao) }

}