package com.example.roomdatabase.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val buttons = listOf(btnLogin, btnSignUp)

            buttons.forEach { button ->
                button.setOnClickListener {
                    button.setBackgroundResource(R.drawable.button_background)
                    button.setTextColor(resources.getColor(R.color.white))

                    buttons.filter { it != button }.forEach { otherButton ->
                        otherButton.setBackgroundResource(R.drawable.button_beforeclick)
                        otherButton.setTextColor(resources.getColor(R.color.black))
                    }

                    when (button) {
                        btnLogin -> {
                            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                        }
                        btnSignUp -> {
                            startActivity(Intent(this@WelcomeActivity, SignUpActivity::class.java))
                        }
                    }
                }
            }
        }

    }
}