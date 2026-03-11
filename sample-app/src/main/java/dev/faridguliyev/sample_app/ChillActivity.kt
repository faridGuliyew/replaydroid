package dev.faridguliyev.sample_app

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this).apply {
            text = "I AM JUST A CHILL ACTIVITY!"
            textSize = 26F
            setPadding(100)
        }

        addContentView(
            tv, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        lifecycle.coroutineScope.launch {
            var count = 0
            while (count < 10) {
                delay(1000)
                count++
                tv.text = "Counter: $count"
            }
            diagnostics.sendDiagnostics()
            tv.text = "Diagnostics sent!"
            finish()
        }
    }
}