package com.example.roomdatabase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userDatabase = UserDatabase
    private var userModel: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val editTexts = listOf(etName, etUserName, etUserPassword,etUserCity,etUserMobileNo, etUserConfirmPassword)

            for (editText in editTexts) {
                editText.setOnFocusChangeListener { _, _ ->
                    editText.setBackgroundResource(R.drawable.afterclick_edittext)
                    editTexts.filter { it != editText }.forEach { it.setBackgroundResource(defaultBackground) }
                }
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
            if (password == confirmPassword) {
                userModel =
                    UserModel(
                        userName = name,
                        userEmail = userEmail,
                        userPhoneNumber=phoneNo,
                        userCity=city,
                        userPassword = password
                    )
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        userDatabase.getDatabase(this@SignUpActivity).userDao().addUser(userModel!!)
                    if (result != -1L) {
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        Toast.makeText(this@SignUpActivity, "Account Created", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Failed to create account",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }else{
                Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show()
            }
            } else {
            Toast.makeText(this, "Required all Field", Toast.LENGTH_SHORT).show()
        }
    }
}