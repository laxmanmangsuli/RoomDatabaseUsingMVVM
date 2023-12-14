package com.example.roomdatabase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.roomdatabase.R
import com.example.roomdatabase.constants.isOnBackPress
import com.example.roomdatabase.databinding.ActivityHomeBinding
import com.example.roomdatabase.model.AdminDatabase
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userDatabase = UserDatabase
    private val adminDatabase = AdminDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
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
                        CoroutineScope(Dispatchers.Main).launch {
                            val user =
                                userDatabase.getDatabase(this@LoginActivity).userDao()
                                    .getUserByEmail(email)
                            if (user != null) {
                                if (user.userEmail == email && user.userPassword == password) {
                                    val intent =
                                        Intent(this@LoginActivity, DashBoardActivity::class.java)
                                    intent.putExtra("email", user.userEmail)
                                    startActivity(intent)
                                    isOnBackPress = false
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Login Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "email and password not match ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            2 -> {
                binding.btnSignIn.setOnClickListener {
                    val email = binding.etUserName.text.toString()
                    val password = binding.etUserPassword.text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val admin =
                                adminDatabase.getDatabase(this@LoginActivity).adminDao()
                                    .getAdminByEmail(email)
                            if (admin != null) {
                                if (admin.adminEmail == email && admin.adminPassword == password) {
                                    val intent =
                                        Intent(this@LoginActivity, ShowAllUserActivity::class.java)
                                    intent.putExtra("email", admin.adminEmail)
                                    startActivity(intent)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Login Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "email and password not match ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, "Data not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else -> {
                binding.btnSignIn.setOnClickListener {
                    Toast.makeText(this, "Select Department", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



