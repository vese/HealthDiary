package com.example.healthdiary.ui.recommendations

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthdiary.R
import com.example.healthdiary.model.recommendations.Recommendation
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RecommendationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recommendation)
        val bundleExtra = intent.getBundleExtra("bundle")
        val recommendation = bundleExtra?.get("recommendation") as Recommendation?

        val title = findViewById<TextView>(R.id.recommendation_title_form)
        val text = findViewById<TextView>(R.id.recommendation_text_form)
        text.movementMethod = ScrollingMovementMethod()
        title.text = recommendation?.title
        text.text = recommendation?.text

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val dark: ImageButton = findViewById(R.id.dark)
        val helpImage: ImageView = findViewById(R.id.help_image)
        fab.setOnClickListener {
            val visibility = if (dark.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
            dark.visibility = visibility
            helpImage.visibility = visibility
        }

        dark.setOnClickListener {
            dark.visibility = View.GONE
            helpImage.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}