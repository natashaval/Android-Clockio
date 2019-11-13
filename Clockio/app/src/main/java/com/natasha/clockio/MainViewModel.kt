package com.natasha.clockio

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.natasha.clockio.base.model.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.Exception
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {
    // https://medium.com/corouteam/exploring-kotlin-coroutines-and-lifecycle-architectural-components-integration-on-android-c63bb8a9156f


    val testData : LiveData<Test?> = liveData(Dispatchers.IO) {
        Log.d("MainViewModel", "TestData is trigger from livedata")
        val response = mainRepository.getTestAuto().body()
        emit(response)
    }

    var testLiveData = liveData(Dispatchers.IO) {
        try {
            val test = mainRepository.getTest()
//            testData.value = test.body()
            Log.d("MainViewModel", "TestLiveData is trigger if GET")
            emit(test)
        } catch (e : Exception) {
            Log.e("MainViewModel", "error in fetch data")
        }
    }
}