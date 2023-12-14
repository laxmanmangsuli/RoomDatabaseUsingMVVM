package com.example.roomdatabase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityHomeBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userDatabase = UserDatabase
    private var userModel: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.apply {
            val defaultBackground = R.drawable.add_background
            val editTexts = listOf( etUserName, etUserPassword)
            for (editText in editTexts) {
                editText.setOnFocusChangeListener { _, _ ->
                    editText.setBackgroundResource(R.drawable.afterclick_edittext)
                    editTexts.filter { it != editText }.forEach { it.setBackgroundResource(defaultBackground) }
                }
            }
        }
        binding.btnSignIn.setOnClickListener {
            val email = binding.etUserName.text.toString()
            val password = binding.etUserPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val user =
                        userDatabase.getDatabase(this@LoginActivity).userDao().getUserByEmail(email)
                    if (user != null) {
                        if (user.userEmail == email && user.userPassword == password) {
                            val intent=Intent(this@LoginActivity,DashBoardActivity::class.java)
                            intent.putExtra("email",user.userEmail)
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "email and password not match ", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}