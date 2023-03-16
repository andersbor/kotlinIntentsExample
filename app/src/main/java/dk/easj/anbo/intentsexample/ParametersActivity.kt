package dk.easj.anbo.intentsexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dk.easj.anbo.intentsexample.databinding.ActivityParametersBinding

class ParametersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParametersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_parameters)
        binding = ActivityParametersBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val name: String? = intent.getStringExtra("NAME")
        val age: Int = intent.getIntExtra("AGE", -1)
        // intent parameters between activities are unsafe, unlike safeArgs between fragments
        binding.textViewName.text = "Welcome $name $age"

        binding.backButton.setOnClickListener {
            finish()
            // return to previous Activity
        }
    }
}