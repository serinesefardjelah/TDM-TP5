package com.example.seance_5

import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seance_5.ui.theme.Seance_5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import kotlin.random.Random


import java.util.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Seance_5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   // Greeting("Android")
                    val context = LocalContext.current
                    val db = ReservationDataBase.getInstance(context)
                    val reservationDao = db.getReservationDao()

//                    var res1 = Reservation(1, "title")
//                    db.getReservationDao().addReservation(res1)
//                    val res = db.getReservationDao().getAllReservation().get(0)
//                    Display(res)
                    DisplayReservation(reservationDao)

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Seance_5Theme {
        Greeting("Android")
    }
}
@Composable
fun DisplayReservation(reservationDao: ReservationDao){
    val count = remember {
        mutableStateOf(0)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val reservations = remember {
        mutableStateOf(listOf<Reservation>())
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        ReservationForm(reservationDao)
//        Button(onClick ={
//            loading.value = true
//
//
//            CoroutineScope(Dispatchers.IO).launch {
//                for(i in 100 .. 105){
//                    val res = Reservation(i,"title${i}", Date(), Date(), "place${i}", 100.0, "qrCode${i}")
//                //val res = Reservation(1,"title1")
//
//                reservationDao.addReservation(res)
//
//                }
//                loading.value = false
//
//            }
//
//        } ) {
//            Text(text = "Add")
//        }

        Button(onClick ={
            CoroutineScope(Dispatchers.IO).launch {
                count.value=reservationDao.getReservationCount()
                reservations.value = reservationDao.getAllReservation()

            }
        } ) {
            Text(text = "Get")
        }
        Text(text = count.value.toString())
        Text(text = "Reservations")
        DisplayReservations(reservations = reservations.value)
//
//        reservations.value.forEach {
//            Display(it)
//
//        }


        loader(loading = loading.value)

    }
}
@Composable
fun loader(loading : Boolean){
    if(loading){
        CircularProgressIndicator()

    }

}

@Composable
fun Display(reservation: Reservation){
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween


      ) {

        Text(text = reservation.reservationId.toString())
        Text(text = reservation.title)
        Text(text = reservation.entry_date.toString())

    }
}
@Composable
fun DisplayReservations(reservations: List<Reservation>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(reservations) { reservation ->
            ReservationCard(reservation = reservation)
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between cards
        }
    }
}


@Composable
fun ReservationCard(reservation: Reservation) {
    Column(
     modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Title: ${reservation.title}",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Entry Date: ${reservation.entry_date?.toString() ?: "N/A"}",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Exit Date: ${reservation.exit_date?.toString() ?: "N/A"}",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Place: ${reservation.place}",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: ${reservation.price}",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "QR Code: ${reservation.qrCode}",
                color = Color.White
            )
        }
    }
}

@Composable
fun ReservationForm(reservationDao: ReservationDao) {
    var entryDate by remember { mutableStateOf<Date?>(null) }
    var exitDate by remember { mutableStateOf<Date?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        DatePicker(
            label = "Entry Date",
            onDateSelected = { entryDate = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            label = "Exit Date",
            onDateSelected = { exitDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Generate random title, place, price, and QR code
                val randomTitle = "Reservation-${UUID.randomUUID()}"
                val randomPlace = "Place-${Random.nextInt(1, 100)}"
                val randomPrice = Random.nextDouble(50.0, 200.0)
                val randomQRCode = UUID.randomUUID().toString()

                // Add reservation using generated data
                val reservation = Reservation(
                    title = randomTitle,
                    entry_date = entryDate,
                    exit_date = exitDate,
                    place = randomPlace,
                    price = randomPrice,
                    qrCode = randomQRCode
                )

                // Add the reservation to the database
                CoroutineScope(Dispatchers.IO).launch {
                    reservationDao.addReservation(reservation)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Reservation")
        }
    }
}



@Composable
fun DatePicker(
    label: String,
    onDateSelected: (Date) -> Unit
) {
    var textValue by remember { mutableStateOf(TextFieldValue()) }

    // Display a TextField for entering date
    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            // Parse the entered date and invoke the onDateSelected callback
            val date = parseDate(it.text)
            if (date != null) {
                onDateSelected(date)
            }
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun parseDate(text: String): Date? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.parse(text)
    } catch (e: Exception) {
        null
    }
}