package com.example.healthdiary.ui.recommendations

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthdiary.R
import com.example.healthdiary.model.recommendations.Recommendation


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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}