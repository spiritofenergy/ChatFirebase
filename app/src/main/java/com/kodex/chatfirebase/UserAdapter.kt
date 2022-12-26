package com.kodex.chatfirebase

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kodex.chatfirebase.databinding.UserListItemBinding

class UserAdapter : ListAdapter<User, UserAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: UserListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User) = with(binding){
            message.text = user.message
            userName.text = user.name
           // Log.d("CheckData", "${message.text}, ${userName.text}")
        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(UserListItemBinding
                    .inflate(LayoutInflater.from(parent.context),
                        parent, false))
                Log.d("CheckData", "companion object")
            }
        }
    }
    class ItemComparator: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
            Log.d("CheckData", "oldItem")

        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder.create(parent)
        Log.d("CheckData", "ItemHolder")
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bind(getItem(position))
     }
}