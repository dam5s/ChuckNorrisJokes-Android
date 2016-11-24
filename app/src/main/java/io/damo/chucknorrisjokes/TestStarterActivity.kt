package io.damo.chucknorrisjokes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.test_starter.*

class TestStarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_starter)

        mainActivityButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
