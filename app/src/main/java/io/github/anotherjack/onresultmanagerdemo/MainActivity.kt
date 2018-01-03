package io.github.anotherjack.onresultmanagerdemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.github.anotherjack.onresultmanager.OnResultManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private val REQUEST_CODE = 42
    private val onResultManager by lazy { OnResultManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        go.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            onResultManager.startForResult(intent,REQUEST_CODE,{requestCode: Int, resultCode: Int, data: Intent? ->
                if (resultCode == Activity.RESULT_OK){
                    val text = data?.getStringExtra("text")
                    Toast.makeText(this,"result -> "+text,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        onResultManager.trigger(requestCode,resultCode,data)
    }
}
