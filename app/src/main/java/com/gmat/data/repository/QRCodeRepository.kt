package com.gmat.data.repository

import kotlinx.coroutines.flow.Flow

interface QRCodeRepository {
    fun startScanning(): Flow<String?>
}