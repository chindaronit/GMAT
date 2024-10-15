package com.gmat.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmat.data.model.UserModel
import com.gmat.data.model.room.UserRoomDao
import com.gmat.data.model.room.UserRoomModel
import com.gmat.data.repository.api.UserAPI
import com.gmat.env.CHECK_CONNECTION
import com.gmat.env.refreshAuthToken
import com.gmat.ui.events.UserEvents
import com.gmat.ui.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAPI: UserAPI,
    private val dao: UserRoomDao
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    init {
        syncUser()
    }

    private var isRefreshingToken=false

    fun onEvent(event: UserEvents) {
        when (event) {
            is UserEvents.AddUser -> {
                addUser(event.user)
            }

            is UserEvents.GetUserByPhone -> {
                getUserByPhone(event.phNo)
            }

            is UserEvents.GetUserByUserId -> {
                getUserByUserId(event.userId)
            }

            is UserEvents.UpdateUser -> {
                updateUser()
            }

            is UserEvents.SignOut -> {
                deleteRoom()
                _state.update {
                    it.copy(
                        phNo = "",
                        user = null,
                        verificationId = "",
                        newQr = "",
                        newProfile = "",
                        newName = "",
                        newVpa = "",
                        isLoading = false,
                        error = null
                    )
                }
            }

            is UserEvents.ChangePhNo -> {
                _state.update { it.copy(phNo = event.phNo) }
            }

            is UserEvents.SignIn -> {
                getUserByPhone(_state.value.phNo)
            }

            is UserEvents.ChangeVerificationId -> {
                _state.update { it.copy(verificationId = event.id) }
            }

            is UserEvents.OnNameChange -> {
                _state.update { it.copy(newName = event.name) }
            }

            is UserEvents.OnProfileChange -> {
                _state.update { it.copy(newProfile = event.profile) }
            }

            is UserEvents.OnChangeQR -> {
                _state.update { it.copy(newQr = event.qr) }
            }

            is UserEvents.OnChangeVPA -> {
                _state.update { it.copy(newVpa = event.vpa) }
            }

            is UserEvents.ClearNewProfile -> {
                _state.update { it.copy(newProfile = "") }
            }

            is UserEvents.SyncUser -> {
                syncUser()
            }

            is UserEvents.UpdateRoom -> {
                updateRoom(event.user, event.verificationId, event.authToken)
            }

            is UserEvents.RefreshToken -> {
                if (!isRefreshingToken) {
                    isRefreshingToken = true
                    viewModelScope.launch {
                        val newToken = refreshToken()
                        println("New token: $newToken")
                        // Handle the new token (e.g., update state, notify UI, etc.)
                        isRefreshingToken = false // Reset the flag after completion
                    }
                }
            }

            is UserEvents.OnUpdateAuthToken -> {
                _state.update { it.copy(authToken = event.token) }
            }
        }
    }

    private fun getUserByUserId(userId: String) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response =
                    userAPI.getUserByUserId(userId, token = "Bearer ${_state.value.authToken}")
                if (response.isSuccessful && response.body() != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    if (response.code() == 401) {
                        if (refreshToken() != null) {
                            getUserByUserId(userId) // Retry after successful token refresh
                        }
                    } else {
                        handleErrorResponse(response) // Handle other non-successful responses
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Check Your Internet Connection"
                    )
                }
            } finally {
                _state.update { it.copy(isLoading = false) } // Ensure loading state is reset after completion
            }
        }
    }


    private fun getUserByPhone(phNo: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response =
                    userAPI.getUserByPhone(phNo, token = "Bearer ${_state.value.authToken}")
                if (response.isSuccessful && response.body() != null) {
                    println(response.body())
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = response.body()
                        )
                    }
                } else {
                    when (response.code()) {
                        404 -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    user = UserModel(),
                                    error = "User not found"
                                )
                            }
                        }

                        401 -> {
                            if (refreshToken() != null) {
                                getUserByPhone(phNo) // Retry after refreshing token
                            }
                        }

                        else -> {
                            handleErrorResponse(response) // Handle other errors
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Check Your Internet Connection"
                    )
                }
            } finally {
                _state.update { it.copy(isLoading = false) } // Ensure loading state is reset
            }
        }
    }

    private fun addUser(user: UserModel) {
        val phNo = state.value.phNo ?: return // Early return if phNo is null
        user.phNo = phNo

        _state.update { it.copy(isLoading = true) } // Set loading state before making the network request

        viewModelScope.launch {
            try {
                val response = userAPI.addUser(user, token = "Bearer ${_state.value.authToken}")
                if (response.isSuccessful) {
                    getUserByPhone(phNo) // Fetch user details after successful addition
                } else {
                    if (response.code() == 401) { // Check if the error is due to an expired token
                        if (refreshToken() != null) {
                            addUser(user) // Retry adding the user after refreshing the token
                        }
                    } else {
                        handleErrorResponse(response) // Handle other non-successful responses
                    }
                }
            } catch (e: Exception) {
                Log.e("AddUser", "Exception: ${e.message}") // Log the exception for debugging
                _state.update {
                    it.copy(
                        isLoading = false, // Reset loading state
                        error = CHECK_CONNECTION
                    )
                }
            } finally {
                _state.update { it.copy(isLoading = false) } // Ensure loading state is reset after the operation
            }
        }
    }


    private fun updateUser() {
        val currentUser = _state.value.user ?: return // Early return if user is null
        val updatedUser = currentUser.copy(
            profile = _state.value.newProfile.ifBlank { currentUser.profile },
            name = _state.value.newName.ifBlank { currentUser.name },
            qr = _state.value.newQr.ifBlank { currentUser.qr },
            vpa = _state.value.newVpa.ifBlank { currentUser.vpa }
        )

        val userId = updatedUser.userId
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val response = userAPI.updateUser(updatedUser, token = "Bearer ${_state.value.authToken}")

            if (response.isSuccessful) {
                getUserByUserId(userId)
            } else {
                when (response.code()) {
                    401 -> {
                        // Handle token refresh and retry logic
                        val newToken = refreshToken()
                        if (newToken != null) {
                            updateUser()
                        } else {
                            // Handle token refresh failure
                            handleErrorResponse(response)
                        }
                    }
                    else -> {
                        handleErrorResponse(response)
                    }
                }
            }
            _state.update { it.copy(isLoading = false) } // Ensure loading is set to false after completion
        }
    }


    private fun syncUser() {
        viewModelScope.launch {
            dao.getUser().collect { userRoomModel ->
                userRoomModel?.let {
                    val user = UserModel(
                        userId = it.userId,
                        vpa = it.vpa,
                        phNo = it.phNo,
                        profile = it.profile,
                        qr = it.qr,
                        isMerchant = it.isMerchant,
                        name = it.name
                    )
                    // Update your state with the retrieved user data
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            user = user,
                            verificationId = it.verificationId
                        )
                    }
                }
                if (userRoomModel == null) {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun updateRoom(user: UserModel, verificationId: String, authToken: String) {
        val userRoomModel = UserRoomModel(
            userId = user.userId,
            profile = user.profile,
            vpa = user.vpa,
            qr = user.qr,
            phNo = user.phNo,
            name = user.name,
            isMerchant = user.isMerchant,
            verificationId = verificationId,
            authToken = authToken
        )

        viewModelScope.launch {
            dao.upsertUser(userRoomModel)
        }
    }

    private fun deleteRoom() {
        val user = _state.value.user!!
        val userRoomModel = UserRoomModel(
            userId = user.userId,
            profile = user.profile,
            vpa = user.vpa,
            qr = user.qr,
            phNo = user.phNo,
            name = user.name,
            isMerchant = user.isMerchant,
            verificationId = _state.value.verificationId
        )

        viewModelScope.launch {
            dao.deleteUser(userRoomModel)
        }
    }

    suspend fun refreshToken(): String? {
        return withContext(Dispatchers.IO) {
            var token: String?
            val tokenRefreshComplete = CompletableDeferred<String?>()

            refreshAuthToken(
                onTokenRefreshed = { tokenReturned ->
                    token = tokenReturned
                    _state.update { it.copy(authToken = token) }
                    tokenRefreshComplete.complete(token) // Complete with the new token
                    updateRoom(user = _state.value.user!!, verificationId = _state.value.verificationId, authToken = token!!)
                },
                onFailure = {
                    tokenRefreshComplete.complete(null) // Complete with null on failure
                }
            )

            // Wait for the token refresh operation to complete
            tokenRefreshComplete.await()
        }
    }


    private fun handleErrorResponse(response: Response<*>) {
        val errorMessage = when (response.code()) {
            400 -> "Bad Request: Please check the input data"
            404 -> "Not Found: Resource not found"
            500 -> "Internal Server Error: Please try again later"
            else -> {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                errorObj.getString("message")
            }
        }
        Log.e("UserViewModel", "Error: $errorMessage")
        _state.update { it.copy(isLoading = false, error = errorMessage) }
    }
}