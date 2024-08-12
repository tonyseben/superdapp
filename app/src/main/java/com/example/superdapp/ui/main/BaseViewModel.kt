package com.example.superdapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface UiState
interface UiEvent
interface UiSideEffect

abstract class BaseViewModel
<State : UiState, Event : UiEvent, Effect : UiSideEffect>(initialState: State) :
    ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() = viewModelScope.launch {
        _event.collect {
            handleEvents(it)
        }
    }

    protected abstract fun handleEvents(event: Event)

    fun setEvent(event: Event) = viewModelScope.launch {
        _event.emit(event)
    }

    protected fun setState(reducer: State.() -> State) {
        val newState = state.value.reducer()
        _state.value = newState
    }

    protected suspend fun setSideEffect(effect: Effect) {
        _effect.send(effect)
    }

    protected fun applySideEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}