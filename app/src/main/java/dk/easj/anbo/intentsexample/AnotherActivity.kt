package dk.easj.anbo.intentsexample

import android.annotation.SuppressLint
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
import com.google.android.material.snackbar.Snackbar
import dk.easj.anbo.intentsexample.databinding.ActivityAnotherBinding

class AnotherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnotherBinding
    // private val pickContactsSubActivity = 2 // for deprecated startActivityForResult

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_another)
        binding = ActivityAnotherBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener {
            finish()
            // return to previous Activity
        }

        binding.browseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://anbo-easj.dk/"))
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
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "johndoe@example.com", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi! I am sending you a test email.")
            // File attachment
            //emailIntent.putExtra(Intent.EXTRA_STREAM, attachedFileUri);
            // Check if the device has an email client
            if (emailIntent.resolveActivity(packageManager) != null) {
                // Prompt the user to select a mail app
                startActivity(
                    Intent.createChooser(
                        emailIntent,
                        "Choose your mail application"
                    )
                )
            } else {
                // Inform the user that no email clients are installed or provide an alternative
                // binding.messageView.text = "No email app on device"
                Snackbar.make(it, "No email app on device", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.genericButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            // Nothing else specified!
            startActivity(intent)
        }

        // https://stackoverflow.com/questions/66301361/android-choosing-the-right-settings-to-prepare-activityresultlauncher-for-picki
        // https://proandroiddev.com/is-onactivityresult-deprecated-in-activity-results-api-lets-deep-dive-into-it-302d5cf6edd
        val pickContactLauncher: ActivityResultLauncher<Void> =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri: Uri ->
                val c: Cursor? = contentResolver.query(uri, null, null, null, null)
                if (c != null) {
                    if (c.moveToFirst()) {
                        val name =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
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

        /* deprecate way of making starting a sub-activity which is expected to return data
        binding.contactsButton.setOnClickListener {
         val intent = Intent(Intent.ACTION_PICK)
         intent.type = ContactsContract.Contacts.CONTENT_TYPE
         startActivityForResult(intent, pickContactsSubActivity)
         // TODO https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
         // TODO https://developer.android.com/training/basics/intents/result
     }*/

    }

    /* Deprecated way of getting data from a sub-activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            binding.choiceTextviewResult.text = "Cancelled. No data."
            return
        }
        if (requestCode == pickContactsSubActivity) {
            val contactData = data?.data
            val cursor = contentResolver.query(contactData!!, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val columnNameIndex =
                    cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                val name = cursor.getString(columnNameIndex)
                binding.choiceTextviewResult.text = name
            }
            cursor.close()
        }
    }*/
}