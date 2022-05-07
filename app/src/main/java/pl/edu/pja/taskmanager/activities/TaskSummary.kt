package pl.edu.pja.taskmanager.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.databinding.ActivitySummaryTaskBinding
import pl.edu.pja.taskmanager.model.dto.TaskDTO
import kotlin.concurrent.thread

class TaskView : AppCompatActivity() {
    private val database by lazy {(application as App).database}
    private val binding by lazy {ActivitySummaryTaskBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupSaveButton()
        binding.name.setText(intent.getStringExtra("name"))
        binding.priority.setText(intent.getStringExtra("priority"))
        binding.progress.setText(intent.getStringExtra("progression"))
        binding.deadline.setText(intent.getStringExtra("deadline"))
        binding.time.setText(intent.getStringExtra("time"))
    }

    private fun setupSaveButton(){
        binding.buttonSave.setOnClickListener{
            if (check()){
                Toast.makeText(applicationContext, "Postęp nie może być pusty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            thread {
                database.records.updateProgress(intent.getLongExtra("id", -1), Integer.parseInt(binding.progress.text.toString()))
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun check(): Boolean {
        if (binding.progress.text.toString().trim().length === 0){
            return true
        }

        return false
    }

}