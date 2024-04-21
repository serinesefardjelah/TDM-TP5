package com.example.seance_5

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.Date

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.seance_5", appContext.packageName)
    }
    lateinit var myDataBase:ReservationDataBase
    @Before
    fun initDB() {
        val appContext =
            InstrumentationRegistry.getInstrumentation().targetContext
        myDataBase =
            Room.inMemoryDatabaseBuilder(appContext, ReservationDataBase::class.java).build()
    }
    @Test
    fun testInsertAndGetReservation() {
        var res1 = Reservation(1, "title", Date(), Date(), "place", 100.0, "qrCode")
        myDataBase.getReservationDao().addReservation(res1)
        val res = myDataBase.getReservationDao().getAllReservation().get(0)
        assertEquals(res1, res)
    }
    @Test
    fun testGetAllReservations() {
        var res1 = Reservation(1, "title", Date(), Date(), "place", 100.0, "qrCode")
        var res2 = Reservation(2, "title2", Date(),     Date(), "place2", 200.0, "qrCode2")
        myDataBase.getReservationDao().addReservation(res1)
        myDataBase.getReservationDao().addReservation(res2)
        val res = myDataBase.getReservationDao().getAllReservation()
        assertEquals(2, res.size)
    }
    @Test
    fun testGetReservationByDate() {
        var res1 = Reservation(
            title = "title",
            entry_date = Date(),
            exit_date = Date(),
            place = "place",
            price = 100.0,
            qrCode = "qrCode"
        )
        var res2 = Reservation(
            title = "title2",
            entry_date = Date(Date().time + 1000),
            exit_date = Date(),
            place = "place2",
            price = 200.0,
            qrCode = "qrCode2"
        )

        myDataBase.getReservationDao().addReservation(res1)
        myDataBase.getReservationDao().addReservation(res2)
        val res = myDataBase.getReservationDao().getReservationByDate(res1.entry_date!!)
        assertEquals(res1, res)
    }
}