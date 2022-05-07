package pl.edu.pja.taskmanager.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.databinding.ActivityAddTaskBinding
import pl.edu.pja.taskmanager.model.dto.TaskDTO
import kotlin.concurrent.thread

class TaskActivity : AppCompatActivity() {
    private val database by lazy {(application as App).database}
    private val binding by lazy {ActivityAddTaskBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupSaveButton()
    }

    private fun setupSaveButton(){
        binding.buttonSave.setOnClickListener{
            if (check()){
                Toast.makeText(applicationContext, "Należy uzupełnić wszystkie pola!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = TaskDTO(
                0,
                binding.name.text.toString(),
                binding.priority.selectedItem.toString(),
                Integer.parseInt(binding.progress.text.toString()),
                binding.deadline.text.toString(),
                "nowe"
            )
            thread {
                database.records.add(task)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun check(): Boolean {
        if (binding.name.text.toString().trim().length === 0){
            return true
        }
        if (binding.deadline.text.toString().trim().length === 0){
            return true
        }
        if (binding.progress.text.toString().trim().length === 0){
            return true
        }
        if (binding.time.text.toString().trim().length === 0){
            return true
        }

        return false
    }

}