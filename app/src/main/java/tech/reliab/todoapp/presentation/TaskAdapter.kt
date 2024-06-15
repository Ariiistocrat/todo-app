package tech.reliab.todoapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tech.reliab.todoapp.R
import tech.reliab.todoapp.databinding.ItemTaskBinding
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.model.getCategoryEnumByText
import tech.reliab.todoapp.util.DateToString

class TaskAdapter(private val setCompleted: (TaskInfoView) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<TaskInfoView> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(
            parent,
            setCompleted
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val photo = tasks[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val setCompleted: (TaskInfoView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskInfoView) {
            binding.description.text = task.description
            binding.category.text = task.category
            binding.category.setTextColor(
                getCategoryEnumByText(task.category).color
            )
            binding.priority.text = task.priority.toString()
            binding.priority.isVisible = task.priority != 0
            binding.isCompleted.isChecked = task.status
            binding.date.text = DateToString.convertDateToString(task.date)
            binding.isCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (task.status == isChecked) return@setOnCheckedChangeListener
                val _task = task.copy(
                    status = isChecked
                )
                setCompleted.invoke(_task)
            }
            binding.root.setOnClickListener {
                it.findNavController().navigate(R.id.createTaskFragment, bundleOf("item" to task))
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                setCompleted: (TaskInfoView) -> Unit
            ): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(binding, setCompleted)
            }
        }
    }

    fun refresh(_tasks: List<TaskInfoView>) {
        val productDiffUtilCallback = TaskInfoDiffCallback(tasks, _tasks)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        tasks = _tasks
        productDiffResult.dispatchUpdatesTo(this)
    }

    class TaskInfoDiffCallback(
        private var oldList: List<TaskInfoView> = mutableListOf(),
        private var newList: List<TaskInfoView> = mutableListOf()
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}