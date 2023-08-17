package com.example.applypixels

import android.text.InputFilter
import android.text.Spanned

class CharacterInputFilter(private val allowedCharacters: String): InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val filteredStringBuilder = StringBuilder()
        for (i in start until end) {
            val character = source?.get(i).toString()
            if (allowedCharacters.contains(character)) {
                filteredStringBuilder.append(character)
            }
        }
        return filteredStringBuilder.toString()
    }

}