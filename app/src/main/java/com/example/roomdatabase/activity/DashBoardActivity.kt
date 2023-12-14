package com.example.roomdatabase.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabase.databinding.ActivityDashBoardBinding
import com.example.roomdatabase.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private val userDatabase = UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email")

        binding.btnDelete.setOnClickListener {
            showDefaultDialog(this, email)
        }
        binding.ivLogOut.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

        }

        CoroutineScope(Dispatchers.Main).launch {
            val user =
                userDatabase.getDatabase(this@DashBoardActivity).userDao().getUserByEmail(email!!)
            if (user != null) {
                binding.tvEmail.text = email
                binding.etCity.text = user.userCity
                binding.etPhoneNo.text = user.userPhoneNumber
                binding.tvEmail.text = email
                binding.etName.text = user.userName
                binding.etPassword.text = user.userPassword
            } else {
                Toast.makeText(this@DashBoardActivity, "Data Not Found", Toast.LENGTH_SHORT).show()
            }

            binding.btnUpdate.setOnClickListener {
                val intent = Intent(this@DashBoardActivity, UpdateActivity::class.java)
                intent.putExtra("email", user?.userEmail)
                intent.putExtra("name", user?.userName)
                intent.putExtra("city", user?.userCity)
                intent.putExtra("phoneNo", user?.userPhoneNumber)
                intent.putExtra("password", user?.userPassword)
                startActivity(intent)
            }

        }
    }

    private fun showDefaultDialog(context: Context, email: String?) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Delete Data")
            setMessage("Are you sure you want to delete?")
            setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    val user = userDatabase.getDatabase(this@DashBoardActivity).userDao()
                        .deleteByUserId(email!!)
                    if (user != -1) {
                        Toast.makeText(
                            this@DashBoardActivity, "Data delete Successfully", Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@DashBoardActivity, "Failed to Data delete", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            setNeutralButton("No") { _, _ ->

            }

        }.create().show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}