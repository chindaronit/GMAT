package com.gmat.ui.viewModel

import androidx.lifecycle.ViewModel
import com.gmat.data.repository.api.TransactionAPI
import com.gmat.ui.events.TransactionEvents
import com.gmat.ui.state.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionAPI: TransactionAPI
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    fun onEvent(event: TransactionEvents) {
        when(event){
            is TransactionEvents.AddTransaction -> TODO()
        }
    }

}