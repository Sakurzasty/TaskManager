package pl.edu.pja.taskmanager.adapter

import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.taskmanager.database.AppDatabase
import pl.edu.pja.taskmanager.databinding.ItemBinding
import pl.edu.pja.taskmanager.model.Task
import java.time.LocalDate
import kotlin.concurrent.thread

class TaskAdapter(private val database: AppDatabase) : RecyclerView.Adapter<TaskViewHolder>() {
    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var tasks = mutableListOf<Task>()
    private var filteredData = mutableListOf<Task>()
    private lateinit var mClickListener: onClickListener
    private lateinit var mClickLongListener: onClickLongListener

    interface onClickListener{
        fun onItemClick(position : Int)
    }
    interface onClickLongListener{
        fun onItemLongClick(position: Int)
    }

    fun setOnItemClickListener(listener: onClickListener){
        mClickListener = listener
    }

    fun setOnItemLongClickListner(listener: onClickLongListener){
        mClickLongListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
       val binding = ItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return TaskViewHolder(binding, mClickListener, mClickLongListener)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    fun remove(position: Int){
        if (position !in tasks.indices) return
        val task = tasks.removeAt(position)
        thread { database.records.update(task.id) }
        notifyItemRemoved(position)
    }

    fun getTask(position: Int): Task {
        return tasks[position]
    }

    fun reload() = thread{
        val currentDateTime = LocalDate.now().toString()
        val data = database.records.getAll().map { it.toTaskModel() }.sortedBy { it.deadline }
        filteredData = data.filter { it.deadline >= currentDateTime && it.status == "nowe" }.toMutableList()
        notifyChanges(filteredData)
    }

    private fun notifyChanges(newData: List<Task>){
        val callback = TaskDiffCallback(tasks, newData)
        val diffs = DiffUtil.calculateDiff(callback)
        tasks = newData.toMutableList()
        handler.post {
            diffs.dispatchUpdatesTo(this)
        }
    }
}