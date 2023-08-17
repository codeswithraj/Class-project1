package com.example.applypixels

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker

class LoginActivity : AppCompatActivity() {
    private lateinit var data: FirebaseAuth
    lateinit var phoneNumber:String
    private lateinit var vId:String
    lateinit var auth: FirebaseAuth
    private var requestcode = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        data= Firebase.auth
        var email=findViewById<EditText>(R.id.idEmai)
        var pass=findViewById<EditText>(R.id.idPasswor)
        var button=findViewById<Button>(R.id.idBt)
        var signUp=findViewById<TextView>(R.id.sendToSignup)
        signUp.setOnClickListener {
            var intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
        button.setOnClickListener {
            data.signInWithEmailAndPassword(email.text.toString(),pass.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"LogIn successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,SignUppActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
        }

        //Phone Authentication


        auth=Firebase.auth
        var phone = findViewById<EditText>(R.id.contact)
        var ccp = findViewById<CountryCodePicker>(R.id.code)
        var buttonPhone = findViewById<Button>(R.id.logInBtn)
        buttonPhone.setOnClickListener {
            ccp.registerCarrierNumberEditText(phone)
            phoneNumber=ccp.fullNumberWithPlus.replace(" ","")
            initiateOtp()
        }
        // verify otp


        var enterOtp=findViewById<EditText>(R.id.verifyId)
        var verifyButton=findViewById<Button>(R.id.verifyBtn)
        verifyButton.setOnClickListener {

            val credential = PhoneAuthProvider.getCredential(vId, enterOtp.text.toString())
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,SignUppActivity::class.java))

                }
                .addOnFailureListener {
                    Toast.makeText(this, "OTP Invalid", Toast.LENGTH_SHORT).show()
                }

        }

        //Google Authentication
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            auth.signOut()
            val googleIntent = googleSignInClient.signInIntent
            startActivityForResult(googleIntent, requestcode)
        }

    }
    // send otp message
    private fun initiateOtp() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(30L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    vId = verificationId
                    Toast.makeText(this@LoginActivity, "send $verificationId", Toast.LENGTH_SHORT).show()


                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {

                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    override fun onActivityResult(ActivityRequestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(ActivityRequestCode, resultCode, data)

        if (ActivityRequestCode == requestcode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { it ->
                val credencial = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credencial)
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
                                    startActivity(Intent(this,SignUppActivity::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }


    }
}