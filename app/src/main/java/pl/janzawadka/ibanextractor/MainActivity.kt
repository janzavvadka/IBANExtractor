package pl.janzawadka.ibanextractor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val extractButton = findViewById<Button>(R.id.extract_button)
        extractButton.setOnClickListener {
            val extractorEditText = findViewById<EditText>(R.id.extractor_edit_text)
            val extractedData = extractorEditText.text
            IBANExtractor(this).extract(extractedData.toString())
        }
    }


}
