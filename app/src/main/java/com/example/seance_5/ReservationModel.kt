package com.example.seance_5

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class ReservationModel(private val ReservationRepository: ReservationRepository) : ViewModel(){
 val count = mutableStateOf(0)

 var allReservations = mutableStateOf(listOf<Reservation>())
 fun getAllReservations(){
  viewModelScope.launch{
   withContext(Dispatchers.IO){
    allReservations.value =  ReservationRepository.getAllReservation()

   }

  }

 }
 fun addReservation(res: Reservation){
  viewModelScope.launch{
   withContext(Dispatchers.IO){
    ReservationRepository.addReservation(res)
   }
  }
 }

 fun getReservationCount(){
  viewModelScope.launch{
   withContext(Dispatchers.IO){
    count.value = ReservationRepository.getReservationCount()
   }
  }
 }

 fun getReservationByDate(date: Date):Reservation{
  var res = Reservation(0,"",null,null,"",0.0,"")
  viewModelScope.launch{
   withContext(Dispatchers.IO){
    res = ReservationRepository.getReservationByDate(date)
   }
  }
  return res
 }

 class Factory(private val reservationRepository: ReservationRepository) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ReservationModel(reservationRepository) as T

  }
 }
}