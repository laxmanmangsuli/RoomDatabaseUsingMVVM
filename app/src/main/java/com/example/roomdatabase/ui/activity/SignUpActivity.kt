package com.example.roomdatabase.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.roomdatabase.R
import com.example.roomdatabase.utility.isOtpVarify
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.model.AdminModel
import com.example.roomdatabase.model.UserModel
import com.example.roomdatabase.utility.OtpVerification
import com.example.roomdatabase.utility.showToast
import com.example.roomdatabase.viewmodel.AdminViewModel
import com.example.roomdatabase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adminViewModel :  AdminViewModel by viewModels()
    private var userModel: UserModel? = null
    private val userViewModel: UserViewModel by viewModels()
    private var adminModel: AdminModel? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var otpVerification: OtpVerification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpVerification = OtpVerification(this)
        binding.ivBack.setOnClickListener {
            finish()
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
            val phone = binding.etUserMobileNo.text.toString().trim()
            if (phone.isNotEmpty()) {
                val phoneNumber = "+91$phone"
                if (phoneNumber.isNotEmpty()) {
                    binding.btnRequestOtp.text = getString(R.string.wait)
                    binding.btnRequestOtp.isClickable = false
                    otpVerification.requestOtp(phoneNumber)
                } else {
                    showToast(this@SignUpActivity,"Please enter a valid phone number")
                }
            }else{
                showToast(this@SignUpActivity,"Required phone number ")
            }
        }

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()
            if (otp.isNotEmpty()) {
                otpVerification.verifyOtp(otp)
            } else {
                showToast(this@SignUpActivity,"Please enter the OTP")
            }
        }

        val department = resources.getStringArray(R.array.department).toList()
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_item,
            department.toTypedArray()
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                accountCreate(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun accountCreate(position: Int) {
//        showFields()
        when (position) {
            1 -> {
                if (isOtpVarify) {
                    binding.btnSave.isEnabled = true
                }
                binding.btnSave.setOnClickListener {
                    val name = binding.etName.text.toString()
                    val userEmail = binding.etUserName.text.toString()
                    val password = binding.etUserPassword.text.toString()
                    val city = binding.etUserCity.text.toString()
                    val phoneNo = binding.etUserMobileNo.text.toString()
                    val confirmPassword = binding.etUserConfirmPassword.text.toString()
                    if (name.isNotEmpty() && userEmail.isNotEmpty() && password.isNotEmpty()) {
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
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = userViewModel.addUer(userModel!!)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        if (result != -1L) {
                                            startActivity(
                                                Intent(
                                                    this@SignUpActivity,
                                                    LoginActivity::class.java
                                                )
                                            )
                                            showToast(this@SignUpActivity,"Account Created")
                                            finish()
                                        } else {
                                            showToast(this@SignUpActivity,"Failed to create account")
                                        }

                                    }
                                }
                            }else {
                                showToast(this@SignUpActivity,"Password and confirm password not match")
                            }
                        } else {
                            showToast(this@SignUpActivity,"Invalid email req. abc@gmail.com")
                        }
                    } else {
                        showToast(this@SignUpActivity,"Required all Field")
                    }
                }
            }
            2 -> {
               hideFields()
                binding.btnSave.setOnClickListener {
                    val name = binding.etName.text.toString()
                    val userEmail = binding.etUserName.text.toString()
                    val password = binding.etUserPassword.text.toString()

                    val confirmPassword = binding.etUserConfirmPassword.text.toString()
                    if (name.isNotEmpty() && userEmail.isNotEmpty() && password.isNotEmpty()) {
                        if (isEmailValid(userEmail)) {
                            if (password == confirmPassword) {
                                adminModel =
                                    AdminModel(
                                        adminName = name,
                                        adminEmail = userEmail,
                                        adminPassword = password
                                    )
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = adminViewModel.addAdmin(adminModel!!)
                                    if (result != -1L) {
                                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                            showToast(this@SignUpActivity,"Account Created")
                                        finish()
                                    } else { showToast(this@SignUpActivity,"Failed to create account")
                                    }

                                }
                            } else {
                                showToast(this,"Password and confirm password not match")
                            }
                        } else {
                            showToast(this,"Invalid email req. abc@gmail.com")

                        }
                    }else {
                        showToast(this,"Required all Field")
                    }
                }
            }
            else -> {
                binding.btnSave.setOnClickListener {
                    showToast(this,"Select Department")
                }
            }
        }
        }

    private fun hideFields() {
        binding.lLSend.visibility = View.GONE
        binding.lLVerify.visibility = View.GONE
        binding.etUserCity.visibility = View.GONE
    }

    private fun showFields() {
        binding.lLSend.visibility = View.VISIBLE
        binding.lLVerify.visibility = View.VISIBLE
        binding.etUserCity.visibility = View.VISIBLE
        binding.btnSave.isEnabled =true
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}