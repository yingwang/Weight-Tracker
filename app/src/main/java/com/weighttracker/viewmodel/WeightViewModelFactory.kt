package com.weighttracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weighttracker.data.repository.WeightRepository

class WeightViewModelFactory(
    private val repository: WeightRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeightViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
