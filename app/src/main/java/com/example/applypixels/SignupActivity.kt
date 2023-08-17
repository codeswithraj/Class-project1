package com.example.applypixels

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var data: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        data= Firebase.auth
        var email=findViewById<EditText>(R.id.idEmail)
        var pass=findViewById<EditText>(R.id.idPassword)
        var button=findViewById<Button>(R.
        id.idBtn)
        button.setOnClickListener {
            data.createUserWithEmailAndPassword(email.text.toString(),pass.text.toString())
                .addOnSuccessListener {
                    val auth=FirebaseAuth.getInstance().currentUser?.uid
                    val db = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                        "name" to it.user?.displayName,
                        "imageUrl" to it.user?.photoUrl,
                        "email" to it.user?.email
                    )

// Add a new document with a generated ID
                    if (auth != null) {
                        db.collection("users").document(auth)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID")
                                startActivity(Intent(this,LoginActivity::class.java))

                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                            }
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}