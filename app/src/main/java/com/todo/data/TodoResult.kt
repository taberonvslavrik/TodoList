package com.todo.data

sealed class TodoResult {
    data class Success(val todos: List<Todo>) : TodoResult()
    data class Error(val exception: Exception) : TodoResult()
}