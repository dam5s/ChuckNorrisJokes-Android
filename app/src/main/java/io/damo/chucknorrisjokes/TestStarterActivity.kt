package io.damo.chucknorrisjokes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.damo.chucknorrisjokes.extensions.bindView

class TestStarterActivity : AppCompatActivity() {

    val mainActivityButton: Button by bindView(R.id.main_activity_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_starter)

        mainActivityButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
