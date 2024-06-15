package tech.reliab.todoapp.presentation.completedTasks

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import im.dacer.androidcharts.PieHelper
import tech.reliab.todoapp.databinding.FragmentCompletedTasksBinding
import tech.reliab.todoapp.domain.model.CategoryEnum
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.model.getCategoryEnumByText
import tech.reliab.todoapp.presentation.TaskAdapter

class CompletedTasksFragment : Fragment() {

    private lateinit var binding: FragmentCompletedTasksBinding
    private val taskAdapter: TaskAdapter = TaskAdapter {
        viewModel.updateTaskStatus(it)
    }
    private val viewModel: CompletedTasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedTasksBinding.inflate(inflater)
        binding.rvTasks.setHasFixedSize(false)
        binding.rvTasks.itemAnimator = null
        binding.rvTasks.adapter = taskAdapter
        viewModel.getCompletedTask()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTask()
    }

    private fun updateTask() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            val tasks = it.map { task -> task.taskInfo }
            taskAdapter.refresh(tasks)
            setupPieChart(
                calculateCategoryWeights(tasks)
            )
        }
    }

    private fun setupPieChart(items: Map<CategoryEnum, Float>) {
        val spannable = SpannableStringBuilder()
        binding.pieChart.showPercentLabel(true)

        val data = items.map {
            val start = spannable.length
            val color = it.key.color
            spannable.append(it.key.text)
            val end = spannable.length
            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.append(", ")
            PieHelper(
                it.value,
                color
            )
        }
        binding.pieChart.setDate(ArrayList(data))
        binding.categoryList.text = spannable
    }

    private fun calculateCategoryWeights(tasks: List<TaskInfoView>): Map<CategoryEnum, Float> {
        val totalItems = tasks.size
        val categoryCounts = tasks.groupingBy { getCategoryEnumByText(it.category) }.eachCount()

        return categoryCounts.mapValues { (_, count) ->
            count.toFloat() / totalItems * 100
        }
    }

}