package com.aydemir.glancetestapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aydemir.glancetestapp.data.StandingsRepositoryImp
import com.aydemir.glancetestapp.model.Resource
import kotlinx.coroutines.flow.last

class GetDataWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val repository = StandingsRepositoryImp.get(appContext)
        val result = repository.getStandings()
        return when (result.last()) {
            is Resource.Success -> {
                Result.Success.success()
            }

            else -> {
                Result.Failure.failure()
            }
        }
    }
}