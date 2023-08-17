package com.example.applypixels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_upp)
        val password = findViewById<EditText>(R.id.pwd)
        val button = findViewById<Button>(R.id.homepageBtn)
        val reset = findViewById<Button>(R.id.resetBtn)
        reset.setOnClickListener {

        }
        val allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#\$%^&*()_-+=[]{}|;:',.<>/?`~"
        password.filters = arrayOf(CharacterInputFilter(allowedCharacters))
        password.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT

        button.setOnClickListener {
            val text = password.text.toString()
            val hasLowerCase = text.matches(Regex(".*[a-z].*"))
            val hasUpperCase = text.matches(Regex(".*[A-Z].*"))
            val hasDigit = text.matches(Regex(".*\\d.*"))
            val hasSpecialCharacter = text.matches(Regex(".*[!@#\$%^&*()\\-_+=\\[\\]{}|;:',.<>/?`~].*"))
            if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialCharacter) {
                Toast.makeText(this, "Welcome To ClassTest", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomepageActivity::class.java))
            } else {
                Toast.makeText(this, "Password is invalid", Toast.LENGTH_SHORT).show()
            }
        }
        val maxLength = 8
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        password.filters = filterArray
    }
}