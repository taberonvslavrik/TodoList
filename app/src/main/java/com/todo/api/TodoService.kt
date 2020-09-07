package com.todo.api

import com.todo.data.Todo
import retrofit2.http.GET

interface TodoService {

    @GET("todos?userId=1")
    suspend fun getTodos(): List<Todo>
}