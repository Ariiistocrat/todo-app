package tech.reliab.todoapp.presentation.home

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import com.skydoves.balloon.showAlignBottom
import im.dacer.androidcharts.PieHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.reliab.todoapp.R
import tech.reliab.todoapp.databinding.FragmentHomeBinding
import tech.reliab.todoapp.domain.model.CategoryEnum
import tech.reliab.todoapp.domain.model.SortEntityEnum
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.domain.model.getCategoryEnumByText
import tech.reliab.todoapp.presentation.TaskAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val taskAdapter: TaskAdapter = TaskAdapter {
        viewModel.updateTaskStatus(it)
    }
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.rvTasks.adapter = taskAdapter
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createTaskFragment)
        }
        createSortBalloon()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTask()
    }

    private fun createSortBalloon() {
        val balloon = Balloon.Builder(requireContext())
            .setLayout(R.layout.sort_dialog_fagment)
            .setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.md_theme_dark_onPrimaryContainer_highContrast
                )
            )
            .setMarginTop(10)
            .setMarginHorizontal(5)
            .setIsVisibleArrow(false)
            .setOverlayShape(BalloonOverlayRoundRect(0f, 0f))
            .setDismissWhenClicked(true)
            .build()
        val contentView = balloon.getContentView()
        val radioPriority = contentView.findViewById<RadioButton>(R.id.radio_priority)
        val radioDatePlus = contentView.findViewById<RadioButton>(R.id.radio_date_plus)
        val radioDateMinus = contentView.findViewById<RadioButton>(R.id.radio_date_minus)
        contentView.findViewById<RadioGroup>(R.id.radio_group)
        val radioGroup = contentView.findViewById<RadioGroup>(R.id.radio_group)
        radioGroup.setOnCheckedChangeListener { _, radioid ->
            val newSort = when (radioid) {
                radioDatePlus.id -> SortEntityEnum.DatePlus
                radioDateMinus.id -> SortEntityEnum.DateMinus
                radioPriority.id -> SortEntityEnum.Priority
                else -> SortEntityEnum.DatePlus
            }
            if (newSort == viewModel.choiceSortType) return@setOnCheckedChangeListener
            viewModel.choiceSortType = newSort
            updateTask()
            balloon.dismiss()
        }
        binding.buttonSort.setOnClickListener {
            when (viewModel.choiceSortType) {
                SortEntityEnum.DatePlus -> radioDatePlus.isChecked = true
                SortEntityEnum.DateMinus -> radioDateMinus.isChecked = true
                SortEntityEnum.Priority -> radioPriority.isChecked = true
            }
            balloon.let {
                binding.buttonSort.showAlignBottom(it, yOff = -10)
            }
        }
    }

    private fun updateTask() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getUncompletedTask().collect {
                val tasks = it.map { task -> task.taskInfo }
                withContext(Dispatchers.Main) {
                    taskAdapter.refresh(tasks)
                    setupPieChart(
                        calculateCategoryWeights(tasks)
                    )
                }
            }
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