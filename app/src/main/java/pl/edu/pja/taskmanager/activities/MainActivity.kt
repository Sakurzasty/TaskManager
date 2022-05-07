package pl.edu.pja.taskmanager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.R
import pl.edu.pja.taskmanager.adapter.TaskAdapter
import pl.edu.pja.taskmanager.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.*
import kotlin.concurrent.thread

private const val REQUEST_NEW_TASK = 1
private const val REQUEST_VIEW_TASK = 2

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val database by lazy {(application as App).database}
    private val taskAdapter by lazy { TaskAdapter(database) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTaskList()
        setupAddButton()
        setupWeek()
        setupCurrentCountTask()
    }

    private fun setupTaskList(){
        binding.recycleList.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            taskAdapter.setOnItemClickListener(object: TaskAdapter.onClickListener{
                override fun onItemClick(position: Int) {
                    val task = taskAdapter.getTask(position)
                    val intent = Intent(context, TaskView::class.java)
                    intent.putExtra("id", task.id)
                    intent.putExtra("name", task.name)
                    intent.putExtra("priority", task.priority)
                    intent.putExtra("progression", task.progression)
                    intent.putExtra("deadline", task.deadline)
                    intent.putExtra("status", task.status)
                    startActivityForResult(
                        intent, REQUEST_VIEW_TASK
                    )
                }
            })

            taskAdapter.setOnItemLongClickListner(object: TaskAdapter.onClickLongListener{
                override fun onItemLongClick(position: Int) {
                    Log.i("TAG", "test")
                }
            })

            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
        }
        taskAdapter.reload()
    }

    private fun setupAddButton(){
        binding.addButton.setOnClickListener{
            startActivityForResult(
                Intent(this, TaskActivity::class.java), REQUEST_NEW_TASK
            )
        }
    }

    private fun setupWeek(){
        binding.week.text = getActualWeek().toString()
    }

    private fun setupCurrentCountTask(){
        val firstOfWeek = LocalDate.now().toString()
        val lastOfWeek = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK , 7).toLocalDate().toString()
        thread {
            binding.countTask.text =
                database.records.getAllFromRange(firstOfWeek, lastOfWeek).size.toString()
        }
    }

    private fun getActualWeek() : Int{
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_NEW_TASK && resultCode == Activity.RESULT_OK) {
            taskAdapter.reload()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}