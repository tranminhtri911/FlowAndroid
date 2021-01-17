package com.example.flowmvvm.utils.extension

import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun AppCompatEditText.searchTextChanged(): StateFlow<String> {
    val query = MutableStateFlow("")
    
    doOnTextChanged { text, _, _, _ ->
        query.value = text.toString()
    }
    
    return query
}
