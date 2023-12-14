package com.example.roomdatabase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabase.R
import com.example.roomdatabase.adapter.UserAdapter
import com.example.roomdatabase.databinding.ActivityShowAllUserBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowAllUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAllUserBinding
    private lateinit var userAdapter: UserAdapter
    private val userDatabase = UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAllUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
        val listOfUser=userDatabase.getDatabase(this@ShowAllUserActivity).userDao().getAllUsers()
            userAdapter = UserAdapter(listOfUser,this@ShowAllUserActivity)
            binding.recyclerView.layoutManager =LinearLayoutManager(this@ShowAllUserActivity)
            binding.recyclerView.adapter = userAdapter
        }

    }
}