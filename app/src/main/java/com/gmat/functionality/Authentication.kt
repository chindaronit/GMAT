package com.gmat.functionality

import android.app.Activity
import android.util.Log
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
    onVerificationCompleted: (verificationId: String) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber("+91$phoneNumber") // Ensure the phone number is in the correct format
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("Login", "Verification completed with $credential")
                // If automatic verification is completed, sign in automatically
                signInWithPhoneAuthCredential(credential, activity) {
                    Log.d("Login", "Sign-in successful")
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("Login", "Verification failed", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("Login", "Code sent, Verification ID: $verificationId")
                onVerificationCompleted(verificationId) // Notify that verification ID has been received
            }
        })
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

// Function to sign in with the PhoneAuthCredential
fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    onSuccess: () -> Unit
) {
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.d("Login", "signInWithCredential:success")
                onSuccess()
            } else {
                Log.w("Login", "signInWithCredential:failure", task.exception)
            }
        }
}