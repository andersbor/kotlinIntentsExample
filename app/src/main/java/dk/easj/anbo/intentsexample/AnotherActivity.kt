package dk.easj.anbo.intentsexample

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import dk.easj.anbo.intentsexample.databinding.ActivityAnotherBinding

class AnotherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnotherBinding

    //@SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_another)
        binding = ActivityAnotherBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener {
            finish()             // return to previous Activity
        }

        binding.browseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://anbo-easj.dk/"))
            // implicit intent: Not saying explicitly which activity you want to go to
            //val chooser = Intent.createChooser(intent, "Browse with")
            startActivity(intent)
        }

        binding.phoneButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+45123456"))
            startActivity(intent)
        }

        binding.mapButton.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:55.6305952,12.0784041"))
                startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(this, "Cannot show map", Toast.LENGTH_LONG).show()
            }
        }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }

        binding.emailButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi! I am sending you a test email.")
            startActivity(emailIntent)
        }

        binding.genericButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            // Nothing else specified!
            startActivity(intent)
        }

        // https://stackoverflow.com/questions/66301361/android-choosing-the-right-settings-to-prepare-activityresultlauncher-for-picki
        // https://proandroiddev.com/is-onactivityresult-deprecated-in-activity-results-api-lets-deep-dive-into-it-302d5cf6edd
        val pickContactLauncher: ActivityResultLauncher<Void?> =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri: Uri? ->
                val c: Cursor? = contentResolver.query(uri!!, null, null, null, null)
                if (c != null) {
                    if (c.moveToFirst()) {
                        val columnIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val name = c.getString(columnIndex)
                        Log.d("APPLE", "onActivityResult: $name")
                        binding.messageView.text = name
                        c.close()
                    }
                } else {
                    binding.messageView.text = "No data"
                }
            }

        binding.contactsButton.setOnClickListener {
            pickContactLauncher.launch(null)
        }
    }
}