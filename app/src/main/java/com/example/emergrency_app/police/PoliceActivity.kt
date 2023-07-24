package com.example.emergrency_app.police
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.police.form.HitniSlucajeviActivity
import com.example.emergrency_app.police.form.TheftActivity

class PoliceActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police)
        val sudarButton: ImageView = findViewById(R.id.sudarButton)
        sudarButton.setOnClickListener {
            // TODO: Logika za poziv policije u slučaju sudara
        }

        val kradjaProvalaButton: ImageButton = findViewById(R.id.kradjaProvalaButton)
        kradjaProvalaButton.setOnClickListener {
            val intent = Intent(this@PoliceActivity, TheftActivity::class.java)
            startActivity(intent)
        }

        val nasiljeButton: ImageButton = findViewById(R.id.nasiljeButton)
        nasiljeButton.setOnClickListener {
            // TODO: Logika za poziv policije u slučaju nasilja
        }

        val sumnjivaAktivnostButton: ImageButton = findViewById(R.id.sumnjivaAktivnostButton)
        sumnjivaAktivnostButton.setOnClickListener {
            // TODO: Logika za poziv policije u slučaju sumnjive ili nezakonite aktivnosti
        }

        val prijetnjeButton: ImageButton = findViewById(R.id.prijetnjeButton)
        prijetnjeButton.setOnClickListener {
            // TODO: Logika za poziv policije u slučaju prijetnji ili terorističkih aktivnosti
        }

        val hitniSlucajButton: ImageButton = findViewById(R.id.hitniSlucajButton)
        hitniSlucajButton.setOnClickListener {
            val intent = Intent(this@PoliceActivity, HitniSlucajeviActivity::class.java)
            startActivity(intent)
        }
    }
}
