package com.example.roomdatabase.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabase.R
import com.example.roomdatabase.ui.activity.adapter.UserAdapter
import com.example.roomdatabase.databinding.ActivityShowAllUserBinding
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import com.example.roomdatabase.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowAllUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAllUserBinding
    private lateinit var userAdapter: UserAdapter
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAllUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
        val listOfUser=userViewModel.getAllUser()
            userAdapter = UserAdapter(listOfUser,this@ShowAllUserActivity)
            binding.recyclerView.layoutManager =LinearLayoutManager(this@ShowAllUserActivity)
            binding.recyclerView.adapter = userAdapter
        }

    }
}