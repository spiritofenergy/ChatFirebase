package com.kodex.chatfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kodex.chatfirebase.databinding.ActivitySingInBinding

class SingInActivity : AppCompatActivity() {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySingInBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val task = getSignedInAccountFromIntent(it.data)
            try {
                 val account = task.getResult((ApiException::class.java))
                if (account != null){
                    firebaseAuthWithGoogle(account.idToken!!)
                    }
            }catch (e: ApiException){
                Log.d("CheckData", "ApiException")
            }
        }
        binding.bSignIn.setOnClickListener {
            singInWithGoogle()
        }
        checkAuthState()
        Toast.makeText(this,
            "Вы уже зарегистрированы!", Toast.LENGTH_LONG).show();
    }
    private fun getClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }
    private fun singInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                Log.d("CheckData", "Google singIn done")
                checkAuthState()
                Toast.makeText(this,
                    "Вы успешно зарегистрированы!", Toast.LENGTH_LONG).show();
            }else{
                Log.d("CheckData", "Google singIn Error")
            }
        }
    }
    private fun checkAuthState(){
        if (auth.currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}