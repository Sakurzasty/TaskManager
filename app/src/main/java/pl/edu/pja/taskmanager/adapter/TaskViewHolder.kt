package pl.edu.pja.taskmanager.adapter

import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.taskmanager.databinding.ItemBinding
import pl.edu.pja.taskmanager.model.Task

class TaskViewHolder(private val layoutBinding: ItemBinding, clickListener: TaskAdapter.onClickListener, clickLongListener: TaskAdapter.onClickLongListener) : RecyclerView.ViewHolder(layoutBinding.root){

    init {
        itemView.setOnClickListener {
            clickListener.onItemClick(adapterPosition)
        }
        itemView.setOnLongClickListener{
            clickLongListener.onItemLongClick(adapterPosition)
            true
        }
    }

    fun bind(task: Task) = with(layoutBinding) {
        name.text = task.name
        priority.text = task.priority
        progressBar.progress = task.progression
        deadline.text = task.deadline
    }
}

