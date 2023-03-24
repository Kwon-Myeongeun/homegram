package com.lovesme.homegram.ui.main.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lovesme.homegram.databinding.FragmentCalendarBinding
import com.lovesme.homegram.ui.viewmodel.CalendarViewModel
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: CalendarViewModel by activityViewModels()
    private val adapter = TodoRVAdapter()

    @Inject
    lateinit var dateFormatText: DateFormatText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        todoViewModel.loadTodo(
            dateFormatText.convertToDateText(
                dateFormatText.getTodayYEAR(),
                dateFormatText.getTodayMONTH(),
                dateFormatText.getTodayDATE()
            )
        )
        binding.dateTv.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                this,
                dateFormatText.getTodayYEAR(),
                dateFormatText.getTodayMONTH(),
                dateFormatText.getTodayDATE(),
            ).show()
        }
        binding.writeTodoBtn.setOnClickListener {
            val date = binding.dateTv.text.toString()
            val contents = binding.todoCompositionContentTv.text.toString()
            binding.todoCompositionContentTv.text.clear()
            todoViewModel.writeTodo(date, contents)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dateTv.text = dateFormatText.getTodayText()
        binding.calendarRecycler.adapter = adapter
        todoViewModel.todo.observe(viewLifecycleOwner) { todo ->
            adapter.submitList(todo.toMutableList())
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = dateFormatText.convertToDateText(year, month, dayOfMonth)
        todoViewModel.loadTodo(date)

        binding.dateTv.text = date
    }
}