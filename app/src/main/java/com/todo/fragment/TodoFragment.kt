package com.todo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todo.R
import com.todo.adapter.TodoAdapter
import com.todo.data.Todo
import com.todo.data.TodoResult
import com.todo.util.toast
import com.todo.vm.TodoViewModel
import kotlinx.android.synthetic.main.fragment_todo.*
import org.koin.android.viewmodel.ext.android.viewModel

class TodoFragment : Fragment() {

    private val todoViewModel by viewModel<TodoViewModel>()

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_todo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
        setUpSubscription()
    }

    private fun setUpViews() {
        setUpRecyclerView()
        setUpClickListeners()
    }

    private fun setUpRecyclerView() {
        rv_todos.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TodoAdapter { todoId, isFinished -> todoViewModel.finishTodo(todoId, isFinished) }.apply {
                setHasStableIds(true)
                todoAdapter = this
            }
            itemAnimator = null
        }
    }


    private fun setUpClickListeners() {
        btn_add.setOnClickListener { addNewTodo() }
    }

    private fun setUpSubscription() {
        todoViewModel.todoLiveData.observe(this, Observer {
            when (it) {
                is TodoResult.Success -> handleTodoList(it.todos)
                is TodoResult.Error -> toast("Error happened during loading")
            }
        })
    }

    private fun handleTodoList(todoList: List<Todo>){
        todoAdapter.submitList(todoList) { scrollListToTheTop() }
    }

    private fun addNewTodo() {
        val inputText = et_todo_input.text?.trim().toString()
        if (inputText.isNotBlank() && inputText.isNotEmpty()) {
            todoViewModel.addTodo(inputText)
            et_todo_input.text?.clear()
        }
    }

    private fun scrollListToTheTop() {
        rv_todos.smoothScrollToPosition(0)
    }
}