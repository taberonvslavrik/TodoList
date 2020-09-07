package com.todo.repository

import com.todo.api.TodoService

class TodoRepositoryImpl(private val todoService: TodoService) : TodoRepository {
    override suspend fun getTodos() = todoService.getTodos()
}