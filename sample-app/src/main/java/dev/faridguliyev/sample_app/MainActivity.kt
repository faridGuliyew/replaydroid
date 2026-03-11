package dev.faridguliyev.sample_app

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val someView = Button(this@MainActivity).apply { text = "CLICK ME ASAP!"
        setOnClickListener {
            startActivity(Intent(this@MainActivity, ChillActivity::class.java))
        }}
        (window.decorView.rootView as ViewGroup).addView(someView)

//        val image = ImageView(this)
//
//        someView.post {
//            (window.decorView.rootView as ViewGroup).removeView(someView)
//            val bitmap = createBitmap(someView.width, someView.height, Bitmap.Config.RGB_565)
//            someView.draw(Canvas(bitmap))
//            image.setImageBitmap(bitmap)
//        }

//        (window.decorView.rootView as ViewGroup).addView(
//            image, ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        )
//        setContentView(LinearLayout(this).apply {
//            addView(
//                Button(this@MainActivity).apply {
//                    text = "CLICK ME ASAP!"
//                },
//                LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//            )
//        })
    }

}