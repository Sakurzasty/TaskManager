package pl.edu.pja.taskmanager.adapter

import androidx.recyclerview.widget.DiffUtil
import pl.edu.pja.taskmanager.model.Task

class TaskDiffCallback(private val before: List<Task>, private val after: List<Task>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = before.size
    override fun getNewListSize(): Int = after.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition].id === after[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition].name == after[newItemPosition].name &&
                before[oldItemPosition].deadline == after[newItemPosition].deadline &&
                before[oldItemPosition].priority == after[newItemPosition].priority &&
                before[oldItemPosition].progression == after[newItemPosition].progression &&
                before[oldItemPosition].status == after[newItemPosition].status
}