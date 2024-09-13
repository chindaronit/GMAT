package com.gmat.data.repository

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class QRCodeRepositoryImpl @Inject constructor(
    private val scanner: GmsBarcodeScanner,
) : QRCodeRepository {

    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch {
                        println(it)
                        send(getDetails(it))
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                    launch {
                        send(null) // Or send an error message like "Scan failed"
                    }
                }
            awaitClose { }
        }
    }


    private fun getDetails(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_WIFI -> {
                val ssid = barcode.wifi!!.ssid
                val password = barcode.wifi!!.password
                val type = barcode.wifi!!.encryptionType
                "ssid : $ssid, password : $password, type : $type"
            }
            Barcode.TYPE_URL -> {
                "url : ${barcode.url!!.url}"
            }

            else -> {
                "Couldn't determine"
            }
        }

    }
}