package com.gmat.env

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMMM, yyyy (hh:mm a)", Locale.getDefault())
    return formatter.format(date)  // Format the Date with AM/PM
}

fun formatDateWithDay(date: Date): String {
    val formatter = SimpleDateFormat("dd MMMM, yyyy (EEEE)", Locale.getDefault())
    return formatter.format(date)  // Format the Date with AM/PM
}
