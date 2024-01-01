package com.example.roomdatabase.utility

import android.os.Handler
import android.widget.Toast
import com.example.roomdatabase.R
import com.example.roomdatabase.ui.activity.SignUpActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerification(private val signUpActivity: SignUpActivity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    fun startCountdown() {
        var remainingTime = COUNTDOWN_DURATION

        val countDownRunnable = object : Runnable {
            override fun run() {
                val seconds = remainingTime / 1000
                signUpActivity.binding.btnRequestOtp.text = signUpActivity.getString(R.string.remaining_time, seconds)
                remainingTime -= COUNTDOWN_INTERVAL

                if (remainingTime >= 0) {
                    Handler().postDelayed(this, COUNTDOWN_INTERVAL)
                } else {
                    signUpActivity.binding.btnRequestOtp.text = signUpActivity.getString(R.string.send_otp)
                    signUpActivity.binding.btnRequestOtp.isClickable = true
                }
            }
        }

        Handler().postDelayed(countDownRunnable, COUNTDOWN_INTERVAL)
    }

    fun requestOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(signUpActivity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(signUpActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    signUpActivity.binding.btnRequestOtp.text = signUpActivity.getString(R.string.send_otp)
                    signUpActivity.binding.btnRequestOtp.isClickable = true
                    signUpActivity.binding.etUserMobileNo.text.clear()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    this@OtpVerification.verificationId = verificationId
                    Toast.makeText(signUpActivity, "OTP Sent", Toast.LENGTH_SHORT).show()
                    startCountdown()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String) {
        if (verificationId != null) {
            signUpActivity.binding.btnVerifyOtp.text = signUpActivity.getString(R.string.wait)
            signUpActivity.binding.btnVerifyOtp.isClickable = false
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            signInWithPhoneAuthCredential(credential)
        } else {
            Toast.makeText(signUpActivity, "Please request OTP first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(signUpActivity) { task ->
                signUpActivity.binding.btnVerifyOtp.text = signUpActivity.getString(R.string.verify_otp)
                signUpActivity.binding.btnVerifyOtp.isClickable = true
                if (task.isSuccessful) {
                    Toast.makeText(signUpActivity, "Authentication successful", Toast.LENGTH_SHORT).show()
                    signUpActivity.binding.btnSave.isEnabled = true
                    isOtpVarify = true
                } else {
                    Toast.makeText(signUpActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
