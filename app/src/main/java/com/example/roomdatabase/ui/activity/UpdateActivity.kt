package com.example.roomdatabase.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityUpdateBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import com.example.roomdatabase.utility.showToast
import com.example.roomdatabase.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateBinding
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email =intent.getStringExtra("email")
        val name =intent.getStringExtra("name")
        val city =intent.getStringExtra("city")
        val phone =intent.getStringExtra("phoneNo")

        binding.tvEmail.text = email
        binding.etName.setText(name)
        binding.etCity.setText(city)
        binding.etPhoneNo.setText(phone)

        binding.btnUpdate.setOnClickListener { updateData(email) }
        binding.lLTitle.setOnClickListener { finish() }

        binding.apply {
            val defaultBackground = R.drawable.add_background
            val editTexts = listOf(etName,etCity,etPhoneNo)

            for (editText in editTexts) {
                editText.setOnFocusChangeListener { _, _ ->
                    editText.setBackgroundResource(R.drawable.afterclick_edittext)
                    editTexts.filter { it != editText }.forEach { it.setBackgroundResource(defaultBackground) }
                }
            }
        }

    }

    private fun updateData(email: String?) {
        val password =intent.getStringExtra("password")
        val userModel =  UserModel(
            userName = binding.etName.text.toString(),
            userEmail = email!!,
            userPhoneNumber=binding.etPhoneNo.text.toString(),
            userCity=binding.etCity.text.toString(),
            userPassword = password!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            val user =userViewModel.updateUser(userModel)
            if (user !=-1) {
                    showToast(this@UpdateActivity, "Data Updated Successfully")
                    val intent = Intent(this@UpdateActivity, DashBoardActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()

            }
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}