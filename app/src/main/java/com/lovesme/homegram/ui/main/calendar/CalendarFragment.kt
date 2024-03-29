package com.lovesme.homegram.ui.main.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.listener.DeleteClickListener
import com.lovesme.homegram.databinding.FragmentCalendarBinding
import com.lovesme.homegram.ui.viewmodel.CalendarViewModel
import com.lovesme.homegram.util.Constants
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : Fragment(), DatePickerDialog.OnDateSetListener, DeleteClickListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: CalendarViewModel by activityViewModels()
    private val adapter = TodoRVAdapter(this)

    @Inject
    lateinit var dateFormatText: DateFormatText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.dateTv.setOnClickListener {
            val (year, month, date) = todoViewModel.getDate()
            DatePickerDialog(
                requireContext(),
                this,
                year,
                month - 1,
                date,
            ).show()
        }
        binding.writeTodoBtn.setOnClickListener {
            val date = binding.dateTv.text.toString()
            val contents = binding.todoCompositionContentTv.text.toString()
            if (contents.trim().isNotEmpty()) {
                binding.todoCompositionContentTv.text.clear()
                todoViewModel.writeTodo(date, contents)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dateTv.text = dateFormatText.getTodayText()
        binding.calendarRecycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    todoViewModel.todo.collect { todo ->
                        adapter.submitList(todo.toMutableList())
                    }
                }
                launch {
                    todoViewModel.date.collect { date ->
                        val uploadDate = arguments?.getString(Constants.PARCELABLE_DATE) ?: date
                        todoViewModel.loadTodo(uploadDate)
                        binding.dateTv.text = uploadDate
                    }
                }
                launch {
                    todoViewModel.connect.collect { connect ->
                        if (!connect) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.internet_connect_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        todoViewModel.changeDate(year, month, dayOfMonth)
    }

    override fun onClickTodoItem(key: String) {
        todoViewModel.deleteTodo(key)
    }
}