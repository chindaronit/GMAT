package com.gmat.ui.events

sealed class QRScannerEvents {
    data object StartScanning: QRScannerEvents()
    data object ClearState: QRScannerEvents()
}
