package com.gmat.env

import com.google.firebase.Timestamp
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Locale


fun formatDate(date: Timestamp?): String {
    return if (date != null) {
        val formatter = SimpleDateFormat("dd MMMM, yyyy (hh:mm a)", Locale.getDefault())
        formatter.format(date.toDate())  // Convert Timestamp to Date and format it
    } else {
        "Unknown Date"  // Handle the null case appropriately
    }
}

fun extractPn(upiUrl: String?): String {
    if (upiUrl == null) return ""

    val regex = "pn=([^&]+)".toRegex()
    val matchResult = regex.find(upiUrl)

    return if (matchResult != null) {
        val encodedPn = matchResult.groupValues[1]
        URLDecoder.decode(encodedPn, StandardCharsets.UTF_8.name()) // Decode URL-encoded value
    } else {
        ""
    }
}
fun extractPa(upiUrl: String?): String {
    if(upiUrl==null) return ""
    val regex = "pa=([^&]+)".toRegex()
    return regex.find(upiUrl)?.groupValues!![1]
}

fun isMerchantUpi(upiUrl: String): Boolean {
    return upiUrl.contains("mc=")
}

fun addGstinToUpiUrl(upiUrl: String, gstin: String): String {
    // Check if the URL already contains any query parameters
    return if (upiUrl.contains("&")) {
        "$upiUrl&gstin=$gstin"
    } else {
        "$upiUrl?gstin=$gstin"
    }
}