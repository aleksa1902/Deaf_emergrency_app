package com.example.emergrency_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.util.Base64
import android.view.View.OnFocusChangeListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var jmbgEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var selectImageButton: Button
    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        dobEditText = findViewById(R.id.dobEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        jmbgEditText = findViewById(R.id.jmbgEditText)
        registerButton = findViewById(R.id.registerButton)
        selectImageButton = findViewById(R.id.selectImageButton)
        profileImageView = findViewById(R.id.profileImageView)

        dobEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                        dobEditText.setText(formattedDate)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }
        }

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val jmbg = jmbgEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(firstName, lastName, dob, email, jmbg, password)) {
                registerUser(firstName, lastName, dob, email, jmbg, password)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                selectedImageUri = data.data
                profileImageView.setImageURI(selectedImageUri)
            }
        }
    }

    private fun validateInput(firstName: String, lastName: String, dob: String, email: String, jmbg: String, password: String): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || email.isEmpty() || jmbg.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Sva polja su obavezna.", Toast.LENGTH_SHORT).show()
            return false
        }

        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        if (!emailPattern.matcher(email).matches()) {
            Toast.makeText(this, "Unesite validnu email adresu.", Toast.LENGTH_SHORT).show()
            return false
        }

        val passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
        if (!passwordPattern.matcher(password).matches()) {
            Toast.makeText(
                this,
                "Šifra treba da sadrži barem jedno veliko slovo, jedan broj i barem jedan karakter.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (jmbg.length != 13 || !jmbg.all { it.isDigit() }) {
            Toast.makeText(this, "JMBG treba da ima tačno 13 cifara.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun registerUser(firstName: String, lastName: String, dob: String, email: String, jmbg: String, password: String) {
        val url = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/register"
        val imageBase64 = selectedImageUri?.let { encodeImageToBase64(it) } ?: ""
        val params = JSONObject()
        params.put("name", firstName)
        params.put("surname", lastName)
        params.put("dateOfBirth", dob)
        params.put("email", email)
        params.put("password", password)
        params.put("JMBG", jmbg)
        params.put("image", imageBase64)  // Base64 encode your image and provide it here

        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { // Registration successful
                setResult(RESULT_OK)
                finish()
                Toast.makeText(this@RegistrationActivity, "Uspešna registracija, sada se prijavite.", Toast.LENGTH_SHORT).show()
            }
        ) { // Registration failed
            Toast.makeText(
                this@RegistrationActivity,
                "Greška prilikom registracije. Pokušajte ponovo.",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(request)
    }

    private fun encodeImageToBase64(uri: Uri): String {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bytes = readBytes(inputStream)
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @Throws(IOException::class)
    private fun readBytes(inputStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream?.read(buffer).also { len = it!! } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}