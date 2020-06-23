package ru.cherryperry.amiami.presentation.base

import androidx.lifecycle.Observer

class NotNullObserver<T>(
    private val action: (T) -> Unit
) : Observer<T> {

    override fun onChanged(value: T?) {
        value?.also { action(value) }
    }
}
