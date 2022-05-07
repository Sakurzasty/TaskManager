package pl.edu.pja.taskmanager.activities

import android.Manifest.permission.SEND_SMS
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.databinding.ActivitySummaryTaskBinding
import kotlin.concurrent.thread

class TaskSummaryActivity : AppCompatActivity() {
    private val database by lazy {(application as App).database}
    private val binding by lazy {ActivitySummaryTaskBinding.inflate(layoutInflater)}
    private val PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupSaveButton()
        setupMessageButton()
        setupSeekBar()
        binding.name.setText(intent.getStringExtra("name"))
        binding.priority.setText(intent.getStringExtra("priority"))
        binding.deadline.setText(intent.getStringExtra("deadline"))
        binding.seekBar.progress = intent.getIntExtra("progression", 0)
        binding.progressBar.progress = intent.getIntExtra("progression", 0)
        binding.textViewProgress.text = intent.getIntExtra("progression", 0).toString() + "%"
        binding.time.setText(intent.getStringExtra("time"))
    }

    private fun setupSaveButton(){
        binding.buttonSave.setOnClickListener{
            thread {
                database.records.updateProgress(intent.getLongExtra("id", -1), binding.seekBar.progress)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setupMessageButton(){
        binding.buttonMessage.setOnClickListener{
            val permissionCheck = ContextCompat.checkSelfPermission(this, SEND_SMS)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                myMessage()
            }else{
                ActivityCompat.requestPermissions(
                    this, arrayOf(SEND_SMS), PERMISSION_REQUEST
                )
            }
        }
    }

    private fun myMessage(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Wprowadź numer telefonu")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_PHONE
        builder.setView(input)
        builder.setPositiveButton("Wyślij") { _: DialogInterface, _: Int -> sendSMS(input.text.toString()) }
        builder.setNegativeButton("Anuluj") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun sendSMS(number : String){
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, "Raport na temat postępu zadania: " + intent.getStringExtra("name") + "\nPostęp zadania: " + intent.getIntExtra("progression", 0).toString() + "%", null, null)
        Toast.makeText(applicationContext, "Wiadomość została wysłana", Toast.LENGTH_SHORT).show()
    }


    private fun setupSeekBar(){
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.progressBar.progress = binding.seekBar.progress
                binding.textViewProgress.text = binding.seekBar.progress.toString() + "%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

}