package com.gmat.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.repository.QRCodeRepository
import com.gmat.ui.events.QRScannerEvents
import com.gmat.ui.state.QRScannerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val repo: QRCodeRepository
):ViewModel() {

    private val _state = MutableStateFlow(QRScannerState())
    val state = _state.asStateFlow()

    fun onEvent(event: QRScannerEvents) {
        when(event){
            QRScannerEvents.ClearState -> {
                _state.update { it.copy(details = "") }
            }
            QRScannerEvents.StartScanning -> {
                startScanning()
            }
        }
    }

    private fun startScanning() {
        viewModelScope.launch {
            repo.startScanning().collect {
                if (!it.isNullOrBlank()) {
                    _state.update { state ->
                        state.copy(details = it)
                    }
                }
            }
        }
    }


}