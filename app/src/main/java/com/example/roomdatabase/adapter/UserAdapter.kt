package com.example.roomdatabase.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.activity.DashBoardActivity
import com.example.roomdatabase.constants.isOnBackPress
import com.example.roomdatabase.databinding.ItemUserLayoutBinding
import com.example.roomdatabase.model.UserModel

class UserAdapter(private val userList:List<UserModel>,private val  context: Context): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(val binding :ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

     val binding = ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context),null,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
            return  userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uList =userList[position]
        holder.binding.tvName.text = uList.userName
        holder.binding.root.setOnClickListener {
            val intent =Intent(context,DashBoardActivity::class.java)
            intent.putExtra("email",uList.userEmail)
            isOnBackPress = true
            context.startActivity(intent)
        }
    }
}