package io.github.anotherjack.onresultmanagerdemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        back.setOnClickListener {
            val intent = Intent()
            intent.putExtra("text",editText.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}
