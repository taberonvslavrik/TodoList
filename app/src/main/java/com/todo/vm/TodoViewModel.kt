package com.todo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.todo.data.TodoResult
import com.todo.data.Todo
import com.todo.util.randInt
import com.todo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import java.util.*

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    private val todoList = LinkedList<Todo>()
    private val todoMutableLiveData = MutableLiveData<TodoResult>()

    val todoLiveData: LiveData<TodoResult> = liveData(Dispatchers.IO) {
        try {
            emitSource(todoMutableLiveData)
            todoList.addAll(todoRepository.getTodos())
            emitTodoList()
        } catch (e: Exception) {
            emit(TodoResult.Error(e))
        }
    }

    fun addTodo(todoText: String) {
        todoList.addFirst(Todo(randInt(), todoText, false))
        emitTodoList()
    }

    fun finishTodo(todoId: Long, isFinished: Boolean){
        todoList.find { it.id == todoId }?.completed = isFinished
        emitTodoList()
    }

    private fun emitTodoList(){
        todoMutableLiveData.postValue(TodoResult.Success(todoList))
    }
}