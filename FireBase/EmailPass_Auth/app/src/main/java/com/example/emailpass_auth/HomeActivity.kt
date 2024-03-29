package com.example.emailpass_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class HomeActivity : AppCompatActivity() {

    private lateinit var logoutbtn:Button
    private lateinit var welcome:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var createNotesBtn:Button
    private lateinit var openNotesBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        welcome = findViewById(R.id.welcomePrompt)
        var name = auth.currentUser?.email.toString()
        welcome.text = "Welcome\n${name}"
        logoutbtn = findViewById(R.id.logOutBtn)
        createNotesBtn = findViewById(R.id.createNotesbutton)
        openNotesBtn = findViewById(R.id.openNotesbutton)

        createNotesBtn.setOnClickListener {
            startActivity(Intent(this,CreateNotesActivity::class.java))
        }

        openNotesBtn.setOnClickListener {
            startActivity(Intent(this,OpenNotesActivity::class.java))
        }

        logoutbtn.setOnClickListener {
            auth.signOut()
            var intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}