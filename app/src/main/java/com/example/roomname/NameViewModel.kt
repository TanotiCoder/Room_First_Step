package com.example.roomname

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NameViewModel @Inject constructor(private val repositoryName: RepositoryName) : ViewModel() {

    var name by mutableStateOf(Name(0, ""))
        private set
    val names = repositoryName.getName()

    fun addName(name: Name) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryName.addName(name)
        }
    }

}