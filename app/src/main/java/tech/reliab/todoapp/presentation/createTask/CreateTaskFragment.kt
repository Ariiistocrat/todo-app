package tech.reliab.todoapp.presentation.createTask

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tech.reliab.todoapp.R
import tech.reliab.todoapp.databinding.FragmentCreateTaskBinding
import tech.reliab.todoapp.domain.model.CategoryEnum
import tech.reliab.todoapp.domain.model.TaskInfoView
import tech.reliab.todoapp.util.DateToString
import tech.reliab.todoapp.util.getSerializableIfContains
import java.util.Calendar


class CreateTaskFragment : Fragment() {

    private lateinit var binding: FragmentCreateTaskBinding
    private val viewModel: CreateTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)
        val item = arguments?.getSerializableIfContains<TaskInfoView>("item")
        item?.let {
            setOldData(it)
        }
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                CategoryEnum.entries.map { it.text }
            )
        binding.filledExposedDropdown.setAdapter(adapter)
        binding.filledExposedDropdown.addTextChangedListener {
            viewModel.category = it.toString()
        }
        binding.buttonCreate.setOnClickListener {
            if (binding.description.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.warning_create_task), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val description = binding.description.text.toString()
            val priority = binding.priority.text.toString().toIntOrNull() ?: 0
            viewModel.createTask(description, priority)
            findNavController().popBackStack()
        }
        initDatePicker()
        return binding.root
    }

    private fun setOldData(item: TaskInfoView) {
        viewModel.inUpdateMode = true
        viewModel.date = item.date
        viewModel.category = item.category
        viewModel.id = item.id
        binding.buttonCreate.text = getString(R.string.update)
        binding.description.setText(item.description)
        binding.priority.text = item.priority.toString()
        val date = Calendar.getInstance().apply {
            time = item.date
        }
        binding.datePicker.updateDate(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        binding.filledExposedDropdown.setText(item.category)
    }

    private fun initDatePicker() {
        val currentDate = Calendar.getInstance()
        binding.datePicker.init(
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            viewModel.date?.let {
                calendar.time = it
            }
            calendar.set(
                year, monthOfYear, dayOfMonth,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
            )
            binding.timePicker.text = DateToString.convertDateToString(calendar.time)
            viewModel.date = calendar.time
        }
        binding.timePicker.setOnClickListener {
            openTimeDialog()
        }
    }

    private fun openTimeDialog() {
        val currentDate = Calendar.getInstance()
        viewModel.date?.let {
            currentDate.time = it
        }
        val timeListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth,
                    hourOfDay, minute
                )
                binding.timePicker.text = DateToString.convertDateToString(calendar.time)
                viewModel.date = calendar.time
            }

        TimePickerDialog(
            activity,
            timeListener,
            currentDate.get(Calendar.HOUR_OF_DAY),
            currentDate.get(Calendar.MINUTE),
            true
        ).show()
    }

}