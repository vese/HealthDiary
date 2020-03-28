package com.example.healthdiary.ui.medicaments

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthdiary.R
import com.example.healthdiary.model.medicaments.Medicament


class MedicamentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_medicament)
        val bundleExtra = intent.getBundleExtra("bundle")
        val medicament = bundleExtra?.get("medicament") as Medicament?

        val name = findViewById<TextView>(R.id.medicament_name)
        val dosage = findViewById<TextView>(R.id.medicament_dosage)
        val manufacturer = findViewById<TextView>(R.id.medicament_manufacturer)
        val price = findViewById<TextView>(R.id.medicament_price)

        name.text = medicament?.name
        dosage.text = medicament?.dosage
        manufacturer.text = medicament?.manufacturer
        price.text = medicament?.price
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}