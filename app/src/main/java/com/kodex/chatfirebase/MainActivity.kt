package com.kodex.chatfirebase

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kodex.chatfirebase.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setUpActionBar()

        val database = Firebase.database("https://chatfirebase-e00a0-default-rtdb.europe-west1.firebasedatabase.app")
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener {
            myRef.child(myRef.push().key ?: "Empty").setValue(User(auth.currentUser?.displayName, binding.editMessage.text.toString()))
        }
        onChangeListener(myRef)
        initRcView()
    }

    private fun initRcView() = with(binding){
            adapter = UserAdapter()
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        rcView.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out){
            auth.signOut()
            finish()
            Toast.makeText(this,
                "Вы вышли из аккаунта",
                Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun onChangeListener(dRef: DatabaseReference){
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
              for (s in snapshot.children ){
                    val user = s.getValue(User::class.java)
                  if(user !== null)list.add(user)
              }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun setUpActionBar(){
        val ab = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)
            runOnUiThread{
                ab?.setDisplayHomeAsUpEnabled(true)
                ab?.setHomeAsUpIndicator(dIcon)
                ab?.title = auth.currentUser?.displayName
            }
        }.start()
        }

}