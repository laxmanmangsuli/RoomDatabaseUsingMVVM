package com.example.roomdatabase.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityHomeBinding
import com.example.roomdatabase.utility.isOnBackPress
import com.example.roomdatabase.utility.showToast
import com.example.roomdatabase.viewmodel.AdminViewModel
import com.example.roomdatabase.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private  val adminViewModel: AdminViewModel by viewModels()
    private  val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        isOnBackPress = false
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this,WelcomeActivity::class.java))
            finish()
        }

        binding.apply {
            val defaultBackground = R.drawable.add_background
            val editTexts = listOf(etUserName, etUserPassword)
            for (editText in editTexts) {
                editText.setOnFocusChangeListener { _, _ ->
                    editText.setBackgroundResource(R.drawable.afterclick_edittext)
                    editTexts.filter { it != editText }
                        .forEach { it.setBackgroundResource(defaultBackground) }
                }
            }
        }

        val department = resources.getStringArray(R.array.department)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, department)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                login(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun login(position: Int) {
        when (position) {
            1 -> {
                binding.btnSignIn.setOnClickListener {

                    val email = binding.etUserName.text.toString()
                    val password = binding.etUserPassword.text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {


                            val user = userViewModel.getUserByEmail(email)
                            if (user != null) {
                                if (user.userEmail == email && user.userPassword == password) {
                                    val intent =
                                        Intent(this@LoginActivity, DashBoardActivity::class.java)
                                    intent.putExtra("email", user.userEmail)
                                    startActivity(intent)
                                    isOnBackPress = false
                                    showToast(this@LoginActivity, "Login Successfully")
                                    finish()
                                } else {
                                    showToast(this@LoginActivity, "email and password not match")

                                }
                            } else {
                                showToast(this@LoginActivity, "Data not found")
                            }
                        }

                    } else {
                        showToast(this@LoginActivity, "Email and Password is Empty")
                    }
                }
            }
            2 -> {
                binding.btnSignIn.setOnClickListener {
                    val email = binding.etUserName.text.toString()
                    val password = binding.etUserPassword.text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val admin =adminViewModel.getAdminByEmail(email)
                                if (admin != null) {
                                    if (admin.adminEmail == email && admin.adminPassword == password) {
                                        val intent =
                                            Intent(
                                                this@LoginActivity,
                                                ShowAllUserActivity::class.java
                                            )
                                        intent.putExtra("email", admin.adminEmail)
                                        startActivity(intent)
                                        showToast(this@LoginActivity, "Login Successfully")
                                        finish()
                                    } else {
                                        showToast(this@LoginActivity, "email and password not match")
                                    }
                                } else {
                                    showToast(this@LoginActivity, "Data not found")
                                }

                        }
                    } else {
                        showToast(this@LoginActivity, "Email and Password is Empty")
                    }

                }
            }
            else -> {
                binding.btnSignIn.setOnClickListener {
                    showToast(this@LoginActivity, "Select Department")
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,WelcomeActivity::class.java))
        finish()
    }
}



