package com.example.seance_5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random


@Composable
fun DisplayReservation(reservationModel: ReservationModel){
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
        ReservationForm(reservationModel)
//        Button(onClick ={
//            loading.value = true
//
//
//            CoroutineScope(Dispatchers.IO).launch {
//                for(i in 100 .. 105){
//                    val res = Reservation(i,"title${i}", Date(), Date(), "place${i}", 100.0, "qrCode${i}")
//                //val res = Reservation(1,"title1")
//
//                reservationModel.addReservation(res)
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
                count.value=reservationModel.getReservationCount()
                reservations.value = reservationModel.getAllReservations()

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
fun ReservationForm(reservationModel: ReservationModel) {
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
                    reservationModel.addReservation(reservation)
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