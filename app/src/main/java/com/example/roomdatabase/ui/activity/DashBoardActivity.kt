package com.example.roomdatabase.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabase.utility.isOnBackPress
import com.example.roomdatabase.databinding.ActivityDashBoardBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.utility.showToast
import com.example.roomdatabase.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private val userViewModel :UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email")

        binding.btnDelete.setOnClickListener {
            showDefaultDialog(this, email)
        }
        binding.ivLogOut.setOnClickListener {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        CoroutineScope(Dispatchers.IO).launch {
            val user = userViewModel.getUserByEmail(email!!)
            if (user != null) {
                binding.tvEmail.text = email
                binding.etCity.text = user.userCity
                binding.etPhoneNo.text = user.userPhoneNumber
                binding.tvEmail.text = email
                binding.etName.text = user.userName
                binding.etPassword.text = user.userPassword
            } else {
                showToast(this@DashBoardActivity,"Data Not Found")
                }

            binding.btnUpdate.setOnClickListener {
                val intent = Intent(this@DashBoardActivity, UpdateActivity::class.java)
                intent.putExtra("email", user?.userEmail)
                intent.putExtra("name", user?.userName)
                intent.putExtra("city", user?.userCity)
                intent.putExtra("phoneNo", user?.userPhoneNumber)
                intent.putExtra("password", user?.userPassword)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun showDefaultDialog(context: Context, email: String?) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Delete Data")
            setMessage("Are you sure you want to delete?")
            setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userViewModel.deleteUserByEmailId(email!!)
                    if (user != -1) {
                        showToast(this@DashBoardActivity,"Data delete Successfully")
                        startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        showToast(this@DashBoardActivity,"Failed to Data delete")
                    }
                }

            }
            setNeutralButton("No") { _, _ ->

            }

        }.create().show()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (isOnBackPress){
            onBackPressedDispatcher.onBackPressed()
            finish()
        }else{
            finishAffinity()
        }
    }
}