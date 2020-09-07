package com.todo.di

import com.google.gson.GsonBuilder
import com.todo.BuildConfig
import com.todo.api.TodoService
import com.todo.repository.TodoRepository
import com.todo.repository.TodoRepositoryImpl
import com.todo.vm.TodoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<Retrofit> { provideRetrofit() }
    single<TodoService> { retrofitService<TodoService>(get()) }
}

val repositoryModule = module {
    single<TodoRepository> { TodoRepositoryImpl(get()) }
}

val viewModelsModule = module {
    viewModel { TodoViewModel(get()) }
}

private fun provideRetrofit() =
    Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

private inline fun <reified T> retrofitService(retrofit: Retrofit) = retrofit.create(T::class.java)