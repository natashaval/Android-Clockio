package com.natasha.clockio.base.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.natasha.clockio.base.di.executor.AppExecutors
import com.natasha.clockio.base.model.*

//https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){

  private val TAG: String = NetworkBoundResource::class.java.simpleName

  private val result = MediatorLiveData<BaseResponse<ResultType>>()

  init {
    result.value = BaseResponse.loading(null)
    @Suppress("LeakingThis")
    val dbSource = loadFromDb()
    result.addSource(dbSource) { data ->
      result.removeSource(dbSource)
      if (shouldFetch(data)) {
        fetchFromNetwork(dbSource)
      } else {
        result.addSource(dbSource) { newData ->
          setValue(BaseResponse.success(newData))
        }
      }
    }

  }

  private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
    Log.d(TAG, "fetchFromNetwork")
    val apiResponse = createCall()
    // we re-attach dbSource as a new source, it will dispatch its latest value quickly
    result.addSource(dbSource) {newData ->
      setValue(BaseResponse.loading(newData))
    }
    result.addSource(apiResponse) { response ->
      result.removeSource(apiResponse)
      result.removeSource(dbSource)
      Log.d(TAG, "fetch from $response")
      when (response) {
        is ApiSuccessResponse -> {
          appExecutors.diskIO().execute {
            saveCallResult(processResponse(response))
            appExecutors.mainThread().execute {
              // we specially request a new live data,
              // otherwise we will get immediately last cached value,
              // which may not be updated with latest results received from network.
              result.addSource(loadFromDb()) {newData ->
                setValue(BaseResponse.success(newData))
              }
            }
          }
        }
        is ApiEmptyResponse -> {
          appExecutors.mainThread().execute {
            // reload from disk whatever we had
            result.addSource(loadFromDb()) { newData ->
              setValue(BaseResponse.success(newData))
            }
          }
        }
        is ApiFailedResponse -> {
          onFetchFailed()
          Log.d(TAG, "fetch ApiFailedResponse ${response.errorMessage}")
          result.addSource(dbSource) {newData ->
            setValue(BaseResponse.error(response.errorMessage, newData))
          }
        }
      }
    }


  }

  @MainThread
  private fun setValue(newValue: BaseResponse<ResultType>) {
    if (result.value != newValue) {
      result.value = newValue
    }
  }

  @MainThread
  protected abstract fun loadFromDb(): LiveData<ResultType>

  @MainThread
  protected abstract fun shouldFetch(data: ResultType?): Boolean

  @MainThread
  protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

  @WorkerThread
  protected abstract fun saveCallResult(item: RequestType)

  @WorkerThread
  protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

  protected open fun onFetchFailed() {}

  fun asLiveData() = result as LiveData<BaseResponse<ResultType>>
}