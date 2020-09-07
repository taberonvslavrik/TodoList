package com.todo.repository

import com.todo.data.Todo

interface TodoRepository {
    suspend fun getTodos(): List<Todo>
}