package dk.easj.anbo.intentsexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.easj.anbo.intentsexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.buttonToAnother.setOnClickListener {
            val intent = Intent(this, AnotherActivity::class.java)
            // explicit intent: Says explicitly where you want to go
            startActivity(intent)
        }

        binding.buttonToActivityWithParameter.setOnClickListener {
            val name: String = binding.editTextName.text.toString().trim()
            val ageStr: String = binding.editTextAge.text.trim().toString()
            val age = ageStr.toInt()
            val intent = Intent(this, ParametersActivity::class.java)
            intent.putExtra("NAME", name)
            intent.putExtra("AGE", age)
            startActivity(intent)
        }

    }
}