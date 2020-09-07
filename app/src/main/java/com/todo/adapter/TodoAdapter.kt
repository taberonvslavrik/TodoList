package com.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.todo.R
import com.todo.data.Todo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_todo.*


class TodoAdapter(private val onTaskCheck: (todoId: Long, isFinished: Boolean) -> Unit) :
    RecyclerView.Adapter<TodoViewHolder>() {

    private val listDiffer = AsyncListDiffer(this,
        object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false), onTaskCheck
    )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = listDiffer.currentList.size

    override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

    fun submitList(data: List<Todo>, postUpdateAction: () -> Unit) {
        listDiffer.submitList(
            data.toMutableList(),
            if (hasListSizeChanged(data)) postUpdateAction else null
        )
    }

    private fun getItem(position: Int): Todo? = listDiffer.currentList[position]

    private fun hasListSizeChanged(newList: List<Todo>) =
        newList.size != listDiffer.currentList.size
}

class TodoViewHolder(
    override val containerView: View,
    private val onTodoCheck: (todoId: Long, isFinished: Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(todo: Todo?) {
        todo?.let { nonNullTodo ->
            tv_todo_text.text = nonNullTodo.title
            cb_todo.isChecked = nonNullTodo.completed
            containerView.setBackgroundColor(getBackgroundColor(nonNullTodo.completed))
            cb_todo.setOnClickListener {
                val todoFinished = !nonNullTodo.completed
                onTodoCheck(nonNullTodo.id, todoFinished)
                containerView.setBackgroundColor(getBackgroundColor(todoFinished))
            }
        }
    }

    private fun getBackgroundColor(isTodoCompleted: Boolean) =
        ContextCompat.getColor(
            containerView.context,
            if (isTodoCompleted) R.color.item_todo_selected else R.color.item_todo_not_selected
        )
}