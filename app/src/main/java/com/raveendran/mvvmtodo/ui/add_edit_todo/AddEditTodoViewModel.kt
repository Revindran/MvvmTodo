package com.raveendran.mvvmtodo.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raveendran.mvvmtodo.data.Todo
import com.raveendran.mvvmtodo.data.TodoRepository
import com.raveendran.mvvmtodo.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvents = Channel<UiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId!!)?.let {
                    title = it.title
                    description = it.description ?: ""
                    this@AddEditTodoViewModel.todo = it
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChanged -> title = event.title
            is AddEditTodoEvent.OnDescriptionChanged -> description = event.description
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvents(UiEvents.ShowSnackBar("The title cannot be empty"))
                        return@launch
                    }
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isChecked = todo?.isChecked ?: false,
                            id = todo?.id
                        )
                    )
                    sendUiEvents(UiEvents.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvents(events: UiEvents) {
        viewModelScope.launch {
            _uiEvents.send(events)
        }
    }
}