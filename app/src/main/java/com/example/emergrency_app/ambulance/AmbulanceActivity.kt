package com.example.emergrency_app.ambulance
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.ambulance.form.InjuriesActivity
import com.example.emergrency_app.ambulance.form.MedicalEmergencyActivity
import com.example.emergrency_app.ambulance.form.PoisoningActivity
import com.example.emergrency_app.ambulance.form.SuddenDeteriorationActivity

class AmbulanceActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance)
        val hitniSlucajevi: ImageView = findViewById(R.id.hitniSlucajevi)
        hitniSlucajevi.setOnClickListener {
            val intent = Intent(this@AmbulanceActivity, MedicalEmergencyActivity::class.java)
            startActivity(intent)
        }

        val povrede: ImageButton = findViewById(R.id.povrede)
        povrede.setOnClickListener {
            val intent = Intent(this@AmbulanceActivity, InjuriesActivity::class.java)
            startActivity(intent)
        }

        val pogorsanoStanje: ImageButton = findViewById(R.id.pogorsanoStanje)
        pogorsanoStanje.setOnClickListener {
            val intent = Intent(this@AmbulanceActivity, SuddenDeteriorationActivity::class.java)
            startActivity(intent)
        }

        val trovanje: ImageButton = findViewById(R.id.trovanje)
        trovanje.setOnClickListener {
            val intent = Intent(this@AmbulanceActivity, PoisoningActivity::class.java)
            startActivity(intent)
        }

        val mentalnaKriza: ImageButton = findViewById(R.id.mentalnaKriza)
        mentalnaKriza.setOnClickListener {

        }

        val porodTrudovi: ImageButton = findViewById(R.id.porodTrudovi)
        porodTrudovi.setOnClickListener {

        }
    }
}
