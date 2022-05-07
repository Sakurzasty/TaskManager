package pl.edu.pja.taskmanager.activities

import android.app.Activity
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.databinding.ActivitySummaryTaskBinding
import kotlin.concurrent.thread

class TaskView : AppCompatActivity() {
    private val database by lazy {(application as App).database}
    private val binding by lazy {ActivitySummaryTaskBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupSaveButton()
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