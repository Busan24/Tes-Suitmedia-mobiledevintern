package com.example.palindromecheckerapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FirstScreen : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var palindromeEditText: EditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        nameEditText = findViewById(R.id.name)
        palindromeEditText = findViewById(R.id.palindrome)
        checkButton = findViewById(R.id.check)
        nextButton = findViewById(R.id.next)

        checkButton.setOnClickListener {
            val input = palindromeEditText.text.toString().trim()

            if (input.isEmpty()) {
                showDialog("Palindrome input cannot be empty.")
            } else {
                val result = isPalindrome(input)
                val message = if (result) "isPalindrome" else "not palindrome"
                showDialog(message)
            }
        }

        nextButton.setOnClickListener {
            val name = nameEditText.text.toString()

            if (name.isNotBlank()) {
                val intent = Intent(this, SecondScreen::class.java)
                intent.putExtra("USER_NAME", name)
                startActivity(intent)
            } else {
                showDialog("Name is required before continuing.")
            }
        }

        palindromeEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                palindromeEditText.clearFocus()
                true
            } else {
                false
            }
        }

        nameEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                nameEditText.clearFocus()
                true
            } else {
                false
            }
        }

    }

    private fun isPalindrome(input: String): Boolean {
        val cleaned = input.replace("\\s".toRegex(), "").lowercase()
        return cleaned == cleaned.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
