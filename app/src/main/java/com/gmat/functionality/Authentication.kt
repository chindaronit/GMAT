package com.gmat.functionality

import android.app.Activity
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


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

            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e.message ?: "Verification failed due to an unknown error")
            }

            override fun onCodeSent(
                verificationIdSent: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
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
                // Get the signed-in user's ID token
                val user = task.result?.user
                user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                    if (tokenTask.isSuccessful) {
                        val idToken = tokenTask.result?.token
                        println(idToken)
                        onSuccess()
                    } else {
                        onFailure("Failed to get ID token")
                    }
                }
            } else {
                onFailure("Otp doesn't match")
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