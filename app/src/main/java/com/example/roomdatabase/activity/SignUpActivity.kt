package com.example.roomdatabase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.roomdatabase.R
import com.example.roomdatabase.constants.COUNTDOWN_DURATION
import com.example.roomdatabase.constants.COUNTDOWN_INTERVAL
import com.example.roomdatabase.constants.Constants
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userDatabase = UserDatabase
    private var userModel: UserModel? = null
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.isEnabled =false

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            createAccount()
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.apply {
            val defaultBackground = R.drawable.add_background
            val editTexts = listOf(etName, etUserName, etUserPassword,etUserCity,etUserMobileNo, etUserConfirmPassword,etOtp)

            for (editText in editTexts) {
                editText.setOnFocusChangeListener { _, _ ->
                    editText.setBackgroundResource(R.drawable.afterclick_edittext)
                    editTexts.filter { it != editText }.forEach { it.setBackgroundResource(defaultBackground) }
                }
            }
        }

        auth = FirebaseAuth.getInstance()
        binding.btnRequestOtp.setOnClickListener {
            val phoneNumber = "+91${binding.etUserMobileNo.text.toString().trim()}"

            if (phoneNumber.isNotEmpty()) {
                binding.btnRequestOtp.text = getString(R.string.wait)
                binding.btnRequestOtp.isClickable = false
                requestOtp(phoneNumber)
            } else {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()
            if (otp.isNotEmpty()) {
                verifyOtp(otp)
            } else {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun requestOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@SignUpActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    this@SignUpActivity.verificationId = verificationId
                    Toast.makeText(this@SignUpActivity, "OTP Sent", Toast.LENGTH_SHORT).show()
                    startCountdown()

                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        if (verificationId != null) {
            binding.btnVerifyOtp.text = getString(R.string.wait)
            binding.btnVerifyOtp.isClickable = false
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            signInWithPhoneAuthCredential(credential)
        } else {
            Toast.makeText(this, "Please request OTP first", Toast.LENGTH_SHORT).show()
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.btnVerifyOtp.text = getString(R.string.verify_otp)
                binding.btnVerifyOtp.isClickable = true
                if (task.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "Authentication successful", Toast.LENGTH_SHORT).show()
                    binding.btnSave.isEnabled =true
                } else {
                    Toast.makeText(this@SignUpActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createAccount() {
        val name = binding.etName.text.toString()
        val userEmail = binding.etUserName.text.toString()
        val password = binding.etUserPassword.text.toString()
        val city = binding.etUserCity.text.toString()
        val phoneNo = binding.etUserMobileNo.text.toString()
        val confirmPassword = binding.etUserConfirmPassword.text.toString()
        if (name.isNotEmpty()&&userEmail.isNotEmpty()&&password.isNotEmpty()) {
            if (isEmailValid(userEmail)) {
                if (password == confirmPassword) {
                    userModel =
                        UserModel(
                            userName = name,
                            userEmail = userEmail,
                            userPhoneNumber = phoneNo,
                            userCity = city,
                            userPassword = password
                        )
                    CoroutineScope(Dispatchers.Main).launch {
                        val result =
                            userDatabase.getDatabase(this@SignUpActivity).userDao()
                                .addUser(userModel!!)
                        if (result != -1L) {
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            Toast.makeText(this@SignUpActivity, "Account Created", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Failed to create account", Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Invalid email req. abc@gmail.com", Toast.LENGTH_SHORT).show()

            }
        }
            else {
            Toast.makeText(this, "Required all Field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun startCountdown() {
        var remainingTime = COUNTDOWN_DURATION

        val countDownRunnable = object : Runnable {
            override fun run() {
                val seconds = remainingTime / 1000
                binding.btnRequestOtp.text = getString(R.string.remaining_time, seconds)
                remainingTime -= COUNTDOWN_INTERVAL

                if (remainingTime >= 0) {
                    Handler().postDelayed(this, COUNTDOWN_INTERVAL)
                } else {
                    binding.btnRequestOtp.text = getString(R.string.send_otp)
                    binding.btnRequestOtp.isClickable = true
                }
            }
        }

        Handler().postDelayed(countDownRunnable, COUNTDOWN_INTERVAL)
    }
}