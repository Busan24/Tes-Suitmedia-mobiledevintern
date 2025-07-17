package com.example.palindromecheckerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SecondScreen : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var chooseUserButton: Button
    private lateinit var userNameTextView: TextView
    private lateinit var selectedUserTextView: TextView

    companion object {
        const val EXTRA_USER_NAME = "USER_NAME"
        const val EXTRA_SELECTED_USER = "SELECTED_USER"
    }

    private val userResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedUser = result.data?.getStringExtra("selected_user")
            selectedUser?.let {
                selectedUserTextView.text = it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)

        backButton = findViewById(R.id.btn_back)
        chooseUserButton = findViewById(R.id.choose_user)
        userNameTextView = findViewById(R.id.nama)
        selectedUserTextView = findViewById(R.id.selected_user)

        val userName = intent.getStringExtra(EXTRA_USER_NAME)
        userNameTextView.text = userName ?: "No Name"

        backButton.setOnClickListener {
            finish()
        }

        chooseUserButton.setOnClickListener {
            val intent = Intent(this, ThirdScreen::class.java)
            intent.putExtra(EXTRA_USER_NAME, userName)
            userResultLauncher.launch(intent)
        }


    }
}
