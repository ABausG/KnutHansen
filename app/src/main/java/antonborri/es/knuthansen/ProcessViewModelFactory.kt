package antonborri.es.knuthansen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ProcessViewModelFactory @Inject constructor(private val processViewModel: ProcessViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProcessViewModel::class.java)) {
            return processViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}