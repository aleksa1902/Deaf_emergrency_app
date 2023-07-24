package com.example.emergrency_app.firefighters

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.firefighters.form.*
import com.example.emergrency_app.police.form.*

class FirefightersActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters)
        val pozarButton: ImageView = findViewById(R.id.pozarButton)
        pozarButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, CarAccidentActivity::class.java)
            startActivity(intent)
        }

        val spasavanjeIzOpasnostiButton: ImageButton = findViewById(R.id.spasavanjeIzOpasnostiButton)
        spasavanjeIzOpasnostiButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, RescureDangerActivity::class.java)
            startActivity(intent)
        }

        val tehnickaIntervencijaButton: ImageButton = findViewById(R.id.tehnickaIntervencijaButton)
        tehnickaIntervencijaButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, InterventionsActivity::class.java)
            startActivity(intent)
        }

        val izlivanjeOpasnihStvariButton: ImageButton = findViewById(R.id.izlivanjeOpasnihStvariButton)
        izlivanjeOpasnihStvariButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, DangerousSubstancesActivity::class.java)
            startActivity(intent)
        }

        val poplaveButton: ImageButton = findViewById(R.id.poplaveButton)
        poplaveButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, FloodsActivity::class.java)
            startActivity(intent)
        }

        val plinButton: ImageButton = findViewById(R.id.plinButton)
        plinButton.setOnClickListener {
            val intent = Intent(this@FirefightersActivity, GasActivity::class.java)
            startActivity(intent)
        }
    }
}
