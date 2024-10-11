package com.gmat.functionality

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.gmat.data.repository.api.UserAPI
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// Function to start phone number verification
fun startPhoneNumberVerification(
    phoneNumber: String,
    activity: Activity,
    auth: FirebaseAuth,
    onVerificationCompleted: (verificationId: String) -> Unit,
    onVerificationFailed: (String) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential,activity, onSuccess = {}, onFailure = {})
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e.message ?: "Verification failed due to an unknown error")
            }

            override fun onCodeSent(verificationIdSent: String, token: PhoneAuthProvider.ForceResendingToken) {
                // Pass the verification ID back to the calling function for further steps
                onVerificationCompleted(verificationIdSent)
                Toast.makeText(activity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
            }
        })
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

// Function to sign in with the PhoneAuthCredential and check Firestore for user existence
fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception?.message ?: "OTP verification failed")
            }
        }
}


fun formatPhoneNumberForVerification(phoneNumber: String, countryCode: String = "+91"): String {
    // Check if the number starts with "+" and already in E.164 format
    return if (phoneNumber.startsWith("+")) {
        phoneNumber
    } else {
        // Otherwise, add the country code and ensure it's formatted correctly
        "$countryCode$phoneNumber"
    }
}