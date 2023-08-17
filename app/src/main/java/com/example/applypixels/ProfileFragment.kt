package com.example.applypixels

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View? {
        // Inflate the layout for this fragment

        var rowVieww=inflater.inflate(R.layout.fragment_profile, container, false)
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance().currentUser?.uid

        val gallaryAccess = rowVieww.findViewById<ImageView>(R.id.images)
        val name = rowVieww.findViewById<EditText>(R.id.names)
        val email = rowVieww.findViewById<EditText>(R.id.emails)
        val button = rowVieww.findViewById<Button>(R.id.edit)
        button.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity2::class.java))

        }
        if (auth != null) {
            db.collection("users").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null
                            name.setText(myData.name)
                            email.setText(myData.email)
                            Glide.with(this)
                                .load(myData.imageUrl)
                                .error(R.drawable.ic_launcher_background)
                                .into(gallaryAccess)
                        } else {
                            Log.e(ContentValues.TAG, "DataModel is null")
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Snapshot is null or doesn't exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error adding document", e)
                }
        }

        return rowVieww
    }
}